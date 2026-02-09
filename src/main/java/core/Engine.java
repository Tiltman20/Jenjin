package core;

import graphics.Renderer;
import input.Input;
import input.MouseInput;

public class Engine {
    private boolean running;
    private final Game game;
    private final Timer timer;
    private final Window window;
    private final Renderer renderer;

    public Engine(Game game){
        this.game = game;
        this.timer = new Timer();
        this.window = new Window("Jenjin", 1280, 720);
        this.renderer = new Renderer();
    }

    public void start(){
        init();
        loop();
        cleanup();
    }

    private void init(){
        window.init();
        renderer.init();
        timer.init();
        game.init();
        running = true;
    }

    private void loop(){
        while(running){
            float dt = timer.getDeltaTime();

            window.update();
            update(dt);
            render();

            Input.endFrame();
            MouseInput.endFrame();
        }
    }

    private void render() {
        renderer.clear();
        game.render();
    }

    private void update(float dt) {
        game.update(dt);
    }

    private void cleanup() {
        game.cleanup();
        window.cleanup();
    }

    public void stop(){
        running = false;
    }
}
