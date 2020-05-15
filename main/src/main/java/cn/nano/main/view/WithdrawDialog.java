package cn.nano.main.view;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import cn.nano.common.utils.PreferenceUtil;
import cn.nano.common.widget.VerificationCodeEditText;
import cn.nano.main.R;
import cn.nano.main.constant.AppPrefs;

public class WithdrawDialog {

    private static final DecimalFormat NUM_FROMAT = new DecimalFormat("0.#");

    public static Dialog create(final Context context, final withdrawClick click) {
        Dialog dialog = new Dialog(context, R.style.dialog);

        //创建view
        dialog.setContentView(R.layout.dialog_withdraw);

        final EditText inoputCoin = dialog.findViewById(R.id.withdraw_input_coin);
        final VerificationCodeEditText inputPsw = dialog.findViewById(R.id.withdraw_input_psw);
        final TextView cost = dialog.findViewById(R.id.withdraw_cost);

        cost.setText(String.format(context.getString(R.string.withdraw_cost), NUM_FROMAT.format(0)));
        inoputCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float ratio = PreferenceUtil.getFloat(context, AppPrefs.FILE_CONFIG, AppPrefs.KEY_CHARGE_RATIO, 0);
                String text = s.toString();
                int coin = 0;
                if (!TextUtils.isEmpty(text)) {
                    coin = Integer.parseInt(text.trim());
                }

                cost.setText(String.format(context.getString(R.string.withdraw_cost), NUM_FROMAT.format(coin * ratio)));

            }
        });

        dialog.findViewById(R.id.withdraw_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coinStr = inoputCoin.getText().toString();
                String psw = inputPsw.getCode();
                if (click != null) {
                    click.onYesClick(coinStr, psw);
                }
            }
        });


        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public static void reset(Context context, Dialog dialog) {
        EditText inoputCoin = dialog.findViewById(R.id.withdraw_input_coin);
        VerificationCodeEditText inputPsw = dialog.findViewById(R.id.withdraw_input_psw);
        TextView cost = dialog.findViewById(R.id.withdraw_cost);

        inoputCoin.setText("");
        inputPsw.clear();
        cost.setText(String.format(context.getString(R.string.withdraw_cost), NUM_FROMAT.format(0)));
    }


    public interface withdrawClick {
        void onYesClick(String coinStr, String psw);
    }
}
