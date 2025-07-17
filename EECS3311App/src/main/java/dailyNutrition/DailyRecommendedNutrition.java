package dailyNutrition;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Map.Entry;

import accCreate.UserData;

public class DailyRecommendedNutrition {

	public static ArrayList<Entry<Nutrient, Double>> getRecommendedNutrition(ArrayList<Entry<Nutrient, Double>>  avgNutrition, UserData userData) {
		//get age, sex, weight
		LocalDate dob = userData.getDob();
		LocalDate dateCurrent = LocalDate.now();
		Period age = Period.between(dob, dateCurrent);
		String sex = userData.getSex();
		double weight = userData.getWeight();
		double ageMax;
		
		//get maxAge category based on age
		if (age.getYears() == 0) {
			if (age.getMonths() > 6 || age.getMonths() == 6 && age.getDays() > 0) {
				ageMax = 0.99;
			} else {
				ageMax = 0.57;
			}
		} else if (age.getYears() >= 1 && age.getYears() < 4) {
			ageMax = 0.99;
		} else if (age.getYears() >= 4 && age.getYears() < 9) {
			ageMax = 8;
		} else if (age.getYears() >= 9 && age.getYears() < 14) {
			ageMax = 13;
		} else if (age.getYears() >= 14 && age.getYears() < 19) {
			ageMax = 18;
		} else if (age.getYears() >= 19 && age.getYears() < 31) {
			ageMax = 30;
		} else if (age.getYears() >= 31 && age.getYears() < 51) {
			ageMax = 50;
		} else if (age.getYears() >= 51 && age.getYears() < 71) {
			ageMax = 70;
		} else {
			ageMax = 122;
		}
		
		//get recommended nutrition based on age, sex, weight
		DailyNutritionDAO dailyNutritionDAO = new DailyNutritionDAO();
		ArrayList<Entry<Nutrient, Double>> recNutrition = dailyNutritionDAO.getRecommendedNutrition(ageMax, sex, weight, avgNutrition);
		return recNutrition;
	}
}