import java.io.Serializable;
import java.util.StringTokenizer;

public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String userId;
	protected String userPw;
	
	public User(String inputString) {
		StringTokenizer stringTokenizer = new StringTokenizer(inputString);
		this.userId = stringTokenizer.nextToken();
		this.userPw = stringTokenizer.nextToken();
        }
	public String getUserId() {
		return userId;
	}
	public String getUserPw() {
		return userPw;
	}
	public boolean match(String userId) {
		return this.userId.equals(userId);
	}
	

}
