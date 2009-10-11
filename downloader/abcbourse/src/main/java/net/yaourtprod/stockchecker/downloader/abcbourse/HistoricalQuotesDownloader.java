/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Queue;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Sauzedde
 * 
 */
public class HistoricalQuotesDownloader {

	/** This class static logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(HistoricalQuotesDownloader.class);
	private static final short FIRST_YEAR = 1992;
	private static final Pattern FORMATED_STRING_PATTERN = Pattern.compile(";");
	private static final Pattern DATE_PATTERN = Pattern.compile("/");
	private HttpClient httpClient = null;
	private File storeDir = null;

	public HistoricalQuotesDownloader(final HttpClient httpClient, final File storeDir) {
		super();
		if (null == httpClient) {
			throw new IllegalArgumentException("A non null HttpClient is mandatory.");
		}

		if (null == storeDir) {
			throw new IllegalArgumentException("A non null File is mandatory.");
		} else {
			if (!(storeDir.exists() && storeDir.isDirectory() && storeDir.canRead() && storeDir.canWrite())) {
				throw new IllegalArgumentException("The storeDir must exist and be a read / write directory.");
			}
		}

		this.httpClient = httpClient;
		this.storeDir = storeDir;
	}

	public boolean download(final LabelAndCodes lac) throws IOException {
		final long allStart = System.nanoTime();
		boolean success = true;

		final GregorianCalendar gc = new GregorianCalendar();
		final int yearNow = gc.get(Calendar.YEAR);

		gc.set(Calendar.DAY_OF_MONTH, 1);
		gc.set(Calendar.MONTH, Calendar.JANUARY);
		gc.set(Calendar.YEAR, FIRST_YEAR);
		String from = null;
		String to = null;
		final File f = new File(storeDir, "quotes-" + lac.getPrimaryCode().getCode() + ".txt");
		while (yearNow > gc.get(Calendar.YEAR)) {
			final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps
					.add(new BasicNameValuePair(
							"ctl00_BodyABC_ToolkitScriptManager1_HiddenField",
							";;AjaxControlToolkit, Version=3.0.20229.20843, Culture=neutral, PublicKeyToken=28f01b0e84b6d53e:fr-FR:3b7d1b28-161f-426a-ab77-b345f2c428f5:e2e86ef9:a9a7729d:9ea3f0e2:9e8e87e9:1df13a87:4c9865be:ba594826:507fcf1b:c7a4182e:3858419b:96741c43:c4c00916:c7c04611:cd120801:38ec41c0"));
			nvps.add(new BasicNameValuePair("__EVENTTARGET", ""));
			nvps.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
			nvps
					.add(new BasicNameValuePair(
							"__VIEWSTATE",
							"/wEPDwUKMTAwOTUyOTgzNWQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFiUFEmN0bDAwJEltYWdlQnV0dG9uMQUSY3RsMDAkQm9keUFCQyRzcmRwBRhjdGwwMCRCb2R5QUJDJGV1cm9saXN0YXAFGGN0bDAwJEJvZHlBQkMkZXVyb2xpc3RicAUYY3RsMDAkQm9keUFCQyRldXJvbGlzdGNwBRljdGwwMCRCb2R5QUJDJGV1cm9saXN0emVwBRpjdGwwMCRCb2R5QUJDJGV1cm9saXN0aHplcAUUY3RsMDAkQm9keUFCQyRhbHRlcnAFFGN0bDAwJEJvZHlBQkMkbm1zcGVwBRBjdGwwMCRCb2R5QUJDJG1sBRRjdGwwMCRCb2R5QUJDJHRyYWNrcAUTY3RsMDAkQm9keUFCQyRwbXRwcAURY3RsMDAkQm9keUFCQyRic3AFFWN0bDAwJEJvZHlBQkMkeGNhYzQwcAUWY3RsMDAkQm9keUFCQyR4c2JmMTIwcAUWY3RsMDAkQm9keUFCQyR4c2JmMjUwcAUVY3RsMDAkQm9keUFCQyR4aXRjYWNwBRdjdGwwMCRCb2R5QUJDJHhjYWNpdDIwcAUWY3RsMDAkQm9keUFCQyR4Y2FjbjIwcAUWY3RsMDAkQm9keUFCQyR4Y2FjczkwcAUXY3RsMDAkQm9keUFCQyR4Y2FjbTEwMHAFGGN0bDAwJEJvZHlBQkMkeGNhY21zMTkwcAUSY3RsMDAkQm9keUFCQyRvYmxwBRZjdGwwMCRCb2R5QUJDJHdhcnJhbnRzBRJjdGwwMCRCb2R5QUJDJGRldnAFEWN0bDAwJEJvZHlBQkMkZGp1BRJjdGwwMCRCb2R5QUJDJG5hc3UFGGN0bDAwJEJvZHlBQkMkaW5kaWNlc0ZScAUSY3RsMDAkQm9keUFCQyRmY3BwBRVjdGwwMCRCb2R5QUJDJG9uZVNpY28FFmN0bDAwJEJvZHlBQkMkZXVyb2xpc3QFHGN0bDAwJEJvZHlBQkMkYWN0aW9uc2luZGljZXMFGmN0bDAwJEJvZHlBQkMkYWN0aW9uc2luZHVzBRVjdGwwMCRCb2R5QUJDJGNvbXBsZXQFG2N0bDAwJEJvZHlBQkMkY29tcGxldG5vd2FycgUTY3RsMDAkQm9keUFCQyRjYlllcwUaY3RsMDAkQm9keUFCQyRJbWFnZUJ1dHRvbjEjom7vETZaT+ZIPSG42B7oOeTBzQ=="));
			nvps.add(new BasicNameValuePair("ctl00$txtAutoComplete", ""));
			nvps.add(new BasicNameValuePair("ctl00$BodyABC$oneSico", "on"));
			nvps.add(new BasicNameValuePair("ctl00$BodyABC$txtOneSico", lac.getPrimaryCode().getCode()));
			nvps.add(new BasicNameValuePair("ctl00$BodyABC$dlFormat", "w"));
			nvps.add(new BasicNameValuePair("ctl00$BodyABC$listFormat", "isin"));
			nvps.add(new BasicNameValuePair("ctl00$BodyABC$ImageButton1.x", "32"));
			nvps.add(new BasicNameValuePair("ctl00$BodyABC$ImageButton1.y", "32"));
			nvps
					.add(new BasicNameValuePair(
							"__EVENTVALIDATION",
							"/wEWOgL63p+wCAKPgp47Auyy8tADAoLSprgMAr7jqP8KAs7D2KsOAonG/cEHAriKpIIJAtPzwZcDAufBpr8OAtqn28IOApeMiP0GAtjwo9MLAoSYiOsEAqajl/kIAsLDoL4BAs/t68QPApeY0uUNArGJ+ZcLAoLFxtkMAtO/kMwLAuq5r9QKAvXt++wMAsapufQGAtT5y/0GApOsm+sGAsLoj7MCAvTZqtILAp+b2IMIAp6bvJgIAo7/6cIPAre767oNAuPI9KICApiV5aUNAuuJ1MQKAu7P4/0BAqKE4sMEAovUvJkOAoyS8LkNArPy16cOAo3fuzkCusuh2g8Cusuh2g8Czcuh2g8CyMuh2g8Cq8uh2g8CyMuh2g8Cusuh2g8CsMuh2g8CvMuh2g8CsMuh2g8Cusuh2g8Cusuh2g8CqtSqawKW8NTsBQKe5dnRCwL1oM32CAL3xdjuDC8+y4vv0R5fTwV7CtuOISwyW29p"));

			from = "01/01/" + gc.get(Calendar.YEAR);
			nvps.add(new BasicNameValuePair("ctl00$BodyABC$strDateDeb", from));
			to = "31/12/" + (gc.get(Calendar.YEAR) + 1);
			nvps.add(new BasicNameValuePair("ctl00$BodyABC$strDateFin", to));
			LOGGER.debug("Downloading now from [{}] to [{}].", from, to);
			final long start = System.nanoTime();
			try {
				final HttpPost httpost = new HttpPost("http://download.abcbourse.com/historiques.aspx");
				httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
				final ResponseHandler<Queue<String>> responseHandler = new MultilinesResponseHandler();
				final Queue<String> responseLines = httpClient.execute(httpost, responseHandler);
				if (null != responseLines) {
					for (final String line : responseLines) {
						if (line.startsWith(lac.getPrimaryCode().getCode())) {
							writeToFile(line);
						} else {
							LOGGER.debug("Not expected data received : [{}...].", line.length() > 20 ? line.substring(0, 20) : line);
						}
					}
				}
			} catch (final Exception e) {
				LOGGER.error("An error occured.", e);
				success = false;
			}
			final long elapsed = System.nanoTime() - start;
			LOGGER.debug("Download took : [{}]ms.", elapsed / 1000000L);
			gc.add(Calendar.YEAR, 2);
		}
		LOGGER.debug("Overall download took : [{}]ms", ((System.nanoTime() - allStart) / 1000000));
		return success;
	}

	private boolean writeToFile(final String formattedLine) throws IOException {
		final boolean success = true;
		final String[] fields = FORMATED_STRING_PATTERN.split(formattedLine);
		if (null != fields && 7 == fields.length) {
			final String[] dateFields = DATE_PATTERN.split(fields[1]);
			final GregorianCalendar gc = new GregorianCalendar();
			gc.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFields[0]));
			gc.set(Calendar.MONTH, Integer.parseInt(dateFields[1]) - 1);
			final short year = Short.parseShort(dateFields[2]);
			gc.set(Calendar.YEAR, year >= 92 ? 1900 + year : 2000 + year);
			final File mainDir = new File(storeDir, fields[0]);
			final File yearDir = new File(mainDir, gc.get(Calendar.YEAR) + "");
			final File monthDir = new File(yearDir, (gc.get(Calendar.MONTH) + 1) < 10 ? "0" + (gc.get(Calendar.MONTH) + 1) : "" + (gc.get(Calendar.MONTH) + 1));
			final File monthFile = new File(monthDir, "data");
			monthDir.mkdirs();
			final FileWriter fw = new FileWriter(monthFile, true);
			fw.write(fields[1]);
			fw.write(';');
			fw.write(fields[2]);
			fw.write(';');
			fw.write(fields[3]);
			fw.write(';');
			fw.write(fields[4]);
			fw.write(';');
			fw.write(fields[5]);
			fw.write(';');
			fw.write(fields[6]);
			fw.write('\n');
			fw.close();
		}
		return success;
	}
}
