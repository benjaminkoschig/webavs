/*
 * Créé le 3 juil. 07
 */

package globaz.corvus.helpers.basescalcul;

import globaz.corvus.acor.parser.rev09.REACORParser;
import globaz.corvus.api.basescalcul.IREBasesCalcul;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.basescalcul.IREPrestationDue;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculDixiemeRevision;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.basescalcul.REBasesCalculNeuviemeRevision;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteVieillesse;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.basescalcul.RESaisieManuelleRABCPDViewBean;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuEnfant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdressePaiementBanqueFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementBeneficiaireFormater;
import globaz.pyxis.adresse.formater.TIAdressePaiementCppFormater;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;
import java.math.BigDecimal;
import java.util.Iterator;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * @author HPE
 * 
 */

public class RESaisieManuelleRABCPDHelper extends PRAbstractHelper {

    // ~ Fields -------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * redefini pour charger l'adresse de paiement.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _init(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._init(viewBean, action, session);

        if (!((RESaisieManuelleRABCPDViewBean) viewBean).isRetourDepuisPyxis()) {

            // Quand on charge la page depuis le bouton nouveau de la base de calcul ou depuis la rcListe de la base de
            // calcul

            // Charger les informations comptables
            REInformationsComptabilite ic = new REInformationsComptabilite();
            ic.setSession((BSession) session);

            ic.setAdressePaiement(PRTiersHelper.getAdressePaiementData((BSession) session,
                    ((BSession) session).getCurrentThreadTransaction(),
                    ((RESaisieManuelleRABCPDViewBean) viewBean).getIdTiersBeneficiaire(),
                    IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA()));

            ((RESaisieManuelleRABCPDViewBean) viewBean).setRetourDepuisPyxis(false);

            // Les setter dans le viewBean
            ((RESaisieManuelleRABCPDViewBean) viewBean).setIdInfoComptaIC(ic.getIdInfoCompta());
            ((RESaisieManuelleRABCPDViewBean) viewBean).setIdTiersAdressePmtIC(ic.getIdTiersAdressePmt());
            // ((RESaisieManuelleRABCPDViewBean)viewBean).setIdDomaineApplicationIC(ic.getIdDomaineApplication());
            ((RESaisieManuelleRABCPDViewBean) viewBean).setIdCompteAnnexeIC(ic.getIdCompteAnnexe());

        }

