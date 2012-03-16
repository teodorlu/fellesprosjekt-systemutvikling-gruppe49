package controller;

import java.util.Arrays;
import java.util.List;

public class CommandExecuter {
	
	public static void register(String[] array){
		
		List<String> input = Arrays.asList(array);
		String username, password, firstName, lastName;
		
		if(input.contains("-u")){
			int uIndex = input.indexOf("-u");
			username = input.get(uIndex+1);
			
			if(input.contains("-p")){
				int pIndex = input.indexOf("-p");
				password = input.get(pIndex+1);
				if(input.contains("-fn")){
					int fnIndex = input.indexOf("-fn");
					firstName = input.get(fnIndex+1);
					if(input.contains("-ln")){
						int lnIndex = input.indexOf("-ln");
						lastName = input.get(lnIndex+1);
						System.out.println(username +" "+ password+" "+ firstName+" "+" "+lastName);
						// Lag et personobjekt her og fjern linja over
					}
					else System.out.println("Feil input: Etternavn");
				}
				else System.out.println("Feil input: Fornavn");
			}
			else System.out.println("Feil input: Password");
		}
		else System.out.println("Feil input: Username");
	}
	
	public static void login(String[] array){
		List<String> input = Arrays.asList(array);
		int uIndex, pIndex;
		String username, password;
		
		if(input.contains("-u") && input.contains("-p")){
			uIndex = input.indexOf("-u");
			pIndex  =input.indexOf("-p");
			username = input.get(uIndex+1);
			password = input.get(pIndex+1);
			System.out.println("User: "+username + " Password: "+password);
			
			//Add LoginToDo, har bare henta brukernavn i username og pw i password
		}
	}
	
	public static void main(String args[]){
		String[] registertest = {"register", "-u", "dzenan", "-p", "mittpassord", "-fn", "firstName", "-ln", "lastName"};
		String[] logintest = {"login", "-u", "Brukernavnet", "-p", "passordet"};
		register(registertest);
		login(logintest);
	}

}
