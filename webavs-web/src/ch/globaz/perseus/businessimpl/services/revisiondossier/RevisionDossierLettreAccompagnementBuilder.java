package ch.globaz.perseus.businessimpl.services.revisiondossier;

import globaz.globall.util.JACalendar;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class RevisionDossierLettreAccompagnementBuilder extends AbstractDocumentBuilder {

    private String csCaisse = null;
    private DocumentData data = null;
    private Demande demande = null;

    public RevisionDossierLettreAccompagnementBuilder() {

    }

    public DocumentData build() throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception {
        try {
            this.loadEntity();
            // this.createJadePublishDocInfo(JACalendar.todayJJsMMsAAAA(), this.getAdMail(), false, this.getDemande()
            // .getDossier().getDemandePrestation().getDemandePrestation().getIdTiers(), this.getSession()
            // .getLabel("PDF_PF_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT"));

            createLettreAccompagnement();
            return getData();
        } catch (Exception e) {
            throw new DecisionException("RevisionDossierLAccompBuilder -  NSS : "
                    + getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel() + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private void createLettreAccompagnement() throws Exception {
        buildHeader(getData(), false, false, demande.getSimpleDemande().getIdAgenceCommunale(), demande.getDossier()
                .getDossier().getGestionnaire(), csCaisse, JACalendar.todayJJsMMsAAAA(),
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 1, 1), false);

        // Insertion dans document du titre de document si c'est l'agence de Lausanne
        if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
            getData().addData("TitreDocument",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 1, 1));
        }

        getData().addData("TitreTiers",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 1, 5));
        getData().addData("Paragraphe1",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 1, 2));
        getData().addData("Paragraphe2",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 1, 3));
        getData().addData("Paragraphe3",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 1, 4));

        if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
            getData().addData("AdresseRetourL1",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 2, 1));
            getData().addData("AdresseRetourL2",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 2, 2));
            getData().addData("AdresseRetourL3",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 2, 3));
            getData().addData("AdresseRetourL4",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 2, 4));
        } else {
            getData().addData("AdresseRetourL1",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 2, 5));
            getData().addData("AdresseRetourL2",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 2, 6));
            getData().addData("AdresseRetourL3",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 2, 7));
            getData().addData("AdresseRetourL4",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 2, 8));
        }

        getData().addData("Paragraphe4",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 3, 1));
        getData().addData("Annexe",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 4, 1));
        getData().addData("TexteAnnexe",
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, 4, 2));
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

    private void loadEntity() throws Exception {
        this.loadEntity(IPFCatalogueTextes.CS_REVISION_DOSSIER_LETTRE_ACCOMPAGNEMENT, getDemande().getDossier()
                .getDemandePrestation().getPersonneEtendue().getTiers().getLangue());
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
