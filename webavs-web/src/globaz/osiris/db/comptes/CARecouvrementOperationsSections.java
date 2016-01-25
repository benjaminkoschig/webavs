package globaz.osiris.db.comptes;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CARecouvrementOperationsSections extends BEntity {
    private static final long serialVersionUID = 3982329924209777292L;

    private String annee = "";
    private String idRubrique = "";
    private String idRubriqueRecouvrement = "";
    private String typeOrdre = "";
    private String dateFinPeriode = "";
    private String idSection = "";
    private String cumulMontant = "";

    /**
     * @return the annee
     */
    public String getAnnee() {
        return annee;
    }

    /**
     * @param annee the annee to set
     */
    public void setAnnee(String annee) {
        this.annee = annee;
    }

    /**
     * @return the idRubrique
     */
    public String getIdRubrique() {
        return idRubrique;
    }

    /**
     * @param idRubrique the idRubrique to set
     */
    public void setIdRubrique(String idRubrique) {
        this.idRubrique = idRubrique;
    }

    /**
     * @return the cumulMontant
     */
    public String getCumulMontant() {
        return cumulMontant;
    }

    /**
     * @param cumulMontant the cumulMontant to set
     */
    public void setCumulMontant(String cumulMontant) {
        this.cumulMontant = cumulMontant;
    }

    @Override
    protected String _getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setAnnee(statement.dbReadString("annee"));
        setIdRubrique(statement.dbReadNumeric("idRubrique"));
        setIdRubriqueRecouvrement(statement.dbReadNumeric("idRubriqueRecouvrement"));
        setTypeOrdre(statement.dbReadNumeric("typeOrdre"));
        setDateFinPeriode(statement.dbReadDateAMJ(CASection.FIELD_DATEFINPERIODE));
        setIdSection(statement.dbReadNumeric(CASection.FIELD_IDSECTION));
        setCumulMontant(statement.dbReadNumeric("cumulMontant"));

    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @return the idRubriqueRecouvrement
     */
    public String getIdRubriqueRecouvrement() {
        return idRubriqueRecouvrement;
    }

    /**
     * @param idRubriqueRecouvrement the idRubriqueRecouvrement to set
     */
    public void setIdRubriqueRecouvrement(String idRubriqueRecouvrement) {
        this.idRubriqueRecouvrement = idRubriqueRecouvrement;
    }

    /**
     * @return the typeOrdre
     */
    public String getTypeOrdre() {
        return typeOrdre;
    }

    /**
     * @param typeOrdre the typeOrdre to set
     */
    public void setTypeOrdre(String typeOrdre) {
        this.typeOrdre = typeOrdre;
    }

    /**
     * @return the dateFinPeriode
     */
    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    /**
     * @param dateFinPeriode the dateFinPeriode to set
     */
    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    /**
     * @return the idSection
     */
    public String getIdSection() {
        return idSection;
    }

    /**
     * @param idSection the idSection to set
     */
    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }
}
