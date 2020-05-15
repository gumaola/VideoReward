package cn.nano.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import cn.nano.common.R;

public class ArcImageView extends AppCompatImageView {
    /*
     *弧形高度
     */
    private int arcHeight;
    private Path arcPath;
    private static final String TAG = "ArcImageView";

    public ArcImageView(Context context) {
        this(context, null);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcImageView);
        arcHeight = typedArray.getDimensionPixelSize(R.styleable.ArcImageView_arcHeight, 0);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPath(w, h);
    }

    private void initPath(int width, int height) {
        if (arcPath == null) {
            arcPath = new Path();
        } else {
            arcPath.reset();
        }

        arcPath.moveTo(0, 0);
        arcPath.lineTo(0, height - arcHeight);
        arcPath.quadTo(width / 2, height, width, height - arcHeight);
        arcPath.lineTo(width, 0);
        arcPath.lineTo(0, 0);

        arcPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(arcPath);
        super.onDraw(canvas);
    }

}
