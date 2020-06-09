package com.couponfoundry.Animation;

import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

public class Shake_animation {
    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 20, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }
}
