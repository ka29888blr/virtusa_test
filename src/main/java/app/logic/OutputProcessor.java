package app.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputProcessor{
	
	private static final String MSG = " : I have no idea what you are talking about";

	private OutputProcessor() {
		
	}
	/**
	 * processReplyForQuestion() iterates over the questionAndReply map that contain all the valid queries as keys.
	 * It further invokes processReply() on each key for processing the response.
	 */
	public static void processReplyForQuestion(){
		Map<String, String> map = InputProcessor.questionAndReply;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			processReply(entry.getKey());
		}
	}

	private static void processReply(String query){
		if (query.toLowerCase().startsWith("how much")){
			findValueOfRoman(query);
		}
		else if (query.toLowerCase().startsWith("how many")){
			findValueOfElement(query);
		}

	}

	/**
	 * findValueOfRoman() processes those queries seeking the decimal equivalent of any RomanNumeral and prints the result.
	 * @param query
	 */
	public static void findValueOfRoman(String query){
		if (isValidinput(query)){
			ArrayList<String> tokenValueToRoman = new ArrayList<String>();
			ArrayList<String> tokenValue = splitQuery(query);
			for (int i = 0; i < tokenValue.size(); i++) {
				tokenValueToRoman.add(InputProcessor.tokenRomanValueMapping.get(tokenValue.get(i)));
			}
			float value = new RomanToDecimal().romanToDecimal(tokenValueToRoman.toString());
			tokenValue.add("is");tokenValue.add(Float.toString(value));
			System.out.println(query+" "+outputFormatter(tokenValue));
		}
		else{
			System.err.println(query+MSG);
		}
	}


	/**
	 * findValueOfElement() processes those queries seeking the Credit value of any quantity of elements and prints the result.
	 * @param query
	 */
	private static void findValueOfElement(String query){
		if (isValidinput(query)){
			ArrayList<String> tokenValue = splitQuery(query);
			ArrayList<String> tokenValueToRoman = new ArrayList<String>();
			String element = null;
			for (int i = 0; i < tokenValue.size(); i++) {
				if(InputProcessor.tokenRomanValueMapping.get(tokenValue.get(i)) != null){
					tokenValueToRoman.add(InputProcessor.tokenRomanValueMapping.get(tokenValue.get(i)));
				}
				else if (InputProcessor.elementValueList.get(tokenValue.get(i)) != null){
					element = tokenValue.get(i);
				}
				else{
					System.err.println(query+MSG);
				}
			}
			float elementValue = (new RomanToDecimal().romanToDecimal(tokenValueToRoman.toString()) * InputProcessor.elementValueList.get(element));
			tokenValue.add("is");tokenValue.add(Float.toString(elementValue));tokenValue.add("Credits");
			System.out.println(query+" "+outputFormatter(tokenValue));
		}
		else{
			System.err.println(query+MSG);
		}
	}

	/**
	 * Formats the response to a query and displays it in readable format
	 * @param output
	 * @return
	 */
	private static String outputFormatter(ArrayList<String> output){
		return output.toString().replace(",", "").replace("[", "").replace("]", "");
	}

	/**
	 * Applies regex on each input in the file to figure out the valid ones.
	 * @param query
	 * @return
	 */
	private static boolean isValidinput(String query){
		Pattern regex = Pattern.compile("[$&+,:;=@#|]");
		Matcher matcher = regex.matcher(query);
		return !matcher.find();

	}

	/**
	 * Splits the query and returns an ArrayList containing only Roman numerals and elements
	 * @param query
	 * @return
	 */
	private static ArrayList<String> splitQuery(String query){
		ArrayList<String> queryArray = new ArrayList<String>(Arrays.asList(query.split("((?<=:)|(?=:))|( )")));
		int startIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < queryArray.size(); i++) {
			String elementName = queryArray.get(i).toLowerCase();
			if(elementName.equals("is")){
				startIndex = i+1;
			}
			else if(elementName.equals("?")){
				endIndex = i;

			}
		}
		String[] array = queryArray.toArray(new String[queryArray.size()]);
		return new ArrayList<String>(Arrays.asList(java.util.Arrays.copyOfRange(array, startIndex, endIndex)));

	}

}
