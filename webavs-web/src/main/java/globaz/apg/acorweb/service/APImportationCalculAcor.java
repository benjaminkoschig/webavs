package globaz.apg.acorweb.service;

import acor.apg.xsd.apg.out.*;
import ch.globaz.common.persistence.EntityService;
import ch.globaz.eavs.utils.StringUtils;
import globaz.apg.acor.parser.APACORPrestationsParser;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPG;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.module.calcul.*;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;
import globaz.apg.module.calcul.wrapper.APPeriodeWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapper;
import globaz.apg.module.calcul.wrapper.APPrestationWrapperComparator;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.PRAcorDomaineException;
import globaz.prestation.application.PRAbstractApplication;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCalcul;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
public class APImportationCalculAcor {

    private final EntityService entityService;

    public APImportationCalculAcor() {
        entityService = EntityService.of(BSessionUtil.getSessionFromThreadContext());
    }

    public void importCalculAcor(String idDroit, String genreService, FCalcul fCalcul) throws Exception {
        LOG.info("Importation des donn�es calcul�es.");
        // TODO WS ACOR APG IMPLEMENT IMPORT DU STYLE IJImportationCalculAcor.importCalculAcor ou REImportationCalculAcor.actionImporterScriptACOR
        APDroitAPG droit = entityService.load(APDroitAPG.class, idDroit);
        final List<APBaseCalcul> basesCalcul = getBasesCalcul(entityService.getSession(), droit);
        final Collection<APPrestationWrapper> pw = getWrappers(droit, entityService.getSession(), genreService, fCalcul, basesCalcul);
        final APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
        calculateur.reprendreDepuisACOR(entityService.getSession(), pw, droit, getFraisDeGarde(droit),basesCalcul);
    }

    private FWCurrency getFraisDeGarde(final APDroitAPG droit) throws Exception {
        return new FWCurrency(droit.loadSituationFamilliale().getFraisGarde());
    }

    private List getBasesCalcul(final BSession session, final APDroitLAPG droit) throws Exception {
        return APBasesCalculBuilder.of(session, droit).createBasesCalcul();
    }

