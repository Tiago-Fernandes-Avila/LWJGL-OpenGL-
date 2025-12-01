#version 330 core 
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec2 aColor;
uniform float move;
out vec4 vertexColor;   



void main() {
    gl_Position = vec4(aPos.x + move, -aPos.y , aPos.z , 1.0);
    vertexColor = vec4(aPos.x + move, -aPos.y , aPos.z , 1.0);
}