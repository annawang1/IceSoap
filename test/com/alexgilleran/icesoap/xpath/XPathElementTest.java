package com.alexgilleran.icesoap.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.alexgilleran.icesoap.exception.XPathParsingException;
import com.alexgilleran.icesoap.xpath.elements.SingleSlashXPElement;
import com.alexgilleran.icesoap.xpath.elements.XPathElement;

public class XPathElementTest extends XPathTest {

	@Test
	public void testMatchesAgainstSelf() throws XPathParsingException {
		for (String testXPath : getTestXPaths()) {
			testAgainstSelf(testXPath);
		}
	}

	@Test
	public void testDoesntMatchBasic() throws XPathParsingException {
		String[] unmatchableXPaths = { "/this/wont/match/anything",
				"/an/@attribute" };

		for (String unmatchablePath : unmatchableXPaths) {
			for (String testXPath : getTestXPaths()) {
				assertFalse(matchStrings(unmatchablePath, testXPath));
			}
		}
	}

	@Test
	public void testSingleSlashMatches() throws XPathParsingException {
		assertTrue(matchStrings("/xpath", "/xpath"));
		assertTrue(matchStrings("/xpath/xpath2", "/xpath/xpath2"));
		assertTrue(matchStrings("/xpath/xpath2",
				"/xpath/xpath2[@predicate=\"true\"]"));
		assertTrue(matchStrings("/xpath/xpath2",
				"/xpath[@predicate1=\"true1\"]/xpath2[@predicate2=\"true2\"]"));
		assertTrue(matchStrings("/xpath[@predicate1=\"true1\"]/xpath2",
				"/xpath[@predicate1=\"true1\"]/xpath2[@predicate2=\"true2\"]"));

		// Build up a multi-predicate xpath and check that it matches a
		// simpler one - do this programmatically seeing as parsing of multiple
		// predicate xpaths isn't yet supported, but can happen in the parser
		XPathElement complexXPath1 = new SingleSlashXPElement("xpath1", null);
		complexXPath1.addPredicate("pred1", "value1");
		complexXPath1.addPredicate("pred2", "value2");
		XPathElement complexXPath2 = new SingleSlashXPElement("xpath2",
				complexXPath1);
		complexXPath2.addPredicate("pred3", "value3");
		complexXPath2.addPredicate("pred4", "value4");
		XPathElement complexXPath3 = new SingleSlashXPElement("xpath3",
				complexXPath2);
		complexXPath3.addPredicate("pred5", "value5");
		complexXPath3.addPredicate("pred6", "value6");

		// Check that it looks the way that it's supposed to
		assertEquals(
				"/xpath1[@pred1=\"value1\" and @pred2=\"value2\"]/xpath2[@pred3=\"value3\" and @pred4=\"value4\"]/xpath3[@pred5=\"value5\" and @pred6=\"value6\"]",
				complexXPath3.toString());

		XPathElement xpathToMatch = XPathFactory.getInstance().compile(
				"/xpath1/xpath2[@pred3=\"value3\"]/xpath3");

		assertTrue(xpathToMatch.matches(complexXPath3));
	}

	private void testAgainstSelf(String xpathString)
			throws XPathParsingException {
		assertTrue(matchStrings(xpathString, xpathString));
	}

	private boolean matchStrings(String xpath1, String xpath2)
			throws XPathParsingException {
		return XPathFactory.getInstance().compile(xpath1)
				.matches(XPathFactory.getInstance().compile(xpath2));
	}
}