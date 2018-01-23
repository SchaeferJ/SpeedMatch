package speedmatch;
import java.net.InetAddress;
import java.sql.*;
import java.util.ArrayList;
import java.io.*;
import javax.sql.*;
import javax.swing.JOptionPane;



public class DBConnect {

	private Connection con;
	private String event;
	private String username = "speedmatch";
	private String password = "loveisintheair!";
	private String tmpdir = System.getProperty("java.io.tmpdir")+Processing.getPathSep();

	public DBConnect(String eventname) {
		con = null;
		this.event = DBTools.sanitize(eventname);
	}

	public DBConnect(Dating d) {
		con = null;
		this.event = DBTools.sanitize(d.getName());
	}

	public void setEventname(String eventname) {
		this.event = DBTools.sanitize(eventname);
	}

	public DBConnect() {
		con=null;
	}

	// Allows to specify user-defined login credentials.

	public void setLogin(String usr, String pwd) {
		this.username = usr;
		this.password = pwd;
	}

	// Searches for database server and establishes DB-connection.

	public void initConnection() {
		this.initConnection(DBTools.getDevices());
	}

	// Establishes DB-connection using predefined IP

	public void initConnection(InetAddress ia) {
		try {
			String ip = ia.getHostAddress();
			String url = "jdbc:mysql://"+ip+":3306/speedmatch";
			Class.forName ("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection (url,username,password);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Unable to connect to database: "+Processing.getLineSep()+e.getMessage()+ Processing.getLineSep() + Processing.getLineSep()+"Check if username and password are correct, if you have selected the right server and"+Processing.getLineSep()+"if you have granted remote access permissions to the user in the RDBMS", "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Creates a table for a specific event

	public void createEventTable() {
		try {
			StringBuilder stm = new StringBuilder("CREATE TABLE ");
			stm.append(event);
			stm.append(" (id INTEGER AUTO_INCREMENT, fname varchar(20), lname varchar(20), gender INTEGER, fa VARCHAR(50), dik VARCHAR(50), ereig VARCHAR(50), feier VARCHAR(50), rev VARCHAR(50), PRIMARY KEY(id));");
			PreparedStatement pstm = con.prepareStatement(stm.toString());
			pstm.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error while updating Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Adds a participant to the Database

	public int insertParticipant(String fname, String lname, int gender, String fa, String dik, String ereig, String feier, String rev) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO "+event+" (fname, lname, gender, fa, dik, ereig, feier, rev) VALUES (?,?,?,?,?,?,?,?);");
			ps.setString(1, fname);
			ps.setString(2, lname);
			ps.setInt(3, gender);
			ps.setString(4, fa);
			ps.setString(5, dik);
			ps.setString(6, ereig);
			ps.setString(7, feier);
			ps.setString(8, rev);
			ps.executeUpdate();
			ps.close();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT LAST_INSERT_ID();");
			int id = -99;
			while(rs.next()) {
				id = rs.getInt(1);
			}
			s.close();
			return id;
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while updating Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
			return -99;
		}
	}

	// Retrieves information from database and adds them to a dating-object

	public Dating dbToEvent(Dating d) {
		try {
			boolean replace = true;
			if (d.getNetParticipants().size()>0) {
				int uin = JOptionPane.showConfirmDialog(null, "Your event already contains participants. Do you want to replace them?","Warning",JOptionPane.YES_NO_OPTION);
				if(uin == JOptionPane.NO_OPTION) {
					replace=false;
				}
			}
			if(replace) {
				d.clearParticipants();
				int count = 0;
				StringBuilder sb = new StringBuilder("SELECT * FROM ");
				sb.append(DBTools.sanitize(d.getName()));
				sb.append(";");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sb.toString());
				while(rs.next()) {
					int id = rs.getInt("id");
					String fname = rs.getString("fname");
					String lname = rs.getString("lname");
					int gnd = rs.getInt("gender");
					String fa = rs.getString("fa");
					String dik = rs.getString("dik");
					String ereig = rs.getString("ereig");
					String feier = rs.getString("feier");
					String rev = rs.getString("rev");
					d.addParticipant(id, fname, lname, gnd, fa, dik, ereig, feier, rev);
					d.addToFile(tmpdir);
					count++;
				}
				JOptionPane.showMessageDialog(null, "Successfully loaded "+count+" Participants from Database.", "Success", JOptionPane.INFORMATION_MESSAGE);
			return d;
			}
		}catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Error while reading from Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
		}catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An unexpected error occured: "+e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		finally {
			return null; //Should never be reached
		}
	}

