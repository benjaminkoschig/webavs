package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.models.adi.AdiSaisieComplexModel;
import ch.globaz.al.business.models.adi.AdiSaisieComplexSearchModel;

/**
 * 
 * Services de la persistance de AdiSaisieComplexModel
 * 
 * @author GMO
 * 
 */
public interface AdiSaisieComplexModelService extends JadeApplicationService {
    /**
     * Service de cr�ation d'un mod�le adiSaisieComplexModel
     * 
     * @param adiSaisieComplexModel
     *            le mod�le � saisir
     * @return AdiSaisieComplexModel le mod�le cr�e
     * @throws JadeApplicationException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AdiSaisieComplexModel create(AdiSaisieComplexModel adiSaisieComplexModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Service de suppression d'un mod�le adiSaisie
     * 
     * @param adiSaisieComplexModel
     *            le mod�le � effacer
     * @return AdiSaisieComplexModel le mod�le effac�
     * @throws JadeApplicationException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AdiSaisieComplexModel delete(AdiSaisieComplexModel adiSaisieComplexModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Initialise un adiSaisieComplexModel avec des valeurs par d�faut si il est nouveau, sinon le charge
     * 
     * @param adiSaisieComplexModel
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
    public AdiSaisieComplexModel initModel(AdiSaisieComplexModel adiSaisieComplexModel, HashMap listeASaisir)
            throws JadePersistenceException, JadeApplicationException;

    /**
     * Service de lecture d'un adiSaisieModel
     * 
     * @param idSaisieModel
     *            id du mod�le qu'on veut charger
     * @return AdiSaisieComplexModel charg�
     * @throws JadeApplicationException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadePersistenceException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public AdiSaisieComplexModel read(String idSaisieModel) throws JadePersistenceException, JadeApplicationException;

    /**
     * Service de recherche sur le mod�le AdiSaisieComplexModel
     * 
     * @param searchModel
     *            mod�le de recherche contenant les crit�res d�finis
     * @return le mod�le de recherche contenant les r�sultats correspondants aux crit�res
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public AdiSaisieComplexSearchModel search(AdiSaisieComplexSearchModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

}
