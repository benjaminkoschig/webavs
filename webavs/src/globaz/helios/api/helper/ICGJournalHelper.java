package globaz.helios.api.helper;

import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.helios.api.ICGEcritureDouble;
import globaz.helios.api.ICGExerciceComptable;
import globaz.helios.api.ICGJournal;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICGJournalHelper extends GlobazHelper implements ICGJournal {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (08.04.2003 14:08:33)
     * 
     * @param args
     *            String[]
     */
    public static void main(String[] args) {

        try {
            BIApplication remoteApplication = GlobazSystem.getApplication("HELIOS");
            BISession remoteSession = remoteApplication.newSession("globazf", "ssiiadm");
            ICGExerciceComptable exercice = (ICGExerciceComptable) remoteSession.getAPIFor(ICGExerciceComptable.class);
            ICGJournal journal = (ICGJournal) remoteSession.getAPIFor(ICGJournal.class);
            ICGEcritureDouble ecriture = (ICGEcritureDouble) remoteSession.getAPIFor(ICGEcritureDouble.class);
            BITransaction transaction = ((BSession) remoteSession).newTransaction();
            try {
                journal.setIdJournal("1022");
                journal.retrieve(transaction);
                exercice.setIdExerciceComptable("1");
                exercice.retrieve(transaction);
                System.out.println(exercice.getFullDescription());
                System.out.println(journal.getLibelle());

                ecriture.setIdExerciceComptable(exercice.getIdExerciceComptable());
                ecriture.setIdJournal(journal.getIdJournal());
                ecriture.setLibelle("test");
                ecriture.setDate("01.06.2003");
                ecriture.setMontant("123.00");
                ecriture.setNumeroCompteCredite("1000.1011.1000");
                ecriture.setNumeroCompteDebite("2000.1201.0000");
                ecriture.add(transaction);
                if (transaction.hasErrors()) {
                    throw new Exception("Exception 1");
                }
                if (remoteSession.hasErrors()) {
                    throw new Exception("Exception 2");
                }

            } catch (Exception e) {
                transaction.closeTransaction();
                e.printStackTrace();
            }
            if (remoteSession.hasErrors()) {
                System.err.println("Non-fatal errors : " + remoteSession.getErrors().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Constructeur du type ICGJournalHelper
     */
    public ICGJournalHelper() {
        super("globaz.helios.db.comptes.CGJournal");
    }

    /**
     * Constructeur du type ICGJournalHelper
     * 
     * @param valueObject
     *            le Value Object contenant les donn�es
     */
    public ICGJournalHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICGJournalHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'impl�mentation
     */
    public ICGJournalHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /**
     * Effacement du journal (Processus) : vide le journal de toutes ses �critures. Met � jour son log et son �tat. Date
     * de cr�ation : (09.04.2003 16:25:47)
     * 
     * @param transaction
     *            globaz.globall.api.BITransaction
     */
    @Override
    public void annuler(globaz.globall.api.BITransaction transaction) throws Exception {
        this._invoke("annuler", transaction);
    }

    /**
     * Effacement du journal (Processus) : vide le journal de toutes ses �critures. Met � jour son log et son �tat. Date
     * de cr�ation : (09.04.2003 16:25:47)
     * 
     * @param transaction
     *            globaz.globall.api.BITransaction
     */
    @Override
    public void annuler(globaz.globall.api.BITransaction transaction, String emailAddress) throws Exception {
        this._invoke("annuler", new Object[] { transaction, emailAddress });
    }

    /**
     * Comptabilisation du journal (Processus) : comptabilise toutes les �critures non comptabilis�es du journal. Met �
     * jour son log et son �tat. Date de cr�ation : (09.04.2003 16:25:12)
     * 
     * @param transaction
     *            globaz.globall.api.BITransaction
     */
    @Override
    public void comptabiliser(globaz.globall.api.BITransaction transaction) throws Exception {
        this._invoke("comptabiliser", transaction);
    }

    /**
     * Comptabilisation du journal (Processus) : comptabilise toutes les �critures non comptabilis�es du journal. Met �
     * jour son log et son �tat. Date de cr�ation : (09.04.2003 16:25:12)
     * 
     * @param transaction
     *            globaz.globall.api.BITransaction
     */
    @Override
    public void comptabiliser(globaz.globall.api.BITransaction transaction, String emailAddress) throws Exception {
        this._invoke("comptabiliser", new Object[] { transaction, emailAddress });
    }

    @Override
    public void delete(BITransaction transaction) throws Exception {
        this._invoke("supprimer", transaction);
    }

    /**
     * D�comptabilisation du journal (Processus) : d�comptabilise toutes les �critures comptabilis�es du journal. Met �
     * jour son log et son �tat. Date de cr�ation : (09.04.2003 16:25:32)
     * 
     * @param transaction
     *            globaz.globall.api.BITransaction
     */
    @Override
    public void exComptabiliser(globaz.globall.api.BITransaction transaction) throws Exception {
        this._invoke("exComptabiliser", transaction);
    }

    /**
     * Commentaire relatif � la m�thode getDate.
     */
    @Override
    public String getDate() {
        return (String) _getValueObject().getProperty("date");
    }

    /**
     * Commentaire relatif � la m�thode getDateValeur.
     */
    @Override
    public String getDateValeur() {
        return (String) _getValueObject().getProperty("dateValeur");
    }

    /**
     * Teste si le journal est confidentiel.
     * 
     * @param newEstConfidentiel
     *            newEstConfidentiel
     */
    @Override
    public Boolean getEstConfidentiel() {
        return (Boolean) _getValueObject().getProperty("estConfidentiel");
    }

    /**
     * Teste si le journal est public (d'autres utilisateurs peuvent manipuler les �critures).
     * 
     * @param newEstPublic
     *            newEstPublic
     */
    @Override
    public Boolean getEstPublic() {
        return (Boolean) _getValueObject().getProperty("estPublic");
    }

    /**
     * Commentaire relatif � la m�thode getEtatLibelle.
     */
    @Override
    public String getEtatLibelle() throws Exception {
        return (String) _getObject("getEtatLibelle", new Object[] {});
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (25.11.2002 10:14:56)
     * 
     * @return String
     */
    @Override
    public String getFullDescription() throws Exception {
        return (String) _getObject("getFullDescription", new Object[] {});
    }

    /**
     * Commentaire relatif � la m�thode getIdEtat.
     */
    @Override
    public String getIdEtat() {
        return (String) _getValueObject().getProperty("idEtat");
    }

    /**
     * Renvoie l'ID de l'exercice comptable.
     * 
     * @return l'ID de la p�riode comptable
     */
    @Override
    public String getIdExerciceComptable() {
        return (String) _getValueObject().getProperty("idExerciceComptable");
    }

    /**
     * Getter
     */
    @Override
    public String getIdJournal() {
        return (String) _getValueObject().getProperty("idJournal");
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.12.2002 10:06:26)
     * 
     * @return String
     */
    @Override
    public String getIdPeriodeComptable() {
        return (String) _getValueObject().getProperty("idPeriodeComptable");
    }

    /**
     * Commentaire relatif � la m�thode getIdTypeJournal.
     */
    @Override
    public String getIdTypeJournal() {
        return (String) _getValueObject().getProperty("idTypeJournal");
    }

    /**
     * Commentaire relatif � la m�thode getLibelle.
     */
    @Override
    public String getLibelle() {
        return (String) _getValueObject().getProperty("libelle");
    }

    /**
     * Commentaire relatif � la m�thode getNumero.
     */
    @Override
    public String getNumero() {
        return (String) _getValueObject().getProperty("numero");
    }

    /**
     * Commentaire relatif � la m�thode getProprietaire.
     */
    @Override
    public String getProprietaire() {
        return (String) _getValueObject().getProperty("proprietaire");
    }

    /**
     * Commentaire relatif � la m�thode getReferenceExterne.
     */
    @Override
    public String getReferenceExterne() {
        return (String) _getValueObject().getProperty("referenceExterne");
    }

    /**
     * Commentaire relatif � la m�thode setDate.
     */
    @Override
    public void setDate(String newDate) {
        _getValueObject().setProperty("date", newDate);
    }

    /**
     * Commentaire relatif � la m�thode setDateValeur.
     */
    @Override
    public void setDateValeur(String newDateValeur) {
        _getValueObject().setProperty("dateValeur", newDateValeur);
    }

    /**
     * Commentaire relatif � la m�thode setEstConfidentiel.
     */
    @Override
    public void setEstConfidentiel(Boolean newEstConfidentiel) {
        _getValueObject().setProperty("estConfidentiel", newEstConfidentiel);
    }

    /**
     * Commentaire relatif � la m�thode setEstPublic.
     */
    @Override
    public void setEstPublic(Boolean newEstPublic) {
        _getValueObject().setProperty("estPublic", newEstPublic);
    }

    /**
     * D�finit l'ID de l'exercice comptable li�e au journal.
     * 
     * @param newIdPeriodeComptable
     *            newIdPeriodeComptable
     */
    @Override
    public void setIdExerciceComptable(String newIdExerciceComptable) {
        _getValueObject().setProperty("idExerciceComptable", newIdExerciceComptable);
    }

    /**
     * Setter
     */
    @Override
    public void setIdJournal(String newIdJournal) {
        _getValueObject().setProperty("idJournal", newIdJournal);
    }

    /**
     * D�finit l'ID du journal via un ID de p�riode comptable et un ID d'exercice comptable.
     * 
     * @param idPeriode
     *            idPeriode
     * @param idExerciceComptable
     *            idExerciceComptable
     * @exception Exception
     *                si ???
     */
    @Override
    public void setIdJournalFrom(String idPeriode, String idExerciceComptable) throws Exception {
        this._invoke("setIdJournalFrom", new Object[] { idPeriode, idExerciceComptable });
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (04.12.2002 10:06:26)
     * 
     * @param newIdPeriodeComptable
     *            String
     */
    @Override
    public void setIdPeriodeComptable(String newIdPeriodeComptable) {
        _getValueObject().setProperty("idPeriodeComptable", newIdPeriodeComptable);
    }

    /**
     * Commentaire relatif � la m�thode setIdTypeJournal.
     */
    @Override
    public void setIdTypeJournal(String newIdTypeJournal) {
        _getValueObject().setProperty("idTypeJournal", newIdTypeJournal);
    }

    /**
     * Commentaire relatif � la m�thode setLibelle.
     */
    @Override
    public void setLibelle(String newLibelle) {
        _getValueObject().setProperty("libelle", newLibelle);
    }

    /**
     * Commentaire relatif � la m�thode setReferenceExterne.
     */
    @Override
    public void setReferenceExterne(String newReferenceExterne) {
        _getValueObject().setProperty("referenceExterne", newReferenceExterne);
    }

    /**
     * Supprime le journal (Processus). Date de cr�ation : (09.04.2003 16:26:11)
     * 
     * @param transaction
     *            globaz.globall.api.BITransaction
     */
    public void supprimer(globaz.globall.api.BITransaction transaction) throws Exception {
        this._invoke("supprimer", transaction);
    }

}
