/*
 * Créé le 12 janv. 07
 */
package globaz.corvus.vb.process;

import globaz.corvus.api.basescalcul.IRERenteAccordee;
import globaz.corvus.api.diminution.IREDiminution;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.module.compta.diminution.REModuleComptableDiminution;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSpy;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.vb.PRAbstractViewBeanSupport;

/**
 * @author fgo
 */

/**
 * @author FGO
 */
public class REDiminutionRenteAccordeeViewBean extends PRAbstractViewBeanSupport {
    private String csCodeMutation = "";
    private String csCodeTraitement = "";
    private String dateDebutDroit = "";
    private String dateFinDroit = "";
    private String errorMsg = "";
    private String genreDiminution = "";
    private String idRenteAccordee = "";
    private String idTiersBeneficiaire = "";
    private Boolean isPrestationBloquee = new Boolean(false);
    private String montant = "";
    private String tiersBeneficiaireInfo = "";

    private FWCurrency totalBloque = new FWCurrency("0", 2);
    private String warningMsg = "";

    /**
     * Constructeur
     */
    public REDiminutionRenteAccordeeViewBean() {
        super();
    }

    /**
     * Récupère le code de mutation
     * 
     * @return
     */
    public String getCsCodeMutation() {
        return csCodeMutation;
    }

    /**
     * Récupère le code de traitement
     * 
     * @return
     */
    public String getCsCodeTraitement() {
        return csCodeTraitement;
    }

    /**
     * Récupère la date de début du droit à la rente
     * 
     * @return
     */
    public String getDateDebutDroit() {
        return dateDebutDroit;
    }

