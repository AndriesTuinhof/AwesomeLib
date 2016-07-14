#version 420

in vec4 color;
in vec2 tex_coords;

uniform sampler2D tex;

out vec4 fragment;

void main() {
	fragment = color*texture(tex, tex_coords);
}