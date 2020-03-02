package ch.globaz.vulpecula.documents;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.sun.star.lang.NullPointerException;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.common.NumeroReference;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JABVR;
import globaz.globall.util.JACCP;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.itext.FAImpressionFacturation;

/**
 * Utilitaire permettant la génération des informations présentes sur un BVR
 *
 * @since WebBMS 0.01.03
 */
public class BulletinVersementReference {
    // Variables nécessaire pour récupérer le noAdhérent dans le catalogue de
    // texte
    private static Map<String, ICTDocument> documents = null;

    private static final String DOMAINE_FACTURATION = FAImpressionFacturation.DOMAINE_FACTURATION;

    public static final String OCRB_NON_FACTURABLE = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    public static final String REFERENCE_NON_FACTURABLE = "XXXXXXXXXXXXXXXXX";
    private static final String RETOUR_LIGNE = "\n";
    private static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";

    private static final String TYPE_FACTURE = FAImpressionFacturation.TYPE_FACTURE;
    private String ccp = null;
    private StringBuilder adresseBvr = null;
    private String ligneReference = REFERENCE_NON_FACTURABLE;

    private String ocrb = OCRB_NON_FACTURABLE;
    private BSession session = null;

    /**
     * Constructeur pour les types BVR 04 sans montant
     *
     * @param session
     * @param numReference
     * @throws Exception
     */
    public BulletinVersementReference(final BSession session, final NumeroReference numReference) throws Exception {
        this(session, numReference, new Montant(0));
    }

    /**
     * Constructeur pour les types BVR 01 avec montant
     *
     * @param session
     * @param numReference
     * @param montant
     * @throws Exception
     */
    public BulletinVersementReference(final BSession session, final NumeroReference numReference, final Montant montant)
            throws Exception {
        this(numReference, montant, null, null);
        this.session = session;
    }

