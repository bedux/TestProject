package com.example.testproject;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

public class Scene implements GLSurfaceView.Renderer {

	
	private List<Shape2D> listDrawable;
	
	private List<ActionListener> listListener;
	
	public ShapeMotion target = null;
	
	private boolean initialize = false;

	public Scene() {
		listDrawable = new ArrayList<Shape2D>();
		listListener = new ArrayList<ActionListener>();
	}

	public static GameCamera camera;

	//initialize object function
	private void init() {
		for (Shape2D sh : listDrawable) {
			sh.Start();
			sh.CreateBuffer();
			sh.CompileShader();
			sh.LoadTexture();

		}

	}

	@Override
	public void onDrawFrame(GL10 gl) {

		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		if (target != null) {
			camera.SetPositon(target.getPosition().x, target.getPosition().y);
		}

		for (Shape2D sh : listDrawable) {

			sh.Draw(camera, StaticValue.identity);
		}

	}

	/**
	 * Add element to the renderer scene.
	 * @param g is the shape 
	 * @see Shape2D 
	 * */
	public void addGlElement(Shape2D g) {

		listDrawable.add(g);
		if (initialize) {
			g.Start();
			g.CreateBuffer();
			g.CompileShader();
			g.LoadTexture();
		}

	}
	/**
	 * Add element to the renderer scene and set as tag. This means that the camera follow that element
	 * @param g is the shape that implement ShapeMotion.
	 * @see ShapeMotion 
	 * */
	public void addGlElementTag(ShapeMotion g) {

		target = g;
		listDrawable.add((Shape2D) g);
		if (initialize) {
			((Shape2D) g).Start();
			((Shape2D) g).CreateBuffer();
			((Shape2D) g).CompileShader();
			((Shape2D) g).LoadTexture();
		}

	}
	/**
	 * Add element to the scene listener.
	 * @param g is the object that implement ActionListener.
	 * @see ActionListener 
	 * */
	public void addGlListener(ActionListener g) {

		listListener.add(g);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		GLES20.glEnable(GLES20.GL_BLEND);
		StaticValue.ratio = width / height;

		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		init();
		camera = new GameCamera(0, 0);
		camera.SetPositon(100, 100);
		initialize = true;

	}

	@Override
	public void onSurfaceCreated(GL10 gl,
			javax.microedition.khronos.egl.EGLConfig config) {

		GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
		ResourceManager.getInstance().LoadTextureResurcesStatic();

	}

	public static int loadShader(int type, String shaderCode) {

		int shader = GLES20.glCreateShader(type);

		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	public void onAction(int x, int y) {
		for (ActionListener as : listListener) {
			as.OnClict(x, y);
		}

	}

}
