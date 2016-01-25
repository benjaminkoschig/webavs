package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe générant une demande de restitution. Elle fait appel à la méthode
 * {@link GenPrestationDossier#executeExtourne(ContextAffilie)} de la classe {@link GenPrestationDossier} pour
 * rechercher les prestations existante pour la période à restituer.
 * 
 * Si un montant fixe doit être restitué, c'est la classe {@link GenPrestationForcee} qui doit être utilisée
 * 
 * @author jts
 * @see GenPrestationDossier
 * @see GenPrestationForcee
 */
public class GenPrestationRestitution extends GenPrestationDossier {

    /**
     * Constructeur
     * 
     * @param context Context contenant les informations nécessaires à la génération
     */
    public GenPrestationRestitution(ContextAffilie context) {
        super(context);
    }

    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationRestitution#execute : context is null");
        }

        // BZ 8138 - Dans le cas d'un dossier radié, prendre en compte le nombre de jour indiqué au niveau de la
        // validité pour le calcul de la restitution si le jour de la radiation n'est pas le dernier jour du mois
        // Faire le même processus que pour un dossier standard (extourner le mois et générer une nouvelle prestations
        // en prenant compte du nombre de jours à restituer)
        super.execute();
    }
}