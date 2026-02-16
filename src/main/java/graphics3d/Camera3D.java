package graphics3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera3D {
    private final Vector3f position =  new Vector3f(0f, 0f, 3f);


    private final Vector3f front = new Vector3f(0f, 0f, -1f);
    private final Vector3f up = new Vector3f(0f, 1f, 0f);
    private final Vector3f right = new Vector3f(1f, 0f, 0f);

    private float yaw = -90f;
    private float pitch = 0f;

    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f projectionMatrix = new Matrix4f();

    private float fov = (float)Math.toRadians(60.0f);
    private float aspect = 16f/9f;
    private float near = 0.1f;
    private float far  = 1000f;

    private Transform3D transform;

    public Camera3D(float fovDeg, float aspect, float near, float far){
        this.fov = fovDeg;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        updateProjection();
        updateVectors();
    }

    public Camera3D() {

    }

    private void updateProjection(){
        projectionMatrix.identity().perspective(fov, aspect, near, far);
    }


    public Matrix4f getViewMatrix(){

        if(transform != null){
            Matrix4f model = transform.getModelMatrix();

            Vector3f camPos = transform.getWorldPosition();

            Vector3f forward = new Vector3f(0,0,-1);
            model.transformDirection(forward);

            Vector3f upVec = new Vector3f(0,1,0);
            model.transformDirection(upVec);

            Vector3f center = new Vector3f(camPos).add(forward);

            viewMatrix.identity().lookAt(camPos, center, upVec);
            return viewMatrix;
        }

        Vector3f center = new Vector3f(position).add(front);
        viewMatrix.identity().lookAt(position, center, up);
        return viewMatrix;
    }


    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public Vector3f getPosition(){
        return position;
    }

    private void updateVectors(){
        front.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.normalize();

        right.set(front).cross(0f, 1f, 0f).normalize();
        up.set(right).cross(front).normalize();
    }



    public void moveForward(float amount) {
        position.add(new Vector3f(front).mul(amount));
    }

    public void moveBackward(float amount) {
        position.sub(new Vector3f(front).mul(amount));
    }

    public void moveRight(float amount) {
        position.add(new Vector3f(right).mul(amount));
    }

    public void moveLeft(float amount) {
        position.sub(new Vector3f(right).mul(amount));
    }

    public void moveUp(float amount) {
        position.add(new Vector3f(up).mul(amount));
    }

    public void moveDown(float amount) {
        position.sub(new Vector3f(up).mul(amount));
    }

    public void rotate(float yawOffset, float pitchOffset) {
        yaw += yawOffset;
        pitch += pitchOffset;

        pitch = Math.max(-89f, Math.min(89f, pitch));
        updateVectors();
    }


    public void setAspect(float aspect) {
        this.aspect = aspect;
        updateProjection();
    }

    public void setTransform(Transform3D transform) {
        this.transform = transform;
    }

    public void setFov(float fov){
        this.fov = fov;
        updateProjection();
    }
    public float getFov(){ return fov; }

    public void setNear(float near){
        this.near = near;
        updateProjection();
    }
    public float getNear(){ return near; }

    public void setFar(float far){
        this.far = far;
        updateProjection();
    }
    public float getFar(){ return far; }

}
