package globaz.apg.module.calcul.wrapper;

import globaz.apg.module.calcul.APResultatCalcul;
import globaz.framework.util.FWCurrency;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Descpription Wrapper class, regroupant les dates de debut/fin de la prestation de base, ainsi que les dates de
 * début/fin de la plus grande période commune de toutes les prestations du droit idDroit.
 * 
 * @author scr Date de création 18 mai 05
 */
public class APPrestationWrapper {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private FWCurrency fraisGarde = null;
    private String idDroit = null;
    private APPeriodeWrapper lastPeriodePGPCAdded = null;

    private APPeriodeWrapper periodeBaseCalcul = null;
    private Collection periodesPGPC = null;

    private APResultatCalcul prestationBase = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * private FWCurrency droitAcquis = null;
     */
    public APPrestationWrapper() {
        super();

        // Périodes ordonnées par date de début.
        periodesPGPC = new TreeSet(new APPeriodeWrapperComparator());
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @param pw
     *            DOCUMENT ME!
     */
    public void addPeriodePGPC(APPeriodeWrapper pw) {
        periodesPGPC.add(pw);
        lastPeriodePGPCAdded = pw;
    }

    /**
     * public FWCurrency getDroitAcquis() { return droitAcquis; } public void setDroitAcquis(FWCurrency currency) {
     * droitAcquis = currency; }
     * 
     * @return la valeur courante de l'attribut frais garde
     */
    public FWCurrency getFraisGarde() {
        if(fraisGarde!=null) {
            return new FWCurrency(fraisGarde.toString());
        }
        return null;
    }

    /**
     * getter pour l'attribut id droit
     * 
     * @return la valeur courante de l'attribut id droit
     */
    public String getIdDroit() {
        return idDroit;
    }

    /**
     * getter pour l'attribut last periode PGPCAdded
     * 
     * @return la valeur courante de l'attribut last periode PGPCAdded
     */
    public APPeriodeWrapper getLastPeriodePGPCAdded() {
        return lastPeriodePGPCAdded;
    }

    /**
     * getter pour l'attribut periode base calcul
     * 
     * @return la valeur courante de l'attribut periode base calcul
     */
    public APPeriodeWrapper getPeriodeBaseCalcul() {
        return periodeBaseCalcul;
    }

    /**
     * getter pour l'attribut periodes PGPC
     * 
     * @return la valeur courante de l'attribut periodes PGPC
     */
    public Collection getPeriodesPGPC() {
        return periodesPGPC;
    }

    /**
     * getter pour l'attribut prestation base
     * 
     * @return la valeur courante de l'attribut prestation base
     */
    public APResultatCalcul getPrestationBase() {
        return prestationBase;
    }

    /**
     * setter pour l'attribut frais garde
     * 
     * @param currency
     *            une nouvelle valeur pour cet attribut
     */
    public void setFraisGarde(FWCurrency currency) {
        fraisGarde = currency;
    }

    /**
     * setter pour l'attribut id droit
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdDroit(String string) {
        idDroit = string;
    }

    /**
     * setter pour l'attribut periode base calcul
     * 
     * @param wrapper
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodeBaseCalcul(APPeriodeWrapper wrapper) {
        periodeBaseCalcul = wrapper;
    }

    /**
     * setter pour l'attribut periodes PGPC
     * 
     * @param collection
     *            une nouvelle valeur pour cet attribut
     */
    public void setPeriodesPGPC(Collection collection) {
        periodesPGPC = collection;
    }

    /**
     * setter pour l'attribut prestation base
     * 
     * @param calcul
     *            une nouvelle valeur pour cet attribut
     */
    public void setPrestationBase(APResultatCalcul calcul) {
        prestationBase = calcul;
    }
}
