package ch.globaz.al.businessimpl.generation.prestations;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;

/**
 * Classe g�n�rant une demande de restitution. Elle fait appel � la m�thode
 * {@link GenPrestationDossier#executeExtourne(ContextAffilie)} de la classe {@link GenPrestationDossier} pour
 * rechercher les prestations existante pour la p�riode � restituer.
 * 
 * Si un montant fixe doit �tre restitu�, c'est la classe {@link GenPrestationForcee} qui doit �tre utilis�e
 * 
 * @author jts
 * @see GenPrestationDossier
 * @see GenPrestationForcee
 */
public class GenPrestationRestitution extends GenPrestationDossier {

    /**
     * Constructeur
     * 
     * @param context Context contenant les informations n�cessaires � la g�n�ration
     */
    public GenPrestationRestitution(ContextAffilie context) {
        super(context);
    }

    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationRestitution#execute : context is null");
        }

        // BZ 8138 - Dans le cas d'un dossier radi�, prendre en compte le nombre de jour indiqu� au niveau de la
        // validit� pour le calcul de la restitution si le jour de la radiation n'est pas le dernier jour du mois
        // Faire le m�me processus que pour un dossier standard (extourner le mois et g�n�rer une nouvelle prestations
        // en prenant compte du nombre de jours � restituer)
        super.execute();
    }
}