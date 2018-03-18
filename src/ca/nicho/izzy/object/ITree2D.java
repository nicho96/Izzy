package ca.nicho.izzy.object;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.primatives.IEdge;
import ca.nicho.izzy.primatives.IVertex;

public class ITree2D extends IObject {
	
	public Branch root;
	
	private float ox;
	private float oy;
	private float oz;
	
	public float r = 1;
	public float g = 1;
	public float b = 1;
	
	protected float min_l = 0.2f;
	protected float ran_l = 0.5f;
	protected float d_a = 50;
	protected float dy_min = 0.5f;
	
	public ITree2D(float height, int iterations, float ox, float oy, float oz, float min_l, float ran_l, float d_a, float dy_min){
		float startAngle = ((float)Math.random() * 20 - 10);
		Branch b = new Branch(height, 0, startAngle);
		this.root = b;
		this.ox = ox;
		this.oy = oy;
		this.oz = oz;
		this.min_l = min_l;
		this.ran_l = ran_l;
		this.d_a = d_a;
		this.dy_min = dy_min;
		recursive(b, iterations - 1);
		prepareEdgelist();
	}
	
	private void recursive(Branch b, int i){
		if(i == 0) return;
		
		float dy = b.length / 16; //Estimated 8 branch tree
		float h = (dy_min + (float)Math.random()) * dy;
		while(h < b.length){
			float l = (((float)Math.random() * ran_l) + min_l) * b.length; // 25-75% the previous branch length
			float da = (float)((Math.random() * d_a) - d_a / 2); //Angle +- 10deg on the last branch
			da = (da + b.angle) % 360; // Normalize the angle 0 - 360deg
			Branch nb = new Branch(l, h, da);
			b.branches.add(nb);
			recursive(nb, i - 1);
			h += (dy_min + (float)Math.random()) * dy; //50%-150% the size of dy
		}
	}
	
	private void prepareEdgelist(){
		drawBranchesRec(ox, oy, oz, root);
	}
	
	private void drawBranchesRec(float x1, float y1, float z1, Branch b){
		
		float rad = b.angle * 0.0174533f;
		float x2 = (float)Math.sin(rad) * b.length + x1;
		float y2 = (float)Math.cos(rad) * b.length + y1;
		float z2 = z1;
		
		String l1 = "v" + vertexList.size();
		IVertex v1 = new IVertex(x1, y1, z1, l1);
		vertexList.put(l1, v1);
		
		String l2 = "v" + vertexList.size();
		IVertex v2 = new IVertex(x2, y2, z2, l2);
		vertexList.put(l2, v2);
		
		String l3 = "e" + edgeList.size();
		edgeList.put(l3, new IEdge(v1, v2, l3));
		
		for(Branch c : b.branches){
			float x3 = (float)Math.sin(rad) * c.base + x1;
			float y3 = (float)Math.cos(rad) * c.base + y1;
			float z3 = 0 + z1;
			drawBranchesRec(x3, y3, z3, c);
		}
		
	}
	
	@Override
	public void drawWire(GL2 gl, float cx, float cy, float cz) {
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3d(r, g, b);
		for(IEdge e : edgeList.values()){
			gl.glVertex3f(e.v1.X(), e.v1.Y(), e.v1.Z());
			gl.glVertex3f(e.v2.X(), e.v2.Y(), e.v2.Z());
		}
		gl.glEnd();		
	}
	
	@Override
	public IVertex getOrigin(){
		return vertexList.get("v0");
	}
	
	protected class Branch {
		
		public ArrayList<Branch> branches = new ArrayList<Branch>();
		
		public float length; // The length of this branch
		public float base; // The distance from the base of the parent branch
		public float angle; // The angle coming off of the previous branch
		
		public Branch(float length, float base, float angle){
			this.length = length;
			this.base = base;
			this.angle = angle;
		}
		
	}

}
