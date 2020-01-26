package athena.account.resource.action;

import athena.friend.resource.summary.Profile;
import athena.util.other.EmptyAction;

import java.util.function.Consumer;

/**
 * An interface that manages accepting friends.
 */
public interface FriendAcceptor {

    /**
     * Wait {@code seconds} for the friend request before timing out.
     *
     * @param seconds the time to wait.
     */
    FriendAcceptor waitUntil(int seconds);

    /**
     * After the wait time from {@code waitUntil} is reached without a friend request the provided {@code run} is called.
     *
     * @param run the action to run.
     */
    FriendAcceptor onExpired(EmptyAction run);

    /**
     * Once the friend request is accepted the {@code profileConsumer} will be accepted.
     *
     * @param profileConsumer the consumer
     */
    FriendAcceptor onAccepted(Consumer<Profile> profileConsumer);

}
