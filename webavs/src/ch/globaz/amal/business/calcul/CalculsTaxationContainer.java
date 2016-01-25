/**
 * 
 */
package ch.globaz.amal.business.calcul;

import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.SimpleRevenuDeterminant;
import ch.globaz.amal.business.models.revenu.SimpleRevenuHistorique;

/**
 * Contient et génère pour une année de subside et une taxation le revenu déterminant correspondant
 * 
 * Utilisée pour le calcul de subsides, sélection d'une taxation
 * 
 * @author dhi
 * 
 */
public class CalculsTaxationContainer {

    private String anneeHistorique = null;
    private SimpleRevenuDeterminant revenuDeterminant = null;
    private RevenuFullComplex taxation = null;

    /**
     * Default Constructor
     * 
     * @param anneeHistorique
     * @param taxation
     */
    public CalculsTaxationContainer(String anneeHistorique, RevenuFullComplex taxation) {
        initializeObject(anneeHistorique, taxation);
    }

    /**
     * Génération du revenu déterminant
     */
    private void generateRevenuDeterminant() {
        // Initialization d'un revenu full complex et
        // d'un calcul revenuformule
        // nécessaires aux calculs
        RevenuHistoriqueComplex revenuHistoriqueComplex = new RevenuHistoriqueComplex();
        CalculsRevenuFormules calculsRevenuFormules = new CalculsRevenuFormules();

        // Set the minimal values
        SimpleRevenuHistorique revenuHistorique = new SimpleRevenuHistorique();
        revenuHistorique.setAnneeHistorique(getAnneeHistorique());
        revenuHistoriqueComplex.setRevenuFullComplex(getTaxation());
        revenuHistoriqueComplex.setSimpleRevenuHistorique(revenuHistorique);
        revenuHistoriqueComplex = calculsRevenuFormules.doCalcul(revenuHistoriqueComplex);

        // Set the revenu determinant
        setRevenuDeterminant(revenuHistoriqueComplex.getSimpleRevenuDeterminant());
    }

    /**
     * @return the anneeHistorique
     */
    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /**
     * @return the revenuDeterminant
     */
    public SimpleRevenuDeterminant getRevenuDeterminant() {
        return revenuDeterminant;
    }

    /**
     * @return the taxation
     */
    public RevenuFullComplex getTaxation() {
        return taxation;
    }

    /**
     * 
     * 
     * @param anneeHistorique
     * @param taxation
     */
    private void initializeObject(String anneeHistorique, RevenuFullComplex taxation) {
        setAnneeHistorique(anneeHistorique);
        setTaxation(taxation);
        generateRevenuDeterminant();
    }

    /**
     * @param anneeHistorique
     *            the anneeHistorique to set
     */
    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    /**
     * @param revenuDeterminant
     *            the revenuDeterminant to set
     */
    public void setRevenuDeterminant(SimpleRevenuDeterminant revenuDeterminant) {
        this.revenuDeterminant = revenuDeterminant;
    }

    /**
     * @param taxation
     *            the taxation to set
     */
    public void setTaxation(RevenuFullComplex taxation) {
        this.taxation = taxation;
    }

}
