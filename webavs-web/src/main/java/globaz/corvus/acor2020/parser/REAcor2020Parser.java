package globaz.corvus.acor2020.parser;

import acor.rentes.xsd.fcalcul.BaseEchelle;
import acor.rentes.xsd.fcalcul.FCalcul;
import acor.rentes.xsd.fcalcul.Rente;
import globaz.commons.nss.NSUtil;
import globaz.corvus.acor.parser.REFeuilleCalculVO;
import globaz.corvus.acor.parser.rev09.REACORParser;
import globaz.corvus.acor2020.business.FractionRente;
import globaz.corvus.acor2020.service.REImportationCalculAcor2020;
import globaz.corvus.api.basescalcul.IREBasesCalcul;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REAddRenteAccordee;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.regles.REDemandeRegles;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.acor2020.mapper.PRConverterUtils;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class REAcor2020Parser {


    public static final int MONTHS_IN_YEAR = 12;
    private static final int CODE_SPECIAL_AJOURNEMENT = 8;

    public static ReturnedValue doMAJPrestations(BSession session, BITransaction transaction, REDemandeRente demandeSource, FCalcul fCalcul, int noCasATraiter) throws PRACORException {
        ReturnedValue returnedValue = new ReturnedValue();

        try {
            // changer de parser si NNSS ou NAVS
            PRDemande demande = new PRDemande();
            demande.setSession(session);
            demande.setIdDemande(demandeSource.getIdDemandePrestation());
            demande.retrieve();

            if (demande.isNew()) {
                throw new PRACORException("Demande prestation non trouvée !!");
            }

            // --------------------------------------------------------------------------------------------------------------------
            // Cas de figure :
            //
            // 1) CAS_NOUVEAU_CALCUL (Standard)
            // ====================================================================================================================
            // ====================================================================================================================
            // Demande source (ENREGISTRE) --> Calculer
            //
            // Résutalt :
            // =============================
            //
            // Demande source
            // |__________ Base Calcul
            // |__________ RA1
            // |__________ RA2
            //
            //
            //
            // 2) CAS_RECALCUL_DEMANDE_VALIDEE (Recalcul à partir d'une demande
            // existante, VALIDE ou PAYE)
            // ====================================================================================================================
            // ====================================================================================================================
            //
            // Demande source#1 (PAYE ou VALIDE) --> Calculer
            // |__________ Base Calcul
            // |__________ RA1
            // |__________ RA2
            //
            // Résutalt :
            // =============================
            // A l'importation des données de ACOR, on va créer une nouvelle
            // demande.
            //
            // Demande source#1
            // |__________ Base Calcul
            // |__________ RA1
            // |__________ RA2
            //
            // Demande #2 (idParent==Demande source#1)
            // |__________ Base Calcul
            // |__________ RA3
            // |__________ RA4
            //
            //
            // Les rentes accordées RA3 et RA4 vont annuler RA1 et/ou RA2. Ce
            // traitement n'est pas automatisé
            // pour le moment.
            //
            //
            //
            //
            //
            // 3) CAS_RECALCUL_DEMANDE_NON_VALIDEE (Recalcul à partir d'une
            // demande existante, non encore validée. ENREGISTRE, AU CALCUL)
            // ====================================================================================================================
            // ====================================================================================================================
            //
            // Demande source#1 (CALCULE) --> Calculer
            // |__________ Base Calcul 1
            // |__________ RA1
            // |__________ RA2
            //
            // Résutalt :
            // =============================
            // A l'importation des données de ACOR, on va créer les BC, RA... et
            // supprimer les anciennes, si existantent.
            //
            // Demande source#1
            // |__________ Base Calcul 2
            // |__________ RA3
            // |__________ RA4
            //
            //
            //
            // --------------------------------------------------------------------------------------------------------------------

            REBasesCalcul bc;
            RERenteAccordee ra;
            String line = "be there or be square";
            List<Long> ids = new LinkedList<>();
            boolean isAnnoncePayForDemandeRente = false;

            /*
             * Toutes les demande non-validées de la famille sont réinitialisées au début de la remontée du calcul ACOR
             * Il faut s'assurer à ce stade que l'on à bien une rente calculée
             */
            RERenteCalculee rc = new RERenteCalculee();
            rc.setSession(session);
            rc.setIdRenteCalculee(demandeSource.getIdRenteCalculee());
            rc.retrieve(transaction);
            if (rc.isNew()) {
                rc.save();
                demandeSource.setIdRenteCalculee(rc.getIdRenteCalculee());
            }

            switch (noCasATraiter) {
                case REImportationCalculAcor2020.CAS_NOUVEAU_CALCUL:
                    break;
                case REImportationCalculAcor2020.CAS_RECALCUL_DEMANDE_NON_VALIDEE:
                    break;
                case REImportationCalculAcor2020.CAS_RECALCUL_DEMANDE_VALIDEE:
                    break;
                default:
                    throw new PRACORException("Il n'est pas permis de recalculer ce cas.");
            }

            boolean isDemandeCloneCreated = false;

            for (FCalcul.Evenement eachEvenement : fCalcul.getEvenement()) {
                FCalcul.Evenement.BasesCalcul.Decision.Prestation premierePrestation = null;
                for (FCalcul.Evenement.BasesCalcul eachBaseCalcul : eachEvenement.getBasesCalcul()) {

                    // On récupère la première prestation "valide" de la base de calcul pour récupérer le type du droit et le bénéficiaire.
                    for (FCalcul.Evenement.BasesCalcul.Decision eachDecision : eachBaseCalcul.getDecision()) {
                        for (FCalcul.Evenement.BasesCalcul.Decision.Prestation eachPrestation : eachDecision.getPrestation()) {
                            if (Objects.nonNull(eachPrestation.getRente())) {
                                premierePrestation = eachPrestation;
                                break;
                            }
                        }
                    }
                    if (Objects.nonNull(premierePrestation)) {
                        bc = importBaseCalcul(session, eachBaseCalcul, premierePrestation, fCalcul);


                        // Si la demande à déjà été clonée et que l'on passe une
                        // 2ème fois dans le traitement de la BC,
                        // on la traite comme s'il s'agissait d'un cas de
                        // nouveau calcul pour éviter de créer un 2ème clone de
                        // la demande.
                        /**
                         * ACK, nous avons besoin de connaître l'id de la copie de la demande qui est fait dans la
                         * méthode doTraiterBaseCalcul(..)
                         * Et oui doTraiterBaseCalcul(..) fait également plein de chose comme son nom ne l'indique
                         * pas....
                         */
                        IdDemandeRente idCopieDemande = new IdDemandeRente();
                        if ((noCasATraiter == REImportationCalculAcor2020.CAS_RECALCUL_DEMANDE_VALIDEE) && isDemandeCloneCreated) {
                            bc = doTraiterBaseCalcul(session, (BTransaction) transaction, demandeSource,
                                    bc, REImportationCalculAcor2020.CAS_NOUVEAU_CALCUL, idCopieDemande);
                        } else {
                            bc = doTraiterBaseCalcul(session, (BTransaction) transaction, demandeSource,
                                    bc, noCasATraiter, idCopieDemande);
                        }
                        returnedValue.setIdCopieDemande(idCopieDemande.getId());
                        if (noCasATraiter == REImportationCalculAcor2020.CAS_RECALCUL_DEMANDE_VALIDEE) {
                            isDemandeCloneCreated = true;
                        }

                        // TODO : le booléen isAjournement est true lorsque la ligne commence par $a sur ancien ACOR ...
//                        boolean isAjournement = Objects.nonNull(eachBaseCalcul.getAjournement());
                        boolean isAjournement = false;
                        for (FCalcul.Evenement.BasesCalcul.Decision eachDecision : eachBaseCalcul.getDecision()) {
                            for (FCalcul.Evenement.BasesCalcul.Decision.Prestation eachPrestation : eachDecision.getPrestation()) {
                                if (Objects.nonNull(eachPrestation.getRente())) {
                                    // importer les rentes accordées
                                    ra = importRenteAccordee(session, (BTransaction) transaction, demandeSource, eachPrestation, eachBaseCalcul, fCalcul);
                                    // si il y a une relation au requerant => la rente
                                    // accordee est pour un des membres de la famille
                                    isAnnoncePayForDemandeRente = (isAnnoncePayForDemandeRente || !JadeStringUtil.isIntegerEmpty(ra.getCsRelationAuRequerant()));

                                    ra.setIdBaseCalcul(bc.getIdBasesCalcul());

                                    // BZ 4427
                                    boolean isCreerRA = true;
                                    if (isAjournement) {
                                        ra.setCsEtat(IREPrestationAccordee.CS_ETAT_AJOURNE);

                                        // si les 3 champs sont vide, on remonte
                                        if (JadeStringUtil.isBlankOrZero(ra.getDureeAjournement())
                                                && JadeStringUtil.isBlankOrZero(ra.getSupplementAjournement())
                                                && JadeStringUtil.isBlankOrZero(ra.getDateRevocationAjournement())) {

                                            isCreerRA = true;

                                        } else // si un des 3 champs vide, on créé pas la RA
                                            if (JadeStringUtil.isBlankOrZero(ra.getDureeAjournement())
                                                    || JadeStringUtil.isBlankOrZero(ra.getSupplementAjournement())
                                                    || JadeStringUtil.isBlankOrZero(ra.getDateRevocationAjournement())) {
                                                isCreerRA = false;

                                                REBasesCalcul bcToDel = new REBasesCalcul();
                                                bcToDel.setSession(session);
                                                bcToDel.setIdBasesCalcul(ra.getIdBaseCalcul());
                                                bcToDel.retrieve();
                                                bcToDel.delete();
                                            }
                                    }
                                    if (isCreerRA) {
                                        Long idRA = Long.parseLong(REAddRenteAccordee.addRenteAccordeeCascade_noCommit(session,
                                                transaction, ra, IREValidationLevel.VALIDATION_LEVEL_NONE));
                                        ids.add(idRA);
                                    }

                                    // Traitement des prestations dues...
                                    Rente rente = eachPrestation.getRente();
                                    if (Objects.nonNull(rente)) {
                                        boolean nonAjournement = rente.getCodeCasSpecial().stream().allMatch(value -> value != CODE_SPECIAL_AJOURNEMENT);
                                        returnedValue.getRemarquesParticulieres().addAll(rente.getRemarque());
                                        Rente.Versement versement = rente.getVersement();
                                        // Si versement est non null, on est sur un $t (total)
                                        if (Objects.nonNull(versement)) {
                                            REPrestationDue pd = new REPrestationDue();
                                            pd.setSession(session);
                                            // si versement non null, on est sur $t
                                            pd.setCsType(IREPrestationDue.CS_TYPE_MNT_TOT);
                                            String dateDernierPmt = REPmtMensuel.getDateDernierPmt(session);

                                            String dateDeTraitement = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(rente.getMoisRapport(), StringUtils.EMPTY));
                                            JADate jDateDateDernierPmt = new JADate(dateDernierPmt);
                                            JADate jDateDateDebutPmt = new JADate(dateDeTraitement);

                                            JACalendar cal = new JACalendarGregorian();
                                            if (cal.compare(jDateDateDebutPmt, jDateDateDernierPmt) != JACalendar.COMPARE_EQUALS) {

                                                throw new Exception("Le calcul dans ACOR doit se faire avec une date de traitement du mois courant.");
                                            }
                                            pd.setDateDebutPaiement(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(versement.getDebut(), StringUtils.EMPTY)));
                                            pd.setDateFinPaiement(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(versement.getFin(), StringUtils.EMPTY)));
                                            if (nonAjournement) {
                                                pd.setMontant(Objects.toString(versement.getMontant(), StringUtils.EMPTY));
                                            }
                                            pd.setCsTypePaiement(null);
                                            pd.setIdRenteAccordee(ra.getIdPrestationAccordee());
                                            pd.add(transaction);
                                        }

                                        for (Rente.Etat eachEtat : rente.getEtat()) {
                                            REPrestationDue pd = importPrestationsDues(session, eachEtat, nonAjournement);

                                            pd.setIdRenteAccordee(ra.getIdPrestationAccordee());
                                            pd.add(transaction);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!isAnnoncePayForDemandeRente) {
                throw new PRACORException("Le fichier annonce.pay ne correspond pas à la demande de rente.");
            }

            // Settage de tous les idRA
            returnedValue.getIdRenteAccordees().addAll(ids);

            // Mise à jour dateDebut et dateFin de la demande
            // --> La date de début représente la date la plus petite des ra
            // --> La date de fin représente la date la plus grande des ra, sauf
            // si un des ra n'a pas de fin, dans ce cas, on laisse vide

            String firstDateDebutRA = "";
            String lastDateFinRA = "";
            boolean isRAWithoutDateFin = false;

            for (Long idRA : ids) {

                // Retrieve de la ra
                RERenteAccordee renteAcc = new RERenteAccordee();
                renteAcc.setSession(session);
                renteAcc.setIdPrestationAccordee(idRA.toString());
                renteAcc.retrieve(transaction);

                // Date de début
                if (!JadeStringUtil.isBlankOrZero(renteAcc.getDateDebutDroit())) {
                    if (firstDateDebutRA.length() == 0) {
                        firstDateDebutRA = renteAcc.getDateDebutDroit();
                    } else {
                        if (Integer.parseInt(
                                PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(renteAcc.getDateDebutDroit())) < Integer.parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(firstDateDebutRA))) {
                            firstDateDebutRA = renteAcc.getDateDebutDroit();
                        }
                    }
                }

                // Date de fin
                if (!JadeStringUtil.isBlankOrZero(renteAcc.getDateFinDroit())) {
                    if (lastDateFinRA.length() == 0) {
                        lastDateFinRA = renteAcc.getDateFinDroit();
                    } else {
                        if (Integer.parseInt(
                                PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(renteAcc.getDateFinDroit())) > Integer.parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(lastDateFinRA))) {
                            lastDateFinRA = renteAcc.getDateFinDroit();
                        }
                    }
                }

                // période infinie ?
                if (JadeStringUtil.isBlankOrZero(renteAcc.getDateFinDroit())) {
                    isRAWithoutDateFin = true;
                }

            }

            demandeSource.setDateDebut(firstDateDebutRA);

            if (isRAWithoutDateFin) {
                demandeSource.setDateFin("");
            } else {
                // on cherche le dernier jour du mois de fin
                JACalendar cal = new JACalendarGregorian();
                JADate dateFin = cal.addMonths(new JADate(lastDateFinRA), 1);
                dateFin = cal.addDays(dateFin, -1);
                demandeSource.setDateFin(dateFin.toStr("."));
            }

            demandeSource.update(transaction);

            // Mise à jours des PrestationsDues Date de fin et l'état.
            // Parcours des RA importées
            for (Long idRa : ids) {

                REPrestationsDuesManager mgr = new REPrestationsDuesManager();
                mgr.setSession(session);
                mgr.setForIdRenteAccordes(idRa.toString());
                mgr.setForCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
                mgr.setOrderBy(REPrestationDue.FIELDNAME_DATE_DEBUT_PAIEMENT + " ASC ");
                mgr.find(transaction, BManager.SIZE_NOLIMIT);
                Iterator<REPrestationDue> iterPD = mgr.iterator();

                // charger la ra en question
                RERenteAccordee ra1 = new RERenteAccordee();
                ra1.setSession(session);
                ra1.setIdPrestationAccordee(idRa.toString());
                ra1.retrieve(transaction);

                REPrestationDue courant = null;
                REPrestationDue suivant = null;

                // Cas spécial, workaround !!!
                // Dans le cas ou aucun $p n'a été retourné par ACOR, il faut le créer
                // Ce cas arrive lors d'anticipation d'une demande de rente.
                // Lors du 2ème cas d'assurance ACOR ne retourne pas les $p.
                // Également dans le cas ou l'on saisi à l'avance une demande
                // vieillesse par exemple.

                JACalendar cal = new JACalendarGregorian();
                if (!iterPD.hasNext()) {

                    // On créé manuellement le $p.
                    REPrestationDue pd = new REPrestationDue();
                    pd.setSession(session);

                    pd.setCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
                    pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
                    pd.setCsTypePaiement(null);

                    pd.setDateDebutPaiement(ra1.getDateDebutDroit());
                    pd.setMontant(ra1.getMontantPrestation());
                    pd.setMontantReductionAnticipation(ra1.getMontantReducationAnticipation());
                    pd.setMontantSupplementAjournement(ra1.getSupplementAjournement());

                    REBasesCalcul baseCal = new REBasesCalcul();
                    baseCal.setSession(session);
                    baseCal.setIdBasesCalcul(ra1.getIdBaseCalcul());
                    baseCal.retrieve(transaction);
                    PRAssert.notIsNew(baseCal, "Impossible de récupérer la base de calcul (101).");

                    pd.setRam(baseCal.getRevenuAnnuelMoyen());
                    pd.setIdRenteAccordee(ra1.getIdPrestationAccordee());
                    pd.add(transaction);

                    // On recharge l'itérator... pour la suite du traitement
                    mgr.find(transaction, BManager.SIZE_NOLIMIT);
                    iterPD = mgr.iterator();

                }

                // Parcours des prestations dues pour chacune des RA
                while (iterPD.hasNext()) {
                    if (courant == null) {
                        courant = iterPD.next();
                    }

                    suivant = null;
                    if (iterPD.hasNext()) {
                        suivant = iterPD.next();
                    }

                    // On set la date de fin de la prestation dues courant, avec
                    // la date début de
                    // la prestation suivante, moins 1 mois.
                    if ((suivant != null) && JadeStringUtil.isBlankOrZero(courant.getDateFinPaiement())) {
                        JADate df = new JADate(suivant.getDateDebutPaiement());

                        cal = new JACalendarGregorian();
                        JADate dfCourant = cal.addMonths(df, -1);
                        JADate theDf = new JADate(dfCourant.toStr("."));

                        if (BSessionUtil.compareDateFirstLower(session, ra1.getDateFinDroit(),
                                PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(theDf.toStrAMJ()))
                                && !JadeStringUtil.isBlankOrZero(ra1.getDateFinDroit())) {

                            theDf = new JADate(ra1.getDateFinDroit());
                        }

                        courant.setDateFinPaiement(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(theDf.toStrAMJ()));

                        // courant.setCsEtat(IREPrestationDue.CS_ETAT_TRAITE);
                        courant.update(transaction);
                    } else if ((suivant == null) && !JadeStringUtil.isBlankOrZero(ra1.getDateFinDroit())) {
                        courant.setDateFinPaiement(ra1.getDateFinDroit());
                        courant.update(transaction);
                    }

                    courant = suivant;
                    if ((null != suivant) && !iterPD.hasNext()) {
                        if (JadeStringUtil.isBlankOrZero(courant.getDateFinPaiement())
                                && !JadeStringUtil.isBlankOrZero(ra1.getDateFinDroit())) {
                            courant.setDateFinPaiement(ra1.getDateFinDroit());
                            courant.update(transaction);
                        }
                    }
                }
            }

            // Dernière étape : MAJ des adresse de pmt des rentes accordées
            // créées
            REBeneficiairePrincipal.doMajAdressePmtDesRentesAccordees(session, transaction, ids);
        } catch (Exception e) {
            throw new PRACORException("impossible de parser : " + e.getMessage(), e);
        }

        return returnedValue;
//        return;
    }

    /**
     * Importe les données de ACOR dans une base de calcul.
     *
     * @param session
     * @param baseCalcul
     * @param premierePrestation
     * @return
     */
    private static REBasesCalcul importBaseCalcul(final BSession session, FCalcul.Evenement.BasesCalcul baseCalcul, FCalcul.Evenement.BasesCalcul.Decision.Prestation premierePrestation, FCalcul fCalcul) {

        REBasesCalcul bc = new REBasesCalculDixiemeRevision();
        bc.setSession(session);

//        String nssTiersBaseCalcul = REACORAbstractFlatFileParser.getField(line, fields, "NSS");
        String nssTiersBaseCalcul = premierePrestation.getBeneficiaire();

        try {
            PRTiersWrapper tiersBaseCalcul = PRTiersHelper.getTiers(session,
                    NSUtil.formatAVSUnknown(nssTiersBaseCalcul));

            if (tiersBaseCalcul != null) {
                bc.setIdTiersBaseCalcul(tiersBaseCalcul.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
            }
        } catch (Exception e) {
        }

//        bc.setDroitApplique(REACORAbstractFlatFileParser.getField(line, fields, "DROIT_APPLIQUE"));  $b37
        bc.setDroitApplique(IREDemandeRente.REVISION_10EME_REVISION);

        if (Objects.nonNull(baseCalcul.getBaseRam())) {
            if (Objects.nonNull(baseCalcul.getBaseRam().getBass())) {
                //        bc.setAnneeBonifTacheAssistance(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_BONIF_TACHE_ASSIST"));
                bc.setAnneeBonifTacheAssistance(Objects.toString(baseCalcul.getBaseRam().getBass().getAnDecimal(), StringUtils.EMPTY));
            }
            if (Objects.nonNull(baseCalcul.getBaseRam().getBte())) {
                //      bc.setAnneeBonifTacheEduc(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_BONIF_TACHE_EDUC"));
                bc.setAnneeBonifTacheEduc(PRConverterUtils.formatFloatToStringWithTwoDecimal(baseCalcul.getBaseRam().getBte().getAnDecimal()));
            }
            if (Objects.nonNull(baseCalcul.getBaseRam().getBtrans())) {
                //        bc.setAnneeBonifTransitoire(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_BONIF_TRANSITOIRE"));
                bc.setAnneeBonifTransitoire(Objects.toString(baseCalcul.getBaseRam().getBtrans().getAnDecimal(), StringUtils.EMPTY));
            } else {
                bc.setAnneeBonifTransitoire("0.0");
            }
            if (Objects.nonNull(baseCalcul.getBaseRam().getRevLucr())) {
                //        bc.setRevenuSplitte(PRStringUtils.getBooleanFromACOR_0_1(REACORAbstractFlatFileParser.getField(line, fields, "REVENU_SPLITTE"))); $b38
                bc.setRevenuSplitte(BooleanUtils.toBoolean(baseCalcul.getBaseRam().getRevLucr().getCodeSplit()));
                // On met la même valeur que le revenu splitté.
                bc.setIsPartageRevenuActuel(BooleanUtils.toBoolean(baseCalcul.getBaseRam().getRevLucr().getCodeSplit()));
                //        bc.setFacteurRevalorisation(REACORAbstractFlatFileParser.getField(line, fields, "FACTEUR_REVALORISATION")); $b50
                bc.setFacteurRevalorisation(Objects.toString(baseCalcul.getBaseRam().getRevLucr().getFacRev(), StringUtils.EMPTY));

                //        bc.setDureeRevenuAnnuelMoyen(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_COTI_RAM")); $b8
                bc.setDureeRevenuAnnuelMoyen(PRConverterUtils.formatAAMMtoAAxMM(baseCalcul.getBaseRam().getRevLucr().getDuree()));
            }
        }
        //        bc.setAnneeDeNiveau(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_NIVEAU")); $b10
        bc.setAnneeDeNiveau(PRConverterUtils.formatAAAAtoAA(Objects.toString(baseCalcul.getAnNiveau(), StringUtils.EMPTY)));
        //        bc.setAnneeTraitement(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_TRAITEMENT")); $b48
        bc.setAnneeTraitement(Objects.toString(baseCalcul.getAnRam(), StringUtils.EMPTY));

        bc.setCodeOfficeAi("000");
        if (Objects.nonNull(baseCalcul.getInvalidite()) && Objects.isNull(baseCalcul.getInvalidite().getType())) {
            //        bc.setCleInfirmiteAyantDroit(REACORAbstractFlatFileParser.getField(line, fields, "CLE_INFIRM_AYANT_DROIT")); $b22
            bc.setCleInfirmiteAyantDroit(Objects.toString(baseCalcul.getInvalidite().getGenreInvalidite(), StringUtils.EMPTY));
            //        bc.setCodeOfficeAi(REACORAbstractFlatFileParser.getField(line, fields, "OFFICE_AI_COMPETANT_AYANT_DROIT")); $b20
            bc.setCodeOfficeAi(Objects.toString(baseCalcul.getInvalidite().getOai(), StringUtils.EMPTY));
            //        bc.setDegreInvalidite(REACORAbstractFlatFileParser.getField(line, fields, "DEGRE_INVALIDITE_AYANT_DROIT")); $b21
            bc.setDegreInvalidite(Objects.toString(baseCalcul.getInvalidite().getDegreInvalidite(), StringUtils.EMPTY));
            //        bc.setInvaliditePrecoce(PRStringUtils.getBooleanFromACOR_0_1(REACORAbstractFlatFileParser.getField(line, fields, "INVALIDITE_PRECOCE_AYANT_DROIT"))); $b24
            bc.setInvaliditePrecoce(BooleanUtils.toBoolean(baseCalcul.getInvalidite().getInvalidePrecoce()));
            //        bc.setSurvenanceEvtAssAyantDroit(JadeStringUtil.removeChar(PRDateFormater.convertDate_MMAA_to_MMxAAAA(REACORAbstractFlatFileParser.getField(line, fields, "SURVENANCE_EVEN_ASSURE_AYANT_DROIT")), '.')); $b23
            bc.setSurvenanceEvtAssAyantDroit(JadeStringUtil.removeChar(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(baseCalcul.getInvalidite().getSurvenanceEvAss(), StringUtils.EMPTY)), '.'));
        }
        //        bc.setEchelleRente(REACORAbstractFlatFileParser.getField(line, fields, "ECHELLE_RENTE")); $b11
        bc.setEchelleRente(Objects.toString(baseCalcul.getEchelle(), StringUtils.EMPTY));
        //        bc.setRevenuAnnuelMoyen(REACORAbstractFlatFileParser.getField(line, fields, "RAM")); $b5
        bc.setRevenuAnnuelMoyen(Objects.toString(baseCalcul.getRam(), StringUtils.EMPTY));
        //        bc.setSupplementCarriere(REACORAbstractFlatFileParser.getField(line, fields, "POURCENT_SUPP_CARRIERE")); $b43
        bc.setSupplementCarriere(Objects.toString(baseCalcul.getSupCar(), StringUtils.EMPTY));
        // Récupération des durées de cotisation
        if (Objects.nonNull(baseCalcul.getBaseEchelle())) {
            //        bc.setAnneeCotiClasseAge(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_COTI_CLASSE_AGE")); $b9
            bc.setAnneeCotiClasseAge(Objects.toString(baseCalcul.getBaseEchelle().getAnCotClss(), StringUtils.EMPTY));

            bc.setAnneeDeNiveau(PRConverterUtils.formatAAAAtoAA(Objects.toString(baseCalcul.getBaseEchelle().getAnNiveau(), StringUtils.EMPTY)));
            for (BaseEchelle.DCot eachDCot : baseCalcul.getBaseEchelle().getDCot()) {
                switch (eachDCot.getType()) {
                    // Mariage/veuvage sans cotisations
                    case 2:
//        bc.setPeriodeMariage(REACORAbstractFlatFileParser.getField(line, fields, "PERIODE_MARIAGE")); $b34
                        StringBuilder periodeMariage = new StringBuilder(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getTotal().getAnnees()));
                        periodeMariage.append(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getTotal().getMois()));
                        bc.setPeriodeMariage(periodeMariage.toString());
                        break;
                    // mois d'appoint

                    //année de jeunesse
                    case 5:
//        bc.setRevenuJeunesse(REACORAbstractFlatFileParser.getField(line, fields, "REVENU_JEUNESSE")); $b33
                        fCalcul.getAnalysePeriodes().stream().filter(analysePeriodes -> nssTiersBaseCalcul.equals(analysePeriodes.getBeneficiaire()))
                                .findFirst()
                                .ifPresent(analysePeriodes -> {
                                    bc.setRevenuJeunesse(Objects.toString(analysePeriodes.getRevJTot(), StringUtils.EMPTY));
                                });
//        bc.setPeriodeJeunesse(REACORAbstractFlatFileParser.getField(line, fields, "PERIODE_JEUNESSE")); $b32
                        StringBuilder periodeJeunesse = new StringBuilder(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getTotal().getAnnees()));
                        periodeJeunesse.append(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getTotal().getMois()));
                        bc.setPeriodeJeunesse(periodeJeunesse.toString());
                        break;
                    case 6:
//        bc.setMoisAppointsAvant73(REACORAbstractFlatFileParser.getField(line, fields, "MOIS_APPOINT_AV_73")); $b13
                        int moisAppointsAv73 = eachDCot.getAv73().getAnnees() * MONTHS_IN_YEAR + eachDCot.getAv73().getMois();
                        bc.setMoisAppointsAvant73(Objects.toString(moisAppointsAv73, StringUtils.EMPTY));
//        bc.setMoisAppointsDes73(REACORAbstractFlatFileParser.getField(line, fields, "MOIS_APPOINT_DES_73")); $b14
                        int moisAppointsAp73 = eachDCot.getAp73().getAnnees() * MONTHS_IN_YEAR + eachDCot.getAp73().getMois();
                        bc.setMoisAppointsDes73(Objects.toString(moisAppointsAp73, StringUtils.EMPTY));
                        break;
                    // année d'ouverture
                    case 7:
//        bc.setMoisCotiAnneeOuvertDroit(REACORAbstractFlatFileParser.getField(line, fields, "MOIS_COTI_ANNEE_OUVERTURE")); $b36
                        int moisCotiAnneeOuvertDroit = eachDCot.getTotal().getAnnees() * MONTHS_IN_YEAR + eachDCot.getTotal().getMois();
                        bc.setMoisCotiAnneeOuvertDroit(Objects.toString(moisCotiAnneeOuvertDroit, StringUtils.EMPTY));
                        break;
                    // assurance étrangère
                    case 8:
//        bc.setPeriodeAssEtrangerAv73(REACORAbstractFlatFileParser.getField(line, fields, "PERIODE_ASS_ETR_AV_73")); $b35
                        StringBuilder periodeAssEtrAv73 = new StringBuilder(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getAv73().getAnnees()));
                        periodeAssEtrAv73.append(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getAv73().getMois()));
                        bc.setPeriodeAssEtrangerAv73(periodeAssEtrAv73.toString());
//        bc.setPeriodeAssEtrangerDes73(REACORAbstractFlatFileParser.getField(line, fields, "PERIODE_ASS_ETR_DES73")); $b49
                        StringBuilder periodeAssEtrAp73 = new StringBuilder(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getAp73().getAnnees()));
                        periodeAssEtrAp73.append(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getAp73().getMois()));
                        bc.setPeriodeAssEtrangerDes73(periodeAssEtrAp73.toString());
                        break;
                    // total
                    case 10:
