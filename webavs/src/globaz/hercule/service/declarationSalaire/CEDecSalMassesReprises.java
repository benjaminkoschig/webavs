package globaz.hercule.service.declarationSalaire;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.math.BigDecimal;

/**
 * Entité représentant les masses reprises pour un contrôle employeur
 * 
 * @author Sullivann Corneille
 * @since 21 févr. 2014
 */
public class CEDecSalMassesReprises extends BEntity {

    private static final long serialVersionUID = 926865535562941438L;
    private BigDecimal masseAvs = BigDecimal.ZERO;
    private BigDecimal masseAC1 = BigDecimal.ZERO;
    private BigDecimal masseAC2 = BigDecimal.ZERO;
    private BigDecimal masseAF = BigDecimal.ZERO;
    private BigDecimal nbReprise = BigDecimal.ZERO;

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        masseAvs = statement.dbReadBigDecimal("MASSE_AVS");
        masseAC1 = statement.dbReadBigDecimal("MASSE_AC1");
        masseAC2 = statement.dbReadBigDecimal("MASSE_AC2");
        masseAF = statement.dbReadBigDecimal("MASSE_AF");
        nbReprise = statement.dbReadBigDecimal("NB_REPRISE");
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

    /**
     * Getter de masseAvs
     * 
     * @return the masseAvs
     */
    public BigDecimal getMasseAvs() {
        return masseAvs;
    }

    /**
     * Setter de masseAvs
     * 
     * @param masseAvs the masseAvs to set
     */
    public void setMasseAvs(BigDecimal masseAvs) {
        this.masseAvs = masseAvs;
    }

    /**
     * Getter de masseAC1
     * 
     * @return the masseAC1
     */
    public BigDecimal getMasseAC1() {
        return masseAC1;
    }

    /**
     * Setter de masseAC1
     * 
     * @param masseAC1 the masseAC1 to set
     */
    public void setMasseAC1(BigDecimal masseAC1) {
        this.masseAC1 = masseAC1;
    }

    /**
     * Getter de masseAC2
     * 
     * @return the masseAC2
     */
    public BigDecimal getMasseAC2() {
        return masseAC2;
    }

    /**
     * Setter de masseAC2
     * 
     * @param masseAC2 the masseAC2 to set
     */
    public void setMasseAC2(BigDecimal masseAC2) {
        this.masseAC2 = masseAC2;
    }

    /**
     * Getter de masseAF
     * 
     * @return the masseAF
     */
    public BigDecimal getMasseAF() {
        return masseAF;
    }

    /**
     * Setter de masseAF
     * 
     * @param masseAF the masseAF to set
     */
    public void setMasseAF(BigDecimal masseAF) {
        this.masseAF = masseAF;
    }

    /**
     * Getter de nbReprise
     * 
     * @return the nbReprise
     */
    public BigDecimal getNbReprise() {
        return nbReprise;
    }

    /**
     * Setter de nbReprise
     * 
     * @param nbReprise the nbReprise to set
     */
    public void setNbReprise(BigDecimal nbReprise) {
        this.nbReprise = nbReprise;
    }

}
