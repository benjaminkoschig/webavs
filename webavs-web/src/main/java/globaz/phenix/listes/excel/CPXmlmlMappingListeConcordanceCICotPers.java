package globaz.phenix.listes.excel;

import globaz.caisse.report.helper.ACaisseReportHelper;
import globaz.campus.db.annonces.GEAnnonces;
import globaz.campus.db.annonces.GEAnnoncesManager;
import globaz.commons.nss.NSUtil;
import globaz.framework.printing.itext.fill.FWIImportProperties;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;
import globaz.helios.tools.TimeHelper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliationCalcul;
import globaz.phenix.db.principale.CPDecisionAffiliationCalculManager;
import globaz.phenix.db.principale.CPDecisionForCompareCI;
import globaz.phenix.db.principale.CPDecisionForCompareCIManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.db.principale.CPDonneesCalcul;
import globaz.phenix.db.principale.CPEcritureMemeTiersManager;
import globaz.phenix.db.principale.CPSortie;
import globaz.phenix.db.principale.CPSortieManager;
import globaz.phenix.process.CPListeExcelConcordanceCotPersCIProcess;
import globaz.pyxis.db.tiers.TIHistoNumAvsListViewBean;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.webavs.common.CommonExcelmlContainer;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @revision SCO 15 déc. 2010
 */
public class CPXmlmlMappingListeConcordanceCICotPers {

    private static CPDecisionForCompareCI chargerInfo(BigDecimal montantCp, BigDecimal montantCINoAffilie,
            BigDecimal montantCINoAVSNoAffilie, CPListeExcelConcordanceCotPersCIProcess process,
            CPDecisionForCompareCI entity, CPDecisionForCompareCI entityPrecedente,
            CPDecisionForCompareCI entitySuivante, CommonExcelmlContainer container, int i) throws Exception {

        BigDecimal difference1 = montantCp.subtract(montantCINoAffilie);
        BigDecimal difference2 = montantCp.subtract(montantCINoAVSNoAffilie);
        BigDecimal limInf = new BigDecimal(process.getFromDiffAdmise());
        BigDecimal limSup = new BigDecimal(process.getToDiffAdmise());

        if (((difference1.intValue() < limInf.intValue()) || (difference1.intValue() > limSup.intValue()))
                || ((difference2.intValue() < limInf.intValue()) || (difference2.intValue() > limSup.intValue()))) {
            entity.setMontantCINoAffilie(montantCINoAffilie);
            entity.setMontantCINoAVSNoAffilie(montantCINoAVSNoAffilie);
            entity.setMontantCP(montantCp);
            entity.setDifference1(difference1);
            entity.setDifference2(difference2);
            CPXmlmlMappingListeConcordanceCICotPers.loadDetail(i, container, entity, entityPrecedente, entitySuivante,
                    process);
        }
        return entity;
    }

    private static String getCategorie(CPDecisionForCompareCI entity, CPDecisionForCompareCI entityPrecedente,
            CPDecisionForCompareCI entitySuivante, CPListeExcelConcordanceCotPersCIProcess process) throws Exception {
        // Catégorie 1 : La sortie n'est pas au CI
        // Catégorie 2 : L'écriture a été passé sur un mauvais numéro AVS
        // Catégorie 3 : Aucune écriture pour l'année
        // Catégorie 4 : L'écriture a été passé sur le mauvais num Affilie (même tiers)
        // Catégorie 5a : Autre avec écriture Clôturées ou montant CP < 0
        // Catégorie 5b : Autre sans écriture clôturées.
        // Catégorie 6 : L'affilié n'a pas de numéro AVS pour l'année

        if (entityPrecedente != null && entityPrecedente.getNumAffilie().equals(entity.getNumAffilie())) {
            return "2";
        }
        if (entitySuivante != null && entitySuivante.getNumAffilie().equals(entity.getNumAffilie())) {
            return "2";
        }

        if (JadeStringUtil.isBlank(entity.getNumAVS())) {
            TIHistoNumAvsListViewBean numAvsManager = new TIHistoNumAvsListViewBean();
            numAvsManager.setSession(process.getSession());
            numAvsManager.setForIdTiers(entity.getIdTiers());
            numAvsManager.setForDateDebutGreaterOrEqualsTo(entity.getAnnee() + "0101");
            numAvsManager.find();

            if (numAvsManager.size() == 0) {
                return "6";
            } else if (numAvsManager.size() > 1) {
                return "6";
            } else {
                entity.setNumAVS(((TIHistoriqueAvs) numAvsManager.getFirstEntity()).getNumAvs());
            }
            return "3";
        }

        if ((entity.getMontantCP().intValue() == 0) && (entity.getMontantCINoAffilie().intValue() != 0)
                && (entity.getMontantCINoAVSNoAffilie().intValue() != 0)) {
            return "1";
        }

        CPEcritureMemeTiersManager ecritureMana = new CPEcritureMemeTiersManager();
        if ((entity.getMontantCP().intValue() != 0) && (entity.getMontantCINoAVSNoAffilie().intValue() == 0)) {
            ecritureMana.setSession(process.getSession());
            ecritureMana.setForAnnee(entity.getAnnee());
            ecritureMana.setForIdTiers(entity.getIdTiers());
            ecritureMana.setForNotNumAffilie(entity.getNumAffilie());
            ecritureMana.find();

            if (ecritureMana.size() > 0) {
                return "4";
            }
        }

        if (entity.getMontantCP().intValue() < 0) {
            return "5a";
        }

        if (ecritureMana.hasEcritureCloturer(entity.getIdCI(), entity.getAnnee(), process.getTransaction())) {
            return "5a";
        } else {
            return "5b";
        }
    }

