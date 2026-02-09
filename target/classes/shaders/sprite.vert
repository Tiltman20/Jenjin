#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aUV;

uniform mat4 uProjection;
uniform vec2 uPosition;

out vec2 vUV;

void main() {
    vUV = aUV;
    vec4 worldPos = vec4(aPos.xy + uPosition, 0.0, 1.0);
    gl_Position = uProjection * worldPos;
}
