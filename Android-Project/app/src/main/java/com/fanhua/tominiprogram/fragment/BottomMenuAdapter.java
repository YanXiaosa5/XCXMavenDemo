package com.fanhua.tominiprogram.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanhua.tominiprogram.R;
import com.fanhua.uiadapter.utils.DensityUtils;
import com.fanhua.uiadapter.utils.ViewUtils;

import java.util.List;

public class BottomMenuAdapter  extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public BottomMenuAdapter(Activity context, List<String> datas) {
        super(R.layout.item_bottom_menu,datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_content,item+"");
        RelativeLayout rl_item = helper.getView(R.id.rl_item);
        ViewUtils.setViewHeight(rl_item, DensityUtils.dp2px(mContext,50));
    }
}
