/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * ErrorResponse The result of an invocation to the SOAMetricsQueryService that represents an invocation error.
 */
public class ErrorResponse extends JavaScriptObject {

    /**
     * Instantiates a new error response.
     */
    protected ErrorResponse() {
    }

    /**
     * From json.
     *
     * @param json the json
     * @return the error response
     */
    public static final native ErrorResponse fromJSON(String json) /*-{
		try {
			return eval('(' + json + ')');
		} catch (err) {
			return null;
		}
    }-*/;

    /**
     * Checks if is service error response.
     *
     * @param rname the rname
     * @return true, if is service error response
     */
    public final native boolean isServiceErrorResponse(String rname) /*-{
		if (this[rname]["ms.errorMessage"])
			return true;
		return false;
    }-*/;

    /**
     * Gets the service errors.
     *
     * @param rname the rname
     * @return the service errors
     */
    public final native JsArray<RemoteError> getServiceErrors(String rname) /*-{
		if (this[rname]["ms.errorMessage"])
			return this[rname]["ms.errorMessage"]["ms.error"];

    }-*/;

    /**
     * Checks if is error response.
     *
     * @return true, if is error response
     */
    public final native boolean isErrorResponse() /*-{
		if (this["ms.error"])
			if (this["ms.error"]["ms.errorMessage"])
				return true;
			else
				return false;
		else
			return false;
    }-*/;

    /**
     * Gets the errors.
     *
     * @return the errors
     */
    public final native JsArray<RemoteError> getErrors() /*-{
		if (this["ms.error"])
			if (this["ms.error"]["ms.errorMessage"])
				return this["ms.error"]["ms.errorMessage"]["error"];
			else
				return null;
		else
			return null;

    }-*/;

}