    /**
     * Méthode qui indique si la décision est après l'affiliation
     * 
     * @param decision
     * @return vrai si la décision est après l'affiliation
     */
    public static boolean isDecisionApresAffiliation(CPDecisionAffiliationCalcul decision,
            CPListeExcelConcordanceCotPersCIProcess process) throws Exception {
        AFAffiliation affilie = new AFAffiliation();
        affilie.setSession(process.getSession());
        affilie.setAffiliationId(decision.getIdAffiliation());
        affilie.retrieve();
        if (affilie.isNew()) {
            return true;
        }
        if (JadeStringUtil.isBlankOrZero(affilie.getDateFin())) {
            return false;
        }
        if (BSessionUtil.compareDateFirstGreaterOrEqual(process.getSession(), decision.getDebutDecision(),
                affilie.getDateFin())) {
            return true;
        }
        return false;

    }

    private static void loadDetail(int i, CommonExcelmlContainer container, CPDecisionForCompareCI entity,
            CPDecisionForCompareCI entityPrecedente, CPDecisionForCompareCI entitySuivante,
            CPListeExcelConcordanceCotPersCIProcess process) throws Exception {

        String categorie = CPXmlmlMappingListeConcordanceCICotPers.getCategorie(entity, entityPrecedente,
                entitySuivante, process);
        process.incProgressCounter();
        container.put(ICPListeColumns.ID_CI, entity.getIdCI());
        if (entityPrecedente != null) {
            if (entity.getNumAffilie().equals(entityPrecedente.getNumAffilie())) {
                container.put(ICPListeColumns.NUM_AFFILIE, "");
            } else {
                container.put(ICPListeColumns.NUM_AFFILIE, entity.getNumAffilie());
            }
        } else {
            container.put(ICPListeColumns.NUM_AFFILIE, entity.getNumAffilie());
        }
        container.put(ICPListeColumns.NUM_AVS, NSUtil.formatAVSUnknown(entity.getNumAVS()));
        container.put(ICPListeColumns.GENRE,
                globaz.phenix.translation.CodeSystem.getLibelleCourt(process.getSession(), entity.getGenreAffilie()));
        container.put(ICPListeColumns.ANNEE, entity.getAnnee());
        container.put(ICPListeColumns.ID_TIERS, entity.getIdTiers());
        container.put(ICPListeColumns.MONTANT_COT_PERS, "" + entity.getMontantCP());
        container.put(ICPListeColumns.MONTANT_CI_NOAFFILIE, "" + entity.getMontantCINoAffilie());
        container.put(ICPListeColumns.MONTANT_CI_NOAFFILIE_NOAVS, "" + entity.getMontantCINoAVSNoAffilie());
        container.put(ICPListeColumns.DIFFERENCE_1, "" + entity.getDifference1());
        container.put(ICPListeColumns.DIFFERENCE_2, "" + entity.getDifference2());
        container.put(ICPListeColumns.CATEGORIE, categorie);

    }

