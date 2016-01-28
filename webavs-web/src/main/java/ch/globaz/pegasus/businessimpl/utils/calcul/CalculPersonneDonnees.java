package ch.globaz.pegasus.businessimpl.utils.calcul;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;

public class CalculPersonneDonnees {

    private float depenses = 0;
    final private List<CalculDonneesCC> donnees = new ArrayList<CalculDonneesCC>();

    private float fortunes = 0;
    final private String id;
    private float revenus = 0;

    public CalculPersonneDonnees(String idPersonne) {
        id = idPersonne;
    }

    /**
     * @return the depenses
     */
    public float getDepenses() {
        return depenses;
    }

    /**
     * @return the donnees
     */
    public List<CalculDonneesCC> getDonnees() {
        return donnees;
    }

    /**
     * @return the fortunes
     */
    public float getFortunes() {
        return fortunes;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the revenus
     */
    public float getRevenus() {
        return revenus;
    }

    /**
     * @param depenses
     *            the depenses to set
     */
    public void setDepenses(float depenses) {
        this.depenses = depenses;
    }

    /**
     * @param fortunes
     *            the fortunes to set
     */
    public void setFortunes(float fortunes) {
        this.fortunes = fortunes;
    }

    /**
     * @param revenus
     *            the revenus to set
     */
    public void setRevenus(float revenus) {
        this.revenus = revenus;
    }

}
