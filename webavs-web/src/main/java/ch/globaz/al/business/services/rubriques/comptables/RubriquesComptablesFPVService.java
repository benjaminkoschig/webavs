package ch.globaz.al.business.services.rubriques.comptables;

/**
 * Service sp�cifique permettant de r�cup�rer une rubrique comptable pour la FPV
 * 
 * @author gmo
 * 
 */
public interface RubriquesComptablesFPVService extends RubriquesComptablesService {
    /**
     * pour forcer le canton de l'affili�, � utiliser uniquement pour les JUnit ou en ayant conscience des cons�quences
     */
    String forceCantonAffilie = null;
    /**
     * pour forcer la CAF, � utiliser uniquement pour les JUnit ou en ayant conscience des cons�quences
     */
    String forceCodeCAF = null;
}
