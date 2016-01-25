package globaz.al.process.declarationVersement;

import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALConstAttestattionVersees;
import ch.globaz.al.business.exceptions.declarationVersement.ALAttestationVersementException;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexSearchModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * Process de génération des attestations de versement
 * 
 * @author pta
 * 
 */

public class ALAttestationsVersementEditionProcess extends ALAbsrtactProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * inclure les prestations en paiements direcs
     */
    private boolean avecVersementDirec = true;
    /**
     * inclure les prestations indirects
     */
    private boolean avecVersementIndirect = true;
    /**
     * inclure les prestations à tiers bénéficiaire
     */
    private boolean avecVersementTiersBeneficiaire = true;
    /**
     * Date comptable début
     */
    private String dateDebut = null;
    /**
     * Date comptable fin
     */
    private String dateFin = null;
    /**
     * Document détaillé par allocataire
     */
    private boolean documentDetailleParAllocataire = true;
    /**
     * Document détaillé par période
     */
    private boolean documentNonDetaillePeriode = true;
    /**
     * email (par défaut celle de l'utilisateur)
     */
    private String email = null;
    /**
     * nss allocataire
     */
    private String nssAllocataire = null;

    /**
     * numéro de l'affilié
     */
    private String numAffilie = null;

    /**
     * Méthode qui créer et envoi les fichiers
     * 
     * @param documentCsv
     * @param fileNameDetail
     */
    private void createFileCsv(String documentCsv, String fileNameDetail) {
        FileOutputStream fichier;

        try {

            fichier = new FileOutputStream(fileNameDetail);

            fichier.write(documentCsv.getBytes());

            fichier.flush();
            fichier.close();

        } catch (Exception e) {
            getLogSession().error(this.getClass().getName(),
                    "Une erreur s'est produite pendant l'exécution du process : " + e.getMessage());
            JadeLogger.error(this.getClass().getName(), "Une erreur s'est produite pendant l'exécution du process : "
                    + e.getMessage());
        }
    }

    private void creationEtEnvoiFichier(String documentDetaille, String documentNonDetaille)
            throws JadeApplicationException, JadePersistenceException {

        if (documentDetaille != null) {

            String fileNameDetail = "prestationVerseesDetaillees.csv";

            createFileCsv(documentDetaille, fileNameDetail);

            try {
                envoiDocument(fileNameDetail);
            } catch (IOException e) {
                getLogSession().error(this.getClass().getName(),
                        "Une erreur s'est produite pendant l'exécution du process : " + e.getMessage());
                JadeLogger.error(this.getClass().getName(),
                        "Une erreur s'est produite pendant l'exécution du process : " + e.getMessage());
            }

            File file = new File(fileNameDetail);
            file.delete();
        }

        if (documentNonDetaille != null) {
            String fileNameNonDetaille = "prestationVerseesNonDetaillees.csv";
            createFileCsv(documentNonDetaille, fileNameNonDetaille);

            try {
                envoiDocument(fileNameNonDetaille);
            } catch (IOException e) {
                getLogSession().error(this.getClass().getName(),
                        "Une erreur s'est produite pendant l'exécution du process : " + e.getMessage());
                JadeLogger.error(this.getClass().getName(),
                        "Une erreur s'est produite pendant l'exécution du process : " + e.getMessage());
            }

            File file = new File(fileNameNonDetaille);
            file.delete();
        }
    }

    /**
     * @throws IOException
     */
    private void envoiDocument(String fileName) throws IOException {
        JadePublishDocumentInfo logInfo = new JadePublishDocumentInfo();
        logInfo.setOwnerEmail(getEmail());
        logInfo.setDocumentTitle("Liste prestations verseés");
        logInfo.setDocumentSubject("Liste prestations verseés");
        logInfo.setArchiveDocument(false);
        JadePublishDocument docInfoCSV = new JadePublishDocument(fileName, logInfo);

        try {
            JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfoCSV));
        } catch (Exception e) {
            getLogSession().error(this.getClass().getName(),
                    "Une erreur s'est produite pendant l'exécution du process : " + e.getMessage());
            JadeLogger.error(this.getClass().getName(), "Une erreur s'est produite pendant l'exécution du process : "
                    + e.getMessage());
        }
    }

    /**
     * @return the avecVersementDirec
     */
    public boolean getAvecVersementDirec() {
        return avecVersementDirec;
    }

    /**
     * @return the avecVersementIndirect
     */
    public boolean getAvecVersementIndirect() {
        return avecVersementIndirect;
    }

    /**
     * @return the avecVersementTiersBeneficiaire
     */
    public boolean getAvecVersementTiersBeneficiaire() {
        return avecVersementTiersBeneficiaire;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getDescription() {
        return null;
    }

    /**
     * @return the documentDetailleParAllocataire
     */
    public boolean getDocumentDetailleParAllocataire() {
        return documentDetailleParAllocataire;
    }

    /**
     * @return the documentDetaillePeriode
     */
    public boolean getDocumentNonDetaillePeriode() {
        return documentNonDetaillePeriode;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return null;
    }

    /**
     * @return the nssAllocataire
     */
    public String getNssAllocataire() {
        return nssAllocataire;
    }

    /**
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    @Override
    public void process() {

        String typePrestation = null;
        String typeDocument = null;
        DossierAttestationVersementComplexSearchModel dossierSearch = new DossierAttestationVersementComplexSearchModel();
        ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>> listPrestAtte = new ArrayList<ArrayList<DeclarationVersementDetailleComplexModel>>();

        // récupération du type de prestation
        try {
            typePrestation = typePrestation(getAvecVersementDirec(), getAvecVersementIndirect(),
                    getAvecVersementTiersBeneficiaire());
            typeDocument = typeDocument(getDocumentDetailleParAllocataire(), getDocumentNonDetaillePeriode());

        } catch (ALAttestationVersementException e) {
            getLogSession().error(this.getClass().getName(),
                    "Une erreur s'est produite pendant l'exécution du process : " + e.getMessage());
            JadeLogger.error(this.getClass().getName(), "Une erreur s'est produite pendant l'exécution du process : "
                    + e.getMessage());
        }

        String documentDetaille = null;
        String documentNonDetaille = null;

        try {
            // liste des prestations à considéder
            listPrestAtte = ALServiceLocator.getAttestationsVersementBusinessService().returnListPaiement(
                    dossierSearch = ALServiceLocator.getDossierBusinessService().searchDossierAttestation(
                            getNumAffilie(), getNssAllocataire()), getDateDebut(), getDateFin(), typePrestation);

            // charger les données pour les documents

            if (JadeStringUtil.equals(ALConstAttestattionVersees.ATTEST_TYPE_DOC_DET_PERIODE, typeDocument, false)) {
                documentDetaille = ALServiceLocator.getAttestationsVersementLoadCsvDetaille()
                        .buildCsvAttestationDetaille(listPrestAtte, dossierSearch, getDateDebut(), getDateFin(),
                                typePrestation);
            } else if (JadeStringUtil.equals(ALConstAttestattionVersees.ATTEST_TYPE_DOC_NON_DETAILLE, typeDocument,
                    false)) {

                documentNonDetaille = ALServiceLocator.getAttestationsVersementLoadCsvDetaille()
                        .buildCsvAttestationNonDetaille(listPrestAtte, dossierSearch, getDateDebut(), getDateFin(),
                                typePrestation);
            } else {
                documentDetaille = ALServiceLocator.getAttestationsVersementLoadCsvDetaille()
                        .buildCsvAttestationDetaille(listPrestAtte, dossierSearch, getDateDebut(), getDateFin(),
                                typePrestation);
                documentNonDetaille = ALServiceLocator.getAttestationsVersementLoadCsvDetaille()
                        .buildCsvAttestationNonDetaille(listPrestAtte, dossierSearch, getDateDebut(), getDateFin(),
                                typePrestation);

            }
            creationEtEnvoiFichier(documentDetaille, documentNonDetaille);

            // catcher l'exception correctement
        } catch (Exception e) {
            getLogSession().error(this.getClass().getName(),
                    "Une erreur s'est produite pendant l'exécution du process : " + e.getMessage());
            JadeLogger.error(this.getClass().getName(), "Une erreur s'est produite pendant l'exécution du process : "
                    + e.getMessage());
        }

    }

    /**
     * @param avecVersementDirec
     *            the avecVersementDirec to set
     */
    public void setAvecVersementDirec(boolean avecVersementDirec) {
        this.avecVersementDirec = avecVersementDirec;
    }

    /**
     * @param avecVersementIndirect
     *            the avecVersementIndirect to set
     */
    public void setAvecVersementIndirect(boolean avecVersementIndirect) {
        this.avecVersementIndirect = avecVersementIndirect;
    }

    /**
     * @param avecVersementTiersBeneficiaire
     *            the avecVersementTiersBeneficiaire to set
     */
    public void setAvecVersementTiersBeneficiaire(boolean avecVersementTiersBeneficiaire) {
        this.avecVersementTiersBeneficiaire = avecVersementTiersBeneficiaire;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param documentDetailleParAllocataire
     *            the documentDetailleParAllocataire to set
     */
    public void setDocumentDetailleParAllocataire(boolean documentDetailleParAllocataire) {
        this.documentDetailleParAllocataire = documentDetailleParAllocataire;
    }

    /**
     * @param documentDetaillePeriode
     *            the documentDetaillePeriode to set
     */
    public void setDocumentNonDetaillePeriode(boolean documentDetaillePeriode) {
        documentNonDetaillePeriode = documentDetaillePeriode;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param nssAllocataire
     *            the nssAllocataire to set
     */
    public void setNssAllocataire(String nssAllocataire) {
        this.nssAllocataire = nssAllocataire;
    }

    /**
     * @param numAffilie
     *            the numAffilie to set
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * méthode qui retourne le type de document excel à sortir (détaillé par période, non détaillé par période ou le
     * sdeux)
     * 
     * @throws ALAttestationVersementException
     */
    private String typeDocument(boolean documentDetaille, boolean docuementNonDetaillé)
            throws ALAttestationVersementException {
        String typeDocument = null;
        // contrôle des paramètre
        if ((documentDetaille != true) && (documentDetaille != false)) {
            throw new ALAttestationVersementException(
                    "ALAttestationsVersementEditionProcess#typeDocument: value documentDetaille must be false or true");
        }
        if ((docuementNonDetaillé != true) && (docuementNonDetaillé != false)) {
            throw new ALAttestationVersementException(
                    "ALAttestationsVersementEditionProcess#typeDocument: value docuemen tNonDetaillé must be false or true");
        }
        if (documentDetaille && docuementNonDetaillé) {
            typeDocument = ALConstAttestattionVersees.ATTEST_TYPE_DOC_DET_ET_NON_DET;
        } else if (documentDetaille && !docuementNonDetaillé) {
            typeDocument = ALConstAttestattionVersees.ATTEST_TYPE_DOC_DET_PERIODE;
        } else if (!documentDetaille && docuementNonDetaillé) {
            typeDocument = ALConstAttestattionVersees.ATTEST_TYPE_DOC_NON_DETAILLE;
        } else {
            throw new ALAttestationVersementException(
                    "ALAttestationsVersementEditionProcess#typeDocument: type Document not found");
        }

        return typeDocument;
    }

    /**
     * méthode qui retourne le type de prestation à rechercher (direct, tiers bénéficiaire, indirect...)
     * 
     * @throws ALAttestationVersementException
     */
    private String typePrestation(boolean prestaDirect, boolean prestaIndirect, boolean tiersBeneficiaire)
            throws ALAttestationVersementException {
        String typePresation = null;
        // contrôle des paramètre
        if ((prestaDirect != true) && (prestaDirect != false)) {
            throw new ALAttestationVersementException(
                    "ALAttestationsVersementEditionProcess#typePrestation: value prestaDriec must be false or true");
        }
        if ((prestaIndirect != true) && (prestaIndirect != false)) {
            throw new ALAttestationVersementException(
                    "ALAttestationsVersementEditionProcess#typePrestation: value prestaIndirect must be false or true");
        }
        if ((tiersBeneficiaire != true) && (tiersBeneficiaire != false)) {
            throw new ALAttestationVersementException(
                    "ALAttestationsVersementEditionProcess#typePrestation: value tiersBeneficiaire must be false or true");

        }

        if (prestaDirect && prestaIndirect && tiersBeneficiaire) {
            typePresation = ALConstAttestattionVersees.ATTEST_PRESTA_IND_DIR_TIERS_BEN;
        } else if (prestaDirect && prestaIndirect && !tiersBeneficiaire) {
            typePresation = ALConstAttestattionVersees.ATTEST_PRESTA_DIR_INDIR;

        } else if (prestaDirect && !prestaIndirect && tiersBeneficiaire) {
            typePresation = ALConstAttestattionVersees.ATTEST_PRESTA_DIR_TIERS_BEN;
        } else if (!prestaDirect && prestaIndirect && tiersBeneficiaire) {
            typePresation = ALConstAttestattionVersees.ATTEST_PRESTA_IND_TIERS_BEN;
        } else if (prestaDirect && !prestaIndirect && !tiersBeneficiaire) {
            typePresation = ALConstAttestattionVersees.ATTEST_PRESTA_DIRECT;
        } else if (!prestaDirect && prestaIndirect && !tiersBeneficiaire) {
            typePresation = ALConstAttestattionVersees.ATTEST_PRESTA_DIR_INDIR;
        } else if (!prestaDirect && !prestaIndirect && tiersBeneficiaire) {
            typePresation = ALConstAttestattionVersees.ATTEST_PRESTA_TIERS_BEN;
        } else {
            throw new ALAttestationVersementException(
                    "ALAttestationsVersementEditionProcess#typePrestation: type Prestation not found");
        }
        return typePresation;
    }
}
