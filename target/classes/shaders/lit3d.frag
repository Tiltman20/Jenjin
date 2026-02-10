#version 330 core

in vec3 vFragPos;
in vec3 vNormal;

out vec4 FragColor;

struct Light {
    vec3 position;
    vec3 color;
};

#define MAX_LIGHTS 4

uniform Light uLights[MAX_LIGHTS];
uniform int uLightCount;

uniform vec3 uViewPos;
uniform vec3 uColor; // Oberflächenfarbe

void main() {
    vec3 norm = normalize(vNormal);
    vec3 viewDir = normalize(uViewPos - vFragPos);

    vec3 result = vec3(0.0);

    for (int i = 0; i < uLightCount; i++) {
        // Ambient
        vec3 ambient = 0.1 * uLights[i].color * uColor;

        // Diffuse
        vec3 lightDir = normalize(uLights[i].position - vFragPos);
        float diff = max(dot(norm, lightDir), 0.0);
        vec3 diffuse = diff * uLights[i].color * uColor;

        // Specular (Blinn)
        vec3 halfwayDir = normalize(lightDir + viewDir);
        float spec = pow(max(dot(norm, halfwayDir), 0.0), 32.0);
        vec3 specular = uLights[i].color * spec * 0.5;

        result += ambient + diffuse + specular;
    }

    FragColor = vec4(vNormal, 1.0);
}
