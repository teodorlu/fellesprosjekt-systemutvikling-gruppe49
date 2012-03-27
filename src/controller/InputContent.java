package controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InputContent {
	
	private Map<String, String> parameterToArgument;
	private List<String> repeatingParameter;
	
	public InputContent(Map<String, String> parameterToArgument,
			List<String> repeatingParameter) {
		super();
		this.parameterToArgument = parameterToArgument;
		this.repeatingParameter = repeatingParameter;
	}
	
	public String get(String parameter) {
		return parameterToArgument.get(parameter);
	}
	
	public List<String> getRepeatingParameter() {
		return new LinkedList<String>(repeatingParameter);
	}

	@SuppressWarnings("unused")
	private InputContent() {
		
	}
}