    private static void loadHeader(CommonExcelmlContainer container, CPListeExcelConcordanceCotPersCIProcess process,
            JadePublishDocumentInfo docInfo) throws Exception {

        container
                .put(ICPListeColumns.HEADER_NOM_CAISSE,
                        FWIImportProperties.getInstance().getProperty(
                                docInfo,
                                ACaisseReportHelper.JASP_PROP_NOM_CAISSE
                                        + process.getSession().getIdLangueISO().toUpperCase()));

        container.put(ICPListeColumns.HEADER_NUM_INFOROM, CPListeExcelConcordanceCotPersCIProcess.NUMERO_INFOROM);
        container.put(ICPListeColumns.HEADER_NOM_LISTE, process.getSession().getLabel("LISTE_CONCORDANCE_COTPERD_CI"));
        container.put(ICPListeColumns.HEADER_ANNEE, process.getForAnnee());
        container.put(ICPListeColumns.HEADER_DATE_VISA, TimeHelper.getCurrentTime() + " - "
                + process.getSession().getUserName());

        container.put(ICPListeColumns.HEADER_CATEGORIE_1, process.getSession().getLabel("CONCORDANCE_CATEGORIE_1"));
        container.put(ICPListeColumns.HEADER_CATEGORIE_2, process.getSession().getLabel("CONCORDANCE_CATEGORIE_2"));
        container.put(ICPListeColumns.HEADER_CATEGORIE_3, process.getSession().getLabel("CONCORDANCE_CATEGORIE_3"));
        container.put(ICPListeColumns.HEADER_CATEGORIE_4, process.getSession().getLabel("CONCORDANCE_CATEGORIE_4"));
        container.put(ICPListeColumns.HEADER_CATEGORIE_5a, process.getSession().getLabel("CONCORDANCE_CATEGORIE_5A"));
        container.put(ICPListeColumns.HEADER_CATEGORIE_5b, process.getSession().getLabel("CONCORDANCE_CATEGORIE_5B"));
        container.put(ICPListeColumns.HEADER_CATEGORIE_6, process.getSession().getLabel("CONCORDANCE_CATEGORIE_6"));

        container.put(ICPListeColumns.HEADER_REINJECTABLE_1, "");
        container.put(ICPListeColumns.HEADER_REINJECTABLE_2, process.getSession().getLabel("NON_REINJECTABLE"));
        container.put(ICPListeColumns.HEADER_REINJECTABLE_3, "");
        container.put(ICPListeColumns.HEADER_REINJECTABLE_4, "");
        container.put(ICPListeColumns.HEADER_REINJECTABLE_5a, process.getSession().getLabel("NON_REINJECTABLE"));
        container.put(ICPListeColumns.HEADER_REINJECTABLE_5b, "");
        container.put(ICPListeColumns.HEADER_REINJECTABLE_6, process.getSession().getLabel("NON_REINJECTABLE"));
    }

