package globaz.helios.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;

/**
 * Cette classe représente une écriture double pour une comptabilité AVS. Une écriture est enregistrée dans un
 * <b>Journal</b> {@link ICGJournal}.<br>
 * Pour l'utiliser, il faut OBLIGATOIREMENT préciser les paramètres suivants :<br>
 * <li>libelle : le libellé de l'écriture<br> <li>montant : le montant de l'écriture<br> <li>date : la date de
 * l'écriture<br> <li>idJournal : l'ID du journal dans lequel l'écriture va être enregistrée<br> <li>
 * idExerciceComptable : l'ID de l'exercice comptable lié à l'écriture<br> <li>
 * numeroCompteCredite : le numéro à 12 chiffres (par ex:'1000.1011.0000') du compte AVS à créditer<br> <li>
 * numeroCompteDebite : le numéro à 12 chiffres (par ex:'2000.1000.0000') du compte AVS à débiter<br>
 * <br>
 * Les paramètres suivants sont facultatifs :<br> <li>remarque : une remarque peut être associée à cette écriture<br>
 * <li>idSecteurAVS : on peut préciser l'ID du secteur AVS lié à cette écriture (par ex: '2000', '3000')<br> <li>
 * piece : l'écriture peut être associée à une pièce comptable<br>
 * 
 * <li>montantMonnaieEtrangere : Pour les comptes en monnaie étrangère, 2 des 3 valeurs suivantes sont<br>
 * obligatoire : montant, montantMonnaieEtrangere, cours (la troisième est calculée automatiquement si non renseignée).
 * <br> <li>cours : Pour les comptes en monnaie étrangère, 2 des 3 valeurs suivantes sont<br>
 * obligatoire : montant, montantMonnaieEtrangere, cours (la troisième est calculée automatiquement si non renseignée).<br>
 * 
 * Example de valeurs pour un compte en monnaie étrangère : <br>
 * Montant : 100.- CHF<br>
 * Cours : 0.68<br>
 * Montant Monnaie étrangère : 68.-<br>
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
 * 		// Récupération de l'ID de l'exercice comptable via une date
 * 		exercice.setIdExerciceComptableFrom(&quot;01.01.2003&quot;, idMandat);
 * 
 * 		// Récupération de l'ID du journal via un code de période (standard AVS, ici &quot;03&quot; = Mars)
 * 		journal.setIdJournalFrom(exercice.getIdExercice(), &quot;03&quot;);
 * 
 * 		// Création d'une écriture double
 * 		ecriture.setIdExerciceComptable(exercice.getIdExerciceComptable());
 * 		ecriture.setIdJournal(journal.getIdJournal());
 * 		ecriture.setLibelle(&quot;Mon écriture&quot;);
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
 * @author Grégoire PICQUOT (gregoire.picquot@globaz.ch)
 * @see ICGJournal
 * @see ICGExerciceComptable
 */
public interface ICGEcritureDouble extends BIEntity {
    /**
     * Ajoute l'écriture en base de données.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si l'ajout échoue
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Efface l'écriture en base de données.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la suppression échoue
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /**
     * Renvoie la valeur de la propriété cours
     * 
     * @return la valeur de la propriété cours
     */
    public String getCours();

    /**
     * Renvoie la valeur de la propriété date
     * 
     * @return la valeur de la propriété date
     */
    public String getDate();

    /**
     * Renvoie l'ID de l'écriture.
     * 
     * @return l'ID de l'écriture
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
     * Renvoie la valeur de la propriété idSecteurAvs
     * 
     * @return la valeur de la propriété idSecteurAvs
     */
    public String getIdSecteurAvs();

    /**
     * Renvoie la valeur de la propriété libelle
     * 
     * @return la valeur de la propriété libelle
     */
    public String getLibelle();

    /**
     * Renvoie la valeur de la propriété montant
     * 
     * @return la valeur de la propriété montant
     */
    public String getMontant();

    /**
     * Renvoie la valeur de la propriété montantMonnaieEtrangere
     * 
     * @return la valeur de la propriété montantMonnaieEtrangere
     */
    public String getMontantMonnaieEtrangere();

    public String getNumeroCentreChargeCredite();

    public String getNumeroCentreChargeDebite();

