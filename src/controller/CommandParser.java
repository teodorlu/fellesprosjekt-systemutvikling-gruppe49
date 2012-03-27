package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class CommandParser {
	
	private Map<String, List<String>> canContainOneOf;
	private Map<String, List<String>> mustContainOneOf;
	private Map<String, List<String>> mustContainAllOf;
	private List<String> implicit;
	private List<String> multipleImplicit;
	private boolean[] parsed;
	
	public CommandParser(String[] format){
		canContainOneOf = new HashMap<String, List<String>>();
		mustContainOneOf = new HashMap<String, List<String>>();
		mustContainAllOf = new HashMap<String, List<String>>();
		implicit = new LinkedList<String>();
		multipleImplicit = new LinkedList<String>();
		parsed = new boolean[format.length];
		build(format);
	}
	
	private void build(String[] format) {
		Stack<String> parent = new Stack<>();
		parent.push(null);
		
		Stack<Map> constraint = new Stack<Map>();
		constraint.push(mustContainAllOf);	// Default
		
		// first string is command id
		for (int i = 1; i < format.length; i++) {
			if (parsed[i])
				continue;
			
			String current = format[i];
			
			switch (current) {
			case "[":
				parent.push(format[i-1]);
				constraint.push(canContainOneOf);
				break;
				
			case "]":
				parent.pop();
				constraint.pop();
				break;
				
			case "(":
				parent.push("format[i-1]");
				constraint.push(mustContainOneOf);
				break;
				
			case ")":
				parent.pop();
				constraint.pop();
				
			default:
				break;
			}
			
			
			
			
		}
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