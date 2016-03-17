package com.grexoft.quickcalci;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class SingleFragmentActivity extends FragmentActivity {

	public static final int PENDING_TRANSACTION_INTENDED = 0;

	public static final int PENDING_TRANSACTION_ON_EXCEPTION = 1;

	public static final int NO_FRAGMENT = -1;

	protected FragmentManager fragmentManager;

	protected int topFragment;

	protected AnimatedFragment fragments[];

	protected int fragmentContainer;

	protected Bundle pendingFragmentTransaction;

	protected int BaseFragment;

	public static final int ANIMATION_DURATION = CalculatorApplication.ANIMATION_DURATION;

	public static final String TAG = CalculatorApplication.TAG;
	
	

	// public static final String SHARED_PREFERENCE_NAME =
	// CommonUtilities.SHARED_PREFERENCE_NAME;

//	MySingleton obj = MySingleton.getInstance();

	protected SharedPreferences settings;

	protected boolean mIsBound = false;	
	
	protected static boolean startMusic;
	
		

	protected void setFragmentParams(int totalFragments, int fragmentContainer,
			int BaseFragment) {

		topFragment = NO_FRAGMENT;

		fragmentManager = getSupportFragmentManager();

		pendingFragmentTransaction = null;

		fragments = new AnimatedFragment[totalFragments];

		this.fragmentContainer = fragmentContainer;

		this.BaseFragment = BaseFragment;

	}
	
	@Override
	public void onBackPressed() {
		
		if (topFragment > BaseFragment) switchToFragment(BaseFragment, null);
		
		else  finish();
	}
	
	

	/*
	 * This method provides only half functionality for switching to fragment.
	 * 
	 * Subclasses need to call this method from initializeFragment(final int
	 * fragmentId, Bundle args)
	 * 
	 * after initializing their fragment
	 */

	protected void switchToFragment(final int fragmentId, 
			Bundle args) {

		// Log.v(TAG, "topFragment before transaction : " + topFragment);

		// this method presumes that a sub class overridden method has
		// been implemented to initialize the fragment
		
		initializeFragment(fragmentId, args);

		if (fragments[fragmentId] == null || topFragment == fragmentId)
			return;

		try {

			if (topFragment > BaseFragment) {

				if (fragmentId > BaseFragment)
					fragments[BaseFragment].setAnimationEnabled(false);

				if (fragments[topFragment].isAnimationEnabled()) {

					fragments[topFragment].setExitAnimation();

					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {

							fragmentManager.popBackStack();

							topFragment = BaseFragment;

							if (topFragment == fragmentId) {

								initVariablesForFragment(fragmentId);
								return;
							}

						}
					}, ANIMATION_DURATION);
				} else {

					fragmentManager.popBackStack();

					topFragment = BaseFragment;

					if (topFragment == fragmentId) {

						initVariablesForFragment(fragmentId);
						return;
					}
				}

			}

			if (args != null) {

				fragments[fragmentId].setArguments(args);
			}

			final FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();

			if (topFragment == NO_FRAGMENT) {

				fragmentTransaction.add(fragmentContainer,
						fragments[fragmentId]);

				fragmentTransaction.commit();

				topFragment = fragmentId;

				initVariablesForFragment(fragmentId);

			} else {

				fragmentTransaction.replace(fragmentContainer,
						fragments[fragmentId]).addToBackStack(null);

				if (fragments[topFragment].isAnimationEnabled()) {

					fragments[topFragment].setExitAnimation();

					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {

							fragmentTransaction.commit();

							topFragment = fragmentId;

							initVariablesForFragment(fragmentId);

						}
					}, ANIMATION_DURATION);

				}

				else {

					fragmentTransaction.commit();

					fragments[topFragment].setAnimationEnabled(true);

					topFragment = fragmentId;

					initVariablesForFragment(fragmentId);

				}

			}

			// Log.v(TAG, "topFragment after transaction : " + topFragment);

		}

		catch (IllegalStateException ex) {

			createPendingFragmentTransaction(PENDING_TRANSACTION_ON_EXCEPTION,
					fragmentId, args);

			ex.printStackTrace();

		}
	}

	@Override
	protected void onPostResume() {

		super.onPostResume();

		if (pendingFragmentTransaction != null) {

			int transactionType = pendingFragmentTransaction
					.getInt("transactionType");

			if (transactionType == PENDING_TRANSACTION_ON_EXCEPTION) {

				handlePendingFragmentTransaction();

			}
		}

	}

	protected void createPendingFragmentTransaction(int transactionType,
			int fragmentId, Bundle args) {

		pendingFragmentTransaction = new Bundle();

		pendingFragmentTransaction.putInt("transactionType", transactionType);

		pendingFragmentTransaction.putInt("fragmentId", fragmentId);

		pendingFragmentTransaction.putBundle("args", args);

	}

	protected void handlePendingFragmentTransaction() {

		if (pendingFragmentTransaction != null) {

			int fragmentId = pendingFragmentTransaction.getInt("fragmentId");

			Bundle args = pendingFragmentTransaction.getBundle("args");

			pendingFragmentTransaction = null;

			switchToFragment(fragmentId, args);
		}
	}

	public void resolveReference(AnimatedFragment animatedFragment) {

		fragments[topFragment] = animatedFragment;

	}
	
	@Override
	public void finish() {
		
		
		super.finish();
		
	}

	/*
	 * This method must be implemented by all subclasses to initialize their
	 * fragments.
	 * 
	 * This method must call switchToFragment(final int fragmentId, boolean
	 * popBackStack, Bundle args) in the end
	 */

	protected abstract void initializeFragment(final int fragmentId, Bundle args);

	/*
	 * This method must be implemented by subclasses to initialize values for
	 * fragments
	 */

	protected abstract void initVariablesForFragment(int fragmentId);

}
