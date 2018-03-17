package ca.nicho.izzy.object;

import org.json.JSONObject;

import com.jogamp.opengl.GL2;

public class ICircle extends IObject {

	public int triangles;
	public float radius, x, y;
	
	public ICircle(float radius, float x, float y, int triangles) {
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.triangles = triangles;
	}
	
	public void generateCircle(){
		
	}

	@Override
	public void drawWire(GL2 gl, float cx, float cy, float cz) {
		float dx = radius;
		float dy = 0;
		float rad = 0;
		
		for(int i = 0; i <= triangles; i++){
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex3f(x, y, 0);
			gl.glVertex3f(dx, dy, 0);
			rad += 2 * Math.PI / triangles;
			float dx2 = (float) Math.sin(rad) * radius;
			float dy2 = (float) Math.cos(rad) * radius;
			gl.glVertex3f(dx, dy, 0);
			gl.glVertex3f(dx2, dy2, 0);
			dx = dx2;
			dy = dy2;
			gl.glEnd();
		}
		
	}

}
