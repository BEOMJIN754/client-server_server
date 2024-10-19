import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

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
	public ArrayList<Student> getAllStudentData() throws RemoteException,NullDataException {
		// TODO Auto-generated method stub
		return data.getAllStudentData();
	}
	@Override
	public ArrayList<Course> getAllCourseData() throws RemoteException, NullDataException{
		// TODO Auto-generated method stub
		return data.getAllCourseData();
	}
	@Override
	public boolean addStudent(String studentInfo) throws RemoteException {
		if(data.addStudent(studentInfo))return true;
		else return false;
	}
	@Override
	public boolean deleteStudent(String studentId) throws RemoteException {
		if(data.deleteStudent(studentId))return true;
		else return false;
	}
	@Override
	public boolean addCourse(String courseInfo) throws RemoteException {
		if(data.addCourse(courseInfo))return true;
		else return false;
	}
	@Override
	public boolean deleteCourse(String courseId) throws RemoteException {
		if(data.deleteCourse(courseId))return true;
		else return false;
	}
}
