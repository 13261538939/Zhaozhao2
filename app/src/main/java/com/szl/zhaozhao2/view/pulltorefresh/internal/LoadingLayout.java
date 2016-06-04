package com.szl.zhaozhao2.view.pulltorefresh.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.szl.zhaozhao2.R;
import com.szl.zhaozhao2.application.DApplication;
import com.szl.zhaozhao2.prefs.PrefsManager;
import com.szl.zhaozhao2.util.CommonUtil;
import com.szl.zhaozhao2.view.pulltorefresh.PullToRefreshBase;


public class LoadingLayout extends FrameLayout {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 600;
	private TextView mTipsTextview;
	private TextView mLastUpdatedTextView;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	View header;

	// private RotateAnimation animation;
	// private RotateAnimation reverseAnimation;

	private String mPullLabel;
	private String mRefreshingLabel;
	private String mReleaseLabel;
	private CharSequence time;
	private boolean ptrHeaderIsHiddenDate = false;

	public LoadingLayout(Context context, final PullToRefreshBase.Mode mode, TypedArray attrs) {
		super(context);

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderIsHiddenDate)) {
			ptrHeaderIsHiddenDate = attrs.getBoolean(
					R.styleable.PullToRefresh_ptrHeaderIsHiddenDate, false);
		}
		header = LayoutInflater.from(context).inflate(
				R.layout.pulltorefresh_listview_head, this);
		mArrowImageView = (ImageView) header
				.findViewById(R.id.head_arrowImageView);
		// mArrowImageView.setMinimumWidth(50);
		// mArrowImageView.setMinimumHeight(45);
		mProgressBar = (ProgressBar) header.findViewById(R.id.head_progressBar);
		mTipsTextview = (TextView) header.findViewById(R.id.head_tipsTextView);
		mLastUpdatedTextView = (TextView) header
				.findViewById(R.id.head_lastUpdatedTextView);
		if (ptrHeaderIsHiddenDate) {
			mLastUpdatedTextView.setVisibility(View.GONE);
		}
		// animation = new RotateAnimation(0, -180,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// animation.setInterpolator(new LinearInterpolator());
		// animation.setDuration(250);
		// animation.setFillAfter(true);
		//
		// reverseAnimation = new RotateAnimation(-180, 0,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f,
		// RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		// reverseAnimation.setInterpolator(new LinearInterpolator());
		// reverseAnimation.setDuration(200);
		// reverseAnimation.setFillAfter(true);

		switch (mode) {
		case PULL_UP_TO_REFRESH:
			// Load in labels
			if (ptrHeaderIsHiddenDate) {
				mPullLabel = context.getString(R.string.drop_up_hidden_date);
				mRefreshingLabel = context.getString(R.string.doing_update);
				mReleaseLabel = context
						.getString(R.string.release_update_up_hiddendate);
			} else {
				mPullLabel = context.getString(R.string.drop_up);
				mRefreshingLabel = context.getString(R.string.doing_update);
				mReleaseLabel = context.getString(R.string.release_update);
			}
			break;

		case PULL_DOWN_TO_REFRESH:
		default:
			// Load in labels
			if (ptrHeaderIsHiddenDate) {
				mPullLabel = context.getString(R.string.drop_dowm_hidden_date);
				mRefreshingLabel = context.getString(R.string.doing_update);
				mReleaseLabel = context
						.getString(R.string.release_update_down_hiddendate);
			} else {
				mPullLabel = context.getString(R.string.drop_dowm);
				mRefreshingLabel = context.getString(R.string.doing_update);
				mReleaseLabel = context.getString(R.string.release_update);
			}
			break;
		}

		// if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
		// ColorStateList colors = attrs
		// .getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
		// setTextColor(null != colors ? colors : ColorStateList
		// .valueOf(0xFF000000));
		// }
		// if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor))
		// {
		// ColorStateList colors = attrs
		// .getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
		// setSubTextColor(null != colors ? colors : ColorStateList
		// .valueOf(0xFF000000));
		// }
		// if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
		// Drawable background = attrs
		// .getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
		// if (null != background) {
		// setBackgroundDrawable(background);
		// }
		// }
		//
		// // Try and get defined drawable from Attrs
		// Drawable imageDrawable = null;
		// if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
		// imageDrawable = attrs
		// .getDrawable(R.styleable.PullToRefresh_ptrDrawable);
		// }
		//
		// // If we don't have a user defined drawable, load the default
		// if (null == imageDrawable) {
		// imageDrawable = context.getResources().getDrawable(
		// R.drawable.default_ptr_drawable);
		// }
		//
		// // Set Drawable, and save width/height
		// setLoadingDrawable(imageDrawable);

		reset();
	}

	public void reset() {
		mTipsTextview.setText(Html.fromHtml(mPullLabel));
		mArrowImageView.setVisibility(View.VISIBLE);
		mArrowImageView.setImageResource(R.drawable.progress3);
		// mArrowImageView.clearAnimation();
		mProgressBar.setVisibility(View.GONE);
		// resetImageRotation();

		time = PrefsManager.getPrefsUtil(DApplication.getApplication()).update_time
				.getVal();
		if (CommonUtil.isEmpty(time)) {
			String label = DateUtils.formatDateTime(
					DApplication.getApplication(), System.currentTimeMillis(),
					DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
			time = label;
		}
		mLastUpdatedTextView.setText(time);
		// if (TextUtils.isEmpty(mLastUpdatedTextView.getText())) {
		// mLastUpdatedTextView.setVisibility(View.GONE);
		// } else {
		// mLastUpdatedTextView.setVisibility(View.VISIBLE);
		// }
	}

	public void releaseToRefresh() {
		mTipsTextview.setText(Html.fromHtml(mReleaseLabel));
		// mArrowImageView.clearAnimation();
		// mArrowImageView.startAnimation(animation);
	}

	public void setPullLabel(String pullLabel) {
		mPullLabel = pullLabel;
		mTipsTextview.setText(Html.fromHtml(mPullLabel));
	}

	public void refreshing() {
		time = PrefsManager.getPrefsUtil(DApplication.getApplication()).update_time
				.getVal();
		if (CommonUtil.isEmpty(time)) {
			String label = DateUtils.formatDateTime(
					DApplication.getApplication(), System.currentTimeMillis(),
					DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
			time = label;
		}
		mLastUpdatedTextView.setText(time);
		mTipsTextview.setText(Html.fromHtml(mRefreshingLabel));
		mArrowImageView.setVisibility(View.GONE);
		mArrowImageView.setImageBitmap(null);
		mProgressBar.setVisibility(View.VISIBLE);
		// mLastUpdatedTextView.setVisibility(View.GONE);
	}

	public void setRefreshingLabel(String refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(String releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	public void pullToRefresh() {

		mTipsTextview.setText(Html.fromHtml(mPullLabel));
		// mArrowImageView.clearAnimation();
		// mArrowImageView.startAnimation(reverseAnimation);
	}

	// public void setTextColor(ColorStateList color) {
	// mHeaderText.setTextColor(color);
	// mSubHeaderText.setTextColor(color);
	// }
	//
	// public void setSubTextColor(ColorStateList color) {
	// mSubHeaderText.setTextColor(color);
	// }

	// public void setTextColor(int color) {
	// setTextColor(ColorStateList.valueOf(color));
	// }

	// public void setLoadingDrawable(Drawable imageDrawable) {
	// // Set Drawable, and save width/height
	// mHeaderImage.setImageDrawable(imageDrawable);
	// mRotationPivotX = imageDrawable.getIntrinsicWidth() / 2f;
	// mRotationPivotY = imageDrawable.getIntrinsicHeight() / 2f;
	// }

	// public void setSubTextColor(int color) {
	// setSubTextColor(ColorStateList.valueOf(color));
	// }

	public void setSubHeaderText(CharSequence label) {
		mLastUpdatedTextView.setText(label);
		PrefsManager.getPrefsUtil(DApplication.getApplication()).update_time
				.setVal(label.toString());
	}

	// public void onPullY(float scaleOfHeight) {
	// mHeaderImageMatrix.setRotate(scaleOfHeight * 90, mRotationPivotX,
	// mRotationPivotY);
	// mHeaderImage.setImageMatrix(mHeaderImageMatrix);
	// }
	//
	// private void resetImageRotation() {
	// mHeaderImageMatrix.reset();
	// mHeaderImage.setImageMatrix(mHeaderImageMatrix);
	// }

}
