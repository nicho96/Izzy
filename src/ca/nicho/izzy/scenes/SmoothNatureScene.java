package ca.nicho.izzy.scenes;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.GLFrame;
import ca.nicho.izzy.GLUtils;
import ca.nicho.izzy.object.IObject;
import ca.nicho.izzy.object.ITerrainSmooth;
import ca.nicho.izzy.object.ITree3D;
import ca.nicho.izzy.primatives.IVertex;

public class SmoothNatureScene extends Scene {

	private ITerrainSmooth terrain;
	
	public SmoothNatureScene(GLFrame frame) {
		super(frame);
		
		terrain = new ITerrainSmooth(100, 5f, 5f, 0.001f);
		createHills(5, 20, 5, 20);
		createLakes(2, 5, 5, 10);
		terrain.findMaxY();
		growTrees();
		terrain.smoothen();
		terrain.colorizeSurface();
		addObject(terrain);
		
	}
	
	public void createLakes(int min, int delta, int minSpan, int spanRange){
		
		int range = (int)(Math.random() * delta) + min;

		for(int i = min; i <= range; i++){
			
			int radius = (int)(Math.random() * spanRange) + minSpan;
			int x = (int)(Math.random() * terrain.grid.length);
			int z = (int)(Math.random() * terrain.grid[0].length);
			float height = -(float)Math.random() / 3 - 0.01f;	
			
			terrain.makeIdent(x - radius / 2, z, height / 4, radius / 2);
			terrain.makeIdent(x + radius / 2, z, height / 4, radius / 2);
			terrain.makeIdent(x, z - radius / 2, height / 4, radius / 2);
			terrain.makeIdent(x, z + radius / 2, height / 4, radius / 2);
			terrain.makeIdent(x, z, height, radius);
						
		}

	}
	
	public void createHills(int min, int delta, int minSpan, int spanRange){
		
		int range = (int)(Math.random() * delta) + min;

		for(int i = min; i <= range; i++){
			
			int radius = (int)(Math.random() * spanRange) + minSpan;
			int x = (int)(Math.random() * terrain.grid.length);
			int z = (int)(Math.random() * terrain.grid[0].length);
			float height = (float)Math.random() / 2 + 0.01f;	
			
			terrain.makeIdent(x - radius / 2, z, height / 4, radius / 2);
			terrain.makeIdent(x + radius / 2, z, height / 4, radius / 2);
			terrain.makeIdent(x, z - radius / 2, height / 4, radius /2);
			terrain.makeIdent(x, z + radius / 2, height / 4, radius /2);
			terrain.makeIdent(x, z, height, radius);
						
		}

	}
	
	public void growTrees(){
		
		int count = 0;
		
		while(count < 200){
			IVertex v = (IVertex)terrain.vertexList.values().toArray()[(int)(terrain.vertexList.size() * Math.random())];
			if(v.y < 0) continue;
			float norm = (v.y - terrain.globalAverageHeight) / terrain.globalMaxY;
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
