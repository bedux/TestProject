package com.example.testproject;

import android.opengl.GLES20;

public class UVPlane extends StaticUVPlane{

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
	private float angle=0;
	
	public void incrementAngle(float l){
		angle+=l;
		
	}
	public void setAngle(float l){
		angle=l;
		
	}
	public UVPlane(float w, float h, float d) {
		super(w, h, d);
		super.mTextureId=StaticValue.ID_GROUND;
		super.mVertexShader=vertexShaderCode;
		
	}
	protected void SetShaderValue(GameCamera camera, float[] modelTransform){
		super.SetShaderValue(camera, modelTransform);
		
		int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uvRotation");
		GLES20.glUniform1f(mMVPMatrixHandle, -angle);
		
		
	}

}
