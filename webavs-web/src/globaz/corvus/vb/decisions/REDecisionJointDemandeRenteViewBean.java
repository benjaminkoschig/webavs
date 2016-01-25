package globaz.corvus.vb.decisions;

import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.decisions.REValidationDecisionsManager;
import globaz.corvus.db.prestations.IREPrestatationsDecisionsRCListViewBean;
import globaz.corvus.db.rentesaccordees.REDecisionJointDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.nnss.PRNSSUtil;
import java.util.Iterator;
import java.util.Vector;

public class REDecisionJointDemandeRenteViewBean extends REDecisionJointDemandeRente implements
        IREPrestatationsDecisionsRCListViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String LABEL_NON_VALIDE = "NON_VALIDE";

    private String dateDebutAffichage = "";
    private String dateFinAffichage = "";
    private String genrePrestationAffichage = "";
    private String orderBy;

    public String getCsEtatDecisionLibelle() {
        return getSession().getCodeLibelle(getCsEtat());
    }

    public String getCsTypeDecisionLibelle() {
        if (IREDecision.CS_TYPE_DECISION_STANDARD.equals(getCsTypeDecision())) {
            return "";
        } else {
            return getSession().getCodeLibelle(getCsTypeDecision());
        }
    }

    /**
     * Méthode qui retourne la date de début la plus ancienne de la décision
     * 
     * @throws Exception
     */
    public String getDateDebut() throws Exception {

        JADate firstDate = new JADate("31.12.9999");
        JACalendar cal = new JACalendarGregorian();

        REValidationDecisionsManager mgr = new REValidationDecisionsManager();
        mgr.setForIdDecision(getIdDecision());
        mgr.setSession(getSession());
        mgr.find();

        for (Iterator<REValidationDecisions> iterator = mgr.iterator(); iterator.hasNext();) {
            REValidationDecisions entity = iterator.next();

            REPrestationsDuesManager mgr1 = new REPrestationsDuesManager();
            mgr1.setSession(getSession());
            mgr1.setForIdPrestationDue(entity.getIdPrestationDue());
            mgr1.find();

            REPrestationDue ent = (REPrestationDue) mgr1.getFirstEntity();

            if (cal.compare(firstDate, new JADate(ent.getDateDebutPaiement())) == JACalendar.COMPARE_FIRSTUPPER) {
                firstDate = new JADate(ent.getDateDebutPaiement());
            }

        }

        return PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(firstDate.toStrAMJ());
    }

    @Override
    public String getDateDebutAffichage() {
        return dateDebutAffichage;
    }

    @Override
    public String getDateFinAffichage() {
        return dateFinAffichage;
    }

    /**
     * Méthode qui retourne le détail du requérant formaté pour les listes
     * 
     * @return le détail du requérant formaté
     */
    public String getDetailRequerant() {
        return PRNSSUtil.formatDetailRequerantListe(getNssRequerant(), getNomRequerant() + " " + getPrenomRequerant(),
                getDateNaissanceRequerant(), getLibelleCourtSexe(), getLibellePays());

    }

    public Vector getEtatsDecisions() {
        Vector<String[]> etatsDemande = PRCodeSystem.getLibellesPourGroupe(IREDecision.CS_GROUPE_ETAT_DECISION,
                getSession());

        // ajout des options custom
        etatsDemande.add(
                1,
                new String[] { REDecisionJointDemandeRenteViewBean.LABEL_NON_VALIDE,
                        getSession().getLabel("JSP_DRE_R_DEMANDES_NON_VALIDE") });
        etatsDemande.add(0, new String[] { "", "" });

        return etatsDemande;
    }

    @Override
    public String getGenrePrestationAffichage() {
        return genrePrestationAffichage;
    }

    /**
     * Méthode qui retourne le libellé court du sexe par rapport au csSexe qui est dans le vb
     * 
     * @return le libellé court du sexe (H ou F)
     */
    public String getLibelleCourtSexe() {
        if (PRACORConst.CS_HOMME.equals(getCsSexeRequerant())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_HOMME");
        } else if (PRACORConst.CS_FEMME.equals(getCsSexeRequerant())) {
            return getSession().getLabel("JSP_LETTRE_SEXE_FEMME");
        } else {
            return "";
        }
    }

    /**
     * Méthode qui retourne le libellé de la nationalité par rapport au csNationalité qui est dans le vb
     * 
     * @return le libellé du pays (retourne une chaîne vide si pays inconnu)
     */
    public String getLibellePays() {
        if ("999".equals(getSession().getCode(getSession().getSystemCode("CIPAYORI", getCsNationaliteRequerant())))) {
            return "";
        } else {
            return getSession().getCodeLibelle(getSession().getSystemCode("CIPAYORI", getCsNationaliteRequerant()));
        }
    }

    public String getOrderBy() {
        return orderBy;
    }

    @Override
    public void setDateDebutAffichage(String dateDebutAffichage) {
        this.dateDebutAffichage = dateDebutAffichage;
    }

    @Override
    public void setDateFinAffichage(String dateFinAffichage) {
        this.dateFinAffichage = dateFinAffichage;
    }

    @Override
    public void setGenrePrestationAffichage(String genrePrestationAffichage) {
        this.genrePrestationAffichage = genrePrestationAffichage;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
