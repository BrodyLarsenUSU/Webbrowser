package sample;


import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.ArrayList;

public class Page {
    public WebView page = new WebView();
    public Button tab = new Button();


    Page(String url){
        this.page.getEngine().load(url);
        this.tab.setText(getText());

        //this listener changes the tabs text property whenever the url changes
        page.getEngine().locationProperty().addListener(ov ->{
            this.tab.setText(getText());
        });
    }

    /**
     * various getters for accessing data on the page
     *
     * website, webengine, url, and a text representation of the url
     *
     */
    public String getWebsite(){
       return this.page.getEngine().getLocation();
    }

    public WebEngine getWebEngine(){
        return this.page.getEngine();
    }
    public String getUrl(){
        return page.getEngine().getLocation();
    }
    public String getText(){
        String text = page.getEngine().getLocation();

        text = text.replace("https:", "");
        text = text.replace("/", "");
        text = text.replace("http:", "");

        //if the text is longer than 12, concatenates and adds elipses
        if(text.length() > 12){
            text = text.substring(0, 11) + "...";
        }
        return text;
    }

    //called to go to a new page
    public void setPage(String url) {
        this.page.getEngine().load(url);

    }

    //called to reload a page
    public void reload(){
        this.page.getEngine().reload();
        this.tab.setText(getText());
    }

    //called to go back
    public void goBack(){
        page.getEngine().getHistory().go(-1);
    }

    //called to go forward
    public void goForward() {
        page.getEngine().getHistory().go(1);
    }
}
