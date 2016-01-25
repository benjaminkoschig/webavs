package ch.globaz.corvus.domaine;

import java.util.HashSet;
import java.util.Set;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.common.domaine.Pourcentage;
import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * Une demande de rente de survivant
 */
public final class DemandeRenteSurvivant extends DemandeRente {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Pourcentage pourcentageReduction;

    public DemandeRenteSurvivant() {
        super();

        pourcentageReduction = Pourcentage.ZERO_POURCENT;

        setTypeDemandeRente(TypeDemandeRente.DEMANDE_SURVIVANT);
    }

    @Override
    public Set<CodePrestation> codesPrestationsAcceptesPourCeTypeDeDemande() {
        Set<CodePrestation> codesPrestationsAcceptes = new HashSet<CodePrestation>();

        for (CodePrestation unCodePrestation : CodePrestation.values()) {
            if (unCodePrestation.isSurvivant()) {
                codesPrestationsAcceptes.add(unCodePrestation);
            }
        }

        return codesPrestationsAcceptes;
    }

    /**
     * @return le pourcentage de réduction du montant de la rente
     */
    public Pourcentage getPourcentageReduction() {
        return pourcentageReduction;
    }

    /**
     * (re-)défini le pourcentage de réduction du montant de la rente
     * 
     * @param pourcentageReduction
     *            un pourcentage
     * @throws NullPointerException
     *             si le pourcentage passé en paramètre est null
     */
    public void setPourcentageReduction(final Pourcentage pourcentageReduction) {
        Checkers.checkNotNull(pourcentageReduction, "demandeSurvivant.pourcentageReduction");
        this.pourcentageReduction = pourcentageReduction;
    }
}
