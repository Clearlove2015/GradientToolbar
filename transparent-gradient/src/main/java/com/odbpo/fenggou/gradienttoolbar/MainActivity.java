package com.odbpo.fenggou.gradienttoolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hankkin.gradationscroll.GradationScrollView;

/**
 * 透明渐变
 */
public class MainActivity extends AppCompatActivity implements GradationScrollView.ScrollViewListener{
    private GradationScrollView scrollView;

    private ListView listView;

    private LinearLayout ll;//ToolBar背景
    private View line;//分隔线（如果不需要刻意去掉）
    private TextView textView;
    private int height;
    private ImageView ivBanner;
    private LinearLayout llOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        llOffset = (LinearLayout) findViewById(R.id.ll_offset);
        //透明状态栏
        StatusBarUtil.setTranslucentForImageView(this,llOffset);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) llOffset.getLayoutParams();
        params1.setMargins(0,-StatusBarUtil.getStatusBarHeight(this)/4,0,0);
        llOffset.setLayoutParams(params1);

        scrollView = (GradationScrollView) findViewById(R.id.scrollview);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textview);
        ivBanner = (ImageView) findViewById(R.id.iv_banner);

        ll = (LinearLayout) findViewById(R.id.ll);
        line = findViewById(R.id.line);

        ivBanner.setFocusable(true);
        ivBanner.setFocusableInTouchMode(true);
        ivBanner.requestFocus();

        initListeners();
        initData();
    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = ivBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                height = ivBanner.getHeight();

                scrollView.setScrollViewListener(MainActivity.this);
            }
        });
    }

    private void initData() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.data));
        listView.setAdapter(adapter);
    }

    /**
     * 滑动监听
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(GradationScrollView scrollView, int x, int y,
                                int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= 0) {   //设置标题的背景颜色
            ll.setBackgroundColor(Color.argb((int) 0, 255,94,94));
        } else if (y > 0 && y <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) y / height;
            float alpha = (255 * scale);
            textView.setTextColor(Color.argb((int) alpha, 1,24,28));
            ll.setBackgroundColor(Color.argb((int) alpha, 255,94,94));
            line.setBackgroundColor(Color.argb((int) alpha, 0,0,0));//分割线颜色，不需要可以去掉
        } else {    //滑动到banner下面设置普通颜色
            ll.setBackgroundColor(Color.argb((int) 255, 255,94,94));
        }
    }
}
