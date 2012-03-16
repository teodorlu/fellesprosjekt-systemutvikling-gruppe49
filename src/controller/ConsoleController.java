package controller;

import java.io.InputStream;

public class ConsoleController {
	
	private InputStream stream;
	
	public ConsoleController(InputStream stream){
		String s = System.console().readLine();
		System.console().printf(s);
	}
	
	public static void main(String[] args) {
		new ConsoleController(System.in);
	}
	
}
