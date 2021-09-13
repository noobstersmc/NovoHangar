package net.noobsters.novohangar.syncedTeams;

import com.google.gson.Gson;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.pubsub.RedisPubSubListener;
import net.noobsters.novohangar.syncedTeams.objects.TeamObject;
import net.noobsters.novohangar.syncedTeams.objects.User;

public class Demo {
    private static Gson gson = new Gson();

    public static void main(String[] args) throws InterruptedException {
        /**
         * Initialize some variables to be used in the demo.
         */
        var users = User.generateUsers();
        var team = TeamObject.createTeamObject("team", users);
        /** Initiliaze a redis connection. */
        var redisClient = RedisClient
                .create(RedisURI.builder().withHost("redis-11764.c73.us-east-1-2.ec2.cloud.redislabs.com")
                        .withPort(11764).withPassword("Gxb1D0sbt3VoyvICOQKC8IwakpVdWegW").build());
        // Create a thread with the logic to intercept the data to simulate a server
        // recieving the data.
        var listenerThread = createNewApplicationThread(redisClient);
        // Start the thread.
        listenerThread.start();
        // Sleep for a few milliseconds to allow the thread to start.
        Thread.sleep(500);
        // Send the serialized data to the redis server.
        redisClient.connectPubSub().async().publish("dedsafio", gson.toJson(team));
        // Sleep for a few milliseconds to allow for the write.
        Thread.sleep(1000);
        var query = redisClient.connect().sync().get("dedsafio-team:" + team.getTeamID());
        System.out.println("The database contains: " + query);
    }

    private static Thread createNewApplicationThread(RedisClient redisClient) {
        return new Thread(() -> {
            // Create an async connection to send command through.
            var asyncConnect = redisClient.connect().async();
            // Create a pubSub connection for communication pipeline.
            var pubSub = redisClient.connectPubSub();
            // Connect the pubSub
            var sync = pubSub.sync();
            // Define the pubSub listener.
            pubSub.addListener(new RedisPubSubListener<String, String>() {

                @Override
                public void message(String channel, String message) {
                    System.out.println("Message received: " + message + " on channel: " + channel);
                    // Reconstruct the serialized object.
                    var teamObjectReconstructed = gson.fromJson(message, TeamObject.class);
                    // Print contents of team
                    teamObjectReconstructed.printTeam();
                    // Save the team data to long term storage and for API access.
                    asyncConnect.set("dedsafio-team:" + teamObjectReconstructed.getTeamID(), message);

                }

                @Override
                public void message(String pattern, String channel, String message) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void subscribed(String channel, long count) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void psubscribed(String pattern, long count) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void unsubscribed(String channel, long count) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void punsubscribed(String pattern, long count) {
                    // TODO Auto-generated method stub

                }

            });
            sync.subscribe("dedsafio");

        });
    }

}