    private SortedSet getWrappers(APDroitAPG droit, BSession session, String genreService, FCalcul fCalcul, List basesCalcul) throws PRACORException {

        try {
            HashMap employeurs = new HashMap();

            PRDemande demande = new PRDemande();
            demande.setSession(session);
            demande.setIdDemande(droit.getIdDemande());
            demande.retrieve();

            if (demande.isNew()) {
                throw new PRACORException("Demande prestation non trouv�e !!");
            }

            SortedSet wrappers = new TreeSet(new APPrestationWrapperComparator());
            APPrestationWrapper wrapper = new APPrestationWrapper();
            wrapper.setIdDroit(droit.getIdDroit());
            APResultatCalcul rc;
            APBaseCalcul baseCalcul;
            String line = null;

            FWCurrency montantTotalPrestation = null;
            // bug selon mail Mme Grosvernier 22.09.2010
            FWCurrency nbrTotalJourService = null;

            // r�cup�ration des frais de gardes
            // ---------------------------------------------------
            String nssAssureImporte = "";
            wrapper.setFraisGarde(new FWCurrency(fCalcul.getFraisGarde().getMontantGardeOctroye()));
            montantTotalPrestation = new FWCurrency(fCalcul.getCarteApg().getAllocTotaleCarteApg());
            nbrTotalJourService = new FWCurrency(fCalcul.getCarteApg().getSommeJoursService());
            nssAssureImporte = checkAndGetNssIntegrite(fCalcul, demande.getIdDemande());
            // TODO : R�cup�ration du nombres de jours suppl�mentaires impossible
            // TODO : Essayer de voir avec les m�tiers o� r�cup�rer cette information avec le m�tier.

            // p�riodes et r�sultats de calcul
            // ----------------------------------------------------
            int cmptPeriode = 0;
            for (PeriodeServiceApgType periodeServiceApgType :
                    fCalcul.getCarteApg().getPeriodeService()) {
                if (wrapper == null) {
                    wrapper = new APPrestationWrapper();
                }

                wrapper.setIdDroit(droit.getIdDroit());

                // dates de la p�riode
                APPeriodeWrapper periode = new APPeriodeWrapper();

                String ddd_importee = periodeServiceApgType.getDebut().toString();
                String dfd_importee = periodeServiceApgType.getFin().toString();

                // Contr�le que les donn�es import�es correspondent bien au
                // droit courant (controle sur le no avs).
                // Permet d'�viter que l'utilisateur click sur importer les
                // donn�es sans avoir pr�alablement calcul�e
                // les APG. Dans ce cas pr�cis, l'importation des donn�es se
                // fait avec le dernier calcul effectu� sur le poste de
                // l'utilisateur.
                String nssAssure = droit.loadDemande().loadTiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                if ((nssAssure != null) && (nssAssureImporte != null)) {
                    nssAssure = JadeStringUtil.removeChar(nssAssure, '.');
                    nssAssureImporte = JadeStringUtil.removeChar(nssAssureImporte, '.');
                    // TODO Le test nss1 �gal nss2 a �t� remplac� par le test
                    // nss1 se termine par nss2.
                    // Lorsqu�ACOR supportera le format � 13 positions, l'ancien
                    // test pourra �tre restaur�.
                    if (!nssAssure.endsWith(nssAssureImporte)) {
                        throw new PRACORException(session.getLabel("ERROR_NON_CONCORD_DONNEES"));
                    }
                }

                periode.setDateDebut(new JADate(ddd_importee));
                periode.setDateFin(new JADate(dfd_importee));
                wrapper.setPeriodeBaseCalcul(periode);

                // r�sultat de calcul
                rc = new APResultatCalcul();
                rc.setDateDebut(periode.getDateDebut()); // HACK: pour eviter
                // une
                // NullPointerException
                rc.setDateFin(periode.getDateFin());

                // copie des informations relatives a l'imposition qui ne sont
                // pas lues du fichier ACOR
                baseCalcul = findBaseCalcul(session, basesCalcul, periode.getDateDebut(),
                        periode.getDateFin());
                rc.setSoumisImpotSource(baseCalcul.isSoumisImpotSource());
                rc.setIdTauxImposition(baseCalcul.getIdTauxImposition());
                rc.setTauxImposition(baseCalcul.getTauxImposition());

                // on force le genre service car ACOR regroupe certains genres
                // et renvoie un code maison qui ne nous
                // aide pas
                rc.setTypeAllocation(session.getCode(droit.getGenreService()));

                // versement � l'assur�
                rc.setVersementAssure(new FWCurrency(periodeServiceApgType.getPartAssure()));

                // r�vision
                IAPReferenceDataPrestation ref;

                if (session.getCode(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE).equals(rc.getTypeAllocation())) {
                    ref = APReferenceDataParser.loadReferenceData(session, "MATERNITE", periode.getDateDebut(),
                            periode.getDateFin(), periode.getDateFin());
                } else {
                    ref = APReferenceDataParser.loadReferenceData(session, "APG", periode.getDateDebut(),
                            periode.getDateFin(), periode.getDateFin());
                }

                rc.setRevision(ref.getNoRevision());
                rc.setAllocationJournaliereMaxFraisGarde(ref.getMontantMaxFraisGarde());
                rc.setAllocationJournaliereExploitation(new FWCurrency(fCalcul.getPeriodeMontantJourn().get(cmptPeriode).getAllocJournExploitation()));

                FWCurrency mj = new FWCurrency(fCalcul.getPeriodeMontantJourn().get(cmptPeriode).getAllocJournBaseEnfants());
                rc.setMontantJournalier(mj);

                // R�cup�ration du basicDailyAmount
                // BZ 8329 : LGA 06.09.2013
                FWCurrency montantAllocJournPlafonnee = new FWCurrency(fCalcul.getPeriodeMontantJourn().get(cmptPeriode).getAllocJournBaseEnfants());
                rc.setBasicDailyAmount(montantAllocJournPlafonnee);
                // BZ 8329

                wrapper.setPrestationBase(rc);

                rc.setNombreJoursSoldes(periodeServiceApgType.getNbJours());
                // TODO : Chercher comment r�cup�rer les jours suppl�mentaires avec le m�tier
                // rc.setNombreJoursSupplementaires();
                // TODO : Chercher comment r�cup�rer le revenu d�terminant moyen
//                rc.setRevenuDeterminantMoyen(new FWCurrency(APACORPrestationsParser.getField(line, fields,
//                        "REVENU_JOURNALIER_MOYEN")));

                // situations professionnelles
//                for (PeriodeRepartitionEmployeurApgType periodeRepartitionEmployeur:
//                        fCalcul.getPeriodeMontantJourn().get(cmptPeriode).getPeriodeRepartition().get(0).getEmployeur()) {
//
//                    APResultatCalculSituationProfessionnel rcSitPro = new APResultatCalculSituationProfessionnel();
//                    String noAffilie = periodeRepartitionEmployeur.getIdEmpl();
//                    // Num�ro d'affili� trop long au valais (reformatage � la sortie)
//                    noAffilie = PRAbstractApplication.getAffileFormater().format(noAffilie);
//                    String nom = APACORPrestationsParser.getField(line, fields, "NOM_EMPLOYEUR");
//                    APACORPrestationsParser.EmployeurInfo info = APACORPrestationsParser.loadEmpInfo(session, employeurs, noAffilie, nom);
//
//                    // completer la sitpro
//                    rcSitPro.setIdAffilie(info.getIdAffilie());
//                    rcSitPro.setIdTiers(info.getIdTiers());
//                    rcSitPro.setNoAffilie(noAffilie);
//                    rcSitPro.setNom(nom);
//
//                    // montants
//                    String montant = APACORPrestationsParser.getField(line, fields, "PART_MONTANT_PERIODE");
//                    double salaireJ = PRCalcul.quotient(montant, String.valueOf(rc.getNombreJoursSoldes()));
//
//                    rcSitPro.setMontant(new FWCurrency(montant));
//
//                    /*
//                     * a ce niveau, il n'est plus important de savoir si l'employeur verse ou non un pourcentage du
//                     * salaire total de l'assur� pendant la p�riode car ACOR s'est deja charge de le prendre en compte.
//                     * On ne renseigne donc ici que le champ salaire journalier sans arrondi.
//                     */
//                    rcSitPro.setSalaireJournalierNonArrondi(new FWCurrency(salaireJ));
//
//                    // completer avec les bases de calcul
//                    APBaseCalculSituationProfessionnel bcSitPro = null;
//                    try {
//                        bcSitPro = APACORPrestationsParser.findBaseCalculSitPro(session, baseCalcul, info.getIdTiers(),
//                                info.getIdAffilie(), rcSitPro.getNom(), periode.getDateDebut(), periode.getDateFin());
//                    } catch (PRACORException e) {
//
//                        // Nouvelle tentative de recherche par #affilie pour les cas affili�s avec 2 affiliations sous
//                        // le m�me #.
//                        bcSitPro = APACORPrestationsParser.findBaseCalculSitProParNoAffilie(session, baseCalcul,
//                                info.getIdTiers(), info.getNoAffilie(), rcSitPro.getNom(), periode.getDateDebut(),
//                                periode.getDateFin(), idsSP);
//                        idsSP.add(bcSitPro.getIdSituationProfessionnelle());
//
//                    }
//                    rcSitPro.setVersementEmployeur(bcSitPro.isPaiementEmployeur());
//                    rcSitPro.setIndependant(bcSitPro.isIndependant());
//                    rcSitPro.setTravailleurSansEmployeur(bcSitPro.isTravailleurSansEmployeur());
//                    rcSitPro.setCollaborateurAgricole(bcSitPro.isCollaborateurAgricole());
//                    rcSitPro.setTravailleurAgricole(bcSitPro.isTravailleurAgricole());
//                    rcSitPro.setSoumisCotisation(bcSitPro.isSoumisCotisation());
//                    rcSitPro.setIdSituationProfessionnelle(bcSitPro.getIdSituationProfessionnelle());
//
//                    // TODO: r�cup�rer les cantons et taux d'imposition
//
//                    rc.addResultatCalculSitProfessionnelle(rcSitPro);
//                }

                // Ajouter dans la liste des wrappers les bases de calcul de
                // sit. prof. si non existante.
                // Dans certains cas, Il arrive que ACOR ne retourne pas
                // l'employeur si vers� � l'assur�. Ces employeurs
                // ne seront donc pas pris en compte lors du calcul de la
                // r�partition des pmts, pour le calcul des cotisations.

                // Il faut donc les rajouter.

//                if (basesCalcul != null) {
//
//                    for (Iterator iter = baseCalcul.getBasesCalculSituationProfessionnel().iterator(); iter.hasNext();) {
//                        APBaseCalculSituationProfessionnel bsp = (APBaseCalculSituationProfessionnel) iter.next();
//
//                        List lrcsp = rc.getResultatsCalculsSitProfessionnelle();
//
//                        boolean found = false;
//                        for (Iterator iter2 = lrcsp.iterator(); iter2.hasNext();) {
//                            APResultatCalculSituationProfessionnel rcsp = (APResultatCalculSituationProfessionnel) iter2
//                                    .next();
//
//                            String nomRCSP = rcsp.getNom();
//                            String nomBSP = bsp.getNom();
//                            try {
//
//                                if (nomBSP.startsWith(" ")) {
//                                    while (nomBSP.startsWith(" ")) {
//                                        nomBSP = nomBSP.substring(1, nomBSP.length() - 1);
//                                    }
//                                }
//
//                                if (nomRCSP.startsWith(" ")) {
//                                    while (nomRCSP.startsWith(" ")) {
//                                        nomRCSP = nomRCSP.substring(1, nomRCSP.length() - 1);
//                                    }
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                nomRCSP = rcsp.getNom();
//                                nomBSP = bsp.getNom();
//                            }
//
//                            /*
//                             * Regex qui remplace une chaine qui commence (^ <-- �� veut dire commence quoi;) par '['
//                             * suivi d'un minimum de un ou de plusieurs chiffres ([0-9] signifie les caract�res valide,
//                             * donc les chiffres, le '+' dit que �� doit correspondre au moins une fois) et qui est
//                             * suivit d'un ']' Si je n'est pas �t� assez claire :
//                             * http://en.wikipedia.org/wiki/Regular_expression saura r�pondre (RCO) BZ 8422
//                             */
//                            nomRCSP = nomRCSP.replaceFirst("^\\[[0-9]+\\]", "");
//                            if (rcsp.getIdTiers().equals(bsp.getIdTiers())
//                                    && (rcsp.getIdAffilie().equals(bsp.getIdAffilie()) || ((rcsp.getNoAffilie() != null) && rcsp
//                                    .getNoAffilie().equals(bsp.getNoAffilie())))
//
//                                    && (nomBSP.contains(nomRCSP) || nomRCSP.contains(nomBSP))) {
//                                found = true;
//                                break;
//                            } else {
//                                found = false;
//                            }
//                        }
//                        // La base de calcul n'existe pas dans la liste des
//                        // r�sultats du calcul retourn�e par ACOR,
//                        // on va donc la cr�er et la rajouter.
//                        if (!found) {
//                            APResultatCalculSituationProfessionnel newRcSitPro = new APResultatCalculSituationProfessionnel();
//                            // completer la sitpro
//                            newRcSitPro.setIdAffilie(bsp.getIdAffilie());
//                            newRcSitPro.setIdTiers(bsp.getIdTiers());
//                            newRcSitPro.setNom(bsp.getNom());
//
//                            // info Inconnue
//                            newRcSitPro.setMontant(new FWCurrency(0));
//                            newRcSitPro.setSalaireJournalierNonArrondi(new FWCurrency(0));
//
//                            // Devrait toujours �tre � false, car ACOR retourne
//                            // toujours l
//                            newRcSitPro.setVersementEmployeur(bsp.isPaiementEmployeur());
//                            newRcSitPro.setIndependant(bsp.isIndependant());
//                            newRcSitPro.setTravailleurSansEmployeur(bsp.isTravailleurSansEmployeur());
//                            newRcSitPro.setCollaborateurAgricole(bsp.isCollaborateurAgricole());
//                            newRcSitPro.setTravailleurAgricole(bsp.isTravailleurAgricole());
//                            newRcSitPro.setSoumisCotisation(bsp.isSoumisCotisation());
//                            newRcSitPro.setIdSituationProfessionnelle(bsp.getIdSituationProfessionnelle());
//
//                            rc.addResultatCalculSitProfessionnelle(newRcSitPro);
//                        }
//                    }
//                }

                wrapper.setPrestationBase(rc);
                wrappers.add(wrapper);
                wrapper = null;
            }

            // taux de participation des employeurs au RJM
            // ------------------------------------------------------------
//            fields = (HashMap) configs.get(APACORPrestationsParser.CODE_EMP_RJM);
//
//            while ((line != null) && !line.startsWith(APACORPrestationsParser.CODE_EMP_RJM)) {
//                // skipper jusqu'aux taux de participation des employeurs au rjm
//                line = bufferedReader.readLine();
//            }
//
//            if (line != null) {
//                do {
//                    String noAffilie = APACORPrestationsParser.getField(line, fields, "NUMERO_AFFILIE_EMPLOYEUR");
//                    // Num�ro d'affili� trop long au valais (reformatage � la sortie)
//                    noAffilie = PRAbstractApplication.getAffileFormater().format(noAffilie);
//                    APACORPrestationsParser.EmployeurInfo info = APACORPrestationsParser.loadEmpInfo(session, employeurs, noAffilie,
//                            APACORPrestationsParser.getField(line, fields, "NOM_EMPLOYEUR"));
//
//                    for (Iterator iter = wrappers.iterator(); iter.hasNext();) {
//                        wrapper = (APPrestationWrapper) iter.next();
//
//                        for (Iterator sitPros = wrapper.getPrestationBase().getResultatsCalculsSitProfessionnelle()
//                                .iterator(); sitPros.hasNext();) {
//                            APResultatCalculSituationProfessionnel sitPro = (APResultatCalculSituationProfessionnel) sitPros
//                                    .next();
//
//                            /*
//                             * Regex qui remplace une chaine qui commence (^ <-- �� veut dire commence quoi;) par '['
//                             * suivi d'un minimum de un ou de plusieurs chiffres ([0-9] signifie les caract�res valide,
//                             * donc les chiffres, le '+' dit que �� doit correspondre au moins une fois) et qui est
//                             * suivit d'un ']' Si je n'est pas �t� assez claire :
//                             * http://en.wikipedia.org/wiki/Regular_expression saura r�pondre (RCO) BZ 8422
//                             */
//                            info.setNom(info.getNom().replaceFirst("^\\[[0-9]+\\]", ""));
//                            sitPro.setNom(sitPro.getNom().replaceFirst("^\\[[0-9]+\\]", ""));
//
//                            if (info.getIdAffilie().equals(sitPro.getIdAffilie())
//                                    && info.getIdTiers().equals(sitPro.getIdTiers())
//                                    && info.getNom().equals(sitPro.getNom())) {
//
//                                FWCurrency taux = new FWCurrency(APACORPrestationsParser.getField(line, fields,
//                                        "TAUX_APPORT_RJM"), 4);
//                                sitPro.setTauxProRata(taux);
//
//                                // Il s'agit d'une situation profesionnelle
//                                // cr�er enti�rement � partir de la base de
//                                // calcul.
//                                // newRcSitPro on va donc y rajouter le montant
//                                // et salaire journalier en le recalculant �
//                                // partir
//                                // du montant total de la prestation au prorata.
//
//                                // Ceci est n�cessaire pour le calcul du montant
//                                // des cotisations, afin de determiner
//                                // si la part salariale est sup�rieure � la part
//                                // de l'ind�pendant, le cas �ch�ant.
//                                if ((sitPro.getSalaireJournalierNonArrondi() == null)
//                                        || JadeStringUtil.isBlankOrZero(sitPro.getSalaireJournalierNonArrondi()
//                                        .toString())) {
//                                    BigDecimal montant = (new BigDecimal(montantTotalPrestation.toString()))
//                                            .multiply(taux.getBigDecimalValue());
//
//                                    // double salaireJ =
//                                    // PRCalcul.quotient(montant.toString(),
//                                    // String.valueOf(wrapper.getPrestationBase().getNombreJoursSoldes()));
//                                    double salaireJ = PRCalcul.quotient(montant.toString(),
//                                            String.valueOf(nbrTotalJourService.intValue()));
//                                    sitPro.setMontant(new FWCurrency(montant.toString()));
//                                    sitPro.setSalaireJournalierNonArrondi(new FWCurrency(salaireJ));
//                                }
//
//                            }
//                        }
//                    }
//                } while (((line = bufferedReader.readLine()) != null)
//                        && line.startsWith(APACORPrestationsParser.CODE_EMP_RJM));
//            }

            return wrappers;
        } catch (PRACORException e) {
            throw e;
        } catch (Exception e) {
            throw new PRACORException("impossible de parser", e);
        }
    }

