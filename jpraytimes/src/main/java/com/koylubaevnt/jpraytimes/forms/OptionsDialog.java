package com.koylubaevnt.jpraytimes.forms;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.koylubaevnt.jpraytimes.JWidget;
import com.koylubaevnt.jpraytimes.audio.CustomPlayer;
import com.koylubaevnt.jpraytimes.forms.components.JTextFieldPlaceholder;
import com.koylubaevnt.jpraytimes.i18n.I18n;
import com.koylubaevnt.jpraytimes.preferences.AppConfig;
import com.koylubaevnt.jpraytimes.preferences.AppConfig.SoundEnum;
import com.koylubaevnt.jpraytimes.tools.WinRegistry;

import id.web.michsan.praytimes.Configuration;
import id.web.michsan.praytimes.Location;
import id.web.michsan.praytimes.Method;
import id.web.michsan.praytimes.Method.HighLatMethod;
import id.web.michsan.praytimes.PrayTimes.Time;

/**
 * @author KojlubaevNT
 */
public class OptionsDialog extends JDialog {

	private static final long serialVersionUID = 1266270237356700254L;
	
	public OptionsDialog() {
		fillData();
		
		loadPlayStopButtonIcons();
		
		initComponents();
		
		setLocationByPlatform(true);
		setModal(true);
		setAlwaysOnTop(true);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowDeactivated(WindowEvent e) {
				if (soundPlayer.getPlayer() != null)
					soundPlayer.pause();
				if (alertsPlayer.getPlayer() != null)
					alertsPlayer.pause();
				
			}
		
		});
		
	}
	
	/**TODO need add some action which can lock[unlock] button "accept" depends on data change
	 * But now hide this button!!!*/
	
	private void initComponents() {
		
		setTitle(I18n.resourceBundle.getString("settings"));
		
        /**
		 * 
		 * Tab Panel: General
		 * 
		 */
        subFontPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("font")));
		GroupLayout subFontLayout = new GroupLayout(subFontPanel);
		subFontPanel.setLayout(subFontLayout);
		subFontLayout.setAutoCreateContainerGaps(true);
		subFontLayout.setAutoCreateGaps(true);
		subFontLayout.setHorizontalGroup(subFontLayout.createSequentialGroup()
				.addGroup(subFontLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(fontNameLabel)
	                    .addComponent(fontSizeLabel)
						.addComponent(fontStyleLabel))
                .addGroup(subFontLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(fontNameComboBox)
                    .addComponent(fontSizeComboBox)
                    .addGroup(subFontLayout.createSequentialGroup()
                    		.addComponent(fontStyleBoldCheckBox)
                    		.addComponent(fontStyleItalicCheckBox)))
	        );
		
		subFontLayout.setVerticalGroup(subFontLayout.createSequentialGroup()
				.addGroup(subFontLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(fontNameLabel)
	                    .addComponent(fontNameComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	                    )
	            .addGroup(subFontLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                    .addComponent(fontSizeLabel)
	                    .addComponent(fontSizeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
	            .addGroup(subFontLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
	                    .addComponent(fontStyleLabel)
	                    .addComponent(fontStyleBoldCheckBox)
	                    .addComponent(fontStyleItalicCheckBox))
	        );
		
		SpinnerModel model = new SpinnerNumberModel(AppConfig.getDisplayOpacity(), 50, 100, 1);
		opacitySpiner.setModel(model);
		
		subDisplayPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("display")));
		GroupLayout subDisplayLayout = new GroupLayout(subDisplayPanel);
		subDisplayPanel.setLayout(subDisplayLayout);
		subDisplayLayout.setAutoCreateContainerGaps(true);
		subDisplayLayout.setAutoCreateGaps(true);
		subDisplayLayout.setHorizontalGroup(subDisplayLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(displayFajrCheckBox)
				.addComponent(displaySunriseCheckBox)
				.addComponent(displayDhuhrCheckBox)
				.addComponent(displayAsrCheckBox)
				.addComponent(displaySunsetCheckBox)
				.addComponent(displayMaghribCheckBox)
				.addComponent(displayIshaCheckBox)
				.addGroup(subDisplayLayout.createSequentialGroup()
						.addComponent(opacityLabel)
						.addComponent(opacitySpiner))
				);
		subDisplayLayout.setVerticalGroup(subDisplayLayout.createSequentialGroup()
				.addComponent(displayFajrCheckBox)
				.addComponent(displaySunriseCheckBox)
				.addComponent(displayDhuhrCheckBox)
				.addComponent(displayAsrCheckBox)
				.addComponent(displaySunsetCheckBox)
				.addComponent(displayMaghribCheckBox)
				.addComponent(displayIshaCheckBox)
				.addGroup(subDisplayLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(opacityLabel)
						.addComponent(opacitySpiner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		
		timeFormatPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("time_format")));
		timeFormatButtonGroup.add(timeFormat24RadioButton);
		timeFormatButtonGroup.add(timeFormat12RadioButton);
		timeFormatButtonGroup.add(timeFormat12AMPMRadioButton);
		GroupLayout timeFormatGroupLayout = new GroupLayout(timeFormatPanel);
		timeFormatPanel.setLayout(timeFormatGroupLayout);
		timeFormatGroupLayout.setHorizontalGroup(timeFormatGroupLayout.createSequentialGroup()
				.addGroup(timeFormatGroupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(timeFormat24RadioButton)
						.addComponent(timeFormat12RadioButton)
						.addComponent(timeFormat12AMPMRadioButton)));
		
		timeFormatGroupLayout.setVerticalGroup(timeFormatGroupLayout.createSequentialGroup()
				.addComponent(timeFormat24RadioButton)
				.addComponent(timeFormat12RadioButton)
				.addComponent(timeFormat12AMPMRadioButton));
		
		languageLoadButton.setVisible(false);
		geneneralPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("general")));
		GroupLayout generalLayout = new GroupLayout(geneneralPanel);
		geneneralPanel.setLayout(generalLayout);
		generalLayout.setAutoCreateContainerGaps(true);
		generalLayout.setAutoCreateGaps(true);
        generalLayout.setHorizontalGroup(generalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
        		.addGroup(generalLayout.createSequentialGroup()
        				.addComponent(languageLabel)
        				.addComponent(languageComboBox)
        				.addComponent(languageLoadButton))
        		.addComponent(alwaysOnTopCheckBox)
        		.addComponent(lanchOnStartupCheckBox)
        		.addComponent(subFontPanel)
        		.addComponent(subDisplayPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        		.addComponent(timeFormatPanel));
		
		generalLayout.setVerticalGroup(generalLayout.createSequentialGroup().
				addGroup(generalLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(languageLabel)
						.addComponent(languageComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(languageLoadButton))
        		.addComponent(alwaysOnTopCheckBox)
        		.addComponent(lanchOnStartupCheckBox)
        		.addComponent(subFontPanel)
        		.addComponent(subDisplayPanel)
        		.addComponent(timeFormatPanel));

		/**TODO add language load algorithm. If it's possible... Try read about ResourceBundle remote loading...*/
		languageLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(languageLoadButton, I18n.resourceBundle.getString("unsupported_operation"));
				
			}
		});
		
		/**
		 * 
		 * Tab Panel: Calculating
		 * 
		 */
		locationPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("location")));
		selectLocationButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					
					public void run() {
						SelectLocationDialog dialog = new SelectLocationDialog();
						dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						dialog.setVisible(true);
						Location l = dialog.getSettings();
						if (l != null) {
							latitudeTextField.setText(String.valueOf(l.getLat()));
							longitudeTextField.setText(String.valueOf(l.getLng()));
							elevationTextField.setText(String.valueOf(l.getElv()));
						}
					}
				});
			}
		});
		/*
		 * 	Location section
		 */
		GroupLayout locationLayout = new GroupLayout(locationPanel);
		locationPanel.setLayout(locationLayout);
		locationLayout.setAutoCreateContainerGaps(true);
		locationLayout.setAutoCreateGaps(true);
		locationLayout.setHorizontalGroup(locationLayout.createSequentialGroup()
				.addGroup(
						locationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(locationTextField)
						.addGroup(locationLayout.createSequentialGroup()
								.addGroup(locationLayout.createSequentialGroup()
										.addComponent(latitudeLabel)
										.addComponent(latitudeTextField))
								.addGroup(locationLayout.createSequentialGroup()
										.addComponent(longitudeLabel)
										.addComponent(longitudeTextField))
								.addGroup(locationLayout.createSequentialGroup()
										.addComponent(elevationLabel)
										.addComponent(elevationTextField))
						))
				.addComponent(selectLocationButton));
		
		locationLayout.setVerticalGroup(
				locationLayout.createSequentialGroup()
				.addGroup(locationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(locationTextField)
						.addComponent(selectLocationButton))
						
				.addGroup(locationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(latitudeLabel)
								.addComponent(latitudeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(longitudeLabel)
								.addComponent(longitudeTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(elevationLabel)
								.addComponent(elevationTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		
		/*
		 * 	TimeZone section
		 */
		timeZonePanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("time_zone")));
		timeZoneButtonGroup.add(timeZoneAutoRadioButton);
		timeZoneButtonGroup.add(timeZoneManualRadioButton);
		
		GroupLayout timeZoneLayout = new GroupLayout(timeZonePanel);
		timeZonePanel.setLayout(timeZoneLayout);
		timeZoneLayout.setAutoCreateContainerGaps(true);
		timeZoneLayout.setAutoCreateGaps(true);
		timeZoneLayout.setHorizontalGroup(timeZoneLayout.createSequentialGroup()
				.addGroup(timeZoneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(timeZoneAutoRadioButton)
						.addComponent(timeZoneManualRadioButton))
				.addGroup(timeZoneLayout.createSequentialGroup()
						.addComponent(timeZoneComboBox)
						.addComponent(timeZoneManualInfoLabel)));
		
		timeZoneLayout.setVerticalGroup(timeZoneLayout.createSequentialGroup()
				.addGroup(timeZoneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(timeZoneAutoRadioButton)
						)
				.addGroup(timeZoneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(timeZoneManualRadioButton)
						.addComponent(timeZoneComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(timeZoneManualInfoLabel)));
		
		/*
		 * Basic Method settings section
		 */
		calculationMethodPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("method_calculation")));
		basicCalculationMethodPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("basic")));
		basicCalculationMethodMaghribButtonGroup.add(basicCalculationMethodMaghribMinutesRadioButton);
		basicCalculationMethodMaghribButtonGroup.add(basicCalculationMethodMaghribDegreesRadioButton);
		basicCalculationMethodIshaButtonGroup.add(basicCalculationMethodIshaMinutesRadioButton);
		basicCalculationMethodIshaButtonGroup.add(basicCalculationMethodIshaDegreesRadioButton);
		GroupLayout basicCalculationMethodLayout = new GroupLayout(basicCalculationMethodPanel);
		basicCalculationMethodPanel.setLayout(basicCalculationMethodLayout);
		basicCalculationMethodLayout.setAutoCreateContainerGaps(true);
		basicCalculationMethodLayout.setAutoCreateGaps(true);
		basicCalculationMethodLayout.setHorizontalGroup(basicCalculationMethodLayout.createSequentialGroup()
				.addGroup(basicCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(basicCalculationMethodLabel)
						.addComponent(basicCalculationMethodFajrLabel)
						.addComponent(basicCalculationMethodMaghribLabel)
						.addComponent(basicCalculationMethodIshaLabel))
				.addGroup(basicCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(basicCalculationMethodComboBox)
						.addGroup(basicCalculationMethodLayout.createSequentialGroup()
								.addGroup(basicCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(basicCalculationMethodFajrTextField)
										.addComponent(basicCalculationMethodMaghribTextField)
										.addComponent(basicCalculationMethodIshaTextField))
								.addGroup(basicCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addComponent(basicCalculationMethodFajrDegreesRadioButton)
											.addComponent(basicCalculationMethodMaghribDegreesRadioButton)
											.addComponent(basicCalculationMethodIshaDegreesRadioButton))
									.addGroup(basicCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
											.addComponent(basicCalculationMethodMaghribMinutesRadioButton)
											.addComponent(basicCalculationMethodIshaMinutesRadioButton)))));
		basicCalculationMethodLayout.setVerticalGroup(basicCalculationMethodLayout.createSequentialGroup()
				.addGroup(basicCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(basicCalculationMethodLabel)
						.addComponent(basicCalculationMethodComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(basicCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(basicCalculationMethodFajrLabel)
						.addComponent(basicCalculationMethodFajrTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(basicCalculationMethodFajrDegreesRadioButton))
				.addGroup(basicCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(basicCalculationMethodMaghribLabel)
						.addComponent(basicCalculationMethodMaghribTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(basicCalculationMethodMaghribDegreesRadioButton)
						.addComponent(basicCalculationMethodMaghribMinutesRadioButton))
				.addGroup(basicCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(basicCalculationMethodIshaLabel)
						.addComponent(basicCalculationMethodIshaTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(basicCalculationMethodIshaDegreesRadioButton)
						.addComponent(basicCalculationMethodIshaMinutesRadioButton)));
		
		/*
		 * Other Method settings section
		 */
		
		otherCalculationMethodPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("other")));
		GroupLayout otherCalculationMethodLayout = new GroupLayout(otherCalculationMethodPanel);
		otherCalculationMethodPanel.setLayout(otherCalculationMethodLayout);
		otherCalculationMethodLayout.setAutoCreateContainerGaps(true);
		otherCalculationMethodLayout.setAutoCreateGaps(true);
		otherCalculationMethodLayout.setHorizontalGroup(otherCalculationMethodLayout.createSequentialGroup()
				.addGroup(otherCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(otherCalculationMethodDhuhrLabel)
						.addComponent(otherCalculationMethodAsrLabel)
						.addComponent(otherCalculationMethodAdjustHLLabel))
				.addGroup(otherCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(otherCalculationMethodLayout.createSequentialGroup()
								.addComponent(otherCalculationMethodDhuhrTextField)
								.addComponent(otherCalculationMethodDhuhrExtraLabel))
						.addComponent(otherCalculationMethodAsrComboBox)
						.addComponent(otherCalculationMethodAdjustHLComboBox)));
		otherCalculationMethodLayout.setVerticalGroup(otherCalculationMethodLayout.createSequentialGroup()
				.addGroup(otherCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(otherCalculationMethodDhuhrLabel)
						.addComponent(otherCalculationMethodDhuhrTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(otherCalculationMethodDhuhrExtraLabel))
				.addGroup(otherCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(otherCalculationMethodAsrLabel)
						.addComponent(otherCalculationMethodAsrComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(otherCalculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(otherCalculationMethodAdjustHLLabel)
						.addComponent(otherCalculationMethodAdjustHLComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		
		GroupLayout calculationMethodLayout = new GroupLayout(calculationMethodPanel);
		calculationMethodPanel.setLayout(calculationMethodLayout);
		calculationMethodLayout.setAutoCreateContainerGaps(true);
		calculationMethodLayout.setAutoCreateGaps(true);
		calculationMethodLayout.setHorizontalGroup(
				calculationMethodLayout.createSequentialGroup()
				.addGroup(calculationMethodLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(basicCalculationMethodPanel)
						.addComponent(otherCalculationMethodPanel)));
		calculationMethodLayout.setVerticalGroup(
				calculationMethodLayout.createSequentialGroup()
				.addComponent(basicCalculationMethodPanel)
				.addComponent(otherCalculationMethodPanel));
		
		
		GroupLayout calculationLayout = new GroupLayout(calculationPanel);
		calculationPanel.setLayout(calculationLayout);
		calculationLayout.setAutoCreateContainerGaps(true);
		calculationLayout.setAutoCreateGaps(true);
		calculationLayout.setHorizontalGroup(calculationLayout.createSequentialGroup()
				.addGroup(
					calculationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(locationPanel)
					.addComponent(timeZonePanel)
					.addComponent(calculationMethodPanel)));
		calculationLayout.setVerticalGroup(calculationLayout.createSequentialGroup()
				.addComponent(locationPanel)
				.addComponent(timeZonePanel)
				.addComponent(calculationMethodPanel));
		
		/**
		 * 
		 * Tab Panel: Alerts & Sounds
		 * 
		 * */
		alertsPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("alerts")));
		GroupLayout subAlertsLayout = new GroupLayout(subAlertsPanel);
		subAlertsPanel.setLayout(subAlertsLayout);
		subAlertsLayout.setAutoCreateContainerGaps(true);
		subAlertsLayout.setAutoCreateGaps(true);
		subAlertsLayout.setHorizontalGroup(subAlertsLayout.createSequentialGroup()
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsFajrLabel)
						.addComponent(alertsSunriseLabel)
						.addComponent(alertsDhuhrLabel)
						.addComponent(alertsAsrLabel)
						.addComponent(alertsSunsetLabel)
						.addComponent(alertsMaghribLabel)
						.addComponent(alertsIshaLabel))
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsBeforeLabel)
						.addComponent(alertsFajrBeforeComboBox)
						.addComponent(alertsSunriseBeforeComboBox)
						.addComponent(alertsDhuhrBeforeComboBox)
						.addComponent(alertsAsrBeforeComboBox)
						.addComponent(alertsSunsetBeforeComboBox)
						.addComponent(alertsMaghribBeforeComboBox)
						.addComponent(alertsIshaBeforeComboBox))
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsAfterLabel)
						.addComponent(alertsFajrAfterComboBox)
						.addComponent(alertsSunriseAfterComboBox)
						.addComponent(alertsDhuhrAfterComboBox)
						.addComponent(alertsAsrAfterComboBox)
						.addComponent(alertsSunsetAfterComboBox)
						.addComponent(alertsMaghribAfterComboBox)
						.addComponent(alertsIshaAfterComboBox)));
		subAlertsLayout.setVerticalGroup(subAlertsLayout.createSequentialGroup()
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsBeforeLabel)
						.addComponent(alertsAfterLabel))
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsFajrLabel)
						.addComponent(alertsFajrBeforeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(alertsFajrAfterComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsSunriseLabel)
						.addComponent(alertsSunriseBeforeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(alertsSunriseAfterComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsDhuhrLabel)
						.addComponent(alertsDhuhrBeforeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(alertsDhuhrAfterComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsAsrLabel)
						.addComponent(alertsAsrBeforeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(alertsAsrAfterComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsSunsetLabel)
						.addComponent(alertsSunsetBeforeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(alertsSunsetAfterComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsMaghribLabel)
						.addComponent(alertsMaghribBeforeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(alertsMaghribAfterComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(subAlertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsIshaLabel)
						.addComponent(alertsIshaBeforeComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(alertsIshaAfterComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		
		alertsAudioTextField.setEditable(false);
		alertsAudioPlayButton = new JButton(playImageIcon);
		alertsAudioStopButton = new JButton(stopImageIcon);
		setSizeButton(alertsAudioPlayButton, buttonDimension);
		setSizeButton(alertsAudioStopButton, buttonDimension);
		alertsAudioTextField.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
					File file;
					JTextField textField = (JTextField)e.getSource();
					String title;
					FileFilter fileFilter = new FileNameExtensionFilter(I18n.resourceBundle.getString("mp3"), new String[] {"mp3"});
					title = I18n.resourceBundle.getString("file_mp3_for_alarm");
					file = openSelectSingleFileDialog(textField, title, fileFilter);
					if (file != null) { 
						textField.setText(file.getName());
						try {
							alertsAlarmFileName = file.toURI().toURL().toString();
						} catch (MalformedURLException e1) {
						}
					}
				}
			}
			
		});
		
		alertsAudioPlayButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				URL fileToPlay = null;
				try {
					fileToPlay = new URL(alertsAlarmFileName);
				} catch (MalformedURLException exeption) {
				}
				if (alertsPlayer.getPath() != null && !alertsPlayer.getPath().toString().equals(fileToPlay.toString())) {
					alertsPlayer.pause();
					alertsPlayer.setPath(fileToPlay);
					alertsPlayer.play(-1);
				} else if (fileToPlay != null && alertsPlayer.getPlayer() == null) {
					alertsPlayer.setPath(fileToPlay);
					alertsPlayer.play(-1);
					}
					
				}
			});
		
		alertsAudioStopButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (alertsPlayer.getPath() != null ) {
					alertsPlayer.pause();
				}
			}
		});
		
		alertsAudioDefaultButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				URL fileToPlay = null;
				String fileName = "";
				try {
					fileToPlay = new URL(AppConfig.getDefaultAlertsAlarmAudio());
					try {
						fileName = new File(fileToPlay.toURI()).getName();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				} catch (MalformedURLException exeption) {
				}
				alertsAlarmFileName = fileToPlay.toString();
				alertsAudioTextField.setText(fileName);
			}
		});

		subAlertsAudioPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("audio")));
		GroupLayout subAlertsAudioLayout = new GroupLayout(subAlertsAudioPanel);
		subAlertsAudioPanel.setLayout(subAlertsAudioLayout);
		subAlertsAudioLayout.setAutoCreateContainerGaps(true);
		subAlertsAudioLayout.setAutoCreateGaps(true);
		subAlertsAudioLayout.setHorizontalGroup(subAlertsAudioLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(subAlertsAudioLayout.createSequentialGroup()
						.addComponent(alertsAudioLabel)
						.addComponent(alertsAudioTextField))
				.addGroup(GroupLayout.Alignment.TRAILING, subAlertsAudioLayout.createSequentialGroup()
						.addComponent(alertsAudioPlayButton)
						.addComponent(alertsAudioStopButton))
					.addComponent(alertsAudioDefaultButton, GroupLayout.Alignment.TRAILING));
		subAlertsAudioLayout.setVerticalGroup(subAlertsAudioLayout.createSequentialGroup()
				.addGroup(subAlertsAudioLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(alertsAudioLabel)
						.addComponent(alertsAudioTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(subAlertsAudioLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(alertsAudioPlayButton)
						.addComponent(alertsAudioStopButton))
				.addComponent(alertsAudioDefaultButton));
		
		subAlertsVisualEffectsPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("visual_effects")));
		GroupLayout subAlertsVisualEffectsLayout = new GroupLayout(subAlertsVisualEffectsPanel);
		subAlertsVisualEffectsPanel.setLayout(subAlertsVisualEffectsLayout);
		subAlertsVisualEffectsLayout .setAutoCreateContainerGaps(true);
		subAlertsVisualEffectsLayout.setAutoCreateGaps(true);
		subAlertsVisualEffectsLayout.setHorizontalGroup(subAlertsVisualEffectsLayout.createSequentialGroup()
				.addGroup(subAlertsVisualEffectsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(displayBaloonTipsCheckBox)
						.addComponent(displayMainScreenCheckBox)
						.addComponent(displayFlashTrayIconCheckBox)));
		
		subAlertsVisualEffectsLayout.setVerticalGroup(subAlertsVisualEffectsLayout.createSequentialGroup()
				.addComponent(displayBaloonTipsCheckBox)
				.addComponent(displayMainScreenCheckBox)
				.addComponent(displayFlashTrayIconCheckBox));
		
		soundsPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("sounds")));
		
		subSoundsFajrPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("fajr")));

		ActionListener playActionListener = new ActionListener() {
			 
			public void actionPerformed(ActionEvent e) {
				URL fileToPlay = null;
				SoundEnum currentSoundEnum = SoundEnum.valueOf(e.getActionCommand());
				try {
					fileToPlay = new URL(AppConfig.mapSoundFiles.get(currentSoundEnum));
				} catch (MalformedURLException exeption) {
				}
				if (fileToPlay != null) {
					if (soundPlayer.getPath() != null && soundPlayer.getSoundEnum() != null) {
						if (soundPlayer.getSoundEnum().equals(currentSoundEnum)) {
							if (soundPlayer.getPlayer() == null) {
								soundPlayer.play(-1);
							}
						} else {
							soundPlayer.pause();
							soundPlayer.setPath(fileToPlay);
							soundPlayer.setSoundEnum(currentSoundEnum);
							soundPlayer.play(-1);
						}
					} else {
						soundPlayer.setPath(fileToPlay);
						soundPlayer.setSoundEnum(currentSoundEnum);
						soundPlayer.play(-1);
					}
				}
			}
		};
		
		ActionListener stopActionListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				SoundEnum currentSoundEnum = SoundEnum.valueOf(e.getActionCommand());
				if (soundPlayer.getPath() != null && soundPlayer.getSoundEnum().equals(currentSoundEnum))
					soundPlayer.pause();
			}
		};
		
		ActionListener defaultSoundActionListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				URL fileToPlay = null;
				String fileName = "";
				SoundEnum soundEnum = SoundEnum.valueOf(e.getActionCommand());
				try {
					fileToPlay = new URL(AppConfig.mapDefaultSoundFiles.get(soundEnum));
					try {
						fileName = new File(fileToPlay.toURI()).getName();
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					}
				} catch (MalformedURLException exeption) {
				}
				if (!AppConfig.mapSoundFiles.get(soundEnum).equals(fileToPlay.toString())) {
					AppConfig.mapSoundFiles.put(soundEnum, fileToPlay.toString());
					if (soundEnum.equals(SoundEnum.PLAY_FAJR))
						azanFajrSoundTextField.setText(fileName);
					else if (soundEnum.equals(SoundEnum.PLAY_DHUHR))
						azanDhuhrSoundTextField.setText(fileName);
					else if (soundEnum.equals(SoundEnum.PLAY_ASR))
						azanAsrSoundTextField.setText(fileName);
					else if (soundEnum.equals(SoundEnum.PLAY_MAGHRIB))
						azanMaghribSoundTextField.setText(fileName);
					else if (soundEnum.equals(SoundEnum.PLAY_ISHA))
						azanIshaSoundTextField.setText(fileName);
					else if (soundEnum.equals(SoundEnum.PLAY_STARTUP))
						startupSoundTextField.setText(fileName);
				}
			}
		};
		MouseListener textFieldSoundDoubleClickMouseListener = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
					File file;
					JTextField textField = (JTextField)e.getSource();
					String title;
					FileFilter fileFilter = new FileNameExtensionFilter(I18n.resourceBundle.getString("mp3"), new String[] {"mp3"});
					title = I18n.resourceBundle.getString("file_mp3_for_" + textField.getName().toLowerCase());
					file = openSelectSingleFileDialog(textField, title, fileFilter);
					if (file != null) { 
						textField.setText(file.getName());
						String fullFileName = "";
						try {
							fullFileName = file.toURI().toURL().toString();
						} catch (MalformedURLException e1) {
						}
						AppConfig.mapSoundFiles.put(SoundEnum.valueOf(textField.getName()), fullFileName);
					}
				}
			}
			
		};

		azanFajrSoundPlayButton = new JButton(playImageIcon);
		azanFajrSoundStopButton = new JButton(stopImageIcon);
		azanFajrSoundPlayButton.setActionCommand(AppConfig.SoundEnum.PLAY_FAJR.toString());
		azanFajrSoundStopButton.setActionCommand(AppConfig.SoundEnum.PLAY_FAJR.toString());
		azanFajrSoundPlayButton.addActionListener(playActionListener);
		azanFajrSoundStopButton.addActionListener(stopActionListener);
		azanFajrSoundDefaultButton.addActionListener(defaultSoundActionListener);
		azanFajrSoundTextField.addMouseListener(textFieldSoundDoubleClickMouseListener);
		azanFajrSoundTextField.setName(AppConfig.SoundEnum.PLAY_FAJR.toString());
		azanFajrSoundTextField.setEditable(false);
		azanFajrSoundDefaultButton.setActionCommand(AppConfig.SoundEnum.PLAY_FAJR.toString());
		setSizeButton(azanFajrSoundPlayButton, buttonDimension);
		setSizeButton(azanFajrSoundStopButton, buttonDimension);
		
		azanDhuhrSoundPlayButton = new JButton(playImageIcon);
		azanDhuhrSoundStopButton = new JButton(stopImageIcon);
		azanDhuhrSoundPlayButton.setActionCommand(AppConfig.SoundEnum.PLAY_DHUHR.toString());
		azanDhuhrSoundStopButton.setActionCommand(AppConfig.SoundEnum.PLAY_DHUHR.toString());
		azanDhuhrSoundDefaultButton.setActionCommand(AppConfig.SoundEnum.PLAY_DHUHR.toString());
		azanDhuhrSoundTextField.setName(AppConfig.SoundEnum.PLAY_DHUHR.toString());
		azanDhuhrSoundTextField.setEditable(false);
		azanDhuhrSoundPlayButton.addActionListener(playActionListener);
		azanDhuhrSoundStopButton.addActionListener(stopActionListener);
		azanDhuhrSoundDefaultButton.addActionListener(defaultSoundActionListener);
		azanDhuhrSoundTextField.addMouseListener(textFieldSoundDoubleClickMouseListener);
		setSizeButton(azanDhuhrSoundPlayButton, buttonDimension);
		setSizeButton(azanDhuhrSoundStopButton, buttonDimension);
		
		azanAsrSoundPlayButton = new JButton(playImageIcon);
		azanAsrSoundStopButton = new JButton(stopImageIcon);
		azanAsrSoundPlayButton.setActionCommand(AppConfig.SoundEnum.PLAY_ASR.toString());
		azanAsrSoundStopButton.setActionCommand(AppConfig.SoundEnum.PLAY_ASR.toString());
		azanAsrSoundDefaultButton.setActionCommand(AppConfig.SoundEnum.PLAY_ASR.toString());
		azanAsrSoundTextField.setName(AppConfig.SoundEnum.PLAY_ASR.toString());
		azanAsrSoundTextField.setEditable(false);
		azanAsrSoundPlayButton.addActionListener(playActionListener);
		azanAsrSoundStopButton.addActionListener(stopActionListener);
		azanAsrSoundDefaultButton.addActionListener(defaultSoundActionListener);
		azanAsrSoundTextField.addMouseListener(textFieldSoundDoubleClickMouseListener);
		setSizeButton(azanAsrSoundPlayButton, buttonDimension);
		setSizeButton(azanAsrSoundStopButton, buttonDimension);
		
		azanMaghribSoundPlayButton = new JButton(playImageIcon);
		azanMaghribSoundStopButton = new JButton(stopImageIcon);
		azanMaghribSoundPlayButton.setActionCommand(AppConfig.SoundEnum.PLAY_MAGHRIB.toString());
		azanMaghribSoundStopButton.setActionCommand(AppConfig.SoundEnum.PLAY_MAGHRIB.toString());
		azanMaghribSoundDefaultButton.setActionCommand(AppConfig.SoundEnum.PLAY_MAGHRIB.toString());
		azanMaghribSoundTextField.setName(AppConfig.SoundEnum.PLAY_MAGHRIB.toString());
		azanMaghribSoundTextField.setEditable(false);
		azanMaghribSoundPlayButton.addActionListener(playActionListener);
		azanMaghribSoundStopButton.addActionListener(stopActionListener);
		azanMaghribSoundDefaultButton.addActionListener(defaultSoundActionListener);
		azanMaghribSoundTextField.addMouseListener(textFieldSoundDoubleClickMouseListener);
		setSizeButton(azanMaghribSoundPlayButton, buttonDimension);
		setSizeButton(azanMaghribSoundStopButton, buttonDimension);
		
		azanIshaSoundPlayButton = new JButton(playImageIcon);
		azanIshaSoundStopButton = new JButton(stopImageIcon);
		azanIshaSoundPlayButton.setActionCommand(AppConfig.SoundEnum.PLAY_ISHA.toString());
		azanIshaSoundStopButton.setActionCommand(AppConfig.SoundEnum.PLAY_ISHA.toString());
		azanIshaSoundDefaultButton.setActionCommand(AppConfig.SoundEnum.PLAY_ISHA.toString());
		azanIshaSoundTextField.setName(AppConfig.SoundEnum.PLAY_ISHA.toString());
		azanIshaSoundTextField.setEditable(false);
		azanIshaSoundPlayButton.addActionListener(playActionListener);
		azanIshaSoundStopButton.addActionListener(stopActionListener);
		azanIshaSoundDefaultButton.addActionListener(defaultSoundActionListener);
		azanIshaSoundTextField.addMouseListener(textFieldSoundDoubleClickMouseListener);
		setSizeButton(azanIshaSoundPlayButton, buttonDimension);
		setSizeButton(azanIshaSoundStopButton, buttonDimension);
		
		startupSoundPlayButton = new JButton(playImageIcon);
		startupSoundStopButton = new JButton(stopImageIcon);
		startupSoundPlayButton.setActionCommand(AppConfig.SoundEnum.PLAY_STARTUP.toString());
		startupSoundStopButton.setActionCommand(AppConfig.SoundEnum.PLAY_STARTUP.toString());
		startupSoundDefaultButton.setActionCommand(AppConfig.SoundEnum.PLAY_STARTUP.toString());
		startupSoundTextField.setName(AppConfig.SoundEnum.PLAY_STARTUP.toString());
		startupSoundTextField.setEditable(false);
		startupSoundPlayButton.addActionListener(playActionListener);
		startupSoundStopButton.addActionListener(stopActionListener);
		startupSoundDefaultButton.addActionListener(defaultSoundActionListener);
		startupSoundTextField.addMouseListener(textFieldSoundDoubleClickMouseListener);
		setSizeButton(startupSoundPlayButton, buttonDimension);
		setSizeButton(startupSoundStopButton, buttonDimension);
		
		GroupLayout subSoundsFajrLayout = new GroupLayout(subSoundsFajrPanel);
		subSoundsFajrPanel.setLayout(subSoundsFajrLayout);
		subSoundsFajrLayout.setAutoCreateContainerGaps(true);
		subSoundsFajrLayout.setAutoCreateGaps(true);
		subSoundsFajrLayout.setHorizontalGroup(subSoundsFajrLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(azanFajrSoundTextField)
				.addGroup(subSoundsFajrLayout.createSequentialGroup()
						.addComponent(azanFajrSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanFajrSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanFajrSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		subSoundsFajrLayout.setVerticalGroup(subSoundsFajrLayout.createSequentialGroup()
				.addComponent(azanFajrSoundTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGroup(subSoundsFajrLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(azanFajrSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanFajrSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanFajrSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		
		subSoundsDhuhrPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("dhuhr")));
		GroupLayout subSoundsDhuhrLayout = new GroupLayout(subSoundsDhuhrPanel);
		subSoundsDhuhrPanel.setLayout(subSoundsDhuhrLayout);
		subSoundsDhuhrLayout.setAutoCreateContainerGaps(true);
		subSoundsDhuhrLayout.setAutoCreateGaps(true);
		subSoundsDhuhrLayout.setHorizontalGroup(subSoundsDhuhrLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(azanDhuhrSoundTextField)
				.addGroup(subSoundsDhuhrLayout.createSequentialGroup()
						.addComponent(azanDhuhrSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanDhuhrSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanDhuhrSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		subSoundsDhuhrLayout.setVerticalGroup(subSoundsDhuhrLayout.createSequentialGroup()
				.addComponent(azanDhuhrSoundTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGroup(subSoundsDhuhrLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(azanDhuhrSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanDhuhrSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanDhuhrSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		
		subSoundsAsrPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("asr")));
		GroupLayout subSoundsAsrLayout = new GroupLayout(subSoundsAsrPanel);
		subSoundsAsrPanel.setLayout(subSoundsAsrLayout);
		subSoundsAsrLayout.setAutoCreateContainerGaps(true);
		subSoundsAsrLayout.setAutoCreateGaps(true);
		subSoundsAsrLayout.setHorizontalGroup(subSoundsAsrLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(azanAsrSoundTextField)
				.addGroup(subSoundsAsrLayout.createSequentialGroup()
						.addComponent(azanAsrSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanAsrSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanAsrSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		subSoundsAsrLayout.setVerticalGroup(subSoundsAsrLayout.createSequentialGroup()
				.addComponent(azanAsrSoundTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGroup(subSoundsAsrLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(azanAsrSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanAsrSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanAsrSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		
		subSoundsMagribPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("maghrib")));
		GroupLayout subSoundsMagribLayout = new GroupLayout(subSoundsMagribPanel);
		subSoundsMagribPanel.setLayout(subSoundsMagribLayout);
		subSoundsMagribLayout.setAutoCreateContainerGaps(true);
		subSoundsMagribLayout.setAutoCreateGaps(true);
		subSoundsMagribLayout.setHorizontalGroup(subSoundsMagribLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(azanMaghribSoundTextField)
				.addGroup(subSoundsMagribLayout.createSequentialGroup()
						.addComponent(azanMaghribSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanMaghribSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanMaghribSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		subSoundsMagribLayout.setVerticalGroup(subSoundsMagribLayout.createSequentialGroup()
				.addComponent(azanMaghribSoundTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGroup(subSoundsMagribLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(azanMaghribSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanMaghribSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanMaghribSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		
		subSoundsIshaPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("isha")));
		GroupLayout subSoundsIshaLayout = new GroupLayout(subSoundsIshaPanel);
		subSoundsIshaPanel.setLayout(subSoundsIshaLayout);
		subSoundsIshaLayout.setAutoCreateContainerGaps(true);
		subSoundsIshaLayout.setAutoCreateGaps(true);
		subSoundsIshaLayout.setHorizontalGroup(subSoundsIshaLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(azanIshaSoundTextField)
				.addGroup(subSoundsIshaLayout.createSequentialGroup()
						.addComponent(azanIshaSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanIshaSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanIshaSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		subSoundsIshaLayout.setVerticalGroup(subSoundsIshaLayout.createSequentialGroup()
				.addComponent(azanIshaSoundTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGroup(subSoundsIshaLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(azanIshaSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(azanIshaSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(azanIshaSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		
		subSoundsStartupPanel.setBorder(BorderFactory.createTitledBorder(I18n.resourceBundle.getString("startup")));
		GroupLayout subSoundsStartupLayout = new GroupLayout(subSoundsStartupPanel);
		subSoundsStartupPanel.setLayout(subSoundsStartupLayout);
		subSoundsStartupLayout.setAutoCreateContainerGaps(true);
		subSoundsStartupLayout.setAutoCreateGaps(true);
		subSoundsStartupLayout.setHorizontalGroup(subSoundsStartupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
				.addComponent(startupSoundTextField)
				.addGroup(subSoundsStartupLayout.createSequentialGroup()
						.addComponent(startupSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(startupSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(startupSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		subSoundsStartupLayout.setVerticalGroup(subSoundsStartupLayout.createSequentialGroup()
				.addComponent(startupSoundTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGroup(subSoundsStartupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(startupSoundPlayButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(startupSoundStopButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addComponent(startupSoundDefaultButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));
		
		GroupLayout soundsLayout = new GroupLayout(soundsPanel);
		soundsPanel.setLayout(soundsLayout);
		soundsLayout.setAutoCreateContainerGaps(true);
		soundsLayout.setAutoCreateGaps(true);
		soundsLayout.setHorizontalGroup(soundsLayout.createSequentialGroup()
				.addGroup(soundsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(subSoundsFajrPanel)
						.addComponent(subSoundsMagribPanel))
				.addGroup(soundsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(subSoundsDhuhrPanel)
						.addComponent(subSoundsIshaPanel))
				.addGroup(soundsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(subSoundsAsrPanel)
						.addComponent(subSoundsStartupPanel)));
		soundsLayout.setVerticalGroup(soundsLayout.createSequentialGroup()
				.addGroup(soundsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(subSoundsFajrPanel)
						.addComponent(subSoundsDhuhrPanel)
						.addComponent(subSoundsAsrPanel))
				.addGroup(soundsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(subSoundsMagribPanel)
						.addComponent(subSoundsIshaPanel)
						.addComponent(subSoundsStartupPanel)));

		GroupLayout alertsLayout = new GroupLayout(alertsPanel);
		alertsPanel.setLayout(alertsLayout);
		alertsLayout.setAutoCreateContainerGaps(true);
		alertsLayout.setAutoCreateGaps(true);
		alertsLayout.setHorizontalGroup(
				alertsLayout.createSequentialGroup()
				.addGroup(alertsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(subAlertsPanel)
					.addComponent(subAlertsAudioPanel)
					.addComponent(subAlertsVisualEffectsPanel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		alertsLayout.setVerticalGroup(alertsLayout.createSequentialGroup()
				.addComponent(subAlertsPanel)
				.addComponent(subAlertsAudioPanel)
				.addComponent(subAlertsVisualEffectsPanel));
		
		subAlertsSoundTabbedPane.add(I18n.resourceBundle.getString("alerts"), alertsPanel);
		subAlertsSoundTabbedPane.add(I18n.resourceBundle.getString("sounds"), soundsPanel);
		
		/**
		 * 
		 * Composition Tab Panel and buttons
		 * 
		 */
		tabbedPane.add(I18n.resourceBundle.getString("general"), geneneralPanel);
		tabbedPane.add(I18n.resourceBundle.getString("calculation"), calculationPanel);
		tabbedPane.add(I18n.resourceBundle.getString("alerts_sounds"), subAlertsSoundTabbedPane);
		
		GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(tabbedPane)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    	.addComponent(acceptButton)
                    	.addComponent(okButton)
                        .addComponent(cancelButton)))));
        layout.linkSize(SwingConstants.HORIZONTAL ,new java.awt.Component[] {acceptButton, okButton, cancelButton});
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabbedPane)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(acceptButton)
                	.addComponent(okButton)
                    .addComponent(cancelButton))));
      acceptButton.setVisible(false);
      pack();
	}
	
	private String[] getSizes() {
        String[] sizeArray = new String[35];
            for(int i = 5; i < 40; i++)
                sizeArray[i - 5] = String.valueOf(i);
        return sizeArray;
    }
	
	/**TODO Fill data*/
	private void fillData() {
		languageComboBox.removeAllItems();
		DefaultComboBoxModel<String> languageComboBoxModel;
		for (Locale locale : AppConfig.getLocales()) {
			languageComboBoxModel =  (DefaultComboBoxModel<String>) languageComboBox.getModel();
			if (languageComboBoxModel.getIndexOf(AppConfig.getLocaleInformation(locale)) < 0)
				languageComboBox.addItem(AppConfig.getLocaleInformation(locale));
			if (locale.equals(I18n.locale))
				languageComboBox.setSelectedItem(AppConfig.getLocaleInformation(locale));
		}		

		fontNameComboBox.setSelectedItem(AppConfig.getDisplayFontName());
		
		fontSizeComboBox.setSelectedItem(String.valueOf(AppConfig.getDisplayFontSize()));
		int fontStyle = AppConfig.getDisplayFontStyleIndex();
		if (fontStyle > 0) {
			if (fontStyle == 1 || fontStyle == 3)
				fontStyleBoldCheckBox.setSelected(true);
			if (fontStyle == 2 || fontStyle == 3)
				fontStyleItalicCheckBox.setSelected(true);
		}
		
		if (AppConfig.getDisplayFormatTimeType() == AppConfig.H24) {
			timeFormat24RadioButton.setSelected(true);
		} else if (AppConfig.getDisplayFormatTimeType() == AppConfig.H12) {
			timeFormat12RadioButton.setSelected(true);
		} else {
			timeFormat12AMPMRadioButton.setSelected(true);
		}
	
		latitudeTextField.setText(String.valueOf(AppConfig.getCalculationLatitude()));
		longitudeTextField.setText(String.valueOf(AppConfig.getCalculationLongitude()));
		elevationTextField.setText(String.valueOf(AppConfig.getCalculationElevation()));
		
		ActionListener listener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				timeZoneComboBox.setEnabled(timeZoneManualRadioButton.isSelected());				
			}
		};
		timeZoneAutoRadioButton.addActionListener(listener);
		timeZoneManualRadioButton.addActionListener(listener);
		if (AppConfig.getTimeZoneType() == AppConfig.AUTO) {
			timeZoneAutoRadioButton.doClick();
			timeZoneAutoRadioButton.setSelected(true);
		} else {
			timeZoneManualRadioButton.doClick();
			timeZoneManualRadioButton.setSelected(true);
		}
		timeZoneComboBox.setSelectedIndex(AppConfig.getTimeZoneIndex());
		
		for (Entry<Integer, Method> entry : AppConfig.mapMethods.entrySet()) {
			basicCalculationMethodComboBox.addItem(I18n.resourceBundle.getString(entry.getValue().getName().toLowerCase()));
		}
		listener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Method m = AppConfig.mapMethods.get(basicCalculationMethodComboBox.getSelectedIndex());
				boolean customMethodSelected = m.equals(Method.CUSTOM);
				basicCalculationMethodFajrTextField.setEnabled(customMethodSelected);
				basicCalculationMethodIshaDegreesRadioButton.setEnabled(customMethodSelected);
				basicCalculationMethodIshaMinutesRadioButton.setEnabled(customMethodSelected);
				basicCalculationMethodIshaTextField.setEnabled(customMethodSelected);
				basicCalculationMethodMaghribDegreesRadioButton.setEnabled(customMethodSelected);
				basicCalculationMethodMaghribMinutesRadioButton.setEnabled(customMethodSelected);
				basicCalculationMethodMaghribTextField.setEnabled(customMethodSelected);
				if (!customMethodSelected) {
					basicCalculationMethodFajrTextField.setText(String.valueOf(m.getConfiguration(Time.FAJR).getValue()));
					basicCalculationMethodMaghribTextField.setText(String.valueOf(m.getConfiguration(Time.MAGHRIB).getValue()));
					basicCalculationMethodIshaTextField.setText(String.valueOf(m.getConfiguration(Time.ISHA).getValue()));
					
					basicCalculationMethodMaghribDegreesRadioButton.setSelected(m.getConfiguration(Time.MAGHRIB).getType() == Configuration.TYPE_ANGLE);
					basicCalculationMethodMaghribMinutesRadioButton.setSelected(m.getConfiguration(Time.MAGHRIB).getType() == Configuration.TYPE_MINUTE);
					 
					basicCalculationMethodIshaDegreesRadioButton.setSelected(m.getConfiguration(Time.ISHA).getType() == Configuration.TYPE_ANGLE);
					basicCalculationMethodIshaMinutesRadioButton.setSelected(m.getConfiguration(Time.ISHA).getType() == Configuration.TYPE_MINUTE);
				}
			}
		};
		basicCalculationMethodComboBox.addActionListener(listener);
		basicCalculationMethodComboBox.setSelectedIndex(AppConfig.getCalculationMethodIndex());
		
		ButtonGroup gp = new ButtonGroup();
		gp.add(basicCalculationMethodFajrDegreesRadioButton);
		
		if (AppConfig.mapMethods.get(basicCalculationMethodComboBox.getSelectedIndex()).equals(Method.CUSTOM)) {
			basicCalculationMethodFajrTextField.setText(String.valueOf(AppConfig.getCalculationMethodFajrValue()));
			basicCalculationMethodIshaTextField.setText(String.valueOf(AppConfig.getCalculationMethodIshaValue()));
			if (AppConfig.getCalculationMethodIshaType() == AppConfig.ANGLE) {
				basicCalculationMethodIshaDegreesRadioButton.setSelected(true);
			} else {
				basicCalculationMethodIshaMinutesRadioButton.setSelected(true);
			}
			basicCalculationMethodMaghribTextField.setText(String.valueOf(AppConfig.getCalculationMethodMaghribValue()));
			if (AppConfig.getCalculationMethodMaghribType() == AppConfig.ANGLE) {
				basicCalculationMethodMaghribDegreesRadioButton.setSelected(true);
			} else {
				basicCalculationMethodMaghribMinutesRadioButton.setSelected(true);
			}
		}
		
		otherCalculationMethodDhuhrTextField.setText(String.valueOf(AppConfig.getCalculationMethodDhuhrValue()));	
		otherCalculationMethodAsrComboBox.addItem(I18n.resourceBundle.getString("asr_factor_standart"));
		otherCalculationMethodAsrComboBox.addItem(I18n.resourceBundle.getString("asr_factor_hanafi"));
		otherCalculationMethodAsrComboBox.setSelectedIndex(AppConfig.getCalculationMethodAsrFactor() - 1);
		
		for (Entry<Integer, HighLatMethod> entry : AppConfig.mapHightLatitudeMethod.entrySet()) {
			otherCalculationMethodAdjustHLComboBox.addItem(I18n.resourceBundle.getString(entry.getValue().name().toLowerCase()));
		}
		otherCalculationMethodAdjustHLComboBox.setSelectedIndex(AppConfig.getCalculationMethodHigherLatitudeIndex());
		
		
		alertsFajrBeforeComboBox.setSelectedItem(AppConfig.getAlertsAlarmFajrBefore());
		alertsFajrAfterComboBox.setSelectedItem(AppConfig.getAlertsAlarmFajrAfter());
		alertsDhuhrBeforeComboBox.setSelectedItem(AppConfig.getAlertsAlarmDhuhrBefore());
		alertsDhuhrAfterComboBox.setSelectedItem(AppConfig.getAlertsAlarmDhuhrAfter());
		alertsAsrBeforeComboBox.setSelectedItem(AppConfig.getAlertsAlarmAsrBefore());
		alertsAsrAfterComboBox.setSelectedItem(AppConfig.getAlertsAlarmAsrAfter());
		alertsSunsetBeforeComboBox.setSelectedItem(AppConfig.getAlertsAlarmSunsetBefore());
		alertsSunsetAfterComboBox.setSelectedItem(AppConfig.getAlertsAlarmSunsetAfter());
		alertsSunriseBeforeComboBox.setSelectedItem(AppConfig.getAlertsAlarmSunriseBefore());
		alertsSunriseAfterComboBox.setSelectedItem(AppConfig.getAlertsAlarmSunriseAfter());
		alertsMaghribBeforeComboBox.setSelectedItem(AppConfig.getAlertsAlarmMaghribBefore());
		alertsMaghribAfterComboBox.setSelectedItem(AppConfig.getAlertsAlarmMaghribAfter());
		alertsIshaBeforeComboBox.setSelectedItem(AppConfig.getAlertsAlarmIshaBefore());
		alertsIshaAfterComboBox.setSelectedItem(AppConfig.getAlertsAlarmIshaAfter());
		
		String str = AppConfig.getAlertsAlarmAudio();
		str = str.substring(str.lastIndexOf("/") + 1);
		alertsAudioTextField.setText(str);
		
		str = AppConfig.getSoundsFajrAudio();
		str = str.substring(str.lastIndexOf("/") + 1);
		azanFajrSoundTextField.setText(str);
		str = AppConfig.getSoundsDhuhrAudio();
		str = str.substring(str.lastIndexOf("/") + 1);
		azanDhuhrSoundTextField.setText(str);
		str = AppConfig.getSoundsAsrAudio();
		str = str.substring(str.lastIndexOf("/") + 1);
		azanAsrSoundTextField.setText(str);
		str = AppConfig.getSoundsMaghribAudio();
		str = str.substring(str.lastIndexOf("/") + 1);
		azanMaghribSoundTextField.setText(str);
		str = AppConfig.getSoundsIshaAudio();
		str = str.substring(str.lastIndexOf("/") + 1);
		azanIshaSoundTextField.setText(str);
		str = AppConfig.getSoundsStartupAudio();
		str = str.substring(str.lastIndexOf("/") + 1);
		startupSoundTextField.setText(str);
		
		acceptButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				saveAllSettings();	
			}
		});
		
		
		okButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				saveAllSettings();
				status = JOptionPane.OK_OPTION;
				dispose();
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				status = JOptionPane.CANCEL_OPTION;
				dispose();				
			}
		});
	}
	
	public int getStatus() {
		return status;
	}
	
	private void saveAllSettings() {
		/*TODO Need check that settings is changed:
		 * if not changed - simple close window
		 * changed - save changed settings to preferences
		 */
		String selectedLocale = languageComboBox.getSelectedItem().toString();
		Locale localeToSave = Locale.getDefault();
		for (Locale locale : AppConfig.getLocales()) {
			if (AppConfig.getLocaleInformation(locale).equals(selectedLocale))
				localeToSave = new Locale(locale.getLanguage(), locale.getCountry(), locale.getVariant());
		}
				
		AppConfig.setLocaleLanguage(localeToSave.getLanguage().toString());
		AppConfig.setLocaleCountry(localeToSave.getCountry().toString());
		AppConfig.setLocaleVariant(localeToSave.getVariant().toString());
		
		AppConfig.setDisplayFontName(fontNameComboBox.getSelectedItem().toString());
		AppConfig.setDisplayFontSize(Integer.valueOf(fontSizeComboBox.getSelectedItem().toString()));
		AppConfig.setDisplayFontStyleIndex((fontStyleBoldCheckBox.isSelected() ? 1 : 0) + (fontStyleItalicCheckBox.isSelected() ? 2 : 0));
				
		AppConfig.setDisplayAlwaysOnTop(alwaysOnTopCheckBox.isSelected());
		AppConfig.setDisplayLaunchOnStartup(lanchOnStartupCheckBox.isSelected());
		OptionsDialog.applicationToStartup(lanchOnStartupCheckBox.isSelected());
		
		AppConfig.setDisplayFajr(displayFajrCheckBox.isSelected());
		AppConfig.setDisplaySunrise(displaySunriseCheckBox.isSelected());	
		AppConfig.setDisplayDhuhr(displayDhuhrCheckBox.isSelected());	
		AppConfig.setDisplayAsr(displayAsrCheckBox.isSelected());	
		AppConfig.setDisplaySunset(displaySunsetCheckBox.isSelected());
		AppConfig.setDisplayMaghrib(displayMaghribCheckBox.isSelected());
		AppConfig.setDisplayIsha(displayIshaCheckBox.isSelected());
		AppConfig.setDisplayOpacity(Integer.valueOf(opacitySpiner.getValue().toString()));
		
		if (timeFormat24RadioButton.isSelected()) {
			AppConfig.setDisplayFormatTimeType(AppConfig.H24);
		} else if (timeFormat12RadioButton.isSelected()) {
			AppConfig.setDisplayFormatTimeType(AppConfig.H12);
		} else {
			AppConfig.setDisplayFormatTimeType(AppConfig.H12S);
		}
	
		/*Calculation preferences*/
		AppConfig.setCalculationLatitude(Double.valueOf(latitudeTextField.getText()));
		AppConfig.setCalculationLongitude(Double.valueOf(longitudeTextField.getText()));
		AppConfig.setCalculationElevation(Double.valueOf(elevationTextField.getText()));
		
		AppConfig.setTimeZoneType(timeZoneAutoRadioButton.isSelected() ? AppConfig.AUTO : AppConfig.MANUAL);
		AppConfig.setTimeZoneIndex(timeZoneComboBox.getSelectedIndex());
		
		AppConfig.setCalculationMethodIndex(basicCalculationMethodComboBox.getSelectedIndex());
		AppConfig.setCalculationMethodFajrValue(Double.valueOf(basicCalculationMethodFajrTextField.getText()));
		AppConfig.setCalculationMethodMaghribValue(Double.valueOf(basicCalculationMethodMaghribTextField.getText()));
		AppConfig.setCalculationMethodMaghribType(basicCalculationMethodMaghribDegreesRadioButton.isSelected() ? AppConfig.ANGLE : AppConfig.MINUTES);
		AppConfig.setCalculationMethodIshaValue(Double.valueOf(basicCalculationMethodIshaTextField.getText()));
		AppConfig.setCalculationMethodIshaType(basicCalculationMethodIshaDegreesRadioButton.isSelected() ? AppConfig.ANGLE : AppConfig.MINUTES);
		
		AppConfig.setCalculationMethodDhuhrValue(Double.valueOf(otherCalculationMethodDhuhrTextField.getText()));
		AppConfig.setCalculationMethodAsrFactor(otherCalculationMethodAsrComboBox.getSelectedIndex() + 1);
		AppConfig.setCalculationMethodHigherLatitudeIndex(otherCalculationMethodAdjustHLComboBox.getSelectedIndex());
		
		AppConfig.setAlertsAlarmFajrBefore(alertsFajrBeforeComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmFajrAfter(alertsFajrAfterComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmDhuhrBefore(alertsDhuhrBeforeComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmDhuhrAfter(alertsDhuhrAfterComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmAsrBefore(alertsAsrBeforeComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmAsrAfter(alertsAsrAfterComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmSunsetBefore(alertsSunsetBeforeComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmSunsetAfter(alertsSunsetAfterComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmSunriseBefore(alertsSunriseBeforeComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmSunriseAfter(alertsSunriseAfterComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmMaghribBefore(alertsMaghribBeforeComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmMaghribAfter(alertsMaghribAfterComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmIshaBefore(alertsIshaBeforeComboBox.getSelectedItem().toString());
		AppConfig.setAlertsAlarmIshaAfter(alertsIshaAfterComboBox.getSelectedItem().toString());
		
		AppConfig.setAlertsAlarmAudio(alertsAlarmFileName);
		
		AppConfig.setAlertsVisualEffectShowBalonTips(displayBaloonTipsCheckBox.isSelected());
		AppConfig.setAlertsVisualEffectShowMainScreen(displayMainScreenCheckBox.isSelected());
		AppConfig.setAlertsVisualEffectFlashIconTray(displayFlashTrayIconCheckBox.isSelected());
		
		AppConfig.setSoundsFajrAudio(AppConfig.mapSoundFiles.get(AppConfig.SoundEnum.PLAY_FAJR));
		AppConfig.setSoundsDhuhrAudio(AppConfig.mapSoundFiles.get(AppConfig.SoundEnum.PLAY_DHUHR));
		AppConfig.setSoundsAsrAudio(AppConfig.mapSoundFiles.get(AppConfig.SoundEnum.PLAY_ASR));
		AppConfig.setSoundsMaghribAudio(AppConfig.mapSoundFiles.get(AppConfig.SoundEnum.PLAY_MAGHRIB));
		AppConfig.setSoundsIshaAudio(AppConfig.mapSoundFiles.get(AppConfig.SoundEnum.PLAY_ISHA));
		AppConfig.setSoundsStartupAudio(AppConfig.mapSoundFiles.get(AppConfig.SoundEnum.PLAY_STARTUP));
		
	}
	
	private void loadPlayStopButtonIcons() {
		int NUM_IMAGES = 6;
		int playIconNumber = 3;
		int stopIconNumber = 5;
		Image[] images = new Image[NUM_IMAGES];
        URL imageURL = getClass().getResource("/com/koylubaevnt/jpraytimes/images/play_icons_32.png");
        Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
        CropImageFilter cropImageFilter;
        FilteredImageSource filteredImageSource;
        for(int i = 0; i < NUM_IMAGES; i++)
        {
        	cropImageFilter = new CropImageFilter(32 * i, 0, 32, 32);
        	filteredImageSource = new FilteredImageSource(image.getSource(), cropImageFilter);
        	images[i] = createImage(filteredImageSource);
        }
        playImageIcon = new ImageIcon(images[playIconNumber]);
        stopImageIcon = new ImageIcon(images[stopIconNumber]);
        
        
        buttonDimension = new Dimension((playImageIcon.getIconWidth() > stopImageIcon.getIconWidth()) ?  playImageIcon.getIconWidth() + 2 : stopImageIcon.getIconWidth() + 2, 
        		(playImageIcon.getIconHeight() > stopImageIcon.getIconHeight()) ? playImageIcon.getIconHeight() + 2 : stopImageIcon.getIconHeight() + 2);
		
	}
	private void setSizeButton(JButton button, Dimension dimension) {
		button.setMaximumSize(dimension);
		button.setPreferredSize(dimension);
		button.setMinimumSize(dimension);
	}
	
	private File openSelectSingleFileDialog(JComponent parent, String title, FileFilter fileFilter) {
		File file = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setDialogTitle(title);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(fileFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		int ret = fileChooser.showOpenDialog(parent);
		if (ret == JFileChooser.APPROVE_OPTION) {
			 file = fileChooser.getSelectedFile();
		}
		return file;
	}
	
	
	/*TO DO set startup check and make coding...*/
    public static void applicationToStartup(boolean status) {
    	//status = true - add to startup
    	//status = false - delete from startup
    	Properties properties = System.getProperties();
    	String osName = properties.getProperty("os.name").toLowerCase();
    	if (osName.indexOf("win") >= 0) {
    		//Add\Delete reference to registry
    		String regString = "Software\\Microsoft\\Windows\\CurrentVersion\\Run";
    		String appNameString = "JPrayTimes";
    		if (status) {
    			String jarFileName = new File(JWidget.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
        		try {
					WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER, regString, appNameString, jarFileName);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
    		} else {
    			try {
					WinRegistry.deleteValue(WinRegistry.HKEY_CURRENT_USER, regString, appNameString);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
    		}
    	} else if (osName.indexOf("mac") >= 0) {
    		//Add reference to ???
    	} else if (osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0 || osName.indexOf("aix") > 0 ) {
    		//Add reference to user.dir???
    		//String installPath = "$XDG_CONFIG_DIRS/autostart/";
    		if (status) {
        		PrintWriter out;
				try {
					out = new PrintWriter(new FileWriter(
					    System.getProperty("user.home")+"/.config/autostart/JPrayTimes.desktop"));
					out.println("[Desktop Entry]");
	    		    out.println("Name=myapp");
	    		    out.println("Comment=Autostart entry for JPrayTimes");
	    		    out.println("Exec=" + new File(JWidget.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath());
	    		    out.flush();
	    		    out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		} else {
    			
    		}
    		    
    	} else if (osName.indexOf("sunos") >= 0){
    		//Add reference to ???
    	} 
    	
    }
	
	/**TODO All variables*/
	private int status = -1;
	private ImageIcon playImageIcon, stopImageIcon;
	private Dimension buttonDimension;
	CustomPlayer alertsPlayer = new CustomPlayer();
	CustomPlayer soundPlayer = new CustomPlayer();
	private String alertsAlarmFileName = AppConfig.getAlertsAlarmAudio();
	
	/**TODO Swing variables*/
	JTabbedPane tabbedPane = new JTabbedPane(
			JTabbedPane.TOP, 
			JTabbedPane.SCROLL_TAB_LAYOUT);
	
	JPanel geneneralPanel = new JPanel();
	JLabel languageLabel = new JLabel(I18n.resourceBundle.getString("language"));
	JComboBox<String> languageComboBox = new JComboBox<String>();
	JButton languageLoadButton = new JButton(I18n.resourceBundle.getString("load_language"));
	/*
	JLabel lookAndFellLabel = new JLabel(I18n.resourceBundle.getString("theme"));
	JComboBox<String> lookAndFellComboBox = new JComboBox<String>();
	*/
	
	JCheckBox alwaysOnTopCheckBox = new JCheckBox(I18n.resourceBundle.getString("always_on_top"), AppConfig.getDisplayAlwaysOnTop());
	JCheckBox lanchOnStartupCheckBox = new JCheckBox(I18n.resourceBundle.getString("launch_on_startup"), AppConfig.getDisplayLaunchOnStartup());	
	
	private String[] fontName = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	JPanel subFontPanel = new JPanel();
	JLabel fontNameLabel = new JLabel(I18n.resourceBundle.getString("font_name"));
	JComboBox<String> fontNameComboBox = new JComboBox<String>(fontName);
	JLabel fontSizeLabel = new JLabel(I18n.resourceBundle.getString("font_size"));
	JComboBox<String> fontSizeComboBox =  new JComboBox<String>(getSizes());
	JLabel fontStyleLabel = new JLabel(I18n.resourceBundle.getString("font_style"));
	JCheckBox fontStyleBoldCheckBox = new JCheckBox(I18n.resourceBundle.getString("font_bold"));
	JCheckBox fontStyleItalicCheckBox = new JCheckBox(I18n.resourceBundle.getString("font_italic"));
	//JComboBox<String> fontStyleComboBox = new JComboBox<String>();
	
	JPanel subDisplayPanel = new JPanel();
	JCheckBox displayFajrCheckBox = new JCheckBox(I18n.resourceBundle.getString("fajr"), AppConfig.getDisplayFajr());	
	JCheckBox displaySunriseCheckBox = new JCheckBox(I18n.resourceBundle.getString("sunrise"), AppConfig.getDisplaySunrise());	
	JCheckBox displayDhuhrCheckBox = new JCheckBox(I18n.resourceBundle.getString("dhuhr"), AppConfig.getDisplayDhuhr());	
	JCheckBox displayAsrCheckBox = new JCheckBox(I18n.resourceBundle.getString("asr"), AppConfig.getDisplayAsr());	
	JCheckBox displaySunsetCheckBox = new JCheckBox(I18n.resourceBundle.getString("sunset"), AppConfig.getDisplaySunset());
	JCheckBox displayMaghribCheckBox = new JCheckBox(I18n.resourceBundle.getString("maghrib"), AppConfig.getDisplayMaghrib());
	JCheckBox displayIshaCheckBox = new JCheckBox(I18n.resourceBundle.getString("isha"), AppConfig.getDisplayIsha());
	JLabel opacityLabel = new JLabel(I18n.resourceBundle.getString("opacity"));
	JSpinner opacitySpiner = new JSpinner();//JSlider(JSlider.HORIZONTAL, 0, 100, AppConfig.getDisplayOpacity());
	JPanel timeFormatPanel = new JPanel();
	ButtonGroup timeFormatButtonGroup = new ButtonGroup();
	JRadioButton timeFormat24RadioButton = new JRadioButton(I18n.resourceBundle.getString("24_hours"));
	JRadioButton timeFormat12RadioButton = new JRadioButton(I18n.resourceBundle.getString("12_hours"));
	JRadioButton timeFormat12AMPMRadioButton = new JRadioButton(I18n.resourceBundle.getString("12_am_pm_hours"));
	
	JPanel calculationPanel = new JPanel();
	JPanel locationPanel = new JPanel();
	JTextField locationTextField = new JTextField();				
	JButton selectLocationButton = new JButton(I18n.resourceBundle.getString("location"));
	JLabel latitudeLabel = new JLabel(I18n.resourceBundle.getString("latitude"));
	JTextField latitudeTextField = new JTextField(5);				
	JLabel longitudeLabel = new JLabel(I18n.resourceBundle.getString("longitude"));
	JTextField longitudeTextField = new JTextField(5);				
	JLabel elevationLabel = new JLabel(I18n.resourceBundle.getString("elevation"));
	JTextField elevationTextField = new JTextField(5);				
	JPanel timeZonePanel = new JPanel();
	JRadioButton timeZoneAutoRadioButton = new JRadioButton(I18n.resourceBundle.getString("auto"));
	JRadioButton timeZoneManualRadioButton = new JRadioButton(I18n.resourceBundle.getString("manual"));
	ButtonGroup timeZoneButtonGroup = new ButtonGroup();
	
	JComboBox<String> timeZoneComboBox = new JComboBox<String>(AppConfig.dstList.toArray(new String[AppConfig.dstList.size()]));
	JLabel timeZoneManualInfoLabel = new JLabel(I18n.resourceBundle.getString("manual_text"));

	JPanel calculationMethodPanel = new JPanel(); 
	JPanel basicCalculationMethodPanel = new JPanel();
	JLabel basicCalculationMethodLabel = new JLabel(I18n.resourceBundle.getString("method"));
	JComboBox<String> basicCalculationMethodComboBox = new JComboBox<String>();
	JLabel basicCalculationMethodFajrLabel = new JLabel(I18n.resourceBundle.getString("fajr"));
	JTextField basicCalculationMethodFajrTextField = new JTextField(5);
	JRadioButton basicCalculationMethodFajrDegreesRadioButton = new JRadioButton(I18n.resourceBundle.getString("degrees"), true);
	JLabel basicCalculationMethodMaghribLabel = new JLabel(I18n.resourceBundle.getString("maghrib"));
	JTextField basicCalculationMethodMaghribTextField = new JTextField(5);
	JRadioButton basicCalculationMethodMaghribDegreesRadioButton = new JRadioButton(I18n.resourceBundle.getString("degrees"));
	JRadioButton basicCalculationMethodMaghribMinutesRadioButton = new JRadioButton(I18n.resourceBundle.getString("maghrib_minutes"));
	ButtonGroup basicCalculationMethodMaghribButtonGroup = new ButtonGroup();
	
	JLabel basicCalculationMethodIshaLabel = new JLabel(I18n.resourceBundle.getString("isha"));
	JTextField basicCalculationMethodIshaTextField = new JTextField(5);
	JRadioButton basicCalculationMethodIshaDegreesRadioButton = new JRadioButton(I18n.resourceBundle.getString("degrees"));
	JRadioButton basicCalculationMethodIshaMinutesRadioButton = new JRadioButton(I18n.resourceBundle.getString("isha_minutes"));
	ButtonGroup basicCalculationMethodIshaButtonGroup = new ButtonGroup();
	
	JPanel otherCalculationMethodPanel = new JPanel();
	JLabel otherCalculationMethodDhuhrLabel = new JLabel(I18n.resourceBundle.getString("dhuhr"));
	JTextField otherCalculationMethodDhuhrTextField = new JTextField(5);
	JLabel otherCalculationMethodDhuhrExtraLabel = new JLabel(I18n.resourceBundle.getString("dhuhr_extra"));
	JLabel otherCalculationMethodAsrLabel = new JLabel(I18n.resourceBundle.getString("asr_method"));
	JComboBox<String> otherCalculationMethodAsrComboBox = new JComboBox<String>();
	JLabel otherCalculationMethodAdjustHLLabel = new JLabel(I18n.resourceBundle.getString("adjust_method"));
	JComboBox<String> otherCalculationMethodAdjustHLComboBox = new JComboBox<String>();
	
	JTabbedPane subAlertsSoundTabbedPane = new JTabbedPane(
			JTabbedPane.TOP, 
			JTabbedPane.SCROLL_TAB_LAYOUT);
	JPanel alertsSoundsPanel = new JPanel();
	JPanel alertsPanel = new JPanel();
	JPanel subAlertsPanel = new JPanel();
	JLabel alertsBeforeLabel = new JLabel(I18n.resourceBundle.getString("alert_before"));
	JLabel alertsAfterLabel = new JLabel(I18n.resourceBundle.getString("alert_after"));
	JLabel alertsFajrLabel = new JLabel(I18n.resourceBundle.getString("fajr"));
	JComboBox<String> alertsFajrBeforeComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JComboBox<String> alertsFajrAfterComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JLabel alertsSunriseLabel = new JLabel(I18n.resourceBundle.getString("sunrise"));
	JComboBox<String> alertsSunriseBeforeComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JComboBox<String> alertsSunriseAfterComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JLabel alertsDhuhrLabel = new JLabel(I18n.resourceBundle.getString("dhuhr"));
	JComboBox<String> alertsDhuhrBeforeComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JComboBox<String> alertsDhuhrAfterComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JLabel alertsAsrLabel = new JLabel(I18n.resourceBundle.getString("asr"));
	JComboBox<String> alertsAsrBeforeComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JComboBox<String> alertsAsrAfterComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JLabel alertsSunsetLabel = new JLabel(I18n.resourceBundle.getString("sunset"));
	JComboBox<String> alertsSunsetBeforeComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JComboBox<String> alertsSunsetAfterComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JLabel alertsMaghribLabel = new JLabel(I18n.resourceBundle.getString("maghrib"));
	JComboBox<String> alertsMaghribBeforeComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JComboBox<String> alertsMaghribAfterComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JLabel alertsIshaLabel = new JLabel(I18n.resourceBundle.getString("isha"));
	JComboBox<String> alertsIshaBeforeComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JComboBox<String> alertsIshaAfterComboBox = new JComboBox<String>(AppConfig.getAlarmMinutes());
	JPanel subAlertsAudioPanel = new JPanel();
	//JLabel alertsAudioTypeLabel = new JLabel(I18n.resourceBundle.getString("alerts_audio_type"));
	//JComboBox<String> alertsAudioTypeComboBox = new JComboBox<String>();
	JLabel alertsAudioLabel = new JLabel(I18n.resourceBundle.getString("audio"));
	//JComboBox<String> alertsAudioAudioComboBox = new JComboBox<String>();
	JTextField alertsAudioTextField = new JTextField(3);
	JButton alertsAudioPlayButton;
	JButton alertsAudioStopButton;
	JButton alertsAudioDefaultButton = new JButton(I18n.resourceBundle.getString("default"));
	
	JPanel subAlertsVisualEffectsPanel = new JPanel();
	JCheckBox displayBaloonTipsCheckBox = new JCheckBox(I18n.resourceBundle.getString("display_baloon_tips"), AppConfig.getAlertsVisualEffectShowBalonTips());
	JCheckBox displayMainScreenCheckBox = new JCheckBox(I18n.resourceBundle.getString("display_main_screen"), AppConfig.getAlertsVisualEffectShowMainScreen());
	JCheckBox displayFlashTrayIconCheckBox = new JCheckBox(I18n.resourceBundle.getString("flash_tray_icon"), AppConfig.getAlertsVisualEffectFlashIconTray());
	
	JPanel soundsPanel = new JPanel();
	JPanel subSoundsFajrPanel = new JPanel();
	JPanel subSoundsDhuhrPanel = new JPanel();
	JPanel subSoundsAsrPanel = new JPanel();
	JPanel subSoundsMagribPanel = new JPanel();
	JPanel subSoundsStartupPanel = new JPanel();
	JPanel subSoundsIshaPanel = new JPanel();
	JTextFieldPlaceholder azanFajrSoundTextField = new JTextFieldPlaceholder(3);
	JTextFieldPlaceholder azanDhuhrSoundTextField = new JTextFieldPlaceholder(3);
	JTextFieldPlaceholder azanAsrSoundTextField = new JTextFieldPlaceholder(3);
	JTextFieldPlaceholder azanMaghribSoundTextField = new JTextFieldPlaceholder(3);
	JTextFieldPlaceholder azanIshaSoundTextField = new JTextFieldPlaceholder(3);
	JTextFieldPlaceholder startupSoundTextField = new JTextFieldPlaceholder(3);
	JButton azanFajrSoundPlayButton;
	JButton azanFajrSoundStopButton;
	JButton azanDhuhrSoundPlayButton;
	JButton azanDhuhrSoundStopButton;
	JButton azanAsrSoundPlayButton;
	JButton azanAsrSoundStopButton;
	JButton azanMaghribSoundPlayButton;
	JButton azanMaghribSoundStopButton;
	JButton azanIshaSoundPlayButton;
	JButton azanIshaSoundStopButton;
	JButton startupSoundPlayButton;
	JButton startupSoundStopButton;
	
	JButton azanFajrSoundDefaultButton = new JButton(I18n.resourceBundle.getString("default"));
	JButton azanDhuhrSoundDefaultButton = new JButton(I18n.resourceBundle.getString("default"));
	JButton azanAsrSoundDefaultButton = new JButton(I18n.resourceBundle.getString("default"));
	JButton azanMaghribSoundDefaultButton = new JButton(I18n.resourceBundle.getString("default"));
	JButton azanIshaSoundDefaultButton = new JButton(I18n.resourceBundle.getString("default"));
	JButton startupSoundDefaultButton = new JButton(I18n.resourceBundle.getString("default"));
	
	JButton okButton = new JButton(I18n.resourceBundle.getString("ok"));
	JButton cancelButton = new JButton(I18n.resourceBundle.getString("cancel"));	
	JButton acceptButton = new JButton(I18n.resourceBundle.getString("accept"));
	
}


