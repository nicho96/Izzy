package ca.nicho.izzy.primatives;

import java.util.ArrayList;

public class IVertex {

	public String label;
	private float x, y, z;
	
	/**
	 * Create a new vertex.
	 * @param x
	 * @param y
	 * @param z
	 */
	public IVertex(float x, float y, float z, String label){
		this.label = label;
		setPoints(x, y, z);
	}
	
	/**
	 * @return the X value of the vertex.
	 */
	public float X(){
		return x;
	}
	
	/**
	 * @return the Y value of the vertex.
	 */
	public float Y(){
		return y;
	}
	
	/**
	 * @return the Z value of the vertex.
	 */
	public float Z(){
		return z;
	}
	
	/**
	 * Change the positions of the vertex points.
	 */
	public void setPoints(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	
}
