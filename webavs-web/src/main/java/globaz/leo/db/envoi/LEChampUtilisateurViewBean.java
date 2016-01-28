/*
 * Cr�� le 22 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.leo.db.envoi;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.leo.constantes.ILEChampUtilisateurDefTable;

/**
 * @author jpa
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LEChampUtilisateurViewBean extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** csGroupe - code systeme qui definit le champ (JCUCHP) */
    private String csChamp = new String();
    /** csGroupe - code systeme qui definit le groupe du champ (JCUGP) */
    private String csGroupe = new String();
    /** Table : LUCHUTP */
    /**
     * idChampUtilisateur - cl� primaire du fichier des champs utilisateurs (JCUID)
     */
    private String idChampUtilisateur = new String();
    /** idJournalisation - cl� primaire du fichier des journalisations (JJOUID) */
    private String idJournalisation = new String();
    /** valeur - definit la valeur du champ (JCUVAL) */
    private String valeur = new String();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdChampUtilisateur(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table LECHUTP
     * 
     * @return String LECHUTP
     */
    @Override
    protected String _getTableName() {
        return ILEChampUtilisateurDefTable.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idChampUtilisateur = statement.dbReadString(ILEChampUtilisateurDefTable.ID_CHAMP_UTILISATEUR);
        idJournalisation = statement.dbReadString(ILEChampUtilisateurDefTable.ID_JOURNALISATION);
        csGroupe = statement.dbReadString(ILEChampUtilisateurDefTable.CS_GROUPE);
        csChamp = statement.dbReadString(ILEChampUtilisateurDefTable.CS_CHAMP);
        valeur = statement.dbReadString(ILEChampUtilisateurDefTable.VALEUR);
    }

    /**
     * Valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Indique la cl� principale du fichier LECHUTP
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�criture de la cl�
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(
                "JCUID",
                _dbWriteNumeric(statement.getTransaction(), getIdChampUtilisateur(),
                        "idChampUtilisateur - cl� primaire du fichier des champs utilisateurs"));
    }

    /**
     * Ecriture des propri�t�s
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     * @throws Exception
     *             si probl�me lors de l'�critrues des propri�t�s
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(
                ILEChampUtilisateurDefTable.ID_CHAMP_UTILISATEUR,
                _dbWriteNumeric(statement.getTransaction(), getIdChampUtilisateur(),
                        "idChampUtilisateur - cl� primaire du fichier des champs utilisateurs"));
        statement.writeField(
                ILEChampUtilisateurDefTable.ID_JOURNALISATION,
                _dbWriteNumeric(statement.getTransaction(), getIdJournalisation(),
                        "idJournalisation - cl� primaire du fichier des journalisations"));
        statement.writeField(
                ILEChampUtilisateurDefTable.CS_GROUPE,
                _dbWriteNumeric(statement.getTransaction(), getCsGroupe(),
                        "csGroupe - code systeme qui definit le groupe du champ"));
        statement
                .writeField(
                        ILEChampUtilisateurDefTable.CS_CHAMP,
                        _dbWriteNumeric(statement.getTransaction(), getCsChamp(),
                                "csChamp - code systeme qui definit le champ"));
        statement
                .writeField(
                        ILEChampUtilisateurDefTable.VALEUR,
                        _dbWriteString(statement.getTransaction(), getValeur(),
                                "csChamp - valeur - definit la valeur du champ"));
    }

    public String getCsChamp() {
        return csChamp;
    }

    public String getCsGroupe() {
        return csGroupe;
    }

    public String getIdChampUtilisateur() {
        return idChampUtilisateur;
    }

    public String getIdJournalisation() {
        return idJournalisation;
    }

    public String getValeur() {
        return valeur;
    }

    public void setCsChamp(String string) {
        csChamp = string;
    }

    public void setCsGroupe(String string) {
        csGroupe = string;
    }

    public void setIdChampUtilisateur(String string) {
        idChampUtilisateur = string;
    }

    public void setIdJournalisation(String string) {
        idJournalisation = string;
    }

    public void setValeur(String string) {
        valeur = string;
    }

}
