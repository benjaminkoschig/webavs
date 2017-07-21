package globaz.hercule.service;

import globaz.aquila.api.ICOEtape;
import globaz.aquila.db.access.poursuite.COHistoriqueAffilie;
import globaz.aquila.db.access.poursuite.COHistoriqueAffilieManager;
import globaz.aquila.service.controleemployeur.COControleEmployeurService;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.globall.util.JATime;
import globaz.hercule.db.ICEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEAffilieControle;
import globaz.hercule.db.controleEmployeur.CEAffilieControleManager;
import globaz.hercule.db.controleEmployeur.CEAttributionPts;
import globaz.hercule.db.controleEmployeur.CEAttributionPtsCumulMasse;
import globaz.hercule.db.controleEmployeur.CEAttributionPtsCumulMasseManager;
import globaz.hercule.db.controleEmployeur.CEAttributionPtsManager;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.db.controleEmployeur.CECotisationAFManager;
import globaz.hercule.db.couverture.CECouverture;
import globaz.hercule.db.couverture.CECouvertureManager;
import globaz.hercule.db.declarationSalaire.CEDeclarationSalaire;
import globaz.hercule.db.declarationSalaire.CEDeclarationSalaireManager;
import globaz.hercule.db.groupement.CEGroupe;
import globaz.hercule.db.groupement.CEGroupeManager;
import globaz.hercule.db.groupement.CEMembre;
import globaz.hercule.db.groupement.CEMembreCouverture;
import globaz.hercule.db.groupement.CEMembreCouvertureManager;
import globaz.hercule.db.groupement.CEMembreGroupe;
import globaz.hercule.db.groupement.CEMembreGroupeManager;
import globaz.hercule.db.groupement.CEMembreManager;
import globaz.hercule.db.reviseur.CEReviseur;
import globaz.hercule.exception.HerculeException;
import globaz.hercule.utils.CEUtils;
import globaz.hercule.utils.CodeSystem;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.db.cotisation.AFCotisationManager;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SCO
 * @since 16 juin 2010
 */
public class CEControleEmployeurService {

    /**
     * Permet de compter le nombre de cotisation AF d'un affilie
     * 
     * @param session
     * @param transaction
     * @param numAffilie
     * @return
     * @throws HerculeException
     */
    public static boolean affilieHasCotisationAF(final BSession session, final BTransaction transaction,
            final String idAffilie) throws HerculeException {

        int nbCotisationAF = 0;

        CECotisationAFManager manager = new CECotisationAFManager();
        manager.setSession(session);
        manager.setIdAffilie(idAffilie);

        try {
            nbCotisationAF = manager.getCount(transaction);
        } catch (Exception e) {
            nbCotisationAF = 0;
        }

        return nbCotisationAF > 0 ? true : false;
    }

    /**
     * Permet le calcul de la couverture d'un affilié suivant sa catégorie de masse salariale et son nombre de points
     * issu de sa notation.
     * 
     * @param session
     *            Une session
     * @param affControle
     *            Le données d'un affilié
     * @param nbrPoints
     *            Le nombre de points (de 0 à 18)
     * @param categorie
     *            La catégorie de masse salariale (0, 1A, 1B, 2, ....)
     * @param annee
     *            L'annee du calcul
     * @param params
     *            Les parametres du tableau du calcul de la période de couverture
     */
    public static String calculCouverture(final BSession session, final BTransaction transaction,
            final CEAffilieControle affControle, final Integer nbrPoints, final String categorie,
            final double masseSalariale, final String annee, final Map<String, String> params,
            final int periode1Derogation) throws HerculeException, JAException {

        if (JadeStringUtil.isEmpty(categorie)) {
            throw new HerculeException("Unabled to calculate the couverture. categorie is null or empty");
        }

        if (affControle == null) {
            throw new HerculeException("Unabled to calculate the couverture. affControle is null");
        }

        if (session == null) {
            throw new HerculeException("Unabled to calculate the couverture. session is null");
        }

        String dateCouverture;

        if (!ICEControleEmployeur.CATEGORIE_MASSE_0.equals(categorie)
                && !ICEControleEmployeur.CATEGORIE_MASSE_1.equals(categorie)) {
            // Si masse salariale != 0 et != 1
            // => évaluation existante ?
            if (nbrPoints != null) {
                // OUI, on lance le recalcul suivant le tableau

                if (JadeStringUtil.isEmpty(affControle.getDateFinControle())) {
                    throw new HerculeException("Unabled to calculate the couverture. The affilie id : "
                            + affControle.getIdAffilie() + " have no ending date de controle");
                }

                int nbrAnnee = CEControleEmployeurService.calculPeriodeCouverture(nbrPoints, categorie, params,
                        periode1Derogation);
                dateCouverture = CEUtils.addAnneeToDate(affControle.getDateFinControle(), nbrAnnee);

                dateCouverture = CEControleEmployeurService.regleGestionAnneeCouverture(annee, dateCouverture);

            } else {
                // NON, on regarde si il y a une date de fin de controle
                if (JadeStringUtil.isEmpty(affControle.getDateFinControle())) {
                    // Oui, il n'y a pas d'année de fin de controle, donc
                    // controle immediat
                    dateCouverture = annee;
                } else {
                    // NON, il y a une date de fin de controle, donc ce sera
                    // l'année de fin de controle + 1er periode
                    int periode1 = (new FWCurrency(params.get(ICEControleEmployeur.PERIODE1))).intValue();
                    dateCouverture = CEUtils.addAnneeToDate(affControle.getDateFinControle(), periode1);
                    dateCouverture = CEControleEmployeurService.regleGestionAnneeCouverture(annee, dateCouverture);
                }
            }
        } else {
            // => annee de couverture présente ou > que l'annee en cours ?
            if (!JadeStringUtil.isEmpty(affControle.getAnneCouverture())
                    && (Integer.parseInt(affControle.getAnneCouverture()) > Integer.parseInt(annee))) {
                // OUI, on recalcule pas, on retourne l'année
                // affControle.getAnneCouverture()
                dateCouverture = affControle.getAnneCouverture();
            } else {
                // NON => masseSalriale < 100000
                if (masseSalariale < 100000.00) {

                    if (JadeStringUtil.isEmpty(affControle.getDateDebutAffiliation())) {
                        throw new HerculeException("Unabled to calculate the couverture. The affilie id : "
                                + affControle.getIdAffilie() + " have no begining date of affiliation");
                    }

                    // OUI, annee couverture =
                    // affControle.getDateDebutAffiliation() + 3
                    int anneCalcule = CEUtils.stringDateToAnnee(affControle.getDateDebutAffiliation()) + 3;

                    if (anneCalcule > Integer.parseInt(annee)) {
                        dateCouverture = Integer.toString(anneCalcule);
                    } else {
                        dateCouverture = annee;
                    }
                } else {
                    // NON, On crée un controle si et seulement si la date d'affiliation est supérieure auu 01.01.2008
                    try {
                        if (BSessionUtil.compareDateFirstGreaterOrEqual(session, affControle.getDateDebutAffiliation(),
                                "01.01.2008")) {

                            CEControleEmployeurService.createControle(session, transaction, affControle.getIdAffilie(),
                                    affControle.getNumeroAffilie(), affControle.getDateDebutAffiliation(), annee);
                            dateCouverture = Integer.toString(CEUtils.stringDateToAnnee(annee) + 4);
                        } else {
                            dateCouverture = annee;
                        }
                    } catch (Exception e) {
                        throw new HerculeException(e);
                    }
                }
            }
        }

        return dateCouverture;
    }

