package com.example;

import org.lwjgl.*;
import java.nio.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
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
        // shader de vertice etapa:
        String fonteShaderDeVertice = " #version 330 core \n" +
                "layout(location = 0) in vec3 aPos; \n" +
                "void main() { \n" +
                "gl_Position = vec4(aPos, 1.0);\n" +
                "}";

        int shaderDeVertice = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(shaderDeVertice, fonteShaderDeVertice);
        glCompileShader(shaderDeVertice);

        if (glGetShaderi(shaderDeVertice, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Vertex Shader Erro:\n" + glGetShaderInfoLog(shaderDeVertice));
        }

        // etapa do shader de fragmento:
        String fonteShaderDerFragmento = "#version 330 core\n" +
                "out vec4 FragColor; \n" +
                "void main() {\n" +
                "    FragColor = vec4(1.0, 1.0, 1.0, 1.0);" +
                "};";
        int shaderDeFragmento = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(shaderDeFragmento, fonteShaderDerFragmento);
        glCompileShader(shaderDeFragmento);
        if (glGetShaderi(shaderDeVertice, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("shader de fragmento: erro na compilação");
        }

        // etapa de vincular os programas de shader em um unico:

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, shaderDeVertice);
        glAttachShader(shaderProgram, shaderDeFragmento);
        glLinkProgram(shaderProgram);

        // verifica se a vinculacao foi bem sucedida
        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Erro ao linkar shader program:\n" + glGetProgramInfoLog(shaderProgram));
        }

        // deleta os shaders que não serão mais usados
        glDeleteShader(shaderDeVertice);
        glDeleteShader(shaderDeFragmento);

        glUseProgram(shaderProgram);

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

