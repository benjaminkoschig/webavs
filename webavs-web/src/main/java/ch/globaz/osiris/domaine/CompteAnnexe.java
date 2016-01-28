package ch.globaz.osiris.domaine;

import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.EntiteDeDomaine;
import ch.globaz.pyxis.domaine.Tiers;

/**
 * Compte annexe de la comptabilité auxiliaire
 */
public class CompteAnnexe extends EntiteDeDomaine {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Tiers titulaire;

    public CompteAnnexe() {
        super();

        titulaire = new Tiers();
    }

    public Tiers getTitulaire() {
        return titulaire;
    }

    public void setTitulaire(Tiers titulaire) {
        Checkers.checkNotNull(titulaire, "titulaire");
        this.titulaire = titulaire;
    }
}
