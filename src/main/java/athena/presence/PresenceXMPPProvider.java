package athena.presence;

import athena.context.DefaultAthenaContext;
import athena.presence.resource.FortnitePresence;
import athena.presence.resource.annotation.PresenceEvent;
import athena.presence.resource.filter.PresenceFilter;
import athena.presence.resource.listener.FortnitePresenceListener;
import athena.util.event.EventFactory;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.PresenceTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Provides XMPP access to {@link Presences}
 */
public final class PresenceXMPPProvider implements StanzaListener {

    /**
     * The event factory for presence events
     */
    private final EventFactory factory;
    /**
     * COW list of listeners.
     */
    private final CopyOnWriteArrayList<FortnitePresenceListener> listeners = new CopyOnWriteArrayList<>();
    /**
     * COW list of filters.
     */
    private final CopyOnWriteArrayList<PresenceFilter> filters = new CopyOnWriteArrayList<>();

    /**
     * The context
     */
    private DefaultAthenaContext context;

    PresenceXMPPProvider(DefaultAthenaContext context) {
        this.context = context;
        this.factory = EventFactory.create(PresenceEvent.class, 1);
        context.connectionManager().connection().addAsyncStanzaListener(this, PresenceTypeFilter.AVAILABLE);
    }

    /**
     * Initialize this provider from another one.
     *
     * @param other the other
     */
    PresenceXMPPProvider(DefaultAthenaContext context, PresenceXMPPProvider other) {
        this.context = context;
        this.listeners.addAll(other.listeners);
        this.filters.addAll(other.filters);
        this.factory = EventFactory.create(other.factory);
        context.connectionManager().connection().addAsyncStanzaListener(this, PresenceTypeFilter.AVAILABLE);
    }

    @Override
    public void processStanza(Stanza packet) {
        final var presence = (Presence) packet;
        if (presence.getStatus() == null) return;
        final var accountId = presence.getFrom().getLocalpartOrNull().asUnescapedString();
        final var fortnitePresence = context.gson().fromJson(presence.getStatus(), FortnitePresence.class);
        fortnitePresence.setFrom(presence.getFrom());

        factory.invoke(fortnitePresence);
        listeners.forEach(listener -> listener.presenceReceived(fortnitePresence));
        filters.stream().filter(filter -> filter.active() && filter.ready() && filter.isRelevant(accountId)).forEach(filter -> filter.consume(fortnitePresence));
    }

    /**
     * Add a filter
     *
     * @param filter the filter
     */
    void useFilter(PresenceFilter filter) {
        filters.add(filter);
    }

    /**
     * Remove a filter.
     *
     * @param filter the filter
     */
    void removeFilter(PresenceFilter filter) {
        filters.remove(filter);
    }

    /**
     * Register a presence listener
     *
     * @param listener the listener
     */
    void registerPresenceListener(FortnitePresenceListener listener) {
        listeners.add(listener);
    }

    /**
     * Register a presence listener.
     *
     * @param listener the listener.
     */
    void unregisterPresenceListener(FortnitePresenceListener listener) {
        listeners.remove(listener);
    }

    /**
     * Register an event listener.
     *
     * @param type the class/type to register.
     */
    void registerEventListener(Object type) {
        factory.registerEventListener(type);
    }

    /**
     * Unregister an event listener.
     *
     * @param type the class/type to register.
     */
    void unregisterEventListener(Object type) {
        factory.unregisterEventListener(type);
    }

    /**
     * Removes the stanza listener.
     */
    void removeStanzaListener() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);
    }

    /**
     * Close this provider.
     */
    void close() {
        removeStanzaListener();
        factory.dispose();
        listeners.clear();
    }

    /**
     * Clean.
     */
    void clean() {
        removeStanzaListener();
    }

}
