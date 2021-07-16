package com.example.opencvexample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<D extends ViewDataBinding,M extends BaseViewModel> extends AppCompatActivity {

    public Handler mainHandler = new Handler(Looper.getMainLooper());

    private D dataBinding;
    private M viewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this,getLayoutId());
        viewModel = getViewModel();
        if(viewModel != null){
            getLifecycle().addObserver(viewModel);
            bindViewModel();
        }

        InjectManage.inject(this);
    }

    private void bindViewModel() {
    }

    protected abstract M getViewModel();

    abstract int getLayoutId();
}
