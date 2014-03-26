package com.example.testproject;

import android.opengl.Matrix;
import android.util.Log;

public class Car extends UVPlane implements ShapeMotion, ActionListener {

	protected float[] mTranslation = StaticValue.identity.clone();
	protected float[] mRotation = StaticValue.identity.clone();
	protected float[] mScale = StaticValue.identity.clone();
	protected Point2f position;
	protected float currentAngle = 0;
	protected float speed = 0.2f;
	protected ParticelSystem pa;

	public Car(float w, float h, float d) {
		super(w, h, d);
		position = new Point2f();
		super.mTextureId = StaticValue.ID_CAR_RED;

	}

	@Override
	public void Start() {
		pa = new ParticelSystem(900);
		super.Start();

	}

	public Point2f getAbsolutePosition() {

		return new Point2f(position.x * StaticValue.ratio, position.y);

	}

	@Override
	public Point2f getPosition() {

		return position;
	}

	@Override
	public void setPosition(Point2f pos) {
		pos.x/=StaticValue.ratio;
		position = pos;
		if (pa != null) {
			pa.incremntPosition(this.position.x + super.w / 2, this.position.y
					+ super.h / 2);
		}
		Matrix.setIdentityM(mTranslation, 0);
		Matrix.translateM(mTranslation, 0, pos.x, pos.y, 0);

	}

	@Override
	public void incremntPosition(float dX, float dY) {
		position.x += dX;
		position.y += dY;

		setPosition(this.getAbsolutePosition());

	}

	@Override
	public void setScale(float x, float y, float z) {
		float[] helper = new float[16];
		Matrix.scaleM(helper, 0, x, y, z);
		Matrix.multiplyMM(mScale, 0, mScale, 0, helper, 0);

	}

	@Override
	public void rotateZ(float angle) {
		currentAngle = angle;
		// Matrix.rotateM(mRotation, 0, angle, 0, 0, 1);

	}

	@Override
	public float[] getResultMatrix() {
		float[] helper = new float[16];
		Matrix.multiplyMM(helper, 0, mTranslation, 0, mRotation, 0);

		return helper;

	}

	public void Draw(GameCamera camera, float[] modelTransform) {
		pa.Draw(camera.getCameraMatrix(StaticValue.identity.clone()));
		Update(1);

		super.Draw(camera, getResultMatrix());

	}

	public void Update(float ratio) {
		Log.v("pos", String.valueOf(this.getAbsolutePosition().x));
		float xa = ((float) Math.sin(currentAngle) * speed) / StaticValue.ratio;
		float ya = (float) Math.cos(currentAngle) * speed;
		this.setAngle(currentAngle);
		incremntPosition(xa, ya);
	}

	@Override
	public void OnClict(float dx, float dy) {
		if (dx > 200) {
			//
			currentAngle -= ((Math.PI / 4) / 45) * 2.5f;
		} else {
			currentAngle += ((Math.PI / 4) / 45) * 2.5f;
			//
		}
		// TODO Auto-generated method stub

	}

}
