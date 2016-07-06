package com.koylubaevnt.jpraytimes.trush;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.joda.time.format.DateTimeFormat;

import com.koylubaevnt.jpraytimes.i18n.I18n;

public class TestClass {
	
	static JFrame frame;
	
	public static void main(String[] args) {
		Properties p = System.getProperties();
		System.out.println(p.getProperty("user.home") + " - " + 
				p.getProperty("os.name") + " - " +
				p.getProperty("os.arch") + " - " +
				p.getProperty("os.version")); 
		/*
		if (calculationDate.get(GregorianCalendar.HOUR_OF_DAY) > dayTime.getHours() || 
				(calculationDate.get(GregorianCalendar.HOUR_OF_DAY) == dayTime.getHours()
				&& calculationDate.get(GregorianCalendar.MINUTE) > dayTime.getMinutes())) {
			//time to change calculation
			nextPrayTime = currentPrayTime;
		}*/
		
		/*
		System.out.println("NextPrayTimeInfo: date.MINUTE = " + calculationDate.get(GregorianCalendar.MINUTE) + ", dateNextPrayTime.MINUTE = " + dateNextPrayTime.get(GregorianCalendar.MINUTE));
		System.out.println("NextPrayTimeInfo: date.HOUR = " + calculationDate.get(GregorianCalendar.HOUR_OF_DAY) + ", dateNextPrayTime.HOUR = " + dateNextPrayTime.get(GregorianCalendar.HOUR_OF_DAY));
		if (currentTime == null || (calculationDate.get(GregorianCalendar.HOUR_OF_DAY) == dateNextPrayTime.get(GregorianCalendar.HOUR_OF_DAY)
				&& calculationDate.get(GregorianCalendar.MINUTE) > dateNextPrayTime.get(GregorianCalendar.MINUTE))) {
			System.out.println("NextPrayTimeInfo: Search next pray time");
			for (Map.Entry<Integer, Time> time : AppConfig.mapTimes.entrySet()) {
				
		//		dt = Util.toDayTime(result.get(time.getValue()).doubleValue(), true);
		//		System.out.println("NextPrayTimeInfo: " + time.getValue().toString() + ", dt.getHours() = " + dt.getHours() + ", dt.getMinutes() = " + dt.getMinutes());
		//		if (dt.getHours() > calculationDate.get(GregorianCalendar.HOUR_OF_DAY) 
		//				|| (dt.getMinutes() > calculationDate.get(GregorianCalendar.MINUTE)
		//					&& dt.getHours() == calculationDate.get(GregorianCalendar.HOUR_OF_DAY))) {
		//			nextTime = time.getValue();
		//			dateNextPrayTime = (GregorianCalendar) calculationDate.clone();
		//			dateNextPrayTime.set(GregorianCalendar.HOUR_OF_DAY, dt.getHours()); 
		//			dateNextPrayTime.set(GregorianCalendar.MINUTE, dt.getMinutes()); 
		//			System. out.println("NextPrayTimeInfo: nextTime = " + nextTime);
					//String message = prayTime.getMethod().getName().toString() + ": " +
					//		"ASR = " + prayTime.getMethod().getAsrFactor() + ", HL = " +
					//		prayTime.getMethod().getHighLatMethod() + ", MN = " + 
					//		prayTime.getMethod().getMidnightMethod() + ", Fajr = " +
					//		prayTime.getMethod().getConfigurationValue(Time.FAJR) +", Magrib = " +
					//		prayTime.getMethod().getConfigurationValue(Time.MAGHRIB) + ", Isha = " +
					//		prayTime.getMethod().getConfigurationValue(Time.ISHA) + ", Dhuhr = " +
					//		prayTime.getMethod().getConfigurationValue(Time.DHUHR)
					//		;
					//JOptionPane.showMessageDialog(null, message);
		//			break;
		//		}
		//	}
		//}
		
		////if (date.get(GregorianCalendar.MINUTE) >= dateNextPrayTime.get(GregorianCalendar.MINUTE)) {
		//	//System.out.println("NextPrayTimeInfo: Minutes Changed");
		//	System.out.println("NextPrayTimeInfo: oldTimeName = " + oldTimeName);
		//	if (!nextTime.equals(oldTimeName)) {
		//		System.out.println("NextPrayTimeInfo: time is new. Refresh Labels, Panels...");
		//		fajrNameLabel.setFont(appFont);
		//		sunriseNameLabel.setFont(appFont);
		//		sunsetNameLabel.setFont(appFont);
		//		dhuhrNameLabel.setFont(appFont);
		//		maghribNameLabel.setFont(appFont);
		//		ishaNameLabel.setFont(appFont);	
		//		asrNameLabel.setFont(appFont);
		//		fajrTimeLabel.setFont(appFont);
		//		sunriseTimeLabel.setFont(appFont);
		//		sunsetTimeLabel.setFont(appFont);
		//		dhuhrTimeLabel.setFont(appFont);
		//		maghribTimeLabel.setFont(appFont);
		//		ishaTimeLabel.setFont(appFont);	
		//		asrTimeLabel.setFont(appFont);
		//		if (nextTime == Time.ASR) {
		//			asrNameLabel.setFont(appFont.deriveFont(Font.BOLD));					
		//			asrTimeLabel.setFont(appFont.deriveFont(Font.BOLD));
		//		}else if (nextTime == Time.FAJR) {
		//			fajrNameLabel.setFont(appFont.deriveFont(Font.BOLD));						
		//			fajrTimeLabel.setFont(appFont.deriveFont(Font.BOLD));
		//		}else if (nextTime == Time.SUNRISE) {
		//			sunriseNameLabel.setFont(appFont.deriveFont(Font.BOLD));
		//			sunriseTimeLabel.setFont(appFont.deriveFont(Font.BOLD));
		//		}else if (nextTime == Time.SUNSET) {
		//			sunsetNameLabel.setFont(appFont.deriveFont(Font.BOLD));					
		//			sunsetTimeLabel.setFont(appFont.deriveFont(Font.BOLD));
		//		}else if (nextTime == Time.DHUHR) {
		//			dhuhrNameLabel.setFont(appFont.deriveFont(Font.BOLD));							
		//			dhuhrTimeLabel.setFont(appFont.deriveFont(Font.BOLD));
		//		}else if (nextTime == Time.MAGHRIB) {
		//			maghribNameLabel.setFont(appFont);					
		//			maghribTimeLabel.setFont(appFont);
		//		}else if (nextTime == Time.ISHA) {
		//			ishaNameLabel.setFont(appFont.deriveFont(Font.BOLD));
		//			ishaTimeLabel.setFont(appFont.deriveFont(Font.BOLD));
		//		}
		//		
		//		stringBuilder.setLength(0);
		//		stringBuilder.append("<html><center><strong>");
		//		stringBuilder.append(I18n.resourceBundle.getString("time_left_to"));
		//		stringBuilder.append(" ");
		//		stringBuilder.append(I18n.resourceBundle.getString(nextTime.toString().toLowerCase()));
		//		stringBuilder.append("</strong>");
		//		stringBuilder.append("</center></html>");
		//		timeLeftTopLabel.setText(stringBuilder.toString());
		//		
		//		oldTimeName = nextTime;
		//	}
		//	
		//	System.out.println("NextPrayTimeInfo: Calculate time before praying.");
		//	dt = Util.toDayTime(result.get(nextTime).doubleValue(), true);
		//	System.out.println("NextPrayTimeInfo: dt.getHours() = " + dt.getHours()  + 
		//			", date.get(GregorianCalendar.HOUR_OF_DAY) = " + calculationDate.get(GregorianCalendar.HOUR_OF_DAY) +
		//			", dt.getMinutes() = " + dt.getMinutes() + ", date.get(GregorianCalendar.MINUTE) = " + calculationDate.get(GregorianCalendar.MINUTE));
		//	int hours = dt.getHours() - calculationDate.get(GregorianCalendar.HOUR_OF_DAY);
		//	int minutes = dt.getMinutes() - calculationDate.get(GregorianCalendar.MINUTE);
		//	hours = minutes < 0 ? hours - 1 : hours;
		//	minutes  = minutes < 0 ? minutes + 60 : minutes;
			
		//	stringBuilder.setLength(0);
		//	stringBuilder.append("<html><center>");
		//	System.out.println("NextPrayTimeInfo: hours = " + hours + ", minutes = " + minutes);
		//	
		//	if (hours == 0 && minutes ==0)
		//	{
		//		//it's time for praying
		//		stringBuilder.append(I18n.resourceBundle.getString("time_to_pray"));
		//		fb.setTime(nextTime);
		//		fb.start();
		//	}
		//	if (hours != 0) {
		//		stringBuilder.append(hours);
		//		stringBuilder.append(" ");
		//		stringBuilder.append(I18n.resourceBundle.getString("hours"));
		//		stringBuilder.append(" ");
		//	}
		//	if (minutes != 0) {
		//		stringBuilder.append(minutes);
		//		stringBuilder.append(" ");
		//		stringBuilder.append(I18n.resourceBundle.getString("minutes"));
		//	}
		//	stringBuilder.append("</center></html>");
		//	timeLeftBottomLabel.setText(stringBuilder.toString());
		//	dateOld = (GregorianCalendar) calculationDate.clone();
		*/	
		//}
		
		/*String message = "You got a new notification message. Isn't it awesome to have such a notification message.";
		String header = "This is header of notification message";
		frame = new JFrame();
		frame.setSize(300,125);
		frame.setAlwaysOnTop(true);
		frame.setUndecorated(true);
		frame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		JLabel headingLabel = new JLabel(header);
		//headingLabel .setIcon(headingIcon); // --- use image icon you want to be as heading image.
		headingLabel.setOpaque(false);
		frame.add(headingLabel, constraints);
		constraints.gridx++;
		constraints.weightx = 0f;
		constraints.weighty = 0f;
		constraints.fill = GridBagConstraints.NONE;
		constraints.anchor = GridBagConstraints.NORTH;
		JButton cloesButton = new JButton(new AbstractAction("X") {
	        
			public void actionPerformed(final ActionEvent e) {
	               frame.dispose();
	        }
		});
		cloesButton.setMargin(new Insets(1, 4, 1, 4));
		cloesButton.setFocusable(false);
		frame.add(cloesButton, constraints);
		constraints.gridx = 0;
		constraints.gridy++;
		constraints.weightx = 1.0f;
		constraints.weighty = 1.0f;
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;
		
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();// size of the screen
		Insets toolHeight = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());// height of the task bar
		frame.setLocation(scrSize.width - frame.getWidth(), scrSize.height - toolHeight.bottom - frame.getHeight());
		
		JLabel messageLabel = new JLabel("<HtMl>"+message);
		frame.add(messageLabel, constraints);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		new Thread(){
		      @Override
		      public void run() {
		           try {
		                  Thread.sleep(5000); // time after which pop up will be disappeared.
		                  frame.dispose();
		           } catch (InterruptedException e) {
		                  e.printStackTrace();
		           }
		      };
		}.start();*/
	}
	/*
	 public static void main(String[] args) {
	 
		GregorianCalendar currentDate = new GregorianCalendar();
		int day = currentDate.get(GregorianCalendar.DAY_OF_MONTH);
		int month = currentDate.get(GregorianCalendar.MONTH) + 1;
		int year = currentDate.get(GregorianCalendar.YEAR);
		int hour = currentDate.get(GregorianCalendar.HOUR);
		int minute = currentDate.get(GregorianCalendar.MINUTE);
				
		System.out.println("===================================================");
		System.out.println("Current date: " + day + "." + month + "." + year + " " + hour + ":" + minute + " maxDays: " + currentDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		for (int i = 0; i < 6; i++) {
			currentDate.set(GregorianCalendar.DAY_OF_MONTH, 1);
			day = currentDate.get(GregorianCalendar.DAY_OF_MONTH);
			month = currentDate.get(GregorianCalendar.MONTH) + 1;
			year = currentDate.get(GregorianCalendar.YEAR);
			hour = currentDate.get(GregorianCalendar.HOUR);
			minute = currentDate.get(GregorianCalendar.MINUTE);
			System.out.println("Set DAY = 1: " +day + "." + month + "." + year + " " + hour + ":" + minute + " maxDays: " + currentDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
			currentDate.add(GregorianCalendar.MONTH, 1);
			day = currentDate.get(GregorianCalendar.DAY_OF_MONTH);
			month = currentDate.get(GregorianCalendar.MONTH) + 1;
			year = currentDate.get(GregorianCalendar.YEAR);
			hour = currentDate.get(GregorianCalendar.HOUR);
			minute = currentDate.get(GregorianCalendar.MINUTE);
			System.out.println("Add MONTH = 1: " +day + "." + month + "." + year + " " + hour + ":" + minute + " maxDays: " + currentDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));	
		}
		System.out.println("===================================================");
		for (int i = 0; i < 6; i++) {
			currentDate.set(GregorianCalendar.DAY_OF_MONTH, 1);
			day = currentDate.get(GregorianCalendar.DAY_OF_MONTH);
			month = currentDate.get(GregorianCalendar.MONTH) + 1;
			year = currentDate.get(GregorianCalendar.YEAR);
			hour = currentDate.get(GregorianCalendar.HOUR);
			minute = currentDate.get(GregorianCalendar.MINUTE);
			System.out.println("Set DAY = 1: " +day + "." + month + "." + year + " " + hour + ":" + minute + " maxDays: " + currentDate.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
			currentDate.add(GregorianCalendar.MONTH, -1);
			day = currentDate.get(GregorianCalendar.DAY_OF_MONTH);
			month = currentDate.get(GregorianCalendar.MONTH) + 1;
			year = currentDate.get(GregorianCalendar.YEAR);
			hour = currentDate.get(GregorianCalendar.HOUR);
			minute = currentDate.get(GregorianCalendar.MINUTE);
			System.out.println("Add MONTH = 1: " +day + "." + month + "." + year + " " + hour + ":" + minute + " maxDays: " + currentDate.getMaximum(GregorianCalendar.DAY_OF_MONTH));	
		}
		
	}
	*/
	 /*
	public void insertTranslationFile() {
		//public static void copyResourceToDir( Class cls, String name, File dir ) throws IOException {
		    //String packageDir = cls.getPackage().getName().replace( '.', '/' );
			String packageDir = I18n.class.getPackage().getName().replace( '.', '/' );
		    String path = "/" + packageDir + "/";
		    OutputStream os = new 
		    InputStream is = GroovyClassLoader.class.getResourceAsStream( path );
		    if( is == null ) {
		        throw new IllegalArgumentException( "Resource not found: " + packageDir );
		    }
		    FileUtils.copyInputStreamToFile( is, new File( dir, name ) );
		//}
	}
	
	public static class Main {

	    // 4MB buffer
	    private static final byte[] BUFFER = new byte[4096 * 1024];

	    
	     * copy input to output stream - available in several StreamUtils or Streams classes 
	       
	    public static void copy(InputStream input, OutputStream output) throws IOException {
	        int bytesRead;
	        while ((bytesRead = input.read(BUFFER))!= -1) {
	            output.write(BUFFER, 0, bytesRead);
	        }
	    }

	    public static void test() throws Exception {
	        // read war.zip and write to append.zip
	        ZipFile war = new ZipFile("war.zip");
	        ZipOutputStream append = new ZipOutputStream(new FileOutputStream("append.zip"));

	        // first, copy contents from existing war
	        Enumeration<? extends ZipEntry> entries = war.entries();
	        while (entries.hasMoreElements()) {
	            ZipEntry e = entries.nextElement();
	            System.out.println("copy: " + e.getName());
	            append.putNextEntry(e);
	            if (!e.isDirectory()) {
	                copy(war.getInputStream(e), append);
	            }
	            append.closeEntry();
	        }

	        // now append some extra content
	        ZipEntry e = new ZipEntry("answer.txt");
	        System.out.println("append: " + e.getName());
	        append.putNextEntry(e);
	        append.write("42\n".getBytes());
	        append.closeEntry();

	        // close
	        war.close();
	        append.close();
	    }
	}
	
	
	
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
	
	public static void main(String[] args) throws Exception {
		// read war.zip and write to append.zip
		ZipFile zipFile = new ZipFile(new File("C:\\Users\\zakirovvm\\Desktop\\PrayTimesWidgetT.jar"));
		//zipFile.
		
        ZipOutputStream append = new ZipOutputStream(new FileOutputStream("C:\\Users\\zakirovvm\\Desktop\\PrayTimesWidgetT.jar"));

        // now append some extra content
        ZipEntry e = new ZipEntry("C:\\Users\\zakirovvm\\Desktop\\application_l18n_lt.properties");
        System.out.println("append: " + e.getName());
        append.putNextEntry(e);
        append.write("42\n".getBytes());
        append.closeEntry();

        // close
        append.close();
		System.out.println("BEGIN");
		String newFileName = "application_l18n_lt";
		String fileExtension = "happyfile";
		String fileExtensionInJar = "properties";
        
		String myClassName = "/" + I18n.class.getName().replace('.', '/') + ".class";
		URL urlJar = I18n.class.getResource(myClassName);
		String urlStr = urlJar.toString();
		int from = "jar:file:".length();
		int to = urlStr.indexOf("!/");
		String jarFileName = urlStr.substring(from, to);
		String resourcePath = "/" + I18n.class.getPackage().getName().replace('.', '/');
		String resourceFileInJar = resourcePath + "/" + newFileName + "." + fileExtensionInJar;
        String resourceFile = "C:/Users/zakirovvm/Desktop/" + newFileName + "." + fileExtension; 
        String resourceFileToJar = "C:/Users/zakirovvm/Desktop/" + newFileName + "." + fileExtensionInJar;
        
        System.out.println("myClassName = " + myClassName);
		System.out.println("urlStr = " + urlStr);
		System.out.println("jarFileName = " + jarFileName);
		System.out.println("resourcePath = " + resourcePath);
        System.out.println("resourceFile = " + resourceFile);
        System.out.println("resourceFileToJar = " + resourceFileToJar);
		URL url = null;
		try {
			url = I18n.class.getResource(resourceFileInJar);
		} catch (Exception e) {
			System.out.println("getResource");
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
			System.out.println(e.getMessage());
		}
		if (url == null) {
			System.out.println("here3 " );
			try {
				
				ZipFile zipFile = new ZipFile(jarFileName);
				zipFile.
				
				if (new File(resourceFile).renameTo(new File(resourceFileToJar)))
					updateJarFile(new File(jarFileName.substring(1)), resourcePath.substring(1), new File(resourceFileToJar));
				else
					System.out.println("can not move");
			} catch (IOException e) {
				System.out.println("updateJarFile");
				System.out.println(e.getLocalizedMessage());
				System.out.println(e.toString());
				System.out.println(e.getMessage());
			}
			
		} else {
			System.out.println("here 2");
			System.out.println(url.toString());
		}
		System.out.println("END");
		return;
       
    }
	
	public static void updateJarFile(File srcJarFile, String targetPackage, File ...filesToAdd) throws IOException {
		System.out.println("updateJarFile START");
		File tmpJarFile = File.createTempFile("tempJar", ".tmp");
		//System.out.println("tmpJarFile = " + tmpJarFile.toString());
	    JarFile jarFile = new JarFile(srcJarFile);
	    boolean jarUpdated = false;
	 
	    try {
	        JarOutputStream tempJarOutputStream = new JarOutputStream(new FileOutputStream(tmpJarFile));
	        System.out.println("JarOutputStream");
	        try {
	            //Added the new files to the jar.
	        	System.out.println("for");
	            for(int i=0; i < filesToAdd.length; i++) {
	            	System.out.println("i = " + i);
	                File file = filesToAdd[i];
	                FileInputStream fis = new FileInputStream(file);
	                try {
	                	byte[] buffer = new byte[1024];
	                    int bytesRead = 0;
	                    JarEntry entry = new JarEntry(targetPackage+File.separator+file.getName());
	                    tempJarOutputStream.putNextEntry(entry);
	                    while((bytesRead = fis.read(buffer)) != -1) {
	                        tempJarOutputStream.write(buffer, 0, bytesRead);
	                    }
	 
	                    //System.out.println(entry.getName() + " added.");
	                }
	                finally {
	                    fis.close();
	                }
	            }
	            
	            System.out.println("Copy original");
	            //Copy original jar file to the temporary one.
	            Enumeration<JarEntry> jarEntries = jarFile.entries();
	            while(jarEntries.hasMoreElements()) {
	                JarEntry entry = jarEntries.nextElement();
	                InputStream entryInputStream = jarFile.getInputStream(entry);
	                tempJarOutputStream.putNextEntry(entry);
	                byte[] buffer = new byte[1024];
	                int bytesRead = 0;
	                while ((bytesRead = entryInputStream.read(buffer)) != -1) {
	                    tempJarOutputStream.write(buffer, 0, bytesRead);
	                }
	            }
	            System.out.println("jarUpdated = true");
	            jarUpdated = true;
	        }
	        catch(Exception ex) {
	        	System.out.println("error = " + ex.getMessage());
	        	ex.printStackTrace();
	            tempJarOutputStream.putNextEntry(new JarEntry("stub"));
	        }
	        finally {
	            tempJarOutputStream.close();
	        }
	 
	    }
	    finally {
	        jarFile.close();
	        //System.out.println(srcJarFile.getAbsolutePath() + " closed.");
	 
	        if (!jarUpdated) {
	            tmpJarFile.delete();
	        }
	    }
	 
	    if (jarUpdated) {
	        if (!srcJarFile.delete()) {
	        	System.out.println("srcJarFile.delete() = PROBLEM!!!!!");
	        }
	        tmpJarFile.renameTo(srcJarFile);
	        //System.out.println(srcJarFile.getAbsolutePath() + " updated.");
	    }
	}
	
	
	public static List<String> getResourceFiles( String path ) throws IOException {
		List<String> filenames = new ArrayList<String>();
		InputStream in = getResourceAsStream( path );
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String resource;
		while( (resource = br.readLine()) != null ) {
			filenames.add( resource );
		}
		return filenames;
	}

	private static InputStream getResourceAsStream( String resource ) {
		final InputStream in = getContextClassLoader().getResourceAsStream( resource );
		return in == null ? TestClass.class.getResourceAsStream(resource) : in;
	}

	private static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	public static void main(String[] args) {
		List<String> fileList = new ArrayList<String>();
		try {
			fileList = getResourceFiles("com/koylubaevnt/jpraytimes/l18n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String string : fileList) {
			System.out.println(string);
		}
	}
	*/
}
