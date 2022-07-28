package globaz.apg.acorweb.service;

import acor.apg.xsd.apg.out.FCalcul;
import ch.globaz.common.persistence.EntityService;
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
        LOG.info("Importation des données calculées.");
        // TODO WS ACOR APG IMPLEMENT IMPORT DU STYLE IJImportationCalculAcor.importCalculAcor ou REImportationCalculAcor.actionImporterScriptACOR
        APDroitAPG droit = entityService.load(APDroitAPG.class, idDroit);
        final List<APBaseCalcul> basesCalcul = getBasesCalcul(entityService.getSession(), droit);
        final Collection<APPrestationWrapper> pw = getWrappers(droit, entityService.getSession(), genreService, fCalcul);
        final APCalculateurPrestationStandardLamatAcmAlpha calculateur = new APCalculateurPrestationStandardLamatAcmAlpha();
        calculateur.reprendreDepuisACOR(entityService.getSession(), pw, droit, getFraisDeGarde(droit),basesCalcul);
    }

    private FWCurrency getFraisDeGarde(final APDroitAPG droit) throws Exception {
        return new FWCurrency(droit.loadSituationFamilliale().getFraisGarde());
    }

    private List getBasesCalcul(final BSession session, final APDroitLAPG droit) throws Exception {
        return APBasesCalculBuilder.of(session, droit).createBasesCalcul();
    }

    private SortedSet getWrappers(APDroitAPG droit, BSession session, String genreService, FCalcul fCalcul){

//        try {
//            PRDemande demande = new PRDemande();
//            demande.setSession(session);
//            demande.setIdDemande(droit.getIdDemande());
//            demande.retrieve();
//
//            if (demande.isNew()) {
//                throw new PRACORException("Demande prestation non trouvée !!");
//            }
//
//            SortedSet wrappers = new TreeSet(new APPrestationWrapperComparator());
//            APPrestationWrapper wrapper = new APPrestationWrapper();
//            APResultatCalcul rc;
//            APBaseCalcul baseCalcul;
//            String line = null;
//
//            FWCurrency montantTotalPrestation = null;
//            // bug selon mail Mme Grosvernier 22.09.2010
//            FWCurrency nbrTotalJourService = null;
//
//            // récupération des frais de gardes
//            // ---------------------------------------------------
//            String nssAssureImporte = "";
//            int nombreJoursSupplementaires = 0;
//            while ((line = bufferedReader.readLine()) != null) {
//                if (line.startsWith(APACORPrestationsParser.CODE_CARTE)) {
//                    wrapper.setFraisGarde(new FWCurrency(APACORPrestationsParser.getField(line, fields,
//                            "MONTANT_FRAIS_GARDE")));
//                    montantTotalPrestation = new FWCurrency(APACORPrestationsParser.getField(line, fields,
//                            "MONTANT_TOTAL_CARTE_APG"));
//                    try {
//                        nbrTotalJourService = new FWCurrency(APACORPrestationsParser.getField(line, fields,
//                                "NOMBRE_TOTAL_JOURS_SERVICE"));
//                    } catch (Exception e) {
//                        nbrTotalJourService = new FWCurrency(1);
//                    }
//
//                    nssAssureImporte = APACORPrestationsParser.getField(line, fields, "NUMERO_AVS_ASSURE");
//                    nombreJoursSupplementaires = Integer.parseInt(APACORPrestationsParser.getField(line, fields, "NOMBRE_JOURS_SUPPLEMENTAIRES"));
//                    break;
//                }
//            }
//            //wrapper.setFraisGarde(fCalcul.getInHost().getInHostCalcul().get);
//
//            // récupération des employeurs et de leurs taux d'impositions
//            // -------------------------
//            boolean aucunEmployeur = false;
//
//            fields = (HashMap) configs.get(APACORPrestationsParser.CODE_EMPLOYEURS);
//
//            do {
//                // skipper les lignes eventuelles jusqu'aux employeurs
//                line = bufferedReader.readLine();
//                aucunEmployeur = (line != null) && line.startsWith(APACORPrestationsParser.CODE_PERIODE);
//            } while ((line != null) && !line.startsWith(APACORPrestationsParser.CODE_EMPLOYEURS) && !aucunEmployeur);
//
//            if (!aucunEmployeur) {
//                do {
//                    String noAffilie = APACORPrestationsParser.getField(line, fields, "NUMERO_AFFILIE_EMPLOYEUR");
//                    // Numéro d'affilié trop long au valais (reformatage à la sortie)
//                    noAffilie = PRAbstractApplication.getAffileFormater().format(noAffilie);
//                    String nom = APACORPrestationsParser.getField(line, fields, "NOM_EMPLOYEUR");
//
//                    APACORPrestationsParser.createEmpInfo(session, employeurs, noAffilie, nom,
//                            APACORPrestationsParser.getField(line, fields, "CANTON_IMPOT"));
//                } while (((line = bufferedReader.readLine()) != null)
//                        && line.startsWith(APACORPrestationsParser.CODE_EMPLOYEURS));
//            }
//
//            // périodes et résultats de calcul
//            // ----------------------------------------------------
//            while ((line != null) && !line.startsWith(APACORPrestationsParser.CODE_PERIODE)) {
//                // skipper jusqu'aux périodes
//                line = bufferedReader.readLine();
//            }
//
//            do {
//                // périodes
//                fields = (HashMap) configs.get(APACORPrestationsParser.CODE_PERIODE);
//
//                if (wrapper == null) {
//                    wrapper = new APPrestationWrapper();
//                }
//
//                wrapper.setIdDroit(droit.getIdDroit());
//
//                // dates de la période
//                APPeriodeWrapper periode = new APPeriodeWrapper();
//
//                String ddd_importee = APACORPrestationsParser.getField(line, fields, "DEBUT_PERIODE_SERVICE");
//                String dfd_importee = APACORPrestationsParser.getField(line, fields, "FIN_PERIODE_SERVICE");
//
//                // Contrôle que les données importées correspondent bien au
//                // droit courant (controle sur le no avs).
//                // Permet d'éviter que l'utilisateur click sur importer les
//                // données sans avoir préalablement calculée
//                // les APG. Dans ce cas précis, l'importation des données se
//                // fait avec le dernier calcul effectué sur le poste de
//                // l'utilisateur.
//                String nssAssure = droit.loadDemande().loadTiers().getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);
//
//                if ((nssAssure != null) && (nssAssureImporte != null)) {
//                    nssAssure = JadeStringUtil.removeChar(nssAssure, '.');
//                    nssAssureImporte = JadeStringUtil.removeChar(nssAssureImporte, '.');
//                    // TODO Le test nss1 égal nss2 a été remplacé par le test
//                    // nss1 se termine par nss2.
//                    // Lorsqu’ACOR supportera le format à 13 positions, l'ancien
//                    // test pourra être restauré.
//                    if (!nssAssure.endsWith(nssAssureImporte)) {
//                        throw new PRACORException(session.getLabel("ERROR_NON_CONCORD_DONNEES"));
//                    }
//                }
//
//                periode.setDateDebut(new JADate(ddd_importee));
//                periode.setDateFin(new JADate(dfd_importee));
//                wrapper.setPeriodeBaseCalcul(periode);
//
//                // résultat de calcul
//                rc = new APResultatCalcul();
//                rc.setDateDebut(periode.getDateDebut()); // HACK: pour eviter
//                // une
//                // NullPointerException
//                rc.setDateFin(periode.getDateFin());
//
//                // copie des informations relatives a l'imposition qui ne sont
//                // pas lues du fichier ACOR
//                baseCalcul = APACORPrestationsParser.findBaseCalcul(session, basesCalcul, periode.getDateDebut(),
//                        periode.getDateFin());
//                rc.setSoumisImpotSource(baseCalcul.isSoumisImpotSource());
//                rc.setIdTauxImposition(baseCalcul.getIdTauxImposition());
//                rc.setTauxImposition(baseCalcul.getTauxImposition());
//
//                // on force le genre service car ACOR regroupe certains genres
//                // et renvoie un code maison qui ne nous
//                // aide pas
//                rc.setTypeAllocation(session.getCode(droit.getGenreService()));
//
//                // versement à l'assuré
//                String versementAssure = APACORPrestationsParser.getField(line, fields, "PART_MONTANT_PERIODE");
//
//                if (!JadeStringUtil.isDecimalEmpty(versementAssure)) {
//                    rc.setVersementAssure(new FWCurrency(versementAssure));
//                }
//
//                // révision
//                IAPReferenceDataPrestation ref;
//
//                if (session.getCode(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE).equals(rc.getTypeAllocation())) {
//                    ref = APReferenceDataParser.loadReferenceData(session, "MATERNITE", periode.getDateDebut(),
//                            periode.getDateFin(), periode.getDateFin());
//                } else {
//                    ref = APReferenceDataParser.loadReferenceData(session, "APG", periode.getDateDebut(),
//                            periode.getDateFin(), periode.getDateFin());
//                }
//
//                rc.setRevision(ref.getNoRevision());
//                rc.setAllocationJournaliereMaxFraisGarde(ref.getMontantMaxFraisGarde());
//                rc.setAllocationJournaliereExploitation(new FWCurrency(APACORPrestationsParser.getField(line, fields,
//                        "MONTANT_ALLOCATION_EXPLOITATION_JOUR")));
//
//                FWCurrency mj = new FWCurrency(APACORPrestationsParser.getField(line, fields,
//                        "ALLOC_BASE_PLUS_ENFANTS_JOURN"));
//                rc.setMontantJournalier(mj);
//
//                // Récupération du basicDailyAmount
//                // BZ 8329 : LGA 06.09.2013
//                FWCurrency montantAllocJournPlafonnee = new FWCurrency(APACORPrestationsParser.getField(line, fields,
//                        "ALLOC_BASE_PLUS_ENFANTS_JOURN"));
//                rc.setBasicDailyAmount(montantAllocJournPlafonnee);
//                // BZ 8329
//
//                wrapper.setPrestationBase(rc);
//
//                rc.setNombreJoursSoldes(Integer.parseInt(APACORPrestationsParser.getField(line, fields, "NOMBRE_JOURS")));
//                rc.setNombreJoursSupplementaires(nombreJoursSupplementaires);
//                rc.setRevenuDeterminantMoyen(new FWCurrency(APACORPrestationsParser.getField(line, fields,
//                        "REVENU_JOURNALIER_MOYEN")));
//
//                // situations professionnelles
//                fields = (HashMap) configs.get(APACORPrestationsParser.CODE_EMP_PERIODE);
//
//                List<String> idsSP = new ArrayList<String>();
//                while (((line = bufferedReader.readLine()) != null)
//                        && line.startsWith(APACORPrestationsParser.CODE_EMP_PERIODE)) {
//                    APResultatCalculSituationProfessionnel rcSitPro = new APResultatCalculSituationProfessionnel();
//                    String noAffilie = APACORPrestationsParser.getField(line, fields, "NUMERO_AFFILIE_EMPLOYEUR");
//                    // Numéro d'affilié trop long au valais (reformatage à la sortie)
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
//                     * salaire total de l'assuré pendant la période car ACOR s'est deja charge de le prendre en compte.
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
//                        // Nouvelle tentative de recherche par #affilie pour les cas affiliés avec 2 affiliations sous
//                        // le même #.
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
//                    // TODO: récupérer les cantons et taux d'imposition
//
//                    rc.addResultatCalculSitProfessionnelle(rcSitPro);
//                }
//
//                // Ajouter dans la liste des wrappers les bases de calcul de
//                // sit. prof. si non existante.
//                // Dans certains cas, Il arrive que ACOR ne retourne pas
//                // l'employeur si versé à l'assuré. Ces employeurs
//                // ne seront donc pas pris en compte lors du calcul de la
//                // répartition des pmts, pour le calcul des cotisations.
//
//                // Il faut donc les rajouter.
//
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
//                             * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '['
//                             * suivi d'un minimum de un ou de plusieurs chiffres ([0-9] signifie les caractères valide,
//                             * donc les chiffres, le '+' dit que çà doit correspondre au moins une fois) et qui est
//                             * suivit d'un ']' Si je n'est pas été assez claire :
//                             * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
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
//                        // résultats du calcul retournée par ACOR,
//                        // on va donc la créer et la rajouter.
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
//                            // Devrait toujours être à false, car ACOR retourne
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
//
//                wrapper.setPrestationBase(rc);
//                wrappers.add(wrapper);
//                wrapper = null;
//            } while ((line != null) && line.startsWith(APACORPrestationsParser.CODE_PERIODE));
//
//            // taux de participation des employeurs au RJM
//            // ------------------------------------------------------------
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
//                    // Numéro d'affilié trop long au valais (reformatage à la sortie)
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
//                             * Regex qui remplace une chaine qui commence (^ <-- çà veut dire commence quoi;) par '['
//                             * suivi d'un minimum de un ou de plusieurs chiffres ([0-9] signifie les caractères valide,
//                             * donc les chiffres, le '+' dit que çà doit correspondre au moins une fois) et qui est
//                             * suivit d'un ']' Si je n'est pas été assez claire :
//                             * http://en.wikipedia.org/wiki/Regular_expression saura répondre (RCO) BZ 8422
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
//                                // créer entièrement à partir de la base de
//                                // calcul.
//                                // newRcSitPro on va donc y rajouter le montant
//                                // et salaire journalier en le recalculant à
//                                // partir
//                                // du montant total de la prestation au prorata.
//
//                                // Ceci est nécessaire pour le calcul du montant
//                                // des cotisations, afin de determiner
//                                // si la part salariale est supérieure à la part
//                                // de l'indépendant, le cas échéant.
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
//
//            return wrappers;
//        } catch (PRACORException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new PRACORException("impossible de parser", e);
//        } finally {
//            try {
//                bufferedReader.close();
//            } catch (IOException e1) {
//                ;
//            }
//        }
        return null;
    }
}
