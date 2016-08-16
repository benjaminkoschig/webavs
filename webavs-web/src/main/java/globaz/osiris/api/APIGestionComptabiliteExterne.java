package globaz.osiris.api;

import globaz.globall.api.BIMessageLog;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.FWPKProvider;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (20.09.2002 16:11:01)
 * 
 * @author: Administrator
 */
/**
 * @author SCH
 * @revision SCO 18 mars 2010
 */
public interface APIGestionComptabiliteExterne {

    public void addOperation(APIOperation oper);

    /**
     * Cl�ture du process. Cette m�thode doit �tre appel�e � la fin du processus de comptabilisation. Elle assume les
     * fonctions suivantes :
     * <p>
     * <ul>
     * v�rification du bon d�roulement du processus
     * <ul>
     * sauvegarde du log des messages
     * <ul>
     * d�clencher la mise en compte s'il n'y a pas d'erreurs
     * <ul>
     * communiquer le log des messages et le r�sultat de la comptabilisatoin par e-mail � l'adresse fournie Date de
     * cr�ation : (16.01.2002 08:40:25)
     */
    public void comptabiliser();

    /**
     * Instancier une nouvelle op�ration Date de cr�ation : (21.01.2002 11:36:44)
     * 
     * @return globaz.osiris.interfaceext.comptes.IntOperation
     */
    public APIEcriture createEcriture();

    public APIEcriture createEcriture(String idEcriture);

    /**
     * Cr�ation du journal de comptabilisation Date de cr�ation : (15.01.2002 16:45:01)
     */
    public APIJournal createJournal();

    /**
     * Cr�ation du journal de comptabilisation de type Facturation
     */

    public APIJournal createJournalTypeFacturation();

