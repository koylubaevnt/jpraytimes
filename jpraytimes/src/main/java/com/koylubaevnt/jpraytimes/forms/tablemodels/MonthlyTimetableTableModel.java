package com.koylubaevnt.jpraytimes.forms.tablemodels;

import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.koylubaevnt.jpraytimes.i18n.I18n;

import id.web.michsan.praytimes.PrayTimes.Time;
import id.web.michsan.praytimes.Util;

public class MonthlyTimetableTableModel extends AbstractTableModel {
		
		private static final long serialVersionUID = 8448252673175487967L;
		private int COLUMN_COUNT = 8;
		public Map<Time, Double>[] mapPrayTimes;
		
		public MonthlyTimetableTableModel() {
			this(null);
		}
		
		public MonthlyTimetableTableModel(Map<Time, Double>[] mapPrayTimes) {
			this.mapPrayTimes = mapPrayTimes;
		}
		
		
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object obj;
			switch (columnIndex) {
			case 0:
				obj = ++rowIndex;
				break;
			case 1:
				obj = Util.toTime24(mapPrayTimes[rowIndex].get(Time.FAJR));
				break;
			case 2:
				obj = Util.toTime24(mapPrayTimes[rowIndex].get(Time.SUNRISE));
				break;
			case 3:
				obj = Util.toTime24(mapPrayTimes[rowIndex].get(Time.DHUHR));
				break;
			case 4:
				obj = Util.toTime24(mapPrayTimes[rowIndex].get(Time.ASR));
				break;
			case 5:
				obj = Util.toTime24(mapPrayTimes[rowIndex].get(Time.SUNSET));
				break;
			case 6:
				obj = Util.toTime24(mapPrayTimes[rowIndex].get(Time.MAGHRIB));
				break;
			default:
				obj = Util.toTime24(mapPrayTimes[rowIndex].get(Time.ISHA));
				break;
			}
			return obj;
		}
		
		public int getRowCount() {
			return (mapPrayTimes == null ? 0 : mapPrayTimes.length);
		}
		
		public int getColumnCount() {
			return COLUMN_COUNT;
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public String getColumnName(int column) {
			String res;
			switch (column) {
			case 0:
				res = I18n.resourceBundle.getString("date");
				break;
			case 1:
				res = I18n.resourceBundle.getString("fajr");
				break;
			case 2:
				res = I18n.resourceBundle.getString("sunrise");
				break;
			case 3:
				res = I18n.resourceBundle.getString("dhuhr");
				break;
			case 4:
				res = I18n.resourceBundle.getString("asr");
				break;
			case 5:
				res = I18n.resourceBundle.getString("sunset");
				break;
			case 6:
				res = I18n.resourceBundle.getString("maghrib");
				break;
			default:
				res = I18n.resourceBundle.getString("isha");
				break;
			} 
			return res;
		}

}
