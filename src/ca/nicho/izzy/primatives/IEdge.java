package ca.nicho.izzy.primatives;

public class IEdge {
	
	public final IVertex i1, i2;
	
	public String label;
	public ITriangle t1, t2;
	
	public float r = -1;
	public float g = -1;
	public float b = -1;
	
	public float width = -1;
	
	public IEdge(IVertex i1, IVertex i2, String label){
		this.label = label;
		this.i1 = i1;
		this.i2 = i2;
	}
	
}
