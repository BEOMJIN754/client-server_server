import java.io.Serializable;
import java.util.StringTokenizer;

public class Course implements Serializable {
	private static final long serialVersionUID = 1L;

	protected String courseId;
	protected String professor;
	protected String cName;
	protected String preRequisite;
	
	public Course(String inputString) {
		StringTokenizer stringTokenizer = new StringTokenizer(inputString);
		this.courseId = stringTokenizer.nextToken();
		this.professor = stringTokenizer.nextToken();
		this.cName = stringTokenizer.nextToken();
		if (stringTokenizer.hasMoreTokens()) {
            this.preRequisite = stringTokenizer.nextToken();
        } else {
            this.preRequisite = ""; // 없으면 빈 문자열로 설정
        }
	}
	
	public boolean match(String courseId) {
		return this.courseId.equals(courseId);
	}
	public String getcName() {
		return this.cName;
	}
	public String getpreRequisite() {
		return this.preRequisite;
	}
	public String toString() {
		String stringReturn = this.courseId + " " + this.professor + " " + this.cName + " " + this.preRequisite;
		return stringReturn;
	}
}
