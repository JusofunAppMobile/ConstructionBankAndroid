package com.jusfoun.baselibrary.Util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtil {

	// 隐藏键盘
	public static void hideSoftKeyboard(Context context,View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	// 隐藏键盘
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 2);
		}
	}

	// 显示键盘
	public static void showSoftKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getCurrentFocus() != null) {
			imm.showSoftInput(activity.getCurrentFocus(), 1);
		}
	}

	// 显示键盘
	public static void showSoftKeyboard(View view, Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		view.requestFocus();
		imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	// 显示键盘
	public static void showSoftKeyboard(EditText editText, Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
	}

	public static boolean openSoftKeyboard(View view, Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.hideSoftInputFromWindow(view.getWindowToken(), 0)) {
			return true;
		} else {
			return false;
		}
	}
}
