#version 330 core

precision mediump float;

uniform sampler2D textureSampler[16];

uniform float time;

in vec2 passTexCoord;
in vec4 passColor;
in float passTextureIndex;

out vec4 outColor;

void main() {
	vec2 uv = passTexCoord + time;
	
	int textureIndex = int(passTextureIndex);
	vec4 textureDiffuse = texture(textureSampler[textureIndex], vec2(uv.x, passTexCoord.y));
	
	outColor = passColor * textureDiffuse; 
}