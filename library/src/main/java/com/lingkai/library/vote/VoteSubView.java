package com.lingkai.library.vote;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ProgressBar.library.R;


import java.text.NumberFormat;


/**
 * 投票的子 view
 */
class VoteSubView extends LinearLayout implements VoteObserver {

    public ProgressBar progressBar;

    private TextView contentView;

    private TextView sortView;

    private TextView numberView;

    private int mTotalNumber = 1;

    private int mCurrentNumber = 1;

    private String mCurrentPercent;

    private AnimatorSet animatorSet;

    private ImageView ivSelectView;

    public FrameLayout fl;

    public VoteSubView(Context context) {
        this(context, null);
    }

    public VoteSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoteSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(getContext(), R.layout.vote_sub_view, this);
        initView();
        initAnimation();
    }

    private void initView() {
        progressBar = findViewById(R.id.progress_view);
        sortView = findViewById(R.id.name_sort_view);
        ivSelectView = findViewById(R.id.iv_select);
        contentView = findViewById(R.id.name_text_view);
        numberView = findViewById(R.id.number_text_view);
        fl = findViewById(R.id.fl);
        numberView.setAlpha(0.0f);
    }

    public void setContent(String content, String sort) {
        contentView.setText(content);
        sortView.setText(sort);
    }

    public void setNumber(int number,String percentage) {
        mCurrentNumber = number;
        mCurrentPercent = percentage;
        numberView.setText(percentage);
    }

    private void initAnimation() {
        animatorSet = new AnimatorSet();
        Animator[] arrayAnimator = new Animator[1];
//        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(contentView, "x", 130);
//        arrayAnimator[0] = objectAnimator;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(numberView, "alpha", 1.0f);
        arrayAnimator[0] = objectAnimator;
        animatorSet.playTogether(arrayAnimator);
        animatorSet.setDuration(VoteView.mAnimationRate);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        setChildViewStatus(selected);
        if (selected) {
            start();
        } else {
            cancel();
        }
    }

    public void start() {
        post(new Runnable() {
            @Override
            public void run() {
                animatorSet.start();
            }
        });
    }

    public void cancel() {
        post(new Runnable() {
            @Override
            public void run() {
                animatorSet.cancel();
            }
        });
    }

    public void setChildViewStatus(boolean isSelected) {
        if (isSelected) {
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBarAnimation(progressBar, mCurrentNumber, mTotalNumber);
                }
            });
            numberView.setVisibility(VISIBLE);
            numberView.setAlpha(0.0f);
        } else {
            progressBar.setProgress(0);
            numberView.setVisibility(GONE);
            ivSelectView.setVisibility(GONE);
            sortView.setVisibility(VISIBLE);
            contentView.setTextColor(Color.parseColor("#1A1A1A"));
            contentView.setCompoundDrawables(null, null, null, null);
            contentView.animate().translationX(0).setDuration(VoteView.mAnimationRate).start();

        }
    }

    @Override
    public void update(View view, boolean status) {
        changeChildrenViewStatus(((int) view.getTag()) == getCurrentIndex());
        if (((int) view.getTag()) == getCurrentIndex()) {
            Log.e("update", "当前被点选的是:" + getCurrentIndex());
            if (status) {
                mCurrentNumber += 1;
                mTotalNumber += 1;
                numberView.setText(mCurrentPercent);
            }
        }
        setSelected(status);
    }

    @Override
    public void setTotalNumber(int totalNumber) {
        mTotalNumber = totalNumber;
    }

    private void progressBarAnimation(final ProgressBar progressBar, int current, int total) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(3);
        float result = ((float) current / (float) total) * 100;
        Log.e("progressBarAnimation", "result" + Math.ceil(result));
        ValueAnimator animator = ValueAnimator.ofInt(0, (int) Math.ceil(result)).setDuration(VoteView.mAnimationRate);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                progressBar.setProgress((int) valueAnimator.getAnimatedValue());
            }
        });
        animator.start();
    }

    private int getCurrentIndex() {
        return (int) getTag();
    }


    private void changeChildrenViewStatus(boolean status) {
        //选中文字颜色
        contentView.setTextColor(Color.parseColor(status ? "#9FC6EA" : "#808080"));
        //数字颜色
        numberView.setTextColor(Color.parseColor(status ? "#9FC6EA" : "#808080"));
        //带勾选框
//        Drawable right = getResources().getDrawable(status ? R.mipmap.vote_selected : R.mipmap.vote_empty);
//        right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
//        contentView.setCompoundDrawables(null, null, right, null);
        if (status){
            ivSelectView.setVisibility(View.VISIBLE);
            sortView.setVisibility(View.GONE);
        } else {
            ivSelectView.setVisibility(View.GONE);
            sortView.setVisibility(View.VISIBLE);
        }

        //进度条颜色设置
        progressBar.setProgressDrawable(getResources().getDrawable(status ? R.drawable.select_progress_view_bg : R.drawable.unselect_progress_view_bg));

        LinearLayout.LayoutParams params = (LayoutParams) getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        setLayoutParams(params);
        //setBackgroundResource(status ? R.drawable.select_bg : R.drawable.unselect_bg);
        params.setMargins(0, 16, 0, 16);
        setLayoutParams(params);
    }

}
