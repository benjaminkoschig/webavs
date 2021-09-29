package ch.globaz.common.document.reference;

import ch.globaz.common.exceptions.CommonBusinessException;
import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.babel.api.ICTDocument;
import globaz.babel.api.ICTListeTextes;
import globaz.babel.api.ICTTexte;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.itext.FAImpressionFacturation;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.parser.IntReferenceBVRParser;
import globaz.pyxis.db.tiers.TITiers;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public abstract class AbstractReference {

    public static final String REFERENCE_NON_FACTURABLE = "XXXXXXXXXXXXXXXXX";
    public static final String TEXTE_INTROUVABLE = "[TEXTE INTROUVABLE]";
    public static final String RETOUR_LIGNE = "\n";

    // Variables nécessaire pour récupérer le noAdhérent dans le catalogue de texte
    public static final String IDENTIFIANT_REF_IDCOMPTEANNEXE = "99";

    private static final String DOMAINE_FACTURATION = FAImpressionFacturation.DOMAINE_FACTURATION;
    private static final String TYPE_FACTURE = FAImpressionFacturation.TYPE_FACTURE;

    private static final Logger LOGGER = Logger.getLogger(AbstractReference.class);

    private static final int MAX_LENGTH_NUM_AFFILIE = 13;
    private static final int MAX_LENGTH_REFERENCE = 26; // +1 du modulo de contrôle = les 27 positions

    private static int lengthIdRole = Integer.MIN_VALUE;
    private static int lengthRefDebiteur = Integer.MIN_VALUE;
    private static int lengthRefFacture = Integer.MIN_VALUE;
    private static int lengthTypeFacture = Integer.MIN_VALUE;

    private String ligneReference;
    private BSession session;
    private Map documents;

    public AbstractReference(){
        ligneReference = AbstractReference.REFERENCE_NON_FACTURABLE;
        session = null;
        documents = null;
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


    protected TITiers getTiers(String idTiers) {
        TITiers tiers = null;
        try {
            tiers = new TITiers();
            tiers.setSession(getSession());
            tiers.setIdTiers(idTiers);
            tiers.retrieve();
        } catch (Exception exception) {
            LOG.error("Impossible de récupérer le tiers avec l'idTiers : {}", idTiers, exception);
        }
        return tiers;
    }

    /**
     * Retourne l'adresse pour le BVR (va rechercher dans le catalogue de textes MUSCA)
     *
     * @return
     */
    public String getAdresse() {
        StringBuilder adresse = new StringBuilder();
        try {
            // va rechercher les textes qui sont au niveau 1
            if (this.getCurrentDocument() == null) {
                adresse.append(AbstractReference.TEXTE_INTROUVABLE);
            } else {
                this.dumpNiveau(1, adresse, AbstractReference.RETOUR_LIGNE);
            }
        } catch (Exception e3) {
            adresse.append(AbstractReference.TEXTE_INTROUVABLE);
        }
        return adresse.toString();
    }

    /**
     * Retourne l'adresse pour le BVR (va rechercher dans le catalogue de textes MUSCA)
     *
     * @return
     */
    public String getAdresse(String langueIso) {
        StringBuilder adresse = new StringBuilder();
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
            apiDocument.setDefault(Boolean.TRUE);
            apiDocument.setActif(Boolean.TRUE);
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
            apiDocument.setDefault(Boolean.TRUE);
            apiDocument.setCodeIsoLangue(langueIso);
            apiDocument.setActif(Boolean.TRUE);
            ICTDocument[] docs = apiDocument.load();
            if ((docs != null) && (docs.length > 0)) {
                document = docs[0];
                getDocuments().put(langueIso, document);
            }
        }
        return document;
    }

    /**
     * Permet de générer un numéro de référence.
     * Actuellement le numéro de référence d'un BVR et d'une QR-facture sont identiques.
     *
     * @param idRole
     * @param idExterneRole
     * @param isPlanPaiement
     * @param typeSection
     * @param idExterneSection
     * @param idCompteAnnexe
     * @return
     * @throws Exception
     */
    protected String genererNumReference(String idRole, String idExterneRole, boolean isPlanPaiement,
                                          String typeSection, String idExterneSection, String idCompteAnnexe) throws Exception {
        // Valeur forcée dans les paramètres pour idRole
        int valIdRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                IntReferenceBVRParser.VAL_ID_ROLE));

        String idExterneRoleUnformatted = removeNotLetterNotDigit(idExterneRole);
        String role = JadeStringUtil.rightJustifyInteger(idRole, getLengthIdRole());
        String refDebiteur = JadeStringUtil.rightJustifyInteger(idExterneRoleUnformatted, getLengthRefDebiteur());
        String refFacture = JadeStringUtil.rightJustifyInteger(idExterneSection, getLengthRefFacture());
        String idTypeFacture;

        // idRole sur 2 positions
        if (role.length() > getLengthIdRole()) {
            role = role.substring(4, 6);
        }

        // Si l'id du rôle est forcé
        if (valIdRole != 0) {
            role = JadeStringUtil.rightJustifyInteger(String.valueOf(valIdRole), getLengthIdRole());
        }

        if (isPlanPaiement) {
            idTypeFacture = JadeStringUtil.rightJustifyInteger(
                    CAApplication.getApplicationOsiris().getProperty(IntReferenceBVRParser.IDENT_PLAN, ""),
                    getLengthTypeFacture());
        } else {
            idTypeFacture = JadeStringUtil.rightJustifyInteger(typeSection, getLengthTypeFacture());
        }

        // Si le numéro d'affilié non formatté comporte plus de 13 caractères, on utilise l'id du compte annexe pour le
        // numéro de référence. On utilise le numéro 99 à la place du rôle pour indiquer que le numéro de référence
        // utilise l'idCompteAnnexe au lieu du numéro d'affilié
        if (refDebiteur.length() > MAX_LENGTH_NUM_AFFILIE) {
            role = IDENTIFIANT_REF_IDCOMPTEANNEXE;
            if (idCompteAnnexe == null) {
                CACompteAnnexe compteAnnexe = findCompteAnnexeForIdRoleAndIdExterneRole(idRole, idExterneRole);
                refDebiteur = compteAnnexe.getIdCompteAnnexe();
            } else {
                refDebiteur = idCompteAnnexe;
            }
        }

        StringBuilder ref = new StringBuilder();
        ref.append(role);
        ref.append(JadeStringUtil.rightJustifyInteger(refDebiteur, getLengthRefDebiteur()));
        ref.append(idTypeFacture);
        ref.append(refFacture); // idPlan = reference facture

        if (ref.length() > MAX_LENGTH_REFERENCE) {
            throw new CommonTechnicalException(AbstractReference.class.getName() + ": " + getSession().getLabel("ERREUR_REFERENCEBVR"));
        }

        return ref.toString();
    }

    /**
     * Supprime tout caractère qui n'est pas une lettre ou un chiffre
     *
     * @author: sel Créé le : 19 déc. 06
     * @param val
     * @return une chaine contenant que des lettres et des chiffres
     */
    protected String removeNotLetterNotDigit(String val) {
        StringBuilder strBuf = new StringBuilder();
        for (int i = 0; i < val.length(); i++) {
            char c = val.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                strBuf.append(c);
            }
        }
        return strBuf.toString();
    }

    /**
     * @author: sel Créé le : 18 déc. 06
     * @return la longueur de id role
     */
    private static int getLengthIdRole() {
        if (lengthIdRole == Integer.MIN_VALUE) {
            lengthIdRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_ROLE));
        }
        return lengthIdRole;
    }

    /**
     * @author: sel Créé le : 18 déc. 06
     * @return la longueur de l'id externe role
     */
    private static int getLengthRefDebiteur() {
        if (lengthRefDebiteur == Integer.MIN_VALUE) {
            lengthRefDebiteur = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_EXTERNE_ROLE));
        }
        return lengthRefDebiteur;
    }

    /**
     * @author: sel Créé le : 18 déc. 06
     * @return la longueur de l'id plan
     */
    private static int getLengthRefFacture() {
        if (lengthRefFacture == Integer.MIN_VALUE) {
            lengthRefFacture = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_PLAN));
        }
        return lengthRefFacture;
    }

    /**
     * @author: sel Créé le : 18 déc. 06
     * @return la longueur de type plan
     */
    private static int getLengthTypeFacture() {
        if (lengthTypeFacture == Integer.MIN_VALUE) {
            lengthTypeFacture = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_TYPE_PLAN));
        }
        return lengthTypeFacture;
    }

    /**
     * Retourne le compte annexe correspondant à l'idRole et l'idExterneRole
     *
     * @param idRole
     * @param idExterneRole
     * @return
     * @throws Exception si aucun résultat
     */
    private CACompteAnnexe findCompteAnnexeForIdRoleAndIdExterneRole(String idRole, String idExterneRole)
            throws Exception {
        CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
        compteAnnexeManager.setSession(getSession());
        compteAnnexeManager.setForIdRole(idRole);
        compteAnnexeManager.setForIdExterneRole(idExterneRole);
        compteAnnexeManager.find(BManager.SIZE_NOLIMIT);

        if (compteAnnexeManager.size() > 0) {
            return (CACompteAnnexe) compteAnnexeManager.getFirstEntity();
        } else {
            throw new CommonBusinessException(ReferenceBVR.class.getName() + ": unable to find compte annexe for idRole" + idRole
                    + " and idExterneRole" + idExterneRole);
        }
    }


    /**
     * Récupère les textes pour un niveau
     *
     * @param niveau
     * @param out
     * @param paraSep
     */
    protected void dumpNiveau(int niveau, StringBuilder out, String paraSep) {
        try {
            for (Iterator paraIter = this.getCurrentDocument().getTextes(niveau).iterator(); paraIter.hasNext();) {
                if (out.length() > 0) {
                    out.append(paraSep);
                }
                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la récupération du document courant",e);
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
    protected void dumpNiveau(int niveau, StringBuilder out, String paraSep, String langueIso) {
        try {
            for (Iterator paraIter = this.getCurrentDocument(getSession(), langueIso).getTextes(niveau).iterator(); paraIter
                    .hasNext();) {
                if (out.length() > 0) {
                    out.append(paraSep);
                }
                out.append(((ICTTexte) paraIter.next()).getDescription());
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors de la récupération du document courant",e);
            out.append(TEXTE_INTROUVABLE);
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
        if (documents == null) {
            documents = new HashMap();
        }
        return documents;
    }

    /**
     * Va chercher le numéro du compte dans Babel
     *
     * @author: sel Créé le : 28 nov. 06
     * @return le N° du compte (ex: 01-12345-1)
     * @throws Exception
     */
    public String getNumeroCC() throws Exception {
        return getTexteBabel(2, 1);
    }

    /**
     * Va chercher le numéro de l'adherent dans Babel
     *
     * @author: sel Créé le : 28 nov. 06
     * @return le N° adherent (ex: 010123451)
     * @throws Exception
     */
    public String getNoAdherent() throws Exception {
        return  getTexteBabel(2, 2);
    }

    /**
     * Va chercher le numéro de l'adherent dans Babel
     *
     * @author: sel Créé le : 28 nov. 06
     * @return le N° adherent (ex: 010123451)
     * @throws Exception
     */
    public String getNoAdherentBMS() throws Exception {
        return getTexteBabel(2, 2);
    }

    /**
     * Va chercher le code pays dans babel
     *
     * @author: sel Créé le : 28 nov. 06
     * @return le N° adherent (ex: 010123451)
     * @throws Exception
     */
    public String getCodePays() throws Exception {
        return  getTexteBabel(3, 1);
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
    public String getTexteBabel(int niveau, int position) throws Exception {
        StringBuilder resString = new StringBuilder();
        if (this.getCurrentDocument() == null) {
            resString.append(TEXTE_INTROUVABLE);
        } else {
            ICTListeTextes listeTextes = this.getCurrentDocument().getTextes(niveau);
            resString.append(listeTextes.getTexte(position));
        }
        return resString.toString();
    }

}
