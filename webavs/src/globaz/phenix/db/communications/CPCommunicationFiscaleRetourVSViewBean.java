package globaz.phenix.db.communications;

import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BEntity;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.communications.CPProcessValiderPlausibilite;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.db.tiers.TITiersViewBean;

public class CPCommunicationFiscaleRetourVSViewBean extends CPCommunicationFiscaleRetourViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String description = "";
    private String vsAdresseAffilie1 = "";
    private String vsAdresseAffilie2 = "";
    private String vsAdresseAffilie3 = "";
    private String vsAdresseAffilie4 = "";
    private String vsAdresseConjoint1 = "";
    private String vsAdresseConjoint2 = "";
    private String vsAdresseConjoint3 = "";
    private String vsAdresseConjoint4 = "";
    private String vsAdresseCtb1 = "";
    private String vsAdresseCtb2 = "";
    private String vsAdresseCtb3 = "";
    private String vsAdresseCtb4 = "";
    private String vsAnneeTaxation = "";
    private String vsAutresRevenusConjoint = "";
    private String vsAutresRevenusCtb = "";
    private String vsCapitalPropreEngageEntrepriseConjoint = "";
    private String vsCapitalPropreEngageEntrepriseCtb = "";
    private String vsCodeTaxation1 = "";
    private String vsCodeTaxation2 = "";
    private String vsCotisationAvsAffilie = "";
    private String vsCotisationAvsConjoint = "";
    private String vsDateCommunication = "";
    private String vsDateDebutAffiliation = "";
    private String vsDateDebutAffiliationCaisseProfessionnelle = "";
    private String vsDateDebutAffiliationConjoint = "";
    private String vsDateDebutAffiliationConjointCaisseProfessionnelle = "";
    private String vsDateDecesCtb = "";
    private String vsDateDemandeCommunication = "";
    private String vsDateFinAffiliation = "";
    private String vsDateFinAffiliationCaisseProfessionnelle = "";
    private String vsDateFinAffiliationConjoint = "";
    private String vsDateFinAffiliationConjointCaisseProfessionnelle = "";
    private String vsDateNaissanceAffilie = "";
    private String vsDateNaissanceConjoint = "";
    private String vsDateNaissanceCtb = "";
    private String vsDateTaxation = "";
    private String vsDebutActiviteConjoint = "";
    private String vsDebutActiviteCtb = "";
    private String vsEtatCivilAffilie = "";
    private String vsEtatCivilCtb = "";
    private String vsFinActiviteConjoint = "";
    private String vsFinActiviteCtb = "";
    private String vsFortunePriveeConjoint = "";
    private String vsFortunePriveeCtb = "";
    private String vsLangue = "";
    private String vsLibre3 = "";
    private String vsLibre4 = "";
    private String vsNbJoursTaxation = "";
    private String vsNoCaisseAgenceAffilie = "";
    private String vsNoCaisseProfessionnelleAffilie = "";
    private String vsNomAffilie = "";
    private String vsNomConjoint = "";
    private String vsNomPrenomContribuableAnnee = "";
    private String vsNoPostalLocalite = "";
    private String vsNumAffilie = "";
    private String vsNumAffilieConjoint = "";
    private String vsNumAffilieInterneCaisseProfessionnelle = "";
    private String vsNumAffilieInterneConjointCaisseProfessionnelle = "";
    private String vsNumAvsAffilie = "";
    private String vsNumAvsConjoint = "";
    private String vsNumAvsCtb = "";
    private String vsNumCaisseAgenceConjoint = "";
    private String vsNumCaisseProfessionnelleConjoint = "";
    private String vsNumCtb = "";
    private String vsNumCtbSuivant = "";
    private String vsNumPostalLocaliteConjoint = "";
    private String vsNumPostalLocaliteCtb = "";
    private String vsRachatLpp = "";
    private String vsRachatLppCjt = "";
    private String vsReserve = "";
    private String vsReserveDateNaissanceConjoint = "";
    private String vsReserveFichierImpression = "";
    private String vsReserveTriNumCaisse = "";
    private String vsRevenuAgricoleConjoint = "";
    private String vsRevenuAgricoleCtb = "";
    private String vsRevenuNonAgricoleConjoint = "";
    private String vsRevenuNonAgricoleCtb = "";
    private String vsRevenuRenteConjoint = "";
    private String vsRevenuRenteCtb = "";
    private String vsSalairesConjoint = "";
    private String vsSalairesContribuable = "";
    private String vsSexeAffilie = "";

    private String vsSexeCtb = "";

    private String vsTypeTaxation = "";

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */
    public CPCommunicationFiscaleRetourVSViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // setValeurChampRecherche(getValeurRechercheBD());
        super._afterRetrieve(transaction);
        setNumContribuableRecu(getVsNumCtb());
        setNumAffilieRecu(this.getVsNumAffilie());
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        if (!isForBackup()) {
            setIdRetour(this._incCounter(transaction, "0", "CPCRETP", "0", "0"));
        }
    }

    /*
     * Traitement avant ajout
     */

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPCRVSP";
        String table2 = "CPCRETP";

        if (isForBackup()) {
            table1 = "CPCRVSB";
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
            return "CPCRVSP";
        } else {
            return "CPCRVSB";
        }

    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        vsNumCtb = statement.dbReadString("IKNUMC");
        vsAnneeTaxation = statement.dbReadNumeric("IKANNE");
        vsDateDemandeCommunication = statement.dbReadDateAMJ("IKDATD");
        vsDateCommunication = statement.dbReadDateAMJ("IKDATC");
        vsDateTaxation = statement.dbReadDateAMJ("IKDATT");
        vsCodeTaxation1 = statement.dbReadNumeric("IKCOD1");
        vsCodeTaxation2 = statement.dbReadNumeric("IKCOD2");
        vsTypeTaxation = statement.dbReadString("IKTYPT");
        vsNumAffilie = statement.dbReadString("IKNUMA");
        vsNumAvsAffilie = statement.dbReadNumeric("IKNAVS");
        vsDateNaissanceAffilie = statement.dbReadDateAMJ("IKDNAI");
        vsDateDebutAffiliation = statement.dbReadDateAMJ("IKDDEB");
        vsDateFinAffiliation = statement.dbReadDateAMJ("IKDFIN");
        vsNomAffilie = statement.dbReadString("IKNOMA");
        vsAdresseAffilie1 = statement.dbReadString("IKADR1");
        vsAdresseAffilie2 = statement.dbReadString("IKADR2");
        vsAdresseAffilie3 = statement.dbReadString("IKADR3");
        vsAdresseAffilie4 = statement.dbReadString("IKADR4");
        vsNoPostalLocalite = statement.dbReadString("IKNPAA");
        vsNoCaisseAgenceAffilie = statement.dbReadNumeric("IKNCAI");
        vsNoCaisseProfessionnelleAffilie = statement.dbReadNumeric("IKNCPR");
        vsDateDebutAffiliationCaisseProfessionnelle = statement.dbReadDateAMJ("IKDDEC");
        vsDateFinAffiliationCaisseProfessionnelle = statement.dbReadDateAMJ("IKDFCP");
        vsNumAffilieInterneCaisseProfessionnelle = statement.dbReadString("IKNUCP");
        vsCotisationAvsAffilie = statement.dbReadNumeric("IKCOTI");
        vsEtatCivilAffilie = statement.dbReadNumeric("IKECIV");
        vsSexeAffilie = statement.dbReadNumeric("IKSEXE");
        vsNumAffilieConjoint = statement.dbReadString("IKNOCO");
        vsNumAvsConjoint = statement.dbReadNumeric("IKNACO");
        vsDateNaissanceConjoint = statement.dbReadDateAMJ("IKDCON");
        vsDateDebutAffiliationConjoint = statement.dbReadDateAMJ("IKDAFC");
        vsDateFinAffiliationConjoint = statement.dbReadDateAMJ("IKFAFC");
        vsNomConjoint = statement.dbReadString("IKNCON");
        vsAdresseConjoint1 = statement.dbReadString("IKADC1");
        vsAdresseConjoint2 = statement.dbReadString("IKADC2");
        vsAdresseConjoint3 = statement.dbReadString("IKADC3");
        vsAdresseConjoint4 = statement.dbReadString("IKADC4");
        vsNumPostalLocaliteConjoint = statement.dbReadString("IKNPAC");
        vsNumCaisseAgenceConjoint = statement.dbReadNumeric("IKNCAC");
        vsNumCaisseProfessionnelleConjoint = statement.dbReadNumeric("IKNCPC");
        vsDateDebutAffiliationConjointCaisseProfessionnelle = statement.dbReadDateAMJ("IKDACC");
        vsDateFinAffiliationConjointCaisseProfessionnelle = statement.dbReadDateAMJ("IKDFAC");
        vsNumAffilieInterneConjointCaisseProfessionnelle = statement.dbReadString("IKNAIC");
        vsCotisationAvsConjoint = statement.dbReadNumeric("IKCOTC");
        vsNomPrenomContribuableAnnee = statement.dbReadString("IKNPCA");
        vsAdresseCtb1 = statement.dbReadString("IKACO1");
        vsAdresseCtb2 = statement.dbReadString("IKACO2");
        vsAdresseCtb3 = statement.dbReadString("IKACO3");
        vsAdresseCtb4 = statement.dbReadString("IKACO4");
        vsNumPostalLocaliteCtb = statement.dbReadString("IKNPCO");
        vsEtatCivilCtb = statement.dbReadNumeric("IKECCB");
        vsSexeCtb = statement.dbReadNumeric("IKSECB");
        vsLangue = statement.dbReadString("IKLANG");
        vsNumAvsCtb = statement.dbReadNumeric("IKAVCB");
        vsDateNaissanceCtb = statement.dbReadDateAMJ("IKDNCB");
        vsDebutActiviteCtb = statement.dbReadDateAMJ("IKDACB");
        vsFinActiviteCtb = statement.dbReadDateAMJ("IKFACB");
        vsDebutActiviteConjoint = statement.dbReadDateAMJ("IKDACO");
        vsFinActiviteConjoint = statement.dbReadDateAMJ("IKFACO");
        vsRevenuNonAgricoleCtb = statement.dbReadNumeric("IKRECB");
        vsRevenuNonAgricoleConjoint = statement.dbReadNumeric("IKRECO");
        vsRevenuAgricoleCtb = statement.dbReadNumeric("IKRAGR");
        vsRevenuAgricoleConjoint = statement.dbReadNumeric("IKRAGC");
        vsCapitalPropreEngageEntrepriseCtb = statement.dbReadNumeric("IKCAPP");
        vsCapitalPropreEngageEntrepriseConjoint = statement.dbReadNumeric("IKCAPC");
        vsRevenuRenteCtb = statement.dbReadNumeric("IKRENT");
        vsRevenuRenteConjoint = statement.dbReadNumeric("IKRENC");
        vsFortunePriveeCtb = statement.dbReadNumeric("IKFORP");
        vsFortunePriveeConjoint = statement.dbReadNumeric("IKFORC");
        vsSalairesContribuable = statement.dbReadNumeric("IKSALA");
        vsSalairesConjoint = statement.dbReadNumeric("IKSALC");
        vsAutresRevenusCtb = statement.dbReadNumeric("IKAREV");
        vsAutresRevenusConjoint = statement.dbReadNumeric("IKAREC");
        vsRachatLpp = statement.dbReadNumeric("IKLIB1");
        vsRachatLppCjt = statement.dbReadNumeric("IKLIB2");
        vsLibre3 = statement.dbReadNumeric("IKLIB3");
        vsLibre4 = statement.dbReadNumeric("IKLIB4");
        vsReserve = statement.dbReadString("IKRESE");
        vsNbJoursTaxation = statement.dbReadNumeric("IKNBJT");
        vsNumCtbSuivant = statement.dbReadNumeric("IKNCBS");
        vsDateDecesCtb = statement.dbReadDateAMJ("IKDDCC");
        vsReserveDateNaissanceConjoint = statement.dbReadDateAMJ("IKRDNC");
        vsReserveFichierImpression = statement.dbReadString("IKRFIM");
        vsReserveTriNumCaisse = statement.dbReadNumeric("IKRTNC");
        super._readProperties(statement);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Les reception sont toujours chargée, elle
     * seront validées par les plausibilités
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        setValeurChampRecherche(formaterNumCtbVS(this.getValeurRechercheBD()));
        setNumContribuableRecu(getVsNumCtb());
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
            process.setJournal(getJournalRetour());
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
        statement.writeField("IKNUMC", this._dbWriteString(statement.getTransaction(), getVsNumCtb(), "vsNumCtb"));
        statement.writeField("IKANNE",
                this._dbWriteNumeric(statement.getTransaction(), getVsAnneeTaxation(), "vsAnneeTaxation"));
        statement.writeField("IKDATD", this._dbWriteDateAMJ(statement.getTransaction(),
                getVsDateDemandeCommunication(), "vsDateDemandeCommunication"));
        statement.writeField("IKDATC",
                this._dbWriteDateAMJ(statement.getTransaction(), getVsDateCommunication(), "vsDateCommunication"));
        statement.writeField("IKDATT",
                this._dbWriteDateAMJ(statement.getTransaction(), getVsDateTaxation(), "vsDateTaxation"));
        statement.writeField("IKCOD1",
                this._dbWriteNumeric(statement.getTransaction(), getVsCodeTaxation1(), "vsCodeTaxation1"));
        statement.writeField("IKCOD2",
                this._dbWriteNumeric(statement.getTransaction(), getVsCodeTaxation2(), "vsCodeTaxation2"));
        statement.writeField("IKTYPT",
                this._dbWriteString(statement.getTransaction(), getVsTypeTaxation(), "vsTypeTaxation"));
        statement.writeField("IKNUMA",
                this._dbWriteString(statement.getTransaction(), this.getVsNumAffilie(0), "vsNumAffilie"));
        statement.writeField("IKNAVS", this._dbWriteNumeric(statement.getTransaction(),
                CPToolBox.unFormat(getVsNumAvsAffilie(0)), "vsNumAvsAffilie"));
        statement
                .writeField("IKDNAI", this._dbWriteDateAMJ(statement.getTransaction(), getVsDateNaissanceAffilie(),
                        "vsDateNaissanceAffilie"));
        statement
                .writeField("IKDDEB", this._dbWriteDateAMJ(statement.getTransaction(), getVsDateDebutAffiliation(),
                        "vsDateDebutAffiliation"));
        statement.writeField("IKDFIN",
                this._dbWriteDateAMJ(statement.getTransaction(), getVsDateFinAffiliation(), "vsDateFinAffiliation"));
        statement.writeField("IKNOMA",
                this._dbWriteString(statement.getTransaction(), getVsNomAffilie(), "vsNomAffilie"));
        statement.writeField("IKADR1",
                this._dbWriteString(statement.getTransaction(), getVsAdresseAffilie1(), "vsAdresseAffilie1"));
        statement.writeField("IKADR2",
                this._dbWriteString(statement.getTransaction(), getVsAdresseAffilie2(), "vsAdresseAffilie2"));
        statement.writeField("IKADR3",
                this._dbWriteString(statement.getTransaction(), getVsAdresseAffilie3(), "vsAdresseAffilie3"));
        statement.writeField("IKADR4",
                this._dbWriteString(statement.getTransaction(), getVsAdresseAffilie4(), "vsAdresseAffilie4"));
        statement.writeField("IKNPAA",
                this._dbWriteString(statement.getTransaction(), getVsNoPostalLocalite(), "vsNoPostalLocalite"));
        statement.writeField("IKNCAI", this._dbWriteNumeric(statement.getTransaction(),
                CPToolBox.unFormat(getVsNoCaisseAgenceAffilie(0)), "vsNoCaisseAgenceAffilie"));
        statement.writeField("IKNCPR", this._dbWriteNumeric(statement.getTransaction(),
                getVsNoCaisseProfessionnelleAffilie(), "vsNoCaisseProfessionnelleAffilie"));
        statement.writeField("IKDDEC", this._dbWriteDateAMJ(statement.getTransaction(),
                getVsDateDebutAffiliationCaisseProfessionnelle(), "vsDateDebutAffiliationCaisseProfessionnelle"));
        statement.writeField("IKDFCP", this._dbWriteDateAMJ(statement.getTransaction(),
                getVsDateFinAffiliationCaisseProfessionnelle(), "vsDateFinAffiliationCaisseProfessionnelle"));
        statement.writeField("IKNUCP", this._dbWriteString(statement.getTransaction(),
                getVsNumAffilieInterneCaisseProfessionnelle(), "vsNumAffilieInterneCaisseProfessionnelle"));
        statement.writeField("IKCOTI",
                this._dbWriteNumeric(statement.getTransaction(), vsCotisationAvsAffilie, "vsCotisationAvsAffilie"));
        statement.writeField("IKECIV",
                this._dbWriteNumeric(statement.getTransaction(), getVsEtatCivilAffilie(), "vsEtatCivilAffilie"));
        statement.writeField("IKSEXE",
                this._dbWriteNumeric(statement.getTransaction(), getVsSexeAffilie(), "vsSexeAffilie"));
        statement.writeField("IKNOCO", this._dbWriteString(statement.getTransaction(),
                CPToolBox.unFormat(getVsNumAffilieConjoint(0)), "vsNumAffilieConjoint"));
        statement.writeField("IKNACO", this._dbWriteNumeric(statement.getTransaction(),
                CPToolBox.unFormat(getVsNumAvsConjoint(0)), "vsNumAvsConjoint"));
        statement.writeField("IKDCON", this._dbWriteDateAMJ(statement.getTransaction(), getVsDateNaissanceConjoint(),
                "vsDateNaissanceConjoint"));
        statement.writeField("IKDAFC", this._dbWriteDateAMJ(statement.getTransaction(),
                getVsDateDebutAffiliationConjoint(), "vsDateDebutAffiliationConjoint"));
        statement.writeField("IKFAFC", this._dbWriteDateAMJ(statement.getTransaction(),
                getVsDateFinAffiliationConjoint(), "vsDateFinAffiliationConjoint"));
        statement.writeField("IKNCON",
                this._dbWriteString(statement.getTransaction(), getVsNomConjoint(), "vsNomConjoint"));
        statement.writeField("IKADC1",
                this._dbWriteString(statement.getTransaction(), getVsAdresseConjoint1(), "vsAdresseConjoint1"));
        statement.writeField("IKADC2",
                this._dbWriteString(statement.getTransaction(), getVsAdresseConjoint2(), "vsAdresseConjoint2"));
        statement.writeField("IKADC3",
                this._dbWriteString(statement.getTransaction(), getVsAdresseConjoint3(), "vsAdresseConjoint3"));
        statement.writeField("IKADC4",
                this._dbWriteString(statement.getTransaction(), getVsAdresseConjoint4(), "vsAdresseConjoint4"));
        statement.writeField("IKNPAC", this._dbWriteString(statement.getTransaction(),
                getVsNumPostalLocaliteConjoint(), "vsNumPostalLocaliteConjoint"));
        statement.writeField("IKNCAC", this._dbWriteNumeric(statement.getTransaction(),
                CPToolBox.unFormat(getVsNumCaisseAgenceConjoint(0)), "vsNumCaisseAgenceConjoint"));
        statement.writeField("IKNCPC", this._dbWriteNumeric(statement.getTransaction(),
                getVsNumCaisseProfessionnelleConjoint(), "vsNumCaisseProfessionnelleConjoint"));
        statement.writeField("IKDACC", this._dbWriteDateAMJ(statement.getTransaction(),
                getVsDateDebutAffiliationConjointCaisseProfessionnelle(),
                "vsDateDebutAffiliationConjointCaisseProfessionnelle"));
        statement.writeField("IKDFAC", this._dbWriteDateAMJ(statement.getTransaction(),
                getVsDateFinAffiliationConjointCaisseProfessionnelle(),
                "vsDateFinAffiliationConjointCaisseProfessionnelle"));
        statement.writeField("IKNAIC", this._dbWriteString(statement.getTransaction(),
                getVsNumAffilieInterneConjointCaisseProfessionnelle(),
                "vsNumAffilieInterneConjointCaisseProfessionnelle"));
        statement.writeField("IKCOTC",
                this._dbWriteNumeric(statement.getTransaction(), vsCotisationAvsConjoint, "vsCotisationAvsConjoint"));
        statement.writeField("IKNPCA", this._dbWriteString(statement.getTransaction(),
                getVsNomPrenomContribuableAnnee(), "vsNomPrenomContribuableAnnee"));
        statement.writeField("IKACO1",
                this._dbWriteString(statement.getTransaction(), getVsAdresseCtb1(), "vsAdresseCtb1"));
        statement.writeField("IKACO2",
                this._dbWriteString(statement.getTransaction(), getVsAdresseCtb2(), "vsAdresseCtb2"));
        statement.writeField("IKACO3",
                this._dbWriteString(statement.getTransaction(), getVsAdresseCtb3(), "vsAdresseCtb3"));
        statement.writeField("IKACO4",
                this._dbWriteString(statement.getTransaction(), getVsAdresseCtb4(), "vsAdresseCtb4"));
        statement.writeField("IKNPCO",
                this._dbWriteString(statement.getTransaction(), getVsNumPostalLocaliteCtb(), "vsNumPostalLocaliteCtb"));
        statement.writeField("IKECCB",
                this._dbWriteNumeric(statement.getTransaction(), getVsEtatCivilCtb(), "vsEtatCivilCtb"));
        statement.writeField("IKSECB", this._dbWriteNumeric(statement.getTransaction(), getVsSexeCtb(), "vsSexeCtb"));
        statement.writeField("IKLANG", this._dbWriteString(statement.getTransaction(), getVsLangue(), "vsLangue"));
        statement.writeField("IKAVCB",
                this._dbWriteNumeric(statement.getTransaction(), CPToolBox.unFormat(getVsNumAvsCtb(0)), "vsNumAvsCtb"));
        statement.writeField("IKDNCB",
                this._dbWriteDateAMJ(statement.getTransaction(), getVsDateNaissanceCtb(), "vsDateNaissanceCtb"));
        statement.writeField("IKDACB",
                this._dbWriteDateAMJ(statement.getTransaction(), getVsDebutActiviteCtb(), "vsDebutActiviteCtb"));
        statement.writeField("IKFACB",
                this._dbWriteDateAMJ(statement.getTransaction(), getVsFinActiviteCtb(), "vsFinActiviteCtb"));
        statement.writeField("IKDACO", this._dbWriteDateAMJ(statement.getTransaction(), getVsDebutActiviteConjoint(),
                "vsDebutActiviteConjoint"));
        statement.writeField("IKFACO",
                this._dbWriteDateAMJ(statement.getTransaction(), getVsFinActiviteConjoint(), "vsFinActiviteConjoint"));
        statement.writeField("IKRECB",
                this._dbWriteNumeric(statement.getTransaction(), vsRevenuNonAgricoleCtb, "vsRevenuNonAgricoleCtb"));
        statement.writeField("IKRECO", this._dbWriteNumeric(statement.getTransaction(), vsRevenuNonAgricoleConjoint,
                "vsRevenuNonAgricoleConjoint"));
        statement.writeField("IKRAGR",
                this._dbWriteNumeric(statement.getTransaction(), vsRevenuAgricoleCtb, "vsRevenuAgricoleCtb"));
        statement.writeField("IKRAGC",
                this._dbWriteNumeric(statement.getTransaction(), vsRevenuAgricoleConjoint, "vsRevenuAgricoleConjoint"));
        statement.writeField("IKCAPP", this._dbWriteNumeric(statement.getTransaction(),
                vsCapitalPropreEngageEntrepriseCtb, "vsCapitalPropreEngageEntrepriseCtb"));
        statement.writeField("IKCAPC", this._dbWriteNumeric(statement.getTransaction(),
                vsCapitalPropreEngageEntrepriseConjoint, "vsCapitalPropreEngageEntrepriseConjoint"));
        statement.writeField("IKRENT",
                this._dbWriteNumeric(statement.getTransaction(), vsRevenuRenteCtb, "vsRevenuRenteCtb"));
        statement.writeField("IKRENC",
                this._dbWriteNumeric(statement.getTransaction(), vsRevenuRenteConjoint, "vsRevenuRenteConjoint"));
        statement.writeField("IKFORP",
                this._dbWriteNumeric(statement.getTransaction(), vsFortunePriveeCtb, "vsFortunePriveeCtb"));
        statement.writeField("IKFORC",
                this._dbWriteNumeric(statement.getTransaction(), vsFortunePriveeConjoint, "vsFortunePriveeConjoint"));
        statement.writeField("IKSALA",
                this._dbWriteNumeric(statement.getTransaction(), vsSalairesContribuable, "vsSalairesContribuable"));
        statement.writeField("IKSALC",
                this._dbWriteNumeric(statement.getTransaction(), vsSalairesConjoint, "vsSalairesConjoint"));
        statement.writeField("IKAREV",
                this._dbWriteNumeric(statement.getTransaction(), vsAutresRevenusCtb, "vsAutresRevenusCtb"));
        statement.writeField("IKAREC",
                this._dbWriteNumeric(statement.getTransaction(), vsAutresRevenusConjoint, "vsAutresRevenusConjoint"));
        statement.writeField("IKLIB1", this._dbWriteNumeric(statement.getTransaction(), vsRachatLpp, "vsRachatLpp"));
        statement.writeField("IKLIB2",
                this._dbWriteNumeric(statement.getTransaction(), vsRachatLppCjt, "vsRachatLppCjt"));
        statement.writeField("IKLIB3", this._dbWriteNumeric(statement.getTransaction(), getVsLibre3(), "vsLibre3"));
        statement.writeField("IKLIB4", this._dbWriteNumeric(statement.getTransaction(), getVsLibre4(), "vsLibre4"));
        statement.writeField("IKRESE", this._dbWriteString(statement.getTransaction(), getVsReserve(), "vsReserve"));
        statement.writeField("IKNBJT",
                this._dbWriteNumeric(statement.getTransaction(), getVsNbJoursTaxation(), "vsNbJoursTaxation"));
        statement.writeField("IKNCBS",
                this._dbWriteNumeric(statement.getTransaction(), getVsNumCtbSuivant(), "vsNumCtbSuivant"));
        statement.writeField("IKDDCC",
                this._dbWriteDateAMJ(statement.getTransaction(), getVsDateDecesCtb(), "vsDateDecesCtb"));
        statement.writeField("IKRDNC", this._dbWriteDateAMJ(statement.getTransaction(),
                getVsReserveDateNaissanceConjoint(), "vsReserveDateNaissanceConjoint"));
        statement.writeField("IKRFIM", this._dbWriteString(statement.getTransaction(), getVsReserveFichierImpression(),
                "vsReserveFichierImpression"));
        statement.writeField("IKRTNC",
                this._dbWriteNumeric(statement.getTransaction(), vsRachatLppCjt, "vsRachatLppCjt"));
    }

    public String formaterNumAffilieVS(String numAffilieVs) {
        try {
            // Si le cas a été envoyé via l'ancienne application => il faut formatter le numéro
            // de la forme suivante xxx.xxx.xxxx en xxx.xx.00xxxx
            if (Integer.parseInt(getVsAnneeTaxation()) >= 2010) {
                return numAffilieVs;
            } else {
                return numAffilieVs.substring(0, 6) + "00" + numAffilieVs.substring(6, 10);
            }
        } catch (Exception ex) {
            return numAffilieVs;
        }
    }

    /**
     * Formate le numéro de contribuable JU
     * 
     * @param dateStr
     */
    public String formaterNumCtbVS(String numCtb) {
        try {
            if (!JadeStringUtil.isEmpty(numCtb)) {
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
            } else {
                return "";
            }
        } catch (Exception ex) {
            return numCtb;
        }
    }

    @Override
    public String getAutreRevenu() {
        return getVsAutresRevenusCtb();
    }

    @Override
    public String getAutreRevenuConjoint() {
        return getVsAutresRevenusConjoint();
    }

    /*
     * Utilse pour surcharger l'impression retour qui est commun pour tout les fiscs
     */
    @Override
    public String getCapitalEntreprise() {
        if (!JadeStringUtil.isBlankOrZero(vsCapitalPropreEngageEntrepriseCtb)) {
            return JANumberFormatter.fmt(vsCapitalPropreEngageEntrepriseCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getCapitalEntrepriseConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsCapitalPropreEngageEntrepriseConjoint)) {
            return JANumberFormatter.fmt(vsCapitalPropreEngageEntrepriseConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getCodeSexe() {
        if (getVsSexeCtb().equalsIgnoreCase("1")) {
            return TITiersViewBean.CS_HOMME;
        } else if (getVsSexeCtb().equalsIgnoreCase("2")) {
            return TITiersViewBean.CS_FEMME;
        } else if (getVsEtatCivilCtb().equalsIgnoreCase("3")) {
            return "502004"; // Hoirie
        }
        return "";
    }

    @Override
    public String getDescription(int cas) {
        description = "";
        if (cas == 0) {
            if (getVsNumCtb().length() > 0) {
                description = formaterNumCtbVS(getVsNumCtb()) + "                            "
                        + getVsDateNaissanceAffilie() + "\n";
            }
            if (this.getVsNumAffilie().length() > 0) {
                try {
                    TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                    IFormatData affilieFormater = app.getAffileFormater();
                    if (affilieFormater != null) {
                        description += getSession().getLabel("NUM_AFFILIE") + " : "
                                + affilieFormater.format(formaterNumAffilieVS(this.getVsNumAffilie(0))) + "\n";
                    } else {
                        description += getSession().getLabel("NUM_AFFILIE") + " : "
                                + formaterNumAffilieVS(this.getVsNumAffilie(0)) + "\n";
                    }
                } catch (Exception e) {
                    description += getSession().getLabel("NUM_AFFILIE") + " : "
                            + formaterNumAffilieVS(this.getVsNumAffilie(0)) + "\n";
                }
            }
            if (getVsNomPrenomContribuableAnnee().length() > 0) {
                description += getVsNomPrenomContribuableAnnee() + "\n";
            }
            if (getVsAdresseCtb1().length() > 0) {
                description += getVsAdresseCtb1() + "\n";
            }
            if (getVsAdresseCtb2().length() > 0) {
                description += getVsAdresseCtb2() + "\n";
            }
            if (getVsAdresseCtb3().length() > 0) {
                description += getVsAdresseCtb3() + "\n";
            }
            if (getVsAdresseCtb4().length() > 0) {
                description += getVsAdresseCtb4() + "\n";
            }
        } else if (cas == 1) {
            description = formaterNumCtbVS(getVsNumCtb());
        } else if (cas == 2) {
            if (!JadeStringUtil.isBlankOrZero(this.getVsNumAffilie())) {
                description = this.getVsNumAffilie(1);
            } else {
                description = getVsNumAffilieConjoint(1);
            }
        } else if (cas == 3) {
            if (!JadeStringUtil.isBlankOrZero(getVsNumAvsCtb(0))) {
                description = getVsNumAvsCtb(1);
            } else {
                description = getVsNumAvsConjoint(1);
            }
        } else if (cas == 4) {
            if (getVsNumCtb().length() > 0) {
                description = formaterNumCtbVS(getVsNumCtb()) + "                            "
                        + getVsDateNaissanceAffilie() + "\n";
            }
            if (getVsNomPrenomContribuableAnnee().length() > 0) {
                description += getVsNomPrenomContribuableAnnee() + "\n";
            }
            if (getVsAdresseCtb1().length() > 0) {
                description += getVsAdresseCtb1() + "\n";
            }
            if (getVsAdresseCtb2().length() > 0) {
                description += getVsAdresseCtb2() + "\n";
            }
            if (getVsAdresseCtb3().length() > 0) {
                description += getVsAdresseCtb3() + "\n";
            }
            if (getVsAdresseCtb4().length() > 0) {
                description += getVsAdresseCtb4() + "\n";
            }
        }
        return description;
    }

    @Override
    public String getEtatCivil() {
        if (getVsEtatCivilCtb().equalsIgnoreCase("1")) {
            return TITiersViewBean.CS_CELIBATAIRE;
        } else if (getVsEtatCivilCtb().equalsIgnoreCase("2")) {
            return TITiersViewBean.CS_MARIE;
        } else if (getVsEtatCivilCtb().equalsIgnoreCase("3")) {
            return TITiersViewBean.CS_VEUF;
        } else if (getVsEtatCivilCtb().equalsIgnoreCase("4")) {
            return TITiersViewBean.CS_DIVORCE;
        } else if (getVsEtatCivilCtb().equalsIgnoreCase("5")) {
            return TITiersViewBean.CS_SEPARE;
        }
        return "";
    }

    @Override
    public String getFortune() {
        if (!JadeStringUtil.isBlankOrZero(vsFortunePriveeCtb)) {
            return JANumberFormatter.fmt(vsFortunePriveeCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    /**
     * @return
     */
    @Override
    public String getNumAvsFisc(int codeFormat) {
        return getVsNumAvsCtb(0);
    }

    @Override
    public String getRachatLpp() {
        if (!JadeStringUtil.isBlankOrZero(vsRachatLpp)) {
            return JANumberFormatter.fmt(vsRachatLpp, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRachatLppCjt() {
        if (!JadeStringUtil.isBlankOrZero(vsRachatLppCjt)) {
            return JANumberFormatter.fmt(vsRachatLppCjt, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuA() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuAgricoleCtb)) {
            return JANumberFormatter.fmt(vsRevenuAgricoleCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuAConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuAgricoleConjoint)) {
            return JANumberFormatter.fmt(vsRevenuAgricoleConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuNA() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuNonAgricoleCtb)) {
            return JANumberFormatter.fmt(vsRevenuNonAgricoleCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuNAConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuNonAgricoleConjoint)) {
            return JANumberFormatter.fmt(vsRevenuNonAgricoleConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuR() {
        FWCurrency cur = new FWCurrency(JANumberFormatter.deQuote(vsRevenuRenteCtb));
        cur.add(JANumberFormatter.deQuote(vsRevenuRenteConjoint));
        if (!JadeStringUtil.isBlankOrZero(cur.toString())) {
            return JANumberFormatter.fmt(cur.toString(), true, false, false, 0);
        } else {
            return "0";
        }
    }

    /*
     * Utilse pour surcharger l'impression retour qui est commun pour tout les fiscs
     */
    @Override
    public String getSalaire() {
        if (!JadeStringUtil.isBlankOrZero(vsSalairesContribuable)) {
            return JANumberFormatter.fmt(vsSalairesContribuable, true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getSalaireConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsSalairesConjoint)) {
            return JANumberFormatter.fmt(vsSalairesConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    private String getValeurRechercheBD() {
        try {
            if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAffilie")) {
                return this.getVsNumAffilie();
            } else if (getJournalRetour().getZoneRecherche().equalsIgnoreCase("numAvs")) {
                return getVsNumAvsAffilie(0);
            } else {
                return getVsNumCtb();
            }
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getValeurRechercheBD(String zoneRecherche) {
        try {
            if (zoneRecherche.equalsIgnoreCase("numAffilie")) {
                return this.getVsNumAffilie();
            } else if (zoneRecherche.equalsIgnoreCase("numAvs")) {
                return getVsNumAvsAffilie(0);
            } else {
                return getVsNumCtb();
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getVsAdresseAffilie1() {
        return vsAdresseAffilie1;
    }

    public String getVsAdresseAffilie2() {
        return vsAdresseAffilie2;
    }

    public String getVsAdresseAffilie3() {
        return vsAdresseAffilie3;
    }

    public String getVsAdresseAffilie4() {
        return vsAdresseAffilie4;
    }

    public String getVsAdresseConjoint1() {
        return vsAdresseConjoint1;
    }

    public String getVsAdresseConjoint2() {
        return vsAdresseConjoint2;
    }

    public String getVsAdresseConjoint3() {
        return vsAdresseConjoint3;
    }

    public String getVsAdresseConjoint4() {
        return vsAdresseConjoint4;
    }

    public String getVsAdresseCtb1() {
        return vsAdresseCtb1;
    }

    public String getVsAdresseCtb2() {
        return vsAdresseCtb2;
    }

    public String getVsAdresseCtb3() {
        return vsAdresseCtb3;
    }

    public String getVsAdresseCtb4() {
        return vsAdresseCtb4;
    }

    public String getVsAnneeTaxation() {
        return vsAnneeTaxation;
    }

    public String getVsAutresRevenusConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsAutresRevenusConjoint)) {
            return JANumberFormatter.fmt(vsAutresRevenusConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsAutresRevenusCtb() {
        if (!JadeStringUtil.isBlankOrZero(vsAutresRevenusCtb)) {
            return JANumberFormatter.fmt(vsAutresRevenusCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsCapitalPropreEngageEntrepriseConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsCapitalPropreEngageEntrepriseConjoint)) {
            return JANumberFormatter.fmt(vsCapitalPropreEngageEntrepriseConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsCapitalPropreEngageEntrepriseCtb() {
        if (!JadeStringUtil.isBlankOrZero(vsCapitalPropreEngageEntrepriseCtb)) {
            return JANumberFormatter.fmt(vsCapitalPropreEngageEntrepriseCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsCodeTaxation1() {
        return vsCodeTaxation1;
    }

    public String getVsCodeTaxation1Libelle() {
        try {
            int valeur = (Integer.valueOf(getVsCodeTaxation1())).intValue();
            switch (valeur) {
                case 0:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION1_0");
                case 1:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION1_1");
                case 2:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION1_2");
                case 3:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION1_3");
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getVsCodeTaxation2() {
        return vsCodeTaxation2;
    }

    public String getVsCodeTaxation2Code() {
        try {
            int valeur = (Integer.valueOf(getVsCodeTaxation2())).intValue();
            switch (valeur) {
                case 0:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_0");
                case 1:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_1");
                case 2:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_2");
                case 3:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_3");
                case 4:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_4");
                case 5:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_5");
                case 6:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_6");
                case 7:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_7");
                case 8:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_8");
                case 9:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_CODE_9");
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getVsCodeTaxation2Libelle() {
        try {
            int valeur = (Integer.valueOf(getVsCodeTaxation2())).intValue();
            switch (valeur) {
                case 0:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_0");
                case 1:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_1");
                case 2:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_2");
                case 3:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_3");
                case 4:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_4");
                case 5:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_5");
                case 6:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_6");
                case 7:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_7");
                case 8:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_8");
                case 9:
                    return getSession().getLabel("FISC_VS_CODE_TAXATION2_9");
                default:
                    return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String getVsCotisationAvsAffilie() {
        if (!JadeStringUtil.isBlankOrZero(vsCotisationAvsAffilie)) {
            return JANumberFormatter.fmt(vsCotisationAvsAffilie, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsCotisationAvsConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsCotisationAvsConjoint)) {
            return JANumberFormatter.fmt(vsCotisationAvsConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsDateCommunication() {
        return vsDateCommunication;
    }

    public String getVsDateDebutAffiliation() {
        return vsDateDebutAffiliation;
    }

    public String getVsDateDebutAffiliationCaisseProfessionnelle() {
        return vsDateDebutAffiliationCaisseProfessionnelle;
    }

    public String getVsDateDebutAffiliationConjoint() {
        return vsDateDebutAffiliationConjoint;
    }

    public String getVsDateDebutAffiliationConjointCaisseProfessionnelle() {
        return vsDateDebutAffiliationConjointCaisseProfessionnelle;
    }

    public String getVsDateDecesCtb() {
        return vsDateDecesCtb;
    }

    public String getVsDateDemandeCommunication() {
        return vsDateDemandeCommunication;
    }

    public String getVsDateFinAffiliation() {
        return vsDateFinAffiliation;
    }

    public String getVsDateFinAffiliationCaisseProfessionnelle() {
        return vsDateFinAffiliationCaisseProfessionnelle;
    }

    public String getVsDateFinAffiliationConjoint() {
        return vsDateFinAffiliationConjoint;
    }

    public String getVsDateFinAffiliationConjointCaisseProfessionnelle() {
        return vsDateFinAffiliationConjointCaisseProfessionnelle;
    }

    public String getVsDateNaissanceAffilie() {
        return vsDateNaissanceAffilie;
    }

    public String getVsDateNaissanceConjoint() {
        return vsDateNaissanceConjoint;
    }

    public String getVsDateNaissanceCtb() {
        return vsDateNaissanceCtb;
    }

    public String getVsDateTaxation() {
        return vsDateTaxation;
    }

    public String getVsDebutActiviteConjoint() {
        return vsDebutActiviteConjoint;
    }

    public String getVsDebutActiviteCtb() {
        return vsDebutActiviteCtb;
    }

    public String getVsEtatCivilAffilie() {
        return vsEtatCivilAffilie;
    }

    public String getVsEtatCivilCtb() {
        return vsEtatCivilCtb;
    }

    public String getVsFinActiviteConjoint() {
        return vsFinActiviteConjoint;
    }

    public String getVsFinActiviteCtb() {
        return vsFinActiviteCtb;
    }

    public String getVsFortunePriveeConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsFortunePriveeConjoint)) {
            return JANumberFormatter.fmt(vsFortunePriveeConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsFortunePriveeCtb() {
        if (!JadeStringUtil.isBlankOrZero(vsFortunePriveeCtb)) {
            return JANumberFormatter.fmt(vsFortunePriveeCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsLangue() {
        return vsLangue;
    }

    public String getVsLibre3() {
        return vsLibre3;
    }

    public String getVsLibre4() {
        return vsLibre4;
    }

    public String getVsNbJoursTaxation() {
        return vsNbJoursTaxation;
    }

    public String getVsNoCaisseAgenceAffilie(int codeFormat) {
        if (codeFormat == 0) {
            return vsNoCaisseAgenceAffilie;
        } else {
            return CPToolBox.formatCaisseAvs(vsNoCaisseAgenceAffilie);
        }
    }

    public String getVsNoCaisseProfessionnelleAffilie() {
        return vsNoCaisseProfessionnelleAffilie;
    }

    public String getVsNomAffilie() {
        return vsNomAffilie;
    }

    public String getVsNomConjoint() {
        return vsNomConjoint;
    }

    public String getVsNomPrenomContribuableAnnee() {
        return vsNomPrenomContribuableAnnee;
    }

    public String getVsNoPostalLocalite() {
        return vsNoPostalLocalite;
    }

    public String getVsNumAffilie(int codeFormat) {
        try {
            if (codeFormat == 0) {
                return JAStringFormatter.unFormat(vsNumAffilie, "", ".");
            } else {
                TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                IFormatData affilieFormater = app.getAffileFormater();
                if (affilieFormater != null) {
                    return affilieFormater.format(vsNumAffilie);
                }
            }
            return vsNumAffilie;
        } catch (Exception e) {
            return vsNumAffilie;
        }
    }

    public String getVsNumAffilieConjoint(int codeFormat) {
        try {
            if (codeFormat == 0) {
                return JAStringFormatter.unFormat(vsNumAffilieConjoint, "", ".");
            } else {
                TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                IFormatData affilieFormater = app.getAffileFormater();
                if (affilieFormater != null) {
                    return affilieFormater.format(vsNumAffilieConjoint);
                }
            }
            return vsNumAffilieConjoint;
        } catch (Exception e) {
            return vsNumAffilieConjoint;
        }
    }

    public String getVsNumAffilieInterneCaisseProfessionnelle() {
        return vsNumAffilieInterneCaisseProfessionnelle;
    }

    public String getVsNumAffilieInterneConjointCaisseProfessionnelle() {
        return vsNumAffilieInterneConjointCaisseProfessionnelle;
    }

    public String getVsNumAvsAffilie(int codeFormat) {
        try {
            if (codeFormat == 0) {
                return vsNumAvsAffilie;
            } else {
                return NSUtil.formatAVSUnknown(vsNumAvsAffilie);
            }
        } catch (Exception e) {
            return vsNumAvsAffilie;
        }
    }

    public String getVsNumAvsConjoint(int codeFormat) {
        try {
            if (codeFormat == 0) {
                return vsNumAvsConjoint;
            } else {
                return NSUtil.formatAVSUnknown(vsNumAvsConjoint);
            }
        } catch (Exception e) {
            return vsNumAvsConjoint;
        }
    }

    public String getVsNumAvsCtb(int codeFormat) {
        try {
            if (codeFormat == 0) {
                return vsNumAvsCtb;
            } else {
                return NSUtil.formatAVSUnknown(vsNumAvsCtb);
            }
        } catch (Exception e) {
            return vsNumAvsCtb;
        }
    }

    public String getVsNumCaisseAgenceConjoint(int codeFormat) {
        if (codeFormat == 0) {
            return vsNumCaisseAgenceConjoint;
        } else {
            return CPToolBox.formatCaisseAvs(vsNumCaisseAgenceConjoint);
        }
    }

    public String getVsNumCaisseProfessionnelleConjoint() {
        return vsNumCaisseProfessionnelleConjoint;
    }

    /**
     * GETTER AND SETTER
     */
    public String getVsNumCtb() {
        return vsNumCtb;
    }

    public String getVsNumCtbSuivant() {
        return vsNumCtbSuivant;
    }

    public String getVsNumPostalLocaliteConjoint() {
        return vsNumPostalLocaliteConjoint;
    }

    public String getVsNumPostalLocaliteCtb() {
        return vsNumPostalLocaliteCtb;
    }

    public String getVsRachatLpp() {
        if (!JadeStringUtil.isBlankOrZero(vsRachatLpp)) {
            return JANumberFormatter.fmt(vsRachatLpp, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsRachatLppCjt() {
        if (!JadeStringUtil.isBlankOrZero(vsRachatLppCjt)) {
            return JANumberFormatter.fmt(vsRachatLppCjt, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsReserve() {
        return vsReserve;
    }

    public String getVsReserveDateNaissanceConjoint() {
        return vsReserveDateNaissanceConjoint;
    }

    public String getVsReserveFichierImpression() {
        return vsReserveFichierImpression;
    }

    public String getVsReserveTriNumCaisse() {
        return vsReserveTriNumCaisse;
    }

    public String getVsRevenuAgricoleConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuAgricoleConjoint)) {
            return JANumberFormatter.fmt(vsRevenuAgricoleConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsRevenuAgricoleCtb() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuAgricoleCtb)) {
            return JANumberFormatter.fmt(vsRevenuAgricoleCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsRevenuNonAgricoleConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuNonAgricoleConjoint)) {
            return JANumberFormatter.fmt(vsRevenuNonAgricoleConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsRevenuNonAgricoleCtb() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuNonAgricoleCtb)) {
            return JANumberFormatter.fmt(vsRevenuNonAgricoleCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsRevenuRenteConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuRenteConjoint)) {
            return JANumberFormatter.fmt(vsRevenuRenteConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsRevenuRenteCtb() {
        if (!JadeStringUtil.isBlankOrZero(vsRevenuRenteCtb)) {
            return JANumberFormatter.fmt(vsRevenuRenteCtb, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsSalairesConjoint() {
        if (!JadeStringUtil.isBlankOrZero(vsSalairesConjoint)) {
            return JANumberFormatter.fmt(vsSalairesConjoint, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsSalairesContribuable() {
        if (!JadeStringUtil.isBlankOrZero(vsSalairesContribuable)) {
            return JANumberFormatter.fmt(vsSalairesContribuable, true, false, false, 0);
        } else {
            return "0";
        }
    }

    public String getVsSexeAffilie() {
        return vsSexeAffilie;
    }

    public String getVsSexeCtb() {
        return vsSexeCtb;
    }

    public String getVsTypeTaxation() {
        return vsTypeTaxation;
    }

    @Override
    public boolean isForBackup() {
        return super.isForBackup();
    }

    public void setVsAdresseAffilie1(String vsAdresseAffilie1) {
        this.vsAdresseAffilie1 = vsAdresseAffilie1;
    }

    public void setVsAdresseAffilie2(String vsAdresseAffilie2) {
        this.vsAdresseAffilie2 = vsAdresseAffilie2;
    }

    public void setVsAdresseAffilie3(String vsAdresseAffilie3) {
        this.vsAdresseAffilie3 = vsAdresseAffilie3;
    }

    public void setVsAdresseAffilie4(String vsAdresseAffilie4) {
        this.vsAdresseAffilie4 = vsAdresseAffilie4;
    }

    public void setVsAdresseConjoint1(String vsAdresseConjoint1) {
        this.vsAdresseConjoint1 = vsAdresseConjoint1;
    }

    public void setVsAdresseConjoint2(String vsAdresseConjoint2) {
        this.vsAdresseConjoint2 = vsAdresseConjoint2;
    }

    public void setVsAdresseConjoint3(String vsAdresseConjoint3) {
        this.vsAdresseConjoint3 = vsAdresseConjoint3;
    }

    public void setVsAdresseConjoint4(String vsAdresseConjoint4) {
        this.vsAdresseConjoint4 = vsAdresseConjoint4;
    }

    public void setVsAdresseCtb1(String vsAdresseCtb1) {
        this.vsAdresseCtb1 = vsAdresseCtb1;
    }

    public void setVsAdresseCtb2(String vsAdresseCtb2) {
        this.vsAdresseCtb2 = vsAdresseCtb2;
    }

    public void setVsAdresseCtb3(String vsAdresseCtb3) {
        this.vsAdresseCtb3 = vsAdresseCtb3;
    }

    public void setVsAdresseCtb4(String vsAdresseCtb4) {
        this.vsAdresseCtb4 = vsAdresseCtb4;
    }

    public void setVsAnneeTaxation(String vsAnneeTaxation) {
        this.vsAnneeTaxation = vsAnneeTaxation;
    }

    public void setVsAutresRevenusConjoint(String vsAutresRevenusConjoint) {
        this.vsAutresRevenusConjoint = vsAutresRevenusConjoint;
    }

    public void setVsAutresRevenusCtb(String vsAutresRevenusCtb) {
        this.vsAutresRevenusCtb = vsAutresRevenusCtb;
    }

    public void setVsCapitalPropreEngageEntrepriseConjoint(String vsCapitalPropreEngageEntrepriseConjoint) {
        this.vsCapitalPropreEngageEntrepriseConjoint = vsCapitalPropreEngageEntrepriseConjoint;
    }

    public void setVsCapitalPropreEngageEntrepriseCtb(String vsCapitalPropreEngageEntrepriseCtb) {
        this.vsCapitalPropreEngageEntrepriseCtb = vsCapitalPropreEngageEntrepriseCtb;
    }

    public void setVsCodeTaxation1(String vsCodeTaxation1) {
        this.vsCodeTaxation1 = vsCodeTaxation1;
    }

    public void setVsCodeTaxation2(String vsCodeTaxation2) {
        this.vsCodeTaxation2 = vsCodeTaxation2;
    }

    public void setVsCotisationAvsAffilie(String vsCotisationAvsAffilie) {
        this.vsCotisationAvsAffilie = vsCotisationAvsAffilie;
    }

    public void setVsCotisationAvsConjoint(String vsCotisationAvsConjoint) {
        this.vsCotisationAvsConjoint = vsCotisationAvsConjoint;
    }

    public void setVsDateCommunication(String vsDateCommunication) {
        this.vsDateCommunication = vsDateCommunication;
    }

    public void setVsDateDebutAffiliation(String vsDateDebutAffiliation) {
        this.vsDateDebutAffiliation = vsDateDebutAffiliation;
    }

    public void setVsDateDebutAffiliationCaisseProfessionnelle(String vsDateDebutAffiliationCaisseProfessionnelle) {
        this.vsDateDebutAffiliationCaisseProfessionnelle = vsDateDebutAffiliationCaisseProfessionnelle;
    }

    public void setVsDateDebutAffiliationConjoint(String vsDateDebutAffiliationConjoint) {
        this.vsDateDebutAffiliationConjoint = vsDateDebutAffiliationConjoint;
    }

    public void setVsDateDebutAffiliationConjointCaisseProfessionnelle(
            String vsDateDebutAffiliationConjointCaisseProfessionnelle) {
        this.vsDateDebutAffiliationConjointCaisseProfessionnelle = vsDateDebutAffiliationConjointCaisseProfessionnelle;
    }

    public void setVsDateDecesCtb(String vsDateDecesCtb) {
        this.vsDateDecesCtb = vsDateDecesCtb;
    }

    public void setVsDateDemandeCommunication(String vsDateDemandeCommunication) {
        this.vsDateDemandeCommunication = vsDateDemandeCommunication;
    }

    public void setVsDateFinAffiliation(String vsDateFinAffiliation) {
        this.vsDateFinAffiliation = vsDateFinAffiliation;
    }

    public void setVsDateFinAffiliationCaisseProfessionnelle(String vsDateFinAffiliationCaisseProfessionnelle) {
        this.vsDateFinAffiliationCaisseProfessionnelle = vsDateFinAffiliationCaisseProfessionnelle;
    }

    public void setVsDateFinAffiliationConjoint(String vsDateFinAffiliationConjoint) {
        this.vsDateFinAffiliationConjoint = vsDateFinAffiliationConjoint;
    }

    public void setVsDateFinAffiliationConjointCaisseProfessionnelle(
            String vsDateFinAffiliationConjointCaisseProfessionnelle) {
        this.vsDateFinAffiliationConjointCaisseProfessionnelle = vsDateFinAffiliationConjointCaisseProfessionnelle;
    }

    public void setVsDateNaissanceAffilie(String vsDateNaissanceAffilie) {
        this.vsDateNaissanceAffilie = vsDateNaissanceAffilie;
    }

    public void setVsDateNaissanceConjoint(String vsDateNaissanceConjoint) {
        this.vsDateNaissanceConjoint = vsDateNaissanceConjoint;
    }

    public void setVsDateNaissanceCtb(String vsDateNaissanceCtb) {
        this.vsDateNaissanceCtb = vsDateNaissanceCtb;
    }

    public void setVsDateTaxation(String vsDateTaxation) {
        this.vsDateTaxation = vsDateTaxation;
    }

    public void setVsDebutActiviteConjoint(String vsDebutActiviteConjoint) {
        this.vsDebutActiviteConjoint = vsDebutActiviteConjoint;
    }

    public void setVsDebutActiviteCtb(String vsDebutActiviteCtb) {
        this.vsDebutActiviteCtb = vsDebutActiviteCtb;
    }

    public void setVsEtatCivilAffilie(String vsEtatCivilAffilie) {
        this.vsEtatCivilAffilie = vsEtatCivilAffilie;
    }

    public void setVsEtatCivilCtb(String vsEtatCivilCtb) {
        this.vsEtatCivilCtb = vsEtatCivilCtb;
    }

    public void setVsFinActiviteConjoint(String vsFinActiviteConjoint) {
        this.vsFinActiviteConjoint = vsFinActiviteConjoint;
    }

    public void setVsFinActiviteCtb(String vsFinActiviteCtb) {
        this.vsFinActiviteCtb = vsFinActiviteCtb;
    }

    public void setVsFortunePriveeConjoint(String vsFortunePriveeConjoint) {
        this.vsFortunePriveeConjoint = vsFortunePriveeConjoint;
    }

    public void setVsFortunePriveeCtb(String vsFortunePriveeCtb) {
        this.vsFortunePriveeCtb = vsFortunePriveeCtb;
    }

    public void setVsLangue(String vsLangue) {
        this.vsLangue = vsLangue;
    }

    public void setVsLibre3(String vsLibre3) {
        this.vsLibre3 = vsLibre3;
    }

    public void setVsLibre4(String vsLibre4) {
        this.vsLibre4 = vsLibre4;
    }

    public void setVsNbJoursTaxation(String vsNbJoursTaxation) {
        this.vsNbJoursTaxation = vsNbJoursTaxation;
    }

    public void setVsNoCaisseAgenceAffilie(String vsNoCaisseAgenceAffilie) {
        this.vsNoCaisseAgenceAffilie = vsNoCaisseAgenceAffilie;
    }

    public void setVsNoCaisseProfessionnelleAffilie(String vsNoCaisseProfessionnelleAffilie) {
        this.vsNoCaisseProfessionnelleAffilie = vsNoCaisseProfessionnelleAffilie;
    }

    public void setVsNomAffilie(String vsNomAffilie) {
        this.vsNomAffilie = vsNomAffilie;
    }

    public void setVsNomConjoint(String vsNomConjoint) {
        this.vsNomConjoint = vsNomConjoint;
    }

    public void setVsNomPrenomContribuableAnnee(String vsNomPrenomContribuableAnnee) {
        this.vsNomPrenomContribuableAnnee = vsNomPrenomContribuableAnnee;
    }

    public void setVsNoPostalLocalite(String vsNoPostalLocalite) {
        this.vsNoPostalLocalite = vsNoPostalLocalite;
    }

    @Override
    public void setVsNumAffilie(String vsNumAffilie) {
        this.vsNumAffilie = vsNumAffilie;
    }

    public void setVsNumAffilieConjoint(String vsNumAffilieConjoint) {
        this.vsNumAffilieConjoint = vsNumAffilieConjoint;
    }

    public void setVsNumAffilieInterneCaisseProfessionnelle(String vsNumAffilieInterneCaisseProfessionnelle) {
        this.vsNumAffilieInterneCaisseProfessionnelle = vsNumAffilieInterneCaisseProfessionnelle;
    }

    public void setVsNumAffilieInterneConjointCaisseProfessionnelle(
            String vsNumAffilieInterneConjointCaisseProfessionnelle) {
        this.vsNumAffilieInterneConjointCaisseProfessionnelle = vsNumAffilieInterneConjointCaisseProfessionnelle;
    }

    public void setVsNumAvsAffilie(String vsNumAvsAffilie) {
        this.vsNumAvsAffilie = vsNumAvsAffilie;
    }

    public void setVsNumAvsConjoint(String vsNumAvsConjoint) {
        this.vsNumAvsConjoint = vsNumAvsConjoint;
    }

    public void setVsNumAvsCtb(String vsNumAvsCtb) {
        this.vsNumAvsCtb = vsNumAvsCtb;
    }

    public void setVsNumCaisseAgenceConjoint(String vsNumCaisseAgenceConjoint) {
        this.vsNumCaisseAgenceConjoint = vsNumCaisseAgenceConjoint;
    }

    public void setVsNumCaisseProfessionnelleConjoint(String vsNumCaisseProfessionnelleConjoint) {
        this.vsNumCaisseProfessionnelleConjoint = vsNumCaisseProfessionnelleConjoint;
    }

    public void setVsNumCtb(String vsNumCtb) {
        this.vsNumCtb = vsNumCtb;
    }

    public void setVsNumCtbSuivant(String vsNumCtbSuivant) {
        this.vsNumCtbSuivant = vsNumCtbSuivant;
    }

    public void setVsNumPostalLocaliteConjoint(String vsNumPostalLocaliteConjoint) {
        this.vsNumPostalLocaliteConjoint = vsNumPostalLocaliteConjoint;
    }

    public void setVsNumPostalLocaliteCtb(String vsNumPostalLocaliteCtb) {
        this.vsNumPostalLocaliteCtb = vsNumPostalLocaliteCtb;
    }

    public void setVsRachatLpp(String vsRachatLpp) {
        this.vsRachatLpp = vsRachatLpp;
    }

    public void setVsRachatLppCjt(String vsRachatLppCjt) {
        this.vsRachatLppCjt = vsRachatLppCjt;
    }

    public void setVsReserve(String vsReserve) {
        this.vsReserve = vsReserve;
    }

    public void setVsReserveDateNaissanceConjoint(String vsReserveDateNaissanceConjoint) {
        this.vsReserveDateNaissanceConjoint = vsReserveDateNaissanceConjoint;
    }

    public void setVsReserveFichierImpression(String vsReserveFichierImpression) {
        this.vsReserveFichierImpression = vsReserveFichierImpression;
    }

    public void setVsReserveTriNumCaisse(String vsReserveTriNumCaisse) {
        this.vsReserveTriNumCaisse = vsReserveTriNumCaisse;
    }

    public void setVsRevenuAgricoleConjoint(String vsRevenuAgricoleConjoint) {
        this.vsRevenuAgricoleConjoint = vsRevenuAgricoleConjoint;
    }

    public void setVsRevenuAgricoleCtb(String vsRevenuAgricoleCtb) {
        this.vsRevenuAgricoleCtb = vsRevenuAgricoleCtb;
    }

    public void setVsRevenuNonAgricoleConjoint(String vsRevenuNonAgricoleConjoint) {
        this.vsRevenuNonAgricoleConjoint = vsRevenuNonAgricoleConjoint;
    }

    public void setVsRevenuNonAgricoleCtb(String vsRevenuNonAgricoleCtb) {
        this.vsRevenuNonAgricoleCtb = vsRevenuNonAgricoleCtb;
    }

    public void setVsRevenuRenteConjoint(String vsRevenuRenteConjoint) {
        this.vsRevenuRenteConjoint = vsRevenuRenteConjoint;
    }

    public void setVsRevenuRenteCtb(String vsRevenuRenteCtb) {
        this.vsRevenuRenteCtb = vsRevenuRenteCtb;
    }

    public void setVsSalairesConjoint(String vsSalairesConjoint) {
        this.vsSalairesConjoint = vsSalairesConjoint;
    }

    public void setVsSalairesContribuable(String vsSalairesContribuable) {
        this.vsSalairesContribuable = vsSalairesContribuable;
    }

    public void setVsSexeAffilie(String vsSexeAffilie) {
        this.vsSexeAffilie = vsSexeAffilie;
    }

    public void setVsSexeCtb(String vsSexeCtb) {
        this.vsSexeCtb = vsSexeCtb;
    }

    public void setVsTypeTaxation(String vsTypeTaxation) {
        this.vsTypeTaxation = vsTypeTaxation;
    }
}
