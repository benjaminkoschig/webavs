package globaz.corvus.helpers.rentesaccordees;

import globaz.corvus.acor.parser.rev09.REACORParser;
import globaz.corvus.api.basescalcul.IREBasesCalcul;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REPeriodeAPI;
import globaz.corvus.db.demandes.REPeriodeAPIManager;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.demandes.REPeriodeInvaliditeManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.regles.REDemandeRegles;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenrePrestationEnum;
import globaz.corvus.vb.rentesaccordees.RERepriseDuDroitViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.hera.utils.SFPeriodeUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.constantes.IConstantes;
import java.math.BigDecimal;
import java.util.List;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * @author SCR
 */
public class RERepriseDuDroitHelper extends PRAbstractHelper {

    class ObjContanier {
        public REBasesCalcul bc;
        public RERenteAccordee ra;
    }

    @Override
    protected void _add(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        BSession session1 = (BSession) session;

        BTransaction transaction = null;
        try {
            transaction = (BTransaction) (session1).newTransaction();
            transaction.openTransaction();

            RERepriseDuDroitViewBean vb = (RERepriseDuDroitViewBean) viewBean;
            String dd1 = vb.getPeriode_1_Du();
            String df1 = vb.getPeriode_1_Au();
            String mnt1 = vb.getMontant1();

            String dd2 = vb.getPeriode_2_Du();
            String df2 = vb.getPeriode_2_Au();
            String mnt2 = vb.getMontant2();

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session1);
            ra.setIdPrestationAccordee(vb.getIdPrestationAccordee());
            ra.retrieve(transaction);
            PRAssert.notIsNew(ra, "Entity not found");

            REInformationsComptabilite ic = new REInformationsComptabilite();
            ic.setSession((BSession) session);
            ic.setIdInfoCompta(ra.getIdInfoCompta());
            ic.retrieve(transaction);
            PRAssert.notIsNew(ic, "Entity not found");

            // TODO Traduire les textes

            // Test sur les 25 ans de l'enfant
            JACalendar cal = new JACalendarGregorian();
            if (REGenrePrestationEnum.groupeEnfant.contains(ra.getCodePrestation())) {
                // BZ-3931
                // - Si l'enfant a plus de 25 ans, tester pour ne pas aller plus
                // loin que le mois
                // des 25 ans (né le 15.08.1985, date de fin 08.2010). Donc, la
                // date de fin est
                // obligatoire dans ce cas-là par l'utilisateur.
                PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());
                JADate d25 = new JADate(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
                // d25.setDay(1);
                d25 = cal.addYears(d25, 25);
                JADate today = JACalendar.today();

                if (cal.compare(today, d25) == JACalendar.COMPARE_FIRSTUPPER) {
                    if ((JadeStringUtil.isBlankOrZero(df1) && !JadeStringUtil.isBlankOrZero(dd1))
                            || (JadeStringUtil.isBlankOrZero(df2) && !JadeStringUtil.isBlankOrZero(dd2))) {

                        throw new Exception("La date de fin est obligatoire et ne peut dépasser les 25 ans de l'enfant");
                    }

                    // df1 et df2 sont au format mm.aaaa, transformé en JADate,
                    // ils ont par défaut le 1er jour du mois.
                    // Pour que les tests de comparaison avec les 25 ans de
                    // l'enfant soient cohérant, on transforme la date
                    // des 25 ans de l'enfant en mm.aaaa en settant cette date
                    // avec le 1er jour du mois.
                    d25.setDay(1);
                    if (!JadeStringUtil.isBlankOrZero(df1)) {
                        JADate df = new JADate(df1);
                        if (cal.compare(d25, df) == JACalendar.COMPARE_SECONDUPPER) {
                            throw new Exception("La date de fin ne peut dépasser les 25 ans de l'enfant");
                        }
                    }

                    if (!JadeStringUtil.isBlankOrZero(df2)) {
                        JADate df = new JADate(df2);
                        if (cal.compare(d25, df) == JACalendar.COMPARE_SECONDUPPER) {
                            throw new Exception("La date de fin ne peut dépasser les 25 ans de l'enfant");
                        }
                    }
                }
            }

            // Bz-3931
            // Dans la date de fin, ne pas accepter les dates plus grandes que
            // le mois de
            // paiement en cours.
            JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt((BSession) session));
            if (!JadeStringUtil.isBlankOrZero(df1)) {
                JADate df = new JADate(df1);
                if (cal.compare(dateDernierPmt, df) == JACalendar.COMPARE_SECONDUPPER) {
                    throw new Exception("La date de fin ne peut pas être supérieur à la date du dernier paiement:");
                }
            }

