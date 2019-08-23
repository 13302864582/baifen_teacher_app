package com.ucuxin.ucuxin.tec.view.dialog;

import com.ucuxin.ucuxin.tec.view.dialog.effects.BaseEffects;
import com.ucuxin.ucuxin.tec.view.dialog.effects.FadeIn;
import com.ucuxin.ucuxin.tec.view.dialog.effects.Fall;
import com.ucuxin.ucuxin.tec.view.dialog.effects.FlipH;
import com.ucuxin.ucuxin.tec.view.dialog.effects.FlipV;
import com.ucuxin.ucuxin.tec.view.dialog.effects.NewsPaper;
import com.ucuxin.ucuxin.tec.view.dialog.effects.RotateBottom;
import com.ucuxin.ucuxin.tec.view.dialog.effects.RotateLeft;
import com.ucuxin.ucuxin.tec.view.dialog.effects.Shake;
import com.ucuxin.ucuxin.tec.view.dialog.effects.SideFall;
import com.ucuxin.ucuxin.tec.view.dialog.effects.SlideBottom;
import com.ucuxin.ucuxin.tec.view.dialog.effects.SlideLeft;
import com.ucuxin.ucuxin.tec.view.dialog.effects.SlideRight;
import com.ucuxin.ucuxin.tec.view.dialog.effects.SlideTop;
import com.ucuxin.ucuxin.tec.view.dialog.effects.Slit;

public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    SlideBottom(SlideBottom.class),
    Slideright(SlideRight.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class effectsClazz;

    Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
