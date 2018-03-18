package ca.nicho.izzy.object;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jogamp.opengl.GL2;

import ca.nicho.izzy.primatives.IEdge;
import ca.nicho.izzy.primatives.ITriangle;
import ca.nicho.izzy.primatives.IVertex;

public class IPolygon extends IObject {

	public IPolygon(JSONObject o){
		parseVertices(o.getJSONArray("vertices"));
		parseEdges(o.getJSONArray("edges"));
		parseFaces(o.getJSONArray("faces"));
		euler = vertexList.size() - edgeList.size() + faceList.size();
	}
	
	private void parseVertices(JSONArray arr){
		for(int i = 0; i < arr.length(); i++){
			JSONObject o = arr.getJSONObject(i);
			String label = o.getString("label");
			IVertex v = new IVertex(o.getFloat("x"), o.getFloat("y"), o.getFloat("z"), label);
			vertexList.put(label, v);
		}
	}
	
	private void parseEdges(JSONArray arr){
		for(int i = 0; i < arr.length(); i++){
			JSONObject o = arr.getJSONObject(i);
			String label = o.getString("label");
			IVertex v1 = vertexList.get(o.getString("v1"));
			IVertex v2 = vertexList.get(o.getString("v2"));
			IEdge v = new IEdge(v1, v2, label);
			edgeList.put(label, v);
		}
	}
	
	private void parseFaces(JSONArray arr){
		for(int i = 0; i < arr.length(); i++){
			JSONObject o = arr.getJSONObject(i);
			IEdge e1 = edgeList.get(o.getString("e1"));
			IEdge e2 = edgeList.get(o.getString("e2"));
			IEdge e3 = edgeList.get(o.getString("e3"));
			ITriangle v = new ITriangle(e1, e2, e3);
			faceList.put(o.getString("label"), v);
		}
	}


	public void drawWire(GL2 gl, float cx, float cy, float cz) {
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3f(1, 1, 1);
		for(IEdge e : edgeList.values()){
			gl.glVertex3f(e.v1.X(), e.v1.Y(), e.v1.Z());
			gl.glVertex3f(e.v2.X(), e.v2.Y(), e.v2.Z());
		}
		gl.glEnd();
	}
	
}
