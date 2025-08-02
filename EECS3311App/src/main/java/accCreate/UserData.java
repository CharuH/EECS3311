package accCreate;

import java.time.LocalDate;

public class UserData {
	private static UserData instance;
	private String username;
	private String password;
	private String units;
	private String sex;
	private LocalDate dob;
	private double height;
	private double weight;
	
	private UserData() {
		this.username = null;
		this.password = null;
		this.units = null;
		this.sex = null;
		this.dob = null;
		this.height = 0;
		this.weight = 0;
	}
	
	public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	

}
