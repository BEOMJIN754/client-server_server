import java.io.Serializable;

public class Registration implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String studentId;
	protected String courseId;
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	@Override
    public String toString() {
        return "Student ID: " + studentId + ", Course ID: " + courseId;
    }
}
