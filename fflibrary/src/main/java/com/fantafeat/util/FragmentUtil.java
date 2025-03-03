package com.fantafeat.util;


import android.util.Log;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.fantafeat.R;

import java.util.Stack;


public class FragmentUtil {

    public static Stack<Fragment> fragmentStack = new Stack<Fragment>();
    private int containerId;
    private Fragment fragment;
    private String currentFragmentTag;
    private String fragmentTag;
    private FragmentActivity activity;

    public void replaceFragment(FragmentActivity activity, int containerId, Fragment fragment, String fragmentTag, ANIMATION_TYPE animationType) {
        this.containerId = containerId;
        this.fragment = fragment;
        this.fragmentTag = fragmentTag;
        this.activity = activity;

        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

        if (fragment != null) {

            switch (animationType) {
                case SLIDE_UP_TO_DOWN:
                    ft.setCustomAnimations(R.anim.enter_top, R.anim.exit_bottom);
                    break;
                case SLIDE_DOWN_TO_UP:

                    ft.setCustomAnimations(R.anim.enter_bottom, R.anim.exit_top);
                    break;
                case SLIDE_LEFT_TO_RIGHT:
                    ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
                    ft.commit();
                    break;
                case SLIDE_RIGHT_TO_LEFT:
                    ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                    break;
                case FLIP_LEFT_TO_RIGHT:
                    ft.setCustomAnimations(R.anim.enter_flip_left, R.anim.exit_flip_right);
                    break;
                case FLIP_RIGHT_TO_LEFT:
                    ft.setCustomAnimations(R.anim.enter_flip_right, R.anim.exit_flip_left);
                    break;
            }
            fragmentStack.clear();
            ft.replace(containerId, fragment, fragmentTag);
            fragmentStack.push(fragment);
            ft.commit();
        } else {
            ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
            ft.replace(containerId, fragment, fragmentTag);
            fragmentStack.push(fragment);
            ft.commit();
        }
    }

    public void addFragment(FragmentActivity activity, int containerId, Fragment fragment, String fragmentTag, ANIMATION_TYPE animationType) {
        this.containerId = containerId;
        this.fragment = fragment;
        this.fragmentTag = fragmentTag;
        this.activity = activity;

        if (!fragmentStack.empty() && fragmentStack.peek() == fragment)
            return;

        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

        if (fragment != null) {
            switch (animationType) {
                case SLIDE_UP_TO_DOWN:
                    ft.setCustomAnimations(R.anim.enter_top, R.anim.exit_bottom);
                    break;
                case SLIDE_DOWN_TO_UP:
                    ft.setCustomAnimations(R.anim.enter_bottom, R.anim.exit_top);
                    break;
                case SLIDE_LEFT_TO_RIGHT:
                    ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
                    break;
                case SLIDE_RIGHT_TO_LEFT:
                    ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                    break;
                case FLIP_LEFT_TO_RIGHT:
                    ft.setCustomAnimations(R.anim.enter_flip_left, R.anim.exit_flip_right);
                    break;
                case FLIP_RIGHT_TO_LEFT:
                    ft.setCustomAnimations(R.anim.enter_flip_right, R.anim.exit_flip_left);
                    break;
                case CUSTOM:
                    AnimationUtils.loadAnimation(activity,R.anim.custom);
                    break;
                default:
                    fragmentStack.clear();
                    break;
            }
            ft.add(containerId, fragment, fragmentTag);
            fragmentStack.push(fragment);
            ft.commit();
        } else {
            ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
            ft.add(containerId, fragment, fragmentTag);
            fragmentStack.push(fragment);
            ft.commit();
        }
    }

