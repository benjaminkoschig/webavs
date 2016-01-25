package globaz.draco.db.inscriptions;

import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationListViewBean;
import globaz.draco.db.declaration.DSLigneDeclarationViewBean;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.util.JANumberFormatter;
import globaz.hercule.utils.CEUtils;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.naos.application.AFApplication;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.assurance.AFCalculAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.parametreAssurance.AFParametreAssurance;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pyxis.constantes.IConstantes;
import java.math.BigDecimal;

public class DSValideMontantDeclarationProcess extends BProcess {

    private static final long serialVersionUID = -4633174595818116094L;

    private DSDeclarationViewBean declaration = null;
    private String idDeclaration = "";
    private BigDecimal montantAC = new BigDecimal("0");
    private BigDecimal montantACII = new BigDecimal("0");
    private BigDecimal montantAvs = new BigDecimal("0");

    public DSValideMontantDeclarationProcess() {
        super();
    }

    /**
     * @param parent
     */
    public DSValideMontantDeclarationProcess(BProcess parent) {
        super(parent);
    }

    /**
     * @param session
     */
    public DSValideMontantDeclarationProcess(BSession session) {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {

    }

    @Override
    protected boolean _executeProcess() throws Exception {
        BSession sessionNaos = (BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                .newSession(getSession());

        boolean wantAnnualisation = ((AFApplication) sessionNaos.getApplication()).wantAnnualiserMasse();

        declaration = new DSDeclarationViewBean();
        declaration.setSession(getSession());
        declaration.setIdDeclaration(idDeclaration);
        declaration.retrieve(getTransaction());
        if (declaration.isNew()) {
            this._addError(getTransaction(), "déclaration inexistante");
            abort();
            return false;
        }
        if (!DSDeclarationViewBean.CS_OUVERT.equals(declaration.getEtat())) {
            this._addError(getSession().getLabel("DECL_NON_OUVERTE"));
            abort();
        }
        DSApplication application = (DSApplication) getSession().getApplication();
        if (!JadeStringUtil.isBlankOrZero(application.giveCodeBloquage())
                && !JadeStringUtil.isBlankOrZero(declaration.getIdJournal())) {
            CIEcritureManager ecrMgr = new CIEcritureManager();
            ecrMgr.setSession(getSession());
            ecrMgr.setForIdJournal(declaration.getIdJournal());
            ecrMgr.setForCategoriePersonnel(getSession().getSystemCode("CICATPER", application.giveCodeBloquage()));
            ecrMgr.setForDateNaissanceNumeric("0");
            if (ecrMgr.getCount() > 0) {
                this._addError(getSession().getLabel("CI_SANS_DATE_NAISSANCE"));
                getMemoryLog().logMessage(getSession().getLabel("CI_SANS_DATE_NAISSANCE"), FWMessage.ERREUR, "");
                abort();
                return false;
            }
        }
        // On a la déclaration, on recherche le total avs

        setMontantsAVSAC();
        genereLignesDeclaration(wantAnnualisation);
        setMontantsAVSAC();
        if (!isOnError()) {
            setSendCompletionMail(false);
        }
        if (getTransaction().hasErrors()) {
            abort();
        }

        return !isAborted();
    }

    private String calculMontant(AFAssurance assurance, int annee) throws Exception {
        String ageMaxHomme = dateMaximum(assurance, annee, CodeSystem.SEXE_HOMME);
        String ageMaxFemme = dateMaximum(assurance, annee, CodeSystem.SEXE_FEMME);
        String ageMinHomme = dateMinimum(assurance, annee, CodeSystem.SEXE_HOMME);
        String ageMinFemme = dateMinimum(assurance, annee, CodeSystem.SEXE_FEMME);
        if (CodeSystem.TYPE_ASS_COTISATION_AC.equals(assurance.getTypeAssurance())
                || CodeSystem.TYPE_ASS_LAA.equals(assurance.getTypeAssurance())) {
            DSInscriptionsIndividuellesManager mgrAC = new DSInscriptionsIndividuellesManager();
            mgrAC.setUntilDateSexeFemme(ageMaxFemme);
            mgrAC.setUntilDateSexeHomme(ageMaxHomme);
            mgrAC.setFromDateFemme(ageMinFemme);
            mgrAC.setFromDateHomme(ageMinHomme);
            mgrAC.setSession(getSession());
            mgrAC.setForIdDeclaration(idDeclaration);
            // mgrAC.setNotForInCatPerso(notIn(assurance,String.valueOf(annee)));
            // Modif pour contrôle d'employeur, si l'annee n'est pas vide :
            // saisie sous l'ancien système
            if (JadeStringUtil.isBlankOrZero(declaration.getAnnee())) {
                mgrAC.setForAnneeInscription((String.valueOf(annee)));
                mgrAC.setNotForInCatPerso(notIn(assurance, String.valueOf(annee)));
            } else {
                mgrAC.setNotForInCatPerso(notIn(assurance, declaration.getAnnee()));
            }
            montantAC = mgrAC.getSum("TEMAI", getTransaction());
            mgrAC.setForAnneeInscription("");
            BigDecimal montantACTotal = mgrAC.getSum("TEMAI", getTransaction());
            declaration.setMasseACTotal(montantACTotal.toString());
            declaration.wantCallMethodAfter(false);
            declaration.update(getTransaction());
            return (montantAC.toString());
        } else if (CodeSystem.TYPE_ASS_COTISATION_AF.equals(assurance.getTypeAssurance())
                || CodeSystem.TYPE_ASS_CPS_AUTRE.equals(assurance.getTypeAssurance())
                || CodeSystem.TYPE_ASS_CPS_GENERAL.equals(assurance.getTypeAssurance())
                || CodeSystem.TYPE_ASS_ACCUEIL_ENFANT.equals(assurance.getTypeAssurance())
                || (CodeSystem.TYPE_ASS_PC_FAMILLE.equals(assurance.getTypeAssurance()) && !"2011".equals(String
                        .valueOf(annee)))) {
            DSInscriptionsIndividuellesManager mgrAF = new DSInscriptionsIndividuellesManager();
            mgrAF.setUntilDateSexeFemme(ageMaxFemme);
            mgrAF.setUntilDateSexeHomme(ageMaxHomme);
            mgrAF.setFromDateFemme(ageMinFemme);
            mgrAF.setFromDateHomme(ageMinHomme);
            mgrAF.setSession(getSession());
            if (JadeStringUtil.isBlankOrZero(declaration.getAnnee())) {
                mgrAF.setForAnneeInscription(String.valueOf(annee));
                mgrAF.setNotForInCatPerso(notIn(assurance, String.valueOf(annee)));
            } else {
                mgrAF.setNotForInCatPerso(notIn(assurance, declaration.getAnnee()));
            }
            mgrAF.setForCodeCanton(assurance.getAssuranceCanton());
            mgrAF.setForIdDeclaration(idDeclaration);
            // mgrAF.setNotForInCatPerso(notIn(assurance,String.valueOf(annee)));
            BigDecimal montAF = mgrAF.getSum("TEMAF", getTransaction());
            return montAF.toString();
        } else if (CodeSystem.TYPE_ASS_MATERNITE.equals(assurance.getTypeAssurance())) {
            DSInscriptionsCalculMontantCI mgrAmat = new DSInscriptionsCalculMontantCI();
            mgrAmat.setUntilDateSexeFemme(ageMaxFemme);
            mgrAmat.setUntilDateSexeHomme(ageMaxHomme);
            mgrAmat.setFromDateSexeFemme(ageMinFemme);
            mgrAmat.setFromDateSexeHomme(ageMinHomme);
            if (JadeStringUtil.isBlankOrZero(declaration.getAnnee())) {
                mgrAmat.setForAnnee(String.valueOf(annee));
                mgrAmat.setNotForInCatPerso(notIn(assurance, String.valueOf(annee)));
            } else {
                mgrAmat.setNotForInCatPerso(notIn(assurance, declaration.getAnnee()));
            }
            mgrAmat.setSession(getSession());
            mgrAmat.setForCodeCanton(IConstantes.CS_LOCALITE_CANTON_GENEVE);
            mgrAmat.setForIdDeclaration(idDeclaration);
            // mgrAmat.setNotForInCatPerso(notIn(assurance,String.valueOf(annee)));
            return mgrAmat.getSum("KBMMON", getTransaction()).toString();
        } else if (CodeSystem.TYPE_ASS_COTISATION_AC2.equals(assurance.getTypeAssurance())) {
            DSInscriptionsIndividuellesManager mgrACII = new DSInscriptionsIndividuellesManager();
            mgrACII.setUntilDateSexeFemme(ageMaxFemme);
            mgrACII.setUntilDateSexeHomme(ageMaxHomme);
            mgrACII.setFromDateFemme(ageMinFemme);
            mgrACII.setFromDateHomme(ageMinHomme);
            mgrACII.setSession(getSession());
            if (JadeStringUtil.isBlankOrZero(declaration.getAnnee())) {
                mgrACII.setForAnneeInscription(String.valueOf(annee));
                mgrACII.setNotForInCatPerso(notIn(assurance, String.valueOf(annee)));
            } else {
                mgrACII.setNotForInCatPerso(notIn(assurance, declaration.getAnnee()));
            }
            mgrACII.setForIdDeclaration(idDeclaration);
            // mgrACII.setNotForInCatPerso(notIn(assurance,String.valueOf(annee)));
            montantACII = mgrACII.getSum("TEMAII", getTransaction());
            mgrACII.setForAnneeInscription("");
            BigDecimal montantAC2Total = mgrACII.getSum("TEMAII", getTransaction());
            declaration.setMasseAC2Total(montantAC2Total.toString());
            declaration.wantCallMethodAfter(false);
            declaration.update(getTransaction());
            return montantACII.toString();
        } else if (CodeSystem.TYPE_ASS_FFPP_MASSE.equals(assurance.getTypeAssurance())) {

            DSInscriptionsIndividuellesManager mgrAF = new DSInscriptionsIndividuellesManager();
            mgrAF.setSession(getSession());
            mgrAF.setUntilDateSexeFemme(ageMaxFemme);
            mgrAF.setUntilDateSexeHomme(ageMaxHomme);
            mgrAF.setFromDateFemme(ageMinFemme);
            mgrAF.setFromDateHomme(ageMinHomme);
            if (JadeStringUtil.isBlankOrZero(declaration.getAnnee())) {
                mgrAF.setForAnneeInscription(String.valueOf(annee));
                mgrAF.setNotForInCatPerso(notIn(assurance, String.valueOf(annee)));
            } else {
                mgrAF.setNotForInCatPerso(notIn(assurance, declaration.getAnnee()));
            }
            if (!JadeStringUtil.isBlankOrZero(assurance.getAssuranceCanton())) {
                mgrAF.setForCodeCanton(assurance.getAssuranceCanton());
            }
            mgrAF.setForIdDeclaration(idDeclaration);
            BigDecimal montFFPP = mgrAF.getSum("TEMAF", getTransaction());

            return montFFPP.toString();

        } else if (CodeSystem.TYPE_ASS_PC_FAMILLE.equals(assurance.getTypeAssurance())
                && "2011".equals(String.valueOf(annee))) {
            // FIXME UNIQUEMENT pour l'année 2011
            return declaration.getMassePeriode();

        } else {
            DSInscriptionsCalculMontantCI mgrAVS = new DSInscriptionsCalculMontantCI();
            mgrAVS.setUntilDateSexeFemme(ageMaxFemme);
            mgrAVS.setUntilDateSexeHomme(ageMaxHomme);
            mgrAVS.setFromDateSexeFemme(ageMinFemme);
            mgrAVS.setFromDateSexeHomme(ageMinHomme);
            mgrAVS.setSession(getSession());
            if (JadeStringUtil.isBlankOrZero(declaration.getAnnee())) {
                mgrAVS.setForAnnee(String.valueOf(annee));
                mgrAVS.setNotForInCatPerso(notIn(assurance, String.valueOf(annee)));
            } else {
                mgrAVS.setNotForInCatPerso(notIn(assurance, declaration.getAnnee()));
            }
            mgrAVS.setForIdDeclaration(idDeclaration);
            // mgrAVS.setNotForInCatPerso(notIn(assurance,String.valueOf(annee)));
            montantAvs = mgrAVS.getSum("KBMMON", getTransaction());
            mgrAVS.setForAnnee("");
            BigDecimal avsTotal = mgrAVS.getSum("KBMMON", getTransaction());
            declaration.setMasseSalTotal(avsTotal.toString());
            declaration.wantCallMethodAfter(false);
            declaration.update(getTransaction());
            return montantAvs.toString();
        }
    }

    /**
     * Méthode qui fourni la date maximum d'une assurance pour la prise en compte par rapport à l'âge de l'assuré
     * 
     * @param assurance
     * @param annee
     * @return l'âge max
     * @throws Exception
     */
    private String dateMaximum(AFAssurance assurance, int annee, String sexe) throws Exception {

        AFParametreAssurance valeur = AFUtil.giveParametreAssurance(CodeSystem.GEN_PARAM_ASS_CODE_CALC_AGE_MAX,
                assurance.getAssuranceId(), "01.01." + String.valueOf(annee), sexe, getSession());
        if (valeur != null) {
            return String.valueOf(annee - Integer.parseInt(valeur.getValeur()));

        }

        return "";

    }

    /**
     * Méthode qui fourni la date minimu d'une assurance pour la prise en compte par rapport à l'âge de l'assuré
     * 
     * @param assurance
     * @param annee
     * @return l'âge max
     * @throws Exception
     */
    private String dateMinimum(AFAssurance assurance, int annee, String sexe) throws Exception {

        AFParametreAssurance valeur = AFUtil.giveParametreAssurance(CodeSystem.GEN_PARAM_ASS_CODE_CALC_AGE_MIN,
                assurance.getAssuranceId(), "01.01." + String.valueOf(annee), sexe, getSession());
        if (valeur != null) {
            return String.valueOf(annee - Integer.parseInt(valeur.getValeur()));

        }

        return "";

    }

    /**
     * on supprime les éventuelles lignes existantes
     */
    private void deleteExistingLines() {
        DSLigneDeclarationListViewBean mgrLigne = new DSLigneDeclarationListViewBean();
        mgrLigne.setSession(getSession());
        mgrLigne.setForIdDeclaration(getIdDeclaration());
        if (!JadeStringUtil.isBlankOrZero(getIdDeclaration())) {
            try {
                mgrLigne.find(getTransaction());
            } catch (Exception e) {

            }
        }
        if (mgrLigne.size() > 0) {
            for (int i = 0; i < mgrLigne.size(); i++) {
                DSLigneDeclarationViewBean ligne = (DSLigneDeclarationViewBean) mgrLigne.get(i);
                try {
                    ligne.delete(getTransaction());
                } catch (Exception e) {

                }
            }

        }
    }

    private void genereLignesDeclaration(boolean wantAnnualisation) {
        // PO, on supprime les éventuelles lignes existantes
        deleteExistingLines();
        try {
            DSInscAnneeMaxMinManager mgrMax = new DSInscAnneeMaxMinManager();
            mgrMax.setSession(getSession());
            mgrMax.setForIdDeclaration(declaration.getIdDeclaration());
            mgrMax.find();

            String anneeMin = "";
            String anneeMax = "";

            if (DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())) {
                try {
                    anneeMin = ((DSInscAnneeMaxMinEntity) mgrMax.getFirstEntity()).getValeurMin();
                    anneeMax = ((DSInscAnneeMaxMinEntity) mgrMax.getFirstEntity()).getValeurMax();
                } catch (Exception e) {
                    anneeMin = declaration.getAnnee();
                    anneeMax = declaration.getAnnee();
                }
            } else if (DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(declaration.getTypeDeclaration())) {
                anneeMin = declaration.getAnneeTaux();
                anneeMax = declaration.getAnneeTaux();
            } else {
                anneeMin = declaration.getAnnee();
                anneeMax = declaration.getAnnee();
            }
            int anneeMinInt = Integer.parseInt(anneeMin);
            int anneeMaxInt = Integer.parseInt(anneeMax);
            // if (
            // !JadeStringUtil.isBlank(JANumberFormatter.deQuote(declaration.getMasseSalTotal())))
            // {
            for (int anneeEnCours = anneeMinInt; anneeEnCours <= anneeMaxInt; anneeEnCours++) {
                AFCotisationManager cotisations = new AFCotisationManager();
                cotisations.setSession(getSession());
                cotisations.setForAffiliationId(declaration.getAffiliationId());
                cotisations.setForAnneeDeclaration(String.valueOf(anneeEnCours));
                cotisations.setForNotMotifFin(globaz.naos.translation.CodeSystem.MOTIF_FIN_EXCEPTION);

                cotisations.find();
                // Contrôler s'il existe déjà des lignes de déclaration
                // Si le montant est différent de zéro on génère les lignes
                String oldId = "0";

                for (int i = 0; i < cotisations.size(); i++) {
                    AFCotisation cotisation = (AFCotisation) cotisations.getEntity(i);
                    // List tauxList = cotisation.getTauxList("31.12." +
                    // declaration.getAnnee());
                    AFTauxAssurance tauxAssurance = null;

                    Boolean wantRecalcul = !DSDeclarationViewBean.CS_COMPLEMENTAIRE.equals(declaration
                            .getTypeDeclaration())
                            && !DSDeclarationViewBean.CS_ICI.equals(declaration.getTypeDeclaration())
                            && !DSDeclarationViewBean.CS_DIVIDENDE.equals(declaration.getTypeDeclaration())
                            && !DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(declaration.getTypeDeclaration());

                    if (DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(declaration.getTypeDeclaration())) {
                        tauxAssurance = cotisation.findTaux("31.12." + declaration.getAnneeTaux(),
                                declaration.getMasseSalTotal(), declaration.getTypeDeclaration(), wantRecalcul,
                                wantAnnualisation);
                    } else {
                        tauxAssurance = cotisation.findTaux("31.12." + anneeEnCours, declaration.getMasseSalTotal(),
                                declaration.getTypeDeclaration(), wantRecalcul, wantAnnualisation);
                    }

                    // On ne prend que les assurances paritaires
                    if (cotisation.getAssurance().getAssuranceGenre().equalsIgnoreCase(CodeSystem.GENRE_ASS_PARITAIRE)
                            && cotisation.getAssurance().isAssurance13().booleanValue()
                            // &&tauxList.size() > 0) {
                            && (tauxAssurance != null)) {

                        // Modif jmc 06.02.2006, si plusieurs fois la même
                        // assurance, on ne la prend qu'une fois
                        if (oldId.equals(cotisation.getAssuranceId())) {
                            continue;
                        }
                        // Modif 5.3 Spécifique CCJU pour la FSFP, si la date de
                        // début est égale à la date de fin, on l'ignore
                        if (!JadeStringUtil.isBlankOrZero(cotisation.getDateDebut())
                                && !JadeStringUtil.isBlankOrZero(cotisation.getDateFin())) {
                            if (BSessionUtil.compareDateEqual(getSession(), cotisation.getDateDebut(),
                                    cotisation.getDateFin())) {
                                continue;
                            }
                        }

                        // INFOROM 169 : Pour les ICI et Diviednte seule les assurances AVS, AC et frais
                        // d'administration doivent être calculées. Les dividendes peuvent avoir des AF.
                        if (declaration.getTypeDeclaration().equals(DSDeclarationViewBean.CS_DIVIDENDE)
                                || declaration.getTypeDeclaration().equals(DSDeclarationViewBean.CS_ICI)) {

                            if (!CodeSystem.TYPE_ASS_COTISATION_AVS_AI.equals(cotisation.getAssurance()
                                    .getTypeAssurance())
                                    && !CodeSystem.TYPE_ASS_COTISATION_AC.equals(cotisation.getAssurance()
                                            .getTypeAssurance())
                                    && !CodeSystem.TYPE_ASS_FRAIS_ADMIN.equals(cotisation.getAssurance()
                                            .getTypeAssurance())
                                    && !CodeSystem.TYPE_ASS_COTISATION_AF.equals(cotisation.getAssurance()
                                            .getTypeAssurance())) {
                                continue;
                            }

                            if (declaration.getTypeDeclaration().equals(DSDeclarationViewBean.CS_ICI)
                                    && CodeSystem.TYPE_ASS_COTISATION_AF.equals(cotisation.getAssurance()
                                            .getTypeAssurance())) {
                                continue;
                            }
                        }

                        oldId = cotisation.getAssuranceId();
                        // On crée pour chaque assurance une ligne de
                        // déclaration
                        DSLigneDeclarationViewBean ligneDec = new DSLigneDeclarationViewBean();
                        ligneDec.setSession(getSession());
                        ligneDec.setIdDeclaration(getIdDeclaration());
                        ligneDec.setAnneCotisation(String.valueOf(anneeEnCours));

                        int anneeRefeference = anneeEnCours;
                        // Si déclaration salaire différés, on prend l'année spécifiée pour le taux
                        if (DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(declaration.getTypeDeclaration())) {
                            anneeRefeference = CEUtils.transformeStringToInt(declaration.getAnneeTaux());
                        }

                        String theIdCompteAnnexe = "";
                        try {
                            theIdCompteAnnexe = declaration.getCompteAnnexe().getIdCompteAnnexe();
                        } catch (Exception e) {
                            theIdCompteAnnexe = "";
                        }

                        if (cotisation.getAssurance().getAssuranceReference() != null) {

                            String dateDebut = "01.01.";
                            String dateFin = "31.12.";

                            // Si déclaration salaire différés, on prend l'année spécifiée pour le taux
                            if (DSDeclarationViewBean.CS_SALAIRE_DIFFERES.equals(declaration.getTypeDeclaration())) {
                                dateDebut += declaration.getAnneeTaux();
                                dateFin += declaration.getAnneeTaux();
                            } else {
                                dateDebut += anneeEnCours;
                                dateFin += anneeEnCours;
                            }

                            double montant = 0;
                            montant =
                            // en cas d'assurence referencée, on calcule la
                            // cotisation de l'assurance en question
                            AFCalculAssurance.calculResultatAssurance(
                                    dateDebut,
                                    dateFin,
                                    cotisation.getAssurance().getAssuranceReference().getTaux(dateFin),
                                    new Double(calculMontant(cotisation.getAssurance().getAssuranceReference(),
                                            anneeRefeference)).doubleValue(), getSession());
                            BigDecimal montantArr = new BigDecimal(montant);
                            montantArr = JANumberFormatter.round(montantArr, 0.05, 2, JANumberFormatter.NEAR);
                            ligneDec.setMontantDeclaration(montantArr.toString());
                        } else {
                            String theMontantDeclaration = calculMontant(cotisation.getAssurance(), anneeRefeference);

                            if (CodeSystem.TYPE_ASS_FFPP_MASSE.equalsIgnoreCase(cotisation.getAssurance()
                                    .getTypeAssurance())
                                    && DSDeclarationViewBean.CS_PRINCIPALE.equalsIgnoreCase(declaration
                                            .getTypeDeclaration())) {

                                try {
                                    theMontantDeclaration = AFUtil.plancheMasse(new FWCurrency(theMontantDeclaration),
                                            cotisation.getAssuranceId(), "01.01." + anneeRefeference, getSession())
                                            .toString();
                                } catch (Exception e) {
                                    JadeLogger.error(DSValideMontantDeclarationProcess.class.getName(), e.toString());
                                    JadeSmtpClient.getInstance().sendMail(
                                            getSession().getUserEMail(),
                                            FWMessageFormat.format(
                                                    getSession().getLabel("ERREUR_PLANCHE_MASSE_EMAIL_SUBJECT"),
                                                    cotisation.getAssurance().getAssuranceLibelle(
                                                            getSession().getIdLangueISO())),
                                            FWMessageFormat.format(
                                                    getSession().getLabel("ERREUR_PLANCHE_MASSE_EMAIL_BODY"),
                                                    declaration.getAffilieNumero(), cotisation.getAssurance()
                                                            .getAssuranceLibelle(getSession().getIdLangueISO()))
                                                    + " (" + e.toString() + ")", null);
                                }

                            }

                            theMontantDeclaration = AFUtil.plafonneMasse(theMontantDeclaration,
                                    cotisation.getAssuranceId(), "01.01." + anneeRefeference, getSession(),
                                    theIdCompteAnnexe, declaration.getTypeDeclaration());

                            ligneDec.setMontantDeclaration(theMontantDeclaration);
                        }
                        ligneDec.setAssuranceId(cotisation.getAssuranceId());
                        // if(!JadeStringUtil.isDecimalEmpty(JANumberFormatter.deQuote(ligneDec.getMontantDeclaration()))){
                        // Pour les ctrl d'empl, on ne rembourse pas les frais
                        // d'adm.
                        if (DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())
                                && CodeSystem.TYPE_ASS_FRAIS_ADMIN.equals(cotisation.getAssurance().getTypeAssurance())) {
                            BigDecimal montantTotal = new BigDecimal(ligneDec.getCotisationDue());
                            if (montantTotal.signum() != -1) {
                                ligneDec.add(getTransaction());
                            }

                        } else {
                            ligneDec.add(getTransaction());
                        }
                        // }
                    }
                    // Calcul des montatns séparés suite à modif ctrl empl et
                    // cat.pers.

                }
                // Activer un calcul spécial de bonus pour les décomptes 13
                // (Seulement pour certaines caisses)
                if ("true".equalsIgnoreCase((getSession().getApplication()).getProperty("bonusPFA"))
                        && !DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())) {
                    getBonus(getTransaction());
                }
                if (DSDeclarationViewBean.CS_LTN.equals(declaration.getTypeDeclaration())
                        || DSDeclarationViewBean.CS_LTN_COMPLEMENTAIRE.equals(declaration.getTypeDeclaration())) {
                    ltn();
                }
                // }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            // retourne liste vide
        }
    }

