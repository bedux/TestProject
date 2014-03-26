package com.example.testproject;

public interface ShapeMotion {
	
	public  Point2f getPosition();
	public  void setPosition(Point2f pos);
	public  void incremntPosition(float dX,float dY);
	public  void setScale(float x,float y,float z);
	public  void rotateZ(float angle);
	public   float[] getResultMatrix( );

}
