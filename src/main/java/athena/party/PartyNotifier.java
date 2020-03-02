package athena.party;

import athena.context.DefaultAthenaContext;
import athena.party.resource.notification.regular.PartyNotification;
import athena.party.service.PartyService;
import athena.party.xmpp.annotation.PartyEvent;
import athena.party.xmpp.event.invite.PartyInviteEvent;
import athena.party.xmpp.event.invite.PartyPingEvent;
import athena.util.event.EventFactory;
import athena.util.request.Requests;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Handles XMPP notifications.
 */
final class PartyNotifier implements StanzaListener {

    /**
     * Create our event factory for party notifications.
     */
    private final EventFactory eventFactory = EventFactory.createAnnotatedFactory(PartyEvent.class);

    private final DefaultAthenaContext context;
    private final PartyService service;
    private final Parties parties;
    private final Gson gson;

    PartyNotifier(DefaultAthenaContext context) {
        this.context = context;
        this.service = context.partyService();
        this.parties = context.party();
        this.gson = context.gson();

        context.connectionManager().connection().addAsyncStanzaListener(this, StanzaTypeFilter.MESSAGE);
    }

    void registerEventListener(Object listener) {
        eventFactory.registerEventListener(listener);
    }

    void unregisterEventListener(Object listener) {
        eventFactory.unregisterEventListener(listener);
    }

    @Override
    public void processStanza(Stanza packet) {
        final var message = (Message) packet;
        // adapt the message to a JSON object.
        final var object = gson.fromJson(message.getBody(), JsonObject.class);
        if (object.has("interactions")) {
            // we have an interaction type.
            // TODO
        } else {
            // we have a regular message type, retrieve the notification.
            final var notification = PartyNotification.of(object.getAsJsonPrimitive("type").getAsString());
            // log if we have an unknown type.
            if (notification == PartyNotification.UNKNOWN) {
                System.err.println("Received unknown notification party type, please report this.");
                System.err.println(object.toString());
                return;
            }
            handleNotification(notification, object);
        }

    }

    private void handleNotification(PartyNotification notification, JsonObject object) {
        if (notification == PartyNotification.PING) {
            // adapt to the event.
            final var event = gson.fromJson(object, PartyPingEvent.class);

            // handle our ping notification.
            // we need to grab the party.
            final var call = service.getUserParties(context.localAccountId(), event.fromAccountId());
            final var response = Requests.executeCall(call);
            // TODO: Since we have a list of parties, don't grab the first but filter?
            // TODO: Not sure if needed
            if (response.size() > 0) event.party(response.get(0));
            eventFactory.invoke(PartyEvent.class, event);
        } else if (notification == PartyNotification.INITIAL_INVITE) {
            // adapt to the event
            final var event = gson.fromJson(object, PartyInviteEvent.class);
            // grab the party information
            final var call = service.getParty(event.partyId());
            final var party = Requests.executeCall(call);
            event.party(party);
            // fire event
            eventFactory.invoke(PartyEvent.class, event);
        }

    }

}
