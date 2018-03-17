package ca.nicho.izzy;

import com.jogamp.opengl.GL2;

public final class GLUtils {

	public static void fillXYPlane(GL2 gl, float d){
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(0, 0, d);
		gl.glVertex3f(d, 0, d);
		gl.glVertex3f(d, 0, 0);
		gl.glEnd();
	}
	
	public static float distance(float x1, float y1, float z1, float x2, float y2, float z2){
		return (float)Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - 1), 2));
	}
	
}
