package cn.nano.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.nano.common.R;

public class VerificationCodeEditText extends FrameLayout {
    private String TAG = "VerificationCodeEditText";

    private Context context;

    private LinearLayout llCodeMain;
    private EditText editCodeNum;
    private TextView tvCode0, tvCode1, tvCode2, tvCode3, tvCode4, tvCode5;

    private int codeTextColor = 0xFF000000;//文字颜色默认黑色
    private int codeTextSize = 14;//文字默认14号字

    private boolean isPassword;

    //监听
    public interface OnInputListener {
        //输入监听
        void OnEdittextChange(CharSequence charSequence, int i, int i1, int i2);

        //输入完成监听
        void OnInputOk(String codeNum);
    }

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }


    public VerificationCodeEditText(Context context) {
        super(context);
        this.context = context;
    }

    public VerificationCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        loadView(attrs);
    }

    public VerificationCodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void loadView(AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.edittext_verification_code, this);

        //点击后先让输入框获取焦点，之后弹出软键盘，输入时进行各种判断
        //页面分两层，一层是显示输入的验证码，另一层是一个输入框，放已经输入的内容，

        initView(view, attrs);
        initEvent();

    }

    private void initView(View view, AttributeSet attrs) {
        llCodeMain = view.findViewById(R.id.v_ll_codeMain);

        editCodeNum = view.findViewById(R.id.v_edit_codeNum);

        tvCode0 = view.findViewById(R.id.v_tv_code0);
        tvCode1 = view.findViewById(R.id.v_tv_code1);
        tvCode2 = view.findViewById(R.id.v_tv_code2);
        tvCode3 = view.findViewById(R.id.v_tv_code3);
        tvCode4 = view.findViewById(R.id.v_tv_code4);
        tvCode5 = view.findViewById(R.id.v_tv_code5);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeEditText);
        codeTextColor = typedArray.getColor(R.styleable.VerificationCodeEditText_codeTextColor, codeTextColor);
        codeTextSize = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeEditText_codeTextSize, codeTextSize);
        isPassword = typedArray.getBoolean(R.styleable.VerificationCodeEditText_password, false);
        typedArray.recycle();

        //字体颜色
        tvCode0.setTextColor(codeTextColor);
        tvCode1.setTextColor(codeTextColor);
        tvCode2.setTextColor(codeTextColor);
        tvCode3.setTextColor(codeTextColor);
        tvCode4.setTextColor(codeTextColor);
        tvCode5.setTextColor(codeTextColor);

        //字体大小
        tvCode0.setTextSize(codeTextSize);
        tvCode1.setTextSize(codeTextSize);
        tvCode2.setTextSize(codeTextSize);
        tvCode3.setTextSize(codeTextSize);
        tvCode4.setTextSize(codeTextSize);
        tvCode5.setTextSize(codeTextSize);

        //输入框背景
        tvCode0.setBackgroundResource(R.drawable.bg_edit_num_left);
        tvCode1.setBackgroundResource(R.drawable.bg_edit_num_mid);
        tvCode2.setBackgroundResource(R.drawable.bg_edit_num_mid);
        tvCode3.setBackgroundResource(R.drawable.bg_edit_num_mid);
        tvCode4.setBackgroundResource(R.drawable.bg_edit_num_mid);
        tvCode5.setBackgroundResource(R.drawable.bg_edit_num_right);
    }

    private void initEvent() {
        //点击控件，获取焦点
        llCodeMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                editCodeNum.setFocusable(true);
                editCodeNum.setFocusableInTouchMode(true);
                editCodeNum.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editCodeNum, 0);
            }
        });


        //输入监听
        editCodeNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    Log.d(TAG, charSequence.toString());

                    String stInput = charSequence.toString();

                    //定义数组，把输入的内容拆开一个个放
                    char[] stCode = new char[]{6};
                    stCode = stInput.toCharArray();

                    switch (stInput.length()) {
                        case 1:
                            if (stCode[0] + "" != null) {
                                //显示当前的内容
                                tvCode0.setText(isPassword ? "●" : stCode[0] + "");
                                //清空右边一位的内容
                                tvCode1.setText("");
                            }
                            break;
                        case 2:
                            if (stCode[1] + "" != null) {
                                //显示当前的内容
                                tvCode1.setText(isPassword ? "●" : stCode[1] + "");
                                //清空右边一位的内容
                                tvCode2.setText("");
                            }
                            break;
                        case 3:
                            if (stCode[2] + "" != null) {
                                //显示当前的内容
                                tvCode2.setText(isPassword ? "●" : stCode[2] + "");
                                //清空右边一位的内容
                                tvCode3.setText("");
                            }
                            break;
                        case 4:
                            if (stCode[3] + "" != null) {
                                //显示当前的内容
                                tvCode3.setText(isPassword ? "●" : stCode[3] + "");
                                //清空右边一位的内容
                                tvCode4.setText("");

                            }
                            break;
                        case 5:
                            if (stCode[4] + "" != null) {
                                //显示当前的内容
                                tvCode4.setText(isPassword ? "●" : stCode[4] + "");
                                //清空右边一位的内容
                                tvCode5.setText("");
                            }
                            break;
                        case 6:
                            if (stCode[5] + "" != null) {
                                //显示当前的内容
                                tvCode5.setText(isPassword ? "●" : stCode[5] + "");
//                                //清空右边一位的内容
//                                tvCode5.setText("");

                                //成功回调
                                if (onInputListener != null) {
                                    onInputListener.OnInputOk(stInput);
                                }
                            }
                            break;
                    }

                } else {

                    tvCode0.setText("");

                }

                if (onInputListener != null) {
                    onInputListener.OnEdittextChange(charSequence, i, i1, i2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public String getCode() {
        return editCodeNum.getText().toString();
    }

    public void clear() {
        editCodeNum.setText("");
        tvCode0.setText("");
        tvCode1.setText("");
        tvCode2.setText("");
        tvCode3.setText("");
        tvCode4.setText("");
        tvCode5.setText("");
    }
}
