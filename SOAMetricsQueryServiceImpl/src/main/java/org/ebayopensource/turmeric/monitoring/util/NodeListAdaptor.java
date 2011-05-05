/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.util;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class NodeListAdaptor.
 */
final public class NodeListAdaptor implements NodeList {

	/** The m_node list. */
	ArrayList<Node> m_nodeList;
	
	/**
	 * Instantiates a new node list adaptor.
	 */
	public NodeListAdaptor() {
		m_nodeList = new ArrayList<Node>();
	}

	/**
	 * Adds the.
	 *
	 * @param node the node
	 */
	public void add(Node node) {
		m_nodeList.add(node);
	}

	/* (non-Javadoc)
	 * @see org.w3c.dom.NodeList#getLength()
	 */
	@Override
	public int getLength() {
		return m_nodeList.size();
	}

	/* (non-Javadoc)
	 * @see org.w3c.dom.NodeList#item(int)
	 */
	@Override
	public Node item(int i) {
		return m_nodeList.get(i);
	}
}
