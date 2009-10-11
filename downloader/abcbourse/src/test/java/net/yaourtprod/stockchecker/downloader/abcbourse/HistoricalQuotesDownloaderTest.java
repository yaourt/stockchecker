/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.annotations.Test;

/**
 * @author Thomas Sauzedde
 * 
 */
public class HistoricalQuotesDownloaderTest {
	@Test(groups = { "UT" })
	public void downloadTest() throws IOException {
		final DefaultHttpClient httpclient = new DefaultHttpClient();
		final File storeDir = new File("target/test-classes/downloaded");
		if (!storeDir.exists()) {
			storeDir.mkdirs();
		}

		final CodeKind kind = new CodeKind("FOO");
		final Code primaryCode = new Code(kind, "FR0000120537");
		final LabelAndCodes lac = new LabelAndCodes(primaryCode, "AAA");
		final HistoricalQuotesDownloader downloader = new HistoricalQuotesDownloader(httpclient, storeDir);
		final boolean result = downloader.download(lac);
		assertTrue(result);
		httpclient.getConnectionManager().shutdown();
	}
}
