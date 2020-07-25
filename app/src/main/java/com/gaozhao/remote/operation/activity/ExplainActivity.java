package com.gaozhao.remote.operation.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.gaozhao.remote.MainActivity;
import com.gaozhao.remote.R;
import com.gaozhao.remote.RemotePaymentBaseActivity;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Author by GaoZhao in RemotePayment
 * Email 18093546728@163.com
 * Date on 2020/7/23.
 * https://me.csdn.net/gao511147456
 */
public class ExplainActivity extends RemotePaymentBaseActivity {
    private VideoView mVideoNet ;
    private JZVideoPlayerStandard jzVideoPlayerStandard;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operational_explain);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("操作说明");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MediaController  mediaController = new MediaController(this);

        //本地视频
        //String uri = "android.resource://" + getPackageName() + "/" + R.raw.big_buck_bunny;
        //网络视频 需要开通网络访问权限

        String uri = "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4";
        jzVideoPlayerStandard = findViewById(R.id.jz_video_player);
        Glide.with(this).load("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1207756410,2136733322&fm=26&gp=0.jpg").into(jzVideoPlayerStandard.thumbImageView);
        jzVideoPlayerStandard.setUp(uri,JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,"微信收款设置演示");



    }

    @Override
    protected void onResume() {
        super.onResume();
        /*

        MediaController localMediaController = new MediaController(this);
        mVideoNet.setMediaController(localMediaController);
        Uri uri = Uri.parse("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
        mVideoNet.setVideoURI(uri);
        mVideoNet.start();

        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        jzVideoPlayerStandard.release();
    }
}
