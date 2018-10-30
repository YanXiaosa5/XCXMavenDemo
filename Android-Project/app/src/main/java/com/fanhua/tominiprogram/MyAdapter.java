package com.fanhua.tominiprogram;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanhua.uiadapter.LibApplication;
import com.fanhua.uiadapter.ResolutionAdapter;
import com.fanhua.uiadapter.enums.ViewScaleType;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<String> datas;

    private Context context;

    private ResolutionAdapter resolutionAdapter;
    
    public MyAdapter(List<String> datas,Context context){
        this.datas = datas;
        this.context = context;
        resolutionAdapter = LibApplication.getInstance().getResolutionAdapter();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(context,R.layout.tem_adapter,null);
        }
        TextView tv_content = convertView.findViewById(R.id.tv_content);
        tv_content.setText(datas.get(position));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        convertView.setLayoutParams(layoutParams);
        resolutionAdapter.setTextSize(tv_content,28);
        return convertView;
    }

}
