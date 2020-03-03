# Athena2
Athena is a java library for interacting with the Fortnite/Epic Games API.

# Features

### Configuration
When building new instances of `Athena` there are many configuration options available:
- Device Auth support
- Kill other sessions
- Handle shutdown/cleanup automatically
- Accept EULA if needed.
- 2FA support
- enabling XMPP.
- Loading XMPP roster (configurable for large-friend accounts)
- Reconnecting automatically on XMPP error.
- Setting platform and application type
- Debug the XMPP connection.

### Account
- Lookup accounts by display name and ID.
- Listen for friend requests coming from the account.
- Add friend listeners attached to an account.
- Basic functions within the `Account` object such as (remove, block, unblock, etc)
- External Auths support
- Device Auth support
- Addresses support
- Changing password

### EULA
- `EulatrackingPublicService` interface for EULA requests.

### Events
- Get all Fortnite events
- Download event data for account
- Event leaderboards

### Shop
- Retrieve the weekly/daily shop.
- Retrieve other storefronts.

### Friends
- Get all friends
- Get the blocklist
- Adding, removing, blocking, unblocking, etc
- Support for setting alias and notes for friends
- Support for indiviual friend profiles (includes their alias, note, mutual, etc)
- Change basic friend settings

### Presence
- Retrieve last-online times for friends.
- Settings
- Broadcast the account is playing Fortnite
- Listen for incoming presences
- Filter presences
- Annotation event system for receiving presences.

### Stats V2
- Look up stats for an account
- Able to be filtered by input and playlist.
- Query specific stats
- Leaderboards

### Chat
- Receive messages to consumers or listeners.
- Send messages
- Reply to messages

# Documentation
Refer to the wiki.

# TODO
- Make events system better
- Finish parties
- General cleanup and QOL
- GraphQL stuff
- MCP
- Fix shop
