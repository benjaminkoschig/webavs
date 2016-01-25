package globaz.aquila.db.rdp;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COContentieux;
import globaz.aquila.db.access.poursuite.COExtraitCompteManager;
import globaz.aquila.db.access.poursuite.COHistorique;
import globaz.aquila.service.COServiceLocator;
import globaz.aquila.service.historique.COHistoriqueService;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CAReferenceRubriqueManager;
import globaz.osiris.db.comptes.extrait.CAExtraitCompte;
import globaz.osiris.db.contentieux.CAEcritureCompteCourantCotSoumisInt;
import globaz.osiris.db.contentieux.CAEcritureRubriqueCotSoumisInt;
import globaz.osiris.db.interet.tardif.CAInteretTardif;
import globaz.osiris.db.interet.tardif.CAInteretTardifFactory;
import globaz.osiris.db.interet.util.CAInteretUtil;
import globaz.osiris.utils.CADateUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class CORequisitionPoursuiteUtil {

    private static final String ZERO_FRANC = "0.00";

    /**
     * Récupère la date de début des intérêts moratoires tardifs pour une section / facture
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @return la date de début des intérêts moratoires tardifs pour une section / facture
     * @throws Exception
     */
    public static String getDateDebutInteretsTardifs(BSession session, BTransaction transaction,
            COContentieux contentieux) throws Exception {
        JADate dateDebutInteret = new JADate();

        CAInteretTardif interet = CAInteretTardifFactory.getInteretTardif(contentieux.getSection()
                .getCategorieSection());
        if (interet != null) {
            // Y-a-t'il une rubrique soumise à intérêt dans la section ?
            FWCurrency montantSoumis = CAInteretUtil.getMontantSoumisParPlans(transaction, contentieux.getIdSection(),
                    null, null);

            if ((montantSoumis != null) && montantSoumis.isPositive()) {
                interet.setIdSection(contentieux.getIdSection());
                dateDebutInteret = interet.getDateCalculDebutInteret(contentieux.getSession(), contentieux.getSession()
                        .getCurrentThreadTransaction());
            }
        }
        return JACalendar.format(dateDebutInteret);
    }

    /**
     * Retourne true si la date d'exécution de la RP était dans le nouveau regime
     * 
     * @param session la session
     * @param curContentieux le contentieux
     * @return true si la RP est dans le nouveau regime.
     */
    public static Boolean isNouveauRegimeSelonDateRP(BSession session, COContentieux curContentieux) {
        try {
            // Nous cherchons la propriété permettant de savoir la date de production du nouveau regime
            String dateProduction = session.getApplication().getProperty("dateProductionNouveauCDP");
            if (dateProduction == null || dateProduction.length() == 0) {
                return false;
            }

            try {
                String dateRP = CORequisitionPoursuiteUtil.getDateExecutionRP(session, curContentieux);
                if (dateRP != null) {
                    // retourne true si la date d'execution de la RP est supérieure ou égale à la date de production du
                    // nouveau regime
                    return BSessionUtil.compareDateFirstGreaterOrEqual(session, dateRP, dateProduction);
                }
            } catch (Exception e) {
                JadeLogger.warn(e, e.getMessage());
            }
        } catch (Exception e) {
            JadeLogger.error(e, "La propriété n'existe pas.");
        }

        return false;
    }

    public static Boolean isOfficeDontWantToUseNewRegime(BSession session, String officeCanton) {
        Boolean dontWantNewRegime = false;
        if (officeCanton == null) {
            return false;
        }

        String office = officeCanton.toUpperCase();

        try {
            String propertyOffice = session.getApplication().getProperty("isOfficeDontWantToUseNewRegime");

            if (propertyOffice == null) {
                throw new Exception("Property inexistante");
            }

            String[] officesAncienRegime = (propertyOffice.trim()).split(",");

            for (String officeAncienRegime : officesAncienRegime) {
                if (office.equals(officeAncienRegime.toUpperCase())) {
                    dontWantNewRegime = true;
                }
            }
            return dontWantNewRegime;
        } catch (Exception e) {
            // Si la property n'existe pas, on fait comme si on n'avait rien fait
            return false;
        }
    }

    /**
     * Obtient la date d'exécution de la RP
     * 
     * @param session la session
     * @param curContentieux le contentieux
     * @return La date d'exécution de la RP
     * @throws Exception
     */
    public static String getDateExecutionRP(BSession session, COContentieux curContentieux) throws Exception {
        // Vérification des paramètres passés
        if (session == null || curContentieux == null) {
            return null;
        }

        COHistoriqueService historiqueService = new COHistoriqueService();
        COHistorique historiqueRP;

        historiqueRP = historiqueService.getHistoriqueForLibEtape(session, curContentieux,
                ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE);

        return (historiqueRP != null) ? historiqueRP.getDateExecution() : null;
    }

    /**
     * Obtient la date d'exécution de la RP en ajoutant 1 jour.
     * 
     * @param session la session
     * @param curContentieux le contentieux
     * @return La date d'exécution de la RP + 1 jour
     * @throws Exception
     */
    public static String getDateExecutionRPPlus1Day(BSession session, COContentieux curContentieux) throws Exception {
        return JadeDateUtil.addDays(getDateExecutionRP(session, curContentieux), 1);
    }

    /**
     * Récupère la date de fin des intérêts moratoires tardifs pour une section / facture
     * 
     * @param session
     * @param transaction
     * @param contentieux
     * @return la date de fin des intérêts moratoires tardifs pour une section / facture
     * @throws Exception
     */
    public static String getDateFinInteretsTardifs(BTransaction transaction, COContentieux contentieux)
            throws Exception {
        JADate dateFinInteret = new JADate();
        CAInteretTardif interet = CAInteretTardifFactory.getInteretTardif(contentieux.getSection()
                .getCategorieSection());
        if (interet != null) {
            // Y-a-t'il une rubrique soumise à intérêt dans la section ?
            FWCurrency montantSoumis = CAInteretUtil.getMontantSoumisParPlans(transaction, contentieux.getIdSection(),
                    null, null);

            if ((montantSoumis != null) && montantSoumis.isPositive()) {
                interet.setIdSection(contentieux.getIdSection());
                dateFinInteret = interet.getDateCalculDebutInteret(contentieux.getSession(), contentieux.getSession()
                        .getCurrentThreadTransaction());

                JACalendar cal = contentieux.getSession().getApplication().getCalendar();
                dateFinInteret = cal.addDays(dateFinInteret, 30 - 1);

                dateFinInteret = CADateUtil.getDateOuvrable(dateFinInteret);
            }
        }

        return JACalendar.format(dateFinInteret);
    }

    /**
     * Retourne le libellé de la créance avec la date de décision
     * 
     * @param session
     * @param contentieux
     * @return le libellé de la créance avec la date de décision
     * @throws Exception
     */
    public static String getLibelleCreance(BSession session, COContentieux contentieux) throws Exception {
        String libelle = contentieux.getSection().getDescription();
        COHistorique historique = COServiceLocator.getHistoriqueService().getHistoriqueForLibEtape(session,
                contentieux, ICOEtape.CS_DECISION);
        if (historique != null) {
            libelle += " " + session.getLabel("RDP_SELON_DECISION_DU") + " " + historique.getDateExecution();
        }

        return libelle;
    }

    /**
     * Montants de la créance SOUMIS à intérets du contentieux.<br/>
     * Non formaté.
     * 
     * @param transaction La transaction
     * @param contentieux Le contentieux
     * @return montant cotisation soumis à intéret + paiement et compensation soumis à intéret.
     * @throws Exception
     */
    public static String getMontantCreanceSoumis(BTransaction transaction, COContentieux contentieux) throws Exception {
        return getMontantCreanceSoumis(transaction, contentieux,
                CORequisitionPoursuiteUtil.getDateFinInteretsTardifs(transaction, contentieux));
    }

    /**
     * Montants de la créance SOUMIS à intérets du contentieux.<br/>
     * Non formaté.
     * 
     * @param transaction La transaction
     * @param contentieux Le contentieux
     * @param date La date de fin d interets
     * @return montant cotisation soumis à intéret + paiement et compensation soumis à intéret.
     * @throws Exception
     */
    public static String getMontantCreanceSoumis(BTransaction transaction, COContentieux contentieux, String date)
            throws Exception {
        // Récuperer le montant des rubriques de cotisations qui sont soumises à
        // intérêt pour la section en cours
        BigDecimal montant = CORequisitionPoursuiteUtil.getMontantEcritureCotRubriqueSoumisInt(
                transaction.getSession(), contentieux.getSection().getIdSection());

        // Récupérer le montant des pmtCmp (standard avec montant < 0, cc
        // débiteur, cc créancier)
        BigDecimal pmtCmpSoumisIntUntilDate = CORequisitionPoursuiteUtil.getPmtCmpEcritureCotRubriqueSoumisInt(
                transaction.getSession(), contentieux.getSection().getIdSection(), date);
        montant = montant.add(pmtCmpSoumisIntUntilDate);

        try {
            JAUtil.checkNumberPositiveOrZero(montant);
        } catch (Exception e) {
            montant = new BigDecimal(CORequisitionPoursuiteUtil.ZERO_FRANC);
        }

        return montant.toString();
    }

    /**
     * Cette méthode retourne la somme des écritures (E%) de type cotisation(avec et sans masse) comptabilisées de la
     * section qui sont soumises aux intérêts
     * 
     * @param session
     * @param idSection
     * @return BigDecimal somme des écritures soumises aux intérêts
     */
    private static BigDecimal getMontantEcritureCotRubriqueSoumisInt(BSession session, String idSection)
            throws Exception {
        BigDecimal montantSoumisInt = new BigDecimal(CORequisitionPoursuiteUtil.ZERO_FRANC);
        CAEcritureRubriqueCotSoumisInt ecMan = new CAEcritureRubriqueCotSoumisInt();
        ecMan.setSession(session);
        ecMan.setForIdSection(idSection);
        ecMan.find(0);
        if (!ecMan.isEmpty()) {
            for (int i = 0; i < ecMan.size(); i++) {
                CAEcriture ec = (CAEcriture) ecMan.getEntity(i);
                montantSoumisInt = montantSoumisInt.add(new BigDecimal(ec.getMontant()));
            }
        }
        return montantSoumisInt;
    }

    /**
     * Cette méthode retourne la somme des pmtCmp (E%) de type cotisation(avec et sans masse) ainsi que les pmtcmp (E%)
     * de type standard qui on un montant < 0 et qui sont comptabilisées de la section qui sont soumises aux intérêts
     * 
     * @param session
     * @param idSection
     * @return BigDecimal somme des écritures soumises aux intérêts
     */
    private static BigDecimal getPmtCmpEcritureCotRubriqueSoumisInt(BSession session, String idSection, String untilDate)
            throws Exception {
        BigDecimal montantSoumisInt = new BigDecimal(CORequisitionPoursuiteUtil.ZERO_FRANC);
        CAEcritureCompteCourantCotSoumisInt ecMan = new CAEcritureCompteCourantCotSoumisInt();
        ecMan.setSession(session);
        ecMan.setForIdSection(idSection);

        if (!JadeStringUtil.isEmpty(untilDate)) {
            ecMan.setUntilDate(untilDate);
        }
        ecMan.find(0);
        if (!ecMan.isEmpty()) {
            for (int i = 0; i < ecMan.size(); i++) {
                CAEcriture ec = (CAEcriture) ecMan.getEntity(i);
                montantSoumisInt = montantSoumisInt.add(new BigDecimal(ec.getMontant()));
            }
        }
        // Si le montant des paiements est positif, on retourne 0.
        if (montantSoumisInt.doubleValue() > 0) {
            montantSoumisInt = new BigDecimal(CORequisitionPoursuiteUtil.ZERO_FRANC);
        }
        return montantSoumisInt;
    }

    /**
     * Retourne un tableau de String contenant :
     * <ul>
     * <li>[0] Solde initial de la section</li>
     * <li>[1] idJournal de création de la section</li>
     * <li>[2] Date de la section</li>
     * </ul>
     * 
     * @param idSection
     * @return String [2]
     * @throws Exception
     */
    public static String[] getSoldeSectionInitial(BSession session, String idSection) throws Exception {
        String idJournal = "";
        BigDecimal solde = new BigDecimal("0");
        String[] retour = { solde.toString(), idJournal, "" + "" };

        COExtraitCompteManager soldeInitialMng = new COExtraitCompteManager();
        soldeInitialMng.setSession(session);
        soldeInitialMng.setForSectionForSoldeInitiale(idSection);
        soldeInitialMng.find(BManager.SIZE_NOLIMIT);
        if (soldeInitialMng.getSize() == 0) {
            return retour;
        } else {
            CAExtraitCompte extraitCompte = (CAExtraitCompte) soldeInitialMng.getFirstEntity();
            idJournal = extraitCompte.getIdJournal();
            if (!JadeStringUtil.isDecimalEmpty(extraitCompte.getMontant())) {
                solde = solde.add(new BigDecimal(extraitCompte.getMontant()));
            }
            retour = new String[] { solde.toString(), idJournal, "" + extraitCompte.getDate() };
        }
        return retour;
    }

    /**
     * 
     * @param session
     * @return
     */
    public static Boolean wantPutOnlyTaxesPoursuite(BSession session) {
        try {
            String allowTaxesPoursuite = session.getApplication().getProperty("wantMettreTaxesRestantesAuxPoursuites");
            if (allowTaxesPoursuite == null) {
                return false;
            }

            return new Boolean(allowTaxesPoursuite);
        } catch (Exception e) {
            JadeLogger.error(e, "La propriété n'existe pas.");
            return false;
        }
    }

    /**
     * Certaines taxes ne doivent pas être affichées sur les documents (réquisitions de poursuites).
     * 
     * @param session
     * @param transaction
     * @param csEtape
     * @param idRubrique
     * @return true si la ligne de taxe ne doit pas s'afficher sur le document
     */
    public static boolean isLineBlocked(BTransaction transaction, String csEtape, String idRubrique) {
        ArrayList<String> list = new ArrayList<String>();

        /*
         * list contient les références rubriques bloquées Tout afficher pour l'affilier <br> Bloquer tous les frais
         * pour l'OP sauf demande de mainlevée sur RCP et Rv <br> Tout afficher pour le tribunal.
         */
        if (ICOEtape.CS_REQUISITION_DE_POURSUITE_ENVOYEE.equals(csEtape)) {
            // Référence Rubrique à bloquer
            list.add(APIReferenceRubrique.FRAIS_POURSUITES);
            list.add(APIReferenceRubrique.FRAIS_AVOCAT);
            list.add(APIReferenceRubrique.FRAIS_POURSUITES_AMORTIS);
            list.add(APIReferenceRubrique.AUTRES_FRAIS_CONTENTIEUX);
            list.add(APIReferenceRubrique.FRAIS_POURSUITES_PARITAIRE);
            list.add(APIReferenceRubrique.FRAIS_AVOCAT_PARITAIRE);
            list.add(APIReferenceRubrique.FRAIS_POURSUITES_AMORTIS_PARITAIRE);
            list.add(APIReferenceRubrique.AUTRES_FRAIS_CONTENTIEUX_PARITAIRE);
            // list.add(APIReferenceRubrique.FRAIS_MAINLEVEE);
            // list.add(APIReferenceRubrique.FRAIS_MAINLEVEE_PARITAIRE);
            // B713 : Les frais de mainlevée doivent figurer sur la RCP et la
            // RV, mais il faut définir une rubrique autre que frais de
            // poursuite, pour ces mainlevées
            // Suite à mainlevée, les frais de mainlevée doivent venir sous
            // annexe, avec l'intitulé jugement de mainlevée
            // Ajouter en bas du document, Annexe : jugement de mainlevée
        } else if (ICOEtape.CS_REQUISITION_DE_CONTINUER_LA_POURSUITE_ENVOYEE.equals(csEtape)) {
            list.add(APIReferenceRubrique.FRAIS_POURSUITES);
            list.add(APIReferenceRubrique.FRAIS_AVOCAT);
            list.add(APIReferenceRubrique.FRAIS_POURSUITES_AMORTIS);
            list.add(APIReferenceRubrique.AUTRES_FRAIS_CONTENTIEUX);
            list.add(APIReferenceRubrique.FRAIS_POURSUITES_PARITAIRE);
            list.add(APIReferenceRubrique.FRAIS_AVOCAT_PARITAIRE);
            list.add(APIReferenceRubrique.FRAIS_POURSUITES_AMORTIS_PARITAIRE);
            list.add(APIReferenceRubrique.AUTRES_FRAIS_CONTENTIEUX_PARITAIRE);
        } else if (ICOEtape.CS_REQUISITION_DE_VENTE_SAISIE.equals(csEtape)) {
            list.add(APIReferenceRubrique.FRAIS_POURSUITES);
            list.add(APIReferenceRubrique.FRAIS_AVOCAT);
            list.add(APIReferenceRubrique.FRAIS_POURSUITES_AMORTIS);
            list.add(APIReferenceRubrique.AUTRES_FRAIS_CONTENTIEUX);
            list.add(APIReferenceRubrique.FRAIS_POURSUITES_PARITAIRE);
            list.add(APIReferenceRubrique.FRAIS_AVOCAT_PARITAIRE);
            list.add(APIReferenceRubrique.FRAIS_POURSUITES_AMORTIS_PARITAIRE);
            list.add(APIReferenceRubrique.AUTRES_FRAIS_CONTENTIEUX_PARITAIRE);
        }

        CAReferenceRubriqueManager manager = new CAReferenceRubriqueManager();
        manager.setSession(transaction.getSession());
        manager.setForCodeReferenceIn(list);

        try {
            manager.find(transaction);
        } catch (Exception e) {
        }

        Iterator<CAReferenceRubrique> i = manager.iterator();
        while (!list.isEmpty() && i.hasNext()) {
            CAReferenceRubrique ref = i.next();
            if (ref.getIdRubrique().equals(idRubrique)) {
                // La rubrique fait partie des références rubrique bloquées.
                return true;
            }
        }

        return false;
    }

}
