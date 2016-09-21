package proj.me.discovery.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

import proj.me.discovery.R;

/**
 * Created by root on 20/12/15.
 */
public class CustomEditText extends EditText{
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.custom_font);
        String font = typedArray.getString(0);
        typedArray.recycle();
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/"+font);
        setTypeface(tf);
    }
}
