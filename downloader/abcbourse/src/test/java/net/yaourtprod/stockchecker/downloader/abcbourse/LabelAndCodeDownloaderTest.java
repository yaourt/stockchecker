/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

import static org.testng.Assert.assertTrue;

import java.io.File;

import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Thomas Sauzedde
 * 
 */
public class LabelAndCodeDownloaderTest {

	/** This class static logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(LabelAndCodeDownloaderTest.class);

	@Test(groups = "INT")
	public void eurolistADownloadTest() {
		final DefaultHttpClient httpclient = new DefaultHttpClient();
		final File storeDir = new File("target/test-classes/downloaded");
		if (!storeDir.exists()) {
			storeDir.mkdirs();
		}

		final LabelAndCodeDownloader downloader = new LabelAndCodeDownloader(httpclient, storeDir);
		final long start = System.nanoTime();
		boolean result = downloader.download(LabelAndCodeDownloader.AvailableDownload.EuroList_A);
		assertTrue(result);
		result = downloader.download(LabelAndCodeDownloader.AvailableDownload.EuroList_B);
		assertTrue(result);
		result = downloader.download(LabelAndCodeDownloader.AvailableDownload.EuroList_C);
		assertTrue(result);
		final long end = System.nanoTime();
		LOGGER.debug("Elapsed time : {} ms", (end - start) / 1000000L);
		httpclient.getConnectionManager().shutdown();
	}
}
