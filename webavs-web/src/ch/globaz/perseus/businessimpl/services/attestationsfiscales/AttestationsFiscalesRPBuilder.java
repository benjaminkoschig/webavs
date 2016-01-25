package ch.globaz.perseus.businessimpl.services.attestationsfiscales;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.rentepont.RentePont;
import ch.globaz.perseus.businessimpl.services.document.AbstractDocumentBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

public class AttestationsFiscalesRPBuilder extends AbstractDocumentBuilder {

    private static final String CDT_ANNEE_ATTESTATION = "{anneeAttestation}";
    private static final String CDT_TITRE_TIERS = "{titreTiers}";

    private String adMail = "";
    private String annee = "";

    private String caisse = "";

    private String dateDocument = "";

    private RentePont dernierRentePont = null;

    private String idTiersAdresseCourrier = "";

    private boolean isSendToGed = false;

    private String titreTiers = "";

    public AttestationsFiscalesRPBuilder() {

    }

    /**
     * ******* CONSTRUCTION DU DOCUMENT*******
     */

    public DocumentData build() throws JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        try {
            this.loadEntity(dernierRentePont);

            createJadePublishDocInfo(dateDocument, adMail, isSendToGed, dernierRentePont.getDossier()
                    .getDemandePrestation().getDemandePrestation().getIdTiers(),
                    getSession().getLabel("PDF_ATTESTATIONSFISCALES_D_TITRE_MAIL") + " - " + " " + annee);

            // Création de l'attestation originale
            return createAttestation(dernierRentePont, false, isSendToGed);

        } catch (Exception e) {
            throw new DecisionException("AttestationsFiscalesBuilder -  NSS : "
                    + dernierRentePont.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel()
                    + " "
                    + dernierRentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation1()
                    + " "
                    + dernierRentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                            .getDesignation2() + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    private DocumentData createAttestation(RentePont dernierrRentePont, boolean isCopie, boolean isGed)
            throws Exception {
        try {
            /**
             * ********** GENERATION DOCUMENT**************
             */

            DocumentData data = new DocumentData();
            // Chargement du titre du document pour le header
            String titreDocument = getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT,
                    1, 1)
                    + "\n" + annee;

            data = buildHeader(data, isCopie, false, dernierrRentePont.getDossier().getDemandePrestation()
                    .getPersonneEtendue().getTiers().getIdTiers(), "", caisse, dateDocument, titreDocument, false);

            data.addData("idProcess", "PCFAttestationsFiscalesRP");

            // Renseignement du bloc reference
            data.addData("REFERENCE",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT, 2, 1));
            data.addData("NSS",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT, 2, 2));
            data.addData("NUMERO_NSS", dernierrRentePont.getDossier().getDemandePrestation().getPersonneEtendue()
                    .getPersonneEtendue().getNumAvsActuel());
            String paragraphe = "";
            if (CSCaisse.CCVD.getCodeSystem().equals(caisse)) {
                data.addData("NIP",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT, 2, 3));
                data.addData("NUMERO_NIP", dernierrRentePont.getDossier().getDemandePrestation().getPersonneEtendue()
                        .getTiers().getIdTiers());
                paragraphe = getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT, 3, 1);
            } else {
                data.addData("TITRE_ATTESTATION",
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT, 1, 1)
                                + " " + annee);
                paragraphe = getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT, 3, 2);
            }

            // Insertion dans document du titre du tiers
            data.addData("titre_tiers", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_DOCUMENT, 3, 1),
                    AttestationsFiscalesRPBuilder.CDT_TITRE_TIERS, titreTiers));

            if (!JadeStringUtil.isEmpty(paragraphe)) {
                data.addData("PARAGRAPHE", PRStringUtils.replaceString(paragraphe,
                        AttestationsFiscalesRPBuilder.CDT_ANNEE_ATTESTATION, annee));
            }

            data.addData("SALUTATION_ATTESTATION", PRStringUtils.replaceString(
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT, 4, 1),
                    AttestationsFiscalesRPBuilder.CDT_TITRE_TIERS, titreTiers));

            data.addData("ANNEXE",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT, 5, 1));
            data.addData("TEXTE_ANNEXE",
                    getBabelContainer().getTexte(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT, 5, 2));
            return data;
        } catch (Exception e) {
            throw new DecisionException("createAttestation " + e.toString(), e);
        }
    }

    public String getAdMail() {
        return adMail;
    }

    public String getAnnee() {
        return annee;
    }

    public String getCaisse() {
        return caisse;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    public RentePont getDernierRentePont() {
        return dernierRentePont;
    }

    public String getIdTiersAdresseCourrier() {
        return idTiersAdresseCourrier;
    }

    public String getTitreTiers() {
        return titreTiers;
    }

    @Override
    public boolean isSendToGed() {
        return isSendToGed;
    }

    private void loadEntity(RentePont rentePont) throws CatalogueTexteException, Exception {
        try {
            // j'utilise l'adresse du requérant
            idTiersAdresseCourrier = rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers()
                    .getIdTiers();
            // je charge le catalogue de texte dans la langue du requérant
            getBabelContainer().setCodeIsoLangue(
                    getSession().getCode(
                            rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getLangue()));
            // le titre est celui du requérant
            titreTiers = getSession().getCodeLibelle(
                    rentePont.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getTitreTiers());

            getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_ATTESTATIONS_FISCALES_RENTE_PONT);

            getBabelContainer().load();

        } catch (Exception e) {
            throw new DecisionException("loadEntity " + e.toString(), e);
        }
    }

    public void setAdMail(String adMail) {
        this.adMail = adMail;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setCaisse(String caisse) {
        this.caisse = caisse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setDernierRentePont(RentePont dernierRentePont) {
        this.dernierRentePont = dernierRentePont;
    }

    public void setIdTiersAdresseCourrier(String idTiersAdresseCourrier) {
        this.idTiersAdresseCourrier = idTiersAdresseCourrier;
    }

    @Override
    public void setSendToGed(boolean isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setTitreTiers(String titreTiers) {
        this.titreTiers = titreTiers;
    }
}
