package graphics3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform3D {
    public Vector3f position = new Vector3f(0f, 0f, 0f);
    public final Vector3f rotation = new Vector3f(0f, 0f, 0f);
    public final Vector3f scale = new Vector3f(1f, 1f, 1f);

    public Transform3D parent;

    private final Matrix4f modelMatrix = new Matrix4f().identity();

    public Matrix4f getModelMatrix() {
        Matrix4f local = new Matrix4f().identity();
        local.translate(position)
            .rotateX((float) Math.toRadians(rotation.x))
            .rotateY((float) Math.toRadians(rotation.y))
            .rotateZ((float) Math.toRadians(rotation.z))
            .scale(scale);

        return parent == null ? local : parent.getModelMatrix().mul(local);
    }

    public Vector3f getWorldPosition(){
        Matrix4f model = getModelMatrix();
        return new Vector3f(model.m30(), model.m31(), model.m32());
    }

    public Matrix4f getWorldMatrix(){
        if(parent == null)
            return getModelMatrix();

        return new Matrix4f(parent.getWorldMatrix())
                .mul(getModelMatrix());
    }


    public Vector3f worldToLocalPosition(Vector3f worldPos){

        if(parent == null)
            return new Vector3f(worldPos);

        Matrix4f invParent = new Matrix4f(parent.getWorldMatrix()).invert();
        Vector3f local = new Vector3f();
        invParent.transformPosition(worldPos, local);

        return local;
    }

}