    /**
     * Cr�e un ordre de recouvrement Date de cr�ation : (11.11.2005 07:20:04)
     * 
     * @return globaz.osiris.interfaceext.comptes.APIOperationOrdreRecouvrement
     */
    public APIOperationOrdreRecouvrement createOperationOrdreRecouvrement();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.02.2002 09:20:04)
     * 
     * @return globaz.osiris.interfaceext.comptes.IntOperationOrdreVersement
     */
    public APIOperationOrdreVersement createOperationOrdreVersement();

    /**
     * Cette m�thode permet de cr�er une section. Les zones suivantes sont mises � jour :<br>
     * <li>idJournal</li>; <li>idTypeSection</li> <li>idCompteAnnexe</li> <li>
     * idExterne</li> <li>date</li>
     * 
     * @param idCompteAnnexe
     * @param idTypeSection
     * @param idExterne
     * @param domaine
     * @param typeAdresse
     * @return APISection, null en cas d'erreurs
     */
    public APISection createSection(String idCompteAnnexe, String idTypeSection, String idExterne, String domaine,
            String typeAdresse);

    /**
     * Cette m�thode permet de cr�er une section. Les zones suivantes sont mises � jour :<br>
     * <li>idJournal</li>; <li>idTypeSection</li> <li>idCompteAnnexe</li> <li>
     * idExterne</li> <li>date</li>
     * 
     * @param idCompteAnnexe
     * @param idTypeSection
     * @param idExterne
     * @param domaine
     * @param typeAdresse
     * @param nonImprimable
     * @return APISection, null en cas d'erreurs
     */
    public APISection createSection(String idCompteAnnexe, String idTypeSection, String idExterne, String domaine,
            String typeAdresse, Boolean nonImprimable);

    /**
     * Cette m�thode permet de cr�er une section. Les zones suivantes sont mises � jour :<br>
     * <li>idJournal</li>; <li>idTypeSection</li> <li>idCompteAnnexe</li> <li>
     * idExterne</li> <li>date</li>
     * 
     * @param idSection
     * @param idCompteAnnexe
     * @param idTypeSection
     * @param idExterne
     * @param domaine
     * @param typeAdresse
     * @param nonImprimable
     * @param idCaisseProf
     * @return APISection, null en cas d'erreurs
     */
    public APISection createSection(String idSection, String idCompteAnnexe, String idTypeSection, String idExterne,
            String domaine, String typeAdresse, Boolean nonImprimable, String idCaisseProf);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29.01.2002 17:21:28)
     * 
     * @return globaz.osiris.interfaceext.comptes.IntCompteAnnexe
     */
    public APICompteAnnexe getCompteAnnexeById(String idCompteAnnexe);

    /**
     * Retourne un compte annexe qui correspond au r�le. Si le compte annexe n'existe pas, on ouvre un nouveau compte
     * Date de cr�ation : (29.01.2002 15:57:08)
     * 
     * @return globaz.osiris.intefaceext.comptes.IntCompteAnnexe
     */
    public APICompteAnnexe getCompteAnnexeByRole(String idTiers, String idRole, String idExterneRole);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.01.2002 16:35:20)
     * 
     * @return globaz.osiris.interfaceext.comptes.IntJournal
     */
    public APIJournal getJournal();

    /**
     * Retourne le log.
     * 
     * @return
     */
    public BIMessageLog getMessageLog();

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31.01.2002 16:22:14)
     * 
     * @return globaz.osiris.interfaceext.comptes.IntSection
     */
    public APISection getSectionByIdExterne(String idCompteAnnexe, String idTypeSection, String idExterne);

    /**
     * M�thode permettant de retrouver la section. Utilis� par Facturation car cette derni�re passe le domaine et
     * typeAdresse en plus.
     * 
     * @param idCompteAnnexe
     * @param idTypeSection
     * @param idExterne
     * @param domaine
     * @param typeAdresse
     * @return
     */
    public APISection getSectionByIdExterne(String idCompteAnnexe, String idTypeSection, String idExterne,
            String domaine, String typeAdresse);

    /**
     * M�thode permettant de retrouver la section. Utilis� par Facturation car cette derni�re passe nonImprimable.
     * 
     * @param idCompteAnnexe
     * @param idTypeSection
     * @param idExterne
     * @param domaine
     * @param typeAdresse
     * @param nonImprimable
     * @return
     */
    public APISection getSectionByIdExterne(String idCompteAnnexe, String idTypeSection, String idExterne,
            String domaine, String typeAdresse, Boolean nonImprimable, FWPKProvider pkProvider);

    /**
     * Retourne vrai s'il y a au moins une erreur fatale Date de cr�ation : (15.01.2002 17:42:15)
     * 
     * @return boolean
     */
    public boolean hasFatalErrors();

    /**
     * La p�riode comptable est-elle ouverte (non cl�tur�e) en comptabilit� g�n�rale.
     * 
     * @param session
     * @param transaction
     * @param dateValeurCG
     * @return
     */
    public boolean isPeriodeComptableOuverte(String dateValeurCG);

    /**
     * Cr�ation de l'ordre group� correspondant au journal.<BR>
     * <BR>
     * - Cr�ation d'un ordre group� avec les ordres du journal sp�cifi�<BR>
     * - Pr�paration de l'ordre group�<BR>
     * <BR>
     * 
     * @param idOrganeExecution
     * @param numeroOG
     * @param dateEcheance
     * @param typeOrdre
     * @param natureOrdre
     * @param libelleOG <br/>
     *            {params si le traitement OG de l'OE est de type ISO20022}
     * @param isoCsTypeAvis Cs du type d'avis ISO20022
     * @param isoGestionnaire nom du gestionnaire pour le traitement 20022
     * @param isoHighPriority (globaz boolean string [1-true/0-2 false])
     */
    public void preparerOrdreGroupe(String idOrganeExecution, String numeroOG, String dateEcheance, String typeOrdre,
            String natureOrdre, String libelleOG, String isoCsTypeAvis, String isoGestionnaire, String isoHighPriority);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.01.2002 13:41:01)
     * 
     * @param param
     *            java.lang.String
     */
    public void setDateValeur(String dateValeur);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2002 09:15:15)
     * 
     * @param address
     *            java.lang.String
     */
    public void setEMailAddress(String address);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.01.2002 13:38:22)
     * 
     * @param newLibelle
     *            java.lang.String
     */
    public void setLibelle(String newLibelle);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.01.2002 13:38:22)
     * 
     * @param newLibelle
     *            java.lang.String
     */
    public void setMessageLog(BIMessageLog messageLog);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.11.2002 08:34:38)
     * 
     * @param process
     *            BProcess
     */
    public void setProcess(BProcess process);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (16.01.2002 08:59:41)
     * 
     * @param value
     *            boolean
     */
    public void setSendCompletionMail(boolean value);

    /**
     * Passer une session de type OSIRIS !
     * 
     * @param newSession
     */
    public void setSession(BSession newSession);

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (15.01.2002 16:41:27)
     * 
     * @param param
     *            globaz.globall.db.BTransaction
     */
    public void setTransaction(BITransaction transaction);
}
