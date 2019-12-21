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
    // Process related params
    private Stage primaryStage;
    private int money;
    private int deal;
    private List idx_list = new ArrayList<Integer>();
    private int cur_card_order; 
    private int user_point;
    private List user_card_list = new ArrayList<Integer>();
    private int host_point;
    private List host_card_list = new ArrayList<Integer>();
    private boolean insurance = false;

    // GUI related params
    private int card_X_loc = 150;
    private int card_margin = 25;
    private int card_Y_loc_user = 150;
    private int card_Y_loc_host = 50;
    final private String card_prefix = "./card/";
    final private String card_ext = ".png";
    private ImageView host_card_hidden = new ImageView();

    MainPane(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.money = 2000;
        init_params();
        init_pane();
    }

    private void init_params(){
        this.deal = 0;
        this.cur_card_order = 0;
        for(int i=1; i<53; i++){  
            this.idx_list.add(i);  
        }
        Collections.shuffle(this.idx_list);
        this.user_card_list = new ArrayList<Integer>();
        this.user_point = 0;
        this.host_card_list = new ArrayList<Integer>();
        this.host_point = 0;
        this.insurance = false;
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
        
        this.user_card_list.add(this.idx_list.get(this.cur_card_order));
        ImageView user_card_left = new ImageView();
        user_card_left.setX(this.card_X_loc+this.card_margin*this.user_card_list.size());
        user_card_left.setY(this.card_Y_loc_user);
        user_card_left.setImage(new Image(idx_2_path()));
        
        this.cur_card_order += 1;
        this.user_card_list.add(this.idx_list.get(this.cur_card_order));
        ImageView user_card_right = new ImageView();
        user_card_right.setX(this.card_X_loc+this.card_margin*this.user_card_list.size());
        user_card_right.setY(this.card_Y_loc_user);
        user_card_right.setImage(new Image(idx_2_path()));

        this.cur_card_order += 1;
        this.host_card_list.add(this.idx_list.get(this.cur_card_order));
        ImageView host_card_left = new ImageView();
        host_card_left.setX(this.card_X_loc+this.card_margin*this.host_card_list.size());
        host_card_left.setY(this.card_Y_loc_host);
        host_card_left.setImage(new Image(idx_2_path()));

        this.cur_card_order += 1;
        this.host_card_list.add(this.idx_list.get(this.cur_card_order));
        this.host_card_hidden.setImage(new Image(idx_2_path("b1fv")));
        this.host_card_hidden.setX(this.card_X_loc+this.card_margin*this.host_card_list.size());
        this.host_card_hidden.setY(this.card_Y_loc_host);

        calc_point(true);
        System.out.printf("User card<%d\r\n",this.user_point);
        calc_point(false);
        System.out.printf("Host card<%d\r\n",this.host_point);
        getChildren().clear();
        getChildren().addAll(dealhint, moneyhint, user_card_left, user_card_right, host_card_left, this.host_card_hidden);
        
        if (this.user_point==21){
            this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
            if (this.host_point==21){
                this.money += this.deal;
            }
            else{
                this.money += (int)this.deal*2.5;
            }
            quitpane();
        }
        else{
            if ((Integer)this.host_card_list.get(0)%13 == 1){
                // Support Insurance here. The shown card is A
                this.insurance = true;
                // Insurance Button
                Button insbt = new Button("Insurance");
                insbt.setLayoutX(200.0);
                insbt.setLayoutY(160.0);
                insbt.setOnMouseClicked(e -> {
                        this.money -= (int)this.deal/2;
                        if (this.host_point==21){
                            this.money += this.deal;
                            this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
                            quitpane();
                        }
                        else{
                            userplaypane();
                        }
                    }
                );
                // Skip Insurance Button
                Button skipbt = new Button("Skip");
                skipbt.setLayoutX(200.0);
                skipbt.setLayoutY(200.0);
                skipbt.setOnMouseClicked(e -> {
                        if (this.host_point==21){
                            this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
                            quitpane();
                        }
                        else{
                            userplaypane();
                        }
                    }
                );
                getChildren().addAll(insbt,skipbt);
            }
            else if (this.host_point==21){
                this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
                quitpane();
            }
            else{
                userplaypane();
            }
        }
        
    }

    private void user_add_card(){
        this.cur_card_order += 1;
        this.user_card_list.add(this.idx_list.get(this.cur_card_order));
        ImageView user_card_new = new ImageView();
        user_card_new.setX(this.card_X_loc+this.card_margin*this.user_card_list.size());
        user_card_new.setY(this.card_Y_loc_user);
        user_card_new.setImage(new Image(idx_2_path()));
        calc_point(true);
        System.out.printf("User card<%d\r\n",this.user_point);
        getChildren().add(user_card_new);
    }

    private void host_add_card(){
        this.cur_card_order += 1;
        this.host_card_list.add(this.idx_list.get(this.cur_card_order));
        ImageView host_card_new = new ImageView();
        host_card_new.setX(this.card_X_loc+this.card_margin*this.host_card_list.size());
        host_card_new.setY(this.card_Y_loc_host);
        host_card_new.setImage(new Image(idx_2_path()));
        calc_point(false);
        System.out.printf("Host card<%d\r\n",this.host_point);
        getChildren().add(host_card_new);
    }

    private void userplaypane(){
        if (this.insurance){
            this.insurance = false;
            getChildren().remove(getChildren().size()-2, getChildren().size());
        }
        HBox opbox = new HBox(5);
        opbox.setAlignment(Pos.BOTTOM_CENTER);
        opbox.setLayoutX(50);
        opbox.setLayoutY(250);
        // Double Button
		Button doublebt = new Button("Double");
		doublebt.setOnMouseClicked(e->{
                this.money -= this.deal;
                this.deal *= 2;
                user_add_card();
                if (this.user_point > 21){
                    quitpane();
                }
                else{
                    hostplaypane();
                }
            }
        );
        Button hitbt = new Button("Hit me");
		hitbt.setOnMouseClicked(e->{
                user_add_card();
                if (this.user_point > 21){
                    quitpane();
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
        // Hidden card is shown
        this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));

        while (this.host_point < 17){
            host_add_card();
        }

        if(this.host_point > 21){
            this.money += 2*this.deal;
        }
        else{
            if(this.user_point > this.host_point){
                this.money += 2*this.deal;
            }
            else if(this.user_point == this.host_point){
                this.money += this.deal;
            }
            else{
                // lose
            }
        }
        quitpane();
    }

    private void quitpane(){
        
        if (this.insurance){
            this.insurance = false;
            getChildren().remove(getChildren().size()-2, getChildren().size());
        }
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
            tmp_point_except_aces += idx_2_point(cur_idx);
            if ((cur_idx%13 == 1)){
                aces_number += 1;
            }
        }
        int aces_equal_11 = aces_number;
        int tmp_point = tmp_point_except_aces;
        while (aces_equal_11>=0){
            tmp_point = tmp_point_except_aces + aces_equal_11*10;
            aces_equal_11 -= 1;
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
        if ((idx%13)<10 && (idx%13)>0){
            return idx%13;
        }
        else{
            return 10;
        }
    }

    private String idx_2_path(){
        return this.card_prefix + String.valueOf(this.idx_list.get(this.cur_card_order)) + this.card_ext; 
        
    }
    private String idx_2_path(String img_name){
        return this.card_prefix + img_name + this.card_ext; 
    }
}