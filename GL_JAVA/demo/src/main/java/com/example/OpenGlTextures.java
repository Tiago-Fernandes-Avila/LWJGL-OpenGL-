package com.example;

import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CURRENT_RASTER_POSITION;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_BORDER_COLOR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WIDTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterfv;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.glActiveTexture;
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
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.opengl.GL46.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.example.Utils.ShaderConfig;

public class OpenGlTextures {
    private int VAO;
    private int program;
    private int VBO;
    private long window;
    private int texture;
    private int texture1;
    private int texture2;
    private int texture3;

    public static void main(String[] args) {
        new OpenGlTextures().run();
    }

    public void init() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);

        window = glfwCreateWindow(640, 480, "Texturas", 0, 0);

        if (window == 0L) {
            throw new RuntimeException("Não foi possivel criar a janela");
        }

        glfwMakeContextCurrent(window);

        glfwSwapInterval(1);
        GL.createCapabilities();

        glfwShowWindow(window);
    }

    public void setupGeometry() {

        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);

        float vertices[] = {
                // Posições (x, y, z) //cores // Coordenadas UV (u, v)
                1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // Topo direito
                1.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // Base direita
                -1.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, // Base esquerda
                -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f // Topo esquerdo
        };
        VBO = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        program = ShaderConfig.createShader().setVertexShader(
                "demo/src/main/java/com/example/shaders/vertexShaders/shaderTexture.glsl")
                .setfragmentShader("demo/src/main/java/com/example/shaders/FragmentShader/fragSTexture.glsl")
                .applyShaderConfig().getProgram();

        texture = new MineTexture().createTexture("demo/src/main/java/com/example/assets/awesomeface.png", 0);

        texture1 = new MineTexture().createTexture("demo/src/main/java/com/example/assets/wall.jpg", 1);

        texture2 = new MineTexture().createTexture("demo/src/main/java/com/example/assets/container.jpg", 2);

        texture3 = new MineTexture().createTexture("demo/src/main/java/com/example/assets/Piso.jpg", 3);

        glUseProgram(program);

        int tex = glGetUniformLocation(program, "ourTexture");
        int tex1 = glGetUniformLocation(program, "ourTexture1");
        glUniform1i(tex, 0);
        glUniform1i(tex1, 1);

    }

    public void loop() {

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        while (!glfwWindowShouldClose(window)) {

            glClear(GL_COLOR_BUFFER_BIT);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);

            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture2);

            glUseProgram(program);
            glBindVertexArray(VAO);
            glDrawArrays(GL_QUADS, 0, 4);

            glfwSwapBuffers(window);
            glfwPollEvents();

        }
    }

    public void run() {
        this.init();
        this.setupGeometry();
        this.loop();

        glfwTerminate();
        glfwDestroyWindow(window);
    }

    public class MineTexture {
        private int texture;
        private int width;
        private int height;
        private ByteBuffer imagBuffer;

        public int createTexture(String texture_path, int pos) {

            texture = glGenTextures();
            glActiveTexture(GL_TEXTURE0 + pos);
            glBindTexture(GL_TEXTURE_2D, texture);

            try (MemoryStack s = MemoryStack.stackPush()) {
                IntBuffer w = s.callocInt(1);
                IntBuffer h = s.callocInt(1);
                IntBuffer nrChannels = s.callocInt(1);

                stbi_set_flip_vertically_on_load(true);
                imagBuffer = stbi_load(texture_path, w, h, nrChannels, 0);
                width = w.get(0);
                height = h.get(0);

                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

                if (imagBuffer != null) {
                    if (nrChannels.get(0) == 3) {
                        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, imagBuffer);
                        glGenerateMipmap(GL_TEXTURE_2D);
                    } else if (nrChannels.get(0) == 4) {
                        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, imagBuffer);
                        glGenerateMipmap(GL_TEXTURE_2D);
                    }

                } else {
                    System.out.println("Erro ao carregar textura");
                }

            }

            stbi_image_free(imagBuffer);

            return texture;
        }

    }

}

