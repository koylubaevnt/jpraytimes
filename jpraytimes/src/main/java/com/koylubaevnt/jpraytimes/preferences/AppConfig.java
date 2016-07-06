package com.koylubaevnt.jpraytimes.preferences;

import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.koylubaevnt.jpraytimes.services.Geocoder;
import com.koylubaevnt.jpraytimes.services.NetworkGeocoder;
import id.web.michsan.praytimes.Method;
import id.web.michsan.praytimes.Method.HighLatMethod;
import id.web.michsan.praytimes.Method.MidnightMethod;
import id.web.michsan.praytimes.PrayTimes.Time;

public class AppConfig {

	private static Preferences preferences;
	
	/*For internationalization*/
	private static String KEY_LOCALE_COUNTRY = "locale_country";
	private static String KEY_LOCALE_LANGUAGE = "locale_language";
	private static String KEY_LOCALE_VARIANT = "locale_variant";
	
	private static String KEY_DISPLAY_ALWAYS_ON_TOP = "display_always_on_top";	
	private static String KEY_DISPLAY_LAUNCH_ON_STARTUP = "display_launch_on_startup";
	
	private static String KEY_DISPLAY_FONT_NAME = "display_font_name";
	private static String KEY_DISPLAY_FONT_SIZE = "display_font_size";
	private static String KEY_DISPLAY_FONT_STYLE = "display_font_style";
	
	private static String KEY_DISPLAY_POSITION_X = "display_position_x";
	private static String KEY_DISPLAY_POSITION_Y = "display_position_y";
	
	private static String KEY_DISPLAY_FAJR = "display_fajr";
	private static String KEY_DISPLAY_SUNRISE = "display_sunrise";
	private static String KEY_DISPLAY_DHUHR = "display_dhuhr";
	private static String KEY_DISPLAY_ASR = "display_asr";
	private static String KEY_DISPLAY_SUNSET = "display_sunset";
	private static String KEY_DISPLAY_MAGHRIB = "display_maghrib";
	private static String KEY_DISPLAY_ISHA = "display_isha";
	private static String KEY_DISPLAY_OPACITY = "display_opacity";
	private static String KEY_DISPLAY_FORMAT_TIME_TYPE = "display_format_time";
	
	/*For calculation prayer times*/
	private static String KEY_LOCATION_LATITUDE = "method_location_latitude";
	private static String KEY_LOCATION_LONGITUDE = "method_location_longitude";
	private static String KEY_LOCATION_ELEVATION = "method_location_elevation";
	private static String KEY_CALCULATION_TIMEZONE_TYPE = "method_timezone_type";
	private static String KEY_CALCULATION_TIMEZONE_INDEX = "method_timezone_index";
	private static String KEY_CALCULATION_METHOD_TYPE = "method_type";
	private static String KEY_CALCULATION_METHOD_FAJR_VALUE = "method_fajr_value";
	private static String KEY_CALCULATION_METHOD_MAGHRIB_TYPE = "method_maghrib_type";
	private static String KEY_CALCULATION_METHOD_MAGHRIB_VALUE = "method_maghrib_value";
	private static String KEY_CALCULATION_METHOD_ISHA_TYPE = "method_isha_type";
	private static String KEY_CALCULATION_METHOD_ISHA_VALUE = "method_isha_value";
	private static String KEY_CALCULATION_METHOD_OTHER_DHUHR_VALUE = "method_dhuhr_value";
	private static String KEY_CALCULATION_METHOD_OTHER_ASR_TYPE = "method_asr_type";
	private static String KEY_CALCULATION_METHOD_OTHER_MIDNIGHT_METHOD_TYPE = "method_midnight_type";
	private static String KEY_CALCULATION_METHOD_OTHER_HL_TYPE = "method_hl_type";
	
