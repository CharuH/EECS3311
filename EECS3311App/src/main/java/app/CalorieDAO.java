package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CalorieDAO {
	public static int getKcalbyFoodID(int id) {
		String query = "SELECT `Nutrient_Value` FROM `nutrients` WHERE Food_id = ? AND Nutrient_code = 208";
        try (Connection conn = Dbfetch.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return (int) rs.getInt("Nutrient_Value"); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; 
    }
    

}
