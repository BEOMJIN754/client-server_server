import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;
public class Student implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String studentId;
    protected String name;
    protected String department;
    protected ArrayList<String> completedCoursesList;
    private ArrayList<String> registeredCourses;
    public Student(String inputString) {
        StringTokenizer stringTokenizer = new StringTokenizer(inputString);
    	this.studentId = stringTokenizer.nextToken();
    	this.name = stringTokenizer.nextToken();
    	this.department = stringTokenizer.nextToken();
    	this.completedCoursesList = new ArrayList<String>();
    	this.registeredCourses = new ArrayList<String>();
    	while (stringTokenizer.hasMoreTokens()) {
    		this.completedCoursesList.add(stringTokenizer.nextToken());
    	}
    }
    public boolean match(String studentId) {
        return this.studentId.equals(studentId);
    }
    public String getName() {
        return this.name;
    }
    public ArrayList<String> getCompletedCourses() {
        return this.completedCoursesList;
    }
    public ArrayList<String> getRegisteredCourses() {
        return this.registeredCourses;
    }
    public void registerCourse(String courseId) {
        if (!registeredCourses.contains(courseId)) {
            this.registeredCourses.add(courseId);
        }
    }
    @Override
    public String toString() {
        String stringReturn = this.studentId + " " + this.name + " " + this.department;

        // completedCoursesList 추가
        for (String course : this.completedCoursesList) {
            stringReturn += " " + course;
        }

        // registeredCourses 추가
        if (!this.registeredCourses.isEmpty()) {
            stringReturn += " Registered Courses:";
            for (String registeredCourse : this.registeredCourses) {
                stringReturn += " " + registeredCourse;
            }
        }

        return stringReturn;
    }

}