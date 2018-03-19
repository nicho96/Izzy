package ca.nicho.izzy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;

public final class GLUtils {

	public static void fillXYPlane(GL2 gl, float d, float y){
		gl.glBegin(GL2.GL_TRIANGLE_FAN);
		gl.glVertex3f(0, y, 0);
		gl.glVertex3f(0, y, d);
		gl.glVertex3f(d, y, d);
		gl.glVertex3f(d, y, 0);
		gl.glEnd();
	}
	
	public static float distance(float x1, float y1, float z1, float x2, float y2, float z2){
		return (float)Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - 1), 2));
	}
	
	public static void drawAxis(GL2 gl){
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3f(1, 0, 0); //RED - X
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(3f, 0, 0);

		gl.glColor3f(0, 1, 0); //GREEN - Y
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(0, 3f, 0);

		gl.glColor3f(0, 0, 1); // BLUE - Z
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(0, 0, 3f);
		gl.glEnd();
	}
	
	 public static void saveImage(GL2 gl2, int width, int height) {

		 try {
		        BufferedImage screenshot = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		        Graphics graphics = screenshot.getGraphics();

		        ByteBuffer buffer = GLBuffers.newDirectByteBuffer(width * height * 4);
		        // be sure you are reading from the right fbo (here is supposed to be the default one)
		        // bind the right buffer to read from
		        gl2.glReadBuffer(GL2.GL_BACK);
		        // if the width is not multiple of 4, set unpackPixel = 1
		        gl2.glReadPixels(0, 0, width, height, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer);

		        for (int h = 0; h < height; h++) {
		            for (int w = 0; w < width; w++) {
		                // The color are the three consecutive bytes, it's like referencing
		                // to the next consecutive array elements, so we got red, green, blue..
		                // red, green, blue, and so on..+ ", "
		                graphics.setColor(new Color((buffer.get() & 0xff), (buffer.get() & 0xff),
		                        (buffer.get() & 0xff)));
		                buffer.get();   // consume alpha
		                graphics.drawRect(w, height - h, 1, 1); // height - h is for flipping the image
		            }
		        }
		        // This is one util of mine, it make sure you clean the direct buffer
		        //BufferUtils.destroyDirectBuffer(buffer);

		        File outputfile = new File(new Date() + ".png");
		        ImageIO.write(screenshot, "png", outputfile);
		    } catch (IOException ex) {
		    	ex.printStackTrace();
		    }
	    }
	
}
