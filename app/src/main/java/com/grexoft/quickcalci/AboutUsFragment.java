package com.grexoft.quickcalci;

import android.graphics.Typeface;
import android.widget.TextView;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

public class AboutUsFragment extends AnimatedFragment {

	@Override
	public void performEnterAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performExitAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLayoutId() {
		layoutId = R.layout.layout_aboutus;

		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		AdBuddiz.showAd(getActivity());
		Typeface bankGothicTypeFace = Typeface.createFromAsset(getActivity().getResources().getAssets(), "fonts/bankgothic_md_bt_medium.TTF");
		
		TextView txtAppName, txtDeveloperNameFirst, txtDeveloperNameLast;
		
		txtAppName = (TextView)fragmentView.findViewById(R.id.txt_app_name);
		
		txtDeveloperNameFirst = (TextView)fragmentView.findViewById(R.id.txt_developer_name_first);
		
		txtDeveloperNameLast = (TextView)fragmentView.findViewById(R.id.txt_developer_name_last);
		
		txtAppName.setTypeface(bankGothicTypeFace, Typeface.BOLD);
		txtDeveloperNameFirst.setTypeface(bankGothicTypeFace,Typeface.BOLD);
		
		txtDeveloperNameLast.setTypeface(bankGothicTypeFace,Typeface.BOLD);
		
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN){
			int textViews[] = {R.id.txt_app_name, R.id.txt_app_version, R.id.txt_design_develop,
					R.id.txt_developer_address, R.id.txt_developer_fb_address,
					R.id.txt_developer_name_first, R.id.txt_developer_name_last,
					R.id.txt_developer_twitter_address, R.id.txt_developer_web_address};
			
			for (int id : textViews){
				((TextView)fragmentView.findViewById(id)).setTextColor(getActivity().getResources().getColor(R.color.bg_btn_dark));
			}
		}
		
		
		
		
	}

}