    /**
     * Permet le calcul de la couverture d'un affilié suivant sa catégorie de masse salariale et son nombre de points
     * issu de sa notation.
     * 
     * @param session
     *            Une session
     * @param affControle
     *            Le données d'un affilié
     * @param nbrPoints
     *            Le nombre de points (de 0 à 18)
     * @param categorie
     *            La catégorie de masse salariale (0, 1A, 1B, 2, ....)
     * @param annee
     *            L'annee du calcul
     * @param params
     *            Les parametres du tableau du calcul de la période de couverture
     */
    public static String calculCouvertureAfterAttributionPts(final BSession session, final BTransaction transaction,
            final CEAffilieControle affControle, final Integer nbrPoints, final String categorie, final String annee,
            final Map<String, String> params, final int periode1Derogation) throws HerculeException, JAException {

        if (JadeStringUtil.isEmpty(categorie)) {
            throw new HerculeException("Unabled to calculate the couverture. categorie is null or empty");
        }

        if (affControle == null) {
            throw new HerculeException("Unabled to calculate the couverture. affControle is null");
        }

        if (session == null) {
            throw new HerculeException("Unabled to calculate the couverture. session is null");
        }

        String dateCouverture = null;

        if (!ICEControleEmployeur.CATEGORIE_MASSE_0.equals(categorie)
                && !ICEControleEmployeur.CATEGORIE_MASSE_1.equals(categorie)) {
            // Si masse salariale != 0 et != 1A et != 1B
            // => évaluation existante ?
            if (nbrPoints != null) {
                // OUI, on lance le recalcul suivant le tableau

                if (JadeStringUtil.isEmpty(affControle.getDateFinControle())) {
                    throw new HerculeException("Unabled to calculate the couverture. The affilie id : "
                            + affControle.getIdAffilie() + " have no ending date de controle");
                }

                int nbrAnnee = CEControleEmployeurService.calculPeriodeCouverture(nbrPoints, categorie, params,
                        periode1Derogation);
                dateCouverture = CEUtils.addAnneeToDate(affControle.getDateFinControle(), nbrAnnee);

                dateCouverture = CEControleEmployeurService.regleGestionAnneeCouverture(annee, dateCouverture);

            } else {
                // NON, on regarde si il y a une date de fin de controle
                if (JadeStringUtil.isEmpty(affControle.getDateFinControle())) {
                    // Oui, il n'y a pas d'année de fin de controle, donc
                    // controle immediat
                    dateCouverture = annee;
                } else {
                    // NON, il y a une date de fin de controle, donc ce sera
                    // l'année de fin de controle + 1er periode
                    int periode1 = (new FWCurrency(params.get(ICEControleEmployeur.PERIODE1))).intValue();
                    dateCouverture = CEUtils.addAnneeToDate(affControle.getDateFinControle(), periode1);
                    dateCouverture = CEControleEmployeurService.regleGestionAnneeCouverture(annee, dateCouverture);
                }
            }
        } else {
            // On est en 0, 1A, 1B

            // On repousse l'année de couverture de 4 ans
            dateCouverture = CEUtils.addAnneeToDate(affControle.getDateFinControle(), 4);

            int int_anneeCouverture = Integer.parseInt(dateCouverture);
            int int_anneeCourante = Integer.parseInt(CEUtils.giveAnneeCourante());

            // Si l'année de couverture est plus petite que l'année courante, l'année courante devient la couverture
            if (int_anneeCouverture < int_anneeCourante) {
                dateCouverture = CEUtils.giveAnneeCourante();
            }
        }

        return dateCouverture;
    }

    /**
     * Permet le calcul de la couverture d'un affilié suivant sa catégorie de masse salariale et son nombre de points
     * issu de sa notation.
     * 
     * @param session
     *            Une session
     * @param affControle
     *            Le données d'un affilié
     * @param nbrPoints
     *            Le nombre de points (de 0 à 18)
     * @param categorie
     *            La catégorie de masse salariale (0, 1A, 1B, 2, ....)
     * @param annee
     *            L'annee du calcul
     * @param params
     *            Les parametres du tableau du calcul de la période de couverture
     * @throws HerculeException
     * @throws JAException
     */
    public static String calculCouvertureGlobal(final BSession session, final CEAffilieControle affControle,
            final Integer nbrPoints, final String categorie, final String annee, final Map<String, String> params,
            final int periode1Derogation) throws HerculeException, JAException {

        if (JadeStringUtil.isEmpty(categorie)) {
            throw new HerculeException("Unabled to calculate the couverture. categorie is null or empty");
        }

        if (affControle == null) {
            throw new HerculeException("Unabled to calculate the couverture. affControle is null");
        }

        if (session == null) {
            throw new HerculeException("Unabled to calculate the couverture. session is null");
        }

        String dateCouverture = null;

        if (!ICEControleEmployeur.CATEGORIE_MASSE_0.equals(categorie)
                && !ICEControleEmployeur.CATEGORIE_MASSE_1.equals(categorie)) {
            // Si masse salariale != 0 et != 1A et != 1B
            // => évaluation existante ?
            if (nbrPoints != null) {
                // OUI, on lance le recalcul suivant le tableau

                if (JadeStringUtil.isEmpty(affControle.getDateFinControle())) {
                    throw new HerculeException("Unabled to calcul the couverture. The affilie id : "
                            + affControle.getIdAffilie() + " have no date de fin de controle");
                }

                int nbrAnnee = CEControleEmployeurService.calculPeriodeCouverture(nbrPoints, categorie, params,
                        periode1Derogation);
                dateCouverture = CEUtils.addAnneeToDate(affControle.getDateFinControle(), nbrAnnee);

                dateCouverture = CEControleEmployeurService.regleGestionAnneeCouverture(annee, dateCouverture);

            } else {
                // NON, on regarde si il y a une date de fin de controle
                if (JadeStringUtil.isEmpty(affControle.getDateFinControle())) {
                    // Oui, il n'y a pas d'année de fin de controle, donc
                    // controle immediat
                    dateCouverture = annee;
                } else {
                    // NON, il y a une date de fin de controle, donc ce sera
                    // l'année de fin de controle + 1er periode
                    int periode1 = (new FWCurrency(params.get(ICEControleEmployeur.PERIODE1))).intValue();
                    dateCouverture = CEUtils.addAnneeToDate(affControle.getDateFinControle(), periode1);
                    dateCouverture = CEControleEmployeurService.regleGestionAnneeCouverture(annee, dateCouverture);
                }
            }
        } else {
            // Si masse salariale > 1B
            // => annee de couverture présente ou > que l'annee en cours ?
            if (!JadeStringUtil.isEmpty(affControle.getAnneCouverture())
                    && (Integer.parseInt(affControle.getAnneCouverture()) > Integer.parseInt(annee))) {
                // OUI, on recalcule pas, on retourne l'année
                // affControle.getAnneCouverture()
                dateCouverture = affControle.getAnneCouverture();
            } else {
                // NON
                dateCouverture = "2011";
            }
        }

        return dateCouverture;
    }

    /**
     * Calcul du nombre de point suivant les codes systemes des 4 notations.<br>
     * Si une note n'est pas renseigné, le nombre de points n'est pas calculé et on retourne null
     * 
     * @param session
     * @param derniereRevision
     * @param qualiteRH
     * @param collaboration
     * @param criteresEntreprise
     * @return
     */
    public static Integer calculNombrePoints(final BSession session, final String derniereRevision,
            final String qualiteRH, final String collaboration, final String criteresEntreprise) {

        // Si une note n'est pas renseigné, C'est qu'il n'y a pas eu encore de
        // notation
        if (JadeStringUtil.isEmpty(derniereRevision) || JadeStringUtil.isEmpty(qualiteRH)
                || JadeStringUtil.isEmpty(collaboration) || JadeStringUtil.isEmpty(criteresEntreprise)) {
            return null;
        }

        FWCurrency currency = new FWCurrency();

        // Ajout de la note de derniere révision
        currency.add(session.getCode(derniereRevision));
        // Ajout de la note de qualiteRH
        currency.add(session.getCode(qualiteRH));
        // Ajout de la note de collaboration
        currency.add(session.getCode(collaboration));
        // Ajout de la note de criteresEntreprise
        currency.add(session.getCode(criteresEntreprise));

        return new Integer(currency.intValue());
    }

    /**
     * Recherche dans les declaration de salaire si il y a des retards.
     * 
     * @param session
     * @param affControle
     * @param annee
     * @param anneePrecedente
     * @param collaborationM
     * @param collaborationC
     * @param collaborationTM
     * @return
     * @throws Exception
     */
    public static String calculNoteDeclarationSalaireEnRetard(final BSession session, final BTransaction transaction,
            final CEAffilieControle affControle, final String annee, final String collaborationM,
            final String collaborationC, final String collaborationTM) throws Exception {

        FWCurrency noteCollaboration = new FWCurrency("0");
        FWCurrency noteCollaborationMax = new FWCurrency(collaborationTM);
        String anneeDebut = "";

        if (!JadeStringUtil.isBlank(affControle.getDateFinControle())) {
            JADate dateFinControle = new JADate(affControle.getDateFinControle());
            anneeDebut = "" + dateFinControle.getYear();
        }

        // On regarde les declarations en retard
        CEDeclarationSalaireManager mgrDS = new CEDeclarationSalaireManager();
        mgrDS.setSession(session);
        mgrDS.setForAnneeDebut(anneeDebut);
        mgrDS.setForAnneeFin(annee);
        mgrDS.setLikeNumAffilie(affControle.getNumeroAffilie());
        mgrDS.setIsDeclarationSalaireOrTaxation(Boolean.TRUE);
        mgrDS.find(transaction, BManager.SIZE_NOLIMIT);

        for (int j = 0; j < mgrDS.size(); j++) {
            CEDeclarationSalaire ds = (CEDeclarationSalaire) mgrDS.getEntity(j);

            if (!JadeStringUtil.isEmpty(ds.getDateCreation())) {

                JADate dateCreation = new JADate(ds.getDateCreation());
                JADate dateReception = JACalendar.today();

                // Date de reception peut ne pas etre renseigné, on prend la
                // date du jour pour la référence
                if (!JadeStringUtil.isEmpty(ds.getDateReception())) {
                    dateReception = new JADate(ds.getDateReception());
                }

                JACalendar cal = new JACalendarGregorian();
                long days = cal.daysBetween(dateCreation, dateReception);

                if ((days > 60) && (days < 90)) {
                    noteCollaboration.add(collaborationC);
                } else if ((days > 90) && (days < 120)) {
                    noteCollaboration.add(collaborationM);
                } else if (days > 120) {
                    // note maximal
                    return collaborationTM;
                }

                // Si on atteint la note maximal, pas besoin de continuer
                if (noteCollaboration.intValue() >= noteCollaborationMax.intValue()) {
                    return collaborationTM;
                }
            }
        }

        return noteCollaboration.toString();
    }

