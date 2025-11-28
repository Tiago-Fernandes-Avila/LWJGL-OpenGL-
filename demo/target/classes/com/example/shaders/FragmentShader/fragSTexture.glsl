#version 330 core

out vec4 FragColor;
in vec2 TexCoord;
in vec3 ourColor;

uniform sampler2D ourTexture;
uniform sampler2D ourTexture1;

void main()
{
    
    //FragColor = texture2D(ourTexture, TexCoord) * vec4(ourColor, 1.0); textura colorida
    FragColor = mix(texture2D(ourTexture, TexCoord), texture2D(ourTexture1, TexCoord),0.8);
}