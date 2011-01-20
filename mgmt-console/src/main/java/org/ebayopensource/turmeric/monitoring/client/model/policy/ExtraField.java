/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client.model.policy;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * ExtraField
 * 
 */
public class ExtraField extends FocusWidget {

	private String labelName;
	private int order;
	private String fieldType;
	private String lenghtBox;
	private String value;

	private Label labelBox;
	
	private TextBox textBox;
	private ListBox listBox;
	private CheckBox checkBox;
	private TextArea textArea;

	public String getLenghtBox() {
		return lenghtBox;
	}

	public void setLenghtBox(String lenghtBox) {
		this.lenghtBox = lenghtBox;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public TextBox getTextBox() {
		return textBox;
	}

	public void setTextBox(TextBox textBox) {
		this.textBox = textBox;
	}

	public ListBox getListBox() {
		return listBox;
	}

	public void setListBox(ListBox listBox) {
		this.listBox = listBox;
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setLabelBox(Label labelBox) {
		this.labelBox = labelBox;
	}

	public Label getLabelBox() {
		return labelBox;
	}

	public void setTextArea(TextArea textArea) {
		this.textArea = textArea;
	}

	public TextArea getTextArea() {
		return textArea;
	}

}
