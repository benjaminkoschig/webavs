package globaz.perseus.process.echeance;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.lyra.process.LYAbstractExcelEcheanceProcess;
import ch.globaz.perseus.businessimpl.services.doc.excel.impl.ListeEcheance;
import ch.globaz.utils.excel.ExcelJob;

/**
 * @author MBO
 */
public class PFEcheanceProcess extends LYAbstractExcelEcheanceProcess<ListeEcheance> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ListeEcheance documentGenerator;
    private String forCsCaisse;
    private String isAidesEtudes;
    private String isAllocations;
    private String isDecisionProjetSansChoix;
    private String isDemandesEnAttente3Mois;
    private String isDossierDateRevision;
    private String isEcheanceLibre;
    private String isEnfantDe16ans;
    private String isEnfantDe18ans;
    private String isEnfantDe25ans;
    private String isEnfantDe6ans;
    private String isEtudiantsEtApprentis;
    private String isFemmeRetraite;
    private String isHommeRetraite;
    private String isIndemnitesJournalieres;
    private String isRentePont;

    public PFEcheanceProcess() {
        super();

        documentGenerator = new ListeEcheance(BSessionUtil.getSessionFromThreadContext());
    }

    @Override
    protected void beforeExecute() {
        documentGenerator.setIdEcheance(getIdEcheance());
        documentGenerator.setEmailAdresse(getEmailAddress());

        String dateDebut = "01." + getMoisTraitement();
        String dateFin = JadeDateUtil.addDays(JadeDateUtil.addMonths(dateDebut, 1), -1);

        documentGenerator.setDateDebut(dateDebut);
        documentGenerator.setDateFin(dateFin);

        documentGenerator.setForCsCaisse(forCsCaisse);

        documentGenerator.setDossierDateRevision(Boolean.parseBoolean(isDossierDateRevision));
        documentGenerator.setEnfantDe6ans(Boolean.parseBoolean(isEnfantDe6ans));
        documentGenerator.setEnfantDe16ans(Boolean.parseBoolean(isEnfantDe16ans));
        documentGenerator.setEnfantDe18ans(Boolean.parseBoolean(isEnfantDe18ans));
        documentGenerator.setEnfantDe25ans(Boolean.parseBoolean(isEnfantDe25ans));
        documentGenerator.setFemmeRetraite(Boolean.parseBoolean(isFemmeRetraite));
        documentGenerator.setHommeRetraite(Boolean.parseBoolean(isHommeRetraite));
        documentGenerator.setRentePont(Boolean.parseBoolean(isRentePont));
        documentGenerator.setIndemnitesJournalieres(Boolean.parseBoolean(isIndemnitesJournalieres));
        documentGenerator.setAllocations(Boolean.parseBoolean(isAllocations));
        documentGenerator.setEcheanceLibre(Boolean.parseBoolean(isEcheanceLibre));
        documentGenerator.setEtudiantsEtApprentis(Boolean.parseBoolean(isEtudiantsEtApprentis));
        documentGenerator.setDecisionProjetSansChoix(Boolean.parseBoolean(isDecisionProjetSansChoix));
        documentGenerator.setDemandesEnAttente3Mois(Boolean.parseBoolean(isDemandesEnAttente3Mois));
        documentGenerator.setAidesEtudes(Boolean.parseBoolean(isAidesEtudes));
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("EXCEL_PF_ECHEANCES_TITRE_PRINCIPAL") + " " + getMoisTraitement() + " "
                + getSession().getCodeLibelle(forCsCaisse);
    }

    @Override
    protected ExcelJob<ListeEcheance> getExcelJob() {
        return new ExcelJob<ListeEcheance>(documentGenerator, getEmailAddress(), true, getDescription(), getName());
    }

    public String getForCsCaisse() {
        return forCsCaisse;
    }

    public String getIsAidesEtudes() {
        return isAidesEtudes;
    }

    public String getIsAllocations() {
        return isAllocations;
    }

    public String getIsDecisionProjetSansChoix() {
        return isDecisionProjetSansChoix;
    }

    public String getIsDemandesEnAttente3Mois() {
        return isDemandesEnAttente3Mois;
    }

    public String getIsDossierDateRevision() {
        return isDossierDateRevision;
    }

    public String getIsEcheanceLibre() {
        return isEcheanceLibre;
    }

    public String getIsEnfantDe16ans() {
        return isEnfantDe16ans;
    }

    public String getIsEnfantDe18ans() {
        return isEnfantDe18ans;
    }

    public String getIsEnfantDe25ans() {
        return isEnfantDe25ans;
    }

    public String getIsEnfantDe6ans() {
        return isEnfantDe6ans;
    }

    public String getIsEtudiantsEtApprentis() {
        return isEtudiantsEtApprentis;
    }

    public String getIsFemmeRetraite() {
        return isFemmeRetraite;
    }

    public String getIsHommeRetraite() {
        return isHommeRetraite;
    }

    public String getIsIndemnitesJournalieres() {
        return isIndemnitesJournalieres;
    }

    public String getIsRentePont() {
        return isRentePont;
    }

    @Override
    public String getName() {
        return PFEcheanceProcess.class.getName();
    }

    @Override
    protected String getSessionApplicationName() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setForCsCaisse(String forCsCaisse) {
        this.forCsCaisse = forCsCaisse;
    }

    public void setIsAidesEtudes(String isAidesEtudes) {
        this.isAidesEtudes = isAidesEtudes;
    }

    public void setIsAllocations(String isAllocations) {
        this.isAllocations = isAllocations;
    }

    public void setIsDecisionProjetSansChoix(String isDecisionProjetSansChoix) {
        this.isDecisionProjetSansChoix = isDecisionProjetSansChoix;
    }

    public void setIsDemandesEnAttente3Mois(String isDemandesEnAttente3Mois) {
        this.isDemandesEnAttente3Mois = isDemandesEnAttente3Mois;
    }

    public void setIsDossierDateRevision(String isDossierDateRevision) {
        this.isDossierDateRevision = isDossierDateRevision;
    }

    public void setIsEcheanceLibre(String isEcheanceLibre) {
        this.isEcheanceLibre = isEcheanceLibre;
    }

    public void setIsEnfantDe16ans(String isEnfantDe16ans) {
        this.isEnfantDe16ans = isEnfantDe16ans;
    }

    public void setIsEnfantDe18ans(String isEnfantDe18ans) {
        this.isEnfantDe18ans = isEnfantDe18ans;
    }

    public void setIsEnfantDe25ans(String isEnfantDe25ans) {
        this.isEnfantDe25ans = isEnfantDe25ans;
    }

    public void setIsEnfantDe6ans(String isEnfantDe6ans) {
        this.isEnfantDe6ans = isEnfantDe6ans;
    }

    public void setIsEtudiantsEtApprentis(String isEtudiantsEtApprentis) {
        this.isEtudiantsEtApprentis = isEtudiantsEtApprentis;
    }

    public void setIsFemmeRetraite(String isFemmeRetraite) {
        this.isFemmeRetraite = isFemmeRetraite;
    }

    public void setIsHommeRetraite(String isHommeRetraite) {
        this.isHommeRetraite = isHommeRetraite;
    }

    public void setIsIndemnitesJournalieres(String isIndemnitesJournalieres) {
        this.isIndemnitesJournalieres = isIndemnitesJournalieres;
    }

    public void setIsRentePont(String isRentePont) {
        this.isRentePont = isRentePont;
    }
}
