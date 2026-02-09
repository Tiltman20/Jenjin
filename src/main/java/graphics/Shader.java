package graphics;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int programId;

    public Shader(String vertexSrc, String fragmentSrc){
        int vertexId = compile(vertexSrc, GL_VERTEX_SHADER);
        int fragmentId = compile(fragmentSrc, GL_FRAGMENT_SHADER);

        programId = glCreateProgram();
        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        glLinkProgram(programId);

        if(glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE){
            throw new RuntimeException("Shader link failed: " + glGetProgramInfoLog(programId));
        }

        glDeleteShader(vertexId);
        glDeleteShader(fragmentId);
    }

    public void setMat4(String name, Matrix4f mat){
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(
                    glGetUniformLocation(programId, name),
                    false,
                    mat.get(stack.mallocFloat(16))
            );
        }
    }

    public void setVec2(String name, float x, float y){
        glUniform2f(glGetUniformLocation(programId, name), x, y);
    }
    public void setVec3(String name, float x, float y, float z) {
        glUniform3f(glGetUniformLocation(programId, name), x, y, z);
    }
    public void setInt(String name, int x) {
        glUniform1i(glGetUniformLocation(programId, name), x);
    }

    public void setIntArray(String name, int[] array){
        glUniform1iv(glGetUniformLocation(programId, name), array);
    }

    private int compile(String src, int type) {
        int id = glCreateShader(type);
        glShaderSource(id, src);
        glCompileShader(id);

        if(glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE){
            throw new RuntimeException("Shader compile failed: " + glGetShaderInfoLog(id));
        }
        return id;
    }

    public void bind(){
        glUseProgram(programId);
    }
    public void unbind(){
        glUseProgram(0);
    }

    public void cleanup(){
        glDeleteProgram(programId);
    }



}
