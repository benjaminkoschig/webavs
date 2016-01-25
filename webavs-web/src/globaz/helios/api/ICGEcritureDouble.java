package globaz.helios.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Cette classe repr�sente une �criture double pour une comptabilit� AVS. Une �criture est enregistr�e dans un
 * <b>Journal</b> {@link ICGJournal}.<br>
 * Pour l'utiliser, il faut OBLIGATOIREMENT pr�ciser les param�tres suivants :<br>
 * <li>libelle : le libell� de l'�criture<br> <li>montant : le montant de l'�criture<br> <li>date : la date de
 * l'�criture<br> <li>idJournal : l'ID du journal dans lequel l'�criture va �tre enregistr�e<br> <li>
 * idExerciceComptable : l'ID de l'exercice comptable li� � l'�criture<br> <li>
 * numeroCompteCredite : le num�ro � 12 chiffres (par ex:'1000.1011.0000') du compte AVS � cr�diter<br> <li>
 * numeroCompteDebite : le num�ro � 12 chiffres (par ex:'2000.1000.0000') du compte AVS � d�biter<br>
 * <br>
 * Les param�tres suivants sont facultatifs :<br> <li>remarque : une remarque peut �tre associ�e � cette �criture<br>
 * <li>idSecteurAVS : on peut pr�ciser l'ID du secteur AVS li� � cette �criture (par ex: '2000', '3000')<br> <li>
 * piece : l'�criture peut �tre associ�e � une pi�ce comptable<br>
 * 
 * <li>montantMonnaieEtrangere : Pour les comptes en monnaie �trang�re, 2 des 3 valeurs suivantes sont<br>
 * obligatoire : montant, montantMonnaieEtrangere, cours (la troisi�me est calcul�e automatiquement si non renseign�e).
 * <br> <li>cours : Pour les comptes en monnaie �trang�re, 2 des 3 valeurs suivantes sont<br>
 * obligatoire : montant, montantMonnaieEtrangere, cours (la troisi�me est calcul�e automatiquement si non renseign�e).<br>
 * 
 * Example de valeurs pour un compte en monnaie �trang�re : <br>
 * Montant : 100.- CHF<br>
 * Cours : 0.68<br>
 * Montant Monnaie �trang�re : 68.-<br>
 * <br>
 * Exemple complet :
 * 
 * <pre>
 * <code>
 * try {
 * 	BIApplication remoteApplication = GlobazSystem.getApplication(&quot;HELIOS&quot;);
 * 	BISession remoteSession = remoteApplication.newSession(&quot;userfr&quot;, &quot;userfr&quot;);
 * 	BITransaction transaction = remoteSession.newTransaction();
 * 	transaction.openTransaction();
 * 
 * 	ICGJournal journal = (ICGJournal) remoteSession.getAPIFor(ICGJournal.class);
 * 	ICGExerciceComptable exercice = (ICGExerciceComptable) remoteSession.getAPIFor(ICGExerciceComptable.class);
 * 	ICGEcritureDouble ecriture = (ICGEcritureDouble) remoteSession.getAPIFor(ICGEcritureDouble.class);
 * 
 * 	String idMandat = &quot;900&quot;;
 * 
 * 	try {
 * 
 * 		// R�cup�ration de l'ID de l'exercice comptable via une date
 * 		exercice.setIdExerciceComptableFrom(&quot;01.01.2003&quot;, idMandat);
 * 
 * 		// R�cup�ration de l'ID du journal via un code de p�riode (standard AVS, ici &quot;03&quot; = Mars)
 * 		journal.setIdJournalFrom(exercice.getIdExercice(), &quot;03&quot;);
 * 
 * 		// Cr�ation d'une �criture double
 * 		ecriture.setIdExerciceComptable(exercice.getIdExerciceComptable());
 * 		ecriture.setIdJournal(journal.getIdJournal());
 * 		ecriture.setLibelle(&quot;Mon �criture&quot;);
 * 		ecriture.setMontant(&quot;9'563'200.55&quot;);
 * 		ecriture.setDate(&quot;04.03.2003&quot;);
 * 		ecriture.setNumeroCompteDebite(&quot;1000.1011.0000&quot;);
 * 		ecriture.setNumeroCompteCredite(&quot;2000.1200.0000&quot;);
 * 
 * 		ecriture.add(transaction);
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
 * @see ICGJournal
 * @see ICGExerciceComptable
 */
public interface ICGEcritureDouble extends BIEntity {
    /**
     * Ajoute l'�criture en base de donn�es.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si l'ajout �choue
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Efface l'�criture en base de donn�es.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la suppression �choue
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoie la valeur de la propri�t� cours
     * 
     * @return la valeur de la propri�t� cours
     */
    public String getCours();

    /**
     * Renvoie la valeur de la propri�t� date
     * 
     * @return la valeur de la propri�t� date
     */
    public String getDate();

    /**
     * Renvoie l'ID de l'�criture.
     * 
     * @return l'ID de l'�criture
     */
    public String getIdEcriture();

    /**
     * Renvoie l'ID de l'exercice comptable.
     * 
     * @return l'ID de l'exercice comptable
     */
    public String getIdExerciceComptable();

    /**
     * Renvoie l'ID du journal.
     * 
     * @return l'ID du journal
     */
    public String getIdJournal();

    /**
     * Renvoie l'ID du mandat.
     * 
     * @return l'ID du mandat
     */
    public String getIdMandat();

    /**
     * Renvoie la valeur de la propri�t� idSecteurAvs
     * 
     * @return la valeur de la propri�t� idSecteurAvs
     */
    public String getIdSecteurAvs();

    /**
     * Renvoie la valeur de la propri�t� libelle
     * 
     * @return la valeur de la propri�t� libelle
     */
    public String getLibelle();

    /**
     * Renvoie la valeur de la propri�t� montant
     * 
     * @return la valeur de la propri�t� montant
     */
    public String getMontant();

    /**
     * Renvoie la valeur de la propri�t� montantMonnaieEtrangere
     * 
     * @return la valeur de la propri�t� montantMonnaieEtrangere
     */
    public String getMontantMonnaieEtrangere();

    public String getNumeroCentreChargeCredite();

    public String getNumeroCentreChargeDebite();

    /**
     * Renvoie le num�ro AVS du compte cr�dit�.
     * 
     * @return le num�ro AVS du compte cr�dit�
     */
    public java.lang.String getNumeroCompteCredite();

    /**
     * Renvoie le num�ro AVS du compte d�bit�.
     * 
     * @return le num�ro AVS du compte d�bit�
     */
    public java.lang.String getNumeroCompteDebite();

    /**
     * Renvoie la valeur de la propri�t� piece.
     * 
     * @return la valeur de la propri�t� piece
     */
    public String getPiece();

    /**
     * Renvoie la valeur de la propri�t� remarque.
     * 
     * @return la valeur de la propri�t� remarque
     */
    public String getRemarque();

    /**
     * Renvoie la valeur du solde du compte cr�dit�.
     * 
     * @return la valeur du solde du compte cr�dit�
     */
    public String getSoldeCompteCredite();

    /**
     * Renvoie la valeur du solde du compte d�bit�.
     * 
     * @return la valeur du solde du compte d�bit�
     */
    public String getSoldeCompteDebite();

    /**
     * Renvoie le total � l'avoir de l'�criture.
     * 
     * @return le total � l'avoir de l'�criture
     */
    public String getTotalAvoir();

    /**
     * Renvoie le total au doit de l'�criture.
     * 
     * @return le total au doit de l'�criture
     */
    public String getTotalDoit();

    /**
     * R�cup�re l'�criture depuis la base de donn�es.<br>
     * La m�thode setIdEcriture doit avoir �t� appel�e.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la r�cup�ration �choue
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * D�finit le cours de l'�criture, par rapport au CHF.
     * 
     * @param value
     *            value
     */
    public void setCours(String value);

    /**
     * D�finit la date de l'�criture.
     * 
     * @param newDate
     *            newDate
     */
    public void setDate(String newDate);

    /**
     * D�finit l'ID de l'�criture.
     * 
     * @param newIdEnteteEcriture
     *            newIdEnteteEcriture
     */
    public void setIdEcriture(String newIdEnteteEcriture);

    /**
     * D�finit l'ID de l'exercice comptable.
     * 
     * @param value
     *            value
     */
    public void setIdExerciceComptable(String value);

    /**
     * D�finit l'ID du journal.
     * 
     * @param newIdJournal
     *            newIdJournal
     */
    public void setIdJournal(String newIdJournal);

    /**
     * D�finit l'ID du secteur AVS li� � l'�criture.
     * 
     * @param newIdSecteurAvs
     *            newIdSecteurAvs
     */
    public void setIdSecteurAvs(String newIdSecteurAvs);

    /**
     * D�finit le libell� de l'�criture.
     * 
     * @param newLibelle
     *            newLibelle
     */
    public void setLibelle(String newLibelle);

    /**
     * D�finit le montant de l'�criture.
     * 
     * @param value
     *            value
     */
    public void setMontant(String value);

    /**
     * D�finit le montant de la monnaie �trang�re de l'�criture.
     * 
     * @param value
     *            value
     */
    public void setMontantMonnaieEtrangere(String value);

    public void setNumeroCentreChargeCredite(String idCentreChargeCredite);

    public void setNumeroCentreChargeDebite(String idCentreChargeDebite);

    /**
     * D�finit le num�ro de compte AVS cr�dit� (par ex: 2000.1000.0000).
     * 
     * @param newIdExterneCompteCredite
     *            newIdExterneCompteCredite
     */
    public void setNumeroCompteCredite(java.lang.String newIdExterneCompteCredite);

    /**
     * D�finit le num�ro de compte AVS d�bit� (par ex: 2000.1000.0000).
     * 
     * @param newIdExterneCompteDebite
     *            newIdExterneCompteDebite
     */
    public void setNumeroCompteDebite(java.lang.String newIdExterneCompteDebite);

    /**
     * D�finit la valeur de la propri�t� pi�ce.
     * 
     * @param newPiece
     *            newPiece
     */
    public void setPiece(String newPiece);

    /**
     * D�finit la valeur de la propri�t� remarque.
     * 
     * @param value
     *            value
     */
    public void setRemarque(String value);

    /**
     * Met � jour l'�criture en base de donn�es.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la mise � jour �choue
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
