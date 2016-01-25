package globaz.osiris.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.ordres.CAOrganeExecution;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

/**
 * Classe : type_conteneur Description : Date de création: 7 juin 04
 * 
 * @author scr
 */
public class CAPaiementEtranger extends CAOperation implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String codeDebitCreditEcran = null;

    private String codeIsoME = new String();

    private String coursME = new String();
    private String idOrganeExecution = new String();
    private String montantEcran = null;

    private String montantME = new String();
    private String montantMEEcran = null;
    private CARubrique rubrique;

    /**
     * Constructor for CAPaiementEtrangerViewBean.
     */
    public CAPaiementEtranger() {
        super();
        setIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);
    }

    public CAPaiementEtranger(CAOperation parent) {
        super(parent);

        setIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);
    }

    @Override
    protected void _beforeActiver(BTransaction tr) {
        try {
            CAGroupement grp = null;

            FWCurrency montantCompensatoir = new FWCurrency(getMontant());
            if (montantCompensatoir.isZero()) {
                return;
            }

            // Création d'un groupement MASTER
            grp = new CAGroupement();
            grp.setSession(getSession());
            grp.setTypeGroupement(CAGroupement.MASTER);
            grp.setIdOperationMaster(getIdOperation());
            grp.add(tr);
            if (tr.hasErrors()) {
                _addError(tr, getSession().getLabel("PMT_ETRANGER_GRP_ERROR"));
                return;
            }

            FWCurrency montantPourProposition = new FWCurrency(montantCompensatoir.toString());
            montantPourProposition.negate();

            String[] args = new String[1];
            args[0] = montantPourProposition.toString();

            Collection listeSections = getCompteAnnexe().getListeSections(APICompteAnnexe.PC_ORDRE_PLUS_ANCIEN);

            // Si pas de facture, on crée une section de type 1, no : yyyy80000
            if ((listeSections == null) || (listeSections.size() == 0)) {

                // On doit créer une section
                CASection section = new CASection();
                section.setSession(getSession());
                section.setIdCompteAnnexe(getCompteAnnexe().getIdCompteAnnexe());
                section.setIdTypeSection("1");

                Calendar cal = new java.util.GregorianCalendar();
                int year = cal.get(Calendar.YEAR);
                section.setIdExterne(String.valueOf(year) + "00000");
                section.setCategorieSection("227000");
                section.setDateSection(getDate());
                section.setIdJournal(getJournal().getIdJournal());
                section.setAlternateKey(CASection.AK_IDEXTERNE);
                section.retrieve(tr);
                if (section.isNew()) {
                    section.add(tr);
                }

                CAPaiement paiement = new CAPaiement();
                paiement.setIdJournal(getIdJournal());
                paiement.setMontant(montantCompensatoir.toString());
                paiement.setDate(getDate());
                paiement.setIdCompte(getIdCompte());
                paiement.setCodeDebitCredit(getCodeDebitCredit());
                paiement.setIdCompteAnnexe(getCompteAnnexe().getIdCompteAnnexe());
                paiement.setIdSection(section.getIdSection());
                paiement.setIdTypeOperation(APIOperation.CAPAIEMENT);
                paiement.setIdCompte(getIdCompte());
                paiement.setLibelle(getLibelle());
                paiement.add(tr);
                createLinkWithMaster(grp, paiement, tr);
                return;
            }

            for (Iterator iter = listeSections.iterator(); iter.hasNext();) {
                CASection section = (CASection) iter.next();
                FWCurrency solde = section.getSoldeToCurrency();
                boolean isLastSection = !iter.hasNext();

                CAPaiement paiement = new CAPaiement();
                paiement.setIdJournal(getIdJournal());
                paiement.setDate(getDate());
                paiement.setIdCompte(getIdCompte());
                paiement.setCodeDebitCredit(getCodeDebitCredit());
                paiement.setIdCompteAnnexe(getCompteAnnexe().getIdCompteAnnexe());
                paiement.setIdSection(section.getIdSection());
                paiement.setIdTypeOperation(APIOperation.CAPAIEMENT);
                paiement.setIdCompte(getIdCompte());
                paiement.setLibelle(getLibelle());

                if (isLastSection) {
                    paiement.setMontant(montantCompensatoir.toString());
                    paiement.add(tr);
                    createLinkWithMaster(grp, paiement, tr);
                    break;
                } else {
                    // montantCompensatoir > 0 && soldeSection < 0
                    if (((montantCompensatoir.compareTo(new FWCurrency(0)) > 0) && (solde.compareTo(new FWCurrency(0)) < 0))
                            || ((montantCompensatoir.compareTo(new FWCurrency(0)) < 0) && (solde
                                    .compareTo(new FWCurrency(0)) > 0))) {

                        FWCurrency absMC = new FWCurrency(montantCompensatoir.toString());
                        absMC.abs();

                        FWCurrency absSC = new FWCurrency(solde.toString());
                        absSC.abs();

                        if (absMC.compareTo(absSC) > 0) {
                            paiement.setMontant(solde.toString());
                        } else {
                            paiement.setMontant(montantCompensatoir.toString());
                        }
                        paiement.add(tr);
                        createLinkWithMaster(grp, paiement, tr);

                        montantCompensatoir.sub(paiement.getMontantToCurrency());
                        if (montantCompensatoir.isZero()) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            _addError(tr, e.getMessage());
        }
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // Laisser la superclasse s'initialiser
        super._beforeAdd(transaction);

        // Forcer le type d'opération
        setIdTypeOperation(APIOperation.CAPAIEMENTETRANGER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        super._readProperties(statement);

        setCoursME(statement.dbReadNumeric("AETCOU"));
        setMontantME(statement.dbReadNumeric("AEMOET"));
        setCodeIsoME(statement.dbReadString("AECISO"));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (21.02.2002 08:50:10)
     * 
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        boolean isSaisieEcran = getSaisieEcran().booleanValue();

        if ((getCompteAnnexe() != null) && (getCompteAnnexe().isASurveiller().booleanValue())
                && (!getQuittanceLogEcran().booleanValue()) && (isSaisieEcran)) {
            getMemoryLog().logMessage(getSession().getLabel("SOUS_SURVEILLANCE"), FWMessage.ERREUR,
                    this.getClass().getName());
        }

        if ((getCompteAnnexe() != null) && (getCompteAnnexe().isVerrouille())
                && (!getQuittanceLogEcran().booleanValue())) {
            getMemoryLog().logMessage(getSession().getLabel("VERROUILLE"), FWMessage.ERREUR, this.getClass().getName());
        }

        // Pour éviter la validation complète
        setSaisieEcran("false");
        super._validate(statement);

        if (isSaisieEcran) {
            setSaisieEcran("true");

            // Charger montant écran
            if ((montantEcran != null) && (montantEcran.trim().length() > 0)) {
                setMontant(JANumberFormatter.deQuote(montantEcran));

                double _mont = 0.0;
                try {
                    _mont = Double.parseDouble(JANumberFormatter.deQuote(montantEcran));
                } catch (Exception e) {
                    _addError(statement.getTransaction(), getSession().getLabel("NUMBER_FORMAT_EXCEPTION_PARSE_DOUBLE"));
                }

                if (_mont < 0.0) {
                    if (codeDebitCreditEcran.equals("D")) {
                        setCodeDebitCredit(APIEcriture.EXTOURNE_DEBIT);
                    } else {
                        setCodeDebitCredit(APIEcriture.EXTOURNE_CREDIT);
                    }
                } else if (codeDebitCreditEcran.equals("D")) {
                    setCodeDebitCredit(APIEcriture.DEBIT);
                } else {
                    setCodeDebitCredit(APIEcriture.CREDIT);
                }

            }

            if ((montantMEEcran != null) && (montantMEEcran.trim().length() > 0)) {
                setMontantME(JANumberFormatter.deQuote(montantMEEcran));

                double _mont = 0.0;
                try {
                    _mont = Double.parseDouble(JANumberFormatter.deQuote(montantMEEcran));
                } catch (Exception e) {
                    _addError(statement.getTransaction(), getSession().getLabel("NUMBER_FORMAT_EXCEPTION_PARSE_DOUBLE"));
                }

                if (_mont < 0.0) {
                    if (codeDebitCreditEcran.equals("D")) {
                        setCodeDebitCredit(APIEcriture.EXTOURNE_DEBIT);
                    } else {
                        setCodeDebitCredit(APIEcriture.EXTOURNE_CREDIT);
                    }
                } else if (codeDebitCreditEcran.equals("D")) {
                    setCodeDebitCredit(APIEcriture.DEBIT);
                } else {
                    setCodeDebitCredit(APIEcriture.CREDIT);
                }

            }

            // Remise à blanc
            codeDebitCreditEcran = null;
            montantEcran = null;
            montantMEEcran = null;

            boolean isMontantMonnaieEtrangere = false;
            boolean isCours = false;
            boolean isMontantCHF = false;

            if (JadeStringUtil.isBlank(getCodeIsoME())) {
                _addError(statement.getTransaction(), getSession().getLabel("PMT_ETRANGER_CODE_ISO_ME_VIDE"));
            }

            // un montant en monnaie étrangère est saisie
            if (!JadeStringUtil.isDecimalEmpty(getMontantME())) {
                isMontantMonnaieEtrangere = true;
            }

            // le cours est saisie
            if (!JadeStringUtil.isDecimalEmpty(getCoursME())) {
                isCours = true;
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("PMT_ETRANGER_COURS_VIDE"));
            }

            // un montant chf est saisi
            if (!JadeStringUtil.isDecimalEmpty(getMontant())) {
                isMontantCHF = true;
            }
            if (!isMontantCHF && !isMontantMonnaieEtrangere) {
                _addError(statement.getTransaction(), getSession().getLabel("PMT_ETRANGER_MONTANT_ET_MONTANT_ME_VIDE"));
            }

            // Conversion

            // Règle de plausibilités :
            // montant CHF + cours --> ok
            // montant CHF + montant Monnaie --> ok
            // montant Monnaie + cours --> ok
            // montant CHF + cours + montant monnaie --> ok

            // Calcul des valeurs manquantes
            // montant CHF * cours = montant monnaie
            // montant monnaie / cours = montant CHF
            // montant CHF / montant monnaie = cours

            // plausi : montant CHF + cours --> calcul montant monnaie
            // plausi : montant CHF + cours + montant monnaie

            if (isMontantCHF && isMontantMonnaieEtrangere && isCours) {
                // OK, rien à faire
            } else if (isMontantCHF && isCours && !isMontantMonnaieEtrangere) {
                BigDecimal montant = new BigDecimal(getMontant());
                montant = montant.setScale(5);
                BigDecimal cours = new BigDecimal(getCoursME());
                montant = montant.divide(cours, 5, BigDecimal.ROUND_HALF_EVEN);

                // Arrondi au centimes, sur 2 digit après la virgule.
                FWCurrency montantMonnaie = new FWCurrency(JANumberFormatter.format(montant.toString(), 0.01, 2,
                        JANumberFormatter.NEAR));
                setMontantME(montantMonnaie.toString());
                // plausi : montant CHF + montant Monnaie --> calcul cours
            } else if (isMontantCHF && isMontantMonnaieEtrangere && !isCours) {
                BigDecimal montant1 = new BigDecimal(getMontant());
                BigDecimal montant2 = new BigDecimal(getMontantME());
                BigDecimal cours = montant1.divide(montant2, 5, BigDecimal.ROUND_HALF_EVEN);
                setCoursME(new FWCurrency(
                        JANumberFormatter.format(cours.toString(), 0.00001, 5, JANumberFormatter.NEAR)).toString());
                // plausi montant Monnaie / cours
            } else if (isMontantMonnaieEtrangere && isCours && !isMontantCHF) {
                BigDecimal montantMonnaie = new BigDecimal(getMontantME());
                BigDecimal cours = new BigDecimal(getCoursME());
                montantMonnaie = montantMonnaie.multiply(cours);

                // Arrondi au centimes, sur 2 digit après la virgule.
                FWCurrency montant = new FWCurrency(JANumberFormatter.format(montantMonnaie.toString(), 0.01, 2,
                        JANumberFormatter.NEAR));
                setMontant(montant.toString());
                // error, des paramètres sont manquant
            } else {
                _addError(statement.getTransaction(), getSession().getLabel("PMT_ETRANGER_PARAMETRE_MANQUANT"));
            }

            if (JadeStringUtil.isBlank(getIdCompte())) {
                _addError(statement.getTransaction(), getSession().getLabel("5114"));
            }
        }

        // Vérifier le type d'opération
        if (!isInstanceOrSubClassOf(APIOperation.CAPAIEMENTETRANGER)) {
            _addError(statement.getTransaction(), getSession().getLabel("5165"));
        }

        // Si céeation d'un paiement étranger via fichier, l'interface fournit
        // l'id de lprgane d'execution qu'il faut convertir pour récupérer
        // l'id de la rubrique.

        // Si création d'un paiement par saisie manuelle, l'interface fournit
        // l'id du compte (id de la rubrique) -> pas besoin de rechercher
        // l'organe d'exécution.
        if (idCompte == null) {
            CAOrganeExecution organeExec = new CAOrganeExecution();
            organeExec.setSession(getSession());
            organeExec.setIdOrganeExecution(idOrganeExecution);
            try {
                organeExec.retrieve(statement.getTransaction());
            } catch (Exception e) {
                JadeLogger.error(this, e);
                _addError(statement.getTransaction(), getSession().getLabel("PMT_ETRANGER_ORGANE_EXEC_ERROR"));
            }
            if ((organeExec == null) || organeExec.isNew()) {
                _addError(statement.getTransaction(), getSession().getLabel("PMT_ETRANGER_ORGANE_EXEC_ERROR"));
            }
            setIdCompte(organeExec.getIdRubrique());
        }

        // Controler l'état du log et demander une quittance si >= avertissement
        if ((getLog() != null) && (getLog().getErrorLevel().compareTo(FWMessage.AVERTISSEMENT) >= 0)) {
            if (!getQuittanceLogEcran().booleanValue() && getSaisieEcran().booleanValue()) {
                // getLog().logMessage("5162", null, FWMessage.ERREUR,
                // getClass().getName());
                _addError(statement.getTransaction(), getSession().getLabel("5162"));
            }
            // else {
            // if (getSaisieEcran().booleanValue())
            // getLog().clear(statement.getTransaction());
            // }
        }
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws java.lang.Exception {

        // Laisser la superclasse effectuer son traitement
        super._writeProperties(statement);

        // Traitement interne
        statement.writeField("LIBELLE", this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
        statement.writeField("MONTANT", this._dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField("PIECE", this._dbWriteString(statement.getTransaction(), getPiece(), "piece"));
        // statement.writeField("TAUX",
        // _dbWriteNumeric(statement.getTransaction(), getTaux(), "taux"));
        statement.writeField("IDCOMPTE", this._dbWriteNumeric(statement.getTransaction(), getIdCompte(), "idCompte"));

        statement.writeField("AETCOU", this._dbWriteNumeric(statement.getTransaction(), getCoursME(), "coursME"));
        statement.writeField("AEMOET", this._dbWriteNumeric(statement.getTransaction(), getMontantME(), "montantME"));
        statement.writeField("AECISO", this._dbWriteString(statement.getTransaction(), getCodeIsoME(), "codeIsoME"));
        statement.writeField("CODEDEBITCREDIT",
                this._dbWriteString(statement.getTransaction(), getCodeDebitCredit(), "codeDebitCredit"));

    }

    private void createLinkWithMaster(CAGroupement grp, CAPaiement paiement, BTransaction tr) throws Exception {
        // Création d'une liaision avec le master
        CAGroupementOperation grpOper = new CAGroupementOperation();
        grpOper.setSession(getSession());
        grpOper.setIdGroupement(grp.getIdGroupement());
        grpOper.setIdOperation(paiement.getIdOperation());
        grpOper.add(tr);
        if (tr.hasErrors()) {
            _addError(tr, getSession().getLabel("PMT_ETRANGER_GRP_OPERATION_ERROR"));
        }
    }

    /**
     * @return
     */
    @Override
    public String getCodeDebitCredit() {
        return codeDebitCredit;
    }

    public String getCodeDebitCreditEcran() {

        // Si pas encore chargé
        if (codeDebitCreditEcran == null) {
            if (getCodeDebitCredit().equals(APIEcriture.DEBIT)
                    || getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
                codeDebitCreditEcran = "D";
            } else {
                codeDebitCreditEcran = "C";
            }
        }

        return codeDebitCreditEcran;
    }

    /**
     * @return
     */
    public String getCodeIsoME() {
        return codeIsoME;
    }

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
     * @return
     */
    public String getCoursME() {
        return coursME;
    }

    public FWParametersSystemCodeManager getCsMonnaies() {
        FWParametersSystemCodeManager csMonnaies = null;

        // liste pas encore chargée, on la charge
        csMonnaies = new FWParametersSystemCodeManager();
        csMonnaies.setSession(getSession());
        csMonnaies.getListeCodesSup("PYMONNAIE", getSession().getIdLangue());

        return csMonnaies;
    }

    /**
     * Surcharge la méthode parent, pour éviter l'appel à _synchroChgValUtili();
     */
    @Override
    public String getDate() {
        return date;
    }

    /**
     * Returns the idCompte.
     * 
     * @return String
     */
    @Override
    public String getIdCompte() {
        return idCompte;
    }

    /**
     * Returns the idOrganeExecution.
     * 
     * @return String
     */
    public String getIdOrganeExecution() {
        return idOrganeExecution;
    }

    /**
     * Returns the libelle.
     * 
     * @return String
     */
    @Override
    public String getLibelle() {
        return libelle;
    }

    @Override
    public String getMontant() {
        double _montant = 0.0;

        // Vérifier que le montant ne soit pas vide
        if (JadeStringUtil.isBlank(montant)) {
            montant = "0.00";
        }

        String _montantSigné = JANumberFormatter.deQuote(montant);

        // Si le code débit crédit est défini
        if (getCodeDebitCredit() != null) {
            try {
                _montant = Double.parseDouble(JANumberFormatter.deQuote(montant));
                // Signe négatif (crédit, extourne débit)
                if (getCodeDebitCredit().equals(APIEcriture.CREDIT)
                        || getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
                    _montant = -Math.abs(_montant);
                    // Signe positif (débit, extoure crédit)
                } else {
                    _montant = Math.abs(_montant);
                }
                _montantSigné = JANumberFormatter.formatNoRound(String.valueOf(_montant), 2);
            } catch (NumberFormatException e) {
                JadeLogger.error(this, e);
            }
        }

        // Retourner le montant
        return JANumberFormatter.deQuote(_montantSigné);
    }

    public String getMontantEcran() {

        // Si montant écran pas encore chargé
        if (montantEcran == null) {
            double _montant = 0.0;

            // Vérifier que le montant ne soit pas vide
            if (JadeStringUtil.isBlank(montant)) {
                montant = "0.00";
            }

            montantEcran = montant;

            // Si le code débit crédit est défini
            if (getCodeDebitCredit() != null) {
                try {
                    _montant = Double.parseDouble(JANumberFormatter.deQuote(montant));
                    // Signe positif (DEBIT, CREDIT)
                    if (getCodeDebitCredit().equals(APIEcriture.DEBIT)
                            || getCodeDebitCredit().equals(APIEcriture.CREDIT)) {
                        _montant = Math.abs(_montant);
                        // Signe négatif (extourne débit, extoure crédit)
                    } else {
                        _montant = -Math.abs(_montant);
                    }
                    montantEcran = JANumberFormatter.formatNoRound(String.valueOf(_montant), 2);
                } catch (NumberFormatException e) {
                    JadeLogger.error(this, e);
                }
            }
        }

        // Retourner le montant
        return JANumberFormatter.deQuote(montantEcran);
    }

    /**
     * @return
     */
    public String getMontantME() {
        double _montant = 0.0;
        // Vérifier que le montant ne soit pas vide
        if (JadeStringUtil.isBlank(montantME)) {
            montantME = "0.00";
        }
        String _montantSigné = JANumberFormatter.deQuote(montantME);

        // Si le code débit crédit est défini
        if (getCodeDebitCredit() != null) {
            try {
                _montant = Double.parseDouble(JANumberFormatter.deQuote(montantME));
                // Signe négatif (crédit, extourne débit)
                if (getCodeDebitCredit().equals(APIEcriture.CREDIT)
                        || getCodeDebitCredit().equals(APIEcriture.EXTOURNE_DEBIT)) {
                    _montant = -Math.abs(_montant);
                    // Signe positif (débit, extoure crédit)
                } else {
                    _montant = Math.abs(_montant);
                }
                _montantSigné = JANumberFormatter.formatNoRound(String.valueOf(_montant), 2);
            } catch (NumberFormatException e) {
                JadeLogger.error(this, e);
            }
        }

        // Retourner le montant
        return JANumberFormatter.deQuote(_montantSigné);
    }

    public String getMontantMEEcran() {

        // Si montant écran pas encore chargé
        if (montantMEEcran == null) {
            double _montant = 0.0;
            // Vérifier que le montant ne soit pas vide
            if (JadeStringUtil.isBlank(montantME)) {
                montantME = "0.00";
            }
            montantMEEcran = montantME;

            // Si le code débit crédit est défini
            if (getCodeDebitCredit() != null) {
                try {
                    _montant = Double.parseDouble(JANumberFormatter.deQuote(montantME));
                    // Signe positif (DEBIT, CREDIT)
                    if (getCodeDebitCredit().equals(APIEcriture.DEBIT)
                            || getCodeDebitCredit().equals(APIEcriture.CREDIT)) {
                        _montant = Math.abs(_montant);
                        // Signe négatif (extourne débit, extoure crédit)
                    } else {
                        _montant = -Math.abs(_montant);
                    }
                    montantMEEcran = JANumberFormatter.formatNoRound(String.valueOf(_montant), 2);
                } catch (NumberFormatException e) {
                    JadeLogger.error(this, e);
                }
            }
        }

        // Retourner le montant
        return JANumberFormatter.deQuote(montantMEEcran);
    }

    /**
     * Returns the piece.
     * 
     * @return String
     */
    @Override
    public String getPiece() {
        return piece;
    }

    /**
     * Returns the taux.
     * 
     * @return String
     */
    @Override
    public String getTaux() {
        return taux;
    }

    /**
     * @param string
     */
    @Override
    public void setCodeDebitCredit(String string) {
        codeDebitCredit = string;
    }

    public void setCodeDebitCreditEcran(String newCodeDebitCreditEcran) {
        codeDebitCreditEcran = newCodeDebitCreditEcran;
    }

    /**
     * @param string
     */
    public void setCodeIsoME(String string) {
        codeIsoME = string;
    }

    /**
     * @param string
     */
    public void setCoursME(String string) {
        coursME = string;
    }

    /**
     * Sets the idCompte.
     * 
     * @param idCompte
     *            The idCompte to set
     */
    @Override
    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * Sets the idOrganeExecution.
     * 
     * @param idOrganeExecution
     *            The idOrganeExecution to set
     */
    public void setIdOrganeExecution(String idOrganeExecution) {
        this.idOrganeExecution = idOrganeExecution;
    }

    /**
     * Sets the montant.
     * 
     * @param montant
     *            The montant to set
     */
    @Override
    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantEcran(String newMontantEcran) {
        montantEcran = newMontantEcran;
        montant = newMontantEcran;
    }

    /**
     * @param string
     */
    public void setMontantME(String string) {
        montantME = string;
    }

    public void setMontantMEEcran(String newMontantMEEcran) {
        montantMEEcran = newMontantMEEcran;
        montantME = newMontantMEEcran;
    }

    /**
     * Sets the piece.
     * 
     * @param piece
     *            The piece to set
     */
    @Override
    public void setPiece(String piece) {
        this.piece = piece;
    }

    /**
     * Sets the taux.
     * 
     * @param taux
     *            The taux to set
     */
    @Override
    public void setTaux(String taux) {
        this.taux = taux;
    }
}
