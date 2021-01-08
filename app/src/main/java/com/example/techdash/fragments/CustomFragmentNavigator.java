package com.example.techdash.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

import com.example.techdash.R;

import java.util.Stack;

@Navigator.Name("custom_fragment_navigator")
public class CustomFragmentNavigator extends FragmentNavigator {
    private static final String TAG = CustomFragmentNavigator.class.getSimpleName();
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mContainerId;

    private Stack<Destination> customBackStack;

    public CustomFragmentNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        mContext = context;
        mFragmentManager = manager;
        mContainerId = containerId;
        customBackStack = new Stack<>();
    }

    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();

        String tag = String.valueOf(destination.getId());

        if (currentFragment != null) {
            transaction.detach(currentFragment);
        }

        Fragment nextFragment = mFragmentManager.findFragmentByTag(tag);
        if (nextFragment == null) {
            String className = destination.getClassName();
            nextFragment = mFragmentManager.getFragmentFactory().instantiate(mContext.getClassLoader(), className);
            transaction.add(mContainerId, nextFragment, tag);
        } else {
            transaction.attach(nextFragment);
        }

        if (!isInsideBackStack(destination)) {
            customBackStack.push(destination);
        }

        transaction.setPrimaryNavigationFragment(nextFragment);
        transaction.setReorderingAllowed(true);
        transaction.commitNow();

        return destination;
    }

    boolean isInsideBackStack(Destination destination) {
        for (Destination i : customBackStack) {
            if (i == destination) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean popBackStack() {
        boolean popable = false;
        if (customBackStack.size() > 1) {
            popable = true;
        }
        while (customBackStack.size() > 1) customBackStack.pop();
        if (popable) {
            navigate(customBackStack.peek(), null, null, null);
            return true;
        } else {
            return false;
        }
    }
}