    /**
     * Renvoie le numéro AVS du compte crédité.
     * 
     * @return le numéro AVS du compte crédité
     */
    public java.lang.String getNumeroCompteCredite();

    /**
     * Renvoie le numéro AVS du compte débité.
     * 
     * @return le numéro AVS du compte débité
     */
    public java.lang.String getNumeroCompteDebite();

    /**
     * Renvoie la valeur de la propriété piece.
     * 
     * @return la valeur de la propriété piece
     */
    public String getPiece();

    /**
     * Renvoie la valeur de la propriété remarque.
     * 
     * @return la valeur de la propriété remarque
     */
    public String getRemarque();

    /**
     * Renvoie la valeur du solde du compte crédité.
     * 
     * @return la valeur du solde du compte crédité
     */
    public String getSoldeCompteCredite();

    /**
     * Renvoie la valeur du solde du compte débité.
     * 
     * @return la valeur du solde du compte débité
     */
    public String getSoldeCompteDebite();

    /**
     * Renvoie le total à l'avoir de l'écriture.
     * 
     * @return le total à l'avoir de l'écriture
     */
    public String getTotalAvoir();

    /**
     * Renvoie le total au doit de l'écriture.
     * 
     * @return le total au doit de l'écriture
     */
    public String getTotalDoit();

    /**
     * Récupère l'écriture depuis la base de données.<br>
     * La méthode setIdEcriture doit avoir été appelée.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la récupération échoue
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    /**
     * Définit le cours de l'écriture, par rapport au CHF.
     * 
     * @param value
     *            value
     */
    public void setCours(String value);

    /**
     * Définit la date de l'écriture.
     * 
     * @param newDate
     *            newDate
     */
    public void setDate(String newDate);

    /**
     * Définit l'ID de l'écriture.
     * 
     * @param newIdEnteteEcriture
     *            newIdEnteteEcriture
     */
    public void setIdEcriture(String newIdEnteteEcriture);

    /**
     * Définit l'ID de l'exercice comptable.
     * 
     * @param value
     *            value
     */
    public void setIdExerciceComptable(String value);

    /**
     * Définit l'ID du journal.
     * 
     * @param newIdJournal
     *            newIdJournal
     */
    public void setIdJournal(String newIdJournal);

    /**
     * Définit l'ID du secteur AVS lié à l'écriture.
     * 
     * @param newIdSecteurAvs
     *            newIdSecteurAvs
     */
    public void setIdSecteurAvs(String newIdSecteurAvs);

    /**
     * Définit le libellé de l'écriture.
     * 
     * @param newLibelle
     *            newLibelle
     */
    public void setLibelle(String newLibelle);

    /**
     * Définit le montant de l'écriture.
     * 
     * @param value
     *            value
     */
    public void setMontant(String value);

    /**
     * Définit le montant de la monnaie étrangère de l'écriture.
     * 
     * @param value
     *            value
     */
    public void setMontantMonnaieEtrangere(String value);

    public void setNumeroCentreChargeCredite(String idCentreChargeCredite);

    public void setNumeroCentreChargeDebite(String idCentreChargeDebite);

    /**
     * Définit le numéro de compte AVS crédité (par ex: 2000.1000.0000).
     * 
     * @param newIdExterneCompteCredite
     *            newIdExterneCompteCredite
     */
    public void setNumeroCompteCredite(java.lang.String newIdExterneCompteCredite);

    /**
     * Définit le numéro de compte AVS débité (par ex: 2000.1000.0000).
     * 
     * @param newIdExterneCompteDebite
     *            newIdExterneCompteDebite
     */
    public void setNumeroCompteDebite(java.lang.String newIdExterneCompteDebite);

    /**
     * Définit la valeur de la propriété pièce.
     * 
     * @param newPiece
     *            newPiece
     */
    public void setPiece(String newPiece);

    /**
     * Définit la valeur de la propriété remarque.
     * 
     * @param value
     *            value
     */
    public void setRemarque(String value);

    /**
     * Met à jour l'écriture en base de données.
     * 
     * @param transaction
     *            transaction
     * @exception java.lang.Exception
     *                si la mise à jour échoue
     */
    public void update(BITransaction transaction) throws java.lang.Exception;
}
