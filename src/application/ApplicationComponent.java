package application;

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