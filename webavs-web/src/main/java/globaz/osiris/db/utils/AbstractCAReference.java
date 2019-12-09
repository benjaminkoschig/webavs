package globaz.osiris.db.utils;

import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.itext.FAImpressionFacturation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractCAReference {

    public static final String REFERENCE_NON_FACTURABLE = "XXXXXXXXXXXXXXXXX";
    public static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";
    public static final String RETOUR_LIGNE = "\n";

    private static final String DOMAINE_FACTURATION = FAImpressionFacturation.DOMAINE_FACTURATION;
    private static final String TYPE_FACTURE = FAImpressionFacturation.TYPE_FACTURE;



    private String ligneReference;
    private BSession session;
    private static Map documents;
    private boolean isQRFacture;


    public AbstractCAReference(){
        ligneReference = AbstractCAReference.REFERENCE_NON_FACTURABLE;
        session = null;
        documents = null;
        isQRFacture = "true".equals(globaz.jade.properties.JadePropertiesService.getInstance().getProperty("common.qrFacture"));
    }

    /**
     * @return the ligneReference
     */
    public String getLigneReference() {
        return ligneReference;
    }

    /**
     * @param ligneReference
     *            the ligneReference to set
     */
    public void setLigneReference(String ligneReference) {
        this.ligneReference = ligneReference;
    }


    /**
     * Retourne l'adresse pour le BVR (va rechercher dans le catalogue de textes MUSCA)
     *
     * @return
     */
    public String getAdresse() {
        StringBuffer adresse = new StringBuffer("");
        try {
            // va rechercher les textes qui sont au niveau 1
            if (this.getCurrentDocument() == null) {
                adresse.append(AbstractCAReference.TEXTE_INTROUVABLE);
            } else {
                this.dumpNiveau(1, adresse, AbstractCAReference.RETOUR_LIGNE);
            }
        } catch (Exception e3) {
            adresse.append(AbstractCAReference.TEXTE_INTROUVABLE);
        }
        return adresse.toString();
    }

    /**
     * Retourne l'adresse pour le BVR (va rechercher dans le catalogue de textes MUSCA)
     *
     * @return
     */
    public String getAdresse(String langueIso) {
        StringBuffer adresse = new StringBuffer("");
        try {
            // va rechercher les textes qui sont au niveau 1
            if (this.getCurrentDocument(getSession(), langueIso) == null) {
                adresse.append(TEXTE_INTROUVABLE);
            } else {
                this.dumpNiveau(1, adresse, RETOUR_LIGNE, langueIso);
            }
        } catch (Exception e3) {
            adresse.append(TEXTE_INTROUVABLE);
        }
        return adresse.toString();
    }

    /**
     * Renvoie la liste des textes pour la langue de la session courante.
     *
     * @return la liste des textes pour la langue de la session courante, null si elle n'a pas été trouvée
     * @throws Exception
     *             en cas d'erreur
     */
    public ICTDocument getCurrentDocument() throws Exception {
        if ((getSession() == null) || (JadeStringUtil.isBlank(getSession().getIdLangueISO()))) {
            return null;
        }
        ICTDocument document = (ICTDocument) getDocuments().get(getSession().getIdLangueISO());
        if (document == null) {
            ICTDocument apiDocument = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            apiDocument.setISession(getSession());
            apiDocument.setCsDomaine(DOMAINE_FACTURATION);
            apiDocument.setCsTypeDocument(TYPE_FACTURE);
            apiDocument.setDefault(new Boolean(true));
            // document.setCodeIsoLangue(plan.getCompteAnnexe().getTiers().getLangueISO());
            // //"FR"
            apiDocument.setActif(new Boolean(true));
            ICTDocument[] docs = apiDocument.load();
            if ((docs != null) && (docs.length > 0)) {
                document = docs[0];
                getDocuments().put(getSession().getIdLangueISO(), document);
            }
        }
        return document;
    }

    /**
     * Renvoie la liste des textes pour la langue de la session courante.
     *
     * @return la liste des textes pour la langue de la session courante, null si elle n'a pas été trouvée
     * @throws Exception
     *             en cas d'erreur
     */
    public ICTDocument getCurrentDocument(BSession session, String langueIso) throws Exception {
        if ((session == null) || (JadeStringUtil.isBlank(langueIso))) {
            return null;
        }
        ICTDocument document = (ICTDocument) getDocuments().get(langueIso);
        if (document == null) {
            ICTDocument apiDocument = (ICTDocument) session.getAPIFor(ICTDocument.class);
            apiDocument.setISession(session);
            apiDocument.setCsDomaine(DOMAINE_FACTURATION);
            apiDocument.setCsTypeDocument(TYPE_FACTURE);
            apiDocument.setDefault(new Boolean(true));
            // document.setCodeIsoLangue(plan.getCompteAnnexe().getTiers().getLangueISO());
            // //"FR"
            apiDocument.setCodeIsoLangue(langueIso);
            apiDocument.setActif(new Boolean(true));
            ICTDocument[] docs = apiDocument.load();
            if ((docs != null) && (docs.length > 0)) {
                document = docs[0];
                getDocuments().put(langueIso, document);
            }
        }
        return document;
    }

    /**
     * Récupère les textes pour un niveau
     *
     * @param niveau
     * @param out
     * @param paraSep
     */
    protected void dumpNiveau(int niveau, StringBuffer out, String paraSep) {
        try {
            for (Iterator paraIter = this.getCurrentDocument().getTextes(niveau).iterator(); paraIter.hasNext();) {
                if (out.length() > 0) {
                    out.append(paraSep);
                }
                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.append(TEXTE_INTROUVABLE);
        }
    }

    /**
     * Récupère les textes pour un niveau et une langue
     *
     * @param niveau
     * @param out
     * @param paraSep
     * @param langueIso
     */
    protected void dumpNiveau(int niveau, StringBuffer out, String paraSep, String langueIso) {
        try {
            for (Iterator paraIter = this.getCurrentDocument(getSession(), langueIso).getTextes(niveau).iterator(); paraIter
                    .hasNext();) {
                if (out.length() > 0) {
                    out.append(paraSep);
                }
                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.append(CAReferenceBVR.TEXTE_INTROUVABLE);
        }
    }

    /**
     * @return the session
     */
    public BSession getSession() {
        return session;
    }

    /**
     * @param session
     *            the session to set
     */
    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Renvoie la table des textes par langue.
     *
     * @return la table des textes par langue.
     */
    public Map getDocuments() {
        if (AbstractCAReference.documents == null) {
            AbstractCAReference.documents = new HashMap();
        }
        return AbstractCAReference.documents;
    }

    /**
     * Va chercher le numéro du compte dans Babel
     *
     * @author: sel Créé le : 28 nov. 06
     * @return le N° du compte (ex: 01-12345-1)
     * @throws Exception
     */
    public String getNumeroCC() throws Exception {
        String res = "";
        res = getTexteBabel(2, 1);
        return res;
    }

    /**
     * Récupère les textes du catalogue de texte
     *
     * @author: sel Créé le : 28 nov. 06
     * @param niveau
     * @param position
     * @return texte
     * @throws Exception
     */
    protected String getTexteBabel(int niveau, int position) throws Exception {
        StringBuffer resString = new StringBuffer("");
        if (this.getCurrentDocument() == null) {
            resString.append(TEXTE_INTROUVABLE);
        } else {
            ICTListeTextes listeTextes = this.getCurrentDocument().getTextes(niveau);
            resString.append(listeTextes.getTexte(position));
        }
        return resString.toString();
    }
}
