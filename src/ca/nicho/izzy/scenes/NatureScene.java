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
		
		hill = new IHill(100, 5f, 5f, 0.001f);
		createHills(5, 20, 5, 20);
		createLakes(2, 5, 5, 10);
		hill.findMaxY();
		growTrees();
		hill.colorizeSurface();
		addObject(hill);
		
	}
	
	public void createLakes(int min, int delta, int minSpan, int spanRange){
		
		int range = (int)(Math.random() * delta) + min;

		for(int i = min; i <= range; i++){
			
			int radius = (int)(Math.random() * spanRange) + minSpan;
			int x = (int)(Math.random() * hill.grid.length);
			int z = (int)(Math.random() * hill.grid[0].length);
			float height = -(float)Math.random() / 3 - 0.01f;	
			
			hill.makeIdent(x - radius / 2, z, height / 4, radius / 2);
			hill.makeIdent(x + radius / 2, z, height / 4, radius / 2);
			hill.makeIdent(x, z - radius / 2, height / 4, radius / 2);
			hill.makeIdent(x, z + radius / 2, height / 4, radius / 2);
			hill.makeIdent(x, z, height, radius);
						
		}

	}
	
	public void createHills(int min, int delta, int minSpan, int spanRange){
		
		int range = (int)(Math.random() * delta) + min;

		for(int i = min; i <= range; i++){
			
			int radius = (int)(Math.random() * spanRange) + minSpan;
			int x = (int)(Math.random() * hill.grid.length);
			int z = (int)(Math.random() * hill.grid[0].length);
			float height = (float)Math.random() / 2 + 0.01f;	
			
			hill.makeIdent(x - radius / 2, z, height / 4, radius / 2);
			hill.makeIdent(x + radius / 2, z, height / 4, radius / 2);
			hill.makeIdent(x, z - radius / 2, height / 4, radius /2);
			hill.makeIdent(x, z + radius / 2, height / 4, radius /2);
			hill.makeIdent(x, z, height, radius);
						
		}

	}
	
	public void growTrees(){
		
		int count = 0;
		
		while(count < 200){
			IVertex v = (IVertex)hill.vertexList.values().toArray()[(int)(hill.vertexList.size() * Math.random())];
			if(v.y < 0) continue;
			float norm = (v.y - hill.globalAverageHeight) / hill.globalMaxY;
			boolean choose = norm * norm * 10 < Math.random();
			if(choose){
				count++;
				float r = (float)Math.random() * 0.2f;
				float g = (float)Math.random() * 0.4f + 0.2f;
				float b = (float)Math.random() * 0.2f;
				ITree3D tree = new ITree3D(0.2f, 3, v.x, v.y, v.z, r, g, b);
				this.addObject(tree);
			}
		}
		
	}
	
	public void tick() {
		
		
		
	}

	public void drawScene(GL2 gl) {
		
		gl.glClearColor(0.53f, 0.81f, 0.92f, 1f);
		
		gl.glColor3f(0, 0, 1);
		GLUtils.fillXYPlane(gl, 5, -0.005f);
		
		for(IObject o : objects){
			gl.glPushMatrix();
			o.drawWire(gl, frame.cameraX, frame.cameraY, frame.cameraZ);
			gl.glPopMatrix();
		}
		
		GLUtils.drawAxis(gl);
		
	}
	
	

	
}
