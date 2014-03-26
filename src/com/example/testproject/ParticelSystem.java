package com.example.testproject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Random;

import android.opengl.GLES20;

import android.util.Log;

public class ParticelSystem  {

	private int count = 0;
	private int insert=0;
	private final int maxNum;
	private float[] triangleCoords;
	private int mProgram;
	private FloatBuffer vertexBuffer;

	private final String vertexShaderCode =

	"uniform mat4 uMVPMatrix;" + "attribute vec4 aPosition;"

	+ "void main() {" + "  gl_Position = uMVPMatrix * aPosition;"+
	//"gl_PointSize =25.0;"

	//+
	"}";

	private final String fragmentShaderCode = "precision mediump float;"

	+ "void main() {" +
	
	"  gl_FragColor =vec4(1.0,0.0,0.0,1.0) ;" + "}";

	public ParticelSystem(int maxCount) {
		maxNum=maxCount;


		triangleCoords = new float[maxCount * 3];

		// prepare shaders and OpenGL program
		int vertexShader = Scene.loadShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);

		int fragmentShader = Scene.loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
		// to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
		// shader to program
		GLES20.glLinkProgram(mProgram); // create OpenGL program executables

	}
	Random rn = new Random();
	public void Draw(float[] mvpMatrix) {
		if (count > 1) {

			GLES20.glUseProgram(mProgram);

			vertexBuffer.position(0);
			// get handle to vertex shader's vPosition member
			int mPositionHandle = GLES20.glGetAttribLocation(mProgram,
					"aPosition");
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT,
					false, (3) * 4, vertexBuffer);

			int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");

			// Apply the projection and view transformation
			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

			// Draw the triangle
			
			GLES20.glLineWidth(20);
			
			GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, count);
			GLES20.glDrawArrays(GLES20.GL_POINTS, 0, count);

			// Disable vertex array
			GLES20.glDisableVertexAttribArray(mPositionHandle);
		}
		// TODO Auto-generated method stub

	}

	
	public void incremntPosition(float dX, float dY) {
		Log.i("Mamma", String.valueOf(insert));
		if (((insert+1)) > maxNum) {
			
			insert=0;
		
		}
		
		if(count+2<maxNum){count++;}else{
			
			System.arraycopy(triangleCoords,3,triangleCoords,0,triangleCoords.length-3);
			
		}
			Log.i("Mamma", String.valueOf(count));
			triangleCoords[3 * count] = dX;
			triangleCoords[3 * count + 1] = dY;
			triangleCoords[3 * count + 2] = 0.5f;
			
			insert++;
			// initialize vertex byte buffer for shape coordinates
			ByteBuffer bb = ByteBuffer.allocateDirect(
			// (number of coordinate values * 4 bytes per float)
					count * 3 * 4);
			// use the device hardware's native byte order
			bb.order(ByteOrder.nativeOrder());

			// create a floating point buffer from the ByteBuffer
			vertexBuffer = bb.asFloatBuffer();
			// add the coordinates to the FloatBuffer
			vertexBuffer.put(Arrays.copyOf(triangleCoords, count * 3));
			// set the buffer to read the first coordinate
			vertexBuffer.position(0);
		

	}

	

}
