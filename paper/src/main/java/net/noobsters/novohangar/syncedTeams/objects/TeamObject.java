package net.noobsters.novohangar.syncedTeams.objects;

import java.util.UUID;

public class TeamObject {
    /** A team object is a team composed of an array of Users. */
    private User[] users;
    private UUID teamID;
    private String teamName;

    /**
     * Constructor for a TeamObject.
     * 
     * @param users    The array of users that make up the team.
     * @param teamID   The UUID of the team.
     * @param teamName The name of the team.
     */
    public TeamObject(User[] users, UUID teamID, String teamName) {
        this.users = users;
        this.teamID = teamID;
        this.teamName = teamName;
    }

    /**
     * Getter for the array of users that make up the team.
     * 
     * @param users    The array of users that make up the team.
     * @param teamID   The UUID of the team.
     * @param teamName Name of the team.
     * @return A new TeamObject.
     */
    public static TeamObject createTeamObject(User[] users, UUID teamID, String teamName) {
        return new TeamObject(users, teamID, teamName);
    }

    /**
     * Constructor for a TeamObject with random teamID.
     * 
     * @param users    The array of users that make up the team.
     * @param teamName Name of the team.
     * @return A new TeamObject.
     */
    public static TeamObject createTeamObject(User[] users, String teamName) {
        return new TeamObject(users, UUID.randomUUID(), teamName);
    }

    /**
     * Constructor for a TeamObject using different notation than traditional array
     * 
     * @param teamName Name of the team.
     * @param users    The array of users that make up the team.
     * @return A new TeamObject.
     */
    public static TeamObject createTeamObject(String teamName, User... users) {
        return new TeamObject(users, UUID.randomUUID(), teamName);
    }

    /**
     * @return the users
     */
    public UUID getTeamID() {
        return teamID;
    }

    /**
     * @return the teamName
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Constructor for a team object.
     * 
     * @param users The array of users that make up the team.
     */
    public TeamObject(User[] users) {
        this.users = users;
    }

    /**
     * Getter for the array of users that make up the team.
     * 
     * @return The array of users that make up the team.
     */
    public User[] getUsers() {
        return users;
    }
}
