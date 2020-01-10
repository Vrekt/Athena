package athena.xmpp.listener;

/**
 * Basic interface for a connection listener.
 */
public interface XMPPConnectionListener {

    /**
     * Invoked when the XMPP connection is made and authenticated.
     */
    void connected();

}
