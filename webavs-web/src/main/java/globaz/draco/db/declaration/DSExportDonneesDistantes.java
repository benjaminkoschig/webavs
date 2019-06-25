package globaz.draco.db.declaration;

import globaz.draco.api.TestDSImportDonneesValidationProcess;
import globaz.draco.api.TestIDSImportDonnees;
import globaz.draco.db.inscriptions.*;
import globaz.globall.db.*;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.properties.JadePropertiesService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class DSExportDonneesDistantes {

    private String affiliationId = "";
    private HashMap afParCanton = new HashMap();
    private String anneeMax = "";
    private String anneeMin = "";
    private String dateReception = "";
    private DSDeclarationViewBean declaration = null;
    private HashMap donneeAssures = new HashMap();
    private String idDeclaration = "";
    private HashMap masseAc = new HashMap();
    private HashMap masseAc2 = new HashMap();
    private HashMap masseTotale = new HashMap();
    private String numeroAfffillie = "";
    private BSession session = null;
    private String typeDeclaration = "";
    private static final String CS_PARAMETRES_IM = "12000006";
    private final String LIST_CAT_AGRIVIT_LAA = "draco.CotisationLAACatAgrivit";
    /**
     * Set les totaux individuels dans la structure DSStructureSyncroAgrivit prévue à cet effet
     *
     * @throws Exception
     */
    public void exportDonnees() throws Exception {
        FWParametersSystemCodeManager csCateg = new FWParametersSystemCodeManager();
        csCateg.setSession(getSession());
        csCateg.getListeCodes("CICATPER", getSession().getIdLangue());
        DSInscAnneeMaxMinManager inscMinMax = new DSInscAnneeMaxMinManager();
        inscMinMax.setSession(getSession());
        inscMinMax.setForIdDeclaration(idDeclaration);
        inscMinMax.find();
        int min = 0;
        int max = 0;

        if (inscMinMax.size() > 0) {
            DSInscAnneeMaxMinEntity minMax = (DSInscAnneeMaxMinEntity) inscMinMax.getFirstEntity();
            if (!JadeStringUtil.isBlankOrZero(minMax.getValeurMin())) {
                anneeMin = minMax.getValeurMin();
                min = Integer.parseInt(minMax.getValeurMin());
            } else {
                anneeMin = declaration.getAnnee();
                min = Integer.parseInt(anneeMin);
            }

            if (!JadeStringUtil.isBlankOrZero(minMax.getValeurMax())) {
                anneeMax = minMax.getValeurMax();
                max = Integer.parseInt(minMax.getValeurMax());
            } else {
                anneeMax = declaration.getAnnee();
                max = Integer.parseInt(anneeMax);
            }
        }
        for (int k = min; k <= max; k++) {
            for (int i = 0; i < csCateg.size(); i++) {
                FWParametersSystemCode catPers = (FWParametersSystemCode) csCateg.get(i);
                // Création enregistrement structure
                DSStructureSyncroAgrivit syncro = new DSStructureSyncroAgrivit();
                syncro.setCatPersonnel(catPers.getIdCode());
                // Montant AC et AC2
                DSInscriptionsIndividuellesManager dsMgr = new DSInscriptionsIndividuellesManager();
                dsMgr.setForIdDeclaration(idDeclaration);
                dsMgr.setForCategoriePersonnel(catPers.getIdCode());
                dsMgr.setForAnneeChomage(String.valueOf(k));
                dsMgr.setSession(getSession());
                dsMgr.find(BManager.SIZE_NOLIMIT);
                BigDecimal montantAC = dsMgr.getSum("TEMAI");
                BigDecimal montantAc2 = dsMgr.getSum("TEMAII");
                // Mise à jour des montants chômage dans la structure
                syncro.setMontantAc(montantAC.toString());
                syncro.setAc2(montantAc2.toString());
                // montant avs
                DSInscriptionsCalculMontantCI mgrSomme = new DSInscriptionsCalculMontantCI();
                mgrSomme.setSession(getSession());
                mgrSomme.setForAnnee(String.valueOf(k));
                mgrSomme.setForIdDeclaration(idDeclaration);
                mgrSomme.setForCategoriePersonnel(catPers.getIdCode());
                BigDecimal montantAvs = mgrSomme.getSum("KBMMON");
                syncro.setMontantAvs(montantAvs.toString());
                /**
                 *
                 *POAVS-671  CCVD - S170306_005 - Cotisation LAA
                 *
                 */
                if(JadePropertiesService.getInstance().getProperty(LIST_CAT_AGRIVIT_LAA).contains(catPers.getIdCode())){
                    FWFindParameterManager param = new FWFindParameterManager();
                    param.setSession(getSession());
                    param.setIdApplParametre(getSession().getApplicationId());
                    param.setIdCodeSysteme(CS_PARAMETRES_IM);
                    param.setIdCleDiffere("PLAFONDAC1");
                    param.setIdActeurParametre("0");
                    param.setPlageValDeParametre("0");
                    if (globaz.draco.translation.CodeSystem.CS_SALAIRE_DIFFERES.equals(declaration.getTypeDeclaration())) {
                        param.setDateDebutValidite("01.01." + declaration.getAnneeTaux());
                    } else {
                        param.setDateDebutValidite("01.01." + declaration.getAnnee());
                    }

                    param.find(BManager.SIZE_NOLIMIT);
                    BigDecimal plafondAc = new BigDecimal(((FWFindParameter) param.getFirstEntity()).getValeurNumParametre());
                    plafondAc = plafondAc.setScale(2);
                    BigDecimal montantLAA = BigDecimal.ZERO;
                    for(int index=0;index<dsMgr.size();index++){
                        DSInscriptionsIndividuelles inscrp=(DSInscriptionsIndividuelles) dsMgr.get(index);
                        BigDecimal montantAVSDetail = new BigDecimal(inscrp.getMontant());
                        if(montantAVSDetail.compareTo(plafondAc) > 0){
                            montantLAA = montantLAA.add(plafondAc);
                        }else{
                            montantLAA = montantLAA.add(montantAVSDetail);
                        }

                    }


                    syncro.setMontantLAA(montantLAA.toString());
                }else{
                    syncro.setMontantLAA(BigDecimal.ZERO.toString());
                }


                // Mise à jour du total par code
                FWParametersSystemCodeManager csCanton = new FWParametersSystemCodeManager();
                csCanton.setSession(getSession());
                csCanton.getListeCodes("PYCANTON", getSession().getIdLangue());
                for (int j = 0; j < csCanton.size(); j++) {
                    FWParametersSystemCode canton = (FWParametersSystemCode) csCanton.get(j);
                    DSInscriptionsIndividuellesManager inscMgr = new DSInscriptionsIndividuellesManager();
                    inscMgr.setForAnneeChomage(String.valueOf(k));
                    inscMgr.setSession(getSession());
                    inscMgr.setForCodeCanton(canton.getIdCode());
                    inscMgr.setForCategoriePersonnel(catPers.getIdCode());
                    inscMgr.setForIdDeclaration(idDeclaration);
                    BigDecimal afCanton = inscMgr.getSum("TEMAF");
                    if (new BigDecimal("0").compareTo(afCanton) != 0) {
                        syncro.setMontantParCanton(canton.getIdCode(), afCanton.toString());
                    }
                }
                donneeAssures.put(catPers.getIdCode() + "/" + String.valueOf(k), syncro);
            }
        }
    }

    /**
     * Exporte les totaux bruts par canton
     *
     * @throws Exception
     */
    public void exportMasseAFParCanton() throws Exception {
        DSInscAnneeMaxMinManager inscMinMax = new DSInscAnneeMaxMinManager();
        inscMinMax.setSession(getSession());
        inscMinMax.setForIdDeclaration(idDeclaration);
        inscMinMax.find();
        int min = 0;
        int max = 0;

        if (inscMinMax.size() > 0) {
            DSInscAnneeMaxMinEntity minMax = (DSInscAnneeMaxMinEntity) inscMinMax.getFirstEntity();
            if (!JadeStringUtil.isBlankOrZero(minMax.getValeurMin())) {
                min = Integer.parseInt(minMax.getValeurMin());
            }
            if (!JadeStringUtil.isBlankOrZero(minMax.getValeurMax())) {
                max = Integer.parseInt(minMax.getValeurMax());

            }
        }

        DSInscriptionsIndividuellesManager dsMgr = new DSInscriptionsIndividuellesManager();
        dsMgr.setForIdDeclaration(getIdDeclaration());
        dsMgr.setSession(getSession());

        FWParametersSystemCodeManager csCanton = new FWParametersSystemCodeManager();
        csCanton.setSession(getSession());
        csCanton.getListeCodes("PYCANTON", getSession().getIdLangue());
        for (int j = min; j <= max; j++) {
            for (int i = 0; i < csCanton.size(); i++) {
                FWParametersSystemCode code = (FWParametersSystemCode) csCanton.get(i);
                dsMgr.setForAnneeChomage(String.valueOf(j));
                dsMgr.setForCodeCanton(code.getIdCode());
                BigDecimal masseCanton = dsMgr.getSum("TEMAF");
                if (new BigDecimal("0").compareTo(masseCanton) != 0) {
                    afParCanton.put(code.getIdCode() + "/" + String.valueOf(j), masseCanton.toString());
                }

            }
        }

    }

    public void exportMasses() throws Exception {

        declaration = new DSDeclarationViewBean();
        declaration.setSession(getSession());
        declaration.setIdDeclaration(idDeclaration);
        declaration.retrieve();
        exportMasseAFParCanton();
        setMasseTotale();
        exportDonnees();
        // appel distant
        String adapterName = "agrivit";
        BSession session = new BSession();

        TestIDSImportDonnees test = new TestIDSImportDonnees();
        test.importDonnees(session, adapterName, declaration.getNoDecompte(), declaration.getAffilieNumero(),
                idDeclaration, declaration.getDateRetourEff(), anneeMin, anneeMax, declaration.getTypeDeclaration(),
                masseTotale, masseAc, masseAc2, donneeAssures, afParCanton);

//        TestDSImportDonneesValidationProcess process = new TestDSImportDonneesValidationProcess()
//        session = getSession();
//        process.setAffiliationId(affiliationId);
//        process.setAnneeMaxStr(anneeMax);
//        process.setAnneeMinStr(anneeMin);
//        process.setMasseAc(masseAc);
//        process.setMasseAc2(masseAc2);
//        process.setMasseTotale(masseTotale);
//        process.setIdDeclarationDistante(declaration.getTypeDeclaration());
//        process.setDateReception(dateReception);
//        process.setTypeDeclaration(typeDeclaration);
//        process.setDeclaration(declaration);
//        process.setDonneeAssures(donneeAssures);
//        process.setAfParCanton(afParCanton);
//        process.setNoDecompte(declaration.getNoDecompte());
//        process.setSession(session);
//        process.executeProcess();

    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public Map getAfParCanton() {
        return afParCanton;
    }

    public Map getDonneeAssures() {
        return donneeAssures;
    }

    public String getIdDeclaration() {
        return idDeclaration;
    }

    public HashMap getMasseAc() {
        return masseAc;
    }

    public HashMap getMasseAc2() {
        return masseAc2;
    }

    public BSession getSession() {
        return session;
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setAfParCanton(HashMap afParCanton) {
        this.afParCanton = afParCanton;
    }

    public void setDonneeAssures(HashMap donneeAssures) {
        this.donneeAssures = donneeAssures;
    }

    public void setIdDeclaration(String idDeclaration) {
        this.idDeclaration = idDeclaration;
    }

    public void setMasseAc(HashMap masseAc) {
        this.masseAc = masseAc;
    }

    public void setMasseAc2(HashMap masseAc2) {
        this.masseAc2 = masseAc2;
    }

    public void setMasseTotale() throws Exception {
        DSInscAnneeMaxMinManager inscMinMax = new DSInscAnneeMaxMinManager();
        inscMinMax.setSession(getSession());
        inscMinMax.setForIdDeclaration(idDeclaration);
        inscMinMax.find();
        int min = 0;
        int max = 0;

        if (inscMinMax.size() > 0) {
            DSInscAnneeMaxMinEntity minMax = (DSInscAnneeMaxMinEntity) inscMinMax.getFirstEntity();
            if (!JadeStringUtil.isBlankOrZero(minMax.getValeurMin())) {
                min = Integer.parseInt(minMax.getValeurMin());
            }
            if (!JadeStringUtil.isBlankOrZero(minMax.getValeurMax())) {
                max = Integer.parseInt(minMax.getValeurMax());
            }
        }
        for (int k = min; k <= max; k++) {
            DSInscriptionsIndividuellesManager dsMgr = new DSInscriptionsIndividuellesManager();
            dsMgr.setForIdDeclaration(idDeclaration);
            dsMgr.setForAnneeChomage(String.valueOf(k));
            dsMgr.setSession(getSession());
            BigDecimal montantAC = dsMgr.getSum("TEMAI");
            masseAc.put(String.valueOf(k), montantAC.toString());
            BigDecimal montantAc2 = dsMgr.getSum("TEMAII");
            masseAc2.put(String.valueOf(k), montantAc2.toString());
            DSInscriptionsCalculMontantCI mgrSomme = new DSInscriptionsCalculMontantCI();
            mgrSomme.setSession(getSession());
            mgrSomme.setForAnnee(String.valueOf(k));
            mgrSomme.setForIdDeclaration(idDeclaration);
            BigDecimal montantAvs = mgrSomme.getSum("KBMMON");
            masseTotale.put(String.valueOf(k), montantAvs.toString());
        }
    }

    public void setMasseTotale(HashMap masseTotale) {
        this.masseTotale = masseTotale;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

};
