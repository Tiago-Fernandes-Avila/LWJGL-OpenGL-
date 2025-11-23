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

// primeiro passo para a pipeline do opengl é criar uma entrada de dados de
// vertices.

// ENTRADA de VERTICES:
// apos pegarmos o dados e armazenarmos no FloatBuffer do lwjgl que faz com que
// tenhamos uma memoria que seja acessivel pelo opengl. criamos um id especifico
// para este (buffer do OpenGL no caso GL_ARRAY_BUFFER que iremos mandar para a
// memoria da gpu).
// no final conectamos todas esses dados com o glBufferData que recebe como
// paramentros: o tipo do buffer para a memoria que conterá os dados de vertices
// no caso GL_ARRAY_BUFFER
// colocamos o buffer do lwjgl que contem um dado que está fora do controle da
// jvm nesse buffer ele precisa ter o metodo flip() chamado para que o opengl possa ler os dados corretamente. o tamanho em bytes é vertices.length * Float.BYTES e nesse buffer preciso inserir os dados com o put(vertices).
// Em seguida, uma dica para o OpenGL sobre como o armazenamento de dados será
// acessado (modificado uma vez, usado várias vezes). Outras opções incluem
// GL_STREAM_DRAW, GL_DYNAMIC_DRAW, etc.
/* 
 * a função original em c do glBindBuffer diz: (target ou seja que tipo de
 * buffer iremos armazenar, o tamanho em bytes, o ponteiro para o array de dados
 * no caso a matriz de vertices e por fim a dica de leitura para a memoria fazer
 * otimizações)
 * já em java não possuimos o luxo de poder passar ponteiros para que o opengl
 * possa acessar no caso temos que usar
 * a ferramenta do lwjgl para empacotar um buffer que terá o tamanho espefico
 * definido pelo vertices.length e mandando direto os dados com o put para o
 * buffer sendo no final apenas 3 parametros no java.
 * 
 * Até agora, armazenamos os dados dos vértices na memória da placa gráfica,
 * gerenciada por um objeto de buffer de vértices chamado VBO .
 * Em seguida, queremos criar um shader de vértices e um shader de fragmentos
 * que processem esses dados; então, vamos começar a construí-lo
 * 
 * Shader de vértices e fragmento:
 * O shader de vértices é um dos shaders programáveis. O
 * OpenGL moderno exige que configuremos pelo menos um shader de vértices e um
 * de fragmentos para realizar qualquer renderização
 * para isso vou utilizar a linguagem de shaders de glsl que é uma linguagem
 * semelhante ao c. onde definimos em seu cabeçalho a sua versão
 * 
 * Vertex Shader Processa cada vértice. Define posição, transformações
 * (matrizes), pode passar dados pro fragment shader (como cor, textura…).
 * 
 * Fragment Shader Processa cada fragmento (pixel) e define a cor final. Aqui
 * você pode aplicar cor, textura, iluminação etc.
 * 
 * criamos os shaders e depois vinculamos eles a um programa de shader.
 * 
 * lembrando que na vinculaçao do shaders haverá erros caso não haja uma
 * sincronia nas entradas e saidas dos dados. em outras palavras: ao vincular os
 * shaders em um programa, as saídas de cada shader são conectadas às entradas
 * do próximo. É aqui que você encontrará erros de vinculação se as saídas e
 * entradas não corresponderem.
 * 
 * 
 * observação importante: a o VAO deve ser criado antes do VBO para que funcione
 * em versões posteriores ao 3.3 do opengl
 * vao = glGenVertexArrays();
 * glBindVertexArray(vao);
 * 
 * Etapa de VAO (Vertex Array Object):
 * Bom ainda a gpu não consegue entender como interpretar os dados que vieram do
 * VBO para isso preciso descrever para ela como fazer isso.
 * para isso vou utilizar o glVertexAttribPointer() para indicar coisas como
 * tipagem dos dados, tamanho de cada vertice por exemplo se possuo um vertice
 * de três dimensões cada componente da coordenanda é um float na matrix de
 * vertices, não há um espaço entre os vertices no array. então temos que dizer nessa função
 * que cada vetice na matriz corresponde a 3 indices, com 0 de offset entre
 * eles, o tipo, a VBO que vamos usar é a ultima que foi definida para o
 * GL_ARRAY_BUFFER com glBindBuffer e por fim podemos indicar se queremos a
 * normalização dos dados em coordenadas 3d que são interpretadas pela OpenGL
 * (de -1 a 1) isso é definido por um booleano.
 * para isso temos o VAO que é um objeto que
 * armazena todas essas configurações de como a gpu deve interpretar os dados do
 * VBO.
 * e configuramos o VAO com o glVertexAttribPointer() e
 * glEnableVertexAttribArray()
 * 
 * 
 * 
 * Para desenhar os objetos que desejamos, o OpenGL nos fornece...glDrawArrays()
 * Função que desenha primitivas usando o shader atualmente ativo, a
 * configuração de atributos de vértice
 * Os parâmetros principais da glDrawArrays são:
 * - O tipo de primitiva a ser desenhada (por exemplo, GL_TRIANGLES para
 * triângulos).
 * - O índice inicial no array de vértices (geralmente 0 para começar do
 * início).
 * - O número de vértices a serem desenhados.
 * previamente definida e os dados de vértice do VBO (vinculados indiretamente
 * via VAO).
 * 
 * EBO é um tipo especial de buffer que armazena índices que OpenGL usa para
 * reaproveitar vertices já criados. assim evitando a duplicação de dados.
 * criamos o EBO com glGenBuffers() e vinculamos ele com
 * glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo) e usamos o glBufferData para
 * enviar os dados dos indices.;
 * 
 * agora a diferença é que ao invés de usar o glDrawArrays para desenhar os
 * vertices, usaremos o glDrawElements que utiliza os indices do EBO para
 * desenhar os vertices reutilizando os dados já existentes.
 * 
 * o glDrawElements recebe como parametros: o tipo de primitiva a ser desenhada, 
 * o número de elementos Ou seja vertices que estão no EBO a serem desenhados, o tipo dos índices e um ponteiro para os índices.
 * Sempre que você chamar glDrawElements , ele usará o EBO atualmente vinculado ao VAO ativo. que usamos a função glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo); para tal já é automaticamente associado ao VAO. ou seja se vincularmos o VAO novamente o EBO continuará vinculado a ele. 
 * 
 * fonte: https://learnopengl.com/Getting-started/Hello-Triangle.
 */
