import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.xml.crypto.Data;

public class Server extends UnicastRemoteObject implements ServerIF{
	
	private static DataIF data;
	private static final long serialVersionUID = 1L;
	
	protected Server() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		try {
			Server server = new Server();
			// 브로커 등록
			Naming.rebind("Server", server);
			System.out.println("Server is ready!!");
			
			data = (DataIF)Naming.lookup("Data");
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	@Override
	public ArrayList<Student> getAllStudentData() throws RemoteException {
		// TODO Auto-generated method stub
		return data.getAllStudentData();
	}
	@Override
	public ArrayList<Course> getAllCourseData() throws RemoteException {
		// TODO Auto-generated method stub
		return data.getAllCourseData();
	}
}
