package com.test.tudou.multitagview.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.test.tudou.multitagview.util.DrawableUtils;

import java.util.ArrayList;

/**
 * Created by tudou on 15-1-3.
 */
public class MultiTagView extends LinearLayout {
    private final int DEFAULT_BUTTON_PADDING = 12;
    private final int DEFAULT_BUTTON_MARGIN = 12;
    private final int DEFAULT_BUTTON_PADDING_TOP = 6;
    private final int DEFAULT_LAYOUT_MARGIN_TOP = 12;
    private final int DEFAULT_TAG_HEIGHT = 35;

    private ArrayList<String> tags;

    private int mEditTextWidth;
    private int tempWidth = 0;
    private LinearLayout mLayoutItem;
    private Context mContext;

    public MultiTagView(Context context) {
        this(context, null);
    }

    public MultiTagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mContext = context;
        init();
    }

    private void init() {
        tags = new ArrayList<>();
        mLayoutItem = new LinearLayout(mContext);
        mLayoutItem.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(mLayoutItem);
        addClickButton();
    }

    private void addClickButton() {
        Button buttonInput = new Button(mContext);
        buttonInput.setPadding(dip2px(DEFAULT_BUTTON_PADDING), dip2px(DEFAULT_BUTTON_PADDING_TOP), dip2px(DEFAULT_BUTTON_PADDING), dip2px(DEFAULT_BUTTON_PADDING_TOP));
        buttonInput.setText("添加");
        buttonInput.setTextColor(Color.parseColor("#666666"));
        buttonInput.setTextSize(15);
        buttonInput.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(mContext);
                new AlertDialog.Builder(mContext)
                        .setMessage("输入内容")
                        .setView(editText)
                        .setPositiveButton("sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!editText.getText().toString().trim().equals("")) {
                                    tags.add(editText.getText().toString().trim());
                                    refresh();
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        mEditTextWidth = (int) (2 * dip2px(DEFAULT_BUTTON_PADDING) + buttonInput.getPaint().measureText("添加"));
        LayoutParams layoutParams = new LayoutParams(mEditTextWidth, dip2px(DEFAULT_TAG_HEIGHT));
        tempWidth += dip2px(DEFAULT_BUTTON_MARGIN) + mEditTextWidth; //add tag width
        //the last tag margin right DEFAULT_BUTTON_MARGIN, don't forget
        if(tempWidth + dip2px(DEFAULT_BUTTON_MARGIN) > getDeviceWidth()){  //if out of screen, add a new layout
            mLayoutItem  = new LinearLayout(mContext);
            LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lParams.topMargin = dip2px(DEFAULT_LAYOUT_MARGIN_TOP);
            mLayoutItem.setLayoutParams(lParams);
            addView(mLayoutItem);
            tempWidth = (int) (2*dip2px(DEFAULT_BUTTON_PADDING) + buttonInput.getPaint().measureText(buttonInput.getText().toString()));
        }
        mLayoutItem.addView(buttonInput, layoutParams);
        tempWidth -= dip2px(DEFAULT_BUTTON_MARGIN) + mEditTextWidth;
    }

    private void addTag(String content) {
        Button button = new Button(mContext);
        button.setText(content);
        button.setTextColor(Color.parseColor("#ffffff"));
        button.setTextSize(15);
        button.setBackgroundColor(Color.parseColor(DrawableUtils.getBackgoundColor()));
        button.setPadding(dip2px(DEFAULT_BUTTON_PADDING), dip2px(DEFAULT_BUTTON_PADDING_TOP),
                dip2px(DEFAULT_BUTTON_PADDING), dip2px(DEFAULT_BUTTON_PADDING_TOP));
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        int btnWidth = (int) (2*dip2px(DEFAULT_BUTTON_PADDING) + button.getPaint().measureText(button.getText().toString()));
        LayoutParams layoutParams = new LayoutParams(btnWidth, dip2px(DEFAULT_TAG_HEIGHT));
        layoutParams.rightMargin = dip2px(DEFAULT_BUTTON_MARGIN);
        tempWidth += dip2px(DEFAULT_BUTTON_MARGIN) + btnWidth; //add tag width
        //the last tag margin right DEFAULT_BUTTON_MARGIN, don't forget
        if(tempWidth + dip2px(DEFAULT_BUTTON_MARGIN) > getDeviceWidth()){  //if out of screen, add a new layout
            mLayoutItem  = new LinearLayout(mContext);
            LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lParams.topMargin = dip2px(DEFAULT_LAYOUT_MARGIN_TOP);
            mLayoutItem.setLayoutParams(lParams);
            addView(mLayoutItem);
            tempWidth = (int) (2 * dip2px(DEFAULT_BUTTON_PADDING) + button.getPaint().measureText(button.getText().toString()));
        }
        mLayoutItem.addView(button, layoutParams);
    }

    private void refresh() {
        removeAllViews();
        mLayoutItem = new LinearLayout(mContext);
        mLayoutItem.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(mLayoutItem);
        tempWidth = 0;
        for (String s : tags) {
            addTag(s);
        }
        addClickButton();
    }

    private int getDeviceWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}