/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateFormatUtil {
	
	private static final String CONSOLE_DATE_FORMAT = "dd MMM yyyy hh:mm:ss aa";
	private static final DateTimeFormat CONSOLE_DATE_FORMATTER = DateTimeFormat.getFormat(CONSOLE_DATE_FORMAT);
	
	public static String toConsoleDateFormat(Date date) {
		if (date == null) {
			return "";
		}
		return CONSOLE_DATE_FORMATTER.format(date);
	}
}