    public void removeFragment(FragmentActivity activity, int containerId, Fragment fragment, ANIMATION_TYPE animationType) {
        this.fragment = fragment;
        this.activity = activity;
        this.containerId = containerId;

        if (fragmentStack.size() == 1) {
            return;
        }

        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

        if (fragment != null) {
//            if (!frag.getTag().equals("fragmentTag")) {
            switch (animationType) {
                case SLIDE_UP_TO_DOWN:
                    ft.setCustomAnimations(R.anim.enter_top, R.anim.exit_bottom);
                    break;
                case SLIDE_DOWN_TO_UP:
                    ft.setCustomAnimations(R.anim.enter_bottom, R.anim.exit_top);
                    break;
                case SLIDE_LEFT_TO_RIGHT:
                    ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
                    break;
                case SLIDE_RIGHT_TO_LEFT:
                    ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                    break;
                case FLIP_LEFT_TO_RIGHT:
                    ft.setCustomAnimations(R.anim.enter_flip_left, R.anim.exit_flip_right);
                    break;
                case FLIP_RIGHT_TO_LEFT:
                    ft.setCustomAnimations(R.anim.enter_flip_right, R.anim.exit_flip_left);
                    break;
            }
            ft.remove(fragmentStack.pop());
            fragmentStack.lastElement().onResume();
            ft.show(fragmentStack.lastElement());
            ft.commit();
        } else {
            ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
            ft.remove(fragmentStack.pop());
            fragmentStack.lastElement().onResume();
            ft.show(fragmentStack.lastElement());
            ft.commit();
            //this.activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_right, R.anim.exit_left).remove(this.fragment).commit();
        }
    }

    public void resumeFragment(FragmentActivity activity, int containerId, Fragment fragment, String fragmentTag, ANIMATION_TYPE animationType) {
        this.fragment = fragment;

        this.activity = activity;
        this.containerId = containerId;

        if (fragmentStack.size() == 1) {
            Log.e("TAG", "resumeFragment: size" );
            return;
        }

        if(!fragmentStack.contains(fragment)){
            Log.e("TAG", "resumeFragment: contains");
        }

        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

        if (fragment != null) {
            switch (animationType) {
                case SLIDE_UP_TO_DOWN:
                    ft.setCustomAnimations(R.anim.enter_top, R.anim.exit_bottom);
                    break;
                case SLIDE_DOWN_TO_UP:
                    ft.setCustomAnimations(R.anim.enter_bottom, R.anim.exit_top);
                    break;
                case SLIDE_LEFT_TO_RIGHT:
                    ft.setCustomAnimations(R.anim.enter_left, R.anim.exit_right);
                    break;
                case SLIDE_RIGHT_TO_LEFT:
                    ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
                    break;
                case FLIP_LEFT_TO_RIGHT:
                    ft.setCustomAnimations(R.anim.enter_flip_left, R.anim.exit_flip_right);
                    break;
                case FLIP_RIGHT_TO_LEFT:
                    ft.setCustomAnimations(R.anim.enter_flip_right, R.anim.exit_flip_left);
                    break;
                default:

                    break;
            }
            Log.e("LastElement", fragment.getTag() + "     " + String.valueOf(fragment));

            //ft.remove(fragmentStack.pop());

            Log.e("LastElement", String.valueOf(fragmentStack.peek()) + "     " + String.valueOf(fragment));

            while (!fragmentStack.empty() &&
                    fragmentStack.size() > 1 &&
                    !fragmentStack.peek().getTag().equals(fragmentTag)) {

                ft.remove(fragmentStack.pop());
            }


            fragmentStack.lastElement().onResume();
            ft.show(fragmentStack.lastElement());
            ft.commit();

        } else {
            ft.setCustomAnimations(R.anim.enter_right, R.anim.exit_left);
            ft.remove(fragmentStack.pop());
            fragmentStack.lastElement().onResume();
            ft.show(fragmentStack.lastElement());
            ft.commit();
        }
    }

    public enum ANIMATION_TYPE {
        SLIDE_UP_TO_DOWN, SLIDE_DOWN_TO_UP, SLIDE_LEFT_TO_RIGHT, SLIDE_RIGHT_TO_LEFT, FLIP_LEFT_TO_RIGHT, FLIP_RIGHT_TO_LEFT,CUSTOM, NONE
    }
}
