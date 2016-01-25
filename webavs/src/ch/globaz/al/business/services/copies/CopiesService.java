package ch.globaz.al.business.services.copies;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.ArrayList;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;

/**
 * Service de gestion des copies de document (ex : d�cisions).
 * 
 * @author jts
 * 
 */
public interface CopiesService extends JadeApplicationService {

    /**
     * @param dossier
     *            DossierComplexModel du dossier allocataire
     * @param typeCopie
     *            Type de copie {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     * @param idTiers
     *            Id tiers pour lequel l'adresse doit �tre d�termin�e
     * 
     * @return ArrayList contenant l'ensemble des copies par d�faut
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public String getIdTiersAdresse(DossierComplexModel dossier, String typeCopie, String idTiers)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Charge des copies par d�faut au dossier si celui-ci n'en poss�de encore aucune. Les copies charg�es d�pendent du
     * type de dossier et de paiement
     * 
     * @param dossier
     *            DossierComplexModel du dossier allocataire
     * @param typeCopie
     *            Type de copie {@link ch.globaz.al.business.constantes.ALCSCopie#GROUP_COPIE_TYPE}
     * 
     * @return ArrayList contenant l'ensemble des copies par d�faut
     * 
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public ArrayList<CopieComplexModel> loadDefaultCopies(DossierComplexModel dossier, String typeCopie)
            throws JadeApplicationException, JadePersistenceException;

}
