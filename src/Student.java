import java.util.ArrayList;
import java.util.StringTokenizer;

public class Student extends Information {
    private static final long serialVersionUID = 1L;
    protected String department;
    protected ArrayList<String> completedCoursesList;

    public Student(String inputString) {
        super(inputString.split(" ")[0], inputString.split(" ")[1]); // id, name 설정
        StringTokenizer stringTokenizer = new StringTokenizer(inputString);
        stringTokenizer.nextToken(); // skip id
        stringTokenizer.nextToken(); // skip name

        this.department = stringTokenizer.nextToken();
        this.completedCoursesList = new ArrayList<String>();

        while (stringTokenizer.hasMoreTokens()) {
            this.completedCoursesList.add(stringTokenizer.nextToken());
        }
    }

    public String getDepartment() {
        return department;
    }

    public ArrayList<String> getCompletedCourses() {
        return completedCoursesList;
    }

    // 추가된 match 메서드
    public boolean match(String studentId) {
        return this.id.equals(studentId); // Information 클래스의 id 필드 사용
    }

    @Override
    public String toString() {
        String stringReturn = super.toString() + " " + department;
        for (String course : completedCoursesList) {
            stringReturn += " " + course;
        }
        return stringReturn;
    }
}
