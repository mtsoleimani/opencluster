package io.taranis.opencluster;

public class App {

	public static void main(String[] args) throws Exception {

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				
				//TODO
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));

	}

}