    public static CommonExcelmlContainer loadResults(CPDecisionForCompareCIManager manager,
            CPListeExcelConcordanceCotPersCIProcess process, JadePublishDocumentInfo docInfo) throws Exception {
        CommonExcelmlContainer container = new CommonExcelmlContainer();

        CPXmlmlMappingListeConcordanceCICotPers.loadHeader(container, process, docInfo);
        CPDecisionForCompareCI entityPrecedente = null;
        CPDecisionForCompareCI entityPrecedenteLister = null;
        CPDecisionForCompareCI entitySuivante = null;
        CPDecisionForCompareCI entity = null;

        for (int i = 0; (i < manager.size()) && !process.isAborted(); i++) {

            entity = (CPDecisionForCompareCI) manager.getEntity(i);
            entitySuivante = (CPDecisionForCompareCI) manager.getEntity(i + 1);

            BigDecimal montantCINoAffilie = CPXmlmlMappingListeConcordanceCICotPers.retourneMontantCI(
                    entity.getNumAffilie(), entity.getAnnee(), process);
            BigDecimal montantCp = CPXmlmlMappingListeConcordanceCICotPers.retourneMontantAffilieEnCours(
                    entity.getNumAffilie(), process);
            BigDecimal montantCINoAVSNoAffilie = CPXmlmlMappingListeConcordanceCICotPers
                    .retourneMontantCINoAVSNoAffilie(entity.getNumAffilie(), entity.getAnnee(), entity.getNumAVS(),
                            process);

            if ((entityPrecedente != null) && entityPrecedente.getNumAffilie().equals(entity.getNumAffilie())) {
                if (!((entityPrecedente.getMontantCP().compareTo(entityPrecedente.getMontantCINoAffilie()) == 0)
                        && (entityPrecedente.getMontantCINoAffilie().compareTo(
                                entityPrecedente.getMontantCINoAVSNoAffilie()) == 0)
                        && (montantCp.compareTo(montantCINoAffilie) == 0) && (montantCINoAVSNoAffilie.intValue() == 0))) {
                    if ((montantCINoAffilie.compareTo(montantCp) != 0)
                            || (montantCINoAffilie.compareTo(montantCINoAVSNoAffilie) != 0)) {

                        entityPrecedenteLister = CPXmlmlMappingListeConcordanceCICotPers.chargerInfo(montantCp,
                                montantCINoAffilie, montantCINoAVSNoAffilie, process, entity, entityPrecedenteLister,
                                null, container, i);
                    }
                }
            } else {
                if ((i + 1) < manager.size()) {
                    BigDecimal montantCINoAVSNoAffilieSuivant = CPXmlmlMappingListeConcordanceCICotPers
                            .retourneMontantCINoAVSNoAffilie(entitySuivante.getNumAffilie(), entitySuivante.getAnnee(),
                                    entitySuivante.getNumAVS(), process);
                    if (entitySuivante.getNumAffilie().equals(entity.getNumAffilie())) {
                        if (!((montantCp.compareTo(montantCINoAffilie) == 0)
                                && (montantCINoAffilie.compareTo(montantCINoAVSNoAffilieSuivant) == 0) && (montantCINoAVSNoAffilie
                                    .intValue() == 0))) {
                            if ((montantCINoAffilie.compareTo(montantCp) != 0)
                                    || (montantCINoAffilie.compareTo(montantCINoAVSNoAffilie) != 0)) {
                                entityPrecedenteLister = CPXmlmlMappingListeConcordanceCICotPers.chargerInfo(montantCp,
                                        montantCINoAffilie, montantCINoAVSNoAffilie, process, entity,
                                        entityPrecedenteLister, entitySuivante, container, i);
                            }
                        }
                    } else {
                        if (!((montantCp.compareTo(montantCINoAffilie) == 0)
                                && (montantCINoAVSNoAffilie.intValue() == 0) && JadeStringUtil.isBlankOrZero(entity
                                .getNumAVS()))) {
                            if ((montantCINoAffilie.compareTo(montantCp) != 0)
                                    || (montantCINoAffilie.compareTo(montantCINoAVSNoAffilie) != 0)) {
                                entityPrecedenteLister = CPXmlmlMappingListeConcordanceCICotPers.chargerInfo(montantCp,
                                        montantCINoAffilie, montantCINoAVSNoAffilie, process, entity,
                                        entityPrecedenteLister, entitySuivante, container, i);
                            }
                        }
                    }
                } else {
                    if ((montantCINoAffilie.compareTo(montantCp) != 0)
                            || (montantCINoAffilie.compareTo(montantCINoAVSNoAffilie) != 0)) {
                        entityPrecedenteLister = CPXmlmlMappingListeConcordanceCICotPers.chargerInfo(montantCp,
                                montantCINoAffilie, montantCINoAVSNoAffilie, process, entity, entityPrecedenteLister,
                                entitySuivante, container, i);
                    }
                }
            }
            entityPrecedente = entity;

        }

        return container;
    }

