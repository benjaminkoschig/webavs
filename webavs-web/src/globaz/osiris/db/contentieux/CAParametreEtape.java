package globaz.osiris.db.contentieux;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAHolidays;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APIEtape;
import globaz.osiris.api.APIEvenementContentieux;
import globaz.osiris.api.APIParametreEtape;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.external.IntDocumentContentieux;
import globaz.osiris.impl.print.itext.list.ICADocumentRappelCSC;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (17.12.2001 08:06:25)
 * 
 * @author: Administrator
 */
public class CAParametreEtape extends globaz.globall.db.BEntity implements java.io.Serializable, APIParametreEtape {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static int AK_IDETAPE_SEQ_CONT = 1;
    public final static int AK_NO_SEQUENCE_SEQ_CONT = 2;
    public final static java.lang.String DATE_ETAPE_PRECEDENTE = "218003";
    public final static java.lang.String DATE_SECTION = "218002";
    public final static java.lang.String ECHEANCE_SECTION = "218001";
    public final static java.lang.String JOUR = "217001";
    public final static java.lang.String JOUR_OUVRABLE = "217002";
    public final static java.lang.String MOIS = "217003";
    public final static java.lang.String SELON_SECTION = "218004";
    private globaz.osiris.db.contentieux.CAEtape _etape;
    private CAEvenementContentieux _evenementContentieux = null;
    private globaz.osiris.db.contentieux.CASequenceContentieux _sequence;
    private CAParametreEtapeCalculTaxeManager cacheParamEtapCalcTaxeMan = null;
    private java.lang.Class cDoc = null;
    private FWParametersSystemCode csDateReference = null;
    private FWParametersSystemCodeManager csDateReferences = null;
    // code systeme
    private FWParametersSystemCode csUnite = null;

    private FWParametersSystemCodeManager csUnites = null;
    private java.lang.String dateReference = new String();
    private java.lang.String delai = new String();
    private CAParametreEtape etapeParametreEtapePrecedente = null;
    private java.lang.String idEtape = new String();
    private java.lang.String idParametreEtape = new String();
    private java.lang.String idSection = new String();
    private java.lang.String idSequenceContentieux = new String();
    private java.lang.Boolean imputerTaxe = new Boolean(false);
    private java.util.Vector listeDocuments = null;

    private java.lang.String nomClasseImpl = new String();
    private java.lang.String sequence = new String();
    private java.lang.String soldelimitedeclenchement = new String();
    private java.lang.String unite = new String();

