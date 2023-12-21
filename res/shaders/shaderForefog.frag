#version 150 core

precision mediump float;

uniform sampler2D lightSampler;

uniform vec2 screenDimensions;
uniform vec2 cameraPosition;
uniform float cameraZoom;
uniform vec2 sunPosition;
uniform int octaves = 4;
uniform float fadeAmount = 1.;
uniform float timeAcc = 0.;
uniform vec3 fogColor = vec3(.8, .3, .35);
uniform vec2 windDir = vec2(1., 0.);

in vec2 passTexCoord;
out vec4 outColor;

float rand(vec2 uv) {
	return fract(sin(dot(uv.xy, vec2(12.9898,78.233))) * 43758.5453123);
}

float noise(vec2 uv) {
  vec2 i = floor(uv);
  vec2 f = fract(uv);
  
  float a = rand(i + vec2(0., 0.));
  float b = rand(i + vec2(1., 0.));
  float c = rand(i + vec2(0., 1.));
  float d = rand(i + vec2(1., 1.));
  
  vec2 cubic = f * f * (3. - 2. * f);
  return mix(a, b, cubic.x) + (c - a) * cubic.y * (1. - cubic.x) + (d - b) * cubic.x * cubic.y;
}


float fBm(vec2 uv) {
  float value = 0.;
  float s = .6;
  
  for(int i = 0; i < octaves; i++) {
    value += noise(uv) * s;
    uv *= 2.;
    s *= .5;
  }
  
  return value;
}

void main() {
	vec2 uv = (gl_FragCoord.xy - .5 * screenDimensions.xy) / screenDimensions.y;
	uv *= cameraZoom;
	uv += cameraPosition * .1;

	vec4 colorLight     = texture(lightSampler, gl_FragCoord.xy / screenDimensions.xy);

	float zoomFactor = 15.; 
	vec2 motion = vec2(fBm((uv + timeAcc) * zoomFactor));
	float finalColor = fBm(uv + motion);
		
	outColor = vec4(fogColor*(colorLight.rgb*.5+.4) + colorLight.rgb, finalColor * fadeAmount);
}