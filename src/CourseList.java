import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CourseList {
protected ArrayList<Course> vCourse;
	
	public CourseList(String sCourseFileName) throws FileNotFoundException, IOException {
		BufferedReader objCourseFile = new BufferedReader(new FileReader(sCourseFileName));
		this.vCourse = new ArrayList<Course>();
		while (objCourseFile.ready()) {
			String cInfo = objCourseFile.readLine();
			if (!cInfo.equals("")) {
				this.vCourse.add(new Course(cInfo));
			}
		}
		objCourseFile.close();
	}

	public ArrayList<Course> getAllCourseRecords() {
		return this.vCourse;
	}

//	public boolean isRegisteredCourse(String sCID) {
//		for (int i = 0; i < this.vCourse.size(); i++) {
//			Course objCourse = (Course) this.vCourse.get(i);
//			if (objCourse.match(sCID)) {
//				return true;
//			}
//		}
//		return false;
//	}
}
