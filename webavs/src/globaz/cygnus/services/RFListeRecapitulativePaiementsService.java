/*
 * Créé le 16 janvier 2012
 */
package globaz.cygnus.services;

import globaz.cygnus.api.TypesDeSoins.IRFTypesDeSoins;
import globaz.cygnus.application.RFApplication;
import globaz.cygnus.mappingXmlml.IRFListeRecapitulationPaiementsListeColumns;
import globaz.cygnus.mappingXmlml.RFXmlmlMappingListeRecapitulativePaiementsEtMutations;
import globaz.cygnus.process.RFListeRecapitulativePaiementsProcess;
import globaz.cygnus.utils.RFExcelmlUtils;
import globaz.cygnus.utils.RFPropertiesUtils;
import globaz.cygnus.utils.RFXmlmlContainer;
import globaz.framework.util.FWMemoryLog;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MBO
 */
public class RFListeRecapitulativePaiementsService {

    private String adresseMail = null;
    private String datePeriode = null;
    private FWMemoryLog log;

    private List<String[]> logsList = new ArrayList<String[]>();

    private RFListeRecapitulativePaiementsProcess process = null;
    private BSession session = null;
    private BITransaction transaction = null;

    /**
     * Constructeur vide
     */
    public RFListeRecapitulativePaiementsService() {

    }

    /**
     * Constructeur pour charger les variables avec les propriétés reçues en paramètre
     * 
     * @param adresseEmail
     * @param session
     * @param transaction
     * @param process
     */
    public RFListeRecapitulativePaiementsService(String adresseEmail, BSession session, BITransaction transaction,
            RFListeRecapitulativePaiementsProcess process) {
        super();
        adresseMail = adresseEmail;
        this.session = session;
        this.transaction = transaction;
        this.process = process;
    }

    /**
     * Initialisation du contexte
     * 
     * @throws Exception
     */
    public void executerListeRecapitulativePaiements() throws Exception {

        RFXmlmlMappingListeRecapitulativePaiementsEtMutations generateurXmlml = new RFXmlmlMappingListeRecapitulativePaiementsEtMutations();
        generateurXmlml.setSession(session);
        generateurXmlml.setDatePeriode(datePeriode);
        RFXmlmlContainer container = generateurXmlml.loadResults(process);

        String nomDoc = getSession().getLabel("PROCESS_LISTE_RECAP_PAIEMENTS");

        // Chargement du modèle .xml dans le docPath selon propriété en DB
        String docPath = "";
        // Si propriété à FALSE, chargement du modèle de base
        if (RFPropertiesUtils.utiliserListeRecapComplete()) {
            docPath = RFExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                    + IRFListeRecapitulationPaiementsListeColumns.Y_MODEL_NAME_FULL, nomDoc, container);
        }
        // Sinon, chargement du modèle complet
        else {
            docPath = RFExcelmlUtils.createDocumentExcel(getSession().getIdLangueISO().toUpperCase() + "/"
                    + IRFListeRecapitulationPaiementsListeColumns.Y_MODEL_NAME, nomDoc, container);
        }

        // Publication du document
        JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();
        docInfo.setApplicationDomain(RFApplication.DEFAULT_APPLICATION_CYGNUS);
        docInfo.setOwnerEmail(adresseMail);
        docInfo.setDocumentTitle(nomDoc);
        docInfo.setDocumentSubject(nomDoc);
        docInfo.setDocumentNotes(getSession().getLabel("PROCESS_LISTE_RECAP_PAIEMENTS"));
        docInfo.setPublishDocument(true);
        docInfo.setArchiveDocument(false);
        process.registerAttachedDocument(docInfo, docPath);

    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public String getDatePeriode() {
        return datePeriode;
    }

    public List<String[]> getListeTypesDeSoinsSupp() {
        // Création d'une liste
        List<String[]> listeTypesSoins = new ArrayList<String[]>();

        // Type de soins souhaités
        String[] cotisationParitaire = { IRFTypesDeSoins.CS_COTISATIONS_PARITAIRES_01 };
        String[] pensionHomeJour = { IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12 + "."
                + IRFTypesDeSoins.st_12_PENSION_HOME_DE_JOUR_14OMPC };
        String[] uniteAccueilTemporaire = { IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12 + "."
                + IRFTypesDeSoins.st_12_UNITE_D_ACCUEIL_TEMPORAIRE };
        String[] participationCourtsejour = { IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12 + "."
                + IRFTypesDeSoins.st_12_COURT_SEJOUR };
        String[] fraisPensionCourtSejour = { IRFTypesDeSoins.CS_STRUCTURE_ET_SEJOURS_12 + "."
                + IRFTypesDeSoins.st_12_SEJOUR_DE_CONVALESCENCE };
        String[] soinsParLaFamille = { IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13 + "."
                + IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC };
        String[] aideAuMenage = {
                IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13 + "." + IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE,
                IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13 + "."
                        + IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL,
                IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13 + "."
                        + IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS,
                IRFTypesDeSoins.CS_MAINTIEN_A_DOMICILE_13 + "."
                        + IRFTypesDeSoins.st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD };
        String[] encadrementSocioEduc = { IRFTypesDeSoins.CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14 };
        String[] traitementDentaire = { IRFTypesDeSoins.CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15 };
        String[] transport = { IRFTypesDeSoins.CS_FRAIS_DE_TRANSPORT_16 };
        String[] franchiseEtParticpation = { IRFTypesDeSoins.CS_FRANCHISE_ET_QUOTEPARTS_17 };

        // Remplissage de la liste
        listeTypesSoins.add(franchiseEtParticpation);
        listeTypesSoins.add(cotisationParitaire);
        listeTypesSoins.add(pensionHomeJour);
        listeTypesSoins.add(uniteAccueilTemporaire);
        listeTypesSoins.add(participationCourtsejour);
        listeTypesSoins.add(fraisPensionCourtSejour);
        listeTypesSoins.add(soinsParLaFamille);
        listeTypesSoins.add(aideAuMenage);
        listeTypesSoins.add(encadrementSocioEduc);
        listeTypesSoins.add(traitementDentaire);
        listeTypesSoins.add(transport);

        return listeTypesSoins;
    }

    public BSession getSession() {
        return session;
    }

    public BITransaction getTransaction() {
        return transaction;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public void setDatePeriode(String datePeriode) {
        this.datePeriode = datePeriode;
    }

    public void setLogsList(List<String[]> logsList) {
        this.logsList = logsList;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setTransaction(BITransaction transaction) {
        this.transaction = transaction;
    }

}