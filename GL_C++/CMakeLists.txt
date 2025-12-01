# Versão mínima do CMake
cmake_minimum_required(VERSION 3.3)

# Nome do seu projeto
project(LearnOpenGLProject C CXX)

# Onde procurar por arquivos de cabeçalho
# O seu compilador vai procurar nos caminhos abaixo para encontrar "glad/glad.h" e "GLFW/glfw3.h"
include_directories(libs/)
include_directories(libs/glad/ libs/glfw/include/ libs/utils libs/glm)

# Adiciona o código fonte da GLFW (biblioteca externa)
# Isso carrega o CMakeLists.txt que está dentro da pasta libs/glfw
add_subdirectory(libs/glfw)

# Adiciona todos os arquivos-fonte (.cpp, .c) que farão parte do seu executável
set(SOURCE_FILES
    src/main.cpp
    libs/glad/glad.c
    src/utilities/Shader.cpp
    
)

# Cria o executável chamado "MeuPrograma" a partir dos arquivos de código
add_executable(MeuPrograma ${SOURCE_FILES})

# Linka o executável com as bibliotecas
# Ele está dizendo: "Para compilar o MeuPrograma, você precisará da GLFW e do OpenGL"
target_link_libraries(MeuPrograma 
    PRIVATE 
    glfw
    GL X11 Xrandr Xinerama Xcursor Xi  # Windows (troque para GL ou opengl para Linux/macOS)
    # Se estiver no Linux: adicione X11 Xrandr Xinerama Xcursor Xi
)