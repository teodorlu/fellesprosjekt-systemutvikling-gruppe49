package controller;

import java.util.Arrays;
import java.util.List;

public class CommandExecuter {
	
	public static void register(String[] array){
		
		List<String> liste = Arrays.asList(array);
		String password = "Feil";
		String username = "Feil";
		String firstName = "Feil";
		String lastName = "Feil";
		
		if(liste.contains("-p")){
			int pIndex = liste.indexOf("-p");
			password = liste.get(pIndex+1);
		}
		
		if(liste.contains("-u")){
			int uIndex = liste.indexOf("-u");
			username = liste.get(uIndex+1);
		}
		
		if(liste.contains("-fn")){
			int fnIndex = liste.indexOf("-u");
			firstName = liste.get(fnIndex+1);
		}
		
		if(liste.contains("-ln")){
			int lnIndex = liste.indexOf("-u");
			lastName = liste.get(lnIndex+1);
		}
		
		System.out.println(username +" "+ password+" "+ firstName+" "+" "+lastName);
		
	}
	
	public static void main(String args[]){
		String[] lol = {"register, -u, dzenan, -p, mittpassord, -fn, -ln"};
	}

}
