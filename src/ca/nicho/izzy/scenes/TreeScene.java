package ca.nicho.izzy.scenes;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.GLFrame;
import ca.nicho.izzy.GLUtils;
import ca.nicho.izzy.object.IObject;
import ca.nicho.izzy.object.ITree2D;
import ca.nicho.izzy.object.ITree3D;

public class TreeScene extends Scene {

	public TreeScene(GLFrame frame) {
		super(frame);
		
		ITree3D tree1 = new ITree3D(1, 5, 0, 0, 0, 0.1f, 0.4f, 90, 90, 0.4f, 0, 0.8f, 0);
		this.addObject(tree1);
		
		//float height, int iterations, float ox, float oy, float oz, float min_l, float ran_l, float d_a, float dy_min
		
		ITree2D tree2 = new ITree2D(1, 5, 2f, 0, 0, 0.1f, 0.2f, 180f, 0.1f);
		this.addObject(tree2);
		
	}

	@Override
	public void drawScene(GL2 gl) {
		gl.glClearColor(0, 0, 0, 1);
		
		gl.glColor3f(0, 0, 1);
		
		for(IObject o : objects){
			gl.glPushMatrix();
			o.drawWire(gl, frame.cameraX, frame.cameraY, frame.cameraZ);
			gl.glPopMatrix();
		}
		
		GLUtils.drawAxis(gl);
	}

	@Override
	public void tick() {
		
	}

}
