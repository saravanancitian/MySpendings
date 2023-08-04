package com.samaya.myspendings;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SpendingsFragmentAdapter extends FragmentStateAdapter {
    public SpendingsFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return SpendingsFragment.newInstance(SpendingsFragment.FRAGMENT_TYPE_ALL_SPENDINGS);
            case 1:
                return SpendingsFragment.newInstance(SpendingsFragment.FRAGMENT_TYPE_DAILY);

            case 2:
                return SpendingsFragment.newInstance(SpendingsFragment.FRAGMENT_TYPE_MONTHLY);

            case 3:
                return SpendingsFragment.newInstance(SpendingsFragment.FRAGMENT_TYPE_YEARlY);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return SpendingsFragment.TOTAL_FRAGMENT_COUNT;
    }
}
