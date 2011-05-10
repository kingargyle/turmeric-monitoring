package org.ebayopensource.turmeric.monitoring.util;

import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

/**
 * This class implements the DifferenceListener interface.  It checks a text node,
 * and looks at the parent node.  If that is a machineName node, it ignores the
 * difference.
 * 
 * This is needed for the unit tests because the test data can be loaded differently.
 * 
 */
public class IgnoreMachineName implements DifferenceListener {

	/* (non-Javadoc)
	 * @see org.custommonkey.xmlunit.DifferenceListener#differenceFound(org.custommonkey.xmlunit.Difference)
	 */
	@Override
	public int differenceFound(Difference diff) {
		Node node = diff.getTestNodeDetail().getNode();
		if (node.getNodeType() == Node.TEXT_NODE) {
			Node parent = node.getParentNode();
			if ("machineName".equals(parent.getLocalName())) {
				return RETURN_IGNORE_DIFFERENCE_NODES_SIMILAR;
			}
			
		}
		return RETURN_ACCEPT_DIFFERENCE;
	}

	/* (non-Javadoc)
	 * @see org.custommonkey.xmlunit.DifferenceListener#skippedComparison(org.w3c.dom.Node, org.w3c.dom.Node)
	 */
	@Override
	public void skippedComparison(Node arg0, Node arg1) {
		
	}

}
