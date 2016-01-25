package globaz.helios.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Cette classe repr�sente un journal comptable. Un journal est li� � un <b>Exercice Comptable</b>
 * {@link ICGExerciceComptable} et peut �tre li� � une <b>P�riode Comptable</b> {@link ICGPeriodeComptable}.<br>
 * Pour r�cup�rer un journal depuis la base de donn�es, il faut fournir son ID et ex�cuter la m�thode <b>retrieve</b>.<br>
 * <br>
 * Si l'ID du journal est inconnu, on peut utiliser la m�thode <b>setIdJournalFrom</b> avec comme param�tre l'ID d'une
 * p�riode comptable et l'ID d'un exercice comptable. <br>
 * Un journal contient des <b>�critures</b> {@link ICGEcritureDouble} et peut �tre comptabilis� via la m�thode
 * <b>comptabiliser</b>.<br>
 * On peut annuler une comptabilisation via la m�thode <b>exComptabiliser</b>.<br>
 * On peut vider un journal de ses �critures (sans le d�truire) avec la m�thode <b>effacer</b>.<br>
 * On peut d�truire un journal avec la m�thode <b>supprimer</b>.<br>
 * <br>
 * La (d�)comptabilisation, la supression et l'effacement d'un journal sont des processus qui changent l'�tat du journal
 * et cr��ent des logs.<br>
 * L'�tat du journal est consultable via la m�thode <b>getIdEtat</b> et son log via <b>getLogs</b>.<br>
 * <br>
 * Si un journal est <b>En traitement</b>, on ne peut lancer un nouveau processus sur celui-ci.<br>
 * Si un journal est <b>Annul�</b> ou en <b>Erreur</b>, on ne peut que le supprimer ou l'effacer.<br>
 * <br>
 * Exemple complet : <br>
 * 
 * <pre>
 * <code>
 * try {
 * 	BIApplication remoteApplication = GlobazSystem.getApplication(&quot;HELIOS&quot;);
 * 	BISession remoteSession = remoteApplication.newSession(&quot;userfr&quot;, &quot;userfr&quot;);
 * 	BITransaction transaction = remoteSession.newTransaction();
 * 	transaction.openTransaction();
 * 
 * 	String idMandat = &quot;900&quot;;
 * 	String code = &quot;11&quot;;
 * 
 * 	String date = &quot;01.01.2003&quot;;
 * 
 * 	ICGExerciceComptable exercice = (ICGExerciceComptable) remoteSession.getAPIFor(ICGExerciceComptable.class);
 * 	ICGPeriodeComptable periode = (ICGPeriodeComptable) remoteSession.getAPIFor(ICGPeriodeComptable.class);
 * 
 * 	ICGJournal journal = (ICGJournal) remoteSession.getAPIFor(ICGJournal.class);
 * 
 * 	try {
 * 
 * 		// On ne connait pas l'ID de l'exercice, on le r�cup�re via une date et l'ID du mandat
 * 		exercice.setIdExerciceComptableFrom(date, idMandat);
 * 
 * 		// On ne connait pas l'ID de la p�riode, on le r�cup�re via l'ID d'un exercice et d'un code standard AVS
 * 		periode.setIdPeriodeComptableFrom(code, exercice.getIdExerciceComptable());
 * 
 * 		// On ne connait pas l'ID du journal, on le r�cup�re via l'ID d'un exercice et l'ID d'une p�riode
 * 		journal.setIdJournalFrom(periode.getIdPeriodeComptable(), exercice.getIdExerciceComptable());
 * 
 * 		journal.retrieve(transaction);
 * 
 * 		if (journal.isNew())
 * 			System.err.println(&quot;Aucun journal.&quot;);
 * 		else {
 * 			journal.comptabiliser(transaction);
 * 
 * 			// N'oublions pas que 'comptabiliser' est un processus trait� en PARALLELE, donc il faut
 * 			// recharger le journal pour connaitre son �tat.
 * 			while (journal.getIdEtat().equals(CS_ETAT_TRAITEMENT)) {
 * 				journal.retrieve(transaction);
 * 			}
 * 			System.out.prinln(journal.getEtatLibelle());
 * 		}
 * 
 * 		// Cr�ation d'un journal !
 * 
 * 		ICGJournal journalNew = (ICGJournal) remoteSession.getAPIFor(ICGJournal.class);
 * 		journalNew.setDate(&quot;12.04.2003&quot;);
 * 		journalNew.setDateValeur(&quot;01.04.2003&quot;);
 * 		journalNew.setIdPeriodeComptable(periode.getIdPeriodeComptable());
 * 		journalNew.setIdExerciceComptable(exercice.getIdExerciceComptable());
 * 		journalNew.setLibelle(&quot;Mon nouveau journal&quot;);
 * 		journalNew.setIdTypeJournal(CS_TYPE_MANUEL);
 * 
 * 		journalNew.add(transaction);
 * 
 * 	} catch (Exception e) {
 * 		transaction.closeTransaction();
 * 		e.printStackTrace();
 * 	}
 * 	if (remoteSession.hasErrors()) {
 * 		System.err.println(&quot;Non-fatal errors : &quot; + remoteSession.getErrors().toString());
 * 	}
 * } catch (Exception e) {
 * 	System.err.println(&quot;Fatal errors : &quot; + e.printStackTrace());
 * }
 * </code>
 * </pre>
 * 
 * @author Gr�goire PICQUOT (gregoire.picquot@globaz.ch)
 * @see ICGPeriodeComptable
 * @see ICGExerciceComptable
 * @see ICGECritureDouble
 */
