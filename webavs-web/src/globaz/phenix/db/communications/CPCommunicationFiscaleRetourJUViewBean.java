package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.communications.CPProcessValiderPlausibilite;

public class CPCommunicationFiscaleRetourJUViewBean extends CPCommunicationFiscaleRetourViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String description = "";

    private java.lang.String juCodeApplication = "";

    private java.lang.String juDateNaissance = "";

    /*
     * Traitement avant ajout
     */

    private Boolean juEpoux = new Boolean(false);
    private java.lang.String juFiller = "";
    private java.lang.String juGenreAffilie = "";
    private java.lang.String juGenreTaxation = "";
    private java.lang.String juLot = "";
    private java.lang.String juNbrJour1 = "";
    private java.lang.String juNbrJour2 = "";
    private java.lang.String juNewNumContribuable = "";
    private java.lang.String juNumContribuable = "";
    private Boolean juTaxeMan = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */
    public CPCommunicationFiscaleRetourJUViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // setValeurChampRecherche(getValeurRechercheBD());
        super._afterRetrieve(transaction);
        if (JadeStringUtil.isEmpty(getJuNewNumContribuable())) {
            setNumContribuableRecu(getJuNumContribuable());
        } else {
            setNumContribuableRecu(getJuNewNumContribuable());
        }
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

        String table1 = "CPCRJUP";
        String table2 = "CPCRETP";

        if (isForBackup()) {
            table1 = "CPCRJUB";
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
            return "CPCRJUP";
        } else {
            return "CPCRJUB";
        }

    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        juCodeApplication = statement.dbReadDateAMJ("IKCAPP");
        juNumContribuable = statement.dbReadString("IKNCJU");
        juLot = statement.dbReadNumeric("IKNLOT");
        juNbrJour1 = statement.dbReadNumeric("IKNNJ1");
        juNbrJour2 = statement.dbReadNumeric("IKNNJ2");
        juGenreAffilie = statement.dbReadString("IKGAJU");
        juGenreTaxation = statement.dbReadString("IKGTJU");
        juEpoux = statement.dbReadBoolean("IKBEPO");
        juFiller = statement.dbReadString("IKNFJU");
        juTaxeMan = statement.dbReadBoolean("IKBTMA");
        juDateNaissance = statement.dbReadNumeric("IKDNAI");
        juNewNumContribuable = statement.dbReadNumeric("IKNNCO");
        super._readProperties(statement);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Les reception sont toujours chargée, elle
     * seront validées par les plausibilités
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getAnnee1())) {
            if ("365".equalsIgnoreCase(getJuNbrJour1())) {
                setDebutExercice1("01.01." + getAnnee1());
                setFinExercice1("31.12." + getAnnee1());
            } else {
                setDebutExercice1("01.01." + getAnnee1());
                setFinExercice1("01.01." + getAnnee1());
            }
        }
        if (!JadeStringUtil.isBlankOrZero(getAnnee2())) {
            if ("365".equalsIgnoreCase(getJuNbrJour2())) {
                setDebutExercice2("01.01." + getAnnee2());
                setFinExercice2("31.12." + getAnnee2());
            } else {
                setDebutExercice2("01.01." + getAnnee2());
                setFinExercice2("01.01." + getAnnee2());
            }
        }
        setValeurChampRecherche(this.getValeurRechercheBD());
        setNumContribuableRecu(getJuNumContribuable());
        CPCommentaireCommunicationManager mng = new CPCommentaireCommunicationManager();
        mng.setSession(getSession());
        mng.setForIdCommunicationRetour(getIdRetour());
        mng.find();
        for (int i = 0; i < mng.size(); i++) {
            ((CPCommentaireCommunication) mng.getEntity(i)).delete();
        }
        if (Boolean.TRUE.equals(getJuTaxeMan())) {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_DOSS_NON_NOTIFIE);
            comment.setIdCommunicationRetour(getIdRetour());
            comment.add(statement.getTransaction());
        } else {
            CPCommentaireCommunication comment = new CPCommentaireCommunication();
            comment.setSession(getSession());
            comment.setIdCommentaire(CPCommentaireCommunication.CS_DOSS_NON_NOTIFIE);
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
            process.setWantMajBackup(isWantMajBackup());
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
        statement.writeField("IKCAPP",
                this._dbWriteString(statement.getTransaction(), getJuCodeApplication(), "juCodeApplication"));
        statement.writeField("IKNCJU",
                this._dbWriteNumeric(statement.getTransaction(), getJuNumContribuable(), "juNumContribuable"));
        statement.writeField("IKNLOT", this._dbWriteNumeric(statement.getTransaction(), getJuLot(), "juLot"));
        statement.writeField("IKNNJ1", this._dbWriteNumeric(statement.getTransaction(), getJuNbrJour1(), "juNbrJour1"));
        statement.writeField("IKNNJ2", this._dbWriteNumeric(statement.getTransaction(), getJuNbrJour2(), "juNbrJour2"));
        statement.writeField("IKGAJU",
                this._dbWriteString(statement.getTransaction(), getJuGenreAffilie(), "juGenreAffilie"));
        statement.writeField("IKGTJU",
                this._dbWriteString(statement.getTransaction(), getJuGenreTaxation(), "juGenreTaxation"));
        statement.writeField("IKBEPO", this._dbWriteBoolean(statement.getTransaction(), getJuEpoux(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "juEpoux"));
        statement.writeField("IKNFJU", this._dbWriteString(statement.getTransaction(), getJuFiller(), "juFiller"));
        statement.writeField("IKBTMA", this._dbWriteBoolean(statement.getTransaction(), getJuTaxeMan(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "juTaxeMan"));
        statement.writeField("IKNNCO",
                this._dbWriteNumeric(statement.getTransaction(), getJuNewNumContribuable(), "newNumContribuable"));
        statement.writeField("IKDNAI",
                this._dbWriteNumeric(statement.getTransaction(), getJuDateNaissance(), "juDateNaissance"));
    }

    /**
     * Formate le numéro de contribuable JU
     * 
     * @param dateStr
     */
    public String formaterNumCtbJU(String numCtb) {
        String un = "";
        String deux = "";
        String trois = "";
        String quatre = "";
        String num = "";
        un = numCtb.substring(0, 3);
        deux = numCtb.substring(3, 6);
        trois = numCtb.substring(6, 9);
        quatre = numCtb.substring(9);
        num = un + "." + deux + "." + trois + "." + quatre;
        return num;
    }

    private String formaterOldNumCtbJU(String juNumContribuable2) {
        // String numFormate= numNonFormate.
        StringBuffer numFormate = new StringBuffer(juNumContribuable2);
        numFormate.insert(3, ".");
        numFormate.insert(6, ".");
        numFormate.insert(9, ".");
        numFormate.insert(12, "/");
        return numFormate.toString();
    }

    @Override
    public String getDescription(int cas) {
        description = "";
        if (cas == 0) {
            if (getJuNewNumContribuable().length() > 0) {
                description += getSession().getLabel("NUM_CONTRIBUABLE") + " : " + getJuNewNumContribuable() + "\n";
            }
        } else {
            description = getJuNewNumContribuable();
        }
        return description;
    }

    public java.lang.String getJuCodeApplication() {
        return juCodeApplication;
    }

    /**
     * @return
     */
    public java.lang.String getJuDateNaissance() {
        return juDateNaissance;
    }

    public Boolean getJuEpoux() {
        return juEpoux;
    }

    /**
     * @return
     */
    public java.lang.String getJuFiller() {
        return juFiller;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getJuGenreAffilie() {
        return juGenreAffilie;
    }

    public java.lang.String getJuGenreTaxation() {
        return juGenreTaxation;
    }

    public java.lang.String getJuLot() {
        return juLot;
    }

    public java.lang.String getJuNbrJour1() {
        return juNbrJour1;
    }

    public java.lang.String getJuNbrJour2() {
        return juNbrJour2;
    }

    public java.lang.String getJuNewNumContribuable() {
        return juNewNumContribuable;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getJuNumContribuable() {
        return juNumContribuable;
    }

    /**
     * @return
     */
    public Boolean getJuTaxeMan() {
        return juTaxeMan;
    }

    private String getValeurRechercheBD() {
        try {
            if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAffilie")) {
                return getNumAffilie();
            } else if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAvs")) {
                return this.getNumAvs(0);
            } else {
                if (!JadeStringUtil.isIntegerEmpty(getJuNewNumContribuable())) {
                    return formaterNumCtbJU(getJuNewNumContribuable());
                } else {
                    return formaterOldNumCtbJU(getJuNumContribuable());
                }
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getValeurRechercheBD(String zoneRecherche) {
        try {
            if (zoneRecherche.equalsIgnoreCase("numAffilie")) {
                return getNumAffilie();
            } else if (zoneRecherche.equalsIgnoreCase("numAvs")) {
                return this.getNumAvs(0);
            } else {
                if (!JadeStringUtil.isIntegerEmpty(getJuNewNumContribuable())) {
                    return getJuNewNumContribuable();
                } else {
                    return getJuNumContribuable();
                }
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean isForBackup() {
        return super.isForBackup();
    }

    public void setJuCodeApplication(java.lang.String string) {
        juCodeApplication = string;
    }

    /**
     * @param string
     */
    public void setJuDateNaissance(java.lang.String string) {
        juDateNaissance = string;
    }

    public void setJuEpoux(Boolean boolean1) {
        juEpoux = boolean1;
    }

    /**
     * @param string
     */
    public void setJuFiller(java.lang.String string) {
        juFiller = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @param newGenreAffilie
     *            java.lang.String
     */
    public void setJuGenreAffilie(java.lang.String newGenreAffilie) {
        juGenreAffilie = newGenreAffilie;
    }

    /**
     * Setter
     */
    public void setJuGenreTaxation(java.lang.String newGenreTaxation) {
        juGenreTaxation = newGenreTaxation;
    }

    public void setJuLot(java.lang.String string) {
        juLot = string;
    }

    public void setJuNbrJour1(java.lang.String string) {
        juNbrJour1 = string;
    }

    public void setJuNbrJour2(java.lang.String string) {
        juNbrJour2 = string;
    }

    public void setJuNewNumContribuable(java.lang.String string) {
        juNewNumContribuable = string;
    }

    /**
     * @param string
     */
    @Override
    public void setJuNumContribuable(java.lang.String string) {
        juNumContribuable = string;
    }

    /**
     * @param boolean1
     */
    public void setJuTaxeMan(Boolean boolean1) {
        juTaxeMan = boolean1;
    }
}
