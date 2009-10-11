/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Queue;

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
public class LabelAndCodeDownloader {

    /** This class static logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LabelAndCodeDownloader.class);

    public enum AvailableDownload {

        SRD("ctl00$BodyABC$srdp"),
        EuroList_A("ctl00$BodyABC$eurolistAp"),
        EuroList_B("ctl00$BodyABC$eurolistBp"),
        EuroList_C("ctl00$BodyABC$eurolistCp"),
        EuroList_EUR("ctl00$BodyABC$eurolistzep"),
        EuroList_Hors_Eur("ctl00$BodyABC$eurolisthzep"),
        Alternext("ctl00$BodyABC$alterp"),
        Marche_Libre_FR("ctl00$BodyABC$mlp"),
        Marche_Libre_Foreign("ctl00$BodyABC$mlep"),
        Trackers("ctl00$BodyABC$trackp"),
        CAC40("ctl00$BodyABC$xcac40p"),
        SBF120("ctl00$BodyABC$xsbf120p"),
        SBF250("ctl00$BodyABC$xsbf250p"),
        Bonds("ctl00$BodyABC$oblp"),
        Warrants("ctl00$BodyABC$warrants"),
        Euro_Exchange("ctl00$BodyABC$devp"),
        DowJones("ctl00$BodyABC$dju"),
        NASDAQ("ctl00$BodyABC$nasu");

        private String code = null;

        AvailableDownload(final String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    };

    private HttpClient httpClient = null;
    private File storeDir = null;

    public LabelAndCodeDownloader(final HttpClient httpClient, final File storeDir) {
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

    public boolean download(final AvailableDownload kind) {
        boolean success = true;
        final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair(
                "__VIEWSTATE",
                "/wEPDwUKMTg1MDgyNzQxNGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFiEFEmN0bDAwJEltYWdlQnV0dG9uMQUSY3RsMDAkQm9keUFCQyRzcmRwBRhjdGwwMCRCb2R5QUJDJGV1cm9saXN0QXAFGGN0bDAwJEJvZHlBQkMkZXVyb2xpc3RCcAUYY3RsMDAkQm9keUFCQyRldXJvbGlzdENwBRljdGwwMCRCb2R5QUJDJGV1cm9saXN0emVwBRpjdGwwMCRCb2R5QUJDJGV1cm9saXN0aHplcAUUY3RsMDAkQm9keUFCQyRhbHRlcnAFFGN0bDAwJEJvZHlBQkMkbm1zcGVwBRFjdGwwMCRCb2R5QUJDJG1scAUSY3RsMDAkQm9keUFCQyRtbGVwBRRjdGwwMCRCb2R5QUJDJHRyYWNrcAUTY3RsMDAkQm9keUFCQyRwbXRwcAURY3RsMDAkQm9keUFCQyRic3AFFWN0bDAwJEJvZHlBQkMkeGNhYzQwcAUWY3RsMDAkQm9keUFCQyR4c2JmMTIwcAUWY3RsMDAkQm9keUFCQyR4c2JmMjUwcAUVY3RsMDAkQm9keUFCQyR4aXRjYWNwBRdjdGwwMCRCb2R5QUJDJHhjYWNpdDIwcAUWY3RsMDAkQm9keUFCQyR4Y2FjbjIwcAUWY3RsMDAkQm9keUFCQyR4Y2FjczkwcAUXY3RsMDAkQm9keUFCQyR4Y2FjbTEwMHAFGGN0bDAwJEJvZHlBQkMkeGNhY21zMTkwcAUSY3RsMDAkQm9keUFCQyRvYmxwBRZjdGwwMCRCb2R5QUJDJHdhcnJhbnRzBRJjdGwwMCRCb2R5QUJDJGRldnAFEWN0bDAwJEJvZHlBQkMkZGp1BRJjdGwwMCRCb2R5QUJDJG5hc3UFEmN0bDAwJEJvZHlBQkMkYmVsZwUTY3RsMDAkQm9keUFCQyRob2xsbgUYY3RsMDAkQm9keUFCQyRpbmRpY2VzRlJwBRJjdGwwMCRCb2R5QUJDJGZjcHAFGmN0bDAwJEJvZHlBQkMkSW1hZ2VCdXR0b24xnuJIstMmHrIyVKfg39CmGe7HgfI="));
        nvps.add(new BasicNameValuePair("ctl00$txtAutoComplete", ""));
        nvps.add(new BasicNameValuePair("ctl00$BodyABC$ImageButton1.x", "44"));
        nvps.add(new BasicNameValuePair("ctl00$BodyABC$ImageButton1.y", "8"));
        nvps.add(new BasicNameValuePair("__EVENTTARGET", ""));
        nvps.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
        nvps.add(new BasicNameValuePair(
                "__EVENTVALIDATION",
                "/wEWIwKUmc66DwKPgp47Auyy8tADAs7D2KsOAunst+kMApix3qkOArOa/L4IAufBpr8OAtqn28IOApeMiP0GAtjwo9MLAty6k7oCAvG607oCAqajl/kIAsLDoL4BAs/t68QPApeY0uUNArGJ+ZcLAoLFxtkMAtO/kMwLAuq5r9QKAvXt++wMAsapufQGAtT5y/0GApOsm+sGAsLoj7MCAvTZqtILAp+b2IMIAp6bvJgIAo7/6cIPAuvt564MAvzwvfoHAre767oNAuPI9KICAvWgzfYIMrG/OVrXiYa1PguLfHFmZ1t9WEI="));
        nvps.add(new BasicNameValuePair(kind.getCode(), "on"));
        final HttpPost httpost = new HttpPost("http://download.abcbourse.com/libelles.aspx");
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

            final ResponseHandler<Queue<String>> responseHandler = new MultilinesResponseHandler();

            final Queue<String> responseLines = httpClient.execute(httpost, responseHandler);
            if (null != responseLines) {
                final File tmpFile = new File(storeDir, kind.name());
                final FileWriter tmpFW = new FileWriter(tmpFile);
                for (final String line : responseLines) {
                    tmpFW.write(line);
                    tmpFW.write('\n');

                    final LabelAndCodes lac = LabelAndCodes.getFromFormattedString(line);
                    final File d = new File(storeDir, lac.getPrimaryCode().getCode().toString());
                    d.mkdirs();
                    final File f = new File(d, "info");
                    final Properties props = new Properties();
                    if (f.exists() && f.isFile() && f.canRead()) {
                        final FileReader reader = new FileReader(f);
                        props.load(reader);
                        reader.close();
                    }

                    if (!"ISIN".equals(lac.getPrimaryCode().getCode().toString())) {
                        final FileWriter fw = new FileWriter(f);
                        props.put("label", lac.getLabel());
                        props.put("code." + lac.getPrimaryCode().getKind().getKind(), lac.getPrimaryCode().getCode().toString());
                        for (final Code c : lac.getOtherCodes()) {
                            props.put("code." + c.getKind().getKind(), c.getCode().toString());
                        }
                        props.store(fw, "Stock information");
                        fw.close();
                    }
                }
                tmpFW.close();
            }
        } catch (final Exception e) {
            LOGGER.error("An error occured.", e);
            success = false;
        }

        return success;
    }
}
