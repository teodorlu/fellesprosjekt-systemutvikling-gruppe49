package controller;

import java.util.Arrays;
import java.util.List;

public class CommandExecuter {
	
	public void register(String[] array){
		
		List<String> liste = Arrays.asList(array);
		
		if(liste.contains("-p")){
			int pIndex = liste.indexOf("-p");
			String password = liste.get(pIndex+1);
		}
		
		if(liste.contains("-u")){
			int uIndex = liste.indexOf("-u");
			String username = liste.get(uIndex+1);
		}
		
		if(liste.contains("-fn")){
			int fnIndex = liste.indexOf("-u");
			String firstName = liste.get(fnIndex+1);
		}
		
		if(liste.contains("-ln")){
			int lnIndex = liste.indexOf("-u");
			String lastName = liste.get(lnIndex+1);
		}
		
		System.out.println(username +" "+ password+" "+ firstName+" "+" "+lastName);
		
	}

}
