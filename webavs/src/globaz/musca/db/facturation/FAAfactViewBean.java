package globaz.musca.db.facturation;

import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.norma.db.fondation.PATraduction;
import globaz.norma.db.fondation.PATraductionManager;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CAReferenceRubriqueManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;

public class FAAfactViewBean extends FAAfact implements globaz.framework.bean.FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private java.lang.String action = null;
    private java.lang.String idCompteAnnexe = "";
    private java.lang.String idExterneRubriqueCompen = null;
    private java.lang.String idTiersFacture = null;
    private java.lang.String libelleModule;

    private java.lang.String libelleRubriqueCompen = null;

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAAfact.TABLE_FIELDS + ",   PMTRADP.LIBELLE AS LIBELLERUB, CARUBRP.IDEXTERNE";
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "FAAFACP AS FAAFACP " + "LEFT JOIN " + _getCollection()
                + "CARUBRP AS CARUBRP ON (CARUBRP.IDRUBRIQUE=FAAFACP.IDRUBRIQUE) " + "INNER JOIN " + _getCollection()
                + "PMTRADP AS PMTRADP ON (PMTRADP.IDTRADUCTION= CARUBRP.IDTRADUCTION " + "AND PMTRADP.codeisolangue="
                + this._dbWriteString(statement.getTransaction(), getSession().getIdLangueISO()).toUpperCase() + ") "
                + "LEFT JOIN " + _getCollection() + "FAREMAP AS FAREMAP ON (FAAFACP.IDREMARQUE=FAREMAP.IDREMARQUE) ";
    }

    public java.lang.String getAction() {
        return action;
    }

    public String getCodeNumCaisse() {
        TIAdministrationViewBean admin = new TIAdministrationViewBean();
        admin.setSession(getSession());
        admin.setIdTiersAdministration(super.getNumCaisse());
        try {
            admin.retrieve();
        } catch (Exception e) {
            _addError(null, e.getMessage());
        }
        return admin.getCodeAdministration();
    }

    public java.lang.String getIdCompteAnnexe() {
        String idExterneRole = super.getIdExterneDebiteurCompensation();
        String idRole = super.getIdRoleDebiteurCompensation();
        if (!JadeStringUtil.isEmpty(idRole) && !JadeStringUtil.isEmpty(idExterneRole)) {
            CACompteAnnexe compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setSession(getSession());
            compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compteAnnexe.setIdExterneRole(idExterneRole);
            compteAnnexe.setIdRole(idRole);
            try {
                compteAnnexe.retrieve();
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
            setIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());

        }
        return idCompteAnnexe;
    }

    public java.lang.String getIdExterneRubriqueCompen() {
        if (JadeStringUtil.isEmpty(getIdExterneRubrique()) || JadeStringUtil.isBlank(getIdExterneRubrique())) {
            try {
                CARubrique rubrique = new CARubrique();
                CAReferenceRubriqueManager ref = new CAReferenceRubriqueManager();
                ref.setSession(getSession());
                ref.setForCodeReference(APIReferenceRubrique.RUBRIQUE_DE_LISSAGE);
                ref.find();
                if (ref.size() > 0) {
                    setIdExterneRubriqueCompen(((CAReferenceRubrique) ref.getFirstEntity()).getIdExterneRubrique());
                    PATraductionManager libelle = new PATraductionManager();
                    libelle.setSession(getSession());
                    rubrique.setSession(getSession());
                    rubrique.setIdRubrique(((CAReferenceRubrique) ref.getFirstEntity()).getIdRubrique());
                    rubrique.retrieve();
                    libelle.setForIdTraduction(rubrique.getIdTraduction());
                    libelle.find();
                    if (libelle.size() > 0) {
                        setLibelleRubriqueCompen(((PATraduction) libelle.getFirstEntity()).getLibelle());
                    }

                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        }
        return idExterneRubriqueCompen;
    }

    public java.lang.String getIdTiersFacture() {
        return idTiersFacture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.10.2002 12:01:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleModule() {
        return libelleModule;
    }

    /**
     * @return
     */
    public java.lang.String getLibelleRubriqueCompen() {
        return libelleRubriqueCompen;
    }

    public java.lang.String getNomDebiteurCompensation() {
        // Récupérer le tiers
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(getIdExterneDebiteurCompensation())) {
            return super.getNomTiers();
        } else {
            return "";
        }
    }

    public void setAction(java.lang.String newAction) {
        action = newAction;
    }

    public void setIdCompteAnnexe(java.lang.String idCompteAnnexe) {
        this.idCompteAnnexe = idCompteAnnexe;
    }

    /**
     * @param string
     */
    public void setIdExterneRubriqueCompen(java.lang.String string) {
        idExterneRubriqueCompen = string;
    }

    public void setIdTiersFacture(java.lang.String newIdTiersFacture) {
        idTiersFacture = newIdTiersFacture;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.10.2002 12:01:40)
     * 
     * @param newLibelleModule
     *            java.lang.String
     */
    public void setLibelleModule(java.lang.String newLibelleModule) {
        libelleModule = newLibelleModule;
    }

    /**
     * @param string
     */
    public void setLibelleRubriqueCompen(java.lang.String string) {
        libelleRubriqueCompen = string;
    }

}