    /**
     * Permet de savoir si l'affilié passée en parametre a une décision pénale.
     * 
     * @param session
     * @param transaction
     * @param affControle
     * @param annee
     * @param noteMaximal
     * @return
     * @throws Exception
     */
    public static String calculNotePourDecisionPenale(final BSession session, final BTransaction transaction,
            final CEAffilieControle affControle, final String annee, final String noteMaximal) throws Exception {

        String anneeDebut = "";

        if (!JadeStringUtil.isBlank(affControle.getDateFinControle())) {
            JADate dateFinControle = new JADate(affControle.getDateFinControle());
            anneeDebut = "" + dateFinControle.getYear();
        }

        // On regarde les declarations en retard
        CEDeclarationSalaireManager mgrDS = new CEDeclarationSalaireManager();
        mgrDS.setSession(session);
        mgrDS.setForAnneeDebut(anneeDebut);
        mgrDS.setForAnneeFin(annee);
        mgrDS.setLikeNumAffilie(affControle.getNumeroAffilie());
        mgrDS.setIsDeclarationSalaireOrTaxation(Boolean.FALSE);
        mgrDS.find(transaction, BManager.SIZE_USEDEFAULT);

        // si une taxation existe, la note est maximale, on retourne donc cette
        // note
        if (mgrDS.size() > 0) {
            return noteMaximal;
        }

        return "0";
    }

    /**
     * Calcul de la période de couverture (nombre d'année) du controle d'employeur.
     * 
     * @param session
     *            Une session
     * @param nbrPoints
     *            Le nombre de points (de 0 à 18)
     * @param categorie
     *            La catégorie de masse salariale (0, 1A, 1B, 2, ....)
     * @return
     */
    public static int calculPeriodeCouverture(final Integer nbrPoints, final String categorie,
            final Map<String, String> params, final int periode1Derogation) {

        int periode1 = (new FWCurrency(params.get(ICEControleEmployeur.PERIODE1))).intValue();
        int periode2 = (new FWCurrency(params.get(ICEControleEmployeur.PERIODE2))).intValue();
        int periode3 = (new FWCurrency(params.get(ICEControleEmployeur.PERIODE3))).intValue();

        int periodeCouverture = periode1;

        // Si la periode 1 est spécifié manuellement, on l'affecte
        if (periode1Derogation > 0) {
            periodeCouverture = periode1Derogation;
        }

        if (ICEControleEmployeur.CATEGORIE_MASSE_2.equals(categorie)) {
            // Si plus petit que 11 points, on ajoute le temps de la période 2
            // Si plus petit que 6 points, on ajoute le temps de la période 3
            if (nbrPoints.intValue() < 11) {
                periodeCouverture += periode2;
            }
            if (nbrPoints.intValue() < 6) {
                periodeCouverture += periode3;
            }

        } else if (ICEControleEmployeur.CATEGORIE_MASSE_3.equals(categorie)) {
            // Si plus petit que 7 points, on ajoute le temps de la période 2
            // Si plus petit que 3 points, on ajoute le temps de la période 3
            if (nbrPoints.intValue() < 7) {
                periodeCouverture += periode2;
            }
            if (nbrPoints.intValue() < 3) {
                periodeCouverture += periode3;
            }

        } else if (ICEControleEmployeur.CATEGORIE_MASSE_4.equals(categorie)) {
            // Dans tous les cas, on contrôle suivant la période 1
            periodeCouverture = periode1;

        }

        return periodeCouverture;
    }

    /**
     * Si l'année d'affiliation est l'année de la recherche de la masse, <BR>
     * on applique un coefficient pour apporter la somme sur l'année entière<BR>
     * Exemple : <BR>
     * - Si l'année = 2009, date de debut d'afficiliation = 05/2009 et masse = 300'000<BR>
     * Le calcul sera donc : 300'000 * 12 / 8<BR>
     * La masse sera donc de 450'000.-<BR>
     * 
     * @param masseSalariale
     * @param annee
     * @param anneeDebutAffiliation
     */
    private static double coefficiantAnneeAffiliation(final double masseSalariale, final String annee,
            final String anneeDebutAffiliation) {

        double masseSalarialeRecalcul = 0;
        int int_annee = CEUtils.transformeStringToInt(annee);

        try {
            JADate date = new JADate(anneeDebutAffiliation);

            if ((int_annee == date.getYear()) && (date.getMonth() > 1)) {

                masseSalarialeRecalcul = masseSalariale * (12.0 / (12.0 - date.getMonth() + 1.0));

            } else {
                masseSalarialeRecalcul = masseSalariale;
            }

        } catch (Exception e) {
            JadeLogger.warn("Technical Exception, unabled to calcul the coeficiant (anneeDebutAffiliation:"
                    + anneeDebutAffiliation + "/annee:" + annee + "/masseSalariale:" + masseSalariale + ")", e);
            masseSalarialeRecalcul = masseSalariale;
        }

        return masseSalarialeRecalcul;
    }

    /**
     * Retourne le nombre d'affilié qui sont dans un groupe donné
     * 
     * @param session
     * @param forIdGroupe
     * @return
     */
    public static int countMembreForIdGroupe(final BSession session, final String forIdGroupe) {

        int nbMembre = 0;

        CEMembreManager manager = new CEMembreManager();
        manager.setSession(session);
        manager.setForIdGroupe(forIdGroupe);

        try {

            manager.find(BManager.SIZE_NOLIMIT);
            nbMembre = manager.getCount();

        } catch (Exception e) {
            JadeLogger.error("Unabled to find all member for the id of groupe : " + forIdGroupe, e);
        }

        return nbMembre;
    }

    /**
     * Crée un controle employeur pour l'affilié donnée et prévu a l'année suivante celle passée en parametre
     * 
     * @param session
     * @param affControle
     * @return
     * @throws HerculeException
     * @throws Exception
     */
    public static boolean createControle(final BSession session, final BTransaction transaction,
            final String idAffiliation, final String numAffilie, final String dateAffiliation, final String annee)
            throws HerculeException {

        // REcherche d'un controle deja exitant avec ces parametres
        CEControleEmployeur controleExistant = null;

        try {
            controleExistant = CEUtils.rechercheDernierControleNonEffectuePrevu(session, transaction, idAffiliation,
                    annee);
        } catch (Exception e) {
            throw new HerculeException("Unabled to check if a controle employeur exist", e);
        }

        // Un controle existe déjà avec ces parametre, on sort de cette methode
        if (controleExistant != null) {
            return false;
        }

        // Création d'un controle employeur pour cet affilié
        CEControleEmployeur controle = new CEControleEmployeur();
        controle.setSession(session);

        int anneeSuivante = CEUtils.transformeStringToInt(annee) + 1;
        controle.setDatePrevue("01.01." + anneeSuivante); // 02.01. année +1

        controle.setGenreControle(CodeSystem.GENRE_CONTROLE_OBLIGATOIRE); // Controle
        // obligatoire
        // controle.setDateEffective(""); //aucune date
        controle.setDateDebutControle(dateAffiliation);// date de debut
        // d'affiliation
        controle.setDateFinControle("31.12." + annee); // 31.12. année
        controle.setNumAffilie(numAffilie);

        // On ajoute le controle
        try {
            controle.add(transaction);
        } catch (Exception e) {
            throw new HerculeException("Unabled to add new controle employeur", e);
        }

        return true;
    }

