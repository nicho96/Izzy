package ca.nicho.izzy.scenes;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.GLFrame;
import ca.nicho.izzy.GLUtils;
import ca.nicho.izzy.object.IObject;
import ca.nicho.izzy.object.IShitstorm;

public class TestScene extends Scene {

	public TestScene(GLFrame frame) {
		super(frame);
	
		this.addObject(new IShitstorm());
		
	}

	@Override
	public void drawScene(GL2 gl) {
		for(IObject o : objects){
			gl.glPushMatrix();
			gl.glRotatef(1, 0, 1, 0);
			o.drawWire(gl, frame.cameraX, frame.cameraY, frame.cameraZ);
			gl.glPopMatrix();
		}
		
		GLUtils.drawAxis(gl);
		
	}

	@Override
	public void tick() {
		
	}

}
