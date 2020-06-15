package globaz.osiris.db.interet.tardif;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.db.rdp.CORequisitionPoursuiteUtil;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CASectionAuxPoursuites;
import globaz.osiris.db.contentieux.CASectionAuxPoursuitesManager;
import globaz.osiris.db.interet.util.CAInteretUtil;
import globaz.osiris.db.interet.util.ecriturenonsoumise.CAEcritureNonSoumise;
import globaz.osiris.db.interet.util.ecriturenonsoumise.CAEcritureNonSoumiseManager;
import globaz.osiris.db.interet.util.planparsection.CAPlanParSection;
import globaz.osiris.db.interet.util.planparsection.CAPlanParSectionManager;
import globaz.osiris.db.interet.util.tauxParametres.CATauxParametre;
import globaz.osiris.db.interets.CADetailInteretMoratoire;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.translation.CACodeSystem;
import globaz.osiris.utils.CATiersUtil;

import java.rmi.RemoteException;
import java.util.*;

public abstract class CAInteretTardif {

    private String idJournal;
    private String idSection;
    private FWCurrency montantSoumisSurcisCalcul;
    private FWCurrency montantCumuleSurcisCalcul;
    private CASection section;
    Map<Periode, Boolean> mapMotifs;

    /**
     * Caculer les intérêts moratoires tardifs pour une section.
     *
     * @param session
     * @param transaction
     * @throws Exception
     */
    public void calculer(BSession session, BTransaction transaction) throws Exception {
        CAPlanParSectionManager manager = CAInteretUtil.getSectionPlans(session, transaction, null, getIdSection(),
                true);

        for (int i = 0; i < manager.size(); i++) {
            CAPlanParSection plan = (CAPlanParSection) manager.get(i);

            CAInteretMoratoire interet = CAInteretUtil.getInteretMoratoire(transaction, plan.getIdPlan(),
                    getIdSection(), getIdJournal());
            // CAInteretMoratoire interet = this.getInteretMoratoire(session, transaction, plan.getIdPlan());

            if (interet.isNew()) {
                FWCurrency montantSoumis = CAInteretUtil.getMontantSoumisParPlans(transaction, getIdSection(),
                        plan.getIdPlan(), null);

                if ((montantSoumis != null) && montantSoumis.isPositive()) {
                    creerInteret(session, transaction, plan, interet, montantSoumis);
                }
            }
        }
    }

