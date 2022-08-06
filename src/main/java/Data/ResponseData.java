package Data;

public class ResponseData {

    private String Login;
    private String Pass;

    public ResponseData (){}

    public void setLogin (String Login) {
        this.Login = Login;
    }

    public void setPass (String Pass) {
        this.Pass = Pass;
    }

    public String getLogin() {
        return Login;
    }

    public String getPass() {
        return Pass;
    }

    public int length() {
        return this.Login.length() + this.Pass.length();
    }
}