        // charger l'adresse de paiement
        rechargerAdressePaiement((BSession) session, (RESaisieManuelleRABCPDViewBean) viewBean);
    }

    public FWViewBeanInterface ajouterRABCPD(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {

        // Uniquement ajout pour cet écran, alors il faut créer :
        // --> Bases de calcul + rente calculée (si pas crée)
        // --> Rente accordée + info_compta
        // --> Prestations dues

        // Reprise du viewBean
        RESaisieManuelleRABCPDViewBean saisieRABCPDVb = (RESaisieManuelleRABCPDViewBean) viewBean;

        // Création de la transaction
        BTransaction transaction = null;
        transaction = (BTransaction) (session).newTransaction();

        try {

            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }
            // Faire toutes les validations
            if (!saisieRABCPDVb.getDroitBasesCalcul().equals("10") && !saisieRABCPDVb.getDroitBasesCalcul().equals("9")) {
                transaction.addErrors(session.getLabel("VALID_SMA_H_DROIT_EGAL_9_10"));
                return viewBean;
            }

            // BZ 7583 - La version du droit vaut 9, Le code revenu (9ème) doit valoir 1,2 ou 3
            if ("9".equals(saisieRABCPDVb.getDroitBasesCalcul())) {
                String codeRevenu = saisieRABCPDVb.getCodeRevenu9emeBasesCalcul();

                if (JadeStringUtil.isDigit(codeRevenu) == false || Integer.parseInt(codeRevenu) < 1
                        || Integer.parseInt(codeRevenu) > 3) {
                    transaction.addErrors(session.getLabel("VALID_CODE_REVENU_DROIT_EGAL_9"));
                    return viewBean;
                }
            }

            // BZ_7218 : Champ "anneeNiveau" obligatoire selon genre prestation
            // Si anneeNiveau vide, on vérifie que le genre de prestation spécifié n'oblige pas un niveau d'année
            if ((JadeStringUtil.isBlank(saisieRABCPDVb.getAnneeNiveauBasesCalcul()))) {
                // Si genrePrestation non vide, on peut vérifier si il exige un niveau d'année.
                if (!JadeStringUtil.isBlankOrZero(saisieRABCPDVb.getGenrePrestationRenteAccordee())) {
                    if (saisieRABCPDVb.isGenrePrestationWithNiveauAnneeMandatory(Integer.valueOf(saisieRABCPDVb
                            .getGenrePrestationRenteAccordee()))) {

                        transaction.addErrors(session.getLabel("VALID_SMA_H_ANNEE_NIVEAU_MANDATORY"));
                        return viewBean;
                    }
                }
            }
            // Sinon on controle que le niveau d'année soit renseigné et compris entre 00 et 99
            else {
                if (!saisieRABCPDVb.isAnneeNiveauBasesCalculCorrect(Integer.valueOf(saisieRABCPDVb
                        .getAnneeNiveauBasesCalcul()))) {
                    transaction.addErrors(session.getLabel("VALID_SMA_H_ANNEE_NIVEAU_INVALID"));
                    return viewBean;
                }
            }

            // BZ 8000 : Le champ "fraction rente" est obligatoire si prestation genre AI, interdit sinon
            CodePrestation codePrestation = null;

            try {
                codePrestation = CodePrestation.parse(saisieRABCPDVb.getGenrePrestationRenteAccordee());
            } catch (IllegalArgumentException exception) {
                transaction.addErrors(session.getLabel("VALID_GENRE_PRESTATION"));
                return viewBean;
            }

            String fraction = saisieRABCPDVb.getFractionRenteRenteAccordee();

            if (codePrestation.isAI()) {
                if (fraction.length() == 0) {
                    transaction.addErrors(session.getLabel("VALID_FRACTION_RENTE_SI_GENRE_AI"));
                    return viewBean;
                }
            } else {
                if (fraction.length() > 0) {
                    transaction.addErrors(session.getLabel("VALID_FRACTION_RENTE_SI_PAS_GENRE_AI"));
                    return viewBean;
                }
            }

            // Création de la base de calcul si nécessaire et de la rente calculée si nécessaire
            if (saisieRABCPDVb.getIsBasesCalculModifiable().booleanValue()) {

                // if (JadeStringUtil.isIntegerEmpty(saisieRABCPDVb.getIdRenteCalculee())){

                RERenteCalculee rc = new RERenteCalculee();
                rc.setSession(session);
                boolean isRCAdded = false;

                if (!JadeStringUtil.isBlankOrZero(saisieRABCPDVb.getIdRenteCalculee())) {
                    rc.setIdRenteCalculee(saisieRABCPDVb.getIdRenteCalculee());
                    rc.retrieve(transaction);
                    if (rc.isNew()) {

                        // Avant de le remplacer, on s'assure qu'il n'y a plus aucune base de calcul liée à cet ancien
                        // ID !!!
                        REBasesCalculManager mgr = new REBasesCalculManager();
                        mgr.setSession(session);
                        mgr.setForIdRenteCalculee(saisieRABCPDVb.getIdRenteCalculee());
                        mgr.find(transaction, 2);
                        if (!mgr.isEmpty()) {

                            if (!JadeStringUtil.isEmpty(saisieRABCPDVb.getIdRenteCalculee())
                                    && !JadeStringUtil.isEmpty(saisieRABCPDVb.getIdDemandeRente())) {

                                throw new Exception(session.getLabel("ERREUR_INCOHERANCE_DONNEES")
                                        + saisieRABCPDVb.getIdRenteCalculee() + "/"
                                        + saisieRABCPDVb.getIdDemandeRente());
                            } else {
                                throw new Exception(session.getLabel("ERREUR_INCOHERANCE_DONNEES"));
                            }
                        }

                        rc.setIdRenteCalculee(null);
                        rc.add(transaction);
                        isRCAdded = true;
                    }
                } else {
                    rc.add(transaction);
                    isRCAdded = true;
                }
                saisieRABCPDVb.setIdRenteCalculee(rc.getIdRenteCalculee());

                if (isRCAdded) {
                    // Update de la demande de rente
                    REDemandeRente demande = new REDemandeRente();
                    demande.setSession(session);
                    demande.setIdDemandeRente(saisieRABCPDVb.getIdDemandeRente());
                    demande.retrieve(transaction);

                    demande.setIdRenteCalculee(saisieRABCPDVb.getIdRenteCalculee());
                    demande.setCsEtat(IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE);
                    demande.update(transaction);
                }

                // }

                // si 10ème révision
                if (saisieRABCPDVb.getDroitBasesCalcul().equals("10")) {

                    REBasesCalculDixiemeRevision bc10 = new REBasesCalculDixiemeRevision();
                    bc10.setSession(session);
                    bc10.setIdRenteCalculee(saisieRABCPDVb.getIdRenteCalculee());
                    bc10.setIdTiersBaseCalcul(saisieRABCPDVb.getIdTierRequerant());
                    bc10.setRevenuAnnuelMoyen(saisieRABCPDVb.getRAMBasesCalcul());
                    bc10.setEchelleRente(saisieRABCPDVb.getEchelleBasesCalcul());
                    bc10.setAnneeTraitement(saisieRABCPDVb.getAnneeTraitementBasesCalcul());
                    bc10.setDureeCotiAvant73(saisieRABCPDVb.getDureeCotAv73BasesCalcul());
                    bc10.setDureeCotiDes73(saisieRABCPDVb.getDureeCotAp73BasesCalcul());
                    bc10.setMoisAppointsAvant73(saisieRABCPDVb.getMoisAppAv73BasesCalcul());
                    bc10.setMoisAppointsDes73(saisieRABCPDVb.getMoisAppAp73BasesCalcul());
                    bc10.setAnneeCotiClasseAge(saisieRABCPDVb.getDureeCotClasseBasesCalcul());
                    bc10.setAnneeDeNiveau(saisieRABCPDVb.getAnneeNiveauBasesCalcul());
                    bc10.setDureeRevenuAnnuelMoyen(saisieRABCPDVb.getDureeCotRAMBasesCalcul());
                    bc10.setInvaliditePrecoce(saisieRABCPDVb.getInvaliditePrecoceBasesCalcul());
                    bc10.setPeriodeAssEtrangerAv73(saisieRABCPDVb.getDureeCotEtrAv73BasesCalcul());
                    bc10.setPeriodeAssEtrangerDes73(saisieRABCPDVb.getDureeCotEtrAp73BasesCalcul());
                    bc10.setSupplementCarriere(saisieRABCPDVb.getSuppAIRAMBasesCalcul());
                    bc10.setAnneeBonifTacheEduc(saisieRABCPDVb.getAnneesEducatifBasesCalcul());
                    bc10.setAnneeBonifTacheAssistance(saisieRABCPDVb.getAnneesAssistanceBasesCalcul());
                    bc10.setAnneeBonifTransitoire(saisieRABCPDVb.getAnneesTransitionBasesCalcul());
                    bc10.setRevenuSplitte(saisieRABCPDVb.getCodeRevenusSplittesBasesCalcul());
                    bc10.setRevenuPrisEnCompte(saisieRABCPDVb.getCodeRevenu9emeBasesCalcul());
                    bc10.setDroitApplique(saisieRABCPDVb.getDroitBasesCalcul());
                    bc10.setDegreInvalidite(saisieRABCPDVb.getDegreInvaliditeDemandeRente());
                    bc10.setCleInfirmiteAyantDroit(saisieRABCPDVb.getCleInfirmiteDemandeRente());
                    bc10.setSurvenanceEvtAssAyantDroit(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(saisieRABCPDVb
                            .getSurvEvAssDemandeRente()));
                    bc10.setCodeOfficeAi(saisieRABCPDVb.getCodeOfficeAIDemandeRente());

                    bc10.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);

                    // On Pas de test sur degreInvalidite pour les API
                    REDemandeRente dem = new REDemandeRente();
                    dem.setIdDemandeRente(saisieRABCPDVb.getIdDemandeRente());
                    dem.retrieve(transaction);
                    if (!dem.isNew() && IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(dem.getCsTypeDemandeRente())) {
                        bc10.setIsDemandeRenteAPI(Boolean.TRUE);
                    }

                    bc10.add(transaction);

                    saisieRABCPDVb.setIdBasesCalcul(bc10.getIdBasesCalcul());

                }

                // si 9ème révision
                if (saisieRABCPDVb.getDroitBasesCalcul().equals("9")) {

                    REBasesCalculNeuviemeRevision bc9 = new REBasesCalculNeuviemeRevision();

                    bc9.setSession(session);
                    bc9.setIdRenteCalculee(saisieRABCPDVb.getIdRenteCalculee());
                    bc9.setIdTiersBaseCalcul(saisieRABCPDVb.getIdTierRequerant());
                    bc9.setRevenuAnnuelMoyen(saisieRABCPDVb.getRAMBasesCalcul());
                    bc9.setEchelleRente(saisieRABCPDVb.getEchelleBasesCalcul());
                    bc9.setAnneeTraitement(saisieRABCPDVb.getAnneeTraitementBasesCalcul());
                    bc9.setDureeCotiAvant73(saisieRABCPDVb.getDureeCotAv73BasesCalcul());
                    bc9.setDureeCotiDes73(saisieRABCPDVb.getDureeCotAp73BasesCalcul());
                    bc9.setMoisAppointsAvant73(saisieRABCPDVb.getMoisAppAv73BasesCalcul());
                    bc9.setMoisAppointsDes73(saisieRABCPDVb.getMoisAppAp73BasesCalcul());
                    bc9.setAnneeCotiClasseAge(saisieRABCPDVb.getDureeCotClasseBasesCalcul());
                    bc9.setAnneeDeNiveau(saisieRABCPDVb.getAnneeNiveauBasesCalcul());
                    bc9.setDureeRevenuAnnuelMoyen(saisieRABCPDVb.getDureeCotRAMBasesCalcul());
                    bc9.setInvaliditePrecoce(saisieRABCPDVb.getInvaliditePrecoceBasesCalcul());
                    bc9.setPeriodeAssEtrangerAv73(saisieRABCPDVb.getDureeCotEtrAv73BasesCalcul());
                    bc9.setPeriodeAssEtrangerDes73(saisieRABCPDVb.getDureeCotEtrAp73BasesCalcul());
                    bc9.setSupplementCarriere(saisieRABCPDVb.getSuppAIRAMBasesCalcul());
                    bc9.setBonificationTacheEducative(saisieRABCPDVb.getBonusEducatifBasesCalcul());
                    bc9.setAnneeBonifTacheEduc(saisieRABCPDVb.getAnneesEducatifBasesCalcul());
                    bc9.setAnneeBonifTacheAssistance(saisieRABCPDVb.getAnneesAssistanceBasesCalcul());
                    bc9.setAnneeBonifTransitoire(saisieRABCPDVb.getAnneesTransitionBasesCalcul());
                    bc9.setRevenuSplitte(saisieRABCPDVb.getCodeRevenusSplittesBasesCalcul());
                    bc9.setRevenuPrisEnCompte(saisieRABCPDVb.getCodeRevenu9emeBasesCalcul());
                    bc9.setDroitApplique(saisieRABCPDVb.getDroitBasesCalcul());

                    bc9.setDegreInvalidite(saisieRABCPDVb.getDegreInvaliditeDemandeRente());
                    bc9.setCleInfirmiteAyantDroit(saisieRABCPDVb.getCleInfirmiteDemandeRente());
                    bc9.setSurvenanceEvtAssAyantDroit(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(saisieRABCPDVb
                            .getSurvEvAssDemandeRente()));
                    bc9.setCodeOfficeAi(saisieRABCPDVb.getCodeOfficeAIDemandeRente());

                    bc9.setCsEtat(IREBasesCalcul.CS_ETAT_ACTIF);

                    // On Pas de test sur degreInvalidite pour les API
                    REDemandeRente dem = new REDemandeRente();
                    dem.setIdDemandeRente(saisieRABCPDVb.getIdDemandeRente());
                    dem.retrieve(transaction);
                    if (!dem.isNew() && IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(dem.getCsTypeDemandeRente())) {
                        bc9.setIsDemandeRenteAPI(Boolean.TRUE);
                    }

                    bc9.add(transaction);

                    saisieRABCPDVb.setIdBasesCalcul(bc9.getIdBasesCalcul());
                }
            }

            // Création de la rente accordée
            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setCodePrestation(saisieRABCPDVb.getGenrePrestationRenteAccordee());
            ra.setDateDebutDroit(saisieRABCPDVb.getDateDebutDroitRenteAccordee());
            ra.setMontantPrestation(saisieRABCPDVb.getMontantRenteAccordee());
            ra.setFractionRente(saisieRABCPDVb.getFractionRenteRenteAccordee());
            ra.setDureeAjournement(saisieRABCPDVb.getDureeAjournementRenteAccordee());
            ra.setSupplementAjournement(saisieRABCPDVb.getSupplementAjournementRenteAccordee());
            ra.setMontantReducationAnticipation(saisieRABCPDVb.getReductionAnticipationRenteAccordee());
            ra.setDateDebutAnticipation(saisieRABCPDVb.getDebAnticipationRenteAccordee());
            ra.setDateFinDroit(saisieRABCPDVb.getFinDroitRenteAccordee());
            ra.setCodeCasSpeciaux1(saisieRABCPDVb.getCodesCasSpecialRenteAccordee1());
            ra.setCodeCasSpeciaux2(saisieRABCPDVb.getCodesCasSpecialRenteAccordee2());
            ra.setCodeCasSpeciaux3(saisieRABCPDVb.getCodesCasSpecialRenteAccordee3());
            ra.setCodeCasSpeciaux4(saisieRABCPDVb.getCodesCasSpecialRenteAccordee4());
            ra.setCodeCasSpeciaux5(saisieRABCPDVb.getCodesCasSpecialRenteAccordee5());
            ra.setCodeSurvivantInvalide(saisieRABCPDVb.getCodeSurvivantInvalideRenteAccordee());
            ra.setIdBaseCalcul(saisieRABCPDVb.getIdBasesCalcul());
            ra.setIdTiersBeneficiaire(saisieRABCPDVb.getIdTiersBeneficiaire());
            ra.setIdTiersBaseCalcul(saisieRABCPDVb.getIdTierRequerant());
            ra.setIdInfoCompta(saisieRABCPDVb.getIdInfoComptaIC());
            ra.setReferencePmt(saisieRABCPDVb.getReferencePmt());
            ra.setAnneeMontantRAM(saisieRABCPDVb.getAnneeTraitementBasesCalcul());

            // /////////////////////////////////////////////////////////////////////////////////////
            // Mise a jours des tiers complémentaire a l'aide de la situation familiale et
            // du tiersBeneficiaire de la RA
            // /////////////////////////////////////////////////////////////////////////////////////
            majTiersComplementaires(session, transaction, ra);

            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession(session);
            bc.setIdBasesCalcul(ra.getIdBaseCalcul());
            bc.retrieve();

            bc.setAnneeTraitement(saisieRABCPDVb.getAnneeTraitementBasesCalcul());
            bc.update();

            ra.setTauxReductionAnticipation(saisieRABCPDVb.getTauxReductionAnticipation());

            REDemandeRente dem = new REDemandeRente();
            dem.setIdDemandeRente(saisieRABCPDVb.getIdDemandeRente());
            dem.retrieve(transaction);

            if (!dem.isNew()) {
                if (dem.getCsTypeDemandeRente().equals(IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE)) {
                    REDemandeRenteVieillesse demVie = new REDemandeRenteVieillesse();
                    demVie.setSession(session);
                    demVie.setIdDemandeRente(dem.getIdDemandeRente());
                    demVie.retrieve(transaction);

                    ra.setAnneeAnticipation((session).getCode(demVie.getCsAnneeAnticipation()));
                }
            }

            REInformationsComptabilite rc = new REInformationsComptabilite();
            rc.setSession(session);
            rc.setIdInfoCompta(ra.getIdInfoCompta());
            rc.retrieve(transaction);

            if (rc.isNew()) {
                rc.setIdTiersAdressePmt(saisieRABCPDVb.getIdTiersAdressePmtIC());
                // rc.setIdAffilieAdressePmt(saisieRABCPDVb.getIdAffilieAdressePmtIC());
                // rc.setIdDomaineApplication(saisieRABCPDVb.getIdDomaineApplicationIC());
                rc.add(transaction);
            } else {
                rc.setIdTiersAdressePmt(saisieRABCPDVb.getIdTiersAdressePmtIC());
                // rc.setIdDomaineApplication(saisieRABCPDVb.getIdDomaineApplicationIC());
                // rc.setIdAffilieAdressePmt(saisieRABCPDVb.getIdAffilieAdressePmtIC());
                rc.update(transaction);
            }

            ra.setIdInfoCompta(rc.getIdInfoCompta());

            // BZ 4666
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());
            String csEtatCivil = null;
            String csCantonDomicile = null;
            ISFMembreFamilleRequerant[] mf = null;
            mf = sf.getMembresFamille(ra.getIdTiersBeneficiaire());

            for (int i = 0; i < mf.length; i++) {
                ISFMembreFamilleRequerant membre = mf[i];
                // On récupère le bénéficiaire en tant que membre de famille
                if (ra.getIdTiersBeneficiaire().equals(membre.getIdTiers())) {
                    csEtatCivil = membre.getCsEtatCivil();
                    csCantonDomicile = membre.getCsCantonDomicile();
                    if (csCantonDomicile == null) {
                        csCantonDomicile = session.getCode(membre.getCsNationalite());
                    }
                    break;
                }
            }
            // Peut arriver dans le cas d'un enfant de la situation familialle, par exemple.
            if (csEtatCivil == null) {
                csEtatCivil = ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE;
            }

            // Je dois transformer le CS_ETAT_CIVIL_X de Hera en CS_X qui est stocké dans RA
            // Depuis Hera : CS_ETAT_CIVIL_CELIBATAIRE
            // Stocké en base de données : CS_CELIBATAIRE

            ra.setCsEtatCivil(PRACORConst.csEtatCivilHeraToCsEtatCivil(csEtatCivil));

            // si ajournement dans demande de rente, on met l'état ajourné, sinon on met calculé
            boolean isRAAjourne = false;

            REDemandeRenteVieillesse demVieillesse = new REDemandeRenteVieillesse();
            demVieillesse.setSession(session);
            demVieillesse.setIdDemandeRente(dem.getIdDemandeRente());
            demVieillesse.retrieve(transaction);

            if (!demVieillesse.isNew()) {

                if (!JadeStringUtil.isBlankOrZero(demVieillesse.getDateRevocationRequerant())) {
                    // si dateFinDroit (dateFinRA (MM.AAAA) = date révocation (JJ.MM.AAAA)) , mettre en ajourné
                    if (saisieRABCPDVb.getFinDroitRenteAccordee()
                            .equals(PRDateFormater.convertDate_JJxMMxAAAA_to_MMxAAAA(demVieillesse
                                    .getDateRevocationRequerant()))) {

                        isRAAjourne = true;

                    }
                }

            }

            if (isRAAjourne) {
                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_AJOURNE);
            } else {
                ra.setCsEtat(IREPrestationAccordee.CS_ETAT_CALCULE);
            }

            ra.setDateRevocationAjournement(saisieRABCPDVb.getDateRevocationDemandeRente());

            ra.add(transaction);
            saisieRABCPDVb.setIdRenteAccordee(ra.getIdPrestationAccordee());

            this.calculerPrestationDues(session, transaction, ra, saisieRABCPDVb);

            REDemandeRente demande = new REDemandeRente();
            demande.setSession(session);
            demande.setIdDemandeRente(saisieRABCPDVb.getIdDemandeRente());
            demande.retrieve(transaction);
            demande.setDateTraitement(REACORParser.retrieveDateTraitement(demande));
            demande = majDemandeDdDf(session, transaction, demande, saisieRABCPDVb.getIdBasesCalcul());
            demande.update(transaction);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
            throw e;
        } finally {
            if (transaction != null) {
                try {
                    if (transaction.hasErrors() || transaction.isRollbackOnly()) {
                        transaction.rollback();
                    } else {
                        transaction.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                } finally {
                    transaction.closeTransaction();
                }
            }
        }

        return saisieRABCPDVb;

    }

    /**
     * 
     * @param session
     * @param transaction
     * @param renteAccordee
     *            La rente accordée pour à utiliser pour la création de la prestation due. Ne peut pas être null
     * @param saisieRABCPDVb
     *            Le viewBean initialisé. Ne peut pas être null
     * @throws Exception
     */
    public void calculerPrestationDues(BSession session, BTransaction transaction, RERenteAccordee renteAccordee,
            RESaisieManuelleRABCPDViewBean saisieRABCPDVb) throws Exception {
        if (session == null) {
            throw new Exception("session can not be null");
        }
        if (transaction == null) {
            throw new Exception("transaction can not be null");
        }
        if (renteAccordee == null) {
            throw new Exception("renteAccordee can not be null");
        }
        if (saisieRABCPDVb == null) {
            throw new Exception("montantRenteAccordee can not be null");
        }
        this.calculerPrestationDues(session, transaction, renteAccordee, saisieRABCPDVb.getRAMBasesCalcul());
    }

    /**
     * Création de la prestation due et de la prestation due pour le retro Si la date de fin de droit de la rente
     * accordée est null, la date du dernier paiement sera prise en compte.
     * 
     * @param session
     *            La session utilisateur en cours. Ne peut pas être null
     * @param transaction
     *            La transaction a utiliser. Ne peut pas être null
     * @param renteAccordee
     *            La rente accordée pour à utiliser pour la création de la prestation due. Ne peut pas être null
     * @param revenuAnnuelMoyenBasesCalcul
     *            Revenu annuel moyen
     * @throws Exception
     *             Toutes les exceptions levées sont renvoyées
     */
    public void calculerPrestationDues(BSession session, BTransaction transaction, RERenteAccordee renteAccordee,
            String revenuAnnuelMoyenBasesCalcul) throws Exception {

        if (session == null) {
            throw new Exception("session can not be null");
        }
        if (transaction == null) {
            throw new Exception("transaction can not be null");
        }
        if (renteAccordee == null) {
            throw new Exception("renteAccordee can not be null");
        }
        if (revenuAnnuelMoyenBasesCalcul == null) {
            throw new Exception("RAMBaseCalcul can not be null");
        }
        // Création de la prestation due ($p)
        REPrestationDue pd = new REPrestationDue();
        pd.setSession(session);
        pd.setDateDebutPaiement(renteAccordee.getDateDebutDroit());
        pd.setDateFinPaiement(renteAccordee.getDateFinDroit());
        pd.setMontant(renteAccordee.getMontantPrestation());
        pd.setRam(revenuAnnuelMoyenBasesCalcul);
        pd.setIdRenteAccordee(renteAccordee.getIdPrestationAccordee());
        pd.setMontantReductionAnticipation(renteAccordee.getMontantReducationAnticipation());
        pd.setMontantSupplementAjournement(renteAccordee.getSupplementAjournement());

        // Par défaut, dans l'état en attente.
        // La validation de la décision mettre la prestation due dans l'état ACTIF
        pd.setCsEtat(IREPrestationDue.CS_ETAT_ATTENTE);
        pd.setCsTypePaiement(IREPrestationDue.CS_TYPE_DE_PMT_PMT_MENS);
        pd.setCsType(IREPrestationDue.CS_TYPE_PMT_MENS);

        pd.add(transaction);

        // Création de la prestation due pour le retro ($t)
        REPrestationDue pdt = new REPrestationDue();
        pdt.setSession(session);
        pdt.setCsType(IREPrestationDue.CS_TYPE_MNT_TOT);
        pdt.setCsTypePaiement(null);
        pdt.setDateDebutPaiement(renteAccordee.getDateDebutDroit());

        if (!JadeStringUtil.isBlankOrZero(renteAccordee.getDateFinDroit())) {
            pdt.setDateFinPaiement(renteAccordee.getDateFinDroit());
        } else {
            pdt.setDateFinPaiement(REPmtMensuel.getDateDernierPmt(session));
        }

        // BZ 5471 arrivée depuis une saisie manuelle, si la date de fin de paiement est plus petite que la date de
        // début, pas de rétro (sinon montant rétro négatif)
        String dateDebutPaiement = pdt.getDateDebutPaiement();
        if (JadeDateUtil.isGlobazDateMonthYear(dateDebutPaiement)) {
            dateDebutPaiement = "01." + dateDebutPaiement;
        }
        String dateFinPaiement = pdt.getDateFinPaiement();
        if (JadeDateUtil.isGlobazDateMonthYear(dateFinPaiement)) {
            dateFinPaiement = "01." + dateFinPaiement;
        }
        if (JadeDateUtil.isDateBefore(dateDebutPaiement, dateFinPaiement)
                || JadeDateUtil.areDatesEquals(dateDebutPaiement, dateFinPaiement)) {
            JADate dd = new JADate(pdt.getDateDebutPaiement());
            JADate df = new JADate(pdt.getDateFinPaiement());

            int yearMax = df.getYear();
            int yearMin = dd.getYear();
            int monthMax = df.getMonth();
            int monthMin = dd.getMonth();
            int diffMonth = 0;

            if (yearMax == yearMin) {
                diffMonth = (monthMax - monthMin) + 1;
            } else {
                int diff1 = (12 - monthMin) + 1;
                int diff2 = monthMax;
                int diff3 = yearMax - yearMin - 1;
                diff3 = diff3 * 12;
                diffMonth = diff1 + diff2 + diff3;
            }

            BigDecimal db = new BigDecimal(diffMonth);
            db = db.multiply(new BigDecimal(renteAccordee.getMontantPrestation()));
            pdt.setMontant(JANumberFormatter.round(db, 0.05, 2, JANumberFormatter.INF).toString());
            pdt.setIdRenteAccordee(renteAccordee.getIdPrestationAccordee());
            pdt.add(transaction);
        }
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    // Mise à jour dateDebut et dateFin de la demande
    // --> La date de début représente la date la plus petite des ra
    // --> La date de fin représente la date la plus grande des ra, sauf si un des ra n'a pas de fin, dans ce cas, on
    // laisse vide
    private REDemandeRente majDemandeDdDf(BSession session, BTransaction transaction, REDemandeRente demande,
            String idBC) throws Exception {

        String firstDateDebutRA = "";
        String lastDateFinRA = "";
        boolean isRAWithoutDateFin = false;

        RERenteAccJoinTblTiersJoinDemRenteManager mgrRAJTTJDRM = new RERenteAccJoinTblTiersJoinDemRenteManager();
        mgrRAJTTJDRM.setSession(session);
        mgrRAJTTJDRM.setForNoDemandeRente(demande.getIdDemandeRente());
        mgrRAJTTJDRM.find();

        for (Iterator iterator = mgrRAJTTJDRM.iterator(); iterator.hasNext();) {
            RERenteAccJoinTblTiersJoinDemandeRente elm = (RERenteAccJoinTblTiersJoinDemandeRente) iterator.next();

            // Date de début
            if (!JadeStringUtil.isBlankOrZero(elm.getDateDebutDroit())) {
                if (firstDateDebutRA.length() == 0) {
                    firstDateDebutRA = elm.getDateDebutDroit();
                } else {
                    if (Integer.parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(elm.getDateDebutDroit())) < Integer
                            .parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(firstDateDebutRA))) {
                        firstDateDebutRA = elm.getDateDebutDroit();
                    }
                }
            }
            // Date de fin
            if (!JadeStringUtil.isBlankOrZero(elm.getDateFinDroit())) {
                if (lastDateFinRA.length() == 0) {
                    lastDateFinRA = elm.getDateFinDroit();
                } else {
                    if (Integer.parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(elm.getDateFinDroit())) > Integer
                            .parseInt(PRDateFormater.convertDate_MMxAAAA_to_AAAAMM(lastDateFinRA))) {
                        lastDateFinRA = elm.getDateFinDroit();
                    }
                }
            }

            // période infinie ?
            if (JadeStringUtil.isBlankOrZero(elm.getDateFinDroit())) {
                isRAWithoutDateFin = true;
            }

        }

        JADate dd = new JADate(firstDateDebutRA);

        demande.setDateDebut(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(dd.toStrAMJ()));

        if (isRAWithoutDateFin) {
            demande.setDateFin("");
        } else {
            demande.setDateFin(JadeDateUtil.getLastDateOfMonth(lastDateFinRA));
        }
        return demande;
    }

    /**
     * Mise a jours des tiers complémentaire a l'aide de la situation familiale et de du tiersBeneficiaire de la RA
     * 
     * Pour ayant droit enfant =======================
     * 
     * rentes 14, 24, 34, 54, 74, 16, 26, 56, 76 -> tiersComplementaire1 père (si conj. inc. 00000000000) ->
     * tiersComplementaire2 mere
     * 
     * rentes 15, 25, 35, 45, 55, 75 -> tiersComplementaire1 mere -> tiersComplementaire2 pere (si conj. inc.
     * 00000000000)
     * 
     * Autres ======
     * 
     * 13, 23, 33, 53, 73 -> tiersComplementaire1 l'autre conjoint
     * 
     * 
     * 10, 20, 50, 70 -> si celibataire -> tiersComplementaire1 rien -> si marie ou veuf -> tiersComplementaire1 l'autre
     * conjoint -> si divorce -> tiersComplementaire1 le dernier ex-conjoint
     * 
     * 
     * @param session
     * @param transaction
     * @param ra
     */
    private void majTiersComplementaires(BSession session, BTransaction transaction, RERenteAccordee ra)
            throws Exception {
        // TODO: harmoniser la recherche de NSS complémentaire

        // Voir dans la situation familiale pour setter le NSS des champs complémentaire 1 et 2
        if(ra.contientCodeCasSpecial("60")){
            SFMembreFamille mbr = new SFMembreFamille();
            mbr.setSession(session);
            mbr.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            mbr.setIdTiers(ra.getIdTiersBeneficiaire());
            mbr.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
            mbr.retrieve();
            // si le tiers n'est pas trouvé dans le domaine rente, recherche dans le domaine standard
            if (mbr.isNew()) {
                mbr.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                mbr.retrieve();
            }

            SFApercuEnfant enf = new SFApercuEnfant();
            enf.setSession(session);
            enf.setIdMembreFamille(mbr.getIdMembreFamille());
            enf.retrieve();

            SFMembreFamille parentLPart1 = null;
            SFMembreFamille parentLPart2 = null;
            if (enf.getPere() != null) {
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getPere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    parentLPart1 = enf.getMere();
                }
            }

            // tiersComplementaire2 mere
            if (enf.getMere() != null) {
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU
                        .equals(enf.getMere().getIdMembreFamille())) {
                    // oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    parentLPart2 = enf.getPere();
                }
            }
            if((parentLPart1 != null && parentLPart2 != null) && parentLPart1.getCsSexe().equals(parentLPart2.getCsSexe()) ){
                if(parentLPart1.getIdTiers().equals(ra.getIdTiersBaseCalcul())){
                    ra.setIdTiersComplementaire1(parentLPart1.getIdTiers());
                    ra.setIdTiersComplementaire2(parentLPart2.getIdTiers());
                }
                if(parentLPart2.getIdTiers().equals(ra.getIdTiersBaseCalcul())){
                    ra.setIdTiersComplementaire1(parentLPart2.getIdTiers());
                    ra.setIdTiersComplementaire2(parentLPart1.getIdTiers());
                }
            }

        }
        // Pour ayant droit enfant
        else if (REGenresPrestations.GENRE_14.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_24.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_34.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_36.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_54.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_74.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_16.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_26.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_56.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_76.equals(ra.getCodePrestation())) {

            SFMembreFamille mbr = new SFMembreFamille();
            mbr.setSession(session);
            mbr.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            mbr.setIdTiers(ra.getIdTiersBeneficiaire());
            mbr.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
            mbr.retrieve();

            // si le tiers n'est pas trouvé dans le domaine rente, recherche dans le domaine standard
            if (mbr.isNew()) {
                mbr.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                mbr.retrieve();
            }

            SFApercuEnfant enf = new SFApercuEnfant();
            enf.setSession(session);
            enf.setIdMembreFamille(mbr.getIdMembreFamille());
            enf.retrieve();

            // tiersComplementaire1 pere (si conj. inc. 00000000000)
            if (enf.getPere() != null) {
                enf.getPere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(enf.getPere().getIdMembreFamille())) {
                    // TODO oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    ra.setIdTiersComplementaire1(enf.getPere().getIdTiers());
                }
            }

            // tiersComplementaire2 mere
            if (enf.getMere() != null) {
                enf.getMere().getCsCantonDomicile();
                ra.setIdTiersComplementaire2(enf.getMere().getIdTiers());
            }

            // Pour ayant droit enfant
        } else if (REGenresPrestations.GENRE_15.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_25.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_35.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_45.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_55.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_75.equals(ra.getCodePrestation())) {

            SFMembreFamille mbr = new SFMembreFamille();
            mbr.setSession(session);
            mbr.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            mbr.setIdTiers(ra.getIdTiersBeneficiaire());
            mbr.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_RENTES);
            mbr.retrieve();

            // si le tiers n'est pas trouvé dans le domaine rente, recherche dans le domaine standard
            if (mbr.isNew()) {
                mbr.setCsDomaineApplication(ISFSituationFamiliale.CS_DOMAINE_STANDARD);
                mbr.retrieve();
            }

            SFApercuEnfant enf = new SFApercuEnfant();
            enf.setSession(session);
            enf.setIdMembreFamille(mbr.getIdMembreFamille());
            enf.retrieve();

            // tiersComplementaire1 mere
            if (enf.getMere() != null) {
                enf.getMere().getCsCantonDomicile();
                ra.setIdTiersComplementaire1(enf.getMere().getIdTiers());
            }

            // tiersComplementaire2 pere (si conj. inc. 00000000000)
            if (enf.getPere() != null) {
                enf.getPere().getCsCantonDomicile();
                if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(enf.getPere().getIdMembreFamille())) {
                    // TODO oui mais on a pas de tiers pour le conjoint inconnu
                } else {
                    ra.setIdTiersComplementaire2(enf.getPere().getIdTiers());
                }
            }

        } else if (REGenresPrestations.GENRE_13.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_23.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_33.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_53.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_73.equals(ra.getCodePrestation())) {

            // Reprendre la situation familiale
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());

            // Reprendre les relations familiales
            ISFRelationFamiliale[] rfs = sf.getRelationsConjoints(ra.getIdTiersBeneficiaire(),
                    JACalendar.todayJJsMMsAAAA());

            // tiers complementaire1 = l'autre conjoint
            for (int j = 0; (rfs != null) && (j < rfs.length); j++) {

                ISFRelationFamiliale rf = rfs[j];

                String idMbrFamille1 = rf.getIdMembreFamilleHomme();
                ISFMembreFamille mf1 = sf.getMembreFamille(idMbrFamille1);
                String idTiers1 = mf1.getIdTiers();
                // Si l'idTiersComplémentaire1 est déjà renseignée, ne pas passer le if sinon l'idTiersComplémentaire1
                // de l'épouse
                // est écrasé par celui de l'ex-épouse
                if (!idTiers1.equals(ra.getIdTiersBeneficiaire())
                        && JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())) {
                    ra.setIdTiersComplementaire1(idTiers1);
                } else {
                    String idMbrFamille2 = rf.getIdMembreFamilleFemme();
                    ISFMembreFamille mf2 = sf.getMembreFamille(idMbrFamille2);
                    String idTiers2 = mf2.getIdTiers();
                    // Si l'idTiersComplémentaire1 est déjà renseignée, ne pas passer le if sinon
                    // l'idTiersComplémentaire1
                    // de l'épouse
                    // est écrasé par celui de l'ex-épouse
                    if (!idTiers2.equals(ra.getIdTiersBeneficiaire())
                            && JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())) {
                        ra.setIdTiersComplementaire1(idTiers2);
                    }
                }
            }

        } else if (REGenresPrestations.GENRE_10.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_20.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_50.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_72.equals(ra.getCodePrestation())
                || REGenresPrestations.GENRE_70.equals(ra.getCodePrestation())) {

            // si marie ou veuf -> tiersComplementaire1 l'autre conjoint
            // si divorce -> tiersComplementaire1 le dernier ex-conjoint

            // Reprendre la situation familiale
            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());

            // Reprendre les relations familiales
            ISFRelationFamiliale[] rfs = sf.getRelationsConjoints(ra.getIdTiersBeneficiaire(),
                    JACalendar.todayJJsMMsAAAA());

            // tiers complementaire1 = l'autre conjoint
            for (int j = 0; (rfs != null) && (j < rfs.length); j++) {

                ISFRelationFamiliale rf = rfs[j];

                String idMbrFamille1 = rf.getIdMembreFamilleHomme();
                ISFMembreFamille mf1 = sf.getMembreFamille(idMbrFamille1);
                String idTiers1 = mf1.getIdTiers();
                // Si l'idTiersComplémentaire1 est déjà renseignée, ne pas passer le if sinon l'idTiersComplémentaire1
                // de l'épouse
                // est écrasé par celui de l'ex-épouse
                if (!idTiers1.equals(ra.getIdTiersBeneficiaire())
                        && JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())) {
                    ra.setIdTiersComplementaire1(idTiers1);
                } else {
                    String idMbrFamille2 = rf.getIdMembreFamilleFemme();
                    ISFMembreFamille mf2 = sf.getMembreFamille(idMbrFamille2);
                    String idTiers2 = mf2.getIdTiers();
                    // Si l'idTiersComplémentaire1 est déjà renseignée, ne pas passer le if sinon
                    // l'idTiersComplémentaire1
                    // de l'épouse
                    // est écrasé par celui de l'ex-épouse
                    if (!idTiers2.equals(ra.getIdTiersBeneficiaire())
                            && JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())) {
                        ra.setIdTiersComplementaire1(idTiers2);
                    }
                }
            }

            // si celibataire -> tiersComplementaire1 rien
            if (rfs != null) {
                if (rfs.length == 0) {
                    ra.setIdTiersComplementaire1("");
                }
            }
        }
    }

    /**
     * charge une adresse de paiement valide.
     * 
     * <p>
     * si les id adresse de paiment et domaine d'adresses sont renseignes, charge et formatte l'adresse correspondante,
     * sinon recherche, charge et formatte une adresse pour le tiers courant.
     * </p>
     * 
     * @param session
     *            DOCUMENT ME!
     * @param raViewBean
     * 
     * @throws Exception
     */
    private void rechargerAdressePaiement(BSession session, RESaisieManuelleRABCPDViewBean saViewBean) throws Exception {

        // si le tiers beneficiaire a change on met a jours le tiers adresse paiement
        if (saViewBean.isTiersBeneficiaireChange()) {

            saViewBean.setIdTiersAdressePmtIC(saViewBean.getIdTiersAdressePmtICDepuisPyxis());
            // saViewBean.setIdDomaineApplicationIC(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        }

        // si le tiers beneficiaire est null, il ne sert a rien de faire une recherche
        // ce cas de figure peut survenir lors du chargement du viewBean utilise dans l'ecran rc
        if (JadeStringUtil.isIntegerEmpty(saViewBean.getIdTiersAdressePmtIC())) {
            return;
        }

        // if (JadeStringUtil.isEmpty(saViewBean.getIdDomaineApplicationIC())){
        // saViewBean.setIdDomaineApplicationIC(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        // }

        // charcher une adresse de paiement pour ce beneficiaire
        TIAdressePaiementData adresse = PRTiersHelper.getAdressePaiementData(session,
                session.getCurrentThreadTransaction(), saViewBean.getIdTiersAdressePmtIC(),
                IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, "", JACalendar.todayJJsMMsAAAA());

        saViewBean.setAdressePaiement(adresse);

        // formatter les infos de l'adresse pour l'affichage correct dans l'ecran
        if ((adresse != null) && !adresse.isNew()) {
            TIAdressePaiementDataSource source = new TIAdressePaiementDataSource();

            source.load(adresse);

            // formatter le no de ccp ou le no bancaire
            if (JadeStringUtil.isEmpty(adresse.getCcp())) {
                saViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementBanqueFormater().format(source));
            } else {
                saViewBean.setCcpOuBanqueFormatte(new TIAdressePaiementCppFormater().format(source));
            }

            // formatter l'adresse
            saViewBean.setAdresseFormattee(new TIAdressePaiementBeneficiaireFormater().format(source));
        } else {
            saViewBean.setCcpOuBanqueFormatte("");
            saViewBean.setAdresseFormattee("");

            // si le tiers beneficiaire a change et que l'on a pas trouve d'adresse
            // on enleve l'idTiersAdresseDePaiement
            if (saViewBean.isTiersBeneficiaireChange()) {
                saViewBean.setIdTiersAdressePmtIC("0");
            }
        }
    }

}
