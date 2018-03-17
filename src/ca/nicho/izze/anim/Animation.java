package ca.nicho.izze.anim;

import ca.nicho.izzy.object.IObject;

public abstract class Animation {
	
	protected IObject o;
	
	public Animation(IObject o){
		this.o = o;
	}
	
	public abstract void tick();

}
