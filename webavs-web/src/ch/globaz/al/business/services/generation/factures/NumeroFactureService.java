package ch.globaz.al.business.services.generation.factures;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.models.dossier.DossierModel;

/**
 * Service permettant de g�n�rer, r�cup�rer et contr�ler des num�ros de facture
 * 
 * @author jts
 * 
 */
public interface NumeroFactureService extends JadeApplicationService {

    /**
     * V�rifie le num�ro de facture <code>numFacture</code>
     * 
     * Les conditions suivantes doivent �tre remplies pour qu'un num�ro soit valide :
     * 
     * <ul>
     * <li>Les quatre premier chiffres doivent correspondre � l'ann�e de la p�riode</li>
     * <li>Les chiffres 5 et six doivent correspondre au mois de la p�riode dans le cas d'une p�riodicit� mensuel ou
     * d'un paiement directe. Dans le cas d'une p�riodicit� trimestriel ils doivent correspondre au num�ro des
     * trimestres (41, 42, 43, 44). Pour une p�riodicit� annuelle ils doivent avoir la valeur 40</li>
     * <li>Les trois derniers chiffres doivent �tre une valeur enti�re entre 000 et 999</li>
     * </ul>
     * 
     * 
     * @param numFacture
     *            num�ro de facture � v�rifier
     * @param periode
     *            p�riode de la facture
     * @param periodicite
     *            p�riodicit� de l'affili� {@link ch.globaz.al.business.constantes.ALCSAffilie#GROUP_PERIODICITE}
     * @param bonification
     *            bonification {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI}
     * @return <code>true</code> si le num�ro de facture est valide
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public boolean checkNumFacture(String numFacture, String periode, String periodicite, String bonification)
            throws JadeApplicationException;

    /**
     * Recherche le dernier num�ro de facture de la r�cap correspondant au valeurs pass�e en param�tre
     * 
     * @param periode
     *            p�riode de la facture
     * @param periodicite
     *            p�riodicit� de l'affili� {@link ch.globaz.al.business.constantes.ALCSAffilie#GROUP_PERIODICITE}
     * @param bonification
     *            bonification {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI}
     * @param numAffilie
     *            num�ro de l'affili�
     * @param onlyOpen
     *            <code>true</code> si seulement les r�cap ouvertes doivent �tre recherch�e, <code>false</code> s'il
     *            faut tenir compte de toutes les r�caps
     * 
     * @return Le num�ro de facture trouv�, <code>null</code> si rien a �t� trouv�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getLastNumFacture(String periode, String periodicite, String bonification, String numAffilie,
            boolean onlyOpen) throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne un num�ro de facture correspondant au valeurs pass�es en param�tre. Si aucun num�ro ne correspond dans
     * une r�cap ouverte, un nouveau num�ro est g�n�r�.
     * 
     * @param periode
     *            p�riode de la facture
     * @param dossier
     *            Dossier contenant les informations permettant de conna�tre l'affili� et le type de bonification
     * 
     * @return Le num�ro de facture trouv� ou g�n�r�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getNumFacture(String periode, DossierModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne un num�ro de facture correspondant au valeurs pass�es en param�tre. Si aucun num�ro ne correspond dans
     * une r�cap ouverte, un nouveau num�ro est g�n�r�.
     * 
     * @param periode
     *            p�riode de la facture
     * @param periodicite
     *            p�riodicit� de l'affili� {@link ch.globaz.al.business.constantes.ALCSAffilie#GROUP_PERIODICITE}
     * @param bonification
     *            bonification {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI}
     * @param numAffilie
     *            num�ro de l'affili�
     * 
     * @return Le num�ro de facture trouv� ou g�n�r�
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getNumFacture(String periode, String periodicite, String bonification, String numAffilie)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * V�rifie si le num�ro de facture pass� en param�tre est disponible. Pour �tre disponible un num�ro ne doit pas
     * �tre utilis� dans une r�cap en CO
     * 
     * @param numFacture
     *            le num�ro de facture � v�rifier
     * @param numAffilie
     *            Num�ro de l'affili� li� � la facture
     * @return <code>true</code> si le num�ro de facture est disponible, <code>false</code>
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isAvailable(String numFacture, String numAffilie, String bonif) throws JadeApplicationException,
            JadePersistenceException;
}
