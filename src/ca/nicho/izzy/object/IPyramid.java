package ca.nicho.izzy.object;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.primatives.IEdge;
import ca.nicho.izzy.primatives.ITriangle;
import ca.nicho.izzy.primatives.IVertex;

public class IPyramid extends IObject {

	
	public float radius;
	public float ox;
	public float oy;
	public float oz;
	
	public float a;
	public float b;
	
	public RealMatrix rotate;
	public RealMatrix transform1;
	public RealMatrix transform2;
	
	public IPyramid(float x, float y, float z, float radius, float a, float b){
				
		double[][] tr1 = {
			{1, 0, 0, -x},
			{0, 1, 0, -y},
			{0, 0, 1, -z},
			{0, 0, 0, 1}
		};
		
		double[][] tr2 = {
			{1, 0, 0, x},
			{0, 1, 0, y},
			{0, 0, 1, z},
			{0, 0, 0, 1}
		};
		
		double[][] a1 = {
				{Math.cos(a), -Math.sin(a), 0, 0},
				{Math.sin(a), Math.cos(a), 0, 0},
				{0, 0, 1, 0},
				{0, 0, 0, 1}
		};
		
		double[][] a2 = {
				{Math.cos(b), 0, Math.sin(b), 0},
				{0, 1, 0, 0},
				{-Math.sin(b), 0, Math.cos(b), 0},
				{0, 0, 0, 1}
		};
		
		transform1 = MatrixUtils.createRealMatrix(tr1);
		transform2 = MatrixUtils.createRealMatrix(tr2);
		rotate = MatrixUtils.createRealMatrix(a1).multiply(MatrixUtils.createRealMatrix(a2));
		
		this.radius = radius;
		this.ox = x;
		this.oy = y;
		this.oz = z;
		this.a = a;
		this.b = b;
		
		float alpha = 0;
		float beta = 90 * 0.0174533f;
		IVertex top = new IVertex(
			(float)(Math.sin(alpha) * Math.cos(beta)) * radius + x,
			(float)(Math.sin(beta)) * radius + y,
			(float)(Math.cos(alpha) * Math.cos(beta)) * radius + z,
			vertexList.size() + "v"
		);
		vertexList.put(top.label, top);
				
		beta -= 120 * 0.0174533f;
		IVertex v1 = new IVertex(
			(float)(Math.sin(alpha) * Math.cos(beta)) * radius + x,
			(float)(Math.sin(beta)) * radius + y,
			(float)(Math.cos(alpha) * Math.cos(beta)) * radius + z,
			vertexList.size() + "v"
		);
		vertexList.put(v1.label, v1);
		
		alpha += 120 * 0.0174533f;
		IVertex v2 = new IVertex(
			(float)(Math.sin(alpha) * Math.cos(beta)) * radius + x,
			(float)(Math.sin(beta)) * radius + y,
			(float)(Math.cos(alpha) * Math.cos(beta)) * radius + z,
			vertexList.size() + "v"
		);
		vertexList.put(v2.label, v2);

		alpha += 120 * 0.0174533f;
		IVertex v3 = new IVertex(
			(float)(Math.sin(alpha) * Math.cos(beta)) * radius + x,
			(float)(Math.sin(beta)) * radius + y,
			(float)(Math.cos(alpha) * Math.cos(beta)) * radius + z,
			vertexList.size() + "v"
		);
		vertexList.put(v3.label, v3);		
		
		// All points connecting to top
		IEdge e1 = new IEdge(top, v1, edgeList.size() + "e");
		edgeList.put(e1.label, e1);
		IEdge e2 = new IEdge(top, v2, edgeList.size() + "e");
		edgeList.put(e2.label, e2);
		IEdge e3 = new IEdge(top, v3, edgeList.size() + "e");
		edgeList.put(e3.label, e3);
		
		// All points connecting the base
		IEdge e4 = new IEdge(v1, v2, edgeList.size() + "e");
		edgeList.put(e4.label, e4);
		IEdge e5 = new IEdge(v2, v3, edgeList.size() + "e");
		edgeList.put(e5.label, e5);
		IEdge e6 = new IEdge(v1, v3, edgeList.size() + "e");
		edgeList.put(e6.label, e6);
	
		
		// Make triangles from edges
		ITriangle t1 = new ITriangle(e4, e5, e6); // Base triangle
		this.faceList.put(faceList.size() + "f", t1);
		ITriangle t2 = new ITriangle(e4, e3, e1);
		this.faceList.put(faceList.size() + "f", t2);
		ITriangle t3 = new ITriangle(e5, e1, e2);
		this.faceList.put(faceList.size() + "f", t3);
		ITriangle t4 = new ITriangle(e6, e2, e3);
		this.faceList.put(faceList.size() + "f", t4);		
		
		// TODO compound these transformations
		this.transform(transform1);
		this.transform(rotate);
		this.transform(transform2);

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
