package com.ucuxin.ucuxin.tec.model;

import java.util.LinkedHashSet;

public class AnswerListModel extends Model {

	private LinkedHashSet<AnswerListItemGson> mAnswerListItemGsons;

	public static final int ANSWER_LIST_MODEL_OBSERVER = 1000;

	public LinkedHashSet<AnswerListItemGson> getAnswerListItemGsons() {
		return mAnswerListItemGsons;
	}

	public void setAnswerListItemGsons(
			LinkedHashSet<AnswerListItemGson> mAnswerListItemGsons) {
		this.mAnswerListItemGsons = mAnswerListItemGsons;
		notifyChanged(ANSWER_LIST_MODEL_OBSERVER);
	}
}