    public static BigDecimal retourneMontantAffilieEnCours(String numAffilie,
            CPListeExcelConcordanceCotPersCIProcess process) throws Exception {

        BigDecimal montantCPBD = new BigDecimal("0");
        BigDecimal montantCP = new BigDecimal("0");
        String montantCiDecision = "";
        BStatement statement = null;
        CPDecisionAffiliationCalcul decision = null;

        // Si la transaction se met en erreurs, il nous faut pouvoir continuer à faire des requêtes pour les prochains
        // cas. Corrections pour le K150624_002, quand la transaction est en erreur, tous les valeurs suivantes se
        // mettaient à zéro car le manager ne pouvait plus se lancer correctement
        if (process.getTransaction().hasErrors() || process.getTransaction().hasWarnings()) {
            process.getSession().getCurrentThreadTransaction().clearErrorBuffer();
            process.getSession().getCurrentThreadTransaction().clearWarningBuffer();
            process.getTransaction().clearErrorBuffer();
            process.getTransaction().clearWarningBuffer();
        }

        CPDecisionAffiliationCalculManager manager = new CPDecisionAffiliationCalculManager();
        manager.setSession(process.getSession());

        AFParticulariteAffiliationManager mgrParticulariteAffiliation = new AFParticulariteAffiliationManager();
        mgrParticulariteAffiliation.setSession(process.getSession());
        if (!JadeStringUtil.isEmpty(process.getForAnnee())) {
            mgrParticulariteAffiliation.setDateDebutLessOrEqual("01.01." + process.getForAnnee());
            mgrParticulariteAffiliation.setDateFinGreatOrEqual("31.12." + process.getForAnnee());
        }
        manager.setNotInIdAffiliation(mgrParticulariteAffiliation.idAffCotPersAutreAgenceOuverte());

        manager.setFromAnneeDecision(process.getForAnnee());
        manager.setTillAnneeDecision(process.getForAnnee());
        manager.setForNoAffilie(numAffilie);
        manager.setIsActiveOrRadie(Boolean.TRUE);
        manager.setForIdDonneesCalcul(CPDonneesCalcul.CS_REV_CI);
        manager.setForExceptTypeDecision(CPDecision.CS_REMISE);
        manager.setForExceptSpecification(CPDecision.CS_SALARIE_DISPENSE);
        manager.setInEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_PB_COMPTABILISATION + ", "
                + CPDecision.CS_REPRISE + ", " + CPDecision.CS_SORTIE);
        // !!! Mettre en premier l'ordre par idTiers à cause des affiliés
        // qui changent de n°
        manager.orderByIdTiers();
        manager.orderByAnnee();
        manager.orderByNoAffilie();

