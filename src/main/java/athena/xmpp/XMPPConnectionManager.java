package athena.xmpp;

import athena.exception.EpicGamesErrorException;
import athena.types.Platform;
import com.google.common.flogger.FluentLogger;
import org.apache.commons.lang3.RandomStringUtils;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Manages the XMPP connection.
 *
 * @author Vrekt, RobertoGraham
 */
public final class XMPPConnectionManager implements ConnectionListener {

    /**
     * Logger for this class.
     */
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    /**
     * XMPP Connection values.
     */
    public static final String XMPP_DOMAIN = "prod.ol.epicgames.com";
    public static final String XMPP_HOST = "xmpp-service-prod.ol.epicgames.com";
    public static final int XMPP_PORT = 5222;

    /**
     * Array of HEX characters to generate a random UUID.
     */
    public static final char[] HEX_UUID =
            new char[]{'A', 'B', 'C', 'D', 'E', 'F', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * The application, ex: "launcher" or "Fortnite"
     */
    private final String application;
    /**
     * The platform
     */
    private final Platform platform;
    /**
     * Configuration
     */
    private final boolean loadRoster, reconnectOnError, debug;

    /**
     * Maps and lists for various different listeners.
     * Mainly just provides convenience but is ugly.
     */
    private final ConcurrentHashMap<Boolean, List<Consumer<XMPPTCPConnection>>> connectionListeners = new ConcurrentHashMap<>();
    private final CopyOnWriteArrayList<Runnable> connectionClosedListeners = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<Consumer<Exception>> connectionErrorListeners = new CopyOnWriteArrayList<>();

    /**
     * Connection and ping manager.
     */
    private XMPPTCPConnection connection;
    private PingManager pingManager;

    public XMPPConnectionManager(boolean loadRoster, boolean reconnectOnError, boolean debug, Platform platform, String application) {
        this.loadRoster = loadRoster;
        this.reconnectOnError = reconnectOnError;
        this.debug = debug;
        this.platform = platform;
        this.application = application;
    }

    /**
     * Connect to the XMPP service.
     *
     * @param accountId   the account ID.
     * @param accessToken the access token.
     * @throws EpicGamesErrorException if any of these exceptions occurred, {@link IOException}, {@link SmackException}, {@link XMPPException}, {@link InterruptedException}
     */
    public void connect(String accountId, String accessToken) throws EpicGamesErrorException {
        final var resource = "V2:" + application + ":" + platform.primaryName() + "::" + RandomStringUtils.random(32, HEX_UUID);

        if (!loadRoster) {
            Roster.setRosterLoadedAtLoginDefault(false);
            Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        }
        if (debug) SmackConfiguration.DEBUG = true;

        try {
            connection = new XMPPTCPConnection(
                    XMPPTCPConnectionConfiguration.builder()
                            .setXmppDomain(XMPP_DOMAIN)
                            .setPort(XMPP_PORT)
                            .setHost(XMPP_HOST)
                            .setConnectTimeout(60000)
                            .setResource(resource)
                            .build());

            pingManager = PingManager.getInstanceFor(connection);
            pingManager.setPingInterval(60);

            connection.addConnectionListener(this);
            connection.setReplyTimeout(60000);
            connection.connect().login(accountId, accessToken);

            if (reconnectOnError) {
                final var reconnection = ReconnectionManager.getInstanceFor(connection);
                reconnection.setFixedDelay(5);
                reconnection.enableAutomaticReconnection();
            }

            // TODO: Debug purposes.
            connection.addAsyncStanzaListener(stanza -> {
                final var msg = (Message) stanza;
                System.err.println(msg.getBody());
            }, new StanzaTypeFilter(Message.class));

        } catch (final IOException | SmackException | XMPPException | InterruptedException exception) {
            throw EpicGamesErrorException.createFromOther(exception);
        }
    }

    /**
     * Disconnect from the XMPP service.
     */
    public void disconnect() {
        if (pingManager != null) pingManager.setPingInterval(-1);
        if (connection != null) connection.disconnect();
    }

    /**
     * Disconnects and clears the list of listeners.
     */
    public void close() {
        disconnect();

        connectionListeners.clear();
        connectionClosedListeners.clear();
        connectionErrorListeners.clear();
    }

    /**
     * Adds a simple connection listener.
     * Key: {@code Boolean.FALSE}
     *
     * @param connectionConsumer the consumer
     */
    public void onConnected(Consumer<XMPPTCPConnection> connectionConsumer) {
        connectionListeners.putIfAbsent(Boolean.FALSE, new ArrayList<>());
        connectionListeners.get(Boolean.FALSE).add(connectionConsumer);
    }

    /**
     * Adds a simple authenticated listener.
     * This method will not include the value {@code resumed} (which indicates if the XMPP connection was resumed)
     * Key: {@code Boolean.TRUE}
     *
     * @param connectionConsumer the consumer
     */
    public void onAuthenticated(Consumer<XMPPTCPConnection> connectionConsumer) {
        connectionListeners.putIfAbsent(Boolean.TRUE, new ArrayList<>());
        connectionListeners.get(Boolean.TRUE).add(connectionConsumer);
    }

    /**
     * Adds a simple listener for when the connection is closed.
     *
     * @param runnable the action
     */
    public void onClosed(Runnable runnable) {
        connectionClosedListeners.add(runnable);
    }

    /**
     * Adds a simple listener for when the connection is closed due to an error.
     *
     * @param exceptionConsumer the consumer
     */
    public void onError(Consumer<Exception> exceptionConsumer) {
        connectionErrorListeners.add(exceptionConsumer);
    }

    /**
     * @return the {@link XMPPTCPConnection}
     */
    public XMPPTCPConnection connection() {
        return connection;
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        if (connectionListeners.containsKey(Boolean.TRUE)) connectionListeners.get(Boolean.TRUE).forEach(connectionConsumer -> connectionConsumer.accept((XMPPTCPConnection) connection));
    }

    @Override
    public void connectionClosed() {
        connectionClosedListeners.forEach(Runnable::run);
    }

    @Override
    public void connected(XMPPConnection connection) {
        if (connectionListeners.containsKey(Boolean.FALSE)) connectionListeners.get(Boolean.TRUE).forEach(connectionConsumer -> connectionConsumer.accept((XMPPTCPConnection) connection));
    }

    @Override
    public void connectionClosedOnError(Exception exception) {
        LOGGER.atSevere().withCause(exception).log("Connection closed on error!");
        connectionErrorListeners.forEach(exceptionConsumer -> exceptionConsumer.accept(exception));
    }
}