//        bc.setDureeCotiAvant73(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_COTI_AV_73")); $b6
                        StringBuilder dureeCotiAv73 = new StringBuilder(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getAv73().getAnnees()));
                        dureeCotiAv73.append(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getAv73().getMois()));
                        bc.setDureeCotiAvant73(dureeCotiAv73.toString());
//        bc.setDureeCotiDes73(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_COTI_DES_73")); $b7
                        StringBuilder dureeCotiAp73 = new StringBuilder(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getAp73().getAnnees()));
                        dureeCotiAp73.append(PRConverterUtils.formatIntToStringWithTwoChar(eachDCot.getAp73().getMois()));
                        bc.setDureeCotiDes73(dureeCotiAp73.toString());
                        break;
                    default:
                        break;
                }
            }
        }

        bc.setReferenceDecision("0");

        return bc;
    }

    /**
     * @param session
     * @param transaction
     * @param demandeSource
     * @param bc
     * @param noCasATraiter
     * @param idCopieDemande ATTENTION, C'EST UN ARGUMENT DE SORTIE
     * @return
     * @throws Exception
     */
    private static REBasesCalcul doTraiterBaseCalcul(final BSession session, final BTransaction transaction,
                                                     final REDemandeRente demandeSource, REBasesCalcul bc, final int noCasATraiter,
                                                     IdDemandeRente idCopieDemande) throws Exception {

        switch (noCasATraiter) {

            case REImportationCalculAcor2020.CAS_NOUVEAU_CALCUL:
                RERenteCalculee rc = new RERenteCalculee();
                rc.setSession(session);
                rc.setIdRenteCalculee(demandeSource.getIdRenteCalculee());
                rc.retrieve(transaction);

                if (rc.isNew()) {
                    // Avant de le remplacer, on s'assure qu'il n'y a plus aucune
                    // base de calcul liée à cet ancien ID !!!
                    if (!JadeStringUtil.isBlankOrZero(demandeSource.getIdRenteCalculee())) {
                        REBasesCalculManager mgr = new REBasesCalculManager();
                        mgr.setSession(session);
                        mgr.setForIdRenteCalculee(demandeSource.getIdRenteCalculee());
                        mgr.find(transaction, 2);
                        if (!mgr.isEmpty()) {
                            throw new Exception(
                                    "Incohérance dans les données, des bases de calculs existe pour idRenteCalculee/idDemandeRente = "
                                            + demandeSource.getIdRenteCalculee() + "/"
                                            + demandeSource.getIdDemandeRente());
                        }
                    }

                    // Rente calculée
                    rc.setIdRenteCalculee("");
                    rc.setSession(session);
                    rc.add(transaction);
                    demandeSource.setIdRenteCalculee(rc.getIdRenteCalculee());
                } else {
                    demandeSource.setIdRenteCalculee(rc.getIdRenteCalculee());
                }

                // on passe la demande dans l'etat...
                demandeSource.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
                demandeSource.setDateTraitement(REACORParser.retrieveDateTraitement(demandeSource));
                demandeSource.update(transaction);

                bc.setIdRenteCalculee(rc.getIdRenteCalculee());
                bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                bc = REBasesCalcul.isBCExisteDeja(bc, session);
                if (bc.isNew()) {
                    bc.setReferenceDecision("0");
                    bc.add(transaction);
                } else {
                    bc.update(transaction);
                }

                break;

            case REImportationCalculAcor2020.CAS_RECALCUL_DEMANDE_NON_VALIDEE:
                // Rente calculee
                rc = new RERenteCalculee();
                rc.setSession(session);
                rc.setIdRenteCalculee(demandeSource.getIdRenteCalculee());
                rc.retrieve(transaction);
                if (rc.isNew()) {
                    throw new PRACORException("!!! RC not found. idRC/idDemande = " + demandeSource.getIdRenteCalculee()
                            + "/" + demandeSource.getIdDemandeRente());
                }

                // on passe la demande dans l'etat...
                demandeSource.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
                demandeSource.setDateTraitement(REACORParser.retrieveDateTraitement(demandeSource));
                demandeSource.update(transaction);

                bc.setIdRenteCalculee(rc.getIdRenteCalculee());
                bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                bc = REBasesCalcul.isBCExisteDeja(bc, session);

                if (bc.isNew()) {
                    bc.setReferenceDecision("0");
                    bc.add(transaction);
                } else {
                    bc.update(transaction);
                }
                break;

            case REImportationCalculAcor2020.CAS_RECALCUL_DEMANDE_VALIDEE:

                // On crée un clone de la demande source, comme demande 'enfant'
                REDemandeRente copieDemandeEnfant = REDemandeRegles.corrigerDemandeRente(session, transaction,
                        demandeSource);
                idCopieDemande.setId(Long.valueOf(copieDemandeEnfant.getIdDemandeRente()));
                // Rente calculee
                rc = new RERenteCalculee();
                rc.setSession(session);
                rc.add(transaction);

                copieDemandeEnfant.setIdRenteCalculee(rc.getIdRenteCalculee());
                copieDemandeEnfant.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
                copieDemandeEnfant.setDateTraitement(REACORParser.retrieveDateTraitement(copieDemandeEnfant));
                copieDemandeEnfant.update(transaction);

                bc.setIdRenteCalculee(copieDemandeEnfant.getIdRenteCalculee());
                bc.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);
                bc.setReferenceDecision("0");
                bc.add(transaction);

                // On recharge la demandeSource comme étant celle de l'enfant.
                // Nécessaire, car la suite du traitement (ajout des RA à la
                // demande) se fait sur la demande source.
                // On veut donc les rajouter à la nouvelle demande crée.
                demandeSource.setIdDemandeRente(copieDemandeEnfant.getIdDemandeRente());
                demandeSource.retrieve(transaction);

                break;
            default:
                throw new PRACORException("Il n'est pas permis de recalculer ce cas.");
        }

        return bc;
    }

    /**
     * Importe les données de ACOR dans une rente accordée.
     *
     * @return
     */
    private static RERenteAccordee importRenteAccordee(final BSession session, final BTransaction transaction,
                                                       final REDemandeRente demande, FCalcul.Evenement.BasesCalcul.Decision.Prestation prestation, FCalcul.Evenement.BasesCalcul baseCalcul, FCalcul fCalcul) throws Exception {

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        // on récupère la rente depuis la prestation.
        Rente rente = prestation.getRente();
        // on récupère le dernier état de la rente.
        Rente.Etat dernierEtat = rente.getEtat().get(rente.getEtat().size() - 1);


        // code cas speciaux maximum 5 de 2 position cadre a droite et complete
        // par des blancs
//        String casSpeciaux = REACORAbstractFlatFileParser.getField(line, fields, "CODE_CAS_SPECIAUX"); $r14
        for (int i = 0; i < rente.getCodeCasSpecial().size(); i++) {
            String codeCasSpecial = PRConverterUtils.formatIntToStringWithTwoChar(rente.getCodeCasSpecial().get(i));
            switch (i) {
                case 0:
                    ra.setCodeCasSpeciaux1(codeCasSpecial);
                    break;
                case 1:
                    ra.setCodeCasSpeciaux2(codeCasSpecial);
                    break;
                case 2:
                    ra.setCodeCasSpeciaux3(codeCasSpecial);
                    break;
                case 3:
                    ra.setCodeCasSpeciaux4(codeCasSpecial);
                    break;
                case 4:
                    ra.setCodeCasSpeciaux5(codeCasSpecial);
                    break;
                default:
                    break;
            }
        }

//        String codeMutation = REACORAbstractFlatFileParser.getField(line, fields, "CODE_MUTATION"); $r19
        String codeMutation = PRConverterUtils.formatIntToStringWithTwoChar(rente.getCodeMutation());

        if (!JadeStringUtil.isBlankOrZero(codeMutation)) {
            ra.setCodeMutation(codeMutation);
        }

//        String fractionRente = REACORAbstractFlatFileParser.getField(line, fields, "FRACTION_RENTE_AI"); $r7
//         ra.setFractionRente(PRACORConst.caFractionRenteToCS(session, fractionRente));
        ra.setFractionRente(FractionRente.getConstFromValue(rente.getFraction()));

//        ra.setCodePrestation(REACORAbstractFlatFileParser.getField(line, fields, "CODE_PRESTATION")); $r5
        ra.setCodePrestation(Objects.toString(rente.getGenre(), StringUtils.EMPTY));

//        ra.setCodeSurvivantInvalide(REACORAbstractFlatFileParser.getField(line, fields, "CODE_SURVIVANT")); $r26
        ra.setCodeSurvivantInvalide(Objects.toString(rente.getSurvInv(), StringUtils.EMPTY));

//        ra.setCsEtatCivil(PRACORConst.caEtatCivilToCS(session, REACORAbstractFlatFileParser.getField(line, fields, "CODE_ETAT_CIVIL"))); $r8
        ra.setCsEtatCivil(PRACORConst.caEtatCivilToCS(session, Objects.toString(prestation.getEtatCivil(), StringUtils.EMPTY)));

        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session, ISFSituationFamiliale.CS_DOMAINE_RENTES, demande.loadDemandePrestation(transaction).getIdTiers());

        ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(demande.loadDemandePrestation(transaction).getIdTiers());

