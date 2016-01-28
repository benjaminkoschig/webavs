package ch.globaz.al.businessimpl.services.adiDecomptes;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe abstract pour la génération des documents de décompte adi détaillé
 * 
 * @author PTA
 * 
 */

public abstract class AdiDecompteDetailleAbstractServiceImpl extends AdiDecompteAbstractServiceImpl {

    /**
     * Méthode qui ajoute les différents tableaux d'une liste de prestations adi d'un même enfant
     * 
     * @param doc
     *            document à remplir
     * @param listDroitsAdiUnEnfant
     *            liste des prestations de l'enfant
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     */

    protected void addListDroitsAdiEnfant(DocumentData doc, ArrayList<AdiEnfantMoisComplexModel> listDroitsAdiUnEnfant,
            HashMap<String, String> donneesDecompte, Collection tableauDetaille, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {

        Iterator<AdiEnfantMoisComplexModel> iter = listDroitsAdiUnEnfant.iterator();

        DataList ligneEnfant = new DataList("enfant");

        // DroitLabel
        ligneEnfant.addData("enfant_droit_label",
                this.getText("al.adi.decompte.detail.infoGeneral.droit.label", langueDocument));
        // nom, prénom, date Naissance,Nss
        ligneEnfant.addData("enfant_donnees_valeur", ": " + donneesDecompte.get("donneeEnfant"));

        // type allocation

        ligneEnfant.addData("enfant_type_droit", /*
                                                  * adiDroit .getTypeDroit()
                                                  */
                " - " + this.getText("al.adi.decompte.detail.infoGeneral.droitType", langueDocument) + " " +

                this.getText(getTextTypeDroit(donneesDecompte.get("typeDroit"), langueDocument)));

        // ajout de la ligne enfant au tableau
        tableauDetaille.add(ligneEnfant);

        DataList titreDetail = new DataList("entete");
        // ajout des titres de colonnes :Droit label
        titreDetail.addData("entete_adi_det_mois",
                this.getText("al.adi.decompte.detail.infoDetaille.mois.label", langueDocument));
        // Montants suisses label
        titreDetail.addData("entete_adi_det_mont_ch",
                this.getText("al.adi.decompte.detail.infoDetaille.prestationsSuisses.label", langueDocument));
        // montants étrangers label
        titreDetail.addData("entete_adi_det_mont_etr",
                this.getText("al.adi.decompte.detail.infoDetaille.prestationsEtrangeres.label", langueDocument));
        // différence à verser label
        titreDetail.addData("entete_adi_det_mont_dif",
                this.getText("al.adi.decompte.detail.infoDetaille.prestationsAVerser.label", langueDocument));

        // ajoute de la liste au tableau
        tableauDetaille.add(titreDetail);

        DataList titreListeDetail = new DataList("enfantDetail");

        // mois label

        titreListeDetail.addData("entete_adi_mois_label", "");

        // Droit label
        titreListeDetail.addData("entete_adi_det_mont_droit",
                this.getText("al.adi.decompte.detail.infoDetaille.droitMontant.label", langueDocument));
        // ménage famille label
        titreListeDetail.addData("entete_adi_det_mont_men",
                this.getText("al.adi.decompte.detail.infoDetaille.menageFamille.label", langueDocument));
        // Total suisse label
        titreListeDetail.addData("entete_adi_det_mont_tot",
                this.getText("al.adi.decompte.detail.infoDetaille.droitTotalMontant.label", langueDocument));
        // monnaieEtrangère label
        titreListeDetail.addData("entete_adi_det_mon_etr",
                this.getText("al.adi.decompte.detail.infoDetaille.droitMontantEtranger.label", langueDocument));
        // Cours label
        titreListeDetail.addData("entete_adi_det_cours",
                this.getText("al.adi.decompte.detail.infoDetaille.coursMonnaieEtrange.label", langueDocument));
        // chf label
        titreListeDetail.addData("entete_adi_det_mont_chf",
                this.getText("al.adi.decompte.detail.infoDetaille.monnaieChf.label", langueDocument));
        // Adi en chf label
        titreListeDetail.addData("entete_adi_det_adi_chf",
                this.getText("al.adi.decompte.detail.infoDetaille.adiMontant.label", langueDocument));

        // ajout des lignes de titre au tableau
        tableauDetaille.add(titreListeDetail);

        // iterateur pour le tableau d'information du droit
        while (iter.hasNext()) {

            // traitement de la prestation adi de l'enfant pour un mois
            AdiEnfantMoisComplexModel adiMemeEnfantMois = iter.next();

            DataList listDonnees = new DataList("colonne");
            // mois
            listDonnees.addData("col_mois",
                    JadeStringUtil.remove(adiMemeEnfantMois.getAdiEnfantMoisModel().getMoisPeriode(), 2, 8));
            // montant du droit
            listDonnees.addData("col_mont_droit", adiMemeEnfantMois.getAdiEnfantMoisModel().getMontantAllocCH());
            // montant ménage réparti
            Double montant = JadeStringUtil.toDouble(adiMemeEnfantMois.getAdiEnfantMoisModel().getMontantRepartiCH());

            listDonnees.addData("col_mont_men", (montant > 0.00) ? adiMemeEnfantMois.getAdiEnfantMoisModel()
                    .getMontantRepartiCHTotal()
                    + " : "
                    + adiMemeEnfantMois.getAdiEnfantMoisModel().getNbrEnfantFamille()
                    + "="
                    + adiMemeEnfantMois.getAdiEnfantMoisModel().getMontantRepartiCH() : adiMemeEnfantMois
                    .getAdiEnfantMoisModel().getMontantRepartiCH());
            // total suisse
            listDonnees.addData("col_total_ch", adiMemeEnfantMois.getAdiEnfantMoisModel().getMontantCHTotal());
            // Montant touché à l'étranger
            listDonnees.addData("col_mont_etr", JANumberFormatter.fmt(adiMemeEnfantMois.getAdiEnfantMoisModel()
                    .getMontantAllocEtr(), true, true, false, 2));
            // coursMonnaie
            listDonnees.addData("col_cours", JANumberFormatter.fmt(adiMemeEnfantMois.getAdiEnfantMoisModel()
                    .getCoursChangeMonnaie(), true, true, false, 6));
            // montant étranger en chf
            listDonnees.addData("col_mont_etr_chf", JANumberFormatter.fmt(adiMemeEnfantMois.getAdiEnfantMoisModel()
                    .getMontantEtrTotalEnCh(), true, true, false, 2));
            // Totao ADi en france suisse par période
            listDonnees.addData("col_mont_adi_chf", JANumberFormatter.fmt(adiMemeEnfantMois.getAdiEnfantMoisModel()
                    .getMontantAdi(), true, true, false, 2));

            // ajout de la listDonnées au tableau
            tableauDetaille.add(listDonnees);

        }

        DataList totalEnfant = new DataList("colonneTotal");
        // ajout du total par enfant
        // calcul du total du droit d'un enfnat

        totalEnfant.addData("col_tot_enfant", JANumberFormatter.fmt(ALServiceLocator.getCalculAdiBusinessService()
                .getTotalVerseParDroitAvecSupp(donneesDecompte.get("idDecompte"), donneesDecompte.get("idEnfant")),
                true, true, false, 2));

        // ajout de la ligne total au tableau
        tableauDetaille.add(totalEnfant);

        // ajout d'un ligne vide
        DataList ligneVide = new DataList("ligneVide");
        // ajout de la ligne vide au tableau
        tableauDetaille.add(ligneVide);

    }

    /**
     * Méthode qui remplit le tableau détaillé des adi
     * 
     * @param doc
     *            document auxqule il faut ajoute le tableau
     * @param adiDroitSearch
     *            selon modèle complexe AdiDroitComplexModel
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected void addTableauDetaille(DocumentData doc,
    /* AdiDroitComplexSearchModel adiDroitSearch */AdiEnfantMoisComplexSearchModel adiSearch, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {

        HashMap<String, String> donneesDecompte = new HashMap<String, String>();

        // identifiant de l'enfant
        String prenomEnfant = null;
        String dateNaissanceEnfant = null;
        String numAvsActuel = null;
        String typeDroit = null;
        // idDroit et non idEnfant
        String idEnfant = null;
        String nomEnfant = null;

        // liste provisoire d'un détail pour le même enfant
        ArrayList<AdiEnfantMoisComplexModel> listMemeEnfant = new ArrayList<AdiEnfantMoisComplexModel>();

        // déclaration du tableau détaillé
        Collection tableauDetaille = new Collection("tableau_liste_detaille");

        for (int i = 0; i < adiSearch.getSize(); i++) {

            AdiEnfantMoisComplexModel adiEnfantMois = (AdiEnfantMoisComplexModel) adiSearch.getSearchResults()[i];

            if (!JadeStringUtil.equals(adiEnfantMois.getDroitComplexModel().getDroitModel().getTypeDroit(),
                    ALCSDroit.TYPE_MEN, false)) {

                if ((JadeStringUtil.equals(adiEnfantMois.getDroitComplexModel().getDroitModel().getIdDroit(), idEnfant,
                        false)) || (i == 0)) {
                    listMemeEnfant.add(adiEnfantMois);

                } else {

                    // données de l'enfant à la hashMap
                    donneesDecompte = donneesDecompteAdi(nomEnfant + " " + prenomEnfant + " " + ", "
                            + dateNaissanceEnfant + ", " + numAvsActuel, typeDroit, adiSearch.getForIdDecompteAdi(),
                            idEnfant);

                    // ajouter les données au document
                    addListDroitsAdiEnfant(doc, listMemeEnfant, donneesDecompte, tableauDetaille, langueDocument);
                    // rafraîchir (vider) la liste de prestationjs d'un même
                    // enfant
                    listMemeEnfant.clear();
                    // ajouter le courant à la liste par enfant
                    listMemeEnfant.add(adiEnfantMois);
                    // vider la hasMap
                    donneesDecompte.clear();

                }
                // si c'est le dernier de la liste il faut créer le document
                if (i == adiSearch.getSize() - 1) {
                    // ajouter les données de l'enfant à la Hashmap

                    donneesDecompte = donneesDecompteAdi(adiEnfantMois.getDroitComplexModel().getEnfantComplexModel()
                            .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                            + " "
                            + adiEnfantMois.getDroitComplexModel().getEnfantComplexModel()
                                    .getPersonneEtendueComplexModel().getTiers().getDesignation2()
                            + " "
                            + ", "
                            + adiEnfantMois.getDroitComplexModel().getEnfantComplexModel()
                                    .getPersonneEtendueComplexModel().getPersonne().getDateNaissance()
                            + ", "
                            + adiEnfantMois.getDroitComplexModel().getEnfantComplexModel()
                                    .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel(),
                    /* JadeCodesSystemsUtil.getCodeLibelle( */adiEnfantMois.getDroitComplexModel().getDroitModel()
                            .getTypeDroit()/* ) */, adiSearch.getForIdDecompteAdi(), adiEnfantMois
                            .getDroitComplexModel().getDroitModel().getIdDroit()/*
                                                                                 * getEnfantComplexModel().
                                                                                 * getEnfantModel ().getIdEnfant()
                                                                                 */);

                    // ajouter les données au document
                    addListDroitsAdiEnfant(doc, listMemeEnfant, donneesDecompte, tableauDetaille, langueDocument);
                    // rafraîchir (vider) la liste de prestationjs d'un même
                    // enfant
                    listMemeEnfant.clear();
                    // vider la HashMap
                    donneesDecompte.clear();

                }

            }
            dateNaissanceEnfant = adiEnfantMois.getDroitComplexModel().getEnfantComplexModel()
                    .getPersonneEtendueComplexModel().getPersonne().getDateNaissance();
            prenomEnfant = adiEnfantMois.getDroitComplexModel().getEnfantComplexModel()
                    .getPersonneEtendueComplexModel().getTiers().getDesignation2();
            nomEnfant = adiEnfantMois.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel()
                    .getTiers().getDesignation1();
            numAvsActuel = adiEnfantMois.getDroitComplexModel().getEnfantComplexModel()
                    .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel();
            typeDroit = /* JadeCodesSystemsUtil.getCodeLibelle( */adiEnfantMois.getDroitComplexModel().getDroitModel()
                    .getTypeDroit();
            idEnfant = adiEnfantMois.getDroitComplexModel().getDroitModel().getIdDroit();

        }
        // ajout du tableau détaillé au document
        doc.add(tableauDetaille);
    }

    /**
     * Méthode qui remplit les infos globales liées à un décompteAdi
     * 
     * @param doc
     *            document à créer
     * @param decompteGlobal
     *            Décompte Adi
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected void addTableauResume(DocumentData doc, AdiDecompteComplexModel decompteGlobal, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {
        // contrôle des paramètres

        if (doc == null) {
            throw new ALAdiDecomptesException("AdiDecompteGlobalAbstractServiceImpl# addTableauResume: doc is null");
        }

        if (decompteGlobal == null) {
            throw new ALAdiDecomptesException(
                    "AdiDecompteGlobalAbstractServiceImpl# addTableauResume: decompteGlobal is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "AdiDecompteDetailleAbstractServiceImpl# addTableauResume: language  " + langueDocument
                            + " is not  valid ");
        }

        // sous titre labe
        doc.addData("adi_info_resu", this.getText("al.adi.decompte.detaille.sousTitre.label", langueDocument) + " "
                + decompteGlobal.getDecompteAdiModel().getPeriodeDebut() + "-"
                + decompteGlobal.getDecompteAdiModel().getPeriodeFin());

        doc.addData("adi_info_chf", this.getText("al.adi.decompte.detail.info.monnaieChf.label", langueDocument));
        // total des adi

        AdiEnfantMoisComplexSearchModel searchComplexModel = new AdiEnfantMoisComplexSearchModel();
        searchComplexModel.setForIdDecompteAdi(decompteGlobal.getDecompteAdiModel().getIdDecompteAdi());
        searchComplexModel = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(searchComplexModel);

        doc.addData("adi_info_mont", JANumberFormatter.fmt(ALServiceLocator.getCalculAdiBusinessService()
                .getTotalDecompte(searchComplexModel), true, true, false, 2));

        // doc.addData("adi_info_mont", ALServiceLocator
        // .getCalculAdiBusinessService().getTotalDecompte(
        // searchComplexModel).toString());

    }

    /**
     * Méthode qui permet d'ajouter le titre du document
     * 
     * @param document
     *            Document auxquel il faut ajouter le titre
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    @Override
    public void addTitreDocument(DocumentData document, AdiDecompteComplexModel decompteAdi, String langueDocument)
            throws JadeApplicationException {

        // contrôle des paramètres
        if (document == null) {
            throw new ALAdiDecomptesException(
                    "AdiDecompteGlobalAbstractServiceImpl# addTitreDocument: document is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALAdiDecomptesException("AdiDecomptDetailleAbstractServiceImpl# addTitreDocument: language  "
                    + langueDocument + " is not  valid ");
        }

        document.addData("info_adi_decompte", this.getText("al.adi.decompte.detail.titre", langueDocument) + "\n"
                + decompteAdi.getDecompteAdiModel().getPeriodeDebut() + "-"
                + decompteAdi.getDecompteAdiModel().getPeriodeFin());

    }

    /**
     * Méthode qui retourne un hashmap contenant des valeur d'un décompte Adi
     * 
     * @param donneesEnfant
     *            nom, prénom et date de naissance
     * @param typeDroit
     *            type de droit, enfnat ou formation
     * @param idDecompte
     *            identifiant du décompte Adi
     * @param idEnfant
     *            identifiant de l'enfant
     * @return donneesDecompteAdi HahMap contenant les données
     */
    private HashMap<String, String> donneesDecompteAdi(String donneesEnfant, String typeDroit, String idDecompte,
            String idEnfant) {
        HashMap<String, String> donneesDecompteAdi = new HashMap<String, String>();
        donneesDecompteAdi.put("donneeEnfant", donneesEnfant);
        donneesDecompteAdi.put("typeDroit", typeDroit);
        donneesDecompteAdi.put("idDecompte", idDecompte);
        // idDroit
        donneesDecompteAdi.put("idEnfant", idEnfant);

        return donneesDecompteAdi;
    }

    @Override
    public DocumentData getDocument(
    /* DossierComplexModel dossier */AdiDecompteComplexModel decompteGlobal,
    /* String periode, */String typeDecompte, DocumentData doc, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {
        // DocumentData doc = new DocumentData();
        // ajoute le titre du document
        addTitreDocument(doc, decompteGlobal, langueDocument);
        // ajoute les infos communes à tous les documents adi
        doc = super.getDocument(decompteGlobal,/* periode, */typeDecompte, doc, langueDocument);
        addTableauResume(doc, decompteGlobal, langueDocument);

        // recherche des droits
        AdiEnfantMoisComplexSearchModel adiSearch = new AdiEnfantMoisComplexSearchModel();

        adiSearch.setForIdDecompteAdi(decompteGlobal.getDecompteAdiModel().getIdDecompteAdi());

        adiSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        adiSearch.setOrderKey("impressionAdi");
        adiSearch = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(adiSearch);

        addTableauDetaille(doc, adiSearch, langueDocument);

        return doc;
    }

    // /**
    // * méthode qui retourne le libellé de droit ménage, enfant ou formation en focntion de la langue
    // *
    // * @param typeDroit
    // * @param langueDocument
    // * @return
    // * @throws JadeApplicationException
    // */
    // private String getTextTypeDroit(String typeDroit, String langueDocument) throws JadeApplicationException {
    // // contrôle des paramètres
    // if (!JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_FORM, false)
    // && !JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_ENF, false)
    // && !JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_MEN, false)) {
    // throw new ALAdiDecomptesException("AdiDecompteDetailleAbstractServiceImpl#getTextTypeDroit: " + typeDroit
    // + " is not valid");
    // }
    // if (JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_ENF, false)) {
    // return this.getText("al.adi.decompte.global.droit.droitEnfant.valeur", langueDocument);
    // } else if (JadeStringUtil.equals(typeDroit, ALCSDroit.TYPE_FORM, false)) {
    // return this.getText("al.adi.decomtpe.global.droit.droitFormation.valeur", langueDocument);
    // } else {
    // return this.getText("al.adi.decompte.global.droit.droitMenage.valeur", langueDocument);
    // }
    // }

}
