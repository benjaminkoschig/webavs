package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleSearchComplexModel;
import ch.globaz.al.businessimpl.documents.AbstractDocument;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service de base des déclaration de versement
 * 
 * @author PTA
 * 
 */
public class DeclarationVersementAbstractServiceImpl extends AbstractDocument {

    /**
     * Méthode qui retourne le tiers bénéficiaire des prestations
     * 
     * @param attesAllo
     * @param idTiersPrest
     * @param dossier
     * @return
     */

    protected String getIdTiersBenefPresta(Boolean attesAlloc, String idTiersPrest, DossierComplexModel dossier) {
        // FIXME contrôle des paramètre
        String idTiersBenefPresta = null;
        // si l'attestation va à l'allocataire, le tiers bénéficiaire est l'allocataire
        if (attesAlloc) {
            idTiersBenefPresta = dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire();
        } else if (!attesAlloc
                && !JadeStringUtil.equals(dossier.getDossierModel().getIdTiersBeneficiaire(), idTiersPrest, false)) {
            idTiersBenefPresta = idTiersPrest;
        } else {
            idTiersBenefPresta = dossier.getDossierModel().getIdTiersBeneficiaire();

        }

        return idTiersBenefPresta;

    }

    protected DeclarationVersementDetailleSearchComplexModel getPrestationDetailles(String idDossier, String dateDebut,
            String dateFin, HashSet<String> typeBoni, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {

        // contrôle des paramètres
        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDetailsPrestations: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDetailsPrestations:  " + dateDebut
                            + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDetailsPrestations:  " + dateFin
                            + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "declarationVersementDetailleAbstractServiceImpl# getDetailsPrestations: language  "
                            + langueDocument + " is not  valid ");
        }

        DeclarationVersementDetailleSearchComplexModel declarationsDetaille = new DeclarationVersementDetailleSearchComplexModel();

        declarationsDetaille.setInBonification(typeBoni);
        declarationsDetaille.setForDateDebut(dateDebut);
        declarationsDetaille.setForDateFin(dateFin);
        declarationsDetaille.setForEtat(ALCSPrestation.ETAT_CO);
        declarationsDetaille.setForIdDossier(idDossier);
        declarationsDetaille.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        declarationsDetaille.setOrderKey("tiersBeneficiaire");
        declarationsDetaille = ALImplServiceLocator.getDeclarationVersementDetailleComplexModelService().search(
                declarationsDetaille);

