package ca.nicho.izzy;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import ca.nicho.izze.anim.Animation;
import ca.nicho.izzy.scenes.NatureScene;
import ca.nicho.izzy.scenes.Scene;
import ca.nicho.izzy.scenes.SmoothNatureScene;
import ca.nicho.izzy.scenes.TestScene;
import ca.nicho.izzy.scenes.TreeScene;

public class GLFrame extends JFrame implements GLEventListener {

		
	public static void main(String[] s){	
		GLFrame frame = new GLFrame();
		
		frame.scenes.add(new NatureScene(frame));
		frame.scenes.add(new TestScene(frame));
		frame.scenes.add(new TreeScene(frame));
		
		frame.initFrame();
	}
	
	public int currentScene = 0;
	public ArrayList<Scene> scenes = new ArrayList<Scene>();
 	
	private Robot robot;
		
	protected Timer timer;
	
	protected GLCanvas canvas;
	protected GLU glu;
	public static GLUT glut;
	
	private float distance = 100;
	
	public float cameraX = 0;
	public float cameraY = 1;
	public float cameraZ = 0;
	
	private float lookatX = 0;
	private float lookatY = 0f;
	private float lookatZ = 0;
	
	private boolean rightPressed;
	private boolean leftPressed;
	private boolean upPressed;
	private boolean downPressed;
	private boolean screenshot = false;
		
	float alpha = (float)Math.PI;
	float beta = 0;
		
	public void initFrame(){
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		
		this.getContentPane().add(canvas);
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(true);
		
		try {
			robot = new Robot();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB ), new Point(), null));
		canvas.addKeyListener(new KeyHandler());
		canvas.requestFocusInWindow();
	}
	
	public void timerUpdate(){
		
		if(currentScene >= 0 && currentScene < scenes.size()){
			Scene scene = scenes.get(currentScene);
			for(Animation a : scene.animations){
				a.tick();
			}
		}
		
		this.updateMovement();
		this.updateMouse();
		canvas.repaint();
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		glu = new GLU();
        glut = new GLUT();
		timer = new Timer(30);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        this.setMouseCenter();
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		this.prepareViewport(gl);				
		
		if(currentScene >= 0 && currentScene < scenes.size()){
			Scene scene = scenes.get(currentScene);
			scene.drawScene(gl);
		}
						
		gl.glFlush();
		if(screenshot){
			GLUtils.saveImage(gl, 2 * canvas.getWidth(), 2 * canvas.getHeight());
			screenshot = false;
		}
	}

	/**
	 * Prepares the viewport for an orthogonal projection.
	 * @param gl GL2 instance
	 */
	private void prepareViewport(GL2 gl){
		// Viewport 1 - Orthogonal static
		float w = 2 * getWidth();
		float h = 2 * getHeight();
		
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
	    gl.glViewport(0, 0, (int)w, (int)h);	    		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(60, w / (double) h, 0.1f, 100);
	    glu.gluLookAt(cameraX, cameraY, cameraZ, lookatX, lookatY, lookatZ, 0, 1, 0);	   
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) {}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {}
	
	private float speed = 0.1f;
	
	private final float SENSITIVITY = 0.005f;
	
	protected void updateMovement(){
		
		float dx1 = (float)(Math.sin(alpha) * Math.cos(beta)) * speed;
		float dy1 = (float)(Math.sin(beta)) * speed;
		float dz1 = (float)(Math.cos(alpha) * Math.cos(beta)) * speed;
		
		float dx2 = (float)Math.cos((float)Math.PI - alpha) * speed;
		float dy2 = 0;
		float dz2 = (float)Math.sin((float)Math.PI - alpha) * speed;
		
		if(upPressed){
			cameraX += dx1;
			lookatX += dx1;
			cameraY += dy1;
			lookatY += dy1;
			cameraZ += dz1;
			lookatZ += dz1;
		}
		if(downPressed){
	
			cameraX -= dx1;
			lookatX -= dx1;
			cameraY -= dy1;
			lookatY -= dx1;
			cameraZ -= dz1;
			lookatZ -= dx1;
		}
		if(leftPressed){
			cameraX -= dx2;
			lookatX -= dx2;
			cameraY -= dy2;
			lookatY -= dx2;
			cameraZ -= dz2;
			lookatZ -= dx2;
		}else if(rightPressed){
			cameraX += dx2;
			lookatX += dx2;
			cameraY += dy2;
			lookatY += dy2;
			cameraZ += dz2;
			lookatZ += dz2;
		}
	}
	
	protected void updateMouse(){
		setCameraPerspective();
		setMouseCenter();
	}
	
	private void setCameraPerspective(){
		int ox = getLocation().x + getWidth() / 2;
		int oy = getLocation().y + getHeight() / 2;
		
		int x = MouseInfo.getPointerInfo().getLocation().x;
		int y = MouseInfo.getPointerInfo().getLocation().y;
		
		alpha = alpha + (x - ox) * SENSITIVITY;
		beta = beta + (y - oy) * SENSITIVITY / 2;
		
		float dx = (float)(Math.sin(alpha) * Math.cos(beta)) * distance;
		float dz = (float)(Math.cos(alpha) * Math.cos(beta)) * distance;
		float dy = (float)(Math.sin(beta)) * distance;
				
		lookatX = dx;
		lookatY = dy;
		lookatZ = dz;
	}
	
	private void setMouseCenter(){
		if(this.isFocused()){
			int mouse_x = getLocation().x + getWidth() / 2;
			int mouse_y = getLocation().y + getHeight() / 2;
			robot.mouseMove(mouse_x, mouse_y);
		}
	}	
	
	private class Timer implements Runnable {
		
		private long time;
		private long lastTime;
		private boolean running = true;
		
		public Timer(long time){
			this.time = time;
			new Thread(this).start();
		}
		
		public void run(){
			while(running){
				long currentTime = System.currentTimeMillis();
				if(currentTime - lastTime >= time){
					timerUpdate();
					lastTime = currentTime;
				}
			}
		}
		
	}
	
	private class KeyHandler implements KeyListener {
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			if(e.getKeyCode() == KeyEvent.VK_UP){
				upPressed = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				downPressed = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				leftPressed = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rightPressed = true;
			}
			
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
				System.exit(1);
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			
			if(e.getKeyCode() == KeyEvent.VK_UP){
				upPressed = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				downPressed = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_LEFT){
				leftPressed = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){
				rightPressed = false;
			}
			
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar() == '1'){
				currentScene = 1;
			}else if(e.getKeyChar() == '2'){
				currentScene = 2;
			}else if(e.getKeyChar() == '3'){
				currentScene = 3;
			}else if(e.getKeyChar() == '4'){
				currentScene = 4;
			}else if(e.getKeyChar() == '5'){
				currentScene = 5;
			}else if(e.getKeyChar() == '6'){
				currentScene = 6;
			}else if(e.getKeyChar() == '7'){
				currentScene = 7;
			}else if(e.getKeyChar() == '8'){
				currentScene = 8;
			}else if(e.getKeyChar() == '9'){
				currentScene = 9;
			}else if(e.getKeyChar() == '0'){
				currentScene = 0;
			}else if(e.getKeyChar() == 'p'){
				screenshot = true;
			}
			
		}
		
	}
	
	
}
