package globaz.aquila.service.cataloguetxt.cache;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import java.util.HashMap;

public class COCacheCatalogue {
    /**
     * @return l'instance de COCacheCatalogue cr��e
     */
    public static COCacheCatalogue newInstance() {
        return new COCacheCatalogue();
    }

    private HashMap catalogueContainer = null; // Cache pour les catalogues

    // selon key
    private HashMap textesContainer = null; // Cache pour les textes selon key

    /**
	 *
	 */
    private COCacheCatalogue() {
        super();
        catalogueContainer = new HashMap();
        textesContainer = new HashMap();
    }

    /**
     * @param langue
     * @param typeDocument
     * @param nomDocument
     * @param idDocument
     * @param isDefault
     * @param document
     */
    public void addDocument(String langue, String typeDocument, String nomDocument, String idDocument,
            Boolean isDefault, ICTDocument document) {
        catalogueContainer.put(getKey(langue, typeDocument, nomDocument, idDocument, isDefault), document);
    }

    /**
     * @param idDocument
     * @param niveau
     * @param textes
     */
    public void addListeTextes(String idDocument, int niveau, ICTListeTextes textes, String langueIso) {
        textesContainer.put(getKey(idDocument, niveau, langueIso), textes);
    }

    /**
     * @param langue
     * @param typeDocument
     * @param nomDocument
     * @param idDocument
     * @param isDefault
     * @return <tt>true</tt> si le document est d�j� cr��
     */
    public boolean containsDocument(String langue, String typeDocument, String nomDocument, String idDocument,
            Boolean isDefault) {
        return catalogueContainer.containsKey(getKey(langue, typeDocument, nomDocument, idDocument, isDefault));
    }

    /**
     * @param idDocument
     * @param niveau
     * @return <tt>true</tt> si les textes ont d�j� �t� charg�
     */
    public boolean containsListeTextes(String idDocument, int niveau, String langueIso) {
        return textesContainer.containsKey(getKey(idDocument, niveau, langueIso));
    }

    /**
     * @param langue
     * @param typeDocument
     * @param nomDocument
     * @param idDocument
     * @param isDefault
     * @return le document correspondant aux param�tres
     */
    public ICTDocument getDocument(String langue, String typeDocument, String nomDocument, String idDocument,
            Boolean isDefault) {
        return (ICTDocument) catalogueContainer.get(getKey(langue, typeDocument, nomDocument, idDocument, isDefault));
    }

    /**
     * @param idDocument
     * @param niveau
     * @return la cl� identifiant le texte.
     */
    public String getKey(String idDocument, int niveau, String langueIso) {
        return idDocument + "_" + Integer.toString(niveau) + "_" + langueIso;
    }

    /**
     * @param langue
     * @param typeDocument
     * @param nomDocument
     * @param idDocument
     * @param isDefault
     * @return la cl� identifiant le document
     */
    public String getKey(String langue, String typeDocument, String nomDocument, String idDocument, Boolean isDefault) {
        return langue + "_" + typeDocument + "_" + nomDocument + "_" + idDocument + "_"
                + (isDefault == null ? Boolean.FALSE : isDefault);
    }

    /**
     * @param idDocument
     * @param niveau
     * @return la liste des textes pour ce niveau.
     */
    public ICTListeTextes getListeTextes(String idDocument, int niveau, String langueIso) {
        return (ICTListeTextes) textesContainer.get(getKey(idDocument, niveau, langueIso));
    }
}
