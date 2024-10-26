import java.io.Serializable;
import java.util.StringTokenizer;

public class Registration implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String studentId;
	protected String courseId;

	public Registration(String inputString) {
		StringTokenizer stringTokenizer = new StringTokenizer(inputString);
		this.studentId = stringTokenizer.nextToken();
		this.courseId = stringTokenizer.nextToken();
	}
	public String getStudentId() {
		return studentId;
	}
	public String getCourseId() {
		return courseId;
	}
	
	@Override
    public String toString() {
        return "Student ID: " + studentId + ", Course ID: " + courseId;
    }
}
