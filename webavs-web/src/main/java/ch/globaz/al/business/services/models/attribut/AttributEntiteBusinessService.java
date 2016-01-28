package ch.globaz.al.business.services.models.attribut;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;

/**
 * Les services métier des attribut entité
 * 
 * @author GMO
 * 
 */
public interface AttributEntiteBusinessService extends JadeApplicationService {

    /**
     * Définit un attribut pour une entité, créé ou màj selon l'attribut et sa valeur. Si la valeur correspond à celle
     * par défaut, aucun changement en base pour les attributs spécifiques à l'entité. C'est le paramètre par défaut qui
     * implicement lié à l'entité
     * 
     * @param idEntite
     *            id de l'entité dont on veut modifier l'attribut
     * @param typeEntite
     *            type de l'entité (nom complet de la classe du modèle simple)
     * @param updatedAttribut
     *            nouvelle valeur de l'attribut
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void changeAttributValueForEntite(String idEntite, String typeEntite, AttributEntiteModel updatedAttribut)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne les listes des attributs liés à l'entité, qu'ils soient spécifiques à l'entité ou ceux par défaut
     * 
     * @param idEntite
     *            id de l'entité dont on veut les attributs
     * @param typeEntite
     *            type de l'entité (nom complet de la classe du modèle simple)
     * @return liste de AttributEntiteModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList getAttributListForEntite(String idEntite, String typeEntite) throws JadeApplicationException,
            JadePersistenceException;

}