    /**
     * Permet de retourner l'annee de couverture active pour un numéro d'affilié donné.<br>
     * Retourne vide si pas trouvé
     * 
     * @param session
     *            Une session
     * @param numAffiliation
     *            Un numéro d'affilié
     * @return
     * @throws HerculeException
     */
    public static String findAnneeCouvertureActiveByNumAffilie(final BSession session, final String numAffiliation)
            throws HerculeException {

        CECouvertureManager manager = new CECouvertureManager();
        manager.setSession(session);
        manager.setForNumAffilie(numAffiliation);
        manager.setIsActif(Boolean.TRUE);

        try {
            manager.find(BManager.SIZE_USEDEFAULT);
        } catch (Exception e) {
            throw new HerculeException("Unabled to find the couverture (for numAffiliation : " + numAffiliation + ")",
                    e);
        }

        if (manager.size() > 0) {
            CECouverture couverture = (CECouverture) manager.getFirstEntity();
            return couverture.getAnnee();
        }

        return "";
    }

    /**
     * Récupération de la catégorie salariale suivant la masse salariale
     * 
     * @param masseSalariale
     * @return
     */
    public static String findCategorie(final double masseSalariale) {
        // Retour de la catégorie de la masse salariale
        if (masseSalariale <= 0.0) {
            return ICEControleEmployeur.CATEGORIE_MASSE_0;
        } else if ((masseSalariale > 0.0) && (masseSalariale < ICEControleEmployeur.CATEGORIE_MASSE_PALIER_0)) {
            // Masse comprise entre 0 et le palier 0
            return ICEControleEmployeur.CATEGORIE_MASSE_1;
        } else if ((masseSalariale >= ICEControleEmployeur.CATEGORIE_MASSE_PALIER_0)
                && (masseSalariale < ICEControleEmployeur.CATEGORIE_MASSE_PALIER_I)) {
            // Masse comprise entre le palier 0 (compris) et le palier 1
            return ICEControleEmployeur.CATEGORIE_MASSE_1;
        } else if ((masseSalariale >= ICEControleEmployeur.CATEGORIE_MASSE_PALIER_I)
                && (masseSalariale < ICEControleEmployeur.CATEGORIE_MASSE_PALIER_II)) {
            // Masse comprise entre le palier 1 (compris) et le palier 2
            return ICEControleEmployeur.CATEGORIE_MASSE_2;
        } else if ((masseSalariale >= ICEControleEmployeur.CATEGORIE_MASSE_PALIER_II)
                && (masseSalariale < ICEControleEmployeur.CATEGORIE_MASSE_PALIER_III)) {
            // Masse comprise entre le palier 2 (compris) et le palier 3
            return ICEControleEmployeur.CATEGORIE_MASSE_3;
        } else if (masseSalariale >= ICEControleEmployeur.CATEGORIE_MASSE_PALIER_III) {
            // Masse plus grand ou égale au palier 4
            return ICEControleEmployeur.CATEGORIE_MASSE_4;
        } else {
            return "";
        }

    }

    public static String findCategorieMasseForGenerationSuivi(final BSession session, final String numeroAffilie,
            final String annee, final String anneFinControle, final int anneeEnArriere) throws HerculeException {
        // SI null ou exception, on considere que la masse salariale vaut 0

        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new HerculeException("Unabled to find the categorie masse salariale. numeroAffilie is null or empty");
        }

        if (JadeStringUtil.isEmpty(annee)) {
            throw new HerculeException("Unabled to find the categorie masse salariale. annee is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to find the categorie masse salariale. session is null");
        }

        double masseSalariale = 0;
        double masseSalarialeRecherche = 0;
        int anneeDeDepart = 0;
        int int_annee = CEUtils.transformeStringToInt(annee);

        // On recherche la masse la plus élevé depuis les 4 dernieres années
        anneeDeDepart = int_annee - anneeEnArriere;

        // Si il y a un contrôle, et que la date de fin de contrôle est pluis récente,
        // On regarde depuis la date de fin de contrôle
        if (!JadeStringUtil.isIntegerEmpty(anneFinControle)) {
            int anneeFinDuControle = CEUtils.transformeStringToInt(anneFinControle);
            if (anneeDeDepart < anneeFinDuControle) {
                anneeDeDepart = anneeFinDuControle;
            }
        }

        for (int i = 0; i <= (int_annee - anneeDeDepart); i++) {

            int anneeDeRecherche = anneeDeDepart + i;

            masseSalarialeRecherche = CEControleEmployeurService.retrieveMasse(session, "" + anneeDeRecherche,
                    numeroAffilie);

            if (masseSalarialeRecherche > masseSalariale) {
                masseSalariale = masseSalarialeRecherche;
            }
        }

