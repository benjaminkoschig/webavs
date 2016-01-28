package globaz.osiris.impl.helios;

import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BTransaction;
import globaz.globall.shared.GlobazValueObject;
import globaz.globall.util.JADate;
import globaz.helios.api.ICGExerciceComptable;
import globaz.helios.api.ICGJournal;
import globaz.helios.api.ICGPeriodeComptable;
import globaz.helios.db.comptes.CGMandat;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.external.IntJournalCG;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (22.05.2003 17:18:22)
 * 
 * @author: Administrator
 */
public class CAHeliosJournal implements IntJournalCG {
    private static final String LABEL_CAHELIOSJOURNAL_NOJOURNAL = "CAHELIOSJOURNAL$NOJOURNAL";

    private String idJournal = new String();
    private ICGJournal journal = null;
    private boolean onError = false;
    private BISession sessionHelios = null;

    /**
     * Commentaire relatif au constructeur CAHeliosJournal.
     */
    public CAHeliosJournal() {
        super();
    }

    /**
     * Ins�re un nouveau journal de comptabilit� g�n�rale Date de cr�ation : (28.10.2002 10:03:54)
     * 
     * @return globaz.osiris.external.IntJournalCG L'interface vers le nouveau journal CG cr��
     * @param transaction
     *            globaz.globall.api.BTransaction La transaction courante
     * @param journalValueObject
     *            globaz.globall.shared.GlobazValueObject Le value object repr�sentant le journal
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public globaz.osiris.external.IntJournalCG addJournal(BTransaction transaction, GlobazValueObject journalValueObject)
            throws Exception {
        // Cr�er une nouvelle instance des classes n�cessaires
        journal = (ICGJournal) getSessionHelios(transaction.getISession()).getAPIFor(ICGJournal.class);
        onError = false;
        ICGExerciceComptable exercice = (ICGExerciceComptable) getSessionHelios(transaction.getISession()).getAPIFor(
                ICGExerciceComptable.class);
        ICGPeriodeComptable periode = (ICGPeriodeComptable) getSessionHelios(transaction.getISession()).getAPIFor(
                ICGPeriodeComptable.class);
        // R�cup�rer la date de valeur
        String dateValeur = (String) journalValueObject.getProperty("dateValeur");
        JADate date = new JADate(dateValeur);
        String idMandat = (String) journalValueObject.getProperty("idMandat");
        // Mandat si null
        if (JadeStringUtil.isIntegerEmpty(idMandat)) {
            idMandat = CGMandat.MANDAT_AVS_DEFAULT_NUMBER;
        }

        // R�cup�rer le mandat
        exercice.setIdExerciceComptableFrom(dateValeur, idMandat);
        periode.setIdPeriodeComptableFrom(JadeStringUtil.rightJustifyInteger(String.valueOf(date.getMonth()), 2),
                exercice.getIdExerciceComptable());
        // Charger les propri�t�s
        journal.setDate((String) journalValueObject.getProperty("date"));
        journal.setDateValeur(dateValeur);
        journal.setIdPeriodeComptable(periode.getIdPeriodeComptable());
        journal.setIdExerciceComptable(exercice.getIdExerciceComptable());
        journal.setLibelle((String) journalValueObject.getProperty("libelle"));
        journal.setReferenceExterne((String) journalValueObject.getProperty("referenceExterne"));

        journal.setEstPublic((Boolean) journalValueObject.getProperty("estPublic"));
        journal.setEstConfidentiel((Boolean) journalValueObject.getProperty("estConfidentiel"));

        journal.setIdTypeJournal(ICGJournal.CS_TYPE_AUTOMATIQUE);

        // Cr�ation du journal
        journal.add(transaction);

        // Contr�ler les erreurs
        if (transaction.hasErrors()) {
            onError = true;
        }

        // Retourner le journal
        return this;
    }

    /**
     * Annuler le journal de comptabilit� g�n�rale Date de cr�ation : (28.10.2002 09:57:01)
     * 
     * @param transaction
     *            globaz.globall.api.BTransaction La transaction
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void annuler(BTransaction transaction) throws Exception {
        // Error si pas de journal
        if (journal == null) {
            throw new Exception(transaction.getSession().getLabel(CAHeliosJournal.LABEL_CAHELIOSJOURNAL_NOJOURNAL));
        }

        // Annuler le journal
        journal.annuler(transaction);

        if (transaction.hasErrors()) {
            onError = true;
        }
    }

    @Override
    public void annuler(BTransaction transaction, String emailAddress) throws Exception {
        // Error si pas de journal
        if (journal == null) {
            throw new Exception(transaction.getSession().getLabel(CAHeliosJournal.LABEL_CAHELIOSJOURNAL_NOJOURNAL));
        }

        // Annuler le journal
        journal.annuler(transaction, emailAddress);

        if (transaction.hasErrors()) {
            onError = true;
        }
    }

    /**
     * Ex�cuter la comptabilisation du journal Date de cr�ation : (28.10.2002 09:55:14)
     * 
     * @param globaz
     *            .globall.api.BTransaction la transaction
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    public void comptabiliser(BTransaction transaction) throws Exception {
        // Error si pas de journal
        if (journal == null) {
            throw new Exception(transaction.getSession().getLabel(CAHeliosJournal.LABEL_CAHELIOSJOURNAL_NOJOURNAL));
        }

        // Comptabiliser le journal
        journal.comptabiliser(transaction);

        if (transaction.hasErrors()) {
            onError = true;
        }

    }

    @Override
    public void comptabiliser(BTransaction transaction, String emailAddress) throws Exception {
        // Error si pas de journal
        if (journal == null) {
            throw new Exception(transaction.getSession().getLabel(CAHeliosJournal.LABEL_CAHELIOSJOURNAL_NOJOURNAL));
        }

        // Comptabiliser le journal
        journal.comptabiliser(transaction, emailAddress);

        if (transaction.hasErrors()) {
            onError = true;
        }
    }

    /**
     * R�cup�re la date Date de cr�ation : (24.10.2002 16:35:52)
     * 
     * @return String la date
     */
    @Override
    public String getDate() {
        if (journal == null) {
            return new String();
        } else {
            return journal.getDate();
        }
    }

