package com.koylubaevnt.jpraytimes.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import com.koylubaevnt.jpraytimes.preferences.AppConfig;


public class I18n {

	public static ResourceBundle resourceBundle;
	public static Locale locale;
	
	static {
		locale = new Locale(AppConfig.getLocaleLanguage(), AppConfig.getLocaleCountry(), AppConfig.getLocaleVariant());
		resourceBundle = ResourceBundle.getBundle("com.koylubaevnt.jpraytimes.i18n.application_l18n", locale);
	}
	
	public static String getText(String key) {
		return resourceBundle.getString(key);
	}
}
