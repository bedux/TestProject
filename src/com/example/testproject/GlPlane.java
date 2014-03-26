package com.example.testproject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class GlPlane extends GlElement {
	private final String vertexShaderCode =

	"uniform mat4 uMVPMatrix;" + " uniform vec2 camPosition;"+
			"uniform float uvRotation;"
			+ "attribute vec4 aPosition; vec2 offset=vec2(0.5,0.5);"

			+ "attribute vec2 aUv;"

			+ "varying vec2 vUv;" + "void main() {"
			+ "  gl_Position = uMVPMatrix * aPosition;" 
			+"float cosX,sinX;"
			+"float cosY,sinY;"

			+"sinX = sin(uvRotation);"
			+"cosX = cos(uvRotation);"

			+"sinY = sin(uvRotation);"
			+"cosY = cos(uvRotation);"

			+"mat2 rotationMatrix = mat2( cosX, -sinX,sinY, cosX);"

		

			+"vec2 newcoords=((aUv-offset)*(rotationMatrix));"
			+"newcoords+=offset; vUv=newcoords;"

			+ "}";

	private final String fragmentShaderCode = "precision mediump float;"
			+ "uniform sampler2D u_Texture;" + "varying vec2 vUv;"
			+ "void main() {" + "vec4 colorTxt=texture2D(u_Texture, vUv);"

			+ "  gl_FragColor =colorTxt  ;" + "}";

	private final FloatBuffer vertexBuffer;
	private final int mProgram;
	private int text;
	private float[] triangleCoords;
	private float d;
	protected float h,w;
	float angle=0;
	public GlPlane(float w, float h,float d,String filename ) {
		this.d=d;
		this.w=w;
		this.h=h;
		super.mTranslation=new float[16];
		super.mRotation=new float[16];
		super.mScale=new float[16];
		super.positio=new Point2f();
		Matrix.setIdentityM(super.mTranslation, 0);
		Matrix.setIdentityM(super.mRotation, 0);
		Matrix.setIdentityM(super.mScale, 0);
		
		
		triangleCoords = new float[] { 0, h, d, 0, 0,

		w, h, d, 1, 0,

		0, 0, d, 0, 1,
		
		0, 0, d, 0, 1,

		w, h, d, 1, 0,

		w, 0, d, 1, 1 };

		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (number of coordinate values * 4 bytes per float)
				triangleCoords.length * 4);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());

		// create a floating point buffer from the ByteBuffer
		vertexBuffer = bb.asFloatBuffer();
		// add the coordinates to the FloatBuffer
		vertexBuffer.put(triangleCoords);
		// set the buffer to read the first coordinate
		vertexBuffer.position(0);

		// prepare shaders and OpenGL program
		int vertexShader = GlGameScene.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);

		int fragmentShader = GlGameScene.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
		// to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
		// shader to program
		GLES20.glLinkProgram(mProgram); // create OpenGL program executables
		try {
			text = Loader.getInstance().getTexture(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Draw(float[] mvpMatrix) {

		
		//Matrix.multiplyMM(mvpMatrix, 0, mvpMatrix, 0, getResultMatrix(), 0);
		// Add program to OpenGL environment
		GLES20.glUseProgram(mProgram);

		vertexBuffer.position(0);
		// get handle to vertex shader's vPosition member
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
				false, (3 + 2) * 4, vertexBuffer);

		vertexBuffer.position(3);

		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aUv");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, true,
				(3 + 2) * 4, vertexBuffer);

		// get handle to shape's transformation matrix camPosition
		int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
				"uMVPMatrix");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uvRotation");
	
		

		GLES20.glUniform1f(mMVPMatrixHandle, -angle);
		//angle+=0.01f;
		
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, text);
		int mTextureUniformHandle;
		mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram,
				"u_Texture");

		GLES20.glUniform1i(mTextureUniformHandle, 0);

		// Draw the triangle
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, triangleCoords.length / 5);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		
		
		
	}

	@Override
	public void setPosition(Point2f pos) {
		Matrix.setIdentityM(super.mTranslation,0);
		Matrix.translateM(super.mTranslation, 0, pos.x, pos.y, 0);
		
	}

	@Override
	public Point2f getPosition() {
		// TODO Auto-generated method stub
		return super.positio;
	}

	@Override
	public void incremntPosition(float dX, float dY) {
		
		super.positio.x+=dX;
		super.positio.y+=dY;
		
		setPosition(new Point2f(super.positio.x, super.positio.y));
	
				
	}

	@Override
	public void setScale(float x, float y, float z) {
		float[] helper=new float[16];
		Matrix.scaleM(helper, 0, x, y, z);
		Matrix.multiplyMM(super.mScale, 0, super.mScale, 0, helper, 0);
		
	}

	@Override
	public void rotateX(float angle) {
		//float[] helper=new float[16];
		Matrix.rotateM(super.mRotation, 0, angle,1, 0, 0);
		//Matrix.multiplyMM(super.mRotation, 0, super.mRotation, 0, helper, 0);
		
				
	}

	@Override
	public void rotateY(float angle) {
		//float[] helper=new float[16];
		Matrix.rotateM(super.mRotation, 0, angle,0, 1, 0);
		//Matrix.multiplyMM(super.mRotation, 0, super.mRotation, 0, helper, 0);
		
	}

	@Override
	public void rotateZ(float angle) {
		//float[] helper=new float[16];
		//Matrix.setIdentityM(super.mRotation, 0);
		Matrix.rotateM(super.mRotation, 0, angle,0, 0, 1);
		//Matrix.multiplyMM(super.mRotation, 0, super.mRotation, 0, helper, 0);
		
	}
	public float[] getResultMatrix( ){
		float[] helper=new float[16];
		float[] helper1=new float[16];
		float[] identity=new float[16];
		
		Matrix.setIdentityM(identity, 0);
		
		
		
	
		//Matrix.multiplyMM(helper1, 0, identity, 0, super.mRotation, 0);
		
		Matrix.multiplyMM(helper, 0, super.mTranslation, 0, super.mRotation, 0);
		
		return helper;
		
		
	}

	@Override
	public void Update(float ratio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCLick(int dx, int dy) {
		// TODO Auto-generated method stub
		
	}

}
