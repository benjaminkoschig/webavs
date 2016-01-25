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
 * Services m�tiers li�s aux prestations
 * 
 * @author PTA/JTS
 */
public interface PrestationBusinessService extends JadeApplicationService {
    /**
     * Retire la prestation � 0 de sa r�cap verrouill�e (TR) pour la mettre dans une r�cap ouverte plus r�cente cr�� la
     * r�cap si n�cessaire (avec p�riode = p�riode suivante � celle actuelle)
     * 
     * @param entete
     *            la prestation � d�placer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void deplaceDansRecapOuverte(EntetePrestationModel entete) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retire la prestation de sa r�cap verrouill�e (TR) pour la mettre dans une r�cap ouverte plus r�cente cr�� la
     * r�cap si n�cessaire (avec p�riode = p�riode suivante � celle actuelle)
     * 
     * @param entete
     *            la prestation � d�placer
     * @param deplaceIfNonZero
     *            indique si le deplacement doit �tre fait si la prestation <> 0
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public void deplaceDansRecapOuverte(EntetePrestationModel entete, boolean deplaceIfNonZero)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode pour le calcul l'�ge d'un enfant � la p�riode de la g�n�ration de la prestation
     * 
     * 
     * @param droitComplex
     *            permet de r�cup�rer la date de naissance
     * @param detailPrestation
     *            p�riode de la g�n�ration du d�tail de la prestation la prestation
     * @return ageEnfant, retourne l'�ge de l'enfant
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getAgeEnfantDetailPrestation(DroitComplexModel droitComplex, DetailPrestationModel detailPrestation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne l'�tat (CS) du l'�tat des prestations ouvertes (non comptabilis�es) (SA ou PR si horlog�res)
     * 
     * @return etat ouvert de la prestation selon param�tre caisse
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getEtatPrestationOuverte() throws JadeApplicationException, JadePersistenceException;

    /**
     * indique si une prestation peut �tre modifi�e / supprim�e ou non
     * 
     * @param etatPrestation
     *            l'�tat actuelle de la prestation @link {@link ALCSPrestation}
     * @return si on peut modifier ou non la prestation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isEditable(String etatPrestation) throws JadeApplicationException, JadePersistenceException;

    /**
     * Change l'�tat des prestation contenue dans <code>prestations</code> � <code>etat</code>. L'�tat des r�cap
     * auquelles les prestations sont li�es est aussi chang�
     * 
     * @param prestations
     *            Prestations pour lesquelles changer l'�tat
     * @param etat
     *            Etat dans lequel passer les prestations
     *            {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_ETAT}
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void updateEtat(CompensationPaiementPrestationComplexSearchModel prestations, String etat)
            throws JadeApplicationException, JadePersistenceException;
}
