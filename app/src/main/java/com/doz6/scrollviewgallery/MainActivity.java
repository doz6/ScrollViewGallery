package com.doz6.scrollviewgallery;

import android.app.Activity;



import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity {
    private LinearLayout mLinear;//存放底部缩略图的线性布局
    private ImageSwitcher mSwitcher;//图片切换器
    List<Integer> imgIds;//存放所有图片ID的集合
    private ImageView[] imgViews;//显示图片的控件
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinear = (LinearLayout) findViewById(R.id.mLinear);
        mSwitcher = (ImageSwitcher) findViewById(R.id.mSwitcher);
        mSwitcher.setFactory(new ViewFactory() {//图片切换器的初始化
            public View makeView() {
                ImageView img = new ImageView(MainActivity.this);
                return img;
            }
        });
        imgIds=getImageIds();//获取所有需要显示的图片
        mSwitcher.setImageResource(imgIds.get(0));//默认显示第一张图片
        init();//执行初始化操作
    }
    public void init(){
        imgViews = new ImageView[imgIds.size()];//创建一个存放图片控件的数组
        //创建一个布局参数，指定控件大小和边距
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(60,80);
        layoutParams.setMargins(0, 0, 5, 0);//设置图片之间的边距
        //为每张图片创建一个ImageView控件，并对其进行简单设置
        for (int i = 0; i < imgViews.length; i++) {
            imgViews[i] = new ImageView(this);//创建ImageView控件
            imgViews[i].setId(imgIds.get(i));//为ImageView控件添加ID属性
            imgViews[i].setBackgroundResource(R.drawable.bg);//为ImageView控件设置背景边框
            imgViews[i].setImageResource(imgIds.get(i));//为ImageView控件设置图片
            imgViews[i].setLayoutParams(layoutParams);
            imgViews[i].setOnClickListener(new MyListener());
            if(i!=0){//默认第一张完全显示，其他图片半透明显示
                imgViews[i].setImageAlpha(100);
            }else{
                imgViews[i].setImageAlpha(255);
            }
            mLinear.addView(imgViews[i]);//将ImageView控件添加到现形布局中去
        }
    }
    private class MyListener implements OnClickListener {//底部图片单击事件监听器
        public void onClick(View v) {
            mSwitcher.setImageResource(v.getId());//改变上面显示的图片
            setAlpha(imgViews);//将所有图片设置为半透明状态
            ((ImageView) v).setImageAlpha(255);//将选中的图片设置为完全显示
        }
    }
    public void setAlpha(ImageView[] imageViews) {//设置所有图片的Alpha值为100，完全显示时，值为255。
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].setImageAlpha(100);
        }
    }

    public List<Integer> getImageIds(){//通过反射机制获取所以符合条件的图片ID
        List<Integer> imageIds=new ArrayList<Integer>();
        try {//获取R.drawable类中的所有成员变量
            Field[] drawableFields = R.drawable.class.getFields();
            //循环遍历这些成员变量，然后判断是否符合指定条件，如果符合条件则将其值添加到集合中去
            for (Field field : drawableFields) {
                if (field.getName().startsWith("x_")) {//获取以X_开始的图片
                    imageIds.add(field.getInt(R.drawable.class));//获取图片的id，并添加到集合中
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageIds;
    }

}
