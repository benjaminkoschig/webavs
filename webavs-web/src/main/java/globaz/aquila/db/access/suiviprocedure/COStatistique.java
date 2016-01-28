/**
 *
 */
package globaz.aquila.db.access.suiviprocedure;

import globaz.aquila.api.ICOHistoriqueConstante;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

// select h.ofiseq, css.pcolut, h.odieta, cse.pcolut, count(*) nombre, sum(oemsol) montant, sum( cast( (CASE
// WHEN onlval is not null THEN onlval else '0' END) as numeric))

/**
 * @author sel
 * 
 */
public class COStatistique extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static String ETAPE_LIBELLE = "etapeLibelle";
    public final static String NB_PAR_ETAPE = "nbParEtape";
    public final static String SEQUENCE_LIBELLE = "sequenceLibelle";
    public final static String SUM_SOLDE = "sumSolde";
    public final static String SUM_TAXES = "sumTaxe";

    private String etapeLibelle = null;
    private String idEtape = null;
    private String idSequence = null;
    private String nbEtape = null;
    private String sequenceLibelle = null;
    private String sumSolde = null;
    private String sumTaxeFrais = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        // Multi Tables
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        etapeLibelle = statement.dbReadString(COStatistique.ETAPE_LIBELLE);
        idEtape = statement.dbReadString(ICOHistoriqueConstante.FNAME_ID_ETAPE);
        idSequence = statement.dbReadString(ICOHistoriqueConstante.FNAME_ID_SEQUENCE);
        nbEtape = statement.dbReadNumeric(COStatistique.NB_PAR_ETAPE);
        sequenceLibelle = statement.dbReadString(COStatistique.SEQUENCE_LIBELLE);
        sumSolde = statement.dbReadString(COStatistique.SUM_SOLDE);
        sumTaxeFrais = statement.dbReadString(COStatistique.SUM_TAXES);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nope
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // nope
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // nope
    }

    /**
     * @return the etapeLibelle
     */
    public String getEtapeLibelle() {
        return etapeLibelle;
    }

    /**
     * @return the idEtape
     */
    public String getIdEtape() {
        return idEtape;
    }

    /**
     * @return the idSequence
     */
    public String getIdSequence() {
        return idSequence;
    }

    /**
     * @return the nbEtape
     */
    public String getNbEtape() {
        return nbEtape;
    }

    /**
     * @return the sequenceLibelle
     */
    public String getSequenceLibelle() {
        return sequenceLibelle;
    }

    /**
     * @return the sumSolde
     */
    public String getSumSolde() {
        return sumSolde;
    }

    /**
     * @return the sumTaxeFrais
     */
    public String getSumTaxeFrais() {
        return sumTaxeFrais;
    }

    /**
     * @param etapeLibelle
     *            the etapeLibelle to set
     */
    public void setEtapeLibelle(String etapeLibelle) {
        this.etapeLibelle = etapeLibelle;
    }

    /**
     * @param idEtape
     *            the idEtape to set
     */
    public void setIdEtape(String idEtape) {
        this.idEtape = idEtape;
    }

    /**
     * @param idSequence
     *            the idSequence to set
     */
    public void setIdSequence(String idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * @param nbEtape
     *            the nbEtape to set
     */
    public void setNbEtape(String nbEtape) {
        this.nbEtape = nbEtape;
    }

    /**
     * @param sequenceLibelle
     *            the sequenceLibelle to set
     */
    public void setSequenceLibelle(String sequenceLibelle) {
        this.sequenceLibelle = sequenceLibelle;
    }

    /**
     * @param sumSolde
     *            the sumSolde to set
     */
    public void setSumSolde(String sumSolde) {
        this.sumSolde = sumSolde;
    }

    /**
     * @param sumTaxeFrais
     *            the sumTaxeFrais to set
     */
    public void setSumTaxeFrais(String sumTaxeFrais) {
        this.sumTaxeFrais = sumTaxeFrais;
    }

}
