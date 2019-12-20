import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.List; 
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class MainPane extends Pane 
{
    private Stage primaryStage;
    private int money;
    private int deal;
    private List idx_list = new ArrayList<Integer>();
    private int cur_card_loc; 
    private int user_point;
    private List user_card_list = new ArrayList<Integer>();
    private int host_point;
    private List host_card_list = new ArrayList<Integer>();
    final private String card_prefix = "./card/";
    final private String card_ext = ".png";
    
    MainPane(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.money = 2000;
        init_params();
        init_pane();
    }

    private void init_params(){
        this.deal = 0;
        this.cur_card_loc = 0;
        for(int i=1; i<53; i++){  
            this.idx_list.add(i);  
        }
        Collections.shuffle(this.idx_list);
        this.user_card_list = new ArrayList<Integer>();
        this.user_point = 0;
        this.host_card_list = new ArrayList<Integer>();
        this.host_point = 0;
    }

    private void init_pane(){
        Label dealhint = new Label("Bet: ");
        TextField dealin = new TextField(Integer.toString(this.deal));
        Label moneyhint = new Label("Money: "+Integer.toString(this.money));

        // Help Button
		Button dealbt = new Button("Deal");
		dealbt.setLayoutX(350.0);
		dealbt.setLayoutY(250.0);
		dealbt.setOnMouseClicked(e -> {
                this.deal = Integer.valueOf(dealin.getText());
                this.money -= this.deal;
                beginplaypane();}
        );
        getChildren().clear();
        getChildren().addAll(dealhint, dealin, moneyhint, dealbt);
    }

    private void beginplaypane(){
        // Initial necessary show.
        Label dealhint = new Label("Bet: "+Integer.toString(this.deal));
        Label moneyhint = new Label("Money: "+Integer.toString(this.money));
        
        this.user_card_list.add(this.idx_list.get(this.cur_card_loc));
        ImageView user_card_left = new ImageView();
        user_card_left.setX(150);
        user_card_left.setY(200);
        user_card_left.setImage(new Image(idx_2_path()));
        
        this.cur_card_loc += 1;
        this.user_card_list.add(this.idx_list.get(this.cur_card_loc));
        ImageView user_card_right = new ImageView();
        user_card_right.setX(200);
        user_card_right.setY(200);
        user_card_right.setImage(new Image(idx_2_path()));

        this.cur_card_loc += 1;
        this.host_card_list.add(this.idx_list.get(this.cur_card_loc));
        ImageView host_card_left = new ImageView();
        host_card_left.setX(150);
        host_card_left.setY(50);
        host_card_left.setImage(new Image(idx_2_path()));

        this.cur_card_loc += 1;
        this.host_card_list.add(this.idx_list.get(this.cur_card_loc));
        ImageView host_card_right = new ImageView();
        host_card_right.setImage(new Image(idx_2_path("b1fv")));
        host_card_right.setX(200);
        host_card_right.setY(50);

        calc_point(true);
        calc_point(false);
        System.out.printf(" Debug>%d", this.user_point);
        System.out.printf(" Debug>%d", this.host_point);
        getChildren().clear();
        getChildren().addAll(dealhint, moneyhint, user_card_left, user_card_right, host_card_left, host_card_right);
        
        if (this.user_point==21){
            host_card_right.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
            if (this.host_point==21){
                this.money += this.deal;
            }
            else{
                this.money += (int)this.deal*2.5;
            }
            quitpane();
        }
        else{
            if (this.host_point==21){
                // Not A
                if ((Integer)this.host_card_list.get(1)%13 != 1){
                    host_card_right.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
                    quitpane();
                }
                else{
                    // Support Insurance here.
                }
            }
            else{
                userplaypane();
            }
        }
        
    }

    private void userplaypane(){
        HBox opbox = new HBox(5);
        opbox.setAlignment(Pos.BOTTOM_CENTER);
        opbox.setLayoutX(50);
        opbox.setLayoutY(250);
        // Double Button
		Button doublebt = new Button("Double");
		doublebt.setOnMouseClicked(
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
                    // Double
				}
			}
        );
        Button hitbt = new Button("Hit me");
		hitbt.setOnMouseClicked(
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
                    // hit
				}
			}
        );
        Button standbt = new Button("Stand");
		standbt.setOnMouseClicked(
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
                    // Stand
                    hostplaypane();
				}
			}
        );
        Button surrenderbt = new Button("Surrender");
		surrenderbt.setOnMouseClicked(e->{
                this.money += this.deal/2;
                quitpane();
            }
        );
        Button splitbt = new Button("Split");
		splitbt.setOnMouseClicked(
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
                    // Split
				}
			}
        );
        opbox.getChildren().addAll(doublebt, hitbt, standbt, surrenderbt, splitbt);
        getChildren().add(opbox);
    }

    private void hostplaypane(){

    }

    private void quitpane(){
		// Again Button
		Button againbt = new Button("Again");
		againbt.setLayoutX(200.0);
		againbt.setLayoutY(160.0);
		againbt.setOnMouseClicked(
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent e){
                    init_params();
                    init_pane();
				}
			}
		);

		// Quit Button
		Button quitbt = new Button("Quit");
		quitbt.setLayoutX(200.0);
		quitbt.setLayoutY(200.0);
		quitbt.setOnMouseClicked(e->{
				this.primaryStage.close();
            }
        );
        
        getChildren().addAll(againbt, quitbt);
    }

    private void calc_point(boolean isuser){
        // Calculate point according to the current card list
        int tmp_point_except_aces = 0;
        int aces_number = 0;
        List tmp_list = new ArrayList<Integer>(); 
        if (isuser){
            tmp_list = this.user_card_list;
        }
        else{
            tmp_list = this.host_card_list;
        }
        for (int i=0;i<tmp_list.size();i++){
            int cur_idx = (Integer)tmp_list.get(i);
            if (cur_idx != 1){
                tmp_point_except_aces += idx_2_point(cur_idx);
            }
            else{
                aces_number += 1;
            }
        }
        int aces_equal_1 = 0;
        int tmp_point = tmp_point_except_aces;
        while (aces_equal_1 <= aces_number){
            tmp_point = tmp_point_except_aces + (aces_number-aces_equal_1)*10 + aces_equal_1*1;
            aces_equal_1 += 1;
            if (tmp_point <= 21){
                break;
            }
        }
        if (isuser){
            this.user_point = tmp_point;
        }
        else{
            this.host_point = tmp_point;
        }
    }

    private int idx_2_point(int idx){
        if (1<(idx%13) && (idx%13)<10){
            return idx%13;
        }
        else{
            return 10;
        }
    }

    private String idx_2_path(){
        return this.card_prefix + String.valueOf(this.idx_list.get(this.cur_card_loc)) + this.card_ext; 
        
    }
    private String idx_2_path(String img_name){
        return this.card_prefix + img_name + this.card_ext; 
    }
}