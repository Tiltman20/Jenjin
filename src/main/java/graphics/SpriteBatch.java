//package graphics;
//
//
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL30.*;
//
//import java.nio.FloatBuffer;
//
//import org.lwjgl.BufferUtils;
//
//public class SpriteBatch {
//    private static final int MAX_SPRITES = 1000;
//    private static final int FLOATS_PER_VERTEX = 6;
//    private static final int VERTICES_PER_SPRITE = 6;
//
//    private static final int MAX_TEXTURES = 8;
//
//    private final Texture[] textureSlots = new Texture[MAX_SPRITES];
//    private int textureSlotCount;
//
//    private final int vao;
//    private final int vbo;
//
//    private final FloatBuffer buffer;
//
//    private int spriteCount = 0;
//    private Shader shader;
//    private Camera2D currentCamera;
//
//    public SpriteBatch() {
//        buffer = BufferUtils.createFloatBuffer(MAX_SPRITES * VERTICES_PER_SPRITE *  FLOATS_PER_VERTEX);
//
//        vao = glGenVertexArrays();
//        vbo = glGenBuffers();
//
//        glBindVertexArray(vao);
//        glBindBuffer(GL_ARRAY_BUFFER, vbo);
//
//        glBufferData(
//                GL_ARRAY_BUFFER,
//                buffer.capacity() * Float.BYTES,
//                GL_DYNAMIC_DRAW
//        );
//
//        glVertexAttribPointer(0, 3, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 0);
//        glEnableVertexAttribArray(0);
//
//        glVertexAttribPointer(1, 2, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 3 * Float.BYTES);
//        glEnableVertexAttribArray(1);
//
//        glVertexAttribPointer(2, 1, GL_FLOAT, false, FLOATS_PER_VERTEX * Float.BYTES, 5 *  Float.BYTES);
//        glEnableVertexAttribArray(2);
//
//        glBindBuffer(GL_ARRAY_BUFFER, 0);
//        glBindVertexArray(0);
//    }
//
//    public void begin(Camera2D camera) {
//        currentCamera = camera;
//        spriteCount = 0;
//        buffer.clear();
//
//        shader = Sprite.getSharedShader();
//        shader.bind();
//        shader.setMat4("uProjection", camera.getProjection());
//    }
//
//    public void draw(Sprite sprite){
//        if (spriteCount >= MAX_SPRITES) return;
//
//        int texIndex = getTextureIndex(sprite.getTexture());
//
//        float x = sprite.getTransform().position.x;
//        float y = sprite.getTransform().position.y;
//
//        float hw = sprite.getWidth() / 2f;
//        float hh = sprite.getHeight() / 2f;
//
//        putVertex(x - hw, y + hh, 0f, 0f, 0f, texIndex);
//        putVertex(x - hw, y - hh, 0f, 0f, 1f, texIndex);
//        putVertex(x + hw, y - hh, 0f, 1f, 1f, texIndex);
//
//        putVertex(x - hw, y + hh, 0f, 0f, 0f, texIndex);
//        putVertex(x + hw, y - hh, 0f, 1f, 1f, texIndex);
//        putVertex(x + hw, y + hh, 0f, 1f, 0f, texIndex);
//
//        spriteCount++;
//    }
//
//    private void putVertex(float x, float y, float z, float u, float v, int texIndex) {
//        buffer.put(x).put(y).put(z).put(u).put(v).put((float)texIndex);
//    }
//
//    public void end(){
//        buffer.flip();
//
//        for (int i = 0; i < textureSlotCount; i++) {
//            glActiveTexture(GL_TEXTURE0 + i);
//            textureSlots[i].bind();
//        }
//
//        int[] samplers = new int[MAX_SPRITES];
//        for (int i = 0; i < MAX_TEXTURES; i++) {
//            samplers[i] = i;
//        }
//        shader.setIntArray("uTextures", samplers);
//
//        glBindBuffer(GL_ARRAY_BUFFER, vbo);
//        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
//        glBindBuffer(GL_ARRAY_BUFFER, 0);
//
//        glBindVertexArray(vao);
//        glDrawArrays(GL_TRIANGLES, 0, spriteCount * VERTICES_PER_SPRITE);
//        glBindVertexArray(0);
//
//        shader.unbind();
//    }
//
//    private int getTextureIndex(Texture texture){
//        for (int i = 0; i<textureSlotCount;i++){
//            if (texture == textureSlots[i]) return i;
//        }
//        if (textureSlotCount >= MAX_TEXTURES){
//            end();
//            begin(currentCamera);
//        }
//        textureSlots[textureSlotCount++] = texture;
//        return textureSlotCount;
//    }
//}
