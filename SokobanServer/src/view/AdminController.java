package view;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import viewmodel.SokobanServer;
import viewmodel.ViewModel;

public class AdminController implements Observer,View,Initializable{

	SokobanServer server;
	
	@FXML
	Label clientsLabel,statusLabel;
	
	@FXML
	ListView<String> listView;
	
	@FXML
	TextArea textArea;
	
	@FXML
	Button startButton,stopButton;
	
	ViewModel viewModel;
	int port;
	
	public ViewModel getViewModel() {
		return viewModel;
	}

	public void setViewModel(ViewModel viewModel) {
		
		this.viewModel = viewModel;
		clientsLabel.textProperty().bind(viewModel.getNumOfClients());
		statusLabel.textProperty().bind(viewModel.getServerStatus());
	}

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		port = 11934;
	}
	
	@Override
	public void stopServer(){
		if(viewModel != null)
			Platform.runLater(()->viewModel.stopServer());
		//disable buttons for 5 seconds until server is shutdown completely
		startButton.setDisable(true);
		stopButton.setDisable(true);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				startButton.setDisable(false);
				stopButton.setDisable(false);
				
			}
		}, 5000);
		
	}
	
	@Override
	public void startServer(){
		if(viewModel != null)
			Platform.runLater(()->viewModel.startServer());
	}
	
	public Label getClientsLabel() {
		return clientsLabel;
	}

	public void setClientsLabel(Label clientsLabel) {
		this.clientsLabel = clientsLabel;
	}

	public Label getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(Label statusLabel) {
		this.statusLabel = statusLabel;
	}

	public ListView<String> getListView() {
		return listView;
	}

	public void setListView(ListView<String> listView) {
		this.listView = listView;
	}

	@Override
	public void close() {
		viewModel.close();
		Platform.exit();
	}

	@Override
	public void update(Observable o, Object arg) {
		
		List<String> clients = (List<String>) arg;
		listView.setItems(FXCollections.observableArrayList(clients));
	}
	
}
