package com.waste.treatment.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.waste.treatment.R;
import com.waste.treatment.adapter.FragmentIndexAdapter;

import com.waste.treatment.databinding.ActivityTestFramgeBinding;
import com.waste.treatment.fragment.HomeFragment;
import com.waste.treatment.fragment.MeFargment;
import com.waste.treatment.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class TestFramgeActivity extends AppCompatActivity {
    private ActivityTestFramgeBinding mBinding;


    private List<Fragment> mFragments;

    private FragmentIndexAdapter mFragmentIndexAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.makeStatusBarTransparent(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_test_framge);
        initData();
        initEvent();
    }

    private void initEvent() {
        mBinding.indexBottomBarHome.setOnClickListener(new TabOnClickListener(0));
        mBinding.indexBottomBarDynamicState.setOnClickListener(new TabOnClickListener(1));
        mBinding.indexBottomBarScan.setOnClickListener(new TabOnClickListener(2));
    }

    private void initIndexFragmentAdapter() {
        mFragmentIndexAdapter = new FragmentIndexAdapter(this.getSupportFragmentManager(), mFragments);
        mBinding.indexVpFragmentListTop.setAdapter(mFragmentIndexAdapter);
        mBinding.indexBottomBarHome.setSelected(true);
        mBinding.indexVpFragmentListTop.setCurrentItem(0);
        mBinding.indexVpFragmentListTop.setOffscreenPageLimit(3);
        mBinding.indexVpFragmentListTop.addOnPageChangeListener(new TabOnPageChangeListener());
    }

    private void initData() {
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new HomeFragment());
        mFragments.add(new MeFargment());
        initIndexFragmentAdapter();
    }


    /**
     * Bottom_Bar的点击事件
     */
    public class TabOnClickListener implements View.OnClickListener {

        private int index = 0;

        public TabOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            if (index == 2) {
                // 跳转到Scan界面
              //  resetTextView();
                Toast.makeText(TestFramgeActivity.this, "点击了扫描按钮", Toast.LENGTH_SHORT).show();
            } else {
                //选择某一页
                mBinding.indexVpFragmentListTop.setCurrentItem(index, false);
            }
        }

    }

    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //当滑动状态改变时调用
        public void onPageScrollStateChanged(int state) {
        }

        //当前页面被滑动时调用
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        //当新的页面被选中时调用
        public void onPageSelected(int position) {
            resetTextView();
            switch (position) {
                case 0:
                    mBinding.indexBottomBarHome.setSelected(true);
                    break;
                case 1:
                    mBinding.indexBottomBarDynamicState.setSelected(true);
                    break;
                /*case 2:
                    index_bottom_bar_integral.setSelected(true);
                    break;
                case 3:
                    index_bottom_bar_me.setSelected(true);
                    break;*/
            }
        }
    }

    /**
     * 重置所有TextView的字体颜色
     */
    private void resetTextView() {
        mBinding.indexBottomBarHome.setSelected(false);
        mBinding.indexBottomBarDynamicState.setSelected(false);
//        index_bottom_bar_integral.setSelected(false);
        //  index_bottom_bar_me.setSelected(false);
    }

}
