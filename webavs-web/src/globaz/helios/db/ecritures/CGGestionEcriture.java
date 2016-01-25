package globaz.helios.db.ecritures;

import globaz.globall.db.BStatement;
import globaz.helios.db.comptes.CGJournal;
import globaz.helios.db.interfaces.CGNeedExerciceComptable;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;

/**
 * Entité permettant l'accès à l'écran d'édition des écritures comptables.
 * 
 * @author DDA
 * 
 */
public class CGGestionEcriture extends CGNeedExerciceComptable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String dateValeur = "";

    private ArrayList ecritures = new ArrayList();
    private String idEnteteEcriture = "";

    private String idFournisseur = "0";

    private String idJournal;
    private String idSection = "0";

    private CGJournal journal;
    private String piece;

    private String remarque = new String();
    private Boolean saisieEcran = new Boolean(false);

    @Override
    protected String _getTableName() {
        // Not used here
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        // Not used here
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        super._validate(statement);
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // Not used here
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // Not used here
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public ArrayList getEcritures() {
        return ecritures;
    }

    public String getIdEnteteEcriture() {
        return idEnteteEcriture;
    }

    public String getIdFournisseur() {
        return idFournisseur;
    }

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdSection() {
        return idSection;
    }

    public CGJournal getJournal() {
        if ((journal == null) && !JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            retrieveJournal();
        }
        return journal;
    }

    public String getPiece() {
        return piece;
    }

    public String getRemarque() {
        return remarque;
    }

    public Boolean getSaisieEcran() {
        return saisieEcran;
    }

    public CGJournal retrieveJournal() {
        if ((journal == null) && !JadeStringUtil.isIntegerEmpty(getIdJournal())) {
            journal = new CGJournal();
            journal.setSession(getSession());

            journal.setIdJournal(getIdJournal());

            try {
                journal.retrieve();
            } catch (Exception e) {
                // Do nothing;
            }
        }

        return journal;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setEcritures(ArrayList ecritures) {
        this.ecritures = ecritures;
    }

    public void setIdEnteteEcriture(String idEnteteEcriture) {
        this.idEnteteEcriture = idEnteteEcriture;
    }

    public void setIdFournisseur(String idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    public void setJournal(CGJournal journal) {
        this.journal = journal;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public void setSaisieEcran(String newSaisieEcran) {
        try {
            saisieEcran = Boolean.valueOf(newSaisieEcran);
        } catch (Exception ex) {
            saisieEcran = new Boolean(false);
        }
    }

}
