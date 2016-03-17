package com.grexoft.quickcalci;

import support.Calculator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CalculatorFragment extends AnimatedFragment implements
		OnTouchListener {

	private String lastAnswer, lastExpression;

	private EditText display;

	private int clickables[] = { R.id.btn_one, R.id.btn_two, R.id.btn_three,
			R.id.btn_four, R.id.btn_five, R.id.btn_six, R.id.btn_seven,
			R.id.btn_eight, R.id.btn_nine, R.id.btn_zero, R.id.btn_point,
			R.id.btn_del, R.id.btn_add, R.id.btn_minus, R.id.btn_divide,
			R.id.btn_multi, R.id.btn_equal, R.id.btn_power, R.id.btn_exp,
			R.id.btn_rightparenthes, R.id.btn_leftparenthes, R.id.btn_sqrt,
			R.id.btn_percentage, R.id.btn_paste, R.id.btn_copy, R.id.btn_clear,
			R.id.btn_save };

	private void saveAnswer() {

		SharedPreferences myPreferences = getActivity().getSharedPreferences(
				CalculatorApplication.SHARED_PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		Editor editor = myPreferences.edit();

		int answerItemCount = myPreferences.getInt("answer_item_count", 0);

		editor.putString("expression_" + answerItemCount, lastExpression);

		editor.putString("answer_" + answerItemCount, lastAnswer);

		answerItemCount++;

		editor.putInt("answer_item_count", answerItemCount);

		editor.commit();

		Toast.makeText(getActivity(), "answer saved", Toast.LENGTH_SHORT)
				.show();

		display.setText("");

	}

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

		layoutId = R.layout.layout_calculator;

	}

	@Override
	public void init() {

		lastAnswer = "";
		lastExpression = "";

		display = (EditText) fragmentView.findViewById(R.id.display);

		for (int i = 0; i < clickables.length; i++)
			try {
				fragmentView.findViewById(clickables[i]).setOnTouchListener(
						this);
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	@SuppressLint("ClickableViewAccessibility")
	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		ClipboardManager clipboardManager = (ClipboardManager) getActivity()
				.getSystemService(Context.CLIPBOARD_SERVICE);
		
		ClipData clip;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			int i;

			Drawable drawable = null;

			for (i = 0; i < clickables.length; i++)
				if (clickables[i] == v.getId())
					break;

			if (i < 12)
				drawable = getActivity().getResources().getDrawable(
						R.drawable.bg_btn_operand_click);

			else if (i < 25)
				drawable = getActivity().getResources().getDrawable(
						R.drawable.bg_btn_operator_click);

			else if (i == 25)
				drawable = getActivity().getResources().getDrawable(
						R.drawable.bg_delete_button_click);

			else if (i == 26)
				drawable = getActivity().getResources().getDrawable(
						R.drawable.bg_save_button_click);

			v.setBackgroundDrawable(drawable);

			String character = null;

			switch (v.getId()) {

			case R.id.btn_one:
				character = "1";
				break;

			case R.id.btn_two:
				character = "2";
				break;

			case R.id.btn_three:
				character = "3";
				break;

			case R.id.btn_four:
				character = "4";
				break;

			case R.id.btn_five:
				character = "5";
				break;

			case R.id.btn_six:
				character = "6";
				break;

			case R.id.btn_seven:
				character = "7";
				break;

			case R.id.btn_eight:
				character = "8";
				break;

			case R.id.btn_nine:
				character = "9";
				break;

			case R.id.btn_zero:
				character = "0";
				break;

			case R.id.btn_point:
				character = ".";
				break;

			case R.id.btn_add:
				character = "+";
				break;

			case R.id.btn_minus:
				character = "-";
				break;

			case R.id.btn_multi:
				character = "×";
				break;

			case R.id.btn_divide:
				character = "÷";
				break;

			case R.id.btn_rightparenthes:
				character = ")";
				break;

			case R.id.btn_leftparenthes:
				character = "(";
				break;

			case R.id.btn_power:
				character = "^";
				break;

			case R.id.btn_sqrt:
				character = "√";
				break;

			case R.id.btn_exp:
				character = "E";
				break;

			case R.id.btn_percentage:
				character = "%";
				break;

			case R.id.btn_save:
				if (!lastAnswer.equals(""))
					saveAnswer();

				break;

			case R.id.btn_paste:
				
				try{
					
					clip = clipboardManager.getPrimaryClip();

					
					String text = clip.getItemAt(0).coerceToText(getActivity())
							.toString();

					display.setText(display.getText().toString() + text);
				}
				catch(NullPointerException ex){
					ex.printStackTrace();
					Toast.makeText(getActivity(), "Clipboard is empty", Toast.LENGTH_SHORT).show();
				}
				
				

				break;

			case R.id.btn_copy:

				clip = ClipData.newPlainText("answer", display.getText().toString());

				clipboardManager.setPrimaryClip(clip);

				Toast.makeText(getActivity(), "Copied to clipboard",
						Toast.LENGTH_SHORT).show();

			}

			if (character != null) {

				if (display.getText().toString()
						.equals(Calculator.ERROR_STRING))
					display.setText(character);

				else
					display.setText(display.getText().toString() + character);
			}

			String displayText = (display.getText().toString());

			if (v.getId() == R.id.btn_del && displayText != null) {
				if (displayText.length() == 1) {
					display.setText("");
					System.out.println("deleted");
				}

				if (displayText.length() > 1) {
					display.setText(displayText.substring(0,
							displayText.length() - 1));
				}
			}

			if (v.getId() == R.id.btn_clear) {
				display.setText("");
				System.out.println("cleared");

			}

			if (v.getId() == R.id.btn_equal && displayText != null
					&& displayText.length() > 1) {

				System.out.println("infix expression : " + displayText);

				Calculator calculator = new Calculator(displayText);

				String ans = calculator.calculate();
				
				String finalAnswer = null;

				if (!ans.equals(Calculator.ERROR_STRING)) {
					
					try{
						
						double answerDouble = Double.parseDouble(ans);

						long answerLong = (long) answerDouble;

						finalAnswer = ans;

						if (answerDouble == answerLong) {

							finalAnswer = String.valueOf((long) answerDouble);
						}

						lastAnswer = finalAnswer;

						lastExpression = displayText;

						
					}
					catch(NumberFormatException ex){
						finalAnswer = Calculator.ERROR_STRING;
					}
					finally{
						display.setText(finalAnswer);
					}

					

				}

				else {

					display.setText(ans);

				}

				// display.setText(inToPost.infixToPostfix());

			}

			System.out.println("text field value : "
					+ display.getText().toString() + " display Text : "
					+ displayText);
		}

		else if (event.getAction() == MotionEvent.ACTION_UP) {

			int i;

			Drawable drawable = null;

			for (i = 0; i < clickables.length; i++)
				if (clickables[i] == v.getId())
					break;

			if (i < 12)
				drawable = getActivity().getResources().getDrawable(
						R.drawable.bg_btn_operand);

			else if (i < 25)
				drawable = getActivity().getResources().getDrawable(
						R.drawable.bg_btn_operator);

			else if (i == 25)
				drawable = getActivity().getResources().getDrawable(
						R.drawable.bg_delete_button);

			else if (i == 26)
				drawable = getActivity().getResources().getDrawable(
						R.drawable.bg_save_button);

			v.setBackgroundDrawable(drawable);
		}
		return true;
	}

}
