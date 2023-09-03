package com.samaya.myspendings.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.samaya.myspendings.fragments.ReportFragment;

public class ReportFragmentAdapter  extends FragmentStateAdapter {
    public ReportFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return ReportFragment.newInstance(ReportFragment.FRAGMENT_REPORT_TYPE_ALL_SPENDINGS);
            case 1:
                return ReportFragment.newInstance(ReportFragment.FRAGMENT_REPORT_TYPE_MONTHLY);

            case 2:
                return ReportFragment.newInstance(ReportFragment.FRAGMENT_REPORT_TYPE_YEARlY);
            case 3:
                return ReportFragment.newInstance(ReportFragment.FRAGMENT_REPORT_TYPE_DATERANGE);

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return ReportFragment.FRAGMENT_REPORT_TYPE_COUNT;
    }
}
