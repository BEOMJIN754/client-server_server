import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class Server extends UnicastRemoteObject implements ServerIF {

    private static DataIF data;
    private static final long serialVersionUID = 1L;
    private Map<String, String> sessionMap = new HashMap<>(); // 초기화 추가

    protected Server() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            Naming.rebind("Server", server);
            ServerLogger.getLogger().log(Level.INFO, "Server is ready.");
            data = (DataIF) Naming.lookup("Data");
        } catch (Exception e) {
            ServerLogger.getLogger().log(Level.SEVERE, "Server setup failed.");
            e.printStackTrace();
        }
    }


    private void log(Level level, String message) {
        ServerLogger.getLogger().log(level, message);
    }

    @Override
    public ArrayList<Student> getAllStudentData(String sessionId) throws RemoteException, NullDataException {
        if (verifySession(sessionId)) {
            log(Level.INFO, "Fetched all student data.");
            return data.getAllStudentData();
        } else {
            log(Level.WARNING, "Invalid session for fetching student data.");
            throw new RemoteException("Invalid session.");
        }
    }

    @Override
    public ArrayList<Course> getAllCourseData(String sessionId) throws RemoteException, NullDataException {
        if (verifySession(sessionId)) {
            log(Level.INFO, "Fetched all course data.");
            return data.getAllCourseData();
        } else {
            log(Level.WARNING, "Invalid session for fetching course data.");
            throw new RemoteException("Invalid session.");
        }
    }

    @Override
    public ArrayList<Registration> getAllRegistrationData(String sessionId) throws RemoteException, NullDataException {
        if (verifySession(sessionId)) {
            log(Level.INFO, "Fetched all registration data.");
            return data.getAllRegistrationData();
        } else {
            log(Level.WARNING, "Invalid session for fetching registration data.");
            throw new RemoteException("Invalid session.");
        }
    }

    @Override
    public boolean addStudent(String studentInfo, String sessionId) throws RemoteException {
        if (verifySession(sessionId)) {
            boolean result = data.addStudent(studentInfo);
            log(Level.INFO, "Added student. Info: " + studentInfo);
            return result;
        } else {
            log(Level.WARNING, "Invalid session for adding student.");
            throw new RemoteException("Invalid session.");
        }
    }

    @Override
    public boolean deleteStudent(String studentId, String sessionId) throws RemoteException {
        if (verifySession(sessionId)) {
            boolean result = data.deleteStudent(studentId);
            log(Level.INFO, "Deleted student. ID: " + studentId);
            return result;
        } else {
            log(Level.WARNING, "Invalid session for deleting student.");
            throw new RemoteException("Invalid session.");
        }
    }

    @Override
    public boolean addCourse(String courseInfo, String sessionId) throws RemoteException {
        if (verifySession(sessionId)) {
            boolean result = data.addCourse(courseInfo);
            log(Level.INFO, "Added course. Info: " + courseInfo);
            return result;
        } else {
            log(Level.WARNING, "Invalid session for adding course.");
            throw new RemoteException("Invalid session.");
        }
    }

    @Override
    public boolean deleteCourse(String courseId, String sessionId) throws RemoteException {
        if (verifySession(sessionId)) {
            boolean result = data.deleteCourse(courseId);
            log(Level.INFO, "Deleted course. ID: " + courseId);
            return result;
        } else {
            log(Level.WARNING, "Invalid session for deleting course.");
            throw new RemoteException("Invalid session.");
        }
    }

    @Override
    public boolean registerCourse(String studentId, String courseId, String sessionId) throws RemoteException, WrongInputException {
        if (verifySession(sessionId)) {
            try {
                checkId(studentId, courseId);
                boolean result = data.registerCourse(studentId, courseId);
                log(Level.INFO, "Registered course. Student ID: " + studentId + ", Course ID: " + courseId);
                return result;
            } catch (NullDataException e) {
                log(Level.SEVERE, "Error in course registration for Student ID: " + studentId + ", Course ID: " + courseId);
                e.printStackTrace();
                return false;
            }
        } else {
            log(Level.WARNING, "Invalid session for course registration.");
            throw new RemoteException("Invalid session.");
        }
    }

    private boolean checkId(String studentId, String courseId) throws RemoteException, NullDataException, WrongInputException {
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
            log(Level.WARNING, "Student ID: " + studentId + " missing prerequisite for Course ID: " + courseId);
            return false;
        }
        return true;
    }

    @Override
    public boolean addUserRecords(String userInfo) throws RemoteException {
        boolean result = data.addUserRecords(userInfo);
        log(Level.INFO, "Added user records. Info: " + userInfo);
        return result;
    }

    @Override
    public boolean deleteUserRecords(String userId) throws RemoteException {
        boolean result = data.deleteUserRecords(userId);
        log(Level.INFO, "Deleted user records. ID: " + userId);
        return result;
    }

    @Override
    public String authenticateUser(String userId, String userPw) throws RemoteException, NullDataException {
        User selectedUser = data.getUserById(userId);
        if (selectedUser != null && selectedUser.getUserPw().equals(userPw)) {
            String sessionId = UUID.randomUUID().toString();
            sessionMap.put(sessionId, userId);
            log(Level.INFO, "User authenticated successfully. User ID: " + userId);
            return sessionId;
        }
        log(Level.WARNING, "Authentication failed for user: " + userId);
        return null;
    }

    @Override
    public boolean verifySession(String sessionId) throws RemoteException {
        boolean isValid = sessionMap.containsKey(sessionId);
        if (!isValid) {
            log(Level.WARNING, "Session verification failed for Session ID.");
        }
        return isValid;
    }

    @Override
    public boolean logoutUser(String sessionId) throws RemoteException {
        if (sessionMap.remove(sessionId) != null) {
            log(Level.INFO, "User logged out successfully.");
            return true;
        }
        log(Level.WARNING, "Logout failed for non-existent Session ID.");
        return false;
    }

    @Override
    public boolean signUp(String userId, String userPw) throws NullDataException, RemoteException {
        checkUserId(userId);
        boolean result = data.addUserRecords(userId + " " + userPw);
        log(Level.INFO, "User signed up. User ID: " + userId);
        return result;
    }

    private boolean checkUserId(String userId) throws NullDataException, RemoteException {
        User selectedUser = data.getUserById(userId);
        if (selectedUser != null) {
            log(Level.WARNING, "Attempted sign up with existing User ID: " + userId);
            System.out.println("이미 존재하는 ID 입니다.");
            return false;
        }
        return true;
    }
}
