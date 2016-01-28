/**
 * class CPDecisionAnneeMultiple écrit le 19/01/05 par JPA
 * 
 * class entité
 * 
 * @author JPA
 **/
package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CPDecisionAnneeMultiple extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String dateDeb = "";
    private String dateFin = "";
    private String ebipas = "";
    private String idAffiliation = "";
    private String idPassage = "";
    private String idTiers = "";
    private String nom = "";
    private String numAffilie = "";
    private String numAvs = "";
    private TITiersViewBean tiers = null;
    private String typeDecision = "";

    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAffilie = statement.dbReadString("MALNAF");
        idTiers = statement.dbReadNumeric("HTITIE");
        numAvs = statement.dbReadString("HXNAVS");
        annee = statement.dbReadNumeric("IAANNE");
        typeDecision = statement.dbReadString("IATTDE");
        dateDeb = statement.dbReadDateAMJ("IADDEB");
        dateFin = statement.dbReadDateAMJ("IADFIN");
        ebipas = statement.dbReadString("EBIPAS");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateDeb() {
        return dateDeb;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getEbipas() {
        return ebipas;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNom() {
        try {
            // enregistrement déjà chargé ?
            if ((tiers == null) || (tiers.isNew())) {
                tiers = new globaz.pyxis.db.tiers.TITiersViewBean();
                tiers.setSession(getSession());
                if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                    tiers.setIdTiers(getIdTiers());
                    tiers.retrieve();
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        nom = tiers.getNom();
        return nom;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getTypeDecision() {
        try {
            typeDecision = globaz.phenix.translation.CodeSystem.getLibelle(getSession(), typeDecision);
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return typeDecision;
    }

    public void setAnnee(String string) {
        annee = string;
    }

    public void setDateDeb(String string) {
        dateDeb = string;
    }

    public void setDateFin(String string) {
        dateFin = string;
    }

    public void setEbipas(String string) {
        ebipas = string;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdPassage(String string) {
        idPassage = string;
    }

    public void setIdTiers(String string) {
        idTiers = string;
    }

    public void setNom(String string) {
        nom = string;
    }

    public void setNumAffilie(String string) {
        numAffilie = string;
    }

    public void setNumAvs(String string) {
        numAvs = string;
    }

    public void setTypeDecision(String string) {
        typeDecision = string;
    }

}