package com.fanhua.tominiprogram.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.fanhua.tominiprogram.R;
import com.fanhua.uiadapter.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BottomMenu2 extends BottomSheetDialogFragment{
    /**
     * 顶部向下偏移量
     */
    private int topOffset = 600;
    private BottomSheetBehavior<FrameLayout> behavior;

    private BottomSheetDialog bottomSheetDialog;

    private List<String> datas;

    private BottomMenuAdapter menuAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("BottomMenu2.onCreateDialog 创建");
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return bottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("BottomMenu2.onCreateView");
        View dialogView = inflater.inflate(R.layout.dialog_monitor_detail2, container, false);
        return dialogView;
    }

    public void requestData(String id){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if(id.equals("1")) {
                list.add("这是第一种数据" + i);
            } else if (id.equals("2")) {
                list.add("这是第二种数据" + i);
            }else{
                list.add("这是第三种数据" + i);
            }
        }

        menuAdapter.replaceData(list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rv_list = view.findViewById(R.id.rv_list);
        menuAdapter = new BottomMenuAdapter(getActivity(), datas);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL,false));
        rv_list.setAdapter(menuAdapter);
        String id = getArguments().getString("id");
        System.out.println("BottomMenu2.onViewCreated"+id);
        if(id != null){
            requestData(id);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 设置软键盘不自动弹出
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = getHeight();
            behavior = BottomSheetBehavior.from(bottomSheet);
            // 初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.TransparentBottomSheetStyle);
    }

    /**
     * 获取屏幕高度
     *
     * @return height
     */
    private int getHeight() {
        int height = ScreenUtils.getScreenHeight(getActivity());
        if (getContext() != null) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            if (wm != null) {
                // 使用Point已经减去了状态栏高度
                wm.getDefaultDisplay().getSize(point);
                height = point.y - getTopOffset();
            }
        }
        return height;
    }

    public int getTopOffset() {
        return topOffset;
    }

    public void setTopOffset(int topOffset) {
        this.topOffset = topOffset;
    }

    public BottomSheetBehavior<FrameLayout> getBehavior() {
        return behavior;
    }
}
