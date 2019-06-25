package globaz.draco.api;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationViewBean;
import globaz.draco.db.declaration.DSStructureSyncroAgrivit;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.properties.JadePropertiesService;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFCalculAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.application.CIApplication;

import java.math.BigDecimal;
import java.util.*;

public class TestDSImportDonneesValidationProcess extends BProcess {

    private static final long serialVersionUID = -3631010607321183808L;
    private String affiliationId = "";
    private Map<?, ?> afParCanton = null;
    private String anneeMaxStr = "";
    private String anneeMinStr = "";
    private String dateReception = "";
    private DSDeclarationViewBean declaration = null;
    private Map<?, ?> donneeAssures = null;
    private String idDeclarationDistante = "";
    private Map<?, ?> masseAc = null;
    private Map<?, ?> masseAc2 = null;
    private Map<?, ?> masseTotale = null;
    private String noDecompte = "";
    private String typeDeclaration = "";


    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    protected boolean _executeProcess() throws Exception {
        // En premier lieu, on crée la déclaration
//        ajouteDeclaration();
        int anneeMin = Integer.parseInt(anneeMinStr);
        int anneeMax = Integer.parseInt(anneeMaxStr);
        for (int j = anneeMin; j <= anneeMax; j++) {
            // On parcourt les cotisations pour voir la masse par assurance
            AFCotisationManager cotisations = new AFCotisationManager();
            cotisations.setSession(getSession());
            cotisations.setForAffiliationId(getAffiliationId());
            cotisations.setForAnneeDeclaration(String.valueOf(j));
            cotisations.setForNotMotifFin(CodeSystem.MOTIF_FIN_EXCEPTION);
            cotisations.find();
            String oldId = "0";
            for (int i = 0; i < cotisations.size(); i++) {

                AFCotisation cotisation = (AFCotisation) cotisations.getEntity(i);
                if (oldId.equals(cotisation.getAssuranceId())) {
                    continue;
                }
                oldId = cotisation.getAssuranceId();

                // List tauxList = cotisation.getTauxList("31.12." + annee);

                // On check juste si un taux existe
                AFTauxAssurance tauxAssurance = cotisation.findTaux("31.12." + String.valueOf(j), "0",
                        declaration.getTypeDeclaration(), true, false);
                // On ne prend que les assurances paritaires
                if (cotisation.getAssurance().getAssuranceGenre().equalsIgnoreCase("801001")
                        && cotisation.getAssurance().isAssurance13().booleanValue()
                        // && tauxList.size() > 0) {
                        && (tauxAssurance != null)) {
                    // On crée pour chaque assurance une ligne de déclaration
                    DSLigneDeclarationViewBean ligneDec = new DSLigneDeclarationViewBean();
                    ligneDec.setIdDeclaration(declaration.getIdDeclaration());
                    // S'il s'agit de l'assurance chômage on prend masseACTotal
                    ligneDec.setAssuranceId(cotisation.getAssuranceId());
                    if(cotisation.getAssurance().getTypeAssurance().equals(CodeSystem.TYPE_ASS_LAA)){
                        ligneDec.setMontantDeclaration(getMasseLAA(String.valueOf(j)));
                    }else{
                        ligneDec.setMontantDeclaration(getMasseMoinsLesExclus(cotisation.getAssurance(), String.valueOf(j)));
                    }


                    if (cotisation.getAssurance().getAssuranceReference() != null) {
                        String dateDebut = "01.01." + String.valueOf(j);
                        String dateFin = "31.12." + String.valueOf(j);
                        double montant = 0;
                        montant = AFCalculAssurance.calculResultatAssurance(dateDebut, dateFin, cotisation
                                        .getAssurance().getAssuranceReference().getTaux(dateFin), new Double(
                                        getMasseMoinsLesExclus(cotisation.getAssurance(), String.valueOf(j))).doubleValue(),
                                getSession());
                        BigDecimal montantArr = new BigDecimal(montant);
                        montantArr = JANumberFormatter.round(montantArr, 0.05, 2, JANumberFormatter.NEAR);
                        ligneDec.setMontantDeclaration(montantArr.toString());
                    }
                    ligneDec.setAnneCotisation(String.valueOf(j));
//                    ligneDec.add(getTransaction());
                }
            }
        }
        return !isOnError();
    }

    private String getMasseLAA(String annee) {
        BigDecimal masseLAA = BigDecimal.ZERO;
        String propRaw = JadePropertiesService.getInstance().getProperty("draco.CotisationLAACatAgrivit");
        List<String> listCat = Arrays.asList(propRaw.split(","));

        for(String keyCat :listCat){
            DSStructureSyncroAgrivit struct = (DSStructureSyncroAgrivit) donneeAssures.get(keyCat + "/" + annee);
            masseLAA = masseLAA.add(new BigDecimal(struct.getMontantLAA()));
        }


        return masseLAA.toString();



    }

