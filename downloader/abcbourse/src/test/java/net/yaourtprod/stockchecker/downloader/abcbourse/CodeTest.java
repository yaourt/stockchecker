/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

import static org.testng.Assert.fail;

import org.testng.annotations.Test;

/**
 * @author Thomas Sauzedde
 * 
 */
public class CodeTest {
	@Test(groups = { "UT" })
	public void constructorTest() {
		final CodeKind ck = new CodeKind("FOO");
		Code code = null;
		try {
			code = new Code(null, null);
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {
		}
		try {
			code = new Code(ck, null);
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {
		}
		try {
			code = new Code(null, "BAR");
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {
		}
		try {
			code = new Code(ck, "");
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {
		}
		try {
			code = new Code(ck, "BAR");
		} catch (final IllegalArgumentException iae) {
			fail("IllegalArgumentException NOT expected.");
		}
	}
}
