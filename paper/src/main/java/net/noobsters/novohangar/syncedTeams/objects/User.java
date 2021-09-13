package net.noobsters.novohangar.syncedTeams.objects;

import java.util.Random;
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
     * Static function that generates a random collection of users
     * 
     * @param size The size of the collection.
     */
    public static User[] generateUsers(int size) {
        User[] users = new User[size];
        for (int i = 0; i < size; i++) {
            users[i] = generateUser();
        }
        return users;
    }

    /**
     * Static function that generates a random collection of users with a max size
     * of 10.
     */
    public static User[] generateUsers() {

        int size = new Random().nextInt(10) + 1;
        User[] users = new User[size];
        for (int i = 0; i < size; i++) {
            users[i] = generateUser();
        }
        return users;
    }

}
