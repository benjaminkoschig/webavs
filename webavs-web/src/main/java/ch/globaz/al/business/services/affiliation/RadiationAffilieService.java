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
 * Service permettant la radiation des dossiers d'un affilié
 * 
 * @author jts
 */
public interface RadiationAffilieService extends JadeApplicationService {

    /**
     * Copie un dossier vers un nouvel affilié et actif à partir de la date donnée
     * 
     * @param dossier
     *            Le dossier à copier
     * @param numAffilie
     *            Numéro de l'affilié destinataire
     * @param dateDebutActivite
     *            Date du début d'activité/validité
     * @return HashMap contenant le nouveau dossier (ALEnumProtocoleRadiationAffilie.DOSSIER), le nombre de droit dans
     *         le dossier (ALEnumProtocoleRadiationAffilie.NB_DROITS), le nombre de droits actifs
     *         (ALEnumProtocoleRadiationAffilie.NB_DROITS_ACTIFS) et le nombre d'annonces générées
     *         (ALEnumProtocoleRadiationAffilie.NB_ANNONCES)
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap<ALEnumProtocoleRadiationAffilie, Object> copierDossier(DossierComplexModel dossier,
            String numAffilie, String dateDebutActivite) throws JadeApplicationException, JadePersistenceException;

    /**
     * Génère les prestations (extourne ou restitution) pour un dossier. La période prise en compte commence au mois
     * suivant la date de radiation du dossier et se termine au mois de la date courante.
     * 
     * Le dossier est en paiement directe des demandes de restitutions sont générées.
     * 
     * @param dossier
     *            Le dossier pour lequel générer les prestations
     * @param hasTransfert
     *            Indique si le dossier de l'allocataire a été transféré à un autre affilié. Si c'est le cas, les
     *            prestation sont générées en extourne. Dans le cas contraire c'est une restitution qui est générée
     * @param isFromEbu
     *            Indique si l'appel provient du module E-Business (E-Business -> Recap AF -> Radier et valider) ou
     *            s'il provient de la radiation d'un affilié depuis les allocations familiales)
     * 
     * @return Liste des prestations générées
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DetailPrestationComplexSearchModel genererPrestationForDossier(DossierComplexModel dossier,
            boolean hasTransfert, boolean isFromEbu) throws JadeApplicationException, JadePersistenceException;

    /**
     * Récupère la liste des dossiers actifs après la date de radiation pour un affilié donné.
     * 
     * @param numAffilie
     *            Numéro de l'affilié
     * @param dateRadiation
     *            Date de radiation
     * @return Résultat de la recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexSearchModel getDossiersForAffilie(String numAffilie, String dateRadiation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Radie un dossier à un date donnée
     * 
     * @param dossier
     *            Le dossier à radier
     * @param dateRadiation
     *            La date de radiation
     * @param reference
     *            Nouvelle référence du dossier
     * 
     * @return HashMap contenant le dossier (ALEnumProtocoleRadiationAffilie.DOSSIER), le nombre de droits radiés
     *         (ALEnumProtocoleRadiationAffilie.NB_DROITS_RADIES) et le nombre d'annonces
     *         ALEnumProtocoleRadiationAffilie.NB_ANNONCES
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap<ALEnumProtocoleRadiationAffilie, Object> radierDossier(DossierComplexModel dossier,
            String dateRadiation, String reference) throws JadeApplicationException, JadePersistenceException;
}
