package com.grexoft.quickcalci;

import android.widget.TextView;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

public class HelpFragment extends AnimatedFragment {

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
		// TODO Auto-generated method stub
		layoutId = R.layout.layout_help;
	}

	@Override
	public void init() {
		AdBuddiz.RewardedVideo.show(getActivity());
		TextView helpView = (TextView)fragmentView.findViewById(R.id.textView5);
		
		String helpViewStr = " It must be noted that the % sign operates differently for different preceding and succeeding operators.\nIf percent sign succeeds + or - sign then it calculates the percent of the number preceding + or - sign. For example, “5+5%” will evaluate to 5.25 and “5-5%” will evaluate to 4.75.\nWhen % operator succeeds other operators then it calculates the percent from 1. For example, the expression “5×5%” will evaluate to 0.25 because calculator first calculates 5% of 1 and then multiplies it with 5.\nWhen % operator precedes + or - or ÷ or ^ or E or √ operator then it first calculates the percent of 1 and then perform the succeeding operation, the succeeding operand. For example, the expression “5%+5” will evaluate to 5.05.\nWhen % percent precedes × operator then it calculates percentage from the number succeeding × operator. For example, the expression “5%*5” or the“5%5” will evaluate to 0.25.";
		
		helpView.setText(helpView.getText().toString() + helpViewStr);
		
	}

}
