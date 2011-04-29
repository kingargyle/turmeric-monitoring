/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class SelectionContext.
 */
public class SelectionContext {

    /** The selected objects. */
    Map<ObjectType, String> selectedObjects = new HashMap<ObjectType, String>();


    /**
     * From history token.
     *
     * @param token the token
     * @return the selection context
     */
    public static SelectionContext fromHistoryToken (HistoryToken token) {
        SelectionContext ctx = new SelectionContext();
        String tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_SERVICE_TOKEN);
        if (tmp != null)
            ctx.select(ObjectType.ServiceName, tmp);
        tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_OPERATION_TOKEN);
        if (tmp != null)
            ctx.select(ObjectType.OperationName, tmp);
        tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_CONSUMER_TOKEN);
        if (tmp != null)
            ctx.select(ObjectType.ConsumerName, tmp);
        tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_ERROR_NAME_TOKEN);
        if (tmp != null)
            ctx.select(ObjectType.ErrorName, tmp);
        tmp = HistoryToken.getValue(token, HistoryToken.SELECTED_ERROR_ID_TOKEN);
        if (tmp != null)
            ctx.select(ObjectType.ErrorId, tmp);
        return ctx;
    }


    /**
     * Select all.
     *
     * @param sc the sc
     */
    public void selectAll (SelectionContext sc) {
        if (sc == null)
            return;
        for (Map.Entry<ObjectType, String> entry:sc.getSelections().entrySet()) {
            select(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Equals.
     *
     * @param sc the sc
     * @return true, if successful
     */
    public boolean equals (SelectionContext sc) {
        if (sc.getSelections().equals(selectedObjects))
            return true;

        return false;
    }
    
    /**
     * Select.
     *
     * @param t the t
     * @param name the name
     */
    public void select(ObjectType t, String name) {
        selectedObjects.put(t, name);
    }

    /**
     * Unselect.
     *
     * @param t the t
     */
    public void unselect(ObjectType t) {
        selectedObjects.remove(t);
    }

    /**
     * Gets the selection.
     *
     * @param t the t
     * @return the selection
     */
    public String getSelection(ObjectType t) {
        return selectedObjects.get(t);
    }
    
    /**
     * Checks if is selected.
     *
     * @param t the t
     * @return true, if is selected
     */
    public boolean isSelected(ObjectType t) {
        return (selectedObjects.get(t)!=null);
    }
    
    /**
     * Gets the selections.
     *
     * @return the selections
     */
    public Map<ObjectType,String> getSelections () {
        return new HashMap<ObjectType,String>(selectedObjects);
    }

    /**
     * To history token.
     *
     * @param presenterId the presenter id
     * @param values the values
     * @return the history token
     */
    public HistoryToken toHistoryToken (String presenterId, Map<String,String> values) {
        HistoryToken token = HistoryToken.newHistoryToken(presenterId, values);
        appendSelections(token);
        return token;
    }

    /**
     * Append to history token.
     *
     * @param token the token
     * @return the history token
     */
    public HistoryToken appendToHistoryToken (HistoryToken token) {
        if (token == null)
            return token;
        appendSelections(token);
        return token;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString () {
        StringBuffer strbuff = new StringBuffer();
        for (Map.Entry<ObjectType,String> entry:selectedObjects.entrySet()) {
            strbuff.append(" "+entry.getKey().toString()+"="+entry.getValue()+" ");
        }
        return strbuff.toString();
    }

    /**
     * Append selections.
     *
     * @param token the token
     */
    protected void appendSelections (HistoryToken token) {
        for (ObjectType t:selectedObjects.keySet()) {
            switch (t) {
                case ServiceName: {
                    if (selectedObjects.get(t) != null)
                        token.addValue(HistoryToken.SELECTED_SERVICE_TOKEN, selectedObjects.get(t));
                    break;
                }
                case OperationName: {
                    if (selectedObjects.get(t) != null)
                        token.addValue(HistoryToken.SELECTED_OPERATION_TOKEN, selectedObjects.get(t));
                    break;
                }
                case ConsumerName: {
                    if (selectedObjects.get(t) != null)
                        token.addValue(HistoryToken.SELECTED_CONSUMER_TOKEN, selectedObjects.get(t));
                    break;
                }
                case ErrorName: {
                    if (selectedObjects.get(t) != null)
                        token.addValue(HistoryToken.SELECTED_ERROR_NAME_TOKEN, selectedObjects.get(t));
                    break;
                }
                case ErrorId: {
                    if (selectedObjects.get(t) != null)
                        token.addValue(HistoryToken.SELECTED_ERROR_ID_TOKEN, selectedObjects.get(t));
                    break;
                }
            }
        }
    }
}
