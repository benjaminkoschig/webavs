/*
 * Créé le 12 janv. 07
 */
package globaz.corvus.vb.process;

import globaz.corvus.api.basescalcul.IRERenteAccordee;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.utils.REPmtMensuel;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.vb.PRAbstractViewBeanSupport;
import java.util.Iterator;
import java.util.List;

/**
 * @author scr
 */

/**
 * @author scr
 */
public class REAnnulerDiminutionViewBean extends PRAbstractViewBeanSupport {
    private String codeMutation = "";
    private String dateDebutDroitRA = "";
    private String dateFinDroitModifiee = "";
    private String dateFinDroitRA = "";
    private String errorMsg = "";
    private String genreRente = "";

    private String idRenteAccordee = "";

    private String idTiersBeneficiaire = "";
    private String montant = "";
    private String tiersBeneficiaireInfo = "";
    private String warningMsg = "";

    /**
     * Constructeur
     */
    public REAnnulerDiminutionViewBean() {
        super();
    }

    /**
     * Récupère le code de mutation
     * 
     * @return
     */
    public String getCodeMutation() {
        return codeMutation;
    }

    public String getCsCodeMutation() {
        return getSession().getSystemCode(IRERenteAccordee.CS_GROUPE_CODE_MUTATION, getCodeMutation());
    }

    public List getCUCSCodeMutation() {

        return globaz.prestation.tools.PRCodeSystem.getCUCS(getSession(),
                globaz.corvus.api.basescalcul.IRERenteAccordee.CS_GROUPE_CODE_MUTATION);

        // String s = "";
        //
        // Iterator<String> iter = cucsCodeMutation.iterator();
        // while (iter.hasNext()) {
        // String cu = iter.next();
        // String cs = iter.next();
        //
        // if (cu.equals(this.codeMutation)) {
        // s += cu + " - ";
        // s += this.getSession().getCodeLibelle(cs);
        // return s;
        // }
        // }
        // return s;
    }

    public String getDateDebutDroitRA() {
        return dateDebutDroitRA;
    }

    public String getDateFinDroitModifiee() {
        return dateFinDroitModifiee;
    }

    public String getDateFinDroitRA() {
        return dateFinDroitRA;
    }

    public String getDatePaiement() {
        return REPmtMensuel.getDateDernierPmt(getSession());
    }

    public String getDatePaiementPlus1Mois() throws JAException {
        JADate date = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));
        JACalendar cal = new JACalendarGregorian();

        date = cal.addMonths(date, 1);

        return PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(date.toStrAMJ());
    }

    /**
     * Récupère le fait que la prestation est bloquée
     * 
     * @return
     */

    public String getDescriptionCodeMutation() {

        java.util.List<String> cucsCodeMutation = globaz.prestation.tools.PRCodeSystem.getCUCS(getSession(),
                globaz.corvus.api.basescalcul.IRERenteAccordee.CS_GROUPE_CODE_MUTATION);

        String s = "";

        Iterator<String> iter = cucsCodeMutation.iterator();
        while (iter.hasNext()) {
            String cu = iter.next();
            String cs = iter.next();

            if (cu.equals(codeMutation)) {
                s += cu + " - ";
                s += getSession().getCodeLibelle(cs);
                return s;
            }
        }
        return s;
    }

    public String getDescriptionCodeMutation(String cs) {
        return getSession().getCodeLibelle(cs);
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getGenreRente() {
        return genreRente;
    }

    /**
     * Récupère l'id de la rente accordée
     * 
     * @return
     */
    public String getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public String getIdRenteCalculee(String idDemandeRente) throws Exception {

        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(getSession());
        demandeRente.setIdDemandeRente(idDemandeRente);
        demandeRente.retrieve();

        if (demandeRente.isNew()) {
            return "";
        } else {
            return demandeRente.getIdRenteCalculee();
        }

    }

    public String getIdTierRequerant(String idDemandeRente) throws Exception {

        REDemandeRente demandeRente = new REDemandeRente();
        demandeRente.setSession(getSession());
        demandeRente.setIdDemandeRente(idDemandeRente);
        demandeRente.retrieve();

        if (demandeRente.isNew()) {
            return "";
        } else {
            PRDemande demandePrest = new PRDemande();
            demandePrest.setSession(getSession());
            demandePrest.setIdDemande(demandeRente.getIdDemandePrestation());
            demandePrest.retrieve();

            if (demandePrest.isNew()) {
                return "";
            } else {
                return demandePrest.getIdTiers();
            }

        }

    }

    /**
     * Récupère l'id du tiers bénéficiaire
     * 
     * @return
     */
    public String getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getMontant() {
        return montant;
    }

    /**
     * @return
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * Récupération de la ligne info sur le tiers bénéficaire
     * 
     * @return
     */
    public String getTiersBeneficiaireInfo() {
        return tiersBeneficiaireInfo;
    }

    public String getWarningMsg() {
        return warningMsg;
    }

    /**
     * Modifie le code de mutation
     * 
     * @param newCodeMutation
     */
    public void setCodeMutation(String newCodeMutation) {
        codeMutation = newCodeMutation;
    }

    public void setCsCodeMutation(String s) {
        setCodeMutation(getSession().getCode(s));
    }

    public void setDateDebutDroitRA(String dateDebutDroitRA) {
        this.dateDebutDroitRA = dateDebutDroitRA;
    }

    public void setDateFinDroitModifiee(String date) {
        dateFinDroitModifiee = date;
    }

    public void setDateFinDroitRA(String dateFinDroitRA) {
        this.dateFinDroitRA = dateFinDroitRA;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setGenreRente(String genreRente) {
        this.genreRente = genreRente;
    }

    /**
     * Modification de l'id rente accordée
     * 
     * @param newIdRenteAccordee
     */
    public void setIdRenteAccordee(String newIdRenteAccordee) {
        idRenteAccordee = newIdRenteAccordee;
    }

    /**
     * Modifie l'id du tiers bénéficiaire
     * 
     * @param newIdTiersBeneficiaire
     */
    public void setIdTiersBeneficiaire(String newIdTiersBeneficiaire) {
        idTiersBeneficiaire = newIdTiersBeneficiaire;
    }

    /**
     * Modifie le montant de la rente accordée
     * 
     * @param montant
     */
    public void setMontant(String montant) {
        this.montant = montant;
    }

    /**
     * Modification de la ligne info sur le tiers bénéficiaire
     * 
     * @param newTiersBeneficiaireInfo
     */
    public void setTiersBeneficiaireInfo(String newTiersBeneficiaireInfo) {
        tiersBeneficiaireInfo = newTiersBeneficiaireInfo;
    }

    public void setWarningMsg(String warningMsg) {
        this.warningMsg = warningMsg;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.vb.PRAbstractViewBeanSupport#validate()
     */
    @Override
    public boolean validate() {
        return true;
    }

}
