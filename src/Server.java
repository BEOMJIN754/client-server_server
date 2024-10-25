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
		}}
	@Override
	public ArrayList<Student> getAllStudentData() throws RemoteException,NullDataException {
		return data.getAllStudentData();
	}
	@Override
	public ArrayList<Course> getAllCourseData() throws RemoteException, NullDataException{
		// TODO Auto-generated method stub
		return data.getAllCourseData();
	}
	@Override
	public ArrayList<Registration> getAllRegistrationData() throws RemoteException, NullDataException {
		return data.getAllRegistrationData();
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
	@Override
	public boolean registerCourse(String studentId, String courseId) throws RemoteException, WrongInputException {
		    try {
		        checkId(studentId, courseId);
		        if(data.registerCourse(studentId,courseId))return true;
		        else return false;
		    } catch (NullDataException e) {
		        e.printStackTrace();
		        return false;
		    }
		}
	private boolean checkId(String studentId, String courseId)
			throws RemoteException, NullDataException, WrongInputException {
		Student selectedStudent = data.getStudentById(studentId);
		if (selectedStudent == null) {
			throw new WrongInputException("Error: Student is not registered.");
		}
		Course selectedCourse = data.getCourseById(courseId);
		if (selectedCourse == null) {
		   throw new WrongInputException("Error: Course is not registered");
		}
		String prerequisite = selectedCourse.getpreRequisite();
		if (!prerequisite.isEmpty() && !selectedStudent.getCompletedCourses().contains(prerequisite)) {
		    System.out.println("Error: Prerequisite not completed.");
		    return false;
		}
		return true;
	}
	
	@Override
	public boolean addUserRecords(String usetInfo) throws RemoteException {
		if(data.addUserRecords(usetInfo))return true;
		else return false;
	}
	@Override
	public boolean deleteUserRecords(String userId) throws RemoteException {
		if(data.deleteUserRecords(userId))return true;
		else return false;
	}
	@Override
	public boolean authenticateUser(String userId, String userPw) throws RemoteException, NullDataException {
		User selectedUser = data.getUserById(userId);
		if(selectedUser !=null && selectedUser.getUserPw().equals(userPw)) {
			return true;
		}
		return false;
	}
	@Override
	public boolean signUp(String userId, String userPw) throws NullDataException, RemoteException {
		checkUserId(userId);
		if(data.addUserRecords(userId+" "+userPw))return true;
		else return false;
	}
	private boolean checkUserId(String userId) throws NullDataException, RemoteException {
		User selectedUser = data.getUserById(userId);
		if(!(selectedUser==null)) {
			System.out.println("이미 존재하는 ID 입니다.");
			return false;
		}
		return true;
	}

}