    /**
     * Récuppère la date de fin du droit à la rente
     * 
     * @return
     */
    public String getDateFinDroit() {
        return dateFinDroit;
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

    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Récupère le code de genre de diminution
     * 
     * @return
     */
    public String getGenreDiminution() {
        return genreDiminution;
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

    /**
     * Récupère le fait que la prestation est bloquée
     * 
     * @return
     */
    public Boolean getIsPrestationBloquee() {
        return isPrestationBloquee;
    }

    public String getLibelleCsCodeMutation() {

        if (JadeStringUtil.isBlankOrZero(getCsCodeMutation())) {
            return getSession().getCodeLibelle(IRERenteAccordee.CS_CODE_MUTATION_DECES_AYANT_DROIT);
        } else {
            return getSession().getCodeLibelle(getCsCodeMutation());
        }

    }

    /**
     * Récupère le montant de la rente accordée
     * 
     * @return
     */
    public String getMontant() {
        return montant;
    }

    public String getMontantRA() throws Exception {

        RERenteAccordee ra = new RERenteAccordee();
        ra.setIdPrestationAccordee(getIdRenteAccordee());
        ra.setSession(getSession());
        ra.retrieve();

        if (ra.isNew()) {
            return "";
        } else {
            return ra.getMontantPrestation();
        }

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

    /**
     * Récupération du total bloqué
     * 
     * @return
     */
    public FWCurrency getTotalBloque() {
        return totalBloque;
    }

    public String getWarningMsg() {
        return warningMsg;
    }

    /**
     * Modifie le code de mutation
     * 
     * @param newCodeMutation
     */
    public void setCsCodeMutation(String newCodeMutation) {
        csCodeMutation = newCodeMutation;
    }

    /**
     * Modifie le cote de traitement
     * 
     * @param newCodeTraitement
     */
    public void setCsCodeTraitement(String newCodeTraitement) {
        csCodeTraitement = newCodeTraitement;
    }

    /**
     * Modifie la date de début du droit à la rente
     * 
     * @param newDateDebutDroit
     */
    public void setDateDebutDroit(String newDateDebutDroit) {
        dateDebutDroit = newDateDebutDroit;
    }

    /**
     * Modifie la date de fin du droit à la rente
     * 
     * @param newDateFinDroit
     */
    public void setDateFinDroit(String newDateFinDroit) {
        dateFinDroit = newDateFinDroit;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * Modification du code de genre de diminution
     * 
     * @param newGenreDiminution
     */
    public void setGenreDiminution(String newGenreDiminution) {
        genreDiminution = newGenreDiminution;
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
     * Modifie le faite que la prestation soit bloquée
     * 
     * @param newIsPrestationBloquee
     */
    public void setIsPrestationBloquee(Boolean newIsPrestationBloquee) {
        isPrestationBloquee = newIsPrestationBloquee;
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

    /**
     * Modification du total bloqué
     * 
     * @param totalBloque
     */
    public void setTotalBloque(FWCurrency totalBloque) {
        this.totalBloque = totalBloque;
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
        boolean valid = true;
        JADate datefin = null;
        try {
            datefin = new JADate(getDateFinDroit());
        } catch (JAException e) {
            _addError(e.toString());
        }
        JADate dateDebut = null;
        FWCurrency mtDette = new FWCurrency("0", 2);
        // recherche le montant de la dette
        try {
            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(getIdRenteAccordee());
            ra.retrieve();
            if (ra.isNew()) {
                JadeLogger.error(this,
                        new Throwable("Impossible de charger la rente accordée no :".concat(getIdRenteAccordee())));
            }
            JACalendarGregorian cal = new JACalendarGregorian();
            dateDebut = cal.addMonths(new JADate("01.".concat(REPmtMensuel.getDateDernierPmt(getSession()))), 1);
            mtDette = REModuleComptableDiminution.getInstance(getSession()).getCumulDesMontantsDeA(getSession(),
                    new BTransaction(getSession()), ra,
                    PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(dateDebut.toStr(".")), getDateFinDroit());
        } catch (JAException e) {
            valid = false;
            JadeLogger.error(this,
                    new Throwable("Impossible de charger la rente accordée no :".concat(getIdRenteAccordee())));
        } catch (Exception e) {
            JadeLogger.error(this,
                    new Throwable("Impossible de charger la rente accordée no :".concat(getIdRenteAccordee())));
            valid = false;
        }

        if (valid) {
            // si date de fin, code mutation obligatoire
            if (!JAUtil.isDateEmpty(datefin.toStr(".")) && (JadeStringUtil.isBlankOrZero(getCsCodeMutation()))) {
                valid = false;
                _addError("JSP_ERROR_DIM_R_A_005");
            }

            // et vice-versa
            if (JAUtil.isDateEmpty(datefin.toStr(".")) && (!JadeStringUtil.isBlankOrZero(getCsCodeMutation()))) {
                valid = false;
                _addError("JSP_ERROR_DIM_R_A_006");
            }

            // si total bloqué différent de 0
            // si différence de mois >= 0

            try {
                JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));
                int diff = PRDateFormater.nbrMoisEntreDates(dateDernierPmt, datefin) - 1;

                if (!getTotalBloque().isZero() && diff < 0) {

                    // si montant bloqué >= montant de la dette
                    if (getTotalBloque().compareTo(mtDette) >= 0) {

                        if (!csCodeTraitement.equals(IREDiminution.CS_GENRE_TRATEMENT_DIM_RESTITUTION)) {
                            _addError("JSP_ERROR_DIM_R_A_004");
                        }

                        // si montant bloqué < montant de la dette
                    } else if (getTotalBloque().compareTo(mtDette) < 0) {

                        if (!csCodeTraitement.equals(IREDiminution.CS_GENRE_TRATEMENT_DIM_RESTITUTION)
                                && !csCodeTraitement
                                        .equals(IREDiminution.CS_GENRE_TRATEMENT_DIM_RESTITUTION_PAR_FACTURATION)) {
                            _addError("JSP_ERROR_DIM_R_A_004");
                        }

                    }
                }

            } catch (JAException e1) {
                valid = false;
                addErrorAvecMessagePret(e1.toString());
            }

        }
        return valid;
    }
}
