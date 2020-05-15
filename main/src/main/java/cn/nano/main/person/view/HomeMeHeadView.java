package cn.nano.main.person.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.nano.common.glide.ImgLoader;
import cn.nano.common.utils.DialogUtil;
import cn.nano.common.utils.ToastUtil;
import cn.nano.main.R;
import cn.nano.main.account.AccountManager;
import cn.nano.main.person.CertificationActivity;
import cn.nano.main.person.MemberListActivity;
import cn.nano.main.person.PersonInfoEditActivity;
import cn.nano.main.server.AutoLoginOutCallback;
import cn.nano.main.server.ServerApi;
import cn.nano.main.server.result.BaseResult;
import cn.nano.main.server.result.MemberInfoResult;
import cn.nano.main.setting.SettingActivity;
import cn.nano.main.view.WithdrawDialog;
import cn.nano.main.view.WithdrawModifyPswDialog;
import okhttp3.Call;

public class HomeMeHeadView implements View.OnClickListener {

    //bind member info
    private ImageView backIcon;
    private ImageView settingIcon;
    private ImageView userAvatar;
    private TextView userName;
    private TextView userId;
    private TextView userVerifyCode;
    private TextView editUserInfo;
    private TextView followUser;
    private TextView userGender;
    private TextView userLocation;
    private TextView userBalance;
    private TextView getMoney;
    private TextView followCountV;
    private TextView fansCountV;
    private TextView favorCountV;
    private TextView userDesc;
    private ImageView genderIconV;

    //提现相关dialog
    private Dialog witdrawDialog;
    private Dialog withdrawModifyDialog;

    private boolean isSelf = true;
    private int otherMemberId = -1;

    private Activity parents;

    private Dialog loading;
    private IPresenter presenter;

    private View inflateView;

    public HomeMeHeadView(Activity p, IPresenter p2, ViewGroup view, boolean self, int id) {
        parents = p;
        presenter = p2;
        isSelf = self;
        otherMemberId = id;

        View head = LayoutInflater.from(p).inflate(R.layout.layout_me_head_view, view, false);
        intflateView(head);
    }

    public View getInfateView() {
        return inflateView;
    }


    private void intflateView(View view) {
        inflateView = view;
        loading = DialogUtil.loadingDialog(parents, null);
        backIcon = view.findViewById(R.id.person_info_back);
        settingIcon = view.findViewById(R.id.person_info_setting);
        userAvatar = view.findViewById(R.id.person_info_avatar);
        userName = view.findViewById(R.id.person_info_name);
        userId = view.findViewById(R.id.person_info_id);
        userVerifyCode = view.findViewById(R.id.person_info_real);
        editUserInfo = view.findViewById(R.id.person_info_edit);
        followUser = view.findViewById(R.id.person_info_follow);
        userGender = view.findViewById(R.id.person_info_gender_text);
        userLocation = view.findViewById(R.id.person_info_location_text);
        userBalance = view.findViewById(R.id.person_info_balance);
        getMoney = view.findViewById(R.id.person_info_get_money);
        followCountV = view.findViewById(R.id.person_info_follow_count);
        fansCountV = view.findViewById(R.id.person_info_fans_count);
        favorCountV = view.findViewById(R.id.person_info_favor_count);
        userDesc = view.findViewById(R.id.person_info_desc);
        genderIconV = view.findViewById(R.id.person_info_gender_icon);

        if (isSelf) {
            view.findViewById(R.id.person_info_follow_count_root).setOnClickListener(this);
            view.findViewById(R.id.person_info_fans_count_root).setOnClickListener(this);
        }

        if (isSelf) {
            settingIcon.setVisibility(View.VISIBLE);
            editUserInfo.setVisibility(View.VISIBLE);
            followUser.setVisibility(View.GONE);
            getMoney.setVisibility(View.VISIBLE);
        } else {
            settingIcon.setVisibility(View.GONE);
            editUserInfo.setVisibility(View.GONE);
            followUser.setVisibility(View.VISIBLE);
            getMoney.setVisibility(View.GONE);
        }

        //说明是me tab
        if (otherMemberId < 0) {
            backIcon.setVisibility(View.GONE);
        } else {
            backIcon.setVisibility(View.VISIBLE);
        }

        settingIcon.setOnClickListener(this);
        getMoney.setOnClickListener(this);
        editUserInfo.setOnClickListener(this);
        followUser.setOnClickListener(this);
        backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.person_info_back) {
            parents.finish();
            return;
        }