public interface ICGJournal extends BIEntity {
    public final static String CS_ETAT_ANNULE = "711005";
    public final static String CS_ETAT_COMPTABILISE = "711002";
    public final static String CS_ETAT_ERREUR = "711003";
    // Etat du journal
    public final static String CS_ETAT_OUVERT = "711001";
    public final static String CS_ETAT_PARTIEL = "711004";
    public final static String CS_ETAT_TOUS = "711007";
    public final static String CS_ETAT_TRAITEMENT = "711006";

    public final static String CS_TYPE_AUTOMATIQUE = "713002";
    public final static String CS_TYPE_ECRITURES_MODELES = "713005";
    public final static String CS_TYPE_JOURNALIER = "713004";
    // Type du journal
    public final static String CS_TYPE_MANUEL = "713001";
    public final static String CS_TYPE_SYSTEME = "713003";

    /**
     * Ajout du journal dans la base de donn�es.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si l'ajout a �chou�
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Effacement du journal (Processus) : vide le journal de toutes ses �critures. Met � jour son log et son �tat. Date
     * de cr�ation : (09.04.2003 16:25:47)
     * 
     * @param transaction
     *            globaz.globall.api.BITransaction
     */
    public void annuler(BITransaction transaction) throws Exception;

    /**
     * Effacement du journal (Processus) : vide le journal de toutes ses �critures. Met � jour son log et son �tat.
     * 
     * @param transaction
     * @param emailAddress
     * @throws Exception
     */
    public void annuler(BITransaction transaction, String emailAddress) throws Exception;

    /**
     * Comptabilisation du journal (Processus) : comptabilise toutes les �critures non comptabilis�es du journal. Met �
     * jour son log et son �tat. Date de cr�ation : (09.04.2003 16:25:12)
     * 
     * @param transaction
     *            globaz.globall.api.BITransaction
     */
    public void comptabiliser(BITransaction transaction) throws Exception;

    /**
     * Comptabilisation du journal (Processus) : comptabilise toutes les �critures non comptabilis�es du journal. Met �
     * jour son log et son �tat.
     * 
     * @param transaction
     * @param emailAddress
     * @throws Exception
     */
    public void comptabiliser(BITransaction transaction, String emailAddress) throws Exception;

    /**
     * D�comptabilisation du journal (Processus) : d�comptabilise toutes les �critures comptabilis�es du journal. Met �
     * jour son log et son �tat. Date de cr�ation : (09.04.2003 16:25:32)
     * 
     * @param transaction
     *            globaz.globall.api.BITransaction
     */
    public void exComptabiliser(BITransaction transaction) throws Exception;

    /**
     * Renvoie la date du journal.
     * 
     * @return la date du journal
     */
    public java.lang.String getDate();

    /**
     * Renvoie la date de valeur du journal.
     * 
     * @return la date de valeur du journal
     */
    public java.lang.String getDateValeur();

    /**
     * Teste si le journal est confidentiel.
     * 
     * @param newEstConfidentiel
     *            newEstConfidentiel
     */
    public Boolean getEstConfidentiel();

