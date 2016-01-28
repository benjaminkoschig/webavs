package globaz.hercule.service.comptabilite;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.math.BigDecimal;

/**
 * <pre>
 * Entity représentant la masse salariale
 * pour un affilié pour une année
 * </pre>
 * 
 * @author Sullivann Corneille
 * @since 12 février 2014
 */
public class CEMassesSalariales extends BEntity {

    private static final long serialVersionUID = -4633872665382360593L;
    private String annee;
    private BigDecimal masse = BigDecimal.ZERO;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        annee = statement.dbReadNumeric("annee");
        masse = statement.dbReadBigDecimal("cumulMasse");
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

    public BigDecimal getMasse() {
        return masse;
    }

    public void setMasse(BigDecimal masse) {
        this.masse = masse;
    }

}
