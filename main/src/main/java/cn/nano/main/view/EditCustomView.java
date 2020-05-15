package cn.nano.main.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.nano.main.R;

public class EditCustomView extends RelativeLayout {

    private ImageView icon;
    private EditText editor;

    public EditCustomView(Context context) {
        this(context, null);
    }

    public EditCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.layout_login_eidtor, this);
        icon = findViewById(R.id.login_editor_icon);
        editor = findViewById(R.id.login_editor_edit);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditCustomView);
        int icoRes = typedArray.getResourceId(R.styleable.EditCustomView_icon, 0);
        String hint = typedArray.getString(R.styleable.EditCustomView_hint);
        typedArray.recycle();

        icon.setImageResource(icoRes);
        editor.setHint(hint);
    }

    public EditText getEditor() {
        return editor;
    }

    public ImageView getIconView() {
        return icon;
    }
}
