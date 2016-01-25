package ch.globaz.al.business.services.rubriques.comptables;

/**
 * Service spécifique permettant de récupérer une rubrique comptable pour la FPV
 * 
 * @author gmo
 * 
 */
public interface RubriquesComptablesFPVService extends RubriquesComptablesService {
    /**
     * pour forcer le canton de l'affilié, à utiliser uniquement pour les JUnit ou en ayant conscience des conséquences
     */
    public String forceCantonAffilie = null;
    /**
     * pour forcer la CAF, à utiliser uniquement pour les JUnit ou en ayant conscience des conséquences
     */
    public String forceCodeCAF = null;
}
