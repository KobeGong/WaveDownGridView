package com.kobe.wavedowngridview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final int STATE_IDLE = 0;
    private static final int STATE_ANIMATING = 1;
    private static final int STATE_DONE = 2;
    private static final int DEFAULT_ANIMATION_DURATION = 500;

    private Button button;
    private GridLayout gridLayout;
    private int animationState;
    private ChildAnimationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables();
        initViews();
    }

    private void initVariables() {
        animationState = STATE_IDLE;
        listener = new ChildAnimationListener();
    }

    private void initViews() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                switch (animationState) {
                    case STATE_IDLE:
                        startGridLayoutAnimation();
                        animationState = STATE_ANIMATING;
                        button.setText(R.string.animating);
                        break;
                    case STATE_ANIMATING:
                        // Nothing to do
                        break;
                    case STATE_DONE:
                        resetGridChildren();
                        animationState = STATE_IDLE;
                        button.setText(R.string.reset_animation);
                        break;
                }
            }
        });
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        initGridChildren();
    }

    private void initGridChildren() {
        GridLayout.LayoutParams lp;
        ImageView imageView;
        for (int i = 0; i < 15; i++) {
            imageView = new ImageView(this);
            lp = new GridLayout.LayoutParams(GridLayout.spec(GridLayout.UNDEFINED),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f));
            lp.leftMargin = 10;
            lp.topMargin = 10;
            lp.rightMargin = 10;
            lp.bottomMargin = 10;
            gridLayout.addView(imageView, lp);
            imageView.setImageResource(R.mipmap.ic_launcher);
            imageView.setBackgroundColor(imageView.hashCode());
        }
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int childCount = gridLayout.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = gridLayout.getChildAt(i);
                    ViewGroup.LayoutParams lp = child.getLayoutParams();
                    lp.height = child.getMeasuredWidth();
                    gridLayout.updateViewLayout(child, lp);
                }
            }
        });
    }

    private void startGridLayoutAnimation() {
        int childCount = gridLayout.getChildCount();
        Animator[] animators = new Animator[childCount];
        for (int i = 0; i < childCount; i++) {
            View child = gridLayout.getChildAt(i);
            animators[i] = createAnimatorForChild(child, i);
        }
        AnimatorSet childrenAnimator = new AnimatorSet();
        childrenAnimator.playTogether(animators);
        childrenAnimator.addListener(listener);
        childrenAnimator.start();
    }

    private Animator createAnimatorForChild(View view, int index) {
        if (view == null) {
            return null;
        }
        boolean isOddIndex = index % 2 == 0;
        int direction = isOddIndex ? -1 : 1;
        XyProperty xyProperty = new XyProperty("XY");
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofObject(xyProperty, null, new QuadPathMotion(new PointF(direction * 200, 0)).getPath(view.getLeft(), view.getTop(), view.getLeft(), view.getTop() + 400));

        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f);

        PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("rotation", 0, direction * -30, 0, direction * 30, 0);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, holder1, holder2, holder3);
        animator.setStartDelay(100 + index * 100);
        animator.setDuration(4 * DEFAULT_ANIMATION_DURATION);
        return animator;
    }

    private void resetGridChildren() {
        int childCount = gridLayout.getChildCount();
        if (childCount <= 0) {
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View child = gridLayout.getChildAt(i);
            child.setAlpha(1f);
            child.setTranslationY(0f);
        }
    }

    private final class ChildAnimationListener extends AnimatorListenerAdapter {

        @Override
        public void onAnimationEnd(@NonNull Animator animation) {
            animationState = STATE_DONE;
            button.setText(R.string.reset_animation);
        }
    }

}
