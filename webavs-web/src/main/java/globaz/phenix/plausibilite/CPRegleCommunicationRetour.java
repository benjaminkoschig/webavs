/*
 * Créé le 2 mars 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.plausibilite;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.FWFindParameter;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.affiliation.AFAffiliationUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CARubrique;
import globaz.pavo.db.compte.CICompteIndividuelUtil;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPCommentaireCommunication;
import globaz.phenix.db.communications.CPCommentaireCommunicationManager;
import globaz.phenix.db.communications.CPCommunicationFiscale;
import globaz.phenix.db.communications.CPCommunicationFiscaleManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourGEViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourSEDEXViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDManager;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVDViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourVSViewBean;
import globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean;
import globaz.phenix.db.divers.CPPeriodeFiscale;
import globaz.phenix.db.divers.CPTableIndependant;
import globaz.phenix.db.principale.CPCotisation;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionManager;
import globaz.phenix.db.principale.CPDonneesBase;
import globaz.phenix.process.communications.plausibiliteImpl.CPGenericReglePlausibilite;
import globaz.phenix.toolbox.CPToolBox;
import globaz.phenix.util.CPUtil;
import globaz.phenix.util.WIRRDataBean;
import globaz.phenix.util.WIRRServiceCallUtil;
import globaz.pyxis.adresse.datasource.TIAbstractAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.application.TIApplication;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TICompositionTiers;
import globaz.pyxis.db.tiers.TICompositionTiersManager;
import globaz.pyxis.db.tiers.TIHistoriqueAvs;
import globaz.pyxis.db.tiers.TIHistoriqueAvsManager;
import globaz.pyxis.db.tiers.TIHistoriqueContribuable;
import globaz.pyxis.db.tiers.TIHistoriqueContribuableManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import ch.globaz.hera.business.services.HeraServiceLocator;
import ch.globaz.orion.business.domaine.demandeacompte.DemandeModifAcompteStatut;
import ch.globaz.orion.db.EBDemandeModifAcompteEntity;

/**
 * @author hna Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPRegleCommunicationRetour extends CPGenericReglePlausibilite {
    /**
     * Détermination du genre de l'affilié selon les données fiscales
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public void ccvsDeterminationGenre() throws Exception {
        getCommunicationRetour().setGenreAffilie("");
        if ((getCommunicationRetour().getAffiliation() != null) && !getCommunicationRetour().getAffiliation().isNew()) {
            if (CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(getCommunicationRetour().getAffiliation()
                    .getTypeAffiliation())) {
                getCommunicationRetour().setGenreAffilie(CPDecision.CS_TSE);
            } else {
                // détermination si il y a uniquement l'AFI
                AFCotisationManager cotiMan = new AFCotisationManager();
                cotiMan.setSession(getSession());
                cotiMan.setForAffiliationId(getCommunicationRetour().getAffiliation().getAffiliationId());
                cotiMan.setForAnneeActive(getCommunicationRetour().getAnnee1());
                cotiMan.find();
                if (cotiMan.getSize() == 1) {
                    AFCotisation coti = (AFCotisation) cotiMan.getFirstEntity();
                    if (coti.getAssurance().getAssuranceGenre().equals(CodeSystem.TYPE_ASS_AFI)) {
                        // uniquement AFI => ok pour calcul
                        getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                    }
                }
            }
            // Vérification condition indépendant -> voir document dans classeur CCVS
            // Calcul du RA + RNA net de l'affilié
            // Soit (RA + RNA) - (fortune expl. arrondie au 1000 sup * taux d'intérêt du capital)
            CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
            // Revenu contribuable
            float revenuAgricole = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuAgricoleCtb()));
            float revenuNonAgricole = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuNonAgricoleCtb()));

            if (!JadeStringUtil.isBlankOrZero(Float.toString(revenuAgricole))
                    || !JadeStringUtil.isBlankOrZero(Float.toString(revenuNonAgricole))) {
                // Si date > âge de retraite et revenu => mettre indépendant
                int anneeCom = Integer.parseInt(getCommunicationRetour().getAnnee1());
                int anneeAvs = JACalendar.getYear((getCommunicationRetour().getTiers().getDateAvs()));
                if (anneeCom > anneeAvs) {
                    getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                }
                boolean salarieProf = false;
                // Si salarié de profession => SI hors canton
                TIAdresseDataSource adresse = getCommunicationRetour().getTiers().getAdresseAsDataSource(
                        IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "31.12." + getCommunicationRetour().getAnnee1(), true);
                String codeCanton = "";
                if (adresse != null) {
                    Hashtable<?, ?> data = adresse.getData();
                    codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
                    if ("050".equalsIgnoreCase(codeCanton)) {
                        codeCanton = IConstantes.CS_LOCALITE_CANTON_JURA;
                        // Si adresse étrangère (code canton=30) => recherche par l'adresse de courrier
                    } else {
                        codeCanton = "505" + codeCanton;
                    }
                }
                if (JadeStringUtil.isEmpty(codeCanton)) {
                    codeCanton = getCommunicationRetour().getTiers().getCanton();
                }
                if (!IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(codeCanton)) {
                    salarieProf = true;
                } else {
                    float salaire = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsSalairesContribuable()));
                    if (salaire > 12000) {
                        if (salaire >= 24000) {
                            salarieProf = true;
                        } else if (salaire > (6 * (revenuAgricole + revenuNonAgricole))) {
                            salarieProf = true;
                        }
                    }
                }
                if (salarieProf) {
                    getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                }
                // calcul de l'intérêt
                float interet = 0;
                float capital = Float
                        .parseFloat(JANumberFormatter.deQuote(comm.getVsCapitalPropreEngageEntrepriseCtb()));
                float fortune = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsFortunePriveeCtb()));
                float rente = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuRenteCtb()));
                // Revenu conjoint
                float capitalCjt = Float.parseFloat(JANumberFormatter.deQuote(comm
                        .getVsCapitalPropreEngageEntrepriseConjoint()));
                float renteCjt = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuRenteConjoint()));
                float varMontant = 0;
                // Recherche montant activité accessoire
                float revenuActviteAccessoireArrondi = Float.parseFloat(FWFindParameter.findParameter(getSession()
                        .getCurrentThreadTransaction(), "10500130", "REVACTACC", comm.getDebutExercice1(), "", 0));
                revenuActviteAccessoireArrondi = JANumberFormatter.round(revenuActviteAccessoireArrondi, 100, 0,
                        JANumberFormatter.SUP);
                // Recherche du taux dans la table des paramètres
                String tauxInteret = FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                        "10500020", "TAUXINTERE", comm.getDebutExercice1(), "", 2);

                if (!JadeStringUtil.isBlankOrZero(comm.getVsCapitalPropreEngageEntrepriseCtb())) {
                    capital = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsCapitalPropreEngageEntrepriseCtb()));
                    // Arrondir le capital au 1000 fr. supérieur
                    if (capital > 0) {
                        varMontant = JANumberFormatter.round(Float.parseFloat(Float.toString(capital)), 1000, 0,
                                JANumberFormatter.SUP);
                        // Calcul des intêrets
                        if (!JadeStringUtil.isBlank(tauxInteret)) {
                            interet = (varMontant * Float.parseFloat(tauxInteret)) / 100;
                        }
                    } else {
                        capital = 0;
                        interet = 0;
                    }
                } else {
                    capital = 0;
                    interet = 0;
                }
                if ((revenuAgricole + revenuNonAgricole - interet) > revenuActviteAccessoireArrondi) {
                    // Test état civil 1 = célib., 2=marié, 3=veuf , 4=divorcé , 5=séparé
                    if (comm.getVsEtatCivilAffilie().equalsIgnoreCase("1")
                            || comm.getVsEtatCivilAffilie().equalsIgnoreCase("3")
                            || comm.getVsEtatCivilAffilie().equalsIgnoreCase("4")) {
                        if ((rente + renteCjt + ((fortune + capital) / 20)) < ((revenuAgricole + revenuNonAgricole - interet) * 4)) {
                            getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                        }
                    } else {
                        if ((rente + renteCjt + ((fortune + capital + capitalCjt) / 10)) < ((revenuAgricole
                                + revenuNonAgricole - interet) * 4)) {
                            getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                        }
                    }
                } else if (!salarieProf) {
                    if ((((rente + renteCjt) * 20) + ((fortune + capital + capitalCjt) / 10)) < 500000) {
                        getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                    }
                }
            }
            if (JadeStringUtil.isBlankOrZero(getCommunicationRetour().getGenreAffilie())) {
                // if
                // (CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getAffiliation().getTypeAffiliation()))
                // {
                getCommunicationRetour().setGenreAffilie(CPDecision.CS_NON_ACTIF);
                // }
                // Aucun genre de défini => erreur
                // ajouterErreur(idParam);
                // return niveauMsg;
                // } else {
                // return "";
            }
            // } else {
            // Si affiliation = null => message déjà existant par isCCVSCondition()
            // return "";
        }
    }

    protected void ccvsDeterminationGenreConjoint() throws Exception {
        getCommunicationRetour().setGenreConjoint("");
        if ((getCommunicationRetour().getAffiliationConjoint() != null)
                && !getCommunicationRetour().getAffiliationConjoint().isNew()) {
            if (CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(getCommunicationRetour().getAffiliation()
                    .getTypeAffiliation())) {
                getCommunicationRetour().setGenreConjoint(CPDecision.CS_TSE);
            } else {
                // détermination si il y a uniquement l'AFI
                AFCotisationManager cotiMan = new AFCotisationManager();
                cotiMan.setSession(getSession());
                cotiMan.setForAffiliationId(getCommunicationRetour().getIdAffiliationConjoint());
                cotiMan.setForAnneeActive(getCommunicationRetour().getAnnee1());
                cotiMan.find();
                if (cotiMan.getSize() == 1) {
                    AFCotisation coti = (AFCotisation) cotiMan.getFirstEntity();
                    if (coti.getAssurance().getAssuranceGenre().equals(CodeSystem.TYPE_ASS_AFI)) {
                        // uniquement AFI => ok pour calcul
                        getCommunicationRetour().setGenreConjoint(CPDecision.CS_INDEPENDANT);
                    }
                }
            }
            // Vérification condition indépendant -> voir document dans classeur CCVS
            // Calcul du RA + RNA net de l'affilié
            // Soit (RA + RNA) - (fortune expl. arrondie au 1000 sup * taux d'intérêt du capital)
            CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
            // Revenu conjoint
            float revenuAgricoleCjt = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuAgricoleConjoint()));
            float revenuNonAgricoleCjt = Float.parseFloat(JANumberFormatter.deQuote(comm
                    .getVsRevenuNonAgricoleConjoint()));

            if (!JadeStringUtil.isBlankOrZero(Float.toString(revenuAgricoleCjt))
                    || !JadeStringUtil.isBlankOrZero(Float.toString(revenuNonAgricoleCjt))) {
                // Si date > âge de retraite et revenu => mettre indépendant
                int anneeCom = Integer.parseInt(getCommunicationRetour().getAnnee1());
                TITiersViewBean cjt = new TITiersViewBean();
                cjt.setSession(getSession());
                cjt.setIdTiers(getCommunicationRetour().getIdConjoint());
                cjt.retrieve();
                int anneeAvs = JACalendar.getYear((cjt.getDateAvs()));
                if (anneeCom > anneeAvs) {
                    getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                }
                boolean salarieProfCjt = false;
                // Si salarié de profession => SI hors canton
                TIAdresseDataSource adresse = cjt.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                        "519005", "31.12." + getCommunicationRetour().getAnnee1(), true);
                String codeCanton = "";
                if (adresse != null) {
                    Hashtable<?, ?> data = adresse.getData();
                    codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
                    if ("050".equalsIgnoreCase(codeCanton)) {
                        codeCanton = IConstantes.CS_LOCALITE_CANTON_JURA;
                        // Si adresse étrangère (code canton=30) => recherche par l'adresse de courrier
                    } else {
                        codeCanton = "505" + codeCanton;
                    }
                }
                if (JadeStringUtil.isEmpty(codeCanton)) {
                    codeCanton = getCommunicationRetour().getTiers().getCanton();
                }
                if (!IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(codeCanton)) {
                    salarieProfCjt = true;
                } else {
                    float salaire = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsSalairesConjoint()));
                    if (salaire > 12000) {
                        if (salaire >= 24000) {
                            salarieProfCjt = true;
                        } else if (salaire > (6 * (revenuAgricoleCjt + revenuNonAgricoleCjt))) {
                            salarieProfCjt = true;
                        }
                    }
                }
                // Test si conjoint est salarié de profession
                if (salarieProfCjt) {
                    getCommunicationRetour().setGenreConjoint(CPDecision.CS_INDEPENDANT);
                }

                float interet = 0;
                // Revenu contribuable
                float capital = Float
                        .parseFloat(JANumberFormatter.deQuote(comm.getVsCapitalPropreEngageEntrepriseCtb()));
                float rente = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuRenteCtb()));

                float capitalCjt = Float.parseFloat(JANumberFormatter.deQuote(comm
                        .getVsCapitalPropreEngageEntrepriseConjoint()));
                float fortuneCjt = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsFortunePriveeConjoint()));
                float fortuneCtb = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsFortunePriveeCtb()));
                float renteCjt = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuRenteConjoint()));
                float varMontant = 0;
                // Recherche montant activité accessoire
                float revenuActviteAccessoireArrondi = Float.parseFloat(FWFindParameter.findParameter(getSession()
                        .getCurrentThreadTransaction(), "10500130", "REVACTACC", comm.getDebutExercice1(), "", 0));
                revenuActviteAccessoireArrondi = JANumberFormatter.round(revenuActviteAccessoireArrondi, 100, 0,
                        JANumberFormatter.SUP);
                // Recherche du taux dans la table des paramètres
                String tauxInteret = FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                        "10500020", "TAUXINTERE", comm.getDebutExercice1(), "", 2);

                if (!JadeStringUtil.isBlankOrZero(comm.getVsCapitalPropreEngageEntrepriseConjoint())) {
                    capitalCjt = Float.parseFloat(JANumberFormatter.deQuote(comm
                            .getVsCapitalPropreEngageEntrepriseConjoint()));
                    // Arrondir le capital au 1000 fr. supérieur
                    if (capitalCjt > 0) {
                        varMontant = JANumberFormatter.round(Float.parseFloat(Float.toString(capitalCjt)), 1000, 0,
                                JANumberFormatter.SUP);
                        // Calcul des intêrets
                        if (!JadeStringUtil.isBlank(tauxInteret)) {
                            interet = (varMontant * Float.parseFloat(tauxInteret)) / 100;
                        }
                    } else {
                        capital = 0;
                        interet = 0;
                    }
                } else {
                    capital = 0;
                    interet = 0;
                }
                if ((revenuAgricoleCjt + revenuNonAgricoleCjt - interet) > revenuActviteAccessoireArrondi) {
                    if ((renteCjt + rente + ((fortuneCjt + fortuneCtb + capital + capitalCjt) / 10)) < ((revenuAgricoleCjt
                            + revenuNonAgricoleCjt - interet) * 4)) {
                        getCommunicationRetour().setGenreConjoint(CPDecision.CS_INDEPENDANT);
                    }
                }
            }
            if (JadeStringUtil.isBlankOrZero(getCommunicationRetour().getGenreConjoint())) {
                // if
                // (CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getAffiliationConjoint().getTypeAffiliation()))
                // {
                getCommunicationRetour().setGenreConjoint(CPDecision.CS_NON_ACTIF);
                // }
                // } else {
                // return "";
            }
            // // Aucun genre de défini => erreur
            // ajouterErreur(idParam);
            // return niveauMsg;
            // } else {
            // Si affiliation = null => message déjà existant par isCCVSCondition()
            // return "";
        }
    }

    public void ccvsSedexDeterminationGenre() throws Exception {
        if ((getCommunicationRetour().getAffiliation() != null) && !getCommunicationRetour().getAffiliation().isNew()) {
            if (CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(getCommunicationRetour().getAffiliation()
                    .getTypeAffiliation())) {
                getCommunicationRetour().setGenreAffilie(CPDecision.CS_TSE);
            } else {
                // détermination si il y a uniquement l'AFI
                AFCotisationManager cotiMan = new AFCotisationManager();
                cotiMan.setSession(getSession());
                cotiMan.setForAffiliationId(getCommunicationRetour().getAffiliation().getAffiliationId());
                cotiMan.setForAnneeActive(getCommunicationRetour().getAnnee1());
                cotiMan.find();
                if (cotiMan.getSize() == 1) {
                    AFCotisation coti = (AFCotisation) cotiMan.getFirstEntity();
                    if (coti.getAssurance().getAssuranceGenre().equals(CodeSystem.TYPE_ASS_AFI)) {
                        // uniquement AFI => ok pour calcul
                        getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                    }
                }
            }
            // Vérification condition indépendant -> voir document dans classeur CCVS
            // Calcul du RA + RNA net de l'affilié
            // Soit (RA + RNA) - (fortune expl. arrondie au 1000 sup * taux d'intérêt du capital)
            // Revenu contribuable
            float revenuAgricole = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getMainIncomeInAgriculture()));
            float revenuAvs = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getIncomeFromSelfEmployment()));
            float revenuNonAgricole = revenuAgricole - revenuAvs;

            if (!JadeStringUtil.isBlankOrZero(Float.toString(revenuAgricole))
                    || !JadeStringUtil.isBlankOrZero(Float.toString(revenuNonAgricole))) {
                // Si date > âge de retraite et revenu => mettre indépendant
                int anneeCom = Integer.parseInt(getCommunicationRetour().getAnnee1());
                int anneeAvs = JACalendar.getYear((getCommunicationRetour().getTiers().getDateAvs()));
                if (anneeCom > anneeAvs) {
                    getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                }
                boolean salarieProf = false;
                // Si salarié de profession => SI hors canton
                TIAdresseDataSource adresse = getCommunicationRetour().getTiers().getAdresseAsDataSource(
                        IConstantes.CS_AVOIR_ADRESSE_DOMICILE, "519005",
                        "31.12." + getCommunicationRetour().getAnnee1(), true);
                String codeCanton = "";
                if (adresse != null) {
                    Hashtable<?, ?> data = adresse.getData();
                    codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
                    if ("050".equalsIgnoreCase(codeCanton)) {
                        codeCanton = IConstantes.CS_LOCALITE_CANTON_JURA;
                        // Si adresse étrangère (code canton=30) => recherche par l'adresse de courrier
                    } else {
                        codeCanton = "505" + codeCanton;
                    }
                }
                if (JadeStringUtil.isEmpty(codeCanton)) {
                    codeCanton = getCommunicationRetour().getTiers().getCanton();
                }
                if (!IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(codeCanton)) {
                    salarieProf = true;
                } else {
                    float salaire = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                            .getEmploymentIncome()));
                    if (salaire > 12000) {
                        if (salaire >= 24000) {
                            salarieProf = true;
                        } else if (salaire > (6 * (revenuAgricole + revenuNonAgricole))) {
                            salarieProf = true;
                        }
                    }
                }
                if (salarieProf) {
                    getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                }
                // calcul de l'intérêt
                float interet = 0;
                float capital = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases().getCapital()));
                float fortune = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases().getAssets()));
                float rente = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases().getPensionIncome()));
                // Revenu conjoint
                // float capitalCjt = Float.parseFloat(JANumberFormatter.deQuote(comm
                // .getVsCapitalPropreEngageEntrepriseConjoint()));
                // float renteCjt = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuRenteConjoint()));
                float capitalCjt = 0;
                float renteCjt = 0;
                float varMontant = 0;
                // Recherche montant activité accessoire
                float revenuActviteAccessoireArrondi = Float.parseFloat(FWFindParameter.findParameter(getSession()
                        .getCurrentThreadTransaction(), "10500130", "REVACTACC", getCommunicationRetour()
                        .getDebutExercice1(), "", 0));
                revenuActviteAccessoireArrondi = JANumberFormatter.round(revenuActviteAccessoireArrondi, 100, 0,
                        JANumberFormatter.SUP);
                // Recherche du taux dans la table des paramètres
                String tauxInteret = FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                        "10500020", "TAUXINTERE", getCommunicationRetour().getDebutExercice1(), "", 2);
                // Arrondir le capital au 1000 fr. supérieur
                if (capital > 0) {
                    varMontant = JANumberFormatter.round(Float.parseFloat(Float.toString(capital)), 1000, 0,
                            JANumberFormatter.SUP);
                    // Calcul des intêrets
                    if (!JadeStringUtil.isBlank(tauxInteret)) {
                        interet = (varMontant * Float.parseFloat(tauxInteret)) / 100;
                    }
                } else {
                    capital = 0;
                    interet = 0;
                }
                if ((revenuAgricole + revenuNonAgricole - interet) > revenuActviteAccessoireArrondi) {
                    // Test état civil 1 = célib., 2=marié, 3=veuf , 4=divorcé , 5=séparé
                    if (getSedexContribuable().getMaritalStatus().equalsIgnoreCase("1")
                            || getSedexContribuable().getMaritalStatus().equalsIgnoreCase("3")
                            || getSedexContribuable().getMaritalStatus().equalsIgnoreCase("4")) {
                        if ((rente + renteCjt + ((fortune + capital) / 20)) < ((revenuAgricole + revenuNonAgricole - interet) * 4)) {
                            getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                        }
                    } else {
                        if ((rente + renteCjt + ((fortune + capital + capitalCjt) / 10)) < ((revenuAgricole
                                + revenuNonAgricole - interet) * 4)) {
                            getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                        }
                    }
                } else if (!salarieProf) {
                    if ((((rente + renteCjt) * 20) + ((fortune + capital + capitalCjt) / 10)) < 500000) {
                        getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                    }
                }
            }
            if (JadeStringUtil.isBlankOrZero(getCommunicationRetour().getGenreAffilie())) {
                // if
                // (CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getAffiliation().getTypeAffiliation()))
                // {
                getCommunicationRetour().setGenreAffilie(CPDecision.CS_NON_ACTIF);
                // }
                // Aucun genre de défini => erreur
                // ajouterErreur(idParam);
                // return niveauMsg;
                // } else {
                // return "";
            }
            // } else {
            // Si affiliation = null => message déjà existant par isCCVSCondition()
            // return "";
        }
    }

    protected void ccvsSedexDeterminationGenreConjoint() throws Exception {
        getCommunicationRetour().setGenreConjoint("");
        if ((getCommunicationRetour().getAffiliationConjoint() != null)
                && !getCommunicationRetour().getAffiliationConjoint().isNew()) {
            if (CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(getCommunicationRetour().getAffiliation()
                    .getTypeAffiliation())) {
                getCommunicationRetour().setGenreConjoint(CPDecision.CS_TSE);
            } else {
                // détermination si il y a uniquement l'AFI
                AFCotisationManager cotiMan = new AFCotisationManager();
                cotiMan.setSession(getSession());
                cotiMan.setForAffiliationId(getCommunicationRetour().getIdAffiliationConjoint());
                cotiMan.setForAnneeActive(getCommunicationRetour().getAnnee1());
                cotiMan.find();
                if (cotiMan.getSize() == 1) {
                    AFCotisation coti = (AFCotisation) cotiMan.getFirstEntity();
                    if (coti.getAssurance().getAssuranceGenre().equals(CodeSystem.TYPE_ASS_AFI)) {
                        // uniquement AFI => ok pour calcul
                        getCommunicationRetour().setGenreConjoint(CPDecision.CS_INDEPENDANT);
                    }
                }
            }
            // Vérification condition indépendant -> voir document dans classeur CCVS
            // Calcul du RA + RNA net de l'affilié
            // Soit (RA + RNA) - (fortune expl. arrondie au 1000 sup * taux d'intérêt du capital)
            // Revenu conjoint
            float revenuAgricoleCjt = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getMainIncomeInAgricultureCjt()));
            float revenuAvsCjt = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getIncomeFromSelfEmploymentCjt()));
            float revenuNonAgricoleCjt = revenuAvsCjt - revenuAgricoleCjt;

            if (!JadeStringUtil.isBlankOrZero(Float.toString(revenuAgricoleCjt))
                    || !JadeStringUtil.isBlankOrZero(Float.toString(revenuNonAgricoleCjt))) {
                // Si date > âge de retraite et revenu => mettre indépendant
                int anneeCom = Integer.parseInt(getCommunicationRetour().getAnnee1());
                TITiersViewBean cjt = new TITiersViewBean();
                cjt.setSession(getSession());
                cjt.setIdTiers(getCommunicationRetour().getIdConjoint());
                cjt.retrieve();
                int anneeAvs = JACalendar.getYear((cjt.getDateAvs()));
                if (anneeCom > anneeAvs) {
                    getCommunicationRetour().setGenreAffilie(CPDecision.CS_INDEPENDANT);
                }
                boolean salarieProfCjt = false;
                // Si salarié de profession => SI hors canton
                TIAdresseDataSource adresse = cjt.getAdresseAsDataSource(IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                        "519005", "31.12." + getCommunicationRetour().getAnnee1(), true);
                String codeCanton = "";
                if (adresse != null) {
                    Hashtable<?, ?> data = adresse.getData();
                    codeCanton = (String) data.get(TIAbstractAdresseDataSource.ADRESSE_VAR_CANTON_CODE_OFAS);
                    if ("050".equalsIgnoreCase(codeCanton)) {
                        codeCanton = IConstantes.CS_LOCALITE_CANTON_JURA;
                        // Si adresse étrangère (code canton=30) => recherche par l'adresse de courrier
                    } else {
                        codeCanton = "505" + codeCanton;
                    }
                }
                if (JadeStringUtil.isEmpty(codeCanton)) {
                    codeCanton = getCommunicationRetour().getTiers().getCanton();
                }
                if (!IConstantes.CS_LOCALITE_CANTON_VALAIS.equalsIgnoreCase(codeCanton)) {
                    salarieProfCjt = true;
                } else {
                    float salaire = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                            .getEmploymentIncomeCjt()));
                    if (salaire > 12000) {
                        if (salaire >= 24000) {
                            salarieProfCjt = true;
                        } else if (salaire > (6 * (revenuAgricoleCjt + revenuNonAgricoleCjt))) {
                            salarieProfCjt = true;
                        }
                    }
                }
                // Test si conjoint est salarié de profession
                if (salarieProfCjt) {
                    getCommunicationRetour().setGenreConjoint(CPDecision.CS_INDEPENDANT);
                }

                float interet = 0;
                // Revenu contribuable
                float capital = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases().getCapital()));
                float rente = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases().getPensionIncome()));

                float capitalCjt = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases().getCapitalCjt()));
                float fortuneCjt = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases().getAssetsCjt()));
                float fortuneCtb = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases().getAssets()));
                float renteCjt = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                        .getPensionIncomeCjt()));
                float varMontant = 0;
                // Recherche montant activité accessoire
                String debutExerciceCjt = "";
                if (!JadeStringUtil.isEmpty(getSedexDonneesCommerciales().getCommencementOfSelfEmploymentCjt())
                        && !"0000-00-00".equalsIgnoreCase(getSedexDonneesCommerciales()
                                .getCommencementOfSelfEmploymentCjt())) {
                    debutExerciceCjt = CPToolBox.formatDate(getSedexDonneesCommerciales()
                            .getCommencementOfSelfEmploymentCjt(), 2);
                } else {
                    debutExerciceCjt = "31.12." + getCommunicationRetour().getAnnee1();
                }
                float revenuActviteAccessoireArrondi = Float.parseFloat(FWFindParameter.findParameter(getSession()
                        .getCurrentThreadTransaction(), "10500130", "REVACTACC", debutExerciceCjt, "", 0));
                revenuActviteAccessoireArrondi = JANumberFormatter.round(revenuActviteAccessoireArrondi, 100, 0,
                        JANumberFormatter.SUP);
                // Recherche du taux dans la table des paramètres
                String tauxInteret = FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                        "10500020", "TAUXINTERE", debutExerciceCjt, "", 2);

                // Arrondir le capital au 1000 fr. supérieur
                if (capitalCjt > 0) {
                    varMontant = JANumberFormatter.round(Float.parseFloat(Float.toString(capitalCjt)), 1000, 0,
                            JANumberFormatter.SUP);
                    // Calcul des intêrets
                    if (!JadeStringUtil.isBlank(tauxInteret)) {
                        interet = (varMontant * Float.parseFloat(tauxInteret)) / 100;
                    }
                } else {
                    capital = 0;
                    interet = 0;
                }
                if ((revenuAgricoleCjt + revenuNonAgricoleCjt - interet) > revenuActviteAccessoireArrondi) {
                    if ((renteCjt + rente + ((fortuneCjt + fortuneCtb + capital + capitalCjt) / 10)) < ((revenuAgricoleCjt
                            + revenuNonAgricoleCjt - interet) * 4)) {
                        getCommunicationRetour().setGenreConjoint(CPDecision.CS_INDEPENDANT);
                    }
                }
            }
            if (JadeStringUtil.isBlankOrZero(getCommunicationRetour().getGenreConjoint())) {
                // if
                // (CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getAffiliationConjoint().getTypeAffiliation()))
                // {
                getCommunicationRetour().setGenreConjoint(CPDecision.CS_NON_ACTIF);
                // }
                // } else {
                // return "";
            }
            // // Aucun genre de défini => erreur
            // ajouterErreur(idParam);
            // return niveauMsg;
            // } else {
            // Si affiliation = null => message déjà existant par isCCVSCondition()
            // return "";
        }
    }

    /**
     * Test si la base de taxation du retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleBaseTaxation(String niveauMsg, String idParam, String description) throws Exception {
        if ((getCommunicationRetour().getDecisionGeneree() != null)
                && (getCommunicationRetour().getDecisionDeBase() != null)) {
            if (getCommunicationRetour().getDecisionGeneree().getTypeDecision()
                    .equals(getCommunicationRetour().getDecisionDeBase().getTypeDecision())) {
                return "";
            } else {
                if ((globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                        getCommunicationRetour().getDecisionGeneree().getTypeDecision()).length() > 0)
                        || (globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                                getCommunicationRetour().getDecisionDeBase().getTypeDecision()).length() > 0)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Test si le début de taxation du fichier de retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleDebutTaxation(String niveauMsg, String idParam, String description) throws Exception {
        if ((getCommunicationRetour().getDecisionGeneree() != null)
                && (getCommunicationRetour().getDecisionDeBase() != null)) {
            if (getCommunicationRetour().getDecisionGeneree().getDebutDecision()
                    .equals(getCommunicationRetour().getDecisionDeBase().getDebutDecision())) {
                return "";
            } else {
                if ((getCommunicationRetour().getDecisionGeneree().getDebutDecision().length() > 0)
                        || (getCommunicationRetour().getDecisionDeBase().getDebutDecision().length() > 0)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
                return "";
            }
        } else {
            return "";
        }
    }

    /*
     * Détermination du genre d'affiliation pour le conjoint selon les données fiscales
     */

    /**
     * Test si l'état civil du retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleEtatCivil(String niveauMsg, String idParam, String description) throws Exception {
        if ((getTiers() != null) && (getTiersProvisoire() != null)) {
            if (getTiers().getEtatCivil().equals(getTiersProvisoire().getEtatCivil())) {
                return "";
            } else {
                if ((globaz.phenix.translation.CodeSystem.getLibelle(getSession(), getTiers().getEtatCivil()).length() > 0)
                        || (globaz.phenix.translation.CodeSystem.getLibelle(getSession(),
                                getTiersProvisoire().getEtatCivil()).length() > 0)) {
                    ajouterErreur(idParam);
                }
                return niveauMsg;
            }
        } else {
            return "";
        }
    }

    /**
     * Test si la fin de taxation du fichier de retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleFinTaxation(String niveauMsg, String idParam, String description) throws Exception {
        if ((getCommunicationRetour().getDecisionGeneree() != null)
                && (getCommunicationRetour().getDecisionDeBase() != null)) {
            if (getCommunicationRetour().getDecisionGeneree().getFinDecision()
                    .equals(getCommunicationRetour().getDecisionDeBase().getFinDecision())) {
                return "";
            } else {
                if ((getCommunicationRetour().getDecisionGeneree().getFinDecision().length() > 0)
                        || (getCommunicationRetour().getDecisionDeBase().getFinDecision().length() > 0)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Test si l'ifd fichier de retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleIFD(String niveauMsg, String idParam, String description) throws Exception {
        if ((getCommunicationRetour().getDecisionGeneree() != null)
                && (getCommunicationRetour().getDecisionDeBase() != null)) {
            if (getCommunicationRetour().getDecisionGeneree().getNumIfd()
                    .equals(getCommunicationRetour().getDecisionDeBase().getNumIfd())) {
                return "";
            } else {
                if ((getCommunicationRetour().getDecisionGeneree().getNumIfd().length() > 0)
                        || (getCommunicationRetour().getDecisionGeneree().getNumIfd().length() > 0)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Test si le code sexe du retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleNom(String niveauMsg, String idParam, String description) throws Exception {
        if ((getTiers() != null) && (getTiersProvisoire() != null)) {
            if (getTiers().getNom().equals(getTiersProvisoire().getNom())) {
                return "";
            } else {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        } else {
            return "";
        }
    }

    /**
     * Test si le numéro AVS du retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleNumAvs(String niveauMsg, String idParam, String description) throws Exception {
        if ((getTiers() != null) && (getTiersProvisoire() != null)) {
            if (getTiers().getNumAvsActuel().equals(getTiersProvisoire().getNumAvsActuel())) {
                return "";
            } else {
                if ((getTiers().getNumAvsActuel().length() > 0)
                        || (getTiersProvisoire().getNumAvsActuel().length() > 0)) {
                    ajouterErreur(idParam);
                }
                return niveauMsg;
            }
        } else {
            return "";
        }
    }

    /**
     * Test si le revenu agricole (revenu autre) du retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleRA(String niveauMsg, String idParam, String description) throws Exception {
        if ((getCommunicationRetour().getDecisionGeneree() != null)
                && (getCommunicationRetour().getDecisionDeBase() != null)) {
            float rna = 0;
            float rnaProvisoire = 0;
            CPDonneesBase donneesDeBase = new CPDonneesBase();
            donneesDeBase.setSession(getSession());
            donneesDeBase.setIdDecision(getCommunicationRetour().getDecisionGeneree().getIdDecision());
            donneesDeBase.retrieve();
            CPDonneesBase donneesDeBaseProvisoire = new CPDonneesBase();
            donneesDeBaseProvisoire.setSession(getSession());
            donneesDeBaseProvisoire.setIdDecision(getCommunicationRetour().getDecisionDeBase().getIdDecision());
            donneesDeBaseProvisoire.retrieve();
            if (donneesDeBase.getRevenuAutre1().length() > 0) {
                rna = Float.parseFloat(JANumberFormatter.deQuote(donneesDeBase.getRevenuAutre1()));
            }
            if (donneesDeBase.getRevenuAutre2().length() > 0) {
                rna += Float.parseFloat(JANumberFormatter.deQuote(donneesDeBase.getRevenuAutre2()));
            }
            if (donneesDeBaseProvisoire.getRevenuAutre1().length() > 0) {
                rnaProvisoire = Float.parseFloat(JANumberFormatter.deQuote(donneesDeBaseProvisoire.getRevenuAutre1()));
            }
            if (donneesDeBaseProvisoire.getRevenuAutre2().length() > 0) {
                rnaProvisoire += Float.parseFloat(JANumberFormatter.deQuote(donneesDeBaseProvisoire.getRevenuAutre2()));
            }
            if (Math.abs(rna - rnaProvisoire) < .0000001) {
                return "";
            } else {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        } else {
            return "";
        }
    }

    /**
     * Test si le revenu (non agricole) du retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleRNA(String niveauMsg, String idParam, String description) throws Exception {
        if ((getCommunicationRetour().getDecisionGeneree() != null)
                && (getCommunicationRetour().getDecisionDeBase() != null)) {
            float ra = 0;
            float raProvisoire = 0;
            CPDonneesBase donneesDeBaseProvisoire = new CPDonneesBase();
            donneesDeBaseProvisoire.setSession(getSession());
            donneesDeBaseProvisoire.setIdDecision(getCommunicationRetour().getDecisionDeBase().getIdDecision());
            donneesDeBaseProvisoire.retrieve();
            if (getCommunicationRetour().getDecisionGeneree().getRevenu1().length() > 0) {
                ra = Float.parseFloat(JANumberFormatter.deQuote(getCommunicationRetour().getDecisionGeneree()
                        .getRevenu1()));
            }
            if (getCommunicationRetour().getDecisionGeneree().getRevenu2().length() > 0) {
                ra += Float.parseFloat(JANumberFormatter.deQuote(getCommunicationRetour().getDecisionGeneree()
                        .getRevenu2()));
            }
            if (donneesDeBaseProvisoire.getRevenu1().length() > 0) {
                raProvisoire = Float.parseFloat(JANumberFormatter.deQuote(donneesDeBaseProvisoire.getRevenu1()));
            }
            if (donneesDeBaseProvisoire.getRevenu2().length() > 0) {
                raProvisoire += Float.parseFloat(JANumberFormatter.deQuote(donneesDeBaseProvisoire.getRevenu2()));
            }
            if (Math.abs(ra - raProvisoire) < .0000001) {
                return "";
            } else {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        } else {
            return "";
        }
    }

    /**
     * Test si le code sexe du retour du fisc correspond à celui de la provisoire
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String controleSexe(String niveauMsg, String idParam, String description) throws Exception {
        if ((getTiers() != null) && (getTiersProvisoire() != null)) {
            if (getTiers().getSexe().equals(getTiersProvisoire().getSexe())) {
                return "";
            } else {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        } else {
            return "";
        }
    }

    /*
     * Test si un lien conjoint existe pour le contribuable ou le conjoint
     */
    protected boolean haveLinkedSpouse() throws Exception {
        boolean lienExistant = false;
        String idRecherche = "";
        // cas ou le contribuable est connu => recherche de son conjoint
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            idRecherche = getCommunicationRetour().getIdTiers();
        } else if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdConjoint())) {
            // cas ou seul l'épouse est connue à la caisse => dans ce cas le fisc renvoie les données sous
            // conjoint...
            idRecherche = getCommunicationRetour().getIdConjoint();
        }
        // Recherche lien de mariage
        if (!JadeStringUtil.isBlankOrZero(idRecherche)) {
            TICompositionTiersManager lien = new TICompositionTiersManager();
            lien.setSession(getSession());
            lien.setForIdTiersEnfantParent(idRecherche);
            lien.setForTypeLien(TICompositionTiers.CS_CONJOINT);
            lien.setForDateEntreDebutEtFin("31.12." + getCommunicationRetour().getAnnee1());
            lien.find();
            if (lien.size() > 0) { // Formatage conjoint
                lienExistant = true;
            }
        }
        return lienExistant;
    }

    /*
     * Retourne niveau du message si affiliation non renseigné
     */
    public String isAffiliationPlusActive(String niveauMsg, String idParam, String description) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getIdAffiliation())
                && JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getIdAffiliationConjoint())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Retourne niveau du message il y a déja une communication et que le code rectificatif est manquant
     */
    public String isAGLAUDateDemandeInf01072008(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean nouvelleCommunication = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isEmpty(nouvelleCommunication.getVdDateDemande())
                && BSessionUtil.compareDateFirstLowerOrEqual(getSession(), nouvelleCommunication.getVdDateDemande(),
                        "01.07.2008")) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Retourne niveau du message il y a déja une communication et que le code rectificatif est manquant
     */
    public String isAGRIVITCommunicationIncomplete(String niveauMsg, String idParam, String description)
            throws Exception {
        CPCommunicationFiscaleRetourVDViewBean nouvelleCommunication = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (JadeStringUtil.isEmpty(nouvelleCommunication.getVdRevenuNet())
                || "0".equalsIgnoreCase(nouvelleCommunication.getVdRevenuNet())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Erreur si non PCI
     */
    public String isAGRIVITPCIUniquement(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean nouvelleCommunication = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (!"PCI".equalsIgnoreCase(nouvelleCommunication.getVdGenreAffilie())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Retourne niveau du message si il y a une décision avec une année =(période IFD) antèrieure à celle de l'IFD qui
     * est encore à l'état provisoire
     */
    public String isAncienneNonDefinitive(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getCommunicationRetour().getIdTiers());
            decManager.setForIsActive(Boolean.TRUE);
            int anneeLimite = Integer.parseInt(getCommunicationRetour().getAnnee1()) - 1;
            decManager.setToAnneeDecision(Integer.toString(anneeLimite));
            decManager.setInTypeDecision(CPDecision.CS_PROVISOIRE + "," + CPDecision.CS_CORRECTION + ","
                    + CPDecision.CS_ACOMPTE);
            decManager.find();
            if (decManager.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /**
     * Test si l'affilié est à une caisse externe
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isCaisseExterneAvs(String niveauMsg, String idParam, String description) throws Exception {
        if (getCommunicationRetour().getAffiliation() != null) {
            if (AFAffiliationUtil.hasCaisseExterne(getCommunicationRetour().getAffiliation(), getCommunicationRetour()
                    .getAnnee1(), globaz.naos.translation.CodeSystem.GENRE_CAISSE_AVS)) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /**
     * Test si n° de contibuable déjà reçu dans ce lot Ex: CCJU - 2 envois avec le même n° si il y a un conjoint pour
     * NAC
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isCCJUExistant(String niveauMsg, String idParam, String description) throws Exception {
        if (getCommunicationRetour().isNonActif() && !JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPCommunicationFiscaleRetourManager mng = new CPCommunicationFiscaleRetourManager();
            mng.setSession(getSession());
            mng.setForIdJournalRetour(getCommunicationRetour().getIdJournalRetour());
            mng.setForIdTiers(getCommunicationRetour().getIdTiers());
            mng.setExceptIdRetour(getCommunicationRetour().getIdRetour());
            mng.setForIdIfd(getCommunicationRetour().getIdIfd());
            mng.setExceptStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
            mng.find();
            if (mng.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Retourne niveau du message il y a déja une communication et que le code rectificatif est manquant
     */
    public String isCCVDCodeRectifManquant(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean nouvelleCommunication = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        String natureComFis = JadeStringUtil.toUpperCase(nouvelleCommunication.getVdNatureComm());
        if ((JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumAffilie())
                && JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumAvs()) && JadeStringUtil
                    .isEmpty(nouvelleCommunication.getVdNumContribuable()))
                || (JadeStringUtil.isEmpty(nouvelleCommunication.getPeriodeFiscale().toString()))) {
            // On ne peut pas retrouver la communication -> pas d'erreur
            return "";
        } else if (!"R1".equalsIgnoreCase(natureComFis) && !"R2".equalsIgnoreCase(natureComFis)) {
            // Si code <> rectif => ok
            CPCommunicationFiscaleRetourVDManager manager = new CPCommunicationFiscaleRetourVDManager();
            manager.setSession(getSession());
            manager.setWhitAffiliation(true);
            manager.setWhitPavsAffilie(true);
            if (!JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumAffilie())) {
                manager.setForNumAffilie(nouvelleCommunication.getVdNumAffilie());
            }
            if (!JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumAvs())) {
                manager.setForNumAvs(nouvelleCommunication.getVdNumAvs());
            }
            if (!JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumContribuable())) {
                manager.setForNumContibuable(nouvelleCommunication.getVdNumContribuable());
            }
            manager.setForGenreAffilie(nouvelleCommunication.getGenreAffilie());
            manager.setForIdIfd(nouvelleCommunication.getIdIfd());
            manager.setExceptIdRetour(nouvelleCommunication.getIdRetour());
            if (manager.getCount() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Présence d'un commentaire
     */
    public String isCCVDCommentaire(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isEmpty(comm.getVdCommentaire())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Retourne niveau du message si les montants sont identiques à une autre communication
     */
    public String isCCVDCommunicationsIdentiques(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean nouvelleCommunication = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if ((JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumAffilie())
                && JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumAvs()) && JadeStringUtil
                    .isEmpty(nouvelleCommunication.getVdNumContribuable()))
                || (JadeStringUtil.isEmpty(nouvelleCommunication.getPeriodeFiscale().toString()))) {
            // On ne peut pas retrouver la communication -> pas d'erreur
            return "";
        } else {
            CPCommunicationFiscaleRetourVDManager manager = new CPCommunicationFiscaleRetourVDManager();
            manager.setSession(getSession());
            manager.setWhitAffiliation(true);
            manager.setWhitPavsAffilie(true);
            if (!JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumAffilie())) {
                manager.setForVdNumAffilie(nouvelleCommunication.getVdNumAffilie());
            }
            if (!JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumAvs())) {
                manager.setForVdNumAvs(nouvelleCommunication.getVdNumAvs());
            }
            if (!JadeStringUtil.isEmpty(nouvelleCommunication.getVdNumContribuable())) {
                manager.setForVdNumContribuable(nouvelleCommunication.getVdNumContribuable());
            }
            manager.setForGenreAffilie(nouvelleCommunication.getGenreAffilie());
            manager.setForIdIfd(nouvelleCommunication.getIdIfd());
            // Prndre seulement celles qui sont inférieures sinon on a le problème en cas de modification
            // (I090306_000024)
            manager.setForLtIdRetour(nouvelleCommunication.getIdRetour());
            manager.find();
            for (int i = 0; i < manager.size(); i++) {
                CPCommunicationFiscaleRetourVDViewBean ancienneCommunication = (CPCommunicationFiscaleRetourVDViewBean) manager
                        .getEntity(i);
                if (ancienneCommunication.getRevenu1().equals(nouvelleCommunication.getRevenu1())
                        && ancienneCommunication.getRevenu2().equals(nouvelleCommunication.getRevenu2())
                        && ancienneCommunication.getCapital().equals(nouvelleCommunication.getCapital())
                        && ancienneCommunication.getVdCommentaire().equals(nouvelleCommunication.getVdCommentaire())
                        && ancienneCommunication.getVdGenreAffilie().equals(nouvelleCommunication.getVdGenreAffilie())
                        && ancienneCommunication.getVdDatDetCapInvesti().equals(
                                nouvelleCommunication.getVdDatDetCapInvesti())
                        && ancienneCommunication.getVdDateDetFortune().equals(
                                nouvelleCommunication.getVdDateDetFortune())
                        && ancienneCommunication.getVdDebAssuj().equals(nouvelleCommunication.getVdDebAssuj())
                        && ancienneCommunication.getFortune().equals(nouvelleCommunication.getFortune())) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            return "";
        }
    }

    /**
     * Condition de base indispensable pour la génération - Cette méthode doit obligatoirement être active chez le
     * client. Recherche si le contribuable à une affiliation pour l'année et le genre concernés (initialise l'id
     * affiliation qui sera utilisé pour la génération des décisions)
     * 
     * @return niveau du message si il n'y a pas d'affiliation ou si il en a plusieurs
     * @throws Exception
     */
    public String isCCVDConditionDeBase(String niveauMsg, String idParam, String description) throws Exception {
        boolean ok = false;
        // Recherche du tiers
        TITiersViewBean tiers = new TITiersViewBean();
        AFAffiliation affiliation = null;
        getCommunicationRetour().setTiers(null);
        getCommunicationRetour().setIdTiers("");
        // Recherche de la propriété qui indique sur quoi la caisse se base
        // pour retrouver le tiers (Ex: la FER sur le n° d'affilié mais la CCJU sur le n° de contribuable
        // Cette information est propre à la caisse et non au canton.
        // Ex: FER -> n° d'affilié mais il se pourrait qu'une autre caisse de GE se base sur le n° de contribuable (EX:
        // FACO)
        // Par défaut : recherche sur le n° de contribuable¨
        // Recherche la période fiscale
        CPPeriodeFiscale periodeFiscale = getCommunicationRetour().getPeriodeFiscale();
        if (periodeFiscale != null) {
            getCommunicationRetour().setIdIfd(periodeFiscale.getIdIfd());
        } else {
            ajouterErreur(idParam);
            getCommunicationRetour().setIdIfd("");
            return niveauMsg;
        }
        // Recherche sur le n° d'affilié
        boolean trouve = false;
        // Recherche
        tiers.setSession(getSession());
        if (JadeStringUtil.isEmpty(getCommunicationRetour().getMajNumContribuable())) {
            // Recherche variable si mise à jour du numéro de contribuable
            getCommunicationRetour().setMajNumContribuable(
                    ((CPApplication) getSession().getApplication()).getMajNumContribuable());
        }
        if ("numAffilie".equalsIgnoreCase(getCommunicationRetour().getJournalRetour().getZoneRecherche())) {
            // formatage du numero selon caisse pour le numero AVS
            if (!JadeStringUtil.isEmpty(getCommunicationRetour().getValeurChampRecherche())) {
                String numAff = "";
                try {
                    TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                    IFormatData affilieFormater = app.getAffileFormater();
                    if (affilieFormater != null) {
                        numAff = affilieFormater.format(getCommunicationRetour().getValeurChampRecherche());
                    }
                } catch (Exception ex) {
                    numAff = getCommunicationRetour().getValeurChampRecherche();
                }
                if (!JadeStringUtil.isEmpty(numAff)) {
                    AFAffiliationManager histo = new AFAffiliationManager();
                    histo.setSession(getSession());
                    histo.setForAffilieNumero(numAff);
                    // histo.setForNumAffilie((getCommunicationRetour().getValeurChampRecherche()).substring(0,3)+"."+(getCommunicationRetour().getValeurChampRecherche()).substring(3));
                    histo.find();
                    if (histo.size() > 0) {
                        tiers.setIdTiers(((AFAffiliation) histo.getFirstEntity()).getIdTiers());
                        affiliation = CPToolBox.returnAffiliation(getSession(), getSession()
                                .getCurrentThreadTransaction(), numAff, getCommunicationRetour().getAnnee1(), "", 2);
                        trouve = true;
                    }
                }
                // } else if (!JadeStringUtil.isEmpty(getCommunicationRetour().getNumAvs())) {
                // TIHistoriqueAvsManager histo = new TIHistoriqueAvsManager();
                // histo.setSession(getSession());
                // histo.setForNumAvs(getCommunicationRetour().getNumAvs());
                // histo.find();
                // for (int i = 0; i < histo.size() && !trouve; i++) {
                // tiers.setIdTiers(((TIHistoriqueAvs) histo.getEntity(i)).getIdTiers());
                // affiliation = CPToolBox.returnAffiliation(getSession(), (BTransaction) getTransaction(),
                // tiers.getIdTiers(), getCommunicationRetour().getAnnee1(), getCommunicationRetour().getGenreAffilie(),
                // 1);
                // // if (affiliation != null) {
                // trouve = true;
                // // }
                // }
            }
        } else if ("numAvs".equalsIgnoreCase(getCommunicationRetour().getJournalRetour().getZoneRecherche())) {
            if (!JadeStringUtil.isEmpty(getCommunicationRetour().getValeurChampRecherche())) {
                TIHistoriqueAvsManager histo = new TIHistoriqueAvsManager();
                histo.setSession(getSession());
                histo.setForNumAvs(getCommunicationRetour().getValeurChampRecherche());
                histo.find();
                if (histo.size() > 0) {
                    tiers.setIdTiers(((TIHistoriqueAvs) histo.getFirstEntity()).getIdTiers());
                    affiliation = CPToolBox.returnAffiliation(getSession(), getSession().getCurrentThreadTransaction(),
                            tiers.getIdTiers(), getCommunicationRetour().getAnnee1(), getCommunicationRetour()
                                    .getGenreAffilie(), 1);
                    trouve = true;
                }
            } else if (!JadeStringUtil.isEmpty(getCommunicationRetour().getNumContribuableRecu())) {
                if (!JadeStringUtil.isEmpty(getCommunicationRetour().getValeurChampRecherche())) {
                    TIHistoriqueContribuableManager histo = new TIHistoriqueContribuableManager();
                    histo.setSession(getSession());
                    histo.setForNumContribuable(getCommunicationRetour().getNumContribuableRecu());
                    histo.find();
                    for (int i = 0; (i < histo.size()) && !trouve; i++) {
                        tiers.setIdTiers(((TIHistoriqueContribuable) histo.getEntity(i)).getIdTiers());
                        affiliation = CPToolBox.returnAffiliation(getSession(), getSession()
                                .getCurrentThreadTransaction(), tiers.getIdTiers(), getCommunicationRetour()
                                .getAnnee1(), getCommunicationRetour().getGenreAffilie(), 1);
                        // if (affiliation != null) {
                        trouve = true;
                        // }
                    }
                }
            }
        } else {
            if (!JadeStringUtil.isEmpty(getCommunicationRetour().getValeurChampRecherche())) {
                TIHistoriqueContribuableManager histo = new TIHistoriqueContribuableManager();
                histo.setSession(getSession());
                histo.setForNumContribuable(getCommunicationRetour().getValeurChampRecherche());
                histo.find();
                for (int i = 0; (i < histo.size()) && !trouve; i++) {
                    tiers.setIdTiers(((TIHistoriqueContribuable) histo.getEntity(i)).getIdTiers());
                    affiliation = CPToolBox.returnAffiliation(getSession(), getSession().getCurrentThreadTransaction(),
                            tiers.getIdTiers(), getCommunicationRetour().getAnnee1(), getCommunicationRetour()
                                    .getGenreAffilie(), 1);
                    trouve = true;
                }
            }
        }
        if (trouve) {
            ok = true;
            tiers.retrieve();
            // Mise à jour du n° de contribuale si il est différent
            // et si la caisse le veut (properties majNumContribuable)
            if (!tiers.isNew() && getCommunicationRetour().getMajNumContribuable().equalsIgnoreCase("YES")
                    && !JadeStringUtil.isEmpty(getCommunicationRetour().getNumContribuableRecu())) {
                // Recherche du n° de contribuable dans historique des tiers en vigueur pour l'année de la communication
                String varNumContri = "";
                TIHistoriqueContribuable hist = new TIHistoriqueContribuable();
                hist.setSession(getSession());
                varNumContri = hist.findPrevKnownNumContribuable(tiers.getIdTiers(), "31.12."
                        + getCommunicationRetour().getAnnee1());
                if (JadeStringUtil.isIntegerEmpty(varNumContri)) {
                    varNumContri = hist.findNextKnownNumContribuable(tiers.getIdTiers(), "31.12."
                            + getCommunicationRetour().getAnnee1());
                    if (JadeStringUtil.isIntegerEmpty(varNumContri)) {
                        varNumContri = "";
                    }
                }
                boolean histoSuperieur = false;
                if (!JadeStringUtil.isEmpty(varNumContri)) {
                    String var1 = CPToolBox.unFormat(varNumContri);
                    String var2 = CPToolBox.unFormat(getCommunicationRetour().getNumContribuableRecu());
                    if (!var1.equalsIgnoreCase(var2)) {
                        // Maj historique si année début historique= année de taxation
                        if (!JadeStringUtil.isEmpty(tiers.getIdTiers())) {
                            TIHistoriqueContribuableManager histComMng = new TIHistoriqueContribuableManager();
                            histComMng.setSession(getSession());
                            histComMng.setForIdTiers(tiers.getIdTiers());
                            histComMng.find();
                            for (int i = 0; i < histComMng.size(); i++) {
                                TIHistoriqueContribuable entity = ((TIHistoriqueContribuable) histComMng.getEntity(i));
                                if (JACalendar.getYear(entity.getEntreeVigueur()) == Integer
                                        .parseInt(getCommunicationRetour().getAnnee1())) {
                                    entity.setEntreeVigueur("01.01." + getCommunicationRetour().getAnnee1());
                                    entity.setNumContribuable(getCommunicationRetour().getNumContribuableRecu());
                                    entity.update(getSession().getCurrentThreadTransaction());
                                } else if (JACalendar.getYear(entity.getEntreeVigueur()) > Integer
                                        .parseInt(getCommunicationRetour().getAnnee1())) {
                                    histoSuperieur = true;
                                }

                            }
                        }
                        // Création et maj num contribuable actuelle si il n'y a pas d'historique > à l'année
                        if (!histoSuperieur) {
                            // Mise à jour du n° actuel (stocké dans tiers) si celui que l'on a modifié dans
                            // l'historique lui était égal.
                            tiers.setMotifModifContribuable(TIHistoriqueContribuable.CS_CREATION);
                            tiers.setDateModifContribuable("01.01." + getCommunicationRetour().getAnnee1());
                            tiers.setNumContribuableActuel(getCommunicationRetour().getNumContribuableRecu());
                            tiers.update(getSession().getCurrentThreadTransaction());
                        } else if (!varNumContri.equalsIgnoreCase(tiers.getNumContribuableActuel())) {
                            // Mise à jour du n° actuel (stocké dans tiers) si celui que l'on a modifié dans
                            // l'historique lui était égal.
                            tiers.setNumContribuableActuel(getCommunicationRetour().getNumContribuableRecu());
                            tiers.update(getSession().getCurrentThreadTransaction());
                        }
                    }
                }
            }
            getCommunicationRetour().setTiers(tiers);
            getCommunicationRetour().setIdTiers(tiers.getIdTiers());
            // Recherche id Conjoint
            TICompositionTiersManager cjt = new TICompositionTiersManager();
            cjt.setSession(getSession());
            // Recherche du conjoint
            // acr: selon directive AVS: Le mariage doit être considéré depuis le début de l'année du mariage (division
            // par deux pour toute l'année).
            // Le divorce doit être considéré depuis le début de l'année du divorce (individuel pour toute l'année).
            // Ce qui signifie que l'on va chercher le conjoint au 31.12 de l'année de la décision
            cjt.setForIdTiersEnfantParent(tiers.getIdTiers());
            cjt.setForTypeLien(TICompositionTiers.CS_CONJOINT);
            cjt.setForDateEntreDebutEtFin("31.12." + getCommunicationRetour().getAnnee1());
            cjt.find();
            if (cjt.size() > 0) { // Formatage conjoint
                String idCjt = "";
                if (((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent().equals(tiers.getIdTiers())) {
                    idCjt = ((TICompositionTiers) cjt.getEntity(0)).getIdTiersEnfant();
                } else {
                    idCjt = ((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent();
                }
                getCommunicationRetour().setIdConjoint(idCjt);
                if (!JadeStringUtil.isIntegerEmpty(idCjt) && (100 != Integer.parseInt(idCjt))) {
                    int nb = CPToolBox.returnNbAffiliation(getSession(), getSession().getCurrentThreadTransaction(),
                            idCjt, getCommunicationRetour().getAnnee1(), "", 1);
                    if (nb == 1) {
                        AFAffiliation affCjt = CPToolBox.returnAffiliation(getSession(), getSession()
                                .getCurrentThreadTransaction(), idCjt, getCommunicationRetour().getAnnee1(), "", 1);
                        if (affCjt != null) {
                            getCommunicationRetour().setIdAffiliationConjoint(affCjt.getAffiliationId());
                        }
                    }
                }
            }
        }
        // Affiliation
        if (getCommunicationRetour().getTiers() != null) {
            // Si affiliation = null -> erreur
            if (affiliation != null) {
                getCommunicationRetour().setIdAffiliation(affiliation.getAffiliationId());
                // Recherche de la communication fiscale (utilise IdTiers et IdIfd)
                getCommunicationRetour().setNumAffilie(affiliation.getAffilieNumero());
                CPCommunicationFiscale communicationFiscale = getCommunicationRetour().getCommunicationFiscale(2);
                if (communicationFiscale != null) {
                    getCommunicationRetour().setIdCommunication(communicationFiscale.getIdCommunication());
                } else {
                    getCommunicationRetour().setIdCommunication("");
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            } else {
                getCommunicationRetour().setIdAffiliation("");
            }
        } else {
            // Erreur tiers inexistant
            getCommunicationRetour().setIdTiers("");
            ajouterErreur(idParam);
            return niveauMsg;
        }
        // Genre affiliation
        if (JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getGenreAffilie())) {
            ajouterErreur(idParam);
            ok = false;
            return niveauMsg;
        }
        // Genre taxation
        if (JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getGenreTaxation())) {
            ajouterErreur(idParam);
            ok = false;
        }
        if (ok) {
            return "";
        } else {
            ajouterErreur(idParam);
            return niveauMsg;
        }
    }

    /*
     * Si pas de commentaire et un revenu PCI à 0
     */
    public String isCCVDControleRevenuPCI(String niveauMsg, String idParam, String description) throws Exception {
        if (!getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_NON_ACTIF)) {
            CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
            if (!comm.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)
                    && !comm.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
                if ((comm.getVdRevPCI().equals("0") || JadeStringUtil.isEmpty(comm.getVdRevPCI()))
                        && (JadeStringUtil.isEmpty(comm.getVdCommentaire()))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            } else if (comm.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)) {
                String varMontant = JANumberFormatter.deQuote(comm.getVdRevenuNet());
                // Pour le type AGR (agriculteur), le revenu est dans une autre zone
                if ((varMontant.equals("0") || JadeStringUtil.isEmpty(varMontant))
                        && (JadeStringUtil.isEmpty(comm.getVdCommentaire()))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            } else if (comm.getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
                String varMontant = JANumberFormatter.deQuote(comm.getVdSalaireCotisant());
                // Pour le type SAL (TSE), le revenu est dans une autre zone
                if ((varMontant.equals("0") || JadeStringUtil.isEmpty(varMontant))
                        && (JadeStringUtil.isEmpty(comm.getVdCommentaire()))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    public String isCCVDControleRevenuPSA(String niveauMsg, String idParam, String description) throws Exception {
        if (getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_NON_ACTIF)) {
            CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
            if (!JadeStringUtil.isEmpty(comm.getVdRevenuActivitesLucratives())
                    && !comm.getVdRevenuActivitesLucratives().equals("0")) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Fichier central: Si affilié inconnu et spontanée
     */
    public String isCCVDFichierCentral(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (JadeStringUtil.isEmpty(comm.getVdNumAffilie())
                && comm.getVdNatureComm().equalsIgnoreCase(CPCommunicationFiscaleRetourVDViewBean.SPONTANEE)) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Controle gain immobilier pour indépendant
     */
    public String isCCVDGainImmobilier(String niveauMsg, String idParam, String description) throws Exception {
        if (!getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_NON_ACTIF)) {
            CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
            if ((!comm.getVdGIprof().equals("0")) && (!JadeStringUtil.isEmpty(comm.getVdGIprof()))) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Imposition selon la dépense
     */
    public String isCCVDImpositionDepense(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isEmpty(comm.getVdImpositionDepense()) && !comm.getVdImpositionDepense().equals("0")) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isCCVDInf2007(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        int varNum = Integer.parseInt(comm.getAnnee1());
        if (varNum < 2007) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Communication RI, PC, décès, départ à l'étranger Si particularité = benef PC, Refugié, assisté ou si motif fin =
     * décès ou départ étranger
     */
    public String isCCVDRiPcDecesDepart(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isBlankOrZero(comm.getIdTiers())) {
            // Recherche des affiliations actives pour l'année de la communication
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation.setAffiliationId(comm.getIdAffiliation());
            affiliation.retrieve();
            if (!affiliation.isNew()) {
                if (CPToolBox.isAffilieAssiste(getSession().getCurrentThreadTransaction(), affiliation,
                        "01.01." + comm.getAnnee1(), "31.12." + comm.getAnnee1())) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
                if (affiliation.getMotifFin().equals("803006") || affiliation.getMotifFin().equals("803013")
                        || affiliation.getMotifFin().equals("19150004") || affiliation.getMotifFin().equals("19130020")) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /*
     * Message si Salaire pour non actif
     */
    public String isCCVDSalaire(String niveauMsg, String idParam, String description) throws Exception {
        if (!getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_NON_ACTIF)) {
            CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
            if (!JadeStringUtil.isIntegerEmpty(comm.getVdSalVerseCjt())
                    || !JadeStringUtil.isIntegerEmpty(comm.getVdSalaireCotisant())) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Si mention de spontanée
     */
    public String isCCVDSpontanee(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (comm.getVdNatureComm().equalsIgnoreCase(CPCommunicationFiscaleRetourVDViewBean.SPONTANEE)) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Vigneron-Tâcheron
     */
    public String isCCVDVigneronTacheron(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isEmpty(comm.getIdAffiliation())) {
            AFAffiliation aff = new AFAffiliation();
            aff.setSession(getSession());
            aff.setAffiliationId(comm.getIdAffiliation());
            aff.retrieve();
            if (!aff.isNew()) {
                String dateRef = "01.01." + comm.getAnnee1();
                if (JACalendar.getYear(aff.getDateDebut()) == Integer.parseInt(comm.getAnnee1())) {
                    dateRef = aff.getDateDebut();
                }
                if (!JadeStringUtil.isIntegerEmpty(aff.getDateFin())
                        && (JACalendar.getYear(aff.getDateFin()) == Integer.parseInt(comm.getAnnee1()))) {
                    dateRef = aff.getDateFin();
                }
                if (AFParticulariteAffiliation.existeParticularite(getSession().getCurrentThreadTransaction(),
                        comm.getIdAffiliation(), "19170075", dateRef)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    public String isCCVSCaisseExterneAvs(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        String numCaisse = getSession().getApplication().getProperty("noCaisseFormate");
        // si couple, il faut que les 2 affiliés soient caisse externe
        if (!JadeStringUtil.isBlankOrZero(comm.getVsNomAffilie())
                && !numCaisse.equalsIgnoreCase(comm.getVsNoCaisseAgenceAffilie(1))
                && !JadeStringUtil.isBlankOrZero(comm.getVsNomConjoint())
                && !numCaisse.equalsIgnoreCase(comm.getVsNumCaisseAgenceConjoint(1))) {
            ajouterErreur(idParam);
            comm.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
            return niveauMsg;
        }
        if (!JadeStringUtil.isBlankOrZero(comm.getVsNomAffilie()) // PO 8900
                && !numCaisse.equalsIgnoreCase(comm.getVsNoCaisseAgenceAffilie(1))
                && JadeStringUtil.isBlankOrZero(comm.getVsNomConjoint())) {
            ajouterErreur(idParam);
            comm.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
            return niveauMsg;
        }
        if (!JadeStringUtil.isBlankOrZero(comm.getVsNomConjoint()) // PO 8900
                && !numCaisse.equalsIgnoreCase(comm.getVsNumCaisseAgenceConjoint(1))
                && JadeStringUtil.isBlankOrZero(comm.getVsNomAffilie())) {
            ajouterErreur(idParam);
            comm.setStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
            return niveauMsg;
        }
        return "";
    }

    public String isCCVSCaisseExterneAvsBis(String niveauMsg, String idParam, String description) throws Exception {

        // si couple, il faut que les 2 affiliés soient caisse externe
        if (!JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliation())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliationConjoint())) {
            if (AFAffiliationUtil.hasCaisseExterne(getCommunicationRetour().getAffiliation(), getCommunicationRetour()
                    .getAnnee1(), globaz.naos.translation.CodeSystem.GENRE_CAISSE_AVS)
                    && AFAffiliationUtil.hasCaisseExterne(getCommunicationRetour().getAffiliationConjoint(),
                            getCommunicationRetour().getAnnee1(), globaz.naos.translation.CodeSystem.GENRE_CAISSE_AVS)) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        if (!JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliation())) {
            if (AFAffiliationUtil.hasCaisseExterne(getCommunicationRetour().getAffiliation(), getCommunicationRetour()
                    .getAnnee1(), globaz.naos.translation.CodeSystem.GENRE_CAISSE_AVS)) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        if (!JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliationConjoint())) {
            if (AFAffiliationUtil.hasCaisseExterne(getCommunicationRetour().getAffiliationConjoint(),
                    getCommunicationRetour().getAnnee1(), globaz.naos.translation.CodeSystem.GENRE_CAISSE_AVS)) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /**
     * Règle pour le Valais pour l'AFI Test si l'affilié change de genre ou de cotisation niveauMsg=1 => error
     * niveauMsg=2 => warning
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isCCVSChangeGenre(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        getCommunicationRetour().setChangementGenre("");
        if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(comm.getGenreAffilie())
                && !JadeStringUtil.isEmpty(comm.getIdAffiliation())) {
            // Tests AFI
            // Si revenu agricole > 75% du revenu non agricole => Peut être considéré comme AFI
            boolean AFIPotentiel = false;
            BigDecimal mRevenuAgricole = new BigDecimal(0);
            BigDecimal mRevenuNonAgricole = new BigDecimal(0);
            BigDecimal m75RevenuNonAgricole = new BigDecimal(0);
            BigDecimal m30RevenuNonAgricole = new BigDecimal(0);
            if (!JadeStringUtil.isEmpty(comm.getVsRevenuNonAgricoleCtb())) {
                mRevenuNonAgricole = new BigDecimal(JANumberFormatter.deQuote(comm.getVsRevenuNonAgricoleCtb()));
            }
            if (!JadeStringUtil.isEmpty(comm.getVsRevenuAgricoleCtb())) {
                mRevenuAgricole = new BigDecimal(JANumberFormatter.deQuote(comm.getVsRevenuAgricoleCtb()));
            }
            // Si revenu négatif => mettre à 0
            if (mRevenuNonAgricole.compareTo(new BigDecimal(0)) == -1) {
                mRevenuNonAgricole = new BigDecimal(0);
            }
            if (mRevenuAgricole.compareTo(new BigDecimal(0)) == -1) {
                mRevenuAgricole = new BigDecimal(0);
            }
            m75RevenuNonAgricole = mRevenuNonAgricole;
            m75RevenuNonAgricole = m75RevenuNonAgricole.multiply(new BigDecimal(75));
            m75RevenuNonAgricole = m75RevenuNonAgricole.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
            m30RevenuNonAgricole = mRevenuNonAgricole;
            m30RevenuNonAgricole = m30RevenuNonAgricole.multiply(new BigDecimal(30));
            m30RevenuNonAgricole = m30RevenuNonAgricole.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
            if (mRevenuAgricole.compareTo(m75RevenuNonAgricole) == 1) {
                // Débit > 30%
                AFIPotentiel = true;
            }
            // Si (RNA * 0.30) <= RA <= (RNA*0.75) => pas de changement
            if ((mRevenuAgricole.compareTo(m30RevenuNonAgricole) >= 0)
                    && (mRevenuAgricole.compareTo(m75RevenuNonAgricole) <= 0)) {
                getCommunicationRetour().setChangementGenre("");
            } else {
                // Recherche cotisation AFI
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(getSession());
                AFCotisation cotiAfi = aff._cotisation(getSession().getCurrentThreadTransaction(),
                        getCommunicationRetour().getIdAffiliation(),
                        globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL,
                        globaz.naos.translation.CodeSystem.TYPE_ASS_AFI, "01.01."
                                + getCommunicationRetour().getAnnee1(),
                        "31.12." + getCommunicationRetour().getAnnee1(), 1);
                // Si revenu agricole => test si cotisation AF
                if (AFIPotentiel) {
                    // Si AFI potentiel et pas de cotisation AFI de définie => Message changement Indépendant -> AFI
                    if (cotiAfi == null) {
                        getCommunicationRetour().setChangementGenre(CPCommunicationFiscaleRetourViewBean.CS_IND_AFI);
                    }
                } else {
                    // Si non AFI potentiel et cotisation AFI définie => Message changement AFI -> Indépendant
                    if (cotiAfi != null) {
                        getCommunicationRetour().setChangementGenre(CPCommunicationFiscaleRetourViewBean.CS_AFI_IND);
                    }
                }
            }
            // Avertir qu'il y a eu un changement de genere
            if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getChangementGenre())) {
                // ajouterErreur(idParam);
                // return niveauMsg;
            }
        }
        return "";
    }

    public String isCCVSChangeGenreApres(String niveauMsg, String idParam, String description) throws Exception {
        // Avertir qu'il y a eu un changement de genere
        if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getChangementGenre())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /**
     * Règle pour le Valais pour l'AFI Test si l'affilié change de genre ou de cotisation niveauMsg=1 => error
     * niveauMsg=2 => warning
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isCCVSChangeGenreConjoint(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        getCommunicationRetour().setChangementGenreConjoint("");
        if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(comm.getGenreConjoint())
                && !JadeStringUtil.isBlankOrZero(comm.getIdAffiliationConjoint())) {
            // Tests AFI
            // Si revenu agricole > 75% du revenu non agricole => Peut être considéré comme AFI
            boolean AFIPotentiel = false;
            BigDecimal mRevenuAgricole = new BigDecimal(0);
            BigDecimal mRevenuNonAgricole = new BigDecimal(0);
            BigDecimal m75RevenuNonAgricole = new BigDecimal(0);
            BigDecimal m30RevenuNonAgricole = new BigDecimal(0);
            // Conjoint
            mRevenuAgricole = new BigDecimal(0);
            mRevenuNonAgricole = new BigDecimal(0);
            if (!JadeStringUtil.isEmpty(comm.getVsRevenuNonAgricoleConjoint())) {
                mRevenuNonAgricole = new BigDecimal(JANumberFormatter.deQuote(comm.getVsRevenuNonAgricoleConjoint()));
            }
            if (!JadeStringUtil.isEmpty(comm.getVsRevenuAgricoleConjoint())) {
                mRevenuAgricole = new BigDecimal(JANumberFormatter.deQuote(comm.getVsRevenuAgricoleConjoint()));
            }
            m75RevenuNonAgricole = mRevenuNonAgricole;
            m75RevenuNonAgricole = m75RevenuNonAgricole.multiply(new BigDecimal(75));
            m75RevenuNonAgricole = m75RevenuNonAgricole.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
            m30RevenuNonAgricole = mRevenuNonAgricole;
            m30RevenuNonAgricole = m30RevenuNonAgricole.multiply(new BigDecimal(30));
            m30RevenuNonAgricole = m30RevenuNonAgricole.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
            if (mRevenuAgricole.compareTo(m75RevenuNonAgricole) == 1) {
                // Débit > 30%
                AFIPotentiel = true;
            }
            // Si (RNA * 0.30) <= RA <= (RNA*0.75) => pas de changement
            if ((mRevenuAgricole.compareTo(m30RevenuNonAgricole) >= 0)
                    && (mRevenuAgricole.compareTo(m75RevenuNonAgricole) <= 0)) {
                getCommunicationRetour().setChangementGenreConjoint("");
            } else {
                // Recherche cotisation AFI
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(getSession());
                AFCotisation cotiAfi = aff._cotisation(getSession().getCurrentThreadTransaction(),
                        comm.getIdAffiliationConjoint(), globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL,
                        globaz.naos.translation.CodeSystem.TYPE_ASS_AFI, "01.01."
                                + getCommunicationRetour().getAnnee1(),
                        "31.12." + getCommunicationRetour().getAnnee1(), 1);
                // Si revenu agricole => test si cotisation AF
                if (AFIPotentiel) {
                    // Si AFI potentiel et pas de cotisation AFI de définie => Message changement Indépendant -> AFI
                    if (cotiAfi == null) {
                        getCommunicationRetour().setChangementGenreConjoint(
                                CPCommunicationFiscaleRetourViewBean.CS_IND_AFI);
                    }
                } else {
                    // Si non AFI potentiel et cotisation AFI définie => Message changement AFI -> Indépendant
                    if (cotiAfi != null) {
                        getCommunicationRetour().setChangementGenreConjoint(
                                CPCommunicationFiscaleRetourViewBean.CS_AFI_IND);
                    }
                }
            }
            // Avertir qu'il y a eu un changement de genere
            if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getChangementGenreConjoint())) {
                // ajouterErreur(idParam);
                // return niveauMsg;
            }
        }
        return "";
    }

    public String isCCVSChangeGenreConjointApres(String niveauMsg, String idParam, String description) throws Exception {
        // Avertir qu'il y a eu un changement de genere
        if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getChangementGenreConjoint())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /**
     * Condition de base indispensable pour la génération pour la CCCVS
     * 
     * @return niveau du message s'il ne remplit pas les condition
     * @throws Exception
     */
    public String isCCVSCondition(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        boolean ok = false;
        // Recherche du tiers
        comm.setGenreAffilie("");
        comm.setGenreConjoint("");
        TITiersViewBean tiers = new TITiersViewBean();
        AFAffiliation affiliation = null;
        getCommunicationRetour().setTiers(null);
        getCommunicationRetour().setIdTiers("");
        getCommunicationRetour().setIdConjoint("");
        getCommunicationRetour().setIdAffiliation("");
        getCommunicationRetour().setIdAffiliationConjoint("");
        // Pour VS: Pour le conjoint, ne pas se baser sur la liaison dans les
        // tiers mais sur les données du fisc
        // => rechercher à la fois le contribuable et le conjoint selon les
        // données du fisc.
        // Tenir compte du fait que le contribuable peut être affilié ailleurs,
        // c'est à dire que seul le conjoint est au valais.
        tiers.setSession(getSession());
        if (JadeStringUtil.isEmpty(getCommunicationRetour().getMajNumContribuable())) {
            // Recherche variable si mise à jour du numéro de contribuable
            getCommunicationRetour().setMajNumContribuable(
                    ((CPApplication) getSession().getApplication()).getMajNumContribuable());
        }
        String tri = "";
        if ("numAffilie".equalsIgnoreCase(getCommunicationRetour().getJournalRetour().getZoneRecherche())) {
            // formatage du numero si numéro = ancien formatage
            String numAff = "";
            AFAffiliationManager histo = new AFAffiliationManager();
            histo.setSession(getSession());
            if (!JadeStringUtil.isBlankOrZero(comm.getVsNumAffilie(0))
                    && !comm.getVsNumAffilie(0).equalsIgnoreCase(comm.getVsNumAffilieConjoint(0))) {
                try {
                    TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                    IFormatData affilieFormater = app.getAffileFormater();
                    if (affilieFormater != null) {
                        numAff = affilieFormater.format(comm.formaterNumAffilieVS(comm.getVsNumAffilie(0)));
                        tri = numAff;
                    }
                } catch (Exception ex) {
                    numAff = getCommunicationRetour().getValeurChampRecherche();
                }
                histo.setForAffilieNumero(numAff);
                histo.find();
                if (histo.size() > 0) {
                    tiers.setIdTiers(((AFAffiliation) histo.getFirstEntity()).getIdTiers());
                    getCommunicationRetour().setTiers(tiers);
                    getCommunicationRetour().setIdTiers(tiers.getIdTiers());
                    if (getCommunicationRetour().getMajNumContribuable().equalsIgnoreCase("YES")) {
                        tiers.retrieve();
                        // Mise à jour du n° de contribuale si il est différent
                        // et si la caisse le veut (properties
                        // majNumContribuable)
                        if (!tiers.isNew()
                                && !JadeStringUtil.isEmpty(getCommunicationRetour().getNumContribuableRecu())
                                && !tiers.getNumContribuableActuel().equalsIgnoreCase(
                                        getCommunicationRetour().getNumContribuableRecu())) {
                            tiers.setMotifModifContribuable(TIHistoriqueContribuable.CS_CREATION);
                            tiers.setDateModifContribuable(getCommunicationRetour().getJournalRetour()
                                    .getDateReception());
                            tiers.setNumContribuableActuel(getCommunicationRetour().getNumContribuableRecu());
                            tiers.update(getSession().getCurrentThreadTransaction());
                        }
                    }
                    affiliation = CPToolBox
                            .returnAffiliation(getSession(), getSession().getCurrentThreadTransaction(), numAff,
                                    getCommunicationRetour().getAnnee1(), getCommunicationRetour().getGenreAffilie(), 2);
                    if (affiliation != null) {
                        ok = true;
                        getCommunicationRetour().setIdAffiliation(affiliation.getAffiliationId());
                    }
                }
            }
            // idem pour conjoint
            if (!JadeStringUtil.isBlankOrZero(comm.getVsNumAffilieConjoint(0))) {
                try {
                    TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                    IFormatData affilieFormater = app.getAffileFormater();
                    if (affilieFormater != null) {
                        numAff = affilieFormater.format(comm.formaterNumAffilieVS(comm.getVsNumAffilieConjoint(0)));
                        if ((JadeStringUtil.isBlankOrZero(tri) || tri.equalsIgnoreCase("000.000.000000"))
                                && !numAff.equalsIgnoreCase("000.000.000000") && !JadeStringUtil.isBlankOrZero(numAff)) {
                            tri = numAff;
                        }
                    }
                } catch (Exception ex) {
                    numAff = comm.getVsNumAffilieConjoint(0);
                }
                histo.setForAffilieNumero(numAff);
                histo.find();
                if (histo.size() > 0) {
                    // tiers.setIdTiers(((AFAffiliation)
                    // histo.getFirstEntity()).getIdTiers());
                    // if(JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdTiers())){
                    // getCommunicationRetour().setTiers(tiers);
                    // getCommunicationRetour().setIdTiers(tiers.getIdTiers());
                    // }
                    getCommunicationRetour().setIdConjoint(((AFAffiliation) histo.getFirstEntity()).getIdTiers());
                    if (getCommunicationRetour().getMajNumContribuable().equalsIgnoreCase("YES")) {
                        tiers.retrieve();
                        // Mise à jour du n° de contribuale si il est différent
                        // et si la caisse le veut (properties
                        // majNumContribuable)
                        if (!tiers.isNew()
                                && !JadeStringUtil.isEmpty(getCommunicationRetour().getNumContribuableRecu())
                                && !tiers.getNumContribuableActuel().equalsIgnoreCase(
                                        getCommunicationRetour().getNumContribuableRecu())) {
                            tiers.setMotifModifContribuable(TIHistoriqueContribuable.CS_CREATION);
                            tiers.setDateModifContribuable(getCommunicationRetour().getJournalRetour()
                                    .getDateReception());
                            tiers.setNumContribuableActuel(getCommunicationRetour().getNumContribuableRecu());
                            tiers.update(getSession().getCurrentThreadTransaction());
                        }
                    }
                    affiliation = CPToolBox
                            .returnAffiliation(getSession(), getSession().getCurrentThreadTransaction(), numAff,
                                    getCommunicationRetour().getAnnee1(), getCommunicationRetour().getGenreAffilie(), 2);
                    if (affiliation != null) {
                        ok = true;
                        getCommunicationRetour().setIdAffiliationConjoint(affiliation.getAffiliationId());
                    }
                }
            }
        }
        if ((getCommunicationRetour().getIdTiers() != null)
                && getCommunicationRetour().getIdTiers().equalsIgnoreCase(getCommunicationRetour().getIdConjoint())) {
            getCommunicationRetour().setIdTiers("");
        }
        getCommunicationRetour().setTri(tri);
        // Recherche la période fiscale
        CPPeriodeFiscale periodeFiscale = getCommunicationRetour().getPeriodeFiscale();
        if (periodeFiscale != null) {
            getCommunicationRetour().setIdIfd(periodeFiscale.getIdIfd());
        } else {
            ajouterErreur(idParam);
            getCommunicationRetour().setIdIfd("");
            return niveauMsg;
        }
        // Affiliation
        if ((getCommunicationRetour().getTiers() != null)
                && !JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getIdTiers())) {
            // Recherche de la communication fiscale (utilise IdTiers et IdIfd)
            CPCommunicationFiscale communicationFiscale = getCommunicationRetour().getCommunicationFiscale(1);
            if (communicationFiscale != null) {
                getCommunicationRetour().setIdCommunication(communicationFiscale.getIdCommunication());
            } else {
                getCommunicationRetour().setIdCommunication("");
            }
        } else if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getIdConjoint())) {
            // Recherche de la communication fiscale (utilise IdTiers et IdIfd)
            CPCommunicationFiscale communicationFiscale = getCommunicationRetour().getCommunicationFiscale(3);
            if (communicationFiscale != null) {
                getCommunicationRetour().setIdCommunication(communicationFiscale.getIdCommunication());
            } else {
                getCommunicationRetour().setIdCommunication("");
            }
        }
        if (!ok) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isCCVSConjointIndependant(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm.getGenreAffilie())
                && !JadeStringUtil.isBlankOrZero(comm.getGenreAffilie())
                && !JadeStringUtil.isBlankOrZero(comm.getIdConjoint())) {
            // si revenu >1 ou capital >0 ==> le cas doit être analysé pour éventuelle répartition
            if ((Integer.parseInt(JANumberFormatter.deQuote(comm.getVsRevenuNonAgricoleConjoint())) > 1)
                    || (Integer.parseInt(JANumberFormatter.deQuote(comm.getVsRevenuAgricoleConjoint())) > 1)
                    || (Integer.parseInt(JANumberFormatter.deQuote(comm.getVsCapitalPropreEngageEntrepriseConjoint())) > 0)) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Contrôle si la cohérence entre l'activité accessoire et le salaire
     */
    public String isCCVSControleActiviteAccessoire(String niveauMsg, String idParam, String description)
            throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        boolean casAControler = false;
        float revenuActviteAccessoire = Float.parseFloat(FWFindParameter.findParameter(getSession()
                .getCurrentThreadTransaction(), "10500130", "REVACTACC", comm.getDebutExercice1(), "", 0));
        // Contrôle contribuable
        if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliation())) {
            // Contrôle si salarié
            boolean salarieProf = false;
            float revenuAgricole = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuAgricoleCtb()));
            float revenuNonAgricole = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuNonAgricoleCtb()));
            float salaire = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsSalairesContribuable()));
            if ((revenuAgricole + revenuNonAgricole) < revenuActviteAccessoire) {
                if (salaire > 12000) {
                    if (salaire >= 24000) {
                        salarieProf = true;
                    } else if (salaire > (6 * (revenuAgricole + revenuNonAgricole))) {
                        salarieProf = true;
                    }
                }
                // Recherche activité acessoire
                boolean activiteAccessoire = AFParticulariteAffiliation.existeParticularite(getSession()
                        .getCurrentThreadTransaction(), getCommunicationRetour().getIdAffiliation(),
                        AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, "01.01."
                                + getCommunicationRetour().getAnnee1());
                if ((activiteAccessoire && !salarieProf) || (salarieProf && !activiteAccessoire)) {
                    casAControler = true;
                }
            }
        }
        // Contrôle conjoint
        if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(getCommunicationRetour().getGenreConjoint())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliationConjoint())) {
            // Contrôle si salarié
            boolean salarieProfCjt = false;
            float revenuAgricoleCjt = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuAgricoleConjoint()));
            float revenuNonAgricoleCjt = Float.parseFloat(JANumberFormatter.deQuote(comm
                    .getVsRevenuNonAgricoleConjoint()));
            float salaireCjt = Float.parseFloat(JANumberFormatter.deQuote(comm.getVsSalairesConjoint()));
            if ((revenuAgricoleCjt + revenuNonAgricoleCjt) < revenuActviteAccessoire) {
                if (salaireCjt > 12000) {
                    if (salaireCjt >= 24000) {
                        salarieProfCjt = true;
                    } else if (salaireCjt > (6 * (revenuAgricoleCjt + revenuNonAgricoleCjt))) {
                        salarieProfCjt = true;
                    }
                }
                // Recherche activité acessoire
                boolean activiteAccessoire = AFParticulariteAffiliation.existeParticularite(getSession()
                        .getCurrentThreadTransaction(), getCommunicationRetour().getIdAffiliationConjoint(),
                        AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, "01.01."
                                + getCommunicationRetour().getAnnee1());
                if ((activiteAccessoire && !salarieProfCjt) || (salarieProfCjt && !activiteAccessoire)) {
                    casAControler = true;
                }
            }
        }
        if (casAControler) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Contrôle si le conjoint dans tiers est le même que celui du fisc Contrôle si le conjoint communiqué est affilié
     * pour les indépendants
     */
    public String isCCVSControleConjoint(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        if (JadeStringUtil.isBlankOrZero(comm.getIdConjoint())
                && !JadeStringUtil.isBlankOrZero(comm.getVsNumAffilieConjoint(0))) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        if (!JadeStringUtil.isBlankOrZero(comm.getIdConjoint()) && (100 != Integer.parseInt(comm.getIdConjoint()))) {
            TIHistoriqueAvsManager avsCjt = new TIHistoriqueAvsManager();
            avsCjt.setSession(getSession());
            avsCjt.setForIdTiers(comm.getIdConjoint());
            avsCjt.setForNumAvs(comm.getVsNumAvsConjoint(1));
            avsCjt.find();
            if (avsCjt.getSize() == 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Contrôle contribuable Erreur si le n° avs du tiers ne correspond pas à celui du fisc
     */
    public String isCCVSControleContribuable(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isEmpty(comm.getIdTiers())) {
            if (!JAStringFormatter.deformatAvs(comm.getTiers().getNumAvsActuel().trim()).equalsIgnoreCase(
                    comm.getVsNumAvsAffilie(0).trim())) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        } else {
            // Si contribuable inconnu (ex caisse externe), regarder si le n° avs du conjoint du fisc == celui du tiers
            if (!JadeStringUtil.isEmpty(comm.getIdConjoint())) {
                if (!JAStringFormatter.deformatAvs(comm.getConjoint().getNumAvsActuel().trim()).equalsIgnoreCase(
                        comm.getVsNumAvsAffilie(0).trim())) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /**
     * erreur si le genre d'affilié de la communication est différent de celui de l'affiliation
     */
    public String isCCVSControleGenreAffilie(String niveauMsg, String idParam, String description) throws Exception {
        ccvsDeterminationGenre();
        isCCVSChangeGenre("", "", "");
        return isControleGenreAffilie(niveauMsg, idParam, description);
    }

    /**
     * erreur si le genre d'affilié de la communication est différent de celui de l'affiliation
     */
    public String isCCVSControleGenreAffilieConjoint(String niveauMsg, String idParam, String description)
            throws Exception {
        ccvsDeterminationGenreConjoint();
        isCCVSChangeGenreConjoint("", "", "");
        return isControleGenreAffilieConjoint(niveauMsg, idParam, description);
    }

    public String isCCVSCumulRevenu(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        comm.setFortune(new FWCurrency(Float.parseFloat(JANumberFormatter.deQuote(comm.getVsFortunePriveeCtb()))
                + Float.parseFloat(JANumberFormatter.deQuote(comm.getVsFortunePriveeConjoint()))).toString());
        comm.setRevenu1(new FWCurrency(Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuNonAgricoleCtb()))
                + Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuNonAgricoleConjoint()))
                + Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuRenteCtb()))
                + Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuRenteConjoint()))).toString());
        comm.setAutreRevenu(new FWCurrency(Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuAgricoleCtb()))
                + Float.parseFloat(JANumberFormatter.deQuote(comm.getVsRevenuAgricoleConjoint()))).toString());
        return "";
    }

    /**
     * 
     * @param niveauMsg
     * @param idParam
     * @param description
     * @return
     * @throws Exception
     */
    public String isCCVSFichierCentral(String niveauMsg, String idParam, String description) throws Exception {
        // On recherche l'affiliation par le numAvs
        TIHistoriqueAvsManager histo = new TIHistoriqueAvsManager();
        histo.setSession(getSession());
        histo.setForNumAvs(NSUtil.formatAVSUnknown(getSedexContribuable().getVn()));
        histo.find();
        for (int i = 0; i < histo.size(); i++) {
            String idContribuable = ((TIHistoriqueAvs) histo.getEntity(i)).getIdTiers();
            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(getSession());
            affiliation = affiliation._retourAffiliation(getSession().getCurrentThreadTransaction(), idContribuable,
                    getCommunicationRetour().getAnnee1(), CodeSystem.TYPE_AFFILI_FICHIER_CENT, 1);
            if (affiliation != null) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Contrôle si le rachat lpp pour conribuable est renseigné
     */
    public String isCCVSRachatLpp(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isIntegerEmpty(comm.getVsRachatLpp())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Contrôle si le rachat lpp pour conribuable est renseigné
     */
    public String isCCVSRachatLppCjt(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isIntegerEmpty(comm.getVsRachatLppCjt())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isCCVSSedexChangeGenre(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourViewBean comm = (CPCommunicationFiscaleRetourViewBean) getCommunicationRetour();
        getCommunicationRetour().setChangementGenre("");
        if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm.getGenreAffilie())
                && !CPDecision.CS_TSE.equalsIgnoreCase(comm.getGenreAffilie())
                && !JadeStringUtil.isEmpty(comm.getIdAffiliation())) {
            // Tests AFI
            // Si revenu agricole > 75% du revenu non agricole => Peut être considéré comme AFI
            boolean AFIPotentiel = false;
            BigDecimal mRevenuAgricole = new BigDecimal(0);
            BigDecimal mRevenuNonAgricole = new BigDecimal(0);
            BigDecimal mRevenu = new BigDecimal(0);
            BigDecimal m75RevenuNonAgricole = new BigDecimal(0);
            BigDecimal m30RevenuNonAgricole = new BigDecimal(0);
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getIncomeFromSelfEmployment())) {
                mRevenu = new BigDecimal(
                        JANumberFormatter.deQuote(getSedexDonneesBases().getIncomeFromSelfEmployment()));
            }
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getMainIncomeInAgriculture())) {
                mRevenuAgricole = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases()
                        .getMainIncomeInAgriculture()));
            }
            // Soustraire le revenu agricole au revenu car celui-ci est compris dedans
            mRevenuNonAgricole = mRevenu.subtract(mRevenuAgricole);
            // Si revenu négatif => mettre à 0
            if (mRevenuNonAgricole.compareTo(new BigDecimal(0)) == -1) {
                mRevenuNonAgricole = new BigDecimal(0);
            }
            if (mRevenuAgricole.compareTo(new BigDecimal(0)) == -1) {
                mRevenuAgricole = new BigDecimal(0);
            }
            m75RevenuNonAgricole = mRevenuNonAgricole;
            m75RevenuNonAgricole = m75RevenuNonAgricole.multiply(new BigDecimal(75));
            m75RevenuNonAgricole = m75RevenuNonAgricole.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
            m30RevenuNonAgricole = mRevenuNonAgricole;
            m30RevenuNonAgricole = m30RevenuNonAgricole.multiply(new BigDecimal(30));
            m30RevenuNonAgricole = m30RevenuNonAgricole.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
            if (mRevenuAgricole.compareTo(m75RevenuNonAgricole) == 1) {
                // Débit > 30%
                AFIPotentiel = true;
            }
            // Si (RNA * 0.30) <= RA <= (RNA*0.75) => pas de changement
            if ((mRevenuAgricole.compareTo(m30RevenuNonAgricole) >= 0)
                    && (mRevenuAgricole.compareTo(m75RevenuNonAgricole) <= 0)) {
                getCommunicationRetour().setChangementGenre("");
            } else {
                // Recherche cotisation AFI
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(getSession());
                AFCotisation cotiAfi = aff._cotisation(getSession().getCurrentThreadTransaction(),
                        getCommunicationRetour().getIdAffiliation(),
                        globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL,
                        globaz.naos.translation.CodeSystem.TYPE_ASS_AFI, "01.01."
                                + getCommunicationRetour().getAnnee1(),
                        "31.12." + getCommunicationRetour().getAnnee1(), 1);
                // Si revenu agricole => test si cotisation AF
                if (AFIPotentiel) {
                    // Si AFI potentiel et pas de cotisation AFI de définie => Message changement Indépendant -> AFI
                    if (cotiAfi == null) {
                        getCommunicationRetour().setChangementGenre(CPCommunicationFiscaleRetourViewBean.CS_IND_AFI);
                    }
                } else {
                    // Si non AFI potentiel et cotisation AFI définie => Message changement AFI -> Indépendant
                    if (cotiAfi != null) {
                        getCommunicationRetour().setChangementGenre(CPCommunicationFiscaleRetourViewBean.CS_AFI_IND);
                    }
                }
            }
            // Avertir qu'il y a eu un changement de genere
            if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getChangementGenre())) {
                // ajouterErreur(idParam);
                // return niveauMsg;
            }
        }
        return "";
    }

    public String isCCVSSedexChangeGenreConjoint(String niveauMsg, String idParam, String description) throws Exception {
        getCommunicationRetour().setChangementGenreConjoint("");
        if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreConjoint())
                && !CPDecision.CS_TSE.equalsIgnoreCase(getCommunicationRetour().getGenreConjoint())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliationConjoint())) {
            // Tests AFI
            // Si revenu agricole > 75% du revenu non agricole => Peut être considéré comme AFI
            boolean AFIPotentiel = false;
            BigDecimal mRevenu = new BigDecimal(0);
            BigDecimal mRevenuAgricole = new BigDecimal(0);
            BigDecimal mRevenuNonAgricole = new BigDecimal(0);
            BigDecimal m75RevenuNonAgricole = new BigDecimal(0);
            BigDecimal m30RevenuNonAgricole = new BigDecimal(0);
            // Conjoint
            if (!JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getIncomeFromSelfEmploymentCjt())) {
                mRevenu = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases()
                        .getIncomeFromSelfEmploymentCjt()));
            }
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getMainIncomeInAgricultureCjt())) {
                mRevenuAgricole = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases()
                        .getMainIncomeInAgricultureCjt()));
            }
            // Soustraire le revenu agricole au revenu total
            mRevenuNonAgricole = mRevenu.subtract(mRevenuAgricole);
            // Si revenu négatif => mettre à 0
            if (mRevenuNonAgricole.compareTo(new BigDecimal(0)) == -1) {
                mRevenuNonAgricole = new BigDecimal(0);
            }
            if (mRevenuAgricole.compareTo(new BigDecimal(0)) == -1) {
                mRevenuAgricole = new BigDecimal(0);
            }
            m75RevenuNonAgricole = mRevenuNonAgricole;
            m75RevenuNonAgricole = m75RevenuNonAgricole.multiply(new BigDecimal(75));
            m75RevenuNonAgricole = m75RevenuNonAgricole.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
            m30RevenuNonAgricole = mRevenuNonAgricole;
            m30RevenuNonAgricole = m30RevenuNonAgricole.multiply(new BigDecimal(30));
            m30RevenuNonAgricole = m30RevenuNonAgricole.divide(new BigDecimal(100), BigDecimal.ROUND_HALF_EVEN);
            if (mRevenuAgricole.compareTo(m75RevenuNonAgricole) == 1) {
                // Débit > 30%
                AFIPotentiel = true;
            }
            // Si (RNA * 0.30) <= RA <= (RNA*0.75) => pas de changement
            if ((mRevenuAgricole.compareTo(m30RevenuNonAgricole) >= 0)
                    && (mRevenuAgricole.compareTo(m75RevenuNonAgricole) <= 0)) {
                getCommunicationRetour().setChangementGenreConjoint("");
            } else {
                // Recherche cotisation AFI
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(getSession());
                AFCotisation cotiAfi = aff._cotisation(getSession().getCurrentThreadTransaction(),
                        getCommunicationRetour().getIdAffiliationConjoint(),
                        globaz.naos.translation.CodeSystem.GENRE_ASS_PERSONNEL,
                        globaz.naos.translation.CodeSystem.TYPE_ASS_AFI, "01.01."
                                + getCommunicationRetour().getAnnee1(),
                        "31.12." + getCommunicationRetour().getAnnee1(), 1);
                // Si revenu agricole => test si cotisation AF
                if (AFIPotentiel) {
                    // Si AFI potentiel et pas de cotisation AFI de définie => Message changement Indépendant -> AFI
                    if (cotiAfi == null) {
                        getCommunicationRetour().setChangementGenreConjoint(
                                CPCommunicationFiscaleRetourViewBean.CS_IND_AFI);
                    }
                } else {
                    // Si non AFI potentiel et cotisation AFI définie => Message changement AFI -> Indépendant
                    if (cotiAfi != null) {
                        getCommunicationRetour().setChangementGenreConjoint(
                                CPCommunicationFiscaleRetourViewBean.CS_AFI_IND);
                    }
                }
            }
            // Avertir qu'il y a eu un changement de genere
            if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getChangementGenreConjoint())) {
                // ajouterErreur(idParam);
                // return niveauMsg;
            }
        }
        return "";
    }

    /**
     * Condition de base indispensable pour la génération pour la CCCVS
     * 
     * @return niveau du message s'il ne remplit pas les condition
     * @throws Exception
     */
    public String isCCVSSedexCondition(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourViewBean comm = (CPCommunicationFiscaleRetourViewBean) getCommunicationRetour();
        boolean ok = false;
        // Recherche du tiers
        comm.setGenreAffilie("");
        comm.setGenreConjoint("");
        TITiersViewBean tiers = new TITiersViewBean();
        AFAffiliation affiliation = null;
        getCommunicationRetour().setTiers(null);
        getCommunicationRetour().setIdTiers("");
        // Pour VS: Pour le conjoint, ne pas se baser sur la liaison dans les
        // tiers mais sur les données du fisc
        // => rechercher à la fois le contribuable et le conjoint selon les
        // données du fisc.
        // Tenir compte du fait que le contribuable peut être affilié ailleurs,
        // c'est à dire que seul le conjoint est au valais.
        tiers.setSession(getSession());
        if (JadeStringUtil.isEmpty(getCommunicationRetour().getMajNumContribuable())) {
            // Recherche variable si mise à jour du numéro de contribuable
            getCommunicationRetour().setMajNumContribuable(
                    ((CPApplication) getSession().getApplication()).getMajNumContribuable());
        }
        String tri = "";
        if ("numAffilie".equalsIgnoreCase(getCommunicationRetour().getJournalRetour().getZoneRecherche())) {
            // formatage du numero si numéro = ancien formatage
            String numAff = "";
            AFAffiliationManager histo = new AFAffiliationManager();
            histo.setSession(getSession());
            if (!JadeStringUtil.isBlankOrZero(getSedexContribuable().getYourBusinessReferenceId())) {
                numAff = getSedexContribuable().getYourBusinessReferenceId();
                tri = numAff;
                histo.setForAffilieNumero(numAff);
                histo.find();
                if (histo.size() > 0) {
                    tiers.setIdTiers(((AFAffiliation) histo.getFirstEntity()).getIdTiers());
                    getCommunicationRetour().setTiers(tiers);
                    getCommunicationRetour().setIdTiers(tiers.getIdTiers());
                    if (getCommunicationRetour().getMajNumContribuable().equalsIgnoreCase("YES")) {
                        tiers.retrieve();
                        // Mise à jour du n° de contribuale si il est différent
                        // et si la caisse le veut (properties
                        // majNumContribuable)
                        if (!tiers.isNew()
                                && !JadeStringUtil.isEmpty(getCommunicationRetour().getNumContribuableRecu())
                                && !tiers.getNumContribuableActuel().equalsIgnoreCase(
                                        getCommunicationRetour().getNumContribuableRecu())) {
                            tiers.setMotifModifContribuable(TIHistoriqueContribuable.CS_CREATION);
                            tiers.setDateModifContribuable(getCommunicationRetour().getJournalRetour()
                                    .getDateReception());
                            tiers.setNumContribuableActuel(getCommunicationRetour().getNumContribuableRecu());
                            tiers.update(getSession().getCurrentThreadTransaction());
                        }
                    }
                    affiliation = CPToolBox
                            .returnAffiliation(getSession(), getSession().getCurrentThreadTransaction(), numAff,
                                    getCommunicationRetour().getAnnee1(), getCommunicationRetour().getGenreAffilie(), 2);

                    if (affiliation != null) {
                        ok = true;
                        getCommunicationRetour().setIdAffiliation(affiliation.getAffiliationId());
                    }
                }
            }
        }
        getCommunicationRetour().setTri(tri);
        // Recherche la période fiscale
        CPPeriodeFiscale periodeFiscale = getCommunicationRetour().getPeriodeFiscale();
        if (periodeFiscale != null) {
            getCommunicationRetour().setIdIfd(periodeFiscale.getIdIfd());
        } else {
            ajouterErreur(idParam);
            getCommunicationRetour().setIdIfd("");
            return niveauMsg;
        }
        // Affiliation
        if (getCommunicationRetour().getTiers() != null) {
            // Recherche de la communication fiscale (utilise IdTiers et IdIfd) - PO 9428
            CPCommunicationFiscale communicationFiscale = getCommunicationRetour().getCommunicationFiscale(1);
            if (communicationFiscale != null) {
                getCommunicationRetour().setIdCommunication(communicationFiscale.getIdCommunication());
            } else {
                getCommunicationRetour().setIdCommunication("");
            }
        }
        if (!ok) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isCCVSSedexConjointIndependant(String niveauMsg, String idParam, String description) throws Exception {
        if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getGenreAffilie())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdConjoint())) {
            // si revenu >1 ou capital >0 ==> le cas doit être analysé pour éventuelle répartition
            BigDecimal mRevenu = new BigDecimal(0);
            BigDecimal mRevenuAgricole = new BigDecimal(0);
            BigDecimal mRevenuNonAgricole = new BigDecimal(0);
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getIncomeFromSelfEmploymentCjt())) {
                mRevenu = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases()
                        .getIncomeFromSelfEmploymentCjt()));
            }
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getMainIncomeInAgricultureCjt())) {
                mRevenuAgricole = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases()
                        .getMainIncomeInAgricultureCjt()));
            }
            // Soustraire le revenu agricole au revenu car celui-ci est compris dedans
            mRevenuNonAgricole = mRevenu.subtract(mRevenuAgricole);
            if ((mRevenuNonAgricole.compareTo(new BigDecimal(1)) == 1)
                    || (mRevenuAgricole.compareTo(new BigDecimal(1)) == 1)
                    || (Integer.parseInt(JANumberFormatter.deQuote(getSedexDonneesBases().getCapitalCjt())) > 0)) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Contrôle si la cohérence entre l'activité accessoire et le salaire
     */
    public String isCCVSSedexControleActiviteAccessoire(String niveauMsg, String idParam, String description)
            throws Exception {
        boolean casAControler = false;
        BigDecimal mRevenu = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases()
                .getIncomeFromSelfEmploymentCjt()));
        BigDecimal salaire = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases().getEmploymentIncome()));

        BigDecimal revenuActiviteAccessoire = new BigDecimal(FWFindParameter.findParameter(getSession()
                .getCurrentThreadTransaction(), "10500130", "REVACTACC", getCommunicationRetour().getDebutExercice1(),
                "", 0));
        // Contrôle contribuable
        if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie())
                && !CPDecision.CS_TSE.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliation())) {
            // Contrôle si salarié
            boolean salarieProf = false;
            if (mRevenu.compareTo(revenuActiviteAccessoire) == -1) {
                if (salaire.compareTo(new BigDecimal(12000)) == 1) {
                    if (salaire.compareTo(new BigDecimal(23999)) == 1) {
                        salarieProf = true;
                    } else if (salaire.compareTo(mRevenu.multiply(new BigDecimal(6))) == 1) {
                        salarieProf = true;
                    }
                }
                // Recherche activité acessoire
                boolean activiteAccessoire = AFParticulariteAffiliation.existeParticularite(getSession()
                        .getCurrentThreadTransaction(), getCommunicationRetour().getIdAffiliation(),
                        AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, "01.01."
                                + getCommunicationRetour().getAnnee1());
                if ((activiteAccessoire && !salarieProf) || (salarieProf && !activiteAccessoire)) {
                    casAControler = true;
                }
            }
        }
        // Contrôle conjoint
        if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreConjoint())
                && !CPDecision.CS_TSE.equalsIgnoreCase(getCommunicationRetour().getGenreConjoint())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliationConjoint())) {
            // Contrôle si salarié
            boolean salarieProfCjt = false;
            BigDecimal revenuCjt = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getIncomeFromSelfEmploymentCjt()));
            BigDecimal salaireCjt = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getEmploymentIncomeCjt()));
            if (mRevenu.compareTo(revenuActiviteAccessoire) == -1) {
                if (salaireCjt.compareTo(new BigDecimal(12000)) == 1) {
                    if (salaireCjt.compareTo(new BigDecimal(23999)) == 1) {
                        salarieProfCjt = true;
                    } else if (salaireCjt.compareTo(revenuCjt.multiply(new BigDecimal(6))) == 1) {
                        salarieProfCjt = true;
                    }
                }
                // Recherche activité acessoire
                boolean activiteAccessoire = AFParticulariteAffiliation.existeParticularite(getSession()
                        .getCurrentThreadTransaction(), getCommunicationRetour().getIdAffiliationConjoint(),
                        AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, "01.01."
                                + getCommunicationRetour().getAnnee1());
                if ((activiteAccessoire && !salarieProfCjt) || (salarieProfCjt && !activiteAccessoire)) {
                    casAControler = true;
                }
            }
        }
        if (casAControler) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Contrôle contribuable Erreur si le n° avs du tiers ne correspond pas à celui du fisc
     */
    public String isCCVSSedexControleContribuable(String niveauMsg, String idParam, String description)
            throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            if (!JAStringFormatter.deformatAvs(getCommunicationRetour().getTiers().getNumAvsActuel().trim())
                    .equalsIgnoreCase(getSedexContribuable().getVn().trim())) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        } else {
            // Si contribuable inconnu (ex caisse externe), regarder si le n° avs du conjoint du fisc == celui du tiers
            if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdConjoint())) {
                TITiersViewBean cjt = new TITiersViewBean();
                cjt.setSession(getSession());
                cjt.setIdTiers(getCommunicationRetour().getIdConjoint());
                cjt.retrieve();
                if (!cjt.isNew()
                        && !JAStringFormatter.deformatAvs(cjt.getNumAvsActuel().trim()).equalsIgnoreCase(
                                getSedexConjoint().getVn().trim())) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /**
     * erreur si le genre d'affilié de la communication est différent de celui de l'affiliation
     */
    public String isCCVSSedexControleGenreAffilie(String niveauMsg, String idParam, String description)
            throws Exception {
        ccvsSedexDeterminationGenre();
        isCCVSSedexChangeGenre("", "", "");
        return isControleGenreAffilie(niveauMsg, idParam, description);
    }

    /**
     * erreur si le genre d'affilié de la communication est différent de celui de l'affiliation
     */
    public String isCCVSSedexControleGenreAffilieConjoint(String niveauMsg, String idParam, String description)
            throws Exception {
        ccvsSedexDeterminationGenreConjoint();
        isCCVSSedexChangeGenreConjoint("", "", "");
        return isControleGenreAffilieConjoint(niveauMsg, idParam, description);
    }

    /**
     * Dtermination du revenu pour CCCVS - Ne pas cumuler les revenus ind et NA
     * 
     * @param niveauMsg
     * @param idParam
     * @param description
     * @return
     * @throws Exception
     */
    public String isCCVSSedexDeterminationRevenu(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourViewBean comm = (CPCommunicationFiscaleRetourViewBean) getCommunicationRetour();
        if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm.getGenreAffilie())
                || CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm.getGenreConjoint())) {
            String renteContribuable = "0";
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getPensionIncome())) {
                renteContribuable = getSedexDonneesBases().getPensionIncome();
            }
            // Si un des conjoints est non actif, cumuler les revenus de rente même si l'autre est indépendant
            String renteConjoint = "0";
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getPensionIncomeCjt())) {
                renteConjoint = getSedexDonneesBases().getPensionIncomeCjt();
            }
            double renteCouple = Double.valueOf(JANumberFormatter.deQuote(renteContribuable)).doubleValue()
                    + Double.valueOf(JANumberFormatter.deQuote(renteConjoint)).doubleValue();
            getCommunicationRetour().setRevenu1(String.valueOf(renteCouple));

        } else {
            String revenuIndependant = "0";
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getIncomeFromSelfEmployment())) {
                revenuIndependant = getSedexDonneesBases().getIncomeFromSelfEmployment();
            }
            String revenuAgricole = getSedexDonneesBases().getMainIncomeInAgriculture();
            if (!JadeStringUtil.isIntegerEmpty(revenuAgricole) && !revenuAgricole.equalsIgnoreCase("null")) {
                double revenuTotal = Double.valueOf(JANumberFormatter.deQuote(revenuIndependant)).doubleValue();
                revenuTotal -= Double.valueOf(JANumberFormatter.deQuote(revenuAgricole)).doubleValue();
                getCommunicationRetour().setRevenu1(String.valueOf(revenuTotal));
            } else {
                getCommunicationRetour().setRevenu1(revenuIndependant);
            }
        }
        // Si conjoint inconnu et non actif => mettre revenu agricole à zéro
        if ((JadeStringUtil.isIntegerEmpty(comm.getGenreAffilie()) && CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm
                .getGenreConjoint()))
                || ((JadeStringUtil.isIntegerEmpty(comm.getGenreConjoint()) || CPDecision.CS_NON_ACTIF
                        .equalsIgnoreCase(comm.getGenreConjoint())) && CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm
                        .getGenreAffilie()))) {
            getCommunicationRetour().setRevenu2("0");
        }
        return "";
    }

    /**
     * Dtermination du revenu pour CCCVS - Ne pas cumuler les revenus ind et NA
     * 
     * @param niveauMsg
     * @param idParam
     * @param description
     * @return
     * @throws Exception
     */
    public String isSedexDeterminationRevenu(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourViewBean comm = (CPCommunicationFiscaleRetourViewBean) getCommunicationRetour();
        if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm.getGenreAffilie())
                || CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm.getGenreConjoint())) {
            String renteContribuable = "0";
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getPensionIncome())) {
                renteContribuable = getSedexDonneesBases().getPensionIncome();
            }
            // Si un des conjoints est non actif, cumuler les revenus de rente même si l'autre est indépendant
            String renteConjoint = "0";
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getPensionIncomeCjt())) {
                renteConjoint = getSedexDonneesBases().getPensionIncomeCjt();
            }
            double renteCouple = Double.valueOf(JANumberFormatter.deQuote(renteContribuable)).doubleValue()
                    + Double.valueOf(JANumberFormatter.deQuote(renteConjoint)).doubleValue();
            getCommunicationRetour().setRevenu1(String.valueOf(renteCouple));

        } else {
            double revenuIndependant = 0;
            double revenuTSE = 0;
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getEmploymentIncome())) {
                revenuTSE = Double.valueOf(JANumberFormatter.deQuote(getSedexDonneesBases().getEmploymentIncome()))
                        .doubleValue();
            }
            if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getIncomeFromSelfEmployment())) {
                revenuIndependant = Double.valueOf(JANumberFormatter.deQuote(getSedexDonneesBases()
                        .getIncomeFromSelfEmployment()));
            }
            double revenuTotal = revenuIndependant + revenuTSE;
            String revenuAgricole = getSedexDonneesBases().getMainIncomeInAgriculture();
            if (!JadeStringUtil.isIntegerEmpty(revenuAgricole) && !revenuAgricole.equalsIgnoreCase("null")) {
                revenuTotal -= Double.valueOf(JANumberFormatter.deQuote(revenuAgricole)).doubleValue();
            }
            getCommunicationRetour().setRevenu1(String.valueOf(revenuTotal));
        }
        // Si conjoint inconnu et non actif => mettre revenu agricole à zéro
        if ((JadeStringUtil.isIntegerEmpty(comm.getGenreAffilie()) && CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm
                .getGenreConjoint()))
                || ((JadeStringUtil.isIntegerEmpty(comm.getGenreConjoint()) || CPDecision.CS_NON_ACTIF
                        .equalsIgnoreCase(comm.getGenreConjoint())) && CPDecision.CS_NON_ACTIF.equalsIgnoreCase(comm
                        .getGenreAffilie()))) {
            getCommunicationRetour().setRevenu2("0");
        }
        return "";
    }

    public String isCCVSSpontanee(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        String numCaisse = getSession().getApplication().getProperty("noCaisseFormate");
        // Ne faire ce test uniquement si caisse affilie <> 23 et nom affilie vide (cas fichier central)
        if (JadeStringUtil.isBlankOrZero(comm.getVsNomAffilie()) // PO 8900
                && numCaisse.equalsIgnoreCase(comm.getVsNoCaisseAgenceAffilie(1))
                && JadeStringUtil.isBlankOrZero(comm.getVsNomConjoint())) {
            return "";
        }

        if (comm.getVsTypeTaxation().equalsIgnoreCase("SP") || comm.getVsTypeTaxation().equalsIgnoreCase("NA")) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /**
     * Retourne true si le tiers a un ci irrecouvrable pour l'année concernée
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isCIIrrecouvrable(String niveauMsg, String idParam, String description) throws Exception {
        // if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())
        // && !JadeStringUtil.isEmpty(getCommunicationRetour().getAnnee1())) {
        // String numAvs = "";
        // TIHistoriqueAvs hist = new TIHistoriqueAvs();
        // hist.setSession(getSession());
        // try {
        // numAvs=hist.findPrevKnownNumAvs(getCommunicationRetour().getIdTiers(),"31.12."+getCommunicationRetour().getAnnee1());
        // if(JadeStringUtil.isEmpty(numAvs)){
        // numAvs=hist.findNextKnownNumAvs(getCommunicationRetour().getIdTiers(),"01.01." +
        // getCommunicationRetour().getAnnee1());
        // }
        // } catch (Exception e) {
        // numAvs="";
        // }
        // // Si aucun n° trouvé dans historique ou NNSS => prendre l'actuel n° avs
        // if(JadeStringUtil.isEmpty(numAvs)){
        // numAvs = getCommunicationRetour().getTiers().getNumAvsActuel();
        // }
        if ((getCommunicationRetour() != null) && !JadeStringUtil.isEmpty(getCommunicationRetour().getNumAvs(0))) {
            CICompteIndividuelUtil ciUtil = new CICompteIndividuelUtil();
            ciUtil.setSession(getSession());
            if (ciUtil.hasIrrecouvrable(getCommunicationRetour().getNumAvs(0), getCommunicationRetour().getAnnee1(),
                    getSession().getCurrentThreadTransaction())) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        // }
        return "";
    }

    /*
     * Retourne niveau du message si il y a un commentaire IFD
     */
    public String isCommentaireIFD(String niveauMsg, String idParam, String description) throws Exception {
        CPCommentaireCommunicationManager comMng = new CPCommentaireCommunicationManager();
        comMng.setSession(getSession());
        comMng.setForIdCommunicationRetour(getCommunicationRetour().getIdRetour());
        comMng.find();
        if (comMng.size() > 0) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * 2ème communication alors que la 1ère communication non traitée Tester à la réception si pour la même affiliation
     * et la même période, il existe une communication en attente.
     */
    public String isCommunicationEnAttente(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourViewBean nouvelleCommunication = (CPCommunicationFiscaleRetourViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isBlankOrZero(nouvelleCommunication.getIdAffiliation())) {
            CPCommunicationFiscaleRetourManager manager = new CPCommunicationFiscaleRetourManager();
            manager.setSession(getSession());
            manager.setWhitAffiliation(true);
            manager.setWhitPavsAffilie(true);
            manager.setForIdAffiliation(nouvelleCommunication.getIdAffiliation());
            manager.setForGenreAffilie(nouvelleCommunication.getGenreAffilie());
            manager.setForIdIfd(nouvelleCommunication.getIdIfd());
            manager.setExceptIdRetour(nouvelleCommunication.getIdRetour());
            manager.setNotInStatus(CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE + ", "
                    + CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
            manager.find();
            if (manager.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        // Test conjoint
        if (!JadeStringUtil.isBlankOrZero(nouvelleCommunication.getIdAffiliationConjoint())) {
            CPCommunicationFiscaleRetourManager manager = new CPCommunicationFiscaleRetourManager();
            manager.setSession(getSession());
            manager.setWhitAffiliationConjoint(true);
            manager.setWhitPavsAffilie(true);
            manager.setForIdAffiliationConjoint(nouvelleCommunication.getIdAffiliationConjoint());
            manager.setForGenreAffilie(nouvelleCommunication.getGenreAffilie());
            manager.setForIdIfd(nouvelleCommunication.getIdIfd());
            manager.setExceptIdRetour(nouvelleCommunication.getIdRetour());
            manager.setNotInStatus(CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE + ", "
                    + CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
            manager.find();
            if (manager.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    public String isCommunicationRectificative(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourViewBean comm = (CPCommunicationFiscaleRetourViewBean) getCommunicationRetour();
        if (comm.getDecisionDeBase().isProvisoireMetier()) {
            return "";
        } else {
            // TODO test si sans changement
            BigDecimal mNewCoti = null;
            BigDecimal mOldCoti = null;
            CPCotisation oldCoti = CPCotisation._returnCotisation(getSession(), comm.getDecisionDeBase()
                    .getIdDecision(), CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
            CPCotisation newCoti = CPCotisation._returnCotisation(getSession(), comm.getDecisionGeneree()
                    .getIdDecision(), CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
            if (oldCoti != null) {
                mOldCoti = new BigDecimal(JANumberFormatter.deQuote(oldCoti.getMontantAnnuel()));
            }
            if (newCoti != null) {
                mNewCoti = new BigDecimal(JANumberFormatter.deQuote(newCoti.getMontantAnnuel()));
                // Test si crédit ( si nouvelle cotisation >0 (mOldCoti==null) ou su nouveau montant > ancien montant
                if (mOldCoti != null) {
                    if (mNewCoti.compareTo(mOldCoti) == 0) {
                        ajouterErreur(idParam);
                        return niveauMsg;
                    }
                } else if (mNewCoti.compareTo(new BigDecimal(0)) == 0) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            return "";
        }
    }

    /**
     * Condition de base indispensable pour la génération - Cette méthode doit obligatoirement être active chez le
     * client. Recherche si le contribuable à une affiliation pour l'année et le genre concernés (initialise l'id
     * affiliation qui sera utilisé pour la génération des décisions)
     * 
     * @return niveau du message si il n'y a pas d'affiliation ou si il en a plusieurs
     * @throws Exception
     */
    public String isConditionDeBase(String niveauMsg, String idParam, String description) throws Exception {
        boolean ok = false;
        // Recherche du tiers
        TITiersViewBean tiers = new TITiersViewBean();
        AFAffiliation affiliation = null;
        getCommunicationRetour().setTiers(null);
        getCommunicationRetour().setIdTiers("");

        // Recherche de la propriété qui indique sur quoi la caisse se base
        // pour retrouver le tiers (Ex: la FER sur le n° d'affilié mais la CCJU sur le n° de contribuable
        // Cette information est propre à la caisse et non au canton.
        // Ex: FER -> n° d'affilié mais il se pourrait qu'une autre caisse de GE se base sur le n° de contribuable (EX:
        // FACO)
        // Par défaut : recherche sur le n° de contribuable¨

        // Recherche sur le n° d'affilié
        boolean trouve = false;
        // Recherche
        tiers.setSession(getSession());
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getValeurChampRecherche())) {
            if (JadeStringUtil.isEmpty(getCommunicationRetour().getMajNumContribuable())) {
                // Recherche variable si mise à jour du numéro de contribuable
                getCommunicationRetour().setMajNumContribuable(
                        ((CPApplication) getSession().getApplication()).getMajNumContribuable());
            }
            if ("numAffilie".equalsIgnoreCase(getCommunicationRetour().getJournalRetour().getZoneRecherche())) {
                // formatage du numero selon caisse pour le numero AVS
                String numAff = "";
                try {
                    TIApplication app = (TIApplication) GlobazSystem.getApplication("PYXIS");
                    IFormatData affilieFormater = app.getAffileFormater();
                    if (affilieFormater != null) {
                        numAff = affilieFormater.format(getCommunicationRetour().getValeurChampRecherche());
                    }
                } catch (Exception ex) {
                    numAff = getCommunicationRetour().getValeurChampRecherche();
                }
                AFAffiliationManager histo = new AFAffiliationManager();
                histo.setSession(getSession());
                histo.setForAffilieNumero(numAff);
                // histo.setForNumAffilie((getCommunicationRetour().getValeurChampRecherche()).substring(0,3)+"."+(getCommunicationRetour().getValeurChampRecherche()).substring(3));
                histo.find();
                if (histo.size() > 0) {
                    tiers.setIdTiers(((AFAffiliation) histo.getFirstEntity()).getIdTiers());
                    affiliation = CPToolBox
                            .returnAffiliation(getSession(), getSession().getCurrentThreadTransaction(), numAff,
                                    getCommunicationRetour().getAnnee1(), getCommunicationRetour().getGenreAffilie(), 2);
                    trouve = true;
                }
            } else if ("numAvs".equalsIgnoreCase(getCommunicationRetour().getJournalRetour().getZoneRecherche())) {
                TIHistoriqueAvsManager histo = new TIHistoriqueAvsManager();
                histo.setSession(getSession());
                histo.setForNumAvs(getCommunicationRetour().getValeurChampRecherche());
                histo.find();
                if (histo.size() > 0) {
                    tiers.setIdTiers(((TIHistoriqueAvs) histo.getFirstEntity()).getIdTiers());
                    affiliation = CPToolBox.returnAffiliation(getSession(), getSession().getCurrentThreadTransaction(),
                            tiers.getIdTiers(), getCommunicationRetour().getAnnee1(), getCommunicationRetour()
                                    .getGenreAffilie(), 1);
                    trouve = true;
                }
            } else {
                TIHistoriqueContribuableManager histo = new TIHistoriqueContribuableManager();
                histo.setSession(getSession());
                histo.setForNumContribuable(getCommunicationRetour().getValeurChampRecherche());
                histo.find();
                for (int i = 0; (i < histo.size()) && !trouve; i++) {
                    tiers.setIdTiers(((TIHistoriqueContribuable) histo.getEntity(i)).getIdTiers());
                    affiliation = CPToolBox.returnAffiliation(getSession(), getSession().getCurrentThreadTransaction(),
                            tiers.getIdTiers(), getCommunicationRetour().getAnnee1(), getCommunicationRetour()
                                    .getGenreAffilie(), 1);
                    if (affiliation != null) {
                        trouve = true;
                    }
                }
            }

            if (trouve) {
                tiers.retrieve();
                // Mise à jour du n° de contribuale si il est différent
                // et si la caisse le veut (properties majNumContribuable)
                if (!tiers.isNew()
                        && getCommunicationRetour().getMajNumContribuable().equalsIgnoreCase("YES")
                        && !JadeStringUtil.isEmpty(getCommunicationRetour().getNumContribuableRecu())
                        && !tiers.getNumContribuableActuel().equalsIgnoreCase(
                                getCommunicationRetour().getNumContribuableRecu())) {
                    // Suppression historique
                    if (!JadeStringUtil.isEmpty(tiers.getIdTiers())) {
                        TIHistoriqueContribuableManager histComMng = new TIHistoriqueContribuableManager();
                        histComMng.setSession(getSession());
                        histComMng.setForIdTiers(tiers.getIdTiers());
                        histComMng.find();
                        for (int i = 0; i < histComMng.size(); i++) {
                            TIHistoriqueContribuable entity = ((TIHistoriqueContribuable) histComMng.getEntity(i));
                            entity.delete(getSession().getCurrentThreadTransaction());
                        }
                    }
                    // Création historique
                    tiers.setMotifModifContribuable(TIHistoriqueContribuable.CS_CREATION);
                    tiers.setDateModifContribuable("01.01." + getCommunicationRetour().getAnnee1());
                    tiers.setNumContribuableActuel(getCommunicationRetour().getNumContribuableRecu());
                    tiers.update(getSession().getCurrentThreadTransaction());
                }
                getCommunicationRetour().setTiers(tiers);
                getCommunicationRetour().setIdTiers(tiers.getIdTiers());
                // Recherche id Conjoint
                TICompositionTiersManager cjt = new TICompositionTiersManager();
                cjt.setSession(getSession());
                // Recherche du conjoint
                // acr: selon directive AVS: Le mariage doit être considéré depuis le début de l'année du mariage
                // (division par deux pour toute l'année).
                // Le divorce doit être considéré depuis le début de l'année du divorce (individuel pour toute l'année).
                // Ce qui signifie que l'on va chercher le conjoint au 31.12 de l'année de la décision
                cjt.setForIdTiersEnfantParent(tiers.getIdTiers());
                cjt.setForTypeLien(TICompositionTiers.CS_CONJOINT);
                cjt.setForDateEntreDebutEtFin("31.12." + getCommunicationRetour().getAnnee1());
                cjt.find();
                if (cjt.size() > 0) { // Formatage conjoint
                    String idCjt = "";
                    if (((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent().equals(tiers.getIdTiers())) {
                        idCjt = ((TICompositionTiers) cjt.getEntity(0)).getIdTiersEnfant();
                    } else {
                        idCjt = ((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent();
                    }
                    getCommunicationRetour().setIdConjoint(idCjt);
                    if (!JadeStringUtil.isIntegerEmpty(idCjt) && (100 != Integer.parseInt(idCjt))) {
                        AFAffiliation affCjt = CPToolBox.returnAffiliation(getSession(), getSession()
                                .getCurrentThreadTransaction(), idCjt, getCommunicationRetour().getAnnee1(), "", 1);
                        if (affCjt != null) {
                            getCommunicationRetour().setIdAffiliationConjoint(affCjt.getAffiliationId());
                        }
                    }
                }
            }
        }

        // Recherche la période fiscale
        CPPeriodeFiscale periodeFiscale = getCommunicationRetour().getPeriodeFiscale();
        if (periodeFiscale != null) {
            getCommunicationRetour().setIdIfd(periodeFiscale.getIdIfd());
        } else {
            ajouterErreur(idParam);
            getCommunicationRetour().setIdIfd("");
            return niveauMsg;
        }
        // Affiliation
        if (getCommunicationRetour().getTiers() != null) {
            // Si affiliation = null -> erreur
            if (affiliation == null) {
                getCommunicationRetour().setIdAffiliation("");
                ajouterErreur(idParam);
                return niveauMsg;
            } else {
                getCommunicationRetour().setIdAffiliation(affiliation.getAffiliationId());
                ok = true;
            }

            // Recherche de la communication fiscale (utilise IdTiers et IdIfd)
            CPCommunicationFiscale communicationFiscale = getCommunicationRetour().getCommunicationFiscale(1);
            if (communicationFiscale != null) {
                getCommunicationRetour().setIdCommunication(communicationFiscale.getIdCommunication());
            } else {
                getCommunicationRetour().setIdCommunication("");
            }
        } else {
            // Erreur tiers inexistant
            getCommunicationRetour().setIdTiers("");
            ajouterErreur(idParam);
            return niveauMsg;
        }
        // Genre affiliation
        if (JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getGenreAffilie())) {
            ajouterErreur(idParam);
            ok = false;
            return niveauMsg;
        }
        // Genre taxation
        if (JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getGenreTaxation())) {
            ajouterErreur(idParam);
            ok = false;
        }
        if (ok) {
            return "";
        } else {
            ajouterErreur(idParam);
            return niveauMsg;
        }

    }

    public String isConjointAvecPlusieursAffiliation(String niveauMsg, String idParam, String description)
            throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdConjoint())) {
            int nb = CPToolBox.returnNbAffiliation(getSession(), getSession().getCurrentThreadTransaction(),
                    getCommunicationRetour().getIdConjoint(), getCommunicationRetour().getAnnee1(), "", 1);
            if (nb > 1) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    public String isContentieux(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdAffiliation())) {
            // Extraction du compte annexe
            String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());
            CACompteAnnexe compte = new CACompteAnnexe();
            compte.setSession(getSession());
            compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compte.setIdRole(role);
            compte.setIdExterneRole(getCommunicationRetour().getAffiliation().getAffilieNumero());
            compte.wantCallMethodBefore(false);
            compte.retrieve();
            if ((compte != null) && !compte.isNew()) {
                if (compte.isCompteAnnexeAvecSectionsPoursuite(true, "")) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /**
     * erreur si le genre d'affilié de la communication est différent de celui de l'affiliation
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isControleGenreAffilie(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdAffiliation())) {
            // Faire la correspondance des genres entre décision et affiliation
            // Indépendant
            if (getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
                    || getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_RENTIER)
                    || getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)) {
                if (!getCommunicationRetour().getAffiliation().getTypeAffiliation()
                        .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP)
                        && !getCommunicationRetour().getAffiliation().getTypeAffiliation()
                                .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // Non- actif
            else if (getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_NON_ACTIF)
                    || getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_ETUDIANT)) {
                if (!getCommunicationRetour().getAffiliation().getTypeAffiliation()
                        .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_NON_ACTIF)
                        && !getCommunicationRetour().getAffiliation().getTypeAffiliation()
                                .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_SELON_ART_1A)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // TSE
            else if (getCommunicationRetour().getGenreAffilie().equalsIgnoreCase(CPDecision.CS_TSE)) {
                if (!getCommunicationRetour().getAffiliation().getTypeAffiliation()
                        .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE)
                        && !getCommunicationRetour().getAffiliation().getTypeAffiliation()
                                .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /**
     * erreur si le genre d'affilié de la communication est différent de celui de l'affiliation
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isControleGenreAffilieConjoint(String niveauMsg, String idParam, String description) throws Exception {
        // Détetmination du genre
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdAffiliationConjoint())) {
            // Faire la correspondance des genres entre décision et affiliation
            // Indépendant
            if (getCommunicationRetour().getGenreConjoint().equalsIgnoreCase(CPDecision.CS_INDEPENDANT)
                    || getCommunicationRetour().getGenreConjoint().equalsIgnoreCase(CPDecision.CS_RENTIER)
                    || getCommunicationRetour().getGenreConjoint().equalsIgnoreCase(CPDecision.CS_AGRICULTEUR)) {
                if (!getCommunicationRetour().getAffiliationConjoint().getTypeAffiliation()
                        .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP)
                        && !getCommunicationRetour().getAffiliationConjoint().getTypeAffiliation()
                                .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // Non- actif
            else if (getCommunicationRetour().getGenreConjoint().equalsIgnoreCase(CPDecision.CS_NON_ACTIF)
                    || getCommunicationRetour().getGenreConjoint().equalsIgnoreCase(CPDecision.CS_ETUDIANT)) {
                if (!getCommunicationRetour().getAffiliationConjoint().getTypeAffiliation()
                        .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_NON_ACTIF)
                        && !getCommunicationRetour().getAffiliationConjoint().getTypeAffiliation()
                                .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_SELON_ART_1A)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // TSE
            else if (getCommunicationRetour().getGenreConjoint().equalsIgnoreCase(CPDecision.CS_TSE)) {
                if (!getCommunicationRetour().getAffiliationConjoint().getTypeAffiliation()
                        .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE)
                        && !getCommunicationRetour().getAffiliationConjoint().getTypeAffiliation()
                                .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /**
     * Message si la nouvelle décision à un montant de rente à 0 alors que celui de la provisoire était différent de 0
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isControleRenteAZeroEtProvisoireDiffZero(String niveauMsg, String idParam, String description)
            throws Exception {
        if (getCommunicationRetour().getDecisionGeneree().isNonActif()
                && (getCommunicationRetour().getDecisionGeneree() != null)
                && (getCommunicationRetour().getDecisionDeBase() != null)) {
            CPDonneesBase donneesDeBase = new CPDonneesBase();
            donneesDeBase.setSession(getSession());
            donneesDeBase.setIdDecision(getCommunicationRetour().getDecisionGeneree().getIdDecision());
            donneesDeBase.retrieve();
            if (JadeStringUtil.isEmpty(donneesDeBase.getRevenu1())
                    || JadeStringUtil.isIntegerEmpty(donneesDeBase.getRevenu1())) {
                CPDonneesBase donneesDeBaseProvisoire = new CPDonneesBase();
                donneesDeBaseProvisoire.setSession(getSession());
                donneesDeBaseProvisoire.setIdDecision(getCommunicationRetour().getDecisionDeBase().getIdDecision());
                donneesDeBaseProvisoire.retrieve();
                if (!JadeStringUtil.isIntegerEmpty(donneesDeBaseProvisoire.getRevenu1())) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /*
     * Test pour a Indépendant marié: situation 1: les chiffres fiscaux font passer la cotisation en dessous du double
     * de la cotis minimale => avertissement pour enquête situation 2: les chiffres fiscaux font passer la cotisation en
     * dessus du double de la cotis. minimale => avertissement pour permettre l'éventuelle annulation de l'affiliation
     * du conjoint comme non actif situation 3: les chiffres fiscaux maintiennent la cotisation en dessous du double de
     * la cotisation minimale => aucun avertissement situation 4: les chiffres fiscaux maintiennent la cotisation en
     * dessus du double de la cotisation minimale => aucun avertissement
     */
    public String isControleSituationAvecConjoint(String niveauMsg, String idParam, String description)
            throws Exception {
        // La décision de base et celle qui est calculé selon la communication sont stockée
        // en variable dans le retour.
        CPDecision decisionGeneree = getCommunicationRetour().getDecisionGeneree();
        // Si conjoint non couvert revient à tester si la lettre pour couple à été renseigné
        if (!decisionGeneree.isNew() && !decisionGeneree.isNonActif()) {
            // test si cotisation < 2* le minimum
            float mCotiIndMinimum = CPTableIndependant.getCotisationMinimum(getSession().getCurrentThreadTransaction(),
                    decisionGeneree.getDebutDecision());
            CPCotisation oldCoti = CPCotisation._returnCotisation(getSession(), getCommunicationRetour()
                    .getDecisionDeBase().getIdDecision(), CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
            float mCotiAnnuel = Float.parseFloat(JANumberFormatter.deQuote(oldCoti.getMontantAnnuel()));
            if (decisionGeneree.getSpecification().equalsIgnoreCase(CPDecision.CS_LETTRE_COUPLE)) {
                // Situation 1: Nouvelle coti < au double du minimum et ancienne coti > au double du minimum
                if (mCotiAnnuel > (mCotiIndMinimum * 2)) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            } else if (!JadeStringUtil.isEmpty(decisionGeneree.getIdConjoint())
                    || Boolean.TRUE.equals(decisionGeneree.getDivision2())) {
                if (mCotiAnnuel < (mCotiIndMinimum * 2)) {
                    // Situation 2: Nouvelle coti > au double du minimum et ancienne coti < au double du minimum
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /**
     * Test si l'affiliation a au moins une cotisation cot. pers.
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isCotisationPersonnelle(String niveauMsg, String idParam, String description) throws Exception {
        if (getCommunicationRetour().getAffiliation() != null) {
            if (!AFAffiliationUtil.hasCotPersActif(getCommunicationRetour().getAffiliation(), "01.01."
                    + getCommunicationRetour().getAnnee1(), "31.12." + getCommunicationRetour().getAnnee1())) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /**
     * Retourne false si il y a une décision en cours d'encodage
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isDecisionEnCours(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdAffiliation(getCommunicationRetour().getIdAffiliation());
            decManager.setForIsActive(Boolean.FALSE);
            decManager.setForIdIfdDefintif(getCommunicationRetour().getIdIfd());
            decManager.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
            decManager.setForExceptIdCommunication(getCommunicationRetour().getIdRetour());
            decManager
                    .setInEtat(CPDecision.CS_VALIDATION + ", " + CPDecision.CS_CREATION + ", " + CPDecision.CS_CALCUL);
            decManager.find();
            if (decManager.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Retourne niveau du message si le dossier n'a pas été taxé (non notifié) => se baser sur le commentaire IFD
     * "DOSSIER_NON_NOTIFIE" créé lors de la lecture du fichier
     */
    public String isDossierNonTaxe(String niveauMsg, String idParam, String description) throws Exception {
        CPCommentaireCommunicationManager comMng = new CPCommentaireCommunicationManager();
        comMng.setSession(getSession());
        comMng.setForIdCommunicationRetour(getCommunicationRetour().getIdRetour());
        comMng.setForIdCommentaire(CPCommentaireCommunication.CS_DOSS_NON_NOTIFIE);
        comMng.find();
        if (comMng.size() > 0) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /**
     * Test si affiliation déjà reçu dans ce lot
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isExistantAffiliationLot(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdAffiliation())) {
            CPCommunicationFiscaleRetourManager mng = new CPCommunicationFiscaleRetourManager();
            mng.setSession(getSession());
            mng.setForIdJournalRetour(getCommunicationRetour().getIdJournalRetour());
            mng.setForIdAffiliation(getCommunicationRetour().getIdAffiliation());
            mng.setExceptIdRetour(getCommunicationRetour().getIdRetour());
            mng.setForIdIfd(getCommunicationRetour().getIdIfd());
            mng.setExceptStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
            mng.find();
            if (mng.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /**
     * Test si tiers déjà reçu dans ce lot
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isExistantTiersLot(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPCommunicationFiscaleRetourManager mng = new CPCommunicationFiscaleRetourManager();
            mng.setSession(getSession());
            mng.setForIdJournalRetour(getCommunicationRetour().getIdJournalRetour());
            mng.setForIdTiers(getCommunicationRetour().getIdTiers());
            mng.setExceptIdRetour(getCommunicationRetour().getIdRetour());
            mng.setForIdIfd(getCommunicationRetour().getIdIfd());
            mng.setExceptStatus(CPCommunicationFiscaleRetourViewBean.CS_ABANDONNE);
            mng.find();
            if (mng.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Retourne niveau du message si la personne a un contentieux (qq soit l'année)
     */
    public String isFERContentieux(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdAffiliation())) {
            // Extraction du compte annexe
            String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());
            CACompteAnnexe compte = new CACompteAnnexe();
            compte.setSession(getSession());
            compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compte.setIdRole(role);
            compte.setIdExterneRole(getCommunicationRetour().getAffiliation().getAffilieNumero());
            compte.wantCallMethodBefore(false);
            compte.retrieve();
            if ((compte != null) && !compte.isNew()) {
                if (compte.isCompteAnnexeAvecSectionsPoursuite(true, getCommunicationRetour().getAnnee1())) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /*
     * Retourne niveau du message si il y a déjà une définitive pour une année et un tiers
     */
    public String isFERTropDePeriode(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())
                && !JadeStringUtil.isEmpty(getCommunicationRetour().getAnnee1())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getCommunicationRetour().getIdTiers());
            decManager.setForIdIfdDefintif(getCommunicationRetour().getAnnee1());
            decManager.find();
            if (((decManager.size() >= 3) && getCommunicationRetour().getAnnee1().equalsIgnoreCase("2001"))
                    || ((decManager.size() >= 2) && !getCommunicationRetour().getAnnee1().equalsIgnoreCase("2001"))) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    public String isFichierCentral(String niveauMsg, String idParam, String description) throws Exception {
        // On recherche d'abord si l'affiliation provient d'une demande (messageId)
        if (JadeStringUtil.isEmpty(getSedexContribuable().getYourBusinessReferenceId())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isFPVDemandeEncours(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourViewBean comm = (CPCommunicationFiscaleRetourViewBean) getCommunicationRetour();
        // Recherche si demande encours pour le tiers
        List<String> statutEnCours = new ArrayList<String>();
        statutEnCours.add(DemandeModifAcompteStatut.A_TRAITER.getValue());
        statutEnCours.add(DemandeModifAcompteStatut.VALIDE.getValue());

        if (EBDemandeModifAcompteEntity.returnNbDemandeInstatusForIdAffiliation(getSession(), comm.getIdAffiliation(),
                statutEnCours) > 0) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Retourne niveau du message si imposition à la source
     */
    public String isGEImpositionSource(String niveauMsg, String idParam, String description) throws Exception {
        CPCommentaireCommunicationManager comMng = new CPCommentaireCommunicationManager();
        comMng.setSession(getSession());
        comMng.setForIdCommunicationRetour(getCommunicationRetour().getIdRetour());
        comMng.setForIdCommentaire(CPCommentaireCommunication.CS_IMPOSITION_SOURCE);
        comMng.find();
        if (comMng.size() > 0) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Retourne niveau du message si non assujetti à l'IBO
     */
    public String isGENonAssujettiIBO(String niveauMsg, String idParam, String description) throws Exception {
        CPCommentaireCommunicationManager comMng = new CPCommentaireCommunicationManager();
        comMng.setSession(getSession());
        comMng.setForIdCommunicationRetour(getCommunicationRetour().getIdRetour());
        comMng.setForIdCommentaire(CPCommentaireCommunication.CS_NON_ASSUJETTI_IBO);
        comMng.find();
        if (comMng.size() > 0) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Retourne niveau du message si non assujetti à l'IFD
     */
    public String isGENonAssujettiIFD(String niveauMsg, String idParam, String description) throws Exception {
        CPCommentaireCommunicationManager comMng = new CPCommentaireCommunicationManager();
        comMng.setSession(getSession());
        comMng.setForIdCommunicationRetour(getCommunicationRetour().getIdRetour());
        comMng.setForIdCommentaire(CPCommentaireCommunication.CS_NON_ASSUJETTI_IFD);
        comMng.find();
        if (comMng.size() > 0) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Retourne niveau du message si obervation (commentaire du fisc)
     */
    public String isGEObservation(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourGEViewBean comm = (CPCommunicationFiscaleRetourGEViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isEmpty(comm.getGeObservations())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /**
     * Retourne niveau de message si il y a une imputation (mise en compte) pour une année et un tiers
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isImputation(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getCommunicationRetour().getIdTiers());
            decManager.setForIsActive(Boolean.TRUE);
            decManager.setForIdIfdDefintif(getCommunicationRetour().getIdIfd());
            decManager.setForTypeDecision(CPDecision.CS_IMPUTATION);
            decManager.find();
            if (decManager.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Retourne niveau du message si il y a déjà une décision et que ce n'est pas une taxation rectificative Décision
     * rectificative = données fiscales différentes de la décision
     */
    public String isJUCondition(String niveauMsg, String idParam, String description) throws Exception {
        // Si décision déjà définitive => erreur
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getCommunicationRetour().getIdTiers());
            decManager.setForIsActive(Boolean.TRUE);
            decManager.setForIdIfdDefintif(getCommunicationRetour().getIdIfd());
            decManager.setInTypeDecision(CPDecision.CS_DEFINITIVE + "," + CPDecision.CS_RECTIFICATION + ","
                    + CPDecision.CS_REDUCTION + "," + CPDecision.CS_REMISE);
            decManager.find();
            if (decManager.size() > 0) {
                CPDecision decision = (CPDecision) decManager.getFirstEntity();
                CPDonneesBase donnee = new CPDonneesBase();
                donnee.setSession(getSession());
                donnee.setIdDecision(decision.getIdDecision());
                donnee.retrieve();
                if (!donnee.isNew()) {
                    // Pour non actif => contrôle sur revenu et fortune
                    if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie())) {
                        if (JANumberFormatter.deQuote(donnee.getRevenu1()).equalsIgnoreCase(
                                JANumberFormatter.deQuote(getCommunicationRetour().getRevenu1()))
                                && JANumberFormatter.deQuote(donnee.getFortuneTotale(1)).equalsIgnoreCase(
                                        JANumberFormatter.deQuote(getCommunicationRetour().getFortune()))) {
                            ajouterErreur(idParam);
                            return niveauMsg;
                        }
                    }
                    // Pour indépendant => contrôle sur revenu et capital
                    else {
                        if (JANumberFormatter.deQuote(donnee.getRevenu1()).equalsIgnoreCase(
                                JANumberFormatter.deQuote(getCommunicationRetour().getRevenu1()))
                                && JANumberFormatter.deQuote(donnee.getCapital()).equalsIgnoreCase(
                                        JANumberFormatter.deQuote(getCommunicationRetour().getCapital()))) {
                            ajouterErreur(idParam);
                            return niveauMsg;
                        }
                    }
                }
            }
        }
        return "";
    }

    public String isLifd(String niveauMsg, String idParam, String description) throws Exception {
        if (!getSedexContribuable().isNew()) {
            // On recherche si on a envoyé la demande pour retrouver l'affiliation
            CPCommunicationFiscaleManager managerCommunication = new CPCommunicationFiscaleManager();
            managerCommunication.setSession(getSession());
            managerCommunication.setForIdMessageSedex(getSedexContribuable().getReferenceMessageId());
            managerCommunication.find();
            if (managerCommunication.size() == 1) {
                CPCommunicationFiscale communication = (CPCommunicationFiscale) managerCommunication.getFirstEntity();
                if (!JadeStringUtil.isEmpty(communication.getOrderscope())
                        && communication.getOrderscope().equals("-1")) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }

            }
        }
        return "";
    }

    /*
     * Retourne niveau du message si les montants sont à zéros
     */
    public String isMontantAZero(String niveauMsg, String idParam, String description) throws Exception {
        if (JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getRevenu1())
                && JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getRevenu2())
                && JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getCapital())
                && JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getFortune())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Contrôle pour les non actif si l'état civil à changé depuis la provoire Tester si il y avait un conjoint ou pas
     * entre la provisoire et la nouvelle décision
     */
    public String isNonActifChangementEtatCivil(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourViewBean comm = (CPCommunicationFiscaleRetourViewBean) getCommunicationRetour();
        if (comm.getGenreTaxation().equalsIgnoreCase(CPDecision.CS_NON_ACTIF)) {
            boolean decisionBaseMarie = false;
            boolean decisionGenereeMarie = false;
            if (comm.getDecisionDeBase().getDivision2().equals(Boolean.TRUE)
                    || !JadeStringUtil.isEmpty(comm.getDecisionDeBase().getIdConjoint())) {
                decisionBaseMarie = true;
            }
            if (comm.getDecisionGeneree().getDivision2().equals(Boolean.TRUE)
                    || !JadeStringUtil.isEmpty(comm.getDecisionGeneree().getIdConjoint())) {
                decisionGenereeMarie = true;
            }
            if (decisionBaseMarie != decisionGenereeMarie) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Retourne niveau du message si la période d'activité du fisc est incomplète Peut avoir 2 période d'exercice -
     * Différencier 0
     */
    public String isPeriodeActiviteIncomplete(String niveauMsg, String idParam, String description) throws Exception {
        int moisDebut1 = 0;
        int moisDebut2 = 0;
        int moisFin1 = 0;
        int moisFin2 = 0;
        // Exercice 1
        if (!JadeStringUtil.isNull(getCommunicationRetour().getDebutExercice1())
                && !JAUtil.isDateEmpty(getCommunicationRetour().getDebutExercice1())) {
            moisDebut1 = JACalendar.getMonth(getCommunicationRetour().getDebutExercice1());
        }
        if (!JadeStringUtil.isNull(getCommunicationRetour().getFinExercice1())
                && !JAUtil.isDateEmpty(getCommunicationRetour().getFinExercice1())) {
            moisFin1 = JACalendar.getMonth(getCommunicationRetour().getFinExercice1());
        }
        // Exercice 2
        if (!JadeStringUtil.isNull(getCommunicationRetour().getDebutExercice2())
                && !JAUtil.isDateEmpty(getCommunicationRetour().getDebutExercice2())) {
            moisDebut2 = JACalendar.getMonth(getCommunicationRetour().getDebutExercice2());
        }
        if (!JadeStringUtil.isNull(getCommunicationRetour().getFinExercice2())
                && !JAUtil.isDateEmpty(getCommunicationRetour().getFinExercice2())) {
            moisFin2 = JACalendar.getMonth(getCommunicationRetour().getFinExercice2());
        }
        if ((moisDebut1 > 2) || (moisDebut2 > 2) || ((moisFin1 != 12) && (moisFin1 != 0))
                || ((moisFin2 != 12) && (moisFin2 != 0))) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isPerteCommerciale(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliation())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getAnnee1())) {
            // recherche si décision n - 1 avait un revenu négatif
            int annee = Integer.parseInt(getCommunicationRetour().getAnnee1()) - 1;
            CPDecisionManager decMng = new CPDecisionManager();
            decMng.setSession(getSession());
            decMng.setForAnneeDecision(Integer.toString(annee));
            decMng.setForIdAffiliation(getCommunicationRetour().getIdAffiliation());
            decMng.setInTypeDecision(CPDecision.CS_DEFINITIVE + ", " + CPDecision.CS_RECTIFICATION);
            decMng.setForIsActive(Boolean.TRUE);
            decMng.find();
            if (decMng.getSize() > 0) {
                CPDecision dec = (CPDecision) decMng.getFirstEntity();
                // recherche du revenu
                CPDonneesBase base = new CPDonneesBase();
                base.setSession(getSession());
                base.setIdDecision(dec.getIdDecision());
                base.retrieve();
                if (!base.isNew() && !JadeStringUtil.isBlankOrZero(base.getRevenu1())) {
                    float revenu = Float.parseFloat(JANumberFormatter.deQuote(base.getRevenu1()));
                    if (revenu < 0) {
                        ajouterErreur(idParam);
                        return niveauMsg;
                    }
                }
            }
        }
        return "";
    }

    /**
     * Contrôle si la décision actuelle a été encodée avec les DIN 1181
     * (Cptisation minimum payée en tant que salarié
     * 
     * @param niveauMsg
     * @param idParam
     * @param description
     * @return
     * @throws Exception
     */
    public String isDIN1181(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliation())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getAnnee1())) {
            // recherche décision en cours
            CPDecisionManager decMng = new CPDecisionManager();
            decMng.setSession(getSession());
            decMng.setForIdAffiliation(getCommunicationRetour().getIdAffiliation());
            decMng.setForAnneeDecision(getCommunicationRetour().getAnnee1());
            decMng.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
            decMng.setForIsActive(Boolean.TRUE);
            decMng.find();
            if (decMng.getSize() > 0) {
                CPDecision dec = (CPDecision) decMng.getFirstEntity();
                // Controle si Cotisation paye en tant que salsairé (DIN 1181)
                if (!dec.isNew() && Boolean.TRUE.equals(dec.getCotiMinimumPayeEnSalarie())) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /**
     * Retourne false si il y a plusieurs décisions d'active pour une année et pour un tiers. niveauMsg=1 => error
     * niveauMsg=2 => warning
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isPlusieursDecisions(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getCommunicationRetour().getIdTiers());
            decManager.setForIsActive(Boolean.TRUE);
            decManager.setForIdIfdDefintif(getCommunicationRetour().getIdIfd());
            decManager.setForExceptTypeDecision(CPDecision.CS_IMPUTATION);
            decManager.find();
            if (decManager.size() > 1) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Retourne niveau de message si il y a déjà une définitive pour une année et un tiers
     */
    public String isPresenceDefinitive(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getCommunicationRetour().getIdTiers());
            decManager.setForIsActive(Boolean.TRUE);
            decManager.setForIdIfdDefintif(getCommunicationRetour().getIdIfd());
            decManager.setInTypeDecision(CPDecision.CS_DEFINITIVE + "," + CPDecision.CS_RECTIFICATION + ","
                    + CPDecision.CS_REDUCTION + "," + CPDecision.CS_REMISE);
            decManager.find();
            if (decManager.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    public String isPresenceDefinitive2006(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getIdTiers())
                || !JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getIdConjoint())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getIdTiers())) {
                decManager.setForIdTiers(getCommunicationRetour().getIdTiers());
            } else {
                decManager.setForIdTiers(getCommunicationRetour().getIdConjoint());
            }
            decManager.setForIsActive(Boolean.TRUE);
            decManager.setForIdIfdDefintif(getCommunicationRetour().getIdIfd());
            decManager.setInTypeDecision(CPDecision.CS_DEFINITIVE + "," + CPDecision.CS_RECTIFICATION);
            decManager.find();
            // Test s'il y adéjà eu une définitive
            if (decManager.size() > 0) {
                ajouterErreur(idParam);
                return niveauMsg;
            } else {
                // Test s'il n'y aucune décision
                CPDecisionManager decManager1 = new CPDecisionManager();
                decManager1.setSession(getSession());
                if (!JadeStringUtil.isIntegerEmpty(getCommunicationRetour().getIdTiers())) {
                    decManager1.setForIdTiers(getCommunicationRetour().getIdTiers());
                } else {
                    decManager1.setForIdTiers(getCommunicationRetour().getIdConjoint());
                }
                decManager1.setForIdIfdDefintif(getCommunicationRetour().getIdIfd());
                decManager1.find();
                if (decManager1.size() == 0) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /**
     * Test si il y a une décision de réduction pour l'année concernée
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isPresenceReduction(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getCommunicationRetour().getIdTiers());
            decManager.setForIdIfdDefintif(getCommunicationRetour().getIdIfd());
            decManager.setForTypeDecision(CPDecision.CS_REDUCTION);
            decManager.find();
            if (decManager.size() > 1) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    };

    /**
     * Test si il y a une décision de remise pour l'année concernée
     * 
     * @param niveau
     *            de message
     * @param id
     *            plausibilite
     * @param message
     *            d'erreur
     * @return niveau du message si condition remplie sinon blanc
     */
    public String isPresenceRemise(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getCommunicationRetour().getIdTiers());
            decManager.setForIdIfdDefintif(getCommunicationRetour().getIdIfd());
            decManager.setForTypeDecision(CPDecision.CS_REMISE);
            decManager.find();
            if (decManager.size() > 1) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }/*
      * Retourne niveau de message si conjoint en âge AVS dans situation familiale
      */

    public String isPrestationRenteAvs(String niveauMsg, String idParam, String description) throws Exception {
        // Tester si potentielle rente avs pour non actif - valable depuis 2011
        int anneeCommunication = Integer.parseInt(getCommunicationRetour().getAnnee1());
        int age = 0;
        if ((anneeCommunication >= 2011)
                && (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie()) || CPDecision.CS_NON_ACTIF
                        .equalsIgnoreCase(getCommunicationRetour().getGenreConjoint()))) {
            // Ne pas regarder dans la situation familiale si un lien conjoint est défini dans tiers
            if (haveLinkedSpouse() == false) {
                // init du context pour nouvelle persistance qui est utilisée par l'interface d'HERA
                BSessionUtil.initContext(getSession(), this);
                try {
                    // contrôle âge contribuable
                    if ((getCommunicationRetour().getTiers() != null)
                            && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getTiers().getNumAvsActuel())) {

                        String dateNaissanceCjt = HeraServiceLocator.getRelationConjointService()
                                .getDateNaissanceConjointForDate(getCommunicationRetour().getTiers().getNumAvsActuel(),
                                        "31.12." + anneeCommunication);
                        if (!JadeStringUtil.isBlankOrZero(dateNaissanceCjt)) {
                            age = anneeCommunication - JACalendar.getYear(dateNaissanceCjt);
                            // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                            if ((getCommunicationRetour().getTiers().getSexe()
                                    .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                                    || (getCommunicationRetour().getTiers().getSexe()
                                            .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                                ajouterErreur(idParam);
                                return niveauMsg;
                            }
                        }
                    }

                    // Cas ou le contribuable est inconnu à la caisse (demande pour le conjoint)
                    // => regarder directement dans les information de la communication
                    else if (!JadeStringUtil.isBlankOrZero(getSedexContribuable().getVn())) {
                        String dateNaissanceCjt = HeraServiceLocator.getRelationConjointService()
                                .getDateNaissanceConjointForDate(getSedexContribuable().getVn(),
                                        "31.12." + anneeCommunication);
                        if (!JadeStringUtil.isBlankOrZero(dateNaissanceCjt)) {
                            age = anneeCommunication - JACalendar.getYear(dateNaissanceCjt);
                            if (((age >= 62) && getSedexContribuable().getSex().equalsIgnoreCase("1"))
                                    || ((age >= 63) && getSedexContribuable().getSex().equalsIgnoreCase("2"))) {
                                ajouterErreur(idParam);
                                return niveauMsg;
                            }
                        }
                    }

                    // contrôle si conjoint (Exemple: nécessaire pour couple marié dont l'homme est inconnu)
                    if ((getCommunicationRetour().getConjoint() != null)
                            && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getConjoint().getNumAvsActuel())) {
                        String dateNaissanceCjt = HeraServiceLocator.getRelationConjointService()
                                .getDateNaissanceConjointForDate(
                                        getCommunicationRetour().getConjoint().getNumAvsActuel(),
                                        "31.12." + anneeCommunication);
                        if (!JadeStringUtil.isBlankOrZero(dateNaissanceCjt)) {
                            age = anneeCommunication - JACalendar.getYear(dateNaissanceCjt);
                            // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                            if ((getCommunicationRetour().getConjoint().getSexe()
                                    .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                                    || (getCommunicationRetour().getConjoint().getSexe()
                                            .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                                ajouterErreur(idParam);
                                return niveauMsg;
                            }
                        }

                        // Cas ou le contribuable est inconnu à la caisse (demande pour le conjoint)
                        // => regarder directement dans les information de la communication
                    } else if (!JadeStringUtil.isBlankOrZero(getSedexConjoint().getVn())) {
                        String dateNaissanceCjt = HeraServiceLocator.getRelationConjointService()
                                .getDateNaissanceConjointForDate(getSedexConjoint().getVn(),
                                        "31.12." + anneeCommunication);
                        if (!JadeStringUtil.isBlankOrZero(dateNaissanceCjt)) {
                            age = anneeCommunication - JACalendar.getYear(getSedexConjoint().getYearMonthDay());
                            if (((age >= 62) && getSedexConjoint().getSex().equalsIgnoreCase("1"))
                                    || ((age >= 63) && getSedexConjoint().getSex().equalsIgnoreCase("2"))) {
                                ajouterErreur(idParam);
                                return niveauMsg;
                            }
                        }
                    }
                } finally {
                    // clore le context utilisé pour nouvelle persistance qui est utilisée par l'interface d'HERA
                    BSessionUtil.stopUsingContext(this);
                }
            }
        }
        // clore le context utilisé pour nouvelle persistance qui est utilisée par l'interface d'HERA
        BSessionUtil.stopUsingContext(this);
        return "";
    }

    /*
     * Retourne niveau du message si c'est une rectificative
     */
    public String isRectificative(String niveauMsg, String idParam, String description) throws Exception {
        if (CPCommentaireCommunication.CS_GENRE_TDR.equalsIgnoreCase(getCommunicationRetour().getGenreTaxation())
                || CPCommentaireCommunication.CS_GENRE_TOR
                        .equalsIgnoreCase(getCommunicationRetour().getGenreTaxation())
                || CPCommentaireCommunication.CS_GENRE_TPR
                        .equalsIgnoreCase(getCommunicationRetour().getGenreTaxation())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isReductionCotisation(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourViewBean comm = (CPCommunicationFiscaleRetourViewBean) getCommunicationRetour();
        if (comm.getAffiliation() != null) {
            if (!JadeStringUtil.isEmpty(comm.getAffiliation().getAffilieNumero())) {
                AFAssuranceManager manager = new AFAssuranceManager();
                manager.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
                manager.setForGenreAssurance(CodeSystem.GENRE_ASS_PERSONNEL);
                manager.setSession(getSession());
                manager.find();
                if (manager.size() > 0) {
                    AFAssurance assurance = (AFAssurance) manager.getFirstEntity();
                    String rubExterne = assurance.getParametreAssuranceValeur(CodeSystem.GEN_PARAM_ASS_REDUCTION,
                            "01.01." + comm.getAnnee1(), "");
                    if (!JadeStringUtil.isNull(rubExterne) && !JadeStringUtil.isEmpty(rubExterne)) {
                        // recherche id Rubrique
                        CARubrique rub = new CARubrique();
                        rub.setSession(getSession());
                        rub.setIdExterne(rubExterne);
                        rub.setAlternateKey(APIRubrique.AK_IDEXTERNE);
                        rub.retrieve();
                        if (!rub.isNew()) {
                            CACompteAnnexe compte = new CACompteAnnexe();
                            compte.setSession(getSession());
                            compte.setAlternateKey(1);
                            compte.setIdRole(CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(
                                    getSession().getApplication()));
                            compte.setIdExterneRole(comm.getAffiliation().getAffilieNumero());
                            compte.wantCallMethodBefore(false);
                            compte.retrieve(getSession().getCurrentThreadTransaction());
                            if (!compte.isNew()
                                    && !JadeStringUtil.isIntegerEmpty(CPToolBox.rechMontantFacture(getSession(),
                                            getSession().getCurrentThreadTransaction(), compte.getIdCompteAnnexe(),
                                            rub.getIdRubrique(), comm.getAnnee1()))) {
                                ajouterErreur(idParam);
                                return niveauMsg;
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    public String isRenteAvs(String niveauMsg, String idParam, String description) throws Exception {
        // InforomD0015
        // Tester si potentielle rente avs pour non actif - valable depuis 2011
        int anneeCommunication = Integer.parseInt(getCommunicationRetour().getAnnee1());
        int age = 0;
        if ((anneeCommunication >= 2011)
                && (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie()) || CPDecision.CS_NON_ACTIF
                        .equalsIgnoreCase(getCommunicationRetour().getGenreConjoint()))) {
            // contrôle âge contribuable
            if ((getCommunicationRetour().getTiers() != null)
                    && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getTiers().getDateNaissance())) {
                age = anneeCommunication - JACalendar.getYear(getCommunicationRetour().getTiers().getDateNaissance());
                // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                if ((getCommunicationRetour().getTiers().getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                        || (getCommunicationRetour().getTiers().getSexe()
                                .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // Cas ou le contribuable est inconnu à la caisse (demande pour le conjoint)
            // => regarder directement dans les information de la communication
            else if (!JadeStringUtil.isBlankOrZero(getSedexContribuable().getYearMonthDay())) {
                age = anneeCommunication - JACalendar.getYear(getSedexContribuable().getYearMonthDay());
                if (((age >= 62) && getSedexContribuable().getSex().equalsIgnoreCase("1"))
                        || ((age >= 63) && getSedexContribuable().getSex().equalsIgnoreCase("2"))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // contrôle âge conjoint
            if ((getCommunicationRetour().getConjoint() != null)
                    && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getConjoint().getDateNaissance())) {
                age = anneeCommunication
                        - JACalendar.getYear(getCommunicationRetour().getConjoint().getDateNaissance());
                // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                if ((getCommunicationRetour().getConjoint().getSexe()
                        .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                        || (getCommunicationRetour().getConjoint().getSexe()
                                .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // Cas ou le contribuable est inconnu à la caisse (demande pour le conjoint)
            // => regarder directement dans les information de la communication
            else if (!JadeStringUtil.isBlankOrZero(getSedexConjoint().getYearMonthDay())) {
                age = anneeCommunication - JACalendar.getYear(getSedexConjoint().getYearMonthDay());
                if (((age >= 62) && getSedexConjoint().getSex().equalsIgnoreCase("1"))
                        || ((age >= 63) && getSedexConjoint().getSex().equalsIgnoreCase("2"))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // Recherche de l'éventuel conjoint dans tiers
            String idRecherche = "";
            // cas ou le contribuable est connu => recherche de son conjoint
            if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
                idRecherche = getCommunicationRetour().getIdTiers();
            } else if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdConjoint())) {
                // cas ou seul l'épouse est connue à la caisse => dans ce cas le fisc renvoie les données sous
                // conjoint...
                idRecherche = getCommunicationRetour().getIdConjoint();
            }
            // Recherche lien de mariage
            if (!JadeStringUtil.isBlankOrZero(idRecherche)) {
                TICompositionTiersManager lien = new TICompositionTiersManager();
                lien.setSession(getSession());
                lien.setForIdTiersEnfantParent(idRecherche);
                lien.setForTypeLien(TICompositionTiers.CS_CONJOINT);
                lien.setForDateEntreDebutEtFin("31.12." + getCommunicationRetour().getAnnee1());
                lien.find();
                if (lien.size() > 0) { // Formatage conjoint
                    TITiersViewBean conjoint = new TITiersViewBean();
                    conjoint.setSession(getSession());
                    if (((TICompositionTiers) lien.getEntity(0)).getIdTiersParent().equals(idRecherche)) {
                        conjoint.setIdTiers(((TICompositionTiers) lien.getEntity(0)).getIdTiersEnfant());
                    } else {
                        conjoint.setIdTiers(((TICompositionTiers) lien.getEntity(0)).getIdTiersParent());
                    }
                    // Ne pas tester si conjoint inconnu
                    if (Integer.parseInt(conjoint.getIdTiers()) != 100) {
                        conjoint.retrieve();
                        age = anneeCommunication - JACalendar.getYear(conjoint.getDateNaissance());
                        // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                        if ((conjoint.getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                                || (conjoint.getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                            ajouterErreur(idParam);
                            return niveauMsg;
                        }
                    }
                }
            }
        }

        return "";
    }

    public String isRenteAvsAutre(String niveauMsg, String idParam, String description) throws Exception {
        // InforomD0015
        // Tester si potentielle rente avs pour non actif - valable depuis 2011
        int anneeCommunication = Integer.parseInt(getCommunicationRetour().getAnnee1());
        int age = 0;
        if ((anneeCommunication >= 2011)
                && (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie()) || CPDecision.CS_NON_ACTIF
                        .equalsIgnoreCase(getCommunicationRetour().getGenreConjoint()))) {
            // contrôle âge contribuable
            if ((getCommunicationRetour().getTiers() != null)
                    && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getTiers().getDateNaissance())) {
                age = anneeCommunication - JACalendar.getYear(getCommunicationRetour().getTiers().getDateNaissance());
                // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                if ((getCommunicationRetour().getTiers().getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                        || (getCommunicationRetour().getTiers().getSexe()
                                .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // contrôle âge conjoint
            else if ((getCommunicationRetour().getConjoint() != null)
                    && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getConjoint().getDateNaissance())) {
                age = anneeCommunication
                        - JACalendar.getYear(getCommunicationRetour().getConjoint().getDateNaissance());
                // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                if ((getCommunicationRetour().getConjoint().getSexe()
                        .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                        || (getCommunicationRetour().getConjoint().getSexe()
                                .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // Recherche de l'éventuel conjoint dans tiers
            String idRecherche = "";
            // cas ou le contribuable est connu => recherche de son conjoint
            if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
                idRecherche = getCommunicationRetour().getIdTiers();
            } else if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdConjoint())) {
                // cas ou seul l'épouse est connue à la caisse => dans ce cas le fisc renvoie les données sous
                // conjoint...
                idRecherche = getCommunicationRetour().getIdConjoint();
            }
            // Recherche lien de mariage
            if (!JadeStringUtil.isBlankOrZero(idRecherche)) {
                TICompositionTiersManager lien = new TICompositionTiersManager();
                lien.setSession(getSession());
                lien.setForIdTiersEnfantParent(idRecherche);
                lien.setForTypeLien(TICompositionTiers.CS_CONJOINT);
                lien.setForDateEntreDebutEtFin("31.12." + getCommunicationRetour().getAnnee1());
                lien.find();
                if (lien.size() > 0) { // Formatage conjoint
                    TITiersViewBean conjoint = new TITiersViewBean();
                    conjoint.setSession(getSession());
                    if (((TICompositionTiers) lien.getEntity(0)).getIdTiersParent().equals(idRecherche)) {
                        conjoint.setIdTiers(((TICompositionTiers) lien.getEntity(0)).getIdTiersEnfant());
                    } else {
                        conjoint.setIdTiers(((TICompositionTiers) lien.getEntity(0)).getIdTiersParent());
                    }
                    // Ne pas tester si conjoint inconnu
                    if (Integer.parseInt(conjoint.getIdTiers()) != 100) {
                        conjoint.retrieve();
                        age = anneeCommunication - JACalendar.getYear(conjoint.getDateNaissance());
                        // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                        if ((conjoint.getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                                || (conjoint.getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                            ajouterErreur(idParam);
                            return niveauMsg;
                        }
                    }
                }
            }
        }

        return "";
    }

    public String isRenteAvsVs(String niveauMsg, String idParam, String description) throws Exception {
        // InforomD0015
        // Tester si potentielle rente avs pour non actif - valable depuis 2011
        int anneeCommunication = Integer.parseInt(getCommunicationRetour().getAnnee1());
        int age = 0;
        CPCommunicationFiscaleRetourVSViewBean comm = (CPCommunicationFiscaleRetourVSViewBean) getCommunicationRetour();
        if ((anneeCommunication >= 2011)
                && (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie()) || CPDecision.CS_NON_ACTIF
                        .equalsIgnoreCase(getCommunicationRetour().getGenreConjoint()))) {
            // contrôle âge contribuable
            if ((getCommunicationRetour().getTiers() != null)
                    && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getTiers().getDateNaissance())) {
                age = anneeCommunication - JACalendar.getYear(getCommunicationRetour().getTiers().getDateNaissance());
                // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                if ((getCommunicationRetour().getTiers().getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                        || (getCommunicationRetour().getTiers().getSexe()
                                .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // Cas ou le contribuable est inconnu à la caisse (demande pour le conjoint)
            // => regarder directement dans les information de la communication
            if (!JadeStringUtil.isBlankOrZero(comm.getVsDateNaissanceCtb())) {
                age = anneeCommunication - JACalendar.getYear(comm.getVsDateNaissanceCtb());
                if (((age >= 62) && comm.getVsSexeCtb().equalsIgnoreCase("1"))
                        || ((age >= 63) && comm.getVsSexeCtb().equalsIgnoreCase("2"))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // contrôle âge conjoint
            if ((getCommunicationRetour().getConjoint() != null)
                    && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getConjoint().getDateNaissance())) {
                age = anneeCommunication
                        - JACalendar.getYear(getCommunicationRetour().getConjoint().getDateNaissance());
                // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                if ((getCommunicationRetour().getConjoint().getSexe()
                        .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                        || (getCommunicationRetour().getConjoint().getSexe()
                                .equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // Cas ou le contribuable est inconnu à la caisse (demande pour le conjoint)
            // => regarder directement dans les information de la communication
            else if (!JadeStringUtil.isBlankOrZero(comm.getVsNumAffilieConjoint(0))) {
                age = anneeCommunication - JACalendar.getYear(comm.getVsDateNaissanceConjoint());
                // Au fisc VS: le conjoint est forcément une femme, il n'y a pas de code sexe communiqué
                if (age >= 63) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
            // Recherche de l'éventuel conjoint dans tiers
            String idRecherche = "";
            // cas ou le contribuable est connu => recherche de son conjoint
            if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdTiers())) {
                idRecherche = getCommunicationRetour().getIdTiers();
            } else if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdConjoint())) {
                // cas ou seul l'épouse est connue à la caisse => dans ce cas le fisc renvoie les données sous
                // conjoint...
                idRecherche = getCommunicationRetour().getIdConjoint();
            }
            // Recherche lien de mariage
            if (!JadeStringUtil.isBlankOrZero(idRecherche)) {
                TICompositionTiersManager lien = new TICompositionTiersManager();
                lien.setSession(getSession());
                lien.setForIdTiersEnfantParent(idRecherche);
                lien.setForTypeLien(TICompositionTiers.CS_CONJOINT);
                lien.setForDateEntreDebutEtFin("31.12." + getCommunicationRetour().getAnnee1());
                lien.find();
                if (lien.size() > 0) { // Formatage conjoint
                    TITiersViewBean conjoint = new TITiersViewBean();
                    conjoint.setSession(getSession());
                    if (((TICompositionTiers) lien.getEntity(0)).getIdTiersParent().equals(idRecherche)) {
                        conjoint.setIdTiers(((TICompositionTiers) lien.getEntity(0)).getIdTiersEnfant());
                    } else {
                        conjoint.setIdTiers(((TICompositionTiers) lien.getEntity(0)).getIdTiersParent());
                    }
                    conjoint.retrieve();
                    age = anneeCommunication - JACalendar.getYear(conjoint.getDateNaissance());
                    // Si homme et age>=62 ou femme et age >=63 ans => Message pour vérifier rente AVS
                    if ((conjoint.getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME) && (age >= 62))
                            || (conjoint.getSexe().equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_FEMME) && (age >= 63))) {
                        ajouterErreur(idParam);
                        return niveauMsg;
                    }
                }
            }
        }

        return "";
    }

    /*
     * Communication RI, PC, décès, départ à l'étranger Si particularité = benef PC, Refugié, assisté ou si motif fin =
     * décès ou départ étranger
     */
    public String isRiPcDecesDepart(String niveauMsg, String idParam, String description) throws Exception {
        CPCommunicationFiscaleRetourVDViewBean comm = (CPCommunicationFiscaleRetourVDViewBean) getCommunicationRetour();
        if (!JadeStringUtil.isBlankOrZero(comm.getIdTiers())) {
            // Recherche des affiliations actives pour l'année de la communication
            String[] typeAffiliation = new String[5];
            typeAffiliation[0] = CodeSystem.TYPE_AFFILI_INDEP_EMPLOY;
            typeAffiliation[1] = CodeSystem.TYPE_AFFILI_INDEP;
            typeAffiliation[2] = CodeSystem.TYPE_AFFILI_NON_ACTIF;
            typeAffiliation[3] = CodeSystem.TYPE_AFFILI_TSE;
            typeAffiliation[4] = CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE;
            AFAffiliationManager affMng = new AFAffiliationManager();
            affMng.setSession(getSession());
            affMng.setForIdTiers(comm.getIdTiers());
            affMng.setForTypeAffiliation(typeAffiliation);
            // affMng.setForDateDebutAffLowerOrEqualTo("31.12." + comm.getAnnee1());
            // affMng.setFromDateFin("02.01." + comm.getAnnee1());
            affMng.find();
            for (int i = 0; i < affMng.size(); i++) {
                AFAffiliation aff1 = (AFAffiliation) affMng.getEntity(i);
                if (CPToolBox.isAffilieAssiste(getSession().getCurrentThreadTransaction(), aff1,
                        "01.01." + comm.getAnnee1(), "31.12." + comm.getAnnee1())) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
                if (aff1.getMotifFin().equals("803006") || aff1.getMotifFin().equals("803013")
                        || aff1.getMotifFin().equals("19150004") || aff1.getMotifFin().equals("19130020")) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    public String isSedexAGRIVITCommunicationIncomplete(String niveauMsg, String idParam, String description)
            throws Exception {
        String revAgr = CPUtil.getRemarkInfo(getCommunicationRetour().getIdRetour(),
                CPCommunicationFiscaleRetourSEDEXViewBean.REMARK_REV_NET, getSession());
        if (JadeStringUtil.isEmpty(revAgr) || "0".equalsIgnoreCase(revAgr)) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isSedexCodeRectifManquant(String niveauMsg, String idParam, String description) throws Exception {
        /*
         * CPSedexContribuable contribuable = new CPSedexContribuable(); contribuable.setSession(getSession());
         * contribuable.setIdRetour(getCommunicationRetour().getIdRetour()); contribuable.setAlternateKey(1);
         * contribuable.retrieve(); if (!"4".equalsIgnoreCase(contribuable.getReportType())) { // Si code <> rectif =>
         * ok CPCommunicationFiscaleRetourVDManager manager = new CPCommunicationFiscaleRetourVDManager();
         * manager.setSession(getSession()); manager.setWhitAffiliation(true); manager.setWhitPavsAffilie(true); if
         * (!JadeStringUtil.isEmpty(getCommunicationRetour().getNumAffilieRecu())) {
         * manager.setForNumAffilie(getCommunicationRetour().getNumAffilieRecu()); } if
         * (!JadeStringUtil.isEmpty(getCommunicationRetour().getNumAvs(0))) {
         * manager.setForNumAvs(getCommunicationRetour().getNumAvs(0)); }
         * manager.setForGenreAffilie(getCommunicationRetour().getGenreAffilie());
         * manager.setForIdIfd(getCommunicationRetour().getIdIfd());
         * manager.setExceptIdRetour(getCommunicationRetour().getIdRetour()); if (manager.getCount() > 0) {
         * ajouterErreur(idParam); return niveauMsg; } }
         */
        return "";
    }

    public String isSedexCommentaire(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getSedexContribuable().getRemark())) {
            if (!getSedexContribuable().getRemark().startsWith("|")) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    public String isSedexCommunicationsIdentiques(String niveauMsg, String idParam, String description)
            throws Exception {
        String numAffilie = getSedexContribuable().getYourBusinessReferenceId();
        String nss = getSedexContribuable().getVn();
        // CPCommunicationFiscaleRetourVDViewBean nouvelleCommunication = (CPCommunicationFiscaleRetourVDViewBean)
        // getCommunicationRetour();
        if ((JadeStringUtil.isEmpty(numAffilie) && JadeStringUtil.isEmpty(nss))
                || (JadeStringUtil.isEmpty(getCommunicationRetour().getPeriodeFiscale().toString()))) {
            // On ne peut pas retrouver la communication -> pas d'erreur
            return "";
        } else {
            CPCommunicationFiscaleRetourManager manager = new CPCommunicationFiscaleRetourManager();
            manager.setSession(getSession());
            manager.setWhitAffiliation(true);
            manager.setWhitPavsAffilie(true);
            if (!JadeStringUtil.isEmpty(nss)) {
                manager.setForNumAvs(NSUtil.formatAVSUnknown(nss));
            } else if (!JadeStringUtil.isEmpty(numAffilie)) {
                manager.setForNumAffilie(numAffilie);
            }

            // manager.setForGenreAffilie(nouvelleCommunication.getGenreAffilie());
            manager.setForIdIfd(getCommunicationRetour().getIdIfd());
            // Prndre seulement celles qui sont inférieures sinon on a le problème en cas de modification
            // (I090306_000024)
            manager.setForLtIdRetour(getCommunicationRetour().getIdRetour());
            manager.find();
            for (int i = 0; i < manager.size(); i++) {
                CPCommunicationFiscaleRetourViewBean ancienneCommunication = (CPCommunicationFiscaleRetourViewBean) manager
                        .getEntity(i);
                if (JANumberFormatter.deQuote(ancienneCommunication.getRevenu1()).equals(
                        JANumberFormatter.deQuote(getCommunicationRetour().getRevenu1()))
                        && JANumberFormatter.deQuote(ancienneCommunication.getRevenu2()).equals(
                                JANumberFormatter.deQuote(getCommunicationRetour().getRevenu2()))
                        && JANumberFormatter.deQuote(ancienneCommunication.getCapital()).equals(
                                JANumberFormatter.deQuote(getCommunicationRetour().getCapital()))
                        && JANumberFormatter.deQuote(ancienneCommunication.getFortune()).equals(
                                JANumberFormatter.deQuote(getCommunicationRetour().getFortune()))) {
                    description = "toto";
                    ajouterErreur(idParam);
                    return niveauMsg;

                }
            }
            return "";
        }
    }

    private boolean isPlausiRenteWIRRVerified(String numAVS, String idParam) throws Exception {

        WIRRDataBean wirrDataBean = new WIRRDataBean();

        wirrDataBean.setNss(numAVS);

        wirrDataBean = WIRRServiceCallUtil.searchRenteWIRR(getSession(), wirrDataBean);

        getCommunicationRetour().setMessageRenteAVS(wirrDataBean.getMessageForUser());

        if (wirrDataBean.hasRenteWIRRFounded() || wirrDataBean.hasTechnicalError()) {
            ajouterErreur(idParam);
            return true;
        }

        return false;

    }

    public String isRenteWIRR(String niveauMsg, String idParam, String description) throws Exception {

        if (!getCommunicationRetour().isNonActif()) {
            return "";
        }

        boolean wantSearchForConjoint = getSedexConjoint() != null
                && !JadeStringUtil.isBlankOrZero(getSedexConjoint().getVn());

        if (isPlausiRenteWIRRVerified(getSedexContribuable().getVn(), idParam)) {
            return niveauMsg;
        } else if (wantSearchForConjoint && isPlausiRenteWIRRVerified(getSedexConjoint().getVn(), idParam)) {
            return niveauMsg;
        }

        return "";

    }

    public String isSedexConditionDeBase(String niveauMsg, String idParam, String description) throws Exception {
        AFAffiliation affiliation = null;
        AFAffiliation affiliationCjt = null;
        boolean trouve = false;
        boolean trouveCjt = false;
        String idContribuable = "";
        String idConjoint = "";
        TITiersViewBean tiers = new TITiersViewBean();

        // Recherche
        if (JadeStringUtil.isEmpty(getCommunicationRetour().getMajNumContribuable())) {
            // Recherche variable si mise à jour du numéro de contribuable
            getCommunicationRetour().setMajNumContribuable(
                    ((CPApplication) getSession().getApplication()).getMajNumContribuable());
        }
        // calcul du revenu
        String revenuNetSalarie = getSedexDonneesBases().getEmploymentIncome();
        String revenuAVS = getSedexDonneesBases().getIncomeFromSelfEmployment();
        String revenuRente = getSedexDonneesBases().getPensionIncome();
        String revenuAgricole = getSedexDonneesBases().getMainIncomeInAgriculture();
        // String revenuAgricole = this.getSedexDonneesBases().getMainIncomeInAgriculture();
        // String rentePontEmployeur = this.getSedexDonneesBases().getOASIBridgingPension();
        // Sauvegarde des dates d'exercice - PO 7089
        if (!JadeStringUtil.isEmpty(getSedexDonneesCommerciales().getCommencementOfSelfEmployment())
                && !"0000-00-00".equalsIgnoreCase(getSedexDonneesCommerciales().getCommencementOfSelfEmployment())) {
            getCommunicationRetour().setDebutExercice1(
                    CPToolBox.formatDate(getSedexDonneesCommerciales().getCommencementOfSelfEmployment(), 2));
        }
        if (!JadeStringUtil.isEmpty(getSedexDonneesCommerciales().getEndOfSelfEmployment())
                && !"0000-00-00".equalsIgnoreCase(getSedexDonneesCommerciales().getEndOfSelfEmployment())) {
            getCommunicationRetour().setFinExercice1(
                    CPToolBox.formatDate(getSedexDonneesCommerciales().getEndOfSelfEmployment(), 2));
        }
        // On somme les 4 si ils sont renseignées.
        double revenuTotal = 0.0;
        if (!JadeStringUtil.isEmpty(revenuNetSalarie) && !revenuNetSalarie.equalsIgnoreCase("null")) {
            revenuTotal += Double.valueOf(JANumberFormatter.deQuote(revenuNetSalarie)).doubleValue();
        }
        if (!JadeStringUtil.isEmpty(revenuAVS) && !revenuAVS.equalsIgnoreCase("null")) {
            revenuTotal += Double.valueOf(JANumberFormatter.deQuote(revenuAVS)).doubleValue();
        }
        if (!JadeStringUtil.isEmpty(revenuRente) && !revenuRente.equalsIgnoreCase("null")) {
            revenuTotal += Double.valueOf(JANumberFormatter.deQuote(revenuRente)).doubleValue();
        }
        if (!JadeStringUtil.isEmpty(getSedexDonneesBases().getPensionIncomeCjt())
                && !getSedexDonneesBases().getPensionIncomeCjt().equalsIgnoreCase("null")) {
            revenuTotal += Double.valueOf(JANumberFormatter.deQuote(getSedexDonneesBases().getPensionIncomeCjt()))
                    .doubleValue();
        }
        getCommunicationRetour().setRevenu1(String.valueOf(revenuTotal));
        if (!JadeStringUtil.isEmpty(revenuAgricole) && !revenuAgricole.equalsIgnoreCase("null")) {
            revenuTotal -= Double.valueOf(JANumberFormatter.deQuote(revenuAgricole)).doubleValue();
            getCommunicationRetour().setRevenu2(String.valueOf(revenuAgricole));
        }
        /*
         * Ne plus cumuler le revenu agricole car celui est déjà compris dans le revenu indépendant if
         * (!JadeStringUtil.isEmpty(revenuAgricole) && !revenuAgricole.equalsIgnoreCase("null")) { revenuTotal +=
         * Double.valueOf(revenuAgricole).doubleValue(); }
         */
        /*
         * PO 8051 - Ne plus cumuler car ce montant est déjà compris dans le montant de rente if
         * (!JadeStringUtil.isEmpty(rentePontEmployeur) && !rentePontEmployeur.equalsIgnoreCase("null")) { revenuTotal
         * += Double.valueOf(rentePontEmployeur).doubleValue(); }
         */
        // Fortune déterminante = fortune contribuable + fortune conjoint
        BigDecimal fortune = new BigDecimal(0);
        BigDecimal fortuneCjt = new BigDecimal(0);
        if (getSedexDonneesBases().getAssets() != null) {
            fortune = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases().getAssets()));
        }
        if (getSedexDonneesBases().getAssetsCjt() != null) {
            fortuneCjt = new BigDecimal(JANumberFormatter.deQuote(getSedexDonneesBases().getAssetsCjt()));
        }
        // Si fortune du contribuable = fortune du conjoint => fortune du couple donc ne pas cumuler.
        if (fortune.compareTo(fortuneCjt) != 0) {
            getCommunicationRetour().setFortune((fortune.add(fortuneCjt)).toString());
        } else {
            getCommunicationRetour().setFortune(fortune.toString());
        }
        // Capital Investi
        if (getSedexDonneesBases().getCapital() != null) {
            getCommunicationRetour().setCapital(getSedexDonneesBases().getCapital());
        } else {
            getCommunicationRetour().setCapital("0");
        }
        // Genre de décision /définitive - rectificative )
        // 1 = normal
        // 2 = spontanée
        // 4 et autre => rectificative
        if (getSedexContribuable().getReportType() != null) {
            if (getSedexContribuable().getReportType().equalsIgnoreCase("1")
                    || getSedexContribuable().getReportType().equalsIgnoreCase("2")) {
                // Si taxation office
                if (getSedexContribuable().getAssessmentType().equalsIgnoreCase("4")) {
                    getCommunicationRetour().setGenreTaxation(CPCommentaireCommunication.CS_GENRE_TO);
                } else {
                    // Définitive
                    getCommunicationRetour().setGenreTaxation(CPCommentaireCommunication.CS_GENRE_TD);
                }
            } else {
                // Si taxation office rectificative
                if (getSedexContribuable().getAssessmentType().equalsIgnoreCase("4")) {
                    getCommunicationRetour().setGenreTaxation(CPCommentaireCommunication.CS_GENRE_TOR);
                } else {
                    // Rectificative
                    getCommunicationRetour().setGenreTaxation(CPCommentaireCommunication.CS_GENRE_TDR);
                }
            }
        }
        // Test si affiliation conjoint et/ou contribuable à récupérer
        // Contribuable à chercher si n° de contribuable et référencde métier vide ou si n° avs de conjoint = n° avs du
        // contribuable
        if ((JadeStringUtil.isBlankOrZero(getSedexContribuable().getYourBusinessReferenceId()) && JadeStringUtil
                .isBlankOrZero(getSedexContribuable().getVn()))
                || (getSedexContribuable().getVn().equalsIgnoreCase(getSedexConjoint().getVn()))) {
            trouve = true;
        }
        if (JadeStringUtil.isBlankOrZero(getSedexConjoint().getVn())) {
            trouveCjt = true;
        }
        // On recherche d'abord si l'affiliation provient d'une demande (messageId)
        if (!getSedexContribuable().isNew()) {
            // On recherche si on a envoyé la demande pour retrouver l'affiliation
            CPCommunicationFiscaleManager managerCommunication = new CPCommunicationFiscaleManager();
            managerCommunication.setSession(getSession());
            managerCommunication.setForIdMessageSedex(getSedexContribuable().getReferenceMessageId());
            managerCommunication.find();
            if (managerCommunication.size() == 1) {
                CPCommunicationFiscale communication = (CPCommunicationFiscale) managerCommunication.getFirstEntity();
                String idAff = communication.getIdAffiliation();
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(getSession());
                aff.setAffiliationId(idAff);
                aff.retrieve();
                if (!aff.isNew()) {
                    tiers.setSession(getSession());
                    tiers.setIdTiers(aff.getIdTiers());
                    tiers.wantCallMethodAfter(false);
                    tiers.retrieve();
                    if (!tiers.isNew()) {
                        // Si num avs = numavs du conjoint => la demande a été faite pour le conjoint.
                        // Dans ce cas mettre l'idaffiliation dans le conjoint et rechercher pour le contribuable
                        if (NSUtil.unFormatAVS(tiers.getNumAvsActuel().trim()).equalsIgnoreCase(
                                NSUtil.unFormatAVS(getSedexConjoint().getVn()).trim())) {
                            affiliationCjt = aff;
                            idConjoint = aff.getIdTiers();
                            trouveCjt = true;
                            trouve = true; // Car la demande a été faite pour le conjoint.
                        } else {
                            affiliation = aff;
                            idContribuable = aff.getIdTiers();
                        }
                    }
                }
            }
            if (!trouve) {
                // sinon on recherche si le fisc nous communique le numéro d'affilié dans la zone business
                String numAffilie = getSedexContribuable().getYourBusinessReferenceId();
                if (!JadeStringUtil.isEmpty(numAffilie)) {
                    // Test si le n° d'affilié n'est pas celui du conjoint
                    if ((affiliationCjt == null) || !numAffilie.equalsIgnoreCase(affiliationCjt.getAffilieNumero())) {
                        AFAffiliationManager histo = new AFAffiliationManager();
                        histo.setSession(getSession());
                        histo.setForAffilieNumero(numAffilie);
                        // histo.setForNumAffilie((getCommunicationRetour().getValeurChampRecherche()).substring(0,3)+"."+(getCommunicationRetour().getValeurChampRecherche()).substring(3));
                        histo.find();
                        if (histo.size() > 0) {
                            idContribuable = (((AFAffiliation) histo.getFirstEntity()).getIdTiers());
                            affiliation = CPToolBox.returnAffiliation(getSession(), getSession()
                                    .getCurrentThreadTransaction(), idContribuable, getCommunicationRetour()
                                    .getAnnee1(), getCommunicationRetour().getGenreAffilie(), 1);
                            if ((affiliation != null) && !affiliation.isNew()) {
                                tiers.setSession(getSession());
                                tiers.setIdTiers(affiliation.getIdTiers());
                                tiers.wantCallMethodAfter(false);
                                tiers.retrieve();
                                if (!tiers.isNew()) {
                                    // Si n° avs = n° avs du conjoint => la demande a été faite pour le conjoint.
                                    // Dans ce cas mettre l'idaffiliation dans le conjoint et rechercher pour le
                                    // contribuable
                                    if (NSUtil.unFormatAVS(tiers.getNumAvsActuel().trim()).equalsIgnoreCase(
                                            NSUtil.unFormatAVS(getSedexConjoint().getVn()).trim())) {
                                        affiliationCjt = affiliation;
                                        idConjoint = affiliation.getIdTiers();
                                        idContribuable = "";
                                        affiliation = null;
                                        trouveCjt = true;
                                        // Vue que le num affilié concerne le conjoint =>
                                        // recherche du contribuable par le NSS
                                        if (!JadeStringUtil.isEmpty(getSedexContribuable().getVn())) {
                                            // On recherche l'affiliation par le numAv
                                            TIHistoriqueAvsManager histoAvs = new TIHistoriqueAvsManager();
                                            histoAvs.setSession(getSession());
                                            histoAvs.setForNumAvs(NSUtil.formatAVSUnknown(getSedexContribuable()
                                                    .getVn()));
                                            histoAvs.find();
                                            if (histoAvs.size() > 0) {
                                                idContribuable = ((TIHistoriqueAvs) histoAvs.getFirstEntity())
                                                        .getIdTiers();
                                                affiliation = CPToolBox.returnAffiliation(getSession(), getSession()
                                                        .getCurrentThreadTransaction(), idContribuable,
                                                        getCommunicationRetour().getAnnee1(), getCommunicationRetour()
                                                                .getGenreAffilie(), 1);
                                            }
                                        }
                                    }
                                    trouve = true; // Car la demande a été faite pour le conjoint.
                                }
                            }
                        }
                    }
                } else {
                    if (!JadeStringUtil.isEmpty(getSedexContribuable().getVn())) {
                        // On recherche l'affiliation par le numAvs
                        TIHistoriqueAvsManager histo = new TIHistoriqueAvsManager();
                        histo.setSession(getSession());
                        histo.setForNumAvs(NSUtil.formatAVSUnknown(getSedexContribuable().getVn()));
                        histo.find();
                        if (histo.size() > 0) {
                            idContribuable = ((TIHistoriqueAvs) histo.getFirstEntity()).getIdTiers();
                            affiliation = CPToolBox.returnAffiliation(getSession(), getSession()
                                    .getCurrentThreadTransaction(), idContribuable, getCommunicationRetour()
                                    .getAnnee1(), getCommunicationRetour().getGenreAffilie(), 1);
                            trouve = true;
                        }
                    }
                }
            }
            if (!trouveCjt) {
                if (!JadeStringUtil.isEmpty(getSedexConjoint().getVn())) {
                    // On recherche l'affiliation par le numAv
                    TIHistoriqueAvsManager histo = new TIHistoriqueAvsManager();
                    histo.setSession(getSession());
                    histo.setForNumAvs(NSUtil.formatAVSUnknown(getSedexConjoint().getVn()));
                    histo.find();
                    for (int i = 0; i < histo.size() && affiliationCjt == null; i++) {
                        idConjoint = ((TIHistoriqueAvs) histo.getEntity(i)).getIdTiers();
                        affiliationCjt = CPToolBox.returnAffiliation(getSession(), getSession()
                                .getCurrentThreadTransaction(), idConjoint, getCommunicationRetour().getAnnee1(),
                                getCommunicationRetour().getGenreAffilie(), 1);
                        if (affiliationCjt == null) {
                            affiliationCjt = CPToolBox.returnAffiliation(getSession(), getSession()
                                    .getCurrentThreadTransaction(), idConjoint, getCommunicationRetour().getAnnee1(),
                                    "", 1);
                        }
                    }
                }
                trouveCjt = true;
            }

            // PO 8965 - Spécificité VD (comme le conjoint n'est jamais transmis => regarder s'il existe dans les tiers
            if (JadeStringUtil.isBlankOrZero(idConjoint) && (JadeStringUtil.isBlankOrZero(idContribuable) == false)) {
                // Recherche id Conjoint
                TICompositionTiersManager cjt = new TICompositionTiersManager();
                cjt.setSession(getSession());
                // Recherche du conjoint
                // acr: selon directive AVS: Le mariage doit être considéré depuis le début de l'année du mariage
                // (division
                // par deux pour toute l'année).
                // Le divorce doit être considéré depuis le début de l'année du divorce (individuel pour toute l'année).
                // Ce qui signifie que l'on va chercher le conjoint au 31.12 de l'année de la décision
                cjt.setForIdTiersEnfantParent(idContribuable);
                cjt.setForTypeLien(TICompositionTiers.CS_CONJOINT);
                cjt.setForDateEntreDebutEtFin("31.12." + getCommunicationRetour().getAnnee1());
                cjt.find();
                if (cjt.size() > 0) { // Formatage conjoint
                    if (((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent().equals(tiers.getIdTiers())) {
                        idConjoint = ((TICompositionTiers) cjt.getEntity(0)).getIdTiersEnfant();
                    } else {
                        idConjoint = ((TICompositionTiers) cjt.getEntity(0)).getIdTiersParent();
                    }
                    if (!JadeStringUtil.isIntegerEmpty(idConjoint) && (100 != Integer.parseInt(idConjoint))) {
                        getCommunicationRetour().setIdConjoint(idConjoint);
                        affiliationCjt = CPToolBox
                                .returnAffiliation(getSession(), getSession().getCurrentThreadTransaction(),
                                        idConjoint, getCommunicationRetour().getAnnee1(), "", 1);
                    }
                }
            }

            if (affiliation != null) {
                trouve = true;
                getCommunicationRetour()
                        .setGenreAffilie(CPToolBox.conversionTypeAffiliationEnGenreAffilie(affiliation));
                getCommunicationRetour().setIdAffiliation(affiliation.getAffiliationId());
                getCommunicationRetour().setTri(affiliation.getAffilieNumero());
            } else {
                getCommunicationRetour().setGenreAffilie("");
                getCommunicationRetour().setIdAffiliation("");
                getCommunicationRetour().setTri("");
            }
            if (affiliationCjt != null) {
                trouveCjt = true;
                getCommunicationRetour().setGenreConjoint(
                        CPToolBox.conversionTypeAffiliationEnGenreAffilie(affiliationCjt));
                getCommunicationRetour().setIdAffiliationConjoint(affiliationCjt.getAffiliationId());
                if (JadeStringUtil.isEmpty(getCommunicationRetour().getTri())) {
                    getCommunicationRetour().setTri(affiliationCjt.getAffilieNumero());
                }
            } else {
                getCommunicationRetour().setGenreConjoint("");
                getCommunicationRetour().setIdAffiliationConjoint("");
            }
            if (!JadeStringUtil.isBlankOrZero(idConjoint)) {
                getCommunicationRetour().setIdConjoint(idConjoint);
            } else {
                getCommunicationRetour().setIdConjoint("");
            }
            if (!JadeStringUtil.isBlankOrZero(idContribuable)) {
                getCommunicationRetour().setIdTiers(idContribuable);
            } else {
                getCommunicationRetour().setIdTiers("");
            }
        }
        // Recherche la période fiscale
        CPPeriodeFiscale periodeFiscale = getCommunicationRetour().getPeriodeFiscale();
        if (periodeFiscale != null) {
            getCommunicationRetour().setIdIfd(periodeFiscale.getIdIfd());
        } else {
            ajouterErreur(idParam);
            getCommunicationRetour().setIdIfd("");
            return niveauMsg;
        }
        if (JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliation())
                && JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliationConjoint())) {
            ajouterErreur(idParam);
            return niveauMsg;
        } else if (trouve && trouveCjt) {
            // Recherche de la communication fiscale (utilise IdTiers et IdIfd)
            CPCommunicationFiscale communicationFiscale = getCommunicationRetour().getCommunicationFiscale(1);
            if ((communicationFiscale != null) && !communicationFiscale.isNew()) {
                getCommunicationRetour().setIdCommunication(communicationFiscale.getIdCommunication());
            } else {
                getCommunicationRetour().setIdCommunication("");
            }
            return "";
        } else {
            ajouterErreur(idParam);
            return niveauMsg;
        }
    }

    /*
     * Test si le conjoint a des revenus
     */
    public String isSedexConjointAvecRevenu(String niveauMsg, String idParam, String description) throws Exception {
        if (getSedexDonneesBases() != null) {
            if (!JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getAssetsCjt())
                    || !JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getCapitalCjt())
                    || !JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getEmploymentIncomeCjt())
                    || !JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getIncomeFromSelfEmploymentCjt())
                    || !JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getMainIncomeInAgricultureCjt())
                    || !JadeStringUtil.isBlank(getSedexDonneesBases().getNonDomesticIncomePresentCjt())
                    || !JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getOASIBridgingPensionCjt())
                    || !JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getPensionIncomeCjt())
                    || !JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getPurchasingLPPCjt())) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /**
     * Ajoute le message paramétré dans la plausibilité si l'état civil du conjoint est différent de celui de Sedex
     * 
     * @param niveauMsg
     * @param idParam
     * @param description
     * @return niveau de message
     * @throws Exception
     */
    public String isSedexConjointEtatCivilDifferent(String niveauMsg, String idParam, String description)
            throws Exception {
        /*
         * PO 9139: Dans les « specs » Sedex : 1=Célibataire 2=Marié 3=Veuf ou veuve 4=Divorcé(e) 5=Non marié(e) 6=Lié
         * par un partenariat 7=Partenariat
         * 
         * 515001=CELIBATAIRE; 515002=MARIE; 515003=DIVORCE; 515004=VEUF; 515005=SEPARE; 515006=SEPARE_DE_FAIT;
         * 515007=LPART; 515008=LPART DISSOUT; 515009=LPART DIS. DECES; 515010=LPART SEP. FAIT
         */
        String code = getSedexContribuable().getMaritalStatus();
        if ((getCommunicationRetour().getConjoint() != null) && !JadeStringUtil.isEmpty(code)) {
            String etatCivilConjoint = getCommunicationRetour().getConjoint().getEtatCivil();
            if (((code.equalsIgnoreCase("1") || code.equalsIgnoreCase("5")) && !etatCivilConjoint
                    .equalsIgnoreCase("515001"))
                    || (code.equalsIgnoreCase("2") && !(etatCivilConjoint.equalsIgnoreCase("515002")
                            || etatCivilConjoint.equalsIgnoreCase("515005") || etatCivilConjoint
                                .equalsIgnoreCase("515006")))
                    || (code.equalsIgnoreCase("3") && !(etatCivilConjoint.equalsIgnoreCase("515004") || etatCivilConjoint
                            .equalsIgnoreCase("515009")))
                    || (code.equalsIgnoreCase("4") && !(etatCivilConjoint.equalsIgnoreCase("515003") || etatCivilConjoint
                            .equalsIgnoreCase("515008")))
                    || ((code.equalsIgnoreCase("6") || code.equalsIgnoreCase("7")) && !(etatCivilConjoint
                            .equalsIgnoreCase("515007") || etatCivilConjoint.equalsIgnoreCase("515010")))) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    /*
     * Contrôle si la cohérence entre l'activité accessoire et le salaire
     */
    public String isSedexControleActiviteAccessoire(String niveauMsg, String idParam, String description)
            throws Exception {
        boolean casAControler = false;
        float revenuActviteAccessoire = Float.parseFloat(FWFindParameter.findParameter(getSession()
                .getCurrentThreadTransaction(), "10500130", "REVACTACC", getCommunicationRetour().getDebutExercice1(),
                "", 0));
        // Contrôle contribuable
        if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie())
                && !CPDecision.CS_TSE.equalsIgnoreCase(getCommunicationRetour().getGenreAffilie())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliation())) {
            // Contrôle si salarié
            boolean salarieProf = false;
            float revenuAgricole = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getMainIncomeInAgriculture()));
            float revenuNonAgricole = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getIncomeFromSelfEmployment()));
            float salaire = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases().getEmploymentIncome()));
            if ((revenuAgricole + revenuNonAgricole) < revenuActviteAccessoire) {
                if (salaire > 12000) {
                    if (salaire >= 24000) {
                        salarieProf = true;
                    } else if (salaire > (6 * (revenuAgricole + revenuNonAgricole))) {
                        salarieProf = true;
                    }
                }
                // Recherche activité acessoire
                boolean activiteAccessoire = AFParticulariteAffiliation.existeParticularite(getSession()
                        .getCurrentThreadTransaction(), getCommunicationRetour().getIdAffiliation(),
                        AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, "01.01."
                                + getCommunicationRetour().getAnnee1());
                if ((activiteAccessoire && !salarieProf) || (salarieProf && !activiteAccessoire)) {
                    casAControler = true;
                }
            }
        }
        // Contrôle conjoint
        if (!CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getCommunicationRetour().getGenreConjoint())
                && !CPDecision.CS_TSE.equalsIgnoreCase(getCommunicationRetour().getGenreConjoint())
                && !JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdAffiliationConjoint())) {
            // Contrôle si salarié
            boolean salarieProfCjt = false;

            float revenuAgricoleCjt = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getMainIncomeInAgricultureCjt()));
            float revenuNonAgricoleCjt = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getIncomeFromSelfEmploymentCjt()));
            float salaireCjt = Float.parseFloat(JANumberFormatter.deQuote(getSedexDonneesBases()
                    .getEmploymentIncomeCjt()));
            if ((revenuAgricoleCjt + revenuNonAgricoleCjt) < revenuActviteAccessoire) {
                if (salaireCjt > 12000) {
                    if (salaireCjt >= 24000) {
                        salarieProfCjt = true;
                    } else if (salaireCjt > (6 * (revenuAgricoleCjt + revenuNonAgricoleCjt))) {
                        salarieProfCjt = true;
                    }
                }
                // Recherche activité acessoire
                boolean activiteAccessoire = AFParticulariteAffiliation.existeParticularite(getSession()
                        .getCurrentThreadTransaction(), getCommunicationRetour().getIdAffiliationConjoint(),
                        AFParticulariteAffiliation.CS_ACTIVITE_ACCESSOIRE, "01.01."
                                + getCommunicationRetour().getAnnee1());
                if ((activiteAccessoire && !salarieProfCjt) || (salarieProfCjt && !activiteAccessoire)) {
                    casAControler = true;
                }
            }
        }
        if (casAControler) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isSedexControleRevenuPCI(String niveauMsg, String idParam, String description) throws Exception {
        if (!getCommunicationRetour().getAffiliation().getTypeAffiliation()
                .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_NON_ACTIF)) {
            // On va rechercher le revenu dans remark
            String revActIdp = CPUtil.getRemarkInfo(getCommunicationRetour().getIdRetour(),
                    CPCommunicationFiscaleRetourSEDEXViewBean.REMARK_REV_ACT_IDP, getSession());
            String revAgr = CPUtil.getRemarkInfo(getCommunicationRetour().getIdRetour(),
                    CPCommunicationFiscaleRetourSEDEXViewBean.REMARK_REV_NET, getSession());
            if (getCommunicationRetour().getAffiliation().getTypeAffiliation()
                    .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP)
                    || getCommunicationRetour().getAffiliation().getTypeAffiliation()
                            .equalsIgnoreCase(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)) {
                // Peut être AGR ou IND (tester les 2)
                if (JadeStringUtil.isNull(revAgr)) {
                    // C'est donc un ind
                    if (JadeStringUtil.isEmpty(revActIdp) || revActIdp.equals("0")) {
                        if (JadeStringUtil.isNull(getCommunicationRetour().getRevenu1())) {
                            return niveauMsg;
                        }
                        if (getCommunicationRetour().getRevenu1().equals("")
                                || getCommunicationRetour().getRevenu1().equals("0")) {
                            ajouterErreur(idParam);
                            return niveauMsg;
                        }

                    }
                } else {
                    // C'est donc un agr
                    if (JadeStringUtil.isEmpty(revAgr) || revAgr.equals("0")) {
                        if (JadeStringUtil.isNull(getCommunicationRetour().getRevenu1())) {
                            return niveauMsg;
                        }
                        if (getCommunicationRetour().getRevenu1().equals("")
                                || getCommunicationRetour().getRevenu1().equals("0")) {
                            ajouterErreur(idParam);
                            return niveauMsg;
                        }
                    }
                }
            }
        }
        return "";
    }

    public String isSedexControleRevenuPSA(String niveauMsg, String idParam, String description) throws Exception {
        String revInd = CPUtil.getRemarkInfo(getCommunicationRetour().getIdRetour(),
                CPCommunicationFiscaleRetourSEDEXViewBean.REMARK_REV_ACT_IDP, getSession());
        if (getCommunicationRetour().getAffiliation().getTypeAffiliation().equals(CodeSystem.TYPE_AFFILI_NON_ACTIF)) {
            if (!JadeStringUtil.isEmpty(revInd) && !revInd.equals("0")) {
                if (JadeStringUtil.isNull(getCommunicationRetour().getRevenu1())) {
                    return niveauMsg;
                }
                if (getCommunicationRetour().getRevenu1().equals("0") || getCommunicationRetour().equals("")) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    public String isSedexDemandeNormale(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getSedexContribuable().getReportType())) {
            if (getSedexContribuable().getReportType().equals("1")) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    public String isSedexRachatLpp(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getPurchasingLPP())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    public String isSedexRachatLppCjt(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getPurchasingLPPCjt())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /*
     * Test si il y a à la fois des revenus pour TSE et IND
     */
    public String isSedexRevenuTSEInd(String niveauMsg, String idParam, String description) throws Exception {
        if (getSedexDonneesBases() != null) {
            if ((!JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getEmploymentIncome()) && !JadeStringUtil
                    .isBlankOrZero(getSedexDonneesBases().getIncomeFromSelfEmployment()))
                    || (!JadeStringUtil.isBlankOrZero(getSedexDonneesBases().getEmploymentIncomeCjt()) && !JadeStringUtil
                            .isBlankOrZero(getSedexDonneesBases().getIncomeFromSelfEmploymentCjt()))) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    public String isSedexRiPcDecesDepart(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isBlankOrZero(getCommunicationRetour().getIdTiers())) {
            // Recherche des affiliations actives pour l'année de la communication
            String[] typeAffiliation = new String[5];
            typeAffiliation[0] = CodeSystem.TYPE_AFFILI_INDEP_EMPLOY;
            typeAffiliation[1] = CodeSystem.TYPE_AFFILI_INDEP;
            typeAffiliation[2] = CodeSystem.TYPE_AFFILI_NON_ACTIF;
            typeAffiliation[3] = CodeSystem.TYPE_AFFILI_TSE;
            typeAffiliation[4] = CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE;
            AFAffiliationManager affMng = new AFAffiliationManager();
            affMng.setSession(getSession());
            affMng.setForIdTiers(getCommunicationRetour().getIdTiers());
            affMng.setForTypeAffiliation(typeAffiliation);
            // affMng.setForDateDebutAffLowerOrEqualTo("31.12." + comm.getAnnee1());
            // affMng.setFromDateFin("02.01." + comm.getAnnee1());
            affMng.find();
            for (int i = 0; i < affMng.size(); i++) {
                AFAffiliation aff1 = (AFAffiliation) affMng.getEntity(i);
                if (CPToolBox.isAffilieAssiste(getSession().getCurrentThreadTransaction(), aff1, "01.01."
                        + getCommunicationRetour().getAnnee1(), "31.12." + getCommunicationRetour().getAnnee1())) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
                if (aff1.getMotifFin().equals("803006") || aff1.getMotifFin().equals("803013")
                        || aff1.getMotifFin().equals("19150004") || aff1.getMotifFin().equals("19130020")) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    public String isSedexSansAssiette(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getSedexContribuable().getAssessmentType())) {
            if (getSedexContribuable().getAssessmentType().equals("11")) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";
    }

    public String isSedexSpontanee(String niveauMsg, String idParam, String description) throws Exception {
        if (JadeStringUtil.isEmpty(getSedexContribuable().getYourBusinessReferenceId())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }

    /**
     * Ajoute le message paramétré dans la plausibilité si l'état civil du tiers est différent de celui de Sedex
     * 
     * @param niveauMsg
     * @param idParam
     * @param description
     * @return niveau de message
     * @throws Exception
     */
    public String isSedexTiersEtatCivilDifferent(String niveauMsg, String idParam, String description) throws Exception {
        /*
         * PO 9139: Dans les « specs » Sedex : 1=Célibataire 2=Marié 3=Veuf ou veuve 4=Divorcé(e) 5=Non marié(e) 6=Lié
         * par un partenariat 7=Partenariat
         * 
         * 515001=CELIBATAIRE; 515002=MARIE; 515003=DIVORCE; 515004=VEUF; 515005=SEPARE; 515006=SEPARE_DE_FAIT;
         * 515007=LPART; 515008=LPART DISSOUT; 515009=LPART DIS. DECES; 515010=LPART SEP. FAIT
         */
        String code = getSedexContribuable().getMaritalStatus();
        if ((getCommunicationRetour().getTiers() != null) && !JadeStringUtil.isEmpty(code)) {
            String etatCivilTiers = getCommunicationRetour().getTiers().getEtatCivil();
            if (((code.equalsIgnoreCase("1") || code.equalsIgnoreCase("5")) && !etatCivilTiers
                    .equalsIgnoreCase("515001"))
                    || (code.equalsIgnoreCase("2") && !(etatCivilTiers.equalsIgnoreCase("515002")
                            || etatCivilTiers.equalsIgnoreCase("515005") || etatCivilTiers.equalsIgnoreCase("515006")))
                    || (code.equalsIgnoreCase("3") && !(etatCivilTiers.equalsIgnoreCase("515004") || etatCivilTiers
                            .equalsIgnoreCase("515009")))
                    || (code.equalsIgnoreCase("4") && !(etatCivilTiers.equalsIgnoreCase("515003") || etatCivilTiers
                            .equalsIgnoreCase("515008")))
                    || ((code.equalsIgnoreCase("6") || code.equalsIgnoreCase("7")) && !(etatCivilTiers
                            .equalsIgnoreCase("515007") || etatCivilTiers.equalsIgnoreCase("515010")))) {
                ajouterErreur(idParam);
                return niveauMsg;
            }
        }
        return "";

    }

    /*
     * public String isServiceDesPentes(String niveauMsg, String idParam, String description) throws Exception { String
     * numAvs = this.getCommunicationRetour().getNumAvs(1); if (JadeStringUtil.isBlankOrZero(numAvs) &&
     * (this.getCommunicationRetour().getTiers() != null)) { numAvs =
     * this.getCommunicationRetour().getTiers().getNumAvsActuel(); } if (JadeStringUtil.isBlankOrZero(numAvs)) { numAvs
     * = this.getCommunicationRetour().getNumAvsFisc(1); } if (!JadeStringUtil.isBlankOrZero(numAvs)) { String
     * messageErreurWebservice = ""; // Appel au webservice if (!JadeStringUtil.isBlankOrZero(messageErreurWebservice))
     * { // Si message = n° avs non trouvé if (messageErreurWebservice.equalsIgnoreCase("test")) { return ""; } else {
     * return messageErreurWebservice; } } else { // traitement des fichiers for (int i = 0; i < array.length; i++) { //
     * Stockage dans la table détail des rentes reçues
     * 
     * }
     * 
     * // Strockage du montant total } } return ""; }
     */
    public String isSoldeCrediteur(String niveauMsg, String idParam, String description) throws Exception {
        if (!JadeStringUtil.isEmpty(getCommunicationRetour().getIdAffiliation())) {
            // Extraction du compte annexe
            String role = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(getSession().getApplication());
            CACompteAnnexe compte = new CACompteAnnexe();
            compte.setSession(getSession());
            compte.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compte.setIdRole(role);
            compte.setIdExterneRole(getCommunicationRetour().getAffiliation().getAffilieNumero());
            compte.wantCallMethodBefore(false);
            compte.retrieve();
            if ((compte != null) && !compte.isNew()) {
                if (compte.getSoldeToCurrency().isNegative()) {
                    ajouterErreur(idParam);
                    return niveauMsg;
                }
            }
        }
        return "";
    }

    /*
     * Retourne niveau du message si c'est une taxation d'office
     */
    public String isTaxationOffice(String niveauMsg, String idParam, String description) throws Exception {
        if (CPCommentaireCommunication.CS_GENRE_TO.equalsIgnoreCase(getCommunicationRetour().getGenreTaxation())
                || CPCommentaireCommunication.CS_GENRE_TOR
                        .equalsIgnoreCase(getCommunicationRetour().getGenreTaxation())) {
            ajouterErreur(idParam);
            return niveauMsg;
        }
        return "";
    }
}
