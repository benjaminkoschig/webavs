package globaz.osiris.db.interet.analytique;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.osiris.db.comptes.CAOperation;

/**
 * idPlan touché(es) pour une section.
 * 
 * @author DDA
 * 
 */
public class CAInteretAnalytique extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FIELD_MONTANTINTERET = "MONTANTINTERET";

    private String idCaisseProfessionnelle;
    private String idRubrique;
    private String idSection;
    private String montant;
    private String montantInteret;

    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    /**
     * @see BEntity#_readProperties(BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdSection(statement.dbReadNumeric(CAOperation.FIELD_IDSECTION));
        setMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));

        try {
            setMontantInteret(statement.dbReadNumeric(CAInteretAnalytique.FIELD_MONTANTINTERET));
            setIdRubrique(statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTE));
            setIdCaisseProfessionnelle(statement.dbReadNumeric(CAOperation.FIELD_IDCAISSEPROFESSIONNELLE));
        } catch (Exception e) {
            // Do nothing
        }

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Nothing
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Nothing
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Nothing
    }

    public String getIdCaisseProfessionnelle() {
        return idCaisseProfessionnelle;
    }

    public String getIdRubrique() {
        return idRubrique;
    }

    public String getIdSection() {
        return idSection;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantInteret() {
        return montantInteret;
    }

    public void setIdCaisseProfessionnelle(String idCaisseProfessionnelle) {
        this.idCaisseProfessionnelle = idCaisseProfessionnelle;
    }

    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantInteret(String montantInteret) {
        this.montantInteret = montantInteret;
    }

}
