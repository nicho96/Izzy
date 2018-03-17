package ca.nicho.izzy.object;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.spherical.twod.Edge;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.GLUtils;
import ca.nicho.izzy.primatives.IEdge;
import ca.nicho.izzy.primatives.IVertex;

public class ITree3D extends IObject {

	private float ox;
	private float oy;
	private float oz;
	
	public float r = 1;
	public float g = 1;
	public float b = 1;
	
	protected float min_l = 0.2f;
	protected float ran_l = 0.5f;
	protected float d_a_x = 50;
	protected float d_a_z = 180;
	protected float dy_min = 0.5f;
	
	private int interations;
	
	protected Branch root;

	public ITree3D(float height, int iterations, float ox, float oy, float oz, float r, float g, float b){
		this(height, iterations, ox, oy, oz, 0.2f, 0.4f, 90, 90, 0.4f, r, g, b);
	}
	
	public ITree3D(float height, int iterations, float ox, float oy, float oz, float min_l, float ran_l, float d_a_x, float d_a_z, float dy_min, float r, float g, float b){
		float startAngleX = ((float)Math.random() * 40 - 20);
		float startAngleZ = ((float)Math.random() * 40 - 20);
		root = new Branch(height, 0, startAngleX, startAngleZ, iterations);
		this.ox = ox;
		this.oy = oy;
		this.oz = oz;
		this.min_l = min_l;
		this.ran_l = ran_l;
		this.d_a_x = d_a_x;
		this.d_a_z = d_a_z;
		this.dy_min = dy_min;
		this.interations = iterations;
		this.r = r;
		this.g = g;
		this.b = b;
		recursive(root, iterations - 1);
		prepareEdgelist();
	}
	
	private void recursive(Branch b, int i){
		if(i == 0) return;
		
		float dy = b.length / 16; //Estimated 16 branch tree
		float h = (dy_min + (float)Math.random()) * dy;
		while(h < b.length){
			float l = (((float)Math.random() * ran_l) + min_l) * b.length;
			float dax = (float)((Math.random() * d_a_x) - d_a_x / 2);
			dax = (dax + b.angleXZ) % 360;
			float daz = (float)((Math.random() * d_a_z) - d_a_z / 2);
			daz = (daz + b.angleXY) % 360;
			Branch nb = new Branch(l, h, dax, daz, i);
						
			b.branches.add(nb);
			recursive(nb, i - 1);
			h += (dy_min + (float)Math.random()) * dy; //50%-150% the size of dy
		}
	}
	
	private void prepareEdgelist(){
		drawBranchesRec(ox, oy, oz, root);
	}
	
	private void drawBranchesRec(float x1, float y1, float z1, Branch b){
				
		float beta = b.angleXY * 0.0174533f;
		float alpha = b.angleXZ * 0.0174533f;
		float x2 = (float)(Math.sin(alpha) * Math.cos(beta)) * b.length + x1;
		float y2 = (float)(Math.cos(alpha) * Math.cos(beta)) * b.length + y1;
		float z2 = (float)(Math.sin(beta)) * b.length + z1;
		
		String l1 = "v" + vertexList.size();
		IVertex v1 = new IVertex(x1, y1, z1, l1);
		vertexList.put(l1, v1);
		
		String l2 = "v" + vertexList.size();
		IVertex v2 = new IVertex(x2, y2, z2, l2);
		vertexList.put(l2, v2);
		
		String l3 = "e" + edgeList.size();
		IEdge e = new IEdge(v1, v2, l3);
		e.r = (1 - b.iteration / (float)this.interations) * this.r;
		e.g = (1 - b.iteration / (float)this.interations) * this.g;
		e.b = (1 - b.iteration / (float)this.interations) * this.b;
		e.width = this.interations - b.iteration;
		edgeList.put(l3, e);
		b.edge = e;
		
		for(Branch c : b.branches){
			float x3 = (float)(Math.sin(alpha)*Math.cos(beta)) * c.base + x1;
			float y3 = (float)(Math.cos(alpha)*Math.cos(beta)) * c.base + y1;
			float z3 = (float)(Math.sin(beta)) * c.base + z1;
			drawBranchesRec(x3, y3, z3, c);
		}
		
	}
	
	@Override
	public void drawWire(GL2 gl, float cx, float cy, float cz) {
		gl.glBegin(GL2.GL_LINES);
		//drawFromEdgeList(gl);
		
		int i = (int)(this.interations * 4 * root.length / GLUtils.distance(ox, oy, oz, cx, cy, cz)) + 1;
		drawRec(gl, root, i);
		
		gl.glEnd();
	}
	
	public void drawFromEdgeList(GL2 gl){
		for(IEdge e : edgeList.values()){
			if(e.r != -1 && e.g != -1 && e.b != -1){
				gl.glColor3f(e.r, e.g, e.b);
			}	
			gl.glVertex3f(e.i1.X(), e.i1.Y(), e.i1.Z());
			gl.glVertex3f(e.i2.X(), e.i2.Y(), e.i2.Z());
		}
	}
	
	public void drawRec(GL2 gl, Branch b, int i){
		if(i == 0) return; 
		
		IEdge e = b.edge;
		
		if(e.r != -1 && e.g != -1 && e.b != -1){
			gl.glColor3f(e.r, e.g, e.b);
		}	
		
		gl.glVertex3f(e.i1.X(), e.i1.Y(), e.i1.Z());
		gl.glVertex3f(e.i2.X(), e.i2.Y(), e.i2.Z());
		
		for(Branch c : b.branches) {
			drawRec(gl, c, i - 1);
		}
	}
	
	@Override
	public IVertex getOrigin(){
		return new IVertex(ox, oy, oz, null);
	}
	
	
	protected class Branch {
		
		public ArrayList<Branch> branches = new ArrayList<Branch>();
		
		public float length; // The length of this branch
		public float base; // The distance from the base of the parent branch
		public float angleXY; // The angle coming off of the previous branch
		public float angleXZ; // The angle coming off of the previous branch
		public int iteration;
		public IEdge edge;
		
		public Branch(float length, float base, float angleXY, float angleXZ, int iteration){
			this.length = length;
			this.base = base;
			this.angleXY = angleXY;
			this.angleXZ = angleXZ;
			this.iteration = iteration;
		}
		
	}
	
	
}
