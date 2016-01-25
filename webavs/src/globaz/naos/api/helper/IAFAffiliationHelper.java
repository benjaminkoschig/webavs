package globaz.naos.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.naos.api.IAFAffiliation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class IAFAffiliationHelper extends GlobazHelper implements IAFAffiliation {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String[] getterToLoad() {
        ArrayList methodToLoad = new ArrayList();
        Method[] methods = IAFAffiliationHelper.class.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().length() > 3) {
                String methodName = method.getName().substring(0, 3);
                if ((methodName.equals("get")) && (!method.getName().equals("getClass"))
                        && (!method.getName().equals("getISession")) && (!method.getName().equals("getterToLoad"))) {
                    methodToLoad.add(method.getName());
                }
            }
        }
        String[] result = new String[methodToLoad.size()];
        methodToLoad.toArray(result);
        return result;
    }

    /*
     * public static String[] METHODS_TO_LOAD = new String[] { "getAfCaisseExterne", "getAffiliationId",
     * "getAffilieNumero", "getAfNumeroExterne", "getAncienAffilieNumero", "getAvsCaisseExterne", "getAvsNumeroExterne",
     * "getBonusMalus", "getBrancheEconomique", "getCaissePartance", "getCaisseProvenance", "getCodeLaa", "getCodeLpp",
     * "getCodeNoga", "getDateCreation", "getDateDebut", "getDateEditionFiche", "getDateEditionFicheM1",
     * "getDateEditionFicheM2", "getDateFin", "getDeclarationSalaire", "getIdTiers", "getLaaCaisse", "getLaaNumero",
     * "getLppCaisse", "getLppNumero", "getMasseAnnuelle", "getMassePeriodicite", "getMembreAssociation",
     * "getMembreComite", "getMotifCreation", "getMotifFin", "getNumeroIDE", "getPeriodicite",
     * "getPersonnaliteJuridique", "getTaxeCo2Fraction", "getTaxeCo2Taux", "getTiers", "getTypeAffiliation", "getIsAPI"
     * };
     */
    /**
     * Constructeur du type IAFAffiliationHelper
     */
    public IAFAffiliationHelper() {
        super("globaz.naos.db.affiliation.AFAffiliation");
        setMethodsToLoad(IAFAffiliationHelper.getterToLoad());
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFAffiliationHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IAFAffiliationHelper(GlobazValueObject valueObject) {
        super(valueObject);
        setMethodsToLoad(IAFAffiliationHelper.getterToLoad());
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Constructeur du type IAFAffiliationHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IAFAffiliationHelper(String implementationClassName) {
        super(implementationClassName);
        setMethodsToLoad(IAFAffiliationHelper.getterToLoad());
        _getValueObject().setProperty("isAPI", new Boolean(true));
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    // public FWParametersSystemCode getCsMembreComite() {
    // return (FWParametersSystemCode)
    // _getValueObject().getProperty("csMembreComite");
    // }
    /**
     * Insérez la description de la méthode ici. Date de création : (20.02.2003 13:50:13)
     */
    @Override
    public Object[] find(Hashtable params) throws Exception {
        Object[] result = null;
        result = _getArray("find", new Object[] { params });
        return result;
    }

    @Override
    public IAFAffiliation[] findAffiliation(Hashtable params) throws Exception {
        IAFAffiliation[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFAffiliationHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject aff = (GlobazValueObject) objResult[i];
                result[i] = new IAFAffiliationHelper(aff);
            }
        }
        return result;
    }

    @Override
    public IAFAffiliation[] findAffiliationAF(Hashtable params) throws Exception {
        IAFAffiliation[] result = null;
        Object[] objResult = find(params);
        if (objResult != null) {
            result = new IAFAffiliationHelper[objResult.length];
            for (int i = 0; i < objResult.length; i++) {
                GlobazValueObject aff = (GlobazValueObject) objResult[i];
                result[i] = new IAFAffiliationHelper(aff);
            }
        }
        return result;
    }

    @Override
    public Boolean getActifAF(String date, String typeAllocataire) throws Exception {
        return (Boolean) _getObject("getActifAF", new Object[] { date, typeAllocataire });
    }

    @Override
    public IAFAffiliation getAffiliationFacturationAF(String date) throws Exception {
        GlobazValueObject aff = (GlobazValueObject) _getObject("getAffiliationFacturationAF", new Object[] { date });
        if (aff != null) {
            return new IAFAffiliationHelper(aff);
        } else {
            return null;
        }
    }

    @Override
    public IAFAffiliation getAffiliationFacturationAF(String date, String typeAllocataire) throws Exception {
        GlobazValueObject aff = (GlobazValueObject) _getObject("getAffiliationFacturationAF", new Object[] { date,
                typeAllocataire });
        if (aff != null) {
            return new IAFAffiliationHelper(aff);
        } else {
            return null;
        }
    }

    @Override
    public java.lang.String getAffiliationId() {
        return (java.lang.String) _getValueObject().getProperty("affiliationId");
    }

    @Override
    public java.lang.String getAffilieNumero() {
        return (java.lang.String) _getValueObject().getProperty("affilieNumero");
    }

    /**
     * Retourne le nom de l'agence communale Date de création : (26.02.2003 09:54:44)
     * 
     * @return
     */
    @Override
    public java.lang.String getAgenceCom(String idAffiliation, String dateJour) throws Exception {

        return (String) _getObject("getAgenceCom", new Object[] { idAffiliation, dateJour });
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    // public FWParametersSystemCode getCsBrancheEconomique() {
    // return (FWParametersSystemCode)
    // _getValueObject().getProperty("csBrancheEconomique");
    // }
    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    // public FWParametersSystemCode getCsDeclarationSalaire() {
    // return (FWParametersSystemCode)
    // _getValueObject().getProperty("csDeclarationSalaire");
    // }
    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    // public FWParametersSystemCode getCsMembreAssociation() {
    // return (FWParametersSystemCode)
    // _getValueObject().getProperty("csMembreAssociation");
    // }

    /**
     * Retourne le numéro de l'agence communale Date de création : (26.02.2003 09:54:44)
     * 
     * @return
     */
    @Override
    public java.lang.String getAgenceComNum(String idAffiliation, String dateJour) throws Exception {

        return (String) _getObject("getAgenceCom", new Object[] { idAffiliation, dateJour });
    }

    @Override
    public java.lang.String getAncienAffilieNumero() {
        return (java.lang.String) _getValueObject().getProperty("ancienAffilieNumero");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 09:54:44)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.Boolean getBonusMalus() {
        return (java.lang.Boolean) _getValueObject().getProperty("bonusMalus");
    }

    @Override
    public java.lang.String getBrancheEconomique() {
        return (java.lang.String) _getValueObject().getProperty("brancheEconomique");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 15:42:09)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getCaissePartance() {
        return (java.lang.String) _getValueObject().getProperty("caissePartance");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 15:41:36)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getCaisseProvenance() {
        return (java.lang.String) _getValueObject().getProperty("caisseProvenance");
    }

    @Override
    public String getCantonAF(String date) throws Exception {
        return (String) _getObject("getCantonAF", new Object[] { date });
    }

    @Override
    public java.lang.String getCodeNoga() {
        return (java.lang.String) _getValueObject().getProperty("codeNoga");
    }

    @Override
    public java.lang.String getDateCreation() {
        return (java.lang.String) _getValueObject().getProperty("dateCreation");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    // public FWParametersSystemCode getCsMotifFin() {
    // return (FWParametersSystemCode)
    // _getValueObject().getProperty("csMotifFin");
    // }
    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    // public FWParametersSystemCode getCsPeriodicite() {
    // return (FWParametersSystemCode)
    // _getValueObject().getProperty("csPeriodicite");
    // }
    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    // public FWParametersSystemCode getCsPersonaliteJuridique() {
    // return (FWParametersSystemCode)
    // _getValueObject().getProperty("csPersonaliteJuridique");
    // }
    /**
     * Insérez la description de la méthode ici. Date de création : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    // public FWParametersSystemCode getCsTypeAffiliation() {
    // return (FWParametersSystemCode)
    // _getValueObject().getProperty("csTypeAffiliation");
    // }
    @Override
    public java.lang.String getDateDebut() {
        return (java.lang.String) _getValueObject().getProperty("dateDebut");
    }

    public java.lang.String getDateEditionFiche() {
        return (java.lang.String) _getValueObject().getProperty("dateEditionFiche");
    }

    public java.lang.String getDateEditionFicheM1() {
        return (java.lang.String) _getValueObject().getProperty("dateEditionFicheM1");
    }

    public java.lang.String getDateEditionFicheM2() {
        return (java.lang.String) _getValueObject().getProperty("dateEditionFicheM2");
    }

    @Override
    public java.lang.String getDateFin() {
        return (java.lang.String) _getValueObject().getProperty("dateFin");
    }

    @Override
    public java.lang.String getDeclarationSalaire() {
        return (java.lang.String) _getValueObject().getProperty("declarationSalaire");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdTiers() {
        return (java.lang.String) _getValueObject().getProperty("idTiers");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.09.2002 15:30:33)
     * 
     * @return java.lang.String
     */

    @Override
    public java.lang.String getMasseAnnuelle() {
        return (java.lang.String) _getValueObject().getProperty("masseAnnuelle");
    }

    @Override
    public java.lang.String getMassePeriodicite() {
        return (java.lang.String) _getValueObject().getProperty("massePeriodicite");
    }

    @Override
    public java.lang.String getMembreAssociation() {
        return (java.lang.String) _getValueObject().getProperty("membreAssociation");
    }

    @Override
    public java.lang.String getMembreComite() {
        return (java.lang.String) _getValueObject().getProperty("membreComite");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 09:56:16)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getMotifCreation() {
        return (java.lang.String) _getValueObject().getProperty("motifCreation");
    }

    @Override
    public java.lang.String getMotifFin() {
        return (java.lang.String) _getValueObject().getProperty("motifFin");
    }

    @Override
    public java.lang.String getNumeroIDE() {
        return (java.lang.String) _getValueObject().getProperty("numeroIDE");
    }

    @Override
    public String getIdeStatut() {
        return (java.lang.String) _getValueObject().getProperty("ideStatut");
    }

    @Override
    public java.lang.String getPeriodicite() {
        return (java.lang.String) _getValueObject().getProperty("periodicite");
    }

    @Override
    public java.lang.String getPersonnaliteJuridique() {
        return (java.lang.String) _getValueObject().getProperty("personnaliteJuridique");
    }

    /**
     * @see globaz.naos.api.IAFAffiliation#getDesignationAffilieLong()
     */
    @Override
    public String getRaisonSociale() {
        return (java.lang.String) _getValueObject().getProperty("raisonSociale");
    }

    /**
     * @see globaz.naos.api.IAFAffiliation#getRaisonSocialeCourt()
     */
    @Override
    public String getRaisonSocialeCourt() {
        return (java.lang.String) _getValueObject().getProperty("raisonSocialeCourt");
    }

    /**
     * @see globaz.naos.api.IAFAffiliation#getDesignationAffilieLong()
     */
    @Override
    public Boolean getReleveParitaire() {
        return (java.lang.Boolean) _getValueObject().getProperty("releveParitaire");
    }

    public java.lang.String getTaxeCo2Fraction() {
        return (java.lang.String) _getValueObject().getProperty("taxeCo2Fraction");
    }

    public java.lang.String getTaxeCo2Taux() {
        return (java.lang.String) _getValueObject().getProperty("taxeCo2Taux");
    }

    @Override
    public java.lang.String getTiersNom() {
        return (java.lang.String) _getValueObject().getProperty("tiersNom");
    }

    @Override
    public java.lang.Boolean getTraitement() {
        return (java.lang.Boolean) _getValueObject().getProperty("traitement");
    }

    @Override
    public java.lang.String getTypeAffiliation() {
        return (java.lang.String) _getValueObject().getProperty("typeAffiliation");
    }

    @Override
    public java.lang.String getTypeAssocie() {
        return (java.lang.String) _getValueObject().getProperty("typeAssocie");
    }

    @Override
    public void setAffiliationId(java.lang.String newAffiliationId) {
        _getValueObject().setProperty("affiliationId", newAffiliationId);
    }

    @Override
    public void setAffilieNumero(java.lang.String newAffilieNumero) {
        _getValueObject().setProperty("affilieNumero", newAffilieNumero);
    }

    @Override
    public void setAffilieNumeroAncien(java.lang.String newAffilieNumeroAncien) {
        _getValueObject().setProperty("affilieNumeroAncien", newAffilieNumeroAncien);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 09:54:44)
     * 
     * @param newBonusMalus
     *            java.lang.String
     */
    @Override
    public void setBonusMalus(java.lang.Boolean newBonusMalus) {
        _getValueObject().setProperty("bonusMalus", newBonusMalus);
    }

    @Override
    public void setBrancheEconomique(java.lang.String newBrancheEconomique) {
        _getValueObject().setProperty("brancheEconomique", newBrancheEconomique);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 15:42:09)
     * 
     * @param newCaissePartance
     *            java.lang.String
     */
    @Override
    public void setCaissePartance(java.lang.String newCaissePartance) {
        _getValueObject().setProperty("caissePartance", newCaissePartance);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (02.09.2002 15:41:36)
     * 
     * @param newCaisseProvenance
     *            java.lang.String
     */
    @Override
    public void setCaisseProvenance(java.lang.String newCaisseProvenance) {
        _getValueObject().setProperty("caisseProvenance", newCaisseProvenance);
    }

    @Override
    public void setCodeNoga(java.lang.String newCodeNoga) {
        _getValueObject().setProperty("codeNoga", newCodeNoga);
    }

    @Override
    public void setDateCreation(java.lang.String newDateCreation) {
        _getValueObject().setProperty("dateCreation", newDateCreation);
    }

    @Override
    public void setDateDebut(java.lang.String newDateDebut) {
        _getValueObject().setProperty("dateDebut", newDateDebut);
    }

    public void setDateEditionFiche(java.lang.String newDateEditionFiche) {
        _getValueObject().setProperty("dateEditionFiche", newDateEditionFiche);
    }

    public void setDateEditionFicheM1(java.lang.String newDateEditionFicheM1) {
        _getValueObject().setProperty("dateEditionFicheM1", newDateEditionFicheM1);
    }

    public void setDateEditionFicheM2(java.lang.String newDateEditionFicheM2) {
        _getValueObject().setProperty("dateEditionFicheM2", newDateEditionFicheM2);
    }

    @Override
    public void setDateFin(java.lang.String newDateFin) {
        _getValueObject().setProperty("dateFin", newDateFin);
    }

    @Override
    public void setDeclarationSalaire(java.lang.String newDeclarationSalaire) {
        _getValueObject().setProperty("declarationSalaire", newDeclarationSalaire);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.09.2002 15:30:33)
     * 
     * @param newDerniereAffiliation
     *            java.lang.String
     */
    public void setDerniereAffiliation(java.lang.String newDerniereAffiliation) {
        _getValueObject().setProperty("derniereAffiliation", newDerniereAffiliation);
    }

    public void setExonerationGenerale(java.lang.Boolean newExonerationGenerale) {
        _getValueObject().setProperty("exonerationGenerale", newExonerationGenerale);
    }

    /**
     * Setter
     */
    @Override
    public void setIdTiers(java.lang.String newIdTiers) {
        _getValueObject().setProperty("idTiers", newIdTiers);
    }

    public void setIrrecouvrable(java.lang.Boolean newIrrecouvrable) {
        _getValueObject().setProperty("irrecouvrable", newIrrecouvrable);
    }

    public void setLiquidation(java.lang.Boolean newLiquidation) {
        _getValueObject().setProperty("liquidation", newLiquidation);
    }

    @Override
    public void setMasseAnnuelle(java.lang.String newMasseAnnuelle) {
        _getValueObject().setProperty("masseAnnuelle", newMasseAnnuelle);
    }

    @Override
    public void setMassePeriodicite(java.lang.String newMassePeriodicite) {
        _getValueObject().setProperty("massePeriodicite", newMassePeriodicite);
    }

    @Override
    public void setMembreAssociation(java.lang.String newMembreAssociation) {
        _getValueObject().setProperty("membreAssociation", newMembreAssociation);
    }

    @Override
    public void setMembreComite(java.lang.String newMembreComite) {
        _getValueObject().setProperty("membreComite", newMembreComite);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 09:56:16)
     * 
     * @param newMotifCreation
     *            java.lang.String
     */
    @Override
    public void setMotifCreation(java.lang.String newMotifCreation) {
        _getValueObject().setProperty("motifCreation", newMotifCreation);
    }

    @Override
    public void setMotifFin(java.lang.String newMotifFin) {
        _getValueObject().setProperty("motifFin", newMotifFin);
    }

    @Override
    public void setNumeroIDE(java.lang.String newNumeroIDE) {
        _getValueObject().setProperty("numeroIDE", newNumeroIDE);
    }

    @Override
    public void setIdeStatut(java.lang.String newIdeStatut) {
        _getValueObject().setProperty("ideStatut", newIdeStatut);
    }

    public void setOccasionnel(java.lang.Boolean newOccasionnel) {
        _getValueObject().setProperty("occasionnel", newOccasionnel);
    }

    @Override
    public void setPeriodicite(java.lang.String newPeriodicite) {
        _getValueObject().setProperty("periodicite", newPeriodicite);
    }

    @Override
    public void setPersonnaliteJuridique(java.lang.String newPersonnaliteJuridique) {
        _getValueObject().setProperty("personnaliteJuridique", newPersonnaliteJuridique);
    }

    public void setPersonnelMaison(java.lang.Boolean newPersonnelMaison) {
        _getValueObject().setProperty("personnelMaison", newPersonnelMaison);
    }

    /**
     * @see globaz.naos.api.IAFAffiliation#setRaisonSociale(java.lang.String)
     */
    @Override
    public void setRaisonSociale(String string) {
        _getValueObject().setProperty("raisonSociale", string);
    }

    /**
     * @see globaz.naos.api.IAFAffiliation#setRaisonSocialeCourt(java.lang.String)
     */
    @Override
    public void setRaisonSocialeCourt(String string) {
        _getValueObject().setProperty("raisonSocialeCourt", string);
    }

    @Override
    public void setReleveParitaire(java.lang.Boolean newReleveParitaire) {
        _getValueObject().setProperty("releveParitaire", newReleveParitaire);
    }

    public void setRelevePersonnel(java.lang.Boolean newRelevePersonnel) {
        _getValueObject().setProperty("relevePersonnel", newRelevePersonnel);
    }

    public void setTaxeCo2Fraction(java.lang.String newTaxeCo2Fraction) {
        _getValueObject().setProperty("taxeCo2Fraction", newTaxeCo2Fraction);
    }

    public void setTaxeCo2Taux(java.lang.String newTaxeCo2Taux) {
        _getValueObject().setProperty("taxeCo2Taux", newTaxeCo2Taux);
    }

    @Override
    public void setTiersNom(java.lang.String newTiersNom) {
        _getValueObject().setProperty("TiersNom", newTiersNom);
    }

    public void setTraitement(java.lang.Boolean newTraitement) {
        _getValueObject().setProperty("traitement", newTraitement);
    }

    @Override
    public void setTypeAffiliation(java.lang.String newTypeAffiliation) {
        _getValueObject().setProperty("typeAffiliation", newTypeAffiliation);
    }

    @Override
    public void setTypeAssocie(java.lang.String newTypeAssocie) {
        _getValueObject().setProperty("typeAssocie", newTypeAssocie);
    }
}