    /**
     * Pour chaque écriture (montant cumulé par date) créer une ligne de détail pour l'intérêt.<br/>
     * Lors de l'ajout de la première ligne l'intérêt sera également ajouté.<br/>
     * Cumul les lignes pour déterminer si l'intérêt est soumis ou exempté.
     *
     * @param session
     * @param transaction
     * @param plan
     * @param interet
     * @param montantSoumis
     * @throws Exception
     */
    private void creerInteret(BSession session, BTransaction transaction, CAPlanParSection plan,
                              CAInteretMoratoire interet, FWCurrency montantSoumis) throws Exception {
        // Liste les écritures du plan et non pas du compte courant
        CAEcritureNonSoumiseManager manager = CAInteretUtil.getEcrituresNonSoumises(session, transaction,
                plan.getIdPlan(), getIdSection(), null, null);

        FWCurrency montantCumule = new FWCurrency();
        montantCumuleSurcisCalcul = new FWCurrency();
        boolean aControlerManuellement = false;
        JADate dateCalculDebutInteret = getDateCalculDebutInteret(session, transaction);
        for (int i = 0; i < manager.size(); i++) {
            CAEcritureNonSoumise ecriture = (CAEcritureNonSoumise) manager.get(i);

            if (ecriture.getMontantToCurrency().isNegative()) {
                if (montantSoumis.isPositive() && isTardif(session, transaction, ecriture.getDate())) {
                    if (CommonProperties.TAUX_INTERET_PANDEMIE.getBooleanValue()) {
                        List<Periode> listPeriodeMotifsSurcis = CAInteretUtil.isSectionSurcisProgaPaiementInPandemie(transaction, session, idSection);
                        if (!listPeriodeMotifsSurcis.isEmpty()) {
                            creerInteretForSurcisProro(session, transaction, dateCalculDebutInteret, ecriture.getJADate(), listPeriodeMotifsSurcis, interet,montantSoumis);
                            montantCumule = montantCumuleSurcisCalcul;
                        } else {
                            boolean isFirst = true;
                            JADate dateCalculDebut = dateCalculDebutInteret;
                            JADate dateCalculFin = ecriture.getJADate();
                            List<CATauxParametre> listTaux = CAInteretUtil.getTaux(transaction, dateCalculDebut.toStr("."), dateCalculFin.toStr("."), CAInteretUtil.CS_PARAM_TAUX, 2);
                            for (CATauxParametre CATauxParametre : listTaux) {
                                double taux = CATauxParametre.getTaux();
                                JADate dateDebut;
                                JADate dateFin;
                                //Aide pour découpage des périodes après la première ligne
                                if (isFirst) {
                                    dateDebut = dateCalculDebut;
                                    isFirst = false;
                                } else {
                                    dateDebut = new JADate(CATauxParametre.getDateDebut().getSwissValue());
                                }
                                if (CATauxParametre.getDateFin() == null) {
                                    dateFin = dateCalculFin;
                                } else {
                                    dateFin = new JADate(CATauxParametre.getDateFin().getSwissValue());
                                }
                                FWCurrency montantInteret = CAInteretUtil.getMontantInteret(session, montantSoumis,
                                        dateFin, dateDebut, taux);

                                if ((montantInteret != null)) {
                                    CADetailInteretMoratoire ligne = new CADetailInteretMoratoire();
                                    ligne.setSession(session);

                                    ligne.setMontantInteret(montantInteret.toString());

                                    ligne.setMontantSoumis(montantSoumis.toString());

                                    ligne.setDateDebut(dateDebut.toStr("."));

                                    ligne.setTaux(String.valueOf(taux));

                                    ligne.setDateFin(dateFin.toStr("."));

                                    if (interet.isNew()) {
                                        interet.add(transaction);
                                    }
                                    ligne.setAnneeCotisation("0");
                                    ligne.setIdInteretMoratoire(interet.getIdInteretMoratoire());
                                    ligne.add(transaction);
                                }
                                montantCumule.add(montantInteret);
                            }
                            dateCalculDebutInteret = session.getApplication().getCalendar().addDays(ecriture.getJADate(), 1);
                        }
                    } else {
                        double taux = CAInteretUtil.getTaux(transaction, dateCalculDebutInteret.toStr("."));
                        FWCurrency montantInteret = CAInteretUtil.getMontantInteret(session, montantSoumis,
                                ecriture.getJADate(), dateCalculDebutInteret, taux);

                        if ((montantInteret != null) && !montantInteret.isZero()) {
                            CADetailInteretMoratoire ligne = new CADetailInteretMoratoire();
                            ligne.setSession(session);

                            ligne.setMontantInteret(montantInteret.toString());

                            ligne.setMontantSoumis(montantSoumis.toString());

                            ligne.setDateDebut(dateCalculDebutInteret.toStr("."));

                            ligne.setTaux(String.valueOf(taux));

                            ligne.setDateFin(ecriture.getJADate().toStr("."));

                            if (interet.isNew()) {
                                interet.add(transaction);
                            }
                            ligne.setAnneeCotisation("0");
                            ligne.setIdInteretMoratoire(interet.getIdInteretMoratoire());
                            ligne.add(transaction);
                        }

                        montantCumule.add(montantInteret);
                        dateCalculDebutInteret = session.getApplication().getCalendar().addDays(ecriture.getJADate(), 1);
                    }
                }
                montantSoumis.add(ecriture.getMontantToCurrency());
            } else {
                if (ecriture.getMontantToCurrency().isPositive()) {
                    aControlerManuellement = true;
                }
            }
        }

        updateMotifCalculInteretMoratoire(session, transaction, interet, montantCumule, aControlerManuellement);
    }


