package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AllocataireModel;

/**
 * Service fournissant des méthodes métier liées à l'allocataire
 * 
 * @author GMO
 * @see ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel
 * @see ch.globaz.al.business.models.allocataire.AllocataireComplexModel
 * @see ch.globaz.al.business.models.allocataire.AllocataireModel
 */
public interface AllocataireBusinessService extends JadeApplicationService {

    /**
     * Retourne le type de résident en fonction du permis de travail et du pays de résidence. Un allocataire ayant un
     * permis de frontalier est considéré comme Suisse. Dans les autres cas le pays de résidence fait foi
     * 
     * @param allocataire
     *            Allocataire pour lequel récupérer le type de résident
     * @return type de résident {@link ch.globaz.al.business.constantes.ALCSTarif}
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public String getTypeResident(AllocataireModel allocataire) throws JadeApplicationException;

    /**
     * Détermine si l'allocataire est utilisé dans un dossier actif
     * 
     * @param idAllocataire
     *            id de l'allocataire à contrôler
     * @return nb de dossiers actifs liés à l'allocataire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int isActif(String idAllocataire) throws JadeApplicationException, JadePersistenceException;

    /**
     * Détermine si l'allocataire est agricole ou non en fonction de la présence ou non d'un enregistrement
     * {@link ch.globaz.al.business.models.allocataire.AgricoleModel} lié à l'allocataire dont l'id est passé en
     * paramètre
     * 
     * @param idAllocataire
     *            l'id de l'allocataire à contrôler
     * @return <code>true</code> si l'allocataire est un agriculteur, <code>false</code> sinon
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isAgricole(String idAllocataire) throws JadeApplicationException, JadePersistenceException;
}
