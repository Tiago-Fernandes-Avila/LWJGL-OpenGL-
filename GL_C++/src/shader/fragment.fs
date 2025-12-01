
// Fragment shader:
// ================
#version 330 core
out vec4 FragColor;
// in vec3 ourColor;
in vec3 ourColor;
in vec2 ourTexCoord;
uniform sampler2D ourTexture1;
uniform sampler2D ourTexture2;
uniform float value;
void main()
{
    
    FragColor = mix(texture(ourTexture1, ourTexCoord) * vec4(value,value,value, 1.0), texture(ourTexture2, vec2(ourTexCoord.x * -1, ourTexCoord.y))  * vec4(value, value ,value, 1.0), 0.2) ; 
    

}
