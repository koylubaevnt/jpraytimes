package com.koylubaevnt.jpraytimes.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import com.koylubaevnt.jpraytimes.forms.tablemodels.LocationTableModel;
import com.koylubaevnt.jpraytimes.i18n.I18n;
import com.koylubaevnt.jpraytimes.services.Geocoder;
import com.koylubaevnt.jpraytimes.services.GoogleGeocoder;
import com.koylubaevnt.jpraytimes.services.add.GeocoderHelper;

import id.web.michsan.praytimes.Location;

public class SelectLocationDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8926861283067071645L;

	private Location location = null;
	GeocoderHelper[] geocoderHelpers;
	
	public SelectLocationDialog() {
		setLocationByPlatform(true);
		setModal(true);
		setAlwaysOnTop(true);
		
		initComponents();
	}
	
	private void initComponents() {
		setTitle(I18n.resourceBundle.getString("select_location"));
		
		LocationTableModel model = new LocationTableModel();
		locationTable.setModel(model);
		
		locationTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
					okButton.doClick();
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(locationTable);
		
		nameLocationButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String address = nameLocationTextField.getText();
				TableModel tableModel = new LocationTableModel();
				if (!address.isEmpty()) {
					Geocoder geocoder = new GoogleGeocoder();
					geocoder.searchGeocoding(address);
					geocoderHelpers = new GeocoderHelper[geocoder.getSize()];
					geocoderHelpers = geocoder.getGeocoderHelper().clone();
					if (geocoderHelpers.length > 0) {
						tableModel = new LocationTableModel(geocoderHelpers);
						locationTable.setModel(tableModel);
					} else {
						locationTable.setModel(tableModel);
					}
				} else {
					locationTable.setModel(tableModel);
				}				
			}
		});
		
		
		okButton = new JButton(I18n.resourceBundle.getString("ok"));
		JButton cancelButton = new JButton(I18n.resourceBundle.getString("cancel"));
		okButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				int rowIndex = locationTable.getSelectedRow();
				if (rowIndex >= 0) {
					int columnIndex = 1;
					location = new Location(
							Double.valueOf(locationTable.getModel().getValueAt(rowIndex, columnIndex++).toString()),
							Double.valueOf(locationTable.getModel().getValueAt(rowIndex, columnIndex++).toString()),
							Double.valueOf(locationTable.getModel().getValueAt(rowIndex, columnIndex++).toString()));
				}
				dispose();	
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});
		
		GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                    .addComponent(nameLocationLabel)
                    .addComponent(nameLocationTextField)
                    .addComponent(nameLocationButton)
               )
            .addComponent(scrollPane)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(okButton)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(cancelButton)
                    )
        );

        layout.linkSize(SwingConstants.HORIZONTAL ,new java.awt.Component[] {okButton, cancelButton});

        layout.setVerticalGroup(
        		layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        				.addComponent(nameLocationLabel)
                        .addComponent(nameLocationTextField)
                        .addComponent(nameLocationButton))
        		.addComponent(scrollPane)
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(okButton)
                        .addComponent(cancelButton))
        );
        
		pack();
	}
	
	public Location getSettings() {
		return location;
	}
	
	
	
	@Override
	public void setModal(boolean modal) {
		// Always modal
		super.setModal(true);
	}


	JLabel nameLocationLabel = new JLabel(I18n.resourceBundle.getString("location_name"));
	JTextField nameLocationTextField = new JTextField(3);
	JButton nameLocationButton = new JButton(I18n.resourceBundle.getString("search"));
	JTable locationTable = new JTable();
	JButton okButton;
	
}
