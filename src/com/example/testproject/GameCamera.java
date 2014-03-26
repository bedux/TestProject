package com.example.testproject;

import android.opengl.Matrix;

public class GameCamera {
	private  float[] mMVPMatrix = new float[16];
	private  float[] mProjectionMatrix = new float[16];
	private  float[] mViewMatrix = new float[16];
	
	public GameCamera(float x,float y){
		
		Matrix.orthoM(mProjectionMatrix, 0, -7, 7, -7, 7, -1, 10);
		Matrix.setLookAtM(mViewMatrix, 0, x, y, 10, x, y, 0f, 0f, 1.0f, 0.0f);
	}
	
	
	public void SetPositon(float x,float y){
		  Matrix.setLookAtM(mViewMatrix, 0, x, y, 10, x, y, 0f, 0f, 1.0f, 0.0f);
	}
	
	public float[] getCameraMatrix(float[] word){
		

		float[]mvMatrix=new float[16];
		Matrix.multiplyMM(mvMatrix, 0, mViewMatrix, 0,word, 0);  
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mvMatrix, 0); 
		
		
		return mMVPMatrix;
	}
	
	
	
	
}
