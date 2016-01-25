/*
 * Créé le Apr 6, 2005
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.osiris.db.comptes;

import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersUserValue;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIAuxiliaire;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIRubrique;
import java.util.Vector;

/**
 * @author dda Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CAAuxiliaire extends CAOperation implements APIAuxiliaire {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final static String LABEL_COMPTE_ANNEXE_NON_AUXILIAIRE = "COMPTE_ANNEXE_NON_AUXILIAIRE";
    private final static String LABEL_OPERATION_NON_AUXILIAIRE = "OPERATION_NON_AUXILIAIRE";

    public static void inverserCodeDebitCredit(CAAuxiliaire auxiliaire) {
        // Inverser le signe
        if (auxiliaire.getCodeDebitCredit().equals(APIEcriture.CREDIT)) {
            auxiliaire.setCodeDebitCredit(APIEcriture.EXTOURNE_CREDIT);
        } else if (auxiliaire.getCodeDebitCredit().equals(APIEcriture.DEBIT)) {
            auxiliaire.setCodeDebitCredit(APIEcriture.EXTOURNE_DEBIT);
        } else if (auxiliaire.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_CREDIT)) {
            auxiliaire.setCodeDebitCredit(APIEcriture.CREDIT);
        } else if (auxiliaire.getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
            auxiliaire.setCodeDebitCredit(APIEcriture.DEBIT);
        }
    }

    private String codeDebitCreditEcran = null;
    private String idExterneRubriqueEcran = "";
    private String montantEcran = null;

    private CARubrique rubrique = null;

    public CAAuxiliaire() {
        super();
        setIdTypeOperation(APIOperation.CAAUXILIAIRE);
    }

    public CAAuxiliaire(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAAUXILIAIRE);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.osiris.db.comptes.CAOperation#_beforeAdd(globaz.globall.db. BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);

        if (JadeStringUtil.isBlank(getIdTypeOperation())) {
            // Forcer le type d'opération
            setIdTypeOperation(APIOperation.CAAUXILIAIRE);
        }

        checkDebitCredit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CAOperation#_beforeUpdate(globaz.globall.db. BTransaction)
     */
    @Override
    protected void _beforeUpdate(BTransaction transaction) throws Exception {
        super._beforeUpdate(transaction);

        checkDebitCredit();
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#_createExtourne(globaz.globall.db.BTransaction, java.lang.String)
     */
    @Override
    protected CAOperation _createExtourne(BTransaction transaction, String text) throws Exception {
        // Création d'un double de l'écriture
        CAAuxiliaire extourne = new CAAuxiliaire();
        extourne.dupliquer(this);

        // Libellé du texte si saisi
        if (!JadeStringUtil.isBlank(text) && (text.length() > 40)) {
            extourne.setLibelle(text.substring(0, 40));
        } else {
            extourne.setLibelle(text);
        }

        // Inverser le signe
        CAAuxiliaire.inverserCodeDebitCredit(extourne);

        // Retourner l'opération
        return extourne;
    }

    /**
     * Créé le : 25 janv. 07
     * 
     * @param transaction
     */
    private void _synchroRubriqueFromEcran(BTransaction transaction) {
        // Si l'id est null, on met à zéro le compte
        if (JadeStringUtil.isBlank(getIdExterneRubriqueEcran())) {
            setIdCompte("");
        } else if ((getCompte() == null) || !getCompte().getIdExterne().equals(getIdExterneRubriqueEcran())) {
            // Instancier un nouveau bean
            CARubrique _rub = new CARubrique();
            _rub.setSession(getSession());
            _rub.setAlternateKey(APIRubrique.AK_IDEXTERNE);
            _rub.setIdExterne(getIdExterneRubriqueEcran());

            // Lecture
            try {
                _rub.retrieve(transaction);
                // En cas d'erreur, on remet à zéro
                if (_rub.isNew()) {
                    setIdCompte("");
                    _addError(transaction, getSession().getLabel("5115"));
                } else {
                    setIdCompte(_rub.getIdRubrique());
                    rubrique = _rub;
                }
            } catch (Exception e) {
                setIdCompte("");
                return;
            }
        }
    }

    /**
     * mise à jour du fichier AJPPVUT pour les valeur par défaut par utilisateur
     */
    @Override
    protected void _synchroValUtili() {
        // mise à jour du fichier FWParametersUserValue - AJPPVUT
        if (valeurUtilisateur == null) {
            valeurUtilisateur = new Vector(6);
        }
        if (!JadeStringUtil.isBlank(getNomEcran())) {
            // chargement des données à mémoriser dans le vecteur
            valeurUtilisateur.removeAllElements();
            valeurUtilisateur.add(0, getIdExterneRoleEcran());
            valeurUtilisateur.add(1, getIdRoleEcran());
            valeurUtilisateur.add(2, getIdExterneSectionEcran());
            valeurUtilisateur.add(3, getIdTypeSectionEcran());
            valeurUtilisateur.add(4, getIdExterneCompteCourantEcran());
            valeurUtilisateur.add(5, getDate());
            valeurUtilisateur.add(6, getIdExterneRubriqueEcran());
            // mise à jour dans le fichier
            FWParametersUserValue valUtili = new FWParametersUserValue();
            valUtili.setSession(getSession());
            valUtili.addValeur("CAAuxiliaire", getNomEcran(), valeurUtilisateur);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CAOperation#_validate(globaz.globall.db.BStatement )
     */
    @Override
    protected void _validate(BStatement statement) {
        // Laisser la superclasse effectuer son traitement
        super._validate(statement);

        // Vérifier le type d'opération
        if ((!isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE))
                && (!isInstanceOrSubClassOf(APIOperation.CAAUXILIAIRE_PAIEMENT))) {
            _addError(statement.getTransaction(), getSession().getLabel(CAAuxiliaire.LABEL_OPERATION_NON_AUXILIAIRE));
        }

        if (!getCompteAnnexe().isCompteAuxiliaire()) {
            _addError(statement.getTransaction(), getSession()
                    .getLabel(CAAuxiliaire.LABEL_COMPTE_ANNEXE_NON_AUXILIAIRE));
        }
    }

    /**
     * Validation des données
     */
    @Override
    protected void _valider(BTransaction transaction) {
        // Valider les données de la superclasse
        super._valider(transaction);

        // Récupérer la rubrique en provenance de l'écran
        if (getSaisieEcran().booleanValue()) {
            _synchroRubriqueFromEcran(transaction);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.db.comptes.CAOperation#_writeProperties(globaz.globall. db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        super._writeProperties(statement);

        statement.writeField("CODEDEBITCREDIT",
                this._dbWriteString(statement.getTransaction(), getCodeDebitCredit(), "codeDebitCredit"));
        statement.writeField("LIBELLE", this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("MONTANT", this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField("PIECE", this._dbWriteString(statement.getTransaction(), getPiece(), "piece"));
        statement.writeField(CAOperation.FIELD_IDCOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));
        statement.writeField(CAOperation.FIELD_PROVENANCEPMT,
                this._dbWriteNumeric(statement.getTransaction(), getProvenancePmt(), "provenancePmt"));
    }

    private void checkDebitCredit() {
        double mont = 0.0;
        try {
            mont = Double.parseDouble(JANumberFormatter.deQuote(montantEcran));
        } catch (Exception e) {
        }

        // Récupérer la masse si montant nul
        if (mont < 0.0) {
            if (getCodeDebitCreditEcran().equals("D")) {
                setCodeDebitCredit(APIAuxiliaire.EXTOURNE_DEBIT);
            } else {
                setCodeDebitCredit(APIAuxiliaire.EXTOURNE_CREDIT);
            }
        } else if (getCodeDebitCreditEcran().equals("D")) {
            setCodeDebitCredit(APIAuxiliaire.DEBIT);
        } else {
            setCodeDebitCredit(APIAuxiliaire.CREDIT);
        }
    }

    /**
     * @see globaz.osiris.db.comptes.CAOperation#dupliquer(globaz.osiris.db.comptes.CAOperation)
     */
    public void dupliquer(CAAuxiliaire oper) {
        // Dupliquer les paramètres de la superclasse
        super.dupliquer(oper);

        // Copier les autres paramètres
        if (oper != null) {
            setCodeDebitCredit(oper.getCodeDebitCredit());
            setLibelle(oper.getLibelle());
            setMontant(oper.getMontant());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIAuxiliaire#getCodeDebitCredit()
     */
    @Override
    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public String getCodeDebitCreditEcran() {
        // Si pas encore chargé
        if (codeDebitCreditEcran == null) {
            if (getCodeDebitCredit().equals(APIAuxiliaire.DEBIT)) {
                codeDebitCreditEcran = "D";
            } else {
                codeDebitCreditEcran = "C";
            }
        }

        return codeDebitCreditEcran;
    }

    /**
     * @return CARubrique
     */
    @Override
    public CARubrique getCompte() {
        if (rubrique == null) {
            rubrique = new CARubrique();
            rubrique.setISession(getSession());
            rubrique.setIdRubrique(getIdCompte());
            try {
                rubrique.retrieve();
                if (rubrique.isNew()) {
                    rubrique = null;
                }
            } catch (Exception e) {
                rubrique = null;
            }
        }
        return rubrique;
    }

    /**
     * @return String
     */
    public String getIdExterneRubriqueEcran() {
        if (getCompte() != null) {
            idExterneRubriqueEcran = getCompte().getIdExterne();
        }
        return idExterneRubriqueEcran;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIAuxiliaire#getLibelle()
     */
    @Override
    public String getLibelle() {
        return libelle;
    }

    @Override
    public String getMontant() {
        String montantSigne = JANumberFormatter.deQuote(montant);

        // Si le code débit crédit est défini
        if (getCodeDebitCredit() != null) {
            try {
                double montantTemp = Double.parseDouble(montantSigne);

                if (getCodeDebitCredit().equals(APIAuxiliaire.CREDIT)
                        || getCodeDebitCredit().equals(APIAuxiliaire.EXTOURNE_DEBIT)) {
                    // Signe négatif (crédit, extourne débit)
                    montantTemp = -Math.abs(montantTemp);
                } else {
                    // Signe positif (débit, extoure crédit)
                    montantTemp = Math.abs(montantTemp);
                }

                montantSigne = JANumberFormatter.formatNoRound(String.valueOf(montantTemp), 2);
            } catch (NumberFormatException e) {
            }
        }

        // Retourner le montant
        return JANumberFormatter.deQuote(montantSigne);
    }

    public String getMontantEcran() {
        // Si montant écran pas encore chargé
        if (montantEcran == null) {
            double mont = 0.0;
            montantEcran = montant;

            // Si le code débit crédit est défini
            if (getCodeDebitCredit() != null) {
                try {
                    mont = Double.parseDouble(JANumberFormatter.deQuote(montant));
                    if (getCodeDebitCredit().equals(APIAuxiliaire.DEBIT)
                            || getCodeDebitCredit().equals(APIAuxiliaire.CREDIT)) {
                        // Signe positif (DEBIT, CREDIT)
                        mont = Math.abs(mont);
                    } else {
                        // Signe négatif (extourne débit, extoure crédit)
                        mont = -Math.abs(mont);
                    }
                    montantEcran = JANumberFormatter.formatNoRound(String.valueOf(mont), 2);
                } catch (NumberFormatException e) {
                }
            }
        }

        // Retourner le montant
        return JANumberFormatter.deQuote(montantEcran);
    }

    @Override
    public String getPiece() {
        return piece;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIAuxiliaire#setCodeDebitCredit(java.lang.String)
     */
    @Override
    public void setCodeDebitCredit(String newCodeDebitCredit) {
        codeDebitCredit = newCodeDebitCredit;
    }

    public void setCodeDebitCreditEcran(String newCodeDebitCreditEcran) {
        codeDebitCreditEcran = newCodeDebitCreditEcran;
    }

    /**
     * @param newIdCompte
     */
    @Override
    public void setIdCompte(String newIdCompte) {
        idCompte = newIdCompte;
        rubrique = null;
    }

    /**
     * @author: sel Créé le : 26 janv. 07
     * @param string
     */
    public void setIdExterneRubriqueEcran(String string) {
        idExterneRubriqueEcran = string;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.api.APIAuxiliaire#setMontant(java.lang.String)
     */
    @Override
    public void setMontant(String newMontant) {
        montant = newMontant;
    }

    public void setMontantEcran(String newMontantEcran) {
        montantEcran = newMontantEcran;
        montant = newMontantEcran;
    }

    @Override
    public void setPiece(String s) {
        piece = s;
    }

}
