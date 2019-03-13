package ch.globaz.al.businessimpl.services.recapitulatifs;

import ch.globaz.al.business.constantes.*;
import ch.globaz.al.business.exceptions.document.ALDocumentException;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseImpressionModelException;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.models.attribut.AttributEntiteModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.DetailPrestationSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.recapitulatifs.RecapitulatifsListeAffilieService;
import ch.globaz.al.businessimpl.documents.AbstractDocument;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe d'impl�mentation des services li�s aux r�capitulatifs d'entreprise
 *
 * @author PTA
 *
 */
public class RecapitulatifsListeAffilieServiceImpl extends AbstractDocument
        implements RecapitulatifsListeAffilieService {

    /**
     * M�thode retournant texte ADI/ADC
     *
     * @param statutDossier
     * @param langue
     * @param forCsv - indique si il faut le libell� pour le CSV(true) ou PDF (false)
     * @return
     * @throws Exception
     * @throws JadeNoBusinessLogSessionError
     */
    private String getStatutDossier(String statutDossier, boolean forCsv, String langue)
            throws JadeApplicationException, JadePersistenceException {
        // contr�le param�tre
        if (!JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langue, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDocumentException(
                    "RecapitulatifsListeAffilieServiceImpl#getStatutDossier: language  " + langue + " is not  valid ");
        }
        // statut du dossier
        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSDossier.GROUP_STATUT, statutDossier)) {
                JadeThread.logError(RecapitulatifsListeAffilieServiceImpl.class.getName(),
                        "statutDossier is not valid");
            }
        } catch (Exception e) {
            throw new ALRecapitulatifEntrepriseImpressionModelException("statutDossier is not valid ", e);

        }

        if (JadeStringUtil.equals(statutDossier, ALCSDossier.STATUT_IS, false)) {
            if (forCsv) {
                return " " + this.getText("al.recapitulatif.csv.colonne.dossier.statutIs", langue);
            } else {
                return " " + this.getText("al.recapitulatif.colonne.dossier.statutIs", langue);
            }

        } else if (JadeStringUtil.equals(statutDossier, ALCSDossier.STATUT_CS, false)) {

            if (forCsv) {
                return " " + this.getText("al.recapitulatif.csv.colonne.dossier.statutCs", langue);
            } else {
                return " " + this.getText("al.recapitulatif.colonne.dossier.statutCs", langue);
            }

        } else {
            return ALConstDocument.STATUT_DOSSIER_VIDE;
        }

    }

    @Override
    public DocumentData loadData(ArrayList recapitulatifs, String numAffilie, String idRecap, String periodeDe,
                                 String periodeA, String agenceCommunaleAvs, String activiteAllocataire, String dateImpression,
                                 String typeBonification) throws JadePersistenceException, JadeApplicationException {

        // contr�le des param�tres
        if (recapitulatifs == null) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#loadData: recapitulatifs is null");
        }
        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#loadData: numAffilie  is null");
        }
        if (JadeStringUtil.isEmpty(idRecap)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#loadData: idRecap  is null");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(periodeDe)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#loadData:  periode debut: " + periodeDe
                            + " is not a globaz date's (month year) valid");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#loadData: periode to" + periodeA
                            + " is not a globaz date's (month year) valid");
        }

        // langue du document
        String langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(numAffilie);

        DocumentData document = new DocumentData();

        setIdDocument(document);

        // logo caisse
        this.setIdEntete(document, activiteAllocataire, ALConstDocument.RECAPITULATIF_DOCUMENT, langueDocument);

        this.addDateAdresse(document, dateImpression,
                AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(numAffilie),
                langueDocument, numAffilie);

        // ent�te
        // recherche pour affichage du dossier ou du num�ro externe
        AttributEntiteModel attributNumSalarie = ALServiceLocator.getAttributEntiteModelService()
                .getAttributAffilieByNumAffilie(ALConstAttributsEntite.AFFICHAGE_NUMERO_SALARIE, numAffilie);
        boolean numSalarieDisplay = false;

        if ((attributNumSalarie != null) && JadeStringUtil.equals("true", attributNumSalarie.getValeurAlpha(), true)) {

            numSalarieDisplay = true;
        } else {
            numSalarieDisplay = false;
        }

        if (recapitulatifs.size() > 0) {
            setEntete(document, langueDocument, numAffilie, numSalarieDisplay);
            // donn�es
            setTable(document, recapitulatifs, idRecap, numAffilie, langueDocument, numSalarieDisplay);
        }

        // infos globales
        setInfos(document, numAffilie, idRecap, periodeDe, periodeA, typeBonification, langueDocument);

        // donn�es pour agence communale AVS si allocataire non actif
        if (JadeStringUtil.equals(activiteAllocataire, ALCSDossier.ACTIVITE_NONACTIF, false)) {
            setAgenceCommunaleAvs(document, agenceCommunaleAvs, langueDocument);
        }

        return document;
    }

    @Override
    public StringBuffer loadDataCSV(ArrayList listTempRecap, Boolean isCharNssRecap) throws JadePersistenceException, JadeApplicationException {
        // v�rification des param�tres
        if (listTempRecap == null) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#loadData: recapitulatifs is null");
        } 
        
        StringBuffer csvContent = new StringBuffer();

        // langue du document
        String numAffilie = ((RecapitulatifEntrepriseImpressionComplexModel) listTempRecap.get(0))
                .getRecapEntrepriseModel().getNumeroAffilie();
        String langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(numAffilie);

        csvContent.append(this.getText("al.recapitulatif.csv.entete.affilie", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.entete.numeroDossier", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.entete.NSS", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.csv.entete.nom", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.csv.entete.prenom", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.csv.entete.periode", langueDocument)).append(";;");
        csvContent.append(this.getText("al.recapitulatif.csv.entete.prestde", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.csv.entete.presta", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.csv.entete.nbrunite", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.csv.entete.unite", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.entete.nombre.enfant", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.entete.montant", langueDocument)).append(";");
        csvContent.append(this.getText("al.recapitulatif.csv.entete.complement", langueDocument)).append(";");
        csvContent.append(" \n");

        Iterator iter = listTempRecap.iterator();
        while (iter.hasNext()) {

            RecapitulatifEntrepriseImpressionComplexModel lignesRecap = (RecapitulatifEntrepriseImpressionComplexModel) iter
                    .next();
            // num�ro de l'affili�
            csvContent.append(lignesRecap.getRecapEntrepriseModel().getNumeroAffilie()).append(";");
            // num�ro dossier
            csvContent.append(lignesRecap.getIdDossier()).append(";");
            
            if (isCharNssRecap){
                csvContent.append("'").append(JadeStringUtil.removeChar(lignesRecap.getNumNSS(), '.')).append(";");
            } else {
                csvContent.append(JadeStringUtil.removeChar(lignesRecap.getNumNSS(), '.')).append(";");
            }

            // nom
            csvContent.append(lignesRecap.getNomAllocataire()).append(" ;");
            // pr�nom de l'allocataire pour la recap
            csvContent.append(lignesRecap.getPrenomAllocataire()).append(";");
            // d�but de la p�riode
            csvContent
                    .append(JadeStringUtil.substring(lignesRecap.getRecapEntrepriseModel().getPeriodeDe(), 3)
                            + JadeStringUtil.substring(lignesRecap.getRecapEntrepriseModel().getPeriodeDe(), 0, 2))
                    .append(";");
            // fin de la p�riode pour la r�cap
            csvContent
                    .append(JadeStringUtil.substring(lignesRecap.getRecapEntrepriseModel().getPeriodeA(), 3)
                            + JadeStringUtil.substring(lignesRecap.getRecapEntrepriseModel().getPeriodeA(), 0, 2))
                    .append(";");
            // p�riode d�but de l'ent�te de la recap
            csvContent.append(JadeStringUtil.substring(lignesRecap.getPeriodeDeEntete(), 3)
                    + JadeStringUtil.substring(lignesRecap.getPeriodeDeEntete(), 0, 2)).append(";");
            // ap�riode fin p�riode pour l'enteter
            csvContent.append(JadeStringUtil.substring(lignesRecap.getPeriodeAEntete(), 3)
                    + JadeStringUtil.substring(lignesRecap.getPeriodeAEntete(), 0, 2)).append(";");

            // nombre d'unit�
            csvContent.append(lignesRecap.getNbreUnite()).append(";");
            // unit�

            // Traitement pour le type d'unit�
            if (JadeStringUtil.equals(lignesRecap.getTypeUnite(), ALCSDossier.UNITE_CALCUL_HEURE, false)) {
                csvContent.append(this.getText("al.recapitulatif.colonne.typeUnite.heure", langueDocument)).append(";");
            } else if (JadeStringUtil.equals(lignesRecap.getTypeUnite(), ALCSDossier.UNITE_CALCUL_MOIS, false)) {
                csvContent.append(this.getText("al.recapitulatif.colonne.typeUnite.mois", langueDocument)).append(";");
            } else if (JadeStringUtil.equals(lignesRecap.getTypeUnite(), ALCSDossier.UNITE_CALCUL_JOUR, false)) {
                csvContent.append(this.getText("al.recapitulatif.colonne.typeUnite.jour", langueDocument)).append(";");
            } else if (JadeStringUtil.equals(lignesRecap.getTypeUnite(), ALCSDossier.UNITE_CALCUL_SPECIAL, false)) {
                csvContent.append("").append(";");
            }

            // nombre d'enfants
            csvContent.append(lignesRecap.getNbrEnfant()).append(";");
            // montant
            csvContent.append(lignesRecap.getMontant()).append(";");
            // compl�ment
            csvContent.append(getStatutDossier(lignesRecap.getStatutDossier(), true, langueDocument)).append(";");

            // cellules devient ligne
            csvContent.append(" \n");
            // ajout de la ligne au tableau

            // retourne une liste comportant les lignes d'une r�capitulatifs

        }
        return csvContent;
    }

    /**
     * M�thode qui ajoute l'agence communale avs
     *
     * @param document
     *            document � compl�ter
     * @param AgenceCommunaleAvs
     *            libell� de l'agence communale
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e
     */
    private void setAgenceCommunaleAvs(DocumentData document, String AgenceCommunaleAvs, String langueDocument)
            throws JadePersistenceException, JadeApplicationException {
        // contr�le des param�tres
        if (document == null) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#setAgenceCommunaleAvs: document is null");

        }
        if (JadeStringUtil.isEmpty(AgenceCommunaleAvs)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#setAgenceCommunaleAvs: agenceCommunaleAvs is empty");

        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl# setAgenceCommunaleAvs: language  " + langueDocument
                            + " is not  valid ");
        }

        // ajout de l'agence communale avs
        document.addData("recapitulatifEntreprise_agenceCommunaleAvs",
                this.getText("al.recapitulatif.copieA", langueDocument) + " " + AgenceCommunaleAvs);

    }

    /*
     * G�re la valeur du champ "recapitulatifEntreprise_alloc_label" du template listeRecapitulatifEntreprise
     */
    private DocumentData setAllocLabel(DocumentData document, String langueDocument, String etatRecap,
                                       String typeBonification)
            throws JadeApplicationException, JadePersistenceException, JadeApplicationServiceNotAvailableException {

        String nomCaisse = ALServiceLocator.getParametersServices().getNomCaisse().toLowerCase();
        String texteComplementCaisse = this.getText("al.recapitulatif.infos.allocation.complement.".concat(nomCaisse),
                langueDocument);

        // si le libell� n'est pas trouv�, on ne met rien
        if ("al.recapitulatif.infos.allocation.complement.".concat(nomCaisse).equals(texteComplementCaisse)) {
            texteComplementCaisse = "";
        }

        String texteCommun = this.getText("al.recapitulatif.infos.allocation", langueDocument);
        String textePaiementDirect = this.getText("al.recapitulatif.infos.direct.paiement", langueDocument);

        if (JadeStringUtil.equals(typeBonification, ALCSPrestation.BONI_DIRECT, false)
                || JadeStringUtil.equals(typeBonification, ALCSPrestation.BONI_RESTITUTION, false)) {

            // la CVCI affiche le complement sp�cifique sur les r�caps directs SA
            if (ALCSPrestation.ETAT_SA.equals(etatRecap)
                    && JadeStringUtil.equals(ALConstCaisse.CAISSE_CVCI, nomCaisse, true)) {
                document.addData("recapitulatifEntreprise_alloc_label",
                        texteCommun + " " + textePaiementDirect.concat(texteComplementCaisse));
            } else {
                document.addData("recapitulatifEntreprise_alloc_label", texteCommun + " " + textePaiementDirect);
            }

        } else {

            if (JadeStringUtil.equals(ALConstCaisse.CAISSE_CCVD, nomCaisse, true)
                    || JadeStringUtil.equals(ALConstCaisse.CAISSE_CVCI, nomCaisse, true)) {
                document.addData("recapitulatifEntreprise_alloc_label", texteCommun.concat(texteComplementCaisse));
            } else if (ALConstCaisse.CAISSE_FVE.equals(nomCaisse.toUpperCase())) {
                document.addData("recapitulatifEntreprise_alloc_label", texteCommun.concat(". "));
                document.addData("recapitulatifEntreprise_complement_label", texteComplementCaisse);
            } else {
                if (ALCSPrestation.ETAT_SA.equals(etatRecap)) {
                    document.addData("recapitulatifEntreprise_alloc_label", texteCommun.concat(texteComplementCaisse));
                } else {
                    document.addData("recapitulatifEntreprise_alloc_label", texteCommun);
                }
            }
        }

        return document;

    }

    /**
     * D�finition des constantes ent�te de la r�cap
     *
     * @param document
     * @param langueDocument
     * @param numeroAffilie
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    private void setEntete(DocumentData document, String langueDocument, String numeroAffilie, boolean numSalarie)
            throws JadeApplicationException, JadePersistenceException {
        // D�finition des entetes

        // entete allocataire
        document.addData("entete_Allocataires", this.getText("al.recapitulatif.entete.allocataire", langueDocument));
        // entete p�riode de travail
        document.addData("entete_periodes_travail", this.getText("al.recapitulatif.entete.periode", langueDocument));
        // entete AF
        document.addData("entete_AF", this.getText("al.recapitulatif.entete.allocation", langueDocument));
        // entete NSS
        document.addData("entete_nss", this.getText("al.recapitulatif.entete.NSS", langueDocument));
        // entete idDossier ou no salari� extene

        // si cantonal suppl�tif ou international suppl�tif, ajouter texte *ADI ou *ADC

        if (numSalarie) {

            document.addData("entete_idDossier",
                    this.getText("al.recapitulatif.entete.salarieExterne", langueDocument));
        } else {
            document.addData("entete_idDossier", this.getText("al.recapitulatif.entete.numeroDossier", langueDocument));
        }

        // entete nom et pr�nom
        document.addData("entete_nom_prenom", this.getText("al.recapitulatif.entete.nom.prenom", langueDocument));
        // entete d�but p�riode
        document.addData("entete_debut", this.getText("al.recapitulatif.entete.periode.debut", langueDocument));
        // entete fin p�riode
        document.addData("entete_fin", this.getText("al.recapitulatif.entete.periode.fin", langueDocument));
        // entete nbre d'enfants
        document.addData("entete_nbr_enfant", this.getText("al.recapitulatif.entete.nombre.enfant", langueDocument));
        // entete CHF
        document.addData("entete_CHF", this.getText("al.recapitulatif.entete.monnaie", langueDocument));
        // entete dates mutations
        document.addData("entete_date_mutation", this.getText("al.recapitulatif.entete.date.mutation", langueDocument));
        // entete unit� de travail
        document.addData("entete_unites_travail",
                this.getText("al.recapitulatif.entete.unite.nombre.type", langueDocument));
        // entete montant
        document.addData("entete_montant", this.getText("al.recapitulatif.entete.montant", langueDocument));
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("RecapitulatifsListeAffilieServiceImpl#setIdDocument : document is null");
        }

        document.addData("AL_idDocument", "AL_recapitulatifEntreprise");
    }

    /**
     * M�thode qui charge les infos relatives � la r�capitulation
     *
     * @param document
     *            document � compl�ter
     * @param numAffilie
     *            num�ro d'affili�
     * @param idRecap
     *            identifiant de la recapitulation
     * @param periodeDe
     *            d�but de la p�riode de la r�cap
     * @param periodeA
     *            fin de la p�riode de la r�cap
     * @param typeBonification
     *            direct, indirect, restitution
     * @param langueDocument
     *            langue du document
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     */
    private void setInfos(DocumentData document, String numAffilie, String idRecap, String periodeDe, String periodeA,
                          String typeBonification, String langueDocument) throws JadeApplicationException, JadePersistenceException {

        // contr�le des param�tres
        if (document == null) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#setInfos: document is null");

        }
        if (JadeStringUtil.isEmpty(numAffilie)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#setInfos: numAffilie is empty");
        }

        if (JadeStringUtil.isEmpty(idRecap)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#setInfos: idRecap is empty");

        }
        if (!JadeDateUtil.isGlobazDateMonthYear(periodeDe)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#setInfos:periodeDe: " + periodeDe
                            + " is not a globaz date's (MM.YYYY) valid");
        }
        if (!JadeDateUtil.isGlobazDateMonthYear(periodeA)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#setInfos:periodeA: " + periodeA
                            + " is not a globaz date's (MM.YYYY) valid");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#setInfos: language  " + langueDocument + " is not  valid ");
        }
        // �tat de la r�cap � provisoire

        String etatRecap = ALServiceLocator.getRecapitulatifEntrepriseModelService().read(idRecap).getEtatRecap();
        if (JadeStringUtil.equals(ALCSPrestation.ETAT_SA, etatRecap, false)) {
            document.addData("recapitulatifEntreprise_texteProvisoire",
                    this.getText("al.recapitulatifEntreprise.provisoire.label", langueDocument));
            document.addData("recapitulatifEntreprise_texteProvisoireAllocVer",
                    this.getText("al.recapitulatif.texte.recProvisoire", langueDocument));

        } else {
            document.addData("recapitulatifEntreprise_texteDefinitif",
                    this.getText("al.recapitulatifEntreprise.definitif.label", langueDocument));
        }

        // p�riode de la de la r�capitulation
        if (JadeStringUtil.equals(periodeDe, periodeA, false)) {
            document.addData("recapitulatifEntreprise_periode_label",
                    this.getText("al.recapitulatif.infos.periode.label", langueDocument));
            document.addData("recapitulatifEntreprise_periode", periodeA);
        } else {
            document.addData("recapitulatifEntreprise_periode_label",
                    this.getText("al.recapitulatif.infos.periode.label", langueDocument));
            document.addData("recapitulatifEntreprise_periode", periodeDe + "-" + periodeA);
        }

        // affilie
        document.addData("recapitulatifEntreprise_affilie", numAffilie);
        // identifiant de la r�capitulation
        document.addData("recapitulatifEntreprise_recap_label",
                this.getText("al.recapitulatif.infos.recap.label", langueDocument));
        // relative � la r�capitulation
        document.addData("recapitulatifEntreprise_recap_label",
                this.getText("al.recapitulatif.infos.recap.label", langueDocument));
        document.addData("recapitulatifEntreprise_recap_num", idRecap);

        document = setAllocLabel(document, langueDocument, etatRecap, typeBonification);
        document = setSignatureDate(document, langueDocument, etatRecap);

    }

    /* G�re l'affichage de la signature dans le template recapitulatifEntreprise.odt */
    private DocumentData setSignatureDate(DocumentData document, String langueDocument, String etatRecap)
            throws JadeApplicationException, JadePersistenceException, JadeApplicationServiceNotAvailableException {

        String nomCaisse = ALServiceLocator.getParametersServices().getNomCaisse().toLowerCase();
        String texteSignature = this.getText("al.recapitulatif.conformeSignature." + nomCaisse, langueDocument);
        // si le libell� n'est pas trouv�, on prend le standard (al.recapitulatif.conformeSignature)
        if ("al.recapitulatif.conformeSignature.".concat(nomCaisse).equals(texteSignature)) {
            texteSignature = this.getText("al.recapitulatif.conformeSignature", langueDocument);
        }

        if (JadeStringUtil.equals(ALConstCaisse.CAISSE_CCVD, nomCaisse.toUpperCase(), true)
                || JadeStringUtil.equals(ALConstCaisse.CAISSE_CVCI, nomCaisse.toUpperCase(), true)
                || JadeStringUtil.equals(ALConstCaisse.CAISSE_FVE, nomCaisse.toUpperCase(), true)) {
            document.addData("recapitulatifEntreprise_date_signature",
                    this.getText("al.recapitulatif.dateFin", langueDocument) + "_______________" + "    "
                            + texteSignature + "_________________");
        } else {
            if (ALCSPrestation.ETAT_SA.equals(etatRecap)) {
                document.addData("recapitulatifEntreprise_date_signature",
                        this.getText("al.recapitulatif.dateFin", langueDocument) + "_______________" + "    "
                                + texteSignature + "_________________");
            }
        }

        return document;

    }

    /**
     * M�thode qui charge les diff�rentes lignes de la liste du r�capitulatif
     *
     * @param document
     *            document � compl�ter
     * @param numeroAffilie
     *            num�ro de l'affili�
     * @param prestations
     *            lignes r�capitulatives de la r�cap
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e
     */

    private void setTable(DocumentData document, ArrayList prestations, String idRecap, String numeroAffilie,
                          String langueDocument, boolean numSalarieDisplay)
            throws JadeApplicationException, JadePersistenceException {
        // v�rification des param�tres

        if (document == null) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#setTable: document is null");

        }

        if (prestations == null) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifEntrepriseImpressionServiceImpl#setTable: recapitulatifs is null");

        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALRecapitulatifEntrepriseImpressionModelException(
                    "RecapitulatifsListeAffilieServiceImpl#setTable: language  " + langueDocument + " is not  valid ");
        }

        boolean droitsVaudAcquisExist = false;

        // montant total pour une r�cap
        String montantTotal = ALServiceLocator.getRecapitulatifEntrepriseBusinessService()
                .calculMontantPourUneRecapEntreprise(idRecap, prestations);

        Collection table = new Collection("liste_recapitulatif_entreprise");
        if (prestations.size() != 0) {

            // parcourir la liste des r�capitulatifs pour les ajouter
            Iterator iter = prestations.iterator();
            while (iter.hasNext()) {

                RecapitulatifEntrepriseImpressionComplexModel recapModel = (RecapitulatifEntrepriseImpressionComplexModel) iter
                        .next();

                DataList ligne = new DataList("colonne");
                // Traitement pour le nss
                ligne.addData("col_nss", recapModel.getNumNSS());

                if (numSalarieDisplay) {

                    ligne.addData("col_idDossier", recapModel.getNumSalarieExterne()
                            + getStatutDossier(recapModel.getStatutDossier(), false, langueDocument));
                } else {
                    ligne.addData("col_idDossier", recapModel.getIdDossier()
                            + getStatutDossier(recapModel.getStatutDossier(), false, langueDocument));
                }

                // Traitement pour le nom et pr�nom de l'allocataire
                ligne.addData("col_allocataire",
                        recapModel.getNomAllocataire() + " " + recapModel.getPrenomAllocataire());
                // Traitement pour les Date de d�but de la mutation et la date
                // de
                // fin de mutation
                if (JadeStringUtil.equals(recapModel.getPeriodeDeEntete(),
                        recapModel.getRecapEntrepriseModel().getPeriodeDe(), false)
                        && JadeStringUtil.equals(recapModel.getPeriodeAEntete(),
                                recapModel.getRecapEntrepriseModel().getPeriodeA(), false)) {
                    // Traitement pour la date de d�but de mutation
                    ligne.addData("col_periode_debut", "");
                    // Traitement pour la Date de fin de la mutation
                    ligne.addData("col_periode_fin", "");
                } else {
                    // Traitement pour la date de d�but de mutation
                    ligne.addData("col_periode_debut", recapModel.getPeriodeDeEntete());
                    // Traitement pour la Date de fin de la mutation
                    ligne.addData("col_periode_fin", recapModel.getPeriodeAEntete());

                }
                // Traitement pour le nombre unit�
                ligne.addData("col_nbre_unite",
                        JadeNumericUtil.isZeroValue(recapModel.getNbreUnite()) ? " " : recapModel.getNbreUnite());
                // Traitement pour le type d'unit�
                if (JadeStringUtil.equals(recapModel.getTypeUnite(), ALCSDossier.UNITE_CALCUL_HEURE, false)) {
                    ligne.addData("col_type_unite",
                            this.getText("al.recapitulatif.colonne.typeUnite.heure", langueDocument));
                } else if (JadeStringUtil.equals(recapModel.getTypeUnite(), ALCSDossier.UNITE_CALCUL_MOIS, false)) {
                    ligne.addData("col_type_unite",
                            this.getText("al.recapitulatif.colonne.typeUnite.mois", langueDocument));

                } else if (JadeStringUtil.equals(recapModel.getTypeUnite(), ALCSDossier.UNITE_CALCUL_JOUR, false)) {
                    ligne.addData("col_type_unite",
                            this.getText("al.recapitulatif.colonne.typeUnite.jour", langueDocument));
                }

                // traitement pour le nombre d'enfants
                if (!JadeStringUtil.isIntegerEmpty(recapModel.getNbrEnfant())) {
                    ligne.addData("col_nbrEnfant", recapModel.getNbrEnfant());

                }

                // INFOROMD0028 : Si il y a un tarif forc� "Vaud droit acquis" on ajout une '*'
                DetailPrestationSearchModel detailPrestationSearchModel = new DetailPrestationSearchModel();
                detailPrestationSearchModel.setForIdEntetePrestation(recapModel.getIdEntete());
                detailPrestationSearchModel = ALImplServiceLocator.getDetailPrestationModelService()
                        .search(detailPrestationSearchModel);
                DetailPrestationModel detailPrestationModel = new DetailPrestationModel();

                if (detailPrestationSearchModel.getSize() > 0) {
                    detailPrestationModel = (DetailPrestationModel) detailPrestationSearchModel.getSearchResults()[0];
                }

                if (JadeStringUtil.equals(ALCSTarif.CATEGORIE_VD_DROIT_ACQUIS,
                        detailPrestationModel.getCategorieTarif(), false)) {
                    // traitement pour le montant avec ajout de l'asterisque
                    ligne.addData("col_montant",
                            "* " + JANumberFormatter.fmt(recapModel.getMontant(), true, true, false, 2));
                    droitsVaudAcquisExist = true;
                } else {
                    // traitement pour le montant
                    ligne.addData("col_montant", JANumberFormatter.fmt(recapModel.getMontant(), true, true, false, 2));
                }

                // ajout de la ligne au tableau
                table.add(ligne);
            }

            DataList ligne = new DataList("colonneMontant");
            // le libell� correspond � l colonne nss
            ligne.addData("col_montant_label", this.getText("al.recapitulatif.colonne.montantTotal", langueDocument));
            ligne.addData("col_montant_total", JANumberFormatter.fmt(montantTotal, true, true, false, 2));
            table.add(ligne);

        }
        document.add(table);

        if (droitsVaudAcquisExist) {
            document.addData("recapitulatifEntreprise_texte_droit_acquis_vaud",
                    this.getText("al.recapitulatif.infos.recap.droit_acquis_vaud", langueDocument));
        }

    }

}
