#ifndef TEXTURE_H
#define TEXTURE_H
#define STB_IMAGE_IMPLEMENTATION
#include "stb_image.h"
#include "iostream"
#include "glad/glad.h"

class Texture
{
private:
    GLuint texture;

public:
    Texture(const char *texturePath, unsigned int textureUnitPos)
    {
        glGenTextures(1, &texture);
        glActiveTexture(GL_TEXTURE0 + textureUnitPos);

        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);                   // wrap no eixo x
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);                   // wrap no eixo y
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR); // filtragem de mipmap
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);               // filtragem de textura

        int width, height, channels;
        stbi_set_flip_vertically_on_load(1);
        unsigned char *data = stbi_load(texturePath, &width, &height, &channels, 0);
        if (data)
        {
            if (channels == 3)
            {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
            }
            else if(channels == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0,GL_RGBA, GL_UNSIGNED_BYTE, data);
            }
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        else
        {
            std::cout << "ERROR fail to load texture" << std::endl;
        }

        stbi_image_free(data);
    }

    void use()
    {
        glBindTexture(GL_TEXTURE_2D, texture);
    }
};

#endif