package com.tjerkw.slideexpandable.library;

import android.view.View;

public interface OnLongListener {
	/**
	 * Called when an action item is clicked.
	 *
	 * @param itemView the view of the list item
	 * @param clickedView the view clicked
	 * @param position the position in the listview
	 */
	public void onLongClick(View clickedView, int position);
}