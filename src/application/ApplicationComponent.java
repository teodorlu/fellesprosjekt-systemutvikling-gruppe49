package application;

import model.*;
import view.*;
import controller.*;

public abstract class ApplicationComponent {
	
	private Application app;
	
	public ApplicationComponent(Application app){
		this.app = app;
	}
	
	public Application getApplication(){
		return this.app;
	}
	
	@SuppressWarnings("unused")
	private ApplicationComponent() {}
}