package com.emergentbit.krispysgamecontrollersamples;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class TriggerActivity extends Activity {
	
	private TriggerView mTriggerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mTriggerView = new TriggerView(this);
		setContentView(mTriggerView);
	}
	
	@Override
	public boolean dispatchGenericMotionEvent(MotionEvent ev) {
		mTriggerView.setGameController(new GameController(ev));
		mTriggerView.invalidate();
		return true;
	}

	public static class TriggerView extends View {

		private GameController mCurrentControllerState;
		private Paint paintBlack = new Paint();
		private Paint paintText = new Paint();
		
		public TriggerView(Context context) {
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
			
			float leftTriggerX = (0f + width/2)-200;
			float rightTriggerX = (0f + width/2)+200;
			float triggerHeight = 0f + height - 400;

			// Triggers
			
			canvas.drawCircle(leftTriggerX, triggerHeight, 100f, paintBlack); // left stick
			canvas.drawCircle(rightTriggerX, triggerHeight, 100f, paintBlack); // right stick
			if(mCurrentControllerState!=null && mCurrentControllerState.getLeftTrigger() != null) {
				float xaxis = mCurrentControllerState.getLeftTrigger().getValue() * 150;
				canvas.drawText("Left Trigger: " + xaxis/150f, leftTriggerX - 100, triggerHeight + 200, paintText);
				canvas.drawLine(leftTriggerX, triggerHeight, leftTriggerX, triggerHeight +xaxis, paintBlack);
			}
			
			if(mCurrentControllerState!=null && mCurrentControllerState.getRightTrigger() != null) {
				float xaxis = mCurrentControllerState.getRightTrigger().getValue() * 150;
				canvas.drawText("Right Trigger: " + xaxis/150f, leftTriggerX - 100, triggerHeight + 270, paintText);
				canvas.drawLine(rightTriggerX, triggerHeight, rightTriggerX, triggerHeight + xaxis, paintBlack);
			}

		}
	}

}
