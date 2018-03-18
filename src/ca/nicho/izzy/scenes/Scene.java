package ca.nicho.izzy.scenes;

import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import ca.nicho.izze.anim.Animation;
import ca.nicho.izzy.GLFrame;
import ca.nicho.izzy.object.IObject;

public abstract class Scene {
	
	protected GLFrame frame;
	public ArrayList<IObject> objects = new ArrayList<IObject>();
	public ArrayList<Animation> animations = new ArrayList<Animation>();
	
	public Scene(GLFrame frame){
		this.frame = frame;
	}
	
	public abstract void drawScene(GL2 gl);
	public abstract void tick();
	
	public void registerAnimation(Animation a){
		this.animations.add(a);
	}

	public void addObject(IObject o){
		this.objects.add(o);
	}
	
	
}
