/**
 * 
 */
package com.koylubaevnt.jpraytimes;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import com.koylubaevnt.jpraytimes.audio.CustomPlayer;
import com.koylubaevnt.jpraytimes.forms.AboutDialog;
import com.koylubaevnt.jpraytimes.forms.MonthlyTimeTable;
import com.koylubaevnt.jpraytimes.forms.OptionsDialog;
import com.koylubaevnt.jpraytimes.forms.components.OwnTrayIcon;
import com.koylubaevnt.jpraytimes.forms.components.TextBubbleBorder;
import com.koylubaevnt.jpraytimes.i18n.I18n;
import com.koylubaevnt.jpraytimes.preferences.AppConfig;
import com.koylubaevnt.jpraytimes.tools.AppLock;

import id.web.michsan.praytimes.Configuration;
import id.web.michsan.praytimes.Location;
import id.web.michsan.praytimes.Method;
import id.web.michsan.praytimes.PrayTimes;
import id.web.michsan.praytimes.PrayTimes.Time;
import id.web.michsan.praytimes.Util;
import id.web.michsan.praytimes.Util.DayTime;

/**
 * @author 
 *
 */
public class JWidget extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2324348484883488453L;
	
	private JFrame mainFrame;
	private PrayTimes prayTime;
	private Map<Time, Double> result; 
	
	
	GraphicsEnvironment ge;
	GraphicsDevice gd;
	
	private int INNER_INSETS = 2;
	private int INSETS = 5;
	private int nameWidth;
	private int timeWidth;
	private int height;
	private int widthPanel;
	private int xMouse;
	private int yMouse;
	private Point widgetPosition; 
	
	private DateFormat dateFormat = DateFormat.getDateInstance();

	private JPopupMenu jPopupMenu;
	private PopupMenu popupMenu;
	
	NextPrayTimeInfo npti;
	SystemTray systemTray;
	TrayIcon trayIcon = null;
	OwnTrayIcon ownTrayIcon = null; 
	
	AbstractBorder abstractBorder;
	
	/*Player for play sounds*/
	CustomPlayer alarmPlayer = new CustomPlayer();
	CustomPlayer player = new CustomPlayer();
	
	/*TODO Global variable for settings application*/
	/*Need add language variable...*/
	private Boolean alwaysOnTop, launchOnStartup;
	private Font appFont;
	private static final Map<Time, Boolean> mapTimeVisible = new HashMap<Time, Boolean>();
	private int timeFormat;
	
	private Location location;
	String timeZone;
	
	private Method calculationMethod;
	private static final Map<Time, Configuration> mapAdjustments = new HashMap<Time, Configuration>();
	private static final Map<Time, Integer> offsets = new HashMap<Time, Integer>();
	private GregorianCalendar calculationDate;
	private final Map<Time, Integer> mapAlarmBefore = new HashMap<Time, Integer>();
	private final Map<Time, Integer> mapAlarmAfter = new HashMap<Time, Integer>();
	URL alarmFileURL;
	boolean showBalonTip, showMainScreen, showFlashTrayIcon;
	URL startupSoundFileURL;
	private final Map<Time, URL> mapSound = new HashMap<Time, URL>();
	
	private Image appImage = createImage("/com/koylubaevnt/jpraytimes/images/tray_icon.png", I18n.resourceBundle.getString("tray_icon"));
	private Image appImageAlternated = createImage("/com/koylubaevnt/jpraytimes/images/tray_icon_alternate.png", I18n.resourceBundle.getString("tray_icon"));
	
	/*TODO variable for calculating*/
	Time nextPrayTime = null;
	Time currentPrayTime = null;
	
	public JWidget() {
		
		setIconImage(appImage);
		
		reinitializeComponents();
		
		registerSystemTray();
		
		if (startupSoundFileURL != null) {
			CustomPlayer cp = new CustomPlayer();
			cp.setPath(startupSoundFileURL);
			cp.play(-1);
		}
		
	}
	
	private void reinitializeComponents() {
		if (alarmPlayer.getPlayer() != null)
			alarmPlayer.pause();
		if (player.getPlayer() != null)
			player.pause();
		
		nextPrayTime = null;
		currentPrayTime = null;
		
		getAppSettings();
		
		makeJPopupMenu();
		makePopupMenu();
		
		stopPlayingLabel.setIcon(new ImageIcon(createImage("/com/koylubaevnt/jpraytimes/images/stop_32.png", "")));
		
		
		calculalateComponentSize();
		
		initComponents();
		
		if (npti != null)
			npti.stop = true;
		npti = new NextPrayTimeInfo();
		npti.start();
		
		mainFrame = this;
	}
	
	/*TODO check that  own tray is working*/
	private void registerSystemTray() {
		if (!SystemTray.isSupported()) {
			ownTrayIcon = new OwnTrayIcon(appImage);
			return;
	    }
		trayIcon = new TrayIcon(appImage);
		trayIcon.setImageAutoSize(true);
		systemTray = SystemTray.getSystemTray();
		try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
        	trayIcon = null;
        	return;
        }
		trayIcon.setPopupMenu(popupMenu);
		
		trayIcon.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
					mainFrame.toFront();
				}
			}
		});
	}
	
	private void showBaloonTip(String caption, String text, MessageType messageType) {
		if (trayIcon != null)
			trayIcon.displayMessage(caption, text, messageType);
		else if (ownTrayIcon != null)
			ownTrayIcon.displayMessage(caption, text, messageType);
	}
	
	private void getAppSettings() {
		/*Global settings is:
		 *
		 * appFont 			- font for application: font, style, size
		 * widgetPosition 	- position of widjet on screen
		 * 
		 */
		int xPosition = AppConfig.getDisplayPositionX();
		int yPosition = AppConfig.getDisplayPositionY();
		widgetPosition = new Point(xPosition, yPosition);
		
		/*
		 * Panel: General
		 */
		/*TODO language*/
		
		alwaysOnTop = AppConfig.getDisplayAlwaysOnTop();
		launchOnStartup = AppConfig.getDisplayLaunchOnStartup();

		String fontName = AppConfig.getDisplayFontName();
		int fontSize = AppConfig.getDisplayFontSize();
		int fontStyle = AppConfig.getDisplayFontStyleIndex();
		appFont = new Font(fontName, fontStyle, fontSize);
		
		mapTimeVisible.put(Time.FAJR, AppConfig.getDisplayFajr());
		mapTimeVisible.put(Time.SUNRISE, AppConfig.getDisplaySunrise());
		mapTimeVisible.put(Time.DHUHR, AppConfig.getDisplayDhuhr());
		mapTimeVisible.put(Time.ASR, AppConfig.getDisplayAsr());
		mapTimeVisible.put(Time.SUNSET, AppConfig.getDisplaySunset());
		mapTimeVisible.put(Time.MAGHRIB, AppConfig.getDisplayMaghrib());
		mapTimeVisible.put(Time.ISHA, AppConfig.getDisplayIsha());
		
		/*TODO timeFormat...*/
		timeFormat = AppConfig.getDisplayFormatTimeType();
		
		/*
		 * Panel: Calculation
		 */
		//Location
		double latitude = AppConfig.getCalculationLatitude();
		double longitude = AppConfig.getCalculationLongitude();
		double elevation = AppConfig.getCalculationElevation();
		location = new Location(latitude, longitude, elevation);
		
		//TimeZone
		timeZone = "";
		if (AppConfig.getTimeZoneType() != AppConfig.AUTO) {
			timeZone = AppConfig.dstList.get(AppConfig.getTimeZoneIndex());
		}
		
		//Methods
		int methodIndex = AppConfig.getCalculationMethodIndex();
		int asrFactor  = AppConfig.getCalculationMethodAsrFactor();
		int hightLatitudeMethodIndex  = AppConfig.getCalculationMethodHigherLatitudeIndex();
		int midnightMethodIndex  = AppConfig.getCalculationMethodMidnightIndex();
		calculationMethod = AppConfig.mapMethods.get(methodIndex);
		prayTime = new PrayTimes(calculationMethod);
		prayTime.setAsrFactor(asrFactor);
		prayTime.setHighLatMethod(AppConfig.mapHightLatitudeMethod.get(hightLatitudeMethodIndex));
		if (calculationMethod.equals(Method.CUSTOM)) {
			prayTime.setMidnightMethod(AppConfig.mapMidnightMethod.get(midnightMethodIndex));
			Configuration config = Configuration.angle(AppConfig.getCalculationMethodFajrValue());
			mapAdjustments.put(Time.FAJR, config);
			if (AppConfig.getCalculationMethodIshaType() == AppConfig.ANGLE)
				config = Configuration.angle(AppConfig.getCalculationMethodIshaValue());
			else
				config = Configuration.minutes(AppConfig.getCalculationMethodIshaValue());
			mapAdjustments.put(Time.ISHA, config);
			if (AppConfig.getCalculationMethodMaghribType() == AppConfig.ANGLE) 
				config = Configuration.angle(AppConfig.getCalculationMethodMaghribValue());
			else
				config = Configuration.minutes(AppConfig.getCalculationMethodMaghribValue());
			mapAdjustments.put(Time.MAGHRIB, config);
		}
		mapAdjustments.put(Time.DHUHR, Configuration.minutes(AppConfig.getCalculationMethodDhuhrValue()));
		prayTime.adjust(mapAdjustments);
		/*TODO in settings form it's not present yet... Need recoding when it's will be done..*/
		
		//Set offsets
		prayTime.tuneOffset(offsets);
		
		/*
		 * Panel: Alerts and Sound
		 */
		/*
		 * Sub panel: Alerts
		 */
		int alarmFajrBefore = Integer.valueOf(AppConfig.getAlertsAlarmFajrBefore());
		int alarmFajrAfter = Integer.valueOf(AppConfig.getAlertsAlarmFajrAfter());
		int alarmSunriseBefore = Integer.valueOf(AppConfig.getAlertsAlarmSunriseBefore());
		int alarmSunriseAfter = Integer.valueOf(AppConfig.getAlertsAlarmSunriseAfter());
		int alarmDhuhrBefore = Integer.valueOf(AppConfig.getAlertsAlarmDhuhrBefore());
		int alarmDhuhrAfter = Integer.valueOf(AppConfig.getAlertsAlarmDhuhrAfter());
		int alarmAsrBefore = Integer.valueOf(AppConfig.getAlertsAlarmAsrBefore());
		int alarmAsrAfter = Integer.valueOf(AppConfig.getAlertsAlarmAsrAfter());
		int alarmSunsetBefore = Integer.valueOf(AppConfig.getAlertsAlarmSunsetBefore());
		int alarmSunsetAfter = Integer.valueOf(AppConfig.getAlertsAlarmSunsetAfter());
		int alarmMaghribBefore = Integer.valueOf(AppConfig.getAlertsAlarmMaghribBefore());
		int alarmMaghribAfter = Integer.valueOf(AppConfig.getAlertsAlarmMaghribAfter());
		int alarmIshaBefore = Integer.valueOf(AppConfig.getAlertsAlarmIshaBefore());
		int alarmIshaAfter = Integer.valueOf(AppConfig.getAlertsAlarmIshaAfter());
		
		mapAlarmBefore.put(Time.FAJR, alarmFajrBefore);
		mapAlarmAfter.put(Time.FAJR, alarmFajrAfter);
		mapAlarmBefore.put(Time.SUNRISE, alarmSunriseBefore);
		mapAlarmAfter.put(Time.SUNRISE, alarmSunriseAfter);
		mapAlarmBefore.put(Time.DHUHR, alarmDhuhrBefore);
		mapAlarmAfter.put(Time.DHUHR, alarmDhuhrAfter);
		mapAlarmBefore.put(Time.ASR, alarmAsrBefore);
		mapAlarmAfter.put(Time.ASR, alarmAsrAfter);
		mapAlarmBefore.put(Time.SUNSET, alarmSunsetBefore);
		mapAlarmAfter.put(Time.SUNSET, alarmSunsetAfter);
		mapAlarmBefore.put(Time.MAGHRIB, alarmMaghribBefore);
		mapAlarmAfter.put(Time.MAGHRIB, alarmMaghribAfter);
		mapAlarmBefore.put(Time.ISHA, alarmIshaBefore);
		mapAlarmAfter.put(Time.ISHA, alarmIshaAfter);
		
		try {
			alarmFileURL = new URL(AppConfig.getAlertsAlarmAudio());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		alarmPlayer.setPath(alarmFileURL);
		
		showBalonTip = AppConfig.getAlertsVisualEffectShowBalonTips();
		showMainScreen = AppConfig.getAlertsVisualEffectShowMainScreen();
		showFlashTrayIcon = AppConfig.getAlertsVisualEffectFlashIconTray();
		
		/*
		 * Sub panel: Sounds
		 */
		URL fajrSoundFileURL = null, dhuhrSoundFileURL = null, asrSoundFileURL = null, maghribSoundFileURL = null, ishaSoundFileURL = null;
		
		try {
			fajrSoundFileURL = new URL(AppConfig.getSoundsFajrAudio());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			dhuhrSoundFileURL = new URL(AppConfig.getSoundsDhuhrAudio());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			asrSoundFileURL = new URL(AppConfig.getSoundsAsrAudio());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			maghribSoundFileURL = new URL(AppConfig.getSoundsMaghribAudio());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			ishaSoundFileURL = new URL(AppConfig.getSoundsIshaAudio());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		mapSound.put(Time.FAJR, fajrSoundFileURL);
		mapSound.put(Time.DHUHR, dhuhrSoundFileURL);
		mapSound.put(Time.ASR, asrSoundFileURL);
		mapSound.put(Time.MAGHRIB, maghribSoundFileURL);
		mapSound.put(Time.ISHA, ishaSoundFileURL);
		
		try {
			startupSoundFileURL = new URL(AppConfig.getSoundsStartupAudio());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		calculationDate = new GregorianCalendar();
	}
	
	private void calculalateComponentSize() {
		FontMetrics fontMetrics, fm;
		int width = 0, width2 = 0;
		int tmp_width, tmp_width2;
		fontMetrics = getFontMetrics(appFont);
		fm = getFontMetrics(appFont.deriveFont(Font.BOLD & Font.ITALIC));
		
		String timeName;
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, Time> time : AppConfig.mapTimes.entrySet()) {
			timeName = time.getValue().toString().toLowerCase();
			if (mapTimeVisible.get(time.getValue()).booleanValue()) {
				tmp_width = fontMetrics.stringWidth(I18n.resourceBundle.getString(timeName));
				width = (tmp_width > width) ? tmp_width : width;
				
				sb.append(I18n.resourceBundle.getString("time_left_to"));
		        sb.append(" ");
		        sb.append(I18n.resourceBundle.getString(timeName));
		        tmp_width2 = fm.stringWidth(sb.toString());
		        width2 = (tmp_width2 > width2) ? tmp_width2 : width2;
		        
		        sb.setLength(0);
		        sb.append("24 ");
		        sb.append(I18n.resourceBundle.getString("hours"));
		        sb.append(" 60 ");
		        sb.append(I18n.resourceBundle.getString("minutes"));
		        tmp_width2 = fontMetrics.stringWidth(sb.toString());
		        width2 = (tmp_width2 > width2) ? tmp_width2 : width2;
			}
	        sb.setLength(0);
		}

        sb.append(I18n.resourceBundle.getString("today"));
        sb.append(": ");
        sb.append(dateFormat.format(calculationDate.getTime()).toString());
        tmp_width2 = fm.stringWidth(sb.toString());
        width2 = (tmp_width2 > width2) ? tmp_width2 : width2;
		
        nameWidth = width;
		height = fontMetrics.getHeight();
		timeWidth = fontMetrics.stringWidth("00:00");
		widthPanel = width2 + INSETS * 4;
		
		if(widthPanel > nameWidth + timeWidth + 3 * INNER_INSETS) {
			int difference = widthPanel - (nameWidth + timeWidth + 4 * INNER_INSETS);
			int first = difference/ 2;
			nameWidth = nameWidth + first;
			timeWidth = timeWidth + difference - first;
			
		} else if(widthPanel < nameWidth + timeWidth + 4 * INNER_INSETS) {
			widthPanel = nameWidth + timeWidth + 4 * INNER_INSETS;
		}
	
	}
	
	public void initComponents() {
		setType(Window.Type.UTILITY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setResizable(false);
		setAlwaysOnTop(alwaysOnTop);
		final JFrame frame = this;
		
		/*TODO add setColor to Frame! Think from witch*/
		
		frame.addComponentListener(new ComponentAdapter() {
		    public void componentResized(ComponentEvent e) {
		    	frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 40, 40));
		    }   
		});
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				if (alarmPlayer.getPlayer() != null)
					alarmPlayer.pause();
				if (player.getPlayer() != null)
					player.pause();
				if (trayIcon != null)
					systemTray.remove(trayIcon);
				AppLock.releaseLock();
			}
		});
		
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
        	setOpacity(AppConfig.getDisplayOpacity() / 100f);
        }
        
		mainPanel.setLayout(new GridBagLayout());
		
		int index = 0;
		int heightMain = 0;
		GridBagConstraints gridBagConstraints;
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = index;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(INSETS, INSETS, 0, INSETS);
        
        StringBuilder sb = new StringBuilder();
        sb.append("<html><strong>");
        sb.append(I18n.resourceBundle.getString("today"));
        sb.append(":");
        sb.append("</strong> <em>");
        sb.append(dateFormat.format(calculationDate.getTime()).toString());
        sb.append("</em></html>");
        
        /*TODO Need think about selection Color...*/
        abstractBorder = new TextBubbleBorder(Color.red,2,16,0);

        todayLabel.setFont(appFont);
        todayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        //todayLabel.setBorder(BorderFactory.createEtchedBorder(Color.white, Color.red));
        todayLabel.setBorder(abstractBorder);
        todayLabel.setText(sb.toString());
        Dimension panelDimension = new Dimension(widthPanel, height * 2);
        heightMain += height * 2 + INSETS; 
        todayLabel.setMaximumSize(panelDimension);
        todayLabel.setMinimumSize(panelDimension);
        todayLabel.setPreferredSize(panelDimension);
        
        mainPanel.add(todayLabel, gridBagConstraints);
        index++;
        
		if (AppConfig.getDisplayFajr()) {
			createPrayTimePanel(fajrPanel, fajrNameLabel, fajrTimeLabel);
			gridBagConstraints.gridy = index;
	        mainPanel.add(fajrPanel, gridBagConstraints);
	        index++;
	        heightMain += height + INSETS + 2 * INNER_INSETS;
		}
		if (AppConfig.getDisplaySunrise()) { 
			createPrayTimePanel(sunrisePanel, sunriseNameLabel, sunriseTimeLabel);
			gridBagConstraints.gridy = index;
	        mainPanel.add(sunrisePanel, gridBagConstraints);
	        index++;
	        heightMain += height + INSETS + 2 * INNER_INSETS;
		}
		if (AppConfig.getDisplayDhuhr()) { 
			createPrayTimePanel(dhuhrPanel, dhuhrNameLabel, dhuhrTimeLabel);
			gridBagConstraints.gridy = index;
	        mainPanel.add(dhuhrPanel, gridBagConstraints);
	        index++;
	        heightMain += height + INSETS + 2 * INNER_INSETS;
		}
		if (AppConfig.getDisplayAsr()) { 
			createPrayTimePanel(asrPanel, asrNameLabel, asrTimeLabel);
			gridBagConstraints.gridy = index;
	        mainPanel.add(asrPanel, gridBagConstraints);
	        index++;
	        heightMain += height + INSETS + 2 * INNER_INSETS;
		}
		if (AppConfig.getDisplaySunset()) { 
			createPrayTimePanel(sunsetPanel, sunsetNameLabel, sunsetTimeLabel);
			gridBagConstraints.gridy = index;
	        mainPanel.add(sunsetPanel, gridBagConstraints);
	        index++;
	        heightMain += height + INSETS + 2 * INNER_INSETS;
		}
		if (AppConfig.getDisplayMaghrib()) { 
			createPrayTimePanel(maghribPanel, maghribNameLabel, maghribTimeLabel);
			gridBagConstraints.gridy = index;
	        mainPanel.add(maghribPanel, gridBagConstraints);
	        index++;
	        heightMain += height + INSETS + 2 * INNER_INSETS;
		}
		if (AppConfig.getDisplayIsha()) { 
			createPrayTimePanel(ishaPanel, ishaNameLabel, ishaTimeLabel);
			gridBagConstraints.gridy = index;
	        mainPanel.add(ishaPanel, gridBagConstraints);
	        index++;
	        heightMain += height + INSETS + 2 * INNER_INSETS;
		}
		fajrPanel.setVisible(AppConfig.getDisplayFajr());
		sunrisePanel.setVisible(AppConfig.getDisplaySunrise());
		dhuhrPanel.setVisible(AppConfig.getDisplayDhuhr());
		asrPanel.setVisible(AppConfig.getDisplayAsr());
		sunsetPanel.setVisible(AppConfig.getDisplaySunset());
		maghribPanel.setVisible(AppConfig.getDisplayMaghrib());
		ishaPanel.setVisible(AppConfig.getDisplayIsha());
		
		createTimeToLeftPanel(timeLeftPanel, timeLeftTopLabel, timeLeftBottomLabel);
        gridBagConstraints.insets = new Insets(INSETS, INSETS, INSETS, INSETS);
        gridBagConstraints.gridy = index;
        mainPanel.add(timeLeftPanel, gridBagConstraints);
        index++;
        heightMain += height * 2 + 2 * INSETS + 2 * INNER_INSETS;
        /*
        JPanel panel = new JPanel();
        stopPlayingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(stopPlayingLabel);
        gridBagConstraints.insets = new Insets(INSETS, INSETS, INSETS, INSETS);
        gridBagConstraints.gridy = index;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        mainPanel.add(panel, gridBagConstraints);
        index++;
        heightMain += panel.getHeight() + 6 * INSETS;// + 2 * INNER_INSETS;
        stopPlayingLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("click");
				super.mouseClicked(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("enter");
				super.mouseEntered(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("exit");
				super.mouseExited(e);
			}
        	
		});
        */
        Dimension mainDimension = new Dimension(panelDimension.width + 2 * INSETS, heightMain);
        mainPanel.setMaximumSize(mainDimension);
        mainPanel.setMinimumSize(mainDimension);
        mainPanel.setPreferredSize(mainDimension);
        add(mainPanel, BorderLayout.CENTER);
		
		getRootPane().setPreferredSize(mainDimension);
		getRootPane().setMinimumSize(mainDimension);
		getRootPane().setMaximumSize(mainDimension);
		
		setPreferredSize(mainDimension);
        setMinimumSize(mainDimension);
        setMaximumSize(mainDimension);
		pack();
		
		if (widgetPosition.getX() == -1 || widgetPosition.getY() == -1) {
			Dimension screeSize = Toolkit.getDefaultToolkit ().getScreenSize ();
			//Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
			//widgetPosition = new Point(screeSize.width - mainDimension.width, screeSize.height - toolHeight.bottom - mainDimension.height);
			widgetPosition = new Point(screeSize.width - mainDimension.width, 0);
			
		}
		
		setLocation(widgetPosition);
		
		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				setLocation(x - xMouse, y - yMouse);
			}
			
		});
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				xMouse = e.getX();
		        yMouse = e.getY();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					AppConfig.setDisplayPositionX(e.getXOnScreen() - e.getX());
					AppConfig.setDisplayPositionY(e.getYOnScreen() - e.getY());
				} else 
					showPopupMenu(e);
			}

			private void showPopupMenu(MouseEvent event) {
				if (SwingUtilities.isRightMouseButton(event) && event.isPopupTrigger()) {
					stopPlayingJMenuItem.setEnabled(alarmPlayer.getPlayer() != null || player.getPlayer() != null);
					jPopupMenu.show(event.getComponent(), event.getX(), event.getY());
				}
			}
		});		
	}
	
	private void setSizeComponent(JComponent component, Dimension dimension) {
		component.setPreferredSize(dimension);
		component.setMaximumSize(dimension);
		component.setMinimumSize(dimension);
	}
	
	public void createPrayTimePanel(JPanel panel, JLabel nameLabel, JLabel timeLabel/*, String name, String time*/) {
		Dimension d = new Dimension(nameWidth, height);
		nameLabel.setFont(appFont);
		nameLabel.setHorizontalAlignment(SwingConstants.LEADING);
		setSizeComponent(nameLabel, d);
		
		d = new Dimension(timeWidth, height);
		timeLabel.setFont(appFont);
		timeLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		setSizeComponent(timeLabel, d);
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(INNER_INSETS, INNER_INSETS, INNER_INSETS, INNER_INSETS);
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(INNER_INSETS, INNER_INSETS, INNER_INSETS, INNER_INSETS);
        panel.add(timeLabel, gbc);
        
        d = new Dimension(nameWidth + timeWidth + 4 * INNER_INSETS, height + INNER_INSETS * 2);
        setSizeComponent(panel, d);
	}
	

	public void createTimeToLeftPanel(JPanel panel, JLabel titleTopLabel, JLabel titleBottomLabel) {
		Dimension d = new Dimension(widthPanel, height);
		titleTopLabel.setFont(appFont);
		titleTopLabel.setHorizontalAlignment(SwingConstants.CENTER);
		setSizeComponent(titleTopLabel, d);
		
		titleBottomLabel.setFont(appFont);
		titleBottomLabel.setHorizontalAlignment(SwingConstants.CENTER);
		setSizeComponent(titleBottomLabel, d);
		
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(INNER_INSETS, INNER_INSETS, 0, INNER_INSETS);
        panel.add(titleTopLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(INNER_INSETS, INNER_INSETS, INNER_INSETS, INNER_INSETS);
        panel.add(titleBottomLabel, gbc);
        //panel.setBorder(BorderFactory.createEtchedBorder(Color.white, Color.red));
        panel.setBorder(abstractBorder);
        d = new Dimension(widthPanel, height * 2 + 2 * INNER_INSETS);
        setSizeComponent(panel, d);        
	}
	
	/*TODO Swing variable*/
	private JPanel mainPanel = new JPanel();
	private JPanel fajrPanel = new JPanel(); 
	private JLabel fajrNameLabel = new JLabel(I18n.resourceBundle.getString("fajr"));
	private JLabel fajrTimeLabel = new JLabel();
	private JLabel asrNameLabel = new JLabel(I18n.resourceBundle.getString("asr"));
    private JPanel asrPanel = new JPanel();
    private JLabel asrTimeLabel = new JLabel();
    private JLabel dhuhrNameLabel = new JLabel(I18n.resourceBundle.getString("dhuhr"));
    private JPanel dhuhrPanel = new JPanel();
    private JLabel dhuhrTimeLabel = new JLabel();
    private JLabel ishaNameLabel = new JLabel(I18n.resourceBundle.getString("isha"));
    private JPanel ishaPanel = new JPanel();
    private JLabel ishaTimeLabel = new JLabel();
    private JLabel maghribNameLabel = new JLabel(I18n.resourceBundle.getString("maghrib"));
    private JPanel maghribPanel = new JPanel();
    private JLabel maghribTimeLabel = new JLabel();
    private JLabel sunriseNameLabel = new JLabel(I18n.resourceBundle.getString("sunrise"));
    private JPanel sunrisePanel = new JPanel();
    private JLabel sunriseTimeLabel = new JLabel();
    private JLabel sunsetNameLabel = new JLabel(I18n.resourceBundle.getString("sunset"));
    private JPanel sunsetPanel = new JPanel();
    private JLabel sunsetTimeLabel = new JLabel();
    
    private JLabel todayLabel = new JLabel();
    private JPanel timeLeftPanel = new JPanel();
    private JLabel timeLeftTopLabel = new JLabel();
    private JLabel timeLeftBottomLabel = new JLabel();
    private JLabel stopPlayingLabel = new JLabel();
    
    private JMenuItem stopPlayingJMenuItem;
    private MenuItem stopPlayingMenuItem;
    
    private class FlashingBorder extends Thread {
    	
    	private Time time;
    	
    	public void setTime(Time time) {
    		this.time = time;
    	}
    	
    	@Override
		public void run() {
			GregorianCalendar d = new GregorianCalendar();
	    	int seconds = 30;
	    	/*TODO Think about selection color*/
	    	Color[] color = new Color[2];
	    	color[0] = new Color(255, 51, 51);
	    	color[1] = new Color(51, 51, 255); 
	    	Border[] border = new Border[2]; 
	    	border[0] = new TextBubbleBorder(color[0], 3, 20, 0);//BorderFactory.createLineBorder(color[0]);
	    	border[1] = new TextBubbleBorder(color[1], 2, 20, 0);//BorderFactory.createLineBorder(color[1]);
	    	JPanel panel = null;
	    	JLabel nameLabel = null;
	    	JLabel timeLabel = null;
	    	if (time == Time.ASR) {
	    		panel = asrPanel;
	    		nameLabel = asrNameLabel;
		    	timeLabel = asrTimeLabel;
			}else if (time == Time.FAJR) {
				panel = fajrPanel;
				nameLabel = fajrNameLabel;
		    	timeLabel = fajrTimeLabel;
			}else if (time == Time.SUNRISE) {
				panel = sunrisePanel;
				nameLabel = sunriseNameLabel;
		    	timeLabel = sunriseTimeLabel;
			}else if (time == Time.SUNSET) {
				panel = sunsetPanel;
				nameLabel = sunsetNameLabel;
		    	timeLabel = sunsetTimeLabel;
			}else if (time == Time.DHUHR) {
				panel = dhuhrPanel;
				nameLabel = dhuhrNameLabel;
		    	timeLabel = dhuhrTimeLabel;
			}else if (time == Time.MAGHRIB) {
				panel = maghribPanel;
				nameLabel = maghribNameLabel;
		    	timeLabel = maghribTimeLabel;
			}else if (time == Time.ISHA) {
				panel = ishaPanel;
				nameLabel = asrNameLabel;
		    	timeLabel = asrTimeLabel;
			} 
	    	int i = 0;
	    	Color nameColor = nameLabel.getForeground();
	    	Color timeColor = timeLabel.getForeground();
	    	while ((d.getTimeInMillis() + seconds * 1000) > new GregorianCalendar().getTimeInMillis()) {
	    		panel.setBorder(border[i]);
	    		nameLabel.setForeground(color[i]);
	    		timeLabel.setForeground(color[i]);
	    		try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    		i++;
	    		if (i > 1) i = 0;
	    	}
	    	panel.setBorder(null);
	    	nameLabel.setForeground(nameColor);
    		timeLabel.setForeground(timeColor);
	    }
    }
    
    private class NextPrayTimeInfo extends Thread {

    	public boolean stop = false;
		StringBuilder stringBuilder = new StringBuilder();
		FlashingBorder fb = new FlashingBorder();
		private boolean alarmBeforeRun, alarmAfterRun, alarmItTimeToPrayRun;
		
		
		@Override
		public void run() {
			DayTime dayTime;
			int delta = 0, hourCurrentTime = 0, hourOldTime = 0;
			boolean changeTime = false;
			boolean prayTimeNameRename = false;
			/*TODO calculation */
			do
			{	if(!Thread.interrupted() && !stop)
				{
					calculationDate = new GregorianCalendar();
					if (nextPrayTime == null || nextPrayTime.equals(currentPrayTime)) {
						changeTime = false;
						result = prayTime.getTimes(calculationDate, location);
						for (Map.Entry<Integer, Time> time : AppConfig.mapTimes.entrySet()) {
							nextPrayTime = time.getValue();
							if (mapTimeVisible.get(nextPrayTime).booleanValue()) {
								dayTime = Util.toDayTime(result.get(nextPrayTime).doubleValue(), true);
								if (dayTime.isNan())
									continue;
								
								hourCurrentTime = dayTime.getHours();
								delta = hourCurrentTime - hourOldTime;
								if (delta < 0 || calculationDate.get(GregorianCalendar.HOUR_OF_DAY) < hourCurrentTime ||
										(calculationDate.get(GregorianCalendar.HOUR_OF_DAY) == hourCurrentTime &&
										calculationDate.get(GregorianCalendar.MINUTE) < dayTime.getMinutes())) {
									changeTime = true;
									break;
								}
								currentPrayTime = nextPrayTime;
								hourOldTime = hourCurrentTime;
							}
						}
						
						if (!changeTime) {
							if (!mapTimeVisible.get(nextPrayTime).booleanValue() 
									|| Util.toDayTime(result.get(nextPrayTime).doubleValue(), true).isNan()
									|| nextPrayTime.equals(currentPrayTime)) {
								for (Map.Entry<Integer, Time> time : AppConfig.mapTimes.entrySet()) {
									nextPrayTime = time.getValue();
									if (mapTimeVisible.get(nextPrayTime).booleanValue()) {
										calculationDate = new GregorianCalendar();
										calculationDate.add(GregorianCalendar.DAY_OF_MONTH, 1);
										result = prayTime.getTimes(calculationDate, location);
										if (!Util.toDayTime(result.get(nextPrayTime).doubleValue(), true).isNan())
											break;
									}
								}
							}
						}
						alarmBeforeRun = false;
						alarmAfterRun = false;
						alarmItTimeToPrayRun = false;
						
						fajrNameLabel.setFont(appFont);
						sunriseNameLabel.setFont(appFont);
						sunsetNameLabel.setFont(appFont);
						dhuhrNameLabel.setFont(appFont);
						maghribNameLabel.setFont(appFont);
						ishaNameLabel.setFont(appFont);	
						asrNameLabel.setFont(appFont);
						fajrTimeLabel.setFont(appFont);
						sunriseTimeLabel.setFont(appFont);
						sunsetTimeLabel.setFont(appFont);
						dhuhrTimeLabel.setFont(appFont);
						maghribTimeLabel.setFont(appFont);
						ishaTimeLabel.setFont(appFont);	
						asrTimeLabel.setFont(appFont);
						
						Font appBoldFont = appFont.deriveFont(Font.BOLD);
						if (nextPrayTime== Time.ASR) {
							asrNameLabel.setFont(appBoldFont);					
							asrTimeLabel.setFont(appBoldFont);
						}else if (nextPrayTime == Time.FAJR) {
							fajrNameLabel.setFont(appBoldFont);						
							fajrTimeLabel.setFont(appBoldFont);
						}else if (nextPrayTime == Time.SUNRISE) {
							sunriseNameLabel.setFont(appBoldFont);
							sunriseTimeLabel.setFont(appBoldFont);
						}else if (nextPrayTime == Time.SUNSET) {
							sunsetNameLabel.setFont(appBoldFont);					
							sunsetTimeLabel.setFont(appBoldFont);
						}else if (nextPrayTime == Time.DHUHR) {
							dhuhrNameLabel.setFont(appBoldFont);							
							dhuhrTimeLabel.setFont(appBoldFont);
						}else if (nextPrayTime == Time.MAGHRIB) {
							maghribNameLabel.setFont(appBoldFont);					
							maghribTimeLabel.setFont(appBoldFont);
						}else if (nextPrayTime == Time.ISHA) {
							ishaNameLabel.setFont(appBoldFont);
							ishaTimeLabel.setFont(appBoldFont);
						}
				
						stringBuilder.setLength(0);
						stringBuilder.append("<html><center><strong>");
						stringBuilder.append(I18n.resourceBundle.getString("time_left_to"));
						stringBuilder.append(" ");
						stringBuilder.append(I18n.resourceBundle.getString(nextPrayTime.toString().toLowerCase()));
						stringBuilder.append("</strong>");
						stringBuilder.append("</center></html>");
						timeLeftTopLabel.setText(stringBuilder.toString());
				
						if (!prayTimeNameRename && calculationDate.get(GregorianCalendar.DAY_OF_MONTH) != new GregorianCalendar().get(GregorianCalendar.DAY_OF_MONTH)) {
							fajrNameLabel.setText(fajrNameLabel.getText() + "*");
							sunriseNameLabel.setText(sunriseNameLabel.getText() + "*");
							dhuhrNameLabel.setText(dhuhrNameLabel.getText() + "*");
							asrNameLabel.setText(asrNameLabel.getText() + "*");
							sunsetNameLabel.setText(sunsetNameLabel.getText() + "*");
							maghribNameLabel.setText(maghribNameLabel.getText() + "*");
							ishaNameLabel.setText(ishaNameLabel.getText() + "*");
							prayTimeNameRename = true;
						} else if(prayTimeNameRename && calculationDate.get(GregorianCalendar.DAY_OF_MONTH) == new GregorianCalendar().get(GregorianCalendar.DAY_OF_MONTH)) {
							fajrNameLabel.setText(I18n.resourceBundle.getString("fajr"));
							sunriseNameLabel.setText(I18n.resourceBundle.getString("sunrise"));
							dhuhrNameLabel.setText(I18n.resourceBundle.getString("dhuhr"));
							asrNameLabel.setText(I18n.resourceBundle.getString("asr"));
							sunsetNameLabel.setText(I18n.resourceBundle.getString("sunset"));
							maghribNameLabel.setText(I18n.resourceBundle.getString("maghrib"));
							ishaNameLabel.setText(I18n.resourceBundle.getString("isha"));
							prayTimeNameRename = false;
						}
						
						if (timeFormat == AppConfig.H24) {
							fajrTimeLabel.setText(Util.toTime24(result.get(Time.FAJR)));
							sunriseTimeLabel.setText(Util.toTime24(result.get(Time.SUNRISE)));
							dhuhrTimeLabel.setText(Util.toTime24(result.get(Time.DHUHR)));
							asrTimeLabel.setText(Util.toTime24(result.get(Time.ASR)));
							sunsetTimeLabel.setText(Util.toTime24(result.get(Time.SUNSET)));
							maghribTimeLabel.setText(Util.toTime24(result.get(Time.MAGHRIB)));
							ishaTimeLabel.setText(Util.toTime24(result.get(Time.ISHA)));
						} else if (timeFormat == AppConfig.H12S) {
							fajrTimeLabel.setText(Util.toTime12(result.get(Time.FAJR), false));
							sunriseTimeLabel.setText(Util.toTime12(result.get(Time.SUNRISE), false));
							dhuhrTimeLabel.setText(Util.toTime12(result.get(Time.DHUHR), false));
							asrTimeLabel.setText(Util.toTime12(result.get(Time.ASR), false));
							sunsetTimeLabel.setText(Util.toTime12(result.get(Time.SUNSET), false));
							maghribTimeLabel.setText(Util.toTime12(result.get(Time.MAGHRIB), false));
							ishaTimeLabel.setText(Util.toTime12(result.get(Time.ISHA), false));
						} else {
							fajrTimeLabel.setText(Util.toTime12(result.get(Time.FAJR), true));
							sunriseTimeLabel.setText(Util.toTime12(result.get(Time.SUNRISE), true));
							dhuhrTimeLabel.setText(Util.toTime12(result.get(Time.DHUHR), true));
							asrTimeLabel.setText(Util.toTime12(result.get(Time.ASR), true));
							sunsetTimeLabel.setText(Util.toTime12(result.get(Time.SUNSET), true));
							maghribTimeLabel.setText(Util.toTime12(result.get(Time.MAGHRIB), true));
							ishaTimeLabel.setText(Util.toTime12(result.get(Time.ISHA), true));
						}
					}
					
					dayTime = Util.toDayTime(result.get(nextPrayTime).doubleValue(), true);
					int currentHour  = calculationDate.get(GregorianCalendar.HOUR_OF_DAY);
					int prayHour = dayTime.getHours();
					
					int hours =  (prayHour - currentHour < 0 ? 24 - currentHour + prayHour : prayHour - currentHour);
					int minutes = dayTime.getMinutes() - calculationDate.get(GregorianCalendar.MINUTE);
					
					hours = minutes < 0 ? hours - 1 : hours;
					minutes  = minutes < 0 ? minutes + 60 : minutes;
					
					stringBuilder.setLength(0);
					stringBuilder.append("<html><center>");
						
					//TODO Add check for alarming before start pray time
					if(!alarmBeforeRun && 
							mapAlarmBefore.get(nextPrayTime) != null 
							&& mapAlarmBefore.get(nextPrayTime).intValue() != 0 
							&& hours == 0 && minutes <= mapAlarmBefore.get(nextPrayTime).intValue()
							&& minutes >= mapAlarmBefore.get(nextPrayTime).intValue() - 5) {
						alarmBeforeRun = true;
						alarmPlayer.play(-1);
						if (showBalonTip) {
							showBaloonTip(I18n.resourceBundle.getString("application_name"), I18n.resourceBundle.getString("alarm_before_note") + " " + mapAlarmBefore.get(nextPrayTime).intValue() + " " +I18n.resourceBundle.getString("minutes"), MessageType.NONE);
						}
						if (showFlashTrayIcon) {
							flashTrayIcon.start();
						}
					}
					
					//TODO Add check for alarming after start pray time
					if (!alarmAfterRun 
							&& mapAlarmAfter.get(currentPrayTime) != null 
							&& mapAlarmAfter.get(currentPrayTime).intValue() != 0 
							&& calculationDate.get(GregorianCalendar.HOUR_OF_DAY) - Util.toDayTime(result.get(currentPrayTime).doubleValue(), true).getHours() == 0 
							&& calculationDate.get(GregorianCalendar.MINUTE) >= mapAlarmAfter.get(currentPrayTime).intValue()
							&& calculationDate.get(GregorianCalendar.MINUTE) <= mapAlarmBefore.get(currentPrayTime).intValue() + 5) {
						alarmAfterRun = true;
						alarmPlayer.play(-1);
						if (showBalonTip) {
							showBaloonTip(I18n.resourceBundle.getString("application_name"), I18n.resourceBundle.getString("alarm_after_note") + " " + mapAlarmAfter.get(currentPrayTime).intValue() + " " +I18n.resourceBundle.getString("minutes"), MessageType.NONE);
						}
						if (showFlashTrayIcon) {
							flashTrayIcon.start();
						}
					}

					//TODO It's time to pray
					if (!changeTime || !nextPrayTime.equals(currentPrayTime)) {
						if (alarmItTimeToPrayRun || hours <= 0 && minutes <=0)
						{
							alarmItTimeToPrayRun = true;
							stringBuilder.append(I18n.resourceBundle.getString("time_to_pray"));
							player.setPath(mapSound.get(nextPrayTime));
							player.play(-1);
							if (showBalonTip) {
								showBaloonTip(I18n.resourceBundle.getString("application_name"), I18n.resourceBundle.getString("alarm_time_topray_note"), MessageType.NONE);
							}
							if (showFlashTrayIcon) {
								flashTrayIcon.start();
							}
							fb.setTime(nextPrayTime);
							fb.start();
							currentPrayTime = nextPrayTime;
						}
						if (hours != 0) {
							stringBuilder.append(hours);
							stringBuilder.append(" ");
							stringBuilder.append(I18n.resourceBundle.getString("hours"));
							stringBuilder.append(" ");
						}
						if (minutes != 0) {
							stringBuilder.append(minutes);
							stringBuilder.append(" ");
							stringBuilder.append(I18n.resourceBundle.getString("minutes"));
						}
						stringBuilder.append("</center></html>");
						timeLeftBottomLabel.setText(stringBuilder.toString());
					}
					
					try {
						/*Correct for second switch*/
						stopPlayingMenuItem.setEnabled(player.getPlayer() != null || alarmPlayer.getPlayer() != null);						
						sleep(60*1000 - (new GregorianCalendar().get(GregorianCalendar.SECOND)) * 1000 + 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				else
				{
					break;
				}
			}
			while(true);
		}
		
	}
    
    /*TODO makeJPopupMenu*/
    private void makeJPopupMenu() {
    	JMenuItem menuItem;
    	JCheckBoxMenuItem checkBoxMenuItem;
    	jPopupMenu = new JPopupMenu("popup Menu");
    	checkBoxMenuItem = new JCheckBoxMenuItem(I18n.resourceBundle.getString("always_on_top"), alwaysOnTop);
    	checkBoxMenuItem.setActionCommand("TEST");
    	checkBoxMenuItem.addItemListener(alwaysOnTopItemListener);
    	jPopupMenu.add(checkBoxMenuItem);
    	checkBoxMenuItem = new JCheckBoxMenuItem(I18n.resourceBundle.getString("launch_on_startup"), launchOnStartup);
    	checkBoxMenuItem.setActionCommand("STARTUP");
    	checkBoxMenuItem.addItemListener(alwaysOnTopItemListener);
    	jPopupMenu.add(checkBoxMenuItem);
    	menuItem = new JMenuItem(I18n.resourceBundle.getString("stop_playing"));
    	menuItem.addActionListener(stopPlayingActionListener);
    	stopPlayingJMenuItem = menuItem;
    	jPopupMenu.add(menuItem);
    	jPopupMenu.addSeparator();
    	menuItem = new JMenuItem(I18n.resourceBundle.getString("monthly_timetable"));
    	menuItem.addActionListener(showMonthlyTimetableActionListener);
    	jPopupMenu.add(menuItem);
    	jPopupMenu.addSeparator();
    	menuItem = new JMenuItem(I18n.resourceBundle.getString("settings"));
    	menuItem.addActionListener(showOptionDialogActionListener);
    	jPopupMenu.add(menuItem);
    	menuItem = new JMenuItem(I18n.resourceBundle.getString("about"));
    	menuItem.addActionListener(showAboutDialogActionListener);
    	jPopupMenu.addSeparator();
    	jPopupMenu.add(menuItem);
    	menuItem = new JMenuItem(I18n.resourceBundle.getString("exit"));
    	menuItem.addActionListener(windowExitActionListener);
    	jPopupMenu.addSeparator();
    	jPopupMenu.add(menuItem);
    }

    /*TODO makePopupMenu*/
    private void makePopupMenu() {
    	MenuItem menuItem;
    	CheckboxMenuItem checkBoxMenuItem;
    	popupMenu = new PopupMenu();
    	checkBoxMenuItem = new CheckboxMenuItem(I18n.resourceBundle.getString("always_on_top"), alwaysOnTop);
    	checkBoxMenuItem.setActionCommand("TEST");
    	checkBoxMenuItem.addItemListener(alwaysOnTopItemListener);
    	popupMenu.add(checkBoxMenuItem);
    	checkBoxMenuItem = new CheckboxMenuItem(I18n.resourceBundle.getString("launch_on_startup"), launchOnStartup);
    	checkBoxMenuItem.setActionCommand("STARTUP");
    	checkBoxMenuItem.addItemListener(alwaysOnTopItemListener );
    	popupMenu.add(checkBoxMenuItem);
    	menuItem = new MenuItem(I18n.resourceBundle.getString("stop_playing"));
    	menuItem.addActionListener(stopPlayingActionListener);
    	stopPlayingMenuItem = menuItem;
    	popupMenu.add(menuItem);
    	popupMenu.addSeparator();
    	menuItem = new MenuItem(I18n.resourceBundle.getString("monthly_timetable"));
    	menuItem.addActionListener(showMonthlyTimetableActionListener);
    	popupMenu.add(menuItem);
    	popupMenu.addSeparator();
    	
    	menuItem = new MenuItem(I18n.resourceBundle.getString("settings"));
    	menuItem.addActionListener(showOptionDialogActionListener);
    	popupMenu.add(menuItem);
    	
    	menuItem = new MenuItem(I18n.resourceBundle.getString("about"));
    	menuItem.addActionListener(showAboutDialogActionListener);
    	popupMenu.addSeparator();
    	popupMenu.add(menuItem);
    	
    	menuItem = new MenuItem(I18n.resourceBundle.getString("exit"));
    	menuItem.addActionListener(windowExitActionListener);
    	popupMenu.addSeparator();
    	popupMenu.add(menuItem);
    	
    	
    }
    
    ItemListener alwaysOnTopItemListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			CheckboxMenuItem cmi = null;
			JCheckBoxMenuItem jcmi = null;
			String actionCommand = "";
			try {
				cmi = (CheckboxMenuItem) e.getSource();
				actionCommand = cmi.getActionCommand();
			} catch (Exception ex) {
				jcmi = (JCheckBoxMenuItem) e.getSource();
				actionCommand = jcmi.getActionCommand();
			}
			
			if (actionCommand.equals("TEST")) {
				alwaysOnTop = e.getStateChange() == ItemEvent.SELECTED;
				setAlwaysOnTop(alwaysOnTop);
				AppConfig.setDisplayAlwaysOnTop(alwaysOnTop);
				AppConfig.savePreferenses();
			} else {
				launchOnStartup = e.getStateChange() == ItemEvent.SELECTED;
				AppConfig.setDisplayLaunchOnStartup(launchOnStartup);
				OptionsDialog.applicationToStartup(launchOnStartup);
				AppConfig.savePreferenses();
			}
		}
	};
	
	ActionListener showMonthlyTimetableActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(new Runnable() {
				
				public void run() {
					MonthlyTimeTable monthlyTimeTable = new MonthlyTimeTable();
					monthlyTimeTable.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
					monthlyTimeTable.setVisible(true);
				}
			});
		}
	};

	ActionListener stopPlayingActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (alarmPlayer.getPlayer() != null)
				alarmPlayer.pause();
			if (player.getPlayer() != null)
				player.pause();
		}
	};
	
    ActionListener showOptionDialogActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					OptionsDialog dialog = new OptionsDialog();
					dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
					dialog.setVisible(true);
					if (dialog.getStatus() == JOptionPane.OK_OPTION) {
						npti.stop = true;
						dispose();
						reinitializeComponents();
						setVisible(true);
					}
				}
			});
		}
	};
	
	ActionListener showAboutDialogActionListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					AboutDialog dialog = new AboutDialog(appImage);
					dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
					dialog.setVisible(true);
				}
			});
		}
	};
	
	ActionListener windowExitActionListener = new ActionListener() {
		
		public void actionPerformed(ActionEvent e) {
			//TODO Need catch window another way!!! Because it can be not Focused!!
			/*Window window = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
			if (window != null){
				WindowEvent windowClosing = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
				window.dispatchEvent(windowClosing);
			} else {*/
				Window[] windows = Window.getOwnerlessWindows();
				for (Window window : windows) {
					WindowEvent windowClosing = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
					window.dispatchEvent(windowClosing);
				}
			//}
		}
	}; 
	
	/*TODO Thread for flashing tray icon: simple change images*/
	Thread flashTrayIcon = new Thread(new Runnable() {
		
		public void run() {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long startTime = new GregorianCalendar().getTimeInMillis();
			while (new GregorianCalendar().getTimeInMillis() < startTime + 30000) {
				if(trayIcon.getImage().equals(appImage)) {
					trayIcon.setImage(appImageAlternated);
				} else {
					trayIcon.setImage(appImage);
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(trayIcon.getImage().equals(appImageAlternated)) 
				trayIcon.setImage(appImage);
			
		}
	});
	
    public static Image createImage(String path, String description) {
        URL imageURL = JWidget.class.getResource(path);
        
        if (imageURL == null) {
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

}

