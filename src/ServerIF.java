import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerIF extends Remote{
	// 리모트를 위한 인터페이스이다!!! 리모트가 들어가야 외부에서 사용 가능한 스켈레톤 이 된다.
	ArrayList<Student> getAllStudentData(String sessionId) throws RemoteException, NullDataException;
	ArrayList<Course> getAllCourseData(String sessionId) throws RemoteException, NullDataException;
	ArrayList<Registration> getAllRegistrationData(String sessionId) throws RemoteException, NullDataException;
	boolean addStudent(String studentInfo, String sessionId) throws RemoteException;
	boolean deleteStudent(String studentId, String sessionId) throws RemoteException;
	boolean addCourse(String courseInfo, String sessionId) throws RemoteException;
	boolean deleteCourse(String courseId, String sessionId) throws RemoteException;
	boolean registerCourse(String studentId, String courseId, String sessionId) throws RemoteException, WrongInputException;
	String authenticateUser(String userId, String userPw)throws RemoteException, NullDataException;
	boolean addUserRecords(String usetInfo) throws RemoteException;
	boolean deleteUserRecords(String userId) throws RemoteException;
	boolean signUp(String userId, String userPw) throws RemoteException, NullDataException;
	boolean verifySession(String sessionId) throws RemoteException;
	boolean logoutUser(String sessionId) throws RemoteException;
}
