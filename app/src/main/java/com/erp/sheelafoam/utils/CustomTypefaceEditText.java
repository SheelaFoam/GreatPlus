package com.erp.sheelafoam.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import com.erp.sheelafoam.R;

/**
 * Created by E5956 on 4/26/2018.
 */

public class CustomTypefaceEditText extends android.support.v7.widget.AppCompatEditText {

        private static final String TAG = "TextView";
        private Typeface mTypeface;

	public CustomTypefaceEditText(Context context, AttributeSet attrs, int defStyle)
        {
            super(context, attrs, defStyle);
            setCustomFont(context, attrs);
        }

	public CustomTypefaceEditText(Context context, AttributeSet attrs)
        {
            super(context, attrs);
            setCustomFont(context, attrs);
        }

	public CustomTypefaceEditText(Context context)
        {
            super(context);
        }

        private void setCustomFont (Context ctx, AttributeSet attrs)
        {
            TypedArray fontTypedArray = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTypefaceTextView);
            String textStyle = fontTypedArray.getString(R.styleable.CustomTypefaceTextView_font_name_with_asset_path);
            try {
                if (textStyle == null) {
                    mTypeface = Typeface.createFromAsset(ctx.getAssets(), "fonts/NanumGothic-Regular.ttf");
                } else {
                    mTypeface = Typeface.createFromAsset(ctx.getAssets(), textStyle);
                }
            } catch (Exception e) {
                Log.w(TAG, "Could not get typeface: " + textStyle + "&&" + e.getMessage() + " " + this.getId());
            }
            setTypeface(mTypeface);
            fontTypedArray.recycle();
        }
    }
