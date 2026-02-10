package graphics3d;

import org.joml.Vector3f;

public class Vertex3D {
    public Vector3f position;
    public Vector3f normal;

    public Vertex3D(Vector3f position,Vector3f normal) {
        this.position = position;
        this.normal = normal;
    }
}
