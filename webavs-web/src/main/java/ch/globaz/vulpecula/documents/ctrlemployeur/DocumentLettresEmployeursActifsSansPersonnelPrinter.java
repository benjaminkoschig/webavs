package ch.globaz.vulpecula.documents.ctrlemployeur;

import globaz.framework.printing.itext.FWIDocumentManager;
import globaz.jade.log.JadeLogger;
import globaz.vulpecula.business.exception.VulpeculaException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;

/**
 * 
 * @author jwe
 * 
 */
public class DocumentLettresEmployeursActifsSansPersonnelPrinter extends DocumentPrinter<Employeur> {
    private static final long serialVersionUID = 7451949163803279932L;
    private String dateReference;
    private String dateEnvoi;

    public DocumentLettresEmployeursActifsSansPersonnelPrinter() {
        super();
    }

    public DocumentLettresEmployeursActifsSansPersonnelPrinter(String dateReference, String dateEnvoi) {
        setDateReference(dateReference);
        setDateEnvoi(dateEnvoi);
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_TYPE_NUMBER;
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentLettresEmployeursActifsSansPersonnel(getCurrentElement(), dateEnvoi, dateReference);
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LETTRE_EMPLOYEURS_ACTIFS_SANS_PERSONNEL_SUBJECT;
    }

    @Override
    public void retrieve() {
        List<Employeur> employeurs = new ArrayList<Employeur>();
        try {
            employeurs = VulpeculaServiceLocator.getEmployeurService().findEmployeursActifsSansPostes(
                    new Date("01.01." + getDateReference()), new Date("31.12." + getDateReference()));

            // parcourir les employeurs et récupérer l'adresse associée
            for (Employeur emp : employeurs) {
                loadAdresseEmployeur(emp);
            }

            setElements(employeurs);

        } catch (VulpeculaException e) {
            JadeLogger.error(this, e.toString());
            // Envoi du message d'erreur par Mail en fin de processus
            String errorMsg = getSession().getLabel("ERROR_FIND_EMPLOYEUR_ACTIF_SANS_PERSONNEL") + e.toString();
            getTransaction().addErrors(errorMsg);

        }
    }

    public String getDateReference() {
        return dateReference;
    }

    public void setDateReference(String dateReference) {
        this.dateReference = dateReference;
    }

    public String getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(String dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    private void loadAdresseEmployeur(Employeur employeur) {
        if (employeur.getAdressePrincipale() == null) {
            employeur.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdressePrioriteCourrierByIdTiers(employeur.getIdTiers()));
        }
    }

}