	private static String KEY_ALERTS_ALARM_FAJR_BEFORE = "alerts_alarm_fajr_before";
	private static String KEY_ALERTS_ALARM_FAJR_AFTER = "alerts_alarm_fajr_after";
	private static String KEY_ALERTS_ALARM_SUNRISE_BEFORE = "alerts_alarm_sunrise_before";
	private static String KEY_ALERTS_ALARM_SUNRISE_AFTER = "alerts_alarm_sunrise_after";
	private static String KEY_ALERTS_ALARM_DHUHR_BEFORE = "alerts_alarm_dhuhr_before";
	private static String KEY_ALERTS_ALARM_DHUHR_AFTER = "alerts_alarm_dhuhr_after";
	private static String KEY_ALERTS_ALARM_ASR_BEFORE = "alerts_alarm_asr_before";
	private static String KEY_ALERTS_ALARM_ASR_AFTER = "alerts_alarm_asr_after";
	private static String KEY_ALERTS_ALARM_SUNSET_BEFORE = "alerts_alarm_sunset_before";
	private static String KEY_ALERTS_ALARM_SUNSET_AFTER = "alerts_alarm_sunset_after";
	private static String KEY_ALERTS_ALARM_MAGHRIB_BEFORE = "alerts_alarm_maghrib_before";
	private static String KEY_ALERTS_ALARM_MAGHRIB_AFTER = "alerts_alarm_maghrib_after";
	private static String KEY_ALERTS_ALARM_ISHA_BEFORE = "alerts_alarm_isha_before";
	private static String KEY_ALERTS_ALARM_ISHA_AFTER = "alerts_alarm_isha_after";
	private static String KEY_ALERTS_ALARM_AUDIO = "alerts_alarm_audio";
	private static String KEY_ALERTS_VIZUAL_EFFECT_SHOW_BALON_TIPS = "alerts_visual_effect_show_balon_tips";
	private static String KEY_ALERTS_VIZUAL_EFFECT_SHOW_MAIN_SCREEN = "alerts_visual_effect_show_main_screen";
	private static String KEY_ALERTS_VIZUAL_EFFECT_FLASH_ICON_TRAY = "alerts_visual_effect_flash_icon_tray";
	
	private static String KEY_SOUNDS_FAJR_AUDIO = "sounds_fajr_audio";
	private static String KEY_SOUNDS_DHUHR_AUDIO = "sounds_dhuhr_audio";
	private static String KEY_SOUNDS_ASR_AUDIO = "sounds_asr_audio";
	private static String KEY_SOUNDS_MAGHRIB_AUDIO = "sounds_maghrib_audio";
	private static String KEY_SOUNDS_ISHA_AUDIO = "sounds_isha_audio";
	private static String KEY_SOUNDS_STARTUP_AUDIO = "sounds_startup_audio";
	
	
	private static Geocoder geocoder; 
	private static Vector<Locale> locales = new Vector<Locale>();
	private static String[] alarmMinutes = { "0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60"};
	
	public static int AUTO = 0;
	public static int MANUAL = 1;
	public static int ANGLE = 0;
	public static int MINUTES = 1;
	public static int HIDE = 0;
	public static int SHOW = 1;
	public static int H24 = 0;
	public static int H12 = 1;
	public static int H12S = 2;
	public static List<String> dstList;
	private static String dstDefaultValue = "";
	private static int indexDstDefault;
	
