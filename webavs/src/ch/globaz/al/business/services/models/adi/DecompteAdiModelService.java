package ch.globaz.al.business.services.models.adi;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.adi.DecompteAdiSearchModel;

/**
 * Service de gestion de persistance des donn�es de ADI - D�compte
 * 
 * @author PTA
 */
public interface DecompteAdiModelService extends JadeApplicationService {

    /**
     * Compte le nombre d'enregistrement retourn�s par le mod�le de recherche pass� en param�tre
     * 
     * @param decompteAdiSearch
     *            mod�le contenant les crit�res de recherche
     * @return nombre d'enregistrement correspondant � la recherche
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public int count(DecompteAdiSearchModel decompteAdiSearch) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * m�thode de cr�ation d'un d�compte ADI
     * 
     * @param decompteAdiModel
     *            mod�le � enregistrer
     * @return le mod�le enregistr�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel create(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * m�thode de suppression d'un d�compte ADI
     * 
     * @param decompteAdiModel
     *            le mod�le � supprimer
     * @return le mod�le supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel delete(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Supprime les d�comptes li� � l'en-t�te pass�e en param�tre
     * 
     * @param idEntete
     *            id de l'en-t�te de prestation pour laquelle supprimer les d�comptes
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @deprecated plus de suppression selon l'id ent�te
     */
    @Deprecated
    public void deleteForIdEntete(String idEntete) throws JadeApplicationException, JadePersistenceException;

    /**
     * Initialisation d'un d�compte
     * 
     * @param decompte
     *            le d�compte � initialiser
     * @return le d�compte initialis�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel initModel(DecompteAdiModel decompte) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * M�thode de lecture d'un d�compte ADI
     * 
     * @param idDecompteAdiModel
     *            id du mod�le � charger
     * @return le mod�le charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel read(String idDecompteAdiModel) throws JadeApplicationException, JadePersistenceException;

    /**
     * Recherche les d�compte ADI selon les crit�res du mod�le de recherche
     * 
     * @param searchModel
     *            le mod�le de recherche
     * @return le mod�le de recherche avec les r�sultats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiSearchModel search(DecompteAdiSearchModel searchModel) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * public DecompteAdiSearchModel search(DecompteAdiSearchModel searchModel) throws JadePersistenceException,
     * JadeApplicationException;
     * 
     * /** M�thode de mis � jour d'un d�compte ADI
     * 
     * @param decompteAdiModel
     *            le mod�le � mettre � jour
     * @return le mod�le mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DecompteAdiModel update(DecompteAdiModel decompteAdiModel) throws JadeApplicationException,
            JadePersistenceException;

}