	public void deletePartByID(int id) {
		try {
			StringBuilder sb = new StringBuilder("DELETE FROM ");
			sb.append(event);
			sb.append(" WHERE id = ?;");
			PreparedStatement ps = con.prepareStatement(sb.toString());
			ps.setInt(1, id);
			ps.executeUpdate();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while reading from Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void delete() {
		try {
			String sql = "DROP TABLE "+this.event+";";
			String sq2 = "DELETE FROM zzzmemory WHERE name = '"+this.event+"';";
			Statement stm = con.createStatement();
			stm.executeUpdate(sql);
			stm.executeUpdate(sq2);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while updating Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Returns an array containing the names of all tables in the Database,
	// i.e. the names of all events

	public String[] getEventsFromDB() {
		try {
			ArrayList<String> elist = new ArrayList<String>();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("show tables;");
			while(rs.next()) {
				String tmp = rs.getString(1);
				if(!tmp.startsWith("zzz")) {
					elist.add(tmp);
				}
			}
			String[] earr = new String[elist.size()];
			earr = elist.toArray(earr);
			return earr;
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while reading from Database "+e.getMessage(), "DATABASE ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	//Checks whether there is already a table with the same name
	public boolean eventExists() {
		String[] ea = this.getEventsFromDB();
		for(int i=0; i<ea.length; i++) {
			if(ea[i].equals(this.event)) {
				return true;
			}
		}
		return false;
	}

	// Stored a Dating-Object as BLOB in Database

	public void storeInDB(Dating d) {
		try {
			PreparedStatement ps = con.prepareStatement("INSERT INTO zzzmemory VALUES (?,?,?);");
			File tmp = File.createTempFile(d.getName(), ".tmp");
			FileOutputStream fout = new FileOutputStream(tmp);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(d);
			oout.close();
			fout.close();
			FileInputStream fin = new FileInputStream(tmp);
			ps.setString(1, d.getName());
			ps.setInt(2, d.getMaxPart());
			ps.setBinaryStream(3, fin);
			ps.executeUpdate();
			fin.close();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "An unexpected error occured while writing BLOB to DB: "+Processing.getLineSep()+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//Updates BLOB on Server
	
	public void updateFile(Dating d) {
		try {
			PreparedStatement ps = con.prepareStatement("UPDATE zzzmemory SET file=? WHERE name=?;");
			File tmp = File.createTempFile(d.getName(), ".tmp");
			FileOutputStream fout = new FileOutputStream(tmp);
			ObjectOutputStream oout = new ObjectOutputStream(fout);
			oout.writeObject(d);
			oout.close();
			fout.close();
			FileInputStream fin = new FileInputStream(tmp);
			ps.setBinaryStream(1, fin);
			ps.setString(2, d.getName());
			ps.executeUpdate();
			fin.close();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "An unexpected error occured while updating BLOB in DB: "+Processing.getLineSep()+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Reads the BLOB from the Database and returns it as a Dating-Object
	
	public Dating readDatingFromDB(String name) {
		try {
			File tmp = File.createTempFile(name+"_in", ".tmp");
			FileOutputStream out = new FileOutputStream(tmp);
			PreparedStatement ps = con.prepareStatement("SELECT file FROM zzzmemory WHERE name = ?;");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				InputStream in = rs.getBinaryStream("file");
				byte[] buffer = new byte[1024];
				while(in.read(buffer)>0) {
					out.write(buffer);
				}
			}
			FileInputStream fin = new FileInputStream(tmp);
			ObjectInputStream oin = new ObjectInputStream(fin);
			Dating d = (Dating) oin.readObject();
			oin.close();
			fin.close();
			return d;
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "An unexpected error occured while reading BLOB from DB: "+Processing.getLineSep()+e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	// Closes Database Connection

	public void close() {
		try {
			con.close();
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error while closing Database Connection: "+e.getMessage(), "CONNECTION ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}


}
