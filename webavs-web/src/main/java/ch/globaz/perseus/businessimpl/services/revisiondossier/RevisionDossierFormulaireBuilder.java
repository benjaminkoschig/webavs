package ch.globaz.perseus.businessimpl.services.revisiondossier;

import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.tools.PRStringUtils;
import globaz.pyxis.constantes.IConstantes;
import java.util.Iterator;
import java.util.List;
import ch.globaz.perseus.business.calcul.InputCalcul;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSDemandesEnCours;
import ch.globaz.perseus.business.constantes.CSFormation;
import ch.globaz.perseus.business.constantes.CSFormationEnfant;
import ch.globaz.perseus.business.constantes.CSPrestationsRecues;
import ch.globaz.perseus.business.constantes.CSRentes;
import ch.globaz.perseus.business.constantes.CSSexePersonne;
import ch.globaz.perseus.business.constantes.CSSourceEnfant;
import ch.globaz.perseus.business.constantes.CSTypeGarde;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueType;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteType;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneType;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RevisionDossierFormulaireBuilder extends AbstractDocumentBuilder {
    private static final String CDT_DATE_DEBUT = "{dateDebut}";
    private static final String CDT_DATE_FIN = "{dateFin}";

    private String csCaisse = null;
    private DocumentData data = null;
    private Demande demande = null;
    private InputCalcul df = null;
    private String nss = "";

    public RevisionDossierFormulaireBuilder() {

    }

    public DocumentData build() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception {
        try {
            this.loadEntity();
            // this.createJadePublishDocInfo(JACalendar.todayJJsMMsAAAA(), this.adMail, false, this.demande.getDossier()
            // .getDemandePrestation().getDemandePrestation().getIdTiers(),
            // this.getSession().getLabel("PDF_PF_REVISION_DOSSIER_FORMULAIRE"));
            createFormulaire();
            return getData();
        } catch (Exception e) {
            throw new DecisionException("RDFBBuild -  NSS : "
                    + demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel() + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createFormulaire() throws Exception {
        try {
            nss = demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel();
            if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
                getData().addData("isCaissePrincipale", "TRUE");
            } else {
                getData().addData("isCaissePrincipale", "FALSE");
            }
            getData().addData("numDoc", getTexte(12, 2));
            getData().addData("dateBasDePage", JACalendar.todayJJsMMsAAAA());
            getData().addData("idProcess", "PCFRevisionDossierFormulaire");
            getData().addData("titre_document", getTexte(1, 24));
            df = loadInputData(demande);
            remplirSituationPersonnelle();
            remplirSituationFinanciere();
            remplirRemarque();
        } catch (Exception e) {
            throw new DecisionException("RDFBCreateForm -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private String dateCession() throws Exception {
        try {
            if (JadeStringUtil.isEmpty(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.CESSION)
                    .getDateCession())) {
                return df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.CESSION).getDateCession();
            } else if (JadeStringUtil.isEmpty(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.CESSION)
                    .getDateCession())) {
                return df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.CESSION).getDateCession();
            } else {
                return df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.CESSION).getDateCession()
                        + " / "
                        + df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.CESSION).getDateCession();
            }
        } catch (Exception e) {
            throw new DecisionException("RDFBDateCession -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }

    }

    private String dateIJetAlloc(RevenuType revenuType) throws Exception {
        try {
            String dateDebutReq = "";
            String dateFinReq = "";
            String dateDebutConj = "";
            String dateFinConj = "";

            dateDebutReq = df.getDonneesFinancieresRequerant().getElementRevenu(revenuType).getDateDebut();
            dateFinReq = df.getDonneesFinancieresRequerant().getElementRevenu(revenuType).getDateFin();
            dateDebutConj = df.getDonneesFinancieresConjoint().getElementRevenu(revenuType).getDateDebut();
            dateFinConj = df.getDonneesFinancieresConjoint().getElementRevenu(revenuType).getDateFin();

            if (JadeStringUtil.isEmpty(dateDebutReq) && JadeStringUtil.isEmpty(dateFinReq)
                    && JadeStringUtil.isEmpty(dateDebutConj) && JadeStringUtil.isEmpty(dateFinConj)) {
                return PRStringUtils.replaceString(PRStringUtils.replaceString(getTexte(7, 35),
                        RevisionDossierFormulaireBuilder.CDT_DATE_DEBUT, "......................."),
                        RevisionDossierFormulaireBuilder.CDT_DATE_FIN, ".......................");
            } else {
                String dateDebut = "";
                String dateFin = "";

                if (JadeStringUtil.isEmpty(dateDebutReq) && JadeStringUtil.isEmpty(dateDebutConj)) {
                    dateDebut = ".......................";
                } else if (JadeStringUtil.isEmpty(dateDebutReq)) {
                    dateDebut = dateDebutConj;
                } else if (JadeStringUtil.isEmpty(dateDebutConj)) {
                    dateDebut = dateDebutReq;
                } else {
                    dateDebut = dateDebutReq + " / " + dateDebutConj;
                }

                if (JadeStringUtil.isEmpty(dateFinReq) && JadeStringUtil.isEmpty(dateFinConj)) {
                    dateFin = ".......................";
                } else if (JadeStringUtil.isEmpty(dateFinReq)) {
                    dateFin = dateFinConj;
                } else if (JadeStringUtil.isEmpty(dateFinConj)) {
                    dateFin = dateFinReq;
                } else {
                    dateFin = dateFinReq + " / " + dateFinConj;
                }

                return PRStringUtils.replaceString(PRStringUtils.replaceString(getTexte(7, 35),
                        RevisionDossierFormulaireBuilder.CDT_DATE_DEBUT, dateDebut),
                        RevisionDossierFormulaireBuilder.CDT_DATE_FIN, dateFin);
            }
        } catch (Exception e) {
            throw new DecisionException("RDFBDateIjEtAlloc -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(),
                    e);
        }
    }

    private String format(String string) throws Exception {
        if ("0.0".equals(string)) {
            return "";
        } else if (JadeStringUtil.isEmpty(string)) {
            return getTexte(12, 1);
        } else if ("null".equals(string)) {
            return getTexte(12, 1);
        } else {
            return string;
        }
    }

    public String getCsCaisse() {
        return csCaisse;
    }

    public DocumentData getData() {
        return data;
    }

    public Demande getDemande() {
        return demande;
    }

    private String getTexte(int niveau, int position) throws Exception {
        try {
            return getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_FORMULAIRE, niveau, position);
        } catch (Exception e) {
            throw new DecisionException("RDFBgetTexte -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void loadEntity() throws Exception {
        this.loadEntity(IPFCatalogueTextes.CS_REVISION_DOSSIER_FORMULAIRE, getDemande().getDossier()
                .getDemandePrestation().getPersonneEtendue().getTiers().getLangue());
    }

    private InputCalcul loadInputData(Demande demande) throws JadePersistenceException, CalculException {
        if (demande == null) {
            throw new CalculException("Unable to load calcul data, demande is null");
        }
        InputCalcul inputCalcul = new InputCalcul(demande);

        try {
            // Chargement des revenus
            RevenuSearchModel revenuSearchModel = new RevenuSearchModel();
            revenuSearchModel.setForIdDemande(demande.getId());
            revenuSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            revenuSearchModel = PerseusServiceLocator.getRevenuService().search(revenuSearchModel);

            for (JadeAbstractModel monRevenu : revenuSearchModel.getSearchResults()) {
                Revenu revenu = (Revenu) monRevenu;
                inputCalcul.addRevenu(revenu);
            }

            // Chargement des dettes
            DetteSearchModel detteSearchModel = new DetteSearchModel();
            detteSearchModel.setForIdDemande(demande.getId());
            detteSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            detteSearchModel = PerseusServiceLocator.getDetteService().search(detteSearchModel);

            for (JadeAbstractModel maDette : detteSearchModel.getSearchResults()) {
                Dette dette = (Dette) maDette;
                inputCalcul.addDette(dette);
            }

            // Chargement de la fortune
            FortuneSearchModel fortuneSearchModel = new FortuneSearchModel();
            fortuneSearchModel.setForIdDemande(demande.getId());
            fortuneSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            fortuneSearchModel = PerseusServiceLocator.getFortuneService().search(fortuneSearchModel);

            for (JadeAbstractModel maFortune : fortuneSearchModel.getSearchResults()) {
                Fortune fortune = (Fortune) maFortune;
                inputCalcul.addFortune(fortune);
            }

            // Chargement des depensesReconnues
            DepenseReconnueSearchModel depenseReconnueSearchModel = new DepenseReconnueSearchModel();
            depenseReconnueSearchModel.setForIdDemande(demande.getId());
            depenseReconnueSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            depenseReconnueSearchModel = PerseusServiceLocator.getDepenseReconnueService().search(
                    depenseReconnueSearchModel);

            for (JadeAbstractModel maDepenseReconnue : depenseReconnueSearchModel.getSearchResults()) {
                DepenseReconnue depenseReconnue = (DepenseReconnue) maDepenseReconnue;
                inputCalcul.addDepenseReconnue(depenseReconnue);
            }

        } catch (DonneesFinancieresException e) {
            throw new CalculException("DonneesFinancieresException during data loading in revisonDossier : "
                    + e.getMessage());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException(
                    "JadeApplicationServiceNotAvailableException during data loading in revisonDossier : "
                            + e.getMessage());
        }

        return inputCalcul;
    }

    private String nomHoirie() throws Exception {
        try {
            if (JadeStringUtil.isEmpty(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.HOIRIE)
                    .getNomHoirie())) {
                return df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.HOIRIE).getNomHoirie();
            } else if (JadeStringUtil.isEmpty(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.HOIRIE)
                    .getNomHoirie())) {
                return df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.HOIRIE).getNomHoirie();
            } else {
                return df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.HOIRIE).getNomHoirie() + " / "
                        + df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.HOIRIE).getNomHoirie();
            }
        } catch (Exception e) {
            throw new DecisionException("RDFBnomHoirie -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirAdressePaiement() throws Exception {
        remplirAdressePaiementLibelle();
        remplirAdressePaiementRequerant();
    }

    private void remplirAdressePaiementLibelle() throws Exception {
        getData().addData("titire_adresse_paiement", getTexte(6, 1));
        getData().addData("titre_banque", getTexte(6, 2));
        getData().addData("titre_nomAdBanque", getTexte(6, 3));
        getData().addData("titre_nomTitCompte", getTexte(6, 4));
        getData().addData("titre_IBAN", getTexte(6, 5));
        getData().addData("titre_poste", getTexte(6, 6));
        getData().addData("titre_nomTitPoste", getTexte(6, 4));
        getData().addData("titre_CCP", getTexte(6, 5));
    }

    private void remplirAdressePaiementRequerant() throws Exception {

        try {
            AdresseTiersDetail adresseTiersDetail = PFUserHelper.getAdressePaiementAssure(demande.getDossier()
                    .getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers(), JACalendar.todayJJsMMsAAAA());

            if (adresseTiersDetail.getFields() != null) {

                if (JadeStringUtil.isEmpty(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE))) {

                    getData().addData("aPoste", "X");
                    getData().addData("CCP",
                            format(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_CCP)));
                    getData().addData(
                            "nomTitPoste",
                            format(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1) + " "
                                    + adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2)));
                    getData().addData("IBAN", format(""));
                    getData().addData("nomTitCompte", format(""));
                    getData().addData("nomAdBanque", format(""));

                } else {
                    getData().addData("abanque", "X");
                    getData().addData("IBAN",
                            format(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_COMPTE)));
                    getData().addData(
                            "nomTitCompte",
                            format(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D1) + " "
                                    + adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSE_VAR_D2)));
                    getData().addData(
                            "nomAdBanque",
                            format(adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_D1)
                                    + " "
                                    + adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_RUE)
                                    + " "
                                    + adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_NUMERO)
                                    + " "
                                    + adresseTiersDetail.getFields().get(AdresseTiersDetail.ADRESSEP_VAR_BANQUE_NPA)
                                    + " "
                                    + adresseTiersDetail.getFields().get(
                                            AdresseTiersDetail.ADRESSEP_VAR_BANQUE_LOCALITE)));
                    getData().addData("CCP", format(""));
                    getData().addData("nomTitPoste", format(""));

                }

            } else {
                getData().addData("CCP", format(""));
                getData().addData("nomTitPoste", format(""));
                getData().addData("IBAN", format(""));
                getData().addData("nomTitCompte", format(""));
                getData().addData("nomAdBanque", format(""));
            }
        } catch (Exception e) {
            throw new DecisionException("RDFBAdPmtReq -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    public void remplirAutresPrestation() throws Exception {
        remplirAutresPrestationLibelle();
        remplirAutresPrestationRequerant();
    }

    private void remplirAutresPrestationLibelle() throws Exception {
        getData().addData("titre_autres_prestation", getTexte(5, 1));
        getData().addData("AutresPrestRecu", getTexte(5, 2));
        getData().addData("autresPrestOuiTitre", getTexte(5, 3));
        getData().addData("autresPrestNonTitre", getTexte(5, 4));
        getData().addData("justificatifs", getTexte(5, 8));
        getData().addData("PrestCours", getTexte(5, 9));
        getData().addData("PrestCoursOuiTitre", getTexte(5, 3));
        getData().addData("PrestCoursNonTitre", getTexte(5, 4));
    }

    private void remplirAutresPrestationRequerant() throws Exception {
        try {
            List<String> listAutrePrestationRecu = demande.getListCsAutresPrestations();
            // if (listAutrePrestationRecu.contains(CSPrestationsRecues.PRESTATIONS_COMPLEMENTAIRES.getCodeSystem())
            // || listAutrePrestationRecu.contains(CSPrestationsRecues.PRESTATIONS_EVAM.getCodeSystem())
            // || listAutrePrestationRecu.contains(CSPrestationsRecues.PRESTATIONS_RI.getCodeSystem())) {
            // this.getData().addData("autresPrestOui", "X");
            // } else {
            // this.getData().addData("autresPrestNon", "X");
            // }

            if (listAutrePrestationRecu.contains(CSPrestationsRecues.PRESTATIONS_COMPLEMENTAIRES.getCodeSystem())) {
                getData().addData("p2", getTexte(5, 6));
            } else {
                getData().addData("p2", getTexte(5, 6));
            }
            if (listAutrePrestationRecu.contains(CSPrestationsRecues.PRESTATIONS_EVAM.getCodeSystem())) {
                getData().addData("p3", getTexte(5, 7));
            } else {
                getData().addData("p3", getTexte(5, 7));
            }
            if (listAutrePrestationRecu.contains(CSPrestationsRecues.PRESTATIONS_RI.getCodeSystem())) {
                getData().addData("p1", getTexte(5, 5));
            } else {
                getData().addData("p1", getTexte(5, 5));
            }
            // if (listAutrePrestationRecu.contains(CSPrestationsRecues.SUBSIDES_LAMAL.getCodeSystem())) {
            //
            // } else {
            //
            // }

            List<String> listAutreDemande = demande.getListCsAutresDemandes();
            // if (!listAutreDemande.isEmpty()) {
            // this.getData().addData("PrestCoursOui", "X");
            // } else {
            // this.getData().addData("PrestCoursNon", "X");
            // }

            if (listAutreDemande.contains(CSDemandesEnCours.AIDE_LOGEMENT.getCodeSystem())) {
                getData().addData("a3", getTexte(5, 12));
            } else {
                getData().addData("a3", getTexte(5, 12));
            }
            if (listAutreDemande.contains(CSDemandesEnCours.AIDES_FORMATION.getCodeSystem())) {
                getData().addData("a2", getTexte(5, 11));
            } else {
                getData().addData("a2", getTexte(5, 11));
            }

            if (listAutreDemande.contains(CSDemandesEnCours.PENSION_ALIMENTAIRES.getCodeSystem())) {
                getData().addData("a1", getTexte(5, 10));
            } else {
                getData().addData("a1", getTexte(5, 10));
            }
        } catch (Exception e) {
            throw new DecisionException(
                    "RDFBRempAutrePrest -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirDepensesReconnues() throws Exception {
        remplirDepensesReconnuesLibelle();
        remplirDepensesReconnuesReq();
        remplirDepensesReconnuesConj();
    }

    private void remplirDepensesReconnuesConj() throws Exception {
        try {
            getData()
                    .addData(
                            "depRecConj_7",
                            format(df.getDonneesFinancieresConjoint()
                                    .getElementDepenseReconnue(DepenseReconnueType.COTISATION_NON_ACTIF).getValeur()
                                    .toString()));
            getData().addData(
                    "depRecConj_9",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementDepenseReconnue(DepenseReconnueType.INTERETS_HYPOTHECAIRES).getValeur()
                            .toString()));
            getData().addData(
                    "depRecConj_10",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementDepenseReconnue(DepenseReconnueType.FRAIS_ENTRETIENS_IMMEUBLE).getValeur()
                            .toString()));
            getData().addData(
                    "depRecConj_11",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementDepenseReconnue(DepenseReconnueType.PENSION_ALIMENTAIRE_VERSEE).getValeur()
                            .toString()));
            getData().addData(
                    "depRecConj_14",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).getValeur().toString()));
            getData().addData(
                    "depRecConj_15",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementDepenseReconnue(DepenseReconnueType.FRAIS_VETEMENTS).getValeur().toString()));
        } catch (Exception e) {
            throw new DecisionException("RDFBdepRecConj -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirDepensesReconnuesLibelle() throws Exception {
        getData().addData("depRecTittre_1", getTexte(9, 1));
        getData().addData("depRecTittre_2", getTexte(9, 2));
        getData().addData("depRecTittre_3", getTexte(9, 3));
        getData().addData("depRecTittre_4", getTexte(9, 4));
        getData().addData("depRecTittre_5", getTexte(9, 5));
        getData().addData("depRecTittre_6", getTexte(9, 6));
        getData().addData("depRecTittre_7", getTexte(9, 7));
        getData().addData("depRecTittre_8", getTexte(9, 8));
        getData().addData("depRecTittre_9", getTexte(9, 9));
        getData().addData("depRecTittre_10", getTexte(9, 10));
        getData().addData("depRecTittre_11", getTexte(9, 11));
        getData().addData("depRecTittre_12", getTexte(9, 12));
        getData().addData("depRecTittre_13", getTexte(9, 13));
        getData().addData("depRecTittre_14", getTexte(9, 14));
        getData().addData("depRecTittre_15", getTexte(9, 15));
    }

    private void remplirDepensesReconnuesReq() throws Exception {
        try {
            getData().addData(
                    "depRecReq_4",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL).getValeur().toString()));
            getData().addData(
                    "depRecReq_5",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementDepenseReconnue(DepenseReconnueType.CHARGES_ANNUELLES).getValeur().toString()));
            getData().addData(
                    "depRecReq_6",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL).getNbPersonnesLogement()));
            getData()
                    .addData(
                            "depRecReq_7",
                            format(df.getDonneesFinancieresRequerant()
                                    .getElementDepenseReconnue(DepenseReconnueType.COTISATION_NON_ACTIF).getValeur()
                                    .toString()));
            getData().addData(
                    "depRecReq_9",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementDepenseReconnue(DepenseReconnueType.INTERETS_HYPOTHECAIRES).getValeur()
                            .toString()));
            getData().addData(
                    "depRecReq_10",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementDepenseReconnue(DepenseReconnueType.FRAIS_ENTRETIENS_IMMEUBLE).getValeur()
                            .toString()));
            getData().addData(
                    "depRecReq_11",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementDepenseReconnue(DepenseReconnueType.PENSION_ALIMENTAIRE_VERSEE).getValeur()
                            .toString()));
            getData().addData(
                    "depRecReq_14",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).getValeur().toString()));
            getData().addData(
                    "depRecReq_15",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementDepenseReconnue(DepenseReconnueType.FRAIS_VETEMENTS).getValeur().toString()));

        } catch (Exception e) {
            throw new DecisionException("RDFBdepRecReq -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirDonneesFinEnfant() throws Exception {
        remplirDonneesFinEnfantLibelle();
        remplirDonneesFinEnfantTableau();
    }

    private void remplirDonneesFinEnfantLibelle() throws Exception {
        getData().addData("revenusEnfantTitre1", getTexte(8, 1));
        getData().addData("revenusEnfantTitre2", getTexte(8, 2));
        getData().addData("nomPrenomEnfant", getTexte(4, 1));
        getData().addData("RevEnfTitre", getTexte(8, 3));
        getData().addData("FraisObRevEnfTitre", getTexte(8, 4));
        getData().addData("AutreRevEnfTitre", getTexte(8, 5));
        getData().addData("BourseEtudeEnfTitre", getTexte(8, 6));
        getData().addData("FortuneEnfTitre", getTexte(8, 7));
        getData().addData("contenuFraisObRev", getTexte(8, 8));
    }

    private void remplirDonneesFinEnfantTableau() throws Exception {
        try {
            Collection RevenusEnfantTableau = new Collection("RevenusEnfant");
            int nbEnfant = 1;
            for (Iterator iterator = df.getListeEnfants().iterator(); iterator.hasNext();) {
                EnfantFamille enfant = (EnfantFamille) iterator.next();
                DataList ligne = new DataList("typeLigneEnfant");
                ligne.addData("nEtpEnfant", Integer.toString(nbEnfant) + " "
                        + enfant.getEnfant().getMembreFamille().getPersonneEtendue().getTiers().getDesignation1() + " "
                        + enfant.getEnfant().getMembreFamille().getPersonneEtendue().getTiers().getDesignation2());
                ligne.addData(
                        "RevEnfMont",
                        format(df
                                .getDonneesFinancieresMembreFamille(
                                        enfant.getEnfant().getMembreFamille().getSimpleMembreFamille()
                                                .getIdMembreFamille())
                                .getElementRevenu(RevenuType.REVENUS_ACTIVITE_ENFANT).getValeur().toString()));

                Float fraisTransport = df
                        .getDonneesFinancieresMembreFamille(enfant.getEnfant().getMembreFamille().getId())
                        .getElementDepenseReconnue(DepenseReconnueType.FRAIS_TRANSPORT).getValeurModifieeTaxateur();
                Float fraisVetements = df
                        .getDonneesFinancieresMembreFamille(enfant.getEnfant().getMembreFamille().getId())
                        .getElementDepenseReconnue(DepenseReconnueType.FRAIS_VETEMENTS).getValeur();
                Float fraisRepas = df.getDonneesFinancieresMembreFamille(enfant.getEnfant().getMembreFamille().getId())
                        .getElementDepenseReconnue(DepenseReconnueType.FRAIS_REPAS).getValeur();

                Float fraisCetEnfant = new Float(0);
                fraisCetEnfant += fraisTransport;
                fraisCetEnfant += fraisVetements;
                fraisCetEnfant += fraisRepas;
                ligne.addData("FraisObRevEnfMont", format(fraisCetEnfant.toString()));

                ligne.addData(
                        "AutreRevEnfMont",
                        format(df
                                .getDonneesFinancieresMembreFamille(
                                        enfant.getEnfant().getMembreFamille().getSimpleMembreFamille()
                                                .getIdMembreFamille())
                                .getElementRevenu(RevenuType.AUTRES_REVENUS_ENFANT).getValeur().toString()));
                ligne.addData(
                        "BourseEtudeEnfMont",
                        format(df
                                .getDonneesFinancieresMembreFamille(
                                        enfant.getEnfant().getMembreFamille().getSimpleMembreFamille()
                                                .getIdMembreFamille()).getElementRevenu(RevenuType.AIDE_FORMATION)
                                .getValeur().toString()));
                ligne.addData(
                        "FortuneEnfMont",
                        format(df
                                .getDonneesFinancieresMembreFamille(
                                        enfant.getEnfant().getMembreFamille().getSimpleMembreFamille()
                                                .getIdMembreFamille()).getElementFortune(FortuneType.FORTUNE_ENFANT)
                                .getValeur().toString()));
                RevenusEnfantTableau.add(ligne);

                nbEnfant++;
            }

            if (df.getListeEnfants().size() < 6) {
                for (int i = df.getListeEnfants().size() + 1; i <= 6; i++) {
                    DataList ligneVide3 = new DataList("typeLigneEnfant");
                    ligneVide3.addData("nEtpEnfant", Integer.toString(i));
                    RevenusEnfantTableau.add(ligneVide3);
                }
            }

            getData().add(RevenusEnfantTableau);
        } catch (Exception e) {
            throw new DecisionException("RDFBdfEnfTab -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirFormation() throws Exception {
        remplirFormationLibelle();
        remplirFormationRequerant();
        remplirFormationConjoint();

    }

    private void remplirFormationConjoint() throws Exception {
        try {
            if (CSFormation.SCOLARITE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationConjoint())) {
                getData().addData("conjoint_f1", "X");
            } else if (CSFormation.FORMATION_ELEMENTAIRE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationConjoint())) {
                getData().addData("conjoint_f3", "X");
            } else if (CSFormation.APPRENTISSAGE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationConjoint())) {
                getData().addData("conjoint_f5", "X");
            } else if (CSFormation.MATURITE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationConjoint())) {
                getData().addData("conjoint_f7", "X");
            } else if (CSFormation.ECOLE_SUPP.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationConjoint())) {
                getData().addData("conjoint_f2", "X");
            } else if (CSFormation.UNI_HES.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationConjoint())) {
                getData().addData("%=conjoint_f4", "X");
            } else if (CSFormation.AUTRE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationConjoint())) {
                getData().addData("conjoint_f6", "X");
            }
        } catch (Exception e) {
            throw new DecisionException("RDFBformConj-  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirFormationLibelle() throws Exception {
        getData().addData("titre_requerantFormation", getTexte(2, 1));
        getData().addData("titre_conjointFormation", getTexte(2, 2));
        getData().addData("scolariteObligatoire", getTexte(2, 3));
        getData().addData("formationEcoleSup", getTexte(2, 4));
        getData().addData("formationProfEle", getTexte(2, 5));
        getData().addData("uniHes", getTexte(2, 6));
        getData().addData("apprentissage", getTexte(2, 7));
        getData().addData("autre", getTexte(2, 8));
        getData().addData("maturite", getTexte(2, 9));
    }

    private void remplirFormationRequerant() throws Exception {
        try {
            if (CSFormation.SCOLARITE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationRequerant())) {
                getData().addData("requerant_f1", "X");
            } else if (CSFormation.FORMATION_ELEMENTAIRE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationRequerant())) {
                getData().addData("requerant_f3", "X");
            } else if (CSFormation.APPRENTISSAGE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationRequerant())) {
                getData().addData("requerant_f5", "X");
            } else if (CSFormation.MATURITE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationRequerant())) {
                getData().addData("requerant_f7", "X");
            } else if (CSFormation.ECOLE_SUPP.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationRequerant())) {
                getData().addData("requerant_f2", "X");
            } else if (CSFormation.UNI_HES.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationRequerant())) {
                getData().addData("%=requerant_f4", "X");
            } else if (CSFormation.AUTRE.getCodeSystem().equals(
                    demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsNiveauFormationRequerant())) {
                getData().addData("requerant_f6", "X");
            }
        } catch (Exception e) {
            throw new DecisionException("RDFBformReq -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirFortuneEtDettes() throws Exception {
        remplirFortuneEtDettesLibelle();
        remplirFortuneEtDettesRequerant();
        remplirFortuneEtDettesConjoint();
    }

    private void remplirFortuneEtDettesConjoint() throws Exception {
        try {
            getData().addData(
                    "forEtDetteConj_3",
                    format(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.LIQUIDITE).getValeur()
                            .toString()));
            getData().addData(
                    "forEtDetteConj_4",
                    format(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.RACHAT_ASSURANCE_VIE)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteConj_5",
                    format(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.HOIRIE).getValeur()
                            .toString()));
            getData().addData(
                    "forEtDetteConj_6",
                    format(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.CESSION).getValeur()
                            .toString()));
            getData().addData(
                    "forEtDetteConj_7",
                    format(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.AUTRE_BIEN).getValeur()
                            .toString()));
            getData().addData(
                    "forEtDetteConj_9",
                    format(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.IMMEUBLE_HABITE)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteConj_10",
                    format(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.AUTRES_IMMEUBLES)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteConj_11",
                    format(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.BIENS_ETRANGERS)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteConj_12",
                    format(df.getDonneesFinancieresConjoint().getElementDette(DetteType.DETTES_HYPOTHECAIRES)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteConj_13",
                    format(df.getDonneesFinancieresConjoint().getElementDette(DetteType.AUTRES_DETTES).getValeur()
                            .toString()));
        } catch (Exception e) {
            throw new DecisionException("RDFBfortEtDetConj -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(),
                    e);
        }
    }

    private void remplirFortuneEtDettesLibelle() throws Exception {
        getData().addData("forEtDetteTitre_1", getTexte(7, 5));
        getData().addData("forEtDetteTitre_2", getTexte(7, 6));
        getData().addData("forEtDetteTitre_3", getTexte(7, 7));
        getData().addData("forEtDetteTitre_4", getTexte(7, 8));
        if (JadeStringUtil.isEmpty(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.HOIRIE)
                .getNomHoirie())
                && JadeStringUtil.isEmpty(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.HOIRIE)
                        .getNomHoirie())) {
            getData().addData("forEtDetteTitre_5",
                    getTexte(7, 9) + "...............................................................");
        } else {
            getData().addData("forEtDetteTitre_5", getTexte(7, 9) + " " + nomHoirie());
        }

        if (JadeStringUtil.isEmpty(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.CESSION)
                .getDateCession())
                && JadeStringUtil.isEmpty(df.getDonneesFinancieresConjoint().getElementFortune(FortuneType.CESSION)
                        .getDateCession())) {
            getData().addData("forEtDetteTitre_6",
                    getTexte(7, 10) + "...............................................................");
        } else {
            getData().addData("forEtDetteTitre_6", getTexte(7, 10) + " " + dateCession());
        }

        getData().addData("forEtDetteTitre_7", getTexte(7, 11));
        getData().addData("forEtDetteTitre_8", getTexte(7, 12));
        getData().addData("forEtDetteTitre_9", getTexte(7, 13));
        getData().addData("forEtDetteTitre_10", getTexte(7, 14));
        getData().addData("forEtDetteTitre_11", getTexte(7, 15));
        getData().addData("forEtDetteTitre_12", getTexte(7, 16));
        getData().addData("forEtDetteTitre_13", getTexte(7, 17));
    }

    private void remplirFortuneEtDettesRequerant() throws Exception {
        try {
            getData().addData(
                    "forEtDetteReq_3",
                    format(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.LIQUIDITE).getValeur()
                            .toString()));
            getData().addData(
                    "forEtDetteReq_4",
                    format(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.RACHAT_ASSURANCE_VIE)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteReq_5",
                    format(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.HOIRIE).getValeur()
                            .toString()));
            getData().addData(
                    "forEtDetteReq_6",
                    format(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.CESSION).getValeur()
                            .toString()));
            getData().addData(
                    "forEtDetteReq_7",
                    format(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.AUTRE_BIEN).getValeur()
                            .toString()));
            getData().addData(
                    "forEtDetteReq_9",
                    format(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.IMMEUBLE_HABITE)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteReq_10",
                    format(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.AUTRES_IMMEUBLES)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteReq_11",
                    format(df.getDonneesFinancieresRequerant().getElementFortune(FortuneType.BIENS_ETRANGERS)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteReq_12",
                    format(df.getDonneesFinancieresRequerant().getElementDette(DetteType.DETTES_HYPOTHECAIRES)
                            .getValeur().toString()));
            getData().addData(
                    "forEtDetteReq_13",
                    format(df.getDonneesFinancieresRequerant().getElementDette(DetteType.AUTRES_DETTES).getValeur()
                            .toString()));
        } catch (Exception e) {
            throw new DecisionException("RDFforEtDetReq -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirInfosPersonnelle() throws Exception {
        remplirInfosPersonnelleLibelle();
        remplirInfosPersonnelleRequerant();
        remplirInfosPersonnelleConjoint();
    }

    private void remplirInfosPersonnelleConjoint() throws Exception {
        try {
            // this.getData().addData(
            // "conjoint_nip",
            // this.format(this.demande.getSituationFamiliale().getConjoint().getMembreFamille()
            // .getPersonneEtendue().getTiers().getIdTiers()));
            getData().addData(
                    "conjoint_nss",
                    format(demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                            .getPersonneEtendue().getNumAvsActuel()));
            getData().addData(
                    "conjoint_nom",
                    format(demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                            .getTiers().getDesignation1()));
            getData().addData(
                    "conjoint_prenom",
                    format(demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                            .getTiers().getDesignation2()));
            getData().addData("conjoint_nomJeuneFille", format(""));
            if (!JadeStringUtil.isEmpty(demande.getSituationFamiliale().getConjoint().getMembreFamille()
                    .getPersonneEtendue().getPersonne().getSexe())) {
                if (CSSexePersonne.MALE.getCodeSystem().equals(
                        demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                                .getPersonne().getSexe())) {
                    getData().addData("conjoint_sexe", getTexte(1, 10));
                } else {
                    getData().addData("conjoint_sexe", getTexte(1, 11));
                }
            } else {
                getData().addData("conjoint_sexe", format(""));
            }

            getData().addData(
                    "conjoint_etatCivil",
                    format(getSession().getCodeLibelle(
                            demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsTypeConjoint())));
            getData().addData(
                    "conjoint_dateNaissance",
                    format(demande.getSituationFamiliale().getConjoint().getMembreFamille().getPersonneEtendue()
                            .getPersonne().getDateNaissance()));
            String nomPays = "";
            if (!JadeStringUtil.isBlankOrZero(getDemande().getSituationFamiliale().getSimpleSituationFamiliale()
                    .getIdConjoint())) {
                PaysSearchSimpleModel pssmConj = new PaysSearchSimpleModel();
                pssmConj.setForIdPays(demande.getSituationFamiliale().getConjoint().getMembreFamille()
                        .getPersonneEtendue().getTiers().getIdPays());
                TIBusinessServiceLocator.getAdresseService().findPays(pssmConj);
                for (JadeAbstractModel paysSearchConj : pssmConj.getSearchResults()) {
                    PaysSimpleModel pays = (PaysSimpleModel) paysSearchConj;
                    nomPays = pays.getLibelleFr();
                    break;
                }
            }

            getData().addData("conjoint_nationalite", format(nomPays));
            getData().addData("conjoint_genrePermis", format(""));

            AdresseTiersDetail detailTiers = PFUserHelper.getAdresseAssure(demande.getSituationFamiliale()
                    .getConjoint().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers(),
                    IConstantes.CS_AVOIR_ADRESSE_DOMICILE, JACalendar.todayJJsMMsAAAA());

            if (null != detailTiers.getFields()) {
                getData().addData(
                        "conjoint_adresse",
                        format(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE) + " "
                                + detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO)));
                getData().addData(
                        "conjoint_noPostalLoc",
                        format(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                                + detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE)));
            } else {
                getData().addData("conjoint_adresse", format(""));
                getData().addData("conjoint_noPostalLoc", format(""));
            }
            getData().addData("conjoint_noTelephone", format(""));
        } catch (Exception e) {
            throw new DecisionException("RDFBInfosPersConj -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(),
                    e);
        }

    }

    private void remplirInfosPersonnelleEnfant() throws Exception {
        remplirInfosPersonnelleEnfantLibelle();
        remplirInfosPersonnelleEnfantTableau();
    }

    private void remplirInfosPersonnelleEnfantLibelle() throws Exception {
        getData().addData("titre_donneesPersonnellesEnfant", getTexte(4, 20));
        getData().addData("nomPrenomEnfant", getTexte(4, 1));
        getData().addData("dateNaissanceEnfant", getTexte(4, 2));
        getData().addData("nationaliteEnfant", getTexte(1, 14));
        getData().addData("EnfantEst", getTexte(4, 3));
        getData().addData("preciser", getTexte(4, 4));
        getData().addData("EnfantEstEtoile", getTexte(4, 5));
        getData().addData("nssEnfant", getTexte(4, 6));
        getData().addData("sexeEnfant", getTexte(4, 7));
        getData().addData("etatCivilEnfant", getTexte(4, 8));
        getData().addData("enfantMariage", getTexte(4, 9));
        getData().addData("enfantMariagePrecedent", getTexte(4, 10));
        getData().addData("enfantHorsMariage", getTexte(4, 11));
        getData().addData("enfantConjoint", getTexte(4, 12));
        getData().addData("enfantRecueilli", getTexte(4, 13));
        getData().addData("enfantGardePartage", getTexte(4, 14));
        getData().addData("enfantGardeExclu", getTexte(4, 15));
        getData().addData("enfantEcolier", getTexte(4, 16));
        getData().addData("enfantEtudiant", getTexte(4, 17));
        getData().addData("enfantApprenti", getTexte(4, 18));
        getData().addData("laisserVide", getTexte(4, 19));
    }

    private void remplirInfosPersonnelleEnfantTableau() throws Exception {
        try {
            Collection infosPersEnfant = new Collection("InfosPersonnelleEnfant");
            int nbEnfant = 1;
            for (Iterator iterator = df.getListeEnfants().iterator(); iterator.hasNext();) {
                EnfantFamille enfant = (EnfantFamille) iterator.next();
                DataList ligne = new DataList("typeLigneEnfantDP");
                ligne.addData("nbEnfantDP", Integer.toString(nbEnfant));
                ligne.addData("nomPrenomE", enfant.getEnfant().getMembreFamille().getPersonneEtendue().getTiers()
                        .getDesignation1()
                        + " "
                        + enfant.getEnfant().getMembreFamille().getPersonneEtendue().getTiers().getDesignation2()
                        + "\n"
                        + "------------------------------------------"
                        + "\n"
                        + enfant.getEnfant().getMembreFamille().getPersonneEtendue().getPersonneEtendue()
                                .getNumAvsActuel());
                // ligne.addData("nssE", enfant.getEnfant().getMembreFamille().getPersonneEtendue().getPersonneEtendue()
                // .getNumAvsActuel());

                if (CSSexePersonne.MALE.getCodeSystem().equals(
                        enfant.getEnfant().getMembreFamille().getPersonneEtendue().getPersonne().getSexe())) {
                    ligne.addData(
                            "dateNaissE",
                            PRStringUtils.replaceString(enfant.getEnfant().getMembreFamille().getPersonneEtendue()
                                    .getPersonne().getDateNaissance(), ".", "")
                                    + "\n" + "-------------------" + "\n" + getTexte(4, 21));
                    // ligne.addData("sexeE", );
                } else {
                    ligne.addData(
                            "dateNaissE",
                            PRStringUtils.replaceString(enfant.getEnfant().getMembreFamille().getPersonneEtendue()
                                    .getPersonne().getDateNaissance(), ".", "")
                                    + "\n" + "-------------------" + "\n" + getTexte(4, 22));
                    // ligne.addData("sexeE", this.getTexte(4, 22));
                }
                String nomPays = "";
                // PaysSearchSimpleModel pssm = new PaysSearchSimpleModel();
                // pssm.setForIdPays((enfant.getEnfant().getMembreFamille().getPersonneEtendue().getTiers().getIdPays()));
                // TIBusinessServiceLocator.getAdresseService().findPays(pssm);
                // for (JadeAbstractModel paysSearch : pssm.getSearchResults()) {
                // PaysSimpleModel pays = (PaysSimpleModel) paysSearch;
                // nomPays = pays.getLibelleFr();
                // break;
                // }
                ligne.addData("natE", nomPays + "\n" + "--------------" + "\n");
                // + enfant.getEnfant().getMembreFamille().getPersonneEtendue().getPersonne().getEtatCivil());
                // ligne.addData("EtatCivilE", enfant.getEnfant().getMembreFamille().getPersonneEtendue().getPersonne()
                // .getEtatCivil());

                if (CSSourceEnfant.DU_CONJOINT.getCodeSystem().equals(enfant.getSimpleEnfantFamille().getCsSource())) {
                    ligne.addData("e4", "X");
                } else if (CSSourceEnfant.HORS_MARIAGE.getCodeSystem().equals(
                        enfant.getSimpleEnfantFamille().getCsSource())) {
                    ligne.addData("e3", "X");
                } else if (CSSourceEnfant.MARIAGE.getCodeSystem().equals(enfant.getSimpleEnfantFamille().getCsSource())) {
                    ligne.addData("e1", "X");
                } else if (CSSourceEnfant.MARIAGE_PRECEDENT.getCodeSystem().equals(
                        enfant.getSimpleEnfantFamille().getCsSource())) {
                    ligne.addData("e2", "X");
                } else if (CSSourceEnfant.RECUEILLI_ADOPTE.getCodeSystem().equals(
                        enfant.getSimpleEnfantFamille().getCsSource())) {
                    ligne.addData("e5", "X");
                }

                if (CSFormationEnfant.APPRENTI.getCodeSystem().equals(enfant.getSimpleEnfantFamille().getCsFormation())) {
                    ligne.addData("e10", "X");
                } else if (CSFormationEnfant.ECOLIER.getCodeSystem().equals(
                        enfant.getSimpleEnfantFamille().getCsFormation())) {
                    ligne.addData("e8", "X");
                } else if (CSFormationEnfant.ETUDIANT.getCodeSystem().equals(
                        enfant.getSimpleEnfantFamille().getCsFormation())) {
                    ligne.addData("e9", "X");
                }

                if (CSTypeGarde.GARDE_EXCLUSIVE.getCodeSystem().equals(enfant.getSimpleEnfantFamille().getCsGarde())) {
                    ligne.addData("e7", "X");
                } else if (CSTypeGarde.GARDE_PARTAGEE.getCodeSystem().equals(
                        enfant.getSimpleEnfantFamille().getCsGarde())) {
                    ligne.addData("e6", "X");
                }

                nbEnfant++;
                infosPersEnfant.add(ligne);

            }

            if (df.getListeEnfants().size() < 6) {
                for (int i = df.getListeEnfants().size() + 1; i <= 6; i++) {
                    DataList ligneVide3 = new DataList("typeLigneEnfantDP");
                    ligneVide3.addData("nbEnfantDP", Integer.toString(i));
                    ligneVide3.addData("nomPrenomE", "\n" + "------------------------------------------" + "\n");
                    ligneVide3.addData("dateNaissE", "\n" + "-------------------" + "\n");
                    ligneVide3.addData("natE", "\n" + "--------------" + "\n");
                    infosPersEnfant.add(ligneVide3);
                }
            }

            getData().add(infosPersEnfant);
        } catch (Exception e) {
            throw new DecisionException(
                    "RDFBInfoPersEnfant -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirInfosPersonnelleLibelle() throws Exception {

        if (getDemande().getSimpleDemande().getCoaching()) {
            getData().addData("titre_situation_personnelle", getTexte(1, 1) + " " + getTexte(1, 23));
        } else {
            getData().addData("titre_situation_personnelle", getTexte(1, 1));
        }

        getData().addData("requerant", getTexte(1, 2));
        getData().addData("conjoint", getTexte(1, 3));
        getData().addData("titre_nip", getTexte(1, 4));
        getData().addData("titre_nss", getTexte(1, 5));
        getData().addData("ttire_nom", getTexte(1, 6));
        getData().addData("titre_prenom", getTexte(1, 7));
        getData().addData("titre_nomJeuneFille", getTexte(1, 8));
        getData().addData("titre_sexe", getTexte(1, 9));
        getData().addData("titre_etatCivil", getTexte(1, 12));
        getData().addData("titre_dateNaissance", getTexte(1, 13));
        getData().addData("titre_nationalite", getTexte(1, 14));
        getData().addData("titre_genrePermis", getTexte(1, 15));
        getData().addData("titre_adresse", getTexte(1, 16));
        getData().addData("titre_noPostalLoc", getTexte(1, 17));
        getData().addData("titre_noTelephone", getTexte(1, 18));
        getData().addData("titre_DateEtablis", getTexte(1, 19));
        getData().addData("titre_noContribuable", getTexte(1, 20));
        getData().addData("titre_profession", getTexte(1, 21));
        getData().addData("titre_nomAdressEmpl", getTexte(1, 22));
    }

    private void remplirInfosPersonnelleRequerant() throws Exception {
        try {
            getData().addData("requerant_nip",
                    format(demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers()));
            getData().addData(
                    "requerant_nss",
                    format(demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel()));
            getData().addData(
                    "requerant_nom",
                    format(demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation1()));
            getData().addData(
                    "requerant_prenom",
                    format(demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation2()));
            getData().addData("requerant_nomJeuneFille", format(""));
            if (CSSexePersonne.MALE.getCodeSystem().equals(
                    demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne().getSexe())) {
                getData().addData("requerant_sexe", getTexte(1, 10));
            } else {
                getData().addData("requerant_sexe", getTexte(1, 11));
            }
            getData().addData(
                    "requerant_etatCivil",
                    format(getSession().getCodeLibelle(
                            demande.getSituationFamiliale().getSimpleSituationFamiliale().getCsTypeConjoint())));
            getData().addData(
                    "requerant_dateNaissance",
                    format(demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne()
                            .getDateNaissance()));
            String nomPays = "";
            PaysSearchSimpleModel pssmReq = new PaysSearchSimpleModel();
            pssmReq.setForIdPays(demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                    .getIdPays());
            TIBusinessServiceLocator.getAdresseService().findPays(pssmReq);
            for (JadeAbstractModel paysSearchReq : pssmReq.getSearchResults()) {
                PaysSimpleModel pays = (PaysSimpleModel) paysSearchReq;
                nomPays = pays.getLibelleFr();
                break;
            }
            getData().addData("requerant_nationalite", format(nomPays));
            if (demande.getSimpleDemande().getPermisB()) {
                getData().addData("requerant_genrePermis", "B");
            } else {
                getData().addData("requerant_genrePermis", format(""));
            }

            AdresseTiersDetail detailTiers = PFUserHelper.getAdresseAssure(demande.getDossier().getDemandePrestation()
                    .getPersonneEtendue().getTiers().getIdTiers(), IConstantes.CS_AVOIR_ADRESSE_DOMICILE,
                    JACalendar.todayJJsMMsAAAA());
            if (null != detailTiers.getFields()) {
                getData().addData(
                        "requerant_adresse",
                        format(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE) + " "
                                + detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO)));
                getData().addData(
                        "requerant_noPostalLoc",
                        format(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA) + " "
                                + detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE)));
            }

            getData().addData("requerant_dateEtablis", format(demande.getSimpleDemande().getDateArrivee()));
            getData().addData("requerant_genrePermis", format(""));
            getData().addData("requerant_noTelephone", format(""));
        } catch (Exception e) {
            throw new DecisionException("RDFBInfoPersReq -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirPensionAllocPrestRente() throws Exception {
        remplirPensionAllocPrestRenteLibelle();
        remplirPensionAllocPrestRenteReq();
        remplirPensionAllocPrestRenteConj();
    }

    private void remplirPensionAllocPrestRenteConj() throws Exception {
        try {
            getData().addData(
                    "pensionConj_2",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.PENSION_ALIMENTAIRE)
                            .getValeur().toString()));
            getData().addData(
                    "pensionConj_3",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.ALLOCATIONS_FAMILIALES)
                            .getValeur().toString()));
            getData().addData(
                    "pensionConj_4",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementRevenu(RevenuType.ALLOCATION_CANTONALE_MATERNITE).getValeur().toString()));
            getData().addData(
                    "pensionConj_5",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.ALLOCATIONS_AMINH)
                            .getValeur().toString()));
            getData().addData(
                    "pensionConj_6",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.AIDE_FORMATION).getValeur()
                            .toString()));
            getData().addData(
                    "pensionConj_7",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.AIDES_LOGEMENT).getValeur()
                            .toString()));
            getData().addData(
                    "pensionConj_8",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.CONTRAT_ENTRETIENS_VIAGER)
                            .getValeur().toString()));

            getData().addData(
                    "pensionConj_9",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.TOTAL_RENTES).getValeur()
                            .toString()));
            getData().addData(
                    "pensionConj_10",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.AUTRES_RENTES).getValeur()
                            .toString()));
            getData().addData(
                    "rendementConj_2",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.LOYERS_ET_FERMAGES)
                            .getValeur().toString()));
            getData().addData(
                    "rendementConj_3",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementRevenu(RevenuType.VALEUR_LOCATIVE_PROPRE_IMMEUBLE).getValeur().toString()));
            getData().addData(
                    "rendementConj_4",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.INTERET_FORTUNE).getValeur()
                            .toString()));
            // this.getData().addData(
            // "rendementConj_5",
            // this.format(this.df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.INTERET_FORTUNE)
            // .getValeur().toString()));
            getData().addData(
                    "autresRevenuConj_2",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.VALEUR_USUFRUIT).getValeur()
                            .toString()));
            getData().addData(
                    "autresRevenuConj_3",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.DROIT_HABITATION).getValeur()
                            .toString()));
            getData().addData(
                    "autresRevenuConj_4",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.SOUS_LOCATION).getValeur()
                            .toString()));
            getData().addData(
                    "autresRevenuConj_5",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.AUTRES_CREANCES).getValeur()
                            .toString()));
            getData().addData(
                    "autresRevenuConj_6",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.SUCCESSION_NON_PARTAGEE)
                            .getValeur().toString()));
        } catch (Exception e) {
            throw new DecisionException("RDFBPensAllocRenteConj -  NSS : " + nss + ", Détail de l'erreur : "
                    + e.toString(), e);
        }
    }

    private void remplirPensionAllocPrestRenteLibelle() throws Exception {
        getData().addData("pensionTitre_1", getTexte(7, 37));
        getData().addData("pensionTitre_2", getTexte(7, 38));
        getData().addData("pensionTitre_3", getTexte(7, 39));
        getData().addData("pensionTitre_4",
                getTexte(7, 40) + "\n" + dateIJetAlloc(RevenuType.ALLOCATION_CANTONALE_MATERNITE));
        getData().addData("pensionTitre_5", getTexte(7, 41) + "\n" + dateIJetAlloc(RevenuType.ALLOCATIONS_AMINH));
        getData().addData("pensionTitre_6", getTexte(7, 42));
        getData().addData("pensionTitre_7", getTexte(7, 43));
        getData().addData("pensionTitre_8", getTexte(7, 44));
        getData().addData("pensionTitre_9", getTexte(7, 45));
        renteSouligne();
        getData().addData("pensionTitre_10", getTexte(7, 53));
        getData().addData("rendementTitre_1", getTexte(7, 54));
        getData().addData("rendementTitre_2", getTexte(7, 55));
        getData().addData("rendementTitre_3", getTexte(7, 56));
        getData().addData("rendementTitre_4", getTexte(7, 57));
        getData().addData("rendementTitre_5", getTexte(7, 58));
        getData().addData("autresRevenuTitre_1", getTexte(7, 59));
        getData().addData("autresRevenuTitre_2", getTexte(7, 60));
        getData().addData("autresRevenuTitre_3", getTexte(7, 61));
        getData().addData("autresRevenuTitre_4", getTexte(7, 62));
        getData().addData("autresRevenuTitre_5", getTexte(7, 63));
        getData().addData("autresRevenuTitre_6", getTexte(7, 64));

    }

    private void remplirPensionAllocPrestRenteReq() throws Exception {
        try {
            getData().addData(
                    "pensionReq_2",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.PENSION_ALIMENTAIRE)
                            .getValeur().toString()));
            getData().addData(
                    "pensionReq_3",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.ALLOCATIONS_FAMILIALES)
                            .getValeur().toString()));
            getData().addData(
                    "pensionReq_4",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementRevenu(RevenuType.ALLOCATION_CANTONALE_MATERNITE).getValeur().toString()));
            getData().addData(
                    "pensionReq_5",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.ALLOCATIONS_AMINH)
                            .getValeur().toString()));
            getData().addData(
                    "pensionReq_6",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.AIDE_FORMATION).getValeur()
                            .toString()));
            getData().addData(
                    "pensionReq_7",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.AIDES_LOGEMENT).getValeur()
                            .toString()));
            getData().addData(
                    "pensionReq_8",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.CONTRAT_ENTRETIENS_VIAGER)
                            .getValeur().toString()));

            getData().addData(
                    "pensionReq_9",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.TOTAL_RENTES).getValeur()
                            .toString()));
            getData().addData(
                    "pensionReq_10",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.AUTRES_RENTES).getValeur()
                            .toString()));
            getData().addData(
                    "rendementReq_2",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.LOYERS_ET_FERMAGES)
                            .getValeur().toString()));
            getData().addData(
                    "rendementReq_3",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementRevenu(RevenuType.VALEUR_LOCATIVE_PROPRE_IMMEUBLE).getValeur().toString()));
            getData().addData(
                    "rendementReq_4",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.INTERET_FORTUNE).getValeur()
                            .toString()));
            // this.getData().addData(
            // "rendementReq_5",
            // this.format(this.df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.INTERET_FORTUNE)
            // .getValeur().toString()));
            getData().addData(
                    "autresRevenuReq_2",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.VALEUR_USUFRUIT).getValeur()
                            .toString()));
            getData().addData(
                    "autresRevenuReq_3",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.DROIT_HABITATION)
                            .getValeur().toString()));
            getData().addData(
                    "autresRevenuReq_4",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.SOUS_LOCATION).getValeur()
                            .toString()));
            getData().addData(
                    "autresRevenuReq_5",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.AUTRES_CREANCES).getValeur()
                            .toString()));
            getData().addData(
                    "autresRevenuReq_6",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.SUCCESSION_NON_PARTAGEE)
                            .getValeur().toString()));
        } catch (Exception e) {
            throw new DecisionException("RDFBPensAllocRenteReq -  NSS : " + nss + ", Détail de l'erreur : "
                    + e.toString(), e);
        }
    }

    private void remplirRemarque() throws Exception {
        getData().addData("remarque_1", getTexte(10, 1));
        getData().addData("remarque_2", getTexte(10, 2));
        getData().addData("remarque_3", getTexte(10, 3));
        getData().addData("remarque_4", getTexte(10, 4));
        getData().addData("lieuEtdate", getTexte(11, 1));
        getData().addData("signature_req", getTexte(11, 2));
        getData().addData("signature_conj", getTexte(11, 3));
        getData().addData("remarque_5", getTexte(11, 4));
        getData().addData("timbre", getTexte(11, 5));
    }

    private void remplirRevenuHypothétique() throws Exception {
        getData().addData("revenuHypoTitre", getTexte(7, 36));
    }

    private void remplirRevenus() throws Exception {
        remplirRevenusLibelle();
        remplirRevenusRequerant();
        remplirRevenusConjoint();
    }

    private void remplirRevenusConjoint() throws Exception {
        try {
            getData().addData(
                    "revenuConj_3",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.SALAIRE_NET).getValeur()
                            .toString()));
            if (!JadeStringUtil.isEmpty(demande.getSituationFamiliale().getConjoint().getId())) {
                if (df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.SALAIRE_NET).getAvec13eme()) {
                    getData().addData("revenuConj_4", getTexte(5, 3));
                } else {
                    getData().addData("revenuConj_4", getTexte(5, 4));
                }
            } else {
                getData().addData("revenuConj_4", format("0.0"));
            }

            getData().addData(
                    "revenuConj_5",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.REVENU_INDEPENDANT)
                            .getValeur().toString()));
            getData().addData(
                    "revenuConj_6",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.SALAIRE_NATURE).getValeur()
                            .toString()));
            getData().addData(
                    "revenuConj_7",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.TAUX_OCCUPATION).getValeur()
                            .toString()));
            if (!JadeStringUtil.isEmpty(demande.getSituationFamiliale().getConjoint().getId())) {
                if (df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.SALAIRE_NET)
                        .getPlusieursEmployeurs()) {
                    getData().addData("revenuConj_8", getTexte(5, 3));
                } else {
                    getData().addData("revenuConj_8", getTexte(5, 4));
                }
            } else {
                getData().addData("revenuConj_8", format("0.0"));
            }

            getData().addData(
                    "iJConj_2",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MALADIE).getValeur().toString()));
            getData().addData(
                    "iJConj_3",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS).getValeur().toString()));
            getData().addData(
                    "iJConj_4",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_CHOMAGE).getValeur().toString()));
            getData().addData(
                    "iJConj_5",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_AI)
                            .getValeur().toString()));
            getData().addData(
                    "iJConj_6",
                    format(df.getDonneesFinancieresConjoint()
                            .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE).getValeur().toString()));
            getData().addData(
                    "iJConj_7",
                    format(df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_APG)
                            .getValeur().toString()));
        } catch (Exception e) {
            throw new DecisionException("RDFBrevConj -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void remplirRevenusLibelle() throws Exception {
        getData().addData("revenuTitre_1", getTexte(7, 18));
        getData().addData("revenuTitre_2", getTexte(7, 19));
        getData().addData("revenuTitre_3", getTexte(7, 20));
        getData().addData("revenuTitre_4", getTexte(7, 21));
        getData().addData("revenuTitre_5", getTexte(7, 22));
        getData().addData("revenuTitre_6", getTexte(7, 23));
        getData().addData("revenuTitre_7", getTexte(7, 24));
        getData().addData("revenuTitre_8", getTexte(7, 25));
        getData().addData("revenuTitre_9", getTexte(7, 26));
        getData().addData("revenuTitre_10", getTexte(7, 65));
        getData().addData("revenuTitre_11", getTexte(7, 27));

        getData().addData("iJTitre_1", getTexte(7, 28));
        getData().addData("iJTitre_2",
                getTexte(7, 29) + "\n" + dateIJetAlloc(RevenuType.INDEMNITES_JOURNALIERES_MALADIE));

        getData().addData("iJTitre_3",
                getTexte(7, 30) + "\n" + dateIJetAlloc(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS));
        getData().addData("iJTitre_4",
                getTexte(7, 31) + "\n" + dateIJetAlloc(RevenuType.INDEMNITES_JOURNALIERES_CHOMAGE));
        getData().addData("iJTitre_5", getTexte(7, 32) + "\n" + dateIJetAlloc(RevenuType.INDEMNITES_JOURNALIERES_AI));
        getData().addData("iJTitre_6",
                getTexte(7, 33) + "\n" + dateIJetAlloc(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE));
        getData().addData("iJTitre_7", getTexte(7, 34) + "\n" + dateIJetAlloc(RevenuType.INDEMNITES_JOURNALIERES_APG));
    }

    private void remplirRevenusRequerant() throws Exception {
        try {
            getData().addData(
                    "revenuReq_3",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.SALAIRE_NET).getValeur()
                            .toString()));
            if (df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.SALAIRE_NET).getAvec13eme()) {
                getData().addData("revenuReq_4", getTexte(5, 3));
            } else {
                getData().addData("revenuReq_4", getTexte(5, 4));
            }

            getData().addData(
                    "revenuReq_5",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.REVENU_INDEPENDANT)
                            .getValeur().toString()));
            getData().addData(
                    "revenuReq_6",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.SALAIRE_NATURE).getValeur()
                            .toString()));
            getData().addData(
                    "revenuReq_7",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.TAUX_OCCUPATION).getValeur()
                            .toString()));
            if (df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.SALAIRE_NET).getPlusieursEmployeurs()) {
                getData().addData("revenuReq_8", getTexte(5, 3));
            } else {
                getData().addData("revenuReq_8", getTexte(5, 4));
            }
            getData().addData(
                    "iJReq_2",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MALADIE).getValeur().toString()));
            getData().addData(
                    "iJReq_3",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS).getValeur().toString()));
            getData().addData(
                    "iJReq_4",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_CHOMAGE).getValeur().toString()));
            getData().addData(
                    "iJReq_5",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_AI)
                            .getValeur().toString()));
            getData().addData(
                    "iJReq_6",
                    format(df.getDonneesFinancieresRequerant()
                            .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE).getValeur().toString()));
            getData().addData(
                    "iJReq_7",
                    format(df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_APG)
                            .getValeur().toString()));
        } catch (Exception e) {
            throw new DecisionException("RDFBrevReq -  NSS : " + nss + ", Détail de l'erreur : " + e.toString(), e);
        }

    }

    private void remplirSituationFinanciere() throws Exception {
        remplirSituationFinanciereLibelle();
        remplirFortuneEtDettes();
        remplirRevenus();
        remplirRevenuHypothétique();
        remplirPensionAllocPrestRente();
        remplirDonneesFinEnfant();
        remplirDepensesReconnues();

    }

    private void remplirSituationFinanciereLibelle() throws Exception {
        getData().addData("titreSituationFin", getTexte(7, 1));
        getData().addData("titreSituationFinReq", getTexte(7, 2));
        getData().addData("titreSituationFinConj", getTexte(7, 3));
        getData().addData("laisserBlanc", getTexte(7, 4));
        getData().addData("CHF", getTexte(7, 66));
    }

    private void remplirSituationPersonnelle() throws Exception {
        remplirInfosPersonnelle();
        remplirFormation();
        remplirTuteur();
        remplirInfosPersonnelleEnfant();
        remplirAutresPrestation();
        remplirAdressePaiement();

    }

    public void remplirTuteur() throws Exception {
        getData().addData("tuteur", getTexte(3, 1));
        getData().addData("curateur", getTexte(3, 2));
        getData().addData("rep", getTexte(3, 3));
        getData().addData("tutelaire", getTexte(3, 4));
        getData().addData("titre_tuteur_nom", getTexte(1, 6));
        getData().addData("titre_tuteur_prenom", getTexte(3, 5));
        getData().addData("titre_tuteur_adresse", getTexte(1, 16));
        getData().addData("titre_tuteur_lien", getTexte(3, 6));
        getData().addData("titire_tuteur_telephone", getTexte(1, 18));
        getData().addData("titire_tuteur_postal", getTexte(1, 17));
    }

    private void renteSouligne() throws Exception {
        List<String> listeRenteReq = df.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.TOTAL_RENTES)
                .getListCsTypeRentes();
        List<String> listeRenteConj = df.getDonneesFinancieresConjoint().getElementRevenu(RevenuType.TOTAL_RENTES)
                .getListCsTypeRentes();

        if (listeRenteReq.contains(CSRentes.LAA.getCodeSystem())
                || listeRenteConj.contains(CSRentes.LAA.getCodeSystem())) {
            getData().addData("r1s", getTexte(7, 46));
        } else {
            getData().addData("r1", getTexte(7, 46));
        }
        if (listeRenteReq.contains(CSRentes.TROISIEME_PILIER.getCodeSystem())
                || listeRenteConj.contains(CSRentes.TROISIEME_PILIER.getCodeSystem())) {
            getData().addData("r2s", getTexte(7, 47));
        } else {
            getData().addData("r2", getTexte(7, 47));
        }
        if (listeRenteReq.contains(CSRentes.ASSURANCES_PRIVES.getCodeSystem())
                || listeRenteConj.contains(CSRentes.ASSURANCES_PRIVES.getCodeSystem())) {
            getData().addData("r3s", getTexte(7, 48));
        } else {
            getData().addData("r3", getTexte(7, 48));
        }
        if (listeRenteReq.contains(CSRentes.ASSURANCES_MILITAIRE.getCodeSystem())
                || listeRenteConj.contains(CSRentes.ASSURANCES_MILITAIRE.getCodeSystem())) {
            getData().addData("r4s", getTexte(7, 67));
        } else {
            getData().addData("r4", getTexte(7, 67));
        }
        if (listeRenteReq.contains(CSRentes.AI.getCodeSystem()) || listeRenteConj.contains(CSRentes.AI.getCodeSystem())) {
            getData().addData("r5s", getTexte(7, 49));
        } else {
            getData().addData("r5", getTexte(7, 49));
        }
        if (listeRenteReq.contains(CSRentes.AVS.getCodeSystem())
                || listeRenteConj.contains(CSRentes.AVS.getCodeSystem())) {
            getData().addData("r6s", getTexte(7, 50));
        } else {
            getData().addData("r6", getTexte(7, 50));
        }
        if (listeRenteReq.contains(CSRentes.LPP.getCodeSystem())
                || listeRenteConj.contains(CSRentes.LPP.getCodeSystem())) {
            getData().addData("r7s", getTexte(7, 51));
        } else {
            getData().addData("r7", getTexte(7, 51));
        }
        if (listeRenteReq.contains(CSRentes.RENTE_ETRANGERE.getCodeSystem())
                || listeRenteConj.contains(CSRentes.RENTE_ETRANGERE.getCodeSystem())) {
            getData().addData("r8s", getTexte(7, 52));
        } else {
            getData().addData("r8", getTexte(7, 52));
        }
    }

    public void setCsCaisse(String csCaisse) {
        this.csCaisse = csCaisse;
    }

    public void setData(DocumentData data) {
        this.data = data;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }
}
