package com.jusfoun.jusfouninquire.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.qxb.jianhang.R;
import com.qxb.jianhang.ui.adapter.ContactWayAdapter;
import com.qxb.jianhang.ui.data.ContactinFormationModel;

import java.util.List;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/26.
 * Description
 */
public class ContactWayDialog extends Dialog {
    private ListView listView;
    private ContactWayAdapter adapter;
    private Context context;
    private TextView mClose;
    public ContactWayDialog(Context context) {
        super(context);
        this.context=context;
    }

    public ContactWayDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
    }

    protected ContactWayDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contact_way);
        initView();
        initViewAction();
    }

    public void initView(){
        listView= (ListView) findViewById(R.id.contact_way_list);
        mClose = (TextView) findViewById(R.id.close_dialog);
    }

    public void initViewAction(){
        adapter=new ContactWayAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null)
                    listener.onItemClick(parent, view, position, id);
            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void refresh(List<ContactinFormationModel> list){
        if (list!=null)
            adapter.refresh(list);
    }

    public void setOnItemtClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    private OnItemClickListener listener;
    public interface OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
