/**
 * class CPDecisionsAPrendre écrit le 19/01/05 par JPA
 * 
 * class entité pour les décisions à prendre càd les affilies qui n'ont pas de décision définitive pour une certaine
 * année
 * 
 * @author JPA
 **/
package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class CPDecisionNonDefinitives extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = "";
    private String idTiers = "";
    private String nomPrenom = "";
    private String numAffilie = "";
    private String numAvs = "";
    private String periodeDeb = "";
    private String periodeFin = "";
    private String specification = "";
    private String typeDecision = "";

    @Override
    protected String _getTableName() {
        return "";
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        numAffilie = statement.dbReadString("MALNAF");
        idTiers = statement.dbReadString("HTITIE");
        annee = statement.dbReadString("IAANNE");
        periodeDeb = statement.dbReadDateAMJ("IADDEB");
        periodeFin = statement.dbReadDateAMJ("IADFIN");
        typeDecision = statement.dbReadString("IATTDE");
        numAvs = statement.dbReadString("HXNAVS");
        specification = statement.dbReadString("IATSPE");
        nomPrenom = statement.dbReadString("HTLDE1") + " " + statement.dbReadString("HTLDE2");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        if (!JadeStringUtil.isBlank(getAnnee())) {
            _addError(statement.getTransaction(), "l'année doit être renseignée");
        }
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

    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    public String getNumAffilie() {
        return numAffilie;
    }

    public String getNumAvs() {
        return numAvs;
    }

    public String getPeriodeDeb() {
        return periodeDeb;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public String getSpecification() {
        return specification;
    }

    public String getTypeDecision() {
        return typeDecision;
    }

}
