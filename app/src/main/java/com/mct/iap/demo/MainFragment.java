package com.mct.iap.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mct.iap.banner.IapBannerBuilder;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.banner_countdown).setOnClickListener(v -> showBannerCountdown());
    }

    private void showBannerCountdown() {
        new IapBannerBuilder(requireContext(), R.layout.iap_banner_countdown)
                .setCountDownTime(3 * 60 * 60 * 1000)
                .bindTime(R.id.tv_hour).hour().bold().and()
                .bindTime(R.id.tv_minute).minute().italic().and()
                .bindTime(R.id.tv_second).second().strikeThru().and()
                .bindTime(R.id.tv_millisecond).millisecond().underline().highlight(Color.RED).and()
                .show(false);
    }

}
