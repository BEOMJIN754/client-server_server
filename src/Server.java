import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Server extends UnicastRemoteObject implements ServerIF {

	private static DataIF data;
	private static final long serialVersionUID = 1L;
	private Map<String, String> sessionMap = new HashMap<>(); // 초기화 추가

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

			data = (DataIF) Naming.lookup("Data");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	 @Override
	    public ArrayList<Student> getAllStudentData(String sessionId) throws RemoteException, NullDataException {
	        if (verifySession(sessionId)) {
	            return data.getAllStudentData();
	        } else {
	            throw new RemoteException("Invalid session.");
	        }
	    }

	    @Override
	    public ArrayList<Course> getAllCourseData(String sessionId) throws RemoteException, NullDataException {
	        if (verifySession(sessionId)) {
	            return data.getAllCourseData();
	        } else {
	            throw new RemoteException("Invalid session.");
	        }
	    }

	    @Override
	    public ArrayList<Registration> getAllRegistrationData(String sessionId) throws RemoteException, NullDataException {
	        if (verifySession(sessionId)) {
	            return data.getAllRegistrationData();
	        } else {
	            throw new RemoteException("Invalid session.");
	        }
	    }

	    @Override
	    public boolean addStudent(String studentInfo, String sessionId) throws RemoteException {
	        if (verifySession(sessionId)) {
	            return data.addStudent(studentInfo);
	        } else {
	            throw new RemoteException("Invalid session.");
	        }
	    }

	    @Override
	    public boolean deleteStudent(String studentId, String sessionId) throws RemoteException {
	        if (verifySession(sessionId)) {
	            return data.deleteStudent(studentId);
	        } else {
	            throw new RemoteException("Invalid session.");
	        }
	    }

	    @Override
	    public boolean addCourse(String courseInfo, String sessionId) throws RemoteException {
	        if (verifySession(sessionId)) {
	            return data.addCourse(courseInfo);
	        } else {
	            throw new RemoteException("Invalid session.");
	        }
	    }

	    @Override
	    public boolean deleteCourse(String courseId, String sessionId) throws RemoteException {
	        if (verifySession(sessionId)) {
	            return data.deleteCourse(courseId);
	        } else {
	            throw new RemoteException("Invalid session.");
	        }
	    }

	    @Override
	    public boolean registerCourse(String studentId, String courseId, String sessionId) throws RemoteException, WrongInputException {
	        if (verifySession(sessionId)) {
	            try {
	                checkId(studentId, courseId);
	                return data.registerCourse(studentId, courseId);
	            } catch (NullDataException e) {
	                e.printStackTrace();
	                return false;
	            }
	        } else {
	            throw new RemoteException("Invalid session.");
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
		if (data.addUserRecords(usetInfo))
			return true;
		else
			return false;
	}

	@Override
	public boolean deleteUserRecords(String userId) throws RemoteException {
		if (data.deleteUserRecords(userId))
			return true;
		else
			return false;
	}

	@Override
	public String authenticateUser(String userId, String userPw) throws RemoteException, NullDataException {
		User selectedUser = data.getUserById(userId);
		if (selectedUser != null && selectedUser.getUserPw().equals(userPw)) {
			// 세션 ID 생성
			String sessionId = UUID.randomUUID().toString();
			sessionMap.put(sessionId, userId);
			System.out.println(sessionMap);
			System.out.println("Login successful. Session ID: " + sessionId);
			return sessionId;
		}
		return null; // 인증 실패 시 null 반환
	}

	@Override
	public boolean verifySession(String sessionId) throws RemoteException {
		return sessionMap.containsKey(sessionId);
	}
	
	 @Override
	    public boolean logoutUser(String sessionId) throws RemoteException {
	        if (sessionMap.remove(sessionId) != null) {
	            System.out.println("Logout successful. Session ID: " + sessionId);
	            return true;
	        }
	        return false;
	    }
	
	@Override
	public boolean signUp(String userId, String userPw) throws NullDataException, RemoteException {
		checkUserId(userId);
		if (data.addUserRecords(userId + " " + userPw))
			return true;
		else
			return false;
	}

	private boolean checkUserId(String userId) throws NullDataException, RemoteException {
		User selectedUser = data.getUserById(userId);
		if (!(selectedUser == null)) {
			System.out.println("이미 존재하는 ID 입니다.");
			return false;
		}
		return true;
	}

}
