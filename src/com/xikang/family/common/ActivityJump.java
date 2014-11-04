/* ==================================================
 * 产品名: 亲情快递
 * 文件名: ActivityJump.java
 * --------------------------------------------------
 * 开发环境: JDK1.6
 * --------------------------------------------------
 * 修订履历    2012/07/07  1.00  初版发行
 * --------------------------------------------------
 * (C) Copyright XIKANG CORPORATION 2012 All Rights Reserved.
 */
package com.xikang.family.common;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import com.xikang.channel.familyexpress.rpc.thrift.family.UserBaseInfo;
import com.xikang.family.activity.AddModeByConfirmActivity;
import com.xikang.family.activity.AddModeByRegEditActivity;
import com.xikang.family.activity.AddModeByUserActivity;
import com.xikang.family.activity.AddModeByUserAndPwdActivity;
import com.xikang.family.activity.FamilyDetailActivity;
import com.xikang.family.activity.FamilyListActivity;
import com.xikang.family.activity.SettingActivity;
import com.xikang.family.activity.SettingHoldActivity;
import com.xikang.family.activity.SettingVersionActivity;

/**
 * 
 * ActivityGroup跳转
 * 
 * 
 * 
 * @author 闫继超
 * @version 1.00
 */

public class ActivityJump {

	public static void JumpAddModeByRegEditActivity(ActivityGroup ag,
			LinearLayout linearLayout) {
		linearLayout.removeAllViews();
		Intent mIntent = new Intent(ag.getApplicationContext(),
				AddModeByRegEditActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window mWindow = ag.getLocalActivityManager().startActivity(
				"AddModeByRegEditActivity", mIntent);
		View mView = mWindow.getDecorView();
		LayoutParams paramView = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		linearLayout.addView(mView, paramView);
	}

	public static void JumpAddModeByConfirmActivity(ActivityGroup ag,
			LinearLayout LinearLayout, Bundle bundle) {
		LinearLayout.removeAllViews();
		Intent mIntent = new Intent(ag.getApplicationContext(),
				AddModeByConfirmActivity.class);
		mIntent.putExtras(bundle);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window mWindow = ag.getLocalActivityManager().startActivity(
				"AddModeByConfirmActivity", mIntent);
		View mView = mWindow.getDecorView();
		LayoutParams paramView = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LinearLayout.addView(mView, paramView);
	}

	public static void JumpAddModeByUserActivity(ActivityGroup ag,
			LinearLayout LinearLayout) {
		LinearLayout.removeAllViews();
		Intent mIntent = new Intent(ag.getApplicationContext(),
				AddModeByUserActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window mWindow = ag.getLocalActivityManager().startActivity(
				"AddModeByUserActivity", mIntent);
		View mView = mWindow.getDecorView();
		LayoutParams paramView = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LinearLayout.addView(mView, paramView);
	}

	public static void JumpAddModeByUserAndPwdActivity(ActivityGroup ag,
			LinearLayout LinearLayout) {
		LinearLayout.removeAllViews();
		Intent mIntent = new Intent(ag.getApplicationContext(),
				AddModeByUserAndPwdActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window mWindow = ag.getLocalActivityManager().startActivity(
				"AddModeByUserAndPwdActivity", mIntent);
		View mView = mWindow.getDecorView();
		LayoutParams paramView = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LinearLayout.addView(mView, paramView);
	}

	public static void JumpFamilyListActivity(ActivityGroup ag,
			LinearLayout LinearLayout) {
		LinearLayout.removeAllViews();
		Intent mIntent = new Intent(ag.getApplicationContext(),
				FamilyListActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window mWindow = ag.getLocalActivityManager().startActivity(
				"FamilyListActivity", mIntent);
		View mView = mWindow.getDecorView();
		LayoutParams paramView = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LinearLayout.addView(mView, paramView);
	}

	public static void JumpFamilyDetailActivity(ActivityGroup ag,
			LinearLayout LinearLayout, UserBaseInfo user) {
		LinearLayout.removeAllViews();
		Intent mIntent = new Intent(ag.getApplicationContext(),
				FamilyDetailActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("userId", user.getUserId());
		bundle.putString("userName", user.getUserName());
		bundle.putString("nikeName", user.getNikeName());
		bundle.putString("relation", user.getRelation());
		bundle.putString("mobile", user.getMobile());
		bundle.putString("email", user.getEmail());
		bundle.putString("figureUrl", user.getFigureUrl());
		bundle.putInt("allowStatus", user.getAllowStatus().getValue());
		bundle.putString("userId", user.getUserId());

		mIntent.putExtras(bundle);

		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window mWindow = ag.getLocalActivityManager().startActivity(
				"FamilyDetailActivity", mIntent);
		View mView = mWindow.getDecorView();
		LayoutParams paramView = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LinearLayout.addView(mView, paramView);
	}

	public static void JumpSettingActivity(ActivityGroup ag,
			LinearLayout LinearLayout) {
		LinearLayout.removeAllViews();
		Intent mIntent = new Intent(ag.getApplicationContext(),
				SettingActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window mWindow = ag.getLocalActivityManager().startActivity(
				"SettingActivity", mIntent);
		View mView = mWindow.getDecorView();
		LayoutParams paramView = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LinearLayout.addView(mView, paramView);
	}

	public static void JumpSettingHoldActivity(ActivityGroup ag,
			LinearLayout LinearLayout) {
		LinearLayout.removeAllViews();
		Intent mIntent = new Intent(ag.getApplicationContext(),
				SettingHoldActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window mWindow = ag.getLocalActivityManager().startActivity(
				"SettingHoldActivity", mIntent);
		View mView = mWindow.getDecorView();
		LayoutParams paramView = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LinearLayout.addView(mView, paramView);
	}

	public static void JumpSettingVersionActivity(ActivityGroup ag,
			LinearLayout LinearLayout) {
		LinearLayout.removeAllViews();
		Intent mIntent = new Intent(ag.getApplicationContext(),
				SettingVersionActivity.class);
		mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window mWindow = ag.getLocalActivityManager().startActivity(
				"SettingVersionActivity", mIntent);
		View mView = mWindow.getDecorView();
		LayoutParams paramView = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LinearLayout.addView(mView, paramView);
	}

}