/*
 */
package globaz.corvus.helpers.process;

import java.math.BigDecimal;
import java.util.*;

import ch.admin.ofit.anakin.donnee.AnnonceErreur;
import ch.globaz.corvus.domaine.constantes.TypeOrdreVersement;
import ch.globaz.prestation.domaine.CodePrestation;
import globaz.commons.nss.NSUtil;
import globaz.corvus.anakin.REAnakinParser;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.basescalcul.IREBasesCalcul;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.decisions.IREDecision;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.api.lots.IRELot;
import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.corvus.api.prestations.IREPrestations;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnonceRenteManager;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel2A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution10Eme;
import globaz.corvus.db.annonces.REAnnoncesDiminution9Eme;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.decisions.REDecisionEntity;
import globaz.corvus.db.decisions.REValidationDecisions;
import globaz.corvus.db.decisions.REValidationDecisionsManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteManager;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.db.ordresversements.REOrdresVersements;
import globaz.corvus.db.prestations.REPrestations;
import globaz.corvus.db.prestations.REPrestationsManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.tools.REArcFrenchValidator;
import globaz.corvus.utils.REDiminutionRenteUtils;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.beneficiaire.principal.REBeneficiairePrincipal;
import globaz.corvus.utils.enumere.genre.prestations.REGenrePrestationEnum;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.utils.survenance.REDateSurvenanceUtil;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.db.infos.PRInfoCompl;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRAssert;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TIRoleManager;

/**
 * Traitement effectuant la logique de validation des décisions.
 *
 * @author scr
 */
public class REValiderDecisionHandler {

    /**
     * Création des annonces 9ème ou 10ème revision pour les rentes accordées qui n'ont pas d'annonces
     *
     * @param idRenteAccordee
     *            la rente accordée que n'a pas d'annonce
     * @throws Exception
     */
    public static void createAnnonce4x(final BSession session, final BTransaction transaction,
                                       final Long idRenteAccordee) throws Exception {

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(idRenteAccordee.toString());
        ra.retrieve(transaction);

        String dateMoisRapport = REPmtMensuel.getDateDernierPmt(session);
        // BZ 6056 passer à ANAKIN le mois de rapport correct (début du droit) si la décision démarre dans le futur
        if (JadeDateUtil.isDateMonthYearBefore(dateMoisRapport, ra.getDateDebutDroit())) {
            dateMoisRapport = ra.getDateDebutDroit();
        }

        dateMoisRapport = PRDateFormater
                .convertDate_AAAAMM_to_MMAA(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(dateMoisRapport));

        REValiderDecisionHandler.createAnnonce4x(session, transaction, idRenteAccordee, dateMoisRapport);
    }

