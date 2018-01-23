package speedmatch;
import java.io.*;

import javax.swing.JOptionPane;

/**
 *
 * @author Jochen
 * 
 */


public class Processing {

	// Detects which operating system SpeedMatch is running on

	public static String detectOS() {
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("win")) {
			return "Windows";
		}
		else if(os.contains("nix")||os.contains("nux")||os.contains("mac")) {
			return "Unix";
		}
		else {
			return "other";
		}
	}
	
	// Returns Path separator
	
	public static char getPathSep() {
		char sep = '/';
		if(detectOS().equals("Windows")) {
			sep = '\\';
		}
		return sep;
	}

	// Returns the working directory of the program in system-specific formatting

	public static String getHomeDir() {
		return System.getProperty("user.home")+ getPathSep()+"SpeedMatch"+getPathSep();
	}

	// Get system-specific Line separator

	public static String getLineSep() {
		String newline = "\n";
		if(detectOS().equals("Windows")) {
			newline = "\r\n";
		}
		return newline;
	}

	// Serializes a Dating-Object and writes it to a .sdo file in a given
	// directory

	public static void saveDating(Dating d, String dir) throws MatchException {
		String outdir = dir + d.getName() + ".sdo";
		File outfile = new File(outdir);
		try {
			FileOutputStream fout = new FileOutputStream(outfile);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(d);
			oout.flush();
			fout.flush();
			oout.close();
			fout.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	// OVERRIDE: Same as above but using default directory

	public static void saveDating(Dating d) throws MatchException {
		String outdir = getHomeDir() + d.getName() + ".sdo";
		File outfile = new File(outdir);
		try {
			FileOutputStream fout = new FileOutputStream(outfile);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(d);
			oout.flush();
			fout.flush();
			oout.close();
			fout.close();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Reads saved dating from disk

	public static Dating readDating(String path) throws MatchException{
		File infile = new File(path);
		if(infile.isDirectory()) {
			throw new MatchException("Error: You specified a directory where a file is required");
		}
		if(!infile.canRead()) {
			throw new MatchException("Error: Cannot read input file");
		}
		try {
			FileInputStream fin = new FileInputStream(infile);
			ObjectInputStream oin = new ObjectInputStream(fin);
			Dating d = (Dating) oin.readObject();
			oin.close();
			fin.close();
			return d;
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	// Same as above, but using default directory
	public static Dating readDatingFromWD(String filename) throws MatchException{
		String indir = getHomeDir() + filename;
		File infile = new File(indir);
		if(infile.isDirectory()) {
			throw new MatchException("Error: You specified a directory where a file is required");
		}
		if(!infile.canRead()) {
			throw new MatchException("Error: Cannot read input file");
		}
		try {
			FileInputStream fin = new FileInputStream(infile);
			ObjectInputStream oin = new ObjectInputStream(fin);
			Dating d = (Dating) oin.readObject();
			oin.close();
			fin.close();
			return d;
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error: "+e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	// Returns an array containing the Names of .sdo-files in a specific directory

	public static String[] getDatingsDir(String path) throws IOException {
		File dir = new File(path);
		if(dir.isDirectory()) {
			String[] farr = dir.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".sdo");
				}});
			return farr;
		}
		else {
			throw new IOException("Not a directory");
		}
	}

	// OVERRIDE: Same as above, but using default directory

	public static String[] getDatingsDir() throws IOException{
		return getDatingsDir(getHomeDir());
	}
	
	//Delete a file from directory
	
	public static void deleteFile (String path, String filename) {
		File f = new File(path+filename);
		if(f.exists()) {
			try {
				f.delete();
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "An unknown error occured while deleting the file", "ERROR", JOptionPane.ERROR_MESSAGE);;
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Unable to access file. Please check read/write permissions", "ERROR", JOptionPane.ERROR_MESSAGE);;
		}
	}
	
	//OVERRIDE: Same as above, but using default directory
	
	public static void deleteFile(String filename) {
		deleteFile(getHomeDir(),filename);
	}
	
	// Writes a String to a file
}
