#version 420

layout(location=1) in vec2 v;
layout(location=2) in vec4 c;
layout(location=3) in vec2 u;

out vec4 color;
out vec2 tex_coords;

//layout(location =1) uniform float scale;


void main() {
	gl_Position = vec4(v,1,1);
	color = c;
	tex_coords = u;
}