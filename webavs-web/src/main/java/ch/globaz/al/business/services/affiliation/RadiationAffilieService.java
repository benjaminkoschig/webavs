package ch.globaz.al.business.services.affiliation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import ch.globaz.al.business.constantes.enumerations.affiliation.ALEnumProtocoleRadiationAffilie;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;

/**
 * Service permettant la radiation des dossiers d'un affili�
 * 
 * @author jts
 */
public interface RadiationAffilieService extends JadeApplicationService {

    /**
     * Copie un dossier vers un nouvel affili� et actif � partir de la date donn�e
     * 
     * @param dossier
     *            Le dossier � copier
     * @param numAffilie
     *            Num�ro de l'affili� destinataire
     * @param dateDebutActivite
     *            Date du d�but d'activit�/validit�
     * @return HashMap contenant le nouveau dossier (ALEnumProtocoleRadiationAffilie.DOSSIER), le nombre de droit dans
     *         le dossier (ALEnumProtocoleRadiationAffilie.NB_DROITS), le nombre de droits actifs
     *         (ALEnumProtocoleRadiationAffilie.NB_DROITS_ACTIFS) et le nombre d'annonces g�n�r�es
     *         (ALEnumProtocoleRadiationAffilie.NB_ANNONCES)
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap<ALEnumProtocoleRadiationAffilie, Object> copierDossier(DossierComplexModel dossier,
            String numAffilie, String dateDebutActivite) throws JadeApplicationException, JadePersistenceException;

    /**
     * G�n�re les prestations (extourne ou restitution) pour un dossier. La p�riode prise en compte commence au mois
     * suivant la date de radiation du dossier et se termine au mois de la date courante.
     * 
     * Le dossier est en paiement directe des demandes de restitutions sont g�n�r�es.
     * 
     * @param dossier
     *            Le dossier pour lequel g�n�rer les prestations
     * @param hasTransfert
     *            Indique si le dossier de l'allocataire a �t� transf�r� � un autre affili�. Si c'est le cas, les
     *            prestation sont g�n�r�es en extourne. Dans le cas contraire c'est une restitution qui est g�n�r�e
     * @param isFromEbu
     *            Indique si l'appel provient du module E-Business (E-Business -> Recap AF -> Radier et valider) ou
     *            s'il provient de la radiation d'un affili� depuis les allocations familiales)
     * 
     * @return Liste des prestations g�n�r�es
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationComplexSearchModel genererPrestationForDossier(DossierComplexModel dossier,
            boolean hasTransfert, boolean isFromEbu) throws JadeApplicationException, JadePersistenceException;

    /**
     * R�cup�re la liste des dossiers actifs apr�s la date de radiation pour un affili� donn�.
     * 
     * @param numAffilie
     *            Num�ro de l'affili�
     * @param dateRadiation
     *            Date de radiation
     * @return R�sultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexSearchModel getDossiersForAffilie(String numAffilie, String dateRadiation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Radie un dossier � un date donn�e
     * 
     * @param dossier
     *            Le dossier � radier
     * @param dateRadiation
     *            La date de radiation
     * @param reference
     *            Nouvelle r�f�rence du dossier
     * 
     * @return HashMap contenant le dossier (ALEnumProtocoleRadiationAffilie.DOSSIER), le nombre de droits radi�s
     *         (ALEnumProtocoleRadiationAffilie.NB_DROITS_RADIES) et le nombre d'annonces
     *         ALEnumProtocoleRadiationAffilie.NB_ANNONCES
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap<ALEnumProtocoleRadiationAffilie, Object> radierDossier(DossierComplexModel dossier,
            String dateRadiation, String reference) throws JadeApplicationException, JadePersistenceException;
}
