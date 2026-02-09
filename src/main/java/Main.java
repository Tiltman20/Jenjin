
import game.TestGame3D;
import core.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Game game = new TestGame3D();
        Engine engine = new Engine(game);
        engine.start();
    }
}