    /**
     * Ajoute une déclaration
     *
     * @throws Exception
     */
    private void ajouteDeclaration() throws Exception {
        /*
         * AFAffiliationManager mgr = new AFAffiliationManager (); mgr.setSession(getSession());
         * mgr.setForAffilieNumero(affiliationId); mgr.find();
         */
        // Modif jmc 5.3
        String idAffiliation = "";
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);

            AFAffiliation affilie = application.getAffilieByNo(getSession(), affiliationId, true, false, "", "",
                    anneeMaxStr, "", "");
            idAffiliation = affilie.getAffiliationId();
        } catch (Exception e) {

        }
        affiliationId = idAffiliation;

        declaration = new DSDeclarationViewBean();

        declaration.setDateRetourEff(dateReception);
        declaration.setAffiliationId(affiliationId);
        declaration.setSession(getSession());
        if (!DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(typeDeclaration)) {
            declaration.setAnnee(anneeMinStr);
        } else {
            declaration.setNoDecompte(noDecompte);
        }
        declaration.setTypeDeclaration(typeDeclaration);
        declaration.setEtat(DSDeclarationViewBean.CS_OUVERT);
        declaration.wantCallMethodAfter(false);
        declaration.setIdDeclarationDistante(idDeclarationDistante);
        declaration.setMasseSalTotal(calculMasseTotale());
        declaration.setMasseACTotal(calculMasseAC());
        declaration.setMasseAC2Total(calculMasseAC2());
        declaration.add(getTransaction());
    }

    private String calculMasseAC() {
        Set<?> set = masseAc.keySet();
        Iterator<?> iter = set.iterator();
        BigDecimal totalRetour = new BigDecimal("0");
        while (iter.hasNext()) {
            String cle = (String) iter.next();
            BigDecimal aAjouter = new BigDecimal((String) masseAc.get(cle));
            totalRetour = totalRetour.add(aAjouter);
        }
        return totalRetour.toString();
    }

    private String calculMasseAC2() {
        Set<?> set = masseAc2.keySet();
        Iterator<?> iter = set.iterator();
        BigDecimal totalRetour = new BigDecimal("0");
        while (iter.hasNext()) {
            String cle = (String) iter.next();
            BigDecimal aAjouter = new BigDecimal((String) masseAc2.get(cle));
            totalRetour = totalRetour.add(aAjouter);
        }
        return totalRetour.toString();
    }

    private String calculMasseTotale() {
        Set<?> set = masseTotale.keySet();
        Iterator<?> iter = set.iterator();
        BigDecimal totalRetour = new BigDecimal("0");
        while (iter.hasNext()) {
            String cle = (String) iter.next();
            BigDecimal aAjouter = new BigDecimal((String) masseTotale.get(cle));
            totalRetour = totalRetour.add(aAjouter);
        }
        return totalRetour.toString();
    }

    public String getAffiliationId() {
        return affiliationId;
    }

    public Map<?, ?> getAfParCanton() {
        return afParCanton;
    }

    public String getAnneeMaxStr() {
        return anneeMaxStr;
    }

    public String getAnneeMinStr() {
        return anneeMinStr;
    }

    public String getDateReception() {
        return dateReception;
    }

    public DSDeclarationViewBean getDeclaration() {
        return declaration;
    }

    public Map<?, ?> getDonneeAssures() {
        return donneeAssures;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdDeclarationDistante() {
        return idDeclarationDistante;
    }

    public Map<?, ?> getMasseAc() {
        return masseAc;
    }

    public Map<?, ?> getMasseAc2() {
        return masseAc2;
    }

    /**
     * Donne la masse de base avant les exclusions de personnel
     *
     * @param cotisationAss
     * @return
     * @throws Exception
     */
    public String getMasseDeBasePourAssurance(AFAssurance assurance, String annee) throws Exception {

        String masseDepart = "0";
        // On regarde si prend AC, AVS
        if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AC)
                || assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_LAA)) {
            if (masseAc.containsKey(annee)) {
                masseDepart = (String) masseAc.get(annee);
            } else {
                masseDepart = "0.00";
            }
        } else if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AC2)) {
            if (masseAc2.containsKey(annee)) {
                masseDepart = (String) masseAc2.get(annee);
            } else {
                masseDepart = "0.00";
            }
        } else if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AF)) {
            // Si c'est une cotisation AF => on regarde la masse du canton
            if (afParCanton.containsKey(assurance.getAssuranceCanton() + "/" + annee)) {
                masseDepart = (String) afParCanton.get(assurance.getAssuranceCanton() + "/" + annee);
            } else {
                masseDepart = "0.00";
            }
        } else {
            if (masseTotale.containsKey(annee)) {
                masseDepart = (String) masseTotale.get(annee);
            } else {
                masseDepart = "0.00";
            }
        }
        return masseDepart;
    }

    /**
     * Retourne la masse à setter dans la ligne de déclaration (masse - personnes exclue)
     *
     * @param cotisationAss
     * @return
     * @throws Exception
     */
    public String getMasseMoinsLesExclus(AFAssurance assurance, String annee) throws Exception {
        String masseDeBase = getMasseDeBasePourAssurance(assurance, annee);
        String[] personnelExclus = assurance.getListExclusionsCatPers("31.12." + annee);
        // Si aucun code n'exclu cette assurance => masse de base
        if (0 == personnelExclus.length) {
            return masseDeBase;

        }
        BigDecimal masseDeBaseBigDec = new BigDecimal(masseDeBase);
        for (int i = 0; i < personnelExclus.length; i++) {
            if (donneeAssures.containsKey(personnelExclus[i] + "/" + annee)) {
                BigDecimal montantASoustraireBigDec = getMontantASoustraire(personnelExclus[i], assurance, annee);
                masseDeBaseBigDec = masseDeBaseBigDec.subtract(montantASoustraireBigDec);
            }
        }

        return masseDeBaseBigDec.toString();
    }

    public Map<?, ?> getMasseTotale() {
        return masseTotale;
    }

    /**
     * Récupère le montant à soustraire, qu'il soit AF ou non
     *
     * @param codePersonnel
     * @return
     */
    public BigDecimal getMontantASoustraire(String codePersonnel, AFAssurance assurance, String annee) {

        if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AC)
                || assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_LAA)) {
            DSStructureSyncroAgrivit struct = (DSStructureSyncroAgrivit) donneeAssures.get(codePersonnel + "/" + annee);
            return new BigDecimal(struct.getMontantAc());
        } else if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AC2)) {
            DSStructureSyncroAgrivit struct = (DSStructureSyncroAgrivit) donneeAssures.get(codePersonnel + "/" + annee);
            return new BigDecimal(struct.getAc2());
        } else if (assurance.getTypeAssurance().equals(CodeSystem.TYPE_ASS_COTISATION_AF)) {
            DSStructureSyncroAgrivit struct = (DSStructureSyncroAgrivit) donneeAssures.get(codePersonnel + "/" + annee);
            return new BigDecimal(struct.getMasseAfParCanton(assurance.getAssuranceCanton()));
        } else {
            DSStructureSyncroAgrivit struct = (DSStructureSyncroAgrivit) donneeAssures.get(codePersonnel + "/" + annee);
            return new BigDecimal(struct.getMontantAvs());
        }
    }

    public String getNoDecompte() {
        return noDecompte;
    }

    public String getTypeDeclaration() {
        return typeDeclaration;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        // TODO Auto-generated method stub
        return GlobazJobQueue.UPDATE_SHORT;
    }

    public void setAffiliationId(String affiliationId) {
        this.affiliationId = affiliationId;
    }

    public void setAfParCanton(Map<?, ?> afParCanton) {
        this.afParCanton = afParCanton;
    }

    public void setAnneeMaxStr(String anneeMaxStr) {
        this.anneeMaxStr = anneeMaxStr;
    }

    public void setAnneeMinStr(String anneeMinStr) {
        this.anneeMinStr = anneeMinStr;
    }

    public void setDateReception(String dateReception) {
        this.dateReception = dateReception;
    }

    public void setDeclaration(DSDeclarationViewBean declaration) {
        this.declaration = declaration;
    }

    public void setDonneeAssures(Map<?, ?> donneeAssures) {
        this.donneeAssures = donneeAssures;
    }

    public void setIdDeclarationDistante(String idDeclarationDistante) {
        this.idDeclarationDistante = idDeclarationDistante;
    }

    public void setMasseAc(Map<?, ?> masseAc) {
        this.masseAc = masseAc;
    }

    public void setMasseAc2(Map<?, ?> masseAc2) {
        this.masseAc2 = masseAc2;
    }

    public void setMasseTotale(Map<?, ?> masseTotale) {
        this.masseTotale = masseTotale;
    }

    public void setNoDecompte(String noDecompte) {
        this.noDecompte = noDecompte;
    }

    public void setTypeDeclaration(String typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }
}
