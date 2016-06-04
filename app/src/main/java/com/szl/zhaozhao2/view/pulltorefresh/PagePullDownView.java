package com.szl.zhaozhao2.view.pulltorefresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.szl.zhaozhao2.R;


/**
 * Created by sina on 13-8-15.
 */

public class PagePullDownView extends PullDownView {

	public static abstract class LoadingState {
		public static int V_PULL_NUME = 3;
		public static int V_PULL_DENO = 10;
		public static int V_UPDATE_NUME = 360;
		public static int V_UPDATE_DENO = 750;

		private long startTime;
		private int oldHeight;

		private int degree;
		private boolean pulling;
		private boolean updating;

		abstract public void pull();

		abstract public void update();

		public int getDegree() {
			return degree;
		}

		public boolean isPulling() {
			return pulling;
		}

		public boolean isUpdating() {
			return updating;
		}
	}

	public static final int USER = 1;
	public static final int PAGE = 2;

	private LoadingState mListener;

	// private PullDownBackground mBackground;
	private PullDownBitmap mCover;

	private BitmapDrawable mCoverDrawable;

	private Rect mRcreenSize;
	private int mCoverDestWidth;
	private int mCoverDstHeight;
	private float mCoverScale;
	private int mCoverDisplayHeight;

	public PagePullDownView(Context context) {
		super(context);
		initData();
	}

