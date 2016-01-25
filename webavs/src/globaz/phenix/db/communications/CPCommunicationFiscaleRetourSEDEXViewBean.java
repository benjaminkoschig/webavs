package globaz.phenix.db.communications;

import globaz.commons.nss.NSUtil;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BEntity;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAStringFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.process.communications.CPProcessValiderPlausibilite;
import globaz.phenix.toolbox.CPToolBox;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import java.math.BigDecimal;

public class CPCommunicationFiscaleRetourSEDEXViewBean extends CPCommunicationFiscaleRetourViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static String REMARK_REV_ACT_IDP = "revActIdp";
    public static String REMARK_REV_NET = "revNet";
    private String codeSexe = "";
    private CPSedexDonneesBase donneeBase = null;
    private CPSedexContribuable donneeContribuable = null;
    private String etatCivil = "";
    private String nssPourRecherche = "";
    private String numAvsFisc = "";

    /**
     * Commentaire relatif au constructeur CPDonneesBase
     */
    public CPCommunicationFiscaleRetourSEDEXViewBean() {
        super();
    }

    @Override
    protected void _afterRetrieve(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // setValeurChampRecherche(getValeurRechercheBD());
        super._afterRetrieve(transaction);
        if (isWantDonneeBase()) {
            donneeBase = new CPSedexDonneesBase();
            donneeBase.setIdRetour(getIdRetour());
            donneeBase.retrieve(transaction);
        }
        if (isWantDonneeContribuable()) {
            donneeContribuable = new CPSedexContribuable();
            donneeContribuable.setIdRetour(getIdRetour());
            donneeContribuable.retrieve(transaction);
        }
    }

    @Override
    protected boolean _autoInherits() {
        return false;
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

    @Override
    protected void _copyPointersFromEntity(BEntity entity) throws Exception {
        // TODO checker si entity est du bon type avant de caster
        setForBackup(((CPCommunicationFiscaleRetourViewBean) entity).isForBackup());
        super.setForBackup(((CPCommunicationFiscaleRetourViewBean) entity).isForBackup());
        super._copyPointersFromEntity(entity);
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPSECON";
        String table2 = "CPCRETP";
        if (isForBackup()) {
            table1 = "CPSECOB";
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
            return "CPCRETP";
        } else {
            return "CPCRETB";
        }
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires) Les reception sont toujours chargée, elle
     * seront validées par les plausibilités
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        setValeurChampRecherche(getNssPourRecherche());
        if (isWantPlausibilite()) {
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
    }

    /*
     * private String getValeurRechercheBD() { try { return NSUtil.formatAVSUnknown(); } catch (Exception e) { return
     * ""; } }
     */
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

        super._writeProperties(statement);
    }

    @Override
    public String getAutreRevenu() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getOASIBridgingPension();
        } else {
            return "";
        }
    }

    @Override
    public String getAutreRevenuConjoint() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getOASIBridgingPensionCjt();
        } else {
            return "";
        }
    }

    @Override
    public String getCapitalEntrepriseConjoint() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getCapitalCjt();
        } else {
            return "0";
        }
    }

    @Override
    public String getCodeCanton() {
        // une fois que tout le monde est sous sedex => supprimer ces héritages et mettre dans le manager principale
        CPSedexContribuable seContri = new CPSedexContribuable();
        seContri.setSession(getSession());
        seContri.setIdRetour(getIdRetour());
        try {
            seContri.retrieve(false);
            return seContri.getSenderId().substring(2, 4);
        } catch (Exception e) {
            seContri = null;
        }
        return "";
    }

    @Override
    public String getCodeSexe() {
        String libSex = "";
        if (codeSexe.equalsIgnoreCase("1")) {
            libSex = IConstantes.CS_PERSONNE_SEXE_HOMME;
        } else if (codeSexe.equalsIgnoreCase("2")) {
            libSex = IConstantes.CS_PERSONNE_SEXE_FEMME;
        }
        return libSex;
    }

    @Override
    public String getDescription(int cas) {

        CPSedexContribuable seContri = new CPSedexContribuable();
        seContri.setSession(getSession());
        seContri.setIdRetour(getIdRetour());
        try {
            seContri.retrieve(false);
        } catch (Exception e) {
            seContri = null;
        }
        String nss = "";
        if (!seContri.isNew() && !JadeStringUtil.isBlankOrZero(seContri.getVn())) {
            if (NSUtil.unFormatAVS(seContri.getVn().trim()).length() == 13) {
                nss = NSUtil.formatAVSNewNum(seContri.getVn().trim());
            } else if (NSUtil.unFormatAVS(seContri.getVn().trim()).length() == 11) {
                nss = JAStringFormatter.formatAVS(seContri.getVn().trim());
            } else {
                nss = seContri.getVn().trim();
            }
            setNumAvsFisc(nss);
            setEtatCivil(seContri.getMaritalStatus());
            setCodeSexe(seContri.getSex());
        }

        CPSedexConjoint seCjt = new CPSedexConjoint();
        seCjt.setSession(getSession());
        seCjt.setIdRetour(getIdRetour());
        try {
            seCjt.retrieve(false);
        } catch (Exception e) {
            seCjt = null;
        }
        String nssCjt = "";
        if (!JadeStringUtil.isBlankOrZero(seCjt.getVn())) {
            if (NSUtil.unFormatAVS(seCjt.getVn().trim()).length() == 13) {
                nssCjt = NSUtil.formatAVSNewNum(seCjt.getVn().trim());
            } else if (NSUtil.unFormatAVS(seCjt.getVn().trim()).length() == 11) {
                nssCjt = JAStringFormatter.formatAVS(seCjt.getVn().trim());
            } else {
                nssCjt = seContri.getOurBusinessReferenceID().trim();
            }
            if (JadeStringUtil.isEmpty(nss)) {
                setNumAvsFisc(nssCjt);
            }
            if (seContri.isNew() || JadeStringUtil.isEmpty(seContri.getSex())) {
                setCodeSexe(seCjt.getSex());
            }
            if (seContri.isNew() || JadeStringUtil.isEmpty(seContri.getMaritalStatus())) {
                setEtatCivil(seCjt.getMaritalStatus());
            }
        }

        String numAffilie = "";
        if (!JadeStringUtil.isBlankOrZero(seContri.getYourBusinessReferenceId())) {
            try {
                TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                IFormatData affilieFormater = app.getAffileFormater();
                if (affilieFormater != null) {
                    numAffilie = affilieFormater.format(seContri.getYourBusinessReferenceId());
                } else {
                    numAffilie = seContri.getYourBusinessReferenceId();
                }
            } catch (Exception e) {
                numAffilie = seContri.getYourBusinessReferenceId();
            }
        }

        String description = "";

        if (cas == 0) {
            if (!JadeStringUtil.isBlankOrZero(numAffilie)) {
                description += getSession().getLabel("NUM_AFFILIE") + " : " + numAffilie;
                description += "\n";
            }
            if (!JadeStringUtil.isBlankOrZero(seContri.getOfficialName())
                    || !JadeStringUtil.isBlankOrZero(seContri.getFirstName())) {
                description += seContri.getOfficialName() + " " + seContri.getFirstName() + "\n";
            }

            if (!JadeStringUtil.isBlank(seContri.getAddressLine1())
                    && !seContri.getAddressLine1().trim()
                            .equalsIgnoreCase(seContri.getOfficialName().trim() + " " + seContri.getFirstName().trim())) {
                description += seContri.getAddressLine1() + "\n";
            }
            if (!JadeStringUtil.isBlank(seContri.getAddressLine2())
                    && !seContri.getAddressLine2().trim().equalsIgnoreCase(seContri.getOfficialName().trim())) {
                description += seContri.getAddressLine2() + "\n";
            }

            if (!JadeStringUtil.isBlank(seContri.getStreet()) || !JadeStringUtil.isBlank(seContri.getHouseNumber())) {
                description += seContri.getStreet() + seContri.getHouseNumber() + "\n";
            }
            if (!JadeStringUtil.isBlank(seContri.getTown()) || !JadeStringUtil.isBlank(seContri.getCountry())) {
                if (!JadeStringUtil.isBlank(seContri.getCountry())) {
                    description += seContri.getCountry() + " ";
                }
                description += seContri.getSwissZipCode() + "  " + seContri.getTown();

                description += "\n";
            }
            description += getSession().getLabel("YEARMONTHDAY") + " : ";
            if (!JadeStringUtil.isBlankOrZero(seContri.getYearMonthDay())) {
                description += CPToolBox.formatDate(seContri.getYearMonthDay(), 2) + "\n";
            }

        } else if (cas == 1) {
            if (!JadeStringUtil.isBlankOrZero(nss)) {
                description = nss;
            } else {
                description = nssCjt;
            }
        } else if (cas == 2) {
            if (!JadeStringUtil.isBlankOrZero(numAffilie)) {
                description = numAffilie;
            } else {
                description = nss;
            }
        } else if (cas == 3) {
            if (!JadeStringUtil.isBlankOrZero(nss)) {
                description = nss;
            } else {
                description = nssCjt;
            }
        } else if (cas == 4) {
            if (!JadeStringUtil.isBlankOrZero(nss)) {
                description = nss + seContri.getYearMonthDay() + "\n";
            }
            if (!JadeStringUtil.isBlankOrZero(seContri.getOfficialName())
                    || !JadeStringUtil.isBlankOrZero(seContri.getFirstName())) {
                description += seContri.getOfficialName() + " " + seContri.getFirstName() + "\n";
            }

            if (!JadeStringUtil.isBlank(seContri.getAddressLine1())
                    && !seContri.getAddressLine1().trim()
                            .equalsIgnoreCase(seContri.getOfficialName().trim() + " " + seContri.getFirstName().trim())) {
                description += seContri.getAddressLine1() + "\n";
            }
            if (!JadeStringUtil.isBlank(seContri.getAddressLine2())
                    && !seContri.getAddressLine2().trim().equalsIgnoreCase(seContri.getOfficialName().trim())) {
                description += seContri.getAddressLine2() + "\n";
            }

            if (!JadeStringUtil.isBlank(seContri.getStreet()) || !JadeStringUtil.isBlank(seContri.getHouseNumber())) {
                description += seContri.getStreet() + seContri.getHouseNumber() + "\n";
            }
            if (!JadeStringUtil.isBlank(seContri.getTown()) || !JadeStringUtil.isBlank(seContri.getCountry())) {
                if (!JadeStringUtil.isBlank(seContri.getCountry())) {
                    description += seContri.getCountry() + " ";
                }
                description += seContri.getSwissZipCode() + "  " + seContri.getTown();
                description += "\n";
            }
        }
        return description;
    }

    /*
     * public String getDescription() { if(getJuNewNumContribuable().length()>0) description +=
     * getSession().getLabel("NUM_CONTRIBUABLE")+" : "+getJuNewNumContribuable ()+"\n"; return description; }
     */

    @Override
    public String getEtatCivil() {
        String code = "";
        if (etatCivil.equalsIgnoreCase("1")) {
            code = "515001";
        } else if (etatCivil.equalsIgnoreCase("2")) {
            code = "515002";
        } else if (etatCivil.equalsIgnoreCase("3")) {
            code = "515004";
        } else if (etatCivil.equalsIgnoreCase("4")) {
            code = "515003";
        } else if (etatCivil.equalsIgnoreCase("5")) {
            code = "515005";
        } else if (etatCivil.equalsIgnoreCase("6")) {
            code = "515007";
        } else if (etatCivil.equalsIgnoreCase("7")) {
            code = "515008";
        }
        return code;
    }

    public String getNssPourRecherche() {
        return nssPourRecherche;
    }

    @Override
    public String getNumAvsFisc(int codeFormat) {
        return numAvsFisc;
    }

    @Override
    public String getRachatLpp() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getPurchasingLPP();
        } else {
            return "0";
        }
    }

    @Override
    public String getRachatLppCjt() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getPurchasingLPPCjt();
        } else {
            return "0";
        }
    }

    @Override
    public String getRemarque() {
        if ((donneeContribuable != null) && !donneeContribuable.isNew()) {
            return donneeContribuable.getRemark();
        } else {
            return "";
        }
    }

    @Override
    public String getRevenuA() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getMainIncomeInAgriculture();
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuAConjoint() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getMainIncomeInAgricultureCjt();
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuNA() {
        double revenuTotal = 0.0;
        if ((donneeBase != null) && !donneeBase.isNew()) {
            String revenuNetSalarie = JANumberFormatter.deQuote(donneeBase.getEmploymentIncome());
            String revenuAVS = JANumberFormatter.deQuote(donneeBase.getIncomeFromSelfEmployment());
            String revenuAgricole = JANumberFormatter.deQuote(donneeBase.getMainIncomeInAgriculture());
            if (!JadeStringUtil.isEmpty(revenuNetSalarie) && !revenuNetSalarie.equalsIgnoreCase("null")) {
                revenuTotal += Double.valueOf(revenuNetSalarie).doubleValue();
            }
            if (!JadeStringUtil.isEmpty(revenuAVS) && !revenuAVS.equalsIgnoreCase("null")) {
                revenuTotal += Double.valueOf(revenuAVS).doubleValue();
            }
            if (!JadeStringUtil.isEmpty(revenuAgricole) && !revenuAgricole.equalsIgnoreCase("null")) {
                revenuTotal -= Double.valueOf(JANumberFormatter.deQuote(revenuAgricole)).doubleValue();
            }
        }
        if (revenuTotal != 0) {
            return JANumberFormatter.fmt(Double.toString(revenuTotal), true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuNAConjoint() {
        double revenuTotal = 0.0;
        if ((donneeBase != null) && !donneeBase.isNew()) {
            String revenuNetSalarie = JANumberFormatter.deQuote(donneeBase.getEmploymentIncomeCjt());
            String revenuAVS = JANumberFormatter.deQuote(donneeBase.getIncomeFromSelfEmploymentCjt());
            String revenuAgricole = JANumberFormatter.deQuote(donneeBase.getMainIncomeInAgricultureCjt());
            if (!JadeStringUtil.isEmpty(revenuNetSalarie) && !revenuNetSalarie.equalsIgnoreCase("null")) {
                revenuTotal += Double.valueOf(revenuNetSalarie).doubleValue();
            }
            if (!JadeStringUtil.isEmpty(revenuAVS) && !revenuAVS.equalsIgnoreCase("null")) {
                revenuTotal += Double.valueOf(revenuAVS).doubleValue();
            }
            if (!JadeStringUtil.isEmpty(revenuAgricole) && !revenuAgricole.equalsIgnoreCase("null")) {
                revenuTotal -= Double.valueOf(JANumberFormatter.deQuote(revenuAgricole)).doubleValue();
            }
        }
        if (revenuTotal != 0) {
            return JANumberFormatter.fmt(Double.toString(revenuTotal), true, false, false, 0);
        } else {
            return "0";
        }
    }

    @Override
    public String getRevenuR() {
        BigDecimal rente = new BigDecimal(0);
        if ((donneeBase != null) && !donneeBase.isNew()) {

            if (!JadeStringUtil.isBlank(donneeBase.getPensionIncome())) {
                rente = rente.add(new BigDecimal(JANumberFormatter.deQuote(donneeBase.getPensionIncome())));
            }
            if (!JadeStringUtil.isBlank(donneeBase.getPensionIncomeCjt())) {
                rente = rente.add(new BigDecimal(JANumberFormatter.deQuote(donneeBase.getPensionIncomeCjt())));
            }
        }
        return JANumberFormatter.fmt(rente.toString(), true, false, false, 0);
    }

    public String getRevenuRConjoint() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getPensionIncomeCjt();
        } else {
            return "0";
        }
    }

    @Override
    public String getSalaire() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getEmploymentIncome();
        } else {
            return "0";
        }
    }

    @Override
    public String getSalaireConjoint() {
        if ((donneeBase != null) && !donneeBase.isNew()) {
            return donneeBase.getEmploymentIncomeCjt();
        } else {
            return "0";
        }
    }

    @Override
    public boolean hasSpy() {
        if (isForRetourOriginale()) {
            return false;
        } else {
            return super.hasSpy();
        }
    }

    public void setCodeSexe(String codeSexe) {
        this.codeSexe = codeSexe;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public void setNssPourRecherche(String nssPourRecherche) {
        this.nssPourRecherche = nssPourRecherche;
    }

    public void setNumAvsFisc(String numAvsFisc) {
        this.numAvsFisc = numAvsFisc;
    }
}
