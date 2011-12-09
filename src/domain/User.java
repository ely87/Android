package domain;

public class User {

	private int idUser;
	private String email;
	private String password;

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User(int idUser, String email, String password) {
		super();
		this.idUser = idUser;
		this.email = email;
		this.password = password;
	}

	public User() {
		super();
	}

}
