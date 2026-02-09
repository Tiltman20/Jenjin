#version 330 core

layout (location = 0) in vec3 aPos;

uniform mat4 uProjection;
uniform vec2 uPosition;

void main(){
    vec4 worldPos = vec4(aPos.xy + uPosition, 0.0f, 1.0f);
    gl_Position = uProjection * worldPos;
}