            if (!JadeStringUtil.isBlankOrZero(df2)) {
                JADate df = new JADate(df2);
                if (cal.compare(dateDernierPmt, df) == JACalendar.COMPARE_SECONDUPPER) {
                    throw new Exception("La date de fin ne peut pas être supérieur à la date du dernier paiement:");
                }
            }

            if (JadeStringUtil.isBlank(dd1) || JadeStringUtil.isBlankOrZero(mnt1)) {
                if (JadeStringUtil.isBlank(dd2) || JadeStringUtil.isBlankOrZero(mnt2)) {
                    throw new Exception(
                            "Au moins une ligne doit être remplie avec les champs 'Période du' et 'Montant' renseignés.");
                } else {
                    ObjContanier oc = doTraitementXLignes((BSession) session, transaction, ra, ic, dd2, df2, mnt2,
                            null, null, null, 1);

                    JADate dd = new JADate(dd2);
                    if (cal.compare(dd, dateDernierPmt) != JACalendar.COMPARE_FIRSTUPPER) {
                        this.addDollarsT_Periodes((BSession) session, transaction, oc.ra, dd2, df2, mnt2);
                    }

                }
            } else {

                if (JadeStringUtil.isBlank(dd2) || JadeStringUtil.isBlankOrZero(mnt2)) {
                    ObjContanier oc = doTraitementXLignes((BSession) session, transaction, ra, ic, dd1, df1, mnt1,
                            null, null, null, 1);

                    JADate dd = new JADate(dd1);
                    if (cal.compare(dd, dateDernierPmt) != JACalendar.COMPARE_FIRSTUPPER) {
                        this.addDollarsT_Periodes((BSession) session, transaction, oc.ra, dd1, df1, mnt1);
                    }

                } else {
                    // Contrôle que la deuxième ligne commence le mois après la
                    // date de fin de la 1ère
                    JADate dfP1 = new JADate(df1);
                    JADate ddP2 = new JADate(dd2);

                    dfP1 = cal.addMonths(dfP1, 1);
                    if (cal.compare(dfP1, ddP2) != JACalendar.COMPARE_EQUALS) {
                        throw new Exception(
                                "La date de début de la 2ème période doit début le mois après la fin de la 1ère période.");
                    }
                    doTraitement2Lignes((BSession) session, transaction, ra, ic, dd1, df1, mnt1, dd2, df2, mnt2);

                }
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    String message = viewBean.getMessage();
                    if (JadeStringUtil.isBlank(message)) {
                        message = "";
                    } else {
                        message += "<br />" + e.getMessage();
                    }
                    viewBean.setMessage(message);
                    viewBean.setMsgType(FWViewBeanInterface.ERROR);
                } finally {
                    transaction.closeTransaction();
                }
            }
        }
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {

        super._retrieve(viewBean, action, session);
        RERepriseDuDroitViewBean vb = (RERepriseDuDroitViewBean) viewBean;

        JADate df = new JADate(vb.getDateFinDroit());
        JACalendar cal = new JACalendarGregorian();
        df = cal.addMonths(df, 1);
        vb.setPeriode_1_Du(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(df.toStrAMJ()));
    }

    private void addDollarsT_Periodes(BSession session, BTransaction transaction, RERenteAccordee newRA,
            String dateDebut, String dateFin, String montant) throws Exception {

        // Création du $t
        REPrestationDue dollarsT = new REPrestationDue();
        dollarsT.setSession(session);
        dollarsT.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
        dollarsT.setCsType(IREPrestationDue.CS_TYPE_MNT_TOT);
        dollarsT.setCsTypePaiement(IREPrestationDue.CS_TYPE_DE_PMT_PMT_MENS);

        JADate dd = new JADate(dateDebut);

        if (JadeStringUtil.isBlankOrZero(dateFin)) {
            dateFin = REPmtMensuel.getDateDernierPmt(session);
        }

        JADate df = new JADate(dateFin);
        JACalendar cal = new JACalendarGregorian();

        int nbrMonth = 1;
        while (cal.compare(dd, df) == JACalendar.COMPARE_FIRSTLOWER) {
            nbrMonth++;
            df = cal.addMonths(df, -1);
        }

        BigDecimal mnt1 = new BigDecimal(montant);
        mnt1 = mnt1.multiply(new BigDecimal(nbrMonth));

        dollarsT.setMontant(mnt1.toString());

        dollarsT.setDateDebutPaiement(dateDebut);
        dollarsT.setDateFinPaiement(dateFin);
        dollarsT.setIdRenteAccordee(newRA.getIdPrestationAccordee());
        dollarsT.add(transaction);

    }

    private void addDollarsT_Periodes(BSession session, BTransaction transaction, RERenteAccordee newRA,
            String dateDebut1, String dateFin1, String montant1, String dateDebut2, String dateFin2, String montant2)
            throws Exception {

        // Création du $t
        REPrestationDue dollarsT = new REPrestationDue();
        dollarsT.setSession(session);
        dollarsT.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
        dollarsT.setCsType(IREPrestationDue.CS_TYPE_MNT_TOT);
        dollarsT.setCsTypePaiement(IREPrestationDue.CS_TYPE_DE_PMT_PMT_MENS);

        JADate dd = new JADate(dateDebut1);

        if (JadeStringUtil.isBlankOrZero(dateFin2)) {
            dateFin2 = REPmtMensuel.getDateDernierPmt(session);
        }

        JADate df1 = new JADate(dateFin1);
        JADate df2 = new JADate(dateFin2);
        JACalendar cal = new JACalendarGregorian();

        int nbrMonth = 0;
        while ((cal.compare(dd, df1) == JACalendar.COMPARE_FIRSTLOWER)
                || (cal.compare(dd, df1) == JACalendar.COMPARE_EQUALS)) {
            nbrMonth++;
            df1 = cal.addMonths(df1, -1);
        }

        BigDecimal mnt1 = new BigDecimal(montant1);
        mnt1 = mnt1.multiply(new BigDecimal(nbrMonth));

        nbrMonth = 0;
        dd = new JADate(dateDebut2);
        while ((cal.compare(dd, df2) == JACalendar.COMPARE_FIRSTLOWER)
                || (cal.compare(dd, df2) == JACalendar.COMPARE_EQUALS)) {
            nbrMonth++;
            df2 = cal.addMonths(df2, -1);
        }

        BigDecimal mnt2 = new BigDecimal(montant2);
        mnt2 = mnt2.multiply(new BigDecimal(nbrMonth));

        mnt1 = mnt1.add(mnt2);
        dollarsT.setMontant(mnt1.toString());

        dollarsT.setDateDebutPaiement(dateDebut1);
        dollarsT.setDateFinPaiement(dateFin2);
        dollarsT.setIdRenteAccordee(newRA.getIdPrestationAccordee());
        dollarsT.add(transaction);

    }

    private REPrestationDue creerPrestationDue(BSession session, BTransaction transaction, String idRenteAccorde,
            String dateDebut, String dateFin, String montant, String montantReductionAnticipation,
            String supplementAjournement, boolean dateFinDoitEtreDefinie) throws Exception {
        REPrestationDue prestationDue = new REPrestationDue();
        prestationDue.setSession(session);
        prestationDue.setMontant(montant);
        prestationDue.setMontantReductionAnticipation(montantReductionAnticipation);
        prestationDue.setMontantSupplementAjournement(supplementAjournement);
        prestationDue.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
        prestationDue.setCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
        prestationDue.setCsTypePaiement(IREPrestationDue.CS_TYPE_DE_PMT_PMT_MENS);

        prestationDue.setDateDebutPaiement(dateDebut);
        if (JadeStringUtil.isBlankOrZero(dateFin) && dateFinDoitEtreDefinie) {
            throw new Exception("La date de fin pour la 1ère période est obligatoire.");
        } else {
            prestationDue.setDateFinPaiement(dateFin);
        }

        prestationDue.setIdRenteAccordee(idRenteAccorde);
        prestationDue.add(transaction);

        return prestationDue;
    }

    private RERenteAccordee creerRenteAccordee(BSession session, BTransaction transaction, RERenteAccordee ra,
            String idCompteAnnexe, String idTiersAdressePaiement, String idBaseCalcul, String dateDebut,
            String dateFin, String montant) throws Exception {

        // Création de la rente accordée
        RERenteAccordee newRA = new RERenteAccordee();
        newRA.setSession(session);
        newRA.setAnneeAnticipation(ra.getAnneeAnticipation());
        newRA.setAnneeMontantRAM(ra.getAnneeMontantRAM());
        newRA.setCodeAuxilliaire(ra.getCodeAuxilliaire());
        newRA.setCodeCasSpeciaux1(ra.getCodeCasSpeciaux1());
        newRA.setCodeCasSpeciaux2(ra.getCodeCasSpeciaux2());
        newRA.setCodeCasSpeciaux3(ra.getCodeCasSpeciaux3());
        newRA.setCodeCasSpeciaux4(ra.getCodeCasSpeciaux4());
        newRA.setCodeCasSpeciaux5(ra.getCodeCasSpeciaux5());
        if (!JadeStringUtil.isBlankOrZero(dateFin)) {
            newRA.setCodeMutation(ra.getCodeMutation());
            newRA.setDateFinDroit(dateFin);
        }
        newRA.setCodePrestation(ra.getCodePrestation());
        newRA.setCodeRefugie(ra.getCodeRefugie());
        newRA.setCodeSurvivantInvalide(ra.getCodeSurvivantInvalide());
        newRA.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        newRA.setCsEtatCivil(ra.getCsEtatCivil());
        newRA.setCsGenre(ra.getCsGenre());
        newRA.setCsRelationAuRequerant(ra.getCsRelationAuRequerant());
        newRA.setDateDebutAnticipation(ra.getDateDebutAnticipation());
        newRA.setDateDebutDroit(dateDebut);
        newRA.setDateFinDroitPrevueEcheance(ra.getDateFinDroitPrevueEcheance());
        newRA.setDateRevocationAjournement(ra.getDateRevocationAjournement());
        newRA.setDureeAjournement(ra.getDureeAjournement());
        newRA.setFractionRente(ra.getFractionRente());
        newRA.setQuotiteRente(ra.getQuotiteRente());
        newRA.setIdBaseCalcul(idBaseCalcul);
        newRA.setIdTiersBaseCalcul(ra.getIdTiersBaseCalcul());
        newRA.setIdTiersBeneficiaire(ra.getIdTiersBeneficiaire());

        if (!JadeStringUtil.isBlankOrZero(ra.getCodePrestation())) {
            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(ra.getCodePrestation()));

            if (codePrestation.isRenteComplementairePourEnfant()) {

                // Date d'échéance de la rente : si rente pour enfant et période d'étude dans la situation familiale, on
                // défini
                // la date d'échéance de la rente en fonction de la dernière période d'étude
                List<ISFPeriode> periodesEtudes = SFPeriodeUtils.getPeriodesDuTiers(session,
                        newRA.getIdTiersBeneficiaire(), ISFSituationFamiliale.CS_DOMAINE_RENTES,
                        ch.globaz.hera.business.constantes.ISFPeriode.CS_TYPE_PERIODE_ETUDE);

                ISFPeriode dernierePeriodeEtude = null;

                if (!periodesEtudes.isEmpty()) {
                    dernierePeriodeEtude = periodesEtudes.get(0);
                }

                if ((dernierePeriodeEtude != null) && !JadeStringUtil.isBlankOrZero(dernierePeriodeEtude.getDateFin())) {
                    newRA.setDateEcheance(JadeDateUtil.convertDateMonthYear(dernierePeriodeEtude.getDateFin()));
                } else {

                    // Si pas de période d'étude, utilisation de l'ancien mécanisme (date d'échéance de la rente si plus
                    // éloignée dans le temps que la date du dernier paiement mensuel)
                    JADate dEch = new JADate(ra.getDateEcheance());
                    JACalendar cal = new JACalendarGregorian();

                    if (cal.compare(dEch, new JADate(REPmtMensuel.getDateDernierPmt(session))) != JACalendar.COMPARE_FIRSTLOWER) {
                        newRA.setDateEcheance(ra.getDateEcheance());
                    }
                }

                // Si les tiers complémentaires ne sont pas renseignés, on va les récupérer à partir de la famille
                if ((JadeStringUtil.isBlankOrZero(newRA.getIdTiersComplementaire1()) || JadeStringUtil
                        .isBlankOrZero(newRA.getIdTiersComplementaire2()))) {

                    ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                            ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());
                    ISFMembreFamille[] parents = sf.getParents(ra.getIdTiersBeneficiaire());

                    if ((parents != null) && (parents.length > 0)) {
                        int nombreHommes = 0;
                        int nombreFemmes = 0;
                        for (int i = 0; i < parents.length; i++) {
                            ISFMembreFamille mf = parents[i];
                            if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(mf.getCsSexe())) {
                                nombreHommes++;
                            } else {
                                nombreFemmes++;
                            }
                        }

                        // Si 2 hommes seulement ou 2 femmes seulement -> LPart
                        if ((nombreHommes == 2) || (nombreFemmes == 2)) {
                            newRA.setIdTiersComplementaire1((parents[0]).getIdTiers());
                            newRA.setIdTiersComplementaire2((parents[1]).getIdTiers());
                        } else {

                            boolean isRenteLieeRenteDuPere = codePrestation
                                    .isRentesComplementairePourEnfantsLieesRenteDuPere();

                            for (int i = 0; i < parents.length; i++) {
                                ISFMembreFamille mf = parents[i];

                                // Pour rente lié à la rente du père ou rente double, le 1er tiers complémentaire doit
                                // être
                                // le père et le second la mère
                                // Pour rente lié à la rente de la mère, le 1er tiers doit être la mère et le second le
                                // père
                                if (IConstantes.CS_PERSONNE_SEXE_HOMME.equals(mf.getCsSexe())) {
                                    if (isRenteLieeRenteDuPere) {
                                        newRA.setIdTiersComplementaire1(mf.getIdTiers());
                                    } else {
                                        newRA.setIdTiersComplementaire2(mf.getIdTiers());
                                    }
                                } else {
                                    if (isRenteLieeRenteDuPere) {
                                        newRA.setIdTiersComplementaire2(mf.getIdTiers());
                                    } else {
                                        newRA.setIdTiersComplementaire1(mf.getIdTiers());
                                    }
                                }
                            }
                        }
                    }

                } else {
                    newRA.setIdTiersComplementaire1(ra.getIdTiersComplementaire1());
                    newRA.setIdTiersComplementaire2(ra.getIdTiersComplementaire2());
                }
            }
        }

        newRA.setMontantPrestation(montant);
        newRA.setMontantReducationAnticipation(ra.getMontantReducationAnticipation());
        newRA.setMontantRenteOrdiRemplacee(ra.getMontantRenteOrdiRemplacee());
        newRA.setPrescriptionAppliquee(ra.getPrescriptionAppliquee());
        newRA.setReductionFauteGrave(ra.getReductionFauteGrave());
        newRA.setReferencePmt(ra.getReferencePmt());
        newRA.setRemarques(ra.getRemarques());

        REInformationsComptabilite newIC = new REInformationsComptabilite();
        newIC.setSession(session);
        newIC.setIdCompteAnnexe(idCompteAnnexe);
        newIC.setIdTiersAdressePmt(idTiersAdressePaiement);
        newIC.add(transaction);

        newRA.setIdInfoCompta(newIC.getIdInfoCompta());
        newRA.setIsPrestationBloquee(Boolean.FALSE);
        newRA.setIsRetenues(Boolean.FALSE);
        newRA.setIsTraitementManuel(Boolean.TRUE);
        newRA.setSupplementAjournement(ra.getSupplementAjournement());
        newRA.setSupplementVeuvage(ra.getSupplementVeuvage());
        newRA.setTauxReductionAnticipation(ra.getTauxReductionAnticipation());
        newRA.setTypeDeMiseAJours(ra.getTypeDeMiseAJours());
        newRA.add(transaction);

        return newRA;
    }

    private RERenteAccordee doTraitement1RA1dollarP(BSession session, BTransaction transaction, RERenteAccordee ra,
            String idBC, REInformationsComptabilite ic, String dateDebut, String dateFin, String montant)
            throws Exception {

        // Création de la rente accordée
        RERenteAccordee newRA = creerRenteAccordee(session, transaction, ra, ic.getIdCompteAnnexe(),
                ic.getIdTiersAdressePmt(), idBC, dateDebut, dateFin, montant);

        REPrestationDue dollarsP = new REPrestationDue();
        dollarsP.setSession(session);
        dollarsP.setMontant(montant);
        dollarsP.setMontantReductionAnticipation(ra.getMontantReducationAnticipation());
        dollarsP.setMontantSupplementAjournement(ra.getSupplementAjournement());
        dollarsP.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
        dollarsP.setCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
        dollarsP.setCsTypePaiement(IREPrestationDue.CS_TYPE_DE_PMT_PMT_MENS);
        dollarsP.setCsTypePaiement(null);

        dollarsP.setDateDebutPaiement(dateDebut);
        if (!JadeStringUtil.isBlankOrZero(dateFin)) {
            dollarsP.setDateFinPaiement(dateFin);
        }

        dollarsP.setIdRenteAccordee(newRA.getIdPrestationAccordee());
        dollarsP.add(transaction);

        return newRA;

    }

    private RERenteAccordee doTraitement1RA2dollarP(BSession session, BTransaction transaction, RERenteAccordee ra,
            String idBC, REInformationsComptabilite ic, String dateDebut1, String dateFin1, String montant1,
            String dateDebut2, String dateFin2, String montant2) throws Exception {

        // Création de la rente accordée
        RERenteAccordee newRA = creerRenteAccordee(session, transaction, ra, ic.getIdCompteAnnexe(),
                ic.getIdTiersAdressePmt(), idBC, dateDebut1, dateFin2, montant2);

        creerPrestationDue(session, transaction, newRA.getIdPrestationAccordee(), dateDebut1, dateFin1, montant1,
                ra.getMontantReducationAnticipation(), ra.getSupplementAjournement(), true);
        creerPrestationDue(session, transaction, newRA.getIdPrestationAccordee(), dateDebut2, dateFin2, montant2,
                ra.getMontantReducationAnticipation(), ra.getSupplementAjournement(), false);

        return newRA;
    }

    private void doTraitement2Lignes(BSession session, BTransaction transaction, RERenteAccordee ra,
            REInformationsComptabilite ic, String dateDebut1, String dateFin1, String montant1, String dateDebut2,
            String dateFin2, String montant2) throws Exception {

        ObjContanier oc = doTraitementXLignes(session, transaction, ra, ic, dateDebut1, dateFin1, montant1, dateDebut2,
                dateFin2, montant2, 2);
        JADate dateDernierPmt = new JADate(REPmtMensuel.getDateDernierPmt(session));
        JADate dd = new JADate(dateDebut1);
        JACalendar cal = new JACalendarGregorian();
        if (cal.compare(dd, dateDernierPmt) != JACalendar.COMPARE_FIRSTUPPER) {
            this.addDollarsT_Periodes(session, transaction, oc.ra, dateDebut1, dateFin1, montant1, dateDebut2,
                    dateFin2, montant2);
        }
    }

    private ObjContanier doTraitementXLignes(BSession session, BTransaction transaction, RERenteAccordee ra,
            REInformationsComptabilite ic, String dateDebutL1, String dateFinL1, String montantL1, String dateDebutL2,
            String dateFinL2, String montantL2, int nbrLigne) throws Exception {

        ObjContanier result = new ObjContanier();

        // On charge la demande de rente.

        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);
        PRAssert.notIsNew(bc, "Entity not found");

        REDemandeRente dem = new REDemandeRente();
        dem.setSession(session);
        dem.setIdRenteCalculee(bc.getIdRenteCalculee());
        dem.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        dem.retrieve(transaction);
        PRAssert.notIsNew(dem, "Entity not found");

        REDemandeRente demSrc = REDemandeRente.loadDemandeRente(session, transaction, dem.getIdDemandeRente(),
                dem.getCsTypeDemandeRente());

        RERenteCalculee newRC = new RERenteCalculee();
        newRC.setSession(session);
        newRC.add(transaction);

        // Copie de la demande de rente !!!
        REDemandeRente newDemande = REDemandeRegles.copierDemandeRente(session, transaction, demSrc);
        newDemande.retrieve(transaction);
        newDemande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
        newDemande.setIdRenteCalculee(newRC.getIdRenteCalculee());
        newDemande.setDateTraitement(REACORParser.retrieveDateTraitement(demSrc));

        if (nbrLigne == 1) {
            newDemande.setDateDebut("01." + dateDebutL1);
            if (!JadeStringUtil.isBlankOrZero(dateFinL1)) {
                JADate df = new JADate(dateFinL1);
                JACalendar cal = new JACalendarGregorian();
                int dayInMonth = cal.daysInMonth(df.getMonth(), df.getYear());
                newDemande.setDateFin(dayInMonth + "." + dateFinL1);
            } else {
                newDemande.setDateFin("");
            }
        } else if (nbrLigne == 2) {
            newDemande.setDateDebut("01." + dateDebutL1);
            if (!JadeStringUtil.isBlankOrZero(dateFinL2)) {
                JADate df = new JADate(dateFinL2);
                JACalendar cal = new JACalendarGregorian();
                int dayInMonth = cal.daysInMonth(df.getMonth(), df.getYear());
                newDemande.setDateFin(dayInMonth + "." + dateFinL2);
            } else {
                newDemande.setDateFin("");
            }

        }

        newDemande.update(transaction);

        // On ajoute les périodes !!!
        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demSrc.getCsTypeDemandeRente())) {
            REPeriodeInvaliditeManager mgr = new REPeriodeInvaliditeManager();
            mgr.setSession(session);
            mgr.setForIdDemandeRente(demSrc.getIdDemandeRente());
            mgr.find(transaction);
            for (int i = 0; i < mgr.size(); i++) {
                REPeriodeInvalidite pDest = new REPeriodeInvalidite();
                pDest.setSession(session);
                REPeriodeInvalidite pSource = (REPeriodeInvalidite) mgr.get(i);
                pDest.setDateDebutInvalidite(pSource.getDateDebutInvalidite());
                pDest.setDateFinInvalidite(pSource.getDateFinInvalidite());
                pDest.setDegreInvalidite(pSource.getDegreInvalidite());
                pDest.setIdDemandeRente(newDemande.getIdDemandeRente());
                pDest.add(transaction);
            }
        } else if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(demSrc.getCsTypeDemandeRente())) {
            REPeriodeAPIManager mgr = new REPeriodeAPIManager();
            mgr.setSession(session);
            mgr.setForIdDemandeRente(demSrc.getIdDemandeRente());
            mgr.find(transaction);
            for (int i = 0; i < mgr.size(); i++) {
                REPeriodeAPI pDest = new REPeriodeAPI();
                pDest.setSession(session);
                REPeriodeAPI pSource = (REPeriodeAPI) mgr.get(i);
                pDest.setDateDebutInvalidite(pSource.getDateDebutInvalidite());
                pDest.setDateFinInvalidite(pSource.getDateFinInvalidite());
                pDest.setCsDegreImpotence(pSource.getCsDegreImpotence());
                pDest.setIsAssistancePratique(pSource.getIsAssistancePratique());
                pDest.setIsResidenceHome(pSource.getIsResidenceHome());
                pDest.setCsGenreDroitApi(pSource.getCsGenreDroitApi());
                pDest.setTypePrestation(pSource.getTypePrestation());
                pDest.setTypePrestationHistorique(pSource.getTypePrestationHistorique());
                pDest.setIdDemandeRente(newDemande.getIdDemandeRente());
                pDest.add(transaction);
            }

        }

        // Création de la base de calcul !!!

        // Copie de la base de calcul
        REBasesCalcul newBC = null;
        if ("10".equals(bc.getDroitApplique())) {
            newBC = new REBasesCalculDixiemeRevision();
            newBC.setSession(session);
            newBC.setAnneeBonifTacheAssistance(bc.getAnneeBonifTacheAssistance());
            newBC.setAnneeBonifTacheEduc(bc.getAnneeBonifTacheEduc());
            newBC.setAnneeBonifTransitoire(bc.getAnneeBonifTransitoire());
            newBC.setAnneeCotiClasseAge(bc.getAnneeCotiClasseAge());
            newBC.setAnneeDeNiveau(bc.getAnneeDeNiveau());
            newBC.setAnneeTraitement(bc.getAnneeTraitement());
            newBC.setCleInfirmiteAyantDroit(bc.getCleInfirmiteAyantDroit());
            newBC.setCodeOfficeAi(bc.getCodeOfficeAi());
            newBC.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
            newBC.setDegreInvalidite(bc.getDegreInvalidite());
            newBC.setQuotiteRente(bc.getQuotiteRente());
            newBC.setDroitApplique(bc.getDroitApplique());
            newBC.setDureeCotiAvant73(bc.getDureeCotiAvant73());
            newBC.setDureeCotiDes73(bc.getDureeCotiDes73());
            newBC.setDureeRevenuAnnuelMoyen(bc.getDureeRevenuAnnuelMoyen());
            newBC.setEchelleRente(bc.getEchelleRente());
            newBC.setFacteurRevalorisation(bc.getFacteurRevalorisation());
            newBC.setIdRenteCalculee(newRC.getIdRenteCalculee());
            newBC.setIdTiersBaseCalcul(bc.getIdTiersBaseCalcul());
            newBC.setInvaliditePrecoce(bc.isInvaliditePrecoce());
            newBC.setIsDemandeRenteAPI(bc.isDemandeRenteAPI());
            newBC.setIsPartageRevenuActuel(bc.getIsPartageRevenuCalcul());
            newBC.setLimiteRevenu(bc.isLimiteRevenu());
            newBC.setMinimuGaranti(bc.isMinimuGaranti());
            newBC.setMoisAppointsAvant73(bc.getMoisAppointsAvant73());
            newBC.setMoisAppointsDes73(bc.getMoisAppointsDes73());
            newBC.setMoisCotiAnneeOuvertDroit(bc.getMoisCotiAnneeOuvertDroit());
            newBC.setMontantMaxR10Ech44(bc.getMontantMaxR10Ech44());
            newBC.setNombreAnneeBTE1(bc.getNombreAnneeBTE1());
            newBC.setNombreAnneeBTE2(bc.getNombreAnneeBTE2());
            newBC.setNombreAnneeBTE4(bc.getNombreAnneeBTE4());
            newBC.setPeriodeAssEtrangerAv73(bc.getPeriodeAssEtrangerAv73());
            newBC.setPeriodeAssEtrangerDes73(bc.getPeriodeAssEtrangerDes73());
            newBC.setPeriodeJeunesse(bc.getPeriodeJeunesse());
            newBC.setResultatComparatif(bc.getResultatComparatif());
            newBC.setRevenuAnnuelMoyen(bc.getRevenuAnnuelMoyen());
            newBC.setRevenuJeunesse(bc.getRevenuJeunesse());
            newBC.setRevenuPrisEnCompte(bc.getRevenuPrisEnCompte());
            newBC.setRevenuSplitte(bc.isRevenuSplitte());
            newBC.setSupplementCarriere(bc.getSupplementCarriere());
            newBC.setSurvenanceEvtAssAyantDroit(bc.getSurvenanceEvtAssAyantDroit());
            newBC.setTypeCalculComparatif(bc.getTypeCalculComparatif());
            newBC.add(transaction);
        }
        // 9ème révision...
        else {
            newBC = new REBasesCalculNeuviemeRevision();
            newBC.setSession(session);
            newBC.setAnneeBonifTacheAssistance(bc.getAnneeBonifTacheAssistance());
            newBC.setAnneeBonifTacheEduc(bc.getAnneeBonifTacheEduc());
            newBC.setAnneeBonifTransitoire(bc.getAnneeBonifTransitoire());
            newBC.setAnneeCotiClasseAge(bc.getAnneeCotiClasseAge());
            newBC.setAnneeDeNiveau(bc.getAnneeDeNiveau());
            newBC.setAnneeTraitement(bc.getAnneeTraitement());
            newBC.setCleInfirmiteAyantDroit(bc.getCleInfirmiteAyantDroit());
            newBC.setCodeOfficeAi(bc.getCodeOfficeAi());
            newBC.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
            newBC.setDegreInvalidite(bc.getDegreInvalidite());
            newBC.setQuotiteRente(bc.getQuotiteRente());
            newBC.setDroitApplique(bc.getDroitApplique());
            newBC.setDureeCotiAvant73(bc.getDureeCotiAvant73());
            newBC.setDureeCotiDes73(bc.getDureeCotiDes73());
            newBC.setDureeRevenuAnnuelMoyen(bc.getDureeRevenuAnnuelMoyen());
            newBC.setEchelleRente(bc.getEchelleRente());
            newBC.setFacteurRevalorisation(bc.getFacteurRevalorisation());
            newBC.setIdRenteCalculee(newRC.getIdRenteCalculee());
            newBC.setIdTiersBaseCalcul(bc.getIdTiersBaseCalcul());
            newBC.setInvaliditePrecoce(bc.isInvaliditePrecoce());
            newBC.setIsDemandeRenteAPI(bc.isDemandeRenteAPI());
            newBC.setIsPartageRevenuActuel(bc.getIsPartageRevenuCalcul());
            newBC.setLimiteRevenu(bc.isLimiteRevenu());
            newBC.setMinimuGaranti(bc.isMinimuGaranti());
            newBC.setMoisAppointsAvant73(bc.getMoisAppointsAvant73());
            newBC.setMoisAppointsDes73(bc.getMoisAppointsDes73());
            newBC.setMoisCotiAnneeOuvertDroit(bc.getMoisCotiAnneeOuvertDroit());
            newBC.setMontantMaxR10Ech44(bc.getMontantMaxR10Ech44());
            newBC.setNombreAnneeBTE1(bc.getNombreAnneeBTE1());
            newBC.setNombreAnneeBTE2(bc.getNombreAnneeBTE2());
            newBC.setNombreAnneeBTE4(bc.getNombreAnneeBTE4());
            newBC.setPeriodeAssEtrangerAv73(bc.getPeriodeAssEtrangerAv73());
            newBC.setPeriodeAssEtrangerDes73(bc.getPeriodeAssEtrangerDes73());
            newBC.setPeriodeJeunesse(bc.getPeriodeJeunesse());
            newBC.setResultatComparatif(bc.getResultatComparatif());
            newBC.setRevenuAnnuelMoyen(bc.getRevenuAnnuelMoyen());
            newBC.setRevenuJeunesse(bc.getRevenuJeunesse());
            newBC.setRevenuPrisEnCompte(bc.getRevenuPrisEnCompte());
            newBC.setRevenuSplitte(bc.isRevenuSplitte());
            newBC.setSupplementCarriere(bc.getSupplementCarriere());
            newBC.setSurvenanceEvtAssAyantDroit(bc.getSurvenanceEvtAssAyantDroit());
            newBC.setTypeCalculComparatif(bc.getTypeCalculComparatif());

            REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
            bc9.setSession(session);
            bc9.setIdBasesCalcul(bc.getIdBasesCalcul());
            bc9.retrieve(transaction);

            ((REBasesCalculNeuviemeRevision) newBC).setBonificationTacheEducative(bc9.getBonificationTacheEducative());
            ((REBasesCalculNeuviemeRevision) newBC).setNbrAnneeEducation(bc9.getNbrAnneeEducation());
            ((REBasesCalculNeuviemeRevision) newBC).setCodeOfficeAiEpouse(bc9.getDegreInvalidite());
            ((REBasesCalculNeuviemeRevision) newBC).setDegreInvaliditeEpouse(bc9.getDegreInvaliditeEpouse());
            ((REBasesCalculNeuviemeRevision) newBC).setCleInfirmiteEpouse(bc9.getCleInfirmiteEpouse());
            ((REBasesCalculNeuviemeRevision) newBC).setSurvenanceEvenementAssureEpouse(bc9
                    .getSurvenanceEvenementAssureEpouse());
            ((REBasesCalculNeuviemeRevision) newBC).setInvaliditePrecoceEpouse(bc9.isInvaliditePrecoceEpouse());
            newBC.add(transaction);
        }

        if (nbrLigne == 1) {
            result.ra = doTraitement1RA1dollarP(session, transaction, ra, newBC.getIdBasesCalcul(), ic, dateDebutL1,
                    dateFinL1, montantL1);

        } else if (nbrLigne == 2) {
            result.ra = doTraitement1RA2dollarP(session, transaction, ra, newBC.getIdBasesCalcul(), ic, dateDebutL1,
                    dateFinL1, montantL1, dateDebutL2, dateFinL2, montantL2);

        }
        result.bc = newBC;
        return result;
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }
}
