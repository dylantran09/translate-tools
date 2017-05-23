package com.example.translate.feature.main;

public interface MainContract {

    interface View {
        void switchToOverlayMode();

        void expandOverlayMode();

        void collapseOverlayMode();
    }

    interface Presenter {
        void onClick(android.view.View v);
    }

    interface Model {

    }
}
