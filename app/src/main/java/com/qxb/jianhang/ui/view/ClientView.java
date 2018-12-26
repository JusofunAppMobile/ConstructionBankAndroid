package com.qxb.jianhang.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.data.ClientInfoModel;
import com.qxb.jianhang.ui.data.TrackRecordDataModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liuguangdan
 * @version create at 2018/9/5/005 10:27
 * @Email lgd@jusfoun.com
 * @Description ${ 客户}
 */
public class ClientView extends LinearLayout {

    protected EditText etName;
    protected EditText etJob;
    protected EditText etName2;
    protected EditText editRemark;
    protected View rootView;
    protected EditText editDemand;
    protected EditText editSuggest;
    protected CheckBox checkboxDemandNo;
    protected CheckBox checkboxDemandYes;
    protected EditText editSupprot;
    protected CheckBox checkboxSupportNo;
    protected CheckBox checkboxSupportYes;
    @BindView(R.id.tvLabel)
    TextView tvLabel;
    private int size = 1;
    private CheckBox checkBox;

    public ClientView(Context context, int size) {
        super(context);
        this.size = size + 1;
        init();
    }

    public ClientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClientView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_client, this);

        ButterKnife.bind(this);
        initView(this);
        tvLabel.setText("客户");
        checkBox.setChecked(true);
    }


    public ClientInfoModel getData() {
        ClientInfoModel model = new ClientInfoModel();
        model.job = etJob.getText().toString();
        model.name = etName.getText().toString();
        model.phone = etName2.getText().toString();
        model.remark = editRemark.getText().toString();
        model.linkPhone = checkBox.isChecked() + "";
        return model;
    }

    private void initView(View rootView) {
        etName = (EditText) rootView.findViewById(R.id.etName);
        etJob = (EditText) rootView.findViewById(R.id.tvJob);
        etName2 = (EditText) rootView.findViewById(R.id.etName2);
        editRemark = (EditText) rootView.findViewById(R.id.edit_remark);
        checkBox = (CheckBox) rootView.findViewById(R.id.checkbox);
        tvLabel = (TextView) rootView.findViewById(R.id.tvLabel);
        editDemand = (EditText) rootView.findViewById(R.id.edit_demand);
        editSuggest = (EditText) rootView.findViewById(R.id.edit_suggest);
        checkboxDemandNo = (CheckBox) rootView.findViewById(R.id.checkbox_demand_no);
        checkboxDemandYes = (CheckBox) rootView.findViewById(R.id.checkbox_demand_yes);
        editSupprot = (EditText) rootView.findViewById(R.id.edit_supprot);
        checkboxSupportNo = (CheckBox) rootView.findViewById(R.id.checkbox_support_no);
        checkboxSupportYes = (CheckBox) rootView.findViewById(R.id.checkbox_support_yes);


        checkboxDemandNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxDemandYes.setChecked(false);
                }
            }
        });


        checkboxDemandYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxDemandNo.setChecked(false);
                }
            }
        });

        checkboxSupportNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxSupportYes.setChecked(false);
                }
            }
        });

        checkboxSupportYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkboxSupportNo.setChecked(false);
                }
            }
        });
    }


    public void setData(TrackRecordDataModel.TrackRecordItemModel model) {
        etName.setText(model.name);
        etJob.setText(model.job);
        editRemark.setText(model.desc);
        checkBox.setChecked(model.linkPhone);
        etName2.setText(model.phone);

        editDemand.setText(model.demand);
        editSuggest.setText(model.suggest);
        editSupprot.setText(model.support);


        if("1".equals(model.isDemandSolved)){
            checkboxDemandYes.setChecked(true);
        }

        if("1".equals(model.isSupportSolved)){
            checkboxSupportYes.setChecked(true);
        }

    }


    /**
     * 获取客户需求
     */
    public String getDemand() {
        return editDemand.getText().toString();
    }

    /**
     * 获取客户建议
     */
    public String getSuggest() {
        return editSuggest.getText().toString();
    }

    /**
     * 获取客户支持
     */
    public String getSupport() {
        return editSupprot.getText().toString();
    }

    /**
     * 获取客户需求 是否解决
     */
    public String getDemandState() {
        if (checkboxDemandYes.isChecked()) {
            return "1";
        }
        return "0";
    }

    /**
     * 获取客户需求 是否解决
     */
    public String getSupportState() {
        if (checkboxSupportYes.isChecked()) {
            return "1";
        }
        return "0";
    }
}
