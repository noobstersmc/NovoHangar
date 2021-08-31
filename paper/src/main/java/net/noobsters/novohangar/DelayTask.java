package net.noobsters.novohangar;

public class DelayTask implements Runnable {
    private long time;
    
    public DelayTask(long time) {
        this.time = time;
    }

    public static DelayTask of(long milliseconds){
        return new DelayTask(milliseconds);
    }    

    @Override
    public void run() {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
