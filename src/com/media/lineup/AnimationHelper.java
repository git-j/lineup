package com.media.lineup;
import android.view.animation.*;
public class AnimationHelper{
//for the previous movement
	public static Animation inFromRightAnimation() {

    	Animation inFromRight = new TranslateAnimation(
    	Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
    	Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
    	);
    	inFromRight.setDuration(350);
    	inFromRight.setInterpolator(new AccelerateInterpolator());
    	return inFromRight;
    	}
    public static Animation outToLeftAnimation() {
    	Animation outtoLeft = new TranslateAnimation(
    	 Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
    	 Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
    	);
    	outtoLeft.setDuration(350);
    	outtoLeft.setInterpolator(new AccelerateInterpolator());
    	return outtoLeft;
    	}    
    // for the next movement
    public static Animation inFromLeftAnimation() {
    	Animation inFromLeft = new TranslateAnimation(
    	Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
    	Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
    	);
    	inFromLeft.setDuration(350);
    	inFromLeft.setInterpolator(new AccelerateInterpolator());
    	return inFromLeft;
    	}
    public static Animation outToRightAnimation() {
    	Animation outtoRight = new TranslateAnimation(
    	 Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
    	 Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
    	);
    	outtoRight.setDuration(350);
    	outtoRight.setInterpolator(new AccelerateInterpolator());
    	return outtoRight;
    	}
}
