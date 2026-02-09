package graphics;

import org.joml.Matrix4f;

public class Camera2D {
    private final Matrix4f projection;

    public Camera2D(float width, float height){
        projection = new Matrix4f()
                .ortho2D(-width/2f, width/2f,
             -height/2f, height/2f);
    }
    public Matrix4f getProjection() {
        return projection;
    }
}