        statement = manager.cursorOpen(process.getTransaction());
        while (((decision = (CPDecisionAffiliationCalcul) manager.cursorReadNext(statement)) != null)
                && (!decision.isNew()) && !process.isAborted()) {

            try {
                if (JadeStringUtil.isEmpty(decision.getMontant()) || Boolean.FALSE.equals(decision.getActive())) {
                    montantCiDecision = "0";
                } else {
                    montantCiDecision = decision.getMontant();
                }
                // Calculer et imputer le CI selon le montant payé comme salarié
                if (CPDecision.CS_SALARIE_DISPENSE.equalsIgnoreCase(decision.getSpecification())) {
                    montantCP = new BigDecimal("0");
                } else {
                    CPDonneesBase base = new CPDonneesBase();
                    base.setSession(process.getSession());
                    base.setIdDecision(decision.getIdDecision());
                    base.retrieve();
                    if (!base.isNew() && !JadeStringUtil.isIntegerEmpty(base.getCotisationSalarie())) {
                        float revenuCiImputation = 0;
                        // recherche coti payé en tant que salarié
                        float cotiEncode = Float.parseFloat(JANumberFormatter.deQuote(base.getCotisationSalarie()));
                        // Calcul du Ci qui doit être imputer selon le montant
                        // de cotisation payé en tant que salarié
                        CPCotisation coti = CPCotisation._returnCotisation(process.getSession(),
                                decision.getIdDecision(), CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                        if (coti != null) {
                            // Calcul du CI
                            revenuCiImputation = (Float.parseFloat(JANumberFormatter.deQuote(coti.getMontantAnnuel())) - cotiEncode)
                                    * (float) 9.9;
                            revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2,
                                    JANumberFormatter.NEAR);
                            revenuCiImputation = Float.parseFloat(JANumberFormatter.deQuote(montantCiDecision))
                                    - revenuCiImputation;
                        } else {
                            revenuCiImputation = cotiEncode * (float) 9.9;
                            revenuCiImputation = JANumberFormatter.round(revenuCiImputation, 1, 2,
                                    JANumberFormatter.NEAR);
                        }
                        montantCP = new BigDecimal(Float.parseFloat(JANumberFormatter.deQuote(montantCiDecision))
                                - revenuCiImputation);
                    } else {
                        montantCP = new BigDecimal(JANumberFormatter.deQuote(montantCiDecision));
                    }
                }
                // Si la décision n'est plus comprise dans l'affiliation =>
                // montant extourné
                // donc montant de décision = 0 et on affiche la période
                // d'affiliation
                if (CPDecision.CS_SORTIE.equalsIgnoreCase(decision.getEtat())) {
                    montantCP = new BigDecimal("0");
                } else if ((!JadeStringUtil.isIntegerEmpty(decision.getFinAffiliation()) && BSessionUtil
                        .compareDateFirstGreaterOrEqual(process.getSession(), decision.getDebutDecision(),
                                decision.getFinAffiliation()))
                        || (BSessionUtil.compareDateFirstLowerOrEqual(process.getSession(), decision.getFinDecision(),
                                decision.getDebutAffiliation()))) {
                    montantCP = new BigDecimal("0");
                } else if (decision.getDebutAffiliation().equalsIgnoreCase(decision.getFinAffiliation())) {
                    montantCP = new BigDecimal("0");
                } else if (CPDecision.CS_IMPUTATION.equalsIgnoreCase(decision.getTypeDecision())) {
                    if (JadeStringUtil.toDouble(JANumberFormatter.deQuote(montantCiDecision)) > 0) {
                        montantCP = new BigDecimal(JANumberFormatter.deQuote("-" + montantCiDecision));
                    } else {
                        montantCP = new BigDecimal(JANumberFormatter.deQuote(montantCiDecision));
                        montantCP = montantCP.abs();
                    }

                }
                // Si la décision est en cours de sortie => prendre les montants
                // des sorties
                CPSortieManager sortieManager = new CPSortieManager();
                sortieManager.setSession(process.getSession());
                sortieManager.setForIdTiers(decision.getIdTiers());
                sortieManager.setForIdDecision(decision.getIdDecision());
                sortieManager.setForAnnee(decision.getAnneeDecision());
                sortieManager.setForChecked(Boolean.FALSE);
                sortieManager.find();
                if (sortieManager.size() > 0) {
                    montantCP = new BigDecimal("0");
                }
                for (int i = 0; i < sortieManager.size(); i++) {
                    String montantSortie = ((CPSortie) sortieManager.getEntity(i)).getMontantCI();
                    if (JadeStringUtil.isEmpty(montantSortie)) {
                        montantSortie = "0";
                    }
                    montantCP = montantCP.subtract(new BigDecimal(JANumberFormatter.deQuote(montantSortie)));
                }
                // SI Etudiant => regarder si le cas a été inscrit au CI dans
                // Campus
                if (CPDecision.CS_ETUDIANT.equalsIgnoreCase(decision.getGenreAffilie())) {
                    // Regarder si l'état dans Campus est "Inscrit au CI"
                    GEAnnoncesManager annMan = new GEAnnoncesManager();
                    annMan.setSession(process.getSession());
                    annMan.setForIdDecision(decision.getIdDecision());
                    annMan.setForCsEtatAnnonce(GEAnnonces.CS_ETAT_COMPTABILISE);
                    annMan.find();
                    if (annMan.getSize() == 0) {
                        montantCP = new BigDecimal("0");
                    }
                }
                montantCPBD = montantCPBD.add(montantCP);

                process.incProgressCounter();
            } catch (Exception e) {
                final String message = decision.getNumAffilie() + " - " + decision.getAnneeDecision() + " - "
                        + decision.getIdDecision();

                Logger.getLogger(process.getClass().getName()).log(Level.INFO, message, e);
                process.getMemoryLog().logMessage(message, FWMessage.INFORMATION, process.getClass().getName());
            }
        }
        manager.cursorClose(statement);
        return montantCPBD;
    }

    /**
     * Méthode qui retourne la somme CI pour un idAffilie
     * 
     * @param idAffilie
     * @return le montant CI
     */
    public static BigDecimal retourneMontantCI(String numAffilie, String annee,
            CPListeExcelConcordanceCotPersCIProcess process) {
        CICompteIndividuelUtil util = new CICompteIndividuelUtil();
        util.setSession(process.getSession());

        return util.getSommeParAnneeNoAffilie(numAffilie, annee);

    }

    public static BigDecimal retourneMontantCINoAVSNoAffilie(String numAffilie, String annee, String numAVS,
            CPListeExcelConcordanceCotPersCIProcess process) {
        CICompteIndividuelUtil util = new CICompteIndividuelUtil();
        util.setSession(process.getSession());

        return util.getSommeParAnneeNoAffilieNoAVS(numAVS, numAffilie, annee);
    }

    private String numAffiliePrecedent = "";

    public String getNumAffiliePrecedent() {
        return numAffiliePrecedent;
    }

    public void setNumAffiliePrecedent(String numAffiliePrecedent) {
        this.numAffiliePrecedent = numAffiliePrecedent;
    }
}
