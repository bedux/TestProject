package com.example.testproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class ResourceManager {
	private static ResourceManager instance = null;
	private Activity mainActivity;

	public void setActivity(Activity mainActivity) {
		this.mainActivity = mainActivity;
	}

	public Activity getActivity() {
		return this.mainActivity;
	}

	private Map<Integer, Integer> textureHasMap;


	private ResourceManager() {
		textureHasMap = new HashMap<Integer, Integer>();

	}

	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}

	/**
	 * this function load all the resource on the scene create.
	 * */
	public void LoadTextureResurcesStatic() {
		textureHasMap.put(StaticValue.ID_GROUND, getTexture("test1.png"));
		textureHasMap.put(StaticValue.ID_CAR_RED, getTexture("car.png"));
	}
	/**
	 * if you need to load a textur in real time use this function. You can get the result by call the method getResource() and pass your id.
	 * @param fileName file name of the file in the asset folder
	 * @param id is the name that the texture is stored in the hashMap. 
	 * 
	 * */
	public void LoadTextureRealTime(String fileName,int ID){
		textureHasMap.put(ID, getTexture(fileName));
		
	}
	
	/**
	 * Get the resource
	 * 
	 * */
	public int getResource(int id){
		return textureHasMap.get(id);
		
	}

	private int getTexture(final String name) {
		int mBrickDataHandle;
		try {
			mBrickDataHandle = loadTexture(mainActivity, name);
			GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBrickDataHandle);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
					GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			return mBrickDataHandle;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;

	}

	private int loadTexture(final Context context, final String name)
			throws IOException {
		final int[] textureHandle = new int[1];

		GLES20.glGenTextures(1, textureHandle, 0);

		if (textureHandle[0] != 0) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false; // No pre-scaling

			// Read in the resource
			AssetManager am = mainActivity.getAssets();
			InputStream is = am.open(name);
			final Bitmap bitmap = BitmapFactory.decodeStream(is);

			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();
		}

		if (textureHandle[0] == 0) {
			throw new RuntimeException("Error loading texture.");
		}

		return textureHandle[0];
	}

}