    public void getBonus(BTransaction transaction) throws java.lang.Exception {
        String anneeMin = "";
        String anneeMax = "";
        DSInscAnneeMaxMinManager mgrMax = new DSInscAnneeMaxMinManager();
        mgrMax.setSession(getSession());
        mgrMax.setForIdDeclaration(declaration.getIdDeclaration());
        mgrMax.find();
        if (DSDeclarationViewBean.CS_CONTROLE_EMPLOYEUR.equals(declaration.getTypeDeclaration())) {
            try {
                anneeMin = ((DSInscAnneeMaxMinEntity) mgrMax.getFirstEntity()).getValeurMin();
                anneeMax = ((DSInscAnneeMaxMinEntity) mgrMax.getFirstEntity()).getValeurMax();
            } catch (Exception e) {
                anneeMin = declaration.getAnnee();
                anneeMax = declaration.getAnnee();
            }
        } else {
            anneeMin = declaration.getAnnee();
            anneeMax = declaration.getAnnee();
        }
        int anneeMinInt = Integer.parseInt(anneeMin);
        int anneeMaxInt = Integer.parseInt(anneeMax);
        // if (
        // !JadeStringUtil.isBlank(JANumberFormatter.deQuote(declaration.getMasseSalTotal())))
        // {
        for (int anneeEnCours = anneeMinInt; anneeEnCours <= anneeMaxInt; anneeEnCours++) {
            DSDeclarationViewBean decl = new DSDeclarationViewBean();
            decl.setSession(getSession());
            decl.setIdDeclaration(getIdDeclaration());
            decl.setAffiliationId(declaration.getAffiliationId());
            decl.getBonus(getTransaction(), Integer.toString(anneeEnCours));
        }
    }

