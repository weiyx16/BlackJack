import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
// import javafx.event.EventHandler;
// import javafx.scene.input.MouseEvent;
// import javafx.scene.control.Button;

public class HelpPane extends Pane 
{
    HelpPane(){
        Label help = new Label("HELP");
        getChildren().addAll(help);
    }
}