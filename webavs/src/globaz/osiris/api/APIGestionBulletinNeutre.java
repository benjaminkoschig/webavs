package globaz.osiris.api;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;

/**
 * Impl�mentation de m�thodes sp�fiques au processus du bulletin neutre.
 * 
 * @author DDA
 * 
 */
public interface APIGestionBulletinNeutre {

    /**
     * Ajouter la nouvelle op�ration de bulletin neutre pr�c�dement cr��e et renseign�e.
     * 
     * @param session
     * @param transaction
     * @param operation
     * @throws Exception
     */
    public void addOperationBulletinNeutre(BSession session, BTransaction transaction,
            APIOperationBulletinNeutre operation) throws Exception;

    /**
     * Comptabiliser le journal de bulletin neutre cr�� pr�c�dement.
     * 
     * @param session
     * @param parent
     */
    public void comptabiliser(BSession session, BProcess parent) throws Exception;

    /**
     * Cr�er le journal de base. Contr�le que la p�riode est bien ouverte pour la date de valeur.
     * 
     * @param session
     * @param transaction
     * @param libelle
     * @param dateValeur
     * @throws Exception
     */
    public void createJournal(BSession session, BTransaction transaction, String libelle, String dateValeur)
            throws Exception;

    /**
     * Cr�er une nouvelle op�ration de type bulletin neutre.
     * 
     * @param session
     * @param transaction
     * @return
     */
    public APIOperationBulletinNeutre createOperationBulletinNeutre(BSession session, BTransaction transaction)
            throws Exception;

    /**
     * Cr�er une section de type APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE
     * 
     * @param session
     * @param transaction
     * @param idCompteAnnexe
     * @param idExterne
     * @param domaine
     * @param typeAdresse
     * @param nonImprimable
     * @param idCaisseProf
     * @return
     * @throws Exception
     */
    public APISection createSection(BSession session, BTransaction transaction, String idCompteAnnexe,
            String idExterne, String domaine, String typeAdresse, Boolean nonImprimable, String idCaisseProf)
            throws Exception;

    /**
     * see {@link APIGestionComptabiliteExterne}{@link #getCompteAnnexeByRole(BSession session, BTransaction
     * transaction, String, String, String)}
     * 
     * @param idTiers
     * @param idRole
     * @param idExterneRole
     * @return
     */
    public APICompteAnnexe getCompteAnnexeByRole(BSession session, BTransaction transaction, String idTiers,
            String idRole, String idExterneRole) throws Exception;

    /**
     * Return l'id rubrique � laquelle il faut ajouter la taxe de sommation du bulletin neutre gr�ce � la r�f�rence
     * rubrique.
     * 
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public String getIdRubriquesTaxeSommation(BSession session, BTransaction transaction) throws Exception;

    /**
     * Return le journal g�n�r�.
     * 
     * @param session
     * @return
     */
    public APIJournal getJournal(BSession session) throws Exception;

    /**
     * Return le montant de la taxe de sommation d'un bulletin neutre.
     * 
     * @param session
     * @param transaction
     * @param idCompteAnnexe
     * @param idRubriqueCompteur
     * @param annee
     * @return
     * @throws Exception
     */
    public String getMontantTaxeSommation(BSession session, BTransaction transaction, String idCompteAnnexe,
            String idRubriqueCompteur, String annee) throws Exception;

}
