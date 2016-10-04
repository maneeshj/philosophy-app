package com.project.ui;


import javax.servlet.annotation.WebServlet;

import com.project.app.PathHelper;
import com.project.app.PathToWiki;
import com.project.app.WebCrawler;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Sizeable;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();
        
        final TextField wikiUrl = new TextField();
        wikiUrl.setCaption("Enter Wikipedia URL or TITLE here:");
        wikiUrl.setWidth(40, Sizeable.Unit.PERCENTAGE);

        Button button = new Button("Submit");
        button.addClickListener( e -> {
            // Parsing
        	String value = wikiUrl.getValue();
        	String url = null;
        	if(!value.contains("http")){
        		url = "https://en.wikipedia.org/wiki/" + value;
        	}
        	else{
        		url = value;
        	}        	
        	PathToWiki path = GetPathToPhilosophy(url);
        	String pathString = GetPathString(path);
        	int hopCount = path.GetHopCount();
            
        	layout.addComponent(new Label(url.substring(url.lastIndexOf("/") + 1) + ":  Path " + pathString, ContentMode.HTML));
            layout.addComponent(new Label("Number of hops: " + hopCount));
        });
        
        layout.addComponents(wikiUrl, button);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(layout);
    }
    
    private PathToWiki GetPathToPhilosophy(String url){
    	WebCrawler crawler = new WebCrawler();
    	return crawler.GetPathToPhilosophy(url);
    }
    
    private String GetPathString(PathToWiki path){
    	return PathHelper.GetPathString(path);
    } 
    
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
