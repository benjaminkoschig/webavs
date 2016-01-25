/*
 * Created on 10.10.2007 JPA Classe JournalQuittance pour les bénéficiaires PC
 */
package globaz.naos.db.beneficiairepc;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TITiers;
import java.io.Serializable;

/**
 * La classe définissant l'entité Quittance (Bénéficiaires PC)
 * 
 * @author jpa
 */
public class AFImpressionQuittance extends BEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseEmail = "";
    private Boolean casExistant = Boolean.FALSE;
    private String dateEvaluation = "";
    private String heures = "";
    private String idAffiliation = "";
    private String idTiers = "";
    private String nbreQuittances = "";
    private String numAffilie = "";
    private String numAvs = "";
    private String user = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
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

    public String getAdresseEmail() {
        if (JadeStringUtil.isEmpty(adresseEmail)) {
            return getSession().getUserEMail();
        } else {
            return adresseEmail;
        }
    }

    public Boolean getCasExistant() {
        return casExistant;
    }

    public String getDateEvaluation() {
        return dateEvaluation;
    }

    public String getHeures() {
        return heures;
    }

    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getNbreQuittances() {
        return nbreQuittances;
    }

    public String getNomBenefJSP() {
        TITiers tiers = new TITiers();
        tiers.setSession(getSession());
        tiers.setIdTiers(getIdTiers());
        try {
            tiers.retrieve();
            return tiers.getNomPrenom();
        } catch (Exception e) {
            return "";
        }
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getUser() {
        if (JadeStringUtil.isEmpty(user)) {
            return getSession().getUserId();
        } else {
            return user;
        }
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public void setCasExistant(Boolean casExistant) {
        this.casExistant = casExistant;
    }

    public void setDateEvaluation(String dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

    public void setHeures(String heures) {
        this.heures = heures;
    }

    public void setIdAffiliation(String idAffiliation) {
        this.idAffiliation = idAffiliation;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNbreQuittances(String nbreQuittances) {
        this.nbreQuittances = nbreQuittances;
    }

    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    public void setNumAvs(String numAvs) {
        this.numAvs = numAvs;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
