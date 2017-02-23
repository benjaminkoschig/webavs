package globaz.osiris.api;

import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import java.util.HashMap;
import java.util.List;

public interface APIGestionRentesExterne {

    /**
     * Ajout d'une écriture. <br/>
     * La méthode createJournal doit avoir été appelée au préalable.
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
     * Créer l'opération ordre de versement, son ordre ainsi que le versement final attaché. <br/>
     * La méthode createOrdreGroupe doit avoir été appelée au préalable.
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
     * Contrôle final du processus des rentes. Càd. les opérations, sections etc. ont-elles été créé avec succès ?
     * 
     * @param session
     * @param transaction
     * @param rentes
     * @throws Exception
     */
    public void checkIntegrity(BSession session, BTransaction transaction, HashMap rentes) throws Exception;

    /**
     * Ferme tout les statements utilisés.
     * 
     * @throws Exception
     */
    public void closeAllStatements() throws Exception;

    /**
     * Créer le journal de base. Contrôle que la période est bien ouverte pour la date de valeur.
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
     * Créer l'ordre groupé ainsi que le journal de destination pour les versements.
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
     * Créer un section. <br/>
     * La méthode createJournal doit avoir été appelée au préalable.
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
     * Mise à jour de l'ordre groupé (montant total et nombre de transactions).
     * 
     * @param parent
     * @param session
     * @param transaction
     * @throws Exception
     */
    public List<String> bouclerOG(BProcess parent, BSession session, BTransaction transaction) throws Exception;

    /**
     * Retrouve ou créer (avec incrémentation +10) une section pour les blocages de prestations.
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
     * Mise à jour des opérations de type ordre de versement en état versé. <br>
     * <i>Commit effectué dans la méthode.</i>
     * 
     * @param session
     * @throws Exception
     */
    public void updateOperationOrdreVersementInEtatVerse(BSession session) throws Exception;
}
