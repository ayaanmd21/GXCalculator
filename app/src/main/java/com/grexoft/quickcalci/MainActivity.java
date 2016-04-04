package com.grexoft.quickcalci;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import java.util.ArrayList;

import support.NavDrawerItem;

@SuppressLint("RtlHardcoded") public class MainActivity extends SingleFragmentActivity {

	private DrawerLayout mDrawerLayout;

	private ListView mDrawerList;

	private String[] navMenuTitles;

	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;

	private NavDrawerListAdapter adapter;

	private LinearLayout mLinearLayout;

	public static final int CALCULATOR_FRAGMENT = 0;

	public static final int ANSWERS_FRAGMENT = 1;

	public static final int HELP_FRAGMENT = 2;

	public static final int ABOUT_FRAGMENT = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		AdBuddiz.setPublisherKey("8ab09792-d37d-4cff-bedd-66414deb2cc1");
		AdBuddiz.cacheAds(this);
		AdBuddiz.RewardedVideo.fetch(this);
		mLinearLayout = (LinearLayout) findViewById(R.id.linearout);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout.setScrimColor(Color.TRANSPARENT);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		for (int i = 0; i < navMenuTitles.length; i++) {

			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));
		}

		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		adapter = new NavDrawerListAdapter(navDrawerItems);

		mDrawerList.setAdapter(adapter);

		setFragmentParams(4, R.id.frame_container, CALCULATOR_FRAGMENT);

		if (savedInstanceState == null) {
			
			mDrawerList.performItemClick(
			        mDrawerList.getAdapter().getView(CALCULATOR_FRAGMENT, null, null),
			        CALCULATOR_FRAGMENT,
			        mDrawerList.getAdapter().getItemId(CALCULATOR_FRAGMENT));
		}

	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@SuppressLint("RtlHardcoded") @Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			mDrawerLayout.closeDrawer(Gravity.LEFT);

			if (position == 4) {

				Intent intent = new Intent(Intent.ACTION_VIEW);
			    //Try Google play
			    intent.setData(Uri.parse("market://details?id=" + CalculatorApplication.APPLICATION_ID));
			    if (!MyStartActivity(intent)) {
			        //Market (Google play) app seems not installed, let's try to open a webbrowser
			        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + CalculatorApplication.APPLICATION_ID));
			        if (!MyStartActivity(intent)) {
			            //Well if this also fails, we have run out of options, inform the user.
			            Toast.makeText(getApplicationContext(), "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
			        }
			    }
			}
			else if (position == 5){
				finish();
			}
			else {
				switchToFragment(position, null);
			}
			
			
		}
	}
	
	private boolean MyStartActivity(Intent aIntent) {
	    try
	    {
	        startActivity(aIntent);
	        return true;
	    }
	    catch (ActivityNotFoundException e)
	    {
	        return false;
	    }
	}

	@SuppressLint("InflateParams") public class NavDrawerListAdapter extends BaseAdapter {

		private ArrayList<NavDrawerItem> navDrawerItems;

		private LayoutInflater mInflater;

		public NavDrawerListAdapter(ArrayList<NavDrawerItem> navDrawerItems) {

			this.navDrawerItems = navDrawerItems;

			mInflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return navDrawerItems.size();
		}

		@Override
		public Object getItem(int position) {
			return navDrawerItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// Log.v(TAG, navDrawerItems.get(position).toString());

			RelativeLayout listItemLayout;

			if (convertView != null) {

				listItemLayout = (RelativeLayout) convertView;
			}

			else {

				listItemLayout = (RelativeLayout) mInflater.inflate(
						R.layout.drawer_list_item, null);
			}

			ImageView imgIcon = (ImageView) listItemLayout
					.findViewById(R.id.icon);
			TextView txtTitle = (TextView) listItemLayout
					.findViewById(R.id.title);

			imgIcon.setImageResource(navDrawerItems.get(position).getIcon());

			txtTitle.setText(navDrawerItems.get(position).getTitle());

			return listItemLayout;
		}

	}

	

	@SuppressLint("RtlHardcoded") public void toggleDrawer(View v) {

		if (mDrawerLayout.isDrawerOpen(mLinearLayout)) {

			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}

		else {

			mDrawerLayout.openDrawer(Gravity.LEFT);
		}
	}

	

	@SuppressLint("NewApi") @SuppressWarnings("deprecation")
	@Override
	protected void initVariablesForFragment(int fragmentId) {
		
		TextView title = (TextView)findViewById(R.id.page_title);
		
		
		
		switch(fragmentId){
		case CALCULATOR_FRAGMENT:
			title.setText("Calculator");
			title.setVisibility(View.VISIBLE);
			mDrawerLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.main_bg));
			break;
			
		case HELP_FRAGMENT:
			title.setText("Help");
			title.setVisibility(View.VISIBLE);
			mDrawerLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.main_bg));
			break;
			
		case ABOUT_FRAGMENT:
			title.setVisibility(View.GONE);
			mDrawerLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_about));
			break;
			
		case ANSWERS_FRAGMENT:
			title.setText("Answers");
			title.setVisibility(View.VISIBLE);
			mDrawerLayout.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.main_bg));
		}

	}

	@Override
	protected void initializeFragment(int fragmentId, Bundle args) {

		switch (fragmentId) {

		case CALCULATOR_FRAGMENT:
			fragments[CALCULATOR_FRAGMENT] = new CalculatorFragment();
			break;
			
		case ANSWERS_FRAGMENT:
			fragments[ANSWERS_FRAGMENT] = new AnswerFragment();
			break;
			
		case HELP_FRAGMENT:
			fragments[HELP_FRAGMENT] = new HelpFragment();
			break;
			
		case ABOUT_FRAGMENT:
			fragments[ABOUT_FRAGMENT] = new AboutUsFragment();
			break;
		}

	}
}
