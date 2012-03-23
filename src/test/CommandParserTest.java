package test;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.CommandParser;

public class CommandParserTest {
	
	@Test (expected=IllegalArgumentException.class)
	public void testInvalidRemoveBraces(){
		String[] invalids = { "<user", "user>", "user", "<>", "<", "" };
		for (String s : invalids)
			CommandParser.removeBraces(s);
	}
	
	@Test
	public void testValidRemoveBraces(){
		String[] input = {"<user>", "<password>", "<m>" };
		String[] actualOutput = new String[input.length];
		String[] correctOutput = {"user", "password", "m"};
		
		for (int i = 0; i < input.length; i++) {
			actualOutput[i] = CommandParser.removeBraces(input[i]);
		}
		
		assertArrayEquals(actualOutput, correctOutput);
	}

}
