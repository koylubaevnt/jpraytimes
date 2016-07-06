package com.koylubaevnt.jpraytimes.forms;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.koylubaevnt.jpraytimes.forms.tablemodels.MonthlyTimetableTableModel;
import com.koylubaevnt.jpraytimes.i18n.I18n;
import com.koylubaevnt.jpraytimes.preferences.AppConfig;

import id.web.michsan.praytimes.Configuration;
import id.web.michsan.praytimes.Location;
import id.web.michsan.praytimes.Method;
import id.web.michsan.praytimes.PrayTimes;
import id.web.michsan.praytimes.PrayTimes.Time;

public class MonthlyTimeTable extends JFrame {

	private static final long serialVersionUID = -3116529744748855720L;

	public MonthlyTimeTable() {
		
		initComponents();
		
		setSize(500, 500);
		setAlwaysOnTop(true);
		setTitle(I18n.resourceBundle.getString("monthly_timetable"));
		setResizable(false);
	}
	
	GregorianCalendar currentDate;
	Map<Time, Double>[] resultsPrayTime;
	int maxDay;
	PrayTimes prayTimes;
	Location location;
	JTable table;
	JLabel dateLabel;
	
	private void initComponents() {
	
		currentDate = new GregorianCalendar();
		
		Method calculationMethod = AppConfig.mapMethods.get(AppConfig.getCalculationMethodIndex());
		prayTimes = new PrayTimes(calculationMethod);
		
		location = new Location(AppConfig.getCalculationLatitude(), AppConfig.getCalculationLongitude(), AppConfig.getCalculationElevation());
		int methodIndex = AppConfig.getCalculationMethodIndex();
		int asrFactor  = AppConfig.getCalculationMethodAsrFactor();
		int hightLatitudeMethodIndex  = AppConfig.getCalculationMethodHigherLatitudeIndex();
		int midnightMethodIndex  = AppConfig.getCalculationMethodMidnightIndex();
		Map<Time, Configuration> mapAdjustments = new HashMap<Time, Configuration>();
		Map<Time, Integer> offsets = new HashMap<Time, Integer>();
		
		calculationMethod = AppConfig.mapMethods.get(methodIndex);
		prayTimes.setAsrFactor(asrFactor);
		prayTimes.setHighLatMethod(AppConfig.mapHightLatitudeMethod.get(hightLatitudeMethodIndex));
		if (calculationMethod.equals(Method.CUSTOM)) {
			prayTimes.setMidnightMethod(AppConfig.mapMidnightMethod.get(midnightMethodIndex));
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
		prayTimes.adjust(mapAdjustments);
		
		//Set offsets
		prayTimes.tuneOffset(offsets);
		
		table = new JTable();
		JPanel headPanel = new JPanel();
		JButton buttonNext = new JButton(I18n.getText("next"));
		JButton buttonPrevious = new JButton(I18n.getText("previous"));
		dateLabel = new JLabel();
		
		
		calculateTableData();
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
		
		buttonNext.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				currentDate.set(GregorianCalendar.DAY_OF_MONTH, 1);
				currentDate.add(GregorianCalendar.MONTH, 1);
				calculateTableData();
			}
		});
		buttonPrevious.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				currentDate.set(GregorianCalendar.DAY_OF_MONTH, 1);
				currentDate.add(GregorianCalendar.MONTH, -1);
				calculateTableData();
			}
		});
		
		headPanel.add(buttonPrevious, BorderLayout.WEST);
		headPanel.add(dateLabel);
		headPanel.add(buttonNext, BorderLayout.EAST);
		add(headPanel, BorderLayout.NORTH);
		
		JPanel buttonPanel = new JPanel();
		JButton buttonOK = new JButton(I18n.getText("ok"));
		buttonPanel.add(buttonOK);
		buttonOK.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	@SuppressWarnings("unchecked")
	private void calculateTableData() {
		maxDay = currentDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		resultsPrayTime = new HashMap[maxDay];
		for (int i = 0; i < maxDay; i++) {
			currentDate.set(GregorianCalendar.DAY_OF_MONTH, i + 1);
			resultsPrayTime[i] = prayTimes.getTimes(currentDate, location);
		}
		MonthlyTimetableTableModel tableModel = new MonthlyTimetableTableModel(resultsPrayTime);
		table.setModel(tableModel);
		DefaultTableCellRenderer centerCellRenderer = new DefaultTableCellRenderer();
	    centerCellRenderer.setHorizontalAlignment(JLabel.CENTER);
	    Font font = table.getFont();
	    if (font != null) {
	    	font = font.deriveFont(Font.BOLD);
	    	table.getTableHeader().setFont(font);
	    }
	    for(int i=0; i < table.getColumnCount(); i++){
        	table.getColumnModel().getColumn(i).setCellRenderer(centerCellRenderer);
        }
	    dateLabel.setText(" " + currentDate.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.LONG, I18n.locale) + " " + currentDate.get(GregorianCalendar.YEAR) + " ");
	}
	
}
