#include <iostream>
#include "glad/glad.h"
#include <GLFW/glfw3.h>
#include <stdio.h>
#include "utilities/Shader.cpp"
#include "utilities/Texture.cpp"
#include "GL/gl.h"
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

void framebuffer_size_callback(GLFWwindow *window, int width, int height);
unsigned int VBO, VAO, texture, program, EBO;
GLFWwindow *window;

void handleEvents(float &value);
void setupGeometry();
int init();
void loop();

int main()
{
    init();
    glm::vec4 vetor(1.0f, 0.0f, 0.0f, 1.0f);
    // glm::mat4 matriz = glm::mat4(1.0f);
    // matriz = glm::translate(matriz, glm::vec3(2.0f,1.0f,1.0f)); combinando matrizes de transformação por meio da lib

    glm::mat4 matriz(1.0f, 0.0f, 0.0f, 1.0f, // colocando a matriz manualmente
                     0.0f, 1.0f, 0.0f, 1.0f,
                     0.0f, 0.0f, 1.0f, 1.0f,
                     0.0f, 0.0f, 0.0f, 1.0f);
    vetor = vetor * matriz;
    std::cout << "Vetor transformado: " << vetor.x << vetor.y << vetor.z << std::endl;
    setupGeometry();
    loop();

    return 0;
}

// Função de callback para redimensionamento da janela
void framebuffer_size_callback(GLFWwindow *window, int width, int height)
{
    glViewport(0, 0, width, height);
}

void setupGeometry()
{

    float square_vertices[] = {
        0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
        0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,  // right bottom
        -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, // left bottom
        -0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f

    };

    glGenVertexArrays(1, &VAO);
    glBindVertexArray(VAO);
    glGenBuffers(1, &VBO);

    glBindBuffer(GL_ARRAY_BUFFER, VBO);

    glBufferData(GL_ARRAY_BUFFER, sizeof(square_vertices), square_vertices, GL_STATIC_DRAW);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(float), 0);
    glEnableVertexAttribArray(0);

    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 8 * sizeof(float), (void *)(3 * sizeof(float)));
    glEnableVertexAttribArray(1);

    glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, 8 * sizeof(float), (void *)(6 * sizeof(float)));
    glEnableVertexAttribArray(2);
}

void loop()
{
    Shader s = Shader("../src/shader/vertex.vs", "../src/shader/fragment.fs");
    s.use();

    Texture tex1("../assets/container.jpg", 0);

    Texture tex2("../assets/awesomeface.png", 1);
    s.setInt("ourTexture1", 0);
    s.setInt("ourTexture2", 1);

    float value = 1.0f;

    while (!glfwWindowShouldClose(window))

    {

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glm::mat4 trans = glm::mat4(1.0f);
        trans = glm::translate(trans, glm::vec3(0.5f, -0.5f, 0.0f));
        trans = glm::rotate(trans, (float)glfwGetTime(), glm::vec3(0.0f, 0.0f, 1.0f));
        glUniformMatrix4fv(glGetUniformLocation(s.ID, "transform"), 1, GL_FALSE, glm::value_ptr(trans));
        handleEvents(value);
        s.setFloat("value", value);
        glBindVertexArray(VAO);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);

        glm::mat4 matriz(1);
        matriz = glm::translate(matriz, glm::vec3(-0.5f, 0.5f, 0.0f));
        float scaleAmount = static_cast<float>(abs(sin(glfwGetTime())) / 2);
        matriz = glm::scale(matriz, glm::vec3(0.5 + scaleAmount,0.5 + scaleAmount,scaleAmount));

        glUniformMatrix4fv(glGetUniformLocation(s.ID, "transform"), 1, GL_FALSE, &matriz[0][0]);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);

        // Verifica eventos e troca buffers
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    // 5. Termina o GLFW
    glfwTerminate();
}

int init()
{
    // 1. Inicializa o GLFW
    glfwInit();
    // Configura a versão do OpenGL/
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    // 2. Cria a janela
    window = glfwCreateWindow(800, 600, "LearnOpenGL Window", NULL, NULL);
    if (window == NULL)
    {
        glGenBuffers(GL_ARRAY_BUFFER, &VBO);
        std::cout << "Falha ao criar janela GLFW" << std::endl;
        glfwTerminate();
        return -1;
    }

    glfwMakeContextCurrent(window);

    // 3. Inicializa o GLAD
    if (!gladLoadGLLoader((GLADloadproc)glfwGetProcAddress))
    {
        std::cout << "Falha ao inicializar GLAD" << std::endl;
        return -1;
    }

    // Configura a viewport
    glViewport(0, 0, 800, 600);
    glfwSetFramebufferSizeCallback(window, framebuffer_size_callback);

    return 0;
}

void handleEvents(float &value)
{
    if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
    {
        glfwSetWindowShouldClose(window, true);
    }

    if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
    {
        value += 0.01f;
        if (value >= 1.0f)
            value = 1.0f;
    }
    if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
    {
        value -= 0.01f;
        if (value <= 0.0f)
        {
            value = 0.0f;
        }
    }
}