package ca.nicho.izzy.scenes;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.GLFrame;
import ca.nicho.izzy.GLUtils;
import ca.nicho.izzy.object.IHill;
import ca.nicho.izzy.object.IObject;
import ca.nicho.izzy.object.ITree3D;
import ca.nicho.izzy.primatives.IVertex;

public class NatureScene extends Scene {

	private IHill hill;
	
	public NatureScene(GLFrame frame) {
		super(frame);
		
		hill = new IHill(100, 1f, 1f, 0.001f);
		createLake();
		createHills();
		hill.findMaxY();
		growTrees();
		hill.colorizeSurface();
		addObject(hill);
		
	}
	
	public void createLake(){
		hill.makeIdent(50, 50, -0.05f, 10);
		hill.makeIdent(45, 45, -0.05f, 8);
	}
	
	public void createHills(){
		hill.makeIdent(50, 50, 0.1f, 6);
		hill.makeIdent(80, 50, 0.1f, 8);
		hill.makeIdent(60, 60, 0.1f, 20);
	}
	
	public void growTrees(){
		
		int count = 0;
		
		while(count < 1000){
			IVertex v = (IVertex)hill.vertexList.values().toArray()[(int)(hill.vertexList.size() * Math.random())];
			if(v.y < 0) continue;
			float norm = (v.y - hill.globalAverageHeight) / hill.globalMaxY;
			boolean choose = norm * norm * 10 < Math.random();
			if(choose){
				count++;
				float r = (float)Math.random();
				float g = (float)Math.random();
				float b = (float)Math.random();
				ITree3D tree = new ITree3D(0.02f, 3, v.x, v.y, v.z, r, g, b);
				this.addObject(tree);
			}
		}
		
	}
	
	public void tick() {}

	public void drawScene(GL2 gl) {
		
		gl.glClearColor(0.53f, 0.81f, 0.92f, 1f);
		
		gl.glColor3f(0, 0, 1);
		GLUtils.fillXYPlane(gl, 1, -0.001f);
		
		for(IObject o : objects){
			gl.glPushMatrix();
			o.drawWire(gl, frame.cameraX, frame.cameraY, frame.cameraZ);
			gl.glPopMatrix();
		}
		
		GLUtils.drawAxis(gl);
		
	}
	
	

	
}
