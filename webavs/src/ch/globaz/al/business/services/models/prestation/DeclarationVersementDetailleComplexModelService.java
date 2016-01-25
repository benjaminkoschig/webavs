package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;

/**
 * Service de gestion de persistance des donn�es du mod�le <code>DeclarationVersementDetailleComplexModel</code>
 * 
 * @author PTA
 * 
 */
public interface DeclarationVersementDetailleComplexModelService extends JadeApplicationService {

    /**
     * Recherche sur les prestations correspondant aux crit�res de recherche d'une d�claration de versement d�taill�e
     * 
     * @param searchModel
     *            mod�le de recherche pour une d�claration de versment d�taill�
     * @return le mod�le de recherche contenant les r�sultats de la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    public DeclarationVersementDetailleSearchComplexModel search(
            DeclarationVersementDetailleSearchComplexModel searchModel) throws JadeApplicationException,
            JadePersistenceException;

}
