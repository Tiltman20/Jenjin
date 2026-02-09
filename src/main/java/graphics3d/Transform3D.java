package graphics3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform3D {
    public final Vector3f position = new Vector3f(0f, 0f, 0f);
    public final Vector3f rotation = new Vector3f(0f, 0f, 0f);
    public final Vector3f scale = new Vector3f(1f, 1f, 1f);

    private final Matrix4f modelMatrix = new Matrix4f();

    public Matrix4f getModelMatrix() {
        modelMatrix.identity()
                .translate(position)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                . scale(scale);
        return modelMatrix;
    }
}