//        String nssTiersBaseCalcul = REACORAbstractFlatFileParser.getField(line, fields, "NSS_BASE_CALCUL"); $b2
        PRTiersWrapper tiersBaseCalcul = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(baseCalcul.getGenerateur()));
        if (tiersBaseCalcul != null) {
            ra.setIdTiersBaseCalcul(tiersBaseCalcul.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

//        String nssBeneficiaire = REACORAbstractFlatFileParser.getField(line, fields, "NSS_BENEFICIAIRE"); $r4
        String nssBeneficiaire = Objects.toString(prestation.getBeneficiaire(), StringUtils.EMPTY);

        for (int i = 0; i < mf.length; i++) {
            ISFMembreFamilleRequerant membre = mf[i];
            String nssNF = membre.getNss();
            // On supprime les '.'
            nssNF = JadeStringUtil.change(nssNF, ".", "");

            if ((nssBeneficiaire != null) && nssBeneficiaire.equals(nssNF)) {
                ra.setCsRelationAuRequerant(membre.getRelationAuRequerant());
            }
        }

        // TODO : anticipation : aller chercher dans le dernier état de la rente
        if (Objects.nonNull(baseCalcul.getAnticipation())) {
            ra = setAnticipationToRA(baseCalcul, ra, rente);
        }

//        ra.setDateDebutDroit(PRDateFormater.convertDate_MMAA_to_MMxAAAA(REACORAbstractFlatFileParser.getField(line, fields, "DEBUT_DROIT"))); $r17
        ra.setDateDebutDroit(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(rente.getDebutDroit(), StringUtils.EMPTY)));
//        ra.setDateFinDroit(PRDateFormater.convertDate_MMAA_to_MMxAAAA(REACORAbstractFlatFileParser.getField(line, fields, "FIN_DROIT"))); $r18
        ra.setDateFinDroit(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(rente.getFinDroit(), StringUtils.EMPTY)));

