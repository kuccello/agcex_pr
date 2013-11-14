package com.emergentbit.krispysgamecontrollersamples;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class JoystickActivity extends Activity {
	
	private JoystickView mJoystickView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Show initial wait for input
		mJoystickView = new JoystickView(this);
		setContentView(mJoystickView);
	}
	
	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent ev) {
		mJoystickView.setGameController(new GameController(ev));
		mJoystickView.invalidate();
		return true;
	}

	public static class JoystickView extends View {

		private GameController mCurrentControllerState;
		Paint paintBlack = new Paint();
		Paint paintText = new Paint();
		
		public JoystickView(Context context) {
			super(context);
			
			paintBlack.setAntiAlias(true);
	        paintBlack.setStrokeWidth(6f);
	        paintBlack.setColor(Color.BLACK);
	        paintBlack.setStyle(Paint.Style.STROKE);
	        paintBlack.setStrokeJoin(Paint.Join.ROUND);
	        
	        paintText.setAntiAlias(true);
	        paintText.setColor(Color.BLUE);
	        paintText.setTextSize(72f);
		}
		
		public void setGameController(GameController controller) {
			mCurrentControllerState = controller;
		}

		@Override
		public void draw(Canvas canvas) {
			
			int width = canvas.getWidth();
			int height = canvas.getHeight();
			
			float leftStickX = (0f + width/2)-200;
			float rightStickX = (0f + width/2)+200;
			float stickHeight = 0f + height - 400;
			float stickRadius = 150f;
			
			canvas.drawCircle(leftStickX, stickHeight, stickRadius, paintBlack); // left stick
			canvas.drawCircle(rightStickX, stickHeight, stickRadius, paintBlack); // right stick
			
			if(mCurrentControllerState!=null && mCurrentControllerState.getLeftJoystick() != null) {
				float xaxis = mCurrentControllerState.getLeftJoystick().getHorizontalValue() * 150;
				float yaxis = mCurrentControllerState.getLeftJoystick().getVerticalValue() * 150;
				canvas.drawText("Left Stick: xaxis = " + xaxis + ", yaxis = "+yaxis, 100, 100, paintText);
				canvas.drawLine(leftStickX, stickHeight, leftStickX + xaxis, stickHeight + yaxis, paintBlack);
			}
			
			if(mCurrentControllerState!=null && mCurrentControllerState.getRightJoystick() != null) {
				float xaxis = mCurrentControllerState.getRightJoystick().getHorizontalValue() * 150;
				float yaxis = mCurrentControllerState.getRightJoystick().getVerticalValue() * 150;
				canvas.drawText("Right Stick: xaxis = " + xaxis + ", yaxis = "+yaxis, 100, 170, paintText);
				canvas.drawLine(rightStickX, stickHeight, rightStickX + xaxis, stickHeight + yaxis, paintBlack);
			}
			
			
			// Buttons
//			canvas.drawCircle(100f + (width-350), 100f + (height - 250), 100f, paintBlack);
//			canvas.drawText("A", 100f + (width-350) - 36, 100f + (height - 250) + 36, paintText);
//
//			canvas.drawCircle(100f + (width-200), 100f + (height - 400), 100f, paintBlack);
//			canvas.drawText("B", 100f + (width-200) - 36, 100f + (height - 400) + 36, paintText);
//
//			canvas.drawCircle(100f + (width-500), 100f + (height - 400), 100f, paintBlack);
//			canvas.drawText("X", 100f + (width-500) - 36, 100f + (height - 400) + 36, paintText);
//
//			canvas.drawCircle(100f + (width-350), 100f + (height - 550), 100f, paintBlack);
//			canvas.drawText("Y", 100f + (width-350) - 36, 100f + (height - 550) + 36, paintText);
			
			// DPAD
//			canvas.drawCircle(300f,height-600,200f,paintBlack);
//			canvas.drawRect(100f, height-600+150, 500f, height-600-150, paintBlack);
		}
		
	}
	
}
