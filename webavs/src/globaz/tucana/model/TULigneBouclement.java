package globaz.tucana.model;

import globaz.itucana.model.ITULines;

/**
 * Classe reflettant une ligne de bouclement
 * 
 * @author fgo date de création : 6 juin 2006
 * @version : version 1.0
 * 
 */
public class TULigneBouclement implements ITULines {
    private String canton = null;
    private String montantNombre = null;
    private String rubrique = null;

    /**
     * Constructeur de la classe TULigneBouclement
     */
    public TULigneBouclement() {
        super();
    }

    /**
     * Constructeur de la classe TULigneBouclement avec passage de tous les attributs de la classe
     */
    public TULigneBouclement(String _canton, String _rubrique, String _montantNombre) {
        super();
        canton = _canton.toUpperCase();
        rubrique = _rubrique;
        montantNombre = _montantNombre;
    }

    /**
     * Retroune l'attribut canton.
     * 
     * @return Retroune l'attribut canton.
     */
    @Override
    public String getCanton() {
        return canton;
    }

    /**
     * Retroune l'attribut montantNombre.
     * 
     * @return Retroune l'attribut montantNombre.
     */
    @Override
    public String getMontantNombre() {
        return montantNombre;
    }

    /**
     * Retroune l'attribut rubrique.
     * 
     * @return Retroune l'attribut rubrique.
     */
    @Override
    public String getRubrique() {
        return rubrique;
    }

    /**
     * la propriété canton à modifier
     * 
     * @param canton
     *            la propriété canton à modifier
     */
    public void setCanton(String canton) {
        this.canton = canton;
    }

    /**
     * la propriété montantNombre à modifier
     * 
     * @param montantNombre
     *            la propriété montantNombre à modifier
     */
    public void setMontantNombre(String montantNombre) {
        this.montantNombre = montantNombre;
    }

    /**
     * la propriété rubrique à modifier
     * 
     * @param rubrique
     *            la propriété rubrique à modifier
     */
    public void setRubrique(String rubrique) {
        this.rubrique = rubrique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer ligne = new StringBuffer();
        ligne.append("************************************************** \n");
        ligne.append("           Ligne bouclement\n");
        ligne.append("************************************************** \n");
        ligne.append("canton : ").append(canton).append("\n");
        ligne.append("rubrique : ").append(rubrique).append("\n");
        ligne.append("montant - nombre : ").append(montantNombre).append("\n");
        return ligne.toString();
    }

}
