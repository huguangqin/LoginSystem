package loginsystem;

public class UserInformation {
	private String name;
	private String passWord;

	public UserInformation() {
		super();
	}

	public UserInformation(String name, String passWord) {
		super();
		this.name = name;
		this.passWord = passWord;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

}
