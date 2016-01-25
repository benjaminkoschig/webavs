package globaz.corvus.itext;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeForPaiementMensuel;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeForPaiementMensuelManager;
import globaz.corvus.process.REGenererListesVerificationProcess;
import globaz.framework.printing.itext.dynamique.FWIAbstractManagerDocumentList;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * @author FGO
 */
public class REListeAdrPaiementRentesEnErreur extends FWIAbstractManagerDocumentList {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forMoisAnnee = "";

    public REListeAdrPaiementRentesEnErreur() {
        // session, prefix, Compagnie, Titre, manager, application
        super(null, null, "", "liste adresse paiement des rentes en erreur",
                new REPrestationAccordeeForPaiementMensuelManager(), REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    public REListeAdrPaiementRentesEnErreur(BSession session) {
        // session, prefix, Compagnie, Titre, manager, application
        super(session, null, "", session.getLabel("LISTE_ADR_DOM_ERR_TITRE"),
                new REPrestationAccordeeForPaiementMensuelManager(), REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    /**
     * transfère des paramètres au manager;
     */
    @Override
    public void _beforeExecuteReport() {

        try {
            // Création du manager
            REPrestationAccordeeForPaiementMensuelManager manager = (REPrestationAccordeeForPaiementMensuelManager) _getManager();
            manager.setForEnCoursAtMois(getForMoisAnnee());
            manager.setForTestAdressePmtOrDomaine(true);
            manager.setForTestCompteAnnexe(true);
            manager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + " , " + IREPrestationAccordee.CS_ETAT_PARTIEL
                    + " , " + IREPrestationAccordee.CS_ETAT_DIMINUE);
            manager.setSession(getSession());
            manager.setExclureMontantsNuls(true);
            manager.find(getTransaction(), BManager.SIZE_NOLIMIT);

            if (manager.size() == 0) {
                setSendMailOnError(false);
                setControleTransaction(false);
                setSendCompletionMail(false);
            }
        } catch (Exception e) {
            getMemoryLog().logMessage(e.getMessage(), getSession().getLabel("LISTE_ADR_DOM_ERR_ERREUR"),
                    FWServlet.ERROR, getSession().getLabel("LISTE_ADR_DOM_ERR_TITRE"));
        }

        getDocumentInfo().setDocumentProperty(REGenererListesVerificationProcess.PROPERTY_DOCUMENT_ORDER,
                REGenererListesVerificationProcess.LISTE_ADRESSE_PAIEMENT_ERREUR_ORDER);

        // on ajoute au doc info le numéro de référence inforom
        getDocumentInfo().setDocumentTypeNumber(IRENoDocumentInfoRom.LISTE_ADRESSES_DE_PAIEMENT_EN_ERREUR);

        _setCompanyName(FWIImportProperties.getInstance().getProperty(getDocumentInfo(),
                ACaisseReportHelper.JASP_PROP_NOM_CAISSE + getSession().getIdLangueISO().toUpperCase()));

        _setDocumentTitle(getSession().getLabel("LISTE_ADR_DOM_ERR_TITRE") + " " + getForMoisAnnee()
                + getSession().getLabel("LISTE_ADR_DOM_ERR_TITRE_02"));
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires)
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

    /**
     * Contenu des cellules
     */
    @Override
    protected void addRow(BEntity entity) throws FWIException {

        REPrestationAccordeeForPaiementMensuel ra = (REPrestationAccordeeForPaiementMensuel) entity;
        // Recherche des information sur le tiers beneficiaire
        PRTiersWrapper assure = null;
        try {
            assure = PRTiersHelper.getPersonneAVS(getSession(), ra.getIdTiersBeneficiaire());
        } catch (Exception e) {
            assure = null;
        }

        if (assure != null) {
            _addCell(assure.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL) + " / "
                    + assure.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + assure.getProperty(PRTiersWrapper.PROPERTY_PRENOM) + " / "
                    + assure.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE) + " / "
                    + getLibelleCourtSexe(assure.getProperty(PRTiersWrapper.PROPERTY_SEXE)) + " / "
                    + getLibellePays(assure.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE)));
        } else {
            _addCell("Tiers non-défini : idTiers = " + ra.getIdTiersBeneficiaire());
        }
        _addCell(ra.getIdPrestationAccordee());
        StringBuffer cellule = new StringBuffer();
        if (JadeNumericUtil.isEmptyOrZero(ra.getIdTiersAdressePmt())) {
            cellule.append(getSession().getLabel("LISTE_ADR_DOM_ERR_ERREUR_01"));
        }
        if (JadeNumericUtil.isEmptyOrZero(ra.getIdDomaineApplication())) {
            if (cellule.length() != 0) {
                cellule.append(" - ");
            }
            cellule.append(getSession().getLabel("LISTE_ADR_DOM_ERR_ERREUR_02"));
        } else if (JadeNumericUtil.isEmptyOrZero(ra.getIdAdrPmtRente())
                && JadeNumericUtil.isEmptyOrZero(ra.getIdAdrPmtStd())) {
            if (cellule.length() != 0) {
                cellule.append(" - ");
            }
            cellule.append(getSession().getLabel("LISTE_ADR_DOM_ERR_ERREUR_02"));
        }
        if (JadeNumericUtil.isEmptyOrZero(ra.getIdCompteAnnexe())) {
            if (cellule.length() != 0) {
                cellule.append(" - ");
            }
            cellule.append(getSession().getLabel("LISTE_ADR_DOM_ERR_ERREUR_03"));
        }

        _addCell(cellule.toString());

    }

    /**
     * Méthode pour charger une ligne vide avec le message : Aucune erreur pour la période : mm.aaaa
     * 
     * @throws FWIException
     */
    protected void addRowVoid() throws FWIException {
        StringBuffer cellule = new StringBuffer();
        cellule.append(getSession().getLabel("LISTE_ERR_AUCUNE_ERREUR") + getForMoisAnnee());
        _addCell(cellule.toString());
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("LISTE_ADR_DOM_ERR_TITRE");
    }

    public String getForMoisAnnee() {
        return forMoisAnnee;
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
     * Initialisation des colonnes et des groupes
     */
    @Override
    protected void initializeTable() {
        // colonnes
        this._addColumnLeft(getSession().getLabel("LISTE_ERR_DETAIL_BENEFICIAIRE"), 30);
        this._addColumnLeft(getSession().getLabel("LISTE_ERR_NO_RA"), 10);
        this._addColumnLeft(getSession().getLabel("LISTE_ERR_GENRE_ERREUR"), 60);

    }

    @Override
    protected void summary() throws FWIException {
        // Si aucune donnée, on insert une ligne avec message "liste vide".
        if (_getManager().size() == 0) {
            addRowVoid();
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setForMoisAnnee(String newForMoisAnnee) {
        forMoisAnnee = newForMoisAnnee;
    }
}
