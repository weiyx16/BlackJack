import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class LOGIN extends Application
{
	// Override the start method in the Application class
	@Override
	public void start(Stage primaryStage)
	{		
		// Begin Button
		Button beginbt = new Button("Begin");
		beginbt.setLayoutX(200.0);
		beginbt.setLayoutY(160.0);
		beginbt.setOnMouseClicked(
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
					MainPane mainpane=new MainPane(primaryStage);
					Scene mainscene=new Scene(mainpane,400,300);
					primaryStage.setScene(mainscene);
				}
			}
		);

		// Help Button
		Button helpbt = new Button("Help");
		helpbt.setLayoutX(200.0);
		helpbt.setLayoutY(200.0);
		helpbt.setOnMouseClicked(
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
					
				}
			}
		);
		
		// Create a pane
		Pane loginpane=new Pane();
		loginpane.getChildren().addAll(beginbt, helpbt);
		// Create a loginscene and place it on the stage
		Scene loginscene=new Scene(loginpane,400,300);
		primaryStage.setTitle("BlackJack");
		primaryStage.setScene(loginscene);
		primaryStage.getIcons().add(new Image("./card/icon.png"));
		primaryStage.show();
	}
	/** The main method is only needed for the IDE with limited JavaFX support. 
		Not needed for running from the command line.
	*/
	public static void main(String[] args)
	{
		Application.launch(args);
	}	
}