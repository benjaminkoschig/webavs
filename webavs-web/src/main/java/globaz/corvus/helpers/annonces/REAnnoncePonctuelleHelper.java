/*
 * Créé le 20 août 07
 */
package globaz.corvus.helpers.annonces;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.corvus.api.annonces.IREAnnonces;
import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.annonces.REAnnonceHeader;
import globaz.corvus.db.annonces.REAnnonceRente;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel2A;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification10Eme;
import globaz.corvus.db.annonces.REAnnoncesAugmentationModification9Eme;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.basescalcul.REBasesCalculManager;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.demandes.REDemandeRenteAPI;
import globaz.corvus.db.demandes.REDemandeRenteInvalidite;
import globaz.corvus.db.demandes.REDemandeRenteJointDemande;
import globaz.corvus.db.demandes.REDemandeRenteJointDemandeManager;
import globaz.corvus.db.demandes.REPeriodeInvalidite;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteJoinAjour;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.db.rentesaccordees.RERenteAccordeeManager;
import globaz.corvus.db.rentesaccordees.RERenteCalculee;
import globaz.corvus.db.rentesaccordees.RERenteLieeJointPrestationAccordee;
import globaz.corvus.db.rentesaccordees.RERenteLieeJointPrestationAccordeeManager;
import globaz.corvus.exceptions.RETechnicalException;
import globaz.corvus.utils.RECodePrestationComplementaireUtil;
import globaz.corvus.utils.REPmtMensuel;
import globaz.corvus.utils.enumere.genre.prestations.REGenrePrestationEnum;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.annonces.REAnnoncePonctuelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.hera.api.ISFEnfant;
import globaz.hera.api.ISFMembreFamille;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFRelationFamiliale;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.SFSituationFamilialeFactory;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRCodeSystem;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.api.ITIPersonne;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressecourrier.TILocalite;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import ch.globaz.prestation.domaine.CodePrestation;

/**
 * @author SCR
 * 
 */
public class REAnnoncePonctuelleHelper extends PRAbstractHelper {

    @Override
    protected void _retrieve(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {

        REAnnoncePonctuelleViewBean vb = (REAnnoncePonctuelleViewBean) viewBean;
        BITransaction transaction = null;
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession((BSession) session);
            ra.setIdPrestationAccordee(vb.getIdRenteAccordee());
            ra.retrieve();

            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession((BSession) session);
            bc.setIdBasesCalcul(ra.getIdBaseCalcul());
            bc.retrieve(transaction);

            ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale((BSession) session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());
            String csEtatCivil = null;
            String csCantonDomicile = null;

            String idMembreFamille = "";
            ISFMembreFamilleRequerant[] mf = sf.getMembresFamille(ra.getIdTiersBeneficiaire());
            for (int i = 0; i < mf.length; i++) {
                ISFMembreFamilleRequerant membre = mf[i];
                // On récupère le bénéficiaire en tant que membre de famille
                if (ra.getIdTiersBeneficiaire().equals(membre.getIdTiers())) {
                    csEtatCivil = membre.getCsEtatCivil();

                    String dateAujourdhui = new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime());
                    PRTiersWrapper adresseDomicile = PRTiersHelper.getTiersAdresseDomicileParId(session,
                            ra.getIdTiersBeneficiaire(), dateAujourdhui);

                    if ((adresseDomicile != null)
                            && !JadeStringUtil.isBlankOrZero(adresseDomicile
                                    .getProperty(PRTiersWrapper.PROPERTY_ID_CANTON))) {
                        csCantonDomicile = adresseDomicile.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON);
                    } else if (!IConstantes.ID_PAYS_SUISSE.equals(membre.getCsNationalite())) {
                        csCantonDomicile = IConstantes.CS_LOCALITE_ETRANGER;
                    } else {
                        csCantonDomicile = "";
                    }

                    idMembreFamille = membre.getIdMembreFamille();
                    break;
                }
            }
            // Peut arriver dans le cas d'un enfant de la situation familialle, par exemple.
            if (csEtatCivil == null) {
                csEtatCivil = ISFSituationFamiliale.CS_ETAT_CIVIL_CELIBATAIRE;
            }

            vb.init((BSession) session, (BTransaction) transaction, ra, bc, csEtatCivil, csCantonDomicile,
                    idMembreFamille);

