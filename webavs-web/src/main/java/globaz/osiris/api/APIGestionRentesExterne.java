package globaz.osiris.api;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.HashMap;
import java.util.List;

public interface APIGestionRentesExterne {

    /**
     * Ajout d'une �criture. <br/>
     * La m�thode createJournal doit avoir �t� appel�e au pr�alable.
     * 
     * @param session
     * @param transaction
     * @param idOperation
     * @param idCompteAnnexe
     * @param idSection
     * @param idRubrique
     * @param idCompteCourant
     * @param montant
     * @param date
     * @param libelle
     * @throws Exception
     */
    public void addEcriture(BSession session, BTransaction transaction, String idOperation, String idCompteAnnexe,
            String idSection, String idRubrique, String idCompteCourant, String montant, String date, String libelle)
            throws Exception;

    /**
     * Cr�er l'op�ration ordre de versement, son ordre ainsi que le versement final attach�. <br/>
     * La m�thode createOrdreGroupe doit avoir �t� appel�e au pr�alable.
     * 
     * @param session
     * @param transaction
     * @param idOperation
     * @param idCompteAnnexe
     * @param idSection
     * @param idRubrique
     * @param idCompteCourant
     * @param montant
     * @param date
     * @param libelle
     * @param idOperationVersement
     * @param idAdressePaiement
     * @param idOrganeExecution
     * @param motif
     * @param nomCache
     * @throws Exception
     */
    public void addVersement(BSession session, BTransaction transaction, String idOperation, String idCompteAnnexe,
            String idSection, String idRubrique, String idCompteCourant, String montant, String date, String libelle,
            String idOperationVersement, String idAdressePaiement, String idOrganeExecution, String motif,
            String nomCache) throws Exception;

    /**
     * Contr�le final du processus des rentes. C�d. les op�rations, sections etc. ont-elles �t� cr�� avec succ�s ?
     * 
     * @param session
     * @param transaction
     * @param rentes
     * @throws Exception
     */
    public void checkIntegrity(BSession session, BTransaction transaction, HashMap rentes) throws Exception;

    /**
     * Ferme tout les statements utilis�s.
     * 
     * @throws Exception
     */
    public void closeAllStatements() throws Exception;

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
     * Cr�er l'ordre group� ainsi que le journal de destination pour les versements.
     * 
     * @param session
     * @param transaction
     * @param libelle
     * @param dateValeur
     * @param numeroOG
     * @throws Exception
     */
    public void createOrdreGroupe(BSession session, BTransaction transaction, String libelle, String dateValeur,
            String numeroOG, String idOrganeExecution) throws Exception;

    /**
     * Cr�er un section. <br/>
     * La m�thode createJournal doit avoir �t� appel�e au pr�alable.
     * 
     * @param session
     * @param transaction
     * @param idCompteAnnexe
     * @param idSection
     * @param idExterne
     * @param creationDate
     * @param dateEcheance
     * @param dateDebutPeriode
     * @param dateFinPeridoe
     * @param montantBase
     * @param montantPaiment
     * @param domaine
     * @param typeAdresse
     * @throws Exception
     */
    public void createSection(BSession session, BTransaction transaction, String idCompteAnnexe, String idSection,
            String idExterne, String creationDate, String dateEcheance, String dateDebutPeriode, String dateFinPeridoe,
            String montantBase, String montantPaiment, String domaine, String typeAdresse) throws Exception;

    /**
     * Mise � jour de l'ordre group� (montant total et nombre de transactions).
     * 
     * @param parent
     * @param session
     * @param transaction
     * @throws Exception
     */
    public List<String> bouclerOG(BProcess parent, BSession session, BTransaction transaction) throws Exception;

    /**
     * Retrouve ou cr�er (avec incr�mentation +10) une section pour les blocages de prestations.
     * 
     * @param session
     * @param transaction
     * @param idCompteAnnexe
     * @param idExterneRole
     * @param idRole
     * @param annee
     *            Exemple : 200874000 => 2008
     * @return
     * @throws Exception
     */
    public APISection getOrCreateLastSectionForPrestationsBloquees(BSession session, BTransaction transaction,
            APIGestionComptabiliteExterne comptaExterne, String idCompteAnnexe, String idExterneRole, String idRole,
            String annee) throws Exception;

    /**
     * Mise � jour des op�rations de type ordre de versement en �tat vers�. <br>
     * <i>Commit effectu� dans la m�thode.</i>
     * 
     * @param session
     * @throws Exception
     */
    public void updateOperationOrdreVersementInEtatVerse(BSession session) throws Exception;
}
