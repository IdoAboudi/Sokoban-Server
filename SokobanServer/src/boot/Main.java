package boot;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.MyModel;
import model.data.database.MySolutionAgent;
import model.data.database.SokobanDBManager;
import solver.SokobanSolver;
import view.AdminController;
import viewmodel.MyViewModel;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			MyModel model = new MyModel(new MySolutionAgent(),new SokobanSolver(),new SokobanDBManager());
			MyViewModel vm = new MyViewModel(model,11934);
			model.addObserver(vm);
			FXMLLoader loader = new FXMLLoader();
			BorderPane root = (BorderPane) loader.load(getClass().getResource("Admin.fxml").openStream());
			AdminController view = (AdminController) loader.getController();
			view.setViewModel(vm);
			vm.addObserver(view);
			Scene scene = new Scene(root,350,170);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Admin view");
			primaryStage.show();
			primaryStage.setOnCloseRequest((e)->{
				view.close();
				Platform.exit();
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
