package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CommandParser {
	
	private Map<String, List<String>> canContainOneOf;
	private Map<String, List<String>> mustContainOneOf;
	private Map<String, List<String>> mustContainAllOf;
	private List<String> implicit;
	private List<String> multipleImplicit;
	
	public CommandParser(String[] format){
		canContainOneOf = new HashMap<String, List<String>>();
		mustContainOneOf = new HashMap<String, List<String>>();
		mustContainAllOf = new HashMap<String, List<String>>();
		implicit = new LinkedList<String>();
		multipleImplicit = new LinkedList<String>();
		build(format);
	}
	
	private void build(String[] format) {
		
		
	}
	
	public static String removeBraces(String source) {
		if (!(source.charAt(0) == '<' && source.charAt(source.length() - 1) == '>'))
			throw new IllegalArgumentException("source did not contain braces");
		return source.substring(1, source.length() - 1);
		
	}

	@SuppressWarnings("unused") // Preventing construction without format
	private CommandParser(){
		
	}
}