    @Override
    protected String getEMailObject() {
        if (!isOnError() && !isAborted() && (getMemoryLog().size() <= 0)) {
            return getSession().getLabel("CALCUL_MASSE_REUSSI");
        } else {
            return getSession().getLabel("CALCUL_MASSE_ECHEC");
        }
    }

    /**
     * @return
     */
    public String getIdDeclaration() {
        return idDeclaration;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void ltn() throws Exception {
        // Bidouille, l'assurance stockée dans draco et la première assurance
        // sans canton(féd) et la prem. avec canton
        boolean foundCanto = false;
        boolean foundFederal = false;
        AFAssuranceManager assMgr = new AFAssuranceManager();
        assMgr.setSession(getSession());
        assMgr.setForTypeAssurance(CodeSystem.TYPE_ASS_IMPOT_SOURCE);
        assMgr.find();
        for (int i = 0; i < assMgr.size(); i++) {
            AFAssurance assurance = (AFAssurance) assMgr.get(i);
            if (!JadeStringUtil.isBlankOrZero(assurance.getAssuranceCanton()) && !foundCanto) {
                DSLigneDeclarationViewBean ligne = new DSLigneDeclarationViewBean();
                ligne.setAnneCotisation(declaration.getAnnee());
                ligne.setMontantDeclaration(declaration.getMasseSalTotal());
                ligne.setIdDeclaration(declaration.getIdDeclaration());
                ligne.setAssuranceId(assurance.getAssuranceId());
                ligne.setSession(getSession());
                ligne.add(getTransaction());
                foundCanto = true;
            } else if (!foundFederal && JadeStringUtil.isBlankOrZero(assurance.getAssuranceCanton())) {
                DSLigneDeclarationViewBean ligne = new DSLigneDeclarationViewBean();
                ligne.setAnneCotisation(declaration.getAnnee());
                ligne.setMontantDeclaration(declaration.getMasseSalTotal());
                ligne.setIdDeclaration(declaration.getIdDeclaration());
                ligne.setAssuranceId(assurance.getAssuranceId());
                ligne.setSession(getSession());
                ligne.add(getTransaction());
                foundFederal = true;
            }
        }

    }

    public String notIn(AFAssurance assurance, String annee) {
        String[] codeExcluAC;
        String notIn = "";
        try {
            codeExcluAC = assurance.getListExclusionsCatPers("31.12." + annee);
            for (int j = 0; j < codeExcluAC.length; j++) {
                if (j == codeExcluAC.length - 1) {
                    notIn = notIn + codeExcluAC[j];
                } else {
                    notIn = notIn + codeExcluAC[j] + ",";
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return notIn;
    }

    /**
     * @param string
     */
    public void setIdDeclaration(String string) {
        idDeclaration = string;
    }

    private void setMontantsAVSAC() throws Exception {
        // calcul de la masse AVs par rapport au montant CI
        DSInscriptionsCalculMontantCI mgrSomme = new DSInscriptionsCalculMontantCI();
        mgrSomme.setSession(getSession());
        mgrSomme.setForIdDeclaration(idDeclaration);
        montantAvs = mgrSomme.getSum("KBMMON", getTransaction());
        declaration.setMasseSalTotal(montantAvs.toString());
        // calcul de la masse AC
        DSInscriptionsIndividuellesManager inscManager = new DSInscriptionsIndividuellesManager();
        inscManager.setSession(getSession());
        inscManager.setForIdDeclaration(idDeclaration);
        montantAC = inscManager.getSum("TEMAI", getTransaction());
        declaration.setMasseACTotal(montantAC.toString());
        montantACII = inscManager.getSum("TEMAII", getTransaction());
        declaration.setMasseAC2Total(montantACII.toString());
        declaration.wantCallMethodAfter(false);
        declaration.update(getTransaction());
    }

    public DSDeclarationViewBean getDeclaration() {
        return declaration;
    }

    public void setDeclaration(DSDeclarationViewBean declaration) {
        this.declaration = declaration;
    }

    public BigDecimal getMontantAC() {
        return montantAC;
    }

    public BigDecimal getMontantACII() {
        return montantACII;
    }

    public BigDecimal getMontantAvs() {
        return montantAvs;
    }

}
