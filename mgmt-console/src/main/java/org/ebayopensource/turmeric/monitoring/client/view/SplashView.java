/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view;

import org.ebayopensource.turmeric.monitoring.client.ConsoleUtil;
import org.ebayopensource.turmeric.monitoring.client.presenter.SplashPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * SplashView
 *
 * Display the splash page.
 * 
 */
public class SplashView extends Composite implements SplashPresenter.Display {
	
	protected Button submitButton;
	protected Label loginLabel;
	protected Label passwordLabel;
	protected Label domainLabel;
	protected TextBox login;
	protected TextBox password;
	protected TextBox domain;
	protected Image logo;
	protected VerticalPanel panel;
	protected String id;
	protected static String lang;
	protected Label displayMessage;
	
	public SplashView ()
	{
		panel = new VerticalPanel();
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		panel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		panel.setWidth("100%");
		initWidget(panel);
		
		FlowPanel languagePanel = new FlowPanel();
		languagePanel.setWidth("100%");
		languagePanel.addStyleName("lang-panel");
		panel.add(languagePanel);
		 
		Anchor it = new Anchor();
		it.setText(ConsoleUtil.constants.italian());
		
		//TODO this doesn't work when refactored into a reusable method!
	    it.addClickHandler(new ClickHandler() {
	            public native void onClick(ClickEvent event) 
	            /*-{ 
	                var l = @org.ebayopensource.turmeric.monitoring.client.view.SplashView::lang;
	                var url = "";
	                var currLocation = $wnd.location.toString().split("?"); 
	                if (currLocation.length > 1) {
	                    var params = currLocation[1].toString().split("&");
	                    var done = false;
	                    for (var i=0;i<params.length;i++) {
	                        if (params[i].indexOf("locale") == 0) {
	                        params[i] = "locale=it";
	                        done = true;
	                        }
	                    }
	                    if (!done) {
	                        params.splice(0,0, "locale=it");
	                    }
	                    url = currLocation[0]+"?"+params.join("&");
	                } else {
	                    var anchors = currLocation[0].toString().split("#");
	                    if (anchors.length > 1) {
	                        url = anchors[0] +"?"+ "locale=it#" + anchors[1];
	                    } else {
	                        url = currLocation[0]+"?"+ "locale=it";
	                    }
	                }

	             $wnd.location.href = url; 
	             $wnd.location.replace(url); 
	             }-*/;
	        });

		Anchor en = new Anchor(ConsoleUtil.constants.english());		
	    en.addClickHandler(new ClickHandler() {
              public native void onClick(ClickEvent event) 
              /*-{ 
                  var l = @org.ebayopensource.turmeric.monitoring.client.view.SplashView::lang;
                  var url = "";
                  var currLocation = $wnd.location.toString().split("?"); 
                  if (currLocation.length > 1) {
                      var params = currLocation[1].toString().split("&");
                      var done = false;
                      for (var i=0;i<params.length;i++) {
                          if (params[i].indexOf("locale") == 0) {
                          params[i] = "locale=en";
                          done = true;
                          }
                      }
                      if (!done) {
                          params.splice(0,0, "locale=en");
                      }
                      url = currLocation[0]+"?"+params.join("&");
                  } else {
                      var anchors = currLocation[0].toString().split("#");
                      if (anchors.length > 1) {
                          url = anchors[0] +"?"+ "locale=en#" + anchors[1];
                      } else {
                          url = currLocation[0]+"?"+ "locale=en";
                      }
                  }
                  
               $wnd.location.href = url; 
               $wnd.location.replace(url); 
               }-*/;
          });
		
	    Anchor fr = new Anchor(ConsoleUtil.constants.french());
	    fr.setEnabled(false);
	    fr.addStyleName("hidden");
	   
		languagePanel.add(it);
		languagePanel.add(fr);
		languagePanel.add(en);
		
		
		this.logo = new Image("images/turmeric.png");
		this.logo.addStyleDependentName("turmeric");
		this.logo.addStyleDependentName("splash");
		
		this.submitButton = new Button(ConsoleUtil.constants.enter());
		this.loginLabel = new Label(ConsoleUtil.constants.login());
		this.login = new TextBox();
		this.passwordLabel = new Label(ConsoleUtil.constants.password());
		this.password = new PasswordTextBox();
		this.domainLabel = new Label(ConsoleUtil.constants.domain());
		this.domain = new TextBox();
		panel.add(logo);
		
		Grid grid = new Grid(4,2);
		grid.addStyleName("splash");

//		TODO all login related behaviour is commented out for R1 until
//		proper consideration in R2 of PolicyService behaviour.

		grid.setWidget(0,0,this.loginLabel);
		grid.setWidget(0,1,this.login);
		grid.setWidget(1,0,this.passwordLabel);
		grid.setWidget(1,1,this.password);
		grid.setWidget(2,0,this.domainLabel);
		grid.setWidget(2,1,this.domain);

		grid.setWidget(3,1,this.submitButton);
		grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		panel.add(grid);
		displayMessage = new Label();
		panel.add(displayMessage);
	}

	public void activate () {
		this.login.setValue("");
		this.password.setValue("");
		setWidth("100%");
	}
	
	public Widget asWidget() {
		return this;
	}

	public HasClickHandlers getSubmitButton() {
		return this.submitButton;
	}
	
	public HasValue<String> getLogin() {
		return this.login;
	}
	
	public HasValue<String> getPassword() {
		return this.password;
	}
	
	public HasValue<String> getDomain() {
	    return this.domain;
	}

	public String getAssociatedId() {
		return this.id;
	}

	public void setAssociatedId(String id) {
		this.id = id;
	}

	protected void configureLanguageButton (Button button) {
        button.addClickHandler(new ClickHandler() {
            public native void onClick(ClickEvent event) 
            /*-{ 
                var l = @org.ebayopensource.turmeric.monitoring.client.view.SplashView::lang;
                alert ("value of static field = "+l);
                var url = "";
                var currLocation = $wnd.location.toString().split("?"); 
                if (currLocation.length > 1) {
                    var params = currLocation[1].toString().split("&");
                    var done = false;
                    for (var i=0;i<params.length;i++) {
                        if (params[i].indexOf("locale") == 0) {
                        params[i] = "locale="+l;
                        done = true;
                        }
                    }
                    if (!done) {
                        params.splice(0,0, "locale="+l);
                    }
                    url = currLocation[0]+"?"+params.join("&");
                } else {
                    url = currLocation[0]+"?"+ "locale="+l;
                }
                
             $wnd.location.href = url; 
             $wnd.location.replace(url); 
             }-*/;
        });
	}

	public void promptMessage(String message) {
		displayMessage.setText(message);
		displayMessage.setVisible(true);
	}
}
