import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerIF extends Remote{
	// 리모트를 위한 인터페이스이다!!! 리모트가 들어가야 외부에서 사용 가능한 스켈레톤 이 된다.
	ArrayList<Student> getAllStudentData () throws RemoteException;
	ArrayList<Course> getAllCourseData () throws RemoteException;
}
