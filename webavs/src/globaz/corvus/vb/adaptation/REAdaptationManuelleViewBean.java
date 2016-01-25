package globaz.corvus.vb.adaptation;

import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

public class REAdaptationManuelleViewBean extends PRAbstractViewBeanSupport {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private static final Object[] METHODES_SEL_RENTES_ACC = new Object[] { new String[] { "idPrestationAccordee",
            "idPrestationAccordee" } };

    private String ancienAnneeMontantRAM = "";
    private String ancienBTEFrancs = "";
    private String ancienCCS1 = "";
    private String ancienCCS2 = "";
    private String ancienCCS3 = "";
    private String ancienCCS4 = "";
    private String ancienCCS5 = "";
    private String ancienMntReductionAnticipation = "";
    private String ancienMntRenteOrdinaireRempl = "";
    private String ancienMontantPrestation = "";
    private String ancienRam = "";
    private String ancienSupplementAjournement = "";
    private String csTypeAdaptation = "";
    private String descriptionTiers = "";
    private String fractionRente = "";
    private String genrePrestation = "";
    private String idPrestationAccordee = "";
    private String idRenteAdaptee = "";
    private Boolean isCreateAnnonceSub = false;
    private Boolean isDejaAdaptee = false;
    private String nouveauAnneeMontantRAM = "";
    private String nouveauBTEFrancs = "";
    private String nouveauCCS1 = "";
    private String nouveauCCS2 = "";
    private String nouveauCCS3 = "";
    private String nouveauCCS4 = "";
    private String nouveauCCS5 = "";
    private String nouveauMntReductionAnticipation = "";
    private String nouveauMntRenteOrdinaireRempl = "";
    private String nouveauMontantPrestation = "";

    private String nouveauRam = "";

    private String nouveauSupplementAjournement = "";

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    public String getAncienAnneeMontantRAM() {
        return ancienAnneeMontantRAM;
    }

    public String getAncienBTEFrancs() {
        return ancienBTEFrancs;
    }

    // ~ Getter & Setter
    // ------------------------------------------------------------------------------------------------

    public String getAncienCCS1() {
        return ancienCCS1;
    }

    public String getAncienCCS2() {
        return ancienCCS2;
    }

    public String getAncienCCS3() {
        return ancienCCS3;
    }

    public String getAncienCCS4() {
        return ancienCCS4;
    }

    public String getAncienCCS5() {
        return ancienCCS5;
    }

    public String getAncienMntReductionAnticipation() {
        return ancienMntReductionAnticipation;
    }

    public String getAncienMntRenteOrdinaireRempl() {
        return ancienMntRenteOrdinaireRempl;
    }

    public String getAncienMontantPrestation() {
        return ancienMontantPrestation;
    }

    public String getAncienRam() {
        return ancienRam;
    }

    public String getAncienSupplementAjournement() {
        return ancienSupplementAjournement;
    }

    public String getCsTypeAdaptation() {
        return csTypeAdaptation;
    }

    public String getDescriptionTiers() {
        return descriptionTiers;
    }

