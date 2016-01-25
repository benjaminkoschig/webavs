package globaz.pavo.db.compte;

import globaz.pavo.process.CIAbstimmfileDocument;
import globaz.pavo.vb.CIAbstractPersistentViewBean;

public class CICompareMasseSalarialeListeViewBean extends CIAbstractPersistentViewBean {
    private Boolean allAffilie = true;
    private String anneeDebut = "";
    private String anneeFin = "";
    private String montant = "";
    private String montantOperator = CIAbstimmfileDocument.Operator.egal.toString();
    private String numeroFrom = "";
    private String numeroTo = "";

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    /**
     * @return the allAffilie
     */
    public Boolean getAllAffilie() {
        return allAffilie;
    }

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    /**
     * @return the montant
     */
    public String getMontant() {
        return montant;
    }

    /**
     * @return the montantOperator
     */
    public String getMontantOperator() {
        return montantOperator;
    }

    /**
     * @return the numeroFrom
     */
    public String getNumeroFrom() {
        return numeroFrom;
    }

    /**
     * @return the numeorTo
     */
    public String getNumeroTo() {
        return numeroTo;
    }

    @Override
    public void retrieve() throws Exception {
    }

    /**
     * @param allAffilie
     *            the allAffilie to set
     */
    public void setAllAffilie(Boolean allAffilie) {
        this.allAffilie = allAffilie;
    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    /**
     * @param montant
     *            the montant to set
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * @param montantOperator
     *            the montantOperator to set
     */
    public void setMontantOperator(String montantOperator) {
        this.montantOperator = montantOperator;
    }

    /**
     * @param numeroFrom
     *            the numeroFrom to set
     */
    public void setNumeroFrom(String numeroFrom) {
        this.numeroFrom = numeroFrom;
    }

    /**
     * @param numeorTo
     *            the numeorTo to set
     */
    public void setNumeroTo(String numeorTo) {
        numeroTo = numeorTo;
    }

    @Override
    public void update() throws Exception {
    }

}
