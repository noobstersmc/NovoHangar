package net.noobsters.novohangar;

import java.util.concurrent.CompletableFuture;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * BukkitTCT - Quick and dirty way to run tasks asynchronously and synchronously
 * without needing any dependencies.
 * 
 * @author jcedeno
 */
public class BukkitTCT extends TaskChainTool {

    private static Plugin plugin = null;

    /**
     * Static method to pass a Bukkit instance to the TaskChainTool. Must be called
     * only once, but can be called as many times as needed in case plugin becomes
     * null.
     * 
     * @param instance Plugin instance
     * @return true if plugin instance was set, false otherwise
     */
    public static boolean registerPlugin(Plugin instance) {
        plugin = instance;
        return isPluginReady();
    }

    /**
     * Helper method to check if plugin instance is ready.
     * 
     * @return true if plugin instance is ready, false otherwise
     */
    public static boolean isPluginReady() {
        return plugin != null && plugin.isEnabled();
    }

    @Override
    public CompletableFuture<Boolean> execute() {

        return CompletableFuture.supplyAsync(() -> {
            /** Loop to execute all tasks in order. */
            while (!isEmpty()) {
                var nextRunnable = pool();
                if (nextRunnable instanceof BukkitRunnable bukkitRunnable) {
                    var bukkitTask = bukkitRunnable.runTask(plugin);

                    var id = bukkitTask.getTaskId();
                    while (!bukkitTask.isCancelled() && plugin.getServer().getScheduler().isCurrentlyRunning(id)) {
                        // Hold on bukkit main thread
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                var thread = new Thread(nextRunnable);
                try {

                    /**
                     * Join the thread so that it waits until whatever needs to be executed gets
                     * executed.
                     */
                    thread.start();
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return true;
        });
    }

}
