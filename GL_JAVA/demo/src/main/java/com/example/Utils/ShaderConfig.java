package com.example.Utils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class ShaderConfig {
    private int shaderProgram;
    private String vertexSrc;
    private String fragmentSrc;

    
    public ShaderConfig(){

    }


    public static ShaderConfig createShader(){
        return new ShaderConfig();
    }



    public ShaderConfig setfragmentShader(String path){
        this.fragmentSrc = ReadShader.read(path);
        return this;
    }
    public ShaderConfig setVertexShader(String path){
        this.vertexSrc = ReadShader.read(path);
        return this;
    }


    public ShaderConfig applyShaderConfig(){
        setShaderProgram();
        return this;
    }


     private void setShaderProgram() {

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSrc);
        glCompileShader(vertexShader);

        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Não foi possivel compilar o shader de vertice!");
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentSrc);
        glCompileShader(fragmentShader);

        

        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Não foi possivel compilar o shader de fragmento!");
        }

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Erro ao linkar shader program:\n" + glGetProgramInfoLog(shaderProgram));
        }

        

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    
    }

    public int getProgram(){
        return this.shaderProgram;
    }

    public ShaderConfig use(){
        glUseProgram(shaderProgram);
        return this;
    }
    
}
