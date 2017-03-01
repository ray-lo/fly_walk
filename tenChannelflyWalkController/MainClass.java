package tenChannelflyWalkController;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class MainClass {
	//loadJarDll is from http://stackoverflow.com/questions/4691095/java-loading-dlls-by-a-relative-path-and-hide-them-inside-a-jar
	public static void loadJarDll(String name) throws Exception {
		name = System.mapLibraryName(name);
		System.out.println(name);
	    InputStream in = MainClass.class.getResourceAsStream(name);
	    byte[] buffer = new byte[1024];
	    int read = -1;
	    File temp = new File(new File(System.getProperty("java.io.tmpdir")), name);
	
	    FileOutputStream fos = new FileOutputStream(temp);

	    	System.out.println(in);
	    while((read = in.read(buffer)) != -1) {
	        fos.write(buffer, 0, read);
	    }
	    fos.close();
	    in.close();
	    addLibraryPath(temp.getParent());
	    System.load(temp.getAbsolutePath());
	}
	//addLibaryPath is from http://stackoverflow.com/questions/15409223/adding-new-paths-for-native-libraries-at-runtime-in-java
	public static void addLibraryPath(String pathToAdd) throws Exception{
	    final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
	    usrPathsField.setAccessible(true);

	    //get array of paths
	    final String[] paths = (String[])usrPathsField.get(null);

	    //check if the path to add is already present
	    for(String path : paths) {
	        if(path.equals(pathToAdd)) {
	            return;
	        }
	    }

	    //add the new path
	    final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
	    newPaths[newPaths.length-1] = pathToAdd;
	    usrPathsField.set(null, newPaths);
	}
	public static void main ( String[] args ) throws Exception{
		
	//	loadJarDll("rxtxSerial");
		//System.out.println("loadedLib dudessss");
	//	ControllerThread c1 = new ControllerThread();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println(System.getProperty("java.library.path"));
					NewGUI window = new NewGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	
		/*
		try {
			c1.establishSerialConnection("/dev/tty.usbmodem1411");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		ChannelAndDuration cd1 = new ChannelAndDuration(1, 1000000000L);
		ChannelAndDuration cd2 = new ChannelAndDuration(2, 1000000000L);
		ChannelAndDuration cd3 = new ChannelAndDuration(3, 1000000000L);
		ChannelAndDuration cd4 = new ChannelAndDuration(4, 1000000000L);
		ChannelAndDuration[] cdArray = {cd1, cd2, cd3, cd4};
		Setting s1 = new Setting(cdArray);
		c1.updateSetting(s1);
		c1.setTotalCycles(100);
		c1.start();
	
		//System.out.p

		//c1.in.read();
		 * 
		 */
	}

}