//        String d = REACORAbstractFlatFileParser.getField(line, fields, "FIN_DROIT_ECHEANCE"); $r28
        String d = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(rente.getFinPrevue(), StringUtils.EMPTY));

        // On rajoute 1 mois à la date d'echeance par rapport à celle
        // remontée de ACOR
        if (!JadeStringUtil.isBlankOrZero(d)) {
            ra.setDateFinDroitPrevueEcheance(d);
            ra.setDateEcheance(d);
        }

        if (Objects.nonNull(baseCalcul.getAjournement())) {
            ra = setAjournementToRA(baseCalcul, ra, rente);
        }

//        PRTiersWrapper wrapper = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser.getField(line, fields, "NSS_BENEFICIAIRE"))); $r4
        PRTiersWrapper wrapper = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(Objects.toString(prestation.getBeneficiaire(), StringUtils.EMPTY)));
        if (wrapper == null) {
//            throw new Exception("Aucun tiers trouvé pour le NSS : " + NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser.getField(line, fields, "NSS_BENEFICIAIRE"))); $r4
            throw new Exception("Aucun tiers trouvé pour le NSS : " + NSUtil.formatAVSUnknown(Objects.toString(prestation.getBeneficiaire(), StringUtils.EMPTY)));
        }

        ra.setIdTiersBeneficiaire(wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));

