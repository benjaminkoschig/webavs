package ch.globaz.al.business.services.importation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.droit.EnfantComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * Service d'importation des données de droits
 * 
 * @author jts
 * 
 */
public interface ImportationDroitService extends JadeApplicationService {

    /**
     * Effectue une recherche dans la persistance afin de vérifier si l'enfant correspondant au modèle passé en
     * paramètre existe déjà.
     * 
     * La recherche s'effecture d'abord par le NSS (s'il existe) puis par le nom, prénom et date de naissance.
     * 
     * Si une instance unique est trouvée, elle est retournée. En cas de résultats multiples, une exception est levée.
     * Si aucune instance n'est trouvée, le modèle d'origine est retourné.
     * 
     * @param enfant
     *            modèle contenant les critères de recherche
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws Exception
     */
    public EnfantComplexModel searchEnfant(EnfantComplexModel enfant, String idDossier, String idPersonneAlfa)
            throws JadeApplicationException, JadePersistenceException, Exception;

    /**
     * Effectue une recherche dans la persistance afin de vérifier si la personne correspondant au modèle passé en
     * paramètre existe déjà.
     * 
     * La recherche s'effecture d'abord par le NSS (s'il existe) puis par le nom, prénom et date de naissance.
     * 
     * Si une instance unique est trouvée, elle est retournée. En cas de résultats multiples, une exception est levée.
     * Si aucune instance n'est trouvée, le modèle d'origine est retourné.
     * 
     * @param personne
     *            modèle contenant les critères de recherche
     * @return Résultat de la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public PersonneEtendueComplexModel searchPersonne(PersonneEtendueComplexModel personne)
            throws JadeApplicationException, JadePersistenceException;
}