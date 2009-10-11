/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.testng.annotations.Test;

/**
 * @author Thomas Sauzedde
 * 
 */
public class LabelAndCodesTest {
	@Test(groups = { "UT" })
	public void constructorTest() {
		final CodeKind ck = new CodeKind("FOO");
		final Code code = new Code(ck, "BAR");
		LabelAndCodes lac = null;
		try {
			lac = new LabelAndCodes(null, null);
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {

		}
		try {
			lac = new LabelAndCodes(code, null);
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {

		}
		try {
			lac = new LabelAndCodes(null, "LABEL");
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {

		}
		try {
			lac = new LabelAndCodes(code, "");
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {

		}
		try {
			lac = new LabelAndCodes(code, "LABEL");
			assertNotNull(lac);
		} catch (final IllegalArgumentException iae) {
			fail("IllegalArgumentException NOT expected.");
		}
	}

	@Test(groups = { "UT" })
	public void otherCodesTest() {
		final CodeKind ck = new CodeKind("FOO");
		final CodeKind ck1 = new CodeKind("AAA");
		final CodeKind ck2 = new CodeKind("BBB");
		final Code code = new Code(ck, "BAR");
		final LabelAndCodes lac = new LabelAndCodes(code, "LABEL");
		final Code otherCode1 = new Code(ck1, "AAA");
		final Code otherCode2 = new Code(ck2, "BBB");
		final Code otherCode3 = new Code(ck2, "CCC");
		final Code otherCode4 = new Code(ck, "CCC");

		Code result = null;
		result = lac.putOtherCode(otherCode1);
		assertNull(result);
		result = lac.putOtherCode(otherCode2);
		assertNull(result);

		assertNotNull(lac.getOtherCodes());
		assertTrue(lac.getOtherCodes().contains(otherCode1));
		assertTrue(lac.getOtherCodes().contains(otherCode2));

		result = lac.putOtherCode(otherCode3);
		assertEquals(result, otherCode2);

		assertNotNull(lac.getOtherCodes());
		assertTrue(lac.getOtherCodes().contains(otherCode1));
		assertFalse(lac.getOtherCodes().contains(otherCode2));
		assertTrue(lac.getOtherCodes().contains(otherCode3));

		result = lac.putOtherCode(otherCode4);
		assertEquals(result, code);
		assertEquals(lac.getPrimaryCode(), otherCode4);
		assertNotNull(lac.getOtherCodes());
		assertFalse(lac.getOtherCodes().contains(otherCode4));

	}
}
