package com.example.opencvexample;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opencvexample.annotation.ContentView;
import com.example.opencvexample.annotation.InjectView;
import com.example.opencvexample.annotation.OnClick;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

@ContentView(R.layout.activity_third)
public class ThirdActivity extends BaseActivity{
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflater.from(this).setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                Button button = new Button(ThirdActivity.this);
                button.setText("this is a test");
                return button;
            }
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return null;
            }
        });
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        SampleAdapter adapter = new SampleAdapter();
    }

    @Override
    protected BaseViewModel getViewModel() {
        return null;
    }

    @Override
    int getLayoutId() {
        return 0;
    }

    @OnClick(R.id.fab)
    public void onClick(View view) {
        getImage();
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private  void getImage() {
//        String url = "";
//        Observable obserDownload=Observable.just(url)
//                .map((url)->{getZipFileFromRemote(url)});
//        Observable obserLocal=Observable.just(url)
//                .map((url)->{getZipFileFromLocal(url)});
//        Observable obserGift=Observable.concat(obserLocal,obserDownload)
//                .takeUnitl((file)->{file!=null});
//        obserGift.subscribeOn(Schedulers.io()).flatMap((file)->{readBitmapsFromZipFile(file)})
//                .subscribe((bitmap)->{showBitmap(bitmap)})

    }

}
