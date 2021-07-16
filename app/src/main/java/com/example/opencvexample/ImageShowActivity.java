package com.example.opencvexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class ImageShowActivity extends AppCompatActivity {

    LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        container = findViewById(R.id.container);
        ArrayList<String> strings = new ArrayList<>();
        for(int j=0;j<50;j++){
            strings.add("the content is "+j);
        }
        List<List<String>> stringList = new ArrayList<>();
        for(int i=0;i<3;i++){
            stringList.add(strings);
        }
            MyViewPager myViewPager = new MyViewPager(this);
            myViewPager.setAdapter(new MyPageAdapter(this,stringList));
            container.addView(myViewPager);
//        }
    }

    class  MyPageAdapter extends PagerAdapter{
        Context context;
        List<List<String>> strings;
        MyPageAdapter(Context context, List<List<String>> strings){
            this.context = context;
            this.strings = strings;

        }
        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return (view == object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            MyListView myListView = new MyListView(context);
            ArrayAdapter adapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_list_item_activated_1,strings.get(position));
            myListView.setAdapter(adapter);
            container.addView(myListView);
            return myListView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
//            super.destroyItem(container, position, object);
        }
    }
}
