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
		double ageYears = age.getYears();
		double ageMonths = age.getMonths();
		double ageDays = age.getDays();
		double ageMax;
		
		//get maxAge category based on age
		if (ageYears == 0) {
			if (ageMonths > 6 || ageMonths == 6 && ageDays > 0) {
				ageMax = 0.99;
			} else {
				ageMax = 0.57;
			}
		} else if (ageYears < 4) {
			ageMax = 0.99;
		} else if (ageYears < 9) {
			ageMax = 8;
		} else if (ageYears < 14) {
			ageMax = 13;
		} else if (ageYears < 19) {
			ageMax = 18;
		} else if (ageYears < 31) {
			ageMax = 30;
		} else if (ageYears < 51) {
			ageMax = 50;
		} else if (ageYears < 71) {
			ageMax = 70;
		} else {
			ageMax = 122;
		}
		
		//get recommended nutrition based on age, sex, weight
		DailyRecommendedNutritionDAO recNutritionDAO = new DailyRecommendedNutritionDAO();
		ArrayList<Entry<Nutrient, Double>> recNutrition = recNutritionDAO.getRecommendedNutrition(ageMax, sex, weight, avgNutrition);
		return recNutrition;
	}
}