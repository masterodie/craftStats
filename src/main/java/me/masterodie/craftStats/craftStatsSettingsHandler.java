package me.masterodie.craftStats;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class craftStatsSettingsHandler {

	public File file;
	public String name;
	public String saveFile;
	private HashMap <String, String> FileContents = new HashMap <String, String>();
	Logger log = Logger.getLogger("Minecraft");
	
	public craftStatsSettingsHandler(String name, String saveFile) {
		this.saveFile = saveFile;
		this.name = name;
	}
	
	public void create(String name) {
		
		if(getClass().getResource(name) == null) {
			log.severe("[craftStats] No Settings file specified!");
			return;
		} 
		
		InputStream input = getClass().getResourceAsStream(name);
		
		if(input != null) {
			FileOutputStream fout = null;
			
			try {
				fout = new FileOutputStream(file);
				byte[] buffer = new byte[8192];
				int length = 0;
				
				while((length = input.read(buffer)) > 0) {
					fout.write(buffer, 0, length);
				}
			} catch (Exception e) {
	          e.printStackTrace();
	        } finally {
	          try {
	            if (input != null)
	              input.close();
	          } catch (Exception e) {
	          }
	          try {
	            if (fout != null)
	              fout.close();
	          }
	          catch (Exception e)
	          {
	          }
	        }
			
		}
	}
	
	public boolean load() {
		if(file == null) {
			this.file = new File(saveFile);
		}
				
		if(!file.exists()) {
			this.create(name);
		}
		
		this.FileContents = loadFileContents();
		return true;
	}
	
	private HashMap<String, String> loadFileContents() {
		HashMap<String, String> result = new HashMap<String, String>();
		
		try {
			BufferedReader bufRead = new BufferedReader(new FileReader(file));
			String word = null;
			
			while(((word = bufRead.readLine()) != null)) {
				if ((word.isEmpty()) || (word.startsWith("#")) || (!word.contains(":"))) continue;
				String[] args = word.split(":");
				result.put(args[0], args[1]);
				
			}
			
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String getPropertyString(String property) {
		
		try {
			if (FileContents.containsKey(property)) {
				return FileContents.get(property);
			}
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public int getPropertyInteger(int property) {
		String result = null;
		
		try {
			if(FileContents.containsKey(property)) {
				result = FileContents.get(property);
				return Integer.valueOf(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -10;
	}
	
	
	public boolean getPropertyBoolean(boolean property) {
		String result = null;
		
		try {
			if(FileContents.containsKey(property)) {
				result = FileContents.get(property);
				if (result.equalsIgnoreCase("true") || result.equalsIgnoreCase("false")) {
					return Boolean.valueOf(result);
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

public double getPropertyDouble(double property) {
	String result = null;
	
	try {
		if(FileContents.containsKey(property)) {
			result = FileContents.get(property);
			return Double.valueOf(result);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
		return -10.0;
	}

public Boolean isValidProperty(String property) {
	
	if (this.FileContents.containsKey(property)) {
		return true;
	}
	
		return false;
	}
}
