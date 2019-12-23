import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Background;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class HelpPane extends Pane 
{
    HelpPane(){
        String instruction = "\t\t\t Instruction:\r\n";
        instruction += "Usage: \r\n";
        instruction += "  Click [Begin] to begin and Click [help] to show this page.\r\n";
        instruction += "  Support single player to [Double],[HitMe],[Stand],[Surrender].\r\n";
        instruction += "  When possible, you can choose to [Split] or [Insurance].\r\n";
        instruction += "  If you choose [Split], no [Double], and need act for two decks.\r\n";
        instruction += "  After each play, you can choose to play [Again] and [Quit].\r\n";
        instruction += "  >> Notice, all the process is saved in \"log.txt\".\r\n";
        instruction += "Rule (simplified version): \r\n";
        instruction += "  If you have BJ, and host not: return 1.5*deal.\r\n";
        instruction += "  If you have BJ, and host has one: a tie.\r\n";
        instruction += "  If host has a \"A\" as shown card, you can buy insurance.\r\n";
        instruction += "  If host has BJ and you have insurance, you only pay 0.5*deal.\r\n";
        instruction += "  Else if you haven't insurance, you need pay 1*deal.\r\n";
        instruction += "  >> Notice, you can't [Double] after once hit.\r\n";
        instruction += "  If you have two same card(different in suits), you can [Split].\r\n";
        instruction += "  After [Split], no more double and play two decks individually.\r\n";
        instruction += "  After [Split], the BJ can only be served as normal 21 point.\r\n";
        instruction += "For more: https://baike.baidu.com/item/21%E7%82%B9/5481683\r\n";
        Label helplabel = new Label(instruction);
        helplabel.setFont(Font.font("Consolas",FontWeight.BOLD, 11));
        helplabel.setTextFill(Color.WHEAT);
        BackgroundImage myBI= new BackgroundImage(new Image("./card/PLAYBG.jpg",400,300,false,true),BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        Background myBG = new Background(myBI);
        setBackground(myBG);
        getChildren().add(helplabel);
    }
}