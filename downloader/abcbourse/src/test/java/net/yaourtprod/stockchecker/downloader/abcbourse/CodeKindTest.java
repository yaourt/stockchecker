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
public class CodeKindTest {
	@Test(groups = { "UT" })
	public void constructorTest() {
		CodeKind ck = null;
		try {
			ck = new CodeKind(null);
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {

		}
		try {
			ck = new CodeKind("");
			fail("IllegalArgumentException expected.");
		} catch (final IllegalArgumentException iae) {

		}
		try {
			ck = new CodeKind("AAA");
		} catch (final IllegalArgumentException iae) {
			fail("IllegalArgumentException NOT expected.");
		}
	}
}
