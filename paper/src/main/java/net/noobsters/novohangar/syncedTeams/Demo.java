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
        var listenerThread = createNewApplicationThread(redisClient);

        listenerThread.start();
        Thread.sleep(500);
        redisClient.connectPubSub().async().publish("dedsafio", gson.toJson(team));

        while (true) {

        }
    }

    private static Thread createNewApplicationThread(RedisClient redisClient) {
        return new Thread(() -> {
            var pubSub = redisClient.connectPubSub();
            var sync = pubSub.sync();
            pubSub.addListener(new RedisPubSubListener<String, String>() {

                @Override
                public void message(String channel, String message) {
                    System.out.println("Message received: " + message + " on channel: " + channel);

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
