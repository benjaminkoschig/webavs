package ch.globaz.skirnir.client;

public interface SkirnirClient {
	// skirnir.client.musca.urlpattern
	String FACTURATION_URLPATTERN_PROP = "skirnir.client.musca.urlpattern";

	void publishFacturation(Long passageId, String document, String type, String subtype, String mode,
			String from, String to, String order, boolean ged, boolean finalprint);
}