            // BZ 5176, si rente principale (code prestation 10,13,20,23,50,70) on compte les rentes liées
            // correspondantes afin de savoir si des annonces ponctuelles supplémentaires doivent être générées
            if ("10".equals(ra.getCodePrestation()) || "13".equals(ra.getCodePrestation())
                    || "20".equals(ra.getCodePrestation()) || "23".equals(ra.getCodePrestation())
                    || "50".equals(ra.getCodePrestation()) || "70".equals(ra.getCodePrestation())) {
                vb.setBesoinAnnonceRentesLieesSiModification(getRentesComplementairesEnCours((BSession) session,
                        ra.getId()).size() > 0);
            }
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
    }

    @Override
    protected void _update(final FWViewBeanInterface viewBean, final FWAction action,
            final globaz.globall.api.BISession session) throws Exception {

        REAnnoncePonctuelleViewBean vb = (REAnnoncePonctuelleViewBean) viewBean;
        BTransaction transaction = null;

        try {
            // BZ 5176, création d'une annonce ponctuelle pour la rente principale
            // court-circuit du corps de la méthode _update car elle sera rappelée dans l'ajout d'annonce
            if (vb.isCreerAnnonceRente()) {
                vb.setCreerAnnonceRente(false);
                this.ajouterAnnoncePonctuelle(vb, action, (BSession) session);
            } else {
                transaction = (BTransaction) ((BSession) session).newTransaction();
                transaction.openTransaction();

                updateWithTransaction(vb, (BSession) session, transaction);
            }

            // BZ 5176, annonces ponctuelles pour les rentes liées
            if (vb.isCreerAnnoncesRentesLiees() && !FWViewBeanInterface.ERROR.equals(vb.getMsgType())) {
                vb.setCreerAnnoncesRentesLiees(false);
                genererAnnoncesPonctuellesRentesLiees(vb, (BSession) session, transaction);
            }
        } catch (Exception e) {
            e.toString();
            e.printStackTrace();
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
    }

    public FWViewBeanInterface ajouterAnnoncePonctuelle(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session) throws Exception {
        REAnnoncePonctuelleViewBean vb = (REAnnoncePonctuelleViewBean) viewBean;
        if (JadeStringUtil.isBlank(vb.getCsCanton())) {
            vb.setMessage(session.getLabel("ERREUR_CANTON_DOMICILE_OBLIGATOIRE"));
            vb.setMsgType(FWViewBeanInterface.ERROR);
        } else {
            return this.ajouterAnnoncePonctuelle(viewBean, action, session, null);
        }
        return vb;
    }

    public FWViewBeanInterface ajouterAnnoncePonctuelle(final FWViewBeanInterface viewBean, final FWAction action,
            final BSession session, BTransaction transaction) throws Exception {

        REAnnoncePonctuelleViewBean vb = (REAnnoncePonctuelleViewBean) viewBean;
        if (vb.isCreerAnnonceRente()) {
            vb.setCreerAnnonceRente(false);
        }
        try {

            if ((transaction == null) || !transaction.isOpened()) {
                transaction = (BTransaction) session.newTransaction();
                transaction.openTransaction();
            }

            RERenteAccordee ra = new RERenteAccordee();
            ra.setSession(session);
            ra.setIdPrestationAccordee(vb.getIdRenteAccordee());
            ra.retrieve(transaction);

            updateWithTransaction(vb, session, transaction);

            // BZ 5785 si la rente est une rentes principale, ou API, mise à jour du canton de domicile de toutes la
            // famille dans la situation familiale (si besoin)
            PRTiersWrapper adresseActuelleTiersRentePrincipale = PRTiersHelper.getTiersAdresseDomicileParId(session,
                    ra.getIdTiersBeneficiaire(), JACalendar.todayJJsMMsAAAA());

            ISFSituationFamiliale situationFamiliale = SFSituationFamilialeFactory.getSituationFamiliale(session,
                    ISFSituationFamiliale.CS_DOMAINE_RENTES, ra.getIdTiersBeneficiaire());

            if (REGenrePrestationEnum.groupe1.contains(ra.getCodePrestation())
                    || REGenrePrestationEnum.groupe2.contains(ra.getCodePrestation())) {

                if (adresseActuelleTiersRentePrincipale == null) {
                    throw new Exception(session.getLabel("ERREUR_ADRESSE_DOMICILE_INTROUVABLE"));
                }

                ISFMembreFamilleRequerant[] membres = situationFamiliale.getMembresFamilleRequerant(
                        ra.getIdTiersBeneficiaire(), JACalendar.todayJJsMMsAAAA());

                for (ISFMembreFamilleRequerant unMembre : membres) {
                    if (!unMembre.getCsCantonDomicile().equals(
                            adresseActuelleTiersRentePrincipale.getProperty(PRTiersWrapper.PROPERTY_ID_CANTON))) {
                        SFMembreFamille membreDb = new SFMembreFamille();
                        membreDb.setIdMembreFamille(unMembre.getIdMembreFamille());
                        membreDb.setSession(session);
                        membreDb.retrieve();

                        if (!membreDb.isNew()) {
                            membreDb.setCsCantonDomicile(adresseActuelleTiersRentePrincipale
                                    .getProperty(PRTiersWrapper.PROPERTY_ID_CANTON));
                            membreDb.update();
                        }
                    }
                }
            } else {
                // si complémentaire, on ne met à jour le canton dans la situation familiale que pour le tiers

                // si le bénéficiaire (un enfant, par exemple) n'a pas d'adresse de domicile,
                // recherche de celle de son parent
                if (adresseActuelleTiersRentePrincipale == null) {
                    ISFMembreFamille[] parents = situationFamiliale.getParents(ra.getIdTiersBeneficiaire());
                    for (ISFMembreFamille unParent : parents) {
                        adresseActuelleTiersRentePrincipale = PRTiersHelper.getTiersAdresseDomicileParId(session,
                                unParent.getIdTiers(), JACalendar.todayJJsMMsAAAA());
                        if (adresseActuelleTiersRentePrincipale != null) {
                            break;
                        }
                    }
                    if (adresseActuelleTiersRentePrincipale == null) {
                        throw new Exception(session.getLabel("ERREUR_ADRESSE_DOMICILE_INTROUVABLE"));
                    }
                }

                SFMembreFamille membreDb = new SFMembreFamille();
                membreDb.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
                membreDb.setIdTiers(ra.getIdTiersBeneficiaire());
                membreDb.setSession(session);
                membreDb.retrieve();

                if (!membreDb.isNew()) {
                    membreDb.setCsCantonDomicile(adresseActuelleTiersRentePrincipale
                            .getProperty(PRTiersWrapper.PROPERTY_ID_CANTON));
                    membreDb.update();
                }
            }

            // Création de l'annonce ponctuelle...
            if ("9".equals(vb.getDroitApplique()) || "10".equals(vb.getDroitApplique())) {
                REAnnoncesAbstractLevel2A arc01 = ajouterAnnoncePonctuelleEnregistrement01(session, transaction, vb);
                REAnnoncesAbstractLevel2A arc02 = ajouterAnnoncePonctuelleEnregistrement02(session, transaction, vb,
                        ra.getCsGenreDroitApi());

                REAnnonceHeader annonceHeader = new REAnnonceHeader();
                annonceHeader.setSession(session);
                annonceHeader.setIdAnnonce(arc01.getId());
                annonceHeader.retrieve(transaction);
                annonceHeader.setIdLienAnnonce(arc02.getId());
                annonceHeader.update(transaction);

                createAnnonceRente(session, transaction, annonceHeader.getIdAnnonce(), ra.getIdPrestationAccordee());
            } else {
                throw new RETechnicalException("Unknown value for 'getDroitApplique()' : " + vb.getDroitApplique());
            }

            if (vb.isCreerAnnoncesRentesLiees()) {
                vb.setBesoinAnnonceRentesLieesSiModification(false);
                vb.setCreerAnnoncesRentesLiees(false);
                genererAnnoncesPonctuellesRentesLiees(vb, session, transaction);
            }
        } catch (Exception e) {
            if ((viewBean.getISession() != null) && (viewBean.getISession().getErrors() != null)) {
                ((BSession) viewBean.getISession()).addError(e.getMessage());
            }
            viewBean.setMessage(e.getMessage());
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            if (transaction != null) {
                transaction.setRollbackOnly();
            }
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
        return viewBean;
    }

    public FWViewBeanInterface ajouterAnnoncePonctuelleAncienNss(final FWViewBeanInterface viewBean,
            final FWAction action, final BSession session) throws Exception {

        String revisionNeuf = "9";
        String revisionDix = "10";

        BITransaction transaction = session.newTransaction();

        try {
            transaction.openTransaction();

            REAnnoncePonctuelleViewBean vb = (REAnnoncePonctuelleViewBean) viewBean;

            PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, vb.getIdTiersBeneficiaire());

            List<RERenteAccordee> list = asRaEnCoursAnnoncePonctuelleAncienNss(vb.getIdTiersBeneficiaire(), session);

            if (list.size() > 0) {

                for (RERenteAccordee ra : list) {

                    if (REGenresPrestations.GENRE_10.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_13.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_20.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_23.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_33.equals(ra.getCodePrestation())
                            ||
                            // REGenresPrestations.GENRE_43.equals(ra.getCodePrestation())||
                            REGenresPrestations.GENRE_50.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_53.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_70.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_72.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_73.equals(ra.getCodePrestation())
                            || (Integer.parseInt(ra.getCodePrestation()) > 80)) {

                        REBasesCalcul bc = new REBasesCalcul();
                        bc.setSession(session);
                        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                        bc.retrieve(transaction);

                        if (revisionNeuf.equals(bc.getDroitApplique())) {
                            REAnnoncesAugmentationModification9Eme arc4301 = this
                                    .ajouterAnnoncePonctuelleNouvelNss43_01(session, vb, ra);
                            REAnnoncesAugmentationModification9Eme arc4302 = this
                                    .ajouterAnnoncePonctuelleNouvelNss43_02(session, vb);

                            arc4301.setNoAssAyantDroit(JadeStringUtil.change(vb.getAncienNSS(), ".", ""));

                            arc4301.setPremierNoAssComplementaire(nssConjoint(vb.getIdTiersBeneficiaire(), session));
                            arc4301.setNouveauNoAssureAyantDroit(JadeStringUtil.change(
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                            arc4301.setCodeMutation("89");

                            arc4301.add(transaction);
                            arc4302.add(transaction);

                            String id4301 = arc4301.getId();
                            String id4302 = arc4302.getId();

                            REAnnonceHeader annonceHeader = new REAnnonceHeader();
                            annonceHeader.setSession(session);
                            annonceHeader.setIdAnnonce(id4301);
                            annonceHeader.retrieve(transaction);
                            annonceHeader.setIdLienAnnonce(id4302);
                            annonceHeader.update(transaction);

                            createAnnonceRente(session, (BTransaction) transaction, annonceHeader.getIdAnnonce(),
                                    ra.getIdPrestationAccordee());

                        } else if (revisionDix.equals(bc.getDroitApplique())) {
                            REAnnoncesAugmentationModification10Eme arc4601 = this
                                    .ajouterAnnoncePonctuelleNouvelNss46_01(session, vb, ra);
                            REAnnoncesAugmentationModification10Eme arc4602 = this
                                    .ajouterAnnoncePonctuelleNouvelNss46_02(session, vb);

                            arc4601.setNoAssAyantDroit(JadeStringUtil.change(vb.getAncienNSS(), ".", ""));

                            arc4601.setPremierNoAssComplementaire(nssConjoint(vb.getIdTiersBeneficiaire(), session));
                            arc4601.setNouveauNoAssureAyantDroit(JadeStringUtil.change(
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                            arc4601.setCodeMutation("89");

                            arc4601.add(transaction);
                            arc4602.add(transaction);

                            String id4601 = arc4601.getId();
                            String id4602 = arc4602.getId();

                            REAnnonceHeader annonceHeader = new REAnnonceHeader();
                            annonceHeader.setSession(session);
                            annonceHeader.setIdAnnonce(id4601);
                            annonceHeader.retrieve(transaction);
                            annonceHeader.setIdLienAnnonce(id4602);
                            annonceHeader.update(transaction);

                            createAnnonceRente(session, (BTransaction) transaction, annonceHeader.getIdAnnonce(),
                                    ra.getIdPrestationAccordee());
                        }
                    } else if (REGenresPrestations.GENRE_14.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_16.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_24.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_26.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_34.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_36.equals(ra.getCodePrestation())
                            ||
                            // REGenresPrestations.GENRE_44.equals(ra.getCodePrestation())||
                            REGenresPrestations.GENRE_54.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_56.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_74.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_76.equals(ra.getCodePrestation())) {

                        REBasesCalcul bc = new REBasesCalcul();
                        bc.setSession(session);
                        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                        bc.retrieve(transaction);

                        if (revisionNeuf.equals(bc.getDroitApplique())) {
                            REAnnoncesAugmentationModification9Eme arc4301 = this
                                    .ajouterAnnoncePonctuelleNouvelNss43_01(session, vb, ra);
                            REAnnoncesAugmentationModification9Eme arc4302 = this
                                    .ajouterAnnoncePonctuelleNouvelNss43_02(session, vb);

                            arc4301.setNoAssAyantDroit(JadeStringUtil.change(vb.getAncienNSS(), ".", ""));

                            arc4301.setPremierNoAssComplementaire(nssPere(vb.getIdTiersBeneficiaire(), session));
                            arc4301.setSecondNoAssComplementaire(nssMere(vb.getIdTiersBeneficiaire(), session));
                            arc4301.setNouveauNoAssureAyantDroit(JadeStringUtil.change(
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                            arc4301.setCodeMutation("89");

                            arc4301.add(transaction);
                            arc4302.add(transaction);

                            String id4301 = arc4301.getId();
                            String id4302 = arc4302.getId();

                            REAnnonceHeader annonceHeader = new REAnnonceHeader();
                            annonceHeader.setSession(session);
                            annonceHeader.setIdAnnonce(id4301);
                            annonceHeader.retrieve(transaction);
                            annonceHeader.setIdLienAnnonce(id4302);
                            annonceHeader.update(transaction);

                            createAnnonceRente(session, (BTransaction) transaction, annonceHeader.getIdAnnonce(),
                                    ra.getIdPrestationAccordee());

                        } else if (revisionDix.equals(bc.getDroitApplique())) {
                            REAnnoncesAugmentationModification10Eme arc4601 = this
                                    .ajouterAnnoncePonctuelleNouvelNss46_01(session, vb, ra);
                            REAnnoncesAugmentationModification10Eme arc4602 = this
                                    .ajouterAnnoncePonctuelleNouvelNss46_02(session, vb);

                            arc4601.setNoAssAyantDroit(JadeStringUtil.change(vb.getAncienNSS(), ".", ""));

                            arc4601.setPremierNoAssComplementaire(nssPere(vb.getIdTiersBeneficiaire(), session));
                            arc4601.setSecondNoAssComplementaire(nssMere(vb.getIdTiersBeneficiaire(), session));
                            arc4601.setNouveauNoAssureAyantDroit(JadeStringUtil.change(
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                            arc4601.setCodeMutation("89");

                            arc4601.add(transaction);
                            arc4602.add(transaction);

                            String id4601 = arc4601.getId();
                            String id4602 = arc4602.getId();

                            REAnnonceHeader annonceHeader = new REAnnonceHeader();
                            annonceHeader.setSession(session);
                            annonceHeader.setIdAnnonce(id4601);
                            annonceHeader.retrieve(transaction);
                            annonceHeader.setIdLienAnnonce(id4602);
                            annonceHeader.update(transaction);

                            createAnnonceRente(session, (BTransaction) transaction, annonceHeader.getIdAnnonce(),
                                    ra.getIdPrestationAccordee());

                        }
                    } else if (REGenresPrestations.GENRE_15.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_25.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_35.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_45.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_55.equals(ra.getCodePrestation())
                            || REGenresPrestations.GENRE_75.equals(ra.getCodePrestation())) {

                        REBasesCalcul bc = new REBasesCalcul();
                        bc.setSession(session);
                        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                        bc.retrieve(transaction);

                        if (revisionNeuf.equals(bc.getDroitApplique())) {

                            REAnnoncesAugmentationModification9Eme arc4301 = this
                                    .ajouterAnnoncePonctuelleNouvelNss43_01(session, vb, ra);
                            REAnnoncesAugmentationModification9Eme arc4302 = this
                                    .ajouterAnnoncePonctuelleNouvelNss43_02(session, vb);

                            arc4301.setNoAssAyantDroit(JadeStringUtil.change(vb.getAncienNSS(), ".", ""));

                            arc4301.setPremierNoAssComplementaire(nssMere(vb.getIdTiersBeneficiaire(), session));
                            arc4301.setSecondNoAssComplementaire(nssPere(vb.getIdTiersBeneficiaire(), session));
                            arc4301.setNouveauNoAssureAyantDroit(JadeStringUtil.change(
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                            arc4301.setCodeMutation("89");

                            arc4301.add(transaction);
                            arc4302.add(transaction);

                            String id4301 = arc4301.getId();
                            String id4302 = arc4302.getId();

                            REAnnonceHeader annonceHeader = new REAnnonceHeader();
                            annonceHeader.setSession(session);
                            annonceHeader.setIdAnnonce(id4301);
                            annonceHeader.retrieve(transaction);
                            annonceHeader.setIdLienAnnonce(id4302);
                            annonceHeader.update(transaction);

                            createAnnonceRente(session, (BTransaction) transaction, annonceHeader.getIdAnnonce(),
                                    ra.getIdPrestationAccordee());

                        } else if (revisionDix.equals(bc.getDroitApplique())) {

                            REAnnoncesAugmentationModification10Eme arc4601 = this
                                    .ajouterAnnoncePonctuelleNouvelNss46_01(session, vb, ra);
                            REAnnoncesAugmentationModification10Eme arc4602 = this
                                    .ajouterAnnoncePonctuelleNouvelNss46_02(session, vb);

                            arc4601.setNoAssAyantDroit(JadeStringUtil.change(vb.getAncienNSS(), ".", ""));

                            arc4601.setPremierNoAssComplementaire(nssMere(vb.getIdTiersBeneficiaire(), session));
                            arc4601.setSecondNoAssComplementaire(nssPere(vb.getIdTiersBeneficiaire(), session));
                            arc4601.setNouveauNoAssureAyantDroit(JadeStringUtil.change(
                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                            arc4601.setCodeMutation("89");

                            arc4601.add(transaction);
                            arc4602.add(transaction);

                            String id4601 = arc4601.getId();
                            String id4602 = arc4602.getId();

                            REAnnonceHeader annonceHeader = new REAnnonceHeader();
                            annonceHeader.setSession(session);
                            annonceHeader.setIdAnnonce(id4601);
                            annonceHeader.retrieve(transaction);
                            annonceHeader.setIdLienAnnonce(id4602);
                            annonceHeader.update(transaction);

                            createAnnonceRente(session, (BTransaction) transaction, annonceHeader.getIdAnnonce(),
                                    ra.getIdPrestationAccordee());

                        }
                    }
                }

                RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager ram = new RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager();
                ram.setSession(session);
                ram.setForIdTiersBaseCalcul(vb.getIdTiersBeneficiaire());
                ram.find(transaction);

                ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                        ISFSituationFamiliale.CS_DOMAINE_RENTES, vb.getIdTiersBeneficiaire());

                if (ram.size() != 0) {

                    RERenteAccJoinTblTiersJoinDemRenteJoinAjour raJTJD = (RERenteAccJoinTblTiersJoinDemRenteJoinAjour) ram
                            .getFirstEntity();

                    ISFMembreFamille mf = sf.getMembreFamille(raJTJD.getIdMembreFamille());

                    if (ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE.equals(mf.getCsEtatCivil())
                            || ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT.equals(mf.getCsEtatCivil())
                            || ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_JUDICIAIREMENT.equals(mf.getCsEtatCivil())
                            || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE.equals(mf.getCsEtatCivil())
                            || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_DE_FAIT.equals(mf
                                    .getCsEtatCivil())
                            || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_JUDICIAIREMENT.equals(mf
                                    .getCsEtatCivil())) {

                        ISFRelationFamiliale[] relfam = sf.getRelationsConjoints(mf.getIdTiers(), null);
                        ISFMembreFamille conjoint = null;

                        if ((relfam != null) && (relfam.length > 0)) {
                            for (ISFRelationFamiliale rf : relfam) {
                                if (PRACORConst.CS_FEMME.equals(mf.getCsSexe())) {
                                    conjoint = sf.getMembreFamille(rf.getIdMembreFamilleHomme());
                                    break;
                                } else {
                                    conjoint = sf.getMembreFamille(rf.getIdMembreFamilleFemme());
                                    break;
                                }
                            }
                        }

                        if (null != conjoint) {
                            List<RERenteAccordee> listConjoint = asRaEnCoursAnnoncePonctuelleAncienNss(
                                    conjoint.getIdTiers(), session);

                            if (listConjoint.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {

                                    RERenteAccordee ra = list.get(i);

                                    REBasesCalcul bc = new REBasesCalcul();
                                    bc.setSession(session);
                                    bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                                    bc.retrieve(transaction);

                                    if (revisionNeuf.equals(bc.getDroitApplique())) {

                                        REAnnoncesAugmentationModification9Eme arc4301 = this
                                                .ajouterAnnoncePonctuelleNouvelNss43_01(session, ra,
                                                        conjoint.getCsCantonDomicile());
                                        REAnnoncesAugmentationModification9Eme arc4302 = this
                                                .ajouterAnnoncePonctuelleNouvelNss43_02(session, ra);

                                        arc4301.setIdTiers(conjoint.getIdTiers());
                                        arc4302.setIdTiers(conjoint.getIdTiers());

                                        PRTiersWrapper twConjoint = PRTiersHelper.getTiersParId(session,
                                                conjoint.getIdTiers());

                                        arc4301.setNoAssAyantDroit(JadeStringUtil.change(
                                                twConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));

                                        arc4301.setPremierNoAssComplementaire(JadeStringUtil.change(
                                                tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));

                                        if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                || "0".equals(ra.getIdTiersComplementaire1())) {
                                            ra.setIdTiersComplementaire1(tw
                                                    .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                            ra.save(transaction);
                                        }

                                        arc4301.setSecondNoAssComplementaire("");
                                        arc4301.setNouveauNoAssureAyantDroit("");
                                        arc4301.setCodeMutation("99");

                                        arc4301.add(transaction);
                                        arc4302.add(transaction);

                                        String id4301 = arc4301.getId();
                                        String id4302 = arc4302.getId();

                                        REAnnonceHeader annonceHeader = new REAnnonceHeader();
                                        annonceHeader.setSession(session);
                                        annonceHeader.setIdAnnonce(id4301);
                                        annonceHeader.retrieve(transaction);
                                        annonceHeader.setIdLienAnnonce(id4302);
                                        annonceHeader.update(transaction);

                                        createAnnonceRente(session, (BTransaction) transaction,
                                                annonceHeader.getIdAnnonce(), ra.getIdPrestationAccordee());

                                    } else if (revisionDix.equals(bc.getDroitApplique())) {

                                        REAnnoncesAugmentationModification10Eme arc4601 = this
                                                .ajouterAnnoncePonctuelleNouvelNss46_01(session, ra,
                                                        conjoint.getCsCantonDomicile());
                                        REAnnoncesAugmentationModification10Eme arc4602 = this
                                                .ajouterAnnoncePonctuelleNouvelNss46_02(session, ra);

                                        arc4601.setIdTiers(conjoint.getIdTiers());
                                        arc4602.setIdTiers(conjoint.getIdTiers());

                                        PRTiersWrapper twConjoint = PRTiersHelper.getTiersParId(session,
                                                conjoint.getIdTiers());

                                        arc4601.setNoAssAyantDroit(JadeStringUtil.change(
                                                twConjoint.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));

                                        arc4601.setPremierNoAssComplementaire(JadeStringUtil.change(
                                                tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));

                                        if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                || "0".equals(ra.getIdTiersComplementaire1())) {
                                            ra.setIdTiersComplementaire1(tw
                                                    .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                            ra.save(transaction);
                                        }

                                        arc4601.setSecondNoAssComplementaire("");
                                        arc4601.setNouveauNoAssureAyantDroit("");
                                        arc4601.setCodeMutation("99");

                                        arc4601.add(transaction);
                                        arc4602.add(transaction);

                                        String id4601 = arc4601.getId();
                                        String id4602 = arc4602.getId();

                                        REAnnonceHeader annonceHeader = new REAnnonceHeader();
                                        annonceHeader.setSession(session);
                                        annonceHeader.setIdAnnonce(id4601);
                                        annonceHeader.retrieve(transaction);
                                        annonceHeader.setIdLienAnnonce(id4602);
                                        annonceHeader.update(transaction);

                                        createAnnonceRente(session, (BTransaction) transaction,
                                                annonceHeader.getIdAnnonce(), ra.getIdPrestationAccordee());

                                    }
                                }
                            }
                        }
                    }
                }

                ISFMembreFamilleRequerant[] mbsFamille = sf.getMembresFamilleRequerant(vb.getIdTiersBeneficiaire(),
                        JACalendar.todayJJsMMsAAAA());

                for (int j = 0; (mbsFamille != null) && (j < mbsFamille.length); j++) {
                    ISFMembreFamilleRequerant membreFamille = mbsFamille[j];
                    ISFEnfant enfant = sf.getEnfant(membreFamille.getIdMembreFamille());
                    // Pas de traitement pour les enfants avec nss 000.0000.0000.00
                    if (!JadeStringUtil.isEmpty(enfant.getNss())) {
                        List<RERenteAccordee> listRAEnfant = asRaEnCoursAnnoncePonctuelleAncienNss(
                                membreFamille.getIdTiers(), session);
                        if (listRAEnfant.size() > 0) {
                            for (RERenteAccordee ra : listRAEnfant) {
                                if (REGenresPrestations.GENRE_14.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_16.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_24.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_26.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_34.equals(ra.getCodePrestation())
                                        ||
                                        // REGenresPrestations.GENRE_44.equals(ra.getCodePrestation())||
                                        REGenresPrestations.GENRE_54.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_56.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_74.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_76.equals(ra.getCodePrestation())) {

                                    REBasesCalcul bc = new REBasesCalcul();
                                    bc.setSession(session);
                                    bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                                    bc.retrieve(transaction);

                                    if (revisionNeuf.equals(bc.getDroitApplique())) {

                                        REAnnoncesAugmentationModification9Eme arc4301 = this
                                                .ajouterAnnoncePonctuelleNouvelNss43_01(session, ra,
                                                        membreFamille.getCsCantonDomicile());
                                        REAnnoncesAugmentationModification9Eme arc4302 = this
                                                .ajouterAnnoncePonctuelleNouvelNss43_02(session, ra);

                                        PRTiersWrapper twEnfant = PRTiersHelper.getTiersParId(session,
                                                ra.getIdTiersBeneficiaire());

                                        if (PRACORConst.CS_HOMME.equals(tw.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
                                            arc4301.setNoAssAyantDroit(JadeStringUtil.change(
                                                    twEnfant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".",
                                                    ""));
                                            arc4301.setPremierNoAssComplementaire(JadeStringUtil.change(
                                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                                            arc4301.setSecondNoAssComplementaire(nssMere(ra.getIdTiersBeneficiaire(),
                                                    session));

                                            if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                    || "0".equals(ra.getIdTiersComplementaire1())
                                                    || JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                    || "0".equals(ra.getIdTiersComplementaire2())) {
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                        || "0".equals(ra.getIdTiersComplementaire1())) {
                                                    ra.setIdTiersComplementaire1(tw
                                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                                }
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                        || "0".equals(ra.getIdTiersComplementaire2())) {
                                                    String idTiersMere = idTiersMere(ra.getIdTiersBeneficiaire(),
                                                            session);
                                                    if (!JadeStringUtil.isEmpty(idTiersMere)) {
                                                        ra.setIdTiersComplementaire2(idTiersMere);
                                                    }
                                                }
                                                ra.save(transaction);
                                            }

                                            arc4301.setNouveauNoAssureAyantDroit("");
                                            arc4301.setCodeMutation("99");
                                        } else {
                                            arc4301.setNoAssAyantDroit(JadeStringUtil.change(
                                                    twEnfant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".",
                                                    ""));

                                            arc4301.setPremierNoAssComplementaire(nssPere(ra.getIdTiersBeneficiaire(),
                                                    session));
                                            arc4301.setSecondNoAssComplementaire(JadeStringUtil.change(
                                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));

                                            if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                    || "0".equals(ra.getIdTiersComplementaire1())
                                                    || JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                    || "0".equals(ra.getIdTiersComplementaire2())) {
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                        || "0".equals(ra.getIdTiersComplementaire1())) {
                                                    String idTiersPere = idTiersPere(ra.getIdTiersBeneficiaire(),
                                                            session);
                                                    if (!JadeStringUtil.isEmpty(idTiersPere)) {
                                                        ra.setIdTiersComplementaire2(idTiersPere);
                                                    }
                                                }
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                        || "0".equals(ra.getIdTiersComplementaire2())) {
                                                    ra.setIdTiersComplementaire1(tw
                                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                                }
                                                ra.save(transaction);
                                            }

                                            arc4301.setNouveauNoAssureAyantDroit("");
                                            arc4301.setCodeMutation("99");
                                        }

                                        arc4301.add(transaction);
                                        arc4302.add(transaction);

                                        String id4301 = arc4301.getId();
                                        String id4302 = arc4302.getId();

                                        REAnnonceHeader annonceHeader = new REAnnonceHeader();
                                        annonceHeader.setSession(session);
                                        annonceHeader.setIdAnnonce(id4301);
                                        annonceHeader.retrieve(transaction);
                                        annonceHeader.setIdLienAnnonce(id4302);
                                        annonceHeader.update(transaction);

                                        createAnnonceRente(session, (BTransaction) transaction,
                                                annonceHeader.getIdAnnonce(), ra.getIdPrestationAccordee());

                                    } else if (revisionDix.equals(bc.getDroitApplique())) {

                                        REAnnoncesAugmentationModification10Eme arc4601 = this
                                                .ajouterAnnoncePonctuelleNouvelNss46_01(session, ra,
                                                        membreFamille.getCsCantonDomicile());
                                        REAnnoncesAugmentationModification10Eme arc4602 = this
                                                .ajouterAnnoncePonctuelleNouvelNss46_02(session, ra);

                                        PRTiersWrapper twEnfant = PRTiersHelper.getTiersParId(session,
                                                ra.getIdTiersBeneficiaire());

                                        if (PRACORConst.CS_HOMME.equals(tw.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
                                            arc4601.setNoAssAyantDroit(JadeStringUtil.change(
                                                    twEnfant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".",
                                                    ""));

                                            arc4601.setPremierNoAssComplementaire(JadeStringUtil.change(
                                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                                            arc4601.setSecondNoAssComplementaire(nssMere(ra.getIdTiersBeneficiaire(),
                                                    session));

                                            if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                    || "0".equals(ra.getIdTiersComplementaire1())
                                                    || JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                    || "0".equals(ra.getIdTiersComplementaire2())) {
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                        || "0".equals(ra.getIdTiersComplementaire1())) {
                                                    ra.setIdTiersComplementaire1(tw
                                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                                }
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                        || "0".equals(ra.getIdTiersComplementaire2())) {
                                                    String idTiersMere = idTiersMere(ra.getIdTiersBeneficiaire(),
                                                            session);
                                                    if (!JadeStringUtil.isEmpty(idTiersMere)) {
                                                        ra.setIdTiersComplementaire2(idTiersMere);
                                                    }
                                                }
                                                ra.save(transaction);
                                            }

                                            arc4601.setNouveauNoAssureAyantDroit("");
                                            arc4601.setCodeMutation("99");
                                        } else {
                                            arc4601.setNoAssAyantDroit(JadeStringUtil.change(
                                                    twEnfant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".",
                                                    ""));

                                            arc4601.setPremierNoAssComplementaire(nssPere(ra.getIdTiersBeneficiaire(),
                                                    session));
                                            arc4601.setSecondNoAssComplementaire(JadeStringUtil.change(
                                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));

                                            if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                    || "0".equals(ra.getIdTiersComplementaire1())
                                                    || JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                    || "0".equals(ra.getIdTiersComplementaire2())) {
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                        || "0".equals(ra.getIdTiersComplementaire1())) {
                                                    String idTiersPere = idTiersPere(ra.getIdTiersBeneficiaire(),
                                                            session);
                                                    if (!JadeStringUtil.isEmpty(idTiersPere)) {
                                                        ra.setIdTiersComplementaire2(idTiersPere);
                                                    }
                                                }
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                        || "0".equals(ra.getIdTiersComplementaire2())) {
                                                    ra.setIdTiersComplementaire1(tw
                                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                                }
                                                ra.save(transaction);
                                            }

                                            arc4601.setNouveauNoAssureAyantDroit("");
                                            arc4601.setCodeMutation("99");
                                        }

                                        arc4601.add(transaction);
                                        arc4602.add(transaction);

                                        String id4601 = arc4601.getId();
                                        String id4602 = arc4602.getId();

                                        REAnnonceHeader annonceHeader = new REAnnonceHeader();
                                        annonceHeader.setSession(session);
                                        annonceHeader.setIdAnnonce(id4601);
                                        annonceHeader.retrieve(transaction);
                                        annonceHeader.setIdLienAnnonce(id4602);
                                        annonceHeader.update(transaction);

                                        createAnnonceRente(session, (BTransaction) transaction,
                                                annonceHeader.getIdAnnonce(), ra.getIdPrestationAccordee());
                                    }
                                } else if (REGenresPrestations.GENRE_15.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_25.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_35.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_45.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_55.equals(ra.getCodePrestation())
                                        || REGenresPrestations.GENRE_75.equals(ra.getCodePrestation())) {

                                    REBasesCalcul bc = new REBasesCalcul();
                                    bc.setSession(session);
                                    bc.setIdBasesCalcul(ra.getIdBaseCalcul());
                                    bc.retrieve(transaction);

                                    if (revisionNeuf.equals(bc.getDroitApplique())) {
                                        REAnnoncesAugmentationModification9Eme arc4301 = this
                                                .ajouterAnnoncePonctuelleNouvelNss43_01(session, ra,
                                                        membreFamille.getCsCantonDomicile());
                                        REAnnoncesAugmentationModification9Eme arc4302 = this
                                                .ajouterAnnoncePonctuelleNouvelNss43_02(session, ra);

                                        PRTiersWrapper twEnfant = PRTiersHelper.getTiersParId(session,
                                                ra.getIdTiersBeneficiaire());

                                        if (PRACORConst.CS_HOMME.equals(tw.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
                                            arc4301.setNoAssAyantDroit(JadeStringUtil.change(
                                                    twEnfant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".",
                                                    ""));

                                            arc4301.setPremierNoAssComplementaire(nssMere(ra.getIdTiersBeneficiaire(),
                                                    session));
                                            arc4301.setSecondNoAssComplementaire(JadeStringUtil.change(
                                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));

                                            if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                    || "0".equals(ra.getIdTiersComplementaire1())
                                                    || JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                    || "0".equals(ra.getIdTiersComplementaire2())) {
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                        || "0".equals(ra.getIdTiersComplementaire1())) {
                                                    String idTiersMere = idTiersMere(ra.getIdTiersBeneficiaire(),
                                                            session);
                                                    if (!JadeStringUtil.isEmpty(idTiersMere)) {
                                                        ra.setIdTiersComplementaire2(idTiersMere);
                                                    }
                                                }
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                        || "0".equals(ra.getIdTiersComplementaire2())) {
                                                    ra.setIdTiersComplementaire1(tw
                                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                                }
                                                ra.save(transaction);
                                            }

                                            arc4301.setNouveauNoAssureAyantDroit("");
                                            arc4301.setCodeMutation("99");

                                        } else {
                                            arc4301.setNoAssAyantDroit(JadeStringUtil.change(
                                                    twEnfant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".",
                                                    ""));

                                            arc4301.setPremierNoAssComplementaire(JadeStringUtil.change(
                                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                                            arc4301.setSecondNoAssComplementaire(nssPere(ra.getIdTiersBeneficiaire(),
                                                    session));

                                            if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                    || "0".equals(ra.getIdTiersComplementaire1())
                                                    || JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                    || "0".equals(ra.getIdTiersComplementaire2())) {
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                        || "0".equals(ra.getIdTiersComplementaire1())) {
                                                    ra.setIdTiersComplementaire1(tw
                                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                                }
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())) {
                                                    String idTiersPere = idTiersPere(ra.getIdTiersBeneficiaire(),
                                                            session);
                                                    if (!JadeStringUtil.isEmpty(idTiersPere)) {
                                                        ra.setIdTiersComplementaire2(idTiersPere);
                                                    }
                                                }
                                                ra.save(transaction);
                                            }

                                            arc4301.setNouveauNoAssureAyantDroit("");
                                            arc4301.setCodeMutation("99");
                                        }

                                        arc4301.add(transaction);
                                        arc4302.add(transaction);

                                        String id4301 = arc4301.getId();
                                        String id4302 = arc4302.getId();

                                        REAnnonceHeader annonceHeader = new REAnnonceHeader();
                                        annonceHeader.setSession(session);
                                        annonceHeader.setIdAnnonce(id4301);
                                        annonceHeader.retrieve(transaction);
                                        annonceHeader.setIdLienAnnonce(id4302);
                                        annonceHeader.update(transaction);

                                        createAnnonceRente(session, (BTransaction) transaction,
                                                annonceHeader.getIdAnnonce(), ra.getIdPrestationAccordee());

                                    } else if (revisionDix.equals(bc.getDroitApplique())) {
                                        REAnnoncesAugmentationModification10Eme arc4601 = this
                                                .ajouterAnnoncePonctuelleNouvelNss46_01(session, ra,
                                                        membreFamille.getCsCantonDomicile());
                                        REAnnoncesAugmentationModification10Eme arc4602 = this
                                                .ajouterAnnoncePonctuelleNouvelNss46_02(session, ra);

                                        PRTiersWrapper twEnfant = PRTiersHelper.getTiersParId(session,
                                                ra.getIdTiersBeneficiaire());

                                        if (PRACORConst.CS_HOMME.equals(tw.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
                                            arc4601.setNoAssAyantDroit(JadeStringUtil.change(
                                                    twEnfant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".",
                                                    ""));

                                            arc4601.setPremierNoAssComplementaire(nssMere(ra.getIdTiersBeneficiaire(),
                                                    session));
                                            arc4601.setSecondNoAssComplementaire(JadeStringUtil.change(
                                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));

                                            if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                    || "0".equals(ra.getIdTiersComplementaire1())
                                                    || JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                    || "0".equals(ra.getIdTiersComplementaire2())) {
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                        || "0".equals(ra.getIdTiersComplementaire1())) {
                                                    String idTiersMere = idTiersMere(ra.getIdTiersBeneficiaire(),
                                                            session);
                                                    if (!JadeStringUtil.isEmpty(idTiersMere)) {
                                                        ra.setIdTiersComplementaire2(idTiersMere);
                                                    }
                                                }
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                        || "0".equals(ra.getIdTiersComplementaire2())) {
                                                    ra.setIdTiersComplementaire1(tw
                                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                                }
                                                ra.save(transaction);
                                            }

                                            arc4601.setNouveauNoAssureAyantDroit("");
                                            arc4601.setCodeMutation("99");

                                        } else {
                                            arc4601.setNoAssAyantDroit(JadeStringUtil.change(
                                                    twEnfant.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".",
                                                    ""));

                                            arc4601.setPremierNoAssComplementaire(JadeStringUtil.change(
                                                    tw.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), ".", ""));
                                            arc4601.setSecondNoAssComplementaire(nssPere(ra.getIdTiersBeneficiaire(),
                                                    session));

                                            if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                    || "0".equals(ra.getIdTiersComplementaire1())
                                                    || JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                    || "0".equals(ra.getIdTiersComplementaire2())) {
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire1())
                                                        || "0".equals(ra.getIdTiersComplementaire1())) {
                                                    ra.setIdTiersComplementaire1(tw
                                                            .getProperty(PRTiersWrapper.PROPERTY_ID_TIERS));
                                                }
                                                if (JadeStringUtil.isEmpty(ra.getIdTiersComplementaire2())
                                                        || "0".equals(ra.getIdTiersComplementaire2())) {
                                                    String idTiersPere = idTiersPere(ra.getIdTiersBeneficiaire(),
                                                            session);
                                                    if (!JadeStringUtil.isEmpty(idTiersPere)) {
                                                        ra.setIdTiersComplementaire2(idTiersPere);
                                                    }
                                                }
                                                ra.save(transaction);
                                            }

                                            arc4601.setNouveauNoAssureAyantDroit("");
                                            arc4601.setCodeMutation("99");
                                        }

                                        arc4601.add(transaction);
                                        arc4602.add(transaction);

                                        String id4601 = arc4601.getId();
                                        String id4602 = arc4602.getId();

                                        REAnnonceHeader annonceHeader = new REAnnonceHeader();
                                        annonceHeader.setSession(session);
                                        annonceHeader.setIdAnnonce(id4601);
                                        annonceHeader.retrieve(transaction);
                                        annonceHeader.setIdLienAnnonce(id4602);
                                        annonceHeader.update(transaction);

                                        createAnnonceRente(session, (BTransaction) transaction,
                                                annonceHeader.getIdAnnonce(), ra.getIdPrestationAccordee());
                                    }
                                }
                            }
                        }
                    }
                }
            }
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
        return viewBean;
    }

    private REAnnoncesAbstractLevel2A ajouterAnnoncePonctuelleEnregistrement01(final BSession session,
            final BTransaction transaction, final REAnnoncePonctuelleViewBean viewBean) throws Exception {

        REAnnoncesAbstractLevel2A annonce_01;

        if ("9".equals(viewBean.getDroitApplique())) {
            annonce_01 = new REAnnoncesAugmentationModification9Eme();
            annonce_01.setCodeApplication("43");
        } else if ("10".equals(viewBean.getDroitApplique())) {
            annonce_01 = new REAnnoncesAugmentationModification10Eme();
            annonce_01.setCodeApplication("46");
        } else {
            throw new RETechnicalException("Unknown value for 'getDroitApplique()' : " + viewBean.getDroitApplique());
        }

        annonce_01.setSession(session);
        annonce_01.setCodeEnregistrement01("01");
        annonce_01.setIdTiers(viewBean.getIdTiersBeneficiaire());
        annonce_01.setNumeroCaisse(CaisseHelperFactory.getInstance().getNoCaisse(session.getApplication()));
        annonce_01.setNumeroAgence(CaisseHelperFactory.getInstance().getNoAgence(session.getApplication()));
        annonce_01.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce_01.setReferenceCaisseInterne(session.getUserId());

        PRTiersWrapper tw = PRTiersHelper.getTiersParId(session, viewBean.getIdTiersBeneficiaire());
        annonce_01.setNoAssAyantDroit(JadeStringUtil.change(tw.getNSS(), ".", ""));

        if (!JadeStringUtil.isBlankOrZero(viewBean.getNssComplementaire1())) {
            String nssComplementaire1 = "756." + viewBean.getNssComplementaire1();
            nssComplementaire1 = JadeStringUtil.change(nssComplementaire1, ".", "");
            annonce_01.setPremierNoAssComplementaire(nssComplementaire1);
        }
        if (!JadeStringUtil.isBlankOrZero(viewBean.getNssComplementaire2())) {
            String nssComplementaire2 = "756." + viewBean.getNssComplementaire2();
            nssComplementaire2 = JadeStringUtil.change(nssComplementaire2, ".", "");
            annonce_01.setSecondNoAssComplementaire(nssComplementaire2);
        }

        annonce_01.setEtatCivil(viewBean.getCodeEtatCivil());
        annonce_01.setIsRefugie(viewBean.getCodeRefugie());

        if (!JadeStringUtil.isBlankOrZero(viewBean.getCsCanton()) && (viewBean.getCsCanton().length() > 3)) {
            annonce_01.setCantonEtatDomicile(TILocalite.getCodeOFASCanton(viewBean.getCsCanton()));
        } else {
            annonce_01.setCantonEtatDomicile(viewBean.getCsCanton());
        }

        annonce_01.setGenrePrestation(viewBean.getGenrePrestation());
        annonce_01.setCodeMutation("99");
        annonce_01.setMoisRapport(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session))));
        annonce_01.setCasSpecial1(viewBean.getCs1());
        annonce_01.setCasSpecial2(viewBean.getCs2());
        annonce_01.setCasSpecial3(viewBean.getCs3());
        annonce_01.setCasSpecial4(viewBean.getCs4());
        annonce_01.setCasSpecial5(viewBean.getCs5());

        annonce_01.add(transaction);

        return annonce_01;
    }

    private REAnnoncesAbstractLevel2A ajouterAnnoncePonctuelleEnregistrement02(final BSession session,
            final BTransaction transaction, final REAnnoncePonctuelleViewBean viewBean, String csGenreDroitAPI)
            throws Exception {

        REAnnoncesAbstractLevel2A annonce02;

        if ("9".equals(viewBean.getDroitApplique())) {
            annonce02 = new REAnnoncesAugmentationModification9Eme();
            annonce02.setCodeApplication("43");
        } else if ("10".equals(viewBean.getDroitApplique())) {
            annonce02 = new REAnnoncesAugmentationModification10Eme();
            annonce02.setCodeApplication("46");
        } else {
            throw new RETechnicalException("Unknown value for 'getDroitApplique()' : " + viewBean.getDroitApplique());
        }

        annonce02.setSession(session);
        annonce02.setCodeEnregistrement01("02");
        annonce02.setIdTiers(viewBean.getIdTiersBeneficiaire());
        annonce02.setRamDeterminant(viewBean.getRAM());
        annonce02.setNumeroCaisse(CaisseHelperFactory.getInstance().getNoCaisse(session.getApplication()));
        annonce02.setNumeroAgence(CaisseHelperFactory.getInstance().getNoAgence(session.getApplication()));
        annonce02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce02.setCasSpecial1(viewBean.getCs1());
        annonce02.setCasSpecial2(viewBean.getCs2());
        annonce02.setCasSpecial3(viewBean.getCs3());
        annonce02.setCasSpecial4(viewBean.getCs4());
        annonce02.setCasSpecial5(viewBean.getCs5());

        if (!JadeStringUtil.isBlank(viewBean.getGenrePrestation())) {
            CodePrestation codePrestation = CodePrestation.getCodePrestation(Integer.parseInt(viewBean
                    .getGenrePrestation()));

            // Spécifique AI/API
            if ((codePrestation.isAPI() || codePrestation.isAI())) {

                // BZ 5174, le numéro de l'office AI doit être sur 2 positions, suppression du 1er chiffre (uniquement
                // pour
                // 9e revision)
                if ("9".equals(viewBean.getDroitApplique())) {
                    annonce02.setOfficeAICompetent(JadeStringUtil.remove(viewBean.getOfficeAI(), 0, 1));
                } else if ("10".equals(viewBean.getDroitApplique())) {
                    annonce02.setOfficeAICompetent(viewBean.getOfficeAI());
                }
                annonce02.setCodeInfirmite(viewBean.getCleInfirmite());

                annonce02.setSurvenanceEvenAssure(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                        .convertDate_MMxAAAA_to_AAAAMM(viewBean.getSurvenanceEvenementAssure())));

                if ((viewBean.getIsInvaliditePrecoce() != null) && viewBean.getIsInvaliditePrecoce().booleanValue()) {
                    annonce02.setAgeDebutInvalidite("1");
                } else {
                    annonce02.setAgeDebutInvalidite("0");
                }

                if (codePrestation.isAPI()) {
                    annonce02.setDegreInvalidite("");
                    annonce02.setAgeDebutInvalidite("");
                    annonce02.setRamDeterminant("");

                    REBasesCalcul bc = new REBasesCalcul();
                    bc.setSession(session);
                    bc.setIdBasesCalcul(viewBean.getIdBaseCalcul());
                    bc.retrieve(transaction);

                    RERenteCalculee rc = new RERenteCalculee();
                    rc.setSession(session);
                    rc.setIdRenteCalculee(bc.getIdRenteCalculee());
                    rc.retrieve(transaction);

                    REDemandeRenteAPI api = new REDemandeRenteAPI();
                    api.setSession(session);
                    api.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
                    api.setIdRenteCalculee(rc.getIdRenteCalculee());
                    api.retrieve(transaction);

                    FWParametersUserCode userCode = new FWParametersUserCode();
                    userCode.setSession(session);
                    userCode.setIdCodeSysteme(csGenreDroitAPI);
                    userCode.setIdLangue("F");
                    userCode.retrieve();

                    annonce02.setGenreDroitAPI(userCode.getCodeUtilisateur());
                    annonce02.setDegreInvalidite("");
                } else {

                    annonce02.setGenreDroitAPI("");
                    annonce02.setDegreInvalidite(viewBean.getDegreInvalidite());
                }
            }
        } else {
            annonce02.setOfficeAICompetent("");
            annonce02.setCodeInfirmite("");
            annonce02.setSurvenanceEvenAssure("");
            annonce02.setAgeDebutInvalidite("");
            annonce02.setGenreDroitAPI("");
            annonce02.setDegreInvalidite("");
        }

        annonce02.add(transaction);

        return annonce02;
    }

    private REAnnoncesAugmentationModification9Eme ajouterAnnoncePonctuelleNouvelNss43_01(final BSession session,
            final REAnnoncePonctuelleViewBean vb, final RERenteAccordee ra) throws Exception {

        REAnnoncesAugmentationModification9Eme annonce43_01 = new REAnnoncesAugmentationModification9Eme();
        annonce43_01.setSession(session);
        annonce43_01.setIdTiers(vb.getIdTiersBeneficiaire());
        annonce43_01.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce43_01.setCodeApplication("43");
        annonce43_01.setCodeEnregistrement01("01");
        annonce43_01.setNumeroCaisse(CaisseHelperFactory.getInstance().getNoCaisse(session.getApplication()));
        annonce43_01.setNumeroAgence(CaisseHelperFactory.getInstance().getNoAgence(session.getApplication()));
        annonce43_01.setReferenceCaisseInterne("PONCT" + session.getUserId());
        annonce43_01.setEtatCivil(vb.getCodeEtatCivil());
        if (JadeStringUtil.isEmpty(vb.getCodeRefugie())) {
            annonce43_01.setIsRefugie("0");
        } else {
            annonce43_01.setIsRefugie(vb.getCodeRefugie());
        }
        if (!JadeStringUtil.isBlankOrZero(vb.getCsCanton()) && (vb.getCsCanton().length() > 3)) {
            annonce43_01.setCantonEtatDomicile(TILocalite.getCodeOFASCanton(vb.getCsCanton()));
        } else {
            annonce43_01.setCantonEtatDomicile(vb.getCsCanton());
        }
        annonce43_01.setGenrePrestation(ra.getCodePrestation());
        annonce43_01.setMoisRapport(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session))));

        return annonce43_01;
    }

    private REAnnoncesAugmentationModification9Eme ajouterAnnoncePonctuelleNouvelNss43_01(final BSession session,
            final RERenteAccordee ra, final String csCantonDomicile) throws Exception {

        REAnnoncesAugmentationModification9Eme annonce43_01 = new REAnnoncesAugmentationModification9Eme();
        annonce43_01.setSession(session);
        annonce43_01.setIdTiers(ra.getIdTiersBeneficiaire());
        annonce43_01.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce43_01.setCodeApplication("43");
        annonce43_01.setCodeEnregistrement01("01");
        annonce43_01.setNumeroCaisse(CaisseHelperFactory.getInstance().getNoCaisse(session.getApplication()));
        annonce43_01.setNumeroAgence(CaisseHelperFactory.getInstance().getNoAgence(session.getApplication()));
        annonce43_01.setReferenceCaisseInterne("PONCT" + session.getUserId());
        annonce43_01.setEtatCivil(PRACORConst.csEtatCivilToAcorForRentes(ra.getCsEtatCivil()));
        if (JadeStringUtil.isEmpty(ra.getCodeRefugie())) {
            annonce43_01.setIsRefugie("0");
        } else {
            annonce43_01.setIsRefugie(ra.getCodeRefugie());
        }
        if (!JadeStringUtil.isBlankOrZero(csCantonDomicile) && (csCantonDomicile.length() > 3)) {
            annonce43_01.setCantonEtatDomicile(TILocalite.getCodeOFASCanton(csCantonDomicile));
        } else {
            annonce43_01.setCantonEtatDomicile(csCantonDomicile);
        }
        annonce43_01.setGenrePrestation(ra.getCodePrestation());
        annonce43_01.setMoisRapport(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session))));

        return annonce43_01;
    }

    private REAnnoncesAugmentationModification9Eme ajouterAnnoncePonctuelleNouvelNss43_02(final BSession session,
            final REAnnoncePonctuelleViewBean vb) throws Exception {

        REAnnoncesAugmentationModification9Eme annonce43_02 = new REAnnoncesAugmentationModification9Eme();
        annonce43_02.setSession(session);
        annonce43_02.setIdTiers(vb.getIdTiersBeneficiaire());
        annonce43_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce43_02.setCodeApplication("43");
        annonce43_02.setCodeEnregistrement01("02");

        return annonce43_02;
    }

    private REAnnoncesAugmentationModification9Eme ajouterAnnoncePonctuelleNouvelNss43_02(final BSession session,
            final RERenteAccordee ra) throws Exception {

        REAnnoncesAugmentationModification9Eme annonce43_02 = new REAnnoncesAugmentationModification9Eme();
        annonce43_02.setSession(session);
        annonce43_02.setIdTiers(ra.getIdTiersBeneficiaire());
        annonce43_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce43_02.setCodeApplication("43");
        annonce43_02.setCodeEnregistrement01("02");

        return annonce43_02;
    }

    private REAnnoncesAugmentationModification10Eme ajouterAnnoncePonctuelleNouvelNss46_01(final BSession session,
            final REAnnoncePonctuelleViewBean vb, final RERenteAccordee ra) throws Exception {

        REAnnoncesAugmentationModification10Eme annonce46_01 = new REAnnoncesAugmentationModification10Eme();
        annonce46_01.setSession(session);
        annonce46_01.setIdTiers(vb.getIdTiersBeneficiaire());
        annonce46_01.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce46_01.setCodeApplication("46");
        annonce46_01.setCodeEnregistrement01("01");
        annonce46_01.setNumeroCaisse(CaisseHelperFactory.getInstance().getNoCaisse(session.getApplication()));
        annonce46_01.setNumeroAgence(CaisseHelperFactory.getInstance().getNoAgence(session.getApplication()));
        annonce46_01.setReferenceCaisseInterne("PONCT" + session.getUserId());
        annonce46_01.setEtatCivil(vb.getCodeEtatCivil());
        if (JadeStringUtil.isEmpty(vb.getCodeRefugie())) {
            annonce46_01.setIsRefugie("0");
        } else {
            annonce46_01.setIsRefugie(vb.getCodeRefugie());
        }
        if (!JadeStringUtil.isBlankOrZero(vb.getCsCanton()) && (vb.getCsCanton().length() > 3)) {
            annonce46_01.setCantonEtatDomicile(TILocalite.getCodeOFASCanton(vb.getCsCanton()));
        } else {
            annonce46_01.setCantonEtatDomicile(vb.getCsCanton());
        }
        annonce46_01.setGenrePrestation(ra.getCodePrestation());
        annonce46_01.setMoisRapport(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session))));

        return annonce46_01;
    }

    private REAnnoncesAugmentationModification10Eme ajouterAnnoncePonctuelleNouvelNss46_01(final BSession session,
            final RERenteAccordee ra, final String csCantonDomicile) throws Exception {

        REAnnoncesAugmentationModification10Eme annonce46_01 = new REAnnoncesAugmentationModification10Eme();
        annonce46_01.setSession(session);
        annonce46_01.setIdTiers(ra.getIdTiersBeneficiaire());
        annonce46_01.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce46_01.setCodeApplication("46");
        annonce46_01.setCodeEnregistrement01("01");
        annonce46_01.setNumeroCaisse(CaisseHelperFactory.getInstance().getNoCaisse(session.getApplication()));
        annonce46_01.setNumeroAgence(CaisseHelperFactory.getInstance().getNoAgence(session.getApplication()));
        annonce46_01.setReferenceCaisseInterne("PONCT" + session.getUserId());
        annonce46_01.setEtatCivil(PRACORConst.csEtatCivilToAcorForRentes(ra.getCsEtatCivil()));
        if (JadeStringUtil.isEmpty(ra.getCodeRefugie())) {
            annonce46_01.setIsRefugie("0");
        } else {
            annonce46_01.setIsRefugie(ra.getCodeRefugie());
        }
        if (!JadeStringUtil.isBlankOrZero(csCantonDomicile) && (csCantonDomicile.length() > 3)) {
            annonce46_01.setCantonEtatDomicile(TILocalite.getCodeOFASCanton(csCantonDomicile));
        } else {
            annonce46_01.setCantonEtatDomicile(csCantonDomicile);
        }
        annonce46_01.setGenrePrestation(ra.getCodePrestation());
        annonce46_01.setMoisRapport(PRDateFormater.convertDate_AAAAMM_to_MMAA(PRDateFormater
                .convertDate_MMxAAAA_to_AAAAMM(REPmtMensuel.getDateDernierPmt(session))));

        return annonce46_01;
    }

    private REAnnoncesAugmentationModification10Eme ajouterAnnoncePonctuelleNouvelNss46_02(final BSession session,
            final REAnnoncePonctuelleViewBean vb) throws Exception {

        REAnnoncesAugmentationModification10Eme annonce46_02 = new REAnnoncesAugmentationModification10Eme();
        annonce46_02.setSession(session);
        annonce46_02.setIdTiers(vb.getIdTiersBeneficiaire());
        annonce46_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce46_02.setCodeApplication("46");
        annonce46_02.setCodeEnregistrement01("02");

        return annonce46_02;
    }

    private REAnnoncesAugmentationModification10Eme ajouterAnnoncePonctuelleNouvelNss46_02(final BSession session,
            final RERenteAccordee ra) throws Exception {

        REAnnoncesAugmentationModification10Eme annonce46_02 = new REAnnoncesAugmentationModification10Eme();
        annonce46_02.setSession(session);
        annonce46_02.setIdTiers(ra.getIdTiersBeneficiaire());
        annonce46_02.setEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonce46_02.setCodeApplication("46");
        annonce46_02.setCodeEnregistrement01("02");

        return annonce46_02;
    }

    private List<RERenteAccordee> asRaEnCoursAnnoncePonctuelleAncienNss(final String idTiersBeneficiaire,
            final BSession session) throws Exception {

        List<RERenteAccordee> list = new ArrayList<RERenteAccordee>();

        RERenteAccordeeManager ram = new RERenteAccordeeManager();
        ram.setSession(session);
        ram.setForIdTiersBeneficiaire(idTiersBeneficiaire);
        ram.setForCsEtatIn(IREPrestationAccordee.CS_ETAT_VALIDE + ", " + IREPrestationAccordee.CS_ETAT_PARTIEL);
        ram.find();

        if (ram.size() != 0) {
            for (int j = 0; j < ram.size(); j++) {
                RERenteAccordee ra = (RERenteAccordee) ram.get(j);
                if (JadeStringUtil.isEmpty(ra.getDateFinDroit())) {
                    list.add(ra);
                } else {
                    String dernierPmtMensuel = REPmtMensuel.getDateDernierPmt(session);
                    String dateFinRa = ra.getDateFinDroit();

                    JACalendar c = new JACalendarGregorian();
                    if (c.compare(dateFinRa, dernierPmtMensuel) == JACalendar.COMPARE_FIRSTUPPER) {
                        list.add(ra);
                    }
                }
            }
        }
        return list;
    }

    private void createAnnonceRente(final BSession session, final BTransaction transaction,
            final String idAnnonceHeader, final String idRenteAccordee) throws Exception {
        REAnnonceRente annonceRente = new REAnnonceRente();
        annonceRente.setSession(session);
        annonceRente.setCsEtat(IREAnnonces.CS_ETAT_OUVERT);
        annonceRente.setCsTraitement(IREAnnonces.CS_CODE_EN_COURS);
        annonceRente.setIdAnnonceHeader(idAnnonceHeader);
        annonceRente.setIdRenteAccordee(idRenteAccordee);
        annonceRente.add(transaction);
    }

    /**
     * @see globaz.framework.controller.FWHelper#execute(globaz.framework.bean.FWViewBeanInterface,
     *      globaz.framework.controller.FWAction, globaz.globall.api.BISession)
     */
    @Override
    protected FWViewBeanInterface execute(final FWViewBeanInterface viewBean, final FWAction action,
            final BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    private void genererAnnoncesPonctuellesRentesLiees(final REAnnoncePonctuelleViewBean viewBean,
            final BSession session, final BTransaction transaction) throws Exception {

        RERenteAccordee renteAccordee = new RERenteAccordee();
        renteAccordee.setSession(session);
        renteAccordee.setId(viewBean.getIdRenteAccordee());
        renteAccordee.retrieve(transaction);

        if (!renteAccordee.isNew()
                && ("10".equals(renteAccordee.getCodePrestation()) || "13".equals(renteAccordee.getCodePrestation())
                        || "20".equals(renteAccordee.getCodePrestation())
                        || "23".equals(renteAccordee.getCodePrestation())
                        || "50".equals(renteAccordee.getCodePrestation()) || "70".equals(renteAccordee
                        .getCodePrestation()))) {
            List<RERenteLieeJointPrestationAccordee> complementaires = getRentesComplementairesEnCours(session,
                    viewBean.getIdRenteAccordee());

            for (RERenteLieeJointPrestationAccordee uneComplementaire : complementaires) {
                REAnnoncePonctuelleViewBean viewBeanPourComplementaire = new REAnnoncePonctuelleViewBean();
                viewBeanPourComplementaire.setIdRenteAccordee(uneComplementaire.getIdPrestationAccordee());
                viewBeanPourComplementaire.setISession(session);
                _retrieve(viewBeanPourComplementaire, null, session);
                viewBeanPourComplementaire.setCsCanton(viewBean.getCsCanton());
                this.ajouterAnnoncePonctuelle(viewBeanPourComplementaire, null, session, transaction);
            }
        }
    }

    private String getCsAtteinte(final BSession session, final String atteinte) {
        Map<String, String> codesCsCodeAtteinte = PRCodeSystem.getCUCSinMap(session,
                IREDemandeRente.CS_GROUPE_ATTEINTE_INVALIDITE);

        for (String key : codesCsCodeAtteinte.keySet()) {
            if (key.equals(atteinte)) {
                return codesCsCodeAtteinte.get(key);
            }
        }

        return "";
    }

    private String getCsInfirmite(final BSession session, final String cleInfirmite) {
        Map<String, String> codesCsCodeInfirmite = PRCodeSystem.getCUCSinMap(session,
                IREDemandeRente.CS_GROUPE_INFIRMITE);
        for (String key : codesCsCodeInfirmite.keySet()) {
            if (key.equals(cleInfirmite)) {
                return codesCsCodeInfirmite.get(key);
            }
        }
        return "";
    }

    private List<RERenteLieeJointPrestationAccordee> getRentesComplementairesEnCours(final BSession session,
            final String idRentePrincipale) throws Exception {

        if (JadeStringUtil.isBlank(idRentePrincipale)) {
            throw new Exception("ID de rente invalide");
        }

        List<RERenteLieeJointPrestationAccordee> rentesComplementaires = new ArrayList<RERenteLieeJointPrestationAccordee>();

        RERenteAccordee rentePrincipale = new RERenteAccordee();
        rentePrincipale.setSession(session);
        rentePrincipale.setId(idRentePrincipale);
        rentePrincipale.retrieve();

        RERenteLieeJointPrestationAccordeeManager manager = new RERenteLieeJointPrestationAccordeeManager();
        manager.setSession(session);
        manager.setForIdTiersLiant(rentePrincipale.getIdTiersBeneficiaire());
        manager.setForCsEtatIn(Arrays.asList(IREPrestationAccordee.CS_ETAT_VALIDE,
                IREPrestationAccordee.CS_ETAT_PARTIEL));
        manager.find();

        PRTiersWrapper tiersPrincipal = PRTiersHelper.getTiersParId(session, rentePrincipale.getIdTiersBeneficiaire());

        List<Integer> codesRentesComplementaires = null;
        if (ITIPersonne.CS_HOMME.equals(tiersPrincipal.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
            codesRentesComplementaires = RECodePrestationComplementaireUtil.getComplementairePourHomme().get(
                    Integer.parseInt(rentePrincipale.getCodePrestation()));
        } else {
            codesRentesComplementaires = RECodePrestationComplementaireUtil.getComplementairePourFemme().get(
                    Integer.parseInt(rentePrincipale.getCodePrestation()));
        }

        if (codesRentesComplementaires == null) {
            throw new Exception("La rente doit être une rente principale pour trouver ses complémentaires");
        }

        for (int i = 0; i < manager.getSize(); i++) {
            RERenteLieeJointPrestationAccordee prestation = (RERenteLieeJointPrestationAccordee) manager.get(i);

            if (JadeStringUtil.isBlankOrZero(prestation.getDateFinDroit())
                    && codesRentesComplementaires.contains(Integer.parseInt(prestation.getCodePrestation()))) {
                rentesComplementaires.add(prestation);
            }
        }

        return rentesComplementaires;
    }

    private String idTiersMere(final String idTiersBeneficiaire, final BSession session) throws Exception {

        String idTiersMere = "";

        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersBeneficiaire);

        ISFMembreFamille[] cParent = sf.getParents(idTiersBeneficiaire);

        for (int j = 0; (cParent != null) && (j < cParent.length); j++) {
            ISFMembreFamille membreFamille = cParent[j];

            if (PRACORConst.CS_FEMME.equals(membreFamille.getCsSexe())) {
                idTiersMere = membreFamille.getIdTiers();
            }
        }
        return idTiersMere;
    }

    private String idTiersPere(final String idTiersBeneficiaire, final BSession session) throws Exception {

        String idTiersPere = "";
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersBeneficiaire);

        ISFMembreFamille[] cParent = sf.getParents(idTiersBeneficiaire);

        for (int j = 0; (cParent != null) && (j < cParent.length); j++) {
            ISFMembreFamille membreFamille = cParent[j];

            if (PRACORConst.CS_HOMME.equals(membreFamille.getCsSexe())) {
                idTiersPere = membreFamille.getIdTiers();
            }
        }
        return idTiersPere;
    }

    private String nssConjoint(final String idTiersBeneficiaire, final BSession session) throws Exception {

        RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager ram = new RERenteAccJoinTblTiersJoinDemRenteJoinAjourManager();
        ram.setSession(session);
        ram.setForIdTiersBeneficiaire(idTiersBeneficiaire);
        ram.find();

        String nss = "";

        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersBeneficiaire);

        if (ram.size() != 0) {

            RERenteAccJoinTblTiersJoinDemRenteJoinAjour ra = (RERenteAccJoinTblTiersJoinDemRenteJoinAjour) ram
                    .getFirstEntity();

            ISFMembreFamille x = sf.getMembreFamille(ra.getIdMembreFamille());
            if (ISFSituationFamiliale.CS_ETAT_CIVIL_MARIE.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_VEUF.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_DE_FAIT.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_SEPARE_JUDICIAIREMENT.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_ENREGISTRE.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_DE_FAIT.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_SEPARE_JUDICIAIREMENT.equals(x.getCsEtatCivil())
                    || ISFSituationFamiliale.CS_ETAT_CIVIL_PARTENARIAT_DISSOUS_DECES.equals(x.getCsEtatCivil())) {
                ISFRelationFamiliale[] relfam = sf.getRelationsConjoints(idTiersBeneficiaire, null);
                for (int j = 0; (relfam != null) && (j < relfam.length); j++) {
                    ISFRelationFamiliale rf = relfam[j];
                    if (PRACORConst.CS_FEMME.equals(ra.getSexe())) {
                        ISFMembreFamille MembreFamille = sf.getMembreFamille(rf.getIdMembreFamilleHomme());
                        nss = JadeStringUtil.change(MembreFamille.getNss(), ".", "");
                        if (nss.length() != 13) {
                            nss = "00000000000";
                        }
                    } else {
                        ISFMembreFamille MembreFamille = sf.getMembreFamille(rf.getIdMembreFamilleFemme());
                        nss = JadeStringUtil.change(MembreFamille.getNss(), ".", "");
                        if (nss.length() != 13) {
                            nss = "00000000000";
                        }
                    }
                    // BZ 9926
                    break;
                }
            }
        }
        return nss;
    }

    private String nssMere(final String idTiersBeneficiaire, final BSession session) throws Exception {

        String nssMere = "00000000000";

        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersBeneficiaire);

        ISFMembreFamille[] cParent = sf.getParents(idTiersBeneficiaire);

        for (int j = 0; (cParent != null) && (j < cParent.length); j++) {
            ISFMembreFamille membreFamille = cParent[j];

            if (PRACORConst.CS_FEMME.equals(membreFamille.getCsSexe())) {
                nssMere = JadeStringUtil.change(membreFamille.getNss(), ".", "");
            }
        }
        return nssMere;
    }

    private String nssPere(final String idTiersBeneficiaire, final BSession session) throws Exception {

        String nssPere = "00000000000";
        ISFSituationFamiliale sf = SFSituationFamilialeFactory.getSituationFamiliale(session,
                ISFSituationFamiliale.CS_DOMAINE_RENTES, idTiersBeneficiaire);

        ISFMembreFamille[] cParent = sf.getParents(idTiersBeneficiaire);

        for (int j = 0; (cParent != null) && (j < cParent.length); j++) {
            ISFMembreFamille membreFamille = cParent[j];

            if (PRACORConst.CS_HOMME.equals(membreFamille.getCsSexe())) {
                nssPere = JadeStringUtil.change(membreFamille.getNss(), ".", "");
            }
        }
        return nssPere;
    }

    private void updateWithTransaction(final REAnnoncePonctuelleViewBean viewBean, final BSession session,
            final BTransaction transaction) throws Exception {
        RERenteAccordee ra = new RERenteAccordee();
        ra.setSession(session);
        ra.setIdPrestationAccordee(viewBean.getIdRenteAccordee());
        ra.retrieve(transaction);

        REBasesCalcul bc = new REBasesCalcul();
        bc.setSession(session);
        bc.setIdBasesCalcul(ra.getIdBaseCalcul());
        bc.retrieve(transaction);

        ra.setCsEtatCivil(viewBean.getCsEtatCivil());
        ra.setIdTiersComplementaire1(viewBean.getIdTiersComplementaire1());
        ra.setIdTiersComplementaire2(viewBean.getIdTiersComplementaire2());
        ra.setCodeCasSpeciaux1(viewBean.getCs1());
        ra.setCodeCasSpeciaux2(viewBean.getCs2());
        ra.setCodeCasSpeciaux3(viewBean.getCs3());
        ra.setCodeCasSpeciaux4(viewBean.getCs4());
        ra.setCodeCasSpeciaux5(viewBean.getCs5());
        ra.setCodeRefugie(viewBean.getCodeRefugie());
        ra.setCsGenreDroitApi(viewBean.getCsGenreDroitAPI());
        ra.update(transaction);

        bc.setRevenuPrisEnCompte(viewBean.getRevenuPrisEnCompte9());
        bc.setRevenuAnnuelMoyen(viewBean.getRAM());
        bc.setDegreInvalidite(viewBean.getDegreInvalidite());
        bc.setCleInfirmiteAyantDroit(viewBean.getCleInfirmite());
        bc.setSurvenanceEvtAssAyantDroit(viewBean.getSurvenanceEvenementAssure());
        bc.setInvaliditePrecoce(viewBean.getIsInvaliditePrecoce());
        bc.setCodeOfficeAi(viewBean.getOfficeAI());

        if (ra.getCodePrestation().equals("81") || ra.getCodePrestation().equals("85")
                || ra.getCodePrestation().equals("84") || ra.getCodePrestation().equals("91")
                || ra.getCodePrestation().equals("94") || ra.getCodePrestation().equals("95")
                || ra.getCodePrestation().equals("82") || ra.getCodePrestation().equals("86")
                || ra.getCodePrestation().equals("88") || ra.getCodePrestation().equals("92")
                || ra.getCodePrestation().equals("96") || ra.getCodePrestation().equals("83")
                || ra.getCodePrestation().equals("87") || ra.getCodePrestation().equals("93")
                || ra.getCodePrestation().equals("89") || ra.getCodePrestation().equals("97")) {
            bc.setIsDemandeRenteAPI(true);
        }

        bc.update(transaction);

        REDemandeRente demande = new REDemandeRente();
        demande.setSession(session);
        demande.setIdRenteCalculee(bc.getIdRenteCalculee());
        demande.setAlternateKey(REDemandeRente.ALTERNATE_KEY_ID_RENTE_CALCULEE);
        demande.retrieve(transaction);

        // Mise à jour des autres demandes uniquement si la demande initiale est une API ou invalidité
        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(demande.getCsTypeDemandeRente())
                || IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(demande.getCsTypeDemandeRente())) {
            REDemandeRenteJointDemandeManager demrentemg = new REDemandeRenteJointDemandeManager();
            demrentemg.setForIdTiersRequ(bc.getIdTiersBaseCalcul());
            demrentemg.setSession(viewBean.getSession());
            demrentemg.find(transaction);

            for (int i = 0; i < demrentemg.size(); i++) {

                REDemandeRenteJointDemande red = (REDemandeRenteJointDemande) demrentemg.get(i);

                if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(red.getCsTypeDemande())
                        || IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(red.getCsTypeDemande())) {

                    boolean isRaEnCours = false;

                    RERenteAccJoinTblTiersJoinDemRenteManager ram = new RERenteAccJoinTblTiersJoinDemRenteManager();
                    ram.setSession(session);
                    ram.setForNoDemandeRente(red.getIdDemandeRente());
                    ram.find(transaction);

                    for (int j = 0; j < ram.size(); j++) {
                        RERenteAccJoinTblTiersJoinDemandeRente a = (RERenteAccJoinTblTiersJoinDemandeRente) ram.get(j);
                        if (JadeStringUtil.isEmpty(a.getDateFinDroit())) {
                            isRaEnCours = true;
                            break;
                        }
                    }

                    if (isRaEnCours) {

                        REBasesCalculManager bcm = new REBasesCalculManager();
                        String cleInfirmite = "";
                        String atteinte = "";

                        if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(red.getCsTypeDemande())) {
                            REDemandeRenteAPI demAPI = new REDemandeRenteAPI();
                            demAPI.setSession(session);
                            demAPI.setIdDemandeRente(red.getIdDemandeRente());
                            demAPI.retrieve(transaction);

                            if (viewBean.getCleInfirmite().length() == 5) {
                                cleInfirmite = JadeStringUtil.substring(viewBean.getCleInfirmite(), 0, 3);
                                atteinte = JadeStringUtil.substring(viewBean.getCleInfirmite(), 3, 2);
                            } else {
                                cleInfirmite = "";
                                atteinte = "";
                            }

                            // IL FAUT RECUPERER LE CODE SYSTEME !
                            demAPI.setCsInfirmite(getCsInfirmite(session, cleInfirmite));
                            demAPI.setCsAtteinte(getCsAtteinte(session, atteinte));

                            demAPI.setCodeOfficeAI(viewBean.getOfficeAI());
                            demAPI.setDateSuvenanceEvenementAssure(viewBean.getSurvenanceEvenementAssure());

                            demAPI.update(transaction);

                            bcm.setSession(session);
                            bcm.setForIdRenteCalculee(red.getIdRenteCalculee());
                            bcm.find(transaction);

                            if (!bcm.isEmpty()) {
                                REBasesCalcul bcD = (REBasesCalcul) bcm.getFirstEntity();
                                bcD.setRevenuAnnuelMoyen(viewBean.getRAM());
                                bcD.setDegreInvalidite(viewBean.getDegreInvalidite());
                                bcD.setCleInfirmiteAyantDroit(viewBean.getCleInfirmite());
                                bcD.setSurvenanceEvtAssAyantDroit(viewBean.getSurvenanceEvenementAssure());
                                bcD.setInvaliditePrecoce(viewBean.getIsInvaliditePrecoce());
                                bcD.setCodeOfficeAi(viewBean.getOfficeAI());
                                bcD.setIsDemandeRenteAPI(true);
                                bcD.update(transaction);
                            }

                        } else if (JadeNumericUtil.isInteger(viewBean.getGenrePrestation())
                                && !CodePrestation.getCodePrestation(Integer.parseInt(viewBean.getGenrePrestation()))
                                        .isAPI()) {
                            REDemandeRenteInvalidite demINV = new REDemandeRenteInvalidite();
                            demINV.setSession(session);
                            demINV.setIdDemandeRente(red.getIdDemandeRente());
                            demINV.retrieve(transaction);

                            if (viewBean.getCleInfirmite().length() == 5) {
                                cleInfirmite = JadeStringUtil.substring(viewBean.getCleInfirmite(), 0, 3);
                                atteinte = JadeStringUtil.substring(viewBean.getCleInfirmite(), 3, 2);
                            } else {
                                cleInfirmite = "";
                                atteinte = "";
                            }

                            // IL FAUT RECUPERER LE CODE SYSTEME !
                            demINV.setCsInfirmite(getCsInfirmite(session, cleInfirmite));
                            demINV.setCsAtteinte(getCsAtteinte(session, atteinte));

                            demINV.setDateSuvenanceEvenementAssure(viewBean.getSurvenanceEvenementAssure());
                            demINV.setCodeOfficeAI(viewBean.getOfficeAI());

                            for (REPeriodeInvalidite pInv : demINV.getPeriodesInvalidite()) {
                                if (JadeStringUtil.isEmpty(pInv.getDateFinInvalidite())) {
                                    pInv.setDegreInvalidite(viewBean.getDegreInvalidite());
                                    pInv.update(transaction);
                                }
                            }
                            demINV.update(transaction);

                            bcm.setSession(session);
                            bcm.setForIdRenteCalculee(red.getIdRenteCalculee());
                            bcm.find(transaction);

                            if (!bcm.isEmpty()) {
                                REBasesCalcul bcD = (REBasesCalcul) bcm.getFirstEntity();
                                bcD.setRevenuAnnuelMoyen(viewBean.getRAM());
                                bcD.setDegreInvalidite(viewBean.getDegreInvalidite());
                                bcD.setCleInfirmiteAyantDroit(viewBean.getCleInfirmite());
                                bcD.setSurvenanceEvtAssAyantDroit(viewBean.getSurvenanceEvenementAssure());
                                bcD.setInvaliditePrecoce(viewBean.getIsInvaliditePrecoce());
                                bcD.setCodeOfficeAi(viewBean.getOfficeAI());
                                bcD.update(transaction);
                            }
                        }
                    }
                }
            }
        }
    }
}
