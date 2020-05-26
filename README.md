# Athena2
Athena is a java library for interacting with the Fortnite/Epic Games API.

# Features
Check out all the features of Athena [here](https://github.com/Vrekt/Athena2/wiki/Features).

This list is probably outdated but will give you a good idea of capabilities!

# Examples

### Basic usage with only the API (Disables XMPP)
```java
final var athena = Athena.athenaWithoutXMPP("username", "password");
```

### Full usage with XMPP.
```java
final var athena = Athena.athenaWithXMPP("username", "password");
```

These are only default configurations, if you want more customization then check out [this](https://github.com/Vrekt/Athena2/wiki/Authenticating).

You can find party example code [here](https://github.com/Vrekt/Athena2/wiki/Parties).

Check out other examples [here](https://github.com/Vrekt/Athena2/wiki/Examples)

# Notes
It is recommended that if you are going to create a bot that will be constantly re-logging from testing and other events then you should use Device Auth. After awhile, Epic will prevent you from logging in and you will have to complete a captcha. To get around this you can use Device Auth. The documentation can be found [here](https://github.com/Vrekt/Athena2/wiki/Authenticating)

# Documentation
The wiki will cover some stuff, but not all. Contact me on discord `vrekt#4387` for further help or questions.

# TODO
- Make events system better
- Finish parties
- General cleanup and QOL
- GraphQL stuff
- MCP
- Fix shop

# Credits
- [Mix](https://github.com/MixV2/EpicResearch)
- iXyles
- [Terbau](https://github.com/Terbau/fortnitepy)
- Bad_Mate | Pat
- [Roberto](https://github.com/RobertoGraham)
- [Kysune](https://github.com/SzymonLisowiec/node-epicgames-client)
