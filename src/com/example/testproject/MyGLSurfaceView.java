/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.testproject;





import android.content.Context;
import android.opengl.GLSurfaceView;


/**
 * A view container where OpenGL ES graphics can be drawn on screen. This view
 * can also be used to capture touch events, such as a user interacting with
 * drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

	private final Scene mRenderer;
	Car c;
	public MyGLSurfaceView(Context context) {
		super(context);

		setEGLContextClientVersion(2);
		mRenderer = new Scene();
		 c=new Car(2,2,3);
		 
		 StaticUVPlane pa=new StaticUVPlane(100,100,1);
		mRenderer.addGlElement(pa);
		mRenderer.addGlElementTag((ShapeMotion)c);
		mRenderer.addGlListener((ActionListener)c);
	
		setRenderer(mRenderer);
		this.setDebugFlags(GLSurfaceView.DEBUG_LOG_GL_CALLS);

	}
	
	public void onTouch(final int x,final int y) {

		  queueEvent(new Runnable() {
              public void run() {
              	mRenderer.onAction(x, y);
              	
              	
              }});

	}

	
	@Override
	public void onPause(){
		super.onPause();
	}
	@Override
	public void onResume(){
		super.onResume();
	}
}
