/*
 * Créé le 2 mars 06
 */
package globaz.ij.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JAVector;
import globaz.ij.db.prononces.IJListeFormulairesNonRecus;
import globaz.ij.db.prononces.IJListeFormulairesNonRecusManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.enums.CommunePolitique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ch.globaz.common.properties.CommonProperties;

/**
 * @author hpe
 * 
 *         Création du document PDF Liste des formulaires non reçus pour un mois donné.
 */
public class IJListeAttestationsNonRecus extends FWIAbstractManagerDocumentList {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NO_REF_INFOROM = "5016PIJ";
    private String annee = "";

    private String dateDebutBaseIndemn = "";
    private String dateDebutPrononce = "";

    private String dateDocument = "";

    private String dateFinBaseIndemn = "";
    private String dateFinPrononce = "";

    private String derniereIdPrononce = null;

    private String mois = "";

    private String orderBy = "";

    private boolean ajouterCommunePolitique = false;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJListeAttestationsNonRecus.
     */
    public IJListeAttestationsNonRecus() {

        // session, prefix, Compagnie, Titre, manager, application
        super(null, "PRESTATIONS", "GLOBAZ", "Liste des attestations non reçues ",
                new IJListeFormulairesNonRecusManager(), "IJ");
    }

    /**
     * Crée une nouvelle instance de la classe IJListeAttestationsNonRecus.
     * 
     */
    public IJListeAttestationsNonRecus(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("ATTEST_NON_RECUES_TITLE"),
                new IJListeFormulairesNonRecusManager(), "IJ");
    }

    public IJListeAttestationsNonRecus(BSession session, String mois, String annee) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, "PRESTATIONS", "GLOBAZ", session.getLabel("ATTEST_NON_RECUES_TITLE") + " " + mois + " " + annee
                + "", new IJListeFormulairesNonRecusManager(), "IJ");
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * transfère des paramètres au manager;
     */
    @Override
    public void _beforeExecuteReport() {
        _setDocumentTitle(getSession().getLabel("ATTEST_NON_RECUES_TITLE") + " " + mois + " " + annee);

        getDocumentInfo().setDocumentTypeNumber(IJListeAttestationsNonRecus.NO_REF_INFOROM);
        IJListeFormulairesNonRecusManager manager = (IJListeFormulairesNonRecusManager) _getManager();
        manager.setSession(getSession());
        manager.setDateDebutPrononce(getDateDebutPrononce());
        manager.setDateFinPrononce(getDateFinPrononce());
        manager.setDateDebutBaseIndemn(getDateDebutBaseIndemn());
        manager.setDateFinBaseIndemn(getDateFinBaseIndemn());
        manager.setOrderBy(getOrderBy());

        try {
            if (manager.getCount(getTransaction()) == 0) {
                addRow(new IJListeFormulairesNonRecus());
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), "", "");
        }
        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

        try {
            ajouterCommunePolitique = CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue();
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), FWMessage.INFORMATION, this.getClass().getName());
        }

    }

    /*
     * Valide le contenu de l'entité (notamment les champs obligatoires)
     */
    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("EMAIL_VIDE"));
        } else {
            if (getEMailAddress().indexOf('@') == -1) {
                this._addError(getSession().getLabel("EMAIL_INVALIDE"));
            }
        }
        setControleTransaction(true);
        setSendCompletionMail(true);
        setSendMailOnError(true);
    }

    @Override
    protected void addRow(BEntity entity) throws FWIException {

        IJListeFormulairesNonRecus formNonRecus = (IJListeFormulairesNonRecus) entity;

        if (!formNonRecus.getIdPrononce().equals(derniereIdPrononce)) {
            if (ajouterCommunePolitique) {
                _addCell(formNonRecus.getCommunePolitique());
                _addCell("");
            }
            _addCell(formNonRecus.getIdPrononce());
            _addCell("");

            PRTiersWrapper tier = null;

            try {
                tier = PRTiersHelper.getTiers(getSession(), formNonRecus.getNoAVSTiers());
            } catch (Exception e) {
                getSession().addError("Tiers introuvable par le n° AVS");
            }
            if (tier != null) {

                _addCell(formNonRecus.getNoAVSTiers() + " / " + formNonRecus.getNomTiers() + " "
                        + formNonRecus.getPrenomTiers() + " / "
                        + tier.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                        + getLibelleCourtSexe(tier.getProperty(PRTiersWrapper.PROPERTY_SEXE)) + " / "
                        + getLibellePays(tier.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
            } else {
                _addCell("");
            }

            _addCell("");
            _addCell(formNonRecus.getIdBaseIndemnisation());
            _addCell("");
            _addCell(formNonRecus.getDateEnvoiFormulaire());
            _addCell("");
            _addCell(formNonRecus.getNbreRappelFormulaire());
            _addCell("");
            _addCell(formNonRecus.getNomAgentExecution());
        }
    }

    public void afterBuildReport() {
        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IPRConstantesExternes.ATTESTATIONS_NON_RECUES_IJ);
    }

    /**
     * Remplit le header de page
     * 
     * @exception java.lang.Exception
     *                en cas s'erreur
     */
    @Override
    protected void bindPageHeader() throws Exception {

        this._addHeaderLine(getFontCompanyName(), _getCompanyName(), null, null, getFontDate(), getDateDocument());

        this._addHeaderLine(null, null, getFontDocumentTitle(), _getDocumentTitle(), null, null);

        if (ajouterCommunePolitique) {
            this._addHeaderLine(getFontDate(),
                    getSession().getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_UTILISATEUR.getKey()) + " : "
                            + getSession().getUserId(), null, null, null, null);

        }
    }

    /**
     * @return
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @return
     */
    public String getDateDebutBaseIndemn() {
        return dateDebutBaseIndemn;
    }

    /**
     * @return
     */
    public String getDateDebutPrononce() {
        return dateDebutPrononce;
    }

    /**
     * @return
     */
    public String getDateDocument() {
        return dateDocument;
    }

    /**
     * @return
     */
    public String getDateFinBaseIndemn() {
        return dateFinBaseIndemn;
    }

    /**
     * @return
     */
    public String getDateFinPrononce() {
        return dateFinPrononce;
    }

    /**
     * @return
     */
    public String getDerniereIdPrononce() {
        return derniereIdPrononce;
    }

    /*
     * Titre de l'email
     */
    /**
     * getter pour l'attribut EMail object
     * 
     * @return la valeur courante de l'attribut EMail object
     */
    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_FORMULAIRE_TITRE");
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est passé en paramètre
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe(String csSexe) {

        if (PRACORConst.CS_HOMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(csSexe)) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }

    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est passé en paramètre
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays(String csNationalite) {

        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", csNationalite)))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", csNationalite));
        }

    }

    /**
     * @return
     */
    public String getMois() {
        return mois;
    }

    /**
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    /*
     * Initialisation des colonnes et des groupes
     */
    /**
	 */
    @Override
    protected void initializeTable() {

        if (ajouterCommunePolitique) {
            IJListeFormulairesNonRecusManager manager = (IJListeFormulairesNonRecusManager) _getManager();
            Set<String> setIdTiers = new HashSet<String>();
            Map<String, String> mapIdTiersCommunePolitique = new HashMap<String, String>();

            for (int i = 0; i < manager.size(); i++) {
                IJListeFormulairesNonRecus formNonRecus = (IJListeFormulairesNonRecus) manager.getEntity(i);
                if (!JadeStringUtil.isBlankOrZero(formNonRecus.getIdTiers())) {
                    setIdTiers.add(formNonRecus.getIdTiers());
                }

            }

            mapIdTiersCommunePolitique = PRTiersHelper.getCommunePolitique(setIdTiers, new Date(), getSession());

            JAVector vectorManagerContainer = _getManager().getContainer();
            for (Object aContainerElement : vectorManagerContainer) {
                IJListeFormulairesNonRecus formNonRecus = (IJListeFormulairesNonRecus) aContainerElement;

                String communePolitique = mapIdTiersCommunePolitique.get(formNonRecus.getIdTiers());
                if (!JadeStringUtil.isEmpty(communePolitique)) {
                    formNonRecus.setCommunePolitique(communePolitique);
                }
            }

            Collections.sort(vectorManagerContainer);

            this._addColumnCenter(getSession()
                    .getLabel(CommunePolitique.LABEL_COMMUNE_POLITIQUE_TITRE_COLONNE.getKey()), 5);
            this._addColumnCenter("", 5);

        }

        // colonnes
        this._addColumnRight(getSession().getLabel("LISTE_FORMULAIRE_NO_PRONONCE"), 10);
        this._addColumnCenter("", 5);
        this._addColumnLeft(getSession().getLabel("LISTE_FORMULAIRE_DETAIL_REQUERANT"), 30);
        this._addColumnCenter("", 5);
        this._addColumnRight(getSession().getLabel("LISTE_FORMULAIRE_NO_BASEIND"), 10);
        this._addColumnCenter("", 5);
        this._addColumnLeft(getSession().getLabel("LISTE_FORMULAIRE_DATEENVOI"), 10);
        this._addColumnCenter("", 5);
        this._addColumnRight(getSession().getLabel("LISTE_FORMULAIRE_NBRAPPEL"), 10);
        this._addColumnCenter("", 5);
        this._addColumnLeft(getSession().getLabel("LISTE_FORMULAIRE_ENVOYEA"), 20);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BProcess#jobQueue()
     */
    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    /**
     * @param string
     */
    public void setAnnee(String string) {
        annee = string;
    }

    /**
     * @param string
     */
    public void setDateDebutBaseIndemn(String string) {
        dateDebutBaseIndemn = string;
    }

    /**
     * @param string
     */
    public void setDateDebutPrononce(String string) {
        dateDebutPrononce = string;
    }

    /**
     * @param string
     */
    public void setDateDocument(String string) {
        dateDocument = string;
    }

    /**
     * @param string
     */
    public void setDateFinBaseIndemn(String string) {
        dateFinBaseIndemn = string;
    }

    /**
     * @param string
     */
    public void setDateFinPrononce(String string) {
        dateFinPrononce = string;
    }

    /**
     * @param string
     */
    public void setDerniereIdPrononce(String string) {
        derniereIdPrononce = string;
    }

    /**
     * @param string
     */
    public void setMois(String string) {
        mois = string;
    }

    /**
     * @param string
     */
    public void setOrderBy(String string) {
        orderBy = string;
    }

}