    /**
     * Teste si le journal est public (d'autres utilisateurs peuvent manipuler les �critures).
     * 
     * @param newEstPublic
     *            newEstPublic
     */
    public Boolean getEstPublic();

    /**
     * Renvoie le libelle de l'�tat du journal (Plus explicite que l'ID de l'�tat).
     * 
     * @return le libelle de l'�tat du journal
     */
    public java.lang.String getEtatLibelle() throws java.lang.Exception;

    /**
     * Renvoie la description compl�te du journal.
     * 
     * @return la description compl�te du journal
     */
    public String getFullDescription() throws java.lang.Exception;

    /**
     * Renvoie l'ID de l'�tat du journal.
     * 
     * @return l'ID de l'�tat du journal
     */
    public java.lang.String getIdEtat();

    /**
     * Renvoie l'ID de l'exercice comptable.
     * 
     * @return l'ID de la p�riode comptable
     */
    public java.lang.String getIdExerciceComptable();

    /**
     * Renvoie l'ID du journal.
     * 
     * @return l'ID du journal
     */
    public java.lang.String getIdJournal();

    /**
     * Renvoie l'ID de la p�riode comptable.
     * 
     * @return l'ID de la p�riode comptable
     */
    public java.lang.String getIdPeriodeComptable();

    /**
     * Renvoie l'ID du type du journal.
     * 
     * @return l'ID du type du journal
     */
    public java.lang.String getIdTypeJournal();

    /**
     * Renvoie le libell� du journal.
     * 
     * @return le libell� du journal
     */
    public java.lang.String getLibelle();

    /**
     * Renvoie le num�ro standard (calcul� automatiquement) du journal.
     * 
     * @return le num�ro standard du journal.
     */
    public java.lang.String getNumero();

    /**
     * Renvoie le propri�taire du journal (rempli automatiquement).
     * 
     * @return le propri�taire du journal
     */
    public java.lang.String getProprietaire();

    /**
     * Renvoie la r�f�rence externe (???) du journal.
     * 
     * @return la r�f�rence externe du journal
     */
    public java.lang.String getReferenceExterne();

    /**
     * R�cup�re le journal depuis la base de donn�es
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la r�cup�ration �choue
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * D�finit la date du journal.
     * 
     * @param newDate
     *            newDate
     */
    public void setDate(java.lang.String newDate);

    /**
     * D�finit la date de valeur du journal.
     * 
     * @param newDateValeur
     *            newDateValeur
     */
    public void setDateValeur(java.lang.String newDateValeur);

    public void setEstConfidentiel(Boolean estPublic);

    public void setEstPublic(Boolean estPublic);

    /**
     * D�finit l'ID de l'exercice comptable li�e au journal.
     * 
     * @param newIdPeriodeComptable
     *            newIdPeriodeComptable
     */
    public void setIdExerciceComptable(java.lang.String newIdExerciceComptable);

    /**
     * D�finit l'ID du journal.
     * 
     * @param newIdJournal
     *            newIdJournal
     */
    public void setIdJournal(java.lang.String newIdJournal);

    /**
     * D�finit l'ID du journal via un ID de p�riode comptable et un ID d'exercice comptable.
     * 
     * @param codePeriode
     *            codePeriode
     * @param idExerciceComptable
     *            idExerciceComptable
     * @exception java.lang.Exception
     *                si ???
     */
    public void setIdJournalFrom(java.lang.String codePeriode, java.lang.String idExerciceComptable)
            throws java.lang.Exception;

    /**
     * D�finit l'ID de la p�riode comptable li�e au journal.
     * 
     * @param newIdPeriodeComptable
     *            newIdPeriodeComptable
     */
    public void setIdPeriodeComptable(java.lang.String newIdPeriodeComptable);

    /**
     * D�finit l'ID du type du journal.
     * 
     * @param newIdTypeJournal
     *            newIdTypeJournal
     */
    public void setIdTypeJournal(java.lang.String newIdTypeJournal);

    /**
     * D�finit le libell� du journal.
     * 
     * @param newLibelle
     *            newLibelle
     */
    public void setLibelle(java.lang.String newLibelle);

    /**
     * D�finit la r�f�rence externe du journal (???).
     * 
     * @param newReferenceExterne
     *            newReferenceExterne
     */
    public void setReferenceExterne(java.lang.String newReferenceExterne);

    /**
     * Met � jour le journal.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la mise � jour �coue
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