    private String checkAndGetNssIntegrite(FCalcul fCalcul, String idDemande) {

        String nss = "";
        PRTiersWrapper tiers = loadTiers(idDemande);
        if(tiers == null){
            throw new PRAcorDomaineException("R�ponse invalide : Impossible de charger le tiers correspondant au requ�rant. ");
        }
        // R�cup�re le NSS du FCalcul re�u d'ACOR
        for (AssureApgType assure :
                fCalcul.getAssure()) {
            if ("req".equals(assure.getFonction())) {
                nss = assure.getId();
                break;
            }
        }
        if (StringUtils.isBlank(nss)) {
            throw new PRAcorDomaineException("R�ponse invalide : Impossible de retrouver le NSS du requ�rant. ");
        }
        if (nss.equals(tiers.getNSS())) {
            throw new PRAcorDomaineException(entityService.getSession().getLabel("IMPORTATION_MAUVAIS_PRONONCE") + " (8)");
        }
        return nss;
    }

    private PRTiersWrapper loadTiers(String idDemande) {
        PRDemande demande = entityService.load(PRDemande.class, idDemande);
        PRTiersWrapper tiers = null;
        try {
            if (demande != null) {
                tiers = demande.loadTiers();
            }
        }catch(Exception e){
            throw new PRAcorDomaineException("R�ponse invalide : Impossible de charger le tiers correspondant au requ�rant. ");
        }
        return tiers;
    }

