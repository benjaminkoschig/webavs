package globaz.hercule.service.comptesIndividuels;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.math.BigDecimal;

/**
 * Entité représentant un nombre de CI pour un affilié pour une année donnée
 * 
 * @author Sullivann Corneille
 * @since 12 février 2014
 */
public class CENombreCI extends BEntity {

    private static final long serialVersionUID = -8702439665110573067L;
    private String annee;
    private BigDecimal nombreCI = BigDecimal.ZERO;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        annee = statement.dbReadNumeric("annee");
        nombreCI = statement.dbReadBigDecimal("nombreCI");
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

    public BigDecimal getNombreCI() {
        return nombreCI;
    }

    public void setNombreCI(BigDecimal nombreCI) {
        this.nombreCI = nombreCI;
    }
}
