package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierListComplexModel;
import ch.globaz.al.business.models.dossier.DossierListComplexSearchModel;

/**
 * Interface de d�claration des services de DossierDetailComplexModel
 * 
 * @author PTA
 * @see ch.globaz.al.business.models.dossier.DossierListComplexModel
 * @see ch.globaz.al.business.models.dossier.DossierListComplexSearchModel
 */
public interface DossierListComplexModelService extends JadeApplicationService {

    /**
     * Charge le dossier correspondant � l'id pass� en param�tre
     * 
     * @param idDossierListModel
     *            id du dossier � charger
     * @return Le dossier charg�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierListComplexModel read(String idDossierListModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Recherche les dossiers correspondant aux crit�res contenus dans le mod�le de recherche pass� en param�tre
     * 
     * @param dossierListComplexSearchModel
     *            mod�le de recherche
     * @return R�sultat de la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierListComplexSearchModel search(DossierListComplexSearchModel dossierListComplexSearchModel)
            throws JadeApplicationException, JadePersistenceException;
}
