package ch.globaz.al.business.services.models.allocataire;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.allocataire.AllocataireModel;

/**
 * Service fournissant des m�thodes m�tier li�es � l'allocataire
 * 
 * @author GMO
 * @see ch.globaz.al.business.models.allocataire.AllocataireAgricoleComplexModel
 * @see ch.globaz.al.business.models.allocataire.AllocataireComplexModel
 * @see ch.globaz.al.business.models.allocataire.AllocataireModel
 */
public interface AllocataireBusinessService extends JadeApplicationService {

    /**
     * Retourne le type de r�sident en fonction du permis de travail et du pays de r�sidence. Un allocataire ayant un
     * permis de frontalier est consid�r� comme Suisse. Dans les autres cas le pays de r�sidence fait foi
     * 
     * @param allocataire
     *            Allocataire pour lequel r�cup�rer le type de r�sident
     * @return type de r�sident {@link ch.globaz.al.business.constantes.ALCSTarif}
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getTypeResident(AllocataireModel allocataire) throws JadeApplicationException;

    /**
     * D�termine si l'allocataire est utilis� dans un dossier actif
     * 
     * @param idAllocataire
     *            id de l'allocataire � contr�ler
     * @return nb de dossiers actifs li�s � l'allocataire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int isActif(String idAllocataire) throws JadeApplicationException, JadePersistenceException;

    /**
     * D�termine si l'allocataire est agricole ou non en fonction de la pr�sence ou non d'un enregistrement
     * {@link ch.globaz.al.business.models.allocataire.AgricoleModel} li� � l'allocataire dont l'id est pass� en
     * param�tre
     * 
     * @param idAllocataire
     *            l'id de l'allocataire � contr�ler
     * @return <code>true</code> si l'allocataire est un agriculteur, <code>false</code> sinon
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isAgricole(String idAllocataire) throws JadeApplicationException, JadePersistenceException;
}
