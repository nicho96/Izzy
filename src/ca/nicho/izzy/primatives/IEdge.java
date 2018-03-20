package ca.nicho.izzy.primatives;

public class IEdge {
	
	public final IVertex v1, v2;
	
	public String label;
	public ITriangle t1, t2;
	
	public float r = -1;
	public float g = -1;
	public float b = -1;
	
	public float width = -1;
	
	public IEdge(IVertex i1, IVertex i2, String label){
		this.label = label;
		this.v1 = i1;
		this.v2 = i2;
	}
	
	public float getEdgeLength() {
		return (float)Math.sqrt(Math.pow(v1.x-v2.x, 2)+Math.pow(v1.y-v2.y, 2)+Math.pow(v1.z-v2.z, 2));
	}
	
	public IVector getVector() {
		return new IVector(v2.x-v1.x, v2.y-v1.y, v2.z-v1.z);
	}
	
	public IVertex paraCoor(float t) {
		return new IVertex(v1.x + t*(v2.x-v1.x), v1.y + t*(v2.y-v1.y), v1.z + t*(v2.z-v1.z), null);
	}
	
	public ITriangle getRandITriangle() {
		return (Math.random() < 0.5) ? t1 : t2;
	}
	
	public IVector getAvgNormalIVector() {
		IVector v1 = t1.getNormalVector();
		IVector v2 = t2.getNormalVector();
		v1.normalize();
		v2.normalize();
		v1.add(v2);
		return v1;
	}
	
	@Override
	public String toString(){
		return "E(" + v1 + ", " + v2 + ")";
	}
	
}
