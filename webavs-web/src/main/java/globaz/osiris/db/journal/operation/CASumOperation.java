package globaz.osiris.db.journal.operation;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CARubrique;

/**
 * @author dda To change the template for this generated type comment go to Window&gt;Preferences&gt;Java&gt;Code
 *         Generation&gt;Code and Comments
 */
public class CASumOperation extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String codeDebitCredit = new String();
    private String idCaisseProfessionnelle = new String();
    private String idCompte = new String();
    private String idCompteCourant = new String();
    private String idExterne = new String();
    private String idJournal = new String();
    private String montant = "0";
    private String noEcritureDouble;
    private String numCompteCG = new String();

    private String piece = new String();

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return CAOperation.TABLE_CAOPERP;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdCompte(statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTE));
        setIdCompteCourant(statement.dbReadNumeric(CAOperation.FIELD_IDCOMPTECOURANT));
        setCodeDebitCredit(statement.dbReadString(CAOperation.FIELD_CODEDEBITCREDIT));
        setPiece(statement.dbReadString(CAOperation.FIELD_PIECE));
        setMontant(statement.dbReadNumeric(CAOperation.FIELD_MONTANT));
        setIdJournal(statement.dbReadString(CAOperation.FIELD_IDJOURNAL));
        setNumCompteCG(statement.dbReadString(CARubrique.FIELD_NUMCOMPTECG));
        setIdExterne(statement.dbReadString(CARubrique.FIELD_IDEXTERNE));
        setIdCaisseProfessionnelle(statement.dbReadNumeric(CAOperation.FIELD_IDCAISSEPROFESSIONNELLE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

    }

    /**
     * @return
     */
    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public String getIdCaisseProfessionnelle() {
        return idCaisseProfessionnelle;
    }

    /**
     * @return
     */
    public String getIdCompte() {
        return idCompte;
    }

    /**
     * @return
     */
    public String getIdCompteCourant() {
        return idCompteCourant;
    }

    /**
     * @return
     */
    public String getIdExterne() {
        return idExterne;
    }

    public String getIdJournal() {
        return idJournal;
    }

    /**
     * @return
     */
    public String getMontant() {
        return montant;
    }

    public String getMontantAbs() {
        FWCurrency tmp = new FWCurrency(getMontant());
        tmp.abs();
        return tmp.toString();
    }

    /**
     * @return
     */
    public String getNoEcritureDouble() {
        return noEcritureDouble;
    }

    /**
     * @return
     */
    public String getNumCompteCG() {
        return numCompteCG;
    }

    /**
     * @return
     */
    public String getPiece() {
        return piece;
    }

    /**
     * Return la query a éxécuté pour mettre à jour l'état des opérations en mode comptabilisé (si noEcritureCollective
     * nest pas vide la mettre à jour également).
     * 
     * @param s
     * @return
     */
    private String getUpdateQuery(BStatement s) {
        String updateQuery = "UPDATE " + new CASumOperationManager().getCollection() + CAOperation.TABLE_CAOPERP;
        updateQuery += " SET " + CAOperation.FIELD_ETAT + " = "
                + this._dbWriteNumeric(s.getTransaction(), APIOperation.ETAT_COMPTABILISE, "etat");

        if (!JadeStringUtil.isIntegerEmpty(getNoEcritureDouble())) {
            updateQuery += "," + CAOperation.FIELD_NOECRCOL + " = "
                    + this._dbWriteNumeric(s.getTransaction(), getNoEcritureDouble(), "noEcritureCollective");
        }

        updateQuery += " WHERE " + CAOperation.FIELD_IDCOMPTE + " = "
                + this._dbWriteNumeric(s.getTransaction(), getIdCompte(), "idCompte") + " AND ";
        updateQuery += CAOperation.FIELD_IDCOMPTECOURANT + " = "
                + this._dbWriteNumeric(s.getTransaction(), getIdCompteCourant(), "idCompteCourant") + " AND ";
        updateQuery += CAOperation.FIELD_CODEDEBITCREDIT + " = "
                + this._dbWriteString(s.getTransaction(), getCodeDebitCredit(), "codeDebitCredit") + " AND ";

        if (!JadeStringUtil.isBlank(getPiece())) {
            updateQuery += CAOperation.FIELD_PIECE + " = "
                    + this._dbWriteString(s.getTransaction(), getPiece(), "piece") + " AND ";
        }

        updateQuery += CAOperation.FIELD_IDJOURNAL + " = "
                + this._dbWriteNumeric(s.getTransaction(), getIdJournal(), "idJournal");

        return updateQuery;
    }

    public boolean isMontantZero() {
        return (new FWCurrency(getMontant())).isZero();
    }

    /**
     * @param string
     */
    public void setCodeDebitCredit(String string) {
        codeDebitCredit = string;
    }

    public void setIdCaisseProfessionnelle(String idCaisseProfessionnelle) {
        this.idCaisseProfessionnelle = idCaisseProfessionnelle;
    }

    /**
     * @param string
     */
    public void setIdCompte(String string) {
        idCompte = string;
    }

    /**
     * @param string
     */
    public void setIdCompteCourant(String string) {
        idCompteCourant = string;
    }

    /**
     * @param string
     */
    public void setIdExterne(String string) {
        idExterne = string;
    }

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

    /**
     * @param string
     */
    public void setNoEcritureDouble(String string) {
        noEcritureDouble = string;
    }

    /**
     * @param string
     */
    public void setNumCompteCG(String string) {
        numCompteCG = string;
    }

    /**
     * @param string
     */
    public void setPiece(String string) {
        piece = string;
    }

    /**
     * toString methode: creates a String representation of the object
     * 
     * @return the String representation
     * @author info.vancauwenberge.tostring plugin
     */
    public String toMyString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CASumOperation[");
        buffer.append("idCompte = ").append(idCompte);
        buffer.append(", codeDebitCredit = ").append(codeDebitCredit);
        buffer.append(", idCompteCourant = ").append(idCompteCourant);
        buffer.append(", piece = ").append(piece);
        buffer.append(", montant = ").append(montant);
        buffer.append(", numCompteCG = ").append(numCompteCG);
        buffer.append(", idExterne = ").append(idExterne);
        buffer.append(", noEcritureCollective = ").append(noEcritureDouble);
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * Exécute la mise à jour des opérations groupées.
     * 
     * @param transaction
     * @throws Exception
     */
    public void update(BTransaction transaction) throws Exception {
        BStatement s = new BStatement(transaction);
        s.createStatement();

        s.execute(getUpdateQuery(s));

        s.closeStatement();
    }
}
