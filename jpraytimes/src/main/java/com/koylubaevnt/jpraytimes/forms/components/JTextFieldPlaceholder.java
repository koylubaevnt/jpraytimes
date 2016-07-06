package com.koylubaevnt.jpraytimes.forms.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class JTextFieldPlaceholder extends JTextField {
		
	private static final long serialVersionUID = 3091153903931201977L;
		private Font originalFont;
	    private Color originalForeground;
	    /**
	     * Grey by default*
	     */
	    private Color placeholderForeground = new Color(160, 160, 160);
	    private boolean textWrittenIn;
	 
	    /**
	     * You can insert all constructors.
	     * I inserted only this one.*
	     */
	    public JTextFieldPlaceholder(int columns) {
	        super(columns);
	    }
	 
	    @Override
	    public void setFont(Font f) {
	        super.setFont(f);
	        if (!isTextWrittenIn()) {
	            originalFont = f;
	        }
	    }
	 
	    public void setForeground(Color fg) {
	        super.setForeground(fg);
	        if (!isTextWrittenIn()) {
	            originalForeground = fg;
	        }
	    }
	 
	    public Color getPlaceholderForeground() {
	        return placeholderForeground;
	    }
	 
	    public void setPlaceholderForeground(Color placeholderForeground) {
	        this.placeholderForeground = placeholderForeground;
	    }
	 
	    public boolean isTextWrittenIn() {
	        return textWrittenIn;
	    }
	 
	    public void setTextWrittenIn(boolean textWrittenIn) {
	        this.textWrittenIn = textWrittenIn;
	    }
	 
	    public void setPlaceholder(final String text) {
	 
	        this.customizeText(text);
	 
	        this.getDocument().addDocumentListener(new DocumentListener() {
	            
	            public void insertUpdate(DocumentEvent e) {
	                warn();
	            }
	 
	            public void removeUpdate(DocumentEvent e) {
	                warn();
	            }
	 
	            public void changedUpdate(DocumentEvent e) {
	                warn();
	            }
	 
	            public void warn() {
	                if (getText().trim().length() != 0) {
	                    setFont(originalFont);
	                    setForeground(originalForeground);
	                    setTextWrittenIn(true);
	                }
	 
	            }
	        });
	 
	        this.addFocusListener(new FocusListener() {
	            
	            public void focusGained(FocusEvent e) {
	                if (!isTextWrittenIn()) {
	                    setText("");
	                }
	 
	            }
	 
	            public void focusLost(FocusEvent e) {
	                if (getText().trim().length() == 0) {
	                    customizeText(text);
	                }
	            }
	 
	        });
	 
	    }
	 
	    private void customizeText(String text) {
	        setText(text);
	        /**If you change font, family and size will follow
	         * changes, while style will always be italic**/
	        setFont(new Font(getFont().getFamily(), Font.ITALIC, getFont().getSize()));
	        setForeground(getPlaceholderForeground());
	        setTextWrittenIn(false);
	    }
	 
	}