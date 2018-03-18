package ca.nicho.izzy;

import com.jogamp.opengl.GL2;

public final class GLUtils {

	public static void fillXYPlane(GL2 gl, float d, float y){
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glVertex3f(0, y, 0);
		gl.glVertex3f(0, y, d);
		gl.glVertex3f(d, y, d);
		gl.glVertex3f(d, y, 0);
		gl.glEnd();
	}
	
	public static float distance(float x1, float y1, float z1, float x2, float y2, float z2){
		return (float)Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - 1), 2));
	}
	
	public static void drawAxis(GL2 gl){
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3f(1, 0, 0); //RED - X
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(3f, 0, 0);

		gl.glColor3f(0, 1, 0); //GREEN - Y
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(0, 3f, 0);

		gl.glColor3f(0, 0, 1); // BLUE - Z
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(0, 0, 3f);
		gl.glEnd();
	}
	
}