    /**
     * Commentaire relatif au constructeur CAParametreEtage
     */
    public CAParametreEtape() {
        super();
    }

    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws Exception {
        // incrémente le prochain numéro
        setIdParametreEtape(this._incCounter(transaction, idParametreEtape));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CAPECTP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idParametreEtape = statement.dbReadNumeric("IDPARAMETREETAPE");
        idSequenceContentieux = statement.dbReadNumeric("IDSEQCON");
        idEtape = statement.dbReadNumeric("IDETAPE");
        sequence = statement.dbReadNumeric("SEQUENCE");
        delai = statement.dbReadNumeric("DELAI");
        unite = statement.dbReadNumeric("UNITE");
        imputerTaxe = statement.dbReadBoolean("IMPUTERTAXE");
        nomClasseImpl = statement.dbReadString("NOMCLASSEIMPL");
        dateReference = statement.dbReadNumeric("DATEREFERENCE");
        soldelimitedeclenchement = statement.dbReadNumeric("SOLDELIMDEC", 2);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) {

        _propertyMandatory(statement.getTransaction(), getIdEtape(), getSession().getLabel("7210"));
        _propertyMandatory(statement.getTransaction(), getIdSequenceContentieux(), getSession().getLabel("7211"));
        _propertyMandatory(statement.getTransaction(), getUnite(), getSession().getLabel("7212"));
        _propertyMandatory(statement.getTransaction(), getDateReference(), getSession().getLabel("7213"));

        FWCurrency cDelai = new FWCurrency(getDelai());
        if (cDelai.isNegative()) {
            _addError(statement.getTransaction(), getSession().getLabel("7214"));
        }

        FWCurrency cSolde = new FWCurrency(getSoldelimitedeclenchement());
        if (cSolde.isNegative()) {
            _addError(statement.getTransaction(), getSession().getLabel("7215"));
        }

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (11.04.2002 10:33:12)
     * 
     * @param alternateKey
     *            int
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    @Override
    protected void _writeAlternateKey(globaz.globall.db.BStatement statement, int alternateKey)
            throws java.lang.Exception {

        // Clé alternée numéro 1 : idSection et idParametreEtape
        switch (alternateKey) {
            case AK_IDETAPE_SEQ_CONT:
                statement.writeKey("IDETAPE", this._dbWriteNumeric(statement.getTransaction(), getIdEtape(), ""));
                statement.writeKey("IDSEQCON",
                        this._dbWriteNumeric(statement.getTransaction(), getIdSequenceContentieux(), ""));
                break;
            case AK_NO_SEQUENCE_SEQ_CONT:
                statement.writeKey("IDSEQCON",
                        this._dbWriteNumeric(statement.getTransaction(), getIdSequenceContentieux(), ""));
                statement.writeKey("SEQUENCE", this._dbWriteNumeric(statement.getTransaction(), getSequence(), ""));

                break;
            default:
                throw new Exception("Alternate key " + alternateKey + " not implemented");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDPARAMETREETAPE",
                this._dbWriteNumeric(statement.getTransaction(), getIdParametreEtape(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDPARAMETREETAPE",
                this._dbWriteNumeric(statement.getTransaction(), getIdParametreEtape(), "idParametreEtape"));
        statement.writeField("IDSEQCON",
                this._dbWriteNumeric(statement.getTransaction(), getIdSequenceContentieux(), "idSeqCon"));
        statement.writeField("IDETAPE", this._dbWriteNumeric(statement.getTransaction(), getIdEtape(), "idEtape"));
        statement.writeField("SEQUENCE", this._dbWriteNumeric(statement.getTransaction(), getSequence(), "sequence"));
        statement.writeField("DELAI", this._dbWriteNumeric(statement.getTransaction(), getDelai(), "delai"));
        statement.writeField("UNITE", this._dbWriteNumeric(statement.getTransaction(), getUnite(), "unite"));
        statement.writeField("IMPUTERTAXE", this._dbWriteBoolean(statement.getTransaction(), getImputerTaxe(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "imputerTaxe"));
        statement.writeField("NOMCLASSEIMPL",
                this._dbWriteString(statement.getTransaction(), getNomClasseImpl(), "nomClasseImpl"));
        statement.writeField("DATEREFERENCE",
                this._dbWriteNumeric(statement.getTransaction(), getDateReference(), "dateReference"));
        statement.writeField("SOLDELIMDEC",
                this._dbWriteNumeric(statement.getTransaction(), getSoldelimitedeclenchement(), "soldeLimDec"));
    }

    public FWParametersSystemCode getCsDateReference() {

        if (csDateReference == null) {
            // liste pas encore chargee, on la charge
            csDateReference = new FWParametersSystemCode();
            csDateReference.getCode(getDateReference());
        }
        return csDateReference;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2001 11:19:02)
     * 
     * @return globaz.bambou.db.AJCodeSystemeManager
     */
    public FWParametersSystemCodeManager getCsDateReferences() {
        // liste déjà chargée ?
        if (csDateReferences == null) {
            // liste pas encore chargée, on la charge
            csDateReferences = new FWParametersSystemCodeManager();
            csDateReferences.setSession(getSession());
            csDateReferences.getListeCodes("OSIDATREF", getSession().getIdLangue());
        }
        return csDateReferences;
    }

    public FWParametersSystemCode getCsUnite() {

        if (csUnite == null) {
            // liste pas encore chargee, on la charge
            csUnite = new FWParametersSystemCode();
            csUnite.getCode(getUnite());
        }
        return csUnite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.12.2001 11:19:02)
     * 
     * @return globaz.bambou.db.AJCodeSystemeManager
     */
    public FWParametersSystemCodeManager getCsUnites() {
        // liste déjà chargée ?
        if (csUnites == null) {
            // liste pas encore chargée, on la charge
            csUnites = new FWParametersSystemCodeManager();
            csUnites.setSession(getSession());
            csUnites.getListeCodes("OSIRUNITE", getSession().getIdLangue());
        }
        return csUnites;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.06.2002 15:25:59)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDateDeclenchement(APISection section) {
        if (getEvenementContentieux(section) != null) {
            if (getEvenementContentieux(section).getEstDeclenche().booleanValue()) {
                return getEvenementContentieux(section).getDateDeclenchement();
            }

            if (getEvenementContentieux(section).getEstModifie().booleanValue()) {
                if (!getEvenementContentieux(section).getEstIgnoree().booleanValue()) {
                    return getEvenementContentieux(section).getDateDeclenchement();
                } else {
                    return "";
                }
            }
        }

        // Si le nombre de jours est zéro
        if (JadeStringUtil.isIntegerEmpty(getDelai())) {
            return "";
        }

        // Calcul de la date

        JADate dateCalcul = new JADate();

        if (getDateReference().equalsIgnoreCase(CAParametreEtape.ECHEANCE_SECTION)) {
            try {
                dateCalcul.fromString(section.getDateEcheance());
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return "";
            }
        } else if (getDateReference().equalsIgnoreCase(CAParametreEtape.DATE_SECTION)) {
            try {
                dateCalcul.fromString(section.getDateSection());
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return "";
            }
        } else if (getDateReference().equalsIgnoreCase(CAParametreEtape.DATE_ETAPE_PRECEDENTE)) {
            try {
                String dateExec;
                // Vérifie si la date de l'étape précédente est vide
                if (getEtapeParametreEtapePrecedente().getEvenementContentieux(section) != null) {
                    dateExec = getEtapeParametreEtapePrecedente().getEvenementContentieux(section).getDateExecution();
                    if (!JadeStringUtil.isBlank(dateExec)) {
                        dateCalcul.fromString(dateExec);
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
            }
        } else if (getDateReference().equalsIgnoreCase(CAParametreEtape.SELON_SECTION)) {
            try {
                dateCalcul.fromString(section.getDateReferenceContentieux(this));
            } catch (Exception e) {
                _addError(null, e.getMessage());
                return "";
            }
        }

        // Calcul de la date selon un jour
        if (getUnite().equalsIgnoreCase(CAParametreEtape.JOUR)) {
            JACalendarGregorian dateGreg = new JACalendarGregorian();
            dateCalcul = dateGreg.addDays(dateCalcul, java.lang.Integer.parseInt(getDelai()));
            return JACalendar.format(dateCalcul);
        }

        // Calcul de la date selon les jours ouvrables
        if (getUnite().equalsIgnoreCase(CAParametreEtape.JOUR_OUVRABLE)) {
            java.net.URL url = this.getClass().getResource("/holidays.xml");
            if (url == null) {
                _addError(null, getSession().getLabel("7216"));
                return "";
            }
            java.io.File f = new java.io.File(url.getFile());
            JAHolidays h = new JAHolidays(f.getPath());
            JACalendarGregorian dateGreg = new JACalendarGregorian(h);
            boolean ferie = false;
            boolean weekend = false;
            dateCalcul = dateGreg.addDays(dateCalcul, java.lang.Integer.parseInt(getDelai()));

            // Test si jour tombe sur un week end
            do {
                weekend = dateGreg.isWeekend(dateCalcul);
                if (weekend) {
                    dateCalcul = dateGreg.addDays(dateCalcul, 1);
                }
            } while (weekend);

            // Test si jour tombe sur un jour férié
            do {
                ferie = dateGreg.isHoliday(dateCalcul);
                if (ferie) {
                    dateCalcul = dateGreg.addDays(dateCalcul, 1);
                }
            } while (ferie);

            return JACalendar.format(dateCalcul);
        }

        // Calcul de la date selon le mois
        if (getUnite().equalsIgnoreCase(CAParametreEtape.MOIS)) {
            JACalendarGregorian dateGreg = new JACalendarGregorian();
            dateCalcul = dateGreg.addMonths(dateCalcul, java.lang.Integer.parseInt(getDelai()));
            int jours = dateGreg.daysInMonth(dateCalcul.getMonth(), dateCalcul.getYear());
            if (jours < dateCalcul.getDay()) {
                dateCalcul.setDay(jours);
            }
            return JACalendar.format(dateCalcul);
        }

        return "";
    }

    @Override
    public java.lang.String getDateReference() {
        return dateReference;
    }

    @Override
    public java.lang.String getDelai() {
        return delai;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    @Override
    public APIEtape getEtape() {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdEtape())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_etape == null) {
            // Instancier un nouveau LOG
            _etape = new CAEtape();
            _etape.setSession(getSession());

            // Récupérer le log en question
            _etape.setIdEtape(getIdEtape());
            try {
                _etape.retrieve();
                if (_etape.hasErrors() || _etape.isNew()) {
                    _etape = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _etape = null;
            }
        }

        return _etape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (04.06.2002 15:00:00)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreEtape
     */
    @Override
    public APIParametreEtape getEtapeParametreEtapePrecedente() {

        if (etapeParametreEtapePrecedente == null) {

            // Chargement du manager
            CAParametreEtapeManager man = new CAParametreEtapeManager();
            man.setSession(getSession());
            man.setForIdSequenceContentieux(getIdSequenceContentieux());
            man.setBeforeNoSequence(getSequence());
            man.setOrderBy(CAParametreEtapeManager.ORDER_SEQUENCE_DESC);
            try {
                man.find();

                // Mémoriser l'étape trouvée
                if (man.size() > 0) {
                    etapeParametreEtapePrecedente = (CAParametreEtape) man.getEntity(0);
                } else {
                    etapeParametreEtapePrecedente = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                etapeParametreEtapePrecedente = null;
            }
        }

        return etapeParametreEtapePrecedente;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.contentieux.CAEvenementContentieux
     */
    @Override
    public APIEvenementContentieux getEvenementContentieux(APISection section) {

        // Si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdParametreEtape())) {
            return null;
        }

        // Si EvenementContentieux pas déjà chargé
        if ((_evenementContentieux == null) || !_evenementContentieux.getIdSection().equals(section.getIdSection())) {
            // Instancier un nouveau EvenementContentieux
            _evenementContentieux = new CAEvenementContentieux();
            _evenementContentieux.setSession(getSession());

            // Récupérer l'EvenementContentieux en question
            _evenementContentieux.setAlternateKey(CAEvenementContentieux.AK_IDSECPARAM);
            _evenementContentieux.setIdParametreEtape(getIdParametreEtape());
            _evenementContentieux.setIdSection(section.getIdSection());
            try {
                _evenementContentieux.retrieve();
                _evenementContentieux.setAlternateKey(0);
                if (_evenementContentieux.hasErrors() || _evenementContentieux.isNew()) {
                    _evenementContentieux = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _evenementContentieux = null;
            }
        }

        return _evenementContentieux;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.07.2002 08:52:58)
     * 
     * @return globaz.osiris.db.contentieux.CAEvenementContentieux
     */
    @Override
    public APIEvenementContentieux getEvenementContentieuxPrecedent(APISection section) {

        // Initialiser
        CAEvenementContentieux ecp = null;
        APIParametreEtape pe = getEtapeParametreEtapePrecedente();

        // Récupérer l'étape précédente si elle existe
        if (pe != null) {
            ecp = new CAEvenementContentieux();
            ecp.setSession(getSession());
            ecp.setIdParametreEtape(pe.getIdParametreEtape());
            ecp.setIdSection(section.getIdSection());
            ecp.setAlternateKey(CAEvenementContentieux.AK_IDSECPARAM);
            try {
                ecp.retrieve();
                ecp.setAlternateKey(0);

                // Si erreur, il n'y a pas d'étape précédente
                if (ecp.hasErrors() || ecp.isNew()) {
                    ecp = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                ecp = null;
            }
        }

        return ecp;

    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.07.2002 08:52:58)
     * 
     * @return globaz.osiris.db.contentieux.CAEvenementContentieux
     */
    public CAEvenementContentieux getEvenementContentieuxPrecedent(CASection section) {

        // Initialiser
        CAEvenementContentieux ecp = null;
        APIParametreEtape pe = getEtapeParametreEtapePrecedente();

        // Récupérer l'étape précédente si elle existe
        if (pe != null) {
            ecp = new CAEvenementContentieux();
            ecp.setSession(getSession());
            ecp.setIdParametreEtape(pe.getIdParametreEtape());
            ecp.setIdSection(section.getIdSection());
            ecp.setAlternateKey(CAEvenementContentieux.AK_IDSECPARAM);
            try {
                ecp.retrieve();
                ecp.setAlternateKey(0);

                // Si erreur, il n'y a pas d'étape précédente
                if (ecp.hasErrors() || ecp.isNew()) {
                    ecp = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                ecp = null;
            }
        }

        return ecp;

    }

    @Override
    public java.lang.String getIdEtape() {
        return idEtape;
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdParametreEtape() {
        return idParametreEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.07.2002 10:14:13)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdSection() {
        return idSection;
    }

    @Override
    public java.lang.String getIdSequenceContentieux() {
        return idSequenceContentieux;
    }

    @Override
    public java.lang.Boolean getImputerTaxe() {
        return imputerTaxe;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 10:22:47)
     * 
     * @return globaz.osiris.interfaceext.contentieux.ICADocumentRappelCSC
     */
    @Override
    public IntDocumentContentieux getInstanceDocumentContentieux() throws Exception {
        if (cDoc == null) {
            cDoc = Class.forName(getNomClasseImpl());
        }
        return (IntDocumentContentieux) cDoc.newInstance();
    }

    public ICADocumentRappelCSC getInstanceDocumentContentieuxCSC() throws Exception {
        if (cDoc == null) {
            cDoc = Class.forName(getNomClasseImpl());
        }
        return (ICADocumentRappelCSC) cDoc.newInstance();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.06.2002 11:13:25)
     * 
     * @return java.util.Vector
     */
    @Override
    public java.util.Vector getListeDocuments() {
        if (listeDocuments == null) {
            listeDocuments = new Vector();
        }

        return listeDocuments;
    }

    @Override
    public java.lang.String getNomClasseImpl() {
        return nomClasseImpl;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (17.06.2002 14:12:08)
     * 
     * @return globaz.osiris.db.contentieux.CAParametreEtapeCalculTaxe
     */
    public CAParametreEtapeCalculTaxeManager getParamEtapCalculTaxes() {
        // liste déjà chargée ?
        if (cacheParamEtapCalcTaxeMan == null) {
            // liste pas encore chargée, on la charge
            cacheParamEtapCalcTaxeMan = new CAParametreEtapeCalculTaxeManager();
            cacheParamEtapCalcTaxeMan.setSession(getSession());
            cacheParamEtapCalcTaxeMan.setForIdParametreEtape(getIdParametreEtape());
            try {
                cacheParamEtapCalcTaxeMan.find();
                if (cacheParamEtapCalcTaxeMan.hasErrors() || cacheParamEtapCalcTaxeMan.isEmpty()) {
                    cacheParamEtapCalcTaxeMan = null;
                }
            } catch (Exception e) {
                cacheParamEtapCalcTaxeMan = null;
            }
        }
        return cacheParamEtapCalcTaxeMan;
    }

    @Override
    public java.lang.String getSequence() {
        return sequence;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public CASequenceContentieux getSequenceContentieux() {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getIdSequenceContentieux())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_sequence == null) {
            // Instancier un nouveau LOG
            _sequence = new CASequenceContentieux();
            _sequence.setSession(getSession());

            // Récupérer le log en question
            _sequence.setIdSequenceContentieux(getIdSequenceContentieux());
            try {
                _sequence.retrieve();
                if (_sequence.hasErrors() || _sequence.isNew()) {
                    _sequence = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _sequence = null;
            }
        }

        return _sequence;
    }

    @Override
    public java.lang.String getSoldelimitedeclenchement() {
        return soldelimitedeclenchement;
    }

    @Override
    public java.lang.String getUnite() {
        return unite;
    }

    @Override
    public void setDateReference(java.lang.String newDateReference) {
        dateReference = newDateReference;
    }

    @Override
    public void setDelai(java.lang.String newDelai) {
        delai = newDelai;
    }

    @Override
    public void setIdEtape(java.lang.String newIdEtape) {
        idEtape = newIdEtape;
    }

    /**
     * Setter
     */
    @Override
    public void setIdParametreEtape(java.lang.String newIdParametreEtape) {
        idParametreEtape = newIdParametreEtape;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (05.07.2002 10:14:13)
     * 
     * @param newIdSection
     *            java.lang.String
     */
    @Override
    public void setIdSection(java.lang.String newIdSection) {
        idSection = newIdSection;
    }

    @Override
    public void setIdSequenceContentieux(java.lang.String newIdSequenceContentieux) {
        idSequenceContentieux = newIdSequenceContentieux;
    }

    @Override
    public void setImputerTaxe(java.lang.Boolean newImputerTaxe) {
        imputerTaxe = newImputerTaxe;
    }

    @Override
    public void setNomClasseImpl(java.lang.String newNomClasseImpl) {
        nomClasseImpl = newNomClasseImpl;
    }

    @Override
    public void setSequence(java.lang.String newSequence) {
        sequence = newSequence;
    }

    @Override
    public void setSoldelimitedeclenchement(java.lang.String newSoldelimitedeclenchement) {
        soldelimitedeclenchement = newSoldelimitedeclenchement;
    }

    @Override
    public void setUnite(java.lang.String newUnite) {
        unite = newUnite;
    }
}
