package ch.globaz.al.businessimpl.services.declarationVersement;

import ch.globaz.al.properties.ALProperties;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import ch.globaz.al.business.constantes.ALCSDeclarationVersement;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstDeclarationVersement;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.models.allocataire.AllocataireComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationGenComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;
import org.apache.commons.lang.StringUtils;

/**
 * Classe d'implémentation du service
 *
 * @author PTA
 */
public abstract class DeclarationVersementGlobalAbstractServiceImpl extends DeclarationVersementAbstractServiceImpl
        implements DeclarationVersementService {

    /**
     * Méthode calculant les montants
     *
     * @param montantTotal montant Total
     * @param montant      à ajouter au montant total
     * @return montantTotal
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String calculMontant(String montantTotal, String montant) throws JadeApplicationException {

        // contrôle des paramètres
        if (!JadeNumericUtil.isNumeric(montantTotal)) {
            throw new ALDeclarationVersementException("DeclarationVersementAbstractServiceImpl#calculMontant: "
                    + montantTotal + " is not a numreric value");
        }
        if (!JadeNumericUtil.isNumeric(montant)) {
            throw new ALDeclarationVersementException("DeclarationVersementAbstractServiceImpl#calculMontant: "
                    + montant + " is not a numreric value");
        }

        double montantTotalRecap = 0.00d;

        montantTotalRecap = JadeStringUtil.parseDouble(montantTotal, 0.00d)
                + JadeStringUtil.parseDouble(montant, 0.00d);

        // Format décimal 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("0.00");
        montantTotal = String.valueOf(df.format(montantTotalRecap));

        return montantTotal;
    }

    /**
     * Méthode calculant les montants
     *
     * @param montantTotal montant Total
     * @param montantIS    montant à retirer au total
     * @return montantTotal
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected String calculMontantIS(String montantTotal, String montantIS) throws JadeApplicationException {

        // contrôle des paramètres
        if (!JadeNumericUtil.isNumeric(montantTotal)) {
            throw new ALDeclarationVersementException("DeclarationVersementAbstractServiceImpl#calculMontant: "
                    + montantTotal + " is not a numreric value");
        }

        double montantTotalRecap = JadeStringUtil.parseDouble(montantTotal, 0.00d) -  + JadeStringUtil.parseDouble(montantIS, 0.00d);

        // Format décimal 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("0.00");
        montantTotal = String.valueOf(df.format(montantTotalRecap));

        return montantTotal;
    }

    /**
     * @param idDossier      identifiant du dossier
     * @param dateDebut      date du début
     * @param dateFin        date du la fin
     * @param dateImpression date à figurer sur le document
     * @return DocumentData
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *                                  faire
     */
    protected DocumentData getDeclarationVersement(String idDossier, String dateDebut, String dateFin,
                                                   String dateImpression, HashSet<String> typeBoni, String langueDocument, Boolean textImpot)
            throws JadePersistenceException, JadeApplicationException {

        // vérification des paramètres

        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getDeclarationVersement: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getDeclarationVersement:  " + dateDebut
                            + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getDeclarationVersement:  " + dateFin
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpression)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getDeclarationVersement:  " + dateImpression
                            + " is not a valid globaz's date ");
        }

        DocumentData document = initDocument();

        this.setIdSignature(document, ALServiceLocator.getDossierModelService().read(idDossier)
                .getActiviteAllocataire(), ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, langueDocument);

        return document;
    }

    /**
     * @param idDossier identifiant du dossier
     * @param dateDebut date de début
     * @param dateFin   date de fin
     * @param typeBoni  type de bonification
     * @return DetailPrestationGenComplexSearchModel
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *                                  faire Recherche sur les types de bonification
     */
    protected DetailPrestationGenComplexSearchModel getDetailsPrestations(String idDossier, String dateDebut,
                                                                          String dateFin, HashSet<String> typeBoni) throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getDetailsPrestations: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getDetailsPrestations:  " + dateDebut
                            + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getDetailsPrestations:  " + dateFin
                            + " is not a valid globaz's date ");
        }

        DetailPrestationGenComplexSearchModel detailPresta = new DetailPrestationGenComplexSearchModel();
        detailPresta.setForPeriodeDebut(dateDebut);
        detailPresta.setForPeriodeFin(dateFin);
        detailPresta.setForIdDossier(idDossier);
        detailPresta.setInTypeBonification(typeBoni);
        detailPresta.setForDateDebut(dateDebut);
        detailPresta.setForDateFin(dateFin);
        detailPresta.setForEtat(ALCSPrestation.ETAT_CO);

        detailPresta.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        detailPresta.setWhereKey("PrestationDeclaration");
        detailPresta.setOrderKey("PrestationDeclaration");
        detailPresta = ALImplServiceLocator.getDetailPrestationGenComplexModelService().search(detailPresta);
        return detailPresta;
    }

    /**
     * Méthode qui permet de récupérer les différents totaux pour les paiment direct
     *
     * @param attestDecla
     * @param idTiersAllocataire
     * @return HashMap
     * @throws JadeApplicationException levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected HashMap<String, String> getTotauxPaiementDirect(
            /* DetailPrestationGenComplexSearchModel presta */ArrayList<DeclarationVersementDetailleComplexModel> attestDecla,
                                                              String idTiersAllocataire) throws JadeApplicationException {
        // contrôle des paramètres
        if (/* presta */attestDecla == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getTotaux: presta is null");
        }

        HashMap<String, String> totaux = new HashMap<String, String>();
        String montantRetroactif = "0.00";
        String montantAnneeVersement = "0.00";
        String montantTotalIS = "0.00";
        for (int i = 0; i < attestDecla.size(); i++) {
            DeclarationVersementDetailleComplexModel declarationVersementLigne = attestDecla.get(i);

            if (!JadeStringUtil.equals(JadeStringUtil.substring(declarationVersementLigne.getPeriode(), 3),
                    (JadeStringUtil.substring(declarationVersementLigne.getDateVersement(), 6)), false)) {

                // montant rétroactif
                montantRetroactif = calculMontant(montantRetroactif,
                        declarationVersementLigne./* getMontant() */getMontantDetailPrestation());

            }
            // calcul pour l'année
            else {
                montantAnneeVersement = calculMontant(montantAnneeVersement,
                        declarationVersementLigne./* getMontant() */getMontantDetailPrestation());

            }

            if (StringUtils.isNotEmpty(declarationVersementLigne.getMontantIS())) {
                montantTotalIS = calculMontant(montantTotalIS,declarationVersementLigne.getMontantIS());
            }

        }

        String montantTotal = calculMontant(montantAnneeVersement, montantRetroactif);
        if (ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()) {
            montantTotal = calculMontantIS(montantTotal, montantTotalIS);
        }

        totaux.put(ALConstDeclarationVersement.TOTAL_RETROACTIF, montantRetroactif);
        totaux.put(ALConstDeclarationVersement.TOTAL_ANNEE, montantAnneeVersement);
        totaux.put(ALConstDeclarationVersement.TOTAL_IS, montantTotalIS);
        totaux.put(ALConstDeclarationVersement.TOTAL, montantTotal);

        return totaux;
    }

    /**
     * Méthode qui permet de récupérer les différents totaux pour paiements indirect
     *
     * @param presta les prestation permettant de récupérer les données
     * @return HashMap
     * @throws JadeApplicationException levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */

    protected HashMap<String, String> getTotauxPaiementsIndirects(
            /* DetailPrestationGenComplexSearchModel */ArrayList<DeclarationVersementDetailleComplexModel> presta,
                                                       String idTiersAllocataire) throws JadeApplicationException {
        // contrôle des paramètres
        if (presta == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# getTotaux: presta is null");
        }

        HashMap<String, String> totaux = new HashMap<String, String>();
        String montantRetroactif = "0.00";
        String montantAnneeVersement = "0.00";
        for (int i = 0; i < presta./* getSize() */size(); i++) {
            /* DetailPrestationGenComplexModel */
            DeclarationVersementDetailleComplexModel declarationVersementLigne = presta
                    .get/* SearchResults()[i] */(i);

            // si le tiers bénéficiaire est 0 ou si le tiers bénéficiaire est égal ou tiers allocataire
            // calcul pour le total des années précédentes
            if (JadeNumericUtil.isEmptyOrZero(declarationVersementLigne
                    ./* getIdTiersBeneficiaire() */getTiersBeneficiaire())
                    || JadeStringUtil.equals(
                    declarationVersementLigne./* getIdTiersBeneficiaire() */getTiersBeneficiaire(),
                    idTiersAllocataire, false)) {
                if (!JadeStringUtil.equals(
                        JadeStringUtil.substring(declarationVersementLigne./* getPeriodeValidite() */getPeriode(), 3),
                        (JadeStringUtil.substring(declarationVersementLigne.getDateVersement(), 6)), false)) {

                    montantRetroactif = calculMontant(montantRetroactif,
                            declarationVersementLigne./* getMontant() */getMontantDetailPrestation());

                }
                // calcul pour l'année
                else {
                    montantAnneeVersement = calculMontant(montantAnneeVersement,
                            declarationVersementLigne./* getMontant() */getMontantDetailPrestation());

                }

            }
        }

        String montantTotal = calculMontant(montantAnneeVersement, montantRetroactif);

        totaux.put(ALConstDeclarationVersement.TOTAL_RETROACTIF, montantRetroactif);
        totaux.put(ALConstDeclarationVersement.TOTAL_ANNEE, montantAnneeVersement);
        totaux.put(ALConstDeclarationVersement.TOTAL, montantTotal);

        return totaux;
    }

    /**
     * Initialise le document global
     *
     * @return DocumentData
     * @throws JadeApplicationException Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *                                  faire
     */
    protected DocumentData initDocument() throws JadeApplicationException, JadePersistenceException {
        DocumentData document = new DocumentData();
        setIdDocument(document);
        // this.setIdEntete(document);
        // this.setIdSignature(document);
        return document;
    }

    /**
     * Métohde qui retourne les données nécessaires à la création d'une déclaration de versement
     *
     * @param detailPrestaSearch
     * @param dossier
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    protected ArrayList<DeclarationVersementDetailleDossierTiers> loadDataDocument(
            DetailPrestationGenComplexSearchModel detailPrestaSearch, DossierComplexModel dossier)
            throws JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres

        ArrayList<DeclarationVersementDetailleDossierTiers> listDeclaVerseDossierTiers = new ArrayList<DeclarationVersementDetailleDossierTiers>();
        // liste temporaire des prestation
        ArrayList<DetailPrestationGenComplexModel> listTempPresta = new ArrayList<DetailPrestationGenComplexModel>();
        // chercher tous les documents de la liste
        String idTiersBeneficiaire = null;
        Boolean idDroitAttesAlloc = null;
        Boolean attesAlloc = null;
        // parcourir les prestations pour les dissssocier en fonction du tiers beneficaire

        for (int i = 0; i < detailPrestaSearch.getSize(); i++) {
            DetailPrestationGenComplexModel detailPresta = (DetailPrestationGenComplexModel) detailPrestaSearch
                    .getSearchResults()[i];
            // si c'est la première prestation on l'ajoute à la liste temporaire ou si le tiers bénéficiaire est au
            // précédent et l'attestation ne va pas à l'alloc ou si l'attestation va à l'alloc comme le précédent
            if ((i == 0)
                    || (JadeStringUtil.equals(idTiersBeneficiaire, detailPresta.getIdTiersBeneficiaire(), false)
                    && attesAlloc.equals(idDroitAttesAlloc) && !attesAlloc)
                    || (attesAlloc.equals(idDroitAttesAlloc) && attesAlloc)) {
                listTempPresta.add(detailPresta);
            } else {
                DeclarationVersementDetailleDossierTiers declaDossierTier = new DeclarationVersementDetailleDossierTiers();
                ArrayList<DetailPrestationGenComplexModel> listDef = new ArrayList<DetailPrestationGenComplexModel>();

                // ajout du tiers beneficiaire
                String idTiersBenef = getIdTiersBenefPresta(attesAlloc, idTiersBeneficiaire, dossier);
                declaDossierTier.setIdTiersDestinataire(idTiersBenef);
                // ajout de la liste temporaire à liste définitive à un objet DeclarationVersementDetailleComplexModel
                listDef.addAll(listTempPresta);
                declaDossierTier.setListPrestaGlob(listDef);
                // ajout du dossier à l'objet DeclarationVersementDetailleComplexModel
                declaDossierTier.setDossier(dossier);
                // ajout de l'objet DeclarationVersementDetailleComplexModel à la liste
                listDeclaVerseDossierTiers.add(declaDossierTier);

                // on vide la liste temporaire
                listTempPresta.clear();
                listTempPresta.add(detailPresta);
            }

            // valeur du tiersBeneficiaire
            idTiersBeneficiaire = detailPresta.getIdTiersBeneficiaire();
            // valeur attestationAlloc à chercher dans le droit
            attesAlloc = ALImplServiceLocator.getDroitModelService().read(detailPresta.getIdDroit())
                    .getAttestationAlloc();
        }
        // voir comment loadDocumentData
        return listDeclaVerseDossierTiers;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.globaz.al.businessimpl.documents.AbstractDocument#setIdDocument(ch .globaz.topaz.datajuicer.DocumentData)
     */
    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        // vérification paramètre

        if (document == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementListAttestationServiceimplServiceImpl#setIdDocument : document is null");
        }
        document.addData("AL_idDocument", "AL_declarationVersement");
    }

    /**
     * Méthode qui charge le tableau dans le document
     *
     * @param document  auquel il faut ajouter le tableau
     * @param idDossier identifiant du dossier
     * @param dateDebut date du début
     * @param dateFin   date de fin
     * @param typeBoni  type de bonification(direct ou indirect)
     * @throws JadeApplicationException Exception levée si l'un des paramètres n'est pas valide
     * @throws JadePersistenceException Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *                                  faire
     */
    protected void setTable(DocumentData document, String idDossier, String idTiersBeneneficiaire,
                            String idTiersAllocataire, String dateDebut, String dateFin, HashSet<String> typeBoni,
                            ArrayList<DeclarationVersementDetailleComplexModel> attestDecla, String typeDeclaration,
                            String langueDocument, AllocataireComplexModel allocataire) throws JadeApplicationException,
            JadePersistenceException {
        // contrôle des paramètres

        if (document == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementListAttestationServiceImpl#setTable : documnet is null");
        }
        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# setTable: idDossier is null");
        }
        if (idTiersBeneneficiaire == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# setTable: idTiersBeneneficiaire is null");
        }

        if (idTiersAllocataire == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementAbstractServiceImpl# setTable: idTiersAllocataire is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException("DeclarationVersementAbstractServiceImpl# setTable:  "
                    + dateDebut + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException("DeclarationVersementAbstractServiceImpl# setTable:  " + dateFin
                    + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalAbstractServiceImpl# setTable: language  " + langueDocument
                            + " is not  valid ");
        }

        HashMap<String, String> totaux = new HashMap<String, String>();
        // Condition paramètre

        if (JadeStringUtil.equals(typeDeclaration, ALCSDeclarationVersement.DECLA_VERS_IND_FRONT, false)
                || JadeStringUtil.equals(typeDeclaration, ALCSDeclarationVersement.DECLA_VERS_IND_NON_ACT, false)) {
            totaux = getTotauxPaiementsIndirects(/* detailPresta */attestDecla, idTiersAllocataire);
        } else {
            totaux = getTotauxPaiementDirect(/* detailPresta */attestDecla, idTiersAllocataire);
        }

        // Récupération des ANS (seulement si la propriété existe ==> BMS)
        totaux = recuperationANS(dateDebut, dateFin, allocataire, totaux);

        // année de versement
        String anneeVersement = JadeStringUtil.substring(/* detailPresta.getForDateDebut() */dateDebut, 6);

        Collection tableauVersement = new Collection("List_declaration_versement");

        // définition des entetes
        document.addData("entete_declarationVersement_prestations",
                this.getText("al.declarationVersement.prestation.libelle", langueDocument));
        document.addData("entete_declarationVersement_montants",
                this.getText("al.declarationVersement.montant.libelle", langueDocument));

        // ajout des données dans les lignes
        DataList ligneAnneePrec = new DataList("colonneMontantAnneePrec");

        ligneAnneePrec.addData("col_montant_annee_Prec_label",
                this.getText("al.declarationVersement.prestation.anneesPrecedentes", langueDocument));
        ligneAnneePrec.addData("col_monnaie_label",
                this.getText("al.declarationVersement.montant.devise", langueDocument));

        ligneAnneePrec.addData("col_montant_annee_prec",
                JANumberFormatter.fmt(totaux.get(ALConstDeclarationVersement.TOTAL_RETROACTIF), true, true, false, 2));

        DataList ligneAnneeCour = new DataList("colonneMontantAnnee");

        // année de versement
        String[] anneeVers = {anneeVersement};

        ligneAnneeCour.addData("col_montant_annee_label",
                this.getText("al.declarationVersement.prestation.anneeCourante", langueDocument, anneeVers));

        ligneAnneeCour.addData("col_monnaie_label",
                this.getText("al.declarationVersement.montant.devise", langueDocument));

        ligneAnneeCour.addData("col_montant_annee",
                JANumberFormatter.fmt(totaux.get(ALConstDeclarationVersement.TOTAL_ANNEE), true, true, false, 2));

        // Impôt source
        DataList ligneImpotSource = new DataList("colonneMontantIS");
        ligneImpotSource.addData("col_montant_IS_label",
                this.getText("al.declarationVersement.prestation.impotSource", langueDocument));

        ligneImpotSource.addData("col_monnaie_label",
                this.getText("al.declarationVersement.montant.devise", langueDocument));

        ligneImpotSource.addData("col_montant_IS",
                JANumberFormatter.fmt(totaux.get(ALConstDeclarationVersement.TOTAL_IS), true, true, false, 2));

        // Ligne total
        DataList ligneTotal = new DataList("colonneMontantTotal");

        ligneTotal.addData("col_montantTotal_Label",
                this.getText("al.declarationVersement.montantTotal", langueDocument));

        ligneTotal.addData("col_monnaie_label", this.getText("al.declarationVersement.montant.devise", langueDocument));
        ligneTotal.addData("col_montant_total",
                JANumberFormatter.fmt(totaux.get(ALConstDeclarationVersement.TOTAL), true, true, false, 2));

        tableauVersement.add(ligneAnneePrec);
        tableauVersement.add(ligneAnneeCour);
        if (ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue() && JadeStringUtil.equals(typeDeclaration, ALCSDeclarationVersement.DECLA_VERS_DIR_IMP_SOURCE, false)) {
            tableauVersement.add(ligneImpotSource);
        }
        tableauVersement.add(ligneTotal);

        document.add(tableauVersement);
    }

    /**
     * Cette méthode va ajouter les ANS aux totaux. Uniquement si la propriété "vulpecula.comptesANS" existe !
     * (Spécifique BMS)
     *
     * @param dateDebut
     * @param dateFin
     * @param allocataire
     * @param totaux
     * @return
     * @throws ALDeclarationVersementException
     */
    private HashMap<String, String> recuperationANS(String dateDebut, String dateFin,
                                                    AllocataireComplexModel allocataire, HashMap<String, String> totaux) throws ALDeclarationVersementException {
        try {
            String comptesANS = JadePropertiesService.getInstance().getProperty("vulpecula.comptesANS");
            if (comptesANS != null && !JadeStringUtil.isBlankOrZero(comptesANS)) {
                List<String> compteANS = Arrays.asList(comptesANS.split("\\s*,\\s*"));
                AllocationSupplNaissanceCAManager allocationSupplNaissanceCAManager = new AllocationSupplNaissanceCAManager();
                allocationSupplNaissanceCAManager.setSession(BSessionUtil.getSessionFromThreadContext());
                allocationSupplNaissanceCAManager.setInIdExterne(compteANS);
                allocationSupplNaissanceCAManager.setForIdExterneRole(allocataire.getPersonneEtendueComplexModel()
                        .getPersonneEtendue().getNumAvsActuel());
                allocationSupplNaissanceCAManager.setDateValeurFrom(dateDebut);
                allocationSupplNaissanceCAManager.setDateValeurTo(dateFin);

                allocationSupplNaissanceCAManager.find();

                for (Iterator it = allocationSupplNaissanceCAManager.getContainer().iterator(); it.hasNext(); ) {
                    AllocationSupplNaissanceCA allocationSupplNaissanceCA = (AllocationSupplNaissanceCA) it.next();
                    String montant = allocationSupplNaissanceCA.getMontant();

                    if (JadeNumericUtil.isNumeric(montant)) {
                        Double d_montant = JadeStringUtil.parseDouble(montant, 0.00);
                        d_montant = d_montant * -1;
                        String anneeAlloc = allocationSupplNaissanceCA.getIdExterne().substring(0, 4);
                        String anneeEnCours = dateDebut.substring(6);

                        if (anneeAlloc.equals(anneeEnCours)) {
                            // Total année
                            Double d_total_annee = JadeStringUtil.parseDouble(
                                    totaux.get(ALConstDeclarationVersement.TOTAL_ANNEE), 0.00);
                            totaux.put(ALConstDeclarationVersement.TOTAL_ANNEE,
                                    String.valueOf(d_total_annee + d_montant));

                            Double d_total = JadeStringUtil.parseDouble(totaux.get(ALConstDeclarationVersement.TOTAL),
                                    0.00);
                            totaux.put(ALConstDeclarationVersement.TOTAL, String.valueOf(d_total + d_montant));
                        } else {
                            // Total rétro
                            Double d_total_retro = JadeStringUtil.parseDouble(
                                    totaux.get(ALConstDeclarationVersement.TOTAL_RETROACTIF), 0.00);
                            totaux.put(ALConstDeclarationVersement.TOTAL_RETROACTIF,
                                    String.valueOf(d_total_retro + d_montant));

                            Double d_total = JadeStringUtil.parseDouble(totaux.get(ALConstDeclarationVersement.TOTAL),
                                    0.00);
                            totaux.put(ALConstDeclarationVersement.TOTAL, String.valueOf(d_total + d_montant));
                        }
                    }

                }
            }
        } catch (Exception e) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementGlobalAbstractServiceImpl# setTable: Erreur traitement ANS ==> "
                            + e.getMessage());
        }

        return totaux;
    }
}
