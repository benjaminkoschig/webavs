package ch.globaz.al.businessimpl.services.adiDecomptes;

import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.models.dossier.CopieComplexModel;
import ch.globaz.al.business.models.dossier.CopieComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.adiDecomptes.AdiDecompteService;
import ch.globaz.al.businessimpl.documents.AbstractDocument;
import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe d'implémentation du service
 * 
 * @author PTA
 * 
 */
public abstract class AdiDecompteAbstractServiceImpl extends AbstractDocument implements AdiDecompteService {

    /**
     * Méthode qui charge les infos générales du document
     * 
     * @param doc
     *            document
     * 
     * @param decompteGlobal
     *            AdiDecompteComplexModel
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected void addInfos(DocumentData doc, AdiDecompteComplexModel decompteGlobal, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres
        if (doc == null) {
            throw new ALAdiDecomptesException("AdiDecompteAbstractServiceImpl# setInfos: doc is null");
        }

        if (decompteGlobal == null) {
            throw new ALAdiDecomptesException("AdiDecompteAbstractServiceImpl# setInfos: dossier is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALAdiDecomptesException("AdiDecompteAbstractServiceImpl# setInfos: language  " + langueDocument
                    + " is not  valid ");
        }

        // label pour no decompte
        doc.addData("info_num_dec_label", this.getText("al.adi.decompte.info.commun.decompte.label", langueDocument));
        // valeur pour no decompte
        doc.addData("info_num_dec_val", decompteGlobal.getDecompteAdiModel().getIdDecompteAdi() + "-"
                + decompteGlobal.getDecompteAdiModel().getDateReception());
        // label pour le dossier
        doc.addData("info_num_dos_label", this.getText("al.adi.decompte.info.commun.dossier.label", langueDocument));

        // valeur pour le dossier
        doc.addData("info_num_dos_val", decompteGlobal.getDecompteAdiModel().getIdDossier());
        // label pour l'affilié
        doc.addData("info_aff_label", this.getText("al.adi.decompte.info.commun.affilieNom.label", langueDocument));
        // valeur pour l'affilié
        AffiliationSearchSimpleModel searchModel = new AffiliationSearchSimpleModel();
        searchModel.setForNumeroAffilie(decompteGlobal.getDossierComplexModel().getDossierModel().getNumeroAffilie());

        // validité à la date du jour
        searchModel.setForDateValidite(JadeDateUtil.getGlobazFormattedDate(new Date()));
        searchModel = AFBusinessServiceLocator.getAffiliationService().find(searchModel);

        AffiliationSimpleModel affiliation = (AffiliationSimpleModel) searchModel.getSearchResults()[0];
        doc.addData("info_aff_val", affiliation.getRaisonSociale());
        // label pour le numéro de l'affilié
        doc.addData("info_num_aff_label", this.getText("al.adi.decompte.info.commun.affilieNum.label", langueDocument));

        // valeur pour le numéro d'affilié
        doc.addData("info_num_aff_val", decompteGlobal.getDossierComplexModel().getDossierModel().getNumeroAffilie());
        // allocataire label
        doc.addData("info_alloc_label",
                this.getText("al.adi.decompte.info.commun.allocataireNom.label", langueDocument));
        // allocataire valeur
        doc.addData("info_alloc_val", decompteGlobal.getDossierComplexModel().getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                + " "
                + decompteGlobal.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel()
                        .getTiers().getDesignation2());
        // nss label
        doc.addData("info_nss_label", this.getText("al.adi.decompte.info.commun.allocataireNss.label", langueDocument));
        // nss valeur
        doc.addData("info_nss_val", decompteGlobal.getDossierComplexModel().getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
        // organisme étranger label
        doc.addData("info_org_etr_label",
                this.getText("al.adi.decompte.info.commun.organeEtranger.label", langueDocument));

        // organisme étranger val
        AdministrationComplexModel caisse = TIBusinessServiceLocator.getAdministrationService().read(
                decompteGlobal.getDecompteAdiModel().getIdTiersOrganismeEtranger());
        doc.addData("info_org_etr_val", caisse.getTiers().getDesignation1());

        // monnaie label
        doc.addData("info_monnaie_etr_label",
                this.getText("al.adi.decompte.info.commun.monnaieEtrangere.label", langueDocument));
        // monnaie val
        doc.addData("info_monnaie_val",
                JadeCodesSystemsUtil.getCodeLibelle(decompteGlobal.getDecompteAdiModel().getCodeMonnaie()));

    }

    /**
     * Méthode qui ajoute le titre du document au document
     * 
     * @param document
     *            document auxquel il faut ajouter le tritre
     * @param decompteAdi
     *            selon AdiDecompteComplexModel
     * 
     * @param lanugeDocument
     *            langue du document
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public abstract void addTitreDocument(DocumentData document, AdiDecompteComplexModel decompteAdi,
            String langueDocument) throws JadeApplicationException;

    /**
     * méthode qui ajoute l'adresse du tiers originale de la décision
     * 
     * @param doc
     * @param idDossier
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */

