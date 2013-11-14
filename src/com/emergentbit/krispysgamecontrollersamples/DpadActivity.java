package com.emergentbit.krispysgamecontrollersamples;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.emergentbit.krispysgamecontrollersamples.GameController.DPad;

public class DpadActivity extends Activity {

	private DPadView mDpadView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDpadView = new DPadView(this);
		setContentView(mDpadView);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
			mDpadView.setGameController(new GameController(event));
			mDpadView.invalidate();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	public static class DPadView extends View {

		private GameController mCurrentControllerState;
		private Paint paintBlack = new Paint();
		private Paint paintThick = new Paint();

		public DPadView(Context context) {
			super(context);
			paintBlack.setAntiAlias(true);
			paintBlack.setStrokeWidth(6f);
			paintBlack.setColor(Color.BLACK);
			paintBlack.setStyle(Paint.Style.STROKE);
			paintBlack.setStrokeJoin(Paint.Join.ROUND);

			paintThick.setStrokeWidth(25f);
			paintThick.setColor(Color.RED);
			paintThick.setStyle(Paint.Style.STROKE);
		}

		public void setGameController(GameController controller) {
			mCurrentControllerState = controller;
		}

		@Override
		public void draw(Canvas canvas) {
			int width = canvas.getWidth();
			int height = canvas.getHeight();

			// DPAD
			canvas.drawCircle(300f, height - height/3, 200f, paintBlack);

			if (mCurrentControllerState != null) {
				DPad dpad = mCurrentControllerState.getDpad();
				if (dpad.getDirectionArray()[DPad.LEFT]) {
					canvas.drawLine(300f, height - height/3, 100f, height - height/3,	paintThick);
				}
				if (dpad.getDirectionArray()[DPad.RIGHT]) {
					canvas.drawLine(300f, height - height/3, 500f, height - height/3,	paintThick);
				}
				if (dpad.getDirectionArray()[DPad.UP]) {
					canvas.drawLine(300f, height - height/3, 300f, height - height/3 - 200,	paintThick);
				}
				if (dpad.getDirectionArray()[DPad.DOWN]) {
					canvas.drawLine(300f, height - height/3, 300f, height - height/3 + 200,	paintThick);
				}
			}

		}
	}
}