        if (id == R.id.person_info_setting) {
            SettingActivity.forward(parents);
            return;
        }

        if (id == R.id.person_info_get_money) {
            getMoney();
            return;
        }

        if (id == R.id.person_info_edit) {
            PersonInfoEditActivity.forward(parents);
            return;
        }

        if (id == R.id.person_info_follow) {
            followUser();
            return;
        }


        if (id == R.id.person_info_fans_count_root) {
            MemberListActivity.forward(parents, MemberListActivity.fans);
            return;
        }

        if (id == R.id.person_info_follow_count_root) {
            MemberListActivity.forward(parents, MemberListActivity.followers);
        }
    }


    private void followUser() {
        loading.show();
        ServerApi.follow(AccountManager.INSTANCE.getUserToken(), 1, otherMemberId, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                loading.dismiss();
                ToastUtil.show(R.string.common_error);
            }

            @Override
            public void onResponse(BaseResult response, int id) {
                loading.dismiss();
                if (response.getCode() != BaseResult.SUCCESS_CODE) {
                    ToastUtil.show(response.getMsg());
                    return;
                }

                //todo
                followUser.setText(R.string.followed);
            }
        });
    }


    /**
     * 提现对话框弹出
     */
    private void getMoney() {
        if (witdrawDialog == null) {
            witdrawDialog = WithdrawDialog.create(parents, new WithdrawDialog.withdrawClick() {
                @Override
                public void onYesClick(String coinStr, String psw) {
                    witdrawDialog.dismiss();
                    postGetMoney(coinStr, psw);
                }
            });
        } else {
            WithdrawDialog.reset(parents, witdrawDialog);
        }

        witdrawDialog.show();
    }

    /**
     * 提现网络请求
     *
     * @param coinStr
     * @param psw
     */
    private void postGetMoney(String coinStr, String psw) {
        if (TextUtils.isEmpty(coinStr)) {
            ToastUtil.show("提现金额为必填项");
            return;
        }

        int coin = Integer.parseInt(coinStr);
        if (coin <= 0) {
            ToastUtil.show("提现金额为必填项");
            return;
        }

        if (TextUtils.isEmpty(psw) || psw.length() < 6) {
            ToastUtil.show("提现密码为必填项！");
            return;
        }

        loading.show();
        ServerApi.withdraw(AccountManager.INSTANCE.getUserToken(), coin, psw, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onResponse(BaseResult response, int id) {
                super.onResponse(response, id);
                loading.dismiss();
                if (response.getCode() == BaseResult.SUCCESS_CODE) {
                    ToastUtil.show(R.string.withdraw_success);
                    //todo refresh
                    presenter.onRefresh();
                } else {
                    int code = response.getCode();
                    switch (code) {
                        case 6://未设置提现密码
                            ToastUtil.show("请先设置您的提现密码");
                            showModifyPsw();
                            break;
                        case 8://未实名认证
                            ToastUtil.show("请先实名认证");
                            CertificationActivity.forward(parents);
                            break;
                        default:
                            ToastUtil.show(response.getMsg());
                            break;

                    }
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                loading.dismiss();
                ToastUtil.show(R.string.common_error);
            }
        });
    }


    /**
     * 设置密码对话框弹出
     */
    private void showModifyPsw() {
        if (withdrawModifyDialog == null) {
            withdrawModifyDialog = WithdrawModifyPswDialog.create(parents, new WithdrawModifyPswDialog.WithdrawModifyCallback() {
                @Override
                public void onYesClick(String psw, String confirm) {
                    withdrawModifyDialog.dismiss();
                    if (TextUtils.isEmpty(psw) || psw.length() < 6) {
                        ToastUtil.show("请输入正确6位密码");
                        return;
                    }

                    if (TextUtils.isEmpty(confirm) || confirm.length() < 6) {
                        ToastUtil.show("确认密码格式不正确");
                        return;
                    }

                    if (!psw.equalsIgnoreCase(confirm)) {
                        ToastUtil.show("两次密码不匹配");
                        return;
                    }

                    modifyPsw(psw, confirm);
                }
            });
        } else {
            WithdrawModifyPswDialog.reset(withdrawModifyDialog);
        }

        withdrawModifyDialog.show();
    }

    /**
     * 设置密码网络请求
     *
     * @param psw
     * @param confirm
     */
    private void modifyPsw(String psw, String confirm) {
        loading.show();

        ServerApi.modifyWithdrawPsw(AccountManager.INSTANCE.getUserToken(), psw, confirm, new AutoLoginOutCallback<BaseResult>() {
            @Override
            public void onResponse(BaseResult response, int id) {
                super.onResponse(response, id);
                loading.dismiss();
                if (response.getCode() == BaseResult.SUCCESS_CODE) {
                    ToastUtil.show(R.string.modify_psw_success);
                } else {
                    ToastUtil.show(response.getMsg());
                }
            }


            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                loading.dismiss();
                ToastUtil.show(R.string.common_error);
            }
        });
    }


    private void bindMemberResult(MemberInfoResult result) {
        String avatar = result.getData().getMInfo().getMember_avatar();
        String name = result.getData().getMInfo().getNick_name();
        String ID = String.valueOf(result.getData().getMInfo().getMember_id());
        String gender = "保密";
        if (result.getData().getMInfo().getMember_sex() == 1) {
            genderIconV.setImageResource(R.mipmap.ico_male);
            genderIconV.setVisibility(View.VISIBLE);
            gender = parents.getString(R.string.male);
        } else if (result.getData().getMInfo().getMember_sex() == 2) {
            genderIconV.setImageResource(R.mipmap.ico_female);
            genderIconV.setVisibility(View.VISIBLE);
            gender = parents.getString(R.string.female);
        } else {
            genderIconV.setVisibility(View.GONE);
        }

        int age = result.getData().getMInfo().getMember_age();
        if (age > 0) {
            gender = gender + age;
        }

        String location = result.getData().getMInfo().getMember_city();
        if (TextUtils.isEmpty(location)) {
            location = "保密";
        }

        String desc = result.getData().getMInfo().getMember_autograph();
        if (TextUtils.isEmpty(desc)) {

            desc = parents.getString(isSelf ? R.string.no_desc_my : R.string.no_desc_other);
        }
        String balance = String.valueOf(result.getData().getMInfo().getCoin_num());

        String followCount = String.valueOf(result.getData().getMInfo().getFollow_num());
        String fansCount = String.valueOf(result.getData().getMInfo().getFans_num());
        String favorCount = String.valueOf(result.getData().getMInfo().getLike_vieo_num());

        ImgLoader.displayWithError(parents, avatar, userAvatar, R.mipmap.icon_avatar_placeholder);

        userName.setText(name);
        userId.setText(String.format(parents.getString(R.string.ID), ID));
        userGender.setText(gender);
        userLocation.setText(location);
        userBalance.setText(balance);
        followCountV.setText(followCount);
        fansCountV.setText(fansCount);
        favorCountV.setText(favorCount);
        userDesc.setText(desc);
    }


    //暴露接口
    public void refreshUserInfo() {
        if (isSelf) {
            loadSelfData();
        } else {
            loadOtherData();
        }
    }

    private void loadSelfData() {
        ServerApi.getSelfInfo(AccountManager.INSTANCE.getUserToken(), new AutoLoginOutCallback<MemberInfoResult>() {
            @Override
            public void onResponse(MemberInfoResult response, int id) {
                presenter.onEndRefresh();
                if (response != null && response.getCode() == BaseResult.SUCCESS_CODE) {
                    AccountManager.INSTANCE.syncUserInfo(response.getData().getMInfo());
                    bindMemberResult(response);
                    return;
                }

                ToastUtil.show(response == null ? parents.getString(R.string.common_error) : response.getMsg());
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                presenter.onEndRefresh();
                ToastUtil.show(R.string.common_error);
            }
        });
    }


    private void loadOtherData() {
        loading.show();
        ServerApi.getOtherUserInfo(AccountManager.INSTANCE.getUserToken(), otherMemberId, new AutoLoginOutCallback<MemberInfoResult>() {
            @Override
            public void onError(Call call, Exception e, int id) {
                loading.dismiss();
                ToastUtil.show(R.string.common_error);
            }

            @Override
            public void onResponse(MemberInfoResult response, int id) {
                loading.dismiss();
                if (response != null && response.getCode() == BaseResult.SUCCESS_CODE) {

                    bindMemberResult(response);
                    return;
                }

                ToastUtil.show(response == null ? parents.getString(R.string.common_error) : response.getMsg());
            }

        });
    }
}