    public String getFractionRente() {

        if (JadeStringUtil.isBlank(fractionRente)) {
            return "0";
        }

        return fractionRente;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    public String getIdPrestationAccordee() {
        return idPrestationAccordee;
    }

    public String getIdRenteAdaptee() {
        return idRenteAdaptee;
    }

    public Boolean getIsCreateAnnonceSub() {
        return isCreateAnnonceSub;
    }

    public Boolean getIsDejaAdaptee() {
        return isDejaAdaptee;
    }

    public Object[] getMethodesSelectionRenteAccordee() {
        return REAdaptationManuelleViewBean.METHODES_SEL_RENTES_ACC;
    }

    public String getNouveauAnneeMontantRAM() throws JAException {

        if (JadeStringUtil.isBlankOrZero(nouveauAnneeMontantRAM)) {

            JACalendar cal = new JACalendarGregorian();

            String datePmt = REPmtMensuel.getDateProchainPmt(getSession());
            JADate date = new JADate(cal.addMonths(datePmt, 1));

            return String.valueOf(date.getYear());
        } else {
            return nouveauAnneeMontantRAM;
        }
    }

    public String getNouveauBTEFrancs() {
        return nouveauBTEFrancs;
    }

    public String getNouveauCCS1() {
        return nouveauCCS1;
    }

    public String getNouveauCCS2() {
        return nouveauCCS2;
    }

    public String getNouveauCCS3() {
        return nouveauCCS3;
    }

    public String getNouveauCCS4() {
        return nouveauCCS4;
    }

    public String getNouveauCCS5() {
        return nouveauCCS5;
    }

    public String getNouveauMntReductionAnticipation() {
        return nouveauMntReductionAnticipation;
    }

    public String getNouveauMntRenteOrdinaireRempl() {
        return nouveauMntRenteOrdinaireRempl;
    }

    public String getNouveauMontantPrestation() {
        return nouveauMontantPrestation;
    }

    public String getNouveauRam() {
        return nouveauRam;
    }

    public String getNouveauSupplementAjournement() {
        return nouveauSupplementAjournement;
    }

    public void setAncienAnneeMontantRAM(String ancienAnneeMontantRAM) {
        this.ancienAnneeMontantRAM = ancienAnneeMontantRAM;
    }

    public void setAncienBTEFrancs(String ancienBTEFrancs) {
        this.ancienBTEFrancs = ancienBTEFrancs;
    }

    public void setAncienCCS1(String ancienCCS1) {
        this.ancienCCS1 = ancienCCS1;
    }

    public void setAncienCCS2(String ancienCCS2) {
        this.ancienCCS2 = ancienCCS2;
    }

    public void setAncienCCS3(String ancienCCS3) {
        this.ancienCCS3 = ancienCCS3;
    }

    public void setAncienCCS4(String ancienCCS4) {
        this.ancienCCS4 = ancienCCS4;
    }

    public void setAncienCCS5(String ancienCCS5) {
        this.ancienCCS5 = ancienCCS5;
    }

    public void setAncienMntReductionAnticipation(String ancienMntReductionAnticipation) {
        this.ancienMntReductionAnticipation = ancienMntReductionAnticipation;
    }

    public void setAncienMntRenteOrdinaireRempl(String ancienMntRenteOrdinaireRempl) {
        this.ancienMntRenteOrdinaireRempl = ancienMntRenteOrdinaireRempl;
    }

    public void setAncienMontantPrestation(String ancienMontantPrestation) {
        this.ancienMontantPrestation = ancienMontantPrestation;
    }

    public void setAncienRam(String ancienRam) {
        this.ancienRam = ancienRam;
    }

    public void setAncienSupplementAjournement(String ancienSupplementAjournement) {
        this.ancienSupplementAjournement = ancienSupplementAjournement;
    }

    public void setCsTypeAdaptation(String csTypeAdaptation) {
        this.csTypeAdaptation = csTypeAdaptation;
    }

    public void setDescriptionTiers(String descriptionTiers) {
        this.descriptionTiers = descriptionTiers;
    }

    public void setFractionRente(String fractionRente) {
        this.fractionRente = fractionRente;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setIdPrestationAccordee(String idPrestationAccordee) {
        this.idPrestationAccordee = idPrestationAccordee;
    }

    public void setIdRenteAdaptee(String idRenteAdaptee) {
        this.idRenteAdaptee = idRenteAdaptee;
    }

    public void setIsCreateAnnonceSub(Boolean isCreateAnnonceSub) {
        this.isCreateAnnonceSub = isCreateAnnonceSub;
    }

    public void setIsDejaAdaptee(Boolean isDejaAdaptee) {
        this.isDejaAdaptee = isDejaAdaptee;
    }

    public void setNouveauAnneeMontantRAM(String nouveauAnneeMontantRAM) {
        this.nouveauAnneeMontantRAM = nouveauAnneeMontantRAM;
    }

    public void setNouveauBTEFrancs(String nouveauBTEFrancs) {
        this.nouveauBTEFrancs = nouveauBTEFrancs;
    }

    public void setNouveauCCS1(String nouveauCCS1) {
        this.nouveauCCS1 = nouveauCCS1;
    }

    public void setNouveauCCS2(String nouveauCCS2) {
        this.nouveauCCS2 = nouveauCCS2;
    }

    public void setNouveauCCS3(String nouveauCCS3) {
        this.nouveauCCS3 = nouveauCCS3;
    }

    public void setNouveauCCS4(String nouveauCCS4) {
        this.nouveauCCS4 = nouveauCCS4;
    }

    public void setNouveauCCS5(String nouveauCCS5) {
        this.nouveauCCS5 = nouveauCCS5;
    }

    public void setNouveauMntReductionAnticipation(String nouveauMntReductionAnticipation) {
        this.nouveauMntReductionAnticipation = nouveauMntReductionAnticipation;
    }

    public void setNouveauMntRenteOrdinaireRempl(String nouveauMntRenteOrdinaireRempl) {
        this.nouveauMntRenteOrdinaireRempl = nouveauMntRenteOrdinaireRempl;
    }

    public void setNouveauMontantPrestation(String nouveauMontantPrestation) {
        this.nouveauMontantPrestation = nouveauMontantPrestation;
    }

    public void setNouveauRam(String nouveauRam) {
        this.nouveauRam = nouveauRam;
    }

    public void setNouveauSupplementAjournement(String nouveauSupplementAjournement) {
        this.nouveauSupplementAjournement = nouveauSupplementAjournement;
    }

    @Override
    public boolean validate() {
        return false;
    }

}
