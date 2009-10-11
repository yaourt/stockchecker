/**
 * 
 */
package net.yaourtprod.stockchecker.downloader.abcbourse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

/**
 * @author Thomas Sauzedde
 * 
 */
public class MultilinesResponseHandler implements ResponseHandler<Queue<String>> {
	@Override
	public Queue<String> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
		final Queue<String> result = new LinkedList<String>();
		final HttpEntity content = response.getEntity();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(content.getContent()));
		String line = null;
		while (null != (line = reader.readLine())) {
			result.add(line);
		}
		reader.close();
		return result;
	}
}