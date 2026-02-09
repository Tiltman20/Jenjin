package core;

public class Timer {
    private double lastTime;

    public void init(){
        lastTime = getTime();
    }

    public float getDeltaTime(){
        double currentTime = getTime();
        float delta = (float)(currentTime - lastTime);
        lastTime = currentTime;
        return delta;
    }

    private double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }
}
