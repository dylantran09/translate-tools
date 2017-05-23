package com.example.translate.feature.main;

public class MainModel implements MainContract.Model {
    private MainContract.Presenter mPresenter;

    public MainModel(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