    public BulletinVersementReference(final NumeroReference numReference, final Montant montant,
            AdresseTiersDetail adressePmt, String ccp) throws Exception {

        if (adressePmt != null && adressePmt.getFields() != null) {
            this.ccp = ccp;
            adresseBvr = new StringBuilder();

            if (isNotEmpty(adressePmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1))) {
                adresseBvr.append(adressePmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1));
                adresseBvr.append("\n");
                adresseBvr.append(adressePmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_NPA));
                adresseBvr.append(" ");
                adresseBvr.append(adressePmt.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_LOCALITE));

                adresseBvr.append("\n");
                adresseBvr.append("\n");
            }

            adresseBvr.append(adressePmt.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1));
            if (isNotEmpty(adressePmt.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2))) {
                adresseBvr.append(" ");
                adresseBvr.append(adressePmt.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2));
            }

            if (isNotEmpty(adressePmt.getFields().get(AdresseTiersDetail.ADRESSE_VAR_ATTENTION))) {
                adresseBvr.append("\n");
                adresseBvr.append(adressePmt.getFields().get(AdresseTiersDetail.ADRESSE_VAR_ATTENTION));
            }
            adresseBvr.append("\n");
            adresseBvr.append(adressePmt.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA));
            adresseBvr.append(" ");
            adresseBvr.append(adressePmt.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
        }

        initBVR(montant, numReference);
    }

    private boolean isNotEmpty(final String stringToTest) {
        return stringToTest != null && stringToTest.trim().length() > 0;
    }

    /**
     * Aucun contrôle de montant minime
     *
     * @param montant
     * @param numReference
     * @throws GlobazTechnicalException en cas de problème sur JABVR
     */
    private void initBVR(final Montant montant, final NumeroReference numReference) {
        try {
            if (!montant.isNegative()) {
                JABVR jadeBvr = new JABVR(montant.toString(), numReference.getValue(), getNoAdherent());

                ligneReference = jadeBvr.get_ligneReference();
                ocrb = jadeBvr.get_ocrb();
            }
        } catch (Exception e) {
            throw new GlobazTechnicalException(ExceptionMessage.ERREUR_TECHNIQUE, e);
        }
    }

    /**
     * @param langue
     * @return
     */
    public String getAdresseBVR(CodeLangue langue) {
        if (adresseBvr != null) {
            return adresseBvr.toString();
        }
        return getAdresseBvrCT(langue);
    }

    /**
     * Retourne l'adresse pour le BVR (va rechercher dans le catalogue de textes MUSCA)
     * Si la langueIso est null, prend la langueIso de la session
     *
     * @return l'adresse BVR présente dans le catalogue de textes la facturation (Musca)
     */
    public String getAdresseBvrCT(CodeLangue langue) {
        StringBuilder adresse = new StringBuilder("");
        try {
            // va rechercher les textes qui sont au niveau 1
            if (getCurrentDocument(langue.getCodeIsoLangue()) == null) {
                adresse.append(TEXTE_INTROUVABLE);
            } else {
                buildAdresse(langue, adresse);
            }
        } catch (Exception e3) {
            adresse.append(TEXTE_INTROUVABLE);
        }
        return adresse.toString();
    }

	/**
	 * @param langue
	 * @param adresse
	 */
	private void buildAdresse(CodeLangue langue, StringBuilder adresse) {
		try {
		    Iterator paraIter = getCurrentDocument(langue.getCodeIsoLangue()).getTextes(1).iterator();
		    while (paraIter.hasNext()) {
		        if (adresse.length() > 0) {
		            adresse.append(RETOUR_LIGNE);
		        }
		        adresse.append(((ICTTexte) paraIter.next()).getDescription());
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    adresse.append(TEXTE_INTROUVABLE);
		}
	}

    /**
     * Renvoie la liste des textes pour la langue de la session courante.
     *
     * @param session
     * @param _langueIso
     * @return la liste des textes pour la langue de la session courante, null si elle n'a pas été trouvée
     * @throws Exception
     */
    private ICTDocument getCurrentDocument(final String _langueIso) throws Exception {
        if (session == null) {
            session = BSessionUtil.getSessionFromThreadContext();
            if (session == null) {
                throw new NullPointerException("Session is null !");
            }
        }

        String langueIso = _langueIso;
        if (JadeStringUtil.isBlank(langueIso)) {
            langueIso = session.getIdLangueISO();
        }

        if (documents == null) {
            documents = new HashMap<String, ICTDocument>();
        }
        ICTDocument document = documents.get(langueIso);

        if (document == null) {
            ICTDocument apiDocument = (ICTDocument) session.getAPIFor(ICTDocument.class);
            apiDocument.setISession(session);
            apiDocument.setCsDomaine(DOMAINE_FACTURATION);
            apiDocument.setCsTypeDocument(TYPE_FACTURE);
            apiDocument.setDefault(true);
            apiDocument.setCodeIsoLangue(langueIso);
            apiDocument.setActif(true);

            ICTDocument[] docs = apiDocument.load();
            if ((docs != null) && (docs.length > 0)) {
                document = docs[0];
                documents.put(langueIso, document);
            }
        }
        return document;
    }

    /**
     * @return the ligneReference
     */
    public String getLigneReference() {
        return ligneReference;
    }

    /**
     * Va chercher le numéro de l'adherent dans Babel
     *
     * @author: sel Créé le : 28 nov. 06
     * @return le N° adherent (ex: 010123451)
     * @throws Exception
     */
    public String getNoAdherent() throws Exception {
        if (ccp != null) {
            return JACCP.formatNoDash(ccp);
        }
        return getTexteBabel(2, 2);
    }

    /**
     * Va chercher le numéro du compte dans Babel
     *
     * @author: sel Créé le : 28 nov. 06
     * @return le N° du compte (ex: 01-12345-1)
     * @throws Exception
     */
    public String getNumeroCC() throws Exception {
        if (ccp != null) {
            return ccp;
        }

        return getTexteBabel(2, 1);
    }

    /**
     * @return the ocrb
     */
    public String getOcrb() {
        return ocrb;
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
    private String getTexteBabel(final int niveau, final int position) throws Exception {
    	StringBuilder resString = new StringBuilder("");
        if (getCurrentDocument(null) == null) {
            resString.append(TEXTE_INTROUVABLE);
        } else {
            ICTListeTextes listeTextes = getCurrentDocument(null).getTextes(niveau);
            resString.append(listeTextes.getTexte(position));
        }
        return resString.toString();
    }

}