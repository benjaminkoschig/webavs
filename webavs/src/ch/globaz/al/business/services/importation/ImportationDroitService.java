package ch.globaz.al.business.services.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * Service d'importation des donn�es de droits
 * 
 * @author jts
 * 
 */
public interface ImportationDroitService extends JadeApplicationService {

    /**
     * Effectue une recherche dans la persistance afin de v�rifier si l'enfant correspondant au mod�le pass� en
     * param�tre existe d�j�.
     * 
     * La recherche s'effecture d'abord par le NSS (s'il existe) puis par le nom, pr�nom et date de naissance.
     * 
     * Si une instance unique est trouv�e, elle est retourn�e. En cas de r�sultats multiples, une exception est lev�e.
     * Si aucune instance n'est trouv�e, le mod�le d'origine est retourn�.
     * 
     * @param enfant
     *            mod�le contenant les crit�res de recherche
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws Exception
     */
    public EnfantComplexModel searchEnfant(EnfantComplexModel enfant, String idDossier, String idPersonneAlfa)
            throws JadeApplicationException, JadePersistenceException, Exception;

    /**
     * Effectue une recherche dans la persistance afin de v�rifier si la personne correspondant au mod�le pass� en
     * param�tre existe d�j�.
     * 
     * La recherche s'effecture d'abord par le NSS (s'il existe) puis par le nom, pr�nom et date de naissance.
     * 
     * Si une instance unique est trouv�e, elle est retourn�e. En cas de r�sultats multiples, une exception est lev�e.
     * Si aucune instance n'est trouv�e, le mod�le d'origine est retourn�.
     * 
     * @param personne
     *            mod�le contenant les crit�res de recherche
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PersonneEtendueComplexModel searchPersonne(PersonneEtendueComplexModel personne)
            throws JadeApplicationException, JadePersistenceException;
}