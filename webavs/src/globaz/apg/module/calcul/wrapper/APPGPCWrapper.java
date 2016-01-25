package globaz.apg.module.calcul.wrapper;

import globaz.framework.util.FWCurrency;
import globaz.globall.util.JADate;

/**
 * Descpription Définit la plus grande période commune aux prestations.
 * 
 * @author scr Date de création 17 mai 05
 */
public class APPGPCWrapper {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private JADate dateDebut = null;
    private JADate dateFin = null;
    private FWCurrency montantBrut = null;

    // Type de prestation;
    // Valeurs possible : -> restitution
    // -> correction
    // -> nouvel prestation
    private int typePrestation = -1;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APPGPCWrapper.
     */
    public APPGPCWrapper() {
        super();
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public JADate getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public JADate getDateFin() {
        return dateFin;
    }

    /**
     * getter pour l'attribut montant brut
     * 
     * @return la valeur courante de l'attribut montant brut
     */
    public FWCurrency getMontantBrut() {
        return montantBrut;
    }

    /**
     * getter pour l'attribut type prestation
     * 
     * @return la valeur courante de l'attribut type prestation
     */
    public int getTypePrestation() {
        return typePrestation;
    }

    /**
     * setter pour l'attribut date debut
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(JADate date) {
        dateDebut = date;
    }

    /**
     * setter pour l'attribut date fin
     * 
     * @param date
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(JADate date) {
        dateFin = date;
    }

    /**
     * setter pour l'attribut montant brut
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setMontantBrut(FWCurrency currency) {
        montantBrut = currency;
    }

    /**
     * setter pour l'attribut type prestation
     * 
     * @param i
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypePrestation(int i) {
        typePrestation = i;
    }
}
