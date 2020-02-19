/**
 *
 */
package globaz.aquila.service.cataloguetxt;

import globaz.aquila.service.cataloguetxt.cache.COCacheCatalogueFacade;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.itext.FAImpressionFacturation;
import java.util.Iterator;

/**
 * @author sel
 */
public class COCatalogueTextesService {

    public static final String CS_CONTENTIEUX_AVS = "5600001";
    public static final String CS_DOMAINE_CONTENTIEUX_CAP_CGAS = "5600003";
    private static final String DOMAINE_FACTURATION = FAImpressionFacturation.DOMAINE_FACTURATION;
    public static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";
    private static final String TYPE_FACTURE = FAImpressionFacturation.TYPE_FACTURE;

    protected ICTDocument document = null;

    // Catalogue de textes
    private ICTDocument documentMusca = null;
    private String domaineDocument = COCatalogueTextesService.CS_CONTENTIEUX_AVS;

    private String idDocument = null;
    private String langueDoc = null;
    private String nomDocument = null;
    // private Boolean isDefault = Boolean.FALSE;
    private int numDocument = 0;
    private BSession session = null;
    private String typeDocument = null;

    /**
     * @param niveau
     *            du catalogue de textes
     * @param out
     *            Buffer de sortie
     * @param paraSep
     *            Chaine de séparation de paragraphe
     */
    public void dumpNiveau(Object key, int niveau, StringBuilder out, String paraSep) {
        try {
            ICTListeTextes textes = null;
            if ((key == null)
                    || !COCacheCatalogueFacade.getInstance().getCache(key)
                            .containsListeTextes(loadCatalogue(key).getIdDocument(), niveau, getLangueDoc())) {
                textes = loadCatalogue(key).getTextes(niveau);
                if (key != null) {
                    COCacheCatalogueFacade.getInstance().getCache(key)
                            .addListeTextes(loadCatalogue(key).getIdDocument(), niveau, textes, getLangueDoc());
                }
            } else {
                textes = COCacheCatalogueFacade.getInstance().getCache(key)
                        .getListeTextes(loadCatalogue(key).getIdDocument(), niveau, getLangueDoc());
            }

            Iterator paraIter = textes.iterator();
            while (paraIter.hasNext()) {
                if (out.length() > 0) {
                    out.append(paraSep);
                }

                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            out.append(niveau + " " + COCatalogueTextesService.TEXTE_INTROUVABLE);
        }
    }

    public String getDomaineDocument() {
        return domaineDocument;
    }

    /**
     * @return the idDocument
     */
    public String getIdDocument() {
        return idDocument;
    }

    /**
     * @return the langueDoc
     */
    public String getLangueDoc() {
        return langueDoc;
    }

    /**
     * Renvoie la liste des textes pour la langue de la session courante.
     * 
     * @return la liste des textes pour la langue de la session courante, null si elle n'a pas été trouvée
     * @throws Exception
     *             en cas d'erreur
     */
    public ICTDocument getMuscaDocument() throws Exception {
        if ((getSession() == null) || (JadeStringUtil.isBlank(getSession().getIdLangueISO()))) {
            return null;
        }
        if (documentMusca == null) {
            ICTDocument apiDocument = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            apiDocument.setISession(getSession());
            apiDocument.setCsDomaine(COCatalogueTextesService.DOMAINE_FACTURATION);
            apiDocument.setCsTypeDocument(COCatalogueTextesService.TYPE_FACTURE);
            apiDocument.setDefault(new Boolean(true));
            apiDocument.setActif(new Boolean(true));
            ICTDocument[] docs = apiDocument.load();
            if ((docs != null) && (docs.length > 0)) {
                documentMusca = docs[0];
            }
        }
        return documentMusca;
    }

    /**
     * @return the nomDocument
     */
    public String getNomDocument() {
        return nomDocument;
    }

    /**
     * @return
     */
    public int getNumDocument() {
        return numDocument;
    }

    /**
     * @return the session
     */
    public BSession getSession() {
        return session;
    }

    /**
     * @return the typeDocument
     */
    public String getTypeDocument() {
        return typeDocument;
    }

    /**
     * @param key
     *            : clé utiliser pour le cache.
     * @param nomDocument
     *            : nom du document à tester.
     * @return true s'il existe un document dans le catalogue de texte.
     */
    public boolean isExistDocument(Object key, String nomDocument) {
        String tempNomDocument = "";
        try {
            tempNomDocument = getNomDocument();
            setNomDocument(nomDocument);
            loadCatalogue(key);
        } catch (Exception e) {
            return false;
        } finally {
            setNomDocument(tempNomDocument);
        }
        return true;
    }

    /**
     * retourne le catalogue de texte pour le document courant.
     * 
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    private ICTDocument loadCatalogue(Object key) throws Exception {
        if ((document == null) || !document.getNom().equals(getNomDocument())) {

            ICTDocument loader = (ICTDocument) getSession().getAPIFor(ICTDocument.class);

            loader.setActif(Boolean.TRUE);
            loader.setCodeIsoLangue(getLangueDoc());
            loader.setCsDomaine(domaineDocument);
            loader.setCsTypeDocument(getTypeDocument());

            if (!JadeStringUtil.isBlank(getNomDocument())) {
                loader.setNom(getNomDocument());
            } else if (!JadeStringUtil.isBlank(getIdDocument())) {
                loader.setIdDocument(getIdDocument());
            } else {
                loader.setDefault(Boolean.TRUE);
            }

            if ((key == null)
                    || !COCacheCatalogueFacade
                            .getInstance()
                            .getCache(key)
                            .containsDocument(getLangueDoc(), getTypeDocument(), getNomDocument(), getIdDocument(),
                                    loader.isDefaut())) {

                ICTDocument[] candidats = loader.load();

                if ((!JadeStringUtil.isBlank(getNomDocument()) || !JadeStringUtil.isBlank(getIdDocument()))
                        && ((candidats == null) || (candidats.length == 0))) {
                    loader.setNom("");
                    loader.setIdDocument("");
                    loader.setDefault(Boolean.TRUE);
                    candidats = loader.load();
                    throw new Exception("Impossible de trouver le catalogue de texte pour : " + getIdDocument() + " - "
                            + getNomDocument());
                    // log("Impossible de trouver le catalogue de texte pour : "
                    // + getIdDocument() + " - " + getNomDocument(),
                    // FWMessage.WARNING);
                    // getMemoryLog().logMessage(getMsgErreurContentieux("Impossible de trouver le catalogue de texte pour : "
                    // + getIdDocument() + " - " + getNomDocument()),
                    // FWMessage.WARNING, getClass().getName());
                }

                if ((candidats == null) || (candidats.length == 0)) {
                    throw new Exception("Impossible de trouver le catalogue de texte");
                }
                if (key != null) {
                    COCacheCatalogueFacade
                            .getInstance()
                            .getCache(key)
                            .addDocument(getLangueDoc(), getTypeDocument(), getNomDocument(), getIdDocument(),
                                    loader.isDefaut(), candidats[numDocument]);
                }
                document = candidats[numDocument];
            } else {
                document = COCacheCatalogueFacade
                        .getInstance()
                        .getCache(key)
                        .getDocument(getLangueDoc(), getTypeDocument(), getNomDocument(), getIdDocument(),
                                loader.isDefaut());
            }
        }

        return document;
    }

    public void setDomaineDocument(String domaineDocument) {
        this.domaineDocument = domaineDocument;
    }

    /**
     * @param idDocument
     *            the idDocument to set
     */
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    /**
     * @param langueDoc
     *            the langueDoc to set
     */
    public void setLangueDoc(String langueDoc) {
        this.langueDoc = langueDoc;
    }

    /**
     * @param nomDocument
     *            the nomDocument to set
     */
    public void setNomDocument(String nomDocument) {
        this.nomDocument = nomDocument;
    }

    /**
     * @param numDocument
     */
    public void setNumDocument(int numDocument) {
        this.numDocument = numDocument;
    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * @param typeDocument
     *            the typeDocument to set
     */
    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    /**
     * Retourne le texte du catalogue courant au niveau et à la position données.
     * 
     * @param niveau
     *            le niveau du texte
     * @param position
     *            la position du texte dans le niveau
     * @return le texte donné ou un message d'erreur si erreur
     */
    public String texte(Object key, int niveau, int position) {
        try {
            if ((key == null)
                    || !COCacheCatalogueFacade.getInstance().getCache(key)
                            .containsListeTextes(loadCatalogue(key).getIdDocument(), niveau, getLangueDoc())) {
                ICTListeTextes textes = loadCatalogue(key).getTextes(niveau);
                if (key != null) {
                    COCacheCatalogueFacade.getInstance().getCache(key)
                            .addListeTextes(loadCatalogue(key).getIdDocument(), niveau, textes, getLangueDoc());
                }
                return textes.getTexte(position).getDescription();
            } else {
                return COCacheCatalogueFacade.getInstance().getCache(key)
                        .getListeTextes(loadCatalogue(key).getIdDocument(), niveau, getLangueDoc()).getTexte(position)
                        .getDescription();
            }
        } catch (Exception e) {
            return niveau + ":" + position + " " + COCatalogueTextesService.TEXTE_INTROUVABLE;
        }
    }
}
