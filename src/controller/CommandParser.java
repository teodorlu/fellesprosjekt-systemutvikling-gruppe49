package controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.management.ImmutableDescriptor;

/**
 * 
 * Assumptions: If multiple inputs of some sort can be taken,
 * those must be at the end in in order to not mess up the other 
 * commands
 *
 */

public class CommandParser {
	
	private Map<String, String> explicitParameterIdentifierToParameterName;
	private List<String> implicitParameter;
	private boolean[] hasParsed;
	private String ignoredCharacters;
	private boolean lastArgumentCanBeMultiple;
	private static String PREVIOUS_MULTIPLE_IDENTIFIER;
	private static String REPEATING_PARAMETER;
	
	public CommandParser(String[] format) {
		explicitParameterIdentifierToParameterName = new LinkedHashMap<String, String>();
		implicitParameter = new LinkedList<String>();
		hasParsed = new boolean[format.length];
		hasParsed[0] = true;
		ignoredCharacters = "[]()/";
		PREVIOUS_MULTIPLE_IDENTIFIER = "...";
		parseFormat(format);
	}
	
//	public Map<String, String> parseInput(String [] input) {
//		Map<String, String> inputMapping = new HashMap<String, String>();
//		
//		// TODO
//		// Need a system for translating input to a map -> location of input is important.
//		// 1 - fill arguments from List<implicit>
//		// 2 - fill arguments from Map<Explicit>
//		// 3 - check for recurring argument
//		
//		return inputMapping;
//	}
	
	private void parseFormat(String[] format) {
		for (int i = 0; i < format.length; i++) {
			String word = format[i]; 
			
			if (hasParsed[i])
				continue;
			
			if (isToBeEgnored(word))
				continue;
			
			if (isIncapsulated(word)){
				implicitParameter.add(stripped(word));
				continue;
			}
			
			if (isParameter(word)) {
				explicitParameterIdentifierToParameterName.put(stripped(format[i+1]), word);
				hasParsed[i] = true;
				hasParsed[i+1] = true;
				continue;
			}
			
			if (word.equals(PREVIOUS_MULTIPLE_IDENTIFIER)
					&& containsOnlyIgnorableCharactersAfter(format, i)) {
				this.lastArgumentCanBeMultiple = true;
			}
		}
	}

	private boolean containsOnlyIgnorableCharactersAfter(String[] format, int i) {
		for (int j = i+1; j < format.length; j++) {
			String word = format[j];
			if (word.length() == 1
					&& ignoredCharacters.indexOf(word.charAt(0)) < 0)
				return false;
		}
		return true;
	}

	private boolean isToBeEgnored(String word) {
		if (word.length() != 1)
			return false;
		
		char c = word.charAt(0);
		if (ignoredCharacters.indexOf(c) < 0)
			return true;
		else
			return false;
	}

	private static boolean isIncapsulated(String word) {
		return		word.charAt(0) == '<'
				&&	word.charAt(word.length()-1) == '>';
	}

	private static String stripped(String word) {
		if (word.length() < 3)
			throw new IllegalArgumentException();
		if (!isIncapsulated(word))
			throw new IllegalArgumentException();
		
		return word.substring(1, word.length()-1);
	}

	private static boolean isParameter(String word) {
		return word.charAt(0) == '-';
	}
	
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("Parser:\n");
		ret.append("explicits: " + explicitParameterIdentifierToParameterName + "\n");
		ret.append("implicits: " + implicitParameter + "\n");
		
		if (lastArgumentCanBeMultiple)
			ret.append("Ends with multiple inputs: " + getMultipleAllowedOfName() + "\n");
		
		return ret.toString();
	}

	private Object getMultipleAllowedOfName() {
		if (!explicitParameterIdentifierToParameterName.isEmpty()) {
			Object[] values = explicitParameterIdentifierToParameterName.keySet().toArray();
			return values[values.length-1];
		}
		return implicitParameter.get(implicitParameter.size() - 1);
	}
	
	public Map<String, String> parseInput(String[] inputArray) {
		List<String> input = Arrays.asList(inputArray);
		HashMap<String, String> parametersToArguments = new HashMap<String, String>();
		
		if (input.size() < 1)
			throw new IllegalArgumentException();
		
		for (int i = 1; i < input.size(); i++) {
			String argument = input.get(i);
			
			// input's fist content is the command name, hence +1
			if (i < implicitParameter.size() + 1) {
				String parameter = implicitParameter.get(i + 1);
				parametersToArguments.put(parameter, argument);
			} else {
				// isExplicitArgument
				if (argument.charAt(0) == '-') {
					if (i + 1 >= input.size())
						throw new IllegalArgumentException();
					
					String parameterIdentifier = argument;
					argument = input.get(i+1);
					
					if (argument.charAt(0) == '-')
						throw new IllegalArgumentException();
					
					String parameter = this.explicitParameterIdentifierToParameterName.get(parameterIdentifier);
					
					parametersToArguments.put(parameter, argument);
				}
			}
		}
		
		// Fix repeating arguments
		if (this.lastArgumentCanBeMultiple) {
			
		}
		
		return parametersToArguments;
	}

	@SuppressWarnings("unused")
	private CommandParser() {
		
	}

	public static void main(String[] args) {
		CommandParser p1 = new CommandParser("edit <id> [ -title <title> -date <date> -s <start> -e <end> -desc <description> -place <place> ]".split(" "));
		CommandParser p2 = new CommandParser("calendar [ -w <week> [ -y <year> ] -u <username1> ... ]".split(" "));
//		System.out.println(p1);
		System.out.println(p2);
	}
}