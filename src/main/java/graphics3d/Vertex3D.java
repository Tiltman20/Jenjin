package graphics3d;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex3D {
    public Vector3f position;
    public Vector3f normal;
    public Vector2f uv;

    public Vertex3D(Vector3f position,Vector3f normal, Vector2f uv) {
        this.position = position;
        this.normal = normal;
        this.uv = uv;
    }
}
