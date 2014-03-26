/**
 * This class is the base drawable class.By default draw a plane of size w,h. 
 */
package com.example.testproject;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Shape2D {

	protected String mVertexShader = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 aPosition;" + "void main() {"
			+ "  gl_Position = uMVPMatrix * aPosition;" + "}";
	protected String mPixelShader = "precision mediump float;"
			+ "void main() {" + "  gl_FragColor =vec4(1.0,1.0,1.0,1.0)  ;"
			+ "}";
	protected int mTextureId = -1;
	protected float[] mVertices;
	protected FloatBuffer mVertexBuffer;
	protected float w, h, d;
	protected int mProgram;
	protected int mTexture;
	protected int nElementVertex = 3;
	protected int mode = GLES20.GL_TRIANGLES;

	public Shape2D(float w, float h, float d) {
		
		this.w = w;
		this.h = h;
		this.d = d;
		

	}

	/**
	 * This function is call at start when the GL context is created
	 * */
	public void Start(){
		w/=StaticValue.ratio;
		mVertices = new float[] { 0, h, d,

				w, h, d,

				0, 0, d,

				0, 0, d,

				w, h, d,

				w, 0, d };

		
		
	}
	/**
	 * Compile the current Shader
	 **/
	public void CompileShader() {
		// prepare shaders and OpenGL program
		int vertexShader = Scene.loadShader(GLES20.GL_VERTEX_SHADER,
				mVertexShader);

		int fragmentShader = Scene.loadShader(GLES20.GL_FRAGMENT_SHADER,
				mPixelShader);

		mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
		// to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
		// shader to program
		GLES20.glLinkProgram(mProgram); // create OpenGL program executables

	}

	/**
	 * Compile the new shader
	 * 
	 * @param fragment
	 *            String represent the fragment shader
	 * @param fragment
	 *            String represent the pixel shader
	 * @see String
	 */
	public void CompileShader(String vertex, String pixel) {
		mVertexShader = vertex;
		mPixelShader = pixel;
		CompileShader();
	}

	/**
	 * @return The pixel shader as String
	 * @see String
	 */
	public String getPixelShader() {
		return this.mPixelShader;

	}

	/**
	 * @return The fragment shader as String
	 * @see String
	 */
	public String getVertexShader() {
		return this.mVertexShader;
	}

	/**
	 * Load a default texture
	 **/
	public void LoadTexture() {

		if (mTextureId != -1) {
			mTexture = ResourceManager.getInstance().getResource(mTextureId);
		}

	}

	/**
	 * Load a default Texture from id
	 * 
	 * @param id
	 *            defined in a Class
	 * @see StaticValue
	 **/
	public void LoadTexture(int id) {
		mTextureId = id;
		LoadTexture();
	}

	/**
	 * return a texture id
	 * 
	 * @see StaticValue
	 * */
	public int GetTexturId() {
		return mTextureId;

	}

	/**
	 * Create buffer
	 * 
	 * */
	public void CreateBuffer() {

		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (number of coordinate values * 4 bytes per float)
				mVertices.length * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		mVertexBuffer = bb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		mVertexBuffer.put(mVertices);
		// set the buffer to read the first coordinate
		mVertexBuffer.position(0);

	}

	/***
	 * Create buffer from given element
	 * 
	 * @param element
	 *            [] is a float array with all elements
	 * @param n
	 *            int represent the number of number of elements for each vertex
	 * */
	public void CreateBuffer(float[] element, int n) {
		mVertices = element;
		nElementVertex = n;

	}

	/***
	 * Draw object using the matrix of a Camera
	 * 
	 * @param camera
	 *            Camera to calculate the position
	 * @see GameCamera
	 * */
	protected void SetShaderValue(GameCamera camera, float[] modelTransform) {
		GLES20.glUseProgram(mProgram);

		mVertexBuffer.position(0);
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, (nElementVertex) * 4, mVertexBuffer);

		int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
				"uMVPMatrix");

		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false,
				camera.getCameraMatrix(modelTransform), 0);

	}

	public void Draw(GameCamera camera, float[] modelTransform) {
		SetShaderValue(camera, modelTransform);
		GLES20.glDrawArrays(mode, 0, mVertices.length / nElementVertex);
	}

}
