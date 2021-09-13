package net.noobsters.novohangar.syncedTeams.objects;

import java.util.UUID;

import com.github.javafaker.Faker;

import lombok.Getter;

public class User {
    private static Faker faker = new Faker();
    private @Getter UUID uuid;
    private @Getter String name;

    /**
     * Creates a new user with a random name.
     * 
     * @param uuid The UUID of the user.
     * @param name The name of the user.
     */
    public User(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * Creates a new user with a random name.
     * 
     * @return A new user.
     */
    public static User generateUser() {
        return new User(UUID.randomUUID(), faker.name().firstName());
    }
    /**
     * Function that generates a user with a random name from internet.
     */

}
