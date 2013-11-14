package com.emergentbit.krispysgamecontrollersamples;

import android.view.InputDevice;
import android.view.InputDevice.MotionRange;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Represents a snapshot instance of a game controller state 
 *
 */
public class GameController {
	/**
	 * Represents an analog control on the game controller - like a Joystick
	 */
	public static class Joystick {
		private final int mHorizontalAxisId;
		private final int mVerticalAxisId;
		private final float mCurrentHorizontalAxisValue;
		private final float mCurrentVerticalAxisValue;
		private final float[] mHorizontalAxisValues;
		private final float[] mVerticalAxisValues;

		/**
		 * 
		 * @param event
		 * @param axisX - the integer id of the horizontal axis
		 * @param axisY - the integer id of the vertical axis
		 * @see {@link MotionEvent#AXIS_X} for an example axis id
		 */
		Joystick(InputEvent event, int axisX, int axisY) {
			InputDevice device = event.getDevice();

			if (event instanceof MotionEvent) {
				MotionEvent motionEvent = (MotionEvent) event;
				int historySize = motionEvent.getHistorySize();
				mHorizontalAxisValues = new float[historySize];
				mVerticalAxisValues = new float[historySize];
				
				// Capture horizontal axis
				MotionRange rangeX = device.getMotionRange(axisX, InputDevice.SOURCE_JOYSTICK);
				if (rangeX == null) {
					rangeX = device.getMotionRange(axisX, InputDevice.SOURCE_GAMEPAD);
				}
				if (rangeX != null) {
					mHorizontalAxisId = rangeX.getAxis();
					for(int historyPt = 0; historyPt < historySize; historyPt +=1) {
						mHorizontalAxisValues[historyPt] = motionEvent.getHistoricalAxisValue(axisX, historyPt);
					}
					mCurrentHorizontalAxisValue = motionEvent.getAxisValue(axisX);
				} else {
					mHorizontalAxisId = -1;
					mCurrentHorizontalAxisValue = 0.0f;
				}
				
				// Capture vertical axis
				MotionRange rangeY = device.getMotionRange(axisY, InputDevice.SOURCE_JOYSTICK);
				if (rangeY == null) {
					rangeY = device.getMotionRange(axisY, InputDevice.SOURCE_GAMEPAD);
				}
				if (rangeY != null) {
					mVerticalAxisId = rangeY.getAxis();
					for(int historyPt = 0; historyPt < historySize; historyPt +=1) {
						mVerticalAxisValues[historyPt] = motionEvent.getHistoricalAxisValue(axisY, historyPt);
					}
					mCurrentVerticalAxisValue = motionEvent.getAxisValue(axisY);
				} else {
					mVerticalAxisId = -1;
					mCurrentVerticalAxisValue = 0.0f;
				}
			} else {
				mHorizontalAxisId = -1;
				mVerticalAxisId = -1;
				mHorizontalAxisValues = null;
				mVerticalAxisValues = null;
				mCurrentHorizontalAxisValue = 0.0f;
				mCurrentVerticalAxisValue = 0.0f;
			}
		}

		public float getHorizontalValue() {
			return mCurrentHorizontalAxisValue;
		}
		
		public float getVerticalValue() {
			return mCurrentVerticalAxisValue;
		}
		
	}

	public static class Trigger {
		private final int mAxisId;
		private final float mCurrentValue;
		
		Trigger(InputEvent event, int axis) {
			InputDevice device = event.getDevice();
			
			if (event instanceof MotionEvent) {
				MotionEvent motionEvent = (MotionEvent) event;
				
				MotionRange range = device.getMotionRange(axis, InputDevice.SOURCE_JOYSTICK);
				if(range==null) {
					range = device.getMotionRange(axis, InputDevice.SOURCE_GAMEPAD);
				}
				if(range!=null) {
					mAxisId = range.getAxis();
					mCurrentValue = motionEvent.getAxisValue(axis);
				} else {
					mAxisId = -1;
					mCurrentValue = -1.0f;
				}
			} else {
				mAxisId = -1;
				mCurrentValue = -1.0f;
			}
			
		}

		public float getValue() {
			return mCurrentValue;
		}

	}

	public static class Button {
		// Left as a todo
	}
	
	public static class DPad {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
		public static final int UP = 2;
		public static final int DOWN = 3;
		private final boolean[] mDirection = {false,false,false,false};
		
