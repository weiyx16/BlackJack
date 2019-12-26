import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.util.ArrayList;  
import java.util.Collections;  
import java.util.List; 
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import javafx.scene.control.Alert;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.Background;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class MainPane extends Pane 
{
    // Process related params
    private Stage primaryStage;
    private int money;
    private int past_money;
    private int deal;
    private List idx_list = new ArrayList<Integer>();
    private int cur_card_order; 
    private int user_point;
    private List user_card_list = new ArrayList<Integer>();
    private int host_point;
    private List host_card_list = new ArrayList<Integer>();
    private boolean insurance = false;
    private boolean is_split = false;
    private List user_card_list_aux = new ArrayList<Integer>();
    private int user_point_aux;
    // Check two splits status
    private boolean main_done = false;
    private boolean aux_done = false;
    private boolean main_bust = false;
    private boolean aux_bust = false;
    private boolean main_sur = false;
    private boolean aux_sur = false;

    // GUI related params
    private int card_X_loc = 125;
    private int card_X_loc_main = 50;
    private int card_X_loc_aux = 200;
    private int card_margin = 20;
    private int card_margin_aux = 18;
    private int card_Y_loc_user = 150;
    private int card_Y_loc_host = 35;
    final private String card_prefix = "./card/";
    final private String card_ext = ".png";
    private ImageView host_card_hidden = new ImageView();
    private ImageView user_card_left = new ImageView();
    private ImageView user_card_right = new ImageView();
    private Label pointhint = new Label();
    private Label pointhint_aux = new Label();
    private Label dealhint = new Label();
    private Label moneyhint = new Label();
    private File bgmusic = new File("card\\bgMusic.wav");
    private MediaPlayer mp1 = new MediaPlayer(new Media(bgmusic.toURI().toString()));
    private MediaView mView = new MediaView(this.mp1);
    // Log relate params
    private String filePath = "./log.txt";
    private List log = new ArrayList<String>();

    MainPane(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.money = 1000;
        this.past_money = 1000;
        BackgroundImage myBI= new BackgroundImage(new Image("./card/PLAYBG.jpg",400,300,false,true),BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,BackgroundSize.DEFAULT);
        Background myBG = new Background(myBI);
        setBackground(myBG);
        this.mp1.setCycleCount(MediaPlayer.INDEFINITE);
        this.mp1.play();
        this.pointhint.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
        this.pointhint.setTextFill(Color.WHEAT);
        this.pointhint_aux.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		this.pointhint_aux.setTextFill(Color.WHEAT);
        init_params();
        init_pane();
    }

    private void init_params(){
        this.deal = 2;
        this.past_money = this.money;
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
        this.is_split = false;
        this.user_card_list_aux = new ArrayList<Integer>();
        this.user_point_aux = 0;
        this.main_done = false;
        this.aux_done = false;
        this.main_bust = false;
        this.aux_bust = false;
        this.main_sur = false;
        this.aux_sur = false;
        this.log = new ArrayList<String>();
    }

    private void init_pane(){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss a"); //a:am/pm 
        Date date = new Date();
        this.log.add("-----------------------------");
        this.log.add(sdf.format(date));

        this.dealhint.setText("Bet: ");
        this.dealhint.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		this.dealhint.setTextFill(Color.WHEAT);
        this.dealhint.setLayoutX(20);
        this.dealhint.setLayoutY(20);
        TextField dealin = new TextField(Integer.toString(this.deal));
        dealin.setLayoutX(50);
        dealin.setLayoutY(18);
        dealin.setFont(Font.font("Agency FB",FontWeight.BOLD, 14));
        // dealin.setMaxSize(dealin.getWidth()/2, dealin.getHeight());
        this.moneyhint.setText("Money: "+Integer.toString(this.money));
        this.moneyhint.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		this.moneyhint.setTextFill(Color.WHEAT);
        this.moneyhint.setLayoutX(300);
        this.moneyhint.setLayoutY(20);

        // Help Button
        Button dealbt = new Button("Deal");
        dealbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		dealbt.setTextFill(new Color(0,0,0,1));
		dealbt.setLayoutX(183.0);
		dealbt.setLayoutY(250.0);
		dealbt.setOnMouseClicked(e -> {
                this.deal = Integer.valueOf(dealin.getText());
                if (this.deal > this.money){
                    this.deal = this.money;
                }
                this.money -= this.deal;
                update_dealmoney();
                beginplaypane();
                this.log.add(">> Bet in: "+Integer.toString(this.deal)+" Left Money: "+Integer.toString(this.money));
            }
        );
        getChildren().clear();
        
        // Quiet Button
        ImageView quietbt = new ImageView();
        if (this.mp1.getStatus() == Status.PAUSED){
            quietbt.setImage(new Image("./card/quiet.png",16,16,false,true));
        }
        if (this.mp1.getStatus() == Status.PLAYING){
            quietbt.setImage(new Image("./card/voice.png",16,16,false,true));
        }
		quietbt.setLayoutX(10.0);
		quietbt.setLayoutY(275.0);
		quietbt.setOnMouseClicked(e -> {
                if (this.mp1.getStatus() == Status.PAUSED){
                    this.mp1.play();
                    quietbt.setImage(new Image("./card/voice.png",16,16,false,true));
                }
                if (this.mp1.getStatus() == Status.PLAYING){
                    this.mp1.pause();
                    quietbt.setImage(new Image("./card/quiet.png",16,16,false,true));
                }
            }
        );
        getChildren().addAll(this.dealhint, this.moneyhint, this.mView, quietbt, dealbt, dealin);
        
    }

    private void beginplaypane(){
        // Initial necessary show.
        getChildren().remove(getChildren().size()-2, getChildren().size());
        this.user_card_list.add(this.idx_list.get(this.cur_card_order));
        this.user_card_left.setX(this.card_X_loc+this.card_margin*this.user_card_list.size());
        this.user_card_left.setY(this.card_Y_loc_user);
        this.user_card_left.setImage(new Image(idx_2_path()));
        
        this.cur_card_order += 1;
        this.user_card_list.add(this.idx_list.get(this.cur_card_order));
        this.user_card_right.setX(this.card_X_loc+this.card_margin*this.user_card_list.size());
        this.user_card_right.setY(this.card_Y_loc_user);
        this.user_card_right.setImage(new Image(idx_2_path()));

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

        calc_point(0);
        this.pointhint.setText(String.valueOf(this.user_point));
        this.pointhint.setLayoutX(200);
        this.pointhint.setLayoutY(250);
        calc_point(1);
        this.log.add("> Initial Card: Host shown card - "+idx_2_chinese((Integer)this.host_card_list.get(0))+" Host hidden card - "+idx_2_chinese((Integer)this.host_card_list.get(1))+", Card Point - "+Integer.toString(this.host_point));
        this.log.add("> Initial Card: User card - "+idx_2_chinese((Integer)this.user_card_list.get(0))+", "+idx_2_chinese((Integer)this.user_card_list.get(1))+", Card Point - "+Integer.toString(this.user_point));
        getChildren().addAll(this.user_card_left, this.user_card_right, this.pointhint, host_card_left, this.host_card_hidden);
        
        if (this.user_point==21){
            this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
            // pause_half_sec();
            if (this.host_point==21){
                this.money += this.deal;
                update_dealmoney();
                this.log.add(">> Tie for both BJ, with money left: "+Integer.toString(this.money));
            }
            else{
                this.money += (int)this.deal*2.5;
                update_dealmoney();
                this.log.add(">> User win for User BJ, with money left: "+Integer.toString(this.money));
            }
            quitpane();
        }
        else{
            if ((Integer)this.host_card_list.get(0)%13 == 1){
                // Support Insurance here. The shown card is A
                this.insurance = true;
                // Insurance Button
                Button insbt = new Button("Insurance");
                insbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
                insbt.setTextFill(new Color(0,0,0,1));
                insbt.setLayoutX(170.0);
                insbt.setLayoutY(160.0);
                insbt.setOnMouseClicked(e -> {
                        this.money -= (int)this.deal/2;
                        update_dealmoney();
                        this.log.add("> User choose insurance, with money left: "+Integer.toString(this.money));
                        if (this.host_point==21){
                            this.money += this.deal;
                            update_dealmoney();
                            this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
                            this.log.add(">> User lose for Host BJ, with money left: "+Integer.toString(this.money));
                            quitpane();
                        }
                        else{
                            if ((Integer)this.user_card_list.get(0)%13 == (Integer)this.user_card_list.get(1)%13){
                                userplaypane_split();
                            }
                            else{
                                userplaypane();
                            }
                        }
                    }
                );
                // Skip Insurance Button
                Button skipbt = new Button("Skip");
                skipbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
                skipbt.setTextFill(new Color(0,0,0,1));
                skipbt.setLayoutX(183.0);
                skipbt.setLayoutY(200.0);
                skipbt.setOnMouseClicked(e -> {
                    this.log.add("> User skip insurance");
                        if (this.host_point==21){
                            this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
                            this.log.add(">> User lose for Host BJ, with money left: "+Integer.toString(this.money));
                            quitpane();
                        }
                        else{
                            if ((Integer)this.user_card_list.get(0)%13 == (Integer)this.user_card_list.get(1)%13){
                                userplay_split_check();
                            }
                            else{
                                userplaypane();
                            }
                        }
                    }
                );
                getChildren().addAll(insbt,skipbt);
            }
            else if (this.host_point==21){
                this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
                this.log.add(">> User lose for Host BJ, with money left: "+Integer.toString(this.money));
                quitpane();
            }
            else{
                if ((Integer)this.user_card_list.get(0)%13 == (Integer)this.user_card_list.get(1)%13){
                    userplay_split_check();
                }
                else{
                    userplaypane();
                }
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
        calc_point(0);
        this.pointhint.setText(String.valueOf(this.user_point));
        getChildren().add(user_card_new);
        String tmplog = "> User add a card, for now: ";
        for (Object current_card: this.user_card_list){
            tmplog += idx_2_chinese((Integer)current_card);
            tmplog += " ";
        }
        tmplog += "and current point is: "+Integer.toString(this.user_point);
        this.log.add(tmplog);
    }

    private void user_add_card(boolean is_aux){
        this.cur_card_order += 1;
        if (is_aux){
            this.user_card_list_aux.add(this.idx_list.get(this.cur_card_order));
            ImageView user_card_new = new ImageView();
            user_card_new.setX(this.card_X_loc_aux+this.card_margin_aux*this.user_card_list_aux.size());
            user_card_new.setY(this.card_Y_loc_user);
            user_card_new.setImage(new Image(idx_2_path()));
            calc_point(2);
            this.pointhint_aux.setText(String.valueOf(this.user_point_aux));
            getChildren().add(user_card_new);
            String tmplog = "> User AUX Deck add a card, for now: ";
            for (Object current_card: this.user_card_list_aux){
                tmplog += idx_2_chinese((Integer)current_card);
                tmplog += " ";
            }
            tmplog += "and current point is: "+Integer.toString(this.user_point_aux);
            this.log.add(tmplog);
        }
        else{
            this.user_card_list.add(this.idx_list.get(this.cur_card_order));
            ImageView user_card_new = new ImageView();
            user_card_new.setX(this.card_X_loc_main+this.card_margin_aux*this.user_card_list.size());
            user_card_new.setY(this.card_Y_loc_user);
            user_card_new.setImage(new Image(idx_2_path()));
            calc_point(0);
            this.pointhint.setText(String.valueOf(this.user_point));
            getChildren().add(user_card_new);
            String tmplog = "> User MAIN Deck add a card, for now: ";
            for (Object current_card: this.user_card_list){
                tmplog += idx_2_chinese((Integer)current_card);
                tmplog += " ";
            }
            tmplog += "and current point is: "+Integer.toString(this.user_point);
            this.log.add(tmplog);
        }
    }

    private void host_add_card(){
        this.cur_card_order += 1;
        this.host_card_list.add(this.idx_list.get(this.cur_card_order));
        ImageView host_card_new = new ImageView();
        host_card_new.setX(this.card_X_loc+this.card_margin*this.host_card_list.size());
        host_card_new.setY(this.card_Y_loc_host);
        host_card_new.setImage(new Image(idx_2_path()));
        calc_point(1);
        getChildren().add(host_card_new);
        String tmplog = "> Host add a card, for now: ";
        for (Object current_card: this.host_card_list){
            tmplog += idx_2_chinese((Integer)current_card);
            tmplog += " ";
        }
        tmplog += "and current point is: "+Integer.toString(this.host_point);
        this.log.add(tmplog);
    }

    private void userplay_split_check(){
        if (this.insurance){
            this.insurance = false;
            getChildren().remove(getChildren().size()-2, getChildren().size());
        }
        this.is_split = true;
        Button splitbt = new Button("Split");
        splitbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		splitbt.setTextFill(new Color(0,0,0,1));
        splitbt.setLayoutX(180.0);
        splitbt.setLayoutY(160.0);
        splitbt.setOnMouseClicked(e->{
                this.money -= this.deal;
                this.deal *= 2;
                update_dealmoney();
                this.log.add("> User Choose Split Cards, with money left: "+Integer.toString(this.money)+" and deal doubled: "+Integer.toString(this.deal));
                userplaypane_split();
            }
        );
        // Skip Split Button
        Button skipbt = new Button("Skip");
        skipbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		skipbt.setTextFill(new Color(0,0,0,1));
        skipbt.setLayoutX(183.0);
        skipbt.setLayoutY(200.0);
        skipbt.setOnMouseClicked(e -> {
                this.log.add("> User Skip Split Cards, with money left: "+Integer.toString(this.money)+" and deal doubled: "+Integer.toString(this.deal));
                userplaypane();
            }
        );
        getChildren().addAll(splitbt, skipbt);
    }

    private void userplaypane(){
        if (this.insurance){
            this.insurance = false;
            getChildren().remove(getChildren().size()-2, getChildren().size());
        }
        if (this.is_split){
            this.is_split = false;
            getChildren().remove(getChildren().size()-2, getChildren().size());
        }
        HBox opbox = new HBox(4);
        opbox.setAlignment(Pos.BOTTOM_CENTER);
        opbox.setLayoutX(100);
        opbox.setLayoutY(270);
        // Double Button
        Button doublebt = new Button("Double");
        doublebt.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		doublebt.setTextFill(new Color(0,0,0,1));
		doublebt.setOnMouseClicked(e->{
                if (this.user_card_list.size() == 2){
                    this.money -= this.deal;
                    this.deal *= 2;
                    update_dealmoney();
                    this.log.add("> User Choose Double, with money left: "+Integer.toString(this.money)+" and deal doubled: "+Integer.toString(this.deal));
                    user_add_card();
                    if (this.user_point > 21){
                        this.log.add(">> User lose for User Bust, with money left: "+Integer.toString(this.money));
                        quitpane();
                    }
                    else{
                        hostplaypane();
                    }
                }
                else{
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.titleProperty().set("WARNING");
                    alert.headerTextProperty().set("You can only double at the beginning");
                    alert.showAndWait();
                }
            }
        );
        Button hitbt = new Button("Hit me");
        hitbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		hitbt.setTextFill(new Color(0,0,0,1));
		hitbt.setOnMouseClicked(e->{
                this.log.add("> User Choose Hit me");
                user_add_card();
                // can't double after hit once.
                doublebt.setGraphic(new ImageView(new Image("./card/forbid.png",16,16,false,true)));
                doublebt.setText(null);
                if (this.user_point > 21){
                    this.log.add(">> User lose for User Bust, with money left: "+Integer.toString(this.money));
                    quitpane();
                }
            }
        );
        Button standbt = new Button("Stand");
        standbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		standbt.setTextFill(new Color(0,0,0,1));
		standbt.setOnMouseClicked(e->{
                this.log.add("> User Choose Stand");
                hostplaypane();
            }
        );
        Button surrenderbt = new Button("Surrender");
        surrenderbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		surrenderbt.setTextFill(new Color(0,0,0,1));
		surrenderbt.setOnMouseClicked(e->{
                this.money += this.deal/2;
                update_dealmoney();
                this.log.add(">> User lose for User Surrendered, with money left: "+Integer.toString(this.money));
                quitpane();
            }
        );
        opbox.getChildren().addAll(doublebt, hitbt, standbt, surrenderbt);
        getChildren().add(opbox);
    }

    private void userplaypane_split(){
        if (this.is_split){
            getChildren().remove(getChildren().size()-2, getChildren().size());
        }
        
        // Split the two cards
        this.user_card_list_aux.add(this.user_card_list.remove(this.user_card_list.size() - 1));
        // Split img view
        // aux is on the right
        this.user_card_right.setX(this.card_X_loc_aux + this.card_margin*this.user_card_list_aux.size()); 
        this.user_card_left.setX(this.card_X_loc_main + this.card_margin*this.user_card_list.size());
        // Split point hint
        this.pointhint.setLayoutX(100);
        this.pointhint.setLayoutY(250);
        this.pointhint_aux.setLayoutX(250);
        this.pointhint_aux.setLayoutY(250);
        // Assign new card to each
        this.log.add("> Automatically assign new cards to AUX Deck");
        user_add_card(true);
        this.log.add("> Automatically assign new cards to MAIN Deck");
        user_add_card(false);


        HBox opbox = new HBox(6);
        opbox.setAlignment(Pos.BOTTOM_CENTER);
        opbox.setLayoutX(50);
        opbox.setLayoutY(270);
        Button standbtmain = new Button("Stand L");
        standbtmain.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		standbtmain.setTextFill(new Color(0,0,0,1));
		standbtmain.setOnMouseClicked(e->{
                if (this.user_point>21 || this.main_sur){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.titleProperty().set("WARNING");
                    alert.headerTextProperty().set("The deck you chose have busted or surrender, you can't choose to Stand Anymore");
                    alert.showAndWait();
                }
                else{
                    this.main_done = true;
                    this.log.add(">.. User Stand for MAIN Deck");
                    if (this.aux_done || this.aux_bust || this.aux_sur){
                        hostplaypane();
                    }
                }
            }
        );
        Button surrenderbtmain = new Button("Surrend L");
        surrenderbtmain.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		surrenderbtmain.setTextFill(new Color(0,0,0,1));
		surrenderbtmain.setOnMouseClicked(e->{
                if (this.user_point>21 || this.main_done){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.titleProperty().set("WARNING");
                    alert.headerTextProperty().set("The deck you chose have busted or standed, you can't choose to Surrender Anymore");
                    alert.showAndWait();
                }
                else{
                    this.main_sur = true;
                    this.log.add(">.. User Surrender for MAIN Deck");
                    if (this.aux_done){
                        hostplaypane();
                    }
                    if (this.aux_bust){
                        this.money += (int) this.deal / 4;
                        update_dealmoney();
                        this.log.add(">> User Lose for one Bust and one Surrender, with money left: "+Integer.toString(this.money));
                        quitpane();
                    }
                    if (this.aux_sur){
                        this.money += (int) this.deal / 2;
                        update_dealmoney();
                        this.log.add(">> User Lose for two Decks Surrender, with money left: "+Integer.toString(this.money));
                        quitpane();
                    }
                }
            }
        );
        // hit botton
        Button hitbtmain = new Button("Hit L");
        hitbtmain.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
        hitbtmain.setTextFill(new Color(0,0,0,1));
        hitbtmain.setOnMouseClicked(e->{
                if (this.user_point > 21 || this.main_done || this.main_sur){
                    this.main_bust = true;
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.titleProperty().set("WARNING");
                    alert.headerTextProperty().set("The deck you chose have busted or stand or surrender, you can't choose to HIT ME");
                    alert.showAndWait();
                }
                else{
                    this.log.add("> User Choose Hit me for MAIN Deck");
                    user_add_card(false);
                    if (this.user_point > 21){
                        this.log.add(">.. User MAIN Deck Bust with point: "+Integer.toString(this.user_point));
                        this.main_bust = true;
                        if (this.aux_done){
                            hostplaypane();
                        }
                        if (this.aux_bust){
                            this.log.add(">> User Lose for both Decks Bust, with money left: "+Integer.toString(this.money));
                            quitpane();
                        }
                        if (this.aux_sur){
                            this.money += (int) this.deal / 4;
                            update_dealmoney();
                            this.log.add(">> User Lose for one Bust and one Surrender, with money left: "+Integer.toString(this.money));
                            quitpane();
                        }
                        hitbtmain.setGraphic(new ImageView(new Image("./card/forbid.png",16,16,false,true)));
                        hitbtmain.setText(null);
                        surrenderbtmain.setGraphic(new ImageView(new Image("./card/forbid.png",16,16,false,true)));
                        surrenderbtmain.setText(null);
                        standbtmain.setGraphic(new ImageView(new Image("./card/forbid.png",16,16,false,true)));
                        standbtmain.setText(null);
                    }
                }
            }
        );
        Button standbtaux = new Button("Stand R");
        standbtaux.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		standbtaux.setTextFill(new Color(0,0,0,1));
		standbtaux.setOnMouseClicked(e->{
                if (this.user_point_aux>21 || this.aux_sur){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.titleProperty().set("WARNING");
                    alert.headerTextProperty().set("The deck you chose have busted or surrender, you can't choose to Stand Anymore");
                    alert.showAndWait();
                }
                else{
                    this.aux_done = true;
                    this.log.add(">.. User Stand for AUX Deck");
                    if (this.main_done || this.main_bust || this.main_sur){
                        hostplaypane();
                    }
                }
            }
        );
        Button surrenderbtaux = new Button("Surrend R");
        surrenderbtaux.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		surrenderbtaux.setTextFill(new Color(0,0,0,1));
		surrenderbtaux.setOnMouseClicked(e->{
                if (this.user_point_aux>21 || this.aux_done){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.titleProperty().set("WARNING");
                    alert.headerTextProperty().set("The deck you chose have busted or stand, you can't choose to Surrender Anymore");
                    alert.showAndWait();
                }
                else{
                    this.aux_sur = true;
                    this.log.add(">.. User Surrender for AUX Deck");
                    if (this.main_done){
                        hostplaypane();
                    }
                    if (this.main_bust){
                        this.money += (int) this.deal / 4;
                        update_dealmoney();
                        this.log.add(">> User Lose for one Bust and one Surrender, with money left: "+Integer.toString(this.money));
                        quitpane();
                    }
                    if (this.main_sur){
                        this.money += (int) this.deal / 2;
                        update_dealmoney();
                        this.log.add(">> User Lose for two Decks Surrender, with money left: "+Integer.toString(this.money));
                        quitpane();
                    }
                }
            }
        );
        Button hitbtaux = new Button("Hit R");
        hitbtaux.setFont(Font.font("Agency FB",FontWeight.BOLD, 12));
		hitbtaux.setTextFill(new Color(0,0,0,1));
		hitbtaux.setOnMouseClicked(e->{
                if (this.user_point_aux > 21 || this.aux_done || this.aux_sur){
                    this.aux_bust = true; 
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.titleProperty().set("WARNING");
                    alert.headerTextProperty().set("The deck you chose have busted or stand or surrender, you can't choose to HIT ME");
                    alert.showAndWait();
                }
                else{
                    this.log.add("> User Choose Hit me for AUX Deck");
                    user_add_card(true);
                    if (this.user_point_aux > 21){
                        this.log.add(">.. User AUX Deck Bust with point: "+Integer.toString(this.user_point_aux));
                        this.aux_bust = true;
                        if (this.main_done){
                            hostplaypane();
                        }
                        if (this.main_bust){
                            this.log.add(">> User Lose for both Decks Bust, with money left: "+Integer.toString(this.money));
                            quitpane();
                        }
                        if (this.main_sur){
                            this.money += (int) this.deal / 4;
                            update_dealmoney();
                            this.log.add(">> User Lose for one Bust and one Surrender, with money left: "+Integer.toString(this.money));
                            quitpane();
                        }
                        hitbtaux.setGraphic(new ImageView(new Image("./card/forbid.png",16,16,false,true)));
                        hitbtaux.setText(null);
                        surrenderbtaux.setGraphic(new ImageView(new Image("./card/forbid.png",16,16,false,true)));
                        surrenderbtaux.setText(null);
                        standbtaux.setGraphic(new ImageView(new Image("./card/forbid.png",16,16,false,true)));
                        standbtaux.setText(null);
                    }
                }
            }
        );
        opbox.getChildren().addAll(hitbtmain, standbtmain, surrenderbtmain, hitbtaux, standbtaux, surrenderbtaux);
        getChildren().addAll(opbox, this.pointhint_aux);
    }

    private void hostplaypane(){
        // Hidden card is shown
        this.host_card_hidden.setImage(new Image(idx_2_path(String.valueOf(this.host_card_list.get(1)))));
        // pause_half_sec();
        this.log.add("> Host playing");
        while (this.host_point < 17){
            host_add_card();
        }

        if(this.host_point > 21){
            if (this.is_split){
                if (this.main_done && this.aux_done){
                    this.money += 2*this.deal;
                    update_dealmoney();
                    this.log.add(">.. User Win for Host Bust, with money left: "+Integer.toString(this.money));
                }
                else{
                    this.money += this.deal;
                    update_dealmoney();
                    this.log.add(">.. User Win for Host Bust, with money left: "+Integer.toString(this.money));
                }
                this.log.add(">> For Split, the final user money is: "+Integer.toString(this.money));
            }
            else{
                this.money += 2*this.deal;
                update_dealmoney();
                this.log.add(">> User win for Host Bust, with money left: "+Integer.toString(this.money));
            }
        }
        else{
            if (this.is_split){
                if (this.main_done){
                    if(this.user_point > this.host_point){
                        this.money += this.deal;
                        update_dealmoney();
                        this.log.add(">.. User win on MAIN Deck for User:Host="+Integer.toString(this.user_point)+":"+Integer.toString(this.host_point)+", with money left: "+Integer.toString(this.money));
                    }
                    else if(this.user_point == this.host_point){
                        this.money += (int) this.deal / 2;
                        update_dealmoney();
                        this.log.add(">.. Tie on MAIN Deck for same point, with money left: "+Integer.toString(this.money));
                    }
                    else{
                        // lose
                        this.log.add(">.. User lose on MAIN Deck for User:Host="+Integer.toString(this.user_point)+":"+Integer.toString(this.host_point)+", with money left: "+Integer.toString(this.money));
                    }
                }
                if (this.aux_done){
                    if(this.user_point_aux > this.host_point){
                        this.money += this.deal;
                        update_dealmoney();
                        this.log.add(">.. User win on AUX Deck for User:Host="+Integer.toString(this.user_point_aux)+":"+Integer.toString(this.host_point)+", with money left: "+Integer.toString(this.money));
                    }
                    else if(this.user_point_aux == this.host_point){
                        this.money += (int) this.deal / 2;
                        update_dealmoney();
                        this.log.add(">.. Tie on AUX Deck for same point, with money left: "+Integer.toString(this.money));
                    }
                    else{
                        // lose
                        this.log.add(">.. User lose on AUX Deck for User:Host="+Integer.toString(this.user_point_aux)+":"+Integer.toString(this.host_point)+", with money left: "+Integer.toString(this.money));
                    }
                }
                this.log.add(">> For Split, the final user money is: "+Integer.toString(this.money));
            }
            else{
                if(this.user_point > this.host_point){
                    this.money += 2*this.deal;
                    update_dealmoney();
                    this.log.add(">> User win for User:Host="+Integer.toString(this.user_point)+":"+Integer.toString(this.host_point)+", with money left: "+Integer.toString(this.money));
                }
                else if(this.user_point == this.host_point){
                    this.money += this.deal;
                    update_dealmoney();
                    this.log.add(">> Tie for same point, with money left: "+Integer.toString(this.money));
                }
                else{
                    // lose
                    this.log.add(">> User lose for User:Host="+Integer.toString(this.user_point)+":"+Integer.toString(this.host_point)+", with money left: "+Integer.toString(this.money));
                }
            }
        }
        quitpane();
    }

    private void quitpane(){
        
        if (this.insurance){
            this.insurance = false;
            getChildren().remove(getChildren().size()-2, getChildren().size());
        }

        int gain = this.money - this.past_money;
        Label gainlabel = new Label();
        gainlabel.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		gainlabel.setTextFill(Color.GOLD);
        Label resultlabel = new Label();
        resultlabel.setFont(Font.font("Agency FB",FontWeight.BOLD, 32));
		resultlabel.setTextFill(Color.BLACK);
        gainlabel.setLayoutX(350);
        gainlabel.setLayoutY(40);
        resultlabel.setLayoutX(160);
        resultlabel.setLayoutY(80);
        if (gain > 0){
            gainlabel.setText("+ "+String.valueOf(gain));
            resultlabel.setText(" You Win! ");
        }
        else if (gain == 0 || this.user_point == this.host_point){
            gainlabel.setText(" Hold on ");
            resultlabel.setText(" A Tie! ");
        }
        else{
            gainlabel.setText(" "+String.valueOf(gain));
            resultlabel.setText(" You Lose! ");
        }

        // Again Button
        Button againbt = new Button("Again");
        againbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		againbt.setTextFill(new Color(0,0,0,1));
		againbt.setLayoutX(180.0);
		againbt.setLayoutY(160.0);
		againbt.setOnMouseClicked(e->{
                if (this.money > 0){
                    this.log.add(">> User choose to play game again");
                    save_log();
                    init_params();
                    init_pane();
                }
                else{
                    this.money = 1000;
                    this.log.add(">> User choose to play game again");
                    this.log.add(">> HAVE TO Regive User 1000");
                    save_log();
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.titleProperty().set("Run out of money");
                    alert.headerTextProperty().set("In fact you have lose all you money... Anyway, give you 1000 again.");
                    alert.showAndWait();
                    init_params();
                    init_pane();
                }
            }
		);

		// Quit Button
        Button quitbt = new Button("Quit");
        quitbt.setFont(Font.font("Agency FB",FontWeight.BOLD, 16));
		quitbt.setTextFill(new Color(0,0,0,1));
		quitbt.setLayoutX(183.0);
		quitbt.setLayoutY(200.0);
		quitbt.setOnMouseClicked(e->{
                this.log.add(">> User choose to quit the game");
                save_log();
				this.primaryStage.close();
            }
        );
        
        getChildren().addAll(resultlabel, gainlabel, againbt, quitbt);
    }

    private void calc_point(int isuser){
        // Calculate point according to the current card list
        int tmp_point_except_aces = 0;
        int aces_number = 0;
        List tmp_list = new ArrayList<Integer>(); 
        switch (isuser){
            case 0:
                tmp_list = this.user_card_list;
                break;
            case 1:
                tmp_list = this.host_card_list;
                break;
            case 2:
                tmp_list = this.user_card_list_aux;
                break;
            default:
                tmp_list = this.user_card_list;
                break;
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
        switch (isuser){
            case 0:
                this.user_point = tmp_point;
                break;
            case 1:
                this.host_point = tmp_point;
                break;
            case 2:
                this.user_point_aux = tmp_point;
                break;
            default:
                this.user_point = tmp_point;
                break;
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

    private String idx_2_chinese(int idx){
        String card_CN = null;
        if (idx%13==1){
            card_CN = "A";
        }
        else if (idx%13==11){
            card_CN = "J";
        }
        else if (idx%13==12){
            card_CN = "Q";
        }
        else if (idx%13==0){
            card_CN = "K";
        }
        else{
            card_CN = Integer.toString(idx%13);
        }
        if ((idx-1)/13==0){
            card_CN = "Spade"+"["+card_CN+"]";
        }
        else if ((idx-1)/13==1){
            card_CN = "Hear"+"["+card_CN+"]";
        }
        else if ((idx-1)/13==1){
            card_CN = "Diamond"+"["+card_CN+"]";
        }
        else{
            card_CN = "Club"+"["+card_CN+"]";
        }
        return card_CN;
    }

    private void update_dealmoney(){
        this.dealhint.setText("Bet: "+Integer.toString(this.deal));
        this.moneyhint.setText("Money: "+Integer.toString(this.money));
    }

    private void pause_half_sec(){
        try{
            Thread.sleep(500);
        }
        catch(Exception e){
            System.exit(0);
        }
    }
    private void save_log(){
        try{
            File file = new File(this.filePath);
            FileOutputStream fos;
            if(!file.exists()){
                file.createNewFile();
                fos = new FileOutputStream(file);
            }
            else{
                fos = new FileOutputStream(file, true);
            }
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            for(Object current_line: this.log){
                osw.write(current_line+"\r\n");
            }
            osw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}