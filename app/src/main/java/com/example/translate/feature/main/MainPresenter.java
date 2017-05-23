package com.example.translate.feature.main;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private MainContract.Model mModel;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mModel = new MainModel(this);
    }

    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()) {

        }
    }
}
