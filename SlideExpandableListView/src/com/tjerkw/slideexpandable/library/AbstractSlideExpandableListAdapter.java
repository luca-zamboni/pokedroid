package com.tjerkw.slideexpandable.library;

import java.util.BitSet;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * Wraps a ListAdapter to give it expandable list view functionality.
 * The main thing it does is add a listener to the getToggleButton
 * which expands the getExpandableView for each list item.
 *
 * @author tjerk
 * @date 6/9/12 4:41 PM
 */
public abstract class AbstractSlideExpandableListAdapter extends WrapperListAdapterImpl {
	
	static boolean nnsidovrebbefare = true;
	
	/**
	 * Reference to the last expanded list item.
	 * Since lists are recycled this might be null if
	 * though there is an expanded list item
	 */
	private View lastOpen = null;
	/**
	 * The position of the last expanded list item.
	 * If -1 there is no list item expanded.
	 * Otherwise it points to the position of the last expanded list item
	 */
	private int lastOpenPosition = -1;
	
	/**
	 * Default Animation duration
	 * Set animation duration with @see setAnimationDuration
	 */
	private int animationDuration = 330;
	
	/**
	 * A list of positions of all list items that are expanded.
	 * Normally only one is expanded. But a mode to expand
	 * multiple will be added soon.
	 *
	 * If an item onj position x is open, its bit is set
	 */
	private BitSet openItems = new BitSet();
	/**
	 * We remember, for each collapsable view its height.
	 * So we dont need to recalculate.
	 * The height is calculated just before the view is drawn.
	 */
	private final SparseIntArray viewHeights = new SparseIntArray(10);

	public OnLongListener listener;
	
	public AbstractSlideExpandableListAdapter(ListAdapter wrapped,OnLongListener listener) {
		super(wrapped);
		this.listener = listener;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		view = wrapped.getView(position, view, viewGroup);
		enableFor(view, position);
		return view;
	}

	public abstract View getExpandToggleButton(View parent);

	public abstract View getExpandableView(View parent);

	public int getAnimationDuration() {
		return animationDuration;
	}
 
	public void setAnimationDuration(int duration) {
		if(duration < 0) {
			throw new IllegalArgumentException("Duration is less than zero");
		}
		
		animationDuration = duration;
	}

	public boolean isAnyItemExpanded() {
		return (lastOpenPosition != -1) ? true : false;
	}

	public void enableFor(View parent, int position) {
		View more = getExpandToggleButton(parent);
		View itemToolbar = getExpandableView(parent);
		itemToolbar.measure(parent.getWidth(), parent.getHeight());

		enableFor(more, itemToolbar, position);
	}


	private void enableFor(final View button, final View target, final int position) {
		if(target == lastOpen && position!=lastOpenPosition) {
			// lastOpen is recycled, so its reference is false
			lastOpen = null;
		}
		if(position == lastOpenPosition) {
			// re reference to the last view
			// so when can animate it when collapsed
			lastOpen = target;
		}
		int height = viewHeights.get(position, -1);
		if(height == -1) {
			viewHeights.put(position, target.getMeasuredHeight());
			updateExpandable(target,position);
		} else {
			updateExpandable(target, position);
		}

		button.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				AbstractSlideExpandableListAdapter.nnsidovrebbefare = false;
				listener.onLongClick(v,position);
				return false; 
			}
		});

		
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {

				if(AbstractSlideExpandableListAdapter.nnsidovrebbefare){
					Log.e("","trololol");
					Animation a = target.getAnimation();

					if (a != null && a.hasStarted() && !a.hasEnded()) {

						a.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationEnd(Animation animation) {
								view.performClick();
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}
						});

					} else {

						target.setAnimation(null);

						int type = target.getVisibility() == View.VISIBLE
								? ExpandCollapseAnimation.COLLAPSE
								: ExpandCollapseAnimation.EXPAND;

						// remember the state
						if (type == ExpandCollapseAnimation.EXPAND) {
							openItems.set(position, true);
						} else {
							openItems.set(position, false);
						}
						// check if we need to collapse a different view
						if (type == ExpandCollapseAnimation.EXPAND) {
							if (lastOpenPosition != -1 && lastOpenPosition != position) {
								if (lastOpen != null) {
									animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
								}
								openItems.set(lastOpenPosition, false);
							}
							lastOpen = target;
							lastOpenPosition = position;
						} else if (lastOpenPosition == position) {
							lastOpenPosition = -1;
						}
						animateView(target, type);
					}	
				}
				AbstractSlideExpandableListAdapter.nnsidovrebbefare = true; 
				
			}
		});
	}

	private void updateExpandable(View target, int position) {

		final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)target.getLayoutParams();
			target.setVisibility(View.GONE);
			params.bottomMargin = 0-viewHeights.get(position);
	}

	/**
	 * Performs either COLLAPSE or EXPAND animation on the target view
	 * @param target the view to animate
	 * @param type the animation type, either ExpandCollapseAnimation.COLLAPSE
	 *			 or ExpandCollapseAnimation.EXPAND
	 */
	private void animateView(final View target, final int type) {
		Animation anim = new ExpandCollapseAnimation(
				target,
				type
		);
		anim.setDuration(getAnimationDuration());
		target.startAnimation(anim);
	}


	/**
	 * Closes the current open item.
	 * If it is current visible it will be closed with an animation.
	 *
	 * @return true if an item was closed, false otherwise
	 */
	public boolean collapseLastOpen() {
		if(isAnyItemExpanded()) {
			// if visible animate it out
			if(lastOpen != null) {
				animateView(lastOpen, ExpandCollapseAnimation.COLLAPSE);
			}
			openItems.set(lastOpenPosition, false);
			lastOpenPosition = -1;
			return true;
		}
		return false;
	}

	public Parcelable onSaveInstanceState(Parcelable parcelable) {

		SavedState ss = new SavedState(parcelable);
		ss.lastOpenPosition = this.lastOpenPosition;
		ss.openItems = this.openItems;
		return ss;
	}

	public void onRestoreInstanceState(SavedState state) {

		this.lastOpenPosition = state.lastOpenPosition;
		this.openItems = state.openItems;
	}

	/**
	 * Utility methods to read and write a bitset from and to a Parcel
	 */
	private static BitSet readBitSet(Parcel src) {
		int cardinality = src.readInt();

		BitSet set = new BitSet();
		for (int i = 0; i < cardinality; i++) {
			set.set(src.readInt());
		}

		return set;
	}

	private static void writeBitSet(Parcel dest, BitSet set) {
		int nextSetBit = -1;

		dest.writeInt(set.cardinality());

		while ((nextSetBit = set.nextSetBit(nextSetBit + 1)) != -1) {
			dest.writeInt(nextSetBit);
		}
	}

	/**
	 * The actual state class
	 */
	static class SavedState extends View.BaseSavedState {
		public BitSet openItems = null;
		public int lastOpenPosition = -1;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			in.writeInt(lastOpenPosition);
			writeBitSet(in, openItems);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			lastOpenPosition = out.readInt();
			 openItems = readBitSet(out);
		}

		//required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<SavedState> CREATOR =
		new Parcelable.Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
