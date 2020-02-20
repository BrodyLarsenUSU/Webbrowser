package sample;

import javafx.animation.RotateTransition;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class Browser {

    public String favoritesDoc;
    public ArrayList<String> favorites = new ArrayList<>();
    public ArrayList<sample.Page> pages = new ArrayList<>();
    public String defaultPage;
    public int curPageIndex;

    Browser(String homePage, String favoritesDoc) {
        sample.Page firstPage = new sample.Page(homePage);
        this.favoritesDoc = favoritesDoc;
        this.favorites = getFavorites();
        this.pages.add(firstPage);
        this.defaultPage = homePage;
        this.curPageIndex = 0;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }

    public void display(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Welcome to the Internet! It only gets worse from here");

        //vbox for entire browser
        VBox basePane = new VBox();
        Scene scene = new Scene(basePane, 1050, 625);

        //Address Bar
        TextField addressBar = new TextField(this.defaultPage);
        addressBar.minWidthProperty().bind(scene.widthProperty().add(-390));

        //buttons
        Button Home = new Button("Home");
        Button back = new Button("BACK");
        Button forward = new Button("FORWARD");
        Button Refresh = new Button("Refresh");
        Button favoriteButton = new Button("Add Favorite");

        //HomePage button logic going back to our cs.usu.edu homepage
        Home.setOnMouseClicked(e -> {
            this.pages.get(curPageIndex).setPage(this.defaultPage);
            //setting the addressbar when you click the home button
            addressBar.setText(this.pages.get(curPageIndex).getWebsite());
        });

        //addressar logic
        addressBar.setOnKeyPressed(e -> {
            //do logic when enter is typed
            if (e.getCode().equals(KeyCode.ENTER)) {
                if (addressBar.getText().contains(".")) {
                    if (addressBar.getText().startsWith("https://")) {
                        this.pages.get(this.curPageIndex).setPage(addressBar.getText());

                    } else if (addressBar.getText().startsWith("http://")) {
                        this.pages.get(this.curPageIndex).setPage(addressBar.getText());
                    } else {
                        this.pages.get(this.curPageIndex).setPage("https://" + addressBar.getText());
                        //setting the addressbar to have the real url so my favorites function works
                        addressBar.setText(this.pages.get(curPageIndex).getWebsite());
                    }
                }
                //yo dog this isnt a URL so do a google search on it
                else {
                    this.pages.get(this.curPageIndex).setPage("https://google.com/search?q=" + addressBar.getText());
                }
            }
        });


        //Refresh logic
        Refresh.setOnMouseClicked(e -> {
            this.pages.get(curPageIndex).reload();
            RotateTransition rotate = new RotateTransition(Duration.millis(1000), Refresh);
            rotate.setByAngle(360);
            rotate.setCycleCount(1);
            rotate.play();
        });
//

        //Backbutton logic
        back.setOnMouseClicked(e -> this.pages.get(curPageIndex).goBack());

        //forward logic
        forward.setOnMouseClicked(e -> this.pages.get(curPageIndex).goForward());

        //Favorite logic to add url to a document and it also appends it to the favorites list
        Menu favoriteMenu = new Menu("Favorites");

        favoriteButton.setOnMouseClicked(e -> {
            //if addressbar is empty dont add nothing to the favorites menu
            String urlToAdd = addressBar.getText();
            //if the searchbar is empty dont do any favorite logic
            if (urlToAdd.equals("")) {
            }
            //it will add the menu item to the menu and create its event so when you click it, it goes to the page, then it writes that URL to a file
            else {
                favorites.add(urlToAdd);
                try {
                    FileWriter fw = new FileWriter(favoritesDoc, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    MenuItem item = new MenuItem(urlToAdd);
                    //creating event for items in menu
                    item.setOnAction(ev -> {
                        addressBar.setText(item.getText());
                        //same logic as 26 lines down... making sure that the items that were read in form the file need to be googled or not
                        if (!item.getText().contains(".")) {
                            pages.get(curPageIndex).setPage("https://google.com/search?q=" + addressBar.getText());
                        } else {
                            pages.get(curPageIndex).setPage(addressBar.getText());
                        }
                    });
                    //adding menuItem to the menu
                    favoriteMenu.getItems().add(item);
                    //appending the url to the file
                    bw.append("\n");
                    bw.append(urlToAdd);
                    bw.close();
                } catch (Exception except) {
                    System.out.println("file not found");
                }
            }
        });


        //favorites menu logic of previously favorited pages
        for (String url : favorites) {
            MenuItem item = new MenuItem(url);
            //event to go to load the page
            item.setOnAction(ev -> {
                addressBar.setText(item.getText());
                //if the menuitem doesnt have a period it needs to be googled else just use the url
                if (!item.getText().contains(".")) {
                    pages.get(curPageIndex).setPage("https://google.com/search?q=" + addressBar.getText());
                } else {
                    pages.get(curPageIndex).setPage(addressBar.getText());
                }
            });
            //adding item to favorites menu
            favoriteMenu.getItems().add(item);
        }
        MenuBar favortiteMenuTab = new MenuBar();

        //add menu to menuBar
        favortiteMenuTab.getMenus().add(favoriteMenu);
        Pane favoritePane = new Pane();
        favoritePane.getChildren().add(favortiteMenuTab);


        //HBox2 is for the address bar etc... the tabs HBox will be above this one
        HBox Hbox2 = new HBox(back);
        Hbox2.getChildren().add(forward);
        Hbox2.getChildren().add(Refresh);
        Hbox2.getChildren().add(Home);
        Hbox2.getChildren().add(addressBar);
        Hbox2.getChildren().add(favoriteButton);
        Hbox2.getChildren().add(favoritePane);


        HBox tabs = new HBox();
        buttonMethod(this.pages.size(), tabs, addressBar);

        HBox tabsAndPlus = new HBox();
        tabsAndPlus.getChildren().add(tabs);
        basePane.getChildren().add(tabsAndPlus);

        //this pane is for the tabs that are being created
        Pane pane = new Pane();

        //plus button
        Button plus = new Button("+");
        plus.setOnMouseClicked(e -> {
            Page newPage = new Page(this.defaultPage);
            //binding new pages to the width of the scene
            newPage.page.minWidthProperty().bind(scene.widthProperty());
            newPage.page.minHeightProperty().bind(scene.heightProperty());
            //listener so when we go to a new location on the  webview object it pulls the new URL to our addressbar
            newPage.page.getEngine().locationProperty().addListener(ov -> {
                addressBar.setText(newPage.getUrl());
            });
            //putting the tabs on the pane
            this.pages.add(newPage);
            tabs.getChildren().add(newPage.tab);
            this.buttonMethod(pages.size(), pane, addressBar);
        });
        tabsAndPlus.getChildren().add(plus);

        //tab magic dont try to understand this code unless you talk to the developers Brody and Johnathan :D
        Page newPage = new Page(this.defaultPage);
        this.pages.add(newPage);
        tabs.getChildren().add(newPage.tab);
        this.buttonMethod(pages.size(), pane, addressBar);
        pane.getChildren().setAll(new Button());
        pane.getChildren().add(this.pages.get(pages.size() - 1).page);
        this.curPageIndex = pages.size() - 1;


        // adds search bar back and forward
        basePane.getChildren().add(Hbox2);

        //adding tab pane
        basePane.getChildren().add(pane);


        //binding to set the first page to the width of the scene and height
        pages.get(curPageIndex).page.minWidthProperty().bind(scene.widthProperty());
        pages.get(curPageIndex).page.minHeightProperty().bind(scene.heightProperty());

        //creating listener for first page so the addressbar correlates to what you click in the page
        int copyIndex = curPageIndex;
        pages.get(copyIndex).getWebEngine().locationProperty().addListener(ov -> {
            addressBar.setText(pages.get(copyIndex).getUrl());
        });

        //YOU REACHED THE END THIS IS SHOWING OUR GUI
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    //ps this is the button method for adding tabs to a pane
    public void buttonMethod(int i, Pane pageBox, TextField addressBar) {

        Button cur = pages.get(i - 1).tab;
        //added to fix bug when you didnt click on a tab after clicking the plus button
        this.curPageIndex = i - 1;
        cur.setOnMouseClicked(e -> {
            pageBox.getChildren().setAll(new Button());
            pageBox.getChildren().add(this.pages.get(i - 1).page);
            //added this so the addressbar changes to the website your at when you switch tabs, this helps my favorites button
            addressBar.setText(pages.get(i - 1).getWebsite());
            this.curPageIndex = i - 1;
        });
    }
}