    /**
     * R�cup�re la date de valeur Date de cr�ation : (08.11.2002 09:11:47)
     * 
     * @return String la date de valeur
     */
    @Override
    public String getDateValeur() {
        if (journal == null) {
            return new String();
        } else {
            return journal.getDateValeur();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.external.IntJournalCG#getEtat()
     */
    @Override
    public String getIdEtat() {
        if (journal == null) {
            return new String();
        } else {
            return journal.getIdEtat();
        }
    }

    @Override
    public String getIdExerciceComptable() {
        if (journal != null) {
            return journal.getIdExerciceComptable();
        } else {
            return null;
        }
    }

    /**
     * Commentaire relatif � la m�thode getIdJournal.
     */
    @Override
    public String getIdJournal() {
        if (journal == null) {
            return idJournal;
        } else {
            return journal.getIdJournal();
        }
    }

    /**
     * Commentaire relatif � la m�thode getLibelle.
     */
    @Override
    public String getLibelle() {
        if (journal == null) {
            return new String();
        } else {
            return journal.getLibelle();
        }
    }

    @Override
    public String getNumero() {
        if (journal == null) {
            return IntJournalCG.NUMERO_NO_JOURNAL;
        } else {
            return journal.getNumero();
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.05.2003 17:25:29)
     * 
     * @return globaz.globall.db.BSession
     */
    @Override
    public BISession getSessionHelios(BISession session) throws Exception {
        // Ouvrir une nouvelle session
        if (sessionHelios == null) {
            BIApplication remoteApplication = GlobazSystem.getApplication("HELIOS");
            sessionHelios = remoteApplication.newSession(session);
        }
        return sessionHelios;
    }

    /**
     * Imprimer le journal de comptabilit� g�n�rale
     * 
     * @exception java.lang.Exception
     *                La description de l'exception Date de cr�ation : (28.10.2002 09:58:17)
     */
    @Override
    public void imprimer() throws Exception {
    }

    /**
     * Indiquer si le journal est en erreur Date de cr�ation : (24.10.2002 16:37:32)
     * 
     * @return boolean l'�tat de l'indicateur d'erreur
     */
    @Override
    public boolean isOnError() {
        return onError;
    }

    /**
     * R�cup�re un journal Date de cr�ation : (24.10.2002 16:32:33)
     * 
     * @param transaction
     *            BTransaction la transaction
     * @param shouldAddErrors
     *            boolean propager les erreurs
     */
    @Override
    public void retrieve(BISession session, BITransaction transaction, boolean shouldAddErrors) throws Exception {
        // R�cup�rer le journal
        journal = (ICGJournal) getSessionHelios(session).getAPIFor(ICGJournal.class);
        onError = false;
        journal.setIdJournal(idJournal);
        journal.retrieve(transaction);

        // Signalier les erreurs
        if (journal.isNew() || ((transaction != null) && transaction.hasErrors())) {
            onError = true;
        }

    }

    /**
     * Affecter l'identifiant du journal Date de cr�ation : (24.10.2002 14:25:37)
     * 
     * @param newIdJournal
     *            String le num�ro du journal
     */
    @Override
    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }
}
