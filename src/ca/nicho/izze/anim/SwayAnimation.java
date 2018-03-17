package ca.nicho.izze.anim;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import ca.nicho.izzy.object.IObject;
import ca.nicho.izzy.primatives.IVertex;

public class SwayAnimation extends Animation {
	
	private RealMatrix s1;
	private RealMatrix s2;
	private RealMatrix t1;
	private RealMatrix t2;
	private RealMatrix left;
	private RealMatrix right;
	
	public SwayAnimation(IObject o, float rate) {
		super(o);
		
		IVertex v = o.getOrigin();
		
		double shear1[][] = {{1, rate, 0, 0},
				 			 {0, 1, 0, 0},
				 			 {0, 0, 1, 0},
				 			 {0, 0, 0, 1}};

		double shear2[][] = {{1, -rate, 0, 0},
				 			 {0, 1, 0, 0},
				 			 {0, 0, 1, 0},
				 			 {0, 0, 0, 1}};
		
		double trans1[][] = {{1, 0, 0, v.X()},
				 {0, 1, 0, v.Y()},
	 			 {0, 0, 1, v.Z()},
	 			 {0, 0, 0, 1}};

		double trans2[][] = {{1, 0, 0, -v.X()},
				 {0, 1, 0, -v.Y()},
				 {0, 0, 1, -v.Z()},
				 {0, 0, 0, 1}};
		
		s1 = MatrixUtils.createRealMatrix(shear1);
		s2 = MatrixUtils.createRealMatrix(shear2);
		t1 = MatrixUtils.createRealMatrix(trans1);
		t2 = MatrixUtils.createRealMatrix(trans2);

		right = t1.multiply(s1).multiply(t2);
		left = t1.multiply(s2).multiply(t2);
		
	}

	private boolean flag = false;
	private int tickCount = 0;

	@Override
	public void tick() {
				
		if(tickCount ++ == 30){
			flag = !flag;
			tickCount = 0;
		}
		if(flag) o.transform(right); else o.transform(left);
	}
	
	

}
