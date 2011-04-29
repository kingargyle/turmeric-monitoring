/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.visualizations.LineChart;

/**
 * The Class SummaryPanel.
 */
public class SummaryPanel extends Composite {
    
    private HorizontalPanel panel;
    private ScrollPanel scroller;
    private Label headingLabel;
    private Image downloadImg;
    private String downloadUrl;
    private Image infoImg;
    private String info;
    private Image downloadImgLight;
    private Image infoImgLight;
    private Grid contentGrid;
    
    
    /**
     * Instantiates a new summary panel.
     */
    public SummaryPanel () {
        //the panel where the table and graphs go
        contentGrid = new Grid(1,2);
        contentGrid.setWidth("100%");
        contentGrid.setHeight("100%");
        contentGrid.setCellPadding(0);
        contentGrid.setCellSpacing(0);
        
        //this is a grid to place the buttons on top of the data table
        Grid verticalGrid = new Grid(2,1);
        verticalGrid.setWidth("100%");
        verticalGrid.setHeight("60%");
        verticalGrid.setCellPadding(0);
        verticalGrid.setCellSpacing(0);
        verticalGrid.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
        
        contentGrid.setWidget(0, 0, verticalGrid);
        
        panel = new HorizontalPanel();
        panel.addStyleName("summary-panel-item"); 
        
        Grid headingGrid = new Grid(1, 2);
        
        headingLabel = new Label("");
        headingGrid.setWidget(0, 0, headingLabel);
        headingGrid.setWidth("100%");

        Grid buttonGrid = new Grid(1,2);
        headingGrid.setWidget(0,1, buttonGrid);
        headingGrid.getCellFormatter().setHorizontalAlignment(0,1, HasHorizontalAlignment.ALIGN_RIGHT);
        infoImg = new Image();
        infoImg.setUrl("images/info.png");
        
        infoImgLight = new Image();
        infoImgLight.setUrl("images/info-light.png");
        
        PushButton ib = new PushButton(infoImg, infoImgLight);
        ib.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (info != null) {
                    InfoDialog dialog = new InfoDialog(false);
                    dialog.setMessage(info);
                    int x = infoImg.getAbsoluteLeft();
                    int y = infoImg.getAbsoluteTop() + infoImg.getOffsetHeight();
                    dialog.getDialog().setAutoHideEnabled(true);
                    dialog.getDialog().setPopupPosition(x, y);
                    dialog.getDialog().show();
                }
            }
        });
        
        
        downloadImg = new Image();
        downloadImg.setUrl("images/dwnld.png");
        downloadImg.addStyleName("dwnld");
        
        downloadImgLight = new Image();
        downloadImgLight.setUrl("images/dwnld-light.png");
        
        PushButton db = new PushButton(downloadImg, downloadImgLight);
        db.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent arg0) {
                final Element downloadIframe = RootPanel.get("__download").getElement(); 
                if (downloadIframe == null)
                   Window.open(downloadUrl,"_blank", "");
                else
                    DOM.setElementAttribute(downloadIframe, "src", downloadUrl); 
            }
        });
       
        buttonGrid.setWidget(0,0,ib);
        buttonGrid.setWidget(0,1,db);
       
        //panel.add(headingGrid);
        panel.setWidth("99%");
        scroller = new ScrollPanel();
        scroller.addStyleName("summary-scroll");
        scroller.add(contentGrid);
        scroller.setSize("100%", "100%");
        panel.add(scroller);
        initWidget(panel);
        setDataTableButtonGrid(headingGrid);
    }
    
    private void setDataTableButtonGrid(Grid buttonGrid){
        ((Grid)contentGrid.getWidget(0, 0)).setWidget(0, 0, buttonGrid);
    }

    /**
     * Sets the heading.
     *
     * @param text the new heading
     */
    public void setHeading (String text) {
        headingLabel.setText(text);
    }
    
    /**
     * Sets the contents.
     *
     * @param widget the new contents
     */
    public void setContents (Widget widget) {
        ((Grid)contentGrid.getWidget(0, 0)).setWidget(1, 0, widget);
    }
    
    /**
     * Gets the info button.
     *
     * @return the info button
     */
    public HasClickHandlers getInfoButton () {
        return infoImg;
    }
    
    /**
     * Sets the info.
     *
     * @param info the new info
     */
    public void setInfo (String info) {
        this.info = info;
    }
    
    /**
     * Sets the content container width.
     *
     * @param width the new content container width
     */
    public void setContentContainerWidth (String width) {
        scroller.setWidth(width);
    }
    
    /**
     * Sets the content container height.
     *
     * @param height the new content container height
     */
    public void setContentContainerHeight (String height) {
        scroller.setHeight(height);
    }
    
    /**
     * Gets the content container.
     *
     * @return the content container
     */
    public Panel getContentContainer () {
        return scroller;
    }
    
    /**
     * Sets the download url.
     *
     * @param url the new download url
     */
    public void setDownloadUrl (String url) {
        downloadUrl = url;
    }
    
    /**
     * Adds the chart.
     *
     * @param lineChart the line chart
     */
    public void addChart(LineChart lineChart){
        scroller.setHeight("260px");
        contentGrid.setWidget(0, 1, lineChart);
    }
    
    /**
     * Gets the chart.
     *
     * @return the chart
     */
    public LineChart getChart(){
        return (LineChart) contentGrid.getWidget(0, 1);
    }
    
}
