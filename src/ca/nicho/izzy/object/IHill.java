package ca.nicho.izzy.object;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.primatives.IEdge;
import ca.nicho.izzy.primatives.ITriangle;
import ca.nicho.izzy.primatives.IVertex;

public class IHill extends IObject {
	
	public float alt;
	public float lat;
	
	public float globalMaxY;
	public float globalAverageHeight;
	
	public IVertex[][] grid;
	
	public IHill(int dim, float lat, float lon, float y){
		grid = new IVertex[dim][dim];
		this.lat = lat;
		this.alt = y;
				
		for(int x = 0; x < dim; x++){
			for(int z = 0; z < dim; z++){
				String l = vertexList.size() + "v";
				grid[x][z] = new IVertex((float)x / lat / dim, y, z / lon / dim, l);
				this.vertexList.put(l, grid[x][z]);
			}
		}
		
		for(int x = 0; x < dim; x++){
			for(int z = 0; z < dim; z++){
				
				IVertex v1 = null;
				IVertex v2 = null;
				IVertex v3 = null;
				IVertex v4 = null;
				
				IEdge e1 = null;
				IEdge e2 = null;
				IEdge e3 = null;
				
				v1 = grid[x][z];
				int xr = x + 1;
				int zd = z + 1;
				
				if(xr < dim){
					String l1 = edgeList.size() + "e";
					v2 = grid[xr][z];
					e1 = new IEdge(v1, v2, l1);
					this.edgeList.put(l1, e1);
					if(zd < dim){
						String l2 = edgeList.size() + "e";
						v3 = grid[xr][zd];
						e2 = new IEdge(v1, v3, l2);
						this.edgeList.put(l2, e2);	
					}
				}
				
				if(zd < dim){
					String l3 = edgeList.size() + "e";
					v4 = grid[x][zd];
					e3 = new IEdge(v1, v4, l3);
					this.edgeList.put(l3, e3);
				}
									
				if(v1 != null && v2 != null && v3 != null){
					String l = faceList.size() + "f";
					ITriangle face = new ITriangle(v1, v2, v3);
					this.faceList.put(l, face);
				}
				
				if(v1 != null && v3 != null && v4 != null){
					String l = faceList.size() + "f";
					ITriangle face = new ITriangle(v1, v3, v4);
					this.faceList.put(l, face);
				}
				
			}
		}			
		step(dim);
	}
	
	private void step(int size){
		int half = size / 2; //Center point
		
		if(half < 1) return;
		
		float range = lat / grid.length;
		
		for(int y = half; y < grid.length; y += size) {
	    	if(y + size >= grid.length) continue;
		    for (int x = half; x < grid.length; x += size) {
		    	if(x + size >= grid.length) continue;
		    	square(x, y, half, (float)Math.random() * range);
		    	diamond(x, y, half, (float)Math.random() * range);
		    }
		}
		step(half);
	}
	
	private void diamond(int x, int y, int size, float r){		
		float avg = (grid[x][y - size].Y() + grid[x + size][y].Y() + grid[x][y + size].Y() + grid[x - size][y].Y()) / 4;
		grid[x][y].setPoints(grid[x][y].X(), avg + r, grid[x][y].Z());
	}
	
	private void square(int x, int y, int size, float r){
		float avg = (grid[x - size][y - size].Y() + grid[x + size][y - size].Y() + grid[x - size][y + size].Y() + grid[x + size][y + size].Y()) / 4;
		grid[x][y].setPoints(grid[x][y].X(), avg + r, grid[x][y].Z());
	}
	
	public void makeIdent(int x, int z, float dy, int radius){
		
		for(int i = 0; i <= 2 * radius; i++){
			for(int o = 0; o <= 2 * radius; o++){
				float value = (radius - Math.abs(Math.min(i, o) - radius)) / (float)radius;
				int j = x - radius + i;
				int k = z - radius + o;
				if(j < 0 || k < 0 || j >= grid.length || k >= grid[0].length) continue;
				IVertex v = grid[j][k];
				float r = dy * (0.5f +  0.5f * (float)Math.random()) * value;
				v.setPoints(v.X(), v.Y() + r, v.Z());
			}
		}
	
	}
	
	public void findMaxY(){
		globalMaxY = this.vertexList.values().iterator().next().Y();
		int count = 0;
		for(IVertex v : this.vertexList.values()){
			if(v.Y() > globalMaxY){
				globalMaxY = v.Y();
			}
			if(v.Y() > 0){
				count ++;
				globalAverageHeight += v.Y();
			}
		}
		if(this.vertexList.size() > 0){
			globalAverageHeight /= count;
		}
	}
	
	public void colorizeSurface(){
						
		for(ITriangle face : this.faceList.values()){
			float maxY = (float)Math.max(face.v1.Y(), Math.max(face.v2.Y(), face.v3.Y()));
			float minY = (float)Math.min(face.v1.Y(), Math.min(face.v2.Y(), face.v3.Y()));
			if(minY < 0){
				face.r = 0.88f;
				face.g = 0.66f;
				face.b = 0.37f;
			}else if(minY < globalAverageHeight) {
				face.r = (float)Math.random() * 0.1f;
				face.g = (float)Math.random() * 0.05f + 0.4f;
				face.b = (float)Math.random() * 0.1f;
			}else{
				float gray = 0.6f + (maxY / globalMaxY) * 0.4f;
				face.r = gray;
				face.g = gray;
				face.b = gray;
			}
		}
	}
	
	@Override
	public void drawWire(GL2 gl, float cx, float cy, float cz) {		
		for(ITriangle t : faceList.values()){
			gl.glBegin(GL2.GL_TRIANGLE_FAN);
			IVertex v1 = t.v1;
			IVertex v2 = t.v2;
			IVertex v3 = t.v3;

			gl.glColor3f(t.r, t.g, t.b);
			
			gl.glVertex3f(v1.X(), v1.Y(), v1.Z());
			gl.glVertex3f(v2.X(), v2.Y(), v2.Z());
			gl.glVertex3f(v3.X(), v3.Y(), v3.Z());
			gl.glEnd();
		}
		
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3f(0, 0, 0);
		for(IEdge e : edgeList.values()){
			gl.glVertex3f(e.v1.X(), e.v1.Y(), e.v1.Z());
			gl.glVertex3f(e.v2.X(), e.v2.Y(), e.v2.Z());
		}
		gl.glEnd();
	}
	
}
