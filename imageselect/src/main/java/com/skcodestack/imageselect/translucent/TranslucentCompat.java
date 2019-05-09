package com.skcodestack.imageselect.translucent;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2017/12/5
 * Version  1.0
 * Description:沉浸式效果工具栏
 */

public class TranslucentCompat {

//    /**
//     * 初始化，在setcontentview前调用--兼容4.4-5.0
//     *
//     * @param activity
//     */
//    public static void initStatusBar(Activity activity) {
//        //判断版本,如果[4.4,5.0)就设置状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        }
//    }
//
//    /**
//     * 初始化，在setcontentview前调用--兼容4.4-5.0
//     *
//     * @param activity
//     */
//    public static void initNavigationBar(Activity activity) {
//        //判断版本,如果[4.4,5.0)就设置导航栏为透明
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            //设置虚拟导航栏为透明
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        }
//    }

    /**
     * 设置状态栏颜色-----toolbar 设置paddingTop 的方式
     * ** 可以让背景入侵到状态栏
     *
     * @param activity
     * @param view                背景入侵到状态栏，toolbar位置矫正
     * @param isFitsSystemWindows 为true,那么就不能让背景入侵到状态栏
     * @param statusBarColor
     */
    public static void setStatusBarColor(Activity activity, View view, boolean isFitsSystemWindows, @ColorInt int statusBarColor) {

        if (view == null) {
            setStatusBarColor(activity, isFitsSystemWindows, statusBarColor);
            return;
        } else {
            //如果有view，那么通过给view添加padding来设置状态栏的显示背景
            isFitsSystemWindows = false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //SDK >= 5.0
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (contentView.getChildCount() <= 0) {
                throw new NullPointerException("please  invoke  after  setContentView");
            }

            ViewGroup customContentView = (ViewGroup) contentView.getChildAt(0);
            customContentView.setFitsSystemWindows(isFitsSystemWindows);

            if (view instanceof Toolbar) {
                Toolbar toolbar = (Toolbar) view;
                //1.先设置toolbar的高度
                int statusBarHeight = TranslucentUtil.getStatusBarHeight(activity);
                ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                params.height += statusBarHeight;
                toolbar.setLayoutParams(params);
                //2.设置paddingTop，以达到状态栏不遮挡toolbar的内容。
                toolbar.setPadding(
                        toolbar.getPaddingLeft(),
                        toolbar.getPaddingTop() + statusBarHeight,
                        toolbar.getPaddingRight(),
                        toolbar.getPaddingBottom());
                //设置顶部的颜色
                toolbar.setBackgroundColor(statusBarColor);
            } else {
                int statusBarHeight = TranslucentUtil.getStatusBarHeight(activity);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height += statusBarHeight;
                view.setLayoutParams(params);
                //2.设置paddingTop，以达到状态栏不遮挡toolbar的内容。
                view.setPadding(
                        view.getPaddingLeft(),
                        view.getPaddingTop() + statusBarHeight,
                        view.getPaddingRight(),
                        view.getPaddingBottom());
                //设置顶部的颜色
                view.setBackgroundColor(statusBarColor);
            }
            activity.getWindow().setStatusBarColor(statusBarColor);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
					//window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    //WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 5.0 > SDK >= 4.4
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (contentView.getChildCount() <= 0) {
                throw new NullPointerException("please  invoke  after  setContentView");
            }
            ViewGroup customContentView = (ViewGroup) contentView.getChildAt(0);
            customContentView.setFitsSystemWindows(isFitsSystemWindows);

            if (view == null) {
                throw new NullPointerException("view is null pinter");
            }
            if (view instanceof Toolbar) {
                Toolbar toolbar = (Toolbar) view;
                //1.先设置toolbar的高度
                int statusBarHeight = TranslucentUtil.getStatusBarHeight(activity);
                ViewGroup.LayoutParams params = toolbar.getLayoutParams();
                params.height += statusBarHeight;
                toolbar.setLayoutParams(params);
                //2.设置paddingTop，以达到状态栏不遮挡toolbar的内容。
                toolbar.setPadding(
                        toolbar.getPaddingLeft(),
                        toolbar.getPaddingTop() + statusBarHeight,
                        toolbar.getPaddingRight(),
                        toolbar.getPaddingBottom());
                //设置顶部的颜色
                toolbar.setBackgroundColor(statusBarColor);
            } else {
                int statusBarHeight = TranslucentUtil.getStatusBarHeight(activity);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height += statusBarHeight;
                view.setLayoutParams(params);
                //2.设置paddingTop，以达到状态栏不遮挡toolbar的内容。
                view.setPadding(
                        view.getPaddingLeft(),
                        view.getPaddingTop() + statusBarHeight,
                        view.getPaddingRight(),
                        view.getPaddingBottom());
                //设置顶部的颜色
                view.setBackgroundColor(statusBarColor);
            }
        } else {
            //SDK < 4.4

        }
    }

    /**
     * 设置状态栏颜色----添加view的方式
     *
     * @param activity
     * @param statusBarColor
     */
    public static void setStatusBarColor(Activity activity, boolean isFitsSystemWindows, @ColorInt int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //SDK >= 5.0

            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (contentView.getChildCount() <= 0) {
                throw new NullPointerException("please  invoke  after  setContentView");
            }
            ViewGroup customContentView = (ViewGroup) contentView.getChildAt(0);
            customContentView.setFitsSystemWindows(isFitsSystemWindows);

            activity.getWindow().setStatusBarColor(statusBarColor);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 5.0 > SDK >= 4.4
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (contentView.getChildCount() <= 0) {
                throw new NullPointerException("please  invoke  after  setContentView");
            }
            ViewGroup customContentView = (ViewGroup) contentView.getChildAt(0);
//            if (!customContentView.getFitsSystemWindows()) {
//                customContentView.setFitsSystemWindows(true);
//            }
            customContentView.setFitsSystemWindows(isFitsSystemWindows);

            View mStatusBarTintView = new View(activity);
            android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TranslucentUtil.getStatusBarHeight(activity));
            params.gravity = Gravity.TOP;
            //如果横屏，
