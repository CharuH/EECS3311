package dailyNutrition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import app.Dbfetch;

public class DailyRecommendedNutritionDAO {
	private Connection connection = null;

	public ArrayList<Entry<Nutrient, Double>> getRecommendedNutrition(double maxAge, String sex, double weight, ArrayList<Entry<Nutrient, Double>> topNutrients) {
		PreparedStatement preparedStatement = null;
	    ResultSet resultSet = null;
	    String searchSQL;
	    ArrayList<Entry<Nutrient, Double>> recNutrition = new ArrayList<>();
	    HashMap<Nutrient, Double> recMap = new HashMap<>();
		try {
			//Connect to the database
			connection = Dbfetch.getConnection();

			// SQL query for reading data and search parameters
			if (maxAge == 0.57 || maxAge == 0.9 || maxAge == 3 || maxAge == 8) {
				searchSQL = "SELECT * FROM dailyrecommended WHERE ageMax = ?";
				preparedStatement = connection.prepareStatement(searchSQL);
				preparedStatement.setDouble(1, maxAge);
			} else {
				searchSQL = "SELECT * FROM dailyrecommended WHERE ageMax = ? AND sex = ?";
				preparedStatement = connection.prepareStatement(searchSQL);
				preparedStatement.setDouble(1, maxAge);
	            preparedStatement.setString(2, sex);
			}
			
			//Execute query
			resultSet = preparedStatement.executeQuery();

			// Process the result
            if (resultSet.next()) {
            	//go through top 10 nutrients + other
               for (Map.Entry<Nutrient, Double> entry: topNutrients) {
            	   Nutrient nutrient = entry.getKey();
            	   double recommendedAmount = 0;
            	   //check if there is corresponding recommended amount for the nutrientID
            	   switch (entry.getKey().getNutrientID()) {
            	   		case 339:
            	   		case 876:
            	   			recommendedAmount = resultSet.getDouble("VitaminD");
            	   			break;
            	   		case 430:
            	   			recommendedAmount = resultSet.getDouble("VitaminK");
            	   			break;
            	   		case 401:
            	   			recommendedAmount = resultSet.getDouble("VitaminC");
            	    		break;
            	   		case 404:
            	   			recommendedAmount = resultSet.getDouble("Thiamin");
            	   			break;
            	   		case 405:
            	   			recommendedAmount = resultSet.getDouble("Riboflavin");
            	   			break;
            	   		case 406:
        	   				recommendedAmount = resultSet.getDouble("Niacin");
        	   				break;
            	   		case 415:
        	   				recommendedAmount = resultSet.getDouble("VitaminB6");
        	   				break;
            	   		case 806:
            	   		case 815:
        	   				recommendedAmount = resultSet.getDouble("Folate");
        	   				break;
            	   		case 418:
            	   		case 874:
        	   				recommendedAmount = resultSet.getDouble("VitaminB12");
        	   				break;
            	   		case 410:
        	   				recommendedAmount = resultSet.getDouble("Pantothenic_Acid");
        	   				break;
            	   		case 416:
        	   				recommendedAmount = resultSet.getDouble("Biotin");
        	   				break;
            	   		case 862:
        	   				recommendedAmount = resultSet.getDouble("Choline");
        	   				break;
            	   		case 301:
        	   				recommendedAmount = resultSet.getDouble("Calcium");
        	   				break;
            	   		case 312:
        	   				recommendedAmount = resultSet.getDouble("Copper");
        	   				break;
            	   		case 303:
        	   				recommendedAmount = resultSet.getDouble("Iron");
        	   				break;
            	   		case 304:
        	   				recommendedAmount = resultSet.getDouble("Magnesium");
        	   				break;
            	   		case 315:
        	   				recommendedAmount = resultSet.getDouble("Manganese");
        	   				break;
            	   		case 305:
        	   				recommendedAmount = resultSet.getDouble("Phosphorus");
        	   				break;
            	   		case 317:
        	   				recommendedAmount = resultSet.getDouble("Selenium");
        	   				break;
            	   		case 309:
        	   				recommendedAmount = resultSet.getDouble("Zinc");
        	   				break;
            	   		case 306:
        	   				recommendedAmount = resultSet.getDouble("Potassium");
        	   				break;
            	   		case 307:
        	   				recommendedAmount = resultSet.getDouble("Sodium");
        	   				break;
            	   		case 205:
        	   				recommendedAmount = resultSet.getDouble("Carbohydrate");
        	   				break;
            	   		case 203:
        	   				recommendedAmount = resultSet.getDouble("Protein") * weight;
        	   				break;
            	   		case 204:
        	   				recommendedAmount = resultSet.getDouble("Fat");
        	   				break;
            	   		case 825:
        	   				recommendedAmount = resultSet.getDouble("Linoleic_Acid_(n-6)");
        	   				break;
            	   		case 605:
            	   		case 606:
        	   				recommendedAmount = resultSet.getDouble("SatTransFats");
        	   				break;
            	   		case 291:
        	   				recommendedAmount = resultSet.getDouble("Fibre");
        	   				break;
            	   		case 255:
        	   				recommendedAmount = resultSet.getDouble("Water");
        	   				break;
            	   		case 269:
        	   				recommendedAmount = resultSet.getDouble("Sugars");
        	   				break;
            	   		case 601:
        	   				recommendedAmount = resultSet.getDouble("Cholesterol");
        	   				break;
            	   }
            	   recMap.put(nutrient, recommendedAmount);   
               }
            }
            recNutrition = new ArrayList<>(recMap.entrySet());
           

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
		return recNutrition;
	}
}
