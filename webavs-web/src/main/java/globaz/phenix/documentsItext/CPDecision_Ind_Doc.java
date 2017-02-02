package globaz.phenix.documentsItext;

import globaz.babel.api.ICTDocument;
import globaz.caisse.report.helper.CaisseHeaderReportBean;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.listes.itext.CPIListeDecisionParam;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.Constante;
import globaz.pyxis.constantes.IConstantes;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Date de création : (26.02.2003 16:54:19)
 * 
 * @author: Administrator
 */
public class CPDecision_Ind_Doc extends CPIDecision_Doc implements Constante {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static Vector<String> fr = new Vector<String>();
    private static Vector<String> lRevenu = new Vector<String>();
    private static Vector<String> mRevenu = new Vector<String>();
    private static Vector<String> operation = new Vector<String>();
    private static Vector<String> rRevenu = new Vector<String>();
    private CPPeriodeFiscale perFis = null;

    /**
     * Date de création : (26.02.2003 16:56:39)
     */
    public CPDecision_Ind_Doc() throws Exception {
        super();
        // this(new
        // BSession(globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    /**
     * Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            BProcess
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPDecision_Ind_Doc(BProcess parent) throws java.lang.Exception {
        super(parent);
    }

    /**
     * Date de création : (26.02.2003 17:00:08)
     * 
     * @param session
     *            globaz.globall.db.BSession
     * @exception java.lang.Exception
     *                La description de l'exception.
     */
    public CPDecision_Ind_Doc(BSession session) throws java.lang.Exception {
        super(session);
    }

    /**
     * Surcharge :
     * 
     * @see globaz.phenix.documentsItext.CPIDecision_Doc#_headerText(globaz.caisse.report.helper.CaisseHeaderReportBean)
     * @param headerBean
     */
    @Override
    protected void _headerText(CaisseHeaderReportBean headerBean) {
        // On récupère les documents du catalogue de textes nécessaires
        decisionsInd = getICTDecisionInd();
        try {
            // Période fiscale
            CPPeriodeFiscale perfis = new CPPeriodeFiscale();
            perfis.setSession(getSession());
            if (!JadeStringUtil.isIntegerEmpty(getDecision().getIdIfdProvisoire())) {
                perfis.setIdIfd(getDecision().getIdIfdProvisoire());
            } else {
                perfis.setIdIfd(getDecision().getIdIfdDefinitif());
            }
            perfis.retrieve();
            setPerFis(perfis);
            // Chargement headerbean
            setHeaderBean(headerBean);
            // Si acompte
            if (CPDecision.CS_ACOMPTE.equalsIgnoreCase(decision.getTypeDecision()) && !isAcompteDetailCalcul()) {
                String texteSource = " ";
                String periodeDecision = getDecision().getDebutDecision() + " - " + getDecision().getFinDecision();
                // Décision provioire.... 01.01.2007 - 31.12.2007
                texteSource = this.getTexteDocument(acomptesInd, acompteInd, 1, 1, _getTitre(periodeDecision))
                        .toString();
                // Commentaires
                texteSource = texteSource
                        + this.getTexteDocument(acomptesInd, acompteInd, 2, 1, _getCommentaire()).toString();
                // Source des données
                texteSource = texteSource
                        + this.getTexteDocument(acomptesInd, acompteInd, 3, 1, _getSource(getDonneeBase())).toString();
                // recherche et insertion du revenu déterminant
                String revenuNet = "";
                revenuNet = getDonneeCalcul().getMontant(decision.getIdDecision(), CPDonneesCalcul.CS_REV_NET);
                if (JadeStringUtil.isIntegerEmpty(revenuNet)) {
                    texteSource = texteSource + this.getTexteDocument(acomptesInd, acompteInd, 4, 2, " ").toString();
                } else {
                    texteSource = texteSource
                            + this.getTexteDocument(acomptesInd, acompteInd, 4, 1, revenuNet).toString();
                }

                texteSource = texteSource
                        + this.getTexteDocument(acomptesInd, acompteInd, 5, 1, _getRemarque()).toString();
                super.setParametres(CPIListeDecisionParam.PARAM_ACOMPTEIND_TEXTESOURCE, texteSource);
                libFranc = this.getTexteDocument(decisionsGeneriqueTemp, decisionGenerique, 3, 1, " ").toString();
                // super.setParametres("P_INFO_CAISSE",
                // getTexteDocument(decisionsGeneriqueTemp, decisionGenerique,
                // 4, 1, " ").toString());
            } else {
                // Zones communes aux décisions
                _headerCommun();
                // Décription des revenus de l'affilié
                if (getDuplicata()) {
                    if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(decision.getLangue())) {
                        super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_DE, Boolean.TRUE);
                        super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_FR, Boolean.FALSE);
                    } else {
                        super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_DE, Boolean.FALSE);
                        super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_FR, Boolean.TRUE);
                    }
                } else {
                    super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_DE, Boolean.FALSE);
                    super.setParametres(CPIListeDecisionParam.PARAM_DUPLICATA_FR, Boolean.FALSE);
                }
                if (!JadeStringUtil.isBlank(getDonneeCalcul().getMontant(getDecision().getIdDecision(),
                        CPDonneesCalcul.CS_REV_BRUT))) {
                    // Description revenus + cotisations
                    setDetailRevenu();
                    super.setParametres("P_SIGNEOPER", getOperation());
                    super.setParametres("P_LREVENU", getLRevenu());
                    super.setParametres("P_FRANC", getLibelleFranc());
                    super.setParametres("P_MREVENU", getMRevenu());
                    super.setParametres("P_RREVENU", getRRevenu());
                    // Décription du revenu brut + capital
                    setDetailRevenuBrut();
                } else {
                    // Description revenu - franchise - capital
                    setDetailRevenuModeApresRevisionCotisation();
                    super.setParametres("P_SIGNEOPER", getOperation());
                    super.setParametres("P_LREVENU", getLRevenu());
                    super.setParametres("P_FRANC", getLibelleFranc());
                    super.setParametres("P_MREVENU", getMRevenu());
                    super.setParametres("P_RREVENU", getRRevenu());
                    // Description du revenu sans cotisation + cotisation
                    setDetailRevenuSansCotisation();
                }

                super.setParametres("P_SIGNEOPERB", getOperation());
                super.setParametres("P_LREVENUB", getLRevenu());
                super.setParametres("P_FRANCB", getLibelleFranc());
                super.setParametres("P_MREVENUB", getMRevenu());
                super.setParametres("P_RREVENUB", getRRevenu());
                // Description du revenu déterminant
                setDetailRevenuNet();
                super.setParametres("P_SIGNEOPERN", getOperation());
                super.setParametres("P_LREVENUN", getLRevenu());
                super.setParametres("P_FRANCN", getLibelleFranc());
                super.setParametres("P_MREVENUN", getMRevenu());
                super.setParametres("P_RREVENUN", getRRevenu());
            }
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
        }
    }

    /**
     * Initialise les documents Surcharge :
     * 
     * @see globaz.phenix.documentsItext.CPIDecision_Doc#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() {
        super.beforeExecuteReport();
        // On va initialiser les documents
        try {
            decisionInd = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
            acompteInd = (ICTDocument) getSession().getAPIFor(ICTDocument.class);
        } catch (Exception e) {
            getMemoryLog().logMessage(e.toString(), FWMessage.ERREUR, this.getClass().getName());
        }
    }

    /**
     * Rempli la désignation du franc - Fr. Date de création : (02.05.2003 11:59:50)
     * 
     * @parm
     * @return java.lang.String
     */
    public java.lang.String getLibelleFranc() {
        // String collection
        Enumeration<String> enum1 = CPDecision_Ind_Doc.fr.elements();
        String libFr = "";
        if (enum1 != null) {
            while (enum1.hasMoreElements()) {
                libFr += enum1.nextElement() + "\n";
            }
        }
        return libFr;
    }

    /**
     * Retourne les libellés des revenus du calcul Date de création : (02.05.2003 11:59:50)
     * 
     * @parm
     * @return java.lang.String
     */
    public java.lang.String getLRevenu() {
        // String collection
        Enumeration<String> enum1 = CPDecision_Ind_Doc.lRevenu.elements();
        String libelleRevenu = "";
        if (enum1 != null) {
            while (enum1.hasMoreElements()) {
                libelleRevenu += enum1.nextElement() + "\n";
            }
        }
        return libelleRevenu;
    }

    /**
     * Retourne les montants des revenus du calcul Date de création : (02.05.2003 11:59:50)
     * 
     * @parm
     * @return java.lang.String
     */
    public java.lang.String getMRevenu() {
        // String collection
        Enumeration<String> enum1 = CPDecision_Ind_Doc.mRevenu.elements();
        String montantRevenu = "";
        if (enum1 != null) {
            while (enum1.hasMoreElements()) {
                montantRevenu += enum1.nextElement() + "\n";
            }
        }
        return montantRevenu;
    }

    /**
     * Retourne les signes de sopérations du calcul Date de création : (02.05.2003 11:59:50)
     * 
     * @parm
     * @return java.lang.String
     */
    public java.lang.String getOperation() {
        // String collection
        Enumeration<String> enum1 = CPDecision_Ind_Doc.operation.elements();
        String oper = "";
        if (enum1 != null) {
            while (enum1.hasMoreElements()) {
                oper += enum1.nextElement() + "\n";
            }
        }
        return oper;
    }

    /**
     * Returns the perFis.
     * 
     * @return CPPeriodeFiscale
     */
    public CPPeriodeFiscale getPerFis() {
        return perFis;
    }

    /**
     * Retourne les remarques sur les revenus du calcul Date de création : (02.05.2003 11:59:50)
     * 
     * @parm
     * @return java.lang.String
     */
    public java.lang.String getRRevenu() {
        // String collection
        Enumeration<String> enum1 = CPDecision_Ind_Doc.rRevenu.elements();
        String remarqueRevenu = "";
        if (enum1 != null) {
            while (enum1.hasMoreElements()) {
                remarqueRevenu += enum1.nextElement();
                if (remarqueRevenu.length() < 60) {
                    remarqueRevenu += "\n";
                }
            }
        }
        return remarqueRevenu;
    }

    /**
     * Stocke les libellés et montant des divers revenus Utilisée pour afficher le descriptif du calcul lors de
     * l'édition des décisions pour les cas qui ont été générées avant la modif du calcul des cotisations qui
     * a nécessité une révision de l'ordre des informations du calcul
     * Cette méthode devra être supprimée lorsque les cas en cours auront été comptabilisés
     */
    public void setDetailRevenu() {
        // remise à zéro des vecteurs d'impression
        clearTableauInfoLigne();
        try {
            if (!getDecision().getAnneePrise().equalsIgnoreCase("2")) {
                // Revenu 1
                ajoutDetailLigneRevenu1();
                // Revenu autre ou agricole
                ajoutDetailLigneRevenuAutreouAgricole();
                // Cotisation 1
                ajoutDetailLigneCotisation1();
            }
            if (!getDecision().getAnneePrise().equalsIgnoreCase("1")) {
                // Revenu 2
                ajoutDetailLigneRevenu2();
                // Revenu autre
                ajoutDetailLigneReveuAutreouAgricole2();
                // Cotisation 2
                ajoutDetailLigneCotisation2();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e.getMessage() + " - Tiers: " + decision.getNumeroAffilie());
        }
    }

    /**
     * Stocke les libellés et montant des divers revenus Utilisée pour afficher le descriptif du calcul lors de
     * l'édition des décisions pour les cas qui ont été générées après la modif du calcul des cotisations qui
     * a nécessité une révision de l'ordre des informations du calcul
     */
    public void setDetailRevenuModeApresRevisionCotisation() {
        // remise à zéro des vecteurs d'impression
        clearTableauInfoLigne();
        try {
            if (!getDecision().getAnneePrise().equalsIgnoreCase("2")) {
                // Revenu 1
                ajoutDetailLigneRevenu1();
                // Revenu autre ou agricole
                ajoutDetailLigneRevenuAutreouAgricole();
            }
            if (!getDecision().getAnneePrise().equalsIgnoreCase("1")) {
                // Revenu 2
                ajoutDetailLigneRevenu2();
                // Revenu autre
                ajoutDetailLigneReveuAutreouAgricole2();
            }

            // Rachat LPP
            ajoutDetailRachatLPP();

            // Franchise
            ajoutDetailLigneFranchise();
            // intéret
            ajoutDetailLigneInteretCapital();

        } catch (Exception e) {
            JadeLogger.error(this, e.getMessage() + " - Tiers: " + decision.getNumeroAffilie());
        }
    }

    public void clearTableauInfoLigne() {
        CPDecision_Ind_Doc.operation.clear();
        CPDecision_Ind_Doc.lRevenu.clear();
        CPDecision_Ind_Doc.fr.clear();
        CPDecision_Ind_Doc.mRevenu.clear();
        CPDecision_Ind_Doc.rRevenu.clear();
    }

    public void ajoutDetailRachatLPP() {
        if (!JadeStringUtil.isEmpty(getDonneeBase().getRachatLPP())) {
            String varTemp;

            CPDecision_Ind_Doc.operation.add("./.");

            CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 12, " ").toString());
            CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 12, libFranc).toString());

            varTemp = getDonneeBase().getRachatLPP();

            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 12, varTemp).toString());
            CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 14, " ").toString());
        }
    }

    public void ajoutDetailLigneCotisation2() {
        if (!JadeStringUtil.isIntegerEmpty(getDonneeBase().getCotisation2())) {
            CPDecision_Ind_Doc.operation.add("+");
            CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 3,
                    perFis.getAnneeRevenuFin()).toString());
            CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 3, libFranc).toString());
            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 3,
                    JANumberFormatter.fmt(getDonneeBase().getCotisation2(), true, false, true, 2)).toString());
            CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 3, " ").toString());
        }
    }

    public void ajoutDetailLigneReveuAutreouAgricole2() {
        if (!JadeStringUtil.isEmpty(getDonneeBase().getRevenuAutre2())) {
            CPDecision_Ind_Doc.operation.add("+");
            CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 2,
                    perFis.getAnneeRevenuFin()).toString());
            CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 2, libFranc).toString());
            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 2,
                    getDonneeBase().getRevenu2()).toString());
            CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 2, " ").toString());
        }
    }

    public void ajoutDetailLigneRevenu2() {
        String varTemp;
        if (getDecision().getAnneePrise().equalsIgnoreCase("T")) {
            CPDecision_Ind_Doc.operation.add("+");
        } else {
            CPDecision_Ind_Doc.operation.add(" ");
        }
        CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 1,
                perFis.getAnneeRevenuFin()).toString());
        CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 1, libFranc).toString());
        if (JadeStringUtil.isEmpty(getDonneeBase().getRevenu2())) {
            varTemp = "0";
        } else {
            varTemp = getDonneeBase().getRevenu2();
        }
        CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 1, varTemp).toString());
        CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 1, " ").toString());
    }

    public void ajoutDetailLigneCotisation1() throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getDonneeBase().getCotisation1())) {
            CPDecision_Ind_Doc.operation.add("+");
            CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 3,
                    perFis.getAnneeRevenuDebut()).toString());
            CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 3, libFranc).toString());
            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 3,
                    JANumberFormatter.fmt(getDonneeBase().getCotisation1(), true, false, true, 2)).toString());
            // D0108 - Libellé si la cotisation a été déterminée automatiquement lors du calcul de la
            // communication fiscale
            if (CPToolBox.isCotisationCalculeeSelonDispositionLegale(getAffiliation().getTypeAffiliation(),
                    decision.getIdCommunication(), getDonneeBase().getCotisation1())) {
                CPDecision_Ind_Doc.rRevenu.add(getSession().getApplication().getLabel("CP_COTISATION_CALCULEE_AUTO",
                        langueDoc));
            } else {
                CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 3, " ").toString());
            }
        }
    }

    public void ajoutDetailLigneCotisation1ApresRevisionCotisation() throws Exception {
        CPDecision_Ind_Doc.operation.add("+");
        CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 3,
                perFis.getAnneeRevenuDebut()).toString());
        CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 3, libFranc).toString());
        CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 3,
                JANumberFormatter.fmt(getDonneeBase().getCotisation1(), true, false, false, 2)).toString());
        // D0108 - Libellé si la cotisation a été déterminée automatiquement lors du calcul de la
        // communication fiscale
        if (CPToolBox.isCotisationCalculeeSelonDispositionLegale(getAffiliation().getTypeAffiliation(),
                decision.getIdCommunication(), getDonneeBase().getCotisation1())) {
            CPDecision_Ind_Doc.rRevenu.add(getSession().getApplication().getLabel("CP_COTISATION_CALCULEE_AUTO",
                    langueDoc));
        } else {
            CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 3, " ").toString());
        }
    }

    public void ajoutDetailLigneRevenuAutreouAgricole() {
        if (!JadeStringUtil.isEmpty(getDonneeBase().getRevenuAutre1())) {
            CPDecision_Ind_Doc.operation.add("+");
            CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 2,
                    perFis.getAnneeRevenuDebut()).toString());
            CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 2, libFranc).toString());
            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 2,
                    getDonneeBase().getRevenuAutre1()).toString());
            CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 2, " ").toString());
        }
    }

    public void ajoutDetailLigneRevenu1() {
        String varTemp;
        CPDecision_Ind_Doc.operation.add(" ");
        CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 1,
                perFis.getAnneeRevenuDebut()).toString());
        CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 1, libFranc).toString());
        if (JadeStringUtil.isEmpty(getDonneeBase().getRevenu1())) {
            varTemp = "0";
        } else {
            varTemp = getDonneeBase().getRevenu1();
        }
        CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 1, varTemp).toString());
        CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 1, " ").toString());
    }

    public void setDetailRevenuBrut() {
        // remise à zéro des vecteurs d'impression
        String varTemp = "";
        clearTableauInfoLigne();
        try {
            if (getDecision() != null) {
                // toolBox.getLibelleCodeSystem() = Recherche d'un libellé selon
                // une langue et non selon
                // la langue du système... devrait être dans le framework
                // Revenu couple
                varTemp = getDonneeCalcul().getMontant(getDecision().getIdDecision(), CPDonneesCalcul.CS_REV_COUPLE);
                if (!JadeStringUtil.isEmpty(varTemp)) {
                    ajoutDetailLigneRevenuCouple(varTemp);
                } else {
                    // Revenu brut
                    CPDecision_Ind_Doc.operation.add("=");
                }
                CPDecision_Ind_Doc.lRevenu.add(this
                        .getTexteDocument(
                                decisionsInd,
                                decisionInd,
                                1,
                                5,
                                CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_REV_BRUT,
                                        decision.getLangue())).toString());
                CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 5, libFranc).toString());
                varTemp = getDonneeCalcul().getMontant(getDecision().getIdDecision(), CPDonneesCalcul.CS_REV_BRUT);
                if (JadeStringUtil.isEmpty(varTemp)) {
                    varTemp = ("0");
                }
                CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 5, varTemp)
                        .toString());
                CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 5, " ").toString());
                // Ajout info revenu moyen
                ajoutDetailLigneRevenuMoyen();
                // Franchise pour rentier
                ajoutDetailLigneFranchise();
                // interêt du capital
                ajoutDetailLigneInteretCapital();
            }
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
        }
    }

    public void setDetailRevenuSansCotisation() {
        // remise à zéro des vecteurs d'impression
        clearTableauInfoLigne();
        try {
            if (getDecision() != null) {
                // Revenu net sans cotisation
                ajoutDetailLigneRevenuNetSansCotisation();
                // Cotisation 1
                ajoutDetailLigneCotisation1ApresRevisionCotisation();
                // Cotisation 2
                ajoutDetailLigneCotisation2();
            }
        } catch (Exception e) {
            getTransaction().addErrors(e.getMessage());
        }
    }

    public void ajoutDetailLigneRevenuNetSansCotisation() throws Exception {
        String varTemp;
        CPDecision_Ind_Doc.operation.add("=");
        // revenu
        CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(
                decisionsInd,
                decisionInd,
                1,
                5,
                CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_REV_SANS_COTISATION,
                        decision.getLangue())).toString());
        CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 5, libFranc).toString());
        varTemp = getDonneeCalcul().getMontant(getDecision().getIdDecision(), CPDonneesCalcul.CS_REV_SANS_COTISATION);
        if (JadeStringUtil.isEmpty(varTemp)) {
            varTemp = ("0");
        }
        CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 5, varTemp).toString());
        CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 5, " ").toString());
    }

    public void ajoutDetailLigneRevenuCouple(String varTemp) throws Exception {
        CPDecision_Ind_Doc.operation.add("=");
        CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 4,
                CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_REV_COUPLE, decision.getLangue()))
                .toString());
        CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 4, libFranc).toString());
        CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 4, varTemp).toString());
        CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 4, " ").toString());
        CPDecision_Ind_Doc.operation.add(" ");
    }

    public void ajoutDetailLigneInteretCapital() throws Exception {
        String varTemp;
        CPDecision_Ind_Doc.operation.add("./.");
        CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 8,
                CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_INTERET, decision.getLangue()))
                .toString());
        CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 8, libFranc).toString());
        varTemp = getDonneeCalcul().getMontant(getDecision().getIdDecision(), CPDonneesCalcul.CS_INTERET);
        String capitalInvesti = JANumberFormatter.deQuote(getDonneeBase().getCapital());
        int capital = 0;
        if (JadeStringUtil.isEmpty(capitalInvesti)) {
            capital = 0;
        } else {
            capital = Integer.parseInt(capitalInvesti);
        }
        if (capital == 0) {
            varTemp = ("0");
            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 8, varTemp).toString());
            CPDecision_Ind_Doc.rRevenu.add(" ");
        } else {
            if (JadeStringUtil.isEmpty(varTemp)) {
                varTemp = ("0");
            }
            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 8, varTemp).toString());
            // int capital =
            // Integer.parseInt(JANumberFormatter.deQuote(donneeBase.getCapital()));
            String varTemp1 = "";
            String varTemp2 = "";
            // Formatage texte prorata Ex: Prorata de 15/15 ème de
            String nbMois = CPToolBox.nbMoisPeriode(getDonneeBase().getDebutExercice1(), getDonneeBase()
                    .getFinExercice1());
            if (!"12".equalsIgnoreCase(nbMois)) {
                varTemp = this.getTexteDocument(decisionsInd, decisionInd, 4, 12, nbMois).toString();
            } else {
                varTemp = "";
            }
            // Formatage texte pour capital ex:2.50% de 156'125
            varTemp1 = this.getTexteDocument(
                    decisionsInd,
                    decisionInd,
                    4,
                    8,
                    JANumberFormatter
                            .fmt(getDonneeCalcul().getMontant(getDecision().getIdDecision(),
                                    CPDonneesCalcul.CS_TAUX_INTCAP), true, false, true, 2)).toString();
            varTemp1 = format(new StringBuffer(varTemp1), getDonneeBase().getCapital()).toString();
            // Selon le cas, formatage du texte sur l'arrondi Ex:
            // (Arrondi au 1000 CHF supèrieur)
            int modulo = 1000;
            if ((capital % modulo) == 0) {
                varTemp2 = "";
            } else {
                varTemp2 = this.getTexteDocument(decisionsInd, decisionInd, 4, 13, " ").toString();
            }
            CPDecision_Ind_Doc.rRevenu.add(varTemp + " " + varTemp1 + " " + varTemp2);

        }
    }

    public void ajoutDetailLigneFranchise() throws Exception {
        String varTemp;
        varTemp = getDonneeCalcul().getMontant(getDecision().getIdDecision(), CPDonneesCalcul.CS_FRANCHISE);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            CPDecision_Ind_Doc.operation.add("./.");
            CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 7,
                    CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_FRANCHISE, decision.getLangue()))
                    .toString());
            CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 7, libFranc).toString());
            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 7, varTemp).toString());
            CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 7, " ").toString());
        }
    }

    public void ajoutDetailLigneRevenuMoyen() throws Exception {
        String varTemp;
        if (getDecision().getAnneePrise().equalsIgnoreCase("T")) {
            CPDecision_Ind_Doc.operation.add(" ");
            CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 6,
                    CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_REV_MOYEN, decision.getLangue()))
                    .toString());
            CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 6, libFranc).toString());
            varTemp = getDonneeCalcul().getMontant(getDecision().getIdDecision(), CPDonneesCalcul.CS_REV_MOYEN);
            if (JadeStringUtil.isEmpty(varTemp)) {
                varTemp = ("0");
            }
            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 6, varTemp).toString());
            CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 6, " ").toString());
        }
    }

    /**
     * Stocke les libellés et montants du paragraphe pour le revenu net Utilisée pour afficher le descriptif du calcul
     * lors de l'édition des décisions
     */
    public void setDetailRevenuNet() {
        clearTableauInfoLigne();
        try {
            if (getDecision() != null) {
                // Insertion du revenu avant prorata lorsque l'exercice est à
                // cheval sur 2 années...
                ajoutDetailLigneRevenuAvantProrata();
                // Insertion revenu net non arrondi si il est pas divisible par
                // 100
                // ( afin de ne pas avoir deux fois le même montant avec le
                // revenu net..
                ajoutDetailLigneRevenuNetNonArrondi();
                // Revenu net
                ajoutDetailLigneRevenuNet();
            }
        } catch (Exception e) {
            JadeLogger.error(this, e.getMessage() + " - Tiers: " + decision.getNumeroAffilie());
        }
    }

    public void ajoutDetailLigneRevenuNet() throws Exception {
        String varTemp;
        varTemp = getDonneeCalcul().getMontant(getDecision().getIdDecision(), CPDonneesCalcul.CS_REV_NET);
        CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 11,
                CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_REV_NET, decision.getLangue()))
                .toString());
        CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 11, libFranc).toString());
        if (JadeStringUtil.isEmpty(varTemp)) {
            varTemp = "0";
        }
        CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 11, varTemp).toString());
        CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 11, " ").toString());
    }

    public void ajoutDetailLigneRevenuNetNonArrondi() throws Exception {
        String varTemp;
        varTemp = getDonneeCalcul().getMontant(getDecision().getIdDecision(), CPDonneesCalcul.CS_REV_NET_NONARRONDI);
        if (!JadeStringUtil.isEmpty(varTemp)) {
            String libelleRevenu = CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_REV_NET_NONARRONDI,
                    decision.getLangue());
            if (JadeStringUtil.isBlank(getDonneeCalcul().getMontant(getDecision().getIdDecision(),
                    CPDonneesCalcul.CS_REV_BRUT))) {
                libelleRevenu = CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_REV_NON_ARRONDI,
                        decision.getLangue());
            }
            CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 1, 10, libelleRevenu)
                    .toString());
            CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 10, libFranc).toString());
            if (JadeStringUtil.isEmpty(varTemp)) {
                varTemp = "0";
            }
            CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 10, varTemp).toString());
            CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 10, " ").toString());
            CPDecision_Ind_Doc.operation.add(" ");
        }
    }

    public void ajoutDetailLigneRevenuAvantProrata() throws Exception {
        String varTemp;
        CPDecision_Ind_Doc.operation.add("=");
        varTemp = getDonneeCalcul().getMontant(getDecision().getIdDecision(), CPDonneesCalcul.CS_REV_NET_AVANT_PRORATA);
        if (!JadeStringUtil.isEmpty(JANumberFormatter.deQuote(varTemp))) {
            float varFloat = Float.parseFloat(JANumberFormatter.deQuote(varTemp));
            if (varFloat > 0) {
                CPDecision_Ind_Doc.lRevenu.add(this.getTexteDocument(
                        decisionsInd,
                        decisionInd,
                        1,
                        9,
                        CPToolBox.getLibelleCodeSystem(getSession(), CPDonneesCalcul.CS_REV_NET_AVANT_PRORATA,
                                decision.getLangue())).toString());
                CPDecision_Ind_Doc.fr.add(this.getTexteDocument(decisionsInd, decisionInd, 2, 9, libFranc).toString());
                CPDecision_Ind_Doc.mRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 3, 9, varTemp)
                        .toString());
                CPDecision_Ind_Doc.rRevenu.add(this.getTexteDocument(decisionsInd, decisionInd, 4, 9, " ").toString());
                CPDecision_Ind_Doc.operation.add(" ");
            }
        }
    }

    /**
     * Sets the perFis.
     * 
     * @param perFis
     *            The perFis to set
     */
    public void setPerFis(CPPeriodeFiscale perFis) {
        this.perFis = perFis;
    }
}
