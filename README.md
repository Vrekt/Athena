# Athena2
Athena is a java library for interacting with the Fortnite/Epic Games API.

# Features
Check out all the features of Athena [here](https://github.com/Vrekt/Athena2/wiki/Features).

This list is probably outdated but will give you a good idea of capabilities!

# Examples

### Basic usage with only the API (Disables XMPP)
```java
            final var athena = new Athena.Builder()
                    .email(myEmail)
                    .password(myPassword)
                    .killOtherSessions()
                    .handleShutdown()
                    .refreshAutomatically()
                    .build();
```

### Full usage with XMPP.
```java
            final var athena = new Athena.Builder()
                    .email(myEmail)
                    .password(myPassword)
                    .killOtherSessions()
                    .handleShutdown()
                    .refreshAutomatically()
                    .enableXmpp()
                    .platform(Platform.WIN)
                    .app("Fortnite")
                    .build()
```

You can find party example code [here](https://github.com/Vrekt/Athena2/wiki/Parties).

Check out other examples [here](https://github.com/Vrekt/Athena2/wiki/Examples)

# Documentation
The wiki will cover some stuff, but not all. Contact me on discord `vrekt#4387` for further help or questions.

# TODO
- Make events system better
- Finish parties
- General cleanup and QOL
- GraphQL stuff
- MCP
- Fix shop
