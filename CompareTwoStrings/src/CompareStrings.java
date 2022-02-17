import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class CompareStrings {

	public static HashMap<Character, Integer> characterCount(String inputString) {
		HashMap<Character, Integer> charCountMap = new HashMap<Character, Integer>();

		char[] strArray = inputString.toCharArray();

		for (char c : strArray) {
			if (charCountMap.containsKey(c)) {
				charCountMap.put(c, charCountMap.get(c) + 1);
			} else {
				charCountMap.put(c, 1);
			}
		}

		return charCountMap;
	}

	public static Double percentage(String str1, String str2) {
		str1 = CompareStrings.removeWhiteSpace(str1);
		str2 = CompareStrings.removeWhiteSpace(str2);
		if (str1.equals(str2)) {
			return (double) 100;
		} else if (str1.length() < str2.length()) {
			return CompareStrings.calculatePercentage(str1, str2);
		} else if (str2.length() < str1.length()) {
			return CompareStrings.calculatePercentage(str2, str1);
		} else {
			return CompareStrings.calculatePercentage(str1, str2);
		}
	}
	
	private static String removeWhiteSpace(String str) {
		char[] strArray = str.toLowerCase().toCharArray();
		String newString = "";
		for(char c: strArray) {
			if(!Character.isWhitespace(c))
				newString +=c;
		}
		return newString;
	}

	public static double calculatePercentage(String str1, String str2) {
		double percentage;
		int maxMatch = 0;
		HashMap<Character, Integer> str1Map = characterCount(str1);
		HashMap<Character, Integer> str2Map = characterCount(str2);
		for (Character ch1 : str1Map.keySet()) {
			if (str2Map.containsKey(ch1) ) {
//				System.out.println(ch1 + " " + str1Map.get(ch1) + " " + str2Map.get(ch1));
				if (str1Map.get(ch1) > str2Map.get(ch1)) {
					maxMatch -= str1Map.get(ch1) - str2Map.get(ch1);
				}
				maxMatch += str1Map.get(ch1);
			}
		}
		percentage = maxMatch  * 100 / str2.length();
		return percentage;

	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.println("Enter 1st string: ");
		String str1 = sc.nextLine();
		System.out.println("Enter 2st string: ");
		String str2 = sc.nextLine();

		Double percentage = CompareStrings.percentage(str1, str2);
		System.out.println(percentage + "% match");

	}
}
