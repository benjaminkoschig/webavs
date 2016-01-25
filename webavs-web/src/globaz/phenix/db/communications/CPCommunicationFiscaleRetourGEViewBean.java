package globaz.phenix.db.communications;

import globaz.commons.nss.NSUtil;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.communications.CPProcessValiderPlausibilite;

public class CPCommunicationFiscaleRetourGEViewBean extends CPCommunicationFiscaleRetourViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String description = "descriptionGE";

    private Boolean geBourses = new Boolean(false);

    /*
     * Traitement avant ajout
     */

    private java.lang.String geDateTransfertMAD = "";
    private Boolean geDivers = new Boolean(false);
    private java.lang.String geExplicationsDivers = "";
    private java.lang.String geGenreAffilie = "";
    private Boolean geImpositionSelonDepense = new Boolean(false);
    private Boolean geImpotSource = new Boolean(false);
    private Boolean geIndemniteJournaliere = new Boolean(false);
    private java.lang.String geNNSS = "";
    private java.lang.String geNom = "";
    private java.lang.String geNomAFC = "";
    private java.lang.String geNomConjoint = "";
    private Boolean geNonAssujettiIBO = new Boolean(false);
    private Boolean geNonAssujettiIFD = new Boolean(false);
    private java.lang.String geNumAffilie = "";
    private java.lang.String geNumAvs = "";
    private java.lang.String geNumAvsConjoint = "";
    private java.lang.String geNumCaisse = "";
    private java.lang.String geNumCommunication = "";
    private java.lang.String geNumContribuable = "";
    private java.lang.String geNumDemande = "";
    private java.lang.String geObservations = "";
    private Boolean gePasActiviteDeclaree = new Boolean(false);
    private Boolean gePension = new Boolean(false);
    private Boolean gePensionAlimentaire = new Boolean(false);
    private Boolean gePersonneNonIdentifiee = new Boolean(false);
    private java.lang.String gePrenom = "";
    private java.lang.String gePrenomAFC = "";
    private java.lang.String gePrenomConjoint = "";
    private Boolean geRenteInvalidite = new Boolean(false);
    private Boolean geRenteViagere = new Boolean(false);
    private Boolean geRenteVieillesse = new Boolean(false);
    private Boolean geRetraite = new Boolean(false);
    private Boolean geTaxationOffice = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */
    public CPCommunicationFiscaleRetourGEViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // setValeurChampRecherche(getValeurRechercheBD());
        super._afterRetrieve(transaction);
        setNumContribuableRecu(getGeNumContribuable());
        setNumAffilieRecu(getGeNumAffilie());
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        if (!isForBackup()) {
            setIdRetour(this._incCounter(transaction, "0", "CPCRETP", "0", "0"));
        }
        // setStatus(CS_RECEPTIONNE);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {

        String table1 = "CPCRGEP";
        String table2 = "CPCRETP";

        if (isForBackup()) {
            table1 = "CPCRGEB";
            table2 = "CPCRETB";
        }

        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".IKIRET=" + _getCollection() + table2 + ".IKIRET)";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        if (!isForBackup()) {
            return "CPCRGEP";
        } else {
            return "CPCRGEB";
        }

    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        geNumCaisse = statement.dbReadString("IKCAGE");
        geNumDemande = statement.dbReadNumeric("IKNDEM");
        geGenreAffilie = statement.dbReadNumeric("IKGAGE");
        geNumAffilie = statement.dbReadString("IKAFGE");
        geNumAvs = statement.dbReadString("IKNAGE");
        geNumContribuable = statement.dbReadString("IKCOGE");
        gePersonneNonIdentifiee = statement.dbReadBoolean("IKBIJO");
        geNom = statement.dbReadString("IKLNOM");
        geNomAFC = statement.dbReadString("IKLNAF");
        gePrenom = statement.dbReadString("IKLPRE");
        gePrenomAFC = statement.dbReadString("IKLPAF");
        geNumAvsConjoint = statement.dbReadString("IKNCJT");
        geNomConjoint = statement.dbReadString("IKLNCJ");
        gePrenomConjoint = statement.dbReadString("IKLPCJ");
        geNumCommunication = statement.dbReadNumeric("IKNCOM");
        geImpotSource = statement.dbReadBoolean("IKBSRC");
        geNonAssujettiIBO = statement.dbReadBoolean("IKBIBO");
        gePasActiviteDeclaree = statement.dbReadBoolean("IKBPAD");
        geImpositionSelonDepense = statement.dbReadBoolean("IKBIDE");
        geTaxationOffice = statement.dbReadBoolean("IKBTOF");
        geObservations = statement.dbReadString("IKLOBS");
        geDateTransfertMAD = statement.dbReadDateAMJ("IKDMAD");
        gePension = statement.dbReadBoolean("IKBPEN");
        geRetraite = statement.dbReadBoolean("IKBRET");
        geRenteVieillesse = statement.dbReadBoolean("IKBRVI");
        geRenteInvalidite = statement.dbReadBoolean("IKBRIN");
        gePensionAlimentaire = statement.dbReadBoolean("IKBPAL");
        geRenteViagere = statement.dbReadBoolean("IKBRVG");
        geIndemniteJournaliere = statement.dbReadBoolean("IKBIJO");
        geBourses = statement.dbReadBoolean("IKBBOU");
        geDivers = statement.dbReadBoolean("IKBDIV");
        geNonAssujettiIFD = statement.dbReadBoolean("IKBNAS");
        geExplicationsDivers = statement.dbReadString("IKLEXP");
        geNNSS = statement.dbReadNumeric("IKNNSS");
        super._readProperties(statement);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Les reception sont toujours chargée, elle
     * seront validées par les plausibilités
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        setValeurChampRecherche(this.getValeurRechercheBD());
        setNumContribuableRecu(getGeNumContribuable());
        CPCommentaireCommunicationManager mng = new CPCommentaireCommunicationManager();
        mng.setSession(getSession());
        mng.setForIdCommunicationRetour(getIdRetour());
        mng.find();
        for (int i = 0; i < mng.size(); i++) {
            ((CPCommentaireCommunication) mng.getEntity(i)).delete();
        }
        // Présence commentaire
        if (!JadeStringUtil.isEmpty(getGeExplicationsDivers()) || !JadeStringUtil.isEmpty(getGeObservations())) {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_PRESENCE_COMMENTAIRE);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.add(statement.getTransaction());
        } else {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_PRESENCE_COMMENTAIRE);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.setAlternateKey(1);
            comment.retrieve(statement.getTransaction());
            if (!comment.isNew()) {
                comment.delete(statement.getTransaction());
            }
        }
        // Imposition à la source
        if (Boolean.TRUE.equals(getGeImpotSource())) {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_IMPOSITION_SOURCE);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.add(statement.getTransaction());
        } else {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_IMPOSITION_SOURCE);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.setAlternateKey(1);
            comment.retrieve(statement.getTransaction());
            if (!comment.isNew()) {
                comment.delete(statement.getTransaction());
            }
        }
        // Non assujetti IBO
        if (Boolean.TRUE.equals(getGeNonAssujettiIBO())) {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_NON_ASSUJETTI_IBO);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.add(statement.getTransaction());
        } else {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_NON_ASSUJETTI_IBO);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.setAlternateKey(1);
            comment.retrieve(statement.getTransaction());
            if (!comment.isNew()) {
                comment.delete(statement.getTransaction());
            }
        }
        // Non assujetti IFD
        if (Boolean.TRUE.equals(getGeNonAssujettiIFD())) {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_NON_ASSUJETTI_IFD);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.add(statement.getTransaction());
        } else {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_NON_ASSUJETTI_IFD);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.setAlternateKey(1);
            comment.retrieve(statement.getTransaction());
            if (!comment.isNew()) {
                comment.delete(statement.getTransaction());
            }
        }
        if (CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE.equalsIgnoreCase(getStatus())
                || CPCommunicationFiscaleRetourViewBean.CS_AVERTISSEMENT.equalsIgnoreCase(getStatus())
                || CPCommunicationFiscaleRetourViewBean.CS_ERREUR.equalsIgnoreCase(getStatus())) {
            // Plausibilité
            CPProcessValiderPlausibilite process = new CPProcessValiderPlausibilite();
            process.setSession(getSession());
            process.setTransaction(statement.getTransaction());
            process.setCommunicationRetour(this);
            process.setSendCompletionMail(false);
            process.setDeclenchement(CPReglePlausibilite.CS_AVANT_GENERATION);
            process.executeProcess();
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(_getBaseTable() + "IKIRET",
                this._dbWriteNumeric(statement.getTransaction(), getIdRetour(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        }
        statement.writeField("IKIRET", this._dbWriteNumeric(statement.getTransaction(), getIdRetour(), "idRetour"));
        statement
                .writeField("IKCAGE", this._dbWriteString(statement.getTransaction(), getGeNumCaisse(), "geNumCaisse"));
        statement.writeField("IKNDEM",
                this._dbWriteNumeric(statement.getTransaction(), getGeNumDemande(), "geNumDemande"));
        statement.writeField("IKGAGE",
                this._dbWriteNumeric(statement.getTransaction(), getGeGenreAffilie(), "geGenreAffilie"));
        statement.writeField("IKAFGE",
                this._dbWriteString(statement.getTransaction(), getGeNumAffilie(), "geNumAffilie"));
        statement.writeField("IKNAGE", this._dbWriteString(statement.getTransaction(), getGeNumAvs(), "geNumAvs"));
        statement.writeField("IKCOGE",
                this._dbWriteString(statement.getTransaction(), getGeNumContribuable(), "geNumContribuable"));
        statement.writeField("IKBPNI", this._dbWriteBoolean(statement.getTransaction(), getGePersonneNonIdentifiee(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "gePersonneNonIdentifiee"));
        statement.writeField("IKLNOM", this._dbWriteString(statement.getTransaction(), getGeNom(), "geNom"));
        statement.writeField("IKLNAF", this._dbWriteString(statement.getTransaction(), getGeNomAFC(), "geNomAFC"));
        statement.writeField("IKLPRE", this._dbWriteString(statement.getTransaction(), getGePrenom(), "gePrenom"));
        statement
                .writeField("IKLPAF", this._dbWriteString(statement.getTransaction(), getGePrenomAFC(), "gePrenomAFC"));
        statement.writeField("IKNCJT",
                this._dbWriteString(statement.getTransaction(), getGeNumAvsConjoint(), "geNumAvsConjoint"));
        statement.writeField("IKLNCJ",
                this._dbWriteString(statement.getTransaction(), getGeNomConjoint(), "geNomConjoint"));
        statement.writeField("IKLPCJ",
                this._dbWriteString(statement.getTransaction(), getGePrenomConjoint(), "gePrenomConjoint"));
        statement.writeField("IKNCOM",
                this._dbWriteNumeric(statement.getTransaction(), getGeNumCommunication(), "geNumCommunication"));
        statement.writeField("IKBSRC", this._dbWriteBoolean(statement.getTransaction(), getGeImpotSource(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geImpotSource"));
        statement.writeField("IKBIBO", this._dbWriteBoolean(statement.getTransaction(), getGeNonAssujettiIBO(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geNonAssujettiIBO"));
        statement.writeField("IKBPAD", this._dbWriteBoolean(statement.getTransaction(), getGePasActiviteDeclaree(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "gePasActiviteDeclaree"));
        statement.writeField("IKBIDE", this._dbWriteBoolean(statement.getTransaction(), getGeImpositionSelonDepense(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geImpositionSelonDepense"));
        statement.writeField("IKBTOF", this._dbWriteBoolean(statement.getTransaction(), getGeTaxationOffice(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geTaxationOffice"));
        statement.writeField("IKLOBS",
                this._dbWriteString(statement.getTransaction(), getGeObservations(), "geObservations"));
        statement.writeField("IKDMAD",
                this._dbWriteDateAMJ(statement.getTransaction(), getGeDateTransfertMAD(), "geDateTransfertMAD"));
        statement.writeField("IKBPEN", this._dbWriteBoolean(statement.getTransaction(), getGePension(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "gePension"));
        statement.writeField("IKBRET", this._dbWriteBoolean(statement.getTransaction(), getGeRetraite(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geRetraite"));
        statement.writeField("IKBRVI", this._dbWriteBoolean(statement.getTransaction(), getGeRenteVieillesse(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geRenteVieillesse"));
        statement.writeField("IKBRIN", this._dbWriteBoolean(statement.getTransaction(), getGeRenteInvalidite(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geRenteInvalidite"));
        statement.writeField("IKBPAL", this._dbWriteBoolean(statement.getTransaction(), getGePensionAlimentaire(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "gePensionAlimentaire"));
        statement.writeField("IKBRVG", this._dbWriteBoolean(statement.getTransaction(), getGeRenteViagere(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geRenteViagere"));
        statement.writeField("IKBIJO", this._dbWriteBoolean(statement.getTransaction(), getGeIndemniteJournaliere(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geIndemniteJournaliere"));
        statement.writeField("IKBBOU", this._dbWriteBoolean(statement.getTransaction(), getGeBourses(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geBourses"));
        statement.writeField("IKBDIV", this._dbWriteBoolean(statement.getTransaction(), getGeDivers(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geDivers"));
        statement.writeField("IKBNAS", this._dbWriteBoolean(statement.getTransaction(), getGeNonAssujettiIFD(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "geNonAssujettiIFD"));
        statement.writeField("IKLEXP",
                this._dbWriteString(statement.getTransaction(), getGeExplicationsDivers(), "geExplicationsDivers"));
        statement.writeField("IKNNSS", this._dbWriteNumeric(statement.getTransaction(), getGeNNSS(), "geNNSS"));
    }

    public String getDescription() {
        if (getGeNumContribuable().length() > 0) {
            description += getSession().getLabel("NUM_CONTRIBUABLE") + " : " + getGeNumContribuable() + "\n";
        }
        if (getGeNumAffilie().length() > 0) {
            description += getSession().getLabel("NUM_AFFILIE") + " : " + getGeNumAffilie() + "\n";
        }
        if (getGeNom().length() > 0) {
            description += getSession().getLabel("NOM") + " : " + getGeNom() + "\n";
        }
        if (getGePrenom().length() > 0) {
            description += getSession().getLabel("PRENOM") + " : " + getGePrenom() + "\n";
        }
        if (getGeNumAvsConjoint().length() > 0) {
            description += getSession().getLabel("NUM_AVS_EPOUX") + " : " + getGeNumAvsConjoint() + "\n";
        }
        if ((getGeNomConjoint().length() > 0) || (getGePrenomConjoint().length() > 0)) {
            description += getSession().getLabel("NOM_EPOUX") + " : " + getGeNomConjoint();
            if (getGePrenomConjoint().length() > 0) {
                description += " ";
            }
            description += getSession().getLabel("PRENOM_EPOUX") + " : " + getGePrenomConjoint() + "\n";
        }
        return description;
    }

    /**
     * @return
     */
    public Boolean getGeBourses() {
        return geBourses;
    }

    public java.lang.String getGeDateTransfertMAD() {
        return geDateTransfertMAD;
    }

    /**
     * @return
     */
    public Boolean getGeDivers() {
        return geDivers;
    }

    /**
     * @return
     */
    public java.lang.String getGeExplicationsDivers() {
        return geExplicationsDivers;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getGeGenreAffilie() {
        return geGenreAffilie;
    }

    /**
     * @return
     */
    public Boolean getGeImpositionSelonDepense() {
        return geImpositionSelonDepense;
    }

    public Boolean getGeImpotSource() {
        return geImpotSource;
    }

    /**
     * @return
     */
    public Boolean getGeIndemniteJournaliere() {
        return geIndemniteJournaliere;
    }

    public java.lang.String getGeNNSS() {
        return geNNSS;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getGeNom() {
        return geNom;
    }

    public java.lang.String getGeNomAFC() {
        return geNomAFC;
    }

    /**
     * @return
     */
    public java.lang.String getGeNomConjoint() {
        return geNomConjoint;
    }

    /**
     * @return
     */
    public Boolean getGeNonAssujettiIBO() {
        return geNonAssujettiIBO;
    }

    /**
     * @return
     */
    public Boolean getGeNonAssujettiIFD() {
        return geNonAssujettiIFD;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getGeNumAffilie() {
        return geNumAffilie;
    }

    public java.lang.String getGeNumAvs() {
        /*
         * if(JadeStringUtil.isIntegerEmpty(geNumAvs)) return ""; else { try{ return
         * JAStringFormatter.unFormatAVS(geNumAvs); } catch (Exception e) {
         */
        return geNumAvs;
        /*
         * } }
         */
    }

    /**
     * @return
     */
    public java.lang.String getGeNumAvsConjoint() {
        return geNumAvsConjoint;
    }

    /**
     * @return
     */
    public java.lang.String getGeNumCaisse() {
        return geNumCaisse;
    }

    public java.lang.String getGeNumCommunication() {
        return geNumCommunication;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getGeNumContribuable() {
        return geNumContribuable;
    }

    public java.lang.String getGeNumDemande() {
        return geNumDemande;
    }

    public java.lang.String getGeObservations() {
        return geObservations;
    }

    /**
     * @return
     */
    public Boolean getGePasActiviteDeclaree() {
        return gePasActiviteDeclaree;
    }

    /**
     * @return
     */
    public Boolean getGePension() {
        return gePension;
    }

    /**
     * @return
     */
    public Boolean getGePensionAlimentaire() {
        return gePensionAlimentaire;
    }

    public Boolean getGePersonneNonIdentifiee() {
        return gePersonneNonIdentifiee;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getGePrenom() {
        return gePrenom;
    }

    public java.lang.String getGePrenomAFC() {
        return gePrenomAFC;
    }

    /**
     * @return
     */
    public java.lang.String getGePrenomConjoint() {
        return gePrenomConjoint;
    }

    /**
     * @return
     */
    public Boolean getGeRenteInvalidite() {
        return geRenteInvalidite;
    }

    /**
     * @return
     */
    public Boolean getGeRenteViagere() {
        return geRenteViagere;
    }

    /**
     * @return
     */
    public Boolean getGeRenteVieillesse() {
        return geRenteVieillesse;
    }

    /**
     * @return
     */
    public Boolean getGeRetraite() {
        return geRetraite;
    }

    /**
     * @return
     */
    public Boolean getGeTaxationOffice() {
        return geTaxationOffice;
    }

    @Override
    public String getNumAvs() {
        return getGeNumAvs();
    }

    public String getNumeroAvs() {
        return getGeNumAvs();
    }

    private String getValeurRechercheBD() {
        try {
            if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAffilie")) {
                return getGeNumAffilie();
            } else if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAvs")) {
                return NSUtil.formatAVSUnknown(getGeNumAvs());
            } else {
                return getGeNumContribuable();
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getValeurRechercheBD(String zoneRecherche) {
        try {
            if (zoneRecherche.equalsIgnoreCase("numAffilie")) {
                return getGeNumAffilie();
            } else if (zoneRecherche.equalsIgnoreCase("numAvs")) {
                return NSUtil.formatAVSUnknown(getGeNumAvs());
            } else {
                return getGeNumContribuable();
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean isForBackup() {
        return super.isForBackup();
    }

    /**
     * @param boolean1
     */
    public void setGeBourses(Boolean boolean1) {
        geBourses = boolean1;
    }

    public void setGeDateTransfertMAD(java.lang.String string) {
        geDateTransfertMAD = string;
    }

    /**
     * @param boolean1
     */
    public void setGeDivers(Boolean boolean1) {
        geDivers = boolean1;
    }

    /**
     * @param string
     */
    public void setGeExplicationsDivers(java.lang.String string) {
        geExplicationsDivers = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @param newGenreAffilie
     *            java.lang.String
     */
    public void setGeGenreAffilie(java.lang.String newGenreAffilie) {
        geGenreAffilie = newGenreAffilie;
    }

    /**
     * @param boolean1
     */
    public void setGeImpositionSelonDepense(Boolean boolean1) {
        geImpositionSelonDepense = boolean1;
    }

    public void setGeImpotSource(Boolean boolean1) {
        geImpotSource = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGeIndemniteJournaliere(Boolean boolean1) {
        geIndemniteJournaliere = boolean1;
    }

    public void setGeNNSS(java.lang.String geNNSS) {
        this.geNNSS = geNNSS;
    }

    /**
     * @param string
     */
    @Override
    public void setGeNom(java.lang.String string) {
        geNom = string;
    }

    public void setGeNomAFC(java.lang.String geNomAFC) {
        this.geNomAFC = geNomAFC;
    }

    /**
     * @param string
     */
    public void setGeNomConjoint(java.lang.String string) {
        geNomConjoint = string;
    }

    /**
     * @param boolean1
     */
    public void setGeNonAssujettiIBO(Boolean boolean1) {
        geNonAssujettiIBO = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGeNonAssujettiIFD(Boolean boolean1) {
        geNonAssujettiIFD = boolean1;
    }

    /**
     * @param string
     */
    @Override
    public void setGeNumAffilie(java.lang.String string) {
        geNumAffilie = string;
    }

    public void setGeNumAvs(java.lang.String string) {
        geNumAvs = string;
    }

    /**
     * @param string
     */
    public void setGeNumAvsConjoint(java.lang.String string) {
        geNumAvsConjoint = string;
    }

    /**
     * @param string
     */
    public void setGeNumCaisse(java.lang.String string) {
        geNumCaisse = string;
    }

    public void setGeNumCommunication(java.lang.String string) {
        geNumCommunication = string;
    }

    /**
     * @param string
     */
    @Override
    public void setGeNumContribuable(java.lang.String string) {
        geNumContribuable = string;
    }

    public void setGeNumDemande(java.lang.String string) {
        geNumDemande = string;
    }

    public void setGeObservations(java.lang.String string) {
        geObservations = string;
    }

    /**
     * @param boolean1
     */
    public void setGePasActiviteDeclaree(Boolean boolean1) {
        gePasActiviteDeclaree = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGePension(Boolean boolean1) {
        gePension = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGePensionAlimentaire(Boolean boolean1) {
        gePensionAlimentaire = boolean1;
    }

    public void setGePersonneNonIdentifiee(Boolean gePersonneNonIdentifiee) {
        this.gePersonneNonIdentifiee = gePersonneNonIdentifiee;
    }

    /**
     * @param string
     */
    @Override
    public void setGePrenom(java.lang.String string) {
        gePrenom = string;
    }

    public void setGePrenomAFC(java.lang.String gePrenomAFC) {
        this.gePrenomAFC = gePrenomAFC;
    }

    /**
     * @param string
     */
    public void setGePrenomConjoint(java.lang.String string) {
        gePrenomConjoint = string;
    }

    /**
     * @param boolean1
     */
    public void setGeRenteInvalidite(Boolean boolean1) {
        geRenteInvalidite = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGeRenteViagere(Boolean boolean1) {
        geRenteViagere = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGeRenteVieillesse(Boolean boolean1) {
        geRenteVieillesse = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGeRetraite(Boolean boolean1) {
        geRetraite = boolean1;
    }

    /**
     * @param boolean1
     */
    public void setGeTaxationOffice(Boolean boolean1) {
        geTaxationOffice = boolean1;
    }

}
