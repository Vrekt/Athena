package athena.party.resource.connection;

import athena.types.Platform;
import athena.util.json.post.annotation.PostDeserialize;
import com.google.gson.annotations.SerializedName;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

import java.time.Instant;

/**
 * Represents the connection of a party member.
 */
public final class Connection {

    /**
     * The JID of this connection.
     */
    private String id;

    /**
     * The JID of this connection.
     */
    private Jid jid;

    /**
     * When they connected.
     */
    @SerializedName("connected_at")
    private Instant connectedAt;
    /**
     * When the connection was updated.
     */
    @SerializedName("updated_at")
    private Instant updatedAt;

    /**
     * if {@code true} leadership cannot be given to the person.
     */
    @SerializedName("yield_leadership")
    private boolean yieldLeadership;

    /**
     * The meta of this connection.
     */
    private ConnectionMeta meta;

    @PostDeserialize
    private void postDeserialize() {
        jid = JidCreate.fromOrThrowUnchecked(id);
    }

    /**
     * @return the ID of this connection.
     */
    public String id() {
        return id;
    }

    /**
     * @return the JID of this connection.
     */
    public Jid jid() {
        return jid;
    }

    /**
     * @return when they connected.
     */
    public Instant connectedAt() {
        return connectedAt;
    }

    /**
     * @return when the connection was updated.
     */
    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * @return {@code true} if leadership shouldn't be accepted.
     */
    public boolean yieldLeadership() {
        return yieldLeadership;
    }

    /**
     * @return the platform of this connection.
     */
    public Platform platform() {
        return meta.connectionPlatform;
    }

    /**
     * @return the type of connection, usually "GAME"
     */
    public String type() {
        return meta.connectionType;
    }

    /**
     * Represents meta inside connection.
     */
    private static final class ConnectionMeta {

        /**
         * The platform of this connection
         */
        @SerializedName("urn:epic:conn:platform_s")
        private Platform connectionPlatform;
        /**
         * The connection type - "game"/???
         */
        @SerializedName("urn:epic:conn:type_s")
        private String connectionType;
    }

}
