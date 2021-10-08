/*
 * Créé le 26 mai 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.helpers.droits;

import globaz.apg.db.droits.APSitProJointEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.vb.droits.APSituationProfessionnelleViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRSituationProfessionnelle;
import globaz.prestation.helpers.PRAbstractHelper;
import globaz.prestation.interfaces.af.IPRAffilie;
import globaz.prestation.interfaces.af.PRAffiliationHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.tools.PRCalcul;
import globaz.pyxis.db.adressepaiement.TIAdressePaiementData;

import java.math.BigDecimal;
import java.util.List;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APSituationProfessionnelleHelper extends PRAbstractHelper {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Donne le montant journalier arrondi au franc supp. correspondant a la situation professionelle
     * 
     * @param repartition
     * @return
     */
    static public FWCurrency getSalaireJournalierVerse(APSituationProfessionnelle sitPro) {

        FWCurrency montantJournalierVerse = null;

        // pour un independant
        if (sitPro.getIsIndependant().booleanValue()) {
            BigDecimal mv = new BigDecimal(sitPro.getRevenuIndependant());
            mv = mv.divide(new BigDecimal("360"), 10, BigDecimal.ROUND_DOWN);
            montantJournalierVerse = new FWCurrency(mv.toString());

        } else {
            // pour les employes

            // si le salaire verse n'est pas un pourcentage mais un montant fixe
            if ((Float.parseFloat(sitPro.getMontantVerse()) > 0) && !sitPro.getIsPourcentMontantVerse().booleanValue()) {

                BigDecimal revenuIntermediaire = null;
                BigDecimal mv = new BigDecimal(sitPro.getMontantVerse());

                // si périodicité horaire
                if (sitPro.getPeriodiciteMontantVerse().equals(IPRSituationProfessionnelle.CS_PERIODICITE_HEURE)) {
                    BigDecimal nbHeuresSemaine = new BigDecimal(sitPro.getHeuresSemaine());
                    revenuIntermediaire = mv.multiply(nbHeuresSemaine);
                    revenuIntermediaire = revenuIntermediaire.divide(new BigDecimal("7"), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité mois
                else if (sitPro.getPeriodiciteMontantVerse().equals(IPRSituationProfessionnelle.CS_PERIODICITE_MOIS)) {
                    revenuIntermediaire = mv.divide(new BigDecimal("30"), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité 4 semaines
                else if (sitPro.getPeriodiciteMontantVerse().equals(
                        IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES)) {
                    revenuIntermediaire = mv.divide(new BigDecimal("28"), 10, BigDecimal.ROUND_DOWN);
                }

                // si périodicité année
                else if (sitPro.getPeriodiciteMontantVerse().equals(IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE)) {
                    revenuIntermediaire = mv.divide(new BigDecimal("360"), 10, BigDecimal.ROUND_DOWN);
                }

                montantJournalierVerse = new FWCurrency(revenuIntermediaire.toString());

            } else {

                BigDecimal revenuIntermediaire = new BigDecimal("0");
                BigDecimal montant = null;

                // si un salaire horaire
                if (Float.parseFloat(sitPro.getSalaireHoraire()) > 0) {

                    BigDecimal nbHeuresSemaine = new BigDecimal(sitPro.getHeuresSemaine());
                    montant = new BigDecimal(sitPro.getSalaireHoraire());
                    montant = montant.multiply(nbHeuresSemaine);
                    revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("7"), 10,
                            BigDecimal.ROUND_DOWN));
                }

                // si un salaire mensuel
                if (Float.parseFloat(sitPro.getSalaireMensuel()) > 0) {
                    montant = new BigDecimal(sitPro.getSalaireMensuel());
                    revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("30"), 10,
                            BigDecimal.ROUND_DOWN));
                }

                // si autre salaire
                if (Float.parseFloat(sitPro.getAutreSalaire()) > 0) {

                    montant = new BigDecimal(sitPro.getAutreSalaire());

                    // si périodicité 4 semaines
                    if (sitPro.getPeriodiciteAutreSalaire().equals(
                            IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES)) {
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("28"), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité année
                    else {
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("360"), 10,
                                BigDecimal.ROUND_DOWN));
                    }
                }

                // si autre remuneration
                if (Float.parseFloat(sitPro.getAutreRemuneration()) > 0) {

                    // si pourcentage le montant considerer est le montant du
                    // revenu principal
                    if (sitPro.getIsPourcentAutreRemun().booleanValue()) {
                        montant = new BigDecimal(revenuIntermediaire.toString());

                        revenuIntermediaire = revenuIntermediaire.add(new BigDecimal(JANumberFormatter.format(
                                PRCalcul.pourcentage100(montant.toString(), sitPro.getAutreRemuneration()), 0.05, 2,
                                JANumberFormatter.NEAR)));
                    } else {
                        montant = new BigDecimal(sitPro.getAutreRemuneration());

                        // si périodicité horaire
                        if (sitPro.getPeriodiciteAutreRemun().equals(IPRSituationProfessionnelle.CS_PERIODICITE_HEURE)) {
                            BigDecimal nbHeuresSemaine = new BigDecimal(sitPro.getHeuresSemaine());
                            montant = montant.multiply(nbHeuresSemaine);
                            revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("7"), 10,
                                    BigDecimal.ROUND_DOWN));
                        }

                        // si périodicité mois
                        else if (sitPro.getPeriodiciteAutreRemun().equals(
                                IPRSituationProfessionnelle.CS_PERIODICITE_MOIS)) {
                            revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("30"), 10,
                                    BigDecimal.ROUND_DOWN));
                        }

                        // si périodicité 4 semaines
                        else if (sitPro.getPeriodiciteAutreRemun().equals(
                                IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES)) {
                            revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("28"), 10,
                                    BigDecimal.ROUND_DOWN));
                        }

                        // si périodicité année
                        else if (sitPro.getPeriodiciteAutreRemun().equals(
                                IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE)) {
                            revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("360"), 10,
                                    BigDecimal.ROUND_DOWN));
                        }
                    }
                }

                // si un salaire nature
                if (Float.parseFloat(sitPro.getSalaireNature()) > 0) {

                    montant = new BigDecimal(sitPro.getSalaireNature());

                    // si périodicité horaire
                    if (sitPro.getPeriodiciteSalaireNature().equals(IPRSituationProfessionnelle.CS_PERIODICITE_HEURE)) {
                        BigDecimal nbHeuresSemaine = new BigDecimal(sitPro.getHeuresSemaine());
                        montant = montant.multiply(nbHeuresSemaine);
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("7"), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité mois
                    else if (sitPro.getPeriodiciteSalaireNature().equals(
                            IPRSituationProfessionnelle.CS_PERIODICITE_MOIS)) {
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("30"), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité 4 semaines
                    else if (sitPro.getPeriodiciteSalaireNature().equals(
                            IPRSituationProfessionnelle.CS_PERIODICITE_4_SEMAINES)) {
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("28"), 10,
                                BigDecimal.ROUND_DOWN));
                    }

                    // si périodicité année
                    else if (sitPro.getPeriodiciteSalaireNature().equals(
                            IPRSituationProfessionnelle.CS_PERIODICITE_ANNEE)) {
                        revenuIntermediaire = revenuIntermediaire.add(montant.divide(new BigDecimal("360"), 10,
                                BigDecimal.ROUND_DOWN));
                    }
                }

                // l'employeur ne verse qu'un pourcentage du salaire
                if ((Float.parseFloat(sitPro.getMontantVerse()) > 0)
                        && sitPro.getIsPourcentMontantVerse().booleanValue()) {

                    // multiplie par le % du salaire verse
                    montantJournalierVerse = new FWCurrency(JANumberFormatter.format(
                            PRCalcul.pourcentage100(revenuIntermediaire.toString(), sitPro.getMontantVerse()), 0.05, 2,
                            JANumberFormatter.NEAR));
                } else {
                    montantJournalierVerse = new FWCurrency(revenuIntermediaire.toString());
                }
            }
        }

        return montantJournalierVerse;
    }

    /**
     * vrais si une des situation prof. du droit n'a pas de revenu
     * 
     * @param idDroit
     * @return
     */
    static public boolean hasSituationProfWithoutIncome(String idDroit, BSession session) {
        int nb = 0;
        if (!JadeStringUtil.isBlankOrZero(idDroit)) {
            APSituationProfessionnelleManager manager = new APSituationProfessionnelleManager();
            manager.setSession(session);
            manager.setForIdDroit(idDroit);
            manager.setForHasNoRevenu(Boolean.TRUE);

            try {
                nb = manager.getCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nb != 0;
    }

    /**
     * @see #actionPreparerChercher(FWViewBeanInterface, FWAction, BSession)
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _chercher(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        actionPreparerChercher(viewBean, action, (BSession) session);
    }

    /**
     * Charger la situation pro et rechercher les affiliations pour le tiers employeur courant.
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
        super._retrieve(viewBean, action, session);
        chargerInfosAffiliations((BSession) session, (APSituationProfessionnelleViewBean) viewBean);
    }

    /**
     * prepare un viewBean pour l'affichage d'informations dans la page rc de la ca page.
     * <p>
     * Charge le droit et les prestations du droit. Positionne les champs du list viewBean en fonction, obtient une
     * adresse de paiement valide.
     * </p>
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public FWViewBeanInterface actionPreparerChercher(FWViewBeanInterface viewBean, FWAction action, BSession session)
            throws Exception {
        chargerInfosAffiliations(session, (APSituationProfessionnelleViewBean) viewBean);

        return viewBean;
    }

    /**
     * recherche les affiliations pour le tiers courant.
     * 
     * @param session
     * @param spViewBean
     * @throws Exception
     */
    private void chargerInfosAffiliations(BSession session, APSituationProfessionnelleViewBean spViewBean)
            throws Exception {
        // dresser la liste des affiliations pour ce tiers
        if (!JadeStringUtil.isIntegerEmpty(spViewBean.getIdTiersEmployeur())) {
            // spViewBean.setAffiliationsEmployeur(PRAffiliationHelper.getAffiliationsTiers(session,
            // spViewBean.getIdTiersEmployeur()));

            // Inforom531
            spViewBean.setAffiliationsEmployeur(PRAffiliationHelper.getAffiliationsTiersExt(session,
                    spViewBean.getIdTiersEmployeur()));
        }

        // retrouver le id affilie en fonction du numero affilie si retour
        // depuis pyxis
        if (JadeStringUtil.isIntegerEmpty(spViewBean.getIdAffilieEmployeur())
                && !JadeStringUtil.isEmpty(spViewBean.getNumAffilieEmployeur())) {
            IPRAffilie affilie = PRAffiliationHelper.getEmployeurParNumAffilie(session,
                    spViewBean.getNumAffilieEmployeur(), spViewBean.getDateDebutDroit());

            if (affilie != null) {
                spViewBean.setIdAffilieEmployeur(affilie.getIdAffilie());
            }
        }

        // renseigner le numero d'affilie par defaut pour le cas ou un id
        // affilie a deja ete choisi.
        if (!JadeStringUtil.isIntegerEmpty(spViewBean.getIdAffilieEmployeur())
                && (spViewBean.getAffiliationsEmployeur() != null)) {
            for (int idAffiliation = 0; idAffiliation < spViewBean.getAffiliationsEmployeur().size(); ++idAffiliation) {
                String[] affiliation = (String[]) spViewBean.getAffiliationsEmployeur().get(idAffiliation);

                if (spViewBean.getIdAffilieEmployeur().equals(affiliation[0])) {
                    spViewBean.setNumAffilieEmployeur(affiliation[1]);
                }
            }
        }
    }

    /**
     * retrouver par introspection le nom de la methode de cette class qu'il faut executer.017.376.010001
     * 
     * @param viewBean
     *            DOCUMENT ME!
     * @param action
     *            DOCUMENT ME!
     * @param session
     *            DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {
        return deleguerExecute(viewBean, action, session);
    }

    public void rechercherAffilie(FWViewBeanInterface viewBean, FWAction action, BSession session) throws Exception {

        APSituationProfessionnelleViewBean spviewBean = (APSituationProfessionnelleViewBean) viewBean;

        IPRAffilie affilie;
        affilie = PRAffiliationHelper.getEmployeurParNumAffilie(session, spviewBean.getNumAffilieEmployeur(),
                spviewBean.getDateDebutDroit(), false);

        if (affilie == null) {
            spviewBean.setNomEmployeur("");
            spviewBean.setIdAffilieEmployeur("");
            spviewBean.setIdTiersEmployeur("");
            spviewBean.setNumAffilieEmployeur("");
            spviewBean.setDateDebut("");
            spviewBean.setDateFin("");

            spviewBean.setMessage(session.getLabel("AFFILIE_NON_TROUVE"));
            spviewBean.setMsgType(FWViewBeanInterface.ERROR);
        }

        else {
            spviewBean.setIdTiersEmployeur(affilie.getIdTiers());
            spviewBean.setIdAffilieEmployeur(affilie.getIdAffilie());
            spviewBean.setNomEmployeur(affilie.getNom());
            spviewBean.setNumAffilieEmployeur(affilie.getNumAffilie());
            spviewBean.setDateDebut(affilie.getDateDebut());
            spviewBean.setDateFin(affilie.getDateFin());
        }
    }

    /**
     * recherche le canton dans les situations professionnelles
     * @param domaine
     * @param situationsProf
     * @return
     * @throws Exception
     */
    public String rechercheCantonAdressePaiementSitProf(BSession session, String domaine, List<APSitProJointEmployeur> situationsProf, String dateDebut) throws Exception {
        String canton = "";
        // vérification du canton de la situation professionnelle
        for (APSitProJointEmployeur sit : situationsProf) {
            TIAdressePaiementData data = PRTiersHelper.getAdressePaiementData(session, session.getCurrentThreadTransaction(), sit.getIdTiers(),
                    domaine, sit.getIdAffilie(), dateDebut);

            if (!data.isNew()) {
                String cantonComparaison = PRTiersHelper.getCanton(session, data.getNpa());
                if(cantonComparaison == null) {
                    // canton de l'adresse de paiement de la banque (indépendant étranger ?)
                    cantonComparaison = PRTiersHelper.getCanton(session, data.getNpa_banque());
                }
                // toutes les situations professionnelles du droit doivent avoir le même canton sinon impossible de déterminer
                if (!canton.isEmpty() && !canton.equals(cantonComparaison)) {
                    throw new Exception("impossible de déterminer le canton d'imposition : plusieurs cantons différents pour plusieurs employeurs : ");
                } else {
                    canton = cantonComparaison;
                }
            }

        }
        return canton;
    }

}
