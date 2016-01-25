package ch.globaz.skirnir.client.impl;

import ch.globaz.skirnir.client.SkirnirClient;

/** A NO-OP implementation. */
public final class SkirnirNoopClientImpl implements SkirnirClient {
	@Override
	public void publishFacturation(Long passageId, String document, String type,
			String subtype, String mode, String from, String to, String order,
			boolean ged, boolean finalprint) {
	}
}
