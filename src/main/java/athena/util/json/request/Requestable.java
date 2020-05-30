package athena.util.json.request;

import athena.Athena;
import athena.account.Accounts;
import athena.account.resource.Account;
import athena.account.service.AccountPublicService;
import athena.chat.FriendChat;
import athena.friend.Friends;
import athena.friend.service.FriendsPublicService;
import athena.party.Parties;
import athena.party.service.PartyService;
import athena.presence.service.PresencePublicService;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to request certain resources/services from the athena instance
 */
public final class Requestable implements TypeAdapterFactory {

    /**
     * The athena instance
     */
    private final Athena athena;

    /**
     * The local account
     */
    private Account localAccount;

    /**
     * Stores every field annotated with {@link Request}
     */
    private final Map<Class<?>, List<Field>> fields = new HashMap<>();

    /**
     * Requestable objects.
     */
    private final Map<Class<?>, Object> requestables = new HashMap<>();

    /**
     * Initialize with all of the types
     *
     * @param athena the athena instance
     * @param types  the types
     * @return a new {@link Requestable}
     */
    public static Requestable allOf(Athena athena, Class<?>... types) {
        return new Requestable(athena, types);
    }

    /**
     * Initialize - will take all fields annotated and add them to a list
     *
     * @param athena the athena instance
     * @param types  the class types.
     */
    private Requestable(Athena athena, Class<?>... types) {
        this.athena = athena;
        for (var clazz : types) {
            final var list = new ArrayList<Field>();
            for (var field : clazz.getDeclaredFields()) {
                final var request = field.getAnnotation(Request.class);
                if (request != null) {
                    field.setAccessible(true);
                    list.add(field);
                }
            }
            if (!list.isEmpty()) fields.put(clazz, list);
        }
    }

    /**
     * Register all objects that can be requested through the annotation {@link Request}
     */
    public void registerRequestables() {
        requestables.put(Accounts.class, athena.account());
        requestables.put(Account.class, athena.localAccount());
        requestables.put(AccountPublicService.class, athena.accountPublicService());
        requestables.put(FriendsPublicService.class, athena.friendsPublicService());
        requestables.put(PresencePublicService.class, athena.presencePublicService());
        requestables.put(PartyService.class, athena.partyService());
        requestables.put(Parties.class, athena.party());
        requestables.put(Friends.class, athena.friend());
        requestables.put(FriendChat.class, athena.chat());
        this.localAccount = athena.localAccount();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        // since this class isn't registered yet.
        if (!fields.containsKey(type.getRawType())) return null;
        // we have friend type
        // so it goes to deserialize
        // check fields

        return new TypeAdapter<>() {
            // the list of fields
            private final List<Field> fields = Requestable.this.fields.get(type.getRawType());
            // the original type adapter.
            private final TypeAdapter<T> original = (TypeAdapter<T>) gson.getDelegateAdapter(Requestable.this, TypeToken.get(type.getRawType()));

            @Override
            public void write(JsonWriter out, T value) throws IOException {
                original.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                final var deserialized = original.read(in);
                // check if we have an account type.
                if (type.getRawType().isAssignableFrom(Account.class)) {
                    // if so, return right away if we have no local account yet.
                    // this indicates we are authenticating and need our account.
                    if (localAccount == null) return deserialized;
                    // other-wise - compare, if its our account ID return the local and if not return the deserialized.
                    final var account = (Account) deserialized;
                    if (account.accountId().equals(localAccount.accountId())) {
                        return (T) localAccount;
                    }
                    return deserialized;
                }

                // if we don't have the account type, iterate through all fields.
                fields.forEach(field -> {
                    final var instance = requestables.get(field.getType());
                    try {
                        field.set(deserialized, instance);
                    } catch (IllegalAccessException exception) {
                        exception.printStackTrace();
                    }
                });
                return deserialized;
            }
        };
    }
}