    private void creerInteretForSurcisProro(BSession session, BTransaction transaction, JADate dateCalculDebut, JADate dateCalculFin, List<Periode> listPeriodeMotifsSurcis, CAInteretMoratoire interet,FWCurrency montantSoumis) throws Exception {
        Map<String, CATauxParametre> mapTaux = CAInteretUtil.getTauxInMap(transaction, dateCalculDebut.toStr("."), dateCalculFin.toStr("."), CAInteretUtil.CS_PARAM_TAUX, 2);
        Map<String, CATauxParametre> mapTauxSurcisProro = CAInteretUtil.getTauxInMap(transaction, dateCalculDebut.toStr("."), dateCalculFin.toStr("."), CAInteretUtil.CS_PARAM_TAUX_SURCIS_PROGATION_PANDEMIE_2020, 2);
        Map<Periode, Boolean> mapIntermediaire = new LinkedHashMap<>();
        Map<Periode, CATauxParametre> mapLigneAInscrire = new LinkedHashMap<>();
        JADate dateDebut = dateCalculDebut;
        JADate dateFin = null;
        boolean isFirst = true;
        //Cas 1 : Multiple motif en surcis/prorogation
//        Periode periodeCalcul = new Periode(dateCalculDebut.toStr("."), dateCalculFin.toStr("."));
//        for (Periode periodeMotifsRaw : listPeriodeMotifsSurcis) {
//            if (periodeCalcul.comparerChevauchement(periodeMotifsRaw) == Periode.ComparaisonDePeriode.LES_PERIODES_SONT_INDEPENDANTES) {
//                listPeriodeMotifsSurcis.remove(periodeMotifsRaw);
//            }
//        }
        Collections.sort(listPeriodeMotifsSurcis);
        /**
         * Préparation des différents périodes avec switch sur les 2 types de taux
         */
        mapIntermediaire = CAInteretUtil.preparesPeriodeInteretsCovids(session, dateCalculDebut, dateCalculFin, listPeriodeMotifsSurcis);

        /**
         * Préparation des lignes des intérêts avec les bons taux
         */
        CAInteretUtil.createLignesAInscrire(session, mapIntermediaire, mapLigneAInscrire, mapTaux, mapTauxSurcisProro);
//

        FWCurrency montantInteret;
        CATauxParametre taux;
        for (Periode keyAVS : mapLigneAInscrire.keySet()) {
            taux = mapLigneAInscrire.get(keyAVS);
            dateDebut = new JADate(keyAVS.getDateDebut());
            dateFin = new JADate(keyAVS.getDateFin());
            montantInteret = CAInteretUtil.getMontantInteret(session, montantSoumis,
                    dateFin, dateDebut, taux.getTaux());
            if ((montantInteret != null)) {
                CADetailInteretMoratoire ligne = new CADetailInteretMoratoire();
                ligne.setSession(session);

                ligne.setMontantInteret(montantInteret.toString());

                ligne.setMontantSoumis(montantSoumis.toString());

                ligne.setDateDebut(dateDebut.toStr("."));

                ligne.setTaux(String.valueOf(taux.getTaux()));

                ligne.setDateFin(dateFin.toStr("."));

                if (interet.isNew()) {
                    interet.add(transaction);
                }
                ligne.setAnneeCotisation("0");
                ligne.setIdInteretMoratoire(interet.getIdInteretMoratoire());
                ligne.add(transaction);
            }
            montantCumuleSurcisCalcul.add(montantInteret);
        }

    }


