package ca.nicho.izzy.object;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.primatives.IEdge;
import ca.nicho.izzy.primatives.ITriangle;
import ca.nicho.izzy.primatives.IVector;
import ca.nicho.izzy.primatives.IVertex;

public class IShitstorm extends IObject {
	public enum splitMode {
	    MEDIAN, SERPINSKI, BARYCENTRIC
	}
	
	public IShitstorm(){
		
		IVertex w1 = new IVertex(1,0,0, vertexList.size() + "v");
		IVertex w2 = new IVertex(0,1,0, vertexList.size() + "v");
		IVertex o1 = new IVertex(1,1,0, vertexList.size() + "v");
		IVertex o2 = new IVertex(0,0,0, vertexList.size() + "v");
		
		IEdge l1 = new IEdge(w1,w2, edgeList.size() + "e");
		IEdge l2 = new IEdge(w1,o1, edgeList.size() + "e");
		IEdge l3 = new IEdge(w2,o1, edgeList.size() + "e");
		IEdge l4 = new IEdge(w1,o2, edgeList.size() + "e");
		IEdge l5 = new IEdge(w2,o2, edgeList.size() + "e");
		
		ITriangle a1 = new ITriangle(l1, l2, l3);
		ITriangle a2 = new ITriangle(l1, l4, l5);
		
		a2.getVertices();
		
		l1.t1 = a1;
		l1.t2 = a2;
		
		E2FList please = new E2FList();
		please.list.add(l1);
		
		please.medianDivide(5);
		HashSet<ITriangle> triangles = please.getITriangles();
		for(ITriangle t : triangles){
			this.faceList.put(faceList.size() + "f", t);
		}
	}
	
	public class E2FList {
		public ArrayList<IEdge> list;
		
		public E2FList() {
			list = new ArrayList<IEdge>();
		}
		
		public void medianDivide(int i) {
			
			for(int o = 0; o < i; o++){
				ArrayList<IEdge> newList = new ArrayList<IEdge>();
				
				for (IEdge e2f : list) {
					//Create a IVertex which bisects the IEdge, and protrudes/intrudes from the IEdge 
					//by the sum of the respective ITriangles normal unit IVectors to a max factor of 1/5 the length of the IEdge
					IVertex splitVertex = e2f.paraCoor((float)Math.random() * 0.1f + 0.5f);
					IVector shift = e2f.t1.getNormalVector();
					shift.normalize();
					shift.scale((Math.random()-0.5)*0.2*e2f.getEdgeLength());
					splitVertex.vectTranslate(shift);
					
					//Create new mid IEdges
					IEdge da = new IEdge(splitVertex, e2f.v1, edgeList.size() + "e");
					IEdge db = new IEdge(splitVertex, e2f.v2, edgeList.size() + "e");
					
					//Find other f1 IEdges
					HashSet<IEdge> IEdgesf1 = new HashSet<IEdge>();
					IEdgesf1.add(e2f.t1.e1);
					IEdgesf1.add(e2f.t1.e2);
					IEdgesf1.add(e2f.t1.e3);
					IEdgesf1.remove(e2f);
					
					Iterator<IEdge> it1 = IEdgesf1.iterator();
					IEdge e1f1 = it1.next();
					IEdge e2f1 = it1.next();
					
					//Find unique f1 IVertex
					HashSet<IVertex> verticesf1 = new HashSet<IVertex>();
					verticesf1.addAll(e2f.t1.getVertices());
					verticesf1.remove(e2f.v1);
					verticesf1.remove(e2f.v2);
					
					//Create f1 median
					Iterator<IVertex> it2 = verticesf1.iterator();
					IEdge emf1 = new IEdge(splitVertex, it2.next(), edgeList.size() + "e");
					
					//Add new f1 E2F to newList
					if (formsTriangle(emf1,e1f1,da)){
						emf1.t1 = new ITriangle(emf1,e1f1,da);
						emf1.t2 = new ITriangle(emf1,e2f1,db);
						newList.add(emf1);
					} else {
						emf1.t1 = new ITriangle(emf1,e1f1,db);
						emf1.t2 = new ITriangle(emf1,e2f1,da);
						newList.add(emf1);
					}
					
					//Find other f2 IEdges
					HashSet<IEdge> edgesf2 = new HashSet<IEdge>();
					edgesf2.add(e2f.t2.e1);
					edgesf2.add(e2f.t2.e2);
					edgesf2.add(e2f.t2.e3);
					edgesf2.remove(e2f);
					
					Iterator<IEdge> it3 = edgesf2.iterator();
					IEdge e1f2 = it3.next();
					IEdge e2f2 = it3.next();
					
					//Find unique f2 IVertex
					HashSet<IVertex> verticesf2 = new HashSet<IVertex>();
					verticesf2.addAll(e2f.t2.getVertices());
					verticesf2.remove(e2f.v1);
					verticesf2.remove(e2f.v2);
					
					//Create f2 median
					Iterator<IVertex> it4 = verticesf2.iterator();
					IEdge emf2 = new IEdge(splitVertex, it4.next(), edgeList.size() + "e");
					
					//Add new f2 E2F to newList
					if (formsTriangle(emf2,e1f2,da)) {
						emf2.t1 = new ITriangle(emf2,e1f2,da);
						emf2.t2 = new ITriangle(emf2,e2f2,db);
						newList.add(emf2);
					} else {
						emf2.t1 = new ITriangle(emf2,e1f2,db);
						emf2.t2 = new ITriangle(emf2,e2f2,da);
						newList.add(emf2);
					}
				}
				
				list = newList;
			}
		}

		public HashSet<ITriangle> getITriangles() {
			HashSet<ITriangle> ITriangles = new HashSet<ITriangle>(); 
			for(IEdge e2f : list) {
				ITriangles.add(e2f.t1);
				ITriangles.add(e2f.t2);
			}
			return ITriangles;
		}
	}
	
	public boolean formsTriangle(IEdge e1, IEdge e2, IEdge e3) {
		HashSet<IVertex> vertices = new HashSet<IVertex>();
		vertices.add(e1.v1);
		vertices.add(e1.v2);
		vertices.add(e2.v1);
		vertices.add(e2.v2);
		vertices.add(e3.v1);
		vertices.add(e3.v2);
	
		return vertices.size() == 3;
	}
	
	
	public static void serpinskiDivide() {
		
	}
	
	public static void baricentricDivide() {
		
	}
	
	public static void roughen(int iterations, int splitMode) {
		
	}


	@Override
	public void drawWire(GL2 gl, float cx, float cy, float cz) {
		
		for(ITriangle t : faceList.values()){
			gl.glColor3f((float)Math.random(), (float)Math.random(), (float)Math.random());
			gl.glBegin(GL2.GL_TRIANGLE_FAN);
			IVertex v1 = t.v1;
			IVertex v2 = t.v2;
			IVertex v3 = t.v3;
			
			gl.glVertex3f(v1.X(), v1.Y(), v1.Z());
			gl.glVertex3f(v2.X(), v2.Y(), v2.Z());
			gl.glVertex3f(v3.X(), v3.Y(), v3.Z());
			gl.glEnd();
		}
	}
	
}
