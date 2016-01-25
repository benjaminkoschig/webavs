package globaz.phenix.db.communications;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.communications.CPProcessValiderPlausibilite;

public class CPCommunicationFiscaleRetourNEViewBean extends CPCommunicationFiscaleRetourViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String description = "descriptionNE";

    private java.lang.String neDateDebutAssuj = "";

    private java.lang.String neDateValeur = "";

    /*
     * Traitement avant ajout
     */

    private java.lang.String neDossierTaxe = "";
    private java.lang.String neDossierTrouve = "";
    private java.lang.String neFiller = "";
    private java.lang.String neFortuneAnnee1 = "";
    private java.lang.String neGenreAffilie = "";
    private java.lang.String neGenreTaxation = "";
    private java.lang.String neIndemniteJour = "";
    private java.lang.String neIndemniteJour1 = "";
    private java.lang.String neNumAvs = "";
    private java.lang.String neNumBDP = "";
    private java.lang.String neNumCaisse = "";
    private java.lang.String neNumClient = "";
    private java.lang.String neNumCommune = "";
    private java.lang.String neNumContribuable = "";
    private java.lang.String nePension = "";
    private java.lang.String nePensionAlimentaire = "";
    private java.lang.String nePensionAlimentaire1 = "";
    private java.lang.String nePensionAnnee1 = "";
    private java.lang.String nePremiereLettreNom = "";
    private java.lang.String neRenteTotale = "";
    private java.lang.String neRenteTotale1 = "";
    private java.lang.String neRenteViagere = "";
    private java.lang.String neRenteViagere1 = "";
    private Boolean neTaxationRectificative = new Boolean(false);

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */

    public CPCommunicationFiscaleRetourNEViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // setValeurChampRecherche(getValeurRechercheBD());
        super._afterRetrieve(transaction);
        setNumContribuableRecu(getNeNumBDP());
        setNumAffilieRecu(getNeNumClient());
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

        String table1 = "CPCRNEP";
        String table2 = "CPCRETP";

        if (isForBackup()) {
            table1 = "CPCRNEB";
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
            return "CPCRNEP";
        } else {
            return "CPCRNEB";
        }

    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        neFiller = statement.dbReadString("IKFINE");
        neNumAvs = statement.dbReadNumeric("IKNANE");
        neNumCaisse = statement.dbReadNumeric("IKCANE");
        neGenreAffilie = statement.dbReadNumeric("IKGANE");
        nePremiereLettreNom = statement.dbReadString("IKPLNO");
        neNumContribuable = statement.dbReadString("IKCONE");
        neNumCommune = statement.dbReadNumeric("IKNCOM");
        neNumBDP = statement.dbReadNumeric("IKNBDP");
        neNumClient = statement.dbReadNumeric("IKNCLI");
        neDateDebutAssuj = statement.dbReadDateAMJ("IKDDAS");
        neGenreTaxation = statement.dbReadString("IKGTNE");
        neTaxationRectificative = statement.dbReadBoolean("IKBTRC");
        neFortuneAnnee1 = statement.dbReadNumeric("IKMFO1", 2);
        nePensionAnnee1 = statement.dbReadNumeric("IKMPE1", 2);
        nePension = statement.dbReadNumeric("IKMPEN", 2);
        nePensionAlimentaire1 = statement.dbReadNumeric("IKMPA1", 2);
        nePensionAlimentaire = statement.dbReadNumeric("IKMPAL", 2);
        neRenteViagere1 = statement.dbReadNumeric("IKMRV1", 2);
        neRenteViagere = statement.dbReadNumeric("IKMRVI", 2);
        neIndemniteJour1 = statement.dbReadNumeric("IKMIJ1", 2);
        neIndemniteJour = statement.dbReadNumeric("IKMIJO", 2);
        neRenteTotale1 = statement.dbReadNumeric("IKMRT1", 2);
        neRenteTotale = statement.dbReadNumeric("IKMRTO", 2);
        neDateValeur = statement.dbReadDateAMJ("IKDVAL");
        neDossierTaxe = statement.dbReadNumeric("IKBDTA");
        neDossierTrouve = statement.dbReadString("IKBDTR");
        super._readProperties(statement);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Les reception sont toujours chargée, elle
     * seront validées par les plausibilités
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        setValeurChampRecherche(this.getValeurRechercheBD());
        // Pour NE le num contribuale est le numBDP et non le champ num
        // contribuable... no comment
        setNumContribuableRecu(getNeNumBDP());
        CPCommentaireCommunicationManager mng = new CPCommentaireCommunicationManager();
        mng.setSession(getSession());
        mng.setForIdCommunicationRetour(getIdRetour());
        mng.find();
        for (int i = 0; i < mng.size(); i++) {
            ((CPCommentaireCommunication) mng.getEntity(i)).delete();
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
        statement.writeField("IKFINE", this._dbWriteString(statement.getTransaction(), getNeFiller(), "fillerNE"));
        statement.writeField("IKNANE", this._dbWriteNumeric(statement.getTransaction(), getNeNumAvs(), "neNumAvs"));
        statement.writeField("IKCANE",
                this._dbWriteNumeric(statement.getTransaction(), getNeNumCaisse(), "neNumCaisse"));
        statement.writeField("IKGANE",
                this._dbWriteNumeric(statement.getTransaction(), getNeGenreAffilie(), "neGenreAffilie"));
        statement.writeField("IKPLNO",
                this._dbWriteString(statement.getTransaction(), getNePremiereLettreNom(), "nePremiereLettreNom"));
        statement.writeField("IKCONE",
                this._dbWriteNumeric(statement.getTransaction(), getNeNumContribuable(), "neNumContribuable"));
        statement.writeField("IKNCOM",
                this._dbWriteNumeric(statement.getTransaction(), getNeNumCommune(), "neNumCommune"));
        statement.writeField("IKNBDP", this._dbWriteNumeric(statement.getTransaction(), getNeNumBDP(), "neNumBDP"));
        statement.writeField("IKNCLI",
                this._dbWriteNumeric(statement.getTransaction(), getNeNumClient(), "neNumClient"));
        statement.writeField("IKDDAS",
                this._dbWriteDateAMJ(statement.getTransaction(), getNeDateDebutAssuj(), "neDateDebutAssuj"));
        statement.writeField("IKGTNE",
                this._dbWriteString(statement.getTransaction(), getNeGenreTaxation(), "neGenreTaxation"));
        statement.writeField("IKBTRC", this._dbWriteBoolean(statement.getTransaction(), getNeTaxationRectificative(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "neTaxationRectificative"));
        statement.writeField("IKMFO1",
                this._dbWriteNumeric(statement.getTransaction(), getNeFortuneAnnee1(), "neFortuneAnnee1"));
        statement.writeField("IKMPE1",
                this._dbWriteNumeric(statement.getTransaction(), getNePensionAnnee1(), "nePensionAnnee1"));
        statement.writeField("IKMPEN", this._dbWriteNumeric(statement.getTransaction(), getNePension(), "nePension"));
        statement.writeField("IKMPA1",
                this._dbWriteNumeric(statement.getTransaction(), getNePensionAlimentaire1(), "nePensionAlimentaire1"));
        statement.writeField("IKMPAL",
                this._dbWriteNumeric(statement.getTransaction(), getNePensionAlimentaire(), "nePensionAlimentaire"));
        statement.writeField("IKMRV1",
                this._dbWriteNumeric(statement.getTransaction(), getNeRenteViagere1(), "neRenteViagere1"));
        statement.writeField("IKMRVI",
                this._dbWriteNumeric(statement.getTransaction(), getNeRenteViagere(), "neRenteViagere"));
        statement.writeField("IKMIJ1",
                this._dbWriteNumeric(statement.getTransaction(), getNeIndemniteJour1(), "neIndemniteJour1"));
        statement.writeField("IKMIJO",
                this._dbWriteNumeric(statement.getTransaction(), getNeIndemniteJour(), "neIndemniteJour"));
        statement.writeField("IKMRT1",
                this._dbWriteNumeric(statement.getTransaction(), getNeRenteTotale1(), "neRenteTotale1"));
        statement.writeField("IKMRTO",
                this._dbWriteNumeric(statement.getTransaction(), getNeRenteTotale(), "neRenteTotale"));
        statement.writeField("IKDVAL",
                this._dbWriteDateAMJ(statement.getTransaction(), getNeDateValeur(), "neDateValeur"));
        statement.writeField("IKBDTA",
                this._dbWriteNumeric(statement.getTransaction(), getNeDossierTaxe(), "neDossierTaxe"));
        statement.writeField("IKBDTR",
                this._dbWriteString(statement.getTransaction(), getNeDossierTrouve(), "neDossierTrouve"));
    }

    @Override
    public String getDescription(int cas) {
        description = "";
        if (cas == 0) {
            if (getNeNumClient().length() > 0) {
                description += getSession().getLabel("NUM_AFFILIE") + " : " + getNeNumClient() + "\n";
            }
            if (getNeNumBDP().length() > 0) {
                description += getSession().getLabel("NUM_BDP") + " : " + getNeNumBDP() + "\n";
            }
            if (getNeNumContribuable().length() > 0) {
                description += getSession().getLabel("NUM_CONTRIBUABLE") + " : " + getNeNumContribuable() + "\n";
            }
        } else if (cas == 1) {
            description = getNeNumContribuable();
        } else if (cas == 2) {
            description = getNeNumClient();
        } else if (cas == 3) {
            description = getNeNumAvs();
        }
        return description;
    }

    public java.lang.String getNeDateDebutAssuj() {
        return neDateDebutAssuj;
    }

    public java.lang.String getNeDateValeur() {
        return neDateValeur;
    }

    /**
     * @return
     */
    public java.lang.String getNeDossierTaxe() {
        return neDossierTaxe;
    }

    /**
     * @return
     */
    public java.lang.String getNeDossierTrouve() {
        return neDossierTrouve;
    }

    /**
     * @return
     */
    public java.lang.String getNeFiller() {
        return neFiller;
    }

    public java.lang.String getNeFortuneAnnee1() {
        return neFortuneAnnee1;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @return java.lang.String
     */
    public java.lang.String getNeGenreAffilie() {
        return neGenreAffilie;
    }

    public java.lang.String getNeGenreTaxation() {
        return neGenreTaxation;
    }

    public java.lang.String getNeIndemniteJour() {
        return neIndemniteJour;
    }

    public java.lang.String getNeIndemniteJour1() {
        return neIndemniteJour1;
    }

    public java.lang.String getNeNumAvs() {
        if (JadeStringUtil.isIntegerEmpty(neNumAvs)) {
            return "";
        } else {
            return neNumAvs;
        }
    }

    /**
     * @return
     */
    public java.lang.String getNeNumBDP() {
        return neNumBDP;
    }

    public java.lang.String getNeNumCaisse() {
        return neNumCaisse;
    }

    public java.lang.String getNeNumClient() {
        return neNumClient;
    }

    /**
     * @return
     */
    public java.lang.String getNeNumCommune() {
        return neNumCommune;
    }

    /**
     * @return
     */
    @Override
    public java.lang.String getNeNumContribuable() {
        return neNumContribuable;
    }

    public java.lang.String getNePension() {
        return nePension;
    }

    public java.lang.String getNePensionAlimentaire() {
        return nePensionAlimentaire;
    }

    public java.lang.String getNePensionAlimentaire1() {
        return nePensionAlimentaire1;
    }

    public java.lang.String getNePensionAnnee1() {
        return nePensionAnnee1;
    }

    public java.lang.String getNePremiereLettreNom() {
        return nePremiereLettreNom;
    }

    public java.lang.String getNeRenteTotale() {
        return neRenteTotale;
    }

    public java.lang.String getNeRenteTotale1() {
        return neRenteTotale1;
    }

    public java.lang.String getNeRenteViagere() {
        return neRenteViagere;
    }

    public java.lang.String getNeRenteViagere1() {
        return neRenteViagere1;
    }

    public Boolean getNeTaxationRectificative() {
        return neTaxationRectificative;
    }

    @Override
    public String getNumAvs() {
        return getNeNumAvs();
    }

    public String getNumeroAVS() {
        return getNeNumAvs();
    }

    private String getValeurRechercheBD() {
        try {
            if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAffilie")) {
                return getNeNumClient();
            } else if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAvs")) {
                return getNeNumAvs();
            } else {
                return getNeNumBDP();
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getValeurRechercheBD(String zoneRecherche) {
        try {
            if (zoneRecherche.equalsIgnoreCase("numAffilie")) {
                return getNeNumClient();
            } else if (zoneRecherche.equalsIgnoreCase("numAvs")) {
                return getNeNumAvs();
            } else {
                return getNeNumContribuable();
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean isForBackup() {
        return super.isForBackup();
    }

    public void setNeDateDebutAssuj(java.lang.String string) {
        neDateDebutAssuj = string;
    }

    public void setNeDateValeur(java.lang.String string) {
        neDateValeur = string;
    }

    /**
     * @param string
     */
    public void setNeDossierTaxe(java.lang.String string) {
        neDossierTaxe = string;
    }

    /**
     * @param string
     */
    public void setNeDossierTrouve(java.lang.String string) {
        neDossierTrouve = string;
    }

    /**
     * @param string
     */
    public void setNeFiller(java.lang.String string) {
        neFiller = string;
    }

    public void setNeFortuneAnnee1(java.lang.String string) {
        neFortuneAnnee1 = string;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.03.2003 14:25:35)
     * 
     * @param newGenreAffilie
     *            java.lang.String
     */
    public void setNeGenreAffilie(java.lang.String newGenreAffilie) {
        neGenreAffilie = newGenreAffilie;
    }

    /**
     * Setter
     */
    public void setNeGenreTaxation(java.lang.String newGenreTaxation) {
        neGenreTaxation = newGenreTaxation;
    }

    public void setNeIndemniteJour(java.lang.String string) {
        neIndemniteJour = string;
    }

    public void setNeIndemniteJour1(java.lang.String string) {
        neIndemniteJour1 = string;
    }

    public void setNeNumAvs(java.lang.String string) {
        neNumAvs = string;
    }

    /**
     * @param string
     */
    public void setNeNumBDP(java.lang.String string) {
        neNumBDP = string;
    }

    public void setNeNumCaisse(java.lang.String string) {
        neNumCaisse = string;
    }

    public void setNeNumClient(java.lang.String string) {
        neNumClient = string;
    }

    /**
     * @param string
     */
    public void setNeNumCommune(java.lang.String string) {
        neNumCommune = string;
    }

    /**
     * @param string
     */
    @Override
    public void setNeNumContribuable(java.lang.String string) {
        neNumContribuable = string;
    }

    public void setNePension(java.lang.String string) {
        nePension = string;
    }

    public void setNePensionAlimentaire(java.lang.String string) {
        nePensionAlimentaire = string;
    }

    public void setNePensionAlimentaire1(java.lang.String string) {
        nePensionAlimentaire1 = string;
    }

    public void setNePensionAnnee1(java.lang.String string) {
        nePensionAnnee1 = string;
    }

    public void setNePremiereLettreNom(java.lang.String string) {
        nePremiereLettreNom = string;
    }

    public void setNeRenteTotale(java.lang.String string) {
        neRenteTotale = string;
    }

    /*
     * public String getRevenuNA(){ return "Revenu NE"; }
     */

    public void setNeRenteTotale1(java.lang.String string) {
        neRenteTotale1 = string;
    }

    public void setNeRenteViagere(java.lang.String string) {
        neRenteViagere = string;
    }

    public void setNeRenteViagere1(java.lang.String string) {
        neRenteViagere1 = string;
    }

    public void setNeTaxationRectificative(Boolean boolean1) {
        neTaxationRectificative = boolean1;
    }

}
