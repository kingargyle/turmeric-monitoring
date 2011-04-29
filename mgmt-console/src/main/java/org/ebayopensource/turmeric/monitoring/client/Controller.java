/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import org.ebayopensource.turmeric.monitoring.client.model.HistoryToken;
import org.ebayopensource.turmeric.monitoring.client.presenter.Presenter;

/**
 * The Interface Controller.
 */
public interface Controller {

    /**
     * Adds the presenter.
     *
     * @param id the id
     * @param p the p
     */
    public void addPresenter(String id, Presenter p);
    
    /**
     * Gets the presenter.
     *
     * @param id the id
     * @return the presenter
     */
    public Presenter getPresenter(String id);
    
    /**
     * Select presenter.
     *
     * @param token the token
     */
    public void selectPresenter(HistoryToken token);
}
