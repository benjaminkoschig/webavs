package globaz.corvus.vb.decisions;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.vb.interetsmoratoires.REInteretMoratoireListViewBean;
import globaz.corvus.vb.interetsmoratoires.REInteretMoratoireViewBean;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;

/**
 * ViewBean pour la préparation standard de décision (sans cas particulier)
 */
public class REPreparerDecisionStandardViewBean extends REPreparerDecisionViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDecision;
    private String decisionDu;
    private String idTiersRequerant;
    private Boolean isDemandeCourantValide;
    private String testRetenue;

    public REPreparerDecisionStandardViewBean() {
        this(null, null);
    }

    public REPreparerDecisionStandardViewBean(Long idDemandeRente, String detaiRequerant) {
        super(idDemandeRente, REPreparerDecisionViewBean.PAGE_PREPARATION_DECISION_STANDARD, detaiRequerant);

        dateDecision = null;
        decisionDu = null;
        idTiersRequerant = null;
        isDemandeCourantValide = Boolean.FALSE;
        testRetenue = null;
    }

    /**
     * @return Vrai s'il existe des intérêts moratoires pour la demande de rente pour le même mois que le mois de
     *         décision
     */
    public boolean areIMCalculated() {

        REInteretMoratoireListViewBean imManager = new REInteretMoratoireListViewBean();
        imManager.setSession(getSession());
        imManager.setForIdDemandeRente(getIdDemandeRente().toString());

        try {
            // si les IM ne sont pas du même mois que la décision, il faut les recalculer
            if (imManager.getCount() > 0) {

                imManager.find();
                REInteretMoratoireViewBean im = (REInteretMoratoireViewBean) imManager.getFirstEntity();

                JADate dateCalcul = new JADate(im.getDateCalculIM());
                JADate dateDecison = new JADate(getDateDecision());

                if ((dateCalcul.getYear() == dateDecison.getYear())
                        && (dateCalcul.getMonth() == dateDecison.getMonth())) {
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentDate() {
        return JACalendar.todayJJsMMsAAAA();
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public String getDecisionDu() {
        return decisionDu;
    }

    public String getIdTiersRequerant() {
        return idTiersRequerant;
    }

    public Boolean getIsDemandeCourantValide() throws Exception {

        if (!isDemandeCourantValide) {

            Long idDemandeRente = getIdDemandeRente();

            if (idDemandeRente != null) {

                REDemandeRenteJointDemande dem = new REDemandeRenteJointDemande();
                dem.setSession(getSession());
                dem.setIdDemandeRente(idDemandeRente.toString());
                dem.retrieve();

                if (dem.getCsEtatDemande().equals(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)) {
                    isDemandeCourantValide = true;
                }
            }
        }

        return isDemandeCourantValide;
    }

    public String[] getMoisAnneeCourantEtSuivant() {
        JADate dateJour = null;
        try {
            dateJour = new JADate(REPmtMensuel.getDateDernierPmt(getSession()));
        } catch (Exception e) {
        }
        String moisAnneeCourant = "";
        String moisAnneeSuivant = "";

        String moisCourant = "";
        String moisSuivant = "";

        boolean isMoisCourantEqualAnneeSuivante = false;
        boolean isMoisSuivantEqualAnneeSuivante = false;

        if (dateJour.getMonth() > 12) {
            isMoisCourantEqualAnneeSuivante = true;
        }

        if ((dateJour.getMonth() + 1) > 12) {
            isMoisSuivantEqualAnneeSuivante = true;
        }

        if (dateJour.getMonth() < 10) {
            moisCourant = "0" + dateJour.getMonth();
        } else {
            moisCourant = String.valueOf(dateJour.getMonth());
        }

        if (isMoisCourantEqualAnneeSuivante) {
            moisAnneeCourant = "01." + String.valueOf(dateJour.getYear() + 1);
        } else {
            moisAnneeCourant = moisCourant + "." + dateJour.getYear();
        }

        moisSuivant = String.valueOf(dateJour.getMonth() + 1);
        if (moisSuivant.length() < 2) {
            moisSuivant = "0" + moisSuivant;
        }

        if (isMoisSuivantEqualAnneeSuivante) {
            moisAnneeSuivant = "01." + String.valueOf(dateJour.getYear() + 1);
        } else {
            moisAnneeSuivant = moisSuivant + "." + dateJour.getYear();
        }

        String[] moisAnneeCourantEtSuivant = { moisAnneeCourant, moisAnneeSuivant };

        return moisAnneeCourantEtSuivant;

    }

    public String getTestRetenue() {
        return testRetenue;
    }

    /**
     * @return Vrai s'il faut calculer des IM pour cette demande de rente
     */
    public boolean isDemandeRenteWithIM() {
        REDemandeRente dr = new REDemandeRente();
        try {
            dr.setSession(getSession());
            dr.setIdDemandeRente(getIdDemandeRente().toString());
            dr.retrieve();

            JACalendar cal = new JACalendarGregorian();
            JADate debutIMNormal = cal.addMonths(new JADate(dr.getDateDebut()), 24);
            JADate debutIMTardif = cal.addMonths(new JADate(dr.getDateDepot()), 12);
            JADate debutIM = null;

            if (BSessionUtil.compareDateFirstGreater(getSession(), debutIMNormal.toStr("."), debutIMTardif.toStr("."))) {
                debutIM = debutIMNormal;
            } else {
                debutIM = debutIMTardif;
            }

            JADate dateDecison = new JADate(getDateDecision());

            if (dateDecison.getYear() > debutIM.getYear()) {
                return true;
            } else if (dateDecison.getYear() == debutIM.getYear()) {

                if (dateDecison.getMonth() >= debutIM.getMonth()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public void setDecisionDu(String decisionDu) {
        this.decisionDu = decisionDu;
    }

    public void setIdTiersRequerant(String idTiersRequerant) {
        this.idTiersRequerant = idTiersRequerant;
    }

    public void setIsDemandeCourantValide(Boolean isDemandeCourantValide) {
        this.isDemandeCourantValide = isDemandeCourantValide;
    }

    public void setTestRetenue(String testRetenue) {
        this.testRetenue = testRetenue;
    }
}
