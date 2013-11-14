package com.emergentbit.krispysgamecontrollersamples;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MultiPlayerActivity extends Activity {
	
	private GameView mGameView;
	private int playerOneDevice = 0;
	private int playerTwoDevice = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGameView = new GameView(this);
		
		setContentView(mGameView);
	}
	
	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent ev) {
		if(playerOneDevice == 0) {
			playerOneDevice = ev.getDeviceId();
		} 
		
		if(ev.getDeviceId() == playerOneDevice){
			mGameView.setPlayerOneGameController(new GameController(ev));
			mGameView.invalidate();
		} else  {
			playerTwoDevice = ev.getDeviceId();
			mGameView.setPlayerTwoGameController(new GameController(ev));
			mGameView.invalidate();
		}
		return true;
	}
	
	public static class GameView extends View {
		
		private GameController mPlayerOneControllerState;
		private GameController mPlayerTwoControllerState;
		Paint paintBlack = new Paint();
		Paint paintText = new Paint();

		public GameView(Context context) {
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
		
		public void setPlayerOneGameController(GameController controller) {
			mPlayerOneControllerState = controller;
		}
		public void setPlayerTwoGameController(GameController controller) {
			mPlayerTwoControllerState = controller;
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
			
			if(mPlayerOneControllerState!=null && mPlayerOneControllerState.getLeftJoystick() != null) {
				float xaxis = mPlayerOneControllerState.getLeftJoystick().getHorizontalValue() * 150;
				float yaxis = mPlayerOneControllerState.getLeftJoystick().getVerticalValue() * 150;
				canvas.drawText("Player One: xaxis = " + xaxis + ", yaxis = "+yaxis, 100, 100, paintText);
				canvas.drawLine(leftStickX, stickHeight, leftStickX + xaxis, stickHeight + yaxis, paintBlack);
			}
			
			if(mPlayerTwoControllerState!=null && mPlayerTwoControllerState.getLeftJoystick() != null) {
				float xaxis = mPlayerTwoControllerState.getLeftJoystick().getHorizontalValue() * 150;
				float yaxis = mPlayerTwoControllerState.getLeftJoystick().getVerticalValue() * 150;
				canvas.drawText("Player Two: xaxis = " + xaxis + ", yaxis = "+yaxis, 100, 170, paintText);
				canvas.drawLine(rightStickX, stickHeight, rightStickX + xaxis, stickHeight + yaxis, paintBlack);
			}
			
		}
		
		
		
	}
	
}
