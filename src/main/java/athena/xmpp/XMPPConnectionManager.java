package athena.xmpp;

import athena.exception.EpicGamesErrorException;
import athena.types.Platform;
import athena.xmpp.listener.XMPPConnectionListener;
import com.google.common.flogger.FluentLogger;
import org.apache.commons.lang3.RandomStringUtils;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingManager;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages the XMPP connection.
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
     * COW list for listeners.
     */
    private final CopyOnWriteArrayList<XMPPConnectionListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Connection and ping manager.
     */
    private XMPPTCPConnection connection;
    private PingManager pingManager;

    /**
     * account ID and access token
     */
    private String accountId, accessToken;

    public XMPPConnectionManager(boolean loadRoster, boolean reconnectOnError, boolean debug, Platform platform, String application) {
        this.loadRoster = loadRoster;
        this.reconnectOnError = reconnectOnError;
        this.debug = debug;
        this.platform = platform;
        this.application = application;
    }

    /**
     * Add a connection listener.
     *
     * @param xmppConnectionListener the connection listener.
     */
    public void addConnectionListener(XMPPConnectionListener xmppConnectionListener) {
        listeners.add(xmppConnectionListener);
    }

    /**
     * Remove a connection listener.
     *
     * @param xmppConnectionListener the connection listener.
     */
    public void removeConnectionListener(XMPPConnectionListener xmppConnectionListener) {
        listeners.remove(xmppConnectionListener);
    }

    /**
     * Connect to the XMPP service.
     *
     * @param accountId   the account ID.
     * @param accessToken the access token.
     * @throws EpicGamesErrorException if any of these exceptions occurred, {@link IOException}, {@link SmackException}, {@link XMPPException}, {@link InterruptedException}
     */
    public void connect(String accountId, String accessToken) throws EpicGamesErrorException {
        // store for later use if needed.
        this.accountId = accountId;
        this.accessToken = accessToken;
        final var resource = "V2:" + application + ":" + platform.primaryName() + "::" + RandomStringUtils.random(32, 0, 0, true, true, HEX_UUID);

        if (!loadRoster) {
            Roster.setRosterLoadedAtLoginDefault(false);
            Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
        }

        if (debug) SmackConfiguration.DEBUG = true;

        try {
            connection = new XMPPTCPConnection(
                    XMPPTCPConnectionConfiguration.builder()
                            .setXmppDomain(XMPP_DOMAIN)
                            .setHost(XMPP_HOST)
                            .setPort(XMPP_PORT)
                            .setUsernameAndPassword(accountId, accessToken)
                            .setConnectTimeout(60000)
                            .setResource(resource)
                            .build());

            pingManager = PingManager.getInstanceFor(connection);
            pingManager.setPingInterval(60);

            connection.addConnectionListener(this);
            connection.setReplyTimeout(60000);
            connection.connect().login();

        } catch (final IOException | SmackException | XMPPException | InterruptedException exception) {
            throw EpicGamesErrorException.createFromOther(exception);
        }
    }

    /**
     * Disconnect from the XMPP service.
     */
    public void disconnect() {
        pingManager.setPingInterval(-1);
        if (connection != null) connection.disconnect();
    }

    /**
     * Disconnects and clears the list of listeners.
     */
    public void close() {
        disconnect();
        listeners.clear();

        accountId = null;
        accessToken = null;
    }

    /**
     * @return the {@link XMPPTCPConnection}
     */
    public XMPPTCPConnection connection() {
        return connection;
    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        // TODO: Not needed.
    }

    @Override
    public void connectionClosed() {
        // TODO: Not needed.
    }

    @Override
    public void connected(XMPPConnection connection) {
        listeners.forEach(XMPPConnectionListener::connected);
    }

    @Override
    public void connectionClosedOnError(Exception exception) {
        LOGGER.atSevere().withCause(exception).log("Connection closed on error!");
        if (!reconnectOnError) {
            LOGGER.atInfo().log("Connection will not be re-established because reconnectOnError is not enabled.");
        } else {
            disconnect();
            connect(accountId, accessToken);
        }
    }
}
