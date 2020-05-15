package cn.nano.main.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import cn.nano.common.widget.VerificationCodeEditText;
import cn.nano.main.R;

public class GenderSelectorDialog {
    public static Dialog create(final Context context, final GenderSelectorWatch callback) {
        Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_gender_selector);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                String gender = "";
                if (id == R.id.gender_male) {
                    gender = context.getString(R.string.male);
                }

                if (id == R.id.gender_female) {
                    gender = context.getString(R.string.female);
                }

                if (id == R.id.gender_none) {
                    gender = context.getString(R.string.in_secret);
                }

                if (callback != null) {
                    callback.onSelect(gender);
                }
            }
        };
        dialog.findViewById(R.id.gender_male).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.gender_female).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.gender_none).setOnClickListener(onClickListener);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


    public interface GenderSelectorWatch {
        void onSelect(String gender);
    }
}
