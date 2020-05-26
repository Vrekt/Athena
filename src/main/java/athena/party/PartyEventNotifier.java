package athena.party;

import athena.context.DefaultAthenaContext;
import athena.party.resource.notification.regular.PartyNotification;
import athena.party.service.PartyService;
import athena.party.xmpp.annotation.PartyEvent;
import athena.party.xmpp.event.invite.PartyInviteEvent;
import athena.party.xmpp.event.invite.PartyPingEvent;
import athena.party.xmpp.event.member.*;
import athena.party.xmpp.event.party.PartyUpdatedEvent;
import athena.util.event.EventFactory;
import athena.util.request.Requests;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Handles XMPP notifications.
 */
final class PartyEventNotifier implements StanzaListener {

    /**
     * Create our event factory for party notifications.
     */
    private final EventFactory eventFactory = EventFactory.createAnnotatedFactory(PartyEvent.class);

    private final DefaultAthenaContext context;
    private final PartyService service;
    private final Parties parties;
    private final Gson gson;

    PartyEventNotifier(DefaultAthenaContext context, Parties parties) {
        this.context = context;
        this.parties = parties;
        this.service = context.partyService();
        this.gson = context.gson();

        context.connectionManager().connection().addAsyncStanzaListener(this, MessageTypeFilter.NORMAL);
    }

    /**
     * Register an event listener
     *
     * @param listener the event listener
     */
    void registerEventListener(Object listener) {
        eventFactory.registerEventListener(listener);
    }

    /**
     * Un-register an event listener
     *
     * @param listener the event listener
     */
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

    /**
     * Handle the party notification
     *
     * @param notification the notification
     * @param object       the json payload
     */
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
        } else if (notification == PartyNotification.MEMBER_JOINED) {
            final var event = gson.fromJson(object, PartyMemberJoinedEvent.class);
            // update our party first.
            parties.updatePartyInformation();
            parties.refreshSquadAssignments();
            event.party(parties.party());
            // fire event now
            eventFactory.invoke(PartyEvent.class, event);
        } else if (notification == PartyNotification.MEMBER_LEFT) {
            final var event = gson.fromJson(object, PartyMemberLeftEvent.class);
            // update our party first.
            parties.updatePartyInformation();
            parties.refreshSquadAssignments();
            event.party(parties.party());
            // fire event now
            eventFactory.invoke(PartyEvent.class, event);
        } else if (notification == PartyNotification.MEMBER_STATE_UPDATED) {
            final var event = gson.fromJson(object, PartyMemberUpdatedEvent.class);
            if (event.accountId().equals(context.localAccountId())) return; // TODO: Change this behavior?
            // update our member
            final var member = parties.updateMember(event.accountId(), event.updated());
            event.member(member);
            event.party(parties.party());
            // fire event now
            eventFactory.invoke(PartyEvent.class, event);
        } else if (notification == PartyNotification.MEMBER_NEW_CAPTAIN) {
            final var event = gson.fromJson(object, PartyMemberNewCaptainEvent.class);
            // update our member
            final var member = parties.updateMember(event.accountId(), event.updated());
            // update who the captain is
            parties.updateCaptain(member);
            // set
            event.member(member);
            event.party(parties.party());
            // fire event now
            eventFactory.invoke(PartyEvent.class, event);
        } else if (notification == PartyNotification.MEMBER_KICKED) {
            final var event = gson.fromJson(object, PartyMemberKickedEvent.class);
            // update our party first.
            parties.updatePartyInformation();
            parties.refreshSquadAssignments();
            event.party(parties.party());
            // fire event now
            eventFactory.invoke(PartyEvent.class, event);
        } else if (notification == PartyNotification.MEMBER_DISCONNECTED) {
            final var event = gson.fromJson(object, PartyMemberDisconnectedEvent.class);
            // update our party first.
            // TODO: We may not have to update here.
            parties.updatePartyInformation();
            event.party(parties.party());
            // fire event now
            eventFactory.invoke(PartyEvent.class, event);
        } else if (notification == PartyNotification.MEMBER_EXPIRED) {
            final var event = gson.fromJson(object, PartyMemberExpiredEvent.class);
            // update our party first.
            parties.updatePartyInformation();
            parties.refreshSquadAssignments();
            event.party(parties.party());
            // fire event now
            eventFactory.invoke(PartyEvent.class, event);
        } else if (notification == PartyNotification.PARTY_UPDATED) {
            final var event = gson.fromJson(object, PartyUpdatedEvent.class);
            // update the party meta
            parties.updateMetaFromEvent(event.updated());
            event.party(parties.party());
            // fire event now
            eventFactory.invoke(PartyEvent.class, event);
        } else if (notification == PartyNotification.MEMBER_REQUIRE_CONFIRMATION) {
            final var event = gson.fromJson(object, PartyMemberRequireConfirmationEvent.class);
            // update party
            event.party(parties.party());
            // fire event now
            eventFactory.invoke(PartyEvent.class, event);
        }

    }

    /**
     * Invoked after refreshing to re-add the stanza listener.
     *
     * @param context the new context.
     */
    void afterRefresh(DefaultAthenaContext context) {
        context
                .connectionManager()
                .connection()
                .addAsyncStanzaListener(this, StanzaTypeFilter.MESSAGE);
    }

    /**
     * Invoked before a refresh to remove the stanza listener.
     */
    void beforeRefresh() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);
    }

    /**
     * Invoked to shutdown this provider.
     */
    void shutdown() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);
        eventFactory.dispose();
    }

}
