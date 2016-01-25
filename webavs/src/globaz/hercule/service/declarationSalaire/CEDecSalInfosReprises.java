package globaz.hercule.service.declarationSalaire;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.math.BigDecimal;

/**
 * Entitö représentant un
 * 
 * @author Sullivann Corneille
 * @since 20 février 2014
 */
public class CEDecSalInfosReprises extends BEntity {

    private static final long serialVersionUID = -6841630928655921027L;
    private String annee;
    private BigDecimal nbCI = BigDecimal.ZERO;
    private BigDecimal masseAvs = BigDecimal.ZERO;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        annee = statement.dbReadNumeric("ANNEE");
        nbCI = statement.dbReadBigDecimal("NB_CI");
        masseAvs = statement.dbReadBigDecimal("MASSE_AVS");
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

    @Override
    protected boolean _allowAdd() {
        return false;
    }

    @Override
    protected boolean _allowDelete() {
        return false;
    }

    @Override
    protected boolean _allowUpdate() {
        return false;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public BigDecimal getNbCI() {
        return nbCI;
    }

    public void setNbCI(BigDecimal nbCI) {
        this.nbCI = nbCI;
    }

    public BigDecimal getMasseAvs() {
        return masseAvs;
    }

    public void setMasseAvs(BigDecimal masseAvs) {
        this.masseAvs = masseAvs;
    }
}
