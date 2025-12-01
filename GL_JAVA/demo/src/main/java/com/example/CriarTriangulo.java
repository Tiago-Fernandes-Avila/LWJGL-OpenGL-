package com.example;

import org.lwjgl.*;

import com.example.Utils.ShaderConfig;

import java.nio.*;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL33.*;

public class CriarTriangulo {
    private int vao;
    private int vbo;
    private int ebo;
    private float[] vertices;
    private int[] indices;

    public CriarTriangulo(float[] vertices, int[] indices) {
        this.vertices = vertices;
        this.indices = indices;
    }

    public void criar() {

        vao = glGenVertexArrays(); // gera o id do VAO
        glBindVertexArray(vao); // vincula o VAO para que as configurações sejam salvas nele

        // entrada de vertice etapa:

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices);
        vertexBuffer.flip(); // Important: call flip() to set the buffer's position to 0 and limit to the
                             // current position.

        vbo = glGenBuffers(); // Generate a buffer ID
        glBindBuffer(GL_ARRAY_BUFFER, vbo); // Bind it to the GL_ARRAY_BUFFER target

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // configurando o EBO
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length * Integer.BYTES);
        indexBuffer.put(indices);
        indexBuffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        // Configuração do VAO etapa:
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0); // como a gpu irá ler os dados do buffer
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        
        ShaderConfig shaderConfig = ShaderConfig
        .createShader()
        .setVertexShader("/home/devtiago/Área de trabalho/Workspace/programas c++/LWJGL-OpenGL-/demo/src/main/java/com/example/shaders/vertexShaders/vertexShaderTest.glsl")
        .setfragmentShader("/home/devtiago/Área de trabalho/Workspace/programas c++/LWJGL-OpenGL-/demo/src/main/java/com/example/shaders/FragmentShader/fragSTest.glsl").applyShaderConfig().use();

    }

    public int getVerticesQuantity() {
        return vertices.length / 3;
    }

    public int getIndicesQuantity() {
        return indices.length;
    }

    public int getVao() {
        return vao;
    }

    public int getVbo() {
        return vbo;
    }

    public int getEbo() {
        return ebo;
    }
}

