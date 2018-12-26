


package com.jusfoun.jusfouninquire.event;

import android.os.Bundle;

import com.jusfoun.baselibrary.event.IEvent;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/10.
 * Description
 */
public class CompanyWebEvent implements IEvent {

    private Bundle argument;
    public CompanyWebEvent(Bundle argument){
        this.argument=argument;
    }

    public Bundle getArgument(){
        return argument;
    }

}
