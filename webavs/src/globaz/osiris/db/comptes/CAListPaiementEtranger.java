package globaz.osiris.db.comptes;

import globaz.framework.util.FWLog;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEcriture;

/**
 * Insérez la description du type ici. Date de création : (18.01.2002 08:44:22)
 * 
 * @author: Administrator
 */
public class CAListPaiementEtranger extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CARole caRole = null;
    private String codeDebitCredit = new String();
    private String date = new String();
    private String descriptionCompteAnnexe = new String();
    private String idCompteAnnexe = new String();
    private String idExterneRole = new String();
    private String idLog = new String();

    private String idOperation = new String();
    private String idRole = new String();

    private FWLog log = null;
    private String montant = new String();

    /**
     * Commentaire relatif au constructeur CAOperation
     */
    public CAListPaiementEtranger() {
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAOPERP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idOperation = statement.dbReadNumeric("IDOPERATION");
        idCompteAnnexe = statement.dbReadNumeric("IDCOMPTEANNEXE");
        idRole = statement.dbReadNumeric("IDROLE");
        idLog = statement.dbReadNumeric("IDLOG");
        date = statement.dbReadDateAMJ("DATE");
        codeDebitCredit = statement.dbReadString("CODEDEBITCREDIT");
        descriptionCompteAnnexe = statement.dbReadString("DESCRIPTION");
        idExterneRole = statement.dbReadString("IDEXTERNEROLE");
        montant = statement.dbReadNumeric("MONTANT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // TODO Raccord de méthode auto-généré

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // TODO Raccord de méthode auto-généré

    }

    /**
     * @return
     */
    public CARole getCaRole() {
        return caRole;
    }

    public CARole getCARole() {
        if (caRole == null) {
            caRole = new CARole();
            caRole.setISession(getSession());
            caRole.setIdRole(getIdRole());
            try {
                caRole.retrieve();
                if (caRole.isNew()) {
                    caRole = null;
                }
            } catch (Exception e) {
                caRole = null;
            }
        }

        return caRole;
    }

    /**
     * @return
     */
    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    /**
     * @return
     */
    public String getCodeDebitCreditEcran() {
        if (APIEcriture.DEBIT.equals(codeDebitCredit) || APIEcriture.EXTOURNE_DEBIT.equals(codeDebitCredit)) {
            return "D";
        } else {
            return "C";
        }

    }

    /**
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * @return
     */
    public String getDescriptionCompteAnnexe() {
        return descriptionCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * @return
     */
    public String getIdExterneRole() {
        return idExterneRole;
    }

    /**
     * @return
     */
    public String getIdLog() {
        return idLog;
    }

    /**
     * @return
     */
    public String getIdOperation() {
        return idOperation;
    }

    /**
     * @return
     */
    public String getIdRole() {
        return idRole;
    }

    public FWLog getLog() {

        // Si le log n'existe pas, retourner null
        if (JadeStringUtil.isIntegerEmpty(getIdLog())) {
            return null;
        }

        // Si log pas déjà chargé
        if (log == null) {
            // Instancier un nouveau LOG
            log = new FWLog();
            log.setSession(getSession());

            // Récupérer le log en question
            log.setIdLog(getIdLog());
            try {
                log.retrieve();
                if (log.isNew()) {
                    log = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                log = null;
            }
        }

        return log;
    }

    /**
     * @return
     */
    public String getMontant() {
        return montant;
    }

    public String getMontantEcran() {

        double _montant = 0.0;
        String montantEcran = montant;

        // Si le code débit crédit est défini
        if (getCodeDebitCredit() != null) {
            try {
                _montant = Double.parseDouble(JANumberFormatter.deQuote(montant));
                // Signe positif (DEBIT, CREDIT)
                if (getCodeDebitCredit().equals(APIEcriture.DEBIT) || getCodeDebitCredit().equals(APIEcriture.CREDIT)) {
                    _montant = Math.abs(_montant);
                    // Signe négatif (extourne débit, extoure crédit)
                } else {
                    _montant = -Math.abs(_montant);
                }
                montantEcran = JANumberFormatter.formatNoRound(String.valueOf(_montant), 2);
            } catch (NumberFormatException e) {
            }
        }
        // Retourner le montant
        return JANumberFormatter.deQuote(montantEcran);
    }

    /**
     * @param role
     */
    public void setCaRole(CARole role) {
        caRole = role;
    }

    /**
     * @param string
     */
    public void setCodeDebitCredit(String string) {
        codeDebitCredit = string;
    }

    /**
     * @param string
     */
    public void setDate(String string) {
        date = string;
    }

    /**
     * @param string
     */
    public void setDescriptionCompteAnnexe(String string) {
        descriptionCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setIdCompteAnnexe(String string) {
        idCompteAnnexe = string;
    }

    /**
     * @param string
     */
    public void setIdExterneRole(String string) {
        idExterneRole = string;
    }

    /**
     * @param string
     */
    public void setIdLog(String string) {
        idLog = string;
    }

    /**
     * @param string
     */
    public void setIdOperation(String string) {
        idOperation = string;
    }

    /**
     * @param string
     */
    public void setIdRole(String string) {
        idRole = string;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

}
