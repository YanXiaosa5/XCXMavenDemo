package com.fanhua.tominiprogram.listdownload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fanhua.tominiprogram.R;

import java.util.ArrayList;
import java.util.List;

public class BackgroundInstallActivity extends AppCompatActivity {

    private List<MainBean> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_install);
        datalist = new ArrayList<>();
    }

    private void initData() {
        MainBean bean = new MainBean("高祖提剑入咸阳，炎炎红日升扶桑", "https://bd.wearekids.cn/chatapk/channel/liaoke_fy46.apk", false);
        datalist.add(bean);
        MainBean bean1 = new MainBean("光武中兴续大统，金乌飞上天中央", "https://apk.huakuitv.com/channel/hkfy1.apk", false);
        datalist.add(bean1);
        MainBean bean2 = new MainBean("哀哉献帝绍海宇，红轮西坠咸池榜", "http://static.gsyjgy.com/apk/hj_AZRONG_su1.apk", false);
        datalist.add(bean2);
        MainBean bean3 = new MainBean("何进无谋中贵乱，凉州董卓居朝堂", "http://dl.hongyanapp.cn/TianTu_v1.1_BB08_4223.apk", false);
        datalist.add(bean3);
        MainBean bean4 = new MainBean("王允定计诛逆党，李榷郭汜兴刀枪", "https://bd.wearekids.cn/chatapk/channel/liaoke_fy46.apk", false);
        datalist.add(bean4);
        MainBean bean5 = new MainBean("四方盗贼如蚁聚，六合奸雄皆鹰扬", "https://apk.huakuitv.com/channel/hkfy1.apk", false);
        datalist.add(bean5);
        MainBean bean6 = new MainBean("孙坚孙策起江左，袁绍袁术兴河梁", "http://static.gsyjgy.com/apk/hj_AZRONG_su1.apk", false);
        datalist.add(bean6);
        MainBean bean7 = new MainBean("刘焉父子居巴蜀，刘表羁旅屯荆襄", "http://static.gsyjgy.com/apk/hj_AZRONG_su1.apk", false);
        datalist.add(bean7);
        MainBean bean8 = new MainBean("张燕张鲁霸南郑，马腾韩遂守西凉", "http://static.gsyjgy.com/apk/hj_AZRONG_su1.apk", false);
        datalist.add(bean8);
        MainBean bean9 = new MainBean("陶谦张绣公孙瓒，各逞雄才占一方", "http://static.gsyjgy.com/apk/hj_AZRONG_su1.apk", false);
        datalist.add(bean9);
    }
}
