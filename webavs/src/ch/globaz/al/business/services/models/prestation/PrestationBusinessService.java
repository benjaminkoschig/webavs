package ch.globaz.al.business.services.models.prestation;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.paiement.CompensationPaiementPrestationComplexSearchModel;

/**
 * Services métiers liés aux prestations
 * 
 * @author PTA/JTS
 */
public interface PrestationBusinessService extends JadeApplicationService {
    /**
     * Retire la prestation à 0 de sa récap verrouillée (TR) pour la mettre dans une récap ouverte plus récente créé la
     * récap si nécessaire (avec période = période suivante à celle actuelle)
     * 
     * @param entete
     *            la prestation à déplacer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deplaceDansRecapOuverte(EntetePrestationModel entete) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retire la prestation de sa récap verrouillée (TR) pour la mettre dans une récap ouverte plus récente créé la
     * récap si nécessaire (avec période = période suivante à celle actuelle)
     * 
     * @param entete
     *            la prestation à déplacer
     * @param deplaceIfNonZero
     *            indique si le deplacement doit être fait si la prestation <> 0
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public void deplaceDansRecapOuverte(EntetePrestationModel entete, boolean deplaceIfNonZero)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode pour le calcul l'âge d'un enfant à la période de la génération de la prestation
     * 
     * 
     * @param droitComplex
     *            permet de récupérer la date de naissance
     * @param detailPrestation
     *            période de la génération du détail de la prestation la prestation
     * @return ageEnfant, retourne l'âge de l'enfant
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getAgeEnfantDetailPrestation(DroitComplexModel droitComplex, DetailPrestationModel detailPrestation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne l'état (CS) du l'état des prestations ouvertes (non comptabilisées) (SA ou PR si horlogères)
     * 
     * @return etat ouvert de la prestation selon paramètre caisse
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getEtatPrestationOuverte() throws JadeApplicationException, JadePersistenceException;

    /**
     * indique si une prestation peut être modifiée / supprimée ou non
     * 
     * @param etatPrestation
     *            l'état actuelle de la prestation @link {@link ALCSPrestation}
     * @return si on peut modifier ou non la prestation
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isEditable(String etatPrestation) throws JadeApplicationException, JadePersistenceException;

    /**
     * Change l'état des prestation contenue dans <code>prestations</code> à <code>etat</code>. L'état des récap
     * auquelles les prestations sont liées est aussi changé
     * 
     * @param prestations
     *            Prestations pour lesquelles changer l'état
     * @param etat
     *            Etat dans lequel passer les prestations
     *            {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT}
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void updateEtat(CompensationPaiementPrestationComplexSearchModel prestations, String etat)
            throws JadeApplicationException, JadePersistenceException;
}
