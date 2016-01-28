package globaz.osiris.db.cumulcotisations;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author dda
 */
public class CACumulCotisationsParAnnee extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeCotisation;
    // CARUBRP variable
    private String idExterne;
    private String sumMasse;

    // CAOPERP variable
    private String sumMontant;

    /**
     * Return le nom de la table (CAOPERP).
     */
    @Override
    protected String _getTableName() {
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setSumMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
        setSumMasse(statement.dbReadNumeric(CAOperation.FIELD_MASSE));
        setAnneeCotisation(statement.dbReadNumeric(CAOperation.FIELD_ANNEECOTISATION));

        setIdExterne(statement.dbReadString(CARubrique.FIELD_IDEXTERNE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not needed here
    }

    public String getAnneeCotisation() {
        return anneeCotisation;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public String getSumMasse() {
        return sumMasse;
    }

    public String getSumMontant() {
        return sumMontant;
    }

    public void setAnneeCotisation(String anneeCotisation) {
        this.anneeCotisation = anneeCotisation;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public void setSumMasse(String sumMasse) {
        this.sumMasse = sumMasse;
    }

    public void setSumMontant(String sumMontant) {
        this.sumMontant = sumMontant;
    }

}