	public PagePullDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
	}

	public void showInfos(boolean showing) {
		mUpdateContent.setVisibility(showing ? View.VISIBLE : View.GONE);
		mTitle.setVisibility(showing ? View.VISIBLE : View.GONE);
	}

	public void setPullDownListener(LoadingState listener) {
		this.mListener = listener;
	}

	private void initData() {
		if (isInEditMode()) {
			return;
		}
		mCoverDisplayHeight = getResources().getDimensionPixelOffset(
				R.dimen.page_info_portrait_margintop)
				+ getResources().getDimensionPixelOffset(
						R.dimen.page_info_nick_height);

		findViewById(R.id.vw_update_divider_id).setVisibility(View.GONE);

		// mBackground = new PullDownBackground(getContext());

		resetMatrix();
	}

	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			long time = System.currentTimeMillis();
			if (!mListener.pulling) {
				mListener.degree = (int) ((mListener.degree + (time - mListener.startTime)
						* LoadingState.V_UPDATE_NUME
						/ LoadingState.V_UPDATE_DENO) % 360);
			}
			mListener.startTime = time;
			mListener.update();

			postDelayed(mRunnable, 10);
		}
	};

	@Override
	public void update() {
		super.update();
		// mPading = 0;
		// mState = STATE_CLOSE;
	}

	// @Override
	// protected void scrollToUpdate() {
	// startUpdate();
	//
	// mFlinger.startUsingDistance(-mPading, 300);
	//
	// postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// if (mUpdateHandle != null) {
	// mUpdateHandle.onUpdate();
	// }
	// }
	// }, 300);
	// }

	protected void scrollToClose() {
		if (mListener != null) {
			mListener.pulling = false;
			mListener.startTime = System.currentTimeMillis();
		}

		super.scrollToClose();
	}

	public void startUpdate() {
		if (mListener != null) {
			mListener.pulling = false;
			mListener.updating = true;
			mListener.startTime = System.currentTimeMillis();

			postDelayed(mRunnable, 10);
		}
	}

	public void completeUpdate() {
		super.endUpdate();
		if (mListener != null) {
			mListener.updating = false;

			if (!mListener.pulling) {
				updateView();
				removeCallbacks(mRunnable);
			}
		}
	}

	@Override
	protected void updateView() {
		super.updateView();

		// if (mPading == 0) {
		// ConcurrentManager.getInsance().resumeThreadPool(AsyncTag.ASYNC_CARD_TAG);
		// } else {
		// ConcurrentManager.getInsance().pauseThreadPool(AsyncTag.ASYNC_CARD_TAG);
		// }

		if (mListener != null) {
			if (mPading == 0) {
				mListener.pulling = false;
			} else {
				mListener.pulling = true;
			}
			int height = mPading;
			if (mListener.pulling) {
				mListener.degree = (mListener.degree + (height - mListener.oldHeight)
						* LoadingState.V_PULL_NUME / LoadingState.V_PULL_DENO) % 360;
			}
			mListener.oldHeight = height;
			mListener.pull();
		}
	}

	@Override
	protected void updateHandler() {
		// 释放回来后执行下载回调
		super.updateHandler();
	}

	public void setCoverDrawable(Drawable drawable) {
		mCoverDrawable = (BitmapDrawable) drawable;

		mCover = new PullDownBitmap(mCoverDrawable);

		resetMatrix();
	}

	public static void getScreenRect(Context ctx_, Rect outrect_) {
		Display screenSize = ((WindowManager) ctx_
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		outrect_.set(0, 0, screenSize.getWidth(), screenSize.getHeight());
	}

	public void resetMatrix() {
		mRcreenSize = new Rect();
		getScreenRect(getContext(), mRcreenSize);

		mCoverDestWidth = mRcreenSize.width();

		// mBackground.setSreenSize(mCoverDestWidth);

		if (mCover != null) {
			mCoverScale = ((float) mCoverDestWidth) / mCover.getWidth();
			mCoverDstHeight = (int) (mCover.getHeight() * mCoverScale);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isInEditMode()) {
			return;
		}
		if (mCover != null) {
			int hideHeight = mCoverDstHeight - mCoverDisplayHeight;
			int translate = 0;
			RectF coverRect = mCover.getRect();
			boolean drawBackground = false;

			if (-mPading == 0) {
				if (((ListView) getContentView()).getFirstVisiblePosition() == 0) {
					translate = -hideHeight / 2 + getCoverHeaderTop();

					coverRect.left = 0;
					coverRect.top = 0;
					coverRect.right = mCoverDestWidth;

					int bottom = translate + mCoverDstHeight;
					int headerBottom = getCoverHeaderBottom() - 1;
					coverRect.bottom = bottom < headerBottom ? bottom
							: headerBottom;
				} else {
					return;
				}
			} else if (-mPading < hideHeight) {
				translate = -(hideHeight + mPading) / 2;

				coverRect.left = 0;
				coverRect.top = 0;
				coverRect.right = mCoverDestWidth;

				int bottom = translate + mCoverDstHeight;
				int headerBottom = getCoverHeaderBottom() - 1;
				coverRect.bottom = bottom < headerBottom ? bottom
						: headerBottom;
			} else {
				translate = -(hideHeight + mPading);

				coverRect.left = 0;
				coverRect.top = translate;
				coverRect.right = mCoverDestWidth;
				coverRect.bottom = coverRect.top + mCoverDstHeight;

				drawBackground = true;
			}

			// mBackground.drawBackground(canvas, translate, drawBackground,
			// true);

			drawCover(canvas, translate);
		} else {
			int translate = 0;
			boolean drawBackground = false;

			if (-mPading == 0) {
				if (((ListView) getContentView()).getFirstVisiblePosition() == 0) {
					translate = mCoverDisplayHeight + getCoverHeaderTop();

					drawBackground = true;
				}
			} else {
				translate = mCoverDisplayHeight - mPading;

				drawBackground = true;
			}

			// mBackground.drawBackground(canvas, translate, drawBackground,
			// false);
		}
	}

	private void drawCover(Canvas canvas, int translate) {
		if (mCover != null) {
			canvas.save();// 记录原来的canvas状态
			canvas.clipRect(0, 0, mCoverDestWidth, mCover.getRect().bottom);
			mCover.drawBitmap(canvas, mCoverScale, mCoverScale, 0, translate);
			canvas.restore();
		}
	}

	private int getCoverHeaderTop() {
		ListView list = (ListView) getContentView();
		ViewGroup header = (ViewGroup) list.getChildAt(0);

		return list.getTop() + header.getTop();
	}

	private int getCoverHeaderBottom() {
		ListView list = (ListView) getContentView();
		ViewGroup header = (ViewGroup) list.getChildAt(0);

		return list.getTop() + header.getTop()
				+ header.getChildAt(0).getBottom();
	}
}
