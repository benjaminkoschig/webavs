package globaz.pavo.util;

import globaz.globall.db.BStatement;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;

public class CIAffilie extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public final static String CS_ASSURANCE_FAC = "804009";

    public final static String CS_EMPLOYEUR = "804002";
    // code systèmes pour le type d'affiliation
    public final static String CS_INDEPENDANT = "804001";
    public final static String CS_INDEPENDANT_EMPLOYEUR = "804005";
    public final static String CS_NA_SELON_ART_1 = "804006";
    public final static String CS_NON_ACTIF = "804004";
    public final static String CS_TSE = "804008";
    private globaz.pyxis.db.tiers.TITiersViewBean _tiers;
    private java.lang.String afCaisseExterne = new String();
    private java.lang.String affiliationId = new String();
    private java.lang.String affilieNumero = new String();
    private java.lang.String affilieNumeroAncien = new String();
    private java.lang.String afNumeroExterne = new String();
    private java.lang.String avsCaisseExterne = new String();
    private java.lang.String avsNumeroExterne = new String();
    private java.lang.Boolean bonusMalus = new Boolean(false);
    private java.lang.String brancheEconomique = new String();
    private java.lang.String caissePartance = new String();
    private java.lang.String caisseProvenance = new String();
    private FWParametersSystemCode csBrancheEconomique = null;
    private FWParametersSystemCode csDeclarationSalaire = null;
    private FWParametersSystemCode csMembreAssociation = null;
    private FWParametersSystemCode csMembreComite = null;
    private FWParametersSystemCode csMotifFin = null;
    private FWParametersSystemCode csPeriodicite = null;
    private FWParametersSystemCode csPersonaliteJuridique = null;
    private FWParametersSystemCode csTypeAffiliation = null;
    private java.lang.String dateDebut = new String();
    private java.lang.String dateEditionFiche = new String();
    private java.lang.String dateEditionFicheM1 = new String();
    private java.lang.String dateEditionFicheM2 = new String();
    private java.lang.String dateFin = new String();
    private java.lang.String declarationSalaire = new String();
    private java.lang.String derniereAffiliation = new String();
    private java.lang.Boolean exonerationGenerale = new Boolean(false);
    private java.lang.Boolean irrecouvrable = new Boolean(false);
    private java.lang.String laaCaisse = new String();
    private java.lang.String laaNumero = new String();

    private java.lang.Boolean liquidation = new Boolean(false);
    private java.lang.String lppCaisse = new String();
    private java.lang.String lppNumero = new String();
    private java.lang.String masseAnnuelle = new String();
    private java.lang.String massePeriodicite = new String();
    private java.lang.String membreAssociation = new String();
    private java.lang.String membreComite = new String();
    private java.lang.String motifCreation = new String();

    private java.lang.String motifFin = new String();
    private java.lang.String numeroIDE = new String();
    private java.lang.Boolean occasionnel = new Boolean(false);
    private java.lang.String periodicite = new String();
    private java.lang.String personnaliteJuridique = new String();
    private java.lang.Boolean personnelMaison = new Boolean(false);
    private java.lang.Boolean releveParitaire = new Boolean(false);
    private java.lang.Boolean relevePersonnel = new Boolean(false);
    private java.lang.String selection = new String();
    private java.lang.String taxeCo2Fraction = new String();
    private java.lang.String taxeCo2Taux = new String();
    private java.lang.String tiersId;
    private java.lang.Boolean traitement = new Boolean(false);
    private java.lang.String typeAffiliation = new String();

    /**
     * Commentaire relatif au constructeur VEAffiliation
     */
    public CIAffilie() {
        super();
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "AFAFFIP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        tiersId = statement.dbReadNumeric("HTITIE");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        affilieNumero = statement.dbReadString("MALNAF");
        dateDebut = statement.dbReadDateAMJ("MADDEB");
        dateFin = statement.dbReadDateAMJ("MADFIN");
        motifFin = statement.dbReadNumeric("MATMOT");
        typeAffiliation = statement.dbReadNumeric("MATTAF");
        motifCreation = statement.dbReadNumeric("MATMCR");
        bonusMalus = statement.dbReadBoolean("MABBMA");
        brancheEconomique = statement.dbReadNumeric("MATBRA");
        personnaliteJuridique = statement.dbReadNumeric("MATJUR");
        exonerationGenerale = statement.dbReadBoolean("MABEXO");
        dateEditionFiche = statement.dbReadDateAMJ("MADFIC");
        dateEditionFicheM1 = statement.dbReadNumeric("MADFI1");
        dateEditionFicheM2 = statement.dbReadNumeric("MADFI2");
        declarationSalaire = statement.dbReadNumeric("MATDEC");
        membreAssociation = statement.dbReadNumeric("MATMAS");
        membreComite = statement.dbReadNumeric("MATMCO");
        irrecouvrable = statement.dbReadBoolean("MABIRR");
        occasionnel = statement.dbReadBoolean("MABOCC");
        personnelMaison = statement.dbReadBoolean("MABMAI");
        liquidation = statement.dbReadBoolean("MABLIQ");
        traitement = statement.dbReadBoolean("MABTRA");
        releveParitaire = statement.dbReadBoolean("MABREP");
        relevePersonnel = statement.dbReadBoolean("MABREI");
        massePeriodicite = statement.dbReadNumeric("MAMMAP", 2);
        masseAnnuelle = statement.dbReadNumeric("MAMMAA", 2);
        periodicite = statement.dbReadNumeric("MATPER");
        avsCaisseExterne = statement.dbReadNumeric("MAIAVS");
        avsNumeroExterne = statement.dbReadString("MALAVS");
        afCaisseExterne = statement.dbReadNumeric("MAIAFA");
        afNumeroExterne = statement.dbReadString("MALAFA");
        laaCaisse = statement.dbReadNumeric("MAILAA");
        laaNumero = statement.dbReadString("MALLAA");
        lppCaisse = statement.dbReadNumeric("MAILPP");
        lppNumero = statement.dbReadString("MALLPP");
        numeroIDE = statement.dbReadString("MALFED");
        taxeCo2Taux = statement.dbReadNumeric("MAMTCO", 5);
        taxeCo2Fraction = statement.dbReadNumeric("MAMFCO", 5);
        affilieNumeroAncien = statement.dbReadString("MALNAA");
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("MAIAFF", _dbWriteNumeric(statement.getTransaction(), getAffiliationId(), ""));
        statement.writeKey("HTITIE", _dbWriteNumeric(statement.getTransaction(), getTiersId(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("HTITIE", _dbWriteNumeric(statement.getTransaction(), getTiersId(), "tiersId"));
        statement
                .writeField("MAIAFF", _dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "affiliationId"));
        statement.writeField("MALNAF", _dbWriteString(statement.getTransaction(), getAffilieNumero(), "affilieNumero"));
        statement.writeField("MADDEB", _dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField("MADFIN", _dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
        statement.writeField("MATMOT", _dbWriteNumeric(statement.getTransaction(), getMotifFin(), "motifFin"));
        statement.writeField("MATTAF",
                _dbWriteNumeric(statement.getTransaction(), getTypeAffiliation(), "typeaffiliation"));
        statement
                .writeField("MATMCR", _dbWriteNumeric(statement.getTransaction(), getMotifCreation(), "motifCreation"));
        statement.writeField("MABBMA", _dbWriteBoolean(statement.getTransaction(), getBonusMalus(), "bonusMalus"));
        statement.writeField("MATBRA",
                _dbWriteNumeric(statement.getTransaction(), getBrancheEconomique(), "brancheEconomique"));
        statement.writeField("MATJUR",
                _dbWriteNumeric(statement.getTransaction(), getPersonnaliteJuridique(), "personnaliteJuridique"));
        statement.writeField("MABEXO",
                _dbWriteBoolean(statement.getTransaction(), isExonerationGenerale(), "exonerationGenerale"));
        statement.writeField("MADFIC",
                _dbWriteDateAMJ(statement.getTransaction(), getDateEditionFiche(), "dateEditionFiche"));
        statement.writeField("MADFI1",
                _dbWriteTime(statement.getTransaction(), getDateEditionFicheM1(), "dateEditionFicheM1"));
        statement.writeField("MADFI2",
                _dbWriteNumeric(statement.getTransaction(), getDateEditionFicheM2(), "dateEditionFicheM2"));
        statement.writeField("MATDEC",
                _dbWriteNumeric(statement.getTransaction(), getDeclarationSalaire(), "declarationSalaire"));
        statement.writeField("MATMAS",
                _dbWriteNumeric(statement.getTransaction(), getMembreAssociation(), "membreAssociation"));
        statement.writeField("MATMCO", _dbWriteNumeric(statement.getTransaction(), getMembreComite(), "membreComite"));
        statement.writeField("MABIRR", _dbWriteBoolean(statement.getTransaction(), isIrrecouvrable(), "irrecouvrable"));
        statement.writeField("MABOCC", _dbWriteBoolean(statement.getTransaction(), isOccasionnel(), "occasionnel"));
        statement.writeField("MABMAI",
                _dbWriteBoolean(statement.getTransaction(), isPersonnelMaison(), "personnelMaison"));
        statement.writeField("MABLIQ", _dbWriteBoolean(statement.getTransaction(), isLiquidation(), "liquidation"));
        statement.writeField("MABTRA", _dbWriteBoolean(statement.getTransaction(), isTraitement(), "traitement"));
        statement.writeField("MABREP",
                _dbWriteBoolean(statement.getTransaction(), isReleveParitaire(), "releveParitaire"));
        statement.writeField("MABREI",
                _dbWriteBoolean(statement.getTransaction(), isRelevePersonnel(), "relevePersonnel"));
        statement.writeField("MAMMAP",
                _dbWriteNumeric(statement.getTransaction(), getMassePeriodicite(), "massePeriodicite"));
        statement
                .writeField("MAMMAA", _dbWriteNumeric(statement.getTransaction(), getMasseAnnuelle(), "masseAnnuelle"));
        statement.writeField("MATPER", _dbWriteNumeric(statement.getTransaction(), getPeriodicite(), "periodicite"));
        statement.writeField("MAIAVS",
                _dbWriteNumeric(statement.getTransaction(), getAvsCaisseExterne(), "avsCaisseExterne"));
        statement.writeField("MALAVS",
                _dbWriteString(statement.getTransaction(), getAvsNumeroExterne(), "avsNumeroExterne"));
        statement.writeField("MAIAFA",
                _dbWriteNumeric(statement.getTransaction(), getAfCaisseExterne(), "afCaisseExterne"));
        statement.writeField("MALAFA",
                _dbWriteString(statement.getTransaction(), getAfNumeroExterne(), "afNumeroExterne"));
        statement.writeField("MAILAA", _dbWriteNumeric(statement.getTransaction(), getLaaCaisse(), "laaCaisse"));
        statement.writeField("MALLAA", _dbWriteString(statement.getTransaction(), getLaaNumero(), "laaNumero"));
        statement.writeField("MAILPP", _dbWriteNumeric(statement.getTransaction(), getLppCaisse(), "lppCaisse"));
        statement.writeField("MALLPP", _dbWriteString(statement.getTransaction(), getLppNumero(), "lppNumero"));
        statement.writeField("MALFED", _dbWriteString(statement.getTransaction(), getNumeroIDE(), "numeroIDE"));
        statement.writeField("MAMTCO", _dbWriteNumeric(statement.getTransaction(), getTaxeCo2Taux(), "taxeCo2Taux"));
        statement.writeField("MAMFCO",
                _dbWriteNumeric(statement.getTransaction(), getTaxeCo2Fraction(), "taxeCo2Fraction"));
        statement.writeField("MALNAA",
                _dbWriteString(statement.getTransaction(), getAffilieNumeroAncien(), "affilieNumeroAncien"));
    }

    public java.lang.String getAfCaisseExterne() {
        return afCaisseExterne;
    }

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    public java.lang.String getAffilieNumero() {
        return affilieNumero;
    }

    public java.lang.String getAffilieNumeroAncien() {
        return affilieNumeroAncien;
    }

    public java.lang.String getAfNumeroExterne() {
        return afNumeroExterne;
    }

    public java.lang.String getAvsCaisseExterne() {
        return avsCaisseExterne;
    }

    public java.lang.String getAvsNumeroExterne() {
        return avsNumeroExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    public java.lang.Boolean getBonusMalus() {
        return bonusMalus;
    }

    public java.lang.String getBrancheEconomique() {
        return brancheEconomique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 15:42:09)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCaissePartance() {
        return caissePartance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 15:41:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCaisseProvenance() {
        return caisseProvenance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    public FWParametersSystemCode getCsBrancheEconomique() {
        // enregistrement déjà chargé ?
        if (csBrancheEconomique == null) {
            // liste pas encore chargée, on la charge
            csBrancheEconomique = new FWParametersSystemCode();
            csBrancheEconomique.getCode(getBrancheEconomique());
        }
        return csBrancheEconomique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    public FWParametersSystemCode getCsDeclarationSalaire() {
        // enregistrement déjà chargé ?
        if (csDeclarationSalaire == null) {
            // liste pas encore chargée on la charge!
            csDeclarationSalaire = new FWParametersSystemCode();
            csDeclarationSalaire.getCode(getDeclarationSalaire());
        }
        return csDeclarationSalaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    public FWParametersSystemCode getCsMembreAssociation() {
        // enregistrement déjà chargé ?
        if (csMembreAssociation == null) {
            // liste pas encore chargée on la charge
            csMembreAssociation = new FWParametersSystemCode();
            csMembreAssociation.getCode(getMembreAssociation());
        }
        return csMembreAssociation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    public FWParametersSystemCode getCsMembreComite() {
        // enregistrement déjà chargé ?
        if (csMembreComite == null) {
            // liste pas encore chargée, on la charge
            csMembreComite = new FWParametersSystemCode();
            csMembreComite.getCode(getMembreComite());
        }
        return csMembreComite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    public FWParametersSystemCode getCsMotifFin() {
        // enregistrement déjà chargé ?
        if (csMotifFin == null) {
            // liste pas encore chargée, on la charge
            csMotifFin = new FWParametersSystemCode();
            csMotifFin.getCode(getMotifFin());
        }
        return csMotifFin;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    public FWParametersSystemCode getCsPeriodicite() {
        // enregistrement déjà chargé ?
        if (csPeriodicite == null) {
            // liste pas encore chargée, on la charge
            csPeriodicite = new FWParametersSystemCode();
            csPeriodicite.getCode(getPeriodicite());
        }
        return csPeriodicite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    public FWParametersSystemCode getCsPersonaliteJuridique() {
        // enregistrement déjà chargé ?
        if (csPersonaliteJuridique == null) {
            // liste pas encore chargée, on la charge
            csPersonaliteJuridique = new FWParametersSystemCode();
            csPersonaliteJuridique.getCode(getPersonnaliteJuridique());
        }
        return csPersonaliteJuridique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    public FWParametersSystemCode getCsTypeAffiliation() {
        // enregistrement déjà chargé ?
        if (csTypeAffiliation == null) {
            // liste pas encore chargée, on la charge
            csTypeAffiliation = new FWParametersSystemCode();
            csTypeAffiliation.getCode(getTypeAffiliation());
        }
        return csTypeAffiliation;
    }

    public java.lang.String getDateDebut() {
        return dateDebut;
    }

    public java.lang.String getDateEditionFiche() {
        return dateEditionFiche;
    }

    public java.lang.String getDateEditionFicheM1() {
        return dateEditionFicheM1;
    }

    public java.lang.String getDateEditionFicheM2() {
        return dateEditionFicheM2;
    }

    public java.lang.String getDateFin() {
        return dateFin;
    }

    public java.lang.String getDeclarationSalaire() {
        return declarationSalaire;
    }

    public java.lang.String getLaaCaisse() {
        return laaCaisse;
    }

    public java.lang.String getLaaNumero() {
        return laaNumero;
    }

    public java.lang.String getLppCaisse() {
        return lppCaisse;
    }

    public java.lang.String getLppNumero() {
        return lppNumero;
    }

    public java.lang.String getMasseAnnuelle() {
        return JANumberFormatter.fmt(masseAnnuelle.toString(), true, false, true, 2);
    }

    public java.lang.String getMassePeriodicite() {
        return JANumberFormatter.fmt(massePeriodicite.toString(), true, false, true, 2);
    }

    public java.lang.String getMembreAssociation() {
        return membreAssociation;
    }

    public java.lang.String getMembreComite() {
        return membreComite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 09:56:16)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMotifCreation() {
        return motifCreation;
    }

    public java.lang.String getMotifFin() {
        return motifFin;
    }

    public java.lang.String getNumeroIDE() {
        return numeroIDE;
    }

    public java.lang.String getPeriodicite() {
        return periodicite;
    }

    public java.lang.String getPersonnaliteJuridique() {
        return personnaliteJuridique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 15:16:10)
     * 
     * @return java.lang.String
     */
    public java.lang.String getSelection() {
        return selection;
    }

    public java.lang.String getTaxeCo2Fraction() {
        return JANumberFormatter.fmt(taxeCo2Fraction.toString(), true, false, true, 5);
    }

    public java.lang.String getTaxeCo2Taux() {
        return JANumberFormatter.fmt(taxeCo2Taux.toString(), true, false, true, 5);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public globaz.pyxis.db.tiers.TITiersViewBean getTiers() {

        // Si si pas d'identifiant, pas d'objet
        if (JAUtil.isIntegerEmpty(getTiersId())) {
            return null;
        }

        // Si log pas déjà chargé
        if (_tiers == null) {
            // Instancier un nouveau LOG
            _tiers = new globaz.pyxis.db.tiers.TITiersViewBean();
            _tiers.setSession(getSession());

            // Récupérer le log en question
            _tiers.setIdTiers(getTiersId());
            try {
                _tiers.retrieve();
                if (_tiers.getSession().hasErrors()) {
                    _tiers = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }

        return _tiers;
    }

    /**
     * Getter
     */
    public java.lang.String getTiersId() {
        return tiersId;
    }

    public java.lang.String getTypeAffiliation() {
        return typeAffiliation;
    }

    public java.lang.Boolean isExonerationGenerale() {
        return exonerationGenerale;
    }

    public java.lang.Boolean isIrrecouvrable() {
        return irrecouvrable;
    }

    public java.lang.Boolean isLiquidation() {
        return liquidation;
    }

    public java.lang.Boolean isOccasionnel() {
        return occasionnel;
    }

    public java.lang.Boolean isPersonnelMaison() {
        return personnelMaison;
    }

    public java.lang.Boolean isReleveParitaire() {
        return releveParitaire;
    }

    public java.lang.Boolean isRelevePersonnel() {
        return relevePersonnel;
    }

    public java.lang.Boolean isTraitement() {
        return traitement;
    }

    public void setAfCaisseExterne(java.lang.String newAfCaisseExterne) {
        afCaisseExterne = newAfCaisseExterne;
    }

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    public void setAffilieNumero(java.lang.String newAffilieNumero) {
        affilieNumero = newAffilieNumero;
    }

    public void setAffilieNumeroAncien(java.lang.String newAffilieNumeroAncien) {
        affilieNumeroAncien = newAffilieNumeroAncien;
    }

    public void setAfNumeroExterne(java.lang.String newAfNumeroExterne) {
        afNumeroExterne = newAfNumeroExterne;
    }

    public void setAvsCaisseExterne(java.lang.String newAvsCaisseExterne) {
        avsCaisseExterne = newAvsCaisseExterne;
    }

    public void setAvsNumeroExterne(java.lang.String newAvsNumeroExterne) {
        avsNumeroExterne = newAvsNumeroExterne;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 09:54:44)
     * 
     * @param newBonusMalus
     *            java.lang.String
     */
    public void setBonusMalus(java.lang.Boolean newBonusMalus) {
        bonusMalus = newBonusMalus;
    }

    public void setBrancheEconomique(java.lang.String newBrancheEconomique) {
        brancheEconomique = newBrancheEconomique;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 15:42:09)
     * 
     * @param newCaissePartance
     *            java.lang.String
     */
    public void setCaissePartance(java.lang.String newCaissePartance) {
        caissePartance = newCaissePartance;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 15:41:36)
     * 
     * @param newCaisseProvenance
     *            java.lang.String
     */
    public void setCaisseProvenance(java.lang.String newCaisseProvenance) {
        caisseProvenance = newCaisseProvenance;
    }

    public void setDateDebut(java.lang.String newDateDebut) {
        dateDebut = newDateDebut;
    }

    public void setDateEditionFiche(java.lang.String newDateEditionFiche) {
        dateEditionFiche = newDateEditionFiche;
    }

    public void setDateEditionFicheM1(java.lang.String newDateEditionFicheM1) {
        dateEditionFicheM1 = newDateEditionFicheM1;
    }

    public void setDateEditionFicheM2(java.lang.String newDateEditionFicheM2) {
        dateEditionFicheM2 = newDateEditionFicheM2;
    }

    public void setDateFin(java.lang.String newDateFin) {
        dateFin = newDateFin;
    }

    public void setDeclarationSalaire(java.lang.String newDeclarationSalaire) {
        declarationSalaire = newDeclarationSalaire;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.09.2002 15:30:33)
     * 
     * @param newDerniereAffiliation
     *            java.lang.String
     */
    public void setDerniereAffiliation(java.lang.String newDerniereAffiliation) {
        derniereAffiliation = newDerniereAffiliation;
    }

    public void setExonerationGenerale(java.lang.Boolean newExonerationGenerale) {
        exonerationGenerale = newExonerationGenerale;
    }

    public void setIrrecouvrable(java.lang.Boolean newIrrecouvrable) {
        irrecouvrable = newIrrecouvrable;
    }

    public void setLaaCaisse(java.lang.String newLaaCaisse) {
        laaCaisse = newLaaCaisse;
    }

    public void setLaaNumero(java.lang.String newLaaNumero) {
        laaNumero = newLaaNumero;
    }

    public void setLiquidation(java.lang.Boolean newLiquidation) {
        liquidation = newLiquidation;
    }

    public void setLppCaisse(java.lang.String newLppCaisse) {
        lppCaisse = newLppCaisse;
    }

    public void setLppNumero(java.lang.String newLppNumero) {
        lppNumero = newLppNumero;
    }

    public void setMasseAnnuelle(java.lang.String newMasseAnnuelle) {
        masseAnnuelle = JANumberFormatter.deQuote(newMasseAnnuelle);
    }

    public void setMassePeriodicite(java.lang.String newMassePeriodicite) {
        massePeriodicite = JANumberFormatter.deQuote(newMassePeriodicite);
    }

    public void setMembreAssociation(java.lang.String newMembreAssociation) {
        membreAssociation = newMembreAssociation;
    }

    public void setMembreComite(java.lang.String newMembreComite) {
        membreComite = newMembreComite;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 09:56:16)
     * 
     * @param newMotifCreation
     *            java.lang.String
     */
    public void setMotifCreation(java.lang.String newMotifCreation) {
        motifCreation = newMotifCreation;
    }

    public void setMotifFin(java.lang.String newMotifFin) {
        motifFin = newMotifFin;
    }

    public void setNumeroIDE(java.lang.String newNumeroIDE) {
        numeroIDE = newNumeroIDE;
    }

    public void setOccasionnel(java.lang.Boolean newOccasionnel) {
        occasionnel = newOccasionnel;
    }

    public void setPeriodicite(java.lang.String newPeriodicite) {
        periodicite = newPeriodicite;
    }

    public void setPersonnaliteJuridique(java.lang.String newPersonnaliteJuridique) {
        personnaliteJuridique = newPersonnaliteJuridique;
    }

    public void setPersonnelMaison(java.lang.Boolean newPersonnelMaison) {
        personnelMaison = newPersonnelMaison;
    }

    public void setReleveParitaire(java.lang.Boolean newReleveParitaire) {
        releveParitaire = newReleveParitaire;
    }

    public void setRelevePersonnel(java.lang.Boolean newRelevePersonnel) {
        relevePersonnel = newRelevePersonnel;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (29.04.2003 15:16:10)
     * 
     * @param newSelection
     *            java.lang.String
     */
    public void setSelection(java.lang.String newSelection) {
        selection = newSelection;
    }

    public void setTaxeCo2Fraction(java.lang.String newTaxeCo2Fraction) {
        taxeCo2Fraction = JANumberFormatter.deQuote(newTaxeCo2Fraction);
    }

    public void setTaxeCo2Taux(java.lang.String newTaxeCo2Taux) {
        taxeCo2Taux = JANumberFormatter.deQuote(newTaxeCo2Taux);
    }

    /**
     * Setter
     */
    public void setTiersId(java.lang.String newTiersId) {
        tiersId = newTiersId;
    }

    public void setTraitement(java.lang.Boolean newTraitement) {
        traitement = newTraitement;
    }

    public void setTypeAffiliation(java.lang.String newTypeAffiliation) {
        typeAffiliation = newTypeAffiliation;
    }

}
