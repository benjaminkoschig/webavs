package ch.globaz.skirnir.client.impl;

import globaz.globall.db.BSession;

import org.apache.commons.lang.StringUtils;

import ch.globaz.skirnir.client.SkirnirClient;
import ch.globaz.skirnir.client.SkirnirClientFactory;

/**
 * Factory par défaut, s'appuie sur les propriétés accédées via la session
 * courante, build un {@link SkirnirClientImpl} si l'url spécifiée n'est pas
 * vide, sinon, un {@link SkirnirNoopClientImpl} (qui ne fait rien).
 */
public class DefaultSkirnirClientFactory implements SkirnirClientFactory {
	@Override
	public SkirnirClient newClient(BSession bsession) {
		// TODO : why BSessionUtil does not work?
		//BSession session = BSessionUtil.getSessionFromThreadContext();
		String urlPattern;
		
		try {
			urlPattern = bsession.getApplication().getProperty(
					SkirnirClient.FACTURATION_URLPATTERN_PROP);
		} catch (Exception e) {
			throw new IllegalStateException("unable to retrieve a property: "
					+ e, e);
		}

		if (StringUtils.isNotBlank(urlPattern)) {
			return new SkirnirClientImpl(urlPattern, bsession.getUserId());
		}

		// by default, return a noop implementation
		return new SkirnirNoopClientImpl();
	}
}
