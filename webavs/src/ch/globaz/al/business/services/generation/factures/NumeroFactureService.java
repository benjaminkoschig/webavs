package ch.globaz.al.business.services.generation.factures;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierModel;

/**
 * Service permettant de générer, récupérer et contrôler des numéros de facture
 * 
 * @author jts
 * 
 */
public interface NumeroFactureService extends JadeApplicationService {

    /**
     * Vérifie le numéro de facture <code>numFacture</code>
     * 
     * Les conditions suivantes doivent être remplies pour qu'un numéro soit valide :
     * 
     * <ul>
     * <li>Les quatre premier chiffres doivent correspondre à l'année de la période</li>
     * <li>Les chiffres 5 et six doivent correspondre au mois de la période dans le cas d'une périodicité mensuel ou
     * d'un paiement directe. Dans le cas d'une périodicité trimestriel ils doivent correspondre au numéro des
     * trimestres (41, 42, 43, 44). Pour une périodicité annuelle ils doivent avoir la valeur 40</li>
     * <li>Les trois derniers chiffres doivent être une valeur entière entre 000 et 999</li>
     * </ul>
     * 
     * 
     * @param numFacture
     *            numéro de facture à vérifier
     * @param periode
     *            période de la facture
     * @param periodicite
     *            périodicité de l'affilié {@link ch.globaz.al.business.constantes.ALCSAffilie#GROUP_PERIODICITE}
     * @param bonification
     *            bonification {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI}
     * @return <code>true</code> si le numéro de facture est valide
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public boolean checkNumFacture(String numFacture, String periode, String periodicite, String bonification)
            throws JadeApplicationException;

    /**
     * Recherche le dernier numéro de facture de la récap correspondant au valeurs passée en paramètre
     * 
     * @param periode
     *            période de la facture
     * @param periodicite
     *            périodicité de l'affilié {@link ch.globaz.al.business.constantes.ALCSAffilie#GROUP_PERIODICITE}
     * @param bonification
     *            bonification {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI}
     * @param numAffilie
     *            numéro de l'affilié
     * @param onlyOpen
     *            <code>true</code> si seulement les récap ouvertes doivent être recherchée, <code>false</code> s'il
     *            faut tenir compte de toutes les récaps
     * 
     * @return Le numéro de facture trouvé, <code>null</code> si rien a été trouvé
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getLastNumFacture(String periode, String periodicite, String bonification, String numAffilie,
            boolean onlyOpen) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne un numéro de facture correspondant au valeurs passées en paramètre. Si aucun numéro ne correspond dans
     * une récap ouverte, un nouveau numéro est généré.
     * 
     * @param periode
     *            période de la facture
     * @param dossier
     *            Dossier contenant les informations permettant de connaître l'affilié et le type de bonification
     * 
     * @return Le numéro de facture trouvé ou généré
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getNumFacture(String periode, DossierModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne un numéro de facture correspondant au valeurs passées en paramètre. Si aucun numéro ne correspond dans
     * une récap ouverte, un nouveau numéro est généré.
     * 
     * @param periode
     *            période de la facture
     * @param periodicite
     *            périodicité de l'affilié {@link ch.globaz.al.business.constantes.ALCSAffilie#GROUP_PERIODICITE}
     * @param bonification
     *            bonification {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI}
     * @param numAffilie
     *            numéro de l'affilié
     * 
     * @return Le numéro de facture trouvé ou généré
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getNumFacture(String periode, String periodicite, String bonification, String numAffilie)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Vérifie si le numéro de facture passé en paramètre est disponible. Pour être disponible un numéro ne doit pas
     * être utilisé dans une récap en CO
     * 
     * @param numFacture
     *            le numéro de facture à vérifier
     * @param numAffilie
     *            Numéro de l'affilié lié à la facture
     * @return <code>true</code> si le numéro de facture est disponible, <code>false</code>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isAvailable(String numFacture, String numAffilie, String bonif) throws JadeApplicationException,
            JadePersistenceException;
}
