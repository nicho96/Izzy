package ca.nicho.izzy.object;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.primatives.IEdge;
import ca.nicho.izzy.primatives.ITriangle;
import ca.nicho.izzy.primatives.IVector;
import ca.nicho.izzy.primatives.IVertex;

public class IPyrFractal extends IObject {

	private int iterations;
	private float ox;
	private float oy;
	private float oz;
	
	public ArrayList<IVertex> vertices = new ArrayList<IVertex>();
	public ArrayList<IEdge> edges = new ArrayList<IEdge>();
	public ArrayList<ITriangle> faces = new ArrayList<ITriangle>();

	
	public IPyrFractal(float radius, float x, float y, float z, int iterations){
		this.iterations = iterations;
		this.ox = x;
		this.oy = y;
		this.oz = z;
		
		recursive(new IPyramid(x, y, z, radius, 0, 0), iterations);
		
		for(IVertex v : vertices){
			vertexList.put(vertexList.size() + "v", v);
		}
		
		for(IEdge e : edges){
			edgeList.put(edgeList.size() + "e", e);
		}
		
		for(ITriangle f : faces){
			faceList.put(faceList.size() + "f", f);
		}
		
	}
	
	public void recursive(IPyramid p, int i){
		vertices.addAll(p.vertexList.values());
		edges.addAll(p.edgeList.values());
		faces.addAll(p.faceList.values());
			
		if(i == 0) return;
		
		for(ITriangle t : p.faceList.values()){
						
			IVertex o = new IVertex(p.ox, p.oy, p.oz, null);
			//System.out.println(o + " " + t.getPointDist(o) + " " + t);
			
			IVector vec = t.getNormalVector();
			float x = vec.x + p.ox;
			float y = vec.y + p.oy;
			float z = vec.z + p.oz;
			
			IPyramid p2 = new IPyramid(x, y, z, p.radius / 2, p.a + 120, p.b + 120);
			
			//float a = (float)Math.tan(vec.x / vec.z) * 57.2958f;
			//float b = (float)Math.asin(vec.y) * 57.2958f;
			//System.out.println(a + " " + b);
			
			recursive(p2, i - 1);
		}
				
	}
	
	@Override
	public void drawWire(GL2 gl, float cx, float cy, float cz) {
		for(ITriangle t : faceList.values()){
			gl.glColor3f(0, 0, 0);
			IVertex v1 = t.v1;
			IVertex v2 = t.v2;
			IVertex v3 = t.v3;
			gl.glBegin(GL2.GL_TRIANGLE_FAN);
			gl.glVertex3f(v1.X(), v1.Y(), v1.Z());
			gl.glVertex3f(v2.X(), v2.Y(), v2.Z());
			gl.glVertex3f(v3.X(), v3.Y(), v3.Z());
			gl.glEnd();
		}
		
		for(IEdge e : edgeList.values()){
			gl.glColor3f(1, 1, 1);
			gl.glBegin(GL2.GL_LINES);
			gl.glVertex3f(e.v1.X(), e.v1.Y(), e.v1.Z());
			gl.glVertex3f(e.v2.X(), e.v2.Y(), e.v2.Z());
			gl.glEnd();
		}	
	}
	
	@Override
	public IVertex getOrigin(){
		return new IVertex(ox, oy, oz, null);
	}
	
}
