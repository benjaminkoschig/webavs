package ch.globaz.perseus.businessimpl.services.impotsource;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.tools.PRDateFormater;
import globaz.prestation.tools.PRStringUtils;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatLot;
import ch.globaz.perseus.business.constantes.CSSexePersonne;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeListeImpotSource;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.constantes.CSTypeVersement;
import ch.globaz.perseus.business.exceptions.impotsource.ImpotSourceException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueDemandePCFAccordeeDecisionException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuType;
import ch.globaz.perseus.business.models.impotsource.PeriodeImpotSource;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.lot.LotSearchModel;
import ch.globaz.perseus.business.models.lot.OrdreVersement;
import ch.globaz.perseus.business.models.lot.OrdreVersementSearchModel;
import ch.globaz.perseus.business.models.lot.Prestation;
import ch.globaz.perseus.business.models.lot.PrestationSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.retenue.RetenueDemandePCFAccordeeDecision;
import ch.globaz.perseus.business.models.retenue.RetenueDemandePCFAccordeeDecisionSearchModel;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.models.situationfamille.EnfantFamilleSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.business.services.impotsource.ImpotSourceService;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.perseus.businessimpl.services.impotsource.jaxb.BaremeType;
import ch.globaz.perseus.businessimpl.services.impotsource.jaxb.CodeBaremeType;
import ch.globaz.perseus.businessimpl.services.impotsource.jaxb.DecompteContribuableType;
import ch.globaz.perseus.businessimpl.services.impotsource.jaxb.IdentiteType;
import ch.globaz.perseus.businessimpl.services.impotsource.jaxb.ListeImpotSource;
import ch.globaz.perseus.businessimpl.services.impotsource.jaxb.ObjectFactory;
import ch.globaz.perseus.businessimpl.services.impotsource.jaxb.SalaireType;
import ch.globaz.perseus.businessimpl.services.impotsource.jaxb.TypeActiviteType;
import ch.globaz.perseus.businessimpl.services.impotsource.jaxb.TypeListType;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.LocaliteSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class ImpotSourceServiceImpl implements ImpotSourceService {
    private static String EXTENTION_XML = ".xml";
    private static String LOCALISATION_XSD = "ch.globaz.perseus.businessimpl.services.impotsource.jaxb";
    private static String MONTANT_CREANCIER_IS = "creancier_is";
    private static String MONTANT_PCFA = "montant_pcfa";
    private static String MONTANT_RETENUE = "montant_retenue";
    private static String MONTANT_RETRO = "montant_retro";
    private static String NOM_XSD = "Liste_impot_source-4.xsd";

    private static final String PERSEUS_XSD_PATH = Jade.getInstance().getRootUrl() + "perseusRoot/xsd/";
    private static String UNDERSCORE = "_";
    private HashMap<String, String> baremePourDossier = new HashMap<String, String>();
    private HashMap<String, HashMap<String, String>> dossierATraiter = null;
    private ArrayList<String> dossierATraiterLC = null;
    private ListeImpotSource listeIp = null;
    private ArrayList<String> litIdDossierPourRetenue = new ArrayList<String>();
    private FWCurrency montantVerseIP = null;
    private ObjectFactory of = null;

    private HashMap<String, RetenueDemandePCFAccordeeDecision> retenuePourDossier = null;

    private BaremeType buildBareme(Demande demande) throws ImpotSourceException {

        try {
            BaremeType baremeType = of.createBaremeType();
            baremeType.setTypeActivite(TypeActiviteType.PRINCIPALE);
            EnfantFamilleSearchModel enfantFamilleSearchModel = new EnfantFamilleSearchModel();
            enfantFamilleSearchModel.setForIdSituationFamiliale(demande.getSituationFamiliale().getId());
            enfantFamilleSearchModel = PerseusImplServiceLocator.getEnfantFamilleService().search(
                    enfantFamilleSearchModel);
            baremeType.setNombreAllocations(new BigInteger(String.valueOf(enfantFamilleSearchModel.getSize())));
            RevenuSearchModel revSearch = new RevenuSearchModel();
            revSearch.setForIdDemande(demande.getId());
            revSearch.setForIdMembreFamille(demande.getSituationFamiliale().getRequerant().getMembreFamille().getId());
            ArrayList<String> types = new ArrayList<String>(1);
            types.add(RevenuType.TAUX_OCCUPATION.getId().toString());
            revSearch.setInType(types);
            revSearch = PerseusServiceLocator.getRevenuService().search(revSearch);

            if (revSearch.getSize() == 0) {
                baremeType.setTauxActivite(0);
            } else {
                for (JadeAbstractModel abstractModel : revSearch.getSearchResults()) {
                    Revenu revenu = (Revenu) abstractModel;
                    baremeType.setTauxActivite(Integer.parseInt(PRStringUtils.replaceString(revenu
                            .getSimpleDonneeFinanciere().getValeur(), ".00", "")));
                    break;
                }
            }

            baremeType.setCodeBareme(definirBareme(demande.getDossier().getDossier().getIdDossier()));
            return baremeType;
        } catch (Exception e) {
            throw new ImpotSourceException("ImpotSourceServieImpl - Error in buildBareme:" + e.toString(), e);
        }
    }

    private void buildEnteteCommun(String csTypeListe, String numDebiteur, String dateDeb, String dateFi)
            throws DatatypeConfigurationException {

        listeIp.setNumDebiteur(BigInteger.valueOf(Long.valueOf(numDebiteur)));
        if (CSTypeListeImpotSource.LISTE_RECAPITULATIVE.getCodeSystem().equals(csTypeListe)) {
            listeIp.setTypeListe(TypeListType.LR);
        } else {
            listeIp.setTypeListe(TypeListType.LC);
        }

        DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();
        XMLGregorianCalendar dateDebut = dataTypeFac.newXMLGregorianCalendar();
        dateDebut.setDay(Integer.parseInt("01"));
        dateDebut.setMonth(Integer.parseInt(JadeStringUtil.substring(dateDeb, 0, 2)));
        dateDebut.setYear(Integer.parseInt(JadeStringUtil.substring(dateDeb, 3, 4)));

        listeIp.setDebutPeriodeDeclaration(dateDebut);

        XMLGregorianCalendar dateFin = dataTypeFac.newXMLGregorianCalendar();
        dateFin.setDay(Integer.parseInt(JadeStringUtil.substring(JadeDateUtil.getLastDateOfMonth(dateFi), 0, 2)));
        dateFin.setMonth(Integer.parseInt(JadeStringUtil.substring(dateFi, 0, 2)));
        dateFin.setYear(Integer.parseInt(JadeStringUtil.substring(dateFi, 3, 4)));

        listeIp.setFinPeriodeDeclaration(dateFin);

    }

    private void buildFinEnteteCommun(BSession session, String csTypeListe) throws Exception {
        listeIp.setMontantBrut(new BigDecimal(montantVerseIP.toString()));

        String tauxCommission = session.getApplication().getProperty("listeIP.taux.commission");
        if (CSTypeListeImpotSource.LISTE_RECAPITULATIVE.getCodeSystem().equals(csTypeListe)) {
            if (!JadeStringUtil.isEmpty(tauxCommission)) {
                Float taux = new Float(tauxCommission);
                Float montantCommission = (montantVerseIP.floatValue() / 100) * taux;
                listeIp.setMontantCommission(new BigDecimal(JANumberFormatter.round(montantCommission.toString(), 0.05,
                        2, JANumberFormatter.NEAR)));

            } else {
                throw new Exception("ImpotSourceServieImpl - Error in buildFinEnteteCommun:"
                        + "Pas de taux de commission renseigné");
            }
        } else {
            listeIp.setMontantCommission(new BigDecimal(0));
        }

    }

    private IdentiteType buildIdentiteType(Demande demande) throws Exception {
        IdentiteType identType = of.createIdentiteType();
        identType.setNumAvs(new BigDecimal(PRStringUtils.replaceString(demande.getDossier().getDemandePrestation()
                .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel(), ".", "")));
        identType.setNom(demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1());
        identType.setPrenom(demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                .getDesignation2());
        identType.setPermisTravail("02");
        if (CSSexePersonne.MALE.getCodeSystem().equals(
                demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne().getSexe())) {
            identType.setCodeSexe("1");
        } else {
            identType.setCodeSexe("2");
        }

        AdresseTiersDetail detailTiers = PFUserHelper.getAdresseAssure(demande.getDossier().getDemandePrestation()
                .getPersonneEtendue().getTiers().getIdTiers(), AdresseService.CS_TYPE_DOMICILE,
                JACalendar.todayJJsMMsAAAA());

        if (null != detailTiers.getFields()) {
            String idLocalite = detailTiers.getFields().get(AdresseTiersDetail.ADRESSE_VAR_LOCALITE_ID);
            LocaliteSimpleModel localite = TIBusinessServiceLocator.getAdresseService().readLocalite(idLocalite);
            identType.setCommune(Integer.parseInt(localite.getNumCommuneOfs()));
        } else {
            throw new Exception("ImpotSourceServieImpl - Error in buildIdentiteType:"
                    + "Pas de localité de domicile trouvée pour le tiers : "
                    + demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel());
        }
        DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();
        XMLGregorianCalendar dateNaissance = dataTypeFac.newXMLGregorianCalendar();
        dateNaissance.setDay(Integer.parseInt(JadeStringUtil.substring(demande.getDossier().getDemandePrestation()
                .getPersonneEtendue().getPersonne().getDateNaissance(), 0, 2)));
        dateNaissance.setMonth(Integer.parseInt(JadeStringUtil.substring(demande.getDossier().getDemandePrestation()
                .getPersonneEtendue().getPersonne().getDateNaissance(), 3, 2)));
        dateNaissance.setYear(Integer.parseInt(JadeStringUtil.substring(demande.getDossier().getDemandePrestation()
                .getPersonneEtendue().getPersonne().getDateNaissance(), 6, 4)));
        identType.setDateNaissance(dateNaissance);
        return identType;
    }

    private SalaireType buildSalaire(Demande demande, String dateDebutPeriode, String dateFinPeriode)
            throws ImpotSourceException, DatatypeConfigurationException {
        SalaireType salType = of.createSalaireType();
        salType.setBareme(buildBareme(demande));
        salType.setNoSequenceSalaire(new BigInteger("1"));
        salType.setRevenuNonProportionel(new BigDecimal(0));

        FWCurrency montantIp = new FWCurrency(0);

        montantIp.add(dossierATraiter.get(demande.getDossier().getId())
                .get(ImpotSourceServiceImpl.MONTANT_CREANCIER_IS));
        montantIp.add(dossierATraiter.get(demande.getDossier().getId()).get(ImpotSourceServiceImpl.MONTANT_RETENUE));

        montantVerseIP.add(montantIp);

        salType.setRetenueDImpot(new BigDecimal(String.valueOf(montantIp)));

        FWCurrency montantPrestation = new FWCurrency();
        montantPrestation.add(dossierATraiter.get(demande.getDossier().getId())
                .get(ImpotSourceServiceImpl.MONTANT_PCFA));
        montantPrestation.add(dossierATraiter.get(demande.getDossier().getId()).get(
                ImpotSourceServiceImpl.MONTANT_RETRO));
        salType.setSalaireVerseOuPrestationImposable(new BigDecimal(montantPrestation.toString()));

        DatatypeFactory dataTypeFac = DatatypeFactory.newInstance();
        XMLGregorianCalendar dateDebut = dataTypeFac.newXMLGregorianCalendar();
        dateDebut.setDay(Integer.parseInt("01"));
        dateDebut.setMonth(Integer.parseInt(JadeStringUtil.substring(dateDebutPeriode, 0, 2)));
        dateDebut.setYear(Integer.parseInt(JadeStringUtil.substring(dateDebutPeriode, 3, 4)));

        salType.setDebutVersement(dateDebut);

        XMLGregorianCalendar dateFin = dataTypeFac.newXMLGregorianCalendar();
        dateFin.setDay(Integer.parseInt(JadeStringUtil.substring(JadeDateUtil.getLastDateOfMonth(dateFinPeriode), 0, 2)));
        dateFin.setMonth(Integer.parseInt(JadeStringUtil.substring(dateFinPeriode, 0, 2)));
        dateFin.setYear(Integer.parseInt(JadeStringUtil.substring(dateFinPeriode, 3, 4)));

        salType.setFinVersement(dateFin);

        return salType;
    }

    private String createAndValidationFichierXML(String fileName) throws JAXBException, MalformedURLException,
            SAXException {
        JAXBContext context = JAXBContext.newInstance(ImpotSourceServiceImpl.LOCALISATION_XSD);
        Marshaller m = context.createMarshaller();
        m.setSchema(getSchema());
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        String path = Jade.getInstance().getSharedDir();
        File xmlFile = new File(path + File.separatorChar + fileName);
        m.marshal(listeIp, xmlFile);
        return xmlFile.getPath();
    }

    private CodeBaremeType definirBareme(String idDossier) throws RetenueDemandePCFAccordeeDecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        String bareme = baremePourDossier.get(idDossier);
        if (!JadeStringUtil.isEmpty(bareme)) {
            return CodeBaremeType.fromValue(bareme);
        } else {
            if (retenuePourDossier.containsKey(idDossier)) {
                RetenueDemandePCFAccordeeDecision retenue = retenuePourDossier.get(idDossier);
                return CodeBaremeType.fromValue(retenue.getSimpleRetenue().getBaremeIS());
            } else {
                throw new RetenueDemandePCFAccordeeDecisionException(
                        "Impossible de déterminer le barème pour le dossier : " + idDossier);
            }
        }
    }

    @Override
    public String genererLC(BSession session, String anneeLC, String numDebiteur) throws ImpotSourceException,
            JadePersistenceException {
        try {
            if (JadeStringUtil.isEmpty(anneeLC)) {
                throw new ImpotSourceException(session.getLabel("PROCESS_PF_GENERATION_LISTE_IP_PAS_DE_PERIODE"));
            } else if (JadeStringUtil.isEmpty(numDebiteur)) {
                throw new ImpotSourceException(
                        session.getLabel("PROCESS_PF_GENERATION_LISTE_IP_PAS_DE_NUMERO_DEBITEUR"));
            } else {
                montantVerseIP = new FWCurrency();
                dossierATraiter = new HashMap<String, HashMap<String, String>>();
                dossierATraiterLC = new ArrayList<String>();
                of = new ObjectFactory();
                // Identifier tous les cas qui ont un OV de type impot source négatif et regénérer la liste
                // recapitulative pour tous ces cas
                // Faire une liste des mois de la demande pour les montants déjà versés dans le mois
                ArrayList<String> listMois = new ArrayList<String>();
                String moisCourant = "01.01." + anneeLC;
                String dateFin = "31.12." + anneeLC;
                while (!JadeDateUtil.isDateMonthYearAfter(moisCourant.substring(3), dateFin.substring(3))) {
                    listMois.add(moisCourant.substring(3));
                    moisCourant = JadeDateUtil.addMonths(moisCourant, 1);
                }

                for (String mois : listMois) {
                    LotSearchModel lotSearchModel = new LotSearchModel();
                    lotSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                    lotSearchModel.setForTypeLot(CSTypeLot.LOT_DECISION.getCodeSystem());
                    lotSearchModel.setForMoisComptabilisation(mois);
                    lotSearchModel.setForEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
                    lotSearchModel = PerseusServiceLocator.getLotService().search(lotSearchModel);
                    for (JadeAbstractModel model : lotSearchModel.getSearchResults()) {
                        Lot lot = (Lot) model;
                        PrestationSearchModel psm = new PrestationSearchModel();
                        psm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        psm.setForIdLot(lot.getId());
                        psm.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
                        psm = PerseusServiceLocator.getPrestationService().search(psm);
                        for (JadeAbstractModel m : psm.getSearchResults()) {
                            Prestation p = (Prestation) m;
                            if (Float.parseFloat(p.getSimplePrestation().getMontantTotal()) < 0) {
                                OrdreVersementSearchModel ordreVersementSearchModel = new OrdreVersementSearchModel();
                                ordreVersementSearchModel.setForIdPrestation(p.getSimplePrestation().getIdPrestation());
                                ordreVersementSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                                ordreVersementSearchModel = PerseusServiceLocator.getOrdreVersementService().search(
                                        ordreVersementSearchModel);
                                for (JadeAbstractModel ovModel : ordreVersementSearchModel.getSearchResults()) {
                                    OrdreVersement ov = (OrdreVersement) ovModel;
                                    if (CSTypeVersement.IMPOT_A_LA_SOURCE.getCodeSystem().equals(
                                            ov.getSimpleOrdreVersement().getCsTypeVersement())
                                            && (Float.parseFloat(ov.getSimpleOrdreVersement().getMontantVersement()) < 0)) {
                                        dossierATraiterLC.add(p.getDecision().getDemande().getDossier().getId());
                                    }
                                }
                            }
                        }
                    }
                }

                listeIp = of.createListeImpotSource();
                buildEnteteCommun(CSTypeListeImpotSource.LISTE_CORRECTIVE.getCodeSystem(), numDebiteur,
                        "01." + anneeLC, "12." + anneeLC);

                if (!dossierATraiterLC.isEmpty()) {
                    loadRetenueForIdDossiers(dossierATraiterLC);

                    Integer numSeq = 1;

                    LoadPCFAccordessWithRetenuIS("01." + anneeLC, "12." + anneeLC,
                            CSTypeListeImpotSource.LISTE_CORRECTIVE.getCodeSystem());

                    for (String idDossier : dossierATraiter.keySet()) {

                        FWCurrency montantPrestation = new FWCurrency(dossierATraiter.get(idDossier).get(
                                ImpotSourceServiceImpl.MONTANT_PCFA));
                        montantPrestation.add(dossierATraiter.get(idDossier).get(ImpotSourceServiceImpl.MONTANT_RETRO));

                        if (montantPrestation.isNegative()) {
                            dossierATraiter.get(idDossier).put(ImpotSourceServiceImpl.MONTANT_PCFA, "0");
                            dossierATraiter.get(idDossier).put(ImpotSourceServiceImpl.MONTANT_RETRO, "0");
                        }

                        Demande demande = PerseusServiceLocator.getDemandeService().getDerniereDemande(idDossier);
                        DecompteContribuableType decompteContrib = of.createDecompteContribuableType();
                        decompteContrib.setNoSequenceDecompte(new BigInteger(String.valueOf(numSeq)));
                        numSeq++;
                        decompteContrib.setIdentite(buildIdentiteType(demande));
                        decompteContrib.getSalaire().add(buildSalaire(demande, "01." + anneeLC, "12." + anneeLC));
                        listeIp.getDecompteContribuable().add(decompteContrib);

                    }
                }
                buildFinEnteteCommun(session, CSTypeListeImpotSource.LISTE_CORRECTIVE.getCodeSystem());

                StringBuilder fileName = new StringBuilder();
                fileName.append(TypeListType.LC);
                fileName.append(ImpotSourceServiceImpl.UNDERSCORE);
                fileName.append(numDebiteur);
                fileName.append(anneeLC);
                fileName.append(ImpotSourceServiceImpl.UNDERSCORE);
                fileName.append("01");
                fileName.append(JadeStringUtil.substring("01." + anneeLC, 0, 2));
                fileName.append(ImpotSourceServiceImpl.UNDERSCORE);
                fileName.append(JadeStringUtil.substring(
                        PRStringUtils.replaceString(JadeDateUtil.getLastDateOfMonth("12." + anneeLC), ".", ""), 0, 4));
                fileName.append(ImpotSourceServiceImpl.EXTENTION_XML);

                return createAndValidationFichierXML(fileName.toString());

            }
        } catch (JAXBException e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            String msgError = "";
            if (!(e.getLinkedException() == null)) {
                SAXParseException se = (SAXParseException) e.getLinkedException();
                msgError = "Erreur fichier non valide ! ==>" + se.getMessage();
                JadeThread.logError(this.getClass().getName(), msgError);
            }
        } catch (Exception e) {
            throw new ImpotSourceException("Exception : " + e.toString(), e);
        }
        return null;
    }

    @Override
    public String genererLR(BSession session, String idPeriode, String numDebiteur) throws ImpotSourceException,
            JadePersistenceException, Exception {
        try {
            if (JadeStringUtil.isBlank(idPeriode)) {
                throw new ImpotSourceException(session.getLabel("PROCESS_PF_GENERATION_LISTE_IP_PAS_DE_PERIODE"));
            } else if (JadeStringUtil.isEmpty(numDebiteur) || "null".equals(numDebiteur)) {
                throw new ImpotSourceException(
                        session.getLabel("PROCESS_PF_GENERATION_LISTE_IP_PAS_DE_NUMERO_DEBITEUR"));
            } else {
                montantVerseIP = new FWCurrency();
                dossierATraiter = new HashMap<String, HashMap<String, String>>();
                of = new ObjectFactory();
                listeIp = of.createListeImpotSource();

                Integer numSeq = 1;

                PeriodeImpotSource periode = PerseusServiceLocator.getPeriodeImpotSourceService().read(idPeriode);
                buildEnteteCommun(CSTypeListeImpotSource.LISTE_RECAPITULATIVE.getCodeSystem(), numDebiteur, periode
                        .getSimplePeriodeImpotSource().getDateDebut(), periode.getSimplePeriodeImpotSource()
                        .getDateFin());

                LoadCasIS(periode, CSTypeListeImpotSource.LISTE_RECAPITULATIVE.getCodeSystem());
                loadRetenueForIdDossiers(litIdDossierPourRetenue);

                for (String idDossier : dossierATraiter.keySet()) {

                    FWCurrency montantTotalVersement = new FWCurrency();

                    montantTotalVersement.add(dossierATraiter.get(idDossier).get(
                            ImpotSourceServiceImpl.MONTANT_CREANCIER_IS));
                    montantTotalVersement.add(dossierATraiter.get(idDossier).get(ImpotSourceServiceImpl.MONTANT_PCFA));
                    montantTotalVersement.add(dossierATraiter.get(idDossier)
                            .get(ImpotSourceServiceImpl.MONTANT_RETENUE));
                    montantTotalVersement.add(dossierATraiter.get(idDossier).get(ImpotSourceServiceImpl.MONTANT_RETRO));

                    if (montantTotalVersement.isPositive()) {
                        Demande demande = PerseusServiceLocator.getDemandeService().getDerniereDemande(idDossier);

                        DecompteContribuableType decompteContrib = of.createDecompteContribuableType();
                        decompteContrib.setNoSequenceDecompte(new BigInteger(String.valueOf(numSeq)));
                        numSeq++;
                        decompteContrib.setIdentite(buildIdentiteType(demande));
                        decompteContrib.getSalaire().add(
                                buildSalaire(demande, periode.getSimplePeriodeImpotSource().getDateDebut(), periode
                                        .getSimplePeriodeImpotSource().getDateFin()));
                        listeIp.getDecompteContribuable().add(decompteContrib);
                    }

                }

                buildFinEnteteCommun(session, CSTypeListeImpotSource.LISTE_RECAPITULATIVE.getCodeSystem());

                StringBuilder fileName = new StringBuilder();
                fileName.append(TypeListType.LR);
                fileName.append(ImpotSourceServiceImpl.UNDERSCORE);
                fileName.append(numDebiteur);
                fileName.append(ImpotSourceServiceImpl.UNDERSCORE);
                fileName.append(PRDateFormater.convertDate_MMxAAAA_to_AAAA(periode.getSimplePeriodeImpotSource()
                        .getDateDebut()));
                fileName.append(ImpotSourceServiceImpl.UNDERSCORE);
                fileName.append("01");
                fileName.append(JadeStringUtil.substring(periode.getSimplePeriodeImpotSource().getDateDebut(), 0, 2));
                fileName.append(ImpotSourceServiceImpl.UNDERSCORE);
                fileName.append(JadeStringUtil.substring(PRStringUtils.replaceString(
                        JadeDateUtil.getLastDateOfMonth(periode.getSimplePeriodeImpotSource().getDateFin()), ".", ""),
                        0, 4));
                fileName.append(ImpotSourceServiceImpl.EXTENTION_XML);

                return createAndValidationFichierXML(fileName.toString());
            }
        } catch (JAXBException e) {
            JadeThread.logError(this.getClass().getName(), e.getMessage());
            String msgError = "";
            if (!(e.getLinkedException() == null)) {
                SAXParseException se = (SAXParseException) e.getLinkedException();
                msgError = "Erreur fichier non valide ! ==>" + se.getMessage();
                JadeThread.logError(this.getClass().getName(), msgError);
            }
        }

        return null;

    }

    private Schema getSchema() throws MalformedURLException, SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return schemaFactory.newSchema(new URL(url()));
    }

    private void LoadCasIS(PeriodeImpotSource periode, String typeListe) throws JadePersistenceException,
            JadeApplicationException {
        LoadPCFAccordessWithRetenuIS(periode.getSimplePeriodeImpotSource().getDateDebut(), periode
                .getSimplePeriodeImpotSource().getDateFin(), typeListe);
    }

    private void LoadPCFAccordessWithRetenuIS(String dateDebut, String dateFin, String typeListe)
            throws JadePersistenceException, JadeApplicationException {

        List<String> listeMoisPrestMensuelle = new ArrayList<String>();
        String enCoursPrestMensuelle = "01." + dateDebut;

        String moisDebutPourPrestationMensuelle = "";

        if (JadeDateUtil.isDateAfter("01." + dateFin, "01."
                + PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt())) {
            moisDebutPourPrestationMensuelle = PerseusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        } else {
            moisDebutPourPrestationMensuelle = dateFin;
        }

        while (!JadeDateUtil.isDateAfter(enCoursPrestMensuelle, "01." + moisDebutPourPrestationMensuelle)) {
            listeMoisPrestMensuelle.add(enCoursPrestMensuelle.substring(3));
            enCoursPrestMensuelle = JadeDateUtil.addMonths(enCoursPrestMensuelle, 1);
        }

        for (String moisEnCours : listeMoisPrestMensuelle) {
            // Compter les préstation mensuelles versées pendant le mois
            List<PCFAccordee> prestationsMensuelles = PerseusServiceLocator.getPmtMensuelService().listPCFAencours(
                    moisEnCours);
            for (PCFAccordee pcfa : prestationsMensuelles) {

                if (CSTypeListeImpotSource.LISTE_CORRECTIVE.getCodeSystem().equals(typeListe)) {
                    if (!dossierATraiterLC.contains(pcfa.getDemande().getDossier().getId())) {
                        continue;
                    }
                }

                SimpleRetenueSearchModel searchModel = new SimpleRetenueSearchModel();
                searchModel.setForIdPcfAccordee(pcfa.getId());
                searchModel.setForCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
                searchModel = PerseusImplServiceLocator.getSimpleRetenueService().search(searchModel);
                for (JadeAbstractModel model : searchModel.getSearchResults()) {
                    SimpleRetenue retenue = (SimpleRetenue) model;
                    // Cas à prendre en compte car payer durant la période avec une retenue IS
                    if (!dossierATraiter.containsKey(pcfa.getDemande().getDossier().getId())) {
                        HashMap<String, String> hasMap = new HashMap<String, String>();
                        hasMap.put(ImpotSourceServiceImpl.MONTANT_PCFA, pcfa.getSimplePCFAccordee().getMontant());
                        hasMap.put(ImpotSourceServiceImpl.MONTANT_RETENUE, retenue.getMontantRetenuMensuel());
                        dossierATraiter.put(pcfa.getDemande().getDossier().getId(), hasMap);
                    } else {
                        FWCurrency montantPCFATotal = new FWCurrency(dossierATraiter.get(
                                pcfa.getDemande().getDossier().getId()).get(ImpotSourceServiceImpl.MONTANT_PCFA));
                        FWCurrency montantRetenuTotal = new FWCurrency(dossierATraiter.get(
                                pcfa.getDemande().getDossier().getId()).get(ImpotSourceServiceImpl.MONTANT_RETENUE));
                        montantPCFATotal.add(pcfa.getSimplePCFAccordee().getMontant());
                        montantRetenuTotal.add(retenue.getMontantRetenuMensuel());
                        dossierATraiter.get(pcfa.getDemande().getDossier().getId()).put(
                                ImpotSourceServiceImpl.MONTANT_PCFA, montantPCFATotal.toString());
                        dossierATraiter.get(pcfa.getDemande().getDossier().getId()).put(
                                ImpotSourceServiceImpl.MONTANT_RETENUE, montantRetenuTotal.toString());
                    }
                    baremePourDossier.put(pcfa.getDemande().getDossier().getDossier().getIdDossier(),
                            retenue.getBaremeIS());
                }
            }
        }
        loadRetroWithIS(listeMoisPrestMensuelle, typeListe, false);
    }

    private void loadRetenueForIdDossiers(List<String> listIdDossiers)
            throws RetenueDemandePCFAccordeeDecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        List<String> listeIdDossiersPourRetenue = new ArrayList<String>();
        for (String idDossier : listIdDossiers) {
            if (!listeIdDossiersPourRetenue.contains(idDossier)) {
                listeIdDossiersPourRetenue.add(idDossier);
            }
        }

        retenuePourDossier = new HashMap<String, RetenueDemandePCFAccordeeDecision>();
        RetenueDemandePCFAccordeeDecisionSearchModel retenueSearch = new RetenueDemandePCFAccordeeDecisionSearchModel();
        retenueSearch.setForIdDossiersIn(listeIdDossiersPourRetenue);
        retenueSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        retenueSearch.setForCsEtatDecision(CSEtatDecision.VALIDE.getCodeSystem());
        retenueSearch.setForCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
        retenueSearch
                .setOrderKey(RetenueDemandePCFAccordeeDecisionSearchModel.DATEDEBUT_DATEVALIDATION_NUMERODECISION_DESC);
        retenueSearch = PerseusServiceLocator.getRetenueDemandePCFAccordeeDecisionService().search(retenueSearch);
        for (JadeAbstractModel model : retenueSearch.getSearchResults()) {
            RetenueDemandePCFAccordeeDecision retenue = (RetenueDemandePCFAccordeeDecision) model;
            if (!retenuePourDossier.containsKey(retenue.getIdDossier())) {
                retenuePourDossier.put(retenue.getIdDossier(), retenue);
            }
        }
    }

    private void loadRetroWithIS(List<String> listeMois, String typeListe, boolean restit)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        for (String mois : listeMois) {
            LotSearchModel lotSearchModel = new LotSearchModel();
            lotSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            lotSearchModel.setForTypeLot(CSTypeLot.LOT_DECISION.getCodeSystem());
            lotSearchModel.setForMoisComptabilisation(mois);
            lotSearchModel.setForEtatCs(CSEtatLot.LOT_VALIDE.getCodeSystem());
            lotSearchModel = PerseusServiceLocator.getLotService().search(lotSearchModel);
            for (JadeAbstractModel model : lotSearchModel.getSearchResults()) {
                Lot lot = (Lot) model;
                PrestationSearchModel psm = new PrestationSearchModel();
                psm.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                psm.setForIdLot(lot.getId());
                psm.getInTypeLot().add(CSTypeLot.LOT_DECISION.getCodeSystem());
                psm = PerseusServiceLocator.getPrestationService().search(psm);
                for (JadeAbstractModel m : psm.getSearchResults()) {
                    Prestation p = (Prestation) m;

                    if (CSTypeListeImpotSource.LISTE_CORRECTIVE.getCodeSystem().equals(typeListe)) {
                        if (!dossierATraiterLC.contains(p.getDecision().getDemande().getDossier().getId())) {
                            continue;
                        }
                    }

                    OrdreVersementSearchModel ordreVersementSearchModel = new OrdreVersementSearchModel();
                    ordreVersementSearchModel.setForIdPrestation(p.getSimplePrestation().getIdPrestation());
                    ordreVersementSearchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                    ordreVersementSearchModel = PerseusServiceLocator.getOrdreVersementService().search(
                            ordreVersementSearchModel);
                    for (JadeAbstractModel ovModel : ordreVersementSearchModel.getSearchResults()) {
                        OrdreVersement ov = (OrdreVersement) ovModel;

                        if (Float.parseFloat(ov.getSimpleOrdreVersement().getMontantVersement()) > 0) {
                            if (CSTypeVersement.IMPOT_A_LA_SOURCE.getCodeSystem().equals(
                                    ov.getSimpleOrdreVersement().getCsTypeVersement())) {
                                // Prise en compte du cas rétro avec IS
                                if (!dossierATraiter.containsKey(p.getDecision().getDemande().getDossier().getId())) {
                                    HashMap<String, String> hasMap = new HashMap<String, String>();
                                    hasMap.put(ImpotSourceServiceImpl.MONTANT_CREANCIER_IS, ov
                                            .getSimpleOrdreVersement().getMontantVersement());
                                    hasMap.put(ImpotSourceServiceImpl.MONTANT_RETRO, p.getSimplePrestation()
                                            .getMontantTotal());
                                    dossierATraiter.put(p.getDecision().getDemande().getDossier().getId(), hasMap);
                                    litIdDossierPourRetenue.add(p.getDecision().getDemande().getDossier().getId());
                                } else {
                                    FWCurrency montantCreanISTotal = new FWCurrency(dossierATraiter.get(
                                            p.getDecision().getDemande().getDossier().getId()).get(
                                            ImpotSourceServiceImpl.MONTANT_CREANCIER_IS));
                                    FWCurrency montantRetroTotal = new FWCurrency(dossierATraiter.get(
                                            p.getDecision().getDemande().getDossier().getId()).get(
                                            ImpotSourceServiceImpl.MONTANT_RETRO));
                                    montantCreanISTotal.add(ov.getSimpleOrdreVersement().getMontantVersement());
                                    montantRetroTotal.add(p.getSimplePrestation().getMontantTotal());
                                    dossierATraiter.get(p.getDecision().getDemande().getDossier().getId())
                                            .put(ImpotSourceServiceImpl.MONTANT_CREANCIER_IS,
                                                    montantCreanISTotal.toString());
                                    dossierATraiter.get(p.getDecision().getDemande().getDossier().getId()).put(
                                            ImpotSourceServiceImpl.MONTANT_RETRO, montantRetroTotal.toString());
                                }
                            }
                        } else if ((Float.parseFloat(ov.getSimpleOrdreVersement().getMontantVersement()) < 0)
                                && CSTypeListeImpotSource.LISTE_CORRECTIVE.getCodeSystem().equals(typeListe)) {
                            if (CSTypeVersement.IMPOT_A_LA_SOURCE.getCodeSystem().equals(
                                    ov.getSimpleOrdreVersement().getCsTypeVersement())) {
                                // Prise en compte du cas rétro avec IS
                                if (!dossierATraiter.containsKey(p.getDecision().getDemande().getDossier().getId())) {
                                    HashMap<String, String> hasMap = new HashMap<String, String>();
                                    hasMap.put(ImpotSourceServiceImpl.MONTANT_CREANCIER_IS, ov
                                            .getSimpleOrdreVersement().getMontantVersement());
                                    hasMap.put(ImpotSourceServiceImpl.MONTANT_RETRO, p.getSimplePrestation()
                                            .getMontantTotal());
                                    dossierATraiter.put(p.getDecision().getDemande().getDossier().getId(), hasMap);
                                    litIdDossierPourRetenue.add(p.getDecision().getDemande().getDossier().getId());
                                } else {
                                    FWCurrency montantCreanISTotal = new FWCurrency(dossierATraiter.get(
                                            p.getDecision().getDemande().getDossier().getId()).get(
                                            ImpotSourceServiceImpl.MONTANT_CREANCIER_IS));
                                    FWCurrency montantRetroTotal = new FWCurrency(dossierATraiter.get(
                                            p.getDecision().getDemande().getDossier().getId()).get(
                                            ImpotSourceServiceImpl.MONTANT_RETRO));
                                    montantCreanISTotal.add(ov.getSimpleOrdreVersement().getMontantVersement());
                                    montantRetroTotal.add(p.getSimplePrestation().getMontantTotal());
                                    dossierATraiter.get(p.getDecision().getDemande().getDossier().getId())
                                            .put(ImpotSourceServiceImpl.MONTANT_CREANCIER_IS,
                                                    montantCreanISTotal.toString());
                                    dossierATraiter.get(p.getDecision().getDemande().getDossier().getId()).put(
                                            ImpotSourceServiceImpl.MONTANT_RETRO, montantRetroTotal.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String url() {
        StringBuilder url = new StringBuilder();
        url.append(ImpotSourceServiceImpl.PERSEUS_XSD_PATH);
        url.append(ImpotSourceServiceImpl.NOM_XSD);
        return url.toString();
    }
}
