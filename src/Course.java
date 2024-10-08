import java.util.StringTokenizer;

public class Course extends Information {
    private static final long serialVersionUID = 1L;
    protected String cName;
    protected String preRequisite;

    public Course(String inputString) {
        super(inputString.split(" ")[0], inputString.split(" ")[1]); // id, professor 설정
        StringTokenizer stringTokenizer = new StringTokenizer(inputString);
        stringTokenizer.nextToken(); // skip id
        stringTokenizer.nextToken(); // skip professor

        this.cName = stringTokenizer.nextToken();
        this.preRequisite = stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : "";
    }

    public String getcName() {
        return cName;
    }

    public String getPreRequisite() {
        return preRequisite;
    }

    @Override
    public String toString() {
        String stringReturn = super.toString() + " " + cName;
        if (!preRequisite.isEmpty()) {
            stringReturn += " " + preRequisite;
        }
        return stringReturn;
    }
}
