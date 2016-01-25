/*
 * mmu Créé le 14 oct. 05
 */
package globaz.hera.impl.standard;

import globaz.hera.db.famille.SFMembreFamille;

/**
 * @author mmu
 * 
 *         14 oct. 05
 */
/*
 * Cette classe étend SFMembreFamille afin d'implémenter la méthodes getEtatCivil en fonction de la date donnée lors de
 * l'instanciation
 */

public class SFMembreFamilleStd extends SFMembreFamille {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEtatCivil = null;

    @Override
    public String getCsEtatCivil() {
        return getEtatCivil(dateEtatCivil);
    }

    /**
     * @return
     */
    public String getDateEtatCivil() {
        return dateEtatCivil;
    }

    /**
     * @param string
     */
    public void setDateEtatCivil(String string) {
        dateEtatCivil = string;
    }

}
