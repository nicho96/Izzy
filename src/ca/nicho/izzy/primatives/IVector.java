package ca.nicho.izzy.primatives;

public class IVector {
	public float x,y,z;
	
	public IVector(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public IVector crossProduct(IVector w) {
		return new IVector(y*w.z-w.y*z,z*w.x-w.z*x,x*w.y-w.x*y);
	}
	
	public float norm() {
		return (float)Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
	}
	
	public void normalize() {
		double n = this.norm();
		x /= n;
		y /= n;
		z /= n;
	}
	
	public void add(IVector v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public void scale(double factor) {
		x *= factor;
		y *= factor;
		z *= factor;
	}
	
	public String toString() {
		return "<" + x + "," + y + "," + z + ">"; 
	}
}