		public DPad(InputEvent event) {
			if(event instanceof MotionEvent) {
				// Hat trick
				MotionEvent motionEvent = (MotionEvent) event;
				float xaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
				float yaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);
				
				if(xaxis < -0.5f) {
					mDirection[LEFT] = true;
				} else if (xaxis > 0.5f) {
					mDirection[RIGHT] = true;
				}
				
				if(yaxis < -0.5f) {
					mDirection[UP] = true;
				} else if (yaxis > 0.5f) {
					mDirection[DOWN] = true;
				}
				
			} else if (event instanceof KeyEvent) {
				// Button event
				KeyEvent keyEvent = (KeyEvent) event;
				if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
					mDirection[LEFT] = true;
				} else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
					mDirection[RIGHT] = true;
				} else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
					mDirection[UP] = true;
				} else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
					mDirection[DOWN] = true;
				}
			}
		}
		
		public boolean[] getDirectionArray() {
			boolean[] copy = new boolean[4];
			copy[LEFT] = mDirection[LEFT];
			copy[RIGHT] = mDirection[RIGHT];
			copy[UP] = mDirection[UP];
			copy[DOWN] = mDirection[DOWN];
			return copy;
		}
	}

	private final InputDevice mDevice;

	private Joystick mLeftJoystick;
	private Joystick mRightJoystick;
	private DPad mDpad;
	private Trigger mLeftTrigger;
	private Trigger mRightTrigger;

	public GameController(KeyEvent event) {
		mDevice = event.getDevice();
		mLeftJoystick = new Joystick(event, MotionEvent.AXIS_X, MotionEvent.AXIS_Y);
		mRightJoystick = new Joystick(event, MotionEvent.AXIS_X, MotionEvent.AXIS_Y);
		mDpad = new DPad(event);
		
		mLeftTrigger = new Trigger(event, MotionEvent.AXIS_LTRIGGER);
		Trigger mLeftTriggerTemp = new Trigger(event, MotionEvent.AXIS_BRAKE);
		if(mLeftTriggerTemp.getValue() != 0.0f && mLeftTrigger.getValue() == 0.0f) {
			mLeftTrigger = mLeftTriggerTemp;
		}
		
		mRightTrigger = new Trigger(event, MotionEvent.AXIS_RTRIGGER);
		Trigger mRightTriggerTemp = new Trigger(event, MotionEvent.AXIS_GAS);
		if(mRightTriggerTemp.getValue() != 0.0f && mRightTrigger.getValue() == 0.0f) {
			mRightTrigger = mRightTriggerTemp;
		}
	}

	public GameController(MotionEvent event) {
		mDevice = event.getDevice();
		mLeftJoystick = new Joystick(event, MotionEvent.AXIS_X, MotionEvent.AXIS_Y);
		mRightJoystick = new Joystick(event, MotionEvent.AXIS_Z, MotionEvent.AXIS_RZ);
		mDpad = new DPad(event);
		
		mLeftTrigger = new Trigger(event, MotionEvent.AXIS_LTRIGGER);
		Trigger mLeftTriggerTemp = new Trigger(event, MotionEvent.AXIS_BRAKE);
		if(mLeftTriggerTemp.getValue() != 0.0f && mLeftTrigger.getValue() == 0.0f) {
			mLeftTrigger = mLeftTriggerTemp;
		}
		
		mRightTrigger = new Trigger(event, MotionEvent.AXIS_RTRIGGER);
		Trigger mRightTriggerTemp = new Trigger(event, MotionEvent.AXIS_GAS);
		if(mRightTriggerTemp.getValue() != 0.0f && mRightTrigger.getValue() == 0.0f) {
			mRightTrigger = mRightTriggerTemp;
		}
	}

	public GameController(InputDevice device) {
		mDevice = device;

		mLeftJoystick = null;
		mRightJoystick = null;
		mDpad = null;
		mLeftTrigger = null;
		mRightTrigger = null;
	}
	
	public Joystick getLeftJoystick() {
		return mLeftJoystick;
	}
	
	public Joystick getRightJoystick() {
		return mRightJoystick;
	}
	
	public Trigger getLeftTrigger() {
		return mLeftTrigger;
	}

	public Trigger getRightTrigger() {
		return mRightTrigger;
	}
	
	public DPad getDpad() {
		return mDpad;
	}

	public static int getGameControllerCount() {
		int finalCount = 0;
		int[] deviceIds = InputDevice.getDeviceIds();
		for(int idx = 0; idx<deviceIds.length;idx+=1) {
			InputDevice device = InputDevice.getDevice(deviceIds[idx]);
			if(isGameInputDevice(device)) {
				finalCount += 1;
			}
		}
		return finalCount;
	}


	public static boolean isGameInputDevice(InputDevice device) {
		return (((device.getSources() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) ||
				((device.getSources() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK));
	}
}
