package com.koylubaevnt.jpraytimes.forms.tablemodels;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.koylubaevnt.jpraytimes.i18n.I18n;
import com.koylubaevnt.jpraytimes.services.add.GeocoderHelper;

public class LocationTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 3188058009447383727L;

	private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
	private int COLUMN_COUNT = 4;
	private GeocoderHelper[] geocoderHelpers;
	
	public LocationTableModel() {
		this(null);
	}
	
	public LocationTableModel(GeocoderHelper[] geocoderHelpers) {
		this.geocoderHelpers = geocoderHelpers;
	}
	
	@Override
	public void addTableModelListener(TableModelListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeTableModelListener(TableModelListener listener) {
		listeners.remove(listener);
	}

	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object obj;
		switch (columnIndex) {
		case 0:
			obj = geocoderHelpers[rowIndex].getAddress();
			break;
		case 1:
			obj = geocoderHelpers[rowIndex].getLatitude();
			break;
		case 2:
			obj = geocoderHelpers[rowIndex].getLongitude();
			break;

		default:
			obj = geocoderHelpers[rowIndex].getElevation();
			break;
		}
		return obj;
	}
	
	public int getRowCount() {
		return (geocoderHelpers == null ? 0 : geocoderHelpers.length);
	}
	
	public int getColumnCount() {
		return COLUMN_COUNT;
	}

	@Override
	public String getColumnName(int column) {
		String res;
		switch (column) {
		case 0:
			res = I18n.resourceBundle.getString("location_name");
			break;
		case 1:
			res = I18n.resourceBundle.getString("latitude");
			break;
		case 2:
			res = I18n.resourceBundle.getString("longitude");
			break;

		default:
			res = I18n.resourceBundle.getString("elevation");
			break;
		} 
		return res;
	}
}
