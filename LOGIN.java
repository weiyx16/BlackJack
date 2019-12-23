import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Background;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class LOGIN extends Application
{
	// Override the start method in the Application class
	@Override
	public void start(Stage primaryStage)
	{		
		// Begin Button
		// Create a pane
		Pane loginpane=new Pane();
		ImageView head = new ImageView(new Image("./card/head.png",400,60,false,true));
		head.setLayoutX(10);
		head.setLayoutY(50);
		Button beginbt = new Button("Begin");
		beginbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		beginbt.setTextFill(new Color(0,0,0,1));
		beginbt.setLayoutX(180.0);
		beginbt.setLayoutY(160.0);
		beginbt.setOnMouseClicked(
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
					MainPane mainpane=new MainPane(primaryStage);
					Scene mainscene=new Scene(mainpane,400,300);
					// mainscene.getStylesheets().add(getClass().getResource("font.css").toExternalForm());
					primaryStage.setScene(mainscene);
				}
			}
		);

		// Help Button
		Button helpbt = new Button("Help");
		helpbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		helpbt.setTextFill(new Color(0,0,0,1));
		helpbt.setLayoutX(183.0);
		helpbt.setLayoutY(200.0);
		helpbt.setOnMouseClicked(
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
					HelpPane helppane=new HelpPane();	
					Scene helpscene=new Scene(helppane,400,300);
					Stage helpstage=new Stage();
					helpstage.setTitle("BlackJackHelp");
					helpstage.setScene(helpscene);
					helpstage.getIcons().add(new Image("./card/icon.png"));
					helpstage.show();
				}
			}
		);
		
		BackgroundImage myBI= new BackgroundImage(new Image("./card/LOGINBG.jpg",400,300,false,true),BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
		loginpane.getChildren().addAll(head, beginbt, helpbt);
		loginpane.setBackground(new Background(myBI));
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