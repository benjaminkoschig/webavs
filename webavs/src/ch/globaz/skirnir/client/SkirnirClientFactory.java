package ch.globaz.skirnir.client;

import globaz.globall.db.BSession;
import ch.globaz.skirnir.client.impl.DefaultSkirnirClientFactory;

public interface SkirnirClientFactory {
	/**
	 * Optional property to set if you would like to build {@link SkirnirClient}
	 * in a different way.
	 */
	String FACTORY_PROPERTY = "skirnir.client.factory.class";

	/**
	 * The default implementation of the factory, used if none other is defined.
	 */
	String DEFAULT_FACTORY_IMPLEMENTATION = DefaultSkirnirClientFactory.class
			.getName();

	/**
	 * Build a client. Never returns <code>null</code>, but may return a noop
	 * implementation.
	 */
	SkirnirClient newClient(BSession bsession);
}