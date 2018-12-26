package com.jusfoun.jusfouninquire.event;

import android.os.Bundle;

import com.jusfoun.baselibrary.event.IEvent;

/**
 * Author  JUSFOUN
 * CreateDate 2015/11/16.
 * Description
 */
public class InvestOrBranchEvent implements IEvent {

    private Bundle argument;
    public InvestOrBranchEvent(Bundle argument){
        this.argument=argument;
    }

    public Bundle getArgument(){
        return argument;
    }
}
