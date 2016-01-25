/*
 * Créé le 9 janv. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.ventilation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CARubriqueManager;
import globaz.osiris.utils.CAUtil;
import java.math.BigDecimal;

/**
 * @author jmc
 */
public class CAVPTypeDeProcedureOrdre extends BEntity {
    private static final long serialVersionUID = -2072667983418338340L;
    public static final String TYPE_PROCEDURE_MONTANT_SIMPLE = "239003";
    public static final String TYPE_PROCEDURE_PART_EMPLOYEUR = "239001";
    public static final String TYPE_PROCEDURE_PART_SALARIE = "239002";
    private String idRubrique = "";
    private String idRubriqueIrrecouvrable = "";
    private String idRubriqueRecouvrement = "";
    private String idRubriqueSpecialeAssociee = "";
    private String numeroRubrique = "";
    private String numeroRubriqueIrrec = "";
    private String numeroRubriqueRecouvrement = "";
    private String numeroRubriqueSpecialeAssociee = "";
    private String ordre = "";
    private Boolean penal = new Boolean(false);
    private String rubriqueOrdreColonne = "";

    private String typeOrdre = "";
    private String typeProcedure = "";
    private String typeProcedureId = "";

    /**
	 *
	 */
    public CAVPTypeDeProcedureOrdre() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        if (JadeStringUtil.isIntegerEmpty(typeProcedureId)) {
            setTypeProcedureId(this._incCounter(transaction, "0"));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "CAOCCP";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        typeProcedureId = statement.dbReadNumeric("BSID");
        idRubrique = statement.dbReadNumeric("AGID");
        typeProcedure = statement.dbReadNumeric("BSTPRO");
        typeOrdre = statement.dbReadNumeric("BSTPOR");
        ordre = statement.dbReadNumeric("BSMORD");
        penal = statement.dbReadBoolean("BSBPEN");
        idRubriqueIrrecouvrable = statement.dbReadNumeric("AGIC");
        idRubriqueRecouvrement = statement.dbReadNumeric("AGIR");
        idRubriqueSpecialeAssociee = statement.dbReadNumeric("AGII");
        rubriqueOrdreColonne = statement.dbReadNumeric("BSMORI");
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Retrouver l'id de la rubrique
        if (!JadeStringUtil.isBlank(numeroRubrique)) {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(getSession());
            mgr.setForIdExterne(numeroRubrique);
            mgr.find();
            if (mgr.size() != 0) {
                idRubrique = ((CARubrique) mgr.getFirstEntity()).getIdRubrique();
            }
        } else {
            idRubrique = "";
        }
        if (!JadeStringUtil.isBlank(numeroRubriqueIrrec)) {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(getSession());
            mgr.setForIdExterne(numeroRubriqueIrrec);
            mgr.find();
            if (mgr.size() != 0) {
                CARubrique rubriqueIrrec = ((CARubrique) mgr.getFirstEntity());
                idRubriqueIrrecouvrable = rubriqueIrrec.getIdRubrique();
                // Contrôle que la nature de rubrique soit "Amortissement"
                if (!APIRubrique.AMORTISSEMENT.equals(rubriqueIrrec.getNatureRubrique())) {
                    statement.getTransaction().addErrors(getSession().getLabel("NATURE_RUBR_DOIT_ETRE_AMORTISSEMENT"));
                }
            }
        } else {
            idRubriqueIrrecouvrable = "";
        }
        if (!JadeStringUtil.isBlank(numeroRubriqueRecouvrement)) {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(getSession());
            mgr.setForIdExterne(numeroRubriqueRecouvrement);
            mgr.find();
            if (mgr.size() != 0) {
                CARubrique rubriqueRecouvrement = ((CARubrique) mgr.getFirstEntity());
                idRubriqueRecouvrement = rubriqueRecouvrement.getIdRubrique();
                // Contrôle que la nature de rubrique soit "Recouvrement"
                if (!APIRubrique.RECOUVREMENT.equals(rubriqueRecouvrement.getNatureRubrique())) {
                    statement.getTransaction().addErrors(getSession().getLabel("NATURE_RUBR_DOIT_ETRE_RECOUVREMENT"));
                }
            }
        } else {
            idRubriqueRecouvrement = "";
        }
        if (!JadeStringUtil.isBlank(numeroRubriqueSpecialeAssociee)) {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(getSession());
            mgr.setForIdExterne(numeroRubriqueSpecialeAssociee);
            mgr.find();
            if (mgr.size() != 0) {
                CARubrique rubriqueSpecialeAssociee = ((CARubrique) mgr.getFirstEntity());
                idRubriqueSpecialeAssociee = rubriqueSpecialeAssociee.getIdRubrique();
            }
        } else {
            idRubriqueSpecialeAssociee = "";
        }
        // plausi
        if (JadeStringUtil.isBlank(idRubrique)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_RUB"));
        }
        if (JadeStringUtil.isBlank(typeProcedure)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_PROC"));
        }
        if (JadeStringUtil.isBlank(ordre)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_ORDRE"));
        }
        if (JadeStringUtil.isBlank(typeOrdre)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_TYPE_ORDRE"));
        }
        if (existeOrdreSemblable()) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_PLA_UNIQUE"));
        }
        if ((new BigDecimal(ordre).compareTo(new BigDecimal(0)) < 0)
                || (new BigDecimal(ordre).compareTo(new BigDecimal(10)) > 0)) {
            statement.getTransaction().addErrors(getSession().getLabel("VENTILATION_ORDRE"));
        }
        if (JadeStringUtil.isBlankOrZero(idRubriqueIrrecouvrable)) {
            statement.getTransaction().addErrors(getSession().getLabel("RUBRIQUE_AMORTISSEMENT_NON_RENSEIGNEE"));
        } else {
            // Une rubrique irrecouvrable ne peut être à la fois simple et salarié / employeur
            if (!CAUtil.isParamRubriqueIrrecTypeOrdreOk(this, getSession())) {
                statement.getTransaction().addErrors(
                        getSession().getLabel("IRRECOUVRABLE_PARAMETRAGE_TYPE_SAL_EMPL_SIMPLE_ERREUR")
                                + getNumeroRubriqueIrrec());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("BSID", this._dbWriteNumeric(statement.getTransaction(), getTypeProcedureId(), ""));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("BSID",
                this._dbWriteNumeric(statement.getTransaction(), getTypeProcedureId(), "typeProcedureId"));
        statement.writeField("AGID", this._dbWriteNumeric(statement.getTransaction(), getIdRubrique(), "idRubrique"));
        statement.writeField("BSTPOR", this._dbWriteNumeric(statement.getTransaction(), getTypeOrdre(), "typeOrdre"));
        statement.writeField("BSTPRO",
                this._dbWriteNumeric(statement.getTransaction(), getTypeProcedure(), "typeProcedure"));
        statement.writeField("BSMORD", this._dbWriteNumeric(statement.getTransaction(), getOrdre(), "ordre"));
        statement.writeField("BSBPEN",
                this._dbWriteBoolean(statement.getTransaction(), isPenal(), BConstants.DB_TYPE_BOOLEAN_CHAR));
        statement.writeField("AGIC", this._dbWriteNumeric(statement.getTransaction(), getIdRubriqueIrrecouvrable(),
                "idRubriqueIrrecouvrable"));
        statement
                .writeField("AGIR", this._dbWriteNumeric(statement.getTransaction(), getIdRubriqueRecouvrement(),
                        "idRubriqueRecouvrement"));
        statement.writeField("AGII", this._dbWriteNumeric(statement.getTransaction(), getIdRubriqueSpecialeAssociee(),
                "idRubriqueSpecialeAssociee"));
        statement.writeField("BSMORI",
                this._dbWriteNumeric(statement.getTransaction(), getRubriqueOrdreColonne(), "idRubriqueOrdre"));
    }

    public boolean existeOrdreSemblable() {
        try {
            CAVPTypeDeProcedureOrdreManager mgr = new CAVPTypeDeProcedureOrdreManager();
            mgr.setSession(getSession());
            mgr.setForTypeProcedure(getTypeProcedure());
            mgr.setForIdRubrique(getIdRubrique());
            mgr.setForTypeOrdre(getTypeOrdre());
            mgr.setForNotId(getTypeProcedureId());
            if (mgr.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * @return
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdRubriqueIrrecouvrable() {
        return idRubriqueIrrecouvrable;
    }

    public String getIdRubriqueRecouvrement() {
        return idRubriqueRecouvrement;
    }

    public String getLibelleRubrique() {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(getIdRubrique());
        try {
            rubrique.retrieve();
        } catch (Exception e) {
            return "n/a";
        }
        if (!rubrique.isNew()) {
            return rubrique.getIdExterne() + " " + rubrique.getDescription(getSession().getIdLangueISO());
            // return rubrique.getIdExterneCompteCourantEcran();
        } else {
            return "n/a";
        }
    }

    /**
     * Permet de récupérer le label d'une rubrique en fonction de l'identifiant de la rubrique donnée en paramètre en
     * cas d'erreur cette methode retourne ""
     * 
     * @param idRubrique
     * @return libellé de la rubrique ou "" en cas d'erreur
     */
    public String getLibelleRubrique(String idRubrique) {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(idRubrique);
        try {
            rubrique.retrieve();
        } catch (Exception e) {
            return "";
        }
        if (!rubrique.isNew()) {
            return rubrique.getDescription(getSession().getIdLangueISO());
        } else {
            return "";
        }
    }

    public String getNumeroRubrique() {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(getIdRubrique());
        try {
            rubrique.retrieve();
        } catch (Exception e) {
            return "";
        }
        if (!rubrique.isNew()) {
            return rubrique.getIdExterne();
        } else {
            return "";
        }
    }

    public String getNumeroRubriqueIrrec() {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(getIdRubriqueIrrecouvrable());
        try {
            rubrique.retrieve();
        } catch (Exception e) {
            return "";
        }
        if (!rubrique.isNew()) {
            return rubrique.getIdExterne();
        } else {
            return "";
        }
    }

    public String getNumeroRubriqueRecouvrement() {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(getIdRubriqueRecouvrement());
        try {
            rubrique.retrieve();
        } catch (Exception e) {
            return "";
        }
        if (!rubrique.isNew()) {
            return rubrique.getIdExterne();
        } else {
            return "";
        }
    }

    public String getNumeroRubriqueSpecialeAssociee() {
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(getSession());
        rubrique.setIdRubrique(getIdRubriqueSpecialeAssociee());
        try {
            rubrique.retrieve();
        } catch (Exception e) {
            return "";
        }
        if (!rubrique.isNew()) {
            return rubrique.getIdExterne();
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getOrdre() {
        return ordre;
    }

    public String getRubriqueOrdreColonne() {
        return rubriqueOrdreColonne;
    }

    /**
     * @return
     */
    public String getTypeOrdre() {
        return typeOrdre;
    }

    /**
     * @return
     */
    public String getTypeProcedure() {
        return typeProcedure;
    }

    /**
     * @return
     */
    public String getTypeProcedureId() {
        return typeProcedureId;
    }

    /**
     * @return
     */
    public Boolean isPenal() {
        return penal;
    }

    /**
     * @param string
     */
    public void setIdRubrique(String string) {
        idRubrique = string;
    }

    public void setIdRubriqueIrrecouvrable(String string) {
        idRubriqueIrrecouvrable = string;
    }

    public void setIdRubriqueRecouvrement(String idRubriqueRecouvrement) {
        this.idRubriqueRecouvrement = idRubriqueRecouvrement;
    }

    /**
     * @param string
     */
    public void setIdTypeProcedure(String string) {
        typeProcedureId = string;
    }

    /**
     * @param string
     */
    public void setNumeroRubrique(String string) {
        numeroRubrique = string;
    }

    /**
     * @param string
     */
    public void setNumeroRubriqueIrrec(String string) {
        numeroRubriqueIrrec = string;
    }

    /**
     * @param string
     */
    public void setNumeroRubriqueRecouvrement(String numeroRubriqueRecouvrement) {
        this.numeroRubriqueRecouvrement = numeroRubriqueRecouvrement;
    }

    /**
     * @param string
     */
    public void setOrdre(String string) {
        ordre = string;
    }

    /**
     * @param boolean1
     */
    public void setPenal(Boolean boolean1) {
        penal = boolean1;
    }

    public void setRubriqueOrdreColonne(String rubriqueOrdreColonne) {
        this.rubriqueOrdreColonne = rubriqueOrdreColonne;
    }

    /**
     * @param string
     */
    public void setTypeOrdre(String string) {
        typeOrdre = string;
    }

    /**
     * @param string
     */
    public void setTypeProcedure(String string) {
        typeProcedure = string;
    }

    /**
     * @param string
     */
    public void setTypeProcedureId(String string) {
        typeProcedureId = string;
    }

    /**
     * @return String
     */
    public String getIdRubriqueSpecialeAssociee() {
        return idRubriqueSpecialeAssociee;
    }

    /**
     * @param idRubriqueSpecialeAssociee the idRubriqueSpecialeAssociee to set
     */
    public void setIdRubriqueSpecialeAssociee(String idRubriqueSpecialeAssociee) {
        this.idRubriqueSpecialeAssociee = idRubriqueSpecialeAssociee;
    }

    public void setNumeroRubriqueSpecialeAssociee(String numeroRubriqueSpecialeAssociee) {
        this.numeroRubriqueSpecialeAssociee = numeroRubriqueSpecialeAssociee;
    }
}