    /**
     * Return la date à partir de laquelle le calcul de l'intérêt, pour l'écriture soumise, doit être fait.
     *
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public abstract JADate getDateCalculDebutInteret(BSession session, BTransaction transaction) throws Exception;

    public String getIdJournal() {
        return idJournal;
    }

    public String getIdSection() {
        return idSection;
    }

    // /**
    // * Return l'intérêt moratoire tardif lié à la section. Si ce dernier n'est pas trouvé alors créé un nouveau sans
    // * l'ajouter en base de données.
    // *
    // * @param session
    // * @param transaction
    // * @param idPlan
    // * @return
    // * @throws Exception
    // */
    // public CAInteretMoratoire getInteretMoratoire(BSession session, BTransaction transaction, String idPlan)
    // throws Exception {
    // CAInteretMoratoireManager manager = new CAInteretMoratoireManager();
    // manager.setSession(session);
    // manager.setForIdSection(this.getIdSection());
    // manager.setForIdPlan(idPlan);
    //
    // ArrayList<String> idGenreInteretIn = new ArrayList<String>();
    // idGenreInteretIn.add(CAGenreInteret.CS_TYPE_TARDIF);
    // manager.setForIdGenreInteretIn(idGenreInteretIn);
    //
    // manager.setForMotifCalculNot(CAInteretMoratoire.CS_A_CONTROLER);
    //
    // manager.find(transaction, BManager.SIZE_NOLIMIT);
    //
    // if (manager.hasErrors()) {
    // throw new Exception(manager.getErrors().toString());
    // }
    //
    // if (manager.isEmpty()) {
    // CAInteretMoratoire interet = new CAInteretMoratoire();
    // interet.setSession(session);
    //
    // interet.setIdSection(this.getIdSection());
    //
    // interet.setDateCalcul(JACalendar.today().toStr("."));
    // interet.setMotifcalcul(CAInteretMoratoire.CS_EXEMPTE);
    // interet.setIdGenreInteret(CAGenreInteret.CS_TYPE_TARDIF);
    // interet.setIdJournalCalcul(this.getIdJournal());
    // interet.setIdPlan(idPlan);
    // interet.setIdRubrique(CAInteretUtil.getIdContrePartie(session, transaction, idPlan,
    // CAGenreInteret.CS_TYPE_TARDIF));
    //
    // // Pas de add => pas chargé base avant insert réel (vraiment tardif)
    //
    // return interet;
    // } else {
    // return (CAInteretMoratoire) manager.getFirstEntity();
    // }
    // }

    /**
     * Return la section en cours.
     *
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public CASection getSection(BSession session, BTransaction transaction) throws Exception {
        if (section == null) {
            section = new CASection();
            section.setSession(session);

            section.setIdSection(getIdSection());

            section.retrieve(transaction);

            if (section.hasErrors() || section.isNew()) {
                throw new Exception(session.getLabel("5126"));
            }
        }

        return section;
    }

    /**
     * L'écriture est-elle réellement tardive ?
     *
     * @param session
     * @param transaction
     * @param dateToTest
     * @return
     * @throws Exception
     */
    public abstract boolean isTardif(BSession session, BTransaction transaction, String dateToTest) throws Exception;

    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    public void setIdSection(String idSection) {
        this.idSection = idSection;
    }

