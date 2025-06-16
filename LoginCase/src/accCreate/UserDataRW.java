package accCreate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDataRW {
	private String url = "jdbc:mysql://localhost:3306/3311_database"; // Replace with correct DB
    private String user = "root"; // Replace with correct username
    private String pass = "adminRomeo"; // Replace with correct password

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    
	public void writeUser(UserData data) {
		String username = data.getUsername();
		String password = data.getPassword();
		String units = data.getUnits();
		String sex = data.getSex();
		LocalDate dob = data.getDob();
		double height = data.getHeight();
		double weight = data.getWeight();
                
        try {
            //Connect to database
            connection = DriverManager.getConnection(url, user, pass);
            

            // SQL query (inserting data)
            String insertSQL = "INSERT INTO accountinfo (Username, Password, Units, Sex, BirthYear, BirthMonth, BirthDay, Height, Weight) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            //set write values
            preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, units);
            preparedStatement.setString(4, sex);
            preparedStatement.setInt(5, dob.getYear());
            preparedStatement.setInt(6, dob.getMonthValue());
            preparedStatement.setInt(7, dob.getDayOfMonth());
            preparedStatement.setDouble(8, height);  //cm
            preparedStatement.setDouble(9, weight);	 //kg

            //execute write
            preparedStatement.executeUpdate();

            

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
	
	public void updateUser(UserData data) {
		String username = data.getUsername();
		String password = data.getPassword();
		String units = data.getUnits();
		String sex = data.getSex();
		LocalDate dob = data.getDob();
		double height = data.getHeight();
		double weight = data.getWeight();
                
        try {
            //Connect to database
            connection = DriverManager.getConnection(url, user, pass);
            

            // SQL query (inserting data)
            String updateSQL = "UPDATE accountinfo SET Password = ?, Units = ?, Sex = ?, BirthYear = ?, BirthMonth = ?, BirthDay = ?, Height = ?, Weight = ?  WHERE Username = ?";

            //set write values
            preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, units);
            preparedStatement.setString(3, sex);
            preparedStatement.setInt(4, dob.getYear());
            preparedStatement.setInt(5, dob.getMonthValue());
            preparedStatement.setInt(6, dob.getDayOfMonth());
            preparedStatement.setDouble(7, height);  //cm
            preparedStatement.setDouble(8, weight);	 //kg
            preparedStatement.setString(9, username);

            //execute write
            preparedStatement.executeUpdate();
            

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
	
	
	public boolean searchUsername(String usernameInp) {
		try {
			//Connect to the database
			connection = DriverManager.getConnection(url, user, pass);

			// SQL query for reading data
			String searchSQL = "SELECT * FROM accountinfo WHERE Username = ?";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			preparedStatement.setString(1, usernameInp);
			resultSet = preparedStatement.executeQuery();

			// Process the result
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	
	public boolean searchUser(String usernameInp, String passwordInp) {
		try {
			//Connect to the database
			connection = DriverManager.getConnection(url, user, pass);

			// SQL query for reading data
			String searchSQL = "SELECT * FROM accountinfo WHERE Username = ? AND Password = ?";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			preparedStatement.setString(1, usernameInp);
            preparedStatement.setString(2, passwordInp);
			resultSet = preparedStatement.executeQuery();

			// Process the result
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public UserData getUser(String usernameInp, String passwordInp) {
		UserData data = null;
		
		try {
			//Connect to the database
			connection = DriverManager.getConnection(url, user, pass);

			// SQL query for reading data
			String searchSQL = "SELECT * FROM accountinfo WHERE Username = ? AND Password = ?";

			//Create Statement and search parameters and execute query
			preparedStatement = connection.prepareStatement(searchSQL);
			preparedStatement.setString(1, usernameInp);
            preparedStatement.setString(2, passwordInp);
			resultSet = preparedStatement.executeQuery();

			// Process the result
            if (resultSet.next()) {
                //details of the found account
                int id = resultSet.getInt("id");
                String username = resultSet.getString("Username");
                String password = resultSet.getString("Password");
                String units = resultSet.getString("Units");
                String sex = resultSet.getString("Sex");
                int birthYear = resultSet.getInt("BirthYear");
                int birthMonth = resultSet.getInt("BirthMonth");
                int birthDay = resultSet.getInt("BirthDay");
                double height = resultSet.getDouble("Height");
                double weight = resultSet.getDouble("Weight");

                data = new UserData(username, password, units, sex, LocalDate.of(birthYear, birthMonth, birthDay), height, weight);
                return data;
            } 

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) resultSet.close();
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	
}
