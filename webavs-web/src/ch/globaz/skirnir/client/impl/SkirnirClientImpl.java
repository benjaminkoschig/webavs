package ch.globaz.skirnir.client.impl;

import static java.util.regex.Pattern.quote;
import globaz.jade.log.JadeLogger;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.Validate;

import ch.globaz.skirnir.client.SkirnirClient;

public class SkirnirClientImpl implements SkirnirClient {
	private final String urlPattern;
	private final String userName;

	public SkirnirClientImpl(String urlPattern, String userName) {
		Validate.notEmpty(urlPattern);
		Validate.notEmpty(userName);
		this.urlPattern = urlPattern;
		this.userName = userName;
	}
	
	/** null/empty safe value */
	private static <T> T nvl(T val, T def) {
		if(val==null) {
			return def;
		}
		
		if(val instanceof String) {
			if(((String) val).trim().isEmpty()) {
				return def;
			}
		}
		
		return val;
	}

	@Override
	public void publishFacturation(Long passageId, String document, String type,
			String subtype, String mode, String from, String to, String order,
			boolean ged, boolean finalprint) {

		String url = urlPattern;

		url = url.replaceAll(quote("${user}"), nvl(userName, "unknown"));
		url = url.replaceAll(quote("${passageId}"), nvl(String.valueOf(passageId), "-1"));
		url = url.replaceAll(quote("${doc}"), nvl(document, "unknown"));
		url = url.replaceAll(quote("${type}"), nvl(type, "unknown"));
		url = url.replaceAll(quote("${subtype}"), nvl(subtype, "unknown"));
		url = url.replaceAll(quote("${mode}"), nvl(mode, "all"));
		url = url.replaceAll(quote("${from}"), nvl(String.valueOf(from), "all"));
		url = url.replaceAll(quote("${to}"), nvl(String.valueOf(to), "all"));
		url = url.replaceAll(quote("${order}"), nvl(order, "none"));
		url = url.replaceAll(quote("${ged}"), String.valueOf(nvl(ged, false)));
		url = url.replaceAll(quote("${fprint}"), String.valueOf(nvl(finalprint, false)));

		HttpClient client = new HttpClient();
		try {
			int get = client.executeMethod(new GetMethod(url));

			if (get != 200) {
				throw new IllegalStateException("unable to invoke http url: "
						+ url + " (result is " + get + ")");
			}

			JadeLogger.info(this,
					"successfully invoked publish service on url " + url);
		} catch (HttpException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}
