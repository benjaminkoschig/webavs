package globaz.corvus.utils.retenues;

import globaz.corvus.api.retenues.IRERetenues;
import globaz.corvus.db.retenues.RERetenuesPaiement;
import globaz.corvus.db.retenues.RERetenuesPaiementManager;
import globaz.corvus.process.exception.REProcessRechercheDateProchainPmtException;
import globaz.corvus.utils.REPmtMensuel;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.db.tauxImposition.PRTauxImposition;
import globaz.prestation.db.tauxImposition.PRTauxImpositionManager;
import globaz.prestation.tauxImposition.api.IPRTauxImposition;
import globaz.prestation.tools.PRDateFormater;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class RERetenuesUtil {

    // Appelé lors de traitement des RA en erreur. Retourne la date du lot en l'état partiel.
    public static BigDecimal getDatePmtCourant(BSession session) throws REProcessRechercheDateProchainPmtException {
        JACalendarGregorian cal = new JACalendarGregorian();
        try {
            return JAUtil.createBigDecimal(
                    PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(cal.addMonths(
                            cal.lastInMonth("01.".concat(REPmtMensuel.getDateDernierPmt(session))), 0)), 0);
        } catch (JAException e) {
            throw new REProcessRechercheDateProchainPmtException(
                    "RERetenuesUtil.class ".concat("rechercheDateProchainPmt()"), e);
        }
    }

    /**
     * Methode pour retourner le prochain paiement au format mm.aaaa
     * 
     * @param newRetenue
     * @return
     * @throws Exception
     */
    private static String getDateProchainPaiementMoisAnnee(BSession session) throws Exception {

        String dateProchainPaiement = RERetenuesUtil.getDateProchainPmt(session).toString() + "01";
        dateProchainPaiement = JADate.newDateFromAMJ(dateProchainPaiement).toString();
        return dateProchainPaiement = dateProchainPaiement.substring(2, 4) + "." + dateProchainPaiement.substring(4);
    }

    /**
     * Recherche la date du prochain paiement (dateDernierPmt + 1 mois)
     * 
     * @return
     * @throws REProcessRechercheDateProchainPmtException
     */
    public static BigDecimal getDateProchainPmt(BSession session) throws REProcessRechercheDateProchainPmtException {
        JACalendarGregorian cal = new JACalendarGregorian();
        try {
            return JAUtil.createBigDecimal(
                    PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(cal.addMonths(
                            cal.lastInMonth("01.".concat(REPmtMensuel.getDateDernierPmt(session))), 1)), 0);
        } catch (JAException e) {
            throw new REProcessRechercheDateProchainPmtException(
                    "RERetenuesUtil.class ".concat("rechercheDateProchainPmt()"), e);
        }
    }

    public static BigDecimal getMontantRestant(RERetenuesPaiement retenue) {
        return JAUtil.createBigDecimal(retenue.getMontantTotalARetenir(), 2).subtract(
                JAUtil.createBigDecimal(retenue.getMontantDejaRetenu(), 2));
    }

    /**
     * Récupération de la somme de toutes les retenues liées à une rente accordée
     * 
     * @param BSession
     *            , RERetenuesPaiement
     * @return somme des retenues (BigDecimal)
     * @throws Exception
     */
    public static BigDecimal getSommesRetenuesByRenteAccordee(BSession session, String idRenteAccordee)
            throws Exception {

        BigDecimal sommeTotalRetenuesRenteAccordee = new BigDecimal(0);

        RERetenuesPaiementManager retenueMgr = new RERetenuesPaiementManager();
        retenueMgr.setSession(session);
        retenueMgr.setForIdRenteAccordee(idRenteAccordee);
        retenueMgr.changeManagerSize(0);
        retenueMgr.find();
        Iterator<RERetenuesPaiement> retenuesItr = retenueMgr.iterator();

        // Récupération des montants de toutes les retenues à l'exception de la retenue courante
        for (int i = 0; i < retenueMgr.size(); i++) {
            if (retenuesItr.hasNext()) {
                RERetenuesPaiement retenue = retenuesItr.next();

                // Récupération de la date du prochain paiement
                String dateProchainPaiementMoisAnnee = RERetenuesUtil.getDateProchainPaiementMoisAnnee(session);

                if (
                // Si dateProchainPaiement est compris dans la période de la retenue
                (JadeDateUtil.isDateAfter("01." + dateProchainPaiementMoisAnnee, "01." + retenue.getDateDebutRetenue()) && JadeDateUtil
                        .isDateBefore("01." + dateProchainPaiementMoisAnnee, "01." + retenue.getDateFinRetenue())) ||
                // Si dateProchainPaiement est égal à la date de fin ou date de début de la retenue
                        (JadeDateUtil.areDatesEquals("01." + dateProchainPaiementMoisAnnee,
                                "01." + retenue.getDateDebutRetenue()) || JadeDateUtil.areDatesEquals("01."
                                + dateProchainPaiementMoisAnnee, "01." + retenue.getDateFinRetenue())) ||
                        // Si la dateProchainPaiement est dans une période sans date de fin de la retenue
                        (JadeDateUtil.isDateAfter("01." + dateProchainPaiementMoisAnnee,
                                "01." + retenue.getDateDebutRetenue()) && JadeStringUtil.isBlankOrZero(retenue
                                .getDateFinRetenue()))

                ) {
                    sommeTotalRetenuesRenteAccordee = sommeTotalRetenuesRenteAccordee.add(new BigDecimal(retenue
                            .getMontantRetenuMensuel().replace("'", "")));
                }

            }
        }

        return sommeTotalRetenuesRenteAccordee;

    }

    /**
     * Récupération de la somme de toutes les retenues liées à une retenue pour une même période
     * 
     * @param RERetenuesPaiement
     *            param
     * @return somme des retenues (BigDecimal)
     * @throws Exception
     */
    public static BigDecimal getSommesRetenuesRenteAccordee(RERetenuesPaiement newRetenue) throws Exception {

        BigDecimal sommeTotalRetenuesRenteAccordee = new BigDecimal(0);

        RERetenuesPaiementManager retenueMgr = new RERetenuesPaiementManager();
        retenueMgr.setSession(newRetenue.getSession());
        retenueMgr.setForIdRenteAccordee(newRetenue.getIdRenteAccordee());
        retenueMgr.changeManagerSize(0);
        retenueMgr.find();
        Iterator<RERetenuesPaiement> retenuesItr = retenueMgr.iterator();

        // Déclaration d'une liste pour stocker toutes les retenues liées à la rente accordée.
        Map<String, RERetenuesPaiement> mapRetenues = new HashMap<String, RERetenuesPaiement>();

        // Récupération de la nouvelle retenue
        mapRetenues.put(newRetenue.getIdRetenue(), newRetenue);

        // Récupération des retenues présentent en DB
        for (int i = 0; i < retenueMgr.size(); i++) {
            if (retenuesItr.hasNext()) {
                RERetenuesPaiement retenue = retenuesItr.next();
                // listRetenues.add(retenue);

                if (!mapRetenues.containsKey(retenue.getIdRetenue())) {
                    mapRetenues.put(retenue.getIdRetenue(), retenue);
                }
            }
        }

        // Récupération de la date du prochain paiement
        String dateProchainPaiement = RERetenuesUtil.getDateProchainPaiementMoisAnnee(newRetenue.getSession());

        // Récupération des montants de toutes les retenues qui touchent la même période que la nouvelle retenue.
        for (String idRetenue : mapRetenues.keySet()/* listRetenues */) {

            RERetenuesPaiement retenue = mapRetenues.get(idRetenue);

            // Vérifie que la retenue est dans la même période que la nouvelle retenue
            if (RERetenuesUtil.isRetenueDansMemePeriode(retenue, newRetenue, dateProchainPaiement)) {

                FWCurrency montantDejaRetenu = new FWCurrency(retenue.getMontantDejaRetenu());
                FWCurrency montantRetenuMensuel = new FWCurrency(retenue.getMontantRetenuMensuel());
                FWCurrency prochaineRetenue = null;
                if (IRERetenues.CS_TYPE_IMPOT_SOURCE.equals(retenue.getCsTypeRetenue())) {

                    // pour l'imposition a la source, le taux peut être donne de plusieurs manières
                    if (!JadeStringUtil.isDecimalEmpty(retenue.getTauxImposition())) {

                        // donne par un taux fixe
                        String montantRA = retenue.getRenteAccordee().getMontantPrestation();

                        montantRetenuMensuel = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                                * (new FWCurrency(retenue.getTauxImposition())).floatValue());
                        montantRetenuMensuel.round(FWCurrency.ROUND_ENTIER);

                    } else if (!JadeStringUtil.isDecimalEmpty(retenue.getMontantRetenuMensuel())) {

                        // un montant fixe
                        montantRetenuMensuel = new FWCurrency(retenue.getMontantRetenuMensuel());

                    } else {

                        // donne par un canton
                        String montantRA = retenue.getRenteAccordee().getMontantPrestation();

                        // recherche du taux
                        PRTauxImpositionManager tManager = new PRTauxImpositionManager();
                        tManager.setSession(newRetenue.getSession());
                        tManager.setForCsCanton(retenue.getCantonImposition());
                        tManager.setForTypeImpot(IPRTauxImposition.CS_TARIF_D);
                        tManager.find();

                        PRTauxImposition t = (PRTauxImposition) tManager.getFirstEntity();
                        String taux = "0.0";
                        if (t != null) {
                            taux = t.getTaux();
                        }

                        montantRetenuMensuel = new FWCurrency((new FWCurrency(montantRA).floatValue() / 100)
                                * (new FWCurrency(taux)).floatValue());
                        montantRetenuMensuel.round(FWCurrency.ROUND_ENTIER);
                    }

                    prochaineRetenue = montantRetenuMensuel;

                } else {
                    FWCurrency tmp = new FWCurrency(retenue.getMontantTotalARetenir());
                    tmp.sub(montantDejaRetenu);
                    // si le montant de la retenue est plus grand que tmp
                    if (montantRetenuMensuel.compareTo(tmp) > 0) {
                        prochaineRetenue = tmp;
                    } else {
                        prochaineRetenue = montantRetenuMensuel;
                    }
                }

                sommeTotalRetenuesRenteAccordee = sommeTotalRetenuesRenteAccordee.add(new BigDecimal(prochaineRetenue
                        .toString().replace("'", "")));
            }
        }

        return sommeTotalRetenuesRenteAccordee;

    }

    /**
     * Test si la retenue est active
     * 
     * @param entity
     * @return
     * @throws REProcessRechercheDateProchainPmtException
     */
    public static boolean isRetenueActive(BSession session, RERetenuesPaiement entity, boolean isTraitementPAEnErreur)
            throws REProcessRechercheDateProchainPmtException {
        boolean active = false;
        BigDecimal mtZero = JAUtil.createBigDecimal("0.00");
        BigDecimal mtRestant = RERetenuesUtil.getMontantRestant(entity);
        // rechcerche la date du prochain paiement
        BigDecimal dateProchainPmt = null;

        if (isTraitementPAEnErreur) {
            dateProchainPmt = RERetenuesUtil.getDatePmtCourant(session);
        } else {
            dateProchainPmt = RERetenuesUtil.getDateProchainPmt(session);
        }

        BigDecimal dateDebutRetenues = JAUtil.createBigDecimal(
                PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(entity.getDateDebutRetenue()), 0);
        BigDecimal dateFinRetenues = JAUtil.createBigDecimal(
                PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(entity.getDateFinRetenue()), 0);

        /*
         * On doit tester les cas de figures suivant
         */
        // - Type Adresse de paiement
        // ET montant restant > 0
        // ET date début des retenues <= dateDernierPmt + 1 mois (=
        // dateProchainPaiement)
        // - Type Facture futures
        // ET montant restant > 0
        // ET date début des retenues <= dateDernierPmt + 1 mois (=
        // dateProchainPaiement)
        // - Type Facture
        // ET montant restant > 0
        // ET date début des retenues <= dateDernierPmt + 1 mois (=
        // dateProchainPaiement)
        // - Type IS
        // ET
        // ( Canton d'imposition <>'' OU
        // Taux <> 0 OU
        // (date début des retenues <= dateDernierPmt + 1 mois (=
        // dateProchainPaiement)
        // ET Date fin des retenues = 0 OU Date fin des retenues >=
        // dateDernierPmt + 1 mois (= dateProchainPaiement)))
        // - Type Compte spécial
        // ET montant restant > 0
        // ET date début des retenues <= dateDernierPmt + 1 mois (=
        // dateProchainPaiement)
        int typeRetenue = Integer.parseInt(entity.getCsTypeRetenue());
        switch (typeRetenue) {
            case IRERetenues.ID_TYPE_ADRESSE_PMT:
                if ((mtRestant.compareTo(mtZero) == 1)
                        && (dateProchainPmt.compareTo(dateDebutRetenues) != -1)
                        && ((dateFinRetenues.compareTo(mtZero) == 0) || (dateFinRetenues.compareTo(dateProchainPmt) != -1))) {
                    active = true;
                }
                break;
            case IRERetenues.ID_TYPE_FACTURE_FUTURE:
                if ((mtRestant.compareTo(mtZero) == 1)
                        && (dateProchainPmt.compareTo(dateDebutRetenues) != -1)
                        && ((dateFinRetenues.compareTo(mtZero) == 0) || (dateFinRetenues.compareTo(dateProchainPmt) != -1))) {
                    active = true;
                }
                break;
            case IRERetenues.ID_TYPE_FACTURE_EXISTANTE:
                if ((mtRestant.compareTo(mtZero) == 1)
                        && (dateProchainPmt.compareTo(dateDebutRetenues) != -1)
                        && ((dateFinRetenues.compareTo(mtZero) == 0) || (dateFinRetenues.compareTo(dateProchainPmt) != -1))) {
                    active = true;
                }
                break;
            case IRERetenues.ID_TYPE_IMPOT_SOURCE:
                if ((!JadeNumericUtil.isEmptyOrZero(entity.getCantonImposition()) || !JadeNumericUtil
                        .isEmptyOrZero(entity.getTauxImposition()))
                        && ((dateProchainPmt.compareTo(dateDebutRetenues) != -1) && ((dateFinRetenues.compareTo(mtZero) == 0) || (dateFinRetenues
                                .compareTo(dateProchainPmt) != -1)))) {
                    active = true;
                }
                break;
            case IRERetenues.ID_TYPE_COMPTE_SPECIAL:
                if ((mtRestant.compareTo(mtZero) == 1) && (dateProchainPmt.compareTo(dateDebutRetenues) != -1)) {
                    active = true;
                }
                break;
        }
        return active;
    }

    private static boolean isRetenueDansMemePeriode(RERetenuesPaiement retenue, RERetenuesPaiement newRetenue,
            String dateProchainPaiement) throws Exception {

        String dateProchainPaiementPlusUnMois = JadeDateUtil.addMonths("01." + dateProchainPaiement, 1);

        // Si la nouvelle retenue est supérieur au prochain paiement, on ne la test pas
        if (JadeDateUtil.areDatesEquals("01." + newRetenue.getDateDebutRetenue(), dateProchainPaiementPlusUnMois)
                || JadeDateUtil.isDateAfter("01." + newRetenue.getDateDebutRetenue(), dateProchainPaiementPlusUnMois)) {
            return false;
        }

        // Condition pour intégrer la retenue courante à la nouvelle retenue
        if (
        // Si la retenue courante commence avant la nouvelle retenue, et n'a pas de date de fin
        (JadeDateUtil.isDateBefore("01." + retenue.getDateDebutRetenue(), "01." + newRetenue.getDateDebutRetenue()) && JadeStringUtil
                .isBlankOrZero(retenue.getDateFinRetenue())) ||
        // Si la retenue courante commence avant la nouvelle retenue, et que sa date de fin ce termine pendant
        // la nouvelle
                ((JadeDateUtil.isDateBefore("01." + retenue.getDateDebutRetenue(),
                        "01." + newRetenue.getDateDebutRetenue())) && (JadeDateUtil.isDateAfter(
                        "01." + retenue.getDateFinRetenue(), "01." + newRetenue.getDateDebutRetenue()) && JadeDateUtil
                        .isDateBefore("01." + retenue.getDateFinRetenue(), "01." + newRetenue.getDateFinRetenue()))) ||
                // Si la retenue courante commence avant et se termine après la nouvelle retenue
                ((JadeDateUtil.isDateBefore("01." + retenue.getDateDebutRetenue(),
                        "01." + newRetenue.getDateDebutRetenue())) && (JadeDateUtil.isDateAfter(
                        "01." + retenue.getDateFinRetenue(), "01." + newRetenue.getDateFinRetenue()))) ||
                // Si la retenue courante commence dans la période de la nouvelle retenue et qu'elle n'est pas plus
                // grande que la date du prochain paiement + 1 mois
                ((JadeDateUtil.areDatesEquals("01." + retenue.getDateDebutRetenue(),
                        "01." + newRetenue.getDateDebutRetenue())) || (JadeDateUtil.isDateAfter(
                        "01." + retenue.getDateDebutRetenue(), "01." + newRetenue.getDateDebutRetenue())
                        && JadeDateUtil.isDateBefore("01." + retenue.getDateDebutRetenue(),
                                "01." + newRetenue.getDateFinRetenue()) && JadeDateUtil.isDateBefore(
                        "01." + retenue.getDateDebutRetenue(), dateProchainPaiementPlusUnMois))) ||
                // Si la retenue courante se termine pendant la période de la nouvelle retenue
                (JadeDateUtil.isDateAfter("01." + retenue.getDateFinRetenue(), "01." + newRetenue.getDateFinRetenue()) && JadeDateUtil
                        .isDateBefore("01." + retenue.getDateFinRetenue(), "01." + newRetenue.getDateFinRetenue())) ||
                // Si la retenue courante se termine en même temps quand la nouvelle retenue commence
                (JadeDateUtil.areDatesEquals("01." + retenue.getDateFinRetenue(),
                        "01." + newRetenue.getDateFinRetenue()))

        ) {
            return true;
        } else {
            return false;
        }
    }
}
