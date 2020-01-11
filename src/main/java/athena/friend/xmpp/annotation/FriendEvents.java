package athena.friend.xmpp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for friend events.
 * Put the annotation above a method and then register it with
 * {@code athena.friend().registerEventListener}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FriendEvents {
}