    /**
     * Mise à jour du motif de calcul de l'intérêt moratoire tardif créé.
     *
     * @param session
     * @param transaction
     * @param interet
     * @param montantCumule
     * @param aControlerManuellement
     * @throws Exception
     */
    private void updateMotifCalculInteretMoratoire(BSession session, BTransaction transaction,
                                                   CAInteretMoratoire interet, FWCurrency montantCumule, boolean aControlerManuellement) throws Exception {
        if (!interet.isNew()) {
            if (getSection(session, transaction).isSectionAuxPoursuites(false)) {
                interet.setRemarque(session.getLabel("IM_ENPOURSUITE"));
                interet.update(transaction);
            } else if (getSection(session, transaction).hasMotifContentieux(CACodeSystem.CS_IRRECOUVRABLE)
                    || ((CACompteAnnexe) getSection(session, transaction).getCompteAnnexe())
                    .isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE)) {
                interet.setRemarque(session.getLabel("IM_IRRECOUVRABLES"));
                interet.update(transaction);
            } else {
                if (aControlerManuellement) {
                    interet.setMotifcalcul(CAInteretMoratoire.CS_A_CONTROLER);
                    interet.update(transaction);
                } else {
                    FWCurrency limiteExempte = new FWCurrency(CAInteretUtil.getLimiteExempteInteretMoratoire(session,
                            transaction));

                    if (montantCumule.compareTo(limiteExempte) == 1) {
                        interet.setMotifcalcul(CAInteretMoratoire.CS_SOUMIS);
                        interet.update(transaction);
                    }
                }
            }
        }
    }

    /**
     * Retourne true si la date d'exécution de la section aux poursuites est après la date de mise en production du
     * nouveau CDP (propriété en DB - dateProductionNouveauCDP)
     * Attention retourne false si la section aux poursuites est null
     *
     * @param sectionAuxPoursuite
     * @return
     * @throws RemoteException
     * @throws Exception
     */
    public static boolean isNouveauCDP(CASectionAuxPoursuites sectionAuxPoursuite) throws RemoteException, Exception {
        boolean isNouveauCDP = false;
        if (sectionAuxPoursuite != null) {

            String dateProductionNouveauCDP = GlobazSystem.getApplication("AQUILA").getProperty(
                    "dateProductionNouveauCDP");

            isNouveauCDP = !JadeStringUtil.isBlank(dateProductionNouveauCDP)
                    && !JadeDateUtil.isDateBefore(sectionAuxPoursuite.getHistorique().getDateExecution(),
                    dateProductionNouveauCDP);
        }
        return isNouveauCDP;
    }

    public static boolean isNouveauCalculPoursuite(BSession session, CASection section) throws Exception {
        if (section == null) {
            return false;
        }
        CASectionAuxPoursuites sectionAuxPoursuite = getSectionAuxPoursuites(session, section.getIdSection());

        // POAVS-223 ajout de && !isRDPProcess
        boolean isNouveauCDP = isNouveauCDP(sectionAuxPoursuite);

        // si le nouveau régime est activé
        if (isNouveauCDP) {
            // récupération du canton de l'office des poursuites relatif au tiers
            String cantonOfficePoursuite = CATiersUtil.getCantonOfficePoursuite(session, section.getCompteAnnexe()
                    .getTiers(), section.getIdExterne(), section.getCompteAnnexe().getIdExterneRole());
            if (JadeStringUtil.isBlank(cantonOfficePoursuite)) {
                JadeLogger.warn(FWMessage.ERREUR, session.getLabel("IM_ERR_OP_INTROUVABLE") + " : "
                        + section.getCompteAnnexe().getIdExterneRole() + " / " + section.getIdExterne());
            }

            // si le canton n'est pas exclu du nouveau régime on applique le nouveau régime
            if (!isOfficeExcluDuNouveauRegime(cantonOfficePoursuite, session)) {
                return true;
            }
        }
        return false;
    }

    public static CASectionAuxPoursuites getSectionAuxPoursuites(BSession session, String idSection) throws Exception {
        CASectionAuxPoursuitesManager managerReqPoursuite = new CASectionAuxPoursuitesManager();
        managerReqPoursuite.setSession(session);
        managerReqPoursuite.setForIdSection(idSection);
        // POAVS-294
        // managerReqPoursuite.setSoldeDifferentZero(true);
        managerReqPoursuite.find();
        CASectionAuxPoursuites sectionAuxPoursuite = (CASectionAuxPoursuites) managerReqPoursuite.getFirstEntity();

        return sectionAuxPoursuite;
    }

    public static Boolean isOfficeExcluDuNouveauRegime(String cantonOfficePoursuite, BSession session)
            throws RemoteException, Exception {
        return CORequisitionPoursuiteUtil.isOfficeDontWantToUseNewRegime(getSessionAquila(session),
                cantonOfficePoursuite);
    }

    private static BSession getSessionAquila(BSession session) throws RemoteException, Exception {
        return (BSession) GlobazSystem.getApplication("AQUILA").newSession(session);
    }

}
