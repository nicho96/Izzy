package ca.nicho.izzy.object;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.primatives.IEdge;
import ca.nicho.izzy.primatives.ITriangle;
import ca.nicho.izzy.primatives.IVertex;

public abstract class IObject {

	public final HashMap<String, IVertex> vertexList;
	public final HashMap<String, IEdge> edgeList;
	public final HashMap<String, ITriangle> faceList;
		
	public IObject(){
		vertexList = new HashMap<String, IVertex>();
		edgeList = new HashMap<String, IEdge>();
		faceList = new HashMap<String, ITriangle>();
	}
	
	public void transform(RealMatrix t){
		for(IVertex v : vertexList.values()){
			double[][] d = {{(double)v.X()},
							{(double)v.Y()},
							{(double)v.Z()},
							{1.0}};
			RealMatrix m = MatrixUtils.createRealMatrix(d);
			double[][] r = t.multiply(m).getData();
			v.setPoints((float)r[0][0], (float)r[1][0], (float)r[2][0]);
		}
	}
	
	protected int euler = 0;
	
	public abstract void drawWire(GL2 gl, float cx, float cy, float cz);
	
	public boolean isEulerCharacteristic(){
		return euler == 2;
	}
	
	public void saveToJSON(File f) throws IOException{
		if(!f.exists()){
			f.createNewFile();
		}
		
		JSONObject o = new JSONObject();
		
		JSONArray vertices = new JSONArray();
		for(Map.Entry<String, IVertex> entry : vertexList.entrySet()){
			JSONObject ov = new JSONObject();
			IVertex v = entry.getValue();
			ov.put("label", entry.getKey());
			ov.put("x", v.X());
			ov.put("y", v.Y());
			ov.put("z", v.Z());
			vertices.put(ov);
		}
		o.put("vertices", vertices);
		
		JSONArray edges = new JSONArray();
		for(Map.Entry<String, IEdge> entry : edgeList.entrySet()){
			JSONObject oe = new JSONObject();
			IEdge e = entry.getValue();
			oe.put("label", entry.getKey());
			oe.put("v1", e.v1.label);
			oe.put("v2", e.v2.label);
			edges.put(oe);
		}
		o.put("edges", edges);
		
		JSONArray faces = new JSONArray();
		for(Map.Entry<String, ITriangle> entry : faceList.entrySet()){
			JSONObject ov = new JSONObject();
			ITriangle t = entry.getValue();
			ov.put("label", entry.getKey());
			ov.put("e1", t.e1.label);
			ov.put("e2", t.e2.label);
			ov.put("e3", t.e3.label);
			vertices.put(ov);
		}
		o.put("faces", faces);
		
		PrintWriter writer = new PrintWriter(f);
		writer.write(o.toString());
		writer.close();
		
	}
	
	public IVertex getOrigin() {
		return null;
	}
	
}