    private static APBaseCalcul findBaseCalcul(BSession session, List<APBaseCalcul> basesCalcul, JADate dateDebutPeriodeAcor, JADate dateFinPeriodeAcor)
            throws PRACORException {
        APBaseCalcul retValue = null;

        for (Iterator iter = basesCalcul.iterator(); iter.hasNext();) {
            retValue = (APBaseCalcul) iter.next();

            try {
                if ((BSessionUtil.compareDateFirstLowerOrEqual(session, retValue.getDateDebut().toString(),
                        dateDebutPeriodeAcor.toString()))
                        && (BSessionUtil.compareDateFirstGreaterOrEqual(session, retValue.getDateFin().toString(),
                        dateFinPeriodeAcor.toString()))) {
                    break; // sortir de la boucle
                }
            } catch (Exception e) {
                throw new PRACORException("comparaison de dates impossibles", e);
            }

            retValue = null; // on a pas trouve donc on va retourner null et non
            // pas la derniere base de calcul
        }

        if(retValue==null && basesCalcul.stream().anyMatch(APBaseCalcul::isExtension)){
            for (Iterator iter = basesCalcul.iterator(); iter.hasNext();) {
                retValue = (APBaseCalcul) iter.next();

                try {
                    if(BSessionUtil.compareDateFirstLowerOrEqual(session, dateDebutPeriodeAcor.toString(), retValue.getDateDebut().toString())
                            && (BSessionUtil.compareDateFirstGreaterOrEqual(session, dateFinPeriodeAcor.toString(),  retValue.getDateFin().toString()))){
                        break; // sortir de la boucle
                    }
                } catch (Exception e) {
                    throw new PRACORException("comparaison de dates impossibles", e);
                }

                retValue = null; // on a pas trouve donc on va retourner null et non
                // pas la derniere base de calcul

            }
        }

        return retValue;
    }
}
