package ch.globaz.skirnir.client;

import static ch.globaz.skirnir.client.SkirnirClientFactory.DEFAULT_FACTORY_IMPLEMENTATION;
import static ch.globaz.skirnir.client.SkirnirClientFactory.FACTORY_PROPERTY;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;

import org.apache.commons.lang.StringUtils;

public class SkirnirUtils {
	private SkirnirUtils() {
	}

	/**
	 * Create the configured factory (or fallback to the default
	 * implementation), and then build a client, using the configuration
	 * available in {@link BSession}.
	 */
	public static SkirnirClient buildDefaultClient(BSession bsession) {
		BApplication app;
		try {
			// TODO explain why this BSessionUtil does not work here?
			//app = BSessionUtil.getSessionFromThreadContext().getApplication();
			app = bsession.getApplication();
		} catch (Exception e) {
			throw new IllegalStateException("unable to get application: " + e,
					e);
		}

		String factoryClass = app.getProperty(FACTORY_PROPERTY);
		if (StringUtils.isBlank(factoryClass)) {
			factoryClass = DEFAULT_FACTORY_IMPLEMENTATION;
		}

		SkirnirClientFactory factory;
		try {
			factory = (SkirnirClientFactory) Class.forName(factoryClass)
					.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("unable to build factory class "
					+ factoryClass + ": " + e, e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("unable to build factory class "
					+ factoryClass + ": " + e, e);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("unable to build factory class "
					+ factoryClass + ": " + e, e);
		}

		return factory.newClient(bsession);
	}
}
