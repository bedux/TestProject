package com.example.testproject;

import android.opengl.GLES20;

public class StaticUVPlane extends Shape2D {

	private final String vertexShaderCode =

	"uniform mat4 uMVPMatrix;"

	+ "attribute vec4 aPosition;"

	+ "attribute vec2 aUv;"

	+ "varying vec2 vUv;"

	+ "void main() {"

	+ "  gl_Position = uMVPMatrix * aPosition;"

	+ "vUv=aUv;"

	+ "}";

	private final String fragmentShaderCode = "precision mediump float;"
			+ "uniform sampler2D u_Texture;" + "varying vec2 vUv;"
			+ "void main() {" + "vec4 colorTxt=texture2D(u_Texture, vUv);"
			+ "  gl_FragColor =colorTxt ;" + "}";

	public StaticUVPlane(float w, float h, float d) {
		super(w, h, d);
		super.mPixelShader = fragmentShaderCode;
		super.mVertexShader = vertexShaderCode;
		super.mTextureId = StaticValue.ID_GROUND;
		super.nElementVertex=5;
		
		
	}
	
	@Override
	public void Start(){
		w/=StaticValue.ratio;
		super.mVertices = new float[] { 0, h, d, 0, 0,

		w, h, d, 1, 0,

		0, 0, d, 0, 1,

		0, 0, d, 0, 1,

		w, h, d, 1, 0,

		w, 0, d, 1, 1 };
		
	}
	
	@Override
	protected void SetShaderValue(GameCamera camera, float[] modelTransform){
		//call the parent method for set the vertex and matrixs on the shader
		super.SetShaderValue(camera, modelTransform);
		
		//load UVs into the shader
		super.mVertexBuffer.position(3);

		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aUv");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT, true,
				(super.nElementVertex) * 4, super.mVertexBuffer);
		
		//enable Texture
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, super.mTexture);
		int mTextureUniformHandle;
		
		//get set sample texture
		mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram,
				"u_Texture");

		GLES20.glUniform1i(mTextureUniformHandle, 0);
		
		
		
	}
	

}
