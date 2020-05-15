package cn.nano.main.comment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import cn.nano.common.app.CommonActivity;
import cn.nano.main.R;

public class CommentEditorActivity extends CommonActivity {

    public static final int COMMENT_EDITOR_REQUEST_CODE = 0x2000;
    public static final String COMMENT_EDITOR_RESULT = "comment_result";


    private EditText editor;

    private View root;

    //统一跳转
    public static void forward(Activity context) {
        Intent intent = new Intent(context, CommentEditorActivity.class);
        context.startActivityForResult(intent, COMMENT_EDITOR_REQUEST_CODE);
        context.overridePendingTransition(0, 0);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_comment_editor);
        editor = findViewById(R.id.comment_editor);

        root = findViewById(R.id.comment_editor_root);


        findViewById(R.id.comment_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });


        editor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    goBack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShouldHideKeyboard(root, ev)) {
                hideInputKeyBroad();
                finish();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    protected boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null) {
            Rect r = new Rect();

            v.getGlobalVisibleRect(r);


            return !r.contains((int) event.getX(), (int) event.getY());
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        String content = editor.getText().toString();

        if (!TextUtils.isEmpty(content)) {
            Intent i = new Intent();
            i.putExtra(COMMENT_EDITOR_RESULT, content);
            setResult(RESULT_OK, i);
        }

        finish();
        overridePendingTransition(0, 0);
    }


}
