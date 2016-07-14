#version 430

layout(location=1) uniform mat4 viewMatrix;
layout(location=2) uniform mat4 viewProjectionMatrix;

layout(location=1) in vec3 v;
layout(location=2) in vec4 c;
layout(location=3) in vec2 u;

out vec4 color;
out vec2 tex_coords;


void main() {
	gl_Position = viewProjectionMatrix*vec4(v,1);
	color = c;
	tex_coords = u;
}