    public void getDateAdresseOriginale(DocumentData doc, String idDossier, String langueDocument, String dateImpression)
            throws JadeApplicationException, JadePersistenceException {

        DossierComplexModel dossier = ALServiceLocator.getDossierComplexModelService().read(idDossier);

        CopieComplexSearchModel copies = new CopieComplexSearchModel();

        copies.setForTypeCopie(ALCSCopie.TYPE_DECISION);
        copies.setForIdDossier(idDossier);
        copies = ALServiceLocator.getCopieComplexModelService().search(copies);

        // reprend le tiers destinataire de la decision originale
        String idTiersCopieOriginale = ((CopieComplexModel) copies.getSearchResults()[0]).getCopieModel()
                .getIdTiersDestinataire();

        // recherche idTiersAffilie
        String idTiersAFfilie = AFBusinessServiceLocator.getAffiliationService().findIdTiersForNumeroAffilie(
                dossier.getDossierModel().getNumeroAffilie());

        if (JadeStringUtil.equals(idTiersCopieOriginale, idTiersAFfilie, false)) {
            // TODO faire appel à méthode avec également numéro d'affilié si l'idtiersOriginale est l'affilié
            this.addDateAdresse(doc, dateImpression, idTiersCopieOriginale, langueDocument, dossier.getDossierModel()
                    .getNumeroAffilie());
        } else {
            this.addDateAdresse(doc, dateImpression, idTiersCopieOriginale, langueDocument, null);
        }

    }

    /**
     * Méthode qui
     * 
     * @param decompteGlobal
     *            selon AdiDecompteGlobal
     * @param typeDecompte
     *            type de décompte
     * @return DocumentData
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    @Override
    public DocumentData getDocument(AdiDecompteComplexModel decompteGlobal, String typeDecompte, DocumentData doc,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {

        // ajoute les infos communes à tous les documents adi
        // logo caisse
        // this.setIdEntete(doc);
        addInfos(doc, /* periode, */decompteGlobal, langueDocument);

        return doc;
    }

    /**
     * méthode qui retourne le libellé de droit ménage, enfant ou formation en focntion de la langue
     * 
     * @param typeDroit
     * @param langueDocument
     * @return
     * @throws JadeApplicationException
     */
    protected String getTextTypeDroit(String typeDroit, String langueDocument) throws JadeApplicationException {
        // contrôle des paramètres
        if (!JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_FORM, false)
                && !JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_ENF, false)
                && !JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_MEN, false)) {
            throw new ALAdiDecomptesException("AdiDecompteDetailleAbstractServiceImpl#getTextTypeDroit: " + typeDroit
                    + " is not valid");
        }
        if (JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_ENF, false)) {
            return this.getText("al.adi.decompte.global.droit.droitEnfant.valeur", langueDocument);
        } else if (JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_FORM, false)) {
            return this.getText("al.adi.decomtpe.global.droit.droitFormation.valeur", langueDocument);
        } else {
            return this.getText("al.adi.decompte.global.droit.droitMenage.valeur", langueDocument);
        }
    }

}
