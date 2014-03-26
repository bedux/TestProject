package com.example.testproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

class Model {
	public ByteBuffer byteBuffer;

}

public class Loader {

	static Loader thisInt = null;
	private Activity mainActivity = null;
	private String buttState = "null";
	private float x = 0;
	private float y = 0;
	private int width = 0;
	private int height = 0;

	public void setPosX(final float state) {
		x = state;

	}

	public float getPosX() {
		return x - 300;

	}

	public void setWidth(final int state) {
		width = state;

	}

	public float getWidth() {
		return width;

	}

	public void setHeight(final int state) {
		height = state;

	}

	public float getHeight() {
		return height;

	}

	public void setPosY(final float state) {
		y = state;

	}

	public float getPosY() {
		return y - 300;

	}

	public void setState(final String state) {
		buttState = state;

	}

	public String getState() {
		return buttState;

	}

	private Loader() {

	}

	public void setActivity(Activity _mainActivity) {

		mainActivity = _mainActivity;
	}

	public static Loader getInstance() {
		if (thisInt == null) {
			thisInt = new Loader();
		}
		return thisInt;

	}

	private static final String ns = null;


	

	

	public int getTexture(final String name) throws IOException {
		int mBrickDataHandle = loadTexture(mainActivity, name);
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mBrickDataHandle);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		
		return mBrickDataHandle;
	}

	public int loadTexture(final Context context, final String name)
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
