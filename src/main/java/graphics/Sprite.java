package graphics;

import core.ResourceLoader;

public class Sprite {

    private static Mesh sharedQuad;
    private static Shader sharedShader;

    private final Texture texture;
    private final Transform transform;

    private final float width;
    private final float height;

    public Sprite(Texture texture, float width, float height) {
        this.texture = texture;
        this.transform = new Transform();
        this.width = width;
        this.height = height;

        initSharedResources();
    }

    private void initSharedResources() {
        if (sharedQuad == null) {
            sharedQuad = new Mesh(createQuad(width, height));
        }

        if (sharedShader == null) {
            sharedShader = new Shader(
                    ResourceLoader.load("shaders/sprite.vert"),
                    ResourceLoader.load("shaders/sprite.frag")
            );
        }
    }

    private static float[] createQuad(float w, float h) {
        float hw = w / 2f;
        float hh = h / 2f;

        return new float[]{
                // pos              // uv
                -hw,  hh, 0f,   0f, 0f,
                -hw, -hh, 0f,   0f, 1f,
                hw, -hh, 0f,   1f, 1f,

                -hw,  hh, 0f,   0f, 0f,
                hw, -hh, 0f,   1f, 1f,
                hw,  hh, 0f,   1f, 0f
        };
    }

    public Transform getTransform() {
        return transform;
    }

    public void render(Camera2D camera) {
        sharedShader.bind();
        sharedShader.setMat4("uProjection", camera.getProjection());
        sharedShader.setVec2(
                "uPosition",
                transform.position.x,
                transform.position.y
        );

        texture.bind();
        sharedQuad.render();
        sharedShader.unbind();
    }

    public void cleanup() {
        texture.unbind();
    }
}