        // Retour de la catégorie de la masse salariale
        return CEControleEmployeurService.findCategorie(masseSalariale);
    }

    /**
     * Permet la récupération de la plus haute catégorie de masse salariale depuis le dernier controle ou lors des x
     * dernieres années
     * 
     * @param session
     * @param numeroAffilie
     * @param annee
     * @param anneFinControle
     * @param dateDebutAffiliation
     * @param anneeEnArriere
     * @return
     * @throws Exception
     */
    public static String findCategorieMasse(final BSession session, final String numeroAffilie, final String annee,
            final String anneFinControle, final int anneeEnArriere) throws HerculeException {

        double masseSalariale = findMasseSalariale(session, numeroAffilie, annee, anneFinControle, anneeEnArriere);

        // Retour de la catégorie de la masse salariale
        return CEControleEmployeurService.findCategorie(masseSalariale);
    }

    public static double findMasseSalariale(final BSession session, final String numeroAffilie, final String annee,
            final String anneFinControle, final int anneeEnArriere) throws HerculeException {

        // SI null ou exception, on considere que la masse salariale vaut 0

        if (JadeStringUtil.isEmpty(numeroAffilie)) {
            throw new HerculeException("Unabled to find the categorie masse salariale. numeroAffilie is null or empty");
        }

        if (JadeStringUtil.isEmpty(annee)) {
            throw new HerculeException("Unabled to find the categorie masse salariale. annee is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to find the categorie masse salariale. session is null");
        }

        double masseSalariale = 0;
        double masseSalarialeRecherche;
        int anneeDeDepart;
        int intAnnee = CEUtils.transformeStringToInt(annee);

        // On recherche la masse la plus élevé depuis la fin de controle ou
        // depuis les 4 dernieres années
        if (!JadeStringUtil.isIntegerEmpty(anneFinControle)) {
            anneeDeDepart = CEUtils.transformeStringToInt(anneFinControle);
        } else {
            anneeDeDepart = intAnnee - anneeEnArriere;
        }

        for (int i = 0; i <= (intAnnee - anneeDeDepart); i++) {

            int anneeDeRecherche = anneeDeDepart + i;

            masseSalarialeRecherche = CEControleEmployeurService.retrieveMasse(session, "" + anneeDeRecherche,
                    numeroAffilie);

            if (masseSalarialeRecherche > masseSalariale) {
                masseSalariale = masseSalarialeRecherche;
            }
        }

        return masseSalariale;
    }

    /**
     * Permet de retrouver la date d'impression d'un controle par son ID.
     * 
     * @param session
     * @param transaction
     * @param idControle
     * @return
     * @throws HerculeException
     */
    public static String findDateImpression(final BSession session, final BTransaction transaction,
            final String idControle) throws HerculeException {

        if (JadeStringUtil.isEmpty(idControle)) {
            throw new HerculeException("Unabled to find the date of impression. idControle is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to find the date of impression. session is null");
        }

        String dateImpression = null;

        CEControleEmployeur controle = new CEControleEmployeur();
        controle.setSession(session);
        controle.setIdControleEmployeur(idControle);

        try {
            controle.retrieve();

            if (controle.isNew()) {
                throw new HerculeException("This control does not exist ( " + idControle + " )");
            }

            dateImpression = controle.getDateImpression();

        } catch (Exception e) {
            throw new HerculeException("Unabled to retrieve the controle (idControle = " + idControle + " )", e);
        }

        return dateImpression;
    }

    /**
     * Permet de récupérer les informations d'un membre et de son groupe.
     * 
     * @param session
     * @param transaction
     * @param idAffilie
     * @return
     * @throws HerculeException
     */
    public static CEMembreGroupe findGroupeForIdAffilie(final BSession session, final BTransaction transaction,
            final String idAffilie) throws HerculeException {

        if (JadeStringUtil.isEmpty(idAffilie)) {
            throw new HerculeException("Unabled to find the id groupe. idAffilie is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to ffind the id groupe. session is null");
        }

        CEMembreGroupe membre = null;

        CEMembreGroupeManager manager = new CEMembreGroupeManager();
        manager.setSession(session);
        manager.setForIdAffiliation(idAffilie);

        try {
            manager.find(transaction, BManager.SIZE_USEDEFAULT);

            if (!manager.isEmpty()) {
                membre = (CEMembreGroupe) manager.getFirstEntity();
            }

        } catch (Exception e) {
            throw new HerculeException("Unabled to retrieve the CEGroupeForMembre", e);
        }

        return membre;
    }

    /**
     * Permet de retourner l'identifiant de l'occurence active pour un numéro d'affilié donné.<br>
     * Retourne vide si pas trouvé
     * 
     * @param session
     *            Une session
     * @param numAffiliation
     *            Un numéro d'affilié
     * @return
     * @throws HerculeException
     */
    public static String findIdCouvertureActiveByNumAffilie(final BSession session, final String numAffiliation)
            throws HerculeException {

        CECouvertureManager manager = new CECouvertureManager();
        manager.setSession(session);
        manager.setForNumAffilie(numAffiliation);
        manager.setIsActif(Boolean.TRUE);

        try {
            manager.find(BManager.SIZE_USEDEFAULT);
        } catch (Exception e) {
            throw new HerculeException("Unabled to find the couverture (for numAffiliation : " + numAffiliation + ")",
                    e);
        }

        if (manager.size() > 0) {
            CECouverture couverture = (CECouverture) manager.getFirstEntity();
            return couverture.getIdCouverture();
        }

        return "";
    }

    /**
     * Permet de récupérer l'identifaint d'un groupe par l'identifiant d'un de ses membres.
     * 
     * @param session
     * @param transaction
     * @param idAffilie
     * @return
     * @throws HerculeException
     */
    public static String findIdGroupeForIdAffilie(final BSession session, final BTransaction transaction,
            final String idAffilie) throws HerculeException {

        if (JadeStringUtil.isEmpty(idAffilie)) {
            throw new HerculeException("Unabled to find the id groupe. idAffilie is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to ffind the id groupe. session is null");
        }

        String idGroupe = null;

        CEMembreManager manager = new CEMembreManager();
        manager.setSession(session);
        manager.setForIdAffiliation(idAffilie);

        try {
            manager.find(transaction, BManager.SIZE_USEDEFAULT);

            if (!manager.isEmpty()) {
                CEMembre membre = (CEMembre) manager.getFirstEntity();
                idGroupe = membre.getIdGroupe();
            }

        } catch (Exception e) {
            throw new HerculeException("Unabled to retrieve the groupe", e);
        }

        return idGroupe;
    }

    /**
     * Permet de récupérer le motif du controle suivant l'id du controle passé en parametre
     * 
     * @param session
     * @param idControle
     * @return
     * @throws HerculeException
     */
    public static String findLibelleMotifControle(final BSession session, final String idControle,
            final String langueIso) throws HerculeException {

        if (JadeStringUtil.isEmpty(idControle)) {
            throw new HerculeException("Unabled to find the motif. idControle is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to find the motif. session is null");
        }

        String motif = "";

        CEControleEmployeur controle = new CEControleEmployeur();
        controle.setSession(session);
        controle.setIdControleEmployeur(idControle);

        try {
            controle.retrieve();

            if (controle.isNew()) {
                throw new HerculeException("This control does not exist ( " + idControle + " )");
            }

            motif = controle.getGenreControle();

        } catch (Exception e) {
            throw new HerculeException("Unabled to retrieve the controle (idControle = " + idControle + " )", e);
        }

        FWParametersUserCode userCode = new FWParametersUserCode();
        userCode.setSession(session);
        userCode.setIdCodeSysteme(motif);
        userCode.setIdLangue(langueIso);
        try {
            userCode.retrieve();
            if (!userCode.isNew()) {
                return userCode.getLibelle();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean hasAttributionPts(final BSession session, final String numAffilie, final String dateDebut,
            final String dateFin) throws HerculeException {

        CEAttributionPtsManager manager = new CEAttributionPtsManager();
        manager.setSession(session);
        manager.setForDateDebut(dateDebut);
        manager.setForDateFin(dateFin);
        manager.setForNumAffilie(numAffilie);

        try {
            manager.find(BManager.SIZE_USEDEFAULT);

            if (manager.size() >= 1) {
                return true;
            }

        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to find attribution point (dateDebut=" + dateDebut
                    + "/dateFin=" + dateFin + "/numAffilie=" + numAffilie + ")", e);
        }

        return false;
    }

    /**
     * Methode permettant de controler si un affilié a une cotisation AVS pour l'année spécifié.
     * 
     * @param session
     * @param idAffilie
     * @param annee
     * @return
     * @throws HerculeException
     */
    public static boolean hasCotisationAVS(final BSession session, final String idAffilie, final String annee)
            throws HerculeException {

        if (JadeStringUtil.isEmpty(idAffilie)) {
            throw new HerculeException("Unabled to find cotisation AVS. idAffilie is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to find cotisation AVS. session is null");
        }

        if (JadeStringUtil.isEmpty(annee)) {
            throw new HerculeException("Unabled to find cotisation AVS. annee is null or empty");
        }

        boolean hasCoti = false;

        AFCotisationManager manager = new AFCotisationManager();
        manager.setSession(session);
        manager.setForTypeAssurance(globaz.naos.translation.CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        manager.setForAnneeActive(annee);
        manager.setForAffiliationId(idAffilie);

        try {
            hasCoti = manager.getCount() > 0;
        } catch (Exception e) {
            throw new HerculeException("Unabled to retrieve cotisation avs (idAffilie = " + idAffilie + " / annee = "
                    + annee + ")", e);
        }

        return hasCoti;
    }

    /**
     * Permet de créer une map de tous les membres.
     * 
     * @param session
     *            Une session
     * @return Une map de membre (id, object membre)
     * @throws HerculeException
     */
    public static Map<String, CEMembre> loadMapMembreGroupe(final BSession session) throws HerculeException {

        CEMembreManager manager = new CEMembreManager();
        manager.setSession(session);

        try {
            manager.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e) {
            throw new HerculeException("Unabled to find all the membre", e);
        }

        Map<String, CEMembre> mapMembre = new HashMap<String, CEMembre>();

        for (int i = 0; i < manager.size(); i++) {
            CEMembre membre = (CEMembre) manager.getEntity(i);
            mapMembre.put(membre.getIdAffiliation(), membre);
        }

        return mapMembre;
    }

    /**
     * Permet de mettre a jour les controles qui ont le meme numero de rapport (les 9 premiers chiffres du rapport)
     * 
     * @param session
     * @param transaction
     * @param idControle
     * @param numAffilie
     * @param numRapport
     * @throws HerculeException
     */
    public static void majInactifControle(final BSession session, final BTransaction transaction,
            final String idControle, final String numAffilie, final String numRapport) throws HerculeException {

        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new HerculeException("Unabled to update the controle. numAffilie is null or empty");
        }

        if (JadeStringUtil.isEmpty(numRapport)) {
            throw new HerculeException("Unabled to update the controle. numRapport is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to update the controle. session is null");
        }

        String racineNumRapport = "";

        try {
            racineNumRapport = numRapport.substring(0, 10);
        } catch (Exception e) {
            throw new HerculeException("Unabled to substring the numero de rapport (numrapport = " + numRapport + ")",
                    e);
        }

        CEControleEmployeurManager contMana = new CEControleEmployeurManager();
        contMana.setSession(session);
        contMana.setForNumAffilie(numAffilie);
        contMana.setLikeNouveauNumRapport(racineNumRapport);
        contMana.setForNotId(idControle);

        try {
            contMana.find(transaction, BManager.SIZE_NOLIMIT);

            for (int i = 0; i < contMana.size(); i++) {

                CEControleEmployeur controle = (CEControleEmployeur) contMana.getFirstEntity();

                controle.setFlagDernierRapport(new Boolean(false));
                controle.wantCallMethodBefore(false);
                controle.update(transaction);
            }

        } catch (Exception e) {
            throw new HerculeException("Unabled to update controle Employeur (numAffilie = " + numAffilie
                    + " / idControle = " + idControle + ")", e);
        }
    }

    /**
     * Permet le recalcul de la note de collaboration
     * 
     * @param session
     *            La session
     * @param affControle
     *            Un object de type CEAffilieControle
     * @param annee
     *            L'annee du recalcul
     * @param anneePrecedente
     *            L'annee précédente du recalcul
     * @param collaborationM
     *            La note de collaborationM
     * @param collaborationC
     *            La note de collaborationC
     * @param collaborationTM
     *            La note de collaborationTM
     * @return
     * @throws Exception
     */
    public static String reCalculNotesCollaboration(final BSession session, final BTransaction transaction,
            final CEAffilieControle affControle, final String annee, final String anneePrecedente,
            final String collaborationM, final String collaborationC, final String collaborationTM) throws Exception {

        // *********************************************
        // Si la note est déjà maximale, on ne fait pas le recalcul de la note
        // de collaboration
        // *********************************************
        if (ICEControleEmployeur.COLLABORATION_TM.equals(affControle.getCollaboration())) {
            return ICEControleEmployeur.COLLABORATION_TM;
        }

        FWCurrency noteCollaboration = new FWCurrency("0");
        FWCurrency noteCollaborationMax = new FWCurrency(collaborationTM);

        // *********************************************
        // On controle le contentieux
        // *********************************************
        COHistoriqueAffilieManager mgrContentieux = COControleEmployeurService.searchHistoriqueAffilie(session,
                transaction, affControle.getNumeroAffilie(), affControle.getDateFinControle(), annee);

        for (int j = 0; j < mgrContentieux.size(); j++) {

            COHistoriqueAffilie contentieuxAffilie = (COHistoriqueAffilie) mgrContentieux.getEntity(j);

            if (contentieuxAffilie.getLibelleEtape().equals(ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_SANS_OPPOSITION)
                    || contentieuxAffilie.getLibelleEtape().equals(
                            ICOEtape.CS_COMMANDEMENT_DE_PAYER_SAISI_AVEC_OPPOSITION)) {
                // Commandement de payer : Note tres mauvaise (3 points)
                return ICEControleEmployeur.COLLABORATION_TM;

            } else if (contentieuxAffilie.getLibelleEtape().equals(ICOEtape.CS_SOMMATION_ENVOYEE)) {
                // Par Sommation : Note correcte (1 points)
                noteCollaboration.add(collaborationC);

            } else if (contentieuxAffilie.getLibelleEtape().equals(ICOEtape.CS_PREMIER_RAPPEL_ENVOYE)
                    || contentieuxAffilie.getLibelleEtape().equals(ICOEtape.CS_DEUXIEME_RAPPEL_ENVOYE)) {
                // Rappel : Note correcte (1 points)
                noteCollaboration.add(collaborationC);
            }

            // Si on atteint la note maximal, pas besoin de continuer
            if (noteCollaboration.intValue() >= noteCollaborationMax.intValue()) {
                return ICEControleEmployeur.COLLABORATION_TM;
            }
        }

        // *********************************************
        // On controle les déclarations de salaire.
        // *********************************************
        noteCollaboration.add(CEControleEmployeurService.calculNoteDeclarationSalaireEnRetard(session, transaction,
                affControle, annee, collaborationM, collaborationC, collaborationTM));

        // Si on atteint la note maximal, pas besoin de continuer
        if (noteCollaboration.intValue() >= noteCollaborationMax.intValue()) {
            return ICEControleEmployeur.COLLABORATION_TM;
        }

        // *********************************************
        // On regarde les taxations d'office.
        // *********************************************
        noteCollaboration.add(CEControleEmployeurService.calculNotePourDecisionPenale(session, transaction,
                affControle, annee, collaborationTM));

        if (noteCollaboration.intValue() == Integer.parseInt(collaborationM)) {
            return ICEControleEmployeur.COLLABORATION_M;
        } else if (noteCollaboration.intValue() >= Integer.parseInt(collaborationTM)) {
            return ICEControleEmployeur.COLLABORATION_TM;
        } else if (noteCollaboration.intValue() == Integer.parseInt(collaborationC)) {
            return ICEControleEmployeur.COLLABORATION_C;
        }

        return ICEControleEmployeur.COLLABORATION_B;
    }

    /**
     * Methode permettant de vérifié l'année de couverture selon les regles suivantes : <br>
     * - Un contrôle d'employeur peut être rapproché (pas en dessous de N+1, donc année de couverture =pas en dessous de
     * N) <br>
     * 
     * @param _anneeReference
     *            L'année de base (généralement l'année en cours)
     * @param _anneeCouvertureCalcule
     *            La nouvelle année calculée qui sera testé dans la méthode
     * @return
     */
    public static String regleGestionAnneeCouverture(final String _anneeReference, final String _anneeCouvertureCalcule)
            throws HerculeException {

        int anneeReference = 0;
        int anneeCouvertureCalcule = 0;

        try {

            anneeReference = Integer.parseInt(_anneeReference);
            anneeCouvertureCalcule = Integer.parseInt(_anneeCouvertureCalcule);

        } catch (Exception e) {
            throw new HerculeException("Unabled to apply rules of management. One argument is not valid");
        }

        if (anneeCouvertureCalcule <= anneeReference) {
            return _anneeReference;
        }

        return _anneeCouvertureCalcule;
    }

    /**
     * Permet de récupérer un controle grâce a son id
     * 
     * @param session
     * @param idControle
     * @return
     * @throws HerculeException
     */
    public static CEControleEmployeur retrieveControle(final BSession session, final String idControle)
            throws HerculeException {

        if (JadeStringUtil.isEmpty(idControle)) {
            throw new HerculeException("Unabled to find the controle. idControle is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to find the controle. session is null");
        }

        CEControleEmployeur controle = new CEControleEmployeur();
        controle.setSession(session);
        controle.setIdControleEmployeur(idControle);
        try {
            controle.retrieve();
        } catch (Exception e) {
            throw new HerculeException("Unabled to retrieve the controle", e);
        }

        return controle;
    }

    /**
     * Permet de récépérer l'année de couverture du groupe d'un affilié
     * 
     * @param session
     * @param idAffiliation
     * @return
     */
    public static String retrieveCouvertureGroupeWithNumAffilie(final BSession session, final String idAffiliation) {

        String anneeCouverture = "";

        try {

            CEMembreManager membreManager = new CEMembreManager();
            membreManager.setSession(session);
            membreManager.setForIdAffiliation(idAffiliation);
            membreManager.find(BManager.SIZE_USEDEFAULT);

            CEMembre membre = (CEMembre) membreManager.getFirstEntity();

            CEGroupeManager groupeManager = new CEGroupeManager();
            groupeManager.setSession(session);
            groupeManager.setForIdGroupe(membre.getIdGroupe());
            groupeManager.find(BManager.SIZE_USEDEFAULT);

            CEGroupe groupe = (CEGroupe) groupeManager.getFirstEntity();
            anneeCouverture = groupe.getAnneeCouvertureMinimal();

        } catch (Exception e) {
            JadeLogger.error("Unabled to retrieve the year of couverture for Affilie id " + idAffiliation, e);
        }

        return anneeCouverture;
    }

    /**
     * Permet de récupérer un Id de groupe suivant un libelle
     * 
     * @param session
     * @param forLibelle
     * @return
     */
    public static String retrieveIdGroupeWithLibelle(final BSession session, final String forLibelle) {

        String id = null;

        try {
            CEGroupeManager manager = new CEGroupeManager();
            manager.setSession(session);
            manager.setForLibelle(forLibelle);
            manager.find(BManager.SIZE_USEDEFAULT);

            if (!manager.isEmpty()) {
                CEGroupe groupe = (CEGroupe) manager.getFirstEntity();
                id = groupe.getIdGroupe();
            }

        } catch (Exception e) {
            JadeLogger.error("Unabled to retrieve the id of groupe for the libelle " + forLibelle, e);
        }

        return id;
    }

    /**
     * Methode de récupération de la masse salariale d'un affilié pour une année donnée
     * 
     * @param session
     *            La session
     * @param anneeMasse
     *            Année de recherche
     * @param numeroAffilie
     *            L'affilié
     * @return
     */
    public static double retrieveMasse(final BSession session, final String anneeMasse, final String numeroAffilie) {

        double masseSalariale = 0;

        CEAttributionPtsCumulMasseManager manager = new CEAttributionPtsCumulMasseManager();
        manager.setSession(session);
        manager.setForAnnee(anneeMasse);
        manager.setForNumAffilie(numeroAffilie);

        try {
            masseSalariale = manager.getSum(CEAttributionPtsCumulMasse.FIELD_CUMUL_MASSE).doubleValue();
        } catch (Exception e) {
            JadeLogger.warn("Technical Exception, unabled to sum the masse salariale (numAffilie:" + numeroAffilie
                    + "/annee:" + anneeMasse + ")", e);
            masseSalariale = 0;
            // Il se peut que cela ne retourne rien ou retourne une erreur car
            // il n'y a pas encore de compteur pour cette affilie
            // dans la comptabilite auxiliaire.
        }

        return masseSalariale;
    }

    /**
     * Récupération de la masse salariale annualisé d'un affilié pour une date donnée
     * 
     * @param session
     * @param anneeMasse
     * @param numeroAffilie
     * @param dateDebutAffiliation
     * @return
     * @throws JAException
     */
    public static double retrieveMasseAnnualisee(final BSession session, final String anneeMasse,
            final String numeroAffilie, final String dateDebutAffiliation) {

        double masseSalariale = CEControleEmployeurService.retrieveMasse(session, anneeMasse, numeroAffilie);

        // Si l'annee de la masse récupérée est celle de la date d'affiliation,
        // on annualise
        try {

            if (anneeMasse.equals("" + CEUtils.stringDateToAnnee(dateDebutAffiliation))) {
                masseSalariale = CEControleEmployeurService.coefficiantAnneeAffiliation(masseSalariale, anneeMasse,
                        dateDebutAffiliation);
            }

        } catch (JAException e) {
            JadeLogger.error("Unabled to get year of the debut affiliation " + dateDebutAffiliation, e);
        }

        return masseSalariale;
    }

    /**
     * Récupération de tous les parametres du tableau de calcul des périodes de couverture
     * 
     * @param session
     *            Une session
     * @param annee
     *            Une année
     * @return CEParamsTableauConteneur Un conteneur contenant les parametres du tableau
     * @throws HerculeException
     */
    public static Map<String, String> retrieveParamTableauPeriode(final BSession session, final String annee)
            throws HerculeException {

        Map<String, String> paramsTableau = new HashMap<String, String>();

        FWFindParameterManager params = new FWFindParameterManager();
        params.setSession(session);
        params.setIdApplParametre(session.getApplicationId());
        params.setIdActeurParametre("0");
        params.setPlageValDeParametre("0");
        params.setDateDebutValidite("01.01." + annee);

        // Periode 1
        params.setIdCodeSysteme("2000001");
        params.setIdCleDiffere(ICEControleEmployeur.PERIODE1);

        try {
            params.find(BManager.SIZE_USEDEFAULT);
        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve parameter of tableau periode (annee :"
                    + annee, e);
        }

        if (params.size() > 0) {
            paramsTableau.put(ICEControleEmployeur.PERIODE1,
                    ((FWFindParameter) params.getFirstEntity()).getValeurNumParametre());
        }

        // Periode 2
        params.setIdCodeSysteme("2000002");
        params.setIdCleDiffere(ICEControleEmployeur.PERIODE2);

        try {
            params.find(BManager.SIZE_USEDEFAULT);
        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve parameter of tableau periode (annee :"
                    + annee, e);
        }

        if (params.size() > 0) {
            paramsTableau.put(ICEControleEmployeur.PERIODE2,
                    ((FWFindParameter) params.getFirstEntity()).getValeurNumParametre());
        }

        // Periode 3
        params.setIdCodeSysteme("2000003");
        params.setIdCleDiffere(ICEControleEmployeur.PERIODE3);

        try {
            params.find(BManager.SIZE_USEDEFAULT);
        } catch (Exception e) {
            throw new HerculeException("Technical exception, unabled to retrieve parameter of tableau periode (annee :"
                    + annee, e);
        }

        if (params.size() > 0) {
            paramsTableau.put(ICEControleEmployeur.PERIODE3,
                    ((FWFindParameter) params.getFirstEntity()).getValeurNumParametre());
        }

        return paramsTableau;
    }

    /**
     * Permet de récupérer un reviseur par son visa
     * 
     * @param session
     * @param transaction
     * @param visaReviseur
     * @return
     * @throws HerculeException
     */
    public static CEReviseur retrieveReviseur(final BSession session, final BTransaction transaction,
            final String visaReviseur) throws HerculeException {

        if (JadeStringUtil.isEmpty(visaReviseur)) {
            throw new HerculeException("Unabled to find the reviseur. visaReviseur is null or empty");
        }

        if (session == null) {
            throw new HerculeException("Unabled to find the reviseur. session is null");
        }

        CEReviseur reviseur = new CEReviseur();
        reviseur.setAlternateKey(CEReviseur.AK_VISAREVISEUR);
        reviseur.setVisa(visaReviseur);
        reviseur.setSession(session);

        try {
            reviseur.retrieve(transaction);
        } catch (Exception e) {
            throw new HerculeException("Unabled to retrieve the reviseur", e);
        }

        if (reviseur.isNew()) {
            reviseur = null;
        }

        return reviseur;
    }

    /**
     * Mise a jour de lâttribution des points pour un affilié si la note de collaboration a changé ou le nombre de
     * points total<BR>
     * - Recupération des anciennes notes pour les mettres non actifs<BR>
     * - Creation d'une notation avec la note de collaboration et le nouveau nombre des points<BR>
     * 
     * @param session
     * @param idAncienAttrPts
     * @param noteCollaboration
     * @param nbrPoints
     * @throws HerculeException
     */
    public static void updateAttributionPoints(final BSession session, final BTransaction transaction,
            final CEAffilieControle affControle, final String noteCollaboration, final Integer nbrPoints)
            throws HerculeException {

        if (nbrPoints == null) {
            return;
        }

        int noteAffCollaboration = Integer.parseInt(affControle.getCollaboration());

        // Si il y a un changement, on fait la mise a jour, sinon non.
        if ((Integer.parseInt(noteCollaboration) != noteAffCollaboration)
                || (nbrPoints.intValue() != Integer.parseInt(affControle.getNbrePoints()))) {

            // Mise a jour de l'ancienne notation
            CEAttributionPts point = new CEAttributionPts();
            point.setSession(session);
            point.setIdAttributionPts(affControle.getIdAttributionPts());
            point.setLastUser(session.getUserId());
            JATime heure = new JATime(JACalendar.now());
            point.setLastModification(CEUtils.giveToday() + " / " + heure.toStr(":"));

            try {
                point.retrieve(transaction);

            } catch (Exception e) {
                throw new HerculeException("Unabled to retrieve the old attributionPts (idAttributionPts :  "
                        + affControle.getIdAttributionPts() + ")", e);
            }

            if (!point.isNew()) {

                try {
                    point.setIsAttributionActive(Boolean.FALSE);
                    point.update(transaction);

                } catch (Exception e) {
                    throw new HerculeException("Unabled to update the old attributionPts (idAttributionPts :  "
                            + affControle.getIdAttributionPts() + ")", e);
                }
            }

            // Création d'une nouvelle notation
            CEAttributionPts newPoint = (CEAttributionPts) point.clone();
            newPoint.setSession(session);
            newPoint.setNbrePoints(nbrPoints.toString());
            newPoint.setCollaboration(noteCollaboration);
            newPoint.setIsAttributionActive(Boolean.TRUE);
            newPoint.setLastUser(session.getUserId());
            newPoint.setLastModification(CEUtils.giveToday() + " / " + heure.toStr(":"));

            try {
                newPoint.add(transaction);

            } catch (Exception e) {
                throw new HerculeException("Unabled to add the new attributionPts", e);
            }
        }
    }

    /**
     * Permet la mise a jour de la note de collaboration ainsi que le nombre de points apres l'ajout d'une note manuelle
     * 
     * @param session
     * @param transaction
     * @param idAttributionPts
     * @param nbrPoints
     * @param noteCollaboration
     * @throws HerculeException
     */
    public static void updateAttributionPointsForAjoutNote(final BSession session, final BTransaction transaction,
            final String idAttributionPts, final String nbrPoints, final String noteCollaboration)
            throws HerculeException {

        // Mise a jour de la notation
        CEAttributionPts point = new CEAttributionPts();
        point.setSession(session);
        point.setIdAttributionPts(idAttributionPts);

        try {
            point.retrieve(transaction);

        } catch (Exception e) {
            throw new HerculeException("Unabled to retrieve the old attributionPts (idAttributionPts :  "
                    + idAttributionPts + ")", e);
        }

        if (!point.isNew()) {

            try {
                point.setCollaboration(noteCollaboration);
                point.setNbrePoints(nbrPoints);
                point.update(transaction);

            } catch (Exception e) {
                throw new HerculeException("Unabled to update the old attributionPts (idAttributionPts :  "
                        + idAttributionPts + ")", e);
            }
        }

    }

    /**
     * Methode de mise a jour de la couverture d'un affilié. <br>
     * Cette methode met en inactif l'ancienne couverture et en ajoute une nouvelle.<br>
     * Si l'affilié n'a pas d'ancienne couverture (nouveau affilié), l'id couverture ne sera pas renseigné (null ou 0)
     * 
     * @param session
     *            Une sessino
     * @param idCouverture
     *            Un identifiant de couverture (null ou 0 si non existant)
     * @param anneCouverture
     *            L'année de couverture
     * @param idAffilie
     *            Un identifiant d'un affilié
     * @throws HerculeException
     */
    public static void updateCouverture(final BSession session, final BTransaction transaction,
            final String idCouverture, final String anneCouverture, final String idAffilie, final String numAffilie)
            throws HerculeException {

        if (anneCouverture == null) {
            throw new HerculeException("Unabled to update the couverture. anneCouverture is null");
        }

        if (session == null) {
            throw new HerculeException("Unabled to update the couverture. session is null");
        }

        // Si un id couverture a été spécifié, cela signifie qu'on a une
        // ancienne couverture pour cette affilié
        // On doit le récupéré pour le mettre a jour (cad le mettre en inactif)
        if (!JadeStringUtil.isEmpty(idCouverture)) {
            CECouverture couverture = new CECouverture();
            couverture.setSession(session);
            couverture.setIdCouverture(idCouverture);

            try {
                couverture.retrieve(transaction);

            } catch (Exception e) {
                throw new HerculeException("Unabled to retrieve the old couverture (idCouverture : " + idCouverture
                        + ")", e);
            }

            couverture.setCouvertureActive(Boolean.FALSE);

            try {
                couverture.update(transaction);

            } catch (Exception e) {
                throw new HerculeException(
                        "Unabled to update the old couverture (idCouverture : " + idCouverture + ")", e);
            }
        }

        // Création d'une nouvelle couverture pour cet affilié
        CECouverture couverture = new CECouverture();
        couverture.setSession(session);
        couverture.setCouvertureActive(Boolean.TRUE);
        couverture.setIdAffilie(idAffilie);
        couverture.setNumAffilie(numAffilie);
        couverture.setAnnee(anneCouverture);

        try {
            couverture.add(transaction);

        } catch (Exception e) {
            throw new HerculeException("Unabled to add the new couverture (idAffilie : " + idCouverture + ", annee : "
                    + anneCouverture + ")", e);
        }

    }

    /**
     * Permet de vérifier l'année de couverture minimal pour un groupe et de la mettre a jour si besoin
     * 
     * @param session
     *            Une session
     * @param membre
     *            Un membre
     * @param annee
     *            Une année de référence
     */
    public static void updateDateCouvertureGroupe(final BSession session, final BTransaction transaction,
            final CEMembre membre, final String annee) throws HerculeException {

        // Récupération du groupe
        CEGroupe groupe = new CEGroupe();
        groupe.setSession(session);
        groupe.setIdGroupe(membre.getIdGroupe());

        try {
            groupe.retrieve(transaction);
        } catch (Exception e) {
            throw new HerculeException("Unabled to retrieve the groupe : (idGroupe:" + membre.getIdGroupe() + ")", e);
        }

        // Si le groupe existe
        if (!groupe.isNew()) {

            // Percours de tous les membres du groupe pour trouver la couverture minimal
            // Si la couverture minimal est inférieur a l'année courante, on garde l'année courante.
            CEMembreCouvertureManager manager = new CEMembreCouvertureManager();
            manager.setSession(session);
            manager.setForIdGroupe(membre.getIdGroupe());

            try {
                manager.find(transaction, BManager.SIZE_NOLIMIT);

            } catch (Exception e) {
                throw new HerculeException("Unabled to update the groupe : (idGroupe:" + membre.getIdGroupe() + ")", e);
            }

            int anneeCouvertureGroupe = CEUtils.transformeStringToInt(annee);
            Object[] listObj = manager.getContainer().toArray();

            for (Object obj : listObj) {
                CEMembreCouverture mbr = (CEMembreCouverture) obj;
                int anneeMembre = CEUtils.transformeStringToInt(mbr.getAnnee());

                if (anneeMembre < anneeCouvertureGroupe) {
                    anneeCouvertureGroupe = anneeMembre;
                }
            }

            // On compare avec l'année courante
            int anneeCourante = CEUtils.transformeStringToInt(CEUtils.giveAnneeCourante());
            if (anneeCouvertureGroupe < anneeCourante) {
                anneeCouvertureGroupe = anneeCourante;
            }

            // On met a jour le groupe avec la couverture la plus basse
            try {
                groupe.setAnneeCouvertureMinimal("" + anneeCouvertureGroupe);
                groupe.update(transaction);
            } catch (Exception e) {
                throw new HerculeException("Unabled to update the groupe : (idGroupe:" + membre.getIdGroupe() + ")", e);
            }
        }
    }

    /**
     * Permet la mise a jour de l'attribution des points ansi que de la couverture
     * 
     * @param session
     * @param transaction
     * @param idattributionPts
     * @throws HerculeException
     */
    public static void updateNoteAndCouverture(final BSession session, final BTransaction transaction,
            final String idControle, final String idattributionPts, final String derniereRevision,
            final String qualiteRH, final String criteresEntreprise) throws HerculeException {

        String anneeReference = CEUtils.giveAnneeCourante();

        Map<String, String> params = CEControleEmployeurService.retrieveParamTableauPeriode(session, anneeReference);
        String collaborationC = Integer
                .toString((new FWCurrency(session.getCode(ICEControleEmployeur.COLLABORATION_C))).intValue());
        String collaborationM = Integer
                .toString((new FWCurrency(session.getCode(ICEControleEmployeur.COLLABORATION_M))).intValue());
        String collaborationTM = Integer.toString((new FWCurrency(session
                .getCode(ICEControleEmployeur.COLLABORATION_TM))).intValue());

        CEAffilieControleManager manager = new CEAffilieControleManager();
        manager.setSession(session);
        manager.setForIdControle(idControle);
        manager.setForIdAttributionPts(idattributionPts);

        try {
            manager.find(transaction, BManager.SIZE_USEDEFAULT);

            if (manager.size() == 1) {

                // Récupération des donnles concernant la notation créée,
                // l'affilié et le controle
                CEAffilieControle affControle = (CEAffilieControle) manager.getFirstEntity();

                // Calcul de la note de collaboration
                String noteCollaboration = CEControleEmployeurService.reCalculNotesCollaboration(session, transaction,
                        affControle, anneeReference, CEUtils.getAnneePrecedente(anneeReference), collaborationM,
                        collaborationC, collaborationTM);

                // Calcul du nombre de point
                Integer nbrPoints = CEControleEmployeurService.calculNombrePoints(session, derniereRevision, qualiteRH,
                        noteCollaboration, criteresEntreprise);

                // Mise a jour de la note de collaboration et du nombre de
                // points
                CEControleEmployeurService.updateAttributionPointsForAjoutNote(session, transaction,
                        affControle.getIdAttributionPts(), nbrPoints.toString(), noteCollaboration);

                // Récupération de la catégorie de la masse salariale
                // ****************************************
                String anneeFinControle = null;
                if (!JadeStringUtil.isEmpty(affControle.getDateFinControle())) {
                    anneeFinControle = "" + CEUtils.stringDateToAnnee(affControle.getDateFinControle());
                }

                String categorie = CEControleEmployeurService
                        .findCategorieMasse(session, affControle.getNumeroAffilie(),
                                CEUtils.getAnneePrecedente(anneeReference), anneeFinControle, 3);

                // Recalcul de la période de couverture
                // ****************************************
                String anneeCouverture = CEControleEmployeurService.calculCouvertureAfterAttributionPts(session,
                        transaction, affControle, nbrPoints, categorie, anneeReference, params,
                        CEUtils.transformeStringToInt(affControle.getParticulariteDerogation()));

                // mise a jour de la couverture si changé
                if (JadeStringUtil.isEmpty(affControle.getAnneCouverture())
                        || !anneeCouverture.equals(affControle.getAnneCouverture())) {
                    CEControleEmployeurService.updateCouverture(session, transaction, affControle.getIdCouverture(),
                            anneeCouverture, affControle.getIdAffilie(), affControle.getNumeroAffilie());
                }

            } else {
                JadeLogger.warn(CEControleEmployeurService.class,
                        "Unabled to update note idControle couverture for idControle (" + idControle
                                + ")/ idAttributionPts (" + idattributionPts + ")");
            }

        } catch (Exception e) {
            throw new HerculeException("Unabled to update note idControle couverture for idControle (" + idControle
                    + ")/ idAttributionPts (" + idattributionPts + ")", e);
        }
    }

    /**
     * Constructeur de CEControleEmployeurService
     */
    protected CEControleEmployeurService() {
        throw new UnsupportedOperationException();
    }
}
