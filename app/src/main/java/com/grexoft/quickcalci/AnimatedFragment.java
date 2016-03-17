package com.grexoft.quickcalci;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public abstract class AnimatedFragment extends Fragment {

	protected SingleFragmentActivity referenceCallback;

	protected boolean animationEnabled = true;

	public static final int ANIMATION_DURATION = CalculatorApplication.ANIMATION_DURATION;

	public static final String TAG = CalculatorApplication.TAG;

	protected View fragmentView;

	protected int layoutId;

	protected boolean canPerformEnterAnimation;

	protected boolean canPerformExitAnimation;

//	protected MySingleton obj = MySingleton.getInstance();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setLayoutId();

		canPerformEnterAnimation = true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		fragmentView = inflater.inflate(layoutId, container, false);

		init();

		fragmentView.setVisibility(View.INVISIBLE);

		return fragmentView;
	}

	@Override
	public void onStart() {

		super.onStart();

		referenceCallback.resolveReference(this);

		fragmentView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {

					}
				});

		if (isAnimationEnabled() && canPerformEnterAnimation) {

			canPerformEnterAnimation = false;

			canPerformExitAnimation = true;

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					fragmentView.setVisibility(View.VISIBLE);

					performEnterAnimation();

				}
			}, 500);
		}

		else {
			Log.v(TAG, "on resume of animated fragment");
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {

			referenceCallback = (SingleFragmentActivity) activity;

		} catch (ClassCastException e) {

			throw new ClassCastException(activity.toString()
					+ " must implement Callback");
		}
	}

	public synchronized boolean isAnimationEnabled() {
		return animationEnabled;
	}

	public synchronized void setAnimationEnabled(boolean animationEnabled) {
		this.animationEnabled = animationEnabled;
	}

	public void setExitAnimation() {
		
		if (animationEnabled && canPerformExitAnimation){
			
			canPerformEnterAnimation = true;

			canPerformExitAnimation = false;
			
			performExitAnimation();
		}

		
	}

	// this method must be implemented for enter animation of a fragment
	public abstract void performEnterAnimation();

	// this method must be implemented for exit animation of a fragment
	public abstract void performExitAnimation();

	public abstract void setLayoutId();

	public abstract void init();

}