	static {
		Pattern pattern = Pattern.compile("application_l18n_([a-zA-Z]*)\\_*([a-zA-Z]*).properties");
		Matcher matcher;
		List<String> fileList = new ArrayList<String>();
		List<String> includeResource = new ArrayList<String>();
		fileList = AppConfig.getListResourceFiles("/com/koylubaevnt/jpraytimes/i18n");
		for (String string : fileList) {
			matcher = pattern.matcher(string);
			if (matcher.matches()) {
				if (!matcher.group(2).isEmpty())
					includeResource.add(matcher.group(1) + "_" + matcher.group(2));
				else
					includeResource.add(matcher.group(1));
			}
		}
	
		for (Locale locale : Locale.getAvailableLocales()) {
			if(!locale.getLanguage().isEmpty())
				if (includeResource.contains(locale.getLanguage() + "_" + locale.getCountry())) {
					locales.addElement(locale);
				} else if (includeResource.contains(locale.getLanguage())) {
					locales.addElement(locale);
				}
		}
		
		Comparator<Locale> comparator = new Comparator<Locale>() {
	        
			public int compare(Locale o1, Locale o2) {
				int res = -1;
	            if (o1.getLanguage().compareTo(o2.getLanguage()) > 0
	            		|| (o1.getLanguage().compareTo(o2.getLanguage()) == 0
	            		&& o1.getDisplayCountry().compareTo(o2.getDisplayCountry()) > 0)) {
	            	res = 1;
	            } else if (o1.getLanguage().compareTo(o2.getLanguage()) == 0
            		&& o1.getDisplayCountry().compareTo(o2.getDisplayCountry()) == 0)
	            	res = 0;
	            
				return res;
	        }
	    };
		Collections.sort(locales, comparator);
		
		try {
			preferences = Preferences.userNodeForPackage(AppConfig.class);
		} catch (NullPointerException e) {
			
		} catch (SecurityException e) {
			
		}
		
		Set<String> allZones = ZoneId.getAvailableZoneIds();
		LocalDateTime dt = LocalDateTime.now();
		List<String> zoneList = new ArrayList<String>(allZones);
		dstList = new ArrayList<String>();
		for (String s : zoneList) {
		    ZoneId zone = ZoneId.of(s);
		    ZonedDateTime zdt = dt.atZone(zone);
		    ZoneOffset offset = zdt.getOffset();
		    if (!offset.toString().equals("Z") && !dstList.contains("GMT" + offset.toString())) {
		    	dstList.add("GMT"+ offset.toString());
		    }
		}
		dstList.add("GMT+00:00");
		Collections.sort(dstList, new Comparator<String>() {
			public int compare(String o1, String o2) {
				int hour1 = Integer.valueOf(o1.substring(3,6));
				int minutes1 = Integer.valueOf(o1.substring(7,9));
				int hour2 = Integer.valueOf(o2.substring(3,6));
				int minutes2 = Integer.valueOf(o2.substring(7,9));
				int ret = -1;
				if (hour1 > hour2) {
					ret = 1;
				} else if(hour1 == hour2 &&  ((Integer.signum(hour1) == -1 && minutes1 < minutes2) || (Integer.signum(hour1) != -1 && minutes1 > minutes2))) {
					ret = 1;
				} else if (hour1 == hour2 && minutes1 == minutes2) {
					ret = 0;
				}
				return ret;
			}
		});
		GregorianCalendar gc = new GregorianCalendar();				
		long hours = TimeUnit.MILLISECONDS.toHours(gc.getTimeZone().getRawOffset());
		long minutes = TimeUnit.MILLISECONDS.toMinutes(gc.getTimeZone().getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
		minutes = Math.abs(minutes);
		if (hours > 0) {
			dstDefaultValue = String.format("GMT+%02d:%02d", hours, minutes);
		} else {
			dstDefaultValue = String.format("GMT-%02d:%02d", hours, minutes);
		}
		indexDstDefault = dstList.indexOf(dstDefaultValue);
		
		geocoder = new NetworkGeocoder();
		geocoder.searchGeocoding();
		
	}
	
	public static final Map<Integer, Method> mapMethods;
	public static final Map<Integer, Time> mapTimes;
	public static final Map<Integer, HighLatMethod> mapHightLatitudeMethod;
	public static final Map<Integer, MidnightMethod> mapMidnightMethod;
	public static final Map<SoundEnum, String> mapSoundFiles = new HashMap<SoundEnum, String>();
	public static final Map<SoundEnum, String> mapDefaultSoundFiles = new HashMap<SoundEnum, String>();
	public enum SoundEnum {PLAY_FAJR, PLAY_DHUHR, PLAY_ASR, PLAY_MAGHRIB, PLAY_ISHA, PLAY_STARTUP};
	
	static {
		TreeMap<Integer, Method> mapM = new TreeMap<Integer, Method>();
		mapM.put(0, Method.MWL);
		mapM.put(1, Method.ISNA);
		mapM.put(2, Method.EGYPT);
		mapM.put(3, Method.MAKKAH);
		mapM.put(4, Method.KARACHI);
		mapM.put(5, Method.TEHRAN);
		mapM.put(6, Method.JAFARI);
		mapM.put(7, Method.CUSTOM);
		//mapM.put(7, Method.RDUM);
		//mapM.put(8, Method.CUSTOM);
		mapMethods = Collections.unmodifiableMap(mapM);
		
		TreeMap<Integer, Time> mapT = new TreeMap<Integer, Time>();
		mapT.put(0, Time.FAJR);
		mapT.put(1, Time.DHUHR);
		mapT.put(2, Time.ASR);
		mapT.put(3, Time.MAGHRIB);
		mapT.put(4, Time.ISHA);
		/*
		mapT.put(0, Time.FAJR);
		mapT.put(1, Time.SUNRISE);
		mapT.put(2, Time.DHUHR);
		mapT.put(3, Time.ASR);
		mapT.put(4, Time.SUNSET);
		mapT.put(5, Time.MAGHRIB);
		mapT.put(6, Time.ISHA);
		 */
		mapTimes = Collections.unmodifiableMap(mapT);
		
		TreeMap<Integer, HighLatMethod> mapH = new TreeMap<Integer, HighLatMethod>();
		mapH.put(0, HighLatMethod.NONE);
		mapH.put(1, HighLatMethod.NIGHT_MIDDLE);
		mapH.put(2, HighLatMethod.ONE_SEVENTH);
		mapH.put(3, HighLatMethod.ANGLE_BASED);
		mapHightLatitudeMethod= Collections.unmodifiableMap(mapH);
		
		TreeMap<Integer, MidnightMethod> mapMid = new TreeMap<Integer, MidnightMethod>();
		mapMid.put(0, MidnightMethod.STANDARD);
		mapMid.put(1, MidnightMethod.JAFARI);
		mapMidnightMethod= Collections.unmodifiableMap(mapMid);
		
		mapSoundFiles.put(SoundEnum.PLAY_FAJR, getSoundsFajrAudio());
		mapSoundFiles.put(SoundEnum.PLAY_DHUHR, getSoundsDhuhrAudio());
		mapSoundFiles.put(SoundEnum.PLAY_ASR, getSoundsAsrAudio());
		mapSoundFiles.put(SoundEnum.PLAY_MAGHRIB, getSoundsMaghribAudio());
		mapSoundFiles.put(SoundEnum.PLAY_ISHA, getSoundsIshaAudio());
		mapSoundFiles.put(SoundEnum.PLAY_STARTUP, getSoundsStartupAudio());
		
		mapDefaultSoundFiles.put(SoundEnum.PLAY_FAJR, getDefaultSoundsFajrAudio());
		mapDefaultSoundFiles.put(SoundEnum.PLAY_DHUHR, getDefaultSoundsDhuhrAudio());
		mapDefaultSoundFiles.put(SoundEnum.PLAY_ASR, getDefaultSoundsAsrAudio());
		mapDefaultSoundFiles.put(SoundEnum.PLAY_MAGHRIB, getDefaultSoundsMaghribAudio());
		mapDefaultSoundFiles.put(SoundEnum.PLAY_ISHA, getDefaultSoundsIshaAudio());
		mapDefaultSoundFiles.put(SoundEnum.PLAY_STARTUP, getDefaultSoundsStartupAudio());
	}
	
	public static void savePreferenses() {
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		try {
			preferences.sync();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getDisplayFontName() {
		return preferences.get(KEY_DISPLAY_FONT_NAME, Font.SANS_SERIF);
	}
	public static void setDisplayFontName(String value) {
		preferences.put(KEY_DISPLAY_FONT_NAME, value);
	}
	public static int getDisplayFontSize() {
		return preferences.getInt(KEY_DISPLAY_FONT_SIZE, 11);
	}
	public static void setDisplayFontSize(int value) {
		preferences.putInt(KEY_DISPLAY_FONT_SIZE, value);
	}
	public static int getDisplayFontStyleIndex() {
		return preferences.getInt(KEY_DISPLAY_FONT_STYLE, Font.PLAIN);
	}
	public static void setDisplayFontStyleIndex(int value) {
		preferences.putInt(KEY_DISPLAY_FONT_STYLE, value);
	}
	public static int getDisplayPositionX() {
		return preferences.getInt(KEY_DISPLAY_POSITION_X, -1);
	}
	public static void setDisplayPositionX(int value) {
		preferences.putInt(KEY_DISPLAY_POSITION_X, value);
	}
	public static int getDisplayPositionY() {
		return preferences.getInt(KEY_DISPLAY_POSITION_Y, -1);
	}
	public static void setDisplayPositionY(int value) {
		preferences.putInt(KEY_DISPLAY_POSITION_Y, value);
	}
	public static int getDisplayFormatTimeType() {
		return preferences.getInt(KEY_DISPLAY_FORMAT_TIME_TYPE, H24);
	}
	public static void setDisplayFormatTimeType(int value) {
		preferences.putInt(KEY_DISPLAY_FORMAT_TIME_TYPE, value);
	}
	
	
	public static String getLocaleLanguage() {
		return preferences.get(AppConfig.KEY_LOCALE_LANGUAGE, Locale.getDefault().getLanguage().toString());
	}
	public static void setLocaleLanguage(String value) {
		preferences.put(AppConfig.KEY_LOCALE_LANGUAGE, value);
	}
	public static String getLocaleCountry() {
		return preferences.get(AppConfig.KEY_LOCALE_COUNTRY, Locale.getDefault().getCountry().toString());
	}
	public static void setLocaleCountry(String value) {
		preferences.put(AppConfig.KEY_LOCALE_COUNTRY, value);
	}
	public static String getLocaleVariant() {
		return preferences.get(AppConfig.KEY_LOCALE_VARIANT, Locale.getDefault().getVariant().toString());
	}
	public static void setLocaleVariant(String value) {
		preferences.put(AppConfig.KEY_LOCALE_VARIANT, value);
	}	
	
	public static int getTimeZoneType() {
		return preferences.getInt(KEY_CALCULATION_TIMEZONE_TYPE, AUTO);
	}
	public static void setTimeZoneType(int value) {
		preferences.putInt(KEY_CALCULATION_TIMEZONE_TYPE, value);
	}
	public static int getTimeZoneIndex() {
		return preferences.getInt(KEY_CALCULATION_TIMEZONE_INDEX, indexDstDefault);
	}
	public static void setTimeZoneIndex(int value) {
		preferences.putInt(KEY_CALCULATION_TIMEZONE_INDEX, value);
	}

	public static double getCalculationLatitude() {
		return preferences.getDouble(KEY_LOCATION_LATITUDE, geocoder.getGeocoderHelper()[0].getLatitude());
	}
	public static void setCalculationLatitude(double value) {
		preferences.putDouble(KEY_LOCATION_LATITUDE, value);
	}
	public static double getCalculationLongitude() {
		return preferences.getDouble(KEY_LOCATION_LONGITUDE, geocoder.getGeocoderHelper()[0].getLongitude());
	}
	public static void setCalculationLongitude(double value) {
		preferences.putDouble(KEY_LOCATION_LONGITUDE, value);
	}
	public static double getCalculationElevation() {
		return preferences.getDouble(KEY_LOCATION_ELEVATION, geocoder.getGeocoderHelper()[0].getElevation());
	}
	public static void setCalculationElevation(double value) {
		preferences.putDouble(KEY_LOCATION_ELEVATION, value);
	}
	
	public static int getCalculationMethodIndex() {
		return preferences.getInt(KEY_CALCULATION_METHOD_TYPE, 0);
	}
	public static void setCalculationMethodIndex(int value) {
		preferences.putInt(KEY_CALCULATION_METHOD_TYPE, value);
	}
	
	public static double getCalculationMethodFajrValue() {
		return preferences.getDouble(KEY_CALCULATION_METHOD_FAJR_VALUE, 18.0d);
	}
	public static void setCalculationMethodFajrValue(double value) {
		preferences.putDouble(KEY_CALCULATION_METHOD_FAJR_VALUE, value);
	}
	public static double getCalculationMethodIshaValue() {
		return preferences.getDouble(KEY_CALCULATION_METHOD_ISHA_VALUE, 18.0d);
	}
	public static void setCalculationMethodIshaValue(double value) {
		preferences.putDouble(KEY_CALCULATION_METHOD_ISHA_VALUE, value);
	}
	public static double getCalculationMethodMaghribValue() {
		return preferences.getDouble(KEY_CALCULATION_METHOD_MAGHRIB_VALUE, 18.0d);
	}
	public static void setCalculationMethodMaghribValue(double value) {
		preferences.putDouble(KEY_CALCULATION_METHOD_MAGHRIB_VALUE, value);
	}
	
	public static int getCalculationMethodIshaType() {
		return preferences.getInt(KEY_CALCULATION_METHOD_ISHA_TYPE, ANGLE);
	}
	public static void setCalculationMethodIshaType(int value) {
		preferences.putInt(KEY_CALCULATION_METHOD_ISHA_TYPE, value);
	}
	public static int getCalculationMethodMaghribType() {
		return preferences.getInt(KEY_CALCULATION_METHOD_MAGHRIB_TYPE, ANGLE);
	}
	public static void setCalculationMethodMaghribType(int value) {
		preferences.putInt(KEY_CALCULATION_METHOD_MAGHRIB_TYPE, value);
	}
	

	public static double getCalculationMethodDhuhrValue() {
		return preferences.getDouble(KEY_CALCULATION_METHOD_OTHER_DHUHR_VALUE, 0.0d);
	}
	public static void setCalculationMethodDhuhrValue(double value) {
		preferences.putDouble(KEY_CALCULATION_METHOD_OTHER_DHUHR_VALUE, value);
	}
	public static int getCalculationMethodAsrFactor() {
		return preferences.getInt(KEY_CALCULATION_METHOD_OTHER_ASR_TYPE, Method.ASR_FACTOR_STANDARD);
	}
	public static void setCalculationMethodAsrFactor(int value) {
		preferences.putInt(KEY_CALCULATION_METHOD_OTHER_ASR_TYPE, value);
	}
	public static int getCalculationMethodHigherLatitudeIndex() {
		return preferences.getInt(KEY_CALCULATION_METHOD_OTHER_HL_TYPE, 1);
	}
	public static void setCalculationMethodHigherLatitudeIndex(int value) {
		preferences.putInt(KEY_CALCULATION_METHOD_OTHER_HL_TYPE, value);
	}
	public static int getCalculationMethodMidnightIndex() {
		return preferences.getInt(KEY_CALCULATION_METHOD_OTHER_MIDNIGHT_METHOD_TYPE, 1);
	}
	public static void setCalculationMethodMidnightIndex(int value) {
		preferences.putInt(KEY_CALCULATION_METHOD_OTHER_MIDNIGHT_METHOD_TYPE, 0);
	}
	
	public static boolean getDisplayFajr() {
		return preferences.getBoolean(KEY_DISPLAY_FAJR, true);
	}
	public static void setDisplayFajr(boolean value) {
		preferences.putBoolean(KEY_DISPLAY_FAJR, value);
	}
	public static boolean getDisplaySunrise() {
		return preferences.getBoolean(KEY_DISPLAY_SUNRISE, true);
	}
	public static void setDisplaySunrise(boolean value) {
		preferences.putBoolean(KEY_DISPLAY_SUNRISE, value);
	}
	public static boolean getDisplayDhuhr() {
		return preferences.getBoolean(KEY_DISPLAY_DHUHR, true);
	}
	public static void setDisplayDhuhr(boolean value) {
		preferences.putBoolean(KEY_DISPLAY_DHUHR, value);
	}
	public static boolean getDisplayAsr() {
		return preferences.getBoolean(KEY_DISPLAY_ASR, true);
	}
	public static void setDisplayAsr(boolean value) {
		preferences.putBoolean(KEY_DISPLAY_ASR, value);
	}
	public static boolean getDisplaySunset() {
		return preferences.getBoolean(KEY_DISPLAY_SUNSET, true);
	}
	public static void setDisplaySunset(boolean value) {
		preferences.putBoolean(KEY_DISPLAY_SUNSET, value);
	}
	public static boolean getDisplayMaghrib() {
		return preferences.getBoolean(KEY_DISPLAY_MAGHRIB, true);
	}
	public static void setDisplayMaghrib(boolean value) {
		preferences.putBoolean(KEY_DISPLAY_MAGHRIB, value);
	}
	public static boolean getDisplayIsha() {
		return preferences.getBoolean(KEY_DISPLAY_ISHA, true);
	}
	public static void setDisplayIsha(boolean value) {
		preferences.putBoolean(KEY_DISPLAY_ISHA, value);
	}
	public static boolean getDisplayAlwaysOnTop() {
		return preferences.getBoolean(KEY_DISPLAY_ALWAYS_ON_TOP, true);
	}
	public static void setDisplayAlwaysOnTop(boolean value) {
		preferences.putBoolean(KEY_DISPLAY_ALWAYS_ON_TOP, value);
	}
	public static boolean getDisplayLaunchOnStartup() {
		return preferences.getBoolean(KEY_DISPLAY_LAUNCH_ON_STARTUP, false);
	}
	public static void setDisplayLaunchOnStartup(boolean value) {
		preferences.putBoolean(KEY_DISPLAY_LAUNCH_ON_STARTUP, value);
	}
	
	public static int getDisplayOpacity() {
		return preferences.getInt(KEY_DISPLAY_OPACITY, 95);
	}
	public static void setDisplayOpacity(int value) {
		preferences.putInt(KEY_DISPLAY_OPACITY, value);
	}

	public static String getAlertsAlarmFajrAfter() {
		return preferences.get(KEY_ALERTS_ALARM_FAJR_AFTER, "0");
	}
	public static void setAlertsAlarmFajrAfter(String value) {
		preferences.put(KEY_ALERTS_ALARM_FAJR_AFTER, value);
	}
	public static String getAlertsAlarmFajrBefore() {
		return preferences.get(KEY_ALERTS_ALARM_FAJR_BEFORE, "0");
	}
	public static void setAlertsAlarmFajrBefore(String value) {
		preferences.put(KEY_ALERTS_ALARM_FAJR_BEFORE, value);
	}
	public static String getAlertsAlarmSunriseAfter() {
		return preferences.get(KEY_ALERTS_ALARM_SUNRISE_AFTER, "0");
	}
	public static void setAlertsAlarmSunriseAfter(String value) {
		preferences.put(KEY_ALERTS_ALARM_SUNRISE_AFTER, value);
	}
	public static String getAlertsAlarmSunriseBefore() {
		return preferences.get(KEY_ALERTS_ALARM_SUNRISE_BEFORE, "0");
	}
	public static void setAlertsAlarmSunriseBefore(String value) {
		preferences.put(KEY_ALERTS_ALARM_SUNRISE_BEFORE, value);
	}
	public static String getAlertsAlarmDhuhrAfter() {
		return preferences.get(KEY_ALERTS_ALARM_DHUHR_AFTER, "0");
	}
	public static void setAlertsAlarmDhuhrAfter(String value) {
		preferences.put(KEY_ALERTS_ALARM_DHUHR_AFTER, value);
	}
	public static String getAlertsAlarmDhuhrBefore() {
		return preferences.get(KEY_ALERTS_ALARM_DHUHR_BEFORE, "0");
	}
	public static void setAlertsAlarmDhuhrBefore(String value) {
		preferences.put(KEY_ALERTS_ALARM_DHUHR_BEFORE, value);
	}
	public static String getAlertsAlarmAsrAfter() {
		return preferences.get(KEY_ALERTS_ALARM_ASR_AFTER, "0");
	}
	public static void setAlertsAlarmAsrAfter(String value) {
		preferences.put(KEY_ALERTS_ALARM_ASR_AFTER, value);
	}
	public static String getAlertsAlarmAsrBefore() {
		return preferences.get(KEY_ALERTS_ALARM_ASR_BEFORE, "0");
	}
	public static void setAlertsAlarmAsrBefore(String value) {
		preferences.put(KEY_ALERTS_ALARM_ASR_BEFORE, value);
	}
	public static String getAlertsAlarmSunsetAfter() {
		return preferences.get(KEY_ALERTS_ALARM_SUNSET_AFTER, "0");
	}
	public static void setAlertsAlarmSunsetAfter(String value) {
		preferences.put(KEY_ALERTS_ALARM_SUNSET_AFTER, value);
	}
	public static String getAlertsAlarmSunsetBefore() {
		return preferences.get(KEY_ALERTS_ALARM_SUNSET_BEFORE, "0");
	}
	public static void setAlertsAlarmSunsetBefore(String value) {
		preferences.put(KEY_ALERTS_ALARM_SUNSET_BEFORE, value);
	}
	public static String getAlertsAlarmMaghribAfter() {
		return preferences.get(KEY_ALERTS_ALARM_MAGHRIB_AFTER, "0");
	}
	public static void setAlertsAlarmMaghribAfter(String value) {
		preferences.put(KEY_ALERTS_ALARM_MAGHRIB_AFTER, value);
	}
	public static String getAlertsAlarmMaghribBefore() {
		return preferences.get(KEY_ALERTS_ALARM_MAGHRIB_BEFORE, "0");
	}
	public static void setAlertsAlarmMaghribBefore(String value) {
		preferences.put(KEY_ALERTS_ALARM_MAGHRIB_BEFORE, value);
	}
	public static String getAlertsAlarmIshaBefore() {
		return preferences.get(KEY_ALERTS_ALARM_ISHA_BEFORE, "0");
	}
	public static void setAlertsAlarmIshaBefore(String value) {
		preferences.put(KEY_ALERTS_ALARM_ISHA_BEFORE, value);
	}
	public static String getAlertsAlarmIshaAfter() {
		return preferences.get(KEY_ALERTS_ALARM_ISHA_AFTER, "0");
	}
	public static void setAlertsAlarmIshaAfter(String value) {
		preferences.put(KEY_ALERTS_ALARM_ISHA_AFTER, value);
	}
	public static String getAlertsAlarmAudio() {
		return preferences.get(KEY_ALERTS_ALARM_AUDIO, getDefaultAlertsAlarmAudio());
	}
	public static void setAlertsAlarmAudio(String value) {
		preferences.put(KEY_ALERTS_ALARM_AUDIO, value);
	}
	
	public static boolean getAlertsVisualEffectShowBalonTips() {
		return preferences.getBoolean(KEY_ALERTS_VIZUAL_EFFECT_SHOW_BALON_TIPS, false);
	}
	public static void setAlertsVisualEffectShowBalonTips(boolean value) {
		preferences.putBoolean(KEY_ALERTS_VIZUAL_EFFECT_SHOW_BALON_TIPS, value);
	}
	public static boolean getAlertsVisualEffectShowMainScreen() {
		return preferences.getBoolean(KEY_ALERTS_VIZUAL_EFFECT_SHOW_MAIN_SCREEN, false);
	}
	public static void setAlertsVisualEffectShowMainScreen(boolean value) {
		preferences.putBoolean(KEY_ALERTS_VIZUAL_EFFECT_SHOW_MAIN_SCREEN, value);
	}
	public static boolean getAlertsVisualEffectFlashIconTray() {
		return preferences.getBoolean(KEY_ALERTS_VIZUAL_EFFECT_FLASH_ICON_TRAY, false);
	}
	public static void setAlertsVisualEffectFlashIconTray(boolean value) {
		preferences.putBoolean(KEY_ALERTS_VIZUAL_EFFECT_FLASH_ICON_TRAY, value);
	}
	
	public static String getSoundsFajrAudio() {
		return preferences.get(KEY_SOUNDS_FAJR_AUDIO, getDefaultSoundsFajrAudio());
	}
	public static void setSoundsFajrAudio(String value) {
		preferences.put(KEY_SOUNDS_FAJR_AUDIO, value);
	}
	public static String getSoundsDhuhrAudio() {
		return preferences.get(KEY_SOUNDS_DHUHR_AUDIO, getDefaultSoundsDhuhrAudio());
	}
	public static void setSoundsDhuhrAudio(String value) {
		preferences.put(KEY_SOUNDS_DHUHR_AUDIO, value);
	}
	public static String getSoundsAsrAudio() {
		return preferences.get(KEY_SOUNDS_ASR_AUDIO, getDefaultSoundsAsrAudio());
	}
	public static void setSoundsAsrAudio(String value) {
		preferences.put(KEY_SOUNDS_ASR_AUDIO, value);
	}
	public static String getSoundsMaghribAudio() {
		return preferences.get(KEY_SOUNDS_MAGHRIB_AUDIO, getDefaultSoundsMaghribAudio());
	}
	public static void setSoundsMaghribAudio(String value) {
		preferences.put(KEY_SOUNDS_MAGHRIB_AUDIO, value);
	}
	public static String getSoundsIshaAudio() {
		
		return preferences.get(KEY_SOUNDS_ISHA_AUDIO, getDefaultSoundsIshaAudio());
	}
	public static void setSoundsIshaAudio(String value) {
		preferences.put(KEY_SOUNDS_ISHA_AUDIO, value);
	}
	public static String getSoundsStartupAudio() {
		return preferences.get(KEY_SOUNDS_STARTUP_AUDIO, getDefaultSoundsStartupAudio());
	}
	public static void setSoundsStartupAudio(String value) {
		preferences.put(KEY_SOUNDS_STARTUP_AUDIO, value);
	}
	
	public static String getDefaultSoundsFajrAudio() {
		return AppConfig.class.getResource("/com/koylubaevnt/jpraytimes/sounds/fajr_athan.mp3").toString();
	}
	public static String getDefaultSoundsDhuhrAudio() {
		return AppConfig.class.getResource("/com/koylubaevnt/jpraytimes/sounds/athan.mp3").toString();
	}
	public static String getDefaultSoundsAsrAudio() {
		return AppConfig.class.getResource("/com/koylubaevnt/jpraytimes/sounds/athan.mp3").toString();
	}
	public static String getDefaultSoundsMaghribAudio() {
		return AppConfig.class.getResource("/com/koylubaevnt/jpraytimes/sounds/athan.mp3").toString();
	}
	public static String getDefaultSoundsIshaAudio() {
		
		return AppConfig.class.getResource("/com/koylubaevnt/jpraytimes/sounds/athan.mp3").toString();
	}
	public static String getDefaultSoundsStartupAudio() {
		return AppConfig.class.getResource("/com/koylubaevnt/jpraytimes/sounds/startup.mp3").toString();
	}
	public static String getDefaultAlertsAlarmAudio() {
		return AppConfig.class.getResource("/com/koylubaevnt/jpraytimes/sounds/alarm.mp3").toString();
	}
	
	public static String[] getAlarmMinutes() {
		return alarmMinutes;
	}
	
	public static Vector<Locale> getLocales() {
		return locales;
	}
	
	public static String getLocaleInformation(Locale locale) {
		return locale.getDisplayLanguage() + (locale.getCountry().isEmpty() ? "" : " [" + locale.getDisplayCountry() + "]");
	}
	
	private static List<String> getListResourceFiles(String path ) {
		List<String> filenames = new ArrayList<String>();
		URI uri;
		Path myPath = null;
		try {
			uri = AppConfig.class.getResource(path).toURI();
			if (uri.getScheme().equals("jar")) {
		        myPath = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()).getPath(path);
		    } else {
		        myPath = Paths.get(uri);
		    }
			for (Path file: Files.newDirectoryStream(myPath)) {
				filenames.add(file.getFileName().toString());
		    }		    
		} catch (URISyntaxException e) {
			
		} catch (IOException e) {
			
		} 
	    return filenames;
	}
	 

	
}