/*
 * Created on 10.10.2007 JPA Classe JournalQuittance pour les bénéficiaires PC
 */
package globaz.naos.db.beneficiairepc;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.translation.CodeSystem;
import java.io.Serializable;

/**
 * La classe définissant l'entité Quittance (Bénéficiaires PC)
 * 
 * @author jpa
 */
public class AFJournalQuittance extends BEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String getEtatLibelle(BSession session, String idJournal) {
        try {
            AFJournalQuittanceManager man = new AFJournalQuittanceManager();
            man.setSession(session);
            man.setForIdJournalQuittance(idJournal);
            man.find();
            AFJournalQuittance journal = (AFJournalQuittance) man.getFirstEntity();
            return journal.getEtatLibelle();
        } catch (Exception e) {
            return "";
        }
    }

    private String annee = "";
    private String dateCreation = "";
    // Fields
    private String descriptionJournal = "";
    private String etat = "";
    // DB Table AFJRNQU
    // Primary Key
    private String idJournalQuittance = "";

    private String nombreQuittances = "";

    /**
     * Constructeur d'AFQuittance
     */
    public AFJournalQuittance() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdJournalQuittance(this._incCounter(transaction, idJournalQuittance));
        if (JadeStringUtil.isEmpty(getAnnee())) {
            JADate date = new JADate(getDateCreation());
            setAnnee(String.valueOf(date.getYear()));
        }
        setNombreQuittances("0");
        setEtat(CodeSystem.BENEFICIAIRE_ETAT_OUVERT);
    }

    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        if (JadeStringUtil.isEmpty(getAnnee())) {
            JADate date = new JADate(getDateCreation());
            setAnnee(String.valueOf(date.getYear()));
        }
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFJRNQU";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idJournalQuittance = statement.dbReadNumeric("MAJQID");
        descriptionJournal = statement.dbReadString("MAJQDE");
        nombreQuittances = statement.dbReadNumeric("MAJQNB");
        dateCreation = statement.dbReadString("MAJQDC");
        etat = statement.dbReadNumeric("MAJQET");
        annee = statement.dbReadNumeric("MAJANN");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        boolean validationOK = true;
        // Test validité des dates
        if (JadeStringUtil.isEmpty(getDateCreation())) {
            _addError(statement.getTransaction(), getSession().getLabel("160"));
        } else {
            validationOK &= _checkDate(statement.getTransaction(), getDateCreation(), getSession().getLabel("160"));
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("MAJQID", this._dbWriteNumeric(statement.getTransaction(), getIdJournalQuittance(), ""));
    }

    // *******************************************************
    // Getter and Setter
    // *******************************************************

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MAJQID", this._dbWriteNumeric(statement.getTransaction(), getIdJournalQuittance(), ""));
        statement.writeField("MAJQDE", this._dbWriteString(statement.getTransaction(), getDescriptionJournal(), ""));
        statement.writeField("MAJQNB", this._dbWriteNumeric(statement.getTransaction(), getNombreQuittances(), ""));
        statement.writeField("MAJQDC", this._dbWriteString(statement.getTransaction(), getDateCreation(), ""));
        statement.writeField("MAJQET", this._dbWriteNumeric(statement.getTransaction(), getEtat(), ""));
        statement.writeField("MAJANN", this._dbWriteNumeric(statement.getTransaction(), getAnnee(), ""));
    }

    public String getAnnee() {
        return annee;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getDescriptionJournal() {
        return descriptionJournal;
    }

    public String getEtat() {
        return etat;
    }

    public String getEtatLibelle() {
        return getSession().getCodeLibelle(getEtat());
    }

    public String getIdJournalQuittance() {
        return idJournalQuittance;
    }

    public String getNombreQuittances() {
        return nombreQuittances;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDescriptionJournal(String descriptionJournal) {
        this.descriptionJournal = descriptionJournal;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setIdJournalQuittance(String idJournalQuittance) {
        this.idJournalQuittance = idJournalQuittance;
    }

    public void setNombreQuittances(String nombreQuittances) {
        this.nombreQuittances = nombreQuittances;
    }
}