        return declarationsDetaille;
    }

    /**
     * Détail de toutes les documents à créer en fonction des tiers bénéficiaires
     * 
     * @param declarSearch
     * @return
     */
    protected ArrayList<DeclarationVersementDetailleDossierTiers> loadDataDocument(
            DeclarationVersementDetailleSearchComplexModel declarSearch, DossierComplexModel dossier) {
        // FIXME contrôle des paramètres

        ArrayList<DeclarationVersementDetailleDossierTiers> listDeclarationVersementDossierTiers = new ArrayList<DeclarationVersementDetailleDossierTiers>();
        // liste temporaire de détail de prestations
        ArrayList<DeclarationVersementDetailleComplexModel> listTempDeclaVersDet = new ArrayList<DeclarationVersementDetailleComplexModel>();

        String idTiersBeneficaire = null;
        Boolean attesAlloc = null;
        // parcourir les prestations pour les dissocier en fonction du tiers bénéficiaire
        for (int i = 0; i < declarSearch.getSize(); i++) {

            DeclarationVersementDetailleComplexModel declarDetail = (DeclarationVersementDetailleComplexModel) declarSearch
                    .getSearchResults()[i];

            // si c'est la première on l'ajoute à la liste temporaire ou si le tiers bénéficiaire est identique au
            // précédent et attest ne va pas à alloc
            // ou si l'attestation va à l'allocataire et comme le précédent
            if ((i == 0)
                    || (JadeStringUtil.equals(idTiersBeneficaire, declarDetail.getTiersBeneficiaire(), false)
                            && attesAlloc.equals(declarDetail.getAttestationAlloc()) && !attesAlloc)
                    || (attesAlloc.equals(declarDetail.getAttestationAlloc()) && attesAlloc)) {
                listTempDeclaVersDet.add(declarDetail);
            } else {
                DeclarationVersementDetailleDossierTiers declaDossierTier = new DeclarationVersementDetailleDossierTiers();
                ArrayList<DeclarationVersementDetailleComplexModel> listDef = new ArrayList<DeclarationVersementDetailleComplexModel>();

                // ajout du tiers beneficiaire
                String idTiersBenef = getIdTiersBenefPresta(attesAlloc, idTiersBeneficaire, dossier);
                declaDossierTier.setIdTiersDestinataire(idTiersBenef);
                // ajout de la liste temporaire à liste définitive à un objet DeclarationVersementDetailleComplexModel
                listDef.addAll(listTempDeclaVersDet);
                declaDossierTier.setListPrestatiion(listDef);
                // ajout du dossier à l'objet DeclarationVersementDetailleComplexModel
                declaDossierTier.setDossier(dossier);
                // ajout de l'objet DeclarationVersementDetailleComplexModel à la liste
                listDeclarationVersementDossierTiers.add(declaDossierTier);

                // on vide la liste temporaire
                listTempDeclaVersDet.clear();
                listTempDeclaVersDet.add(declarDetail);
            }
            // si c'est la dernière prestation on l'ajoute à la liste
            if (i == declarSearch.getSize() - 1) {
                DeclarationVersementDetailleDossierTiers declaDossierTier = new DeclarationVersementDetailleDossierTiers();
                ArrayList<DeclarationVersementDetailleComplexModel> listDef = new ArrayList<DeclarationVersementDetailleComplexModel>();

                // ajout du tiers beneficiaire
                String idTiersBenef = getIdTiersBenefPresta(declarDetail.getAttestationAlloc(),
                        declarDetail.getTiersBeneficiaire(), dossier);
                declaDossierTier.setIdTiersDestinataire(idTiersBenef);
                // ajout de la liste temporaire à liste définitive à un objet DeclarationVersementDetailleComplexModel
                listDef.addAll(listTempDeclaVersDet);
                declaDossierTier.setListPrestatiion(listDef);
                // ajout du dossier à l'objet DeclarationVersementDetailleComplexModel
                declaDossierTier.setDossier(dossier);
                // ajout de l'objet DeclarationVersementDetailleComplexModel à la liste
                listDeclarationVersementDossierTiers.add(declaDossierTier);
                // on vide la liste temporaire
                listTempDeclaVersDet.clear();
            }

            // valeur du tiersBeneficiaire
            idTiersBeneficaire = declarDetail.getTiersBeneficiaire();
            // valeur attestationAlloc
            attesAlloc = declarDetail.getAttestationAlloc();
        }
        return listDeclarationVersementDossierTiers;

    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        // DO NOTHING, redéfinit dans les classes enfant
    }

    /**
     * Méthode "settant" les infos. du dossier
     * 
     * @param document
     *            document à créer
     * @param dateDebut
     *            date du début
     * @param dateFin
     *            date de la fin
     * @param dossierComplex
     *            modèle permettant dde récupérer les infos liées au dossier
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected void setInfos(DocumentData document, DossierComplexModel dossierComplex, String dateDebut,
            String dateFin, String langueDocument) throws JadeApplicationException {

        // contôle des paramètres
        if (document == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementListAttestationServiceImpl#setInfos : document is null");
        }

        if (dossierComplex == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementListAttestationServiceImpl#setInfos : dossierComplex is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException("DeclarationVersementAbstractServiceImpl# setInfos:  "
                    + dateDebut + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException("DeclarationVersementAbstractServiceImpl# setInfos:  " + dateFin
                    + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException("EcheancesServiceListeAffilieImpl#setTexte: language  "
                    + langueDocument + " is not  valid ");
        }

        String[] periode = { dateDebut, dateFin };
        document.addData("info_concerne_label_declaration_versement_concerne",
                this.getText("al.declarationVersement.prestation.periode.du.au", langueDocument, periode));

        // infos relatives à l'affilié
        document.addData("info_numero_affilie_label", this.getText("al.declarationVersement.noAffilie", langueDocument));
        document.addData("info_numero_affilie_valeur", dossierComplex.getDossierModel().getNumeroAffilie());

        // données relatives à l'allocataire
        document.addData("info_reference_allocataire_label",
                this.getText("al.declarationVersement.allocataire", langueDocument));
        document.addData("info_reference_allocataire_valeur", dossierComplex.getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                + " "
                + dossierComplex.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                        .getDesignation2());
        document.addData("info_nss_label", this.getText("al.declarationVersement.nss", langueDocument));
        document.addData("info_reference_nss_valeur", dossierComplex.getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
        document.addData("info_idDossier_label", this.getText("al.declarationVersement.dossier", langueDocument));
        document.addData("info_idDossier_valeur", dossierComplex.getDossierModel().getIdDossier());

    }

    /**
     * Méthode "settant" les données du tiers allocataire en copie à
     * 
     * @param doc
     *            document à fournir
     * @param dossier
     *            selon DossierComplexModel
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected void setTableCopie(DocumentData doc, DossierComplexModel dossier, String langueDocument)
            throws JadeApplicationException {
        // vérification des paramètres

        if (doc == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDirectServieImpl# setTableCopie: DocumentData is null");
        }
        if (dossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDirectServieImpl# setTableCopie: DossierComplexModel is null");

        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException("DeclarationVersementDirectServiceImpl#setTableCopie: language  "
                    + langueDocument + " is not  valid ");
        }
        Collection tableCopie = new Collection("declaration_Avis_A");

        if (!JadeStringUtil.equals(dossier.getTiersBeneficiaireModel().getIdTiers(), dossier
                .getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), false)) {

            DataList ligne = new DataList("colonne");

            // ajout copie à
            ligne.addData("col_copie_label", this.getText("al.declarationVersement.prestation.copie", langueDocument));

            // ajouter designation 1 et designation 2 pour colonne copie à
            ligne.addData("col_copie", dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                    .getDesignation1()

                    + " "
                    + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers()
                            .getDesignation2());

            tableCopie.add(ligne);

        }
        doc.add(tableCopie);
    }

}