//	        if(this.mNavBarAvailable && !this.mConfig.isNavigationAtBottom()) {
//	            params.rightMargin = this.mConfig.getNavigationBarWidth();
//	        }

            mStatusBarTintView.setLayoutParams(params);
            mStatusBarTintView.setBackgroundColor(statusBarColor);
            mStatusBarTintView.setVisibility(View.VISIBLE);
            ((ViewGroup) activity.getWindow().getDecorView()).addView(mStatusBarTintView);


        } else {
            //SDK < 4.4

        }
    }


    /**
     * 设置导航栏颜色--------自定义
     *
     * @param activity
     * @param bottomNavigationBar
     * @param navigationBarColor
     */
    public static void setNavigationBarColor(Activity activity, View bottomNavigationBar, boolean isFitsSystemWindows, int navigationBarColor) {

        if (bottomNavigationBar == null) {
            setNavigationBarColor(activity, isFitsSystemWindows, navigationBarColor);
            return;
        } else {
            isFitsSystemWindows = false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!isFitsSystemWindows) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                // 5.0 > SDK >= 4.4
                if (bottomNavigationBar != null) {
                    //解决低版本4.4+的虚拟导航栏的
                    if (TranslucentUtil.hasNavigationBarShow(activity.getWindowManager())) {
                        ViewGroup.LayoutParams p = bottomNavigationBar.getLayoutParams();
                        int navBarHeight = TranslucentUtil.getNavBarHeight(activity);
                        p.height += navBarHeight;
                        bottomNavigationBar.setLayoutParams(p);
                        //2.设置paddingTop，以达到状态栏不遮挡toolbar的内容。
                        bottomNavigationBar.setPadding(
                                bottomNavigationBar.getPaddingLeft(),
                                bottomNavigationBar.getPaddingTop(),
                                bottomNavigationBar.getPaddingRight(),
                                bottomNavigationBar.getPaddingBottom() + navBarHeight);
                        //设置底部导航栏的颜色
                        bottomNavigationBar.setBackgroundColor(navigationBarColor);
                    }
                }
            }
            //SDK >= 5.0
            activity.getWindow().setNavigationBarColor(navigationBarColor);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (!isFitsSystemWindows) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

                // 5.0 > SDK >= 4.4
                if (bottomNavigationBar != null) {
                    //解决低版本4.4+的虚拟导航栏的
                    if (TranslucentUtil.hasNavigationBarShow(activity.getWindowManager())) {
                        ViewGroup.LayoutParams p = bottomNavigationBar.getLayoutParams();
                        int navBarHeight = TranslucentUtil.getNavBarHeight(activity);
                        p.height += navBarHeight;
                        bottomNavigationBar.setLayoutParams(p);
                        //2.设置paddingTop，以达到状态栏不遮挡toolbar的内容。
                        bottomNavigationBar.setPadding(
                                bottomNavigationBar.getPaddingLeft(),
                                bottomNavigationBar.getPaddingTop(),
                                bottomNavigationBar.getPaddingRight(),
                                bottomNavigationBar.getPaddingBottom() + navBarHeight);
                        //设置底部导航栏的颜色
                        bottomNavigationBar.setBackgroundColor(navigationBarColor);
                    }
                }
            }
        } else {
            //SDK < 4.4
        }

    }


    /**
     * 设置导航栏颜色-----添加view的方式
     *
     * @param activity
     * @param navigationBarColor
     */
    public static void setNavigationBarColor(Activity activity, boolean isFitsSystemWindows, int navigationBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!isFitsSystemWindows) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            //SDK >= 5.0
            activity.getWindow().setNavigationBarColor(navigationBarColor);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (!isFitsSystemWindows) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            // 5.0 > SDK >= 4.4
            View bottomNavigationBar = new View(activity);
            if (bottomNavigationBar != null) {
                //解决低版本4.4+的虚拟导航栏的
                if (TranslucentUtil.hasNavigationBarShow(activity.getWindowManager())) {
                    android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TranslucentUtil.getNavBarHeight(activity));
                    params.gravity = Gravity.BOTTOM;
                    //如果横屏，
//                    if(this.mNavBarAvailable && !this.mConfig.isNavigationAtBottom()) {
//        	            params.rightMargin = this.mConfig.getNavigationBarWidth();
//        	        }
                    bottomNavigationBar.setLayoutParams(params);
                    //设置底部导航栏的颜色
                    bottomNavigationBar.setBackgroundColor(navigationBarColor);
                    bottomNavigationBar.setVisibility(View.VISIBLE);
                    ((ViewGroup) activity.getWindow().getDecorView()).addView(bottomNavigationBar);
                }
            }
        } else {
            //SDK < 4.4
        }
    }
	
	
	
	/**
     * 设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int statusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().
                        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    public static void statusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    public static void statusBarDarkMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

}
