/*
 * mmu Cr�� le 14 oct. 05
 */
package globaz.hera.impl.standard;

import globaz.hera.db.famille.SFMembreFamille;

/**
 * @author mmu
 * 
 *         14 oct. 05
 */
/*
 * Cette classe �tend SFMembreFamille afin d'impl�menter la m�thodes getEtatCivil en fonction de la date donn�e lors de
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
