package viewmodel;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;

public class MyViewModel extends Observable implements ViewModel,Observer {

	private SokobanServer server;
	private Model model;

	private StringProperty numOfClients;
	private StringProperty serverStatus;
	private boolean serverActive = false;


	public MyViewModel(Model model,int port){
		this.model = model;
		server = new SokobanServer(model,port);
		numOfClients = new SimpleStringProperty();
		serverStatus = new SimpleStringProperty();
	}

	@Override
	public void update(Observable arg0, Object arg1) {

		Platform.runLater(()->{
			numOfClients.set(Integer.toString(model.getNumOfClients()));
			if(serverActive)
				serverStatus.set("On");
			else serverStatus.set("Off");
			setChanged();
			notifyObservers(model.getConnectedClients());
		});

	}

	@Override
	public void startServer() {
		Platform.runLater(()->serverStatus.set("Starting.."));
		server.start();
		serverActive = true;
	}

	@Override
	public void stopServer() {
		
		Platform.runLater(()->{
			serverStatus.set("Stoping..");
			server.stopServer();
			serverActive = false;
			model.deleteAllClients();
		});

	}

	@Override
	public void close() {
		if(serverActive)
			stopServer();
		model.close();
	}

	@Override
	public StringProperty getNumOfClients() {
		return numOfClients;
	}

	public StringProperty getServerStatus() {
		return serverStatus;
	}


}
