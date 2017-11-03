package globaz.corvus.utils.beneficiaire.principal;

import globaz.corvus.dao.IREValidationLevel;
import globaz.corvus.dao.REInfoCompta;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.utils.enumere.genre.prestations.REGenrePrestationEnum;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class REBeneficiairePrincipal {

    /**
     * Si aucune adresse de pmt trouvée pour la rente accordée, récupération de l'adresse de pmt selon l'ordre suivant :
     * Adr pmt de personne vivant du groupe prioritaire Si pers. dans même groupe, prendre l'adr. du plus jeune
     * 
     * @param session
     * @param transaction
     * @param demande
     * @param idsRA
     *            Liste des ids des RA!!!
     * @throws Exception
     */
    public static void doMajAdressePmtDesRentesAccordees(BSession session, BITransaction transaction, List<Long> idsRA)
            throws Exception {

        /*
         * 
         * Etape 1 : on stocke l'adresse de pmt du groupe prioritaire !!!!!
         */
        int _groupLevel = 100;
        String _idTiersAdressePmt = null;
        JADate _dateNaissance = null;

        JACalendar cal = new JACalendarGregorian();
        // Parcours de toutes les RA
        Set<Long> lst = null;
        String idTiersArdPmtAssureDecede = null;
        for (Long idRA : idsRA) {
            lst = new HashSet<Long>();
            lst.add(idRA);

            int level = REBeneficiairePrincipal.getGroupLevel(session, transaction, lst);
            if (level <= _groupLevel) {

                RERenteAccordee ra = new RERenteAccordee();
                ra.setSession(session);
                ra.setIdPrestationAccordee(idRA.toString());
                ra.retrieve(transaction);
                PRAssert.notIsNew(ra, null);

                PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());
                String idTiersAdrPmt = ra.loadInformationsComptabilite().getIdTiersAdressePmt();

                // La personne est décédée... on ne la prend donc pas en compte pour le calcul !!!
                if (!JadeStringUtil.isBlankOrZero(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES))) {
                    idTiersArdPmtAssureDecede = idTiersAdrPmt;
                    continue;
                }

                if (level < _groupLevel) {
                    if (!JadeStringUtil.isBlankOrZero(idTiersAdrPmt)) {
                        _groupLevel = level;
                        _idTiersAdressePmt = idTiersAdrPmt;
                    }
                }
                // Dans le même groupe... on stocke le plus jeune!!!
                else {
                    if (!JadeStringUtil.isBlankOrZero(idTiersAdrPmt)) {
                        String dn = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
                        JADate dateNaissance = null;
                        if (!JadeStringUtil.isBlankOrZero(dn)) {
                            dateNaissance = new JADate(dn);

                            if ((_dateNaissance == null)
                                    || (cal.compare(dateNaissance, _dateNaissance) == JACalendar.COMPARE_FIRSTUPPER)) {
                                _groupLevel = level;
                                _idTiersAdressePmt = idTiersAdrPmt;
                                _dateNaissance = new JADate(
                                        PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateNaissance.toStrAMJ()));
                            }
                        }
                    }

                }
            }
        }

        /*
         * 
         * Etape 2 : Pour toutes les RA n'ayant pas d'adresse de pmt, on stocke celle précédemment récupérée !!!
         */

        // Dans le cas d'un assuré décédé sans aucun membre de la famille, _idTiersAdrPmt aura la valeur null.
        // Il faut dans ce cas prendre l'adresse de pmt de l'assuré décédé.
        if (JadeStringUtil.isBlankOrZero(_idTiersAdressePmt)
                && !JadeStringUtil.isBlankOrZero(idTiersArdPmtAssureDecede)) {
            _idTiersAdressePmt = idTiersArdPmtAssureDecede;
        }
        for (Long idRA : idsRA) {

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setIdPrestationAccordee(idRA.toString());
            ra.retrieve(transaction);
            PRAssert.notIsNew(ra, null);

            REInformationsComptabilite ic = ra.loadInformationsComptabilite();

            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, ic.getIdTiersAdressePmt());

            if (tw != null) {
                if (JadeStringUtil.isBlankOrZero(ic.getIdTiersAdressePmt())) {

                    ic.setIdTiersAdressePmt(_idTiersAdressePmt);

                    ic.update(transaction);
                } else if (!JadeStringUtil.isEmpty(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES))) {

                    ic.setIdTiersAdressePmt(ra.getIdTiersComplementaire2());

                    ic.update(transaction);
                }
            }
        }
    }

    /**
     * @param session
     * @param transaction
     * @param idsRA
     * @return
     * @throws Exception
     */
    public static int getGroupLevel(BSession session, BITransaction transaction, Set<Long> idsRA) throws Exception {
        /*
         * 1. les prestations 10, 13, 20, 23, 50, 70, 72 sont considérées comme le tri 1 2. les prestations 81, 82, 83,
         * 84, 85, 86, 87, 88, 91, 92, 93, 95, 96, 97 sont considérées comme le tri 2 3. les prestations 33, 53, 73 sont
         * considérées comme le tri 4 4. les prestations 14, 15, 16, 24, 25, 26, 34, 35, 45, 54, 55, 74, 75 sont
         * considérées comme le tri 5
         */

        // Récupération du bénéficiaire principal
        // Priorité au groupe 1, puis 2, 4 et 5

        int groupLevel = 100;

        for (Long idRA : idsRA) {
            RERenteAccordee ra = REBeneficiairePrincipal.retrieveRA(session, transaction, idRA);

            if (REGenrePrestationEnum.groupe1.contains(ra.getCodePrestation())) {
                groupLevel = 1;
                break;

            } else if (REGenrePrestationEnum.groupe2.contains(ra.getCodePrestation())) {
                if (groupLevel > 2) {
                    groupLevel = 2;
                }
            } else if (REGenrePrestationEnum.groupe4.contains(ra.getCodePrestation())) {
                if (groupLevel > 4) {
                    groupLevel = 4;
                }
            } else if (REGenrePrestationEnum.groupe5.contains(ra.getCodePrestation())) {

                if (groupLevel > 5) {
                    groupLevel = 5;
                }
            }
        }
        return groupLevel;
    }

    /**
     * @param session
     * @param transaction
     * @param idsRA
     * @return
     * @throws Exception
     */
    public static int getGroupLevel(BSession session, BITransaction transaction, String idRA) throws Exception {
        /*
         * 1. les prestations 10, 13, 20, 23, 50, 70, 72 sont considérées comme le tri 1
         * 2. les prestations 81, 82, 83, 84, 85, 86, 87, 88, 91, 92, 93, 95, 96, 97 sont considérées comme le tri 2
         * 3. les prestations 33, 53, 73 sont considérées comme le tri 4
         * 4. les prestations 14, 15, 16, 24, 25, 26, 34, 35, 45, 54, 55, 74, 75 sont considérées comme le tri 5
         */

        // Récupération du bénéficiaire principal
        // Priorité au groupe 1, puis 2, 4 et 5

        int groupLevel = 100;

        RERenteAccordee ra = REBeneficiairePrincipal.retrieveRA(session, transaction, Long.parseLong(idRA));

        if (REGenrePrestationEnum.groupe1.contains(ra.getCodePrestation())) {
            groupLevel = 1;

        } else if (REGenrePrestationEnum.groupe2.contains(ra.getCodePrestation())) {
            if (groupLevel > 2) {
                groupLevel = 2;
            }
        } else if (REGenrePrestationEnum.groupe4.contains(ra.getCodePrestation())) {
            if (groupLevel > 4) {
                groupLevel = 4;
            }
        } else if (REGenrePrestationEnum.groupe5.contains(ra.getCodePrestation())) {

            if (groupLevel > 5) {
                groupLevel = 5;
            }
        }
        return groupLevel;
    }

    /**
     * Permet de determiner le groupe level du genre de rente en fonction du code prestation
     * 
     * @param codePrestation
     * @return le niveau
     */
    public static int determineGroupLevel(String codePrestation) {
        // Récupération du bénéficiaire principal
        // Priorité au groupe 1, puis 2, 4 et 5

        int groupLevel = 100;

        if (REGenrePrestationEnum.groupe1.contains(codePrestation)) {
            groupLevel = 1;

        } else if (REGenrePrestationEnum.groupe2.contains(codePrestation)) {
            if (groupLevel > 2) {
                groupLevel = 2;
            }
        } else if (REGenrePrestationEnum.groupe4.contains(codePrestation)) {
            if (groupLevel > 4) {
                groupLevel = 4;
            }
        } else if (REGenrePrestationEnum.groupe5.contains(codePrestation)) {

            if (groupLevel > 5) {
                groupLevel = 5;
            }
        }
        return groupLevel;
    }

    // Retourne la plus grande date de fin de toutes les RA passée en paramètres
    // null si pas de date de fin
    private static JADate getMaxDateFinRentesAccordees(BSession session, BITransaction transaction,
            Collection<Long> idsRA) throws Exception {

        StringBuffer sb = new StringBuffer();
        for (Iterator<Long> iterator = idsRA.iterator(); iterator.hasNext();) {
            Long idRA = iterator.next();
            sb.append(idRA);
            if (iterator.hasNext()) {
                sb.append(",");
            }

        }
        JADate maxDate = new JADate("01.01.1970");
        JADate currentDate = null;
        RERenteAccordeeManager mgr = new RERenteAccordeeManager();
        mgr.setSession(session);
        mgr.setForIdsRentesAccordees(sb.toString());
        mgr.find(transaction);

        JACalendar cal = new JACalendarGregorian();
        for (int i = 0; i < mgr.size(); i++) {
            RERenteAccordee ra = (RERenteAccordee) mgr.getEntity(i);
            // Pas de date de fin
            if (JadeStringUtil.isBlankOrZero(ra.getDateFinDroit())) {
                return null;
            }
            currentDate = new JADate(ra.getDateFinDroit());
            if (cal.compare(maxDate, currentDate) == JACalendar.COMPARE_FIRSTLOWER) {
                maxDate = currentDate;
            }
        }
        return maxDate;
    }

    /**
     * On référence le compte annexe des RA pour une même décision, par rapport au tiers dans le groupe le plus
     * prioritaire
     * 
     * @param session
     * @param transaction
     * @param demande
     * @param idsRA
     * @throws Exception
     */
    public static void initComptesAnnexesDesRentesAccordees(BSession session, BITransaction transaction, Set<Long> idsRA)
            throws Exception {

        /*
         * Etape 1 : on stocke l'idTiers du groupe prioritaire !!!!!
         */
        int _groupLevel = 100;
        String _idTiers = null;
        JADate _dateNaissance = null;

        JACalendar cal = new JACalendarGregorian();
        // Parcours de toutes les RA
        Set<Long> lst = null;

        String idTiersAssureDecede = null;

        for (Long idRA : idsRA) {
            lst = new HashSet<Long>();
            lst.add(idRA);

            int level = REBeneficiairePrincipal.getGroupLevel(session, transaction, lst);
            if (level <= _groupLevel) {

                RERenteAccordee ra = new RERenteAccordee();
                ra.setSession(session);
                ra.setIdPrestationAccordee(idRA.toString());
                ra.retrieve(transaction);
                PRAssert.notIsNew(ra, null);

                PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());

                // La personne est décédée... on ne la prend donc pas en compte pour le calcul !!!
                if (!JadeStringUtil.isBlankOrZero(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_DECES))) {
                    idTiersAssureDecede = ra.getIdTiersBeneficiaire();
                    continue;
                }

                String idTiers = ra.getIdTiersBeneficiaire();
                if (level < _groupLevel) {
                    if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                        _groupLevel = level;
                        _idTiers = idTiers;
                    }
                }
                // Dans le même groupe... on stocke le plus jeune!!!
                else {
                    if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                        String dn = tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE);
                        JADate dateNaissance = null;
                        if (!JadeStringUtil.isBlankOrZero(dn)) {
                            dateNaissance = new JADate(dn);

                            if ((_dateNaissance == null)
                                    || (cal.compare(dateNaissance, _dateNaissance) == JACalendar.COMPARE_FIRSTUPPER)) {
                                _groupLevel = level;
                                _idTiers = idTiers;
                                _dateNaissance = new JADate(
                                        PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dateNaissance.toStrAMJ()));
                            }
                        }
                    }
                }
            }
        }

        /*
         * Etape 2 : Création du compte annexe pour le tiers principal, et référencement dans toute les autres RA.
         */
        for (Long idRA : idsRA) {

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setIdPrestationAccordee(idRA.toString());
            ra.retrieve(transaction);
            PRAssert.notIsNew(ra, null);
            REInformationsComptabilite ic = ra.loadInformationsComptabilite();
            if (JadeStringUtil.isBlankOrZero(ic.getIdCompteAnnexe())) {

                // Dans le cas d'un assuré décédé sans aucun membre de la famille, _idTiers aura la valeur null.
                // Il faut dans ce cas créé le CA de l'assuré décédé.
                if (JadeStringUtil.isBlankOrZero(_idTiers) && !JadeStringUtil.isBlankOrZero(idTiersAssureDecede)) {
                    _idTiers = idTiersAssureDecede;
                }
                REInfoCompta.initCompteAnnexe_noCommit(session, transaction, _idTiers, ic,
                        IREValidationLevel.VALIDATION_LEVEL_ALL);
            }
        }
    }

    /**
     * Recherche le bénéficiaire principal d'une décision. La liste des RA
     * 
     * @return
     */
    public static REBeneficiairePrincipalVO retrieveBeneficiairePrincipal(BSession session, BITransaction transaction,
            Collection<Long> idsRA) throws Exception {
        /*
         * 1. les prestations 10, 13, 20, 23, 50, 70, 72 sont considérées comme le tri 1 2. les prestations 81, 82, 83,
         * 84, 85, 86, 87, 88, 91, 92, 93, 95, 96, 97 sont considérées comme le tri 2 3. les prestations 33, 53, 73 sont
         * considérées comme le tri 4 4. les prestations 14, 15, 16, 24, 25, 26, 34, 35, 45, 54, 55, 74, 75 sont
         * considérées comme le tri 5
         */

        // Récupération du bénéficiaire principal
        // Priorité au groupe 1, puis 2, 4 et 5

        Iterator<Long> iter2 = idsRA.iterator();
        int groupLevel = 100;

        REBeneficiairePrincipalVO bp = new REBeneficiairePrincipalVO();

        while (iter2.hasNext()) {
            Long idRA = iter2.next();
            RERenteAccordee ra = REBeneficiairePrincipal.retrieveRA(session, transaction, idRA);
            REInformationsComptabilite ic = new REInformationsComptabilite();
            ic.setSession(session);
            ic.setIdInfoCompta(ra.getIdInfoCompta());
            ic.retrieve(transaction);
            PRAssert.notIsNew(ic, null);

            if (REGenrePrestationEnum.groupe1.contains(ra.getCodePrestation())) {
                groupLevel = 1;

                bp.idTiersBeneficiairePrincipal = ra.getIdTiersBeneficiaire();
                bp.ra = ra;
                break;

            } else if (REGenrePrestationEnum.groupe2.contains(ra.getCodePrestation())) {
                if (groupLevel > 2) {
                    groupLevel = 2;

                    bp.idTiersBeneficiairePrincipal = ra.getIdTiersBeneficiaire();
                    bp.ra = ra;

                }
            } else if (REGenrePrestationEnum.groupe4.contains(ra.getCodePrestation())) {
                if (groupLevel > 4) {
                    groupLevel = 4;

                    bp.idTiersBeneficiairePrincipal = ra.getIdTiersBeneficiaire();
                    bp.ra = ra;

                }
            } else if (REGenrePrestationEnum.groupe5.contains(ra.getCodePrestation())) {

                // On garde le plus jeune du groupe 5.
                if (groupLevel >= 5) {
                    groupLevel = 5;
                    if (bp.dateNaissance == null) {
                        bp.idTiersBeneficiairePrincipal = ra.getIdTiersBeneficiaire();
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());
                        if (!JadeStringUtil.isBlankOrZero(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE))) {
                            bp.dateNaissance = new JADate(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
                        }
                        bp.ra = ra;
                    } else {
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());

                        JACalendar cal = new JACalendarGregorian();
                        if (!JadeStringUtil.isBlankOrZero(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE))) {
                            if (cal.compare(bp.dateNaissance,
                                    new JADate(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE))) == JACalendar.COMPARE_FIRSTLOWER) {
                                bp.dateNaissance = new JADate(tw.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE));
                                bp.ra = ra;
                                bp.idTiersBeneficiairePrincipal = ra.getIdTiersBeneficiaire();
                            }
                        }
                    }
                }
            }
        }

        /*
         * Règles :
         * 
         * Dem. 1 (père) | --> BC (père) | --> RA (père) --> RA (enfant)
         * 
         * 
         * Dem. 2 (mère) | --> BC (mère) | --> RA1 (mère) Adr. 1 --> RA2 (enfant) Adr. 1
         * 
         * --> BC (père) | --> RA3 (père) Adr. 2 --> RA4 (enfant) Adr. 1
         * 
         * 
         * 
         * Pour dem 2 : 1 Décision pour RA1 + RA2 sur le compta annexe de la mère Pour dem 2 : 1 Décision pour RA3 sur
         * le compta annexe du père Pour dem 2 : 1 Décision pour RA4 sur le compta annexe du père (RA4 est de niveau 4).
         * 
         * Si pas de bénéficiaire principale pour la décision de niveau 1 ou 2, récupérer tiers de la base de calcul.
         * Récupérer son adr. pmt. Si est la même que le bénéficiaire de la RA, on verse sur la CA du tiers de la BC.
         * Sinon, on verse sur le CA de la RA.
         * 
         * Dans ce cas, la RA4 serait versée sur le CA de l'enfant, car l'adr. de pmt du père est différente.
         * 
         * A faire valider par rje.
         * 
         * 
         * Autre exemple :
         * 
         * Dem. 1 (mère) | --> BC (mère) Ard.1 | --> RA (enfant) Adr.1
         * 
         * Dans ce cas, le bénéficiaire principal est la mère, car même adr. de pmt. Donc tout est versé sur le CA de la
         * mère.
         */

        /*
         * 
         * Algorythme de recherche du bénéficiaire principal
         * 
         * 
         * 
         * Si le bénéficaire principal ne fait pas partie du groupe 1 ou 2, le rechercherdans une autre demande.
         * 
         * 
         * ------------------------------------------------------------------------récuperer la RA en cours du tiers :
         * ra.idTierBC;Si pas trouvé, Exception;
         * 
         * Si adr. pmt de ra.idtierBC == adr. pmt 'bénef. principal trouvé' //Bénéf. principal = ra.idTierBCSinon //Si
         * bénéf. courant est dans grp 5 //récupérer le tiers du groupe 4 avec la même adresse de pmt que le 'bénéf.
         * courant'(l'enfant) //Si trouvé //bénéf. pmt est celui du grp 4 //Sinon //le bénéf. du pmt est le plus jeune
         * du groupe 5
         * 
         * //Sinon, bénéf. courant est dans grp 4, //c'est lui même
         * ------------------------------------------------------------------------
         */

        String listIdRA = "-1 ";
        for (Long idRA : idsRA) {
            listIdRA += ", " + idRA;
        }

        if (groupLevel > 2) {
            String idTiersBaseCalcul = bp.ra.getIdTiersBaseCalcul();
            RERenteAccordeeManager mgr = new RERenteAccordeeManager();
            mgr.setSession(session);
            mgr.setForIdsRentesAccordees(listIdRA);
            // Ce test est nécessaire, car si idTiersBaseCalcul est vide, le mgr retournera toute les RA.
            if (JadeStringUtil.isBlankOrZero(idTiersBaseCalcul)) {
                mgr.setForIdTiersBeneficiaire("-1");
            } else {
                mgr.setForIdTiersBeneficiaire(idTiersBaseCalcul);
            }

            mgr.setForEnCoursAtMois(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(JACalendar.todayJJsMMsAAAA()));
            mgr.find(transaction, 2);

            // Possible par exemple pour le cas d'une demande survivant, ou le père décédé
            // n'a pas de rente accordée. Dans ce cas, rechercher la RA pour un autre groupe.
            boolean pasDeRATrouvee = false;
            if (mgr.size() == 0) {
                pasDeRATrouvee = true;
            }

            if (mgr.size() > 1) {
                throw new Exception("Erreur : Plusieurs RA trouvée pour le tiers id = " + idTiersBaseCalcul);
            } else {
                String adrPmt1 = null;
                String adrPmt2 = null;
                RERenteAccordee ra = null;
                if (pasDeRATrouvee) {
                    // On force de test d'égalité sur les adr. a faux, pour continuer la recherche....
                    adrPmt1 = "aaaa";
                    adrPmt1 = "zzzz";
                }
                // On a trouve un RA
                else {
                    ra = (RERenteAccordee) mgr.getEntity(0);
                    adrPmt1 = ra.loadInformationsComptabilite().getIdTiersAdressePmt();
                    adrPmt2 = bp.ra.loadInformationsComptabilite().getIdTiersAdressePmt();
                }

                if (adrPmt1.equals(adrPmt2)) {
                    bp = new REBeneficiairePrincipalVO();
                    bp.ra = ra;
                    bp.idTiersBeneficiairePrincipal = ra.getIdTiersBeneficiaire();
                } else if (groupLevel == 5) {
                    // récupérer le tiers du groupe 4 avec la même adresse de pmt que le 'bénéf. courant'(l'enfant)
                    RERenteAccordeeManager mgr2 = new RERenteAccordeeManager();
                    mgr2.setSession(session);

                    // Ce test est nécessaire, car si idTiersBaseCalcul est vide, le mgr retournera toute les RA.
                    if (JadeStringUtil.isBlankOrZero(bp.ra.getIdTiersBaseCalcul())) {
                        mgr2.setForIdTiersBeneficiaire("-1");
                    } else {
                        mgr2.setForIdTiersBeneficiaire(bp.ra.getIdTiersBaseCalcul());
                    }

                    JADate maxDateFin = REBeneficiairePrincipal.getMaxDateFinRentesAccordees(session, transaction,
                            idsRA);
                    if (maxDateFin != null) {
                        // Rétro pure
                        mgr2.setForEnCoursAtMois(PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(maxDateFin.toStrAMJ()));
                    } else {
                        mgr2.setForEnCoursAtMois(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(JACalendar
                                .todayJJsMMsAAAA()));
                    }
                    mgr2.find(transaction, BManager.SIZE_NOLIMIT);
                    boolean found = false;
                    for (int i = 0; i < mgr2.size(); i++) {
                        RERenteAccordee elm = (RERenteAccordee) mgr2.getEntity(0);
                        // BZ 4420 On ajoute le groupe 1 dans le test.
                        if (REGenrePrestationEnum.groupe4.contains(elm.getCodePrestation())
                                || REGenrePrestationEnum.groupe1.contains(elm.getCodePrestation())) {
                            String adrPmt10 = bp.ra.loadInformationsComptabilite().getIdTiersAdressePmt();
                            String adrPmt20 = elm.loadInformationsComptabilite().getIdTiersAdressePmt();

                            if (adrPmt10.equals(adrPmt20)) {
                                bp.ra = elm;
                                bp.idTiersBeneficiairePrincipal = elm.getIdTiersBeneficiaire();
                                found = true;
                                break;
                            }
                        }

                    }
                    if (!found) {
                        // On garde le groupe 5, est automatiquement le plus jeune, selon traitement dans grp 5.
                        ;
                    }
                }
                // Est dans le groupe 4
                else {
                    // Est le bénéficiaire principal
                    ;
                }
            }
        }
        if (groupLevel == 100) {
            throw new Exception("Genre de prestation non répertorié ! idsRA = " + idsRA.toString());
        }
        return bp;

    }

    private static RERenteAccordee retrieveRA(BSession session, BITransaction transaction, Long idRA) throws Exception {

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(idRA.toString());
        ra.retrieve(transaction);
        PRAssert.notIsNew(ra, null);
        return ra;
    }

}
