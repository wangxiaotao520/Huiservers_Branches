package com.huacheng.libraryservice.utils;

import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Description:
 * Author: badge
 * Create: 2018/7/10 15:37
 */
public class RotateUtils {
    private RotateUtils() {
    }

    /**
     * 根据当前的状态来旋转箭头。
     */
    @SuppressWarnings("all")
    public static void rotateArrow(ImageView arrow, boolean flag) {
        float pivotX = arrow.getWidth() / 2f;
        float pivotY = arrow.getHeight() / 2f;
        float fromDegrees = 0f;
        float toDegrees = 0f;
        // flag为true则向上
        if (flag) {
            fromDegrees = 180f;
            toDegrees = 0f;//360f
        } else {
            fromDegrees = 0f;
            toDegrees = 180f;
        }
//旋转动画效果   参数值 旋转的开始角度  旋转的结束角度  pivotX x轴伸缩值
        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees,
                pivotX, pivotY);
//该方法用于设置动画的持续时间，以毫秒为单位
        animation.setDuration(100);
//设置重复次数
//animation.setRepeatCount(int repeatCount);
//动画终止时停留在最后一帧
        animation.setFillAfter(true);
//启动动画
        arrow.startAnimation(animation);
    }
}
