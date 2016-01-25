package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.models.adi.AdiSaisieModel;
import ch.globaz.al.business.models.adi.AdiSaisieSearchModel;

/**
 * Service de gestion de persistance des donn�es de ADI du mod�le de saisie - D�compte
 * 
 * @author GMO
 */
public interface AdiSaisieModelService extends JadeApplicationService {
    /**
     * Service de cr�ation d'une saisie ADI
     * 
     * @param adiSaisieModel
     *            le mod�le � cr�er
     * @return AdiSaisieModel le mod�le cr��
     * @throws JadeApplicationException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AdiSaisieModel create(AdiSaisieModel adiSaisieModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Service de suppression d'un mod�le adiSaisie
     * 
     * @param adiSaisieModel
     *            le mod�le � effacer
     * @return AdiSaisieModel le mod�le effac�
     * @throws JadeApplicationException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AdiSaisieModel delete(AdiSaisieModel adiSaisieModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Initialise un adiSaisieModel avec des valeurs par d�faut si il est nouveau, sinon le charge
     * 
     * @param adiSaisieModel
     *            Le mod�le adiSaisi � initialiser
     * @param listeASaisir
     *            permet de savoir avec quelles valeurs on doit initialiser le mod�le (p�riode,enfant)
     * @return Le mod�le adiSaisi initialis�
     * @throws JadeApplicationException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AdiSaisieModel initModel(AdiSaisieModel adiSaisieModel, HashMap listeASaisir)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Service de lecture d'un adiSaisieModel
     * 
     * @param idSaisieModel
     *            id du mod�le qu'on veut charger
     * @return AdiSaisieModel charg�
     * @throws JadeApplicationException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AdiSaisieModel read(String idSaisieModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Service de recherche les saisies ADI
     * 
     * @param searchModel
     *            mod�le de recherche
     * @return mod�le de recherche avec les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AdiSaisieSearchModel search(AdiSaisieSearchModel searchModel) throws JadeApplicationException,
            JadePersistenceException;
}
