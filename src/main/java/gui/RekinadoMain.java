
package rekinado;

import java.io.FileNotFoundException;

public class RekinadoMain {

	public static class UIListener {
		public void actualizeUI(GUInterface newui){
			gui = newui;
		}
		
		public void simulate(){
			gui.print();
			
		}

	}
	private static GUInterface gui = new GUInterface();
	private static final UIListener uilistener = new UIListener();
	

	public static void main(String[] args) {
		gui.addListener(uilistener);
	}
	
}
