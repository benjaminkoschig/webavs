package ch.globaz.perseus.businessimpl.services.statsOFS;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.pyxis.constantes.IConstantes;
import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXParseException;
import ch.globaz.perseus.business.calcul.InputCalcul;
import ch.globaz.perseus.business.calcul.OutputData;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSRentes;
import ch.globaz.perseus.business.constantes.CSTauxOccupation;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.calcul.CalculException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.exceptions.models.lot.LotException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.exceptions.paiement.PaiementException;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.demande.DemandeSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueType;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamille;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.statsOFS.StatsOFSService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.statsOFS.jaxb.Antragsteller;
import ch.globaz.perseus.businessimpl.services.statsOFS.jaxb.Antragsteller.Erwerbsit;
import ch.globaz.perseus.businessimpl.services.statsOFS.jaxb.Dossier;
import ch.globaz.perseus.businessimpl.services.statsOFS.jaxb.Dossier.Row;
import ch.globaz.perseus.businessimpl.services.statsOFS.jaxb.Dossiers;
import ch.globaz.perseus.businessimpl.services.statsOFS.jaxb.ObjectFactory;
import ch.globaz.perseus.businessimpl.services.statsOFS.jaxb.Ue;
import ch.globaz.perseus.businessimpl.services.statsOFS.jaxb.WbslEinkommensart;
import ch.globaz.perseus.businessimpl.utils.StatsOFS;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class StatsOFSServiceImpl extends PerseusAbstractServiceImpl implements StatsOFSService {
    private static final String PERSEUS_XSD_PATH = Jade.getInstance().getRootUrl() + "perseusRoot/xsd/";
    private HashMap<String, String> conteneurDateComptabilisationPrestationRetro = new HashMap<String, String>();
    private ArrayList<String> dossierDejaTraiter = new ArrayList<String>();
    private Dossiers dossiers = null;
    private ObjectFactory of = null;
    private boolean isSommeRevenuAdditionnelSuperieurA1 = false;
    private static final String DATE_JJ_MM_DEBUT_ANNEE = "01.01.";
    private static final int NB_MOIS_RETRO = -6;

    private String calculerMontantAutresPrestationsAssurancesSociale(InputCalcul inputCalcul) throws CalculException {
        Float montantAutresPrestationAssuranceSociale = new Float("0.00");
        List<String> listeRenteReq = inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.TOTAL_RENTES).getListCsTypeRentes();
        List<String> listeRenteConj = inputCalcul.getDonneesFinancieresConjoint()
                .getElementRevenu(RevenuType.TOTAL_RENTES).getListCsTypeRentes();

        if (!listeRenteReq.isEmpty()) {
            montantAutresPrestationAssuranceSociale += inputCalcul.getDonneesFinancieresRequerant()
                    .getElementRevenu(RevenuType.TOTAL_RENTES).getValeur();
            montantAutresPrestationAssuranceSociale += inputCalcul.getDonneesFinancieresRequerant()
                    .getElementRevenu(RevenuType.AUTRES_RENTES).getValeur();
        }

        if (!listeRenteConj.isEmpty()) {
            montantAutresPrestationAssuranceSociale += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.TOTAL_RENTES).getValeur();
            montantAutresPrestationAssuranceSociale += inputCalcul.getDonneesFinancieresConjoint()
                    .getElementRevenu(RevenuType.AUTRES_RENTES).getValeur();
        }

        montantAutresPrestationAssuranceSociale += inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_CHOMAGE).getValeur();
        montantAutresPrestationAssuranceSociale += inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MALADIE).getValeur();
        montantAutresPrestationAssuranceSociale += inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS).getValeur();
        montantAutresPrestationAssuranceSociale += inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_AI).getValeur();
        montantAutresPrestationAssuranceSociale += inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE).getValeur();

        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            montantAutresPrestationAssuranceSociale += inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.RENTE_ENFANT).getValeur();
        }

        return this.roundFloat(montantAutresPrestationAssuranceSociale / 12).toString();

    }

    private String calculerMontantPrestationSocialesSousConditionRessources(InputCalcul inputCalcul)
            throws CalculException {

        Float montantReturn = new Float(0);
        Float aidesLogement = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.AIDES_LOGEMENT)
                .getValeur();
        Float allocationFamiliales = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.ALLOCATIONS_FAMILIALES).getValeur();
        Float aidesEtudes = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.AIDE_FORMATION)
                .getValeur();
        Float brapa = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.BRAPA).getValeur();
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            aidesEtudes += inputCalcul.getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.AIDE_FORMATION).getValeur();
            brapa += inputCalcul.getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.PENSION_ALIMENTAIRE_ENFANT).getValeur();
            brapa += inputCalcul.getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.BRAPA_ENFANT).getValeur();
        }

        montantReturn += aidesLogement;
        montantReturn += allocationFamiliales;
        montantReturn += aidesEtudes;
        montantReturn += brapa;

        return this.roundFloat(montantReturn / 12).toString();

    }

    private String calculerTotalRevenusAdditionnels(InputCalcul inputCalcul) throws CalculException {
        Float montantReturn = new Float(0);
        Float loyersEtFermages = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.LOYERS_ET_FERMAGES).getValeur();
        Float interetFortune = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INTERET_FORTUNE).getValeur();
        Float valeurLocative = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.VALEUR_LOCATIVE_PROPRE_IMMEUBLE).getValeur();
        Float sousLocation = inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.SOUS_LOCATION)
                .getValeur();
        Float allocationCantonalMaternite = inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.ALLOCATION_CANTONALE_MATERNITE).getValeur();
        Float autreRevenusEnfant = new Float(0);
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            autreRevenusEnfant += inputCalcul
                    .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.AUTRES_REVENUS_ENFANT).getValeur();
        }

        montantReturn += loyersEtFermages;
        montantReturn += interetFortune;
        montantReturn += valeurLocative;
        montantReturn += sousLocation;
        montantReturn += allocationCantonalMaternite;
        montantReturn += autreRevenusEnfant;
        if (montantReturn == 0) {
            isSommeRevenuAdditionnelSuperieurA1 = false;
            return "";
        } else {
            Float result = this.roundFloat(montantReturn / 12);
            int valMin = result.compareTo(new Float(1));

            isSommeRevenuAdditionnelSuperieurA1 = !(valMin < 0);

            return result.toString();
        }
    }

    private boolean checkAnneeEnquete(BSession session, String anneeEnquete, JadeBusinessLogSession logSession) {
        boolean isAnneeEnqueteOK = false;
        if ((null != anneeEnquete) && !JadeStringUtil.isEmpty(anneeEnquete)) {
            isAnneeEnqueteOK = true;
        } else {
            JadeThread.logError(
                    this.getClass().getName(),
                    BSessionUtil.getSessionFromThreadContext().getLabel(
                            "PROCESS_PF_GENERATION_XML_STSTS_OFS_ERREUR_ANNEE_ENQUETE_OBLIGATOIRE"));
        }

        return isAnneeEnqueteOK;
    }

    private Antragsteller createAntragsteller(PCFAccordee pcfAcc, InputCalcul inputCalcul,
            AdresseTiersDetail detailsTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        Antragsteller antrag = of.createAntragsteller();
        antrag.setEinkommen(of.createAntragstellerEinkommen());
        antrag.setErwerbsit(createErwerbsit(pcfAcc));
        antrag.setRow(createAntragstellerRow(pcfAcc, inputCalcul, detailsTiers));
        return antrag;
    }

    private Antragsteller.Row createAntragstellerRow(PCFAccordee pcfAcc, InputCalcul inputCalcul,
            AdresseTiersDetail detailsTiers) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        Antragsteller.Row row = of.createAntragstellerRow();
        row.getVersichertennummer().add(
                pcfAcc.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                        .getNumAvsActuel());
        row.setNachname(pcfAcc.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                .getDesignation1());
        row.setVorname(pcfAcc.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                .getDesignation2());
        row.getAusbildungId().add(
                StatsOFS.getValueFromCodeSystem(pcfAcc.getDemande().getSituationFamiliale()
                        .getSimpleSituationFamiliale().getCsNiveauFormationRequerant()));
        row = remplirDetailsAdresse(row, detailsTiers);
        row = remplirInfoDemographieRequerant(row, pcfAcc);
        row = remplirTauxActivite(row, inputCalcul);

        return row;
    }

    private Erwerbsit createErwerbsit(PCFAccordee pcfAcc) {
        Erwerbsit er = of.createAntragstellerErwerbsit();
        er.getRow().add(createErwerbsitRow(pcfAcc));
        return er;

    }

    private Erwerbsit.Row createErwerbsitRow(PCFAccordee pcfAcc) {
        Erwerbsit.Row row = of.createAntragstellerErwerbsitRow();
        String codeSystem = pcfAcc.getDemande().getSituationFamiliale().getSimpleSituationFamiliale()
                .getCsSituationActiviteRequerant();
        String valeurConvertie = StatsOFS.getValueFromCodeSystem(codeSystem);
        row.getErwerbsituationId().add(valeurConvertie);
        return row;
    }

    private String createFichierXML(BSession session, String anneeEnquete) throws Exception {
        // création du contexte
        JAXBContext context = JAXBContext.newInstance("ch.globaz.perseus.businessimpl.services.statsOFS.jaxb");

        SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory
                .newSchema(new URL(StatsOFSServiceImpl.PERSEUS_XSD_PATH + "shsDossier_2013-08.xsd"));

        Marshaller m = context.createMarshaller();
        m.setSchema(schema);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        String path = Jade.getInstance().getSharedDir();
        File xmlFile = new File(path + File.separatorChar + session.getLabel("PROCESS_STATS_OFS_TITRE_FICHIER") + "_"
                + anneeEnquete + ".xml");
        m.marshal(dossiers, xmlFile);
        return xmlFile.getPath();
    }

    private Ue.Person createPerson(InputCalcul inputCalcul, PCFAccordee pcfAcc)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        Ue.Person person = of.createUePerson();
        person = createRowPerson(person, inputCalcul, pcfAcc);
        return person;
    }

    private Row createRow(String anneeEnquete, InputCalcul inputCalcul, PCFAccordee pcfacc,
            AdresseTiersDetail detailTiers) throws JadePersistenceException, JAException, JadeApplicationException {
        Row row = of.createDossierRow();
        row.setUeMonatsleistungenBedarf(calculerMontantPrestationSocialesSousConditionRessources(inputCalcul));
        row.setJahr(new BigInteger(anneeEnquete));
        row.setDossiernummer(new BigInteger(pcfacc.getDemande().getDossier().getDossier().getIdDossier()));
        row.setDatAufnahme(dateOuvertureDossier(pcfacc));
        row.setAnzPersonenHh(new BigInteger(inputCalcul.getDonneesFinancieresRequerant()
                .getElementDepenseReconnue(DepenseReconnueType.LOYER_ANNUEL).getNbPersonnesLogement()));
        row.getDatErsteAuszahlung().add(getDatePremierVersement(pcfacc));
        Integer nbPersonnesFamille = 0;
        if (JadeStringUtil.isEmpty(inputCalcul.getDemande().getSituationFamiliale().getConjoint().getId())) {
            nbPersonnesFamille = 1;
        } else {
            nbPersonnesFamille = 2;
        }
        row.setUeMonatserwerbseinkommen(getRevenuMensuel(pcfacc, inputCalcul));
        nbPersonnesFamille += inputCalcul.getListeEnfants().size();
        row.setAnzPersonenUe(new BigInteger(nbPersonnesFamille.toString()));
        row.setUeMonatssozialversleistungen(calculerMontantAutresPrestationsAssurancesSociale(inputCalcul));
        row.setUeMonatsZusatzeinkommen(calculerTotalRevenusAdditionnels(inputCalcul));
        row.setUeMonatseinkommen(this.roundFloat(pcfacc.getCalcul().getDonnee(OutputData.REVENUS_DETERMINANT) / 12)
                .toString());
        row.setBetragZugesprochen(pcfacc.getSimplePCFAccordee().getMontant());
        FWCurrency montantVerseStatsOFS = PerseusServiceLocator.getDossierService().calculerMontantVerseStatsOFS(
                pcfacc.getDemande().getDossier(), anneeEnquete);
        if (montantVerseStatsOFS.isZero()) {
            row.setBetragTotAuszahlungen("");
        } else {
            row.setBetragTotAuszahlungen(montantVerseStatsOFS.toString());
        }

        if (hasMenagePrestationDecembreAnneeEnquête(pcfacc, anneeEnquete)) {
            row.setBBezugStichtag(StatsOFS.OUI);
        } else {
            row.setBBezugStichtag(StatsOFS.NON);
            // Changer la date de fin uniquement pour les cas RETRO en indiquant la date de comptabilisatation
            // de la prestation.
            if (conteneurDateComptabilisationPrestationRetro.containsKey(pcfacc.getSimplePCFAccordee()
                    .getIdPCFAccordee())) {
                // Utilisation de la date de comptabilisation comme date du dernier versement, uniquement pour les cas
                // retroactif
                String dateDeComptabilisationPrestation = "";
                dateDeComptabilisationPrestation = conteneurDateComptabilisationPrestationRetro.get(pcfacc
                        .getSimplePCFAccordee().getIdPCFAccordee());
                row.getDatLetzteZahlung().add(dateDeComptabilisationPrestation);

            } else {
                row.getDatLetzteZahlung().add(pcfacc.getDemande().getSimpleDemande().getDateFin());
            }

        }

        row.getDatAbgeschlossen().add(dateClotureDossier(pcfacc, "31.12." + anneeEnquete));

        String idLocalite = detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID);
        LocaliteSimpleModel localite = TIBusinessServiceLocator.getAdresseService().readLocalite(idLocalite);
        row.setGemeindeId(new BigInteger(localite.getNumCommuneOfs()));

        // var 1.01 comme celle d'avant
        row.setSozialleistungstraegerId(new BigInteger(StatsOFS.SOZIALLEISTUNGSTRAEGERID));

        // var 1.01 comme celle d'avant
        row.setShLeistungstypId(new BigInteger("22"));

        // var 1.00 type de prestation ???
        row.setShLeistungsartId(new BigInteger("22"));

        // var 16.04 pas obligatoire et pas dans le formulaire
        row.getBeendigungsgrundId().add("-9");

        // var 40.0.1 pas dans le formulaire
        String isNouveauDossier = isNouveauDossier(pcfacc, anneeEnquete);
        row.getAntragsartId().add(isNouveauDossier);
        return row;
    }

    private Ue.Person createRowPerson(Ue.Person person, InputCalcul inputCalcul, PCFAccordee pcfAcc)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        int numPersonne = 1;
        if (!JadeStringUtil.isEmpty(inputCalcul.getDemande().getSituationFamiliale().getConjoint().getId())) {
            person.getRow().add(createRowPersonConjoint(pcfAcc, numPersonne));
            numPersonne++;
        }

        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            person.getRow().add(createRowPersonEnfant(ef, numPersonne));
            numPersonne++;
        }
        return person;

    }

    private Ue.Person.Row createRowPersonCommun(Ue.Person.Row row, PersonneEtendueComplexModel person,
            String codeSystemEtatCivil) throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        row.getGeschlechtId().add(StatsOFS.getValueFromCodeSystem(person.getPersonne().getSexe()));
        row.getGeburtsjahr()
                .add(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(person.getPersonne().getDateNaissance()));
        row.getZivilstandId().add(StatsOFS.getValueFromCodeSystem(codeSystemEtatCivil));
        row.setNationalitaetLandId(getNationalite(person.getTiers().getIdPays()));
        row.getVersichertennummer().add(person.getPersonneEtendue().getNumAvsActuel());
        return row;
    }

    private Ue.Person.Row createRowPersonConjoint(PCFAccordee pcfAcc, int numPersonne)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        Ue.Person.Row row = of.createUePersonRow();
        row.setUePersonId(new BigInteger(String.valueOf(numPersonne)));
        row.getVerwandtschaftsgradId().add(
                StatsOFS.getValueFromCodeSystem(pcfAcc.getDemande().getSituationFamiliale()
                        .getSimpleSituationFamiliale().getCsTypeConjoint()));
        row = createRowPersonCommun(row, pcfAcc.getDemande().getSituationFamiliale().getConjoint().getMembreFamille()
                .getPersonneEtendue(), pcfAcc.getDemande().getSituationFamiliale().getSimpleSituationFamiliale()
                .getCsEtatCivilConjoint());
        return row;
    }

    private Ue.Person.Row createRowPersonEnfant(EnfantFamille enfant, int numPersonne)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        Ue.Person.Row row = of.createUePersonRow();
        row.setUePersonId(new BigInteger(String.valueOf(numPersonne)));
        row.getVerwandtschaftsgradId().add(
                StatsOFS.getValueFromCodeSystem(enfant.getSimpleEnfantFamille().getCsSource()));
        row = createRowPersonCommun(row, enfant.getEnfant().getMembreFamille().getPersonneEtendue(), enfant
                .getSimpleEnfantFamille().getCsEtatCivil());
        return row;
    }

    private Ue createUe(InputCalcul inputCalcul, PCFAccordee pcfAcc)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        Ue ue = of.createUe();
        ue.setPerson(createPerson(inputCalcul, pcfAcc));
        return ue;
    }

    private WbslEinkommensart createWbslEinkommensart(PCFAccordee pcfAcc, InputCalcul inputCalcul, PCFAccordee pcfacc)
            throws CalculException, PCFAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        WbslEinkommensart wbsl = of.createWbslEinkommensart();
        List<String> listeRenteReq = inputCalcul.getDonneesFinancieresRequerant()
                .getElementRevenu(RevenuType.TOTAL_RENTES).getListCsTypeRentes();
        List<String> listeRenteConj = inputCalcul.getDonneesFinancieresConjoint()
                .getElementRevenu(RevenuType.TOTAL_RENTES).getListCsTypeRentes();

        wbsl = createWbslEinKommensartPrestationAssuranceSociale(wbsl, inputCalcul, listeRenteReq, listeRenteConj,
                pcfacc);

        wbsl = createWbslEinKommensartPrestationSocialesSousCondition(wbsl, inputCalcul);
        return wbsl;
    }

    private WbslEinkommensart createWbslEinKommensartPrestationAssuranceSociale(WbslEinkommensart wbsl,
            InputCalcul inputCalcul, List<String> listeRenteReq, List<String> listeRenteConj, PCFAccordee pcfacc)
            throws CalculException, PCFAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        wbsl.getRow().add(createWbslEinKommensartRowPourRevenu(inputCalcul, pcfacc));
        wbsl.getRow().add(createWbslEinkommensartRowForLACI(inputCalcul));

        wbsl.getRow().add(
                createWbslEinKommensartRowPourPrestationAssuranceSociale("103", listeRenteReq, listeRenteConj,
                        CSRentes.AVS.getCodeSystem()));
        wbsl.getRow().add(
                createWbslEinKommensartRowPourPrestationAssuranceSociale("104", listeRenteReq, listeRenteConj,
                        CSRentes.VEUF_VEUVE.getCodeSystem()));
        wbsl.getRow().add(
                createWbslEinKommensartRowPourPrestationAssuranceSociale("105", listeRenteReq, listeRenteConj,
                        CSRentes.LPP.getCodeSystem()));
        wbsl.getRow().add(
                createWbslEinKommensartRowPourPrestationAssuranceSociale("106", listeRenteReq, listeRenteConj, ""));
        wbsl.getRow().add(
                createWbslEinKommensartRowPourPrestationAssuranceSocialeRenteAI("107", listeRenteReq, listeRenteConj,
                        CSRentes.AI.getCodeSystem(), inputCalcul));
        wbsl.getRow().add(
                createWbslEinKommensartRowPourPrestationAssuranceSociale("108", listeRenteReq, listeRenteConj,
                        CSRentes.LAA.getCodeSystem()));

        wbsl.getRow().add(createWbslEinkommensartRowForIJCmSuvaAi(inputCalcul));

        wbsl.getRow().add(
                createWbslEinkommensartRowForAutresPrestationAssuranceSociale(inputCalcul, listeRenteReq,
                        listeRenteConj));

        return wbsl;
    }

    private WbslEinkommensart createWbslEinKommensartPrestationSocialesSousCondition(WbslEinkommensart wbsl,
            InputCalcul inputCalcul) throws CalculException {
        wbsl.getRow().add(createWbslEinKommensartRowCommon("19", StatsOFS.NE_SAIS_PAS));
        wbsl.getRow().add(createWbslEinKommensartRowCommon("3", StatsOFS.NE_SAIS_PAS));
        wbsl.getRow().add(createWbslEinKommensartRowCommon("4", StatsOFS.NE_SAIS_PAS));
        wbsl.getRow().add(createWbslEinKommensartRowCommon("15", StatsOFS.NE_SAIS_PAS));
        wbsl.getRow().add(createWbslEinKommensartRowCommon("11", StatsOFS.NE_SAIS_PAS));
        wbsl.getRow().add(createWbslEinKommensartRowCommon("7", StatsOFS.NE_SAIS_PAS));

        if (0 == inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.AIDES_LOGEMENT).getValeur()) {
            wbsl.getRow().add(createWbslEinKommensartRowCommon("5", StatsOFS.NON));
        } else {
            wbsl.getRow().add(createWbslEinKommensartRowCommon("5", StatsOFS.OUI));
        }

        if ((0 == inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.BRAPA).getValeur())
                && !hasRevenuEnfant(inputCalcul, RevenuType.BRAPA_ENFANT)) {
            wbsl.getRow().add(createWbslEinKommensartRowCommon("114", StatsOFS.NON));
        } else {
            wbsl.getRow().add(createWbslEinKommensartRowCommon("114", StatsOFS.OUI));
        }

        boolean hasBourseEtude = false;
        if (0 != inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.AIDE_FORMATION).getValeur()) {
            hasBourseEtude = true;
        }

        if (hasBourseEtude) {
            wbsl.getRow().add(createWbslEinKommensartRowCommon("9", StatsOFS.OUI));
        } else {
            if (hasRevenuEnfant(inputCalcul, RevenuType.AIDE_FORMATION)) {
                wbsl.getRow().add(createWbslEinKommensartRowCommon("9", StatsOFS.OUI));
            } else {
                wbsl.getRow().add(createWbslEinKommensartRowCommon("9", StatsOFS.NON));
            }
        }

        if ((0 == inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.LOYERS_ET_FERMAGES)
                .getValeur())
                && (0 == inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.INTERET_FORTUNE)
                        .getValeur())
                && (0 == inputCalcul.getDonneesFinancieresRegroupees()
                        .getElementRevenu(RevenuType.VALEUR_LOCATIVE_PROPRE_IMMEUBLE).getValeur())
                && (0 == inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.SOUS_LOCATION)
                        .getValeur())) {
            wbsl.getRow().add(createWbslEinKommensartRowCommon("1", StatsOFS.NON));
        } else {
            wbsl.getRow().add(
                    createWbslEinKommensartRowCommon("1", (isSommeRevenuAdditionnelSuperieurA1) ? StatsOFS.OUI
                            : StatsOFS.NON));
        }

        boolean hasPensionAlimentaire = false;
        if (0 != inputCalcul.getDonneesFinancieresRegroupees().getElementRevenu(RevenuType.PENSION_ALIMENTAIRE)
                .getValeur()) {
            hasPensionAlimentaire = true;
        }

        if (hasPensionAlimentaire && isSommeRevenuAdditionnelSuperieurA1) {
            wbsl.getRow().add(createWbslEinKommensartRowCommon("113", StatsOFS.OUI));
        } else {
            if (hasRevenuEnfant(inputCalcul, RevenuType.PENSION_ALIMENTAIRE_ENFANT)
                    && isSommeRevenuAdditionnelSuperieurA1) {
                wbsl.getRow().add(createWbslEinKommensartRowCommon("113", StatsOFS.OUI));
            } else {
                wbsl.getRow().add(createWbslEinKommensartRowCommon("113", StatsOFS.NON));
            }
        }

        if (0 == inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.ALLOCATION_CANTONALE_MATERNITE).getValeur()) {
            wbsl.getRow().add(createWbslEinKommensartRowCommon("2", StatsOFS.NON));
        } else {
            wbsl.getRow().add(
                    createWbslEinKommensartRowCommon("2", (isSommeRevenuAdditionnelSuperieurA1) ? StatsOFS.OUI
                            : StatsOFS.NON));
        }

        if (!hasRevenuEnfant(inputCalcul, RevenuType.AUTRES_REVENUS_ENFANT)) {
            wbsl.getRow().add(createWbslEinKommensartRowCommon("20", StatsOFS.NON));
        } else {
            wbsl.getRow().add(
                    createWbslEinKommensartRowCommon("20", (isSommeRevenuAdditionnelSuperieurA1) ? StatsOFS.OUI
                            : StatsOFS.NON));
        }

        return wbsl;
    }

    private WbslEinkommensart.Row createWbslEinKommensartRowCommon(String id, String boolein) {
        WbslEinkommensart.Row row = of.createWbslEinkommensartRow();
        row.getShEinkommensartId().add(id);
        row.setBInAbklaerung(boolein);
        return row;
    }

    private WbslEinkommensart.Row createWbslEinkommensartRowForAutresPrestationAssuranceSociale(
            InputCalcul inputCalcul, List<String> listeReq, List<String> ListeConj) {
        if (!"0.0".equals(inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MILITAIRE).getValeur().toString())) {
            return createWbslEinKommensartRowCommon("112", StatsOFS.OUI);
        } else if (listeReq.contains(CSRentes.TROISIEME_PILIER.getCodeSystem())
                || ListeConj.contains(CSRentes.TROISIEME_PILIER.getCodeSystem())) {
            return createWbslEinKommensartRowCommon("112", StatsOFS.OUI);
        } else if (listeReq.contains(CSRentes.ASSURANCES_MILITAIRE.getCodeSystem())
                || ListeConj.contains(CSRentes.ASSURANCES_MILITAIRE.getCodeSystem())) {
            return createWbslEinKommensartRowCommon("112", StatsOFS.OUI);
        } else if (listeReq.contains(CSRentes.ASSURANCES_PRIVES.getCodeSystem())
                || ListeConj.contains(CSRentes.ASSURANCES_PRIVES.getCodeSystem())) {
            return createWbslEinKommensartRowCommon("112", StatsOFS.OUI);
        } else if (!"0.0".equals(inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.RENTES_ETRANGERES).getValeur().toString())) {
            return createWbslEinKommensartRowCommon("112", StatsOFS.OUI);
        } else {
            return createWbslEinKommensartRowCommon("112", StatsOFS.NON);
        }

    }

    private WbslEinkommensart.Row createWbslEinkommensartRowForIJCmSuvaAi(InputCalcul inputCalcul) {
        if (!"0.0".equals(inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_ACCIDENTS).getValeur().toString())
                || !"0.0".equals(inputCalcul.getDonneesFinancieresRegroupees()
                        .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_MALADIE).getValeur().toString())
                || !"0.0".equals(inputCalcul.getDonneesFinancieresRegroupees()
                        .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_AI).getValeur().toString())) {
            return createWbslEinKommensartRowCommon("115", StatsOFS.OUI);
        } else {
            return createWbslEinKommensartRowCommon("115", StatsOFS.NON);
        }
    }

    private WbslEinkommensart.Row createWbslEinkommensartRowForLACI(InputCalcul inputCalcul) {
        if (!"0.0".equals(inputCalcul.getDonneesFinancieresRegroupees()
                .getElementRevenu(RevenuType.INDEMNITES_JOURNALIERES_CHOMAGE).getValeur().toString())) {
            return createWbslEinKommensartRowCommon("102", StatsOFS.OUI);
        } else {
            return createWbslEinKommensartRowCommon("102", StatsOFS.NON);
        }
    }

    private WbslEinkommensart.Row createWbslEinKommensartRowPourPrestationAssuranceSociale(String id,
            List<String> listeReq, List<String> ListeConj, String typeRente) throws CalculException,
            PCFAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        WbslEinkommensart.Row row = of.createWbslEinkommensartRow();
        row.getShEinkommensartId().add(id);
        if (listeReq.contains(typeRente) || ListeConj.contains(typeRente)) {
            row.setBInAbklaerung(StatsOFS.OUI);
        } else {
            row.setBInAbklaerung(StatsOFS.NON);
        }
        return row;
    }

    private WbslEinkommensart.Row createWbslEinKommensartRowPourPrestationAssuranceSocialeRenteAI(String id,
            List<String> listeReq, List<String> ListeConj, String typeRente, InputCalcul inputCalcul)
            throws CalculException, PCFAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        boolean hasRenteAI = false;
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            if (inputCalcul.getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(RevenuType.RENTE_ENFANT).getValeur() != 0) {
                hasRenteAI = true;

            }
        }

        WbslEinkommensart.Row row = null;
        if (hasRenteAI) {
            row = of.createWbslEinkommensartRow();
            row.getShEinkommensartId().add(id);
            row.setBInAbklaerung(StatsOFS.OUI);
        } else {
            row = createWbslEinKommensartRowPourPrestationAssuranceSociale(id, listeReq, ListeConj, typeRente);
        }

        return row;
    }

    private WbslEinkommensart.Row createWbslEinKommensartRowPourRevenu(InputCalcul inputCalcul, PCFAccordee pcfacc)
            throws CalculException, PCFAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        WbslEinkommensart.Row row = of.createWbslEinkommensartRow();
        row.getShEinkommensartId().add("101");
        if (JadeStringUtil.isBlankOrZero(getRevenuMensuel(pcfacc, inputCalcul))) {
            row.setBInAbklaerung(StatsOFS.NON);
        } else {
            row.setBInAbklaerung(StatsOFS.OUI);
        }
        return row;
    }

    private String dateClotureDossier(PCFAccordee pcfAcc, String dateFinAnneeEnquete) throws JAException {
        String dateClotureDossier = "";

        // Modification de la date de cloture du dossier pour utiliser la date de comptabilisation (+6mois),
        // pour les cas retro
        if (conteneurDateComptabilisationPrestationRetro.containsKey(pcfAcc.getSimplePCFAccordee().getIdPCFAccordee())) {
            // Utilisation de la date de comptabilisation comme date du dernier versement, uniquement pour les cas
            // retroactif
            String dateDeComptabilisationPrestation = conteneurDateComptabilisationPrestationRetro.get(pcfAcc
                    .getSimplePCFAccordee().getIdPCFAccordee());

            String dateClotureProvisoire = JadeDateUtil.addMonths(dateDeComptabilisationPrestation, 6);

            if (PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dateFinAnneeEnquete).equals(
                    PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dateClotureProvisoire))) {
                dateClotureDossier = dateClotureProvisoire;
            }

        }

        else if (!JadeStringUtil.isEmpty(pcfAcc.getDemande().getSimpleDemande().getDateFin())) {

            String dateClotureProvisoire = JadeDateUtil.addMonths(pcfAcc.getDemande().getSimpleDemande().getDateFin(),
                    6);

            if (PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dateFinAnneeEnquete).equals(
                    PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dateClotureProvisoire))) {
                dateClotureDossier = dateClotureProvisoire;
            }

        }

        return dateClotureDossier;
    }

    private String dateOuvertureDossier(PCFAccordee pcfAcc) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Date de début de la première demande du requérant. Dans le cas d'un arrêt
        // des versements de six mois, c'est la date de début de la nouvelle demande
        // donnant droit à nouveau aux versements qui doit être renseigné
        DemandeSearchModel demandeSearch = loadDemandeForNumOFS(pcfAcc.getDemande().getSimpleDemande().getNumeroOFS());

        for (JadeAbstractModel model : demandeSearch.getSearchResults()) {
            Demande demande = (Demande) model;
            return demande.getSimpleDemande().getDateDebut();

        }
        return "";
    }

    @Override
    public String genererFichierXML(BSession session, JadeBusinessLogSession logSession, String anneeEnquete)
            throws Exception {
        try {
            dossierDejaTraiter.clear();
            if (checkAnneeEnquete(session, anneeEnquete, logSession)) {

                genererFichierXMLAfterCheck(anneeEnquete);

                if (!JadeThread.logHasMessagesOfLevel(JadeBusinessMessageLevels.ERROR)) {
                    return createFichierXML(session, anneeEnquete);
                }
            }
        } catch (JAXBException e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            String msgError = "";
            if (!(e.getLinkedException() == null)) {
                SAXParseException se = (SAXParseException) e.getLinkedException();
                msgError = "Erreur fichier non valide ! ==>" + se.getMessage();
                JadeThread.logError(this.getClass().getName(), msgError);
            }
            e.printStackTrace();
        }

        return null;
    }

    private void genererFichierXMLAfterCheck(String anneeEnquete) throws JadePersistenceException,
            JadeApplicationException, JAException {
        of = new ObjectFactory();
        dossiers = of.createDossiers();
        traitementCasAnneeEnquete(anneeEnquete);
    }

    private AdresseTiersDetail getAdresseTiers(PCFAccordee pcfacc) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        AdresseTiersDetail detailTiers = PFUserHelper.getAdresseAssure(pcfacc.getDemande().getDossier()
                .getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers(),
                IConstantes.CS_AVOIR_ADRESSE_DOMICILE, pcfacc.getDemande().getSimpleDemande().getDateDebut());

        if (null != detailTiers.getFields()) {
            String idLocalite = detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID);
            if (!JadeStringUtil.isEmpty(idLocalite)) {
                LocaliteSimpleModel localite = TIBusinessServiceLocator.getAdresseService().readLocalite(idLocalite);
                if (JadeStringUtil.isBlankOrZero(localite.getNumCommuneOfs())) {
                    numeroCommuneOFSInconnu(pcfacc);
                    return null;
                } else {
                    return detailTiers;
                }
            } else {
                numeroCommuneOFSInconnu(pcfacc);
                return null;
            }
        } else {
            numeroCommuneOFSInconnu(pcfacc);
            return null;
        }
    }

    public HashMap<String, String> getConteneurDateComptabilisationPrestationRetro() {
        return conteneurDateComptabilisationPrestationRetro;
    }

    private String getDatePremierVersement(PCFAccordee pcfAcc) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        String datePremierVersement = "";
        if (conteneurDateComptabilisationPrestationRetro.containsKey(pcfAcc.getSimplePCFAccordee().getIdPCFAccordee())) {
            datePremierVersement = conteneurDateComptabilisationPrestationRetro.get(pcfAcc.getSimplePCFAccordee()
                    .getIdPCFAccordee());
        } else {
            DecisionSearchModel decisionSearch = new DecisionSearchModel();
            decisionSearch.setForNumOFS(pcfAcc.getDemande().getSimpleDemande().getNumeroOFS());
            decisionSearch.setForIdDossier(pcfAcc.getDemande().getDossier().getDossier().getIdDossier());
            decisionSearch.setOrderKey(DecisionSearchModel.ORDER_BY_DATE_DEBUT_ASC);
            decisionSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            decisionSearch.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
            decisionSearch.setForCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
            decisionSearch = PerseusServiceLocator.getDecisionService().search(decisionSearch);
            if (decisionSearch.getSize() > 0) {
                Decision dec = (Decision) decisionSearch.getSearchResults()[0];
                datePremierVersement = dec.getDemande().getSimpleDemande().getDateDebut();
            }

        }
        return datePremierVersement;

    }

    public ArrayList<String> getDossierDejaTraiter() {
        return dossierDejaTraiter;
    }

    private String getNationalite(String idPays) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        String codeIsoPays = "";
        PaysSearchSimpleModel pssmReq = new PaysSearchSimpleModel();
        pssmReq.setForIdPays(idPays);
        pssmReq = TIBusinessServiceLocator.getAdresseService().findPays(pssmReq);
        if (pssmReq.getSize() > 0) {
            PaysSimpleModel pays = (PaysSimpleModel) pssmReq.getSearchResults()[0];
            codeIsoPays = StatsOFS.getValueFromCodeSystem(pays.getCodeIso());
        }

        return StatsOFS.getNeSaisPasSiAucuneValeur(codeIsoPays);
    }

    private String getRevenuMensuel(PCFAccordee pcfacc, InputCalcul inputCalcul) throws CalculException,
            PCFAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        Float revenuProfessionnel = new Float(0);

        if (null != pcfacc) {
            revenuProfessionnel += pcfacc.getCalcul().getDonnee(OutputData.REVENUS_ACTIVITE);
        }

        if (null != inputCalcul) {
            for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
                // Calcul du revenu modifié par la franchise
                revenuProfessionnel += inputCalcul
                        .getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                        .getElementRevenu(RevenuType.REVENUS_ACTIVITE_ENFANT).getValeur();
            }
        }

        return this.roundFloat(revenuProfessionnel / 12).toString();

    }

    private boolean hasMenagePrestationDecembreAnneeEnquête(PCFAccordee pcfAcc, String anneeEnquete) {
        boolean hasPrestationDecembre = false;
        String dateFinAnneeEnquete = "31.12." + anneeEnquete;
        if (JadeStringUtil.isEmpty(pcfAcc.getDemande().getSimpleDemande().getDateFin())) {
            hasPrestationDecembre = true;
        } else if (dateFinAnneeEnquete.equals(pcfAcc.getDemande().getSimpleDemande().getDateFin())) {
            hasPrestationDecembre = true;
        }

        return hasPrestationDecembre;

    }

    private boolean hasRevenuEnfant(InputCalcul inputCalcul, RevenuType type) throws CalculException {
        boolean hasRetenu = false;
        for (EnfantFamille ef : inputCalcul.getListeEnfants()) {
            if (0 != inputCalcul.getDonneesFinancieresMembreFamille(ef.getEnfant().getMembreFamille().getId())
                    .getElementRevenu(type).getValeur()) {
                return hasRetenu = true;
            }
        }

        return hasRetenu;
    }

    private String isNouveauDossier(PCFAccordee pcfAcc, String anneeEnquete) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        // Dossier connu l'année précédente si
        String dateDebut = JadeDateUtil.addYears("01.01." + anneeEnquete, -1);
        String dateFin = JadeDateUtil.addYears("31.12." + anneeEnquete, -1);
        DecisionSearchModel decisionSearch = loadDecisionOctroiValideValableDurantAnneePrecedentEnqueteForidDossier(
                anneeEnquete, dateDebut, dateFin, pcfAcc.getDemande().getSimpleDemande().getIdDossier());
        if (decisionSearch.getSize() >= 1) {
            boolean isReouverture = false;
            for (JadeAbstractModel model : decisionSearch.getSearchResults()) {
                Decision dec = (Decision) model;
                if (!pcfAcc.getDemande().getSimpleDemande().getNumeroOFS()
                        .equals(dec.getDemande().getSimpleDemande().getNumeroOFS())) {
                    isReouverture = true;
                    break;
                }
            }

            if (isReouverture) {
                return StatsOFS.REOUVERTURE_DOSSIER_APRES_SIX_MOIS;
            } else {
                return StatsOFS.NOUVEAU_DOSSIER;
            }
        } else {
            return StatsOFS.NOUVEAU_DOSSIER;
        }

    }

    private boolean isPostePleinTemps(String taux) {
        return Integer.parseInt(taux) >= 90;
    }

    private DecisionSearchModel loadDecisionOctroiValideValableDurantAnneePrecedentEnqueteForidDossier(
            String anneeEnquete, String dateDebut, String dateFin, String idDossier) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DecisionSearchModel decisionSearch = new DecisionSearchModel();
        decisionSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        decisionSearch.setForCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
        decisionSearch.setForDateFin(dateFin);
        decisionSearch.setForIdDossier(idDossier);
        decisionSearch.setForDateDebut(dateDebut);
        decisionSearch.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
        decisionSearch.setWhereKey(DecisionSearchModel.WITH_ANNEE_VALABLE);
        decisionSearch.setOrderKey(DecisionSearchModel.ORDER_BY_DATE_FIN_AND_DATE_VALIDATION_AND_NUM_DECISION_DESC);
        return PerseusServiceLocator.getDecisionService().search(decisionSearch);

    }

    private DemandeSearchModel loadDemandeForNumOFS(String numeroOFS) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        DemandeSearchModel demandeSearch = new DemandeSearchModel();
        demandeSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        demandeSearch.setForCsEtatDemande(CSEtatDemande.VALIDE.getCodeSystem());
        demandeSearch.setForNumeroOFS(numeroOFS);
        demandeSearch.setOrderKey(DemandeSearchModel.ORDER_BY_DATE_DEBUT_ASC);
        return PerseusServiceLocator.getDemandeService().search(demandeSearch);

    }

    /**
     * Permet de charger les données financières concerant la demande passée en paramètre
     * 
     * @param demande
     * @return InputCalcul Les données financières nécessaires au calcul
     * @throws JadePersistenceException
     * @throws CalculException
     */
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

            // Chargement des variables métiers
            VariableMetierSearch variableMetierSearch = new VariableMetierSearch();
            variableMetierSearch.setForDateValable(demande.getSimpleDemande().getDateDebut().substring(3));
            variableMetierSearch = PerseusServiceLocator.getVariableMetierService().search(variableMetierSearch);

            for (JadeAbstractModel maVariableMetier : variableMetierSearch.getSearchResults()) {
                VariableMetier variableMetier = (VariableMetier) maVariableMetier;
                inputCalcul.addVariableMetier(variableMetier);
            }

        } catch (DonneesFinancieresException e) {
            throw new CalculException("DonneesFinancieresException during data loading in calcul : " + e.getMessage());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CalculException("JadeApplicationServiceNotAvailableException during data loading in calcul : "
                    + e.getMessage());
        } catch (VariableMetierException e) {
            throw new CalculException("VariableMetierException during data loading in calcul : " + e.getMessage());
        }

        return inputCalcul;
    }

    private PCFAccordee loadPCFAccordee(String idPCFAccordee) throws PCFAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        return PerseusServiceLocator.getPCFAccordeeService().read(idPCFAccordee);
    }

    private HashMap<String, String> loadPCFAccordee(String anneeEnquete, String dateDebut, String dateFin)
            throws PaiementException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            LotException, PCFAccordeeException {

        // Modification de la date de début des cas retro au 01.07 de l'année précédent l'année renseigné dans
        // l'écran
        HashMap<String, String> listePCFAccordee = loadPCFAccordeePrestationMensuelle(anneeEnquete, dateDebut, dateFin);
        HashMap<String, Prestation> listePrestationRetro = loadPrestationPaiementRetro(anneeEnquete, dateDebut, dateFin);

        for (Entry<String, Prestation> entry : listePrestationRetro.entrySet()) {
            String idDossier = entry.getKey();
            if (!listePCFAccordee.containsKey(idDossier)) {
                Prestation prestation = entry.getValue();

                PCFAccordeeSearchModel pcfSearch = new PCFAccordeeSearchModel();
                pcfSearch.setForIdDemande(prestation.getDecision().getDemande().getSimpleDemande().getIdDemande());
                pcfSearch = PerseusServiceLocator.getPCFAccordeeService().search(pcfSearch);
                if (pcfSearch.getSize() > 0) {
                    PCFAccordee pcfAccordee = (PCFAccordee) pcfSearch.getSearchResults()[0];
                    listePCFAccordee.put(pcfAccordee.getDemande().getDossier().getDossier().getIdDossier(), pcfAccordee
                            .getSimplePCFAccordee().getIdPCFAccordee());
                    conteneurDateComptabilisationPrestationRetro.put(pcfAccordee.getSimplePCFAccordee()
                            .getIdPCFAccordee(), prestation.getLot().getSimpleLot().getDateEnvoi());
                }
            }
        }
        listePrestationRetro.clear();
        return listePCFAccordee;

    }

    private HashMap<String, String> loadPCFAccordeePrestationMensuelle(String anneeEnquete, String dateDebut,
            String dateFin) throws PaiementException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        List<String> listeMoisPrestMensuelle = new ArrayList<String>();

        String enCoursPrestMensuelle = "01.07."
                + JadeStringUtil.substring(JadeDateUtil.addYears("01.01." + anneeEnquete, -1), 6);

        String moisDebutPourPrestationMensuelle = "";

        if (JadeDateUtil.isDateAfter(dateFin + anneeEnquete, "01."
                + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt())) {
            moisDebutPourPrestationMensuelle = PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        } else {
            moisDebutPourPrestationMensuelle = JadeStringUtil.substring(dateFin, 3) + anneeEnquete;
        }

        while (!JadeDateUtil.isDateAfter(enCoursPrestMensuelle, "01." + moisDebutPourPrestationMensuelle)) {
            listeMoisPrestMensuelle.add(enCoursPrestMensuelle.substring(3));
            enCoursPrestMensuelle = JadeDateUtil.addMonths(enCoursPrestMensuelle, 1);
        }

        HashMap<String, String> listePCFAccordeePaiementMensuel = new HashMap<String, String>();
        for (String moisEnCours : listeMoisPrestMensuelle) {
            // Compter les préstation mensuelles versées pendant le mois
            List<PCFAccordee> prestationsMensuelles = PerseusServiceLocator.getPmtMensuelService().listPCFAencours(
                    moisEnCours);
            for (PCFAccordee pcfa : prestationsMensuelles) {
                listePCFAccordeePaiementMensuel.put(pcfa.getDemande().getDossier().getDossier().getIdDossier(), pcfa
                        .getSimplePCFAccordee().getIdPCFAccordee());
            }
            prestationsMensuelles.clear();
        }

        return listePCFAccordeePaiementMensuel;
    }

    private HashMap<String, Prestation> loadPrestationPaiementRetro(String anneeEnquete, String dateDebut,
            String dateFin) throws LotException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // La date de début doit être antérieur de 6 mois (règle des 6 mois OFS) à la date de début initiale
        String dateDebutAnterieur = JadeDateUtil.addMonths(DATE_JJ_MM_DEBUT_ANNEE + anneeEnquete, NB_MOIS_RETRO);

        PrestationSearchModel prestationSearch = new PrestationSearchModel();
        prestationSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        prestationSearch.setForCsTypeLot(CSTypeLot.LOT_DECISION.getCodeSystem());
        prestationSearch.setForEtatLot(CSEtatLot.LOT_VALIDE.getCodeSystem());
        prestationSearch.setBetweenDateComptabilisationDebut(dateDebutAnterieur);
        prestationSearch.setBetweenDateComptabilisationFin(dateFin + anneeEnquete);
        prestationSearch.setOrderKey(PrestationSearchModel.ORDER_BY_DATE_COMPTABILIASTION_LOT_ASC);

        prestationSearch = PerseusServiceLocator.getPrestationService().search(prestationSearch);

        HashMap<String, Prestation> prestationRetro = new HashMap<String, Prestation>();
        for (JadeAbstractModel prestaResult : prestationSearch.getSearchResults()) {
            Prestation prestation = (Prestation) prestaResult;
            Float montantPrestation = Float.parseFloat(prestation.getSimplePrestation().getMontantTotal());
            if (montantPrestation > 0) {
                prestationRetro.put(prestation.getDecision().getDemande().getDossier().getDossier().getIdDossier(),
                        prestation);
            }

        }

        return prestationRetro;
    }

    private void numeroCommuneOFSInconnu(PCFAccordee pcfAcc) {
        String[] param = new String[1];
        param[0] = pcfAcc.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                .getNumAvsActuel();
        JadeThread.logError(this.getClass().getName(), "perseus.statistiqueOFS.numero.commune.indetermine", param);
    }

    private Antragsteller.Row remplirDetailsAdresse(Antragsteller.Row row, AdresseTiersDetail detailTiers)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        row = remplirDetailsAdresseAdresseTrouve(detailTiers, row);
        return row;
    }

    private Antragsteller.Row remplirDetailsAdresseAdresseTrouve(AdresseTiersDetail detailTiers, Antragsteller.Row row)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        row.setZvrStrasse(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_RUE));
        row.setZvrHausNr(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NUMERO));
        row.getZvrPlz().add(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_NPA));
        row.setZvrOrt(detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE));
        String idLocalite = detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID);
        LocaliteSimpleModel localite = TIBusinessServiceLocator.getAdresseService().readLocalite(idLocalite);
        row.getZvrGemeindeId().add(localite.getNumCommuneOfs());
        return row;

    }

    private Antragsteller.Row remplirInfoDemographieRequerant(Antragsteller.Row row, PCFAccordee pcfAcc)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        row.setDatGeburt(pcfAcc.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getPersonne()
                .getDateNaissance());

        row.getGeschlechtId().add(
                StatsOFS.getValueFromCodeSystem(pcfAcc.getDemande().getDossier().getDemandePrestation()
                        .getPersonneEtendue().getPersonne().getSexe()));
        row.getZivilstandId().add(
                StatsOFS.getValueFromCodeSystem(pcfAcc.getDemande().getSituationFamiliale()
                        .getSimpleSituationFamiliale().getCsEtatCivilRequerant()));
        String codeIsoPays = getNationalite(pcfAcc.getDemande().getDossier().getDemandePrestation()
                .getPersonneEtendue().getTiers().getIdPays());
        row.setNationalitaetLandId(codeIsoPays);
        if (!StatsOFS.CODE_ISO_SUISSE.equals(codeIsoPays)) {
            // Si différent de suisse, alors renseigner le statut de séjour
            row.getAufenthaltsstatusId().add(
                    StatsOFS.getValueFromCodeSystem(pcfAcc.getDemande().getSimpleDemande().getStatutSejour()));
        }

        return row;
    }

    private Antragsteller.Row remplirTauxActivite(Antragsteller.Row row, InputCalcul inputCalcul)
            throws CalculException {
        if (inputCalcul.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.SALAIRE_NET)
                .getPlusieursEmployeurs()) {
            row.getBeschaeftigungsgradId().add(remplirTauxActiviteAvecPlusieursEmployeur(inputCalcul));
        } else {
            row.getBeschaeftigungsgradId().add(remplirTauxActiviteAvecUnEmployeur(inputCalcul));
        }
        return row;
    }

    /**
     * Récupère le taux d'occupation depuis le données financière.
     * Retourne <code>null</code> si aucun taux n'est présent
     * Sinon retourne la valeur du taux sans les décimales (ex 80.0 retournera 80)
     * 
     * @param inputCalcul Le calcul
     * @return retourne la valeur du taux sans les décimales (ex 80.0 retournera 80) ou null si aucun taux n'est présent
     * @throws CalculException
     */
    public String getTauxOccupation(InputCalcul inputCalcul) throws CalculException {
        String tauxOccupation = null;
        Float value = inputCalcul.getDonneesFinancieresRequerant().getElementRevenu(RevenuType.TAUX_OCCUPATION)
                .getValeur();
        if (value != null) {
            tauxOccupation = String.valueOf(value);
            if (tauxOccupation.contains(".")) {
                tauxOccupation = tauxOccupation.substring(0, tauxOccupation.indexOf("."));
            }
        }
        return tauxOccupation;
    }

    private String remplirTauxActiviteAvecPlusieursEmployeur(InputCalcul inputCalcul) throws CalculException {
        String tauxOccupation = getTauxOccupation(inputCalcul);
        if (tauxOccupation == null) {
            return StatsOFS.NE_SAIS_PAS;
        } else if (isPostePleinTemps(tauxOccupation)) {
            return CSTauxOccupation.POSTE_A_PLEIN_TEMPS_ET_TEMPS_PARTEL.getValue();
        } else {
            return CSTauxOccupation.PLUSIEURS_POSTES_TEMPS_PARTIEL.getValue();
        }
    }

    private String remplirTauxActiviteAvecUnEmployeur(InputCalcul inputCalcul) throws CalculException {
        String tauxOccupation = getTauxOccupation(inputCalcul);
        if (tauxOccupation == null) {
            return StatsOFS.NE_SAIS_PAS;
        } else if (isPostePleinTemps(tauxOccupation)) {
            return CSTauxOccupation.PLEIN_TEMPS.getValue();
        } else {
            return CSTauxOccupation.POSTE_TEMPS_PATIEL.getValue();
        }
    }

    public void setConteneurDateComptabilisationPrestationRetro(
            HashMap<String, String> conteneurDateComptabilisationPrestationRetro) {
        this.conteneurDateComptabilisationPrestationRetro = conteneurDateComptabilisationPrestationRetro;
    }

    public void setDossierDejaTraiter(ArrayList<String> dossierDejaTraiter) {
        this.dossierDejaTraiter = dossierDejaTraiter;
    }

    private void traitementCasAnneeEnquete(String anneeEnquete) throws JadePersistenceException,
            JadeApplicationException, JAException {
        HashMap<String, String> pcfPayee = this.loadPCFAccordee(anneeEnquete, "01.01.", "31.12.");
        for (Entry<String, String> entry : pcfPayee.entrySet()) {
            String idPcfAccordee = entry.getValue();
            if (!dossierDejaTraiter.contains(entry.getKey())) {
                traiterCas(idPcfAccordee, anneeEnquete);
            }
        }
    }

    private void traiterCas(String idPCFAccordee, String anneeEnquete) throws JadePersistenceException,
            JadeApplicationException, JAException {
        Dossier dossier = of.createDossier();
        PCFAccordee pcfacc = this.loadPCFAccordee(idPCFAccordee);
        InputCalcul inputCalcul = loadInputData(pcfacc.getDemande());
        dossier.setShFremdId(pcfacc.getDemande().getDossier().getDossier().getIdDossier());
        dossier.setBNeubezuegerrecord("1");
        AdresseTiersDetail detailTiers = getAdresseTiers(pcfacc);
        if (null != detailTiers) {
            dossier.setRow(createRow(anneeEnquete, inputCalcul, pcfacc, detailTiers));
            dossier.setAntragsteller(createAntragsteller(pcfacc, inputCalcul, detailTiers));
            dossier.setUe(createUe(inputCalcul, pcfacc));
            dossier.setWbslEinkommensart(createWbslEinkommensart(pcfacc, inputCalcul, pcfacc));
            dossiers.getDossier().add(dossier);
            dossierDejaTraiter.add(pcfacc.getDemande().getSimpleDemande().getIdDossier());
        }
    }
}
