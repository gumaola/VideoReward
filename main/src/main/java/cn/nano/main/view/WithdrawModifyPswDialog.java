package cn.nano.main.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import cn.nano.common.widget.VerificationCodeEditText;
import cn.nano.main.R;

public class WithdrawModifyPswDialog {
    public static Dialog create(Context context, final WithdrawModifyCallback callback) {
        Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_modify_withedraw_psw);

        final VerificationCodeEditText psw = dialog.findViewById(R.id.withdraw_modify_input_psw);
        final VerificationCodeEditText confiorm = dialog.findViewById(R.id.withdraw_confirm_input_psw);

        dialog.findViewById(R.id.withdraw_modify_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    String pswStr = psw.getCode();
                    String confirmStr = confiorm.getCode();

                    callback.onYesClick(pswStr, confirmStr);
                }
            }
        });

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public static void reset(Dialog dialog) {
        VerificationCodeEditText psw = dialog.findViewById(R.id.withdraw_modify_input_psw);
        VerificationCodeEditText confiorm = dialog.findViewById(R.id.withdraw_confirm_input_psw);

        psw.clear();
        confiorm.clear();
    }


    public interface WithdrawModifyCallback {
        void onYesClick(String psw, String confirm);
    }
}
