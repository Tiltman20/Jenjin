package game;

public class SimpleCubeMesh {
    public static float[] VERTICES = {
            // vorne
            -0.5f, -0.5f,  0.5f, // 0
            0.5f, -0.5f,  0.5f, // 1
            0.5f,  0.5f,  0.5f, // 2
            -0.5f,  0.5f,  0.5f, // 3

            // hinten
            -0.5f, -0.5f, -0.5f, // 4
            0.5f, -0.5f, -0.5f, // 5
            0.5f,  0.5f, -0.5f, // 6
            -0.5f,  0.5f, -0.5f  // 7
    };
    public static int[] INDICES = {
            // Front
            0, 1, 2,
            2, 3, 0,

            // Right
            1, 5, 6,
            6, 2, 1,

            // Back
            5, 4, 7,
            7, 6, 5,

            // Left
            4, 0, 3,
            3, 7, 4,

            // Top
            3, 2, 6,
            6, 7, 3,

            // Bottom
            4, 5, 1,
            1, 0, 4
    };
}