//        wrapper = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser.getField(line, fields, "PREMIER_NSS_COMPLEMENTAIRE"))); $r10
        wrapper = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(Objects.toString(rente.getNcpl1(), StringUtils.EMPTY)));
        if (wrapper != null) {
            ra.setIdTiersComplementaire1(wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

//        wrapper = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(REACORAbstractFlatFileParser.getField(line, fields, "SECOND_NSS_COMPLEMENTAIRE"))); $r11
        wrapper = PRTiersHelper.getTiers(session, NSUtil.formatAVSUnknown(Objects.toString(rente.getNcpl2(), StringUtils.EMPTY)));
        if (wrapper != null) {
            ra.setIdTiersComplementaire2(wrapper.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
        }

//        ra.setMontantPrestation(REACORAbstractFlatFileParser.getField(line, fields, "MONTANT_PRESTATION")); $r12
        if (rente.getCodeCasSpecial().stream().allMatch(value -> value != CODE_SPECIAL_AJOURNEMENT)) {
            ra.setMontantPrestation(Objects.toString(dernierEtat.getMontant(), StringUtils.EMPTY));
        }

//        ra.setReductionFauteGrave(REACORAbstractFlatFileParser.getField(line, fields, "REDUCTION_FAUTE_GRAVE")); $r15
        ra.setReductionFauteGrave(Objects.toString(rente.getFauteGrave(), StringUtils.EMPTY));

        ra.setIsTraitementManuel(Boolean.FALSE);

        ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
        ra.setAnneeMontantRAM(REACORParser.computeAnneeMontantRAM(ra, session));

        // ra.setSupplementVeuvage(REACORAbstractFlatFileParser.getField(line, fields, "SUPPL_VEUVAGE")); $r29
        if (rente.isSupplementVeuvage()) {
            ra.setSupplementVeuvage("1");
        } else {
            ra.setSupplementVeuvage("0");
        }

//         ra.setPrescriptionAppliquee(REACORAbstractFlatFileParser.getField(line, fields, "PRESCRIPTION_APPLIQUEE")); $r30
        ra.setPrescriptionAppliquee(getNombreAnneePrescriptionAppliquee(rente));

        List<Integer> refugies = fCalcul.getAssure().stream().filter(assure -> StringUtils.equals(assure.getId().getValue(), prestation.getBeneficiaire())).map(assure -> assure.getRefugie()).collect(Collectors.toList());
        if (refugies.stream().anyMatch(value -> Objects.nonNull(value))) {
            // ra.setCodeRefugie(REACORAbstractFlatFileParser.getField(line, fields, "CODE_REFUGIE")); $r9 ou $r19 ??
            ra.setCodeRefugie("1");
        } else {
            ra.setCodeRefugie("0");
        }

        return ra;
    }

    private static String getNombreAnneePrescriptionAppliquee(Rente rente) {
        String nbAnnee = StringUtils.EMPTY;
        if (Objects.nonNull(rente.getArt25LPGA())) {
            nbAnnee = "1";
        } else if (Objects.nonNull(rente.getArt24LPGA())) {
            nbAnnee = "5";
        } else if (Objects.nonNull(rente.getArt97CP())) {
            nbAnnee = "7";
        }
        return nbAnnee;
    }

    /**
     * Ajout des données d'ajournemenet dans la rente accordée.
     *
     * @param baseCalcul : la base de calcul
     * @param ra         : la rente accordée
     * @param rente      : la rente
     * @return la rente accordée mis à jour
     */
    private static RERenteAccordee setAjournementToRA(FCalcul.Evenement.BasesCalcul baseCalcul, RERenteAccordee ra, Rente rente) {
        for (FCalcul.Evenement.BasesCalcul.Ajournement.Tranche eachTranche : baseCalcul.getAjournement().getTranche()) {
            for (FCalcul.Evenement.BasesCalcul.Ajournement.Tranche.Rente eachRente : eachTranche.getRente()) {
                if (StringUtils.equals(Objects.toString(eachRente.getGenre()), Objects.toString(rente.getGenre()))) {
                    // TODO : formatter date
//                        ra.setDateRevocationAjournement(PRDateFormater.convertDate_MMAA_to_MMxAAAA(REACORAbstractFlatFileParser.getField(line, fields, "DATE_REVOCATION_AJOURNEMENT"))); $r22
                    ra.setDateRevocationAjournement(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(eachTranche.getDateRevocation(), StringUtils.EMPTY)));
                    //        ra.setDureeAjournement(REACORAbstractFlatFileParser.getField(line, fields, "DUREE_AJOURNEMENT")); $r20
                    ra.setDureeAjournement(PRConverterUtils.formatMMtoAxMM(eachTranche.getDureeAjournement()));
                    //        ra.setSupplementAjournement(REACORAbstractFlatFileParser.getField(line, fields, "SUPPLEMENT_AJOURNEMENT")); $r21
                    ra.setSupplementAjournement(Objects.toString(eachTranche.getMontantSupplement(), StringUtils.EMPTY));
                    return ra;
                }
            }
        }
        return ra;
    }

    /**
     * Ajout des données d'anticipation dans la rente accordée.
     *
     * @param baseCalcul : la base de calcul
     * @param ra         : la rente accordée
     * @param rente      : la rente
     * @return la rente accordée mis à jour.
     */
    private static RERenteAccordee setAnticipationToRA(FCalcul.Evenement.BasesCalcul baseCalcul, RERenteAccordee ra, Rente rente) {
        for (FCalcul.Evenement.BasesCalcul.Anticipation.Tranche eachTranche : baseCalcul.getAnticipation().getTranche()) {
            for (FCalcul.Evenement.BasesCalcul.Anticipation.Tranche.Rente eachRente : eachTranche.getRente()) {
                if (StringUtils.equals(Objects.toString(eachRente.getGenre()), Objects.toString(rente.getGenre()))) {
                    //                        ra.setAnneeAnticipation(REACORAbstractFlatFileParser.getField(line, fields, "ANNEE_ANTICIPATION")); $r23
                    ra.setAnneeAnticipation(PRConverterUtils.convertMMtoA(eachTranche.getDureeAnticipation()));
//                        ra.setDateDebutAnticipation(PRDateFormater.convertDate_AAAAMM_to_MMAAAA(PRDateFormater.convertDate_MMAA_to_AAAAMM(REACORAbstractFlatFileParser.getField(line, fields, "DATE_DEBUT_ANTICIPATION"))));
                    ra.setDateDebutAnticipation(PRDateFormater.convertDate_AAAAMM_to_MMAAAA(PRDateFormater.convertDate_AAAAMMJJ_to_AAAAMM(Objects.toString(eachTranche.getDateAnticipation(), StringUtils.EMPTY))));
//                        ra.setMontantReducationAnticipation(REACORAbstractFlatFileParser.getField(line, fields, "MONTANT_REDUCT_ANTICIPATION"));
                    if (Objects.nonNull(eachTranche.getMontantReduction())) {
                        ra.setMontantReducationAnticipation(Objects.toString(Math.round(eachTranche.getMontantReduction()), StringUtils.EMPTY));
                    } else {
                        ra.setMontantReducationAnticipation(Objects.toString(Math.round(eachRente.getMontantReduction()), StringUtils.EMPTY));
                    }
                    return ra;
                }
            }
        }
        return ra;
    }

    /**
     * Importe les données de ACOR dans une rente accordée.
     *
     * @return
     */
    private static REPrestationDue importPrestationsDues(final BSession session,
                                                         final Rente.Etat etat,
                                                         final boolean isNonAjournement) {
        REPrestationDue pd = new REPrestationDue();
        pd.setSession(session);
        pd.setCsType(IREPrestationDue.CS_TYPE_PMT_MENS);
        pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
//                pd.setMontantReductionAnticipation(REACORAbstractFlatFileParser.getField(line, fields, "MONTANT_REDUC_ANTICI")); $p8
        pd.setMontantReductionAnticipation(Objects.toString(etat.getRedAnt(), StringUtils.EMPTY));
//                    pd.setMontantSupplementAjournement(REACORAbstractFlatFileParser.getField(line, fields, "MONTANT_SUPPL_AJOURN")); $p9
        pd.setMontantSupplementAjournement(Objects.toString(etat.getSupAj(), StringUtils.EMPTY));
        //        pd.setDateDebutPaiement(PRDateFormater.convertDate_AAAAMM_to_MMxAAAA(REACORAbstractFlatFileParser.getField(line, fields, "DEBUT_PAIEMENT"))); $p5
        pd.setDateDebutPaiement(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(etat.getDebut(), StringUtils.EMPTY)));
        pd.setDateFinPaiement(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(Objects.toString(etat.getFin(), StringUtils.EMPTY)));
        //        pd.setMontant(REACORAbstractFlatFileParser.getField(line, fields, "MONTANT")); $p6
        if (isNonAjournement) {
            pd.setMontant(Objects.toString(etat.getMontant(), StringUtils.EMPTY));
        }
        //        pd.setRam(REACORAbstractFlatFileParser.getField(line, fields, "RAM")); $p7
        pd.setRam(Objects.toString(etat.getRam(), StringUtils.EMPTY));
        pd.setCsTypePaiement(null);
        return pd;
    }

    private static class IdDemandeRente {
        private Long id;

        /**
         * @return the id
         */
        public final Long getId() {
            return id;
        }

        /**
         * @param id the id to set
         */
        public final void setId(Long id) {
            if (this.id == null) {
                this.id = id;
            }
        }

    }


    /******************************************* Partie MAJ FCalcul historique **************************************************/

    /**
     * Maj des données supplémetaires depuis le fichier f_calcul pour ACOR 2020
     *
     * @param session
     * @param transaction
     * @param fCalcul
     * @param rentesAccordees
     * @throws Exception
     */
    public static Set<String> doMAJExtraData(final BSession session, final BTransaction transaction, final FCalcul fCalcul,
                                             final List<Long> rentesAccordees) throws Exception {

        Set<String> rentesWithoutBte = new HashSet<>();
        /* Pour chacune des rentes accordées et $p, maj du taux de réduction pour anticipation et maj des années bte entière, demi et quart dans les bases de calculs.
         Ces données sont remontées de la feuille de calcul. */
        if (fCalcul != null) {

            // On récupère la liste des Rentes Accordées.
            RERenteAccordeeManager raManager = new RERenteAccordeeManager();
            raManager.setSession(session);
            raManager.setForIdsRentesAccordees(StringUtils.join(rentesAccordees, ","));
            raManager.find(transaction, BManager.SIZE_NOLIMIT);

            for (FCalcul.Evenement eachEvenement : fCalcul.getEvenement()) {
                for (FCalcul.Evenement.BasesCalcul eachBaseCalcul : eachEvenement.getBasesCalcul()) {

                    // Pour chaque Base de Calcul, on récupère les années BTE et le taux de réduction d'anticipation s'ils existent.
                    String an1 = null;
                    String an2 = null;
                    String an4 = null;
                    FCalcul.Evenement.BasesCalcul.BaseRam.Bte bte = null;
                    if (Objects.nonNull(eachBaseCalcul.getBaseRam())) {
                        bte = eachBaseCalcul.getBaseRam().getBte();
                    }
                    if (Objects.nonNull(bte)) {
                        an1 = Objects.toString(bte.getAn1(), StringUtils.EMPTY);
                        an2 = Objects.toString(bte.getAn2(), StringUtils.EMPTY);
                        an4 = Objects.toString(bte.getAn4(), StringUtils.EMPTY);
                    }
                    String tauxReductionAnticipation = null;
                    if (Objects.nonNull(eachBaseCalcul.getAnticipation()) && !eachBaseCalcul.getAnticipation().getTranche().isEmpty())
                        tauxReductionAnticipation = Objects.toString(eachBaseCalcul.getAnticipation().getTranche().get(0).getTauxReductionAnticipation(), StringUtils.EMPTY);

                    for (FCalcul.Evenement.BasesCalcul.Decision eachDecision : eachBaseCalcul.getDecision()) {
                        for (FCalcul.Evenement.BasesCalcul.Decision.Prestation eachPrestation : eachDecision.getPrestation()) {
                            // Il faut retrouver la base de calcul + rente
                            // accordee et $p
                            // pour leur affecter les années bte et taux
                            // réduction
                            if (Objects.nonNull(eachPrestation.getRente())) {
                                String nss = eachPrestation.getBeneficiaire();
                                String ddAAAAMMJJ = Objects.toString(eachPrestation.getRente().getDebutDroit(), StringUtils.EMPTY);

                                boolean baseCalculSimFound = false;
                                for (RERenteAccordee eachRA : raManager.getContainerAsList()) {
                                    PRAssert.notIsNew(eachRA, null);

                                    PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, eachRA.getIdTiersBeneficiaire());
                                    String nss2 = tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                                    nss = NSUtil.unFormatAVS(nss);
                                    nss2 = NSUtil.unFormatAVS(nss2);

                                    String ddAAAAMMJJ2 = PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMMJJ(eachRA.getDateDebutDroit());

                                    String codePrst1 = eachRA.getCodePrestation();
                                    String codePrst2 = Objects.toString(eachPrestation.getRente().getGenre(), StringUtils.EMPTY);

                                    if (nss.equals(nss2) && ddAAAAMMJJ.equals(ddAAAAMMJJ2) && codePrst1.equals(codePrst2)) {

                                        if (Objects.nonNull(tauxReductionAnticipation)) {
                                            eachRA.setTauxReductionAnticipation(tauxReductionAnticipation);
                                            eachRA.update(transaction);
                                        }

                                        if (Objects.isNull(eachBaseCalcul.getAnalysePeriodes()) && isAnDecimalNonNull(bte) && !baseCalculSimFound) {
                                            REBasesCalcul baseCalculSim = getBaseCalculSim(session, transaction, eachRA);
                                            // Si les nb BTE de la base de calcul similaire sont non nuls, on set leur valeur.
                                            if (!JadeStringUtil.isBlankOrZero(baseCalculSim.getNombreAnneeBTE1()) || !JadeStringUtil.isBlankOrZero(baseCalculSim.getNombreAnneeBTE2()) || !JadeStringUtil.isBlankOrZero(baseCalculSim.getNombreAnneeBTE4())) {
                                                an1 = baseCalculSim.getNombreAnneeBTE1();
                                                an2 = baseCalculSim.getNombreAnneeBTE2();
                                                an4 = baseCalculSim.getNombreAnneeBTE4();
                                                rentesWithoutBte.remove(eachRA.getId());
                                                baseCalculSimFound = true;
                                            }
                                            // Sinon, on set leur valeur à vide et on ajoute l'id de la rente calculée à une liste pour prévenir l'utilisateur.
                                            else {
                                                an1 = StringUtils.EMPTY;
                                                an2 = StringUtils.EMPTY;
                                                an4 = StringUtils.EMPTY;
                                                rentesWithoutBte.add(eachRA.getId());
                                            }
                                        }

                                        if (!JadeStringUtil.isBlankOrZero(an1) || !JadeStringUtil.isBlankOrZero(an2) || !JadeStringUtil.isBlankOrZero(an4)) {
                                            // On récupère la base de calcul liée à cette rente de calcul depuis la base de données.
                                            REBasesCalcul basesCalc = new REBasesCalcul();
                                            basesCalc.setSession(session);
                                            basesCalc.setIdBasesCalcul(eachRA.getIdBaseCalcul());
                                            basesCalc.retrieve(transaction);
                                            PRAssert.notIsNew(basesCalc, null);

                                            basesCalc.setNombreAnneeBTE1(an1);
                                            basesCalc.setNombreAnneeBTE2(an2);
                                            basesCalc.setNombreAnneeBTE4(an4);
                                            basesCalc.update(transaction);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        return rentesWithoutBte;
    }

    private static boolean isAnDecimalNonNull(FCalcul.Evenement.BasesCalcul.BaseRam.Bte bte) {
        if (Objects.nonNull(bte) && Objects.nonNull(bte.getAnDecimal())) {
            return bte.getAnDecimal() != 0.0;
        }
        return false;
    }

    /**
     * Permet de récupérer la base de calcul similaire à celle de la rente calculée : même code prestation, même tiers bénéficiaire et état "en cours".
     * On contrôle également sur les dates de droit pour récupérer la dernière rente.
     *
     * @param session
     * @param transaction
     * @param eachRA      : la rente calculée
     * @return la base de calcul similaire à celle de la rente calculée.
     * @throws Exception
     */
    private static REBasesCalcul getBaseCalculSim(final BSession session, final BTransaction transaction, RERenteAccordee eachRA) throws Exception {
        RERenteAccordeeManager raManager = new RERenteAccordeeManager();
        raManager.setSession(session);
        raManager.setForCodesPrestationsIn(eachRA.getCodePrestation());
        raManager.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL + ", " + IREPrestationAccordee.CS_ETAT_DIMINUE);
        raManager.setForIdTiersBeneficiaire(eachRA.getIdTiersBeneficiaire());
        raManager.find(transaction, BManager.SIZE_NOLIMIT);

        String idBaseCalculSim = StringUtils.EMPTY;
        String dateFinDroitRente = StringUtils.EMPTY;
        for (RERenteAccordee eachRente : raManager.getContainerAsList()) {
            // On contrôle si la date de début de rente est bien antérieure à celle que l'on calcule.
            if (eachRente.getDateDebutDroit().compareTo(eachRA.getDateDebutDroit()) < 0) {
                // Si la date de fin de rente est vide, il s'agit de la dernière rente similaire.
                if (eachRente.getDateFinDroit().isEmpty()) {
                    idBaseCalculSim = eachRente.getIdBaseCalcul();
                    break;
                } else {
                    // Sinon, on récupère la rente dont la date de fin est la plus récente.
                    if (dateFinDroitRente.compareTo(eachRente.getDateFinDroit()) < 0) {
                        dateFinDroitRente = eachRente.getDateFinDroit();
                        idBaseCalculSim = eachRente.getIdBaseCalcul();
                    }
                }
            }
        }
        REBasesCalcul baseCalculSim = new REBasesCalcul();
        baseCalculSim.setIdBasesCalcul(idBaseCalculSim);
        baseCalculSim.setSession(session);
        baseCalculSim.retrieve(transaction);

        return baseCalculSim;
    }

    public static final List<REFeuilleCalculVO> parseCIAdd(final BSession session, final BITransaction transaction,
                                                           final REDemandeRente demandeSource, final FCalcul fCalcul) throws PRACORException {

        List<REFeuilleCalculVO> retValue = new LinkedList<>();

        REFeuilleCalculVO fcParBaseCalculVO = null;
        REFeuilleCalculVO.ElementVO elmFCVO = null;

        try {
            // changer de parser si NNSS ou NAVS
            PRDemande demande = new PRDemande();
            demande.setSession(session);
            demande.setIdDemande(demandeSource.getIdDemandePrestation());
            demande.retrieve();

            if (demande.isNew()) {
                throw new PRACORException("Demande prestation non trouvée !!");
            } else {

                PRTiersWrapper tiers = PRTiersHelper.getTiersParId(session, demande.getIdTiers());

            }
            REBasesCalcul bc;
            RERenteAccordee ra;

            for (FCalcul.Evenement eachEvenement : fCalcul.getEvenement()) {
                FCalcul.Evenement.BasesCalcul.Decision.Prestation premierePrestation = null;
                for (FCalcul.Evenement.BasesCalcul eachBaseCalcul : eachEvenement.getBasesCalcul()) {

                    // On récupère la première prestation "valide" de la base de calcul pour récupérer le type du droit et le bénéficiaire.
                    for (FCalcul.Evenement.BasesCalcul.Decision eachDecision : eachBaseCalcul.getDecision()) {
                        for (FCalcul.Evenement.BasesCalcul.Decision.Prestation eachPrestation : eachDecision.getPrestation()) {
                            if (Objects.nonNull(eachPrestation.getRente())) {
                                premierePrestation = eachPrestation;
                                break;
                            }
                        }
                    }
                    if (Objects.nonNull(premierePrestation)) {
                        bc = importBaseCalcul(session, eachBaseCalcul, premierePrestation, fCalcul);

                        if (fcParBaseCalculVO != null) {
                            retValue.add(fcParBaseCalculVO);
                        }
                        fcParBaseCalculVO = new REFeuilleCalculVO();

                        for (FCalcul.Evenement.BasesCalcul.Decision eachDecision : eachBaseCalcul.getDecision()) {
                            for (FCalcul.Evenement.BasesCalcul.Decision.Prestation eachPrestation : eachDecision.getPrestation()) {
                                if (Objects.nonNull(eachPrestation.getRente())) {
                                    ra = REAcor2020Parser.importRenteAccordee(session, (BTransaction) transaction, demandeSource, eachPrestation, eachBaseCalcul, fCalcul);

                                    elmFCVO = fcParBaseCalculVO.new ElementVO();

                                    elmFCVO.setRAM(bc.getRevenuAnnuelMoyen());
                                    elmFCVO.setIdTiers(ra.getIdTiersBeneficiaire());
                                    elmFCVO.setMontantRente(ra.getMontantPrestation());
                                    elmFCVO.setGenreRente(ra.getCodePrestation());
                                    fcParBaseCalculVO.addElementVO(elmFCVO);
                                }
                            }
                        }


                    }
                }
            }
        } catch (Exception e) {
            throw new PRACORException("impossible de parser : " + e.getMessage(), e);
        }

        return retValue;
    }
}

