package app.logic;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConversionRules {

	private static final Character[] NonRepeatingRomanNumerals = {'D', 'L', 'V'};
	private static final Character[] RepeatingRomanNumerals = {'I','V','X','M'};
	private static Map<Character,Integer> repeatableLiteralsCount = getRepeatableLiteralsCount();
	private static Map<Character,Integer> nonRepeatableLiteralsCount = getNonRepeatableLiteralsCount();
	
	private ConversionRules() {
		
	}
	
	private static Map<Character,Integer> getRepeatableLiteralsCount(){
		Map<Character,Integer>map = new HashMap<Character,Integer>() {
			{
				put('I', 0);
				put('X', 0);
				put('C', 0);
				put('M', 0);
			}
		};
		return map;
	}

	private static Map<Character,Integer> getNonRepeatableLiteralsCount(){
		Map<Character,Integer>map = new HashMap<Character,Integer>() {
			{
				put('V', 0);
				put('L', 0);
				put('D', 0);
			}
		};
		return map;
	}

	private static final Map<Integer, Integer[]> ROMAN_SUBTRACTABLE_MAPPING = Collections.unmodifiableMap(
			new HashMap<Integer, Integer[]>() {
				{
					put(1, new Integer[] {5, 10});
					put(5, new Integer[] {});
					put(10, new Integer[] {50,100});
					put(50, new Integer[] {});
					put(100, new Integer[] {100,1000});
					put(500, new Integer[] {});
					put(1000, new Integer[] {});
				}
			});

	private static final Map<Character, Integer> ROMAN_TO_NUMERIC_MAPPING = Collections.unmodifiableMap(
			new HashMap<Character, Integer>() {
				{
					put('I', 1);
					put('V', 5);
					put('X', 10);
					put('L', 50);
					put('C', 100);
					put('D', 500);
					put('M', 1000);
				}
			});

	/**
	 * checkLiteralCountValidity() method keeps the count of all repeatable and non repeatable Literals.
	 * 
	 */
	public static void checkLiteralCountValidity(Character currentLiteral){
		if(checkIfLiteralPresent(NonRepeatingRomanNumerals, currentLiteral)){
			nonRepeatableLiteralsCount.put(currentLiteral, nonRepeatableLiteralsCount.get(currentLiteral) + 1);
			if(nonRepeatableLiteralsCount.containsValue(3)){
				System.err.println("Error : Roman Numeral V,L,D cannot be repeated.");	
				System.exit(0);
			}
		}
		else if(checkIfLiteralPresent(RepeatingRomanNumerals, currentLiteral)){
			Character keyForValueContainingThree = getKeyForValueContainingThree();
			if(keyForValueContainingThree != '\0'){
				if (currentLiteral.equals(keyForValueContainingThree)){
					System.err.println("Error : Roman Numeral "+currentLiteral+" cannot repeat 4 times successively");
					System.exit(0);
				}
				else if(currentLiteralSmallerThanPrevious(currentLiteral, keyForValueContainingThree)) {
					repeatableLiteralsCount.put(currentLiteral, repeatableLiteralsCount.get(currentLiteral) +1);
					repeatableLiteralsCount.put(keyForValueContainingThree, 0);
				}
			}
			else{
				repeatableLiteralsCount.put(currentLiteral, repeatableLiteralsCount.get(currentLiteral) +1);
			}
		}
	}

	/**
	 * getKeyForValueContainingThree() method returns the key corresponding to value 3.This is used while checking the count of Literals 
	 * that cannot be repeated more than 3 times.
	 * 
	 */
	private static char getKeyForValueContainingThree(){
		char key = '\0';
		Iterator<Map.Entry<Character,Integer>> iter = repeatableLiteralsCount.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Character,Integer> entry = iter.next();
			if (entry.getValue().equals(3)) {
				return Character.valueOf(entry.getKey());
			}
		}
		return key;
	}

	/**
	 * checks if currentLiteral is smaller than the previous one. This rule is applied when repeatable literals can
	 * occur 4 times only if the 3rd and 4th occurance has a smaller value between them.
	 * @param CurrentLiteral
	 * @param keyForValueContainingThree
	 * @return
	 */
	private static boolean currentLiteralSmallerThanPrevious(char CurrentLiteral, char keyForValueContainingThree){
		if (ROMAN_TO_NUMERIC_MAPPING.get(CurrentLiteral)> ROMAN_TO_NUMERIC_MAPPING.get(keyForValueContainingThree)){
			System.err.println("Error : Should have been a lesser Roman Numeral next instead of "+CurrentLiteral);
			System.exit(0);
			return false;
		}
		else{
			return true;
		}
	}

	/**
	 * Applies the subtaction logic and checks if the element is subtractable by the other or not.
	 * @param lastDecimal
	 * @param decimal
	 * @param lastNumber
	 * @return
	 */
	public static float subtractionLogic(Float lastDecimal, Float decimal, Float lastNumber){
		if(Arrays.asList(ROMAN_SUBTRACTABLE_MAPPING.get(Math.round(decimal))).contains(Math.round(lastNumber))){
			return lastDecimal - decimal;
		}
		else
			return lastDecimal + decimal;
	}


	/**
	 * Checks if an element is present in an array
	 * @param array
	 * @param literal
	 * @return
	 */
	public static boolean checkIfLiteralPresent(Character[] array, Character literal){
		boolean result = false;
		for (int i = 0; i < array.length; i++) {
			if(array[i].equals(literal))
				result =  true;
		}
		return result;
	}

	public static void resetLiteralsCounter(){
		repeatableLiteralsCount = getRepeatableLiteralsCount();
		nonRepeatableLiteralsCount = getNonRepeatableLiteralsCount();

	}
}