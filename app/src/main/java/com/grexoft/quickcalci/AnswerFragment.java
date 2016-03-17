package com.grexoft.quickcalci;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.purplebrain.adbuddiz.sdk.AdBuddiz;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class AnswerFragment extends AnimatedFragment {

	private List<AnswerItem> answers;
	private SharedPreferences myPreferences;
	private ListView listAnswer;

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
		layoutId = R.layout.layout_answer;
	}

	@Override
	public void init() {
		AdBuddiz.showAd(getActivity());
		myPreferences = getActivity().getSharedPreferences(
				CalculatorApplication.SHARED_PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		listAnswer = (ListView) fragmentView.findViewById(R.id.list_answers);

		answers = new ArrayList<AnswerItem>();

		int answersCount = myPreferences.getInt("answer_item_count", 0);

		for (int i = 0; i < answersCount; i++) {

			System.out
					.println("item " + i + "  expression : "
							+ myPreferences.getString("expression_" + i, "")
							+ " andwer : "
							+ myPreferences.getString("answer_" + i, ""));

			answers.add(new AnswerItem(i, myPreferences.getString("expression_"
					+ i, ""), myPreferences.getString("answer_" + +i, "")));
		}

		showAnswers();

	}

	private void showAnswers() {

		if (answers.size() > 0) {

			listAnswer.setAdapter(new AnswerAdapter());

			fragmentView.findViewById(R.id.txt_msg).setVisibility(View.GONE);

		}

		else {

			fragmentView.findViewById(R.id.txt_msg).setVisibility(View.VISIBLE);

			listAnswer.setAdapter(null);

		}

	}

	@SuppressLint({ "NewApi", "InflateParams" })
	private class AnswerAdapter extends BaseAdapter {

		private LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);;

		@Override
		public int getCount() {

			return answers != null ? answers.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LinearLayout listItemLayout;

			if (convertView != null) {

				listItemLayout = (LinearLayout) convertView;
			}

			else {

				listItemLayout = (LinearLayout) inflater.inflate(
						R.layout.answer_list_item, null);
			}

			TextView txtExpr = (TextView) listItemLayout
					.findViewById(R.id.txt_expr);

			txtExpr.setText(answers.get(position).getExpression());

			TextView txtAnswer = (TextView) listItemLayout
					.findViewById(R.id.txt_answer);

			final String answerString = answers.get(position).getAnswer();

			txtAnswer.setText(answerString);

			ImageView btnCopy = (ImageView) listItemLayout
					.findViewById(R.id.btn_copy);

			btnCopy.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					ClipboardManager clipboard = (ClipboardManager) getActivity()
							.getSystemService(Context.CLIPBOARD_SERVICE);

					ClipData clip = ClipData.newPlainText("answer",
							answerString);

					clipboard.setPrimaryClip(clip);

					Toast.makeText(getActivity(), "Copied to clipboard",
							Toast.LENGTH_SHORT).show();

				}
			});

			ImageView btnDelete = (ImageView) listItemLayout
					.findViewById(R.id.btn_clear);

			btnDelete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					resetAnswers(answers.get(position).getAnswerId());

				}
			});

			return listItemLayout;

		}

	}

	private void resetAnswers(int itemToBeDeleted) {

		System.out.println("item to be deleted" + itemToBeDeleted);

		answers.remove(itemToBeDeleted);

		Editor editor = myPreferences.edit();

		for (int i = 0; i < answers.size(); i++) {

			answers.get(i).answerId = i;

			editor.putString("expression_" + i, answers.get(i).getExpression());

			editor.putString("answer_" + i, answers.get(i).getAnswer());

			System.out.println("item " + i + " expression : "
					+ answers.get(i).getExpression());

		}

		editor.putInt("answer_item_count", answers.size());

		editor.commit();

		showAnswers();

	}

	private class AnswerItem {

		private int answerId;

		private String expression;

		private String answer;

		public AnswerItem(int answerId, String expression, String answer) {

			this.answerId = answerId;

			this.expression = expression;

			this.answer = answer;
		}

		public int getAnswerId() {
			return answerId;
		}

		public String getExpression() {
			return expression;
		}

		public String getAnswer() {
			return answer;
		}

	}

}
