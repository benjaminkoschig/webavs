package globaz.apg.impl.process;

/**
 * <p>
 * Process de la génération des compensations
 * </p>
 * 
 * <p>
 * Traitement spécial :
 * <p>
 * <p>
 * Les compensations ne sont proposées que lorsque l'affilié a des dettes. Mais en aucun cas la case "Compenser" est
 * cochée.
 * </p>
 * 
 * 
 * @author DVH
 * @author HPE
 */
public class APGenererCompensationsProcess002 extends APGenererCompensationsProcessAvecSectionCompensable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected boolean isAffilieEnFaillite(SectionEtCompteAnnexe sectionEnCours, String annee) {
        return false;
    }

    @Override
    protected boolean isSectionACompenserAutomatiquement(SectionEtCompteAnnexe section) {
        return false;
    }

    @Override
    protected boolean isSectionAVerifier(SectionEtCompteAnnexe section) {
        return false;
    }

    @Override
    public boolean isModulePorterEnCompte() {
        return false;
    }
}
