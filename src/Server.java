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
                Student selectedStudent = checkId(studentId, courseId);
                if (selectedStudent == null) {
                    return false;
                }
                boolean result = data.registerCourse(studentId,courseId);
                if (result) {
                    selectedStudent.registerCourse(courseId); // 학생 객체에 과목 ID 등록
                    log(Level.INFO, "Registered course. Student ID: " + studentId + ", Course ID: " + courseId);
                }
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


    private Student checkId(String studentId, String courseId) throws RemoteException, NullDataException {
        Student selectedStudent = data.getStudentById(studentId);
        if (selectedStudent == null) {
            System.out.println("Error: The student is not registered.");
            return null;
        }

        Course selectedCourse = data.getCourseById(courseId);
        if (selectedCourse == null) {
            System.out.println("Error: The course is not registered.");
            return null;
        }

        String prerequisite = selectedCourse.getpreRequisite();

        // 선수 과목 미이수 확인
        if (!prerequisite.isEmpty() && !selectedStudent.getCompletedCourses().contains(prerequisite)) {
            System.out.println("The student has not completed the prerequisite course.");
            return null;
        }

        // 중복 과목 수강 확인
        if (selectedStudent.getCompletedCourses().contains(courseId)) {
            System.out.println("The student has already completed this course.");
            log(Level.SEVERE, "Error in course registration for Student ID: " + studentId + ", Course ID: " + courseId);
            
            return null;
        }

        return selectedStudent; // 모든 조건을 만족하는 학생 반환
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
    public Student findStudentById(String studentId, String sessionId) throws RemoteException, NullDataException {
        if (verifySession(sessionId)) {
            Student student = data.getStudentById(studentId);
            log(Level.INFO, "Fetched student data for ID: " + studentId);
            return student;
        } else {
            log(Level.WARNING, "Invalid session for student search.");
            throw new RemoteException("Invalid session.");
        }
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
    public boolean signUp(String userId, String userPw,String firstName,String lastName) throws NullDataException, RemoteException {
        checkUserId(userId);
        boolean result = data.addUserRecords(userId + " " + userPw+" "+firstName+" "+lastName);
        log(Level.INFO, "User signed up. User ID: ");
        return result;
    }

    private boolean checkUserId(String userId) throws NullDataException, RemoteException {
        User selectedUser = data.getUserById(userId);
        if (selectedUser != null) {
            log(Level.WARNING, "Attempted sign up with existing User ID: ");
            System.out.println("이미 존재하는 ID 입니다.");
            return false;
        }
        return true;
    }
    
    @Override
    public Course findCourseById(String courseId, String sessionId) throws RemoteException, NullDataException {
        if (verifySession(sessionId)) {
            Course course = data.getCourseById(courseId);
            log(Level.INFO, "Fetched course data for ID: " + courseId);
            return course;
        } else {
            log(Level.WARNING, "Invalid session for course search.");
            throw new RemoteException("Invalid session.");
        }
    }

    @Override
    public boolean deleteRegistration(String studentId, String courseId, String sessionId) throws RemoteException {
        if (verifySession(sessionId)) {
            boolean result = data.deleteRegistration(studentId, courseId);
            if (result) {
                log(Level.INFO, "Deleted registration. Student ID: " + studentId + ", Course ID: " + courseId);
            } else {
                log(Level.WARNING, "Failed to delete registration for Student ID: " + studentId + ", Course ID: " + courseId);
            }
            return result;
        } else {
            log(Level.WARNING, "Invalid session for registration deletion.");
            throw new RemoteException("Invalid session.");
        }
    }
}
