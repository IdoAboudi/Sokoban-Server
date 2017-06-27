package viewmodel;

import javafx.beans.property.StringProperty;

public interface ViewModel {

	public void startServer();
	public void stopServer();
	public StringProperty getNumOfClients();
	public StringProperty getServerStatus();
	public void close();
}