    /**
     * Création des annonces 9ème ou 10ème revision pour les rentes accordées qui n'ont pas d'annonces
     *
     * @param idRenteAccordee
     *            la rente accordée que n'a pas d'annonce
     * @param moisRapport
     *            le mois de rapport qui sera assigné à l'annonce
     * @throws Exception
     */
    public static void createAnnonce4x(final BSession session, final BTransaction transaction,
                                       final Long idRenteAccordee, final String moisRapport) throws Exception {

        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(idRenteAccordee.toString());
        ra.retrieve(transaction);

        if (!ra.isNew()) {
            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession(session);
            bc.setIdBasesCalcul(ra.getIdBaseCalcul());
            bc.retrieve(transaction);

            REDemandeRenteManager drManager = new REDemandeRenteManager();
            drManager.setSession(session);
            drManager.setForIdRenteCalculee(bc.getIdRenteCalculee());
            drManager.find(transaction);

            REDemandeRente dr = (REDemandeRente) drManager.getFirstEntity();

            if (!bc.isNew() && (dr != null)) {

                PRInfoCompl ic = dr.loadInfoComplementaire(transaction);

                // ///////////////////////////////////////////////////////////////
                // Creation de l'enregistrement 1
                // ///////////////////////////////////////////////////////////////
                REAnnoncesAbstractLevel2A annonce_01 = null;

                if ("9".equals(bc.getDroitApplique())) {

                    annonce_01 = REValiderDecisionHandler.remplirEnregistrement01(
                            new REAnnoncesAugmentationModification9Eme(), session, transaction, ra, moisRapport,
                            ic.getIsRefugie());
                    annonce_01.setCodeApplication("41");

                    ((REAnnoncesAugmentationModification9Eme) annonce_01)
                            .setMensualiteRenteOrdRemp(REValiderDecisionHandler
                                    .formatMontantPourAnnonce(new FWCurrency(ra.getMontantRenteOrdiRemplacee())));
                } else if ("10".equals(bc.getDroitApplique())) {
                    annonce_01 = REValiderDecisionHandler.remplirEnregistrement01(
                            new REAnnoncesAugmentationModification10Eme(), session, transaction, ra, moisRapport,
                            ic.getIsRefugie());
                    annonce_01.setCodeApplication("44");
                }

                annonce_01.add(transaction);

                String idAnnonce_01 = annonce_01.getId();

                REAnnonceRente annonceRente = new REAnnonceRente();
                annonceRente.setSession(session);
                annonceRente.setIdAnnonceHeader(idAnnonce_01);
                annonceRente.setIdRenteAccordee(ra.getId());
                annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                annonceRente.add(transaction);

                // ///////////////////////////////////////////////////////////////
                // Creation de l'enregistrement 2
                // ///////////////////////////////////////////////////////////////
                REAnnoncesAbstractLevel2A annonce_02 = null;
                CodePrestation codePrestation = CodePrestation
                        .getCodePrestation(Integer.parseInt(ra.getCodePrestation()));

                if (codePrestation.isAPI()) {
                    annonce_02 = REValiderDecisionHandler.remplirEnregistrement02API(session, transaction, dr, ra);
                } else if ("9".equals(bc.getDroitApplique())) {
                    annonce_02 = REValiderDecisionHandler.remplirEnregistrement02_9emeRevision(
                            new REAnnoncesAugmentationModification9Eme(), session, transaction, bc, ra);
                    annonce_02.setCodeApplication("41");
                } else {
                    annonce_02 = REValiderDecisionHandler.remplirEnregistrement02_10emeRevision(
                            new REAnnoncesAugmentationModification10Eme(), session, transaction, bc, ra);
                    annonce_02.setCodeApplication("44");
                }

                annonce_02.setCodeEnregistrement01("02");
                annonce_02.add(transaction);

                String idAnnonce_02 = annonce_02.getId();

                // ///////////////////////////////////////////////////////////////
                // mise à jour de l'annonce header
                // ///////////////////////////////////////////////////////////////
                REAnnonceHeader annonceHeader = new REAnnonceHeader();
                annonceHeader.setSession(session);
                annonceHeader.setIdAnnonce(idAnnonce_01);
                annonceHeader.retrieve(transaction);

                annonceHeader.setIdLienAnnonce(idAnnonce_02);
                annonceHeader.update(transaction);

                // Validation des annonces
                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, annonce_01, annonce_02,
                        moisRapport);
                StringBuffer buff = new StringBuffer();

                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }
                if (buff.length() > 0) {
                    throw new Exception(buff.toString());
                }
            }
        }
    }

    /**
     * Format un montant sur 5 positions avec des zéro comblant les vides<br/>
     * Exemple : 123.00 Fr. donnera 00123
     *
     * @param montant
     *            le montant à formatter
     * @return le montant sur 5 position arrondi au franc
     */
    private static String formatMontantPourAnnonce(final FWCurrency montant) {
        String montantToString = montant.toString();

        int nbCaractereMontant = JANumberFormatter
                .formatZeroValues(montantToString.substring(0, montantToString.length() - 3), 0).length();
        int nbZeroAAjouter = 5 - nbCaractereMontant;
        String chaineZero = "";
        for (int i = 0; i < nbZeroAAjouter; i++) {
            chaineZero += "0";
        }
        return chaineZero + montantToString.substring(0, montantToString.length() - 3);
    }

    private static String formatXPosAppendWithZero(final int nombrePos, final boolean isAppendLeft,
                                                   final String value) {
        StringBuffer result = new StringBuffer();

        if (JadeStringUtil.isEmpty(value)) {

            for (int i = 0; i < nombrePos; i++) {
                result.append("0");
            }
        } else {
            int diff = nombrePos - value.length();
            // Append left
            if (isAppendLeft) {
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
                result.append(value);
            }
            // Append right
            else {
                result.append(value);
                for (int i = 0; i < diff; i++) {
                    result.append("0");
                }
            }
        }
        return result.toString();
    }

    private static REAnnoncesAbstractLevel2A remplirEnregistrement01(final REAnnoncesAbstractLevel2A annonce_01,
                                                                     final BSession session, final BTransaction transaction, final RERenteAccordee ra,
                                                                     final String dateMoisRapport, final boolean isRefugie) throws Exception {

        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(ra.getCodePrestation()));

        annonce_01.setSession(session);

        annonce_01.setCodeEnregistrement01("01");
        annonce_01.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce_01.setNumeroCaisse(session.getApplication().getProperty("noCaisse"));
        annonce_01.setNumeroAgence(session.getApplication().getProperty("noAgence"));

        annonce_01.setIdTiers(ra.getIdTiersBeneficiaire());
        PRTiersWrapper tierBeneficiaire = PRTiersHelper.getTiersParId(session, ra.getIdTiersBeneficiaire());
        annonce_01.setNoAssAyantDroit(
                NSUtil.unFormatAVS(tierBeneficiaire.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));

        PRTiersWrapper tierBeneficiaireAdr = PRTiersHelper.getTiersAdresseDomicileParId(session,
                ra.getIdTiersBeneficiaire(), JACalendar.todayJJsMMsAAAA());
        PRTiersWrapper tierRequerantAdr = PRTiersHelper.getTiersAdresseDomicileParId(session, ra.getIdTiersBaseCalcul(),
                JACalendar.todayJJsMMsAAAA());

        String codeCanton = "";
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());

        ISFMembreFamilleRequerant[] mfr = sf.getMembresFamilleRequerant(ra.getIdTiersBeneficiaire());

        if (mfr != null) {
            // On recherche le requérant
            for (int i = 0; i < mfr.length; i++) {
                ISFMembreFamilleRequerant mf = mfr[i];
                if (ISFSituationFamiliale.CS_TYPE_RELATION_REQUERANT.equals(mf.getRelationAuRequerant())) {
                    String csCantonDomicile = mf.getCsCantonDomicile();
                    codeCanton = PRACORConst.csCantonToAcor(csCantonDomicile);
                    break;
                }
            }
        }

        // Prendre le canton à partir de la situation familiale de
        // l'assuré (bénéficiaire de la prestation).
        // Si retour de sf est étranger ou null, continuer avec le
        // traitement ci-dessous.

        // 030 -> Etranger
        if (JadeStringUtil.isBlankOrZero(codeCanton) || "000".equals(codeCanton) || "030".equals(codeCanton)) {

            // Tester si code etranger pour tiersBenef, prendre

            if (tierRequerantAdr != null) {
                codeCanton = PRACORConst
                        .csCantonToAcor(tierRequerantAdr.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON));
            } else {
                if (tierBeneficiaireAdr != null) {
                    String csCanton = tierBeneficiaireAdr.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON);
                    codeCanton = PRACORConst.csCantonToAcor(csCanton);
                } else if (JadeNumericUtil.isInteger(ra.getCodePrestation())
                        && codePrestation.isRenteComplementairePourEnfant()) {
                    // cas des prises de décision pour enfant uniquement
                    // on prendra le code canton de l'adresse de paiement de la rente

                    REInformationsComptabilite infoComptable = new REInformationsComptabilite();
                    infoComptable.setSession(session);
                    infoComptable.setIdInfoCompta(ra.getIdInfoCompta());
                    infoComptable.retrieve();

                    if (!infoComptable.isNew()) {
                        TIAdressePaiementData adressePaiement = PRTiersHelper.getAdressePaiementData(session,
                                transaction, infoComptable.getIdTiersAdressePmt(),
                                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", "");
                        if (!JadeStringUtil.isBlank(adressePaiement.getNpa())) {
                            String csCodeCanton = PRTiersHelper.getCanton(session, adressePaiement.getNpa());
                            codeCanton = PRACORConst.csCantonToAcor(csCodeCanton);
                        }
                    }
                } else {
                    codeCanton = "";
                }
            }
        }
        annonce_01.setCantonEtatDomicile(codeCanton);

        // BZ 4666
        ISFSituationFamiliale sitFam = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());
        String csEtatCivil = null;
        ISFMembreFamilleRequerant[] mf = sitFam.getMembresFamille(ra.getIdTiersBeneficiaire());
        for (int i = 0; i < mf.length; i++) {
            ISFMembreFamilleRequerant membre = mf[i];
            // On récupère le bénéficiaire en tant que membre de famille
            if (ra.getIdTiersBeneficiaire().equals(membre.getIdTiers())) {
                csEtatCivil = membre.getCsEtatCivil();

                break;
            }
        }

        // Peut arriver dans le cas d'un enfant de la situation familialle, par exemple.
        if (csEtatCivil == null) {
            csEtatCivil = ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE;
        }

        // Je dois transformer le CS_ETAT_X de Hera en code 1, 2, 3, ... stocké dans l'annonce
        // depuis hera : CS_ETAT_CELIBATAIRE
        // Stocké en base de données : 1, 2, 3, 4

        annonce_01.setEtatCivil(PRACORConst.csEtatCivilHeraToAcorForRentes(session, csEtatCivil));

        PRTiersWrapper tierComp1 = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire1());
        if (tierComp1 != null) {
            annonce_01.setPremierNoAssComplementaire(
                    NSUtil.unFormatAVS(tierComp1.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        } else if (codePrestation.isRenteComplementairePourEnfant()
                || (!ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE.equals(csEtatCivil)
                && !ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_JUDICIAIREMENT.equals(csEtatCivil)
                && !ISFSituationFamiliale.CS_ETAT_CIVIL_DIVORCE.equals(csEtatCivil))) {
            annonce_01.setPremierNoAssComplementaire("00000000000");
        } else {
            annonce_01.setPremierNoAssComplementaire("");
        }

        PRTiersWrapper tierComp2 = PRTiersHelper.getTiersParId(session, ra.getIdTiersComplementaire2());
        if (tierComp2 != null) {
            annonce_01.setSecondNoAssComplementaire(
                    NSUtil.unFormatAVS(tierComp2.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
        } else if (codePrestation.isRenteComplementairePourEnfant()) {
            annonce_01.setSecondNoAssComplementaire("00000000000");
        } else {
            annonce_01.setSecondNoAssComplementaire("");
        }

        if (!JadeStringUtil.isBlankOrZero(ra.getCodeMutation())) {
            if (ra.getCodeMutation().length() == 1) {
                annonce_01.setCodeMutation("0" + ra.getCodeMutation());
            } else {
                annonce_01.setCodeMutation(ra.getCodeMutation());
            }
        }

        annonce_01.setReferenceCaisseInterne("AUG" + session.getUserId().toUpperCase());
        annonce_01.setGenrePrestation(ra.getCodePrestation());
        annonce_01.setDebutDroit(PRDateFormater
                .convertDate_AAAAMM_to_MMAA(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(ra.getDateDebutDroit())));
        annonce_01.setFinDroit(PRDateFormater
                .convertDate_AAAAMM_to_MMAA(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(ra.getDateFinDroit())));
        annonce_01.setMoisRapport(dateMoisRapport);
        annonce_01.setCodeMutation(ra.getCodeMutation());
        annonce_01.setIsRefugie(isRefugie ? "1" : "0");

        annonce_01.setMensualitePrestationsFrancs(
                REValiderDecisionHandler.formatMontantPourAnnonce(new FWCurrency(ra.getMontantPrestation())));

        return annonce_01;
    }

    private static REAnnoncesAbstractLevel2A remplirEnregistrement02_10emeRevision(
            final REAnnoncesAugmentationModification10Eme annonce_02, final BSession session,
            final BTransaction transaction, final REBasesCalcul bc, final RERenteAccordee ra) {

        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(ra.getCodePrestation()));

        if (codePrestation.isRenteExtraordinaire()) {
            annonce_02.setAnneeNiveau("");
            annonce_02.setEchelleRente("");
            annonce_02.setDureeCoEchelleRenteAv73("");
            annonce_02.setDureeCoEchelleRenteDes73("");
            annonce_02.setAnneeCotClasseAge("");
            annonce_02.setRamDeterminant("");
            annonce_02.setCodeRevenuSplitte("");
            annonce_02.setDureeCotPourDetRAM("");
            annonce_02.setNombreAnneeBTE("");
            annonce_02.setNbreAnneeBTA("");
            annonce_02.setNbreAnneeBonifTrans("");
            annonce_02.setDateRevocationAjournement("");
            annonce_02.setDureeAjournement("");
            annonce_02.setSupplementAjournement("");
            annonce_02.setDateDebutAnticipation("");
            annonce_02.setNbreAnneeAnticipation("");
            annonce_02.setReductionAnticipation("");
        } else {
            annonce_02.setAnneeNiveau(bc.getAnneeDeNiveau());
            annonce_02.setEchelleRente(bc.getEchelleRente());
            annonce_02.setDureeCoEchelleRenteAv73(PRStringUtils.replaceString(bc.getDureeCotiAvant73(), ".", ""));
            annonce_02.setDureeCoEchelleRenteDes73(PRStringUtils.replaceString(bc.getDureeCotiDes73(), ".", ""));
            annonce_02.setAnneeCotClasseAge(bc.getAnneeCotiClasseAge());
            annonce_02.setRamDeterminant(bc.getRevenuAnnuelMoyen());
            annonce_02.setCodeRevenuSplitte(bc.isRevenuSplitte().booleanValue() ? "1" : "0");
            annonce_02.setDureeCotPourDetRAM(PRStringUtils.replaceString(bc.getDureeRevenuAnnuelMoyen(), ".", ""));

            String anneesBTE = PRStringUtils.replaceString(bc.getAnneeBonifTacheEduc(), ".", "");
            if (JadeStringUtil.isBlankOrZero(anneesBTE)) {
                annonce_02.setNombreAnneeBTE("");
            } else {
                annonce_02.setNombreAnneeBTE(anneesBTE);
            }

            String annesBTA = PRStringUtils.replaceString(bc.getAnneeBonifTacheAssistance(), ".", "");
            if (JadeStringUtil.isBlankOrZero(annesBTA)) {
                annonce_02.setNbreAnneeBTA("");
            } else {
                annonce_02.setNbreAnneeBTA(annesBTA);
            }

            String annesBTR = PRStringUtils.replaceString(bc.getAnneeBonifTransitoire(), ".", "");
            if (JadeStringUtil.isBlankOrZero(annesBTR)) {
                annonce_02.setNbreAnneeBonifTrans("");
            } else {
                annonce_02.setNbreAnneeBonifTrans(annesBTR);
            }

            annonce_02.setDateRevocationAjournement(PRDateFormater.convertDate_AAAAMM_to_MMAA(
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(ra.getDateRevocationAjournement())));
            annonce_02.setDureeAjournement(PRStringUtils.replaceString(ra.getDureeAjournement(), ".", ""));

            if (JadeStringUtil.isBlankOrZero(ra.getSupplementAjournement())) {
                annonce_02.setSupplementAjournement("");
            } else {
                annonce_02.setSupplementAjournement(REValiderDecisionHandler
                        .formatMontantPourAnnonce(new FWCurrency(ra.getSupplementAjournement())));
            }

            annonce_02.setDateDebutAnticipation(PRDateFormater.convertDate_AAAAMM_to_MMAA(
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(ra.getDateDebutAnticipation())));

            if (JadeStringUtil.isBlankOrZero(ra.getAnneeAnticipation())) {
                annonce_02.setNbreAnneeAnticipation("");
            } else {
                annonce_02.setNbreAnneeAnticipation(ra.getAnneeAnticipation());
            }

            if (JadeStringUtil.isBlankOrZero(ra.getMontantReducationAnticipation())) {
                annonce_02.setReductionAnticipation("");
            } else {
                annonce_02.setReductionAnticipation(REValiderDecisionHandler
                        .formatMontantPourAnnonce(new FWCurrency(ra.getMontantReducationAnticipation())));
            }
        }

        annonce_02.setCasSpecial1(ra.getCodeCasSpeciaux1());
        annonce_02.setCasSpecial2(ra.getCodeCasSpeciaux2());
        annonce_02.setCasSpecial3(ra.getCodeCasSpeciaux3());
        annonce_02.setCasSpecial4(ra.getCodeCasSpeciaux4());
        annonce_02.setCasSpecial5(ra.getCodeCasSpeciaux5());

        if (codePrestation.isRenteExtraordinaire() || codePrestation.isAPI()) {
            annonce_02.setDureeCotManquante48_72("");
            annonce_02.setDureeCotManquante73_78("");
        } else {
            annonce_02.setDureeCotManquante48_72(
                    REValiderDecisionHandler.formatXPosAppendWithZero(2, true, bc.getMoisAppointsAvant73()));
            annonce_02.setDureeCotManquante73_78(
                    REValiderDecisionHandler.formatXPosAppendWithZero(2, true, bc.getMoisAppointsDes73()));
        }

        if (JadeStringUtil.isBlankOrZero(bc.getCodeOfficeAi())) {
            annonce_02.setOfficeAICompetent("");
        } else {
            annonce_02.setOfficeAICompetent(bc.getCodeOfficeAi());
        }

        if (JadeStringUtil.isBlankOrZero(bc.getDegreInvalidite())) {
            annonce_02.setDegreInvalidite("");
            annonce_02.setAgeDebutInvalidite("");
        } else {
            annonce_02.setDegreInvalidite(
                    REValiderDecisionHandler.formatXPosAppendWithZero(3, true, bc.getDegreInvalidite()));
            annonce_02.setAgeDebutInvalidite(bc.isInvaliditePrecoce().booleanValue() ? "1" : "0");
        }

        if (JadeStringUtil.isBlankOrZero(bc.getCleInfirmiteAyantDroit())) {
            annonce_02.setCodeInfirmite("");
        } else {
            annonce_02.setCodeInfirmite(bc.getCleInfirmiteAyantDroit());
        }

        if (JadeStringUtil.isBlankOrZero(bc.getSurvenanceEvtAssAyantDroit())) {
            annonce_02.setSurvenanceEvenAssure("");
        } else {
            annonce_02.setSurvenanceEvenAssure(PRDateFormater.convertDate_AAAAMM_to_MMAA(
                    PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(bc.getSurvenanceEvtAssAyantDroit())));
        }

        if (JadeStringUtil.isBlankOrZero(ra.getReductionFauteGrave())) {
            annonce_02.setReduction("");
        } else {
            annonce_02.setReduction(
                    REValiderDecisionHandler.formatMontantPourAnnonce(new FWCurrency(ra.getReductionFauteGrave())));
        }

        if (codePrestation.isSurvivant() && codePrestation.isRentePrincipale()) {
            annonce_02.setIsSurvivant(ra.getCodeSurvivantInvalide());
        } else {
            annonce_02.setIsSurvivant("");
        }

        return annonce_02;
    }

    private static REAnnoncesAugmentationModification10Eme remplirEnregistrement02API(final BSession session,
                                                                                      final BTransaction transaction, final REDemandeRente dr, final RERenteAccordee ra) throws Exception {

        REDemandeRenteAPI api = new REDemandeRenteAPI();
        api.setSession(session);
        api.setIdDemandeRente(dr.getIdDemandeRente());
        api.retrieve(transaction);

        REAnnoncesAugmentationModification10Eme annonce44_02 = new REAnnoncesAugmentationModification10Eme();
        annonce44_02.setSession(session);

        annonce44_02.setCodeApplication("44");
        annonce44_02.setCodeEnregistrement01("02");
        annonce44_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);

        annonce44_02.setIdTiers(ra.getIdTiersBeneficiaire());

        annonce44_02.setOfficeAICompetent(api.getCodeOfficeAI());
        annonce44_02.setCodeInfirmite(session.getCode(api.getCsInfirmite()) + session.getCode(api.getCsAtteinte()));

        String dateSurvenance = "";
        if (!JadeStringUtil.isBlank(api.getDateSuvenanceEvenementAssure())) {
            dateSurvenance = api.getDateSuvenanceEvenementAssure();

            if (JadeDateUtil.isGlobazDate(dateSurvenance)) {
                dateSurvenance = dateSurvenance.substring(3); // date au format MM.AAAA
            }
        } else {
            dateSurvenance = REDateSurvenanceUtil.getSurvenancePeriodeAPI(session, api.getIdDemandeRente());
        }
        dateSurvenance = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(dateSurvenance);
        dateSurvenance = PRDateFormater.convertDate_AAAAMM_to_MMAA(dateSurvenance);

        annonce44_02.setSurvenanceEvenAssure(dateSurvenance);
        annonce44_02.setGenreDroitAPI(session.getCode(ra.getCsGenreDroitApi()));
        annonce44_02.setCasSpecial1(ra.getCodeCasSpeciaux1());
        annonce44_02.setCasSpecial2(ra.getCodeCasSpeciaux2());
        annonce44_02.setCasSpecial3(ra.getCodeCasSpeciaux3());
        annonce44_02.setCasSpecial4(ra.getCodeCasSpeciaux4());
        annonce44_02.setCasSpecial5(ra.getCodeCasSpeciaux5());

        return annonce44_02;

    }

    private static REAnnoncesAbstractLevel2A remplirEnregistrement02_9emeRevision(
            REAnnoncesAugmentationModification9Eme annonce_02, final BSession session, final BTransaction transaction,
            final REBasesCalcul bc, final RERenteAccordee ra) throws Exception {

        CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(ra.getCodePrestation()));

        if (codePrestation.isRenteExtraordinaire()) {
            annonce_02.setAnneeNiveau("");
            annonce_02.setDureeCotPourDetRAM("");
            annonce_02.setRevenuPrisEnCompte("");
            annonce_02.setEchelleRente("");
            annonce_02.setDureeCoEchelleRenteAv73("");
            annonce_02.setDureeCoEchelleRenteDes73("");
            annonce_02.setDureeCotManquante48_72("");
            annonce_02.setDureeCotManquante73_78("");
            annonce_02.setRamDeterminant("");
            annonce_02.setAnneeCotClasseAge("");
        } else {
            annonce_02.setAnneeNiveau(bc.getAnneeDeNiveau());
            annonce_02.setDureeCotPourDetRAM(PRStringUtils.replaceString(bc.getDureeRevenuAnnuelMoyen(), ".", ""));
            annonce_02.setRevenuPrisEnCompte(bc.getRevenuPrisEnCompte());
            annonce_02.setEchelleRente(bc.getEchelleRente());
            annonce_02.setDureeCoEchelleRenteAv73(
                    JadeStringUtil.fillWithZeroes(PRStringUtils.replaceString(bc.getDureeCotiAvant73(), ".", ""), 2));
            annonce_02.setDureeCoEchelleRenteDes73(
                    JadeStringUtil.fillWithZeroes(PRStringUtils.replaceString(bc.getDureeCotiDes73(), ".", ""), 2));
            annonce_02.setDureeCotManquante48_72(JadeStringUtil.fillWithZeroes(bc.getMoisAppointsAvant73(), 2));
            annonce_02.setDureeCotManquante73_78(JadeStringUtil.fillWithZeroes(bc.getMoisAppointsDes73(), 2));
            annonce_02.setRamDeterminant(JadeStringUtil.fillWithZeroes(bc.getRevenuAnnuelMoyen(), 8));
            annonce_02.setAnneeCotClasseAge(bc.getAnneeCotiClasseAge());
        }

        annonce_02.setDateRevocationAjournement(PRDateFormater.convertDate_AAAAMM_to_MMAA(
                PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(ra.getDateRevocationAjournement())));
        annonce_02.setDureeAjournement(PRStringUtils.replaceString(ra.getDureeAjournement(), ".", ""));
        // BZ 6270
        if (JadeStringUtil.isDecimalEmpty(ra.getSupplementAjournement())) {
            annonce_02.setSupplementAjournement("");
        } else {
            int lengthMontant = String.valueOf(new FWCurrency(ra.getSupplementAjournement()).intValue()).length();
            int nbZeroAajouter = 5 - lengthMontant;

            String montant = String.valueOf(new FWCurrency(ra.getSupplementAjournement()).intValue());

            for (int i = 0; i < nbZeroAajouter; i++) {
                montant = "0" + montant;
            }

            annonce_02.setSupplementAjournement(montant);

        }

        // BZ 7595
        if (codePrestation.isRenteExtraordinaire()) {
            annonce_02.setIsMinimumGaranti(bc.isMinimuGaranti().booleanValue() ? "1" : "0");
        } else {
            annonce_02.setIsMinimumGaranti(bc.isMinimuGaranti().booleanValue() ? "1" : "");
        }

        // Pour la 9ème, sur 3 position !!!!
        if ((bc.getCodeOfficeAi() != null && !JadeStringUtil.isBlankOrZero(bc.getCodeOfficeAi()))
                && (bc.getCodeOfficeAi().length() == 3)) {
            String s = bc.getCodeOfficeAi().substring(1, 3);
            annonce_02.setOfficeAICompetent(s);
        }

        annonce_02.setDegreInvalidite(
                JadeStringUtil.isDecimalEmpty(bc.getDegreInvalidite()) ? "" : bc.getDegreInvalidite());
        annonce_02.setCodeInfirmite(
                JadeStringUtil.isDecimalEmpty(bc.getCleInfirmiteAyantDroit()) ? "" : bc.getCleInfirmiteAyantDroit());
        annonce_02.setSurvenanceEvenAssure(PRDateFormater.convertDate_AAAAMM_to_MMAA(
                PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(bc.getSurvenanceEvtAssAyantDroit())));
        annonce_02.setAgeDebutInvalidite(bc.isInvaliditePrecoce().booleanValue() ? "1" : "0");
        annonce_02.setReduction(
                JadeStringUtil.isDecimalEmpty(ra.getReductionFauteGrave()) ? "" : ra.getReductionFauteGrave());

        annonce_02.setCasSpecial1(ra.getCodeCasSpeciaux1());
        annonce_02.setCasSpecial2(ra.getCodeCasSpeciaux2());
        annonce_02.setCasSpecial3(ra.getCodeCasSpeciaux3());
        annonce_02.setCasSpecial4(ra.getCodeCasSpeciaux4());
        annonce_02.setCasSpecial5(ra.getCodeCasSpeciaux5());

        // recherche du nombre d'année BTE
        String nombreAnneeBTE = null;

        // si rien n'est défini dans la base de calcul standard, on vérifie dans la base de calcul 9ème
        if (JadeStringUtil.isBlankOrZero(bc.getAnneeBonifTacheEduc())) {
            REBasesCalculNeuviemeRevision bc9emeRev = new REBasesCalculNeuviemeRevision();
            bc9emeRev.setSession(session);
            bc9emeRev.setIdBasesCalcul(bc.getIdBasesCalcul());
            bc9emeRev.retrieve(transaction);

            if (!JadeStringUtil.isBlankOrZero(bc9emeRev.getNbrAnneeEducation())) {
                if (bc9emeRev.getNbrAnneeEducation().length() > 2) {
                    nombreAnneeBTE = bc9emeRev.getNbrAnneeEducation().substring(0, 2);
                } else {
                    nombreAnneeBTE = bc9emeRev.getNbrAnneeEducation();
                }
            }
        } else {
            if (bc.getAnneeBonifTacheEduc().length() > 2) {
                nombreAnneeBTE = bc.getAnneeBonifTacheEduc().substring(0, 2);
            } else {
                nombreAnneeBTE = bc.getAnneeBonifTacheEduc();
            }
        }

        annonce_02.setNombreAnneeBTE(nombreAnneeBTE);
        // BZ 7595
        if (codePrestation.isRenteExtraordinaire()) {
            annonce_02.setIsLimiteRevenu("0");
        } else {
            annonce_02.setIsLimiteRevenu("");
        }

        REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();
        bc9.setSession(session);
        bc9.setIdBasesCalcul(bc.getIdBasesCalcul());
        bc9.retrieve(transaction);

        // BZ 6270
        if (!JadeStringUtil.isBlankOrZero(bc9.getBonificationTacheEducative())) {
            int bteBaseCalculSansCentimes = new FWCurrency(bc9.getBonificationTacheEducative()).intValue();
            int nbZeroAajouter = 6 - Integer.toString(bteBaseCalculSansCentimes).length();

            StringBuilder bteMoyenAnnonce = new StringBuilder();

            for (int i = 0; i < nbZeroAajouter; i++) {
                bteMoyenAnnonce.append("0");
            }
            bteMoyenAnnonce.append(Integer.toString(bteBaseCalculSansCentimes));

            annonce_02.setBteMoyennePrisEnCompte(bteMoyenAnnonce.toString());
        } else {
            annonce_02.setBteMoyennePrisEnCompte("");
        }

        if (!JadeStringUtil.isDecimalEmpty(bc9.getBonificationTacheEducative())) {
            BigDecimal revenuSansBTE = new BigDecimal(bc.getRevenuAnnuelMoyen());
            revenuSansBTE = revenuSansBTE.subtract(new BigDecimal(bc9.getBonificationTacheEducative()));
            annonce_02.setRevenuAnnuelMoyenSansBTE(
                    JadeStringUtil.fillWithZeroes(revenuSansBTE.toBigInteger().toString(), 8));
        } else {
            annonce_02.setRevenuAnnuelMoyenSansBTE("");
        }

        annonce_02.setReduction(
                JadeStringUtil.isDecimalEmpty(ra.getReductionFauteGrave()) ? "" : ra.getReductionFauteGrave());

        // BZ 7595
        // Si la rente accordées est de type invalidité extraordinaire
        if (codePrestation.isAI() && codePrestation.isRenteExtraordinaire()) {
            annonce_02.setRevenuPrisEnCompte("");
        }

        REArcFrenchValidator frenchValidator = new REArcFrenchValidator();
        annonce_02 = frenchValidator.validateARC_41_02(annonce_02);

        return annonce_02;
    }

    private String idDecision = "";

    private String idDemandeRente = "";

    private final Set<String> idsPrestationsDues = new TreeSet<String>();

    private final Map<Long, String> idsPrstDuesParRenteAccordee = new HashMap<Long, String>();

    private BSession session = null;

    /**
     * Crée une nouvelle instance de la classe REValiderDecisionsProcess.
     */
    public REValiderDecisionHandler(final BSession session) {
        this.session = session;

    }

    protected void assertNotIsNew(final BEntity entity, final String errorMsg) throws Exception {
        if (entity.isNew()) {
            throw new Exception(errorMsg == null ? ""
                    : errorMsg + " " + entity.getClass().getName() + " not Found. id=" + entity.getId());
        }
    }

    /**
     * @param prestationDue
     * @param dateDecisionDu
     *            format : mm.aaaa
     * @return -1 : il y a du courant, mais n'est pas validé 1 : il y a du courant qui est validé 0 : il n'y a pas de
     *         courant
     */
    private int checkCourantPrestationDue(final REPrestationDue prestationDue, final String decisionDu)
            throws Exception {

        int result = 0;

        // On détermine s'il y a du courant
        // Condition : Etat == ACTIF et pas de date de fin
        // Etat = ACTIF et date de fin, avec date de decision (decisionDu) comprise entre date debut
        // et date de fin de la prestation due.

        if (JadeStringUtil.isBlankOrZero(prestationDue.getDateFinPaiement())) {
            if (IREPrestationDue.CS_ETAT_ACTIF.equals(prestationDue.getCsEtat())) {
                result = 1;
            } else {
                result = -1;
            }
        }

        JADate dateDebutPmt = new JADate(prestationDue.getDateDebutPaiement());
        JADate dateFinPmt = new JADate(prestationDue.getDateFinPaiement());
        JADate dateDecisionDu = new JADate(decisionDu);

        JACalendar cal = new JACalendarGregorian();
        if ((JACalendar.COMPARE_FIRSTUPPER == cal.compare(dateDecisionDu, dateDebutPmt))
                && (JACalendar.COMPARE_FIRSTLOWER == cal.compare(dateDecisionDu, dateFinPmt))) {

            if (IREPrestationDue.CS_ETAT_ACTIF.equals(prestationDue.getCsEtat())) {
                result = 1;
            } else {
                result = -1;
            }
        }
        return result;
    }

    private REAnnoncesAugmentationModification9Eme createAnnonce41(final BSession session,
                                                                   final REAnnoncesAugmentationModification9Eme annonce41) throws Exception {

        REAnnoncesAugmentationModification9Eme annonce41new = new REAnnoncesAugmentationModification9Eme();
        annonce41new.setSession(getSession());
        annonce41new.setAgeDebutInvalidite(annonce41.getAgeDebutInvalidite());
        annonce41new.setAgeDebutInvaliditeEpouse(annonce41.getAgeDebutInvaliditeEpouse());
        annonce41new.setAnneeCotClasseAge(annonce41.getAnneeCotClasseAge());
        annonce41new.setAnneeNiveau(annonce41.getAnneeNiveau());
        annonce41new.setBteMoyennePrisEnCompte(annonce41.getBteMoyennePrisEnCompte());
        annonce41new.setCantonEtatDomicile(annonce41.getCantonEtatDomicile());
        annonce41new.setCasSpecial1(annonce41.getCasSpecial1());
        annonce41new.setCasSpecial2(annonce41.getCasSpecial2());
        annonce41new.setCasSpecial3(annonce41.getCasSpecial3());
        annonce41new.setCasSpecial4(annonce41.getCasSpecial4());
        annonce41new.setCasSpecial5(annonce41.getCasSpecial5());
        annonce41new.setCodeApplication(annonce41.getCodeApplication());
        annonce41new.setCodeEnregistrement01(annonce41.getCodeEnregistrement01());
        annonce41new.setCodeInfirmite(annonce41.getCodeInfirmite());
        annonce41new.setCodeInfirmiteEpouse(annonce41.getCodeInfirmiteEpouse());
        annonce41new.setCodeMutation(annonce41.getCodeMutation());
        annonce41new.setCodeTraitement(annonce41.getCodeTraitement());
        annonce41new.setDateLiquidation(annonce41.getDateLiquidation());
        annonce41new.setDateRevocationAjournement(annonce41.getDateRevocationAjournement());
        annonce41new.setDebutDroit(annonce41.getDebutDroit());
        annonce41new.setDegreInvalidite(annonce41.getDegreInvalidite());
        annonce41new.setDegreInvaliditeEpouse(annonce41.getDegreInvaliditeEpouse());
        annonce41new.setDureeAjournement(annonce41.getDureeAjournement());
        annonce41new.setDureeCoEchelleRenteAv73(annonce41.getDureeCoEchelleRenteAv73());
        annonce41new.setDureeCoEchelleRenteDes73(annonce41.getDureeCoEchelleRenteDes73());
        annonce41new.setDureeCotManquante48_72(annonce41.getDureeCotManquante48_72());
        annonce41new.setDureeCotManquante73_78(annonce41.getDureeCotManquante73_78());
        annonce41new.setDureeCotPourDetRAM(annonce41.getDureeCotPourDetRAM());
        annonce41new.setEchelleRente(annonce41.getEchelleRente());
        annonce41new.setEtat(annonce41.getEtat());
        annonce41new.setEtatCivil(annonce41.getEtatCivil());
        annonce41new.setFinDroit(annonce41.getFinDroit());
        annonce41new.setGenreDroitAPI(annonce41.getGenreDroitAPI());
        annonce41new.setGenrePrestation(annonce41.getGenrePrestation());
        annonce41new.setIdTiers(annonce41.getIdTiers());
        annonce41new.setIsLimiteRevenu(annonce41.getIsLimiteRevenu());
        annonce41new.setIsMinimumGaranti(annonce41.getIsMinimumGaranti());
        annonce41new.setIsRefugie(annonce41.getIsRefugie());
        annonce41new.setMensualitePrestationsFrancs("");
        annonce41new.setMensualiteRenteOrdRemp(annonce41.getMensualiteRenteOrdRemp());
        annonce41new.setMoisRapport("");
        annonce41new.setNoAssAyantDroit(annonce41.getNoAssAyantDroit());
        annonce41new.setNombreAnneeBTE(annonce41.getNombreAnneeBTE());
        annonce41new.setNouveauNoAssureAyantDroit(annonce41.getNouveauNoAssureAyantDroit());
        annonce41new.setNumeroAgence(annonce41.getNumeroAgence());
        annonce41new.setNumeroCaisse(annonce41.getNumeroCaisse());
        annonce41new.setOfficeAiCompEpouse(annonce41.getOfficeAiCompEpouse());
        annonce41new.setOfficeAICompetent(annonce41.getOfficeAICompetent());
        annonce41new.setPremierNoAssComplementaire(annonce41.getPremierNoAssComplementaire());
        annonce41new.setRamDeterminant(annonce41.getRamDeterminant());
        annonce41new.setReduction(annonce41.getReduction());
        annonce41new.setReferenceCaisseInterne(annonce41.getReferenceCaisseInterne().toUpperCase());
        annonce41new.setRevenuAnnuelMoyenSansBTE(annonce41.getRevenuAnnuelMoyenSansBTE());
        annonce41new.setRevenuPrisEnCompte(annonce41.getRevenuPrisEnCompte());
        annonce41new.setSecondNoAssComplementaire(annonce41.getSecondNoAssComplementaire());
        annonce41new.setSession(session);
        annonce41new.setSupplementAjournement(annonce41.getSupplementAjournement());
        annonce41new.setSurvenanceEvenAssure(annonce41.getSurvenanceEvenAssure());
        annonce41new.setSurvenanceEvtAssureEpouse(annonce41.getSurvenanceEvtAssureEpouse());

        return annonce41new;

    }

    private REAnnoncesAugmentationModification10Eme createAnnonce44(final BSession session,
                                                                    final REAnnoncesAugmentationModification10Eme annonce44) throws Exception {

        REAnnoncesAugmentationModification10Eme annonce44new = new REAnnoncesAugmentationModification10Eme();
        annonce44new.setSession(getSession());
        annonce44new.setAgeDebutInvalidite(annonce44.getAgeDebutInvalidite());
        annonce44new.setAnneeCotClasseAge(annonce44.getAnneeCotClasseAge());
        annonce44new.setAnneeNiveau(annonce44.getAnneeNiveau());
        annonce44new.setCantonEtatDomicile(annonce44.getCantonEtatDomicile());
        annonce44new.setCasSpecial1(annonce44.getCasSpecial1());
        annonce44new.setCasSpecial2(annonce44.getCasSpecial2());
        annonce44new.setCasSpecial3(annonce44.getCasSpecial3());
        annonce44new.setCasSpecial4(annonce44.getCasSpecial4());
        annonce44new.setCasSpecial5(annonce44.getCasSpecial5());
        annonce44new.setCodeApplication(annonce44.getCodeApplication());
        annonce44new.setCodeEnregistrement01(annonce44.getCodeEnregistrement01());
        annonce44new.setCodeInfirmite(annonce44.getCodeInfirmite());
        annonce44new.setCodeMutation(annonce44.getCodeMutation());
        annonce44new.setCodeRevenuSplitte(annonce44.getCodeRevenuSplitte());
        annonce44new.setDateDebutAnticipation(annonce44.getDateDebutAnticipation());
        annonce44new.setDateRevocationAjournement(annonce44.getDateRevocationAjournement());
        annonce44new.setDebutDroit(annonce44.getDebutDroit());
        annonce44new.setDegreInvalidite(annonce44.getDegreInvalidite());
        annonce44new.setDureeAjournement(annonce44.getDureeAjournement());
        annonce44new.setDureeCoEchelleRenteAv73(annonce44.getDureeCoEchelleRenteAv73());
        annonce44new.setDureeCoEchelleRenteDes73(annonce44.getDureeCoEchelleRenteDes73());
        annonce44new.setDureeCotManquante48_72(annonce44.getDureeCotManquante48_72());
        annonce44new.setDureeCotManquante73_78(annonce44.getDureeCotManquante73_78());
        annonce44new.setDureeCotPourDetRAM(annonce44.getDureeCotPourDetRAM());
        annonce44new.setEchelleRente(annonce44.getEchelleRente());
        annonce44new.setEtat(annonce44.getEtat());
        annonce44new.setEtatCivil(annonce44.getEtatCivil());
        annonce44new.setFinDroit(annonce44.getFinDroit());
        annonce44new.setGenreDroitAPI(annonce44.getGenreDroitAPI());
        annonce44new.setGenrePrestation(annonce44.getGenrePrestation());
        annonce44new.setIdTiers(annonce44.getIdTiers());
        annonce44new.setIsRefugie(annonce44.getIsRefugie());
        annonce44new.setIsSurvivant(annonce44.getIsSurvivant());
        annonce44new.setMensualitePrestationsFrancs("");
        annonce44new.setMoisRapport("");
        annonce44new.setNbreAnneeAnticipation(annonce44.getNbreAnneeAnticipation());
        annonce44new.setNbreAnneeBonifTrans(annonce44.getNbreAnneeBonifTrans());
        annonce44new.setNbreAnneeBTA(annonce44.getNbreAnneeBTA());
        annonce44new.setNoAssAyantDroit(annonce44.getNoAssAyantDroit());
        annonce44new.setNombreAnneeBTE(annonce44.getNombreAnneeBTE());
        annonce44new.setNouveauNoAssureAyantDroit(annonce44.getNouveauNoAssureAyantDroit());
        annonce44new.setNumeroAgence(annonce44.getNumeroAgence());
        annonce44new.setNumeroCaisse(annonce44.getNumeroCaisse());
        annonce44new.setOfficeAICompetent(annonce44.getOfficeAICompetent());
        annonce44new.setPremierNoAssComplementaire(annonce44.getPremierNoAssComplementaire());
        annonce44new.setRamDeterminant(annonce44.getRamDeterminant());
        annonce44new.setReduction(annonce44.getReduction());
        annonce44new.setReductionAnticipation(annonce44.getReductionAnticipation());
        annonce44new.setReferenceCaisseInterne(annonce44.getReferenceCaisseInterne().toUpperCase());
        annonce44new.setSecondNoAssComplementaire(annonce44.getSecondNoAssComplementaire());
        annonce44new.setSupplementAjournement(annonce44.getSupplementAjournement());
        annonce44new.setSurvenanceEvenAssure(annonce44.getSurvenanceEvenAssure());
        annonce44new.setSession(session);

        return annonce44new;

    }

    private void doFullValidation(final BTransaction transaction, final REDecisionEntity decision) throws Exception {

        if (decision.isNew()) {
            throw new Exception("Decision not found. id = " + getIdDecision());
        }

        if (!IREDecision.CS_ETAT_PREVALIDE.equals(decision.getCsEtat())) {
            throw new Exception("Pour être validée, La décision doit se trouver dans l'état 'PRE-VALIDE'");
        }

        REPrestations prst = decision.getPrestation(transaction);
        REOrdresVersements[] ovs = prst.getOrdresVersement(transaction);

        for (int i = 0; i < ovs.length; i++) {
            if (IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(ovs[i].getCsType())) {
                FWCurrency montantOV = new FWCurrency(0);
                if (!JadeStringUtil.isBlankOrZero(ovs[i].getMontant())) {
                    montantOV = new FWCurrency(ovs[i].getMontant());
                }

                if (JadeStringUtil.isBlankOrZero(ovs[i].getIdTiersAdressePmt()) && !montantOV.isZero()) {
                    String nss = "";
                    try {
                        PRTiersWrapper tw = PRTiersHelper.getTiersParId(getSession(), ovs[i].getIdTiers());
                        nss = " NSS : " + tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL);

                    } catch (Exception e) {
                        e.printStackTrace();
                        nss = " NSS non trouvé";
                    }

                    throw new Exception(getSession().getLabel("ADRESSE_PMT_BP_ABSENTE") + nss);
                }
                break;
            }
        }

        if (!JadeStringUtil.isBlankOrZero(decision.getDateDebutRetro())) {
            doValidateOV(transaction, decision);
        }

        loadPrestationsDues(transaction, decision.getDecisionDepuis());

        // Parcours des rentes accordées
        Long idFirstRaFound = null;
        Iterator<Long> iter = idsPrstDuesParRenteAccordee.keySet().iterator();
        if (iter.hasNext()) {
            idFirstRaFound = iter.next();
        }
        if (idFirstRaFound == null) {
            throw new Exception("Aucune rente accordée trouvée pour la décision no : " + decision.getIdDecision());
        }

        /*
         * Validation RETRO
         *
         * 1) Le courant doit avoir été préalablement validé, s'il y en a... 2) La RA doit etre dans l'état PARTIEL OU
         * CALCULE 3) Pas de prestation dues de type RETRO 4) La/les bases de calculs doivent être dans l'état ACTIF 5)
         * La demande de rente doit etre dans l'état PARTIEL s'il y a du courant, CALCULE autrement
         */

        // On charge la demande de rente
        REDemandeRente demande = null;

        if (IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision())) {

            iter = idsPrstDuesParRenteAccordee.keySet().iterator();
            while (iter.hasNext()) {
                Long idRA = iter.next();
                RERenteAccordee ra = new RERenteAccordee();
                ra.setSession(getSession());
                ra.setIdPrestationAccordee(idRA.toString());
                ra.retrieve(transaction);
                assertNotIsNew(ra, "005 - Entity not found");

                // On charge la demande de rente
                if (demande == null) {
                    demande = loadDemandeRente(transaction, ra.getIdBaseCalcul());
                }

                // Plausi 4)
                REBasesCalcul bc = new REBasesCalcul();
                bc.setSession(getSession());
                bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                bc.retrieve(transaction);

                assertNotIsNew(bc, "006 - Entity not found");

                if (!IREBasesCalcul.CS_ETAT_ACTIF.equals(bc.getCsEtat())) {
                    throw new Exception("Base de calcul dans un état différent d'actif pour la décision no : "
                            + decision.getIdDecision());
                }

                String lstIdPrstDues = idsPrstDuesParRenteAccordee.get(idRA);
                List<String> idsPrstDues = JadeStringUtil.split(lstIdPrstDues, "-");

                // Parcours des prestations dues par rente accordée
                Iterator<String> iter2 = idsPrstDues.iterator();
                while (iter2.hasNext()) {
                    String id = iter2.next();
                    id = id.substring(0, id.length() - 1);

                    REPrestationDue pd = new REPrestationDue();
                    pd.setSession(getSession());
                    pd.setIdPrestationDue(id);
                    pd.retrieve(transaction);
                    assertNotIsNew(pd, "007 - Entity not found");

                    // Plausi 1)
                    int courant = checkCourantPrestationDue(pd, decision.getDecisionDepuis());

                    if (courant < 0) {
                        throw new Exception("Vous devez préalablement valider le COURANT pour la décision no : "
                                + decision.getIdDecision());
                    }

                    // Il est possible que la RA validée avec le courant aie été diminuée avant la validation du RETRO
                    // !!!
                    if (((courant == 0)) && (!IREPrestationAccordee.CS_ETAT_DIMINUE.equals(ra.getCsEtat())
                            && !IREPrestationAccordee.CS_ETAT_CALCULE.equals(ra.getCsEtat())
                            && !IREPrestationAccordee.CS_ETAT_PARTIEL.equals(ra.getCsEtat()))) {
                        throw new Exception(
                                "Incohérance dans les données, la RA doit être dans l'état 'CALCULE' pour la décision no : "
                                        + decision.getIdDecision());
                    } else if ((courant == 1) && IREPrestationAccordee.CS_ETAT_DIMINUE.equals(ra.getCsEtat())) {
                        ;// OK
                    }
                    // Plausi 2)
                    else if ((courant > 0) && !IREPrestationAccordee.CS_ETAT_PARTIEL.equals(ra.getCsEtat())) {
                        throw new Exception(
                                "Incohérance dans les données, la RA doit être dans l'état 'PARTIEL' pour la décision no : "
                                        + decision.getIdDecision());
                    }
                    if (IREPrestationDue.CS_TYPE_MNT_TOT.equals(pd.getCsEtat())) {
                        throw new Exception("Prestation Due Wrong type error.");
                    }
                }
            }
        }

        /*
         * Validation COURANT
         *
         *
         * 20) La RA doit etre dans l'état CALCULE 30) Toute les prestations dues de la décision doivent être de type
         * COURANT et se trouver dans l'état ATTENTE. 40) La/les bases de calculs doivent être dans l'état ACTIF 50) La
         * demande de rente doit etre dans l'état CALCULE
         */
        else if (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision())) {

            iter = idsPrstDuesParRenteAccordee.keySet().iterator();
            while (iter.hasNext()) {
                Long idRA = iter.next();
                RERenteAccordee ra = new RERenteAccordee();
                ra.setSession(getSession());
                ra.setIdPrestationAccordee(idRA.toString());
                ra.retrieve(transaction);
                assertNotIsNew(ra, "008 - Entity not found");

                // On charge la demande de rente
                if (demande == null) {
                    demande = loadDemandeRente(transaction, ra.getIdBaseCalcul());
                }

                // Plausi 20)

                if (!IREPrestationAccordee.CS_ETAT_CALCULE.equals(ra.getCsEtat())) {
                    throw new Exception(
                            "Incohérance dans les données, la RA doit être dans l'état 'CALCULE' pour la décision no : "
                                    + decision.getIdDecision());
                }

                // Plausi 40)
                REBasesCalcul bc = new REBasesCalcul();
                bc.setSession(getSession());
                bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                bc.retrieve(transaction);

                assertNotIsNew(bc, "009 - Entity not found");

                if (!IREBasesCalcul.CS_ETAT_ACTIF.equals(bc.getCsEtat())) {
                    throw new Exception("BC Wrong state error");
                }
            }

            // Plausi 30)
            Iterator<String> iter2 = idsPrestationsDues.iterator();
            while (iter2.hasNext()) {
                String idPD = iter2.next();

                REPrestationDue pd = new REPrestationDue();
                pd.setSession(getSession());
                pd.setIdPrestationDue(idPD);
                pd.retrieve(transaction);
                assertNotIsNew(pd, "010 - Entity not found");

                if (!IREPrestationDue.CS_ETAT_ATTENTE.equals(pd.getCsEtat())) {
                    throw new Exception("Prestation Due Wrong state error.");
                }

                if (!IREPrestationDue.CS_TYPE_PMT_MENS.equals(pd.getCsType())) {
                    throw new Exception("Prestation Due Wrong Type error.");
                }
            }

            // Plausi 50)
            if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(demande.getCsEtat())) {
                throw new Exception(
                        "Incohérance dans les données, la demande doit être dans l'état 'CALCULE' pour la décision no : "
                                + decision.getIdDecision());
            }
        }

        /*
         * Validation STANDARD
         *
         * 100) Il ne doit pas y avoir de RETRO ni de COURANT 200) La RA doit etre dans l'état CALCULE 300) Toute les
         * prestations dues de la RA doivent être dans l'état ATTENTE. 400) La/les bases de calculs doivent être dans
         * l'état ACTIF 500) La demande de rente doit etre dans l'état CALCULE ou PARTIEL. Si plusieurs décisions à
         * valider, la 1ère va faire passer la demande de CALCULE à PARTIEL.
         */
        else if (IREDecision.CS_TYPE_DECISION_STANDARD.equals(decision.getCsTypeDecision())) {

            iter = idsPrstDuesParRenteAccordee.keySet().iterator();
            while (iter.hasNext()) {
                Long idRA = iter.next();
                RERenteAccordee ra = new RERenteAccordee();
                ra.setSession(getSession());
                ra.setIdPrestationAccordee(idRA.toString());
                ra.retrieve(transaction);
                assertNotIsNew(ra, "011 - Entity not found");

                // On charge la demande de rente
                if (demande == null) {
                    demande = loadDemandeRente(transaction, ra.getIdBaseCalcul());
                }

                // Plausi 200)
                if (!IREPrestationAccordee.CS_ETAT_CALCULE.equals(ra.getCsEtat())) {
                    throw new Exception(
                            "Incohérence dans les données, la RA doit être dans l'état 'CALCULE' pour la décision no : "
                                    + decision.getIdDecision());
                }

                // Plausi 400)
                REBasesCalcul bc = new REBasesCalcul();
                bc.setSession(getSession());
                bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                bc.retrieve(transaction);

                assertNotIsNew(bc, "012 - Entity not found");

                if (!IREBasesCalcul.CS_ETAT_ACTIF.equals(bc.getCsEtat())) {
                    throw new Exception("BC Wrong state error");
                }

                // Plausi 100)
                REPrestationsDuesJointDemandeRenteManager mgr = new REPrestationsDuesJointDemandeRenteManager();
                mgr.setSession(getSession());
                mgr.setForNoDemandeRente(demande.getIdDemandeRente());
                mgr.setForNoRenteAccordee(idRA.toString());
                mgr.setForCsEtatPrestationDue(IREPrestationDue.CS_ETAT_ACTIF);
                mgr.setForCsTypePrestationDue(IREPrestationDue.CS_TYPE_PMT_MENS);
                mgr.find(2);
                // Il y a du courant validé
                if (mgr.size() > 0) {
                    throw new Exception("Erreur, il ne doit pas y avoir de COURANT déjà validé pour la décision no : "
                            + decision.getIdDecision());
                }

                // Plausi 300)
                String lstIdPrstDues = idsPrstDuesParRenteAccordee.get(idRA);
                List<String> idsPrstDues = JadeStringUtil.split(lstIdPrstDues, "-");
                Iterator<String> iter2 = idsPrstDues.iterator();
                while (iter2.hasNext()) {
                    String id = iter2.next();
                    id = id.substring(0, id.length() - 1);

                    REPrestationDue pd = new REPrestationDue();
                    pd.setSession(getSession());
                    pd.setIdPrestationDue(id);
                    pd.retrieve(transaction);

                    assertNotIsNew(pd, "013 - Entity not found");

                    if (!IREPrestationDue.CS_ETAT_ATTENTE.equals(pd.getCsEtat())) {
                        throw new Exception("Prestation Due Wrong state error.");
                    }
                }
            }

            // Plausi 500)
            if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(demande.getCsEtat())
                    && !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(demande.getCsEtat())) {
                throw new Exception(
                        "Incohérance dans les données, la demande doit être dans l'état 'CALCULE' pour la décision no : "
                                + decision.getIdDecision());
            }

        }
    }

    private void doMiseEnLot(final BSession session, final BTransaction transaction, RELot lot,
                             final REDecisionEntity decision) throws Exception {

        // Récupération de la prestation pour la mettre dans le lot (s'il y en
        // a)
        if (lot == null) {
            // Création du lot de type DECISION si non existant :
            RELotManager mgr = new RELotManager();
            mgr.setSession(getSession());
            mgr.setForCsType(IRELot.CS_TYP_LOT_DECISION);
            mgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
            mgr.setForCsEtat(IRELot.CS_ETAT_LOT_OUVERT);
            mgr.find(transaction);

            if (mgr.size() > 0) {
                lot = (RELot) mgr.getEntity(0);
            } else {
                lot = new RELot();
                lot.setSession(getSession());
                lot.setCsEtatLot(IRELot.CS_ETAT_LOT_OUVERT);
                lot.setCsTypeLot(IRELot.CS_TYP_LOT_DECISION);
                lot.setCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
                lot.setDateCreationLot(JACalendar.todayJJsMMsAAAA());
                lot.add(transaction);
                lot.retrieve(transaction);
                lot.setDescription(getSession().getLabel("VALIDER_DECISION_DESCR_LOT_1") + " "
                        + REPmtMensuel.getDateDernierPmt(getSession()) + " "
                        + getSession().getLabel("VALIDER_DECISION_DESCR_LOT_2") + " " + lot.getIdLot());
                lot.update(transaction);
            }
        }

        REPrestationsManager prstMgr = new REPrestationsManager();
        prstMgr.setSession(getSession());
        prstMgr.setForIdDecision(decision.getIdDecision());
        prstMgr.setForCsEtat(IREPrestations.CS_ETAT_PRE_ATTENTE);
        prstMgr.find(transaction, 2);

        if (prstMgr.size() > 1) {
            throw new Exception("Erreur : multiple prestations pour la décision no : " + decision.getIdDecision());
        }

        if (prstMgr.size() > 0) {
            REPrestations prestation = (REPrestations) prstMgr.getEntity(0);
            prestation.setIdLot(lot.getIdLot());
            prestation.update(transaction);
        }

    }

    private REValiderDecisionVO doPreparationMAJDemandeRente(final REValiderDecisionVO vo, final REDemandeRente demande,
                                                             final String csTypeValidation) throws Exception {

        // Lors de la validation de plusieurs décisions pour une même demande,
        // la demande doit être maj lors du dernier traitement uniquement,
        // autrement le système de validation
        // Générera une erreur lors de traitement de la 2ème décision, car pour
        // pouvoir valider une décision, la demande
        // doit être dans l'état calculé.

        if (IREDecision.CS_TYPE_DECISION_COURANT.equals(csTypeValidation)) {
            vo.addDemandePourMAJEtat(demande.getIdDemandeRente(), IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE);
        } else {
            vo.addDemandePourMAJEtat(demande.getIdDemandeRente(), IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE);
        }
        return vo;
    }

    public REValiderDecisionVO doTraitement(final BTransaction transaction) throws Exception {

        if (!REPmtMensuel.isValidationDecisionAuthorise(getSession())) {
            throw new Exception("L'état du système actuel interdit la validation des décisions.");
        }

        REValiderDecisionVO result = new REValiderDecisionVO();

        REDecisionEntity decision = new REDecisionEntity();
        decision.setSession(getSession());
        decision.setIdDecision(getIdDecision());
        decision.retrieve(transaction);

        doFullValidation(transaction, decision);

        if (transaction.hasErrors() || getSession().hasErrors()) {

            throw new Exception("La validation a echoué pour la décision no : " + decision.getIdDecision());
        }
        // Traitement
        if (IREDecision.CS_TYPE_DECISION_COURANT.equals(decision.getCsTypeDecision())) {
            result = doTraitementCourant(result, decision, transaction);
        } else if (IREDecision.CS_TYPE_DECISION_RETRO.equals(decision.getCsTypeDecision())) {
            result = doTraitementRetro(result, decision, transaction);
        } else {
            result = doTraitementStandard(result, decision, transaction);
        }

        // Tout c'est bien passé, il faut encore créer le compte annexe dans la
        // compta, si pas encore fait...
        // Et ceci pour chaque bénéficiaire de rente accordée... Toutes les RA
        // de cette décisions pointeront
        // sur le compte annexe du bénéficiaire princial

        Set<Long> idsRA = new HashSet<Long>(idsPrstDuesParRenteAccordee.keySet());

        REBeneficiairePrincipal.initComptesAnnexesDesRentesAccordees(getSession(), transaction, idsRA);

        // Diminution des rentes en cours
        // Cf. Doc validationBatch, 10.2 Diminution des rentes en cours lors de la validation de la décision

        REDemandeRente dem = new REDemandeRente();
        dem.setSession(getSession());
        dem.setIdDemandeRente(idDemandeRente);
        dem.retrieve(transaction);
        PRAssert.notIsNew(dem, null);
        String idTiersRequerant = dem.loadDemandePrestation(transaction).getIdTiers();
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(getSession(),
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersRequerant);

        RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager mgr = new RERenteAccordeeJoinInfoComptaJoinPrstDuesJoinDecisionsManager();
        mgr.setSession(getSession());
        mgr.setForIdDecision(decision.getIdDecision());
        mgr.find(transaction, BManager.SIZE_NOLIMIT);

        String idTiersPrincipal = "";
        REPrestations prst = decision.getPrestation(transaction);
        REOrdresVersements[] ovs = prst.getOrdresVersement(transaction);

        for (int i = 0; i < ovs.length; i++) {
            if (IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(ovs[i].getCsType())) {
                idTiersPrincipal = ovs[i].getIdTiers();
                break;
            }
        }

        if (JadeStringUtil.isIntegerEmpty(idTiersPrincipal)) {
            throw new Exception("Le bénéficiaire principal n'a pas été trouvé dans les OV. idDecision = "
                    + decision.getIdDecision());
        }

        ISFMembreFamilleRequerant[] mf = sf.getMembresFamilleRequerant(idTiersRequerant);

        // Récupération des OV de type Dettes
        // Seuls les OV avec le flag isCompense vont diminuer la RA concernée et
        // généré des annonces de diminution.
        for (int i = 0; i < ovs.length; i++) {
            REOrdresVersements ov = ovs[i];
            if ((ov.getCsTypeOrdreVersement() == TypeOrdreVersement.DIMINUTION_DE_RENTE) && ov.getIsCompense()) {

                // Si pas de RA a diminuer, passe au membre suivant !!!
                // Des OV de type Dette peuvent ne pas avoir
                // d'idRenteAccordeeDiminuee dans
                // le cas des compensations inter-décision.
                // En résumé, ceux qui on le flag isCID == true n'ont pas de
                // idRADiminuee

                // Il en va de même pour des Dettes à compenser pour des RA avec
                // date de fin.
                // Dans ce cas, il n'y a pas de RA a diminuer. L'OV est là
                // uniquement pour compenser
                // le montant à restituer.
                if (JadeStringUtil.isBlankOrZero(ov.getIdRenteAccordeeDiminueeParOV())) {
                    continue;
                }

                boolean isMbrFamFound = false;

                for (int k = 0; k < mf.length; k++) {
                    ISFMembreFamilleRequerant membre = mf[k];

                    if (ov.getIdTiers().equals(membre.getIdTiers())) {
                        isMbrFamFound = true;
                        REDemandeRente demRente = new REDemandeRente();
                        demRente.setSession(getSession());
                        demRente.setIdDemandeRente(decision.getIdDemandeRente());
                        demRente.retrieve(transaction);
                        PRAssert.notIsNew(demRente, null);

                        // Contrôle du changement de l'état civil...

                        JADate dateMin = new JADate(demRente.getDateDebut());
                        ISFMembreFamille mfADate = sf.getMembreFamille(membre.getIdMembreFamille(), dateMin.toStr("."));

                        String etatCivilActuel = membre.getCsEtatCivil();
                        String etatCivilPasse = mfADate.getCsEtatCivil();

                        boolean isChangementEtatCivil = false;

                        if ((etatCivilActuel == null) && (etatCivilPasse == null)) {
                            isChangementEtatCivil = false;
                        } else if (((etatCivilActuel == null) && (etatCivilPasse != null))
                                || ((etatCivilActuel != null) && (etatCivilPasse == null))) {
                            isChangementEtatCivil = true;
                        } else if (!etatCivilPasse.equals(etatCivilActuel)) {
                            isChangementEtatCivil = true;
                        }

                        RERenteAccordee raADiminuer = new RERenteAccordee();
                        raADiminuer.setSession(getSession());
                        raADiminuer.setIdPrestationAccordee(ov.getIdRenteAccordeeDiminueeParOV());
                        raADiminuer.retrieve(transaction);
                        PRAssert.notIsNew(raADiminuer, null);

                        for (Long idRA : idsPrstDuesParRenteAccordee.keySet()) {
                            RERenteAccordee ra = new RERenteAccordee();
                            ra.setSession(getSession());
                            ra.setIdPrestationAccordee(idRA.toString());
                            ra.retrieve(transaction);
                            assertNotIsNew(ra, "001 - Entity not found");

                            if (ra.getIdTiersBeneficiaire().equals(raADiminuer.getIdTiersBeneficiaire())) {
                                // MAJ du code mutation
                                if (IREDemandeRente.CS_TYPE_CALCUL_TRANSITOIRE.equals(demRente.getCsTypeCalcul())) {
                                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_PRESTATION_TRANSITOIRE);
                                } else {
                                    raADiminuer = initCodeMutation(ra, raADiminuer, isChangementEtatCivil);
                                }
                            }
                        }

                        // Il n'y a pas de date de fin -> maj de la rente en
                        // cours (ra à diminuer)
                        if (JadeStringUtil.isBlankOrZero(raADiminuer.getDateFinDroit())) {

                            JADate ddNouveauDroit = new JADate(demRente.getDateDebut());
                            raADiminuer.getIdBaseCalcul();
                            REBasesCalcul bc = new REBasesCalcul();
                            bc.setSession(getSession());
                            bc.setIdBasesCalcul(raADiminuer.getIdBaseCalcul());
                            bc.retrieve(transaction);

                            RERenteCalculee rc = new RERenteCalculee();
                            rc.setSession(getSession());
                            rc.setIdRenteCalculee(bc.getIdRenteCalculee());
                            rc.retrieve(transaction);
                            PRAssert.notIsNew(rc, null);

                            REDemandeRente ancienDroit = new REDemandeRente();
                            ancienDroit.setSession(getSession());
                            ancienDroit.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                            ancienDroit.setIdRenteCalculee(rc.getIdRenteCalculee());
                            ancienDroit.retrieve(transaction);

                            PRAssert.notIsNew(ancienDroit, null);
                            JADate ddAncienDroitRAD = new JADate(raADiminuer.getDateDebutDroit());

                            raADiminuer.setCsEtat(IREPrestationAccordee.CS_ETAT_DIMINUE);

                            JADate dfRAaDiminuer = null;
                            /*
                             * Si le nouveau droit débute en 12.2006, l'ancien droit est en cours depuis 02.2007, mettre
                             * la fin de droit 01.2007
                             */
                            JACalendar cal = new JACalendarGregorian();

                            if (cal.compare(ddNouveauDroit, ddAncienDroitRAD) == JACalendar.COMPARE_FIRSTLOWER) {
                                dfRAaDiminuer = new JADate(ddAncienDroitRAD.toStr("."));
                                // Le mois précédent
                                dfRAaDiminuer = cal.addMonths(dfRAaDiminuer, -1);
                                raADiminuer.setDateFinDroit(
                                        PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dfRAaDiminuer.toStrAMJ()));
                            } else {
                                dfRAaDiminuer = new JADate(ddNouveauDroit.toStr("."));
                                // Le mois précédent
                                dfRAaDiminuer = cal.addMonths(dfRAaDiminuer, -1);
                                raADiminuer.setDateFinDroit(
                                        PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(dfRAaDiminuer.toStrAMJ()));
                            }
                            raADiminuer.update(transaction);

                            // MAJ de l'état de la demande !!!
                            String dateDiminution = REDiminutionRenteUtils.getDateDiminutionDemande(getSession(),
                                    transaction, ancienDroit);
                            result.addDemandePourDiminution(ancienDroit.getIdDemandeRente(), dateDiminution);

                            // Création des annonces de diminution

                            REBasesCalculDixiemeRevision bc10 = new REBasesCalculDixiemeRevision();
                            REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();

                            String noRevision;
                            if (bc.isNew()) {
                                throw new Exception(
                                        "Retrieve impossible de la base de calcul (REDiminutionRenteAccordeeProcess");
                            } else {
                                bc10.setSession(getSession());
                                bc10.setIdBasesCalcul(bc.getIdBasesCalcul());
                                bc10.retrieve(transaction);
                                if (bc10.isNew()) {
                                    bc9.setSession(getSession());
                                    bc9.setIdBasesCalcul(bc.getIdBasesCalcul());
                                    bc9.retrieve(transaction);
                                    if (bc9.isNew()) {
                                        throw new Exception(
                                                "Retrieve impossible de la base de calcul (REDiminutionRenteAccordeeProcess");
                                    } else {
                                        noRevision = "9";
                                    }
                                } else {
                                    noRevision = "10";
                                }
                            }

                            // dateMoisRapport = date du dernier paiement + 1
                            // mois.
                            String dateMoisRapport = REPmtMensuel.getDateDernierPmt(getSession());
                            JADate dmr = new JADate(dateMoisRapport);
                            dmr = cal.addMonths(dmr, 1);

                            // Cas des décisions prise en avances de 2 mois. Le
                            // mois de rapport de l'annonce de diminution
                            // doit être 1 mois plus grand que le mois de la
                            // diminution.
                            if ((cal.compare(dfRAaDiminuer, dmr) == JACalendar.COMPARE_FIRSTUPPER)
                                    || (cal.compare(dfRAaDiminuer, dmr) == JACalendar.COMPARE_EQUALS)) {

                                dmr = cal.addMonths(dfRAaDiminuer, 1);
                            }

                            dateMoisRapport = PRDateFormater.convertDate_AAAAMMJJ_to_AAAAMM(dmr.toStrAMJ());
                            dateMoisRapport = PRDateFormater.convertDate_AAAAMM_to_MMAA(dateMoisRapport);

                            PRTiersWrapper tier = PRTiersHelper.getTiersParId(getSession(),
                                    raADiminuer.getIdTiersBeneficiaire());

                            if (null == tier) {
                                throw new Exception("Tier introuvable (REDiminutionRenteAccordeeProcess)");
                            }

                            String idAnnonce = "";

                            if (noRevision.equals("10")) {
                                // Créer annonce pour 10 ème révision si bc =
                                // 10ème révision

                                REAnnoncesDiminution10Eme annonce10Eme = new REAnnoncesDiminution10Eme();
                                annonce10Eme.setSession(getSession());
                                annonce10Eme.setCodeApplication("45");
                                annonce10Eme.setCodeEnregistrement01("01");
                                annonce10Eme.setNumeroCaisse(getSession().getApplication().getProperty("noCaisse"));
                                annonce10Eme.setNumeroAgence(getSession().getApplication().getProperty("noAgence"));
                                annonce10Eme.setNumeroAnnonce("");
                                annonce10Eme.setReferenceCaisseInterne("DIM" + getSession().getUserId().toUpperCase());
                                annonce10Eme.setNoAssAyantDroit(
                                        NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
                                annonce10Eme.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                annonce10Eme.setGenrePrestation(raADiminuer.getCodePrestation());

                                int lengthMontant = String
                                        .valueOf(new FWCurrency(raADiminuer.getMontantPrestation()).intValue())
                                        .length();
                                int nbZeroAajouter = 5 - lengthMontant;

                                String montant = String
                                        .valueOf(new FWCurrency(raADiminuer.getMontantPrestation()).intValue());

                                for (int j = 0; j < nbZeroAajouter; j++) {
                                    montant = "0" + montant;
                                }

                                annonce10Eme.setMensualitePrestationsFrancs(montant);

                                annonce10Eme.setFinDroit(PRDateFormater.convertDate_AAAAMM_to_MMAA(
                                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(raADiminuer.getDateFinDroit())));
                                annonce10Eme.setMoisRapport(dateMoisRapport);
                                annonce10Eme.setCodeMutation(raADiminuer.getCodeMutation());
                                annonce10Eme.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session,
                                        annonce10Eme, null, dateMoisRapport);
                                StringBuffer buff = new StringBuffer();
                                while ((erreurs != null) && erreurs.hasMoreElements()) {
                                    AnnonceErreur erreur = erreurs.nextElement();
                                    buff.append(erreur.getMessage()).append("\n");
                                }
                                if (buff.length() > 0) {
                                    throw new Exception(buff.toString());
                                }
                                if(raADiminuer.contientCodeCasSpecial("60")){
                                    PRTiersWrapper tierCompl = PRTiersHelper.getTiersParId(getSession(),
                                            raADiminuer.getIdTiersComplementaire1());
                                    annonce10Eme.setPremierNoAssComplementaire(
                                            NSUtil.unFormatAVS(tierCompl.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
                                }

                                annonce10Eme.add(transaction);

                                idAnnonce = annonce10Eme.getIdAnnonce();

                            } else if (noRevision.equals("9")) {
                                // Créer annonce pour 9 ème révision si bc =
                                // 9ème révision

                                REAnnoncesDiminution9Eme annonce9Eme = new REAnnoncesDiminution9Eme();
                                annonce9Eme.setSession(getSession());
                                annonce9Eme.setCodeApplication("42");
                                annonce9Eme.setCodeEnregistrement01("01");
                                annonce9Eme.setNumeroCaisse(getSession().getApplication().getProperty("noCaisse"));
                                annonce9Eme.setNumeroAgence(getSession().getApplication().getProperty("noAgence"));
                                annonce9Eme.setNumeroAnnonce("");
                                annonce9Eme.setReferenceCaisseInterne("DIM" + getSession().getUserId().toUpperCase());
                                annonce9Eme.setNoAssAyantDroit(
                                        NSUtil.unFormatAVS(tier.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
                                annonce9Eme.setIdTiers(tier.getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                annonce9Eme.setGenrePrestation(raADiminuer.getCodePrestation());

                                int lengthMontant = String
                                        .valueOf(new FWCurrency(raADiminuer.getMontantPrestation()).intValue())
                                        .length();
                                int nbZeroAajouter = 5 - lengthMontant;

                                String montant = String
                                        .valueOf(new FWCurrency(raADiminuer.getMontantPrestation()).intValue());

                                for (int j = 0; j < nbZeroAajouter; j++) {
                                    montant = "0" + montant;
                                }

                                annonce9Eme.setMensualitePrestationsFrancs(montant);

                                annonce9Eme.setFinDroit(PRDateFormater.convertDate_AAAAMM_to_MMAA(
                                        PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(raADiminuer.getDateFinDroit())));
                                annonce9Eme.setMoisRapport(dateMoisRapport);
                                annonce9Eme.setCodeMutation(raADiminuer.getCodeMutation());
                                annonce9Eme.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session,
                                        annonce9Eme, null, dateMoisRapport);
                                StringBuffer buff = new StringBuffer();
                                while ((erreurs != null) && erreurs.hasMoreElements()) {
                                    AnnonceErreur erreur = erreurs.nextElement();
                                    buff.append(erreur.getMessage()).append("\n");
                                }
                                if (buff.length() > 0) {
                                    throw new Exception(buff.toString());
                                }
                                if(raADiminuer.contientCodeCasSpecial("60")){
                                    PRTiersWrapper tierCompl = PRTiersHelper.getTiersParId(getSession(),
                                            raADiminuer.getIdTiersComplementaire1());
                                    annonce9Eme.setPremierNoAssComplementaire(
                                            NSUtil.unFormatAVS(tierCompl.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
                                }
                                annonce9Eme.add(transaction);

                                idAnnonce = annonce9Eme.getIdAnnonce();

                            } else {
                                throw new Exception(
                                        "Erreur dans la diminution des rentes accordées, no de révision invalide : "
                                                + noRevision + " pour la décision no : " + decision.getIdDecision());
                            }

                            // Création de l'annonce de rente pour la liaison
                            // entre les ra et les annonces
                            REAnnonceRente annonceRente = new REAnnonceRente();
                            annonceRente.setSession(getSession());
                            annonceRente.setIdAnnonceHeader(idAnnonce);
                            annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
                            annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
                            annonceRente.setIdRenteAccordee(raADiminuer.getIdPrestationAccordee());
                            annonceRente.add(transaction);
                        }
                    }
                }
                if (!isMbrFamFound) {
                    throw new Exception("Aucun membre de la famille trouvé pour compenser l'OV id = "
                            + ov.getIdOrdreVersement() + " pour la décision no : " + decision.getIdDecision());
                }
            }
        }

        return result;
    }

    /**
     * @param session
     * @param getTransaction
     *            ()
     * @param annonce
     * @return true si l'annonce liée à la decision du courant à été trouvée.
     * @throws Exception
     */
    private boolean doTraitementAnnoncesPourRetro(final BSession session, final BTransaction transaction,
                                                  final REAnnonceRente annonce, final REDecisionEntity decision) throws Exception {

        boolean isDecisionForCourantFound = true;

        // Contrôler qu'il s'agisse d'une annonce d'augmentation
        // Il serait possible que la prestation courante soit diminuée alors que
        // le rétro n'ait pas encore été validé !!!
        // Dans ce cas, l'on trouverait une annonce d'augmentation et une
        // annonce de diminution.
        // Evt. des annonce de modification (ponctuelle...)
        annonce.retrieve(transaction);
        REAnnoncesAugmentationModification10Eme annonce44 = new REAnnoncesAugmentationModification10Eme();
        annonce44.setSession(getSession());
        annonce44.setIdAnnonce(annonce.getIdAnnonceHeader());
        annonce44.retrieve(transaction);

        REAnnoncesAugmentationModification9Eme annonce41 = new REAnnoncesAugmentationModification9Eme();
        //
        if (annonce44.isNew()) {
            annonce41.setSession(getSession());
            annonce41.setIdAnnonce(annonce.getIdAnnonceHeader());
            annonce41.retrieve(transaction);
            //
            if (annonce41.isNew()) {
                isDecisionForCourantFound = false;
            }
        }

        if (isDecisionForCourantFound) {
            String idAnnonce4xNew = "";

            // Création d'une annonce de rente identique à celle modifiée (pour
            // le rétro)
            REAnnonceRente annonceNew = new REAnnonceRente();
            annonceNew.setSession(getSession());
            annonceNew.setIdRenteAccordee(annonce.getIdRenteAccordee());
            annonceNew.setCsTraitement(annonce.getCsTraitement());
            annonceNew.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);

            // Annonce 44
            if (!annonce44.isNew()) {
                // Création de la copie pour l'enregistrement 01 des annonces 44
                // !
                REAnnoncesAugmentationModification10Eme annonce4401new = createAnnonce44(getSession(), annonce44);
                annonce4401new.setEtat(IREAnnonces.CS_ETAT_OUVERT);

                REPrestationsAccordees pa = new REPrestationsAccordees();
                pa.setSession(getSession());
                pa.setIdPrestationAccordee(annonce.getIdRenteAccordee());
                pa.retrieve(transaction);
                PRAssert.notIsNew(pa, "Rente accordée non trouvée. idARC / idRA = " + annonce.getIdAnnonceRente() + "/"
                        + annonce.getIdRenteAccordee());

                String dMMAA = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(pa.getDateDebutDroit());
                dMMAA = PRDateFormater.convertDate_AAAAMM_to_MMAA(dMMAA);
                annonce4401new.setDebutDroit(dMMAA);

                dMMAA = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(decision.getDateFinRetro());
                dMMAA = PRDateFormater.convertDate_AAAAMM_to_MMAA(dMMAA);
                annonce4401new.setFinDroit(dMMAA);
                annonce4401new.setMensualitePrestationsFrancs(annonce44.getMensualitePrestationsFrancs());

                String dateMoisRapport = REPmtMensuel.getDateDernierPmt(getSession());
                dateMoisRapport = PRDateFormater
                        .convertDate_AAAAMM_to_MMAA(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(dateMoisRapport));

                annonce4401new.setMoisRapport(dateMoisRapport);
                annonce4401new.setCodeMutation(IREAnnonces.CODE_MUTATION_AUTRE_EVENEMENT);

                annonce4401new.add(transaction);

                idAnnonce4xNew = annonce4401new.getIdAnnonce();

                REAnnoncesAugmentationModification10Eme annonce4402 = new REAnnoncesAugmentationModification10Eme();
                annonce4402.setSession(getSession());
                annonce4402.setIdAnnonce(annonce44.getIdLienAnnonce());
                annonce4402.retrieve(transaction);

                // Création de la copie pour l'enregistrement 02 des annonces 44
                // !
                REAnnoncesAugmentationModification10Eme annonce4402new = createAnnonce44(getSession(), annonce4402);
                annonce4402new.setDebutDroit("");
                annonce4402new.setFinDroit("");
                annonce4402new.setMoisRapport("");
                annonce4402new.setCodeMutation("");
                annonce4402new.setEtat(IREAnnonces.CS_ETAT_OUVERT);
                annonce4402new.add(transaction);

                // Mise à jour de l'idLien de l'enr. 01
                annonce4401new.setIdLienAnnonce(annonce4402new.getId());
                annonce4401new.update(transaction);

                // Validation des annonces
                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, annonce4401new,
                        annonce4402new, dateMoisRapport);
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }
                if (buff.length() > 0) {
                    throw new Exception(buff.toString());
                }

            }
            // Traitement annonce 41
            else {

                String dateMoisRapport = REPmtMensuel.getDateDernierPmt(getSession());
                dateMoisRapport = PRDateFormater
                        .convertDate_AAAAMM_to_MMAA(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAAMM(dateMoisRapport));

                // Création de la copie pour l'enregistrement 01 des annonces 41
                // !
                REAnnoncesAugmentationModification9Eme annonce4101new = createAnnonce41(getSession(), annonce41);

                REPrestationsAccordees pa = new REPrestationsAccordees();
                pa.setSession(getSession());
                pa.setIdPrestationAccordee(annonce.getIdRenteAccordee());
                pa.retrieve(transaction);
                PRAssert.notIsNew(pa, "Rente accordée non trouvée. idARC / idRA = " + annonce.getIdAnnonceRente() + "/"
                        + annonce.getIdRenteAccordee());

                String dMMAA = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(pa.getDateDebutDroit());
                dMMAA = PRDateFormater.convertDate_AAAAMM_to_MMAA(dMMAA);
                annonce4101new.setDebutDroit(dMMAA);

                dMMAA = PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(decision.getDateFinRetro());
                dMMAA = PRDateFormater.convertDate_AAAAMM_to_MMAA(dMMAA);
                annonce4101new.setFinDroit(dMMAA);
                annonce4101new.setMensualitePrestationsFrancs(annonce41.getMensualitePrestationsFrancs());

                annonce4101new.setMoisRapport(dateMoisRapport);
                annonce4101new.setCodeMutation(IREAnnonces.CODE_MUTATION_AUTRE_EVENEMENT);
                annonce4101new.setEtat(IREAnnonces.CS_ETAT_OUVERT);
                annonce4101new.add(transaction);

                idAnnonce4xNew = annonce4101new.getId();

                REAnnoncesAugmentationModification9Eme annonce4102 = new REAnnoncesAugmentationModification9Eme();
                annonce4102.setSession(getSession());
                annonce4102.setIdAnnonce(annonce41.getIdLienAnnonce());
                annonce4102.retrieve(transaction);

                // Création de la copie pour l'enregistrement 02 des annonces 41
                // !
                REAnnoncesAugmentationModification9Eme annonce4102new = createAnnonce41(getSession(), annonce4102);
                annonce4102new.setDebutDroit("");
                annonce4102new.setFinDroit("");
                annonce4102new.setMoisRapport("");
                annonce4102new.setCodeMutation("");
                annonce4102new.setEtat(IREAnnonces.CS_ETAT_OUVERT);
                REArcFrenchValidator frenchValidator = new REArcFrenchValidator();
                annonce4102new = frenchValidator.validateARC_41_02(annonce4102new);

                annonce4102new.add(transaction);

                // Mise à jour de l'idLien de l'enr. 01
                annonce4101new.setIdLienAnnonce(annonce4102new.getId());
                annonce4101new.update(transaction);

                // Validation des annonces
                Enumeration<AnnonceErreur> erreurs = REAnakinParser.getInstance().parse(session, annonce4101new,
                        annonce4102new, dateMoisRapport);
                StringBuffer buff = new StringBuffer();
                while ((erreurs != null) && erreurs.hasMoreElements()) {
                    AnnonceErreur erreur = erreurs.nextElement();
                    buff.append(erreur.getMessage()).append("\n");
                }
                if (buff.length() > 0) {
                    throw new Exception(buff.toString());
                }

            }

            annonceNew.setIdAnnonceHeader(idAnnonce4xNew);
            annonceNew.add(transaction);
        }

        return isDecisionForCourantFound;
    }

    /*
     *
     * Validation de la décision (Type = COURANT)
     */
    protected REValiderDecisionVO doTraitementCourant(REValiderDecisionVO vo, final REDecisionEntity decision,
                                                      final BTransaction transaction) throws Exception {
        // Pour une demande avec 2 rente accordée, il est possible que le RA1
        // ait du rétro et pas la RA2.
        // Il est donc nécessaire de distinguer le rétro par rente accordée, du
        // rétro par demande.

        // Indique s'il y a du retro dans la rente accordée.
        boolean isRetroDansRA = false;

        REDemandeRente demande = null;
        RELot lot = null;

        for (Long idRA : idsPrstDuesParRenteAccordee.keySet()) {

            // Mise à jour des annonces liées au ra
            REAnnonceRenteManager annonceMgr = new REAnnonceRenteManager();
            annonceMgr.setSession(getSession());
            annonceMgr.setForIdRenteAccordee(idRA.toString());
            annonceMgr.find(transaction, BManager.SIZE_NOLIMIT);

            // si pa d'annonce pour la RA, on en cree une
            if (annonceMgr.size() == 0) {
                REValiderDecisionHandler.createAnnonce4x(session, transaction, idRA);
                annonceMgr.find(transaction, BManager.SIZE_NOLIMIT);
            }

            for (REAnnonceRente annonce : annonceMgr.getContainerAsList()) {
                annonce.retrieve(transaction);
                annonce.setIdDecision(decision.getIdDecision());
                annonce.update(transaction);

                REAnnoncesAbstractLevel1A ann = new REAnnoncesAbstractLevel1A();
                ann.setSession(getSession());
                ann.setIdAnnonce(annonce.getIdAnnonceHeader());
                ann.retrieve(transaction);

                if (!ann.isNew()) {

                    // L'annonce pour la décision du courant aura pour mois de
                    // rapport et date de début
                    // Le mois en cours.
                    ann.setMoisRapport(decision.getDecisionDepuis());
                    ann.setDebutDroit(PRDateFormater.convertDate_AAAAMM_to_MMAA(
                            PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(decision.getDecisionDepuis())));
                    ann.update(transaction);

                } else {
                    throw new Exception(
                            "Erreur dans le traitement des annonces pour la décision no : " + decision.getIdDecision());
                }
            }

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(idRA.toString());
            ra.retrieve(transaction);
            assertNotIsNew(ra, "002 - Entity not found");
            if (demande == null) {
                demande = loadDemandeRente(transaction, ra.getIdBaseCalcul());
            }

            isRetroDansRA = isRetroDansRA(demande.getIdDemandeRente(), idRA.toString());

            // MAJ de la rente accordée...
            if (isRetroDansRA) {
                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_PARTIEL);
            } else {
                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
            }
            ra.update(transaction);

            // On set le role rentier du tiers bénéficiaire de la rente
            // accordée...
            initRoleTiersRentier(getSession(), transaction, ra.getIdTiersBeneficiaire());

            doMiseEnLot(getSession(), transaction, lot, decision);

            // MAJ des prestations dues de la rente accordée
            // Ne contient que le 'mensuel ($p)'
            String lstIdPrstDues = idsPrstDuesParRenteAccordee.get(idRA);
            List<String> idsPrstDues = JadeStringUtil.split(lstIdPrstDues, "-");

            Iterator<String> iter2 = idsPrstDues.iterator();
            while (iter2.hasNext()) {
                String id = iter2.next();
                id = id.substring(0, id.length() - 1);

                REPrestationDue pd = new REPrestationDue();
                pd.setSession(getSession());
                pd.setIdPrestationDue(id);
                pd.retrieve(transaction);

                /*
                 * result -1 : courant, mais n'est pas validé 1 :courant qui est validé 0 : pas de courant
                 */

                int result = checkCourantPrestationDue(pd, decision.getDecisionDepuis());
                // Courant déjà validé
                if (result == 1) {
                    throw new Exception(
                            "Le courant à déjà été validé pour la décision no : " + decision.getIdDecision());
                }
                // Courant non validé
                else if (result == -1) {
                    pd.setCsEtat(IREPrestationDue.CS_ETAT_ACTIF);
                }
                // Pas de courant
                else {
                    ;
                }
                pd.update(transaction);
            }
        }

        // MAJ de la demande de rente
        vo = doPreparationMAJDemandeRente(vo, demande, IREDecision.CS_TYPE_DECISION_COURANT);

        // MAJ de la décisin
        decision.setCsEtat(IREDecision.CS_ETAT_VALIDE);
        decision.setDateValidation(JACalendar.todayJJsMMsAAAA());
        decision.setValidePar(getSession().getUserId());
        decision.setCsTypeDecision(IREDecision.CS_TYPE_DECISION_COURANT);
        decision.update(transaction);

        return vo;
    }

    /*
     *
     * Validation de la décision (Type = RETRO)
     */
    protected REValiderDecisionVO doTraitementRetro(REValiderDecisionVO vo, final REDecisionEntity decision,
                                                    final BTransaction transaction) throws Exception {

        REDemandeRente demande = null;

        RELot lot = null;

        for (Long idRA : idsPrstDuesParRenteAccordee.keySet()) {

            // Mise à jour des annonces liées au ra
            REAnnonceRenteManager annonceMgr = new REAnnonceRenteManager();
            annonceMgr.setSession(getSession());
            annonceMgr.setForIdRenteAccordee(idRA.toString());
            annonceMgr.find(transaction, BManager.SIZE_NOLIMIT);

            // On recherche l'annonce d'augmentation créé lors de la validation
            // du courant.
            // Si inexistante, on la créée
            if (annonceMgr.size() == 0) {
                createAnnonce4x(session, transaction, idRA);
            }

            for (REAnnonceRente annonce : annonceMgr.getContainerAsList()) {
                REAnnonceHeader annonceHeader = new REAnnonceHeader();
                annonceHeader.setSession(getSession());
                annonceHeader.setIdAnnonce(annonce.getId());
                annonceHeader.retrieve();

                if (Objects.nonNull(annonceHeader) && (annonceHeader.getCodeApplication()=="41" || annonceHeader.getCodeApplication()=="44")) {
                    if (!JadeStringUtil.isBlankOrZero(annonce.getIdDecision())) {

                        REDecisionEntity decCourant = new REDecisionEntity();
                        decCourant.setSession(getSession());
                        decCourant.setIdDecision(annonce.getIdDecision());
                        decCourant.retrieve(transaction);
                        PRAssert.notIsNew(decCourant, null);



                        if (!IREDecision.CS_TYPE_DECISION_COURANT.equals(decCourant.getCsTypeDecision())) {
                            continue;
                        } else {
                            doTraitementAnnoncesPourRetro(getSession(), transaction, annonce, decision);
                        }
                    }
                    // Pas d'annonce pour le courant. Peut arriver, si que de rétro,
                    // pour un des enfants par exemple!!!!
                    else {

                        REAnnoncesAbstractLevel1A ann = new REAnnoncesAbstractLevel1A();
                        ann.setSession(getSession());
                        ann.setIdAnnonce(annonce.getIdAnnonceHeader());
                        ann.retrieve(transaction);

                        if (!ann.isNew() && !IREAnnonces.CS_ETAT_ENVOYE.equals(ann.getEtat())) {
                            ann.setMoisRapport(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                                    .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(getSession()))));

                            ann.update(transaction);

                            annonce.retrieve(transaction);
                            annonce.setIdDecision(decision.getIdDecision());
                            annonce.update(transaction);

                        } else if (ann.isNew()) {
                            throw new Exception("Error #665978 : Annonce not found");
                        }
                    }
                }
            }

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(idRA.toString());
            ra.retrieve(transaction);
            assertNotIsNew(ra, "003 - Entity not found");
            if (demande == null) {
                demande = loadDemandeRente(transaction, ra.getIdBaseCalcul());
            }

            // MAJ de la rente accordée
            // Il est possible que la RA validée (PARTIEL) lors de la validation du courant
            // aie été diminuée avant la validation du rétro. Dans ce cas, elle ne doit pas repasser dans l'état VALIDE.
            if (!IREPrestationAccordee.CS_ETAT_DIMINUE.equals(ra.getCsEtat())) {
                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
                ra.update(transaction);
            }

            // On set le role rentier du tiers bénéficiaire de la rente
            // accordée...
            initRoleTiersRentier(getSession(), transaction, ra.getIdTiersBeneficiaire());

            doMiseEnLot(getSession(), transaction, lot, decision);

            // MAJ des prestations dues de la rente accordée
            String lstIdPrstDues = idsPrstDuesParRenteAccordee.get(idRA);
            List<String> idsPrstDues = JadeStringUtil.split(lstIdPrstDues, "-");
            Iterator<String> iter2 = idsPrstDues.iterator();
            while (iter2.hasNext()) {
                String id = iter2.next();
                id = id.substring(0, id.length() - 1);

                REPrestationDue pd = new REPrestationDue();
                pd.setSession(getSession());
                pd.setIdPrestationDue(id);
                pd.retrieve(transaction);

                if (IREPrestationDue.CS_TYPE_MNT_TOT.equals(pd.getCsType())) {
                    throw new Exception("Error during prevalidation, wrong prestation type");
                }

                int result = checkCourantPrestationDue(pd, decision.getDecisionDepuis());

                // -1 : courant, mais n'est pas validé
                // 1 :courant qui est validé
                // 0 : pas de courant
                if (result == -1) {
                    throw new Exception(
                            "Le courant n'a pas encore été validé pour la décision no : " + decision.getIdDecision());
                }

                // Pas de courant, on valide donc le rétro
                // cad que l'on set l'état de la prestation de type 'mensuel'
                // dans l'état TRAITE.
                else {
                    pd.setCsEtat(IREPrestationDue.CS_ETAT_TRAITE);
                    pd.update(transaction);
                }
            }
        }

        // MAJ de la demande de rente
        vo = doPreparationMAJDemandeRente(vo, demande, IREDecision.CS_TYPE_DECISION_RETRO);

        // MAJ de la décisin
        decision.setCsEtat(IREDecision.CS_ETAT_VALIDE);
        decision.setDateValidation(JACalendar.todayJJsMMsAAAA());
        decision.setValidePar(getSession().getUserId());
        decision.setCsTypeDecision(IREDecision.CS_TYPE_DECISION_RETRO);
        decision.update(transaction);

        return vo;
    }

    /*
     *
     * Validation de la décision (Type = STANDARD)
     */
    protected REValiderDecisionVO doTraitementStandard(REValiderDecisionVO vo, final REDecisionEntity decision,
                                                       final BTransaction transaction) throws Exception {

        REDemandeRente demande = null;

        RELot lot = null;

        for (Long idRA : idsPrstDuesParRenteAccordee.keySet()) {

            // Mise à jour des annonces liées au ra
            REAnnonceRenteManager annonceMgr = new REAnnonceRenteManager();
            annonceMgr.setSession(getSession());
            annonceMgr.setForIdRenteAccordee(idRA.toString());
            annonceMgr.find(transaction, BManager.SIZE_NOLIMIT);

            // si pa d'annonce pour la RA, on en cree une
            if (annonceMgr.size() == 0) {
                REValiderDecisionHandler.createAnnonce4x(session, transaction, idRA);
                annonceMgr.find(transaction, BManager.SIZE_NOLIMIT);
            }

            for (REAnnonceRente annonce : annonceMgr.getContainerAsList()) {
                annonce.retrieve(transaction);
                annonce.setIdDecision(decision.getIdDecision());
                annonce.update(transaction);

                REAnnoncesAbstractLevel1A ann = new REAnnoncesAbstractLevel1A();
                ann.setSession(getSession());
                ann.setIdAnnonce(annonce.getIdAnnonceHeader());
                ann.retrieve(transaction);

                if (!ann.isNew()) {

                    JACalendar cal = new JACalendarGregorian();

                    // Si date début > date du dernier pmt, mettre date début
                    if (cal.compare(PRDateFormater.convertDate_MMAA_to_MMxAAAA(ann.getDebutDroit()),
                            REPmtMensuel.getDateDernierPmt(getSession())) == JACalendar.COMPARE_FIRSTUPPER) {

                        ann.setMoisRapport(ann.getDebutDroit());

                    } else {
                        ann.setMoisRapport(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                                .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(getSession()))));
                    }

                    ann.update(transaction);
                }
            }

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(getSession());
            ra.setIdPrestationAccordee(idRA.toString());
            ra.retrieve(transaction);
            assertNotIsNew(ra, "004 - Entity not found");
            if (demande == null) {
                demande = loadDemandeRente(transaction, ra.getIdBaseCalcul());
            }

            // MAJ de la rente accordée
            ra.setCsEtat(IREPrestationAccordee.CS_ETAT_VALIDE);
            ra.update(transaction);

            // On set le role rentier du tiers bénéficiaire de la rente
            // accordée...
            initRoleTiersRentier(getSession(), transaction, ra.getIdTiersBeneficiaire());

            doMiseEnLot(getSession(), transaction, lot, decision);

            // MAJ des prestations dues de la rente accordée
            String lstIdPrstDues = idsPrstDuesParRenteAccordee.get(idRA);
            List<String> idsPrstDues = JadeStringUtil.split(lstIdPrstDues, "-");

            Iterator<String> iter2 = idsPrstDues.iterator();
            while (iter2.hasNext()) {
                // boolean isRetro = false;

                String id = iter2.next();
                id = id.substring(0, id.length() - 1);

                REPrestationDue pd = new REPrestationDue();
                pd.setSession(getSession());
                pd.setIdPrestationDue(id);
                pd.retrieve(transaction);

                if (IREPrestationDue.CS_TYPE_MNT_TOT.equals(pd.getCsType())) {
                    throw new Exception("Error during prevalidation, wrong prestation type");
                }

                /*
                 * result -1 : courant, mais n'est pas validé 1 :courant qui est validé 0 : pas de courant
                 */

                int result = checkCourantPrestationDue(pd, decision.getDecisionDepuis());
                // C'est du courant déjà validé
                if (result == 1) {
                    throw new Exception("Le courant à déjà été validé");
                }
                // C'est du courant non validé
                else if (result == -1) {
                    pd.setCsEtat(IREPrestationDue.CS_ETAT_ACTIF);
                    pd.update(transaction);
                }
                // C'est du rétro
                // cad que l'on set l'état de la prestation de type 'mensuel'
                // dans l'état TRAITE.
                else {
                    pd.setCsEtat(IREPrestationDue.CS_ETAT_TRAITE);
                    pd.update(transaction);

                }

            }

        }
        // MAJ de la demande de rente
        vo = doPreparationMAJDemandeRente(vo, demande, IREDecision.CS_TYPE_DECISION_STANDARD);

        // MAJ de la décisin
        decision.retrieve(transaction);
        decision.setCsEtat(IREDecision.CS_ETAT_VALIDE);
        decision.setDateValidation(JACalendar.todayJJsMMsAAAA());
        decision.setValidePar(getSession().getUserId());
        decision.setCsTypeDecision(IREDecision.CS_TYPE_DECISION_STANDARD);
        decision.update(transaction);

        return vo;
    }

    private void doValidateOV(final BTransaction transaction, final REDecisionEntity decision) throws Exception {

        REPrestations prestation = decision.getPrestation(transaction);
        REOrdresVersements[] ovs = prestation.getOrdresVersement(transaction);

        FWCurrency cumulCreanciers = new FWCurrency(0);
        // Cumul des dettes de la CA et rentes en cours
        // FWCurrency cumulDettes = new FWCurrency(0);

        FWCurrency montantIS = new FWCurrency(0);
        FWCurrency montantIM = new FWCurrency(0);
        FWCurrency montantBenefPrincipal = new FWCurrency(0);
        FWCurrency montantPrestation = new FWCurrency(prestation.getMontantPrestation());

        // Pour les décisions sans rétro, pas d'OV dans la prestation.
        if (ovs.length == 0) {
            return;

        }
        for (int i = 0; i < ovs.length; i++) {
            REOrdresVersements ov = ovs[i];
            if (IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE.equals(ov.getCsType())
                    || IREOrdresVersements.CS_TYPE_TIERS.equals(ov.getCsType())) {
                cumulCreanciers.add(ov.getMontant());
            } else if (IREOrdresVersements.CS_TYPE_IMPOT_SOURCE.equals(ov.getCsType())) {
                montantIS.add(ov.getMontant());
            } else if (IREOrdresVersements.CS_TYPE_INTERET_MORATOIRE.equals(ov.getCsType())) {
                montantIM.add(ov.getMontant());
            } else if (IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL.equals(ov.getCsType())) {
                montantBenefPrincipal.add(ov.getMontant());
            }
        }

        // Si le dettes des créanciers est > montant de la prestation... erreur
        if (cumulCreanciers.compareTo(montantPrestation) == 1) {
            throw new Exception(
                    "Erreur: Le montant total des créances est supérieur au montant de la prestation pour la décision no : "
                            + decision.getIdDecision());
        }

        montantPrestation.sub(montantBenefPrincipal);
        montantPrestation.sub(cumulCreanciers);
        montantPrestation.sub(montantIS);
        montantPrestation.sub(montantIM);
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdDemandeRente() {
        return idDemandeRente;
    }

    public BSession getSession() {
        return session;
    }

    /**
     * Met à jours le champ code mutation de la RA à diminuer
     *
     * @param raCourante
     * @param raADiminuer
     * @param isChangementEtatCivil
     * @return
     * @throws Exception
     */
    private RERenteAccordee initCodeMutation(final RERenteAccordee raCourante, final RERenteAccordee raADiminuer,
                                             final boolean isChangementEtatCivil) throws Exception {

        if (raCourante.getIdTiersBeneficiaire().equals(raADiminuer.getIdTiersBeneficiaire())) {

            if (REGenresPrestations.GENRE_10.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_13.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_20.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_23.equals(raCourante.getCodePrestation())) {
                if (REGenrePrestationEnum.groupe1.contains(raADiminuer.getCodePrestation())) {
                    if (REGenresPrestations.GENRE_50.equals(raADiminuer.getCodePrestation())
                            || REGenresPrestations.GENRE_70.equals(raADiminuer.getCodePrestation())
                            || REGenresPrestations.GENRE_72.equals(raADiminuer.getCodePrestation())) {
                        raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_CONVERSION_PRST_AI_EN_AVS);
                    } else {
                        // Si changement d'état civil dans le mois de début de
                        // la nouvelle rente...
                        if (isChangementEtatCivil) {
                            raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_CHGMT_ETAT_CIVIL);
                        }
                    }
                }
            } else if (REGenresPrestations.GENRE_50.equals(raCourante.getCodePrestation())) {
                if (REGenresPrestations.GENRE_10.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_13.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_20.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_23.equals(raADiminuer.getCodePrestation())) {
                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_CONVERSION_PRST_AI_EN_AVS);
                } else if (REGenresPrestations.GENRE_50.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_70.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_72.equals(raADiminuer.getCodePrestation())) {
                    // Modification de la fraction
                    if ((raCourante.getFractionRente() != null)
                            && !raCourante.getFractionRente().equals(raADiminuer.getFractionRente())) {
                        raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_CONVERSION_RENTE_ENTIERE);
                    } else if (isChangementEtatCivil) {
                        raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_CHGMT_ETAT_CIVIL);
                    }
                }
            } else if (REGenresPrestations.GENRE_70.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_72.equals(raCourante.getCodePrestation())) {
                if (REGenresPrestations.GENRE_10.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_13.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_20.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_23.equals(raADiminuer.getCodePrestation())) {
                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_CONVERSION_PRST_AI_EN_AVS);
                } else if (REGenresPrestations.GENRE_50.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_70.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_72.equals(raADiminuer.getCodePrestation())) {
                    // Modification de la fraction
                    if ((raCourante.getFractionRente() != null)
                            && !raCourante.getFractionRente().equals(raADiminuer.getFractionRente())) {
                        raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_CONVERSION_RENTE_ENTIERE);
                    } else if (isChangementEtatCivil) {
                        raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_CHGMT_ETAT_CIVIL);
                    }
                }
            } else if (REGenresPrestations.GENRE_14.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_24.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_34.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_54.equals(raCourante.getCodePrestation())) {
                if (REGenresPrestations.GENRE_14.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_24.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_34.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_54.equals(raADiminuer.getCodePrestation())) {
                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_EVENEMENT_PROCHE_FAM);
                }
            } else if (REGenresPrestations.GENRE_15.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_25.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_35.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_45.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_55.equals(raCourante.getCodePrestation())) {
                if (REGenresPrestations.GENRE_15.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_25.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_35.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_45.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_55.equals(raADiminuer.getCodePrestation())) {
                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_EVENEMENT_PROCHE_FAM);
                }
            } else if (REGenresPrestations.GENRE_16.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_26.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_36.equals(raCourante.getCodePrestation())) {
                if (REGenresPrestations.GENRE_16.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_26.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_36.equals(raADiminuer.getCodePrestation())) {
                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_EVENEMENT_PROCHE_FAM);
                }
            } else if (REGenresPrestations.GENRE_33.equals(raCourante.getCodePrestation())) {
                if (REGenresPrestations.GENRE_33.equals(raADiminuer.getCodePrestation())) {
                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_EVENEMENT_PROCHE_FAM);
                }
            } else if (REGenresPrestations.GENRE_74.equals(raCourante.getCodePrestation())) {
                if (REGenresPrestations.GENRE_14.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_24.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_34.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_54.equals(raADiminuer.getCodePrestation())) {
                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_EVENEMENT_PROCHE_FAM);
                }
            } else if (REGenresPrestations.GENRE_75.equals(raCourante.getCodePrestation())) {
                if (REGenresPrestations.GENRE_15.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_25.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_35.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_45.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_55.equals(raADiminuer.getCodePrestation())) {
                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_EVENEMENT_PROCHE_FAM);
                }
            } else if (REGenresPrestations.GENRE_85.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_86.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_87.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_89.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_94.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_95.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_96.equals(raCourante.getCodePrestation())
                    || REGenresPrestations.GENRE_97.equals(raCourante.getCodePrestation())) {
                if (REGenresPrestations.GENRE_81.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_82.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_83.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_84.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_88.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_91.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_92.equals(raADiminuer.getCodePrestation())
                        || REGenresPrestations.GENRE_93.equals(raADiminuer.getCodePrestation())) {
                    raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_CONVERSION_PRST_AI_EN_AVS);
                }
            } else {
                raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_AUTRE_EVENEMENT);
            }
        }

        if (JadeStringUtil.isIntegerEmpty(raADiminuer.getCodeMutation())) {
            raADiminuer.setCodeMutation(IREAnnonces.CODE_MUTATION_AUTRE_EVENEMENT);
        }

        return raADiminuer;
    }

    private void initRoleTiersRentier(final BSession session, final BTransaction transaction, final String idTiers)
            throws Exception {

        TIRoleManager mgr = new TIRoleManager();
        mgr.setSession(session);
        mgr.setForIdTiers(idTiers);
        mgr.setForRole(TIRole.CS_RENTIER);
        mgr.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        // On ajoute le role rentier
        if (mgr.getCount() == 0) {
            TIRole role = new TIRole();
            role.setSession(session);
            role.setIdTiers(idTiers);
            role.setRole(TIRole.CS_RENTIER);
            role.setDebutRole(JACalendar.todayJJsMMsAAAA());
            role.add(transaction);
        }
    }

    private boolean isRetroDansRA(final String idDemande, final String idRA) throws Exception {
        REPrestationsDuesJointDemandeRenteManager mgr = new REPrestationsDuesJointDemandeRenteManager();
        mgr.setSession(getSession());
        mgr.setForNoDemandeRente(idDemande);
        mgr.setForNoRenteAccordee(idRA);
        mgr.setForCsTypePrestationDue(IREPrestationDue.CS_TYPE_MNT_TOT);
        mgr.find(2);
        // Il y a du retro deja valide
        if (mgr.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Charge la demande de rente
     *
     * @param idBaseCalcul
     * @return
     * @throws Exception
     */
    protected REDemandeRente loadDemandeRente(final BTransaction transaction, final String idBaseCalcul)
            throws Exception {

        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(getSession());
        bc.setIdBasesCalcul(idBaseCalcul);
        bc.retrieve(transaction);

        REDemandeRente demande = null;
        demande = new REDemandeRente();
        demande.setSession(getSession());
        demande.setIdRenteCalculee(bc.getIdRenteCalculee());
        demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demande.retrieve(transaction);

        return demande;

    }

    /**
     * Charge les prestations dues de la décisions, par rente accordées Seule les prestations dues de type "MENSUEL"
     * sont retournées Liste des rentes accordées concernées par les prestations dues<br/>
     * <br/>
     * Format : <br/>
     * <br/>
     * Key = idRenteAccordée Value = idPrstDue-idPrstDue-idPrstDue
     */
    protected void loadPrestationsDues(final BTransaction transaction, final String decisionDu) throws Exception {

        REValidationDecisionsManager mgr = new REValidationDecisionsManager();
        mgr.setSession(getSession());
        mgr.setForIdDecision(getIdDecision());
        mgr.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < mgr.size(); i++) {
            REValidationDecisions validRente = (REValidationDecisions) mgr.getEntity(i);

            REPrestationDue pd = new REPrestationDue();
            pd.setSession(getSession());
            pd.setIdPrestationDue(validRente.getIdPrestationDue());
            pd.retrieve(transaction);

            assertNotIsNew(pd, "014 - Entity not found.");

            if (idsPrstDuesParRenteAccordee.containsKey(pd.getIdRenteAccordee())) {
                String lst = idsPrstDuesParRenteAccordee.get(pd.getIdRenteAccordee());
                lst += pd.getIdPrestationDue() + "-";
                idsPrstDuesParRenteAccordee.put(Long.parseLong(pd.getIdRenteAccordee()), lst);
            } else {
                String elm = pd.getIdPrestationDue() + "-";
                idsPrstDuesParRenteAccordee.put(Long.parseLong(pd.getIdRenteAccordee()), elm);
            }

            idsPrestationsDues.add(pd.getIdPrestationDue());
        }
    }

    public void setIdDecision(final String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdDemandeRente(final String idDemandeRente) {
        this.idDemandeRente = idDemandeRente;
    }
}
