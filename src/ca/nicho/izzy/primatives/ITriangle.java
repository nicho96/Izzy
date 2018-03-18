package ca.nicho.izzy.primatives;

import java.util.HashSet;

public class ITriangle {

	public IEdge e1, e2, e3;
	public IVertex v1, v2, v3;
	public float r;
	public float g;
	public float b;
	
	public ITriangle(IEdge i1, IEdge i2, IEdge i3){
		this.e1 = i1;
		this.e2 = i2;
		this.e3 = i3;
		
		this.v1 = i1.v1;
		this.v2 = i1.v2;
		this.v3 = i2.v1;
		
		if(v1 == v3 || v2 == v3){
			v3 = i2.v2;
		}
		
	}
	
	public ITriangle(IVertex v1, IVertex v2, IVertex v3){
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}
	
		public IEdge getLongestEdge() {
		double l1 = e1.getEdgeLength();
		double l2 = e2.getEdgeLength();
		double l3 = e3.getEdgeLength();
		if(l1 > l2 && l1 > l3)
			return e1;
		else if(l2 > l3)
			return e2;
		else 
			return e3;
	}
		
	public IVector getNormalVector() {
		return e1.getVector().crossProduct(e2.getVector());
	}
	
	public IVertex baryCoor(float a, float b, float c) {
		float sum = a + b + c;
		if(e2.v1 != e1.v1 && e2.v1 != e1.v2) {
			return new IVertex((a*e2.v1.x + b*e1.v1.x + c*e1.v2.x) / sum,
					(a*e2.v1.y + b*e1.v1.y + c*e1.v2.y) / sum,
					(a*e2.v1.z + b*e1.v1.z + c*e1.v2.z) / sum,
					null);
		} else {
			return new IVertex((a*e2.v2.x + b*e1.v1.x + c*e1.v2.x) / sum,
					(a*e2.v2.y + b*e1.v1.y + c*e1.v2.y)/sum,
					(a*e2.v2.z + b*e1.v1.z + c*e1.v2.z)/sum,
					null);
		}
	}
	
	public HashSet<IVertex> getVertices(){
		HashSet<IVertex> set = new HashSet<IVertex>();
		set.add(v1);
		set.add(v2);
		set.add(v3);
		return set;
	}
	
	public String toString(){
		return "ITriangle(" + v1 + ", " + v2 + ", " + v3 + ")" + this.hashCode();
	}
	
}
