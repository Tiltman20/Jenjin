package game;

public class PrimitivePlane {
    public static final float[] VERTICES = {

            // Front (+Z)
            -0.5f,-0.5f, 0f,   0,0,1,
            0.5f,-0.5f, 0f,   0,0,1,
            0.5f, 0.5f, 0f,   0,0,1,
            -0.5f, 0.5f, 0f,   0,0,1
    };
    public static final int[] INDICES = {
            // Front
            0,  1,  2,   2,  3,  0
    };
}
