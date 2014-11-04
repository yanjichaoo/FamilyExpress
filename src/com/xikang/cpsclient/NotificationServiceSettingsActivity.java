package com.xikang.cpsclient;

import com.xikang.family.activity.R;
import com.xikang.family.common.Constants;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

/**
 * Activity for displaying the notification setting view.
 */
public class NotificationServiceSettingsActivity extends PreferenceActivity {
	private ServiceManager serviceManager = new ServiceManager(this);

	public NotificationServiceSettingsActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPreferenceScreen(createPreferenceHierarchy());

		CheckBoxPreference notifyPref = (CheckBoxPreference) getPreferenceManager()
				.findPreference(Constants.SETTINGS_NOTIFICATION_ENABLED);
		if (notifyPref.isChecked()) {
			notifyPref.setTitle(R.string.notifications_enabled);
			serviceManager.startService();
		} else {
			notifyPref.setTitle(R.string.notifications_disabled);
			serviceManager.stopService();
		}
	}

	private PreferenceScreen createPreferenceHierarchy() {
		PreferenceManager preferenceManager = getPreferenceManager();
		preferenceManager
				.setSharedPreferencesName(Constants.SHARED_PREFERENCE_NAME);
		preferenceManager.setSharedPreferencesMode(Context.MODE_PRIVATE);

		PreferenceScreen root = preferenceManager.createPreferenceScreen(this);

		CheckBoxPreference notifyPref = new CheckBoxPreference(this);
		notifyPref.setKey(Constants.SETTINGS_NOTIFICATION_ENABLED);
		notifyPref.setTitle(R.string.notifications_enabled);
		notifyPref.setSummaryOn(R.string.notifications_enabled_summary);
		notifyPref.setSummaryOff(R.string.notifications_disabled_summary);
		notifyPref.setDefaultValue(Boolean.TRUE);
		notifyPref
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						boolean checked = Boolean.valueOf(newValue.toString());
						if (checked) {
							preference.setTitle(R.string.notifications_enabled);
							serviceManager.startService();
						} else {
							preference
									.setTitle(R.string.notifications_disabled);
							serviceManager.stopService();
						}
						return true;
					}
				});

		root.addPreference(notifyPref);
		return root;
	}

}
