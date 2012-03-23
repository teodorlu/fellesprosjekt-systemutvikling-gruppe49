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
	
	public CommandParser(String[] format){
		canContainOneOf = new HashMap<String, List<String>>();
		mustContainOneOf = new HashMap<String, List<String>>();
		mustContainAllOf = new HashMap<String, List<String>>();
		implicit = new LinkedList<String>();
		build();
	}
	
	private void build() {
		
		
	}

	private CommandParser(){
		
	}
}