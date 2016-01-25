package ch.globaz.al.businessimpl.services.adiDecomptes;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstCaisse;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexSearchModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe abstraite des d�comptes globaux ADI
 * 
 * @author PTA
 * 
 */
public abstract class AdiDecompteGlobalAbstractServiceImpl extends AdiDecompteAbstractServiceImpl {

    /**
     * M�thode qui ajoute le tableau du d�compte global
     * 
     * @param document
     *            Document auxquel il faut ajouter le tableau global
     * @param adiDroitSearch
     *            recherche des d�comptesDesDroits
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    protected void addTableauGlobal(DocumentData document, AdiEnfantMoisComplexSearchModel adiDroitSearch,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {

        // contr�le des param�tres
        if (document == null) {
            throw new ALAdiDecomptesException("AdiDecompteAbstractServiceImpl# addTableauGlobal: document is null");
        }
        if (adiDroitSearch == null) {
            throw new ALAdiDecomptesException(
                    "AdiDecompteAbstractServiceImpl# addTableauGlobal: adiDroitSearch is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALAdiDecomptesException("AdiDecompteAbstractServiceImpl# setInfos: language  " + langueDocument
                    + " is not  valid ");
        }

        // initialisation de la collection
        Collection tableauGlobal = new Collection("tableau_adi_global");

        // titre des colonnes
        DataList list = new DataList("entete");

        // entete Nom, Prenom, enfant
        list.addData("entete_adi_glob_nom",
                this.getText("al.adi.decompte.global.droit.nomPrenom.label", langueDocument));
        // entete droit
        list.addData("entete_adi_glob_droit", this.getText("al.adi.decompte.global.droit.droit.label", langueDocument));
        // entete adiReparti m�nage-enfants
        list.addData("entete_adi_glob_reparti",
                this.getText("al.adi.decompte.global.droit.montantReparti.label", langueDocument));
        // entete adiReparti enfants
        String nomCaisse = ALServiceLocator.getParametersServices().getNomCaisse();

        if (ALConstCaisse.CAISSE_FVE.equals(nomCaisse.toUpperCase())) {
            list.addData("entete_adi_glob_non_reparti", this.getText(
                    "al.adi.decompte.global.droit.montantRepartiDroit.label.".concat(nomCaisse.toLowerCase()),
                    langueDocument));
        } else {
            list.addData("entete_adi_glob_non_reparti",
                    this.getText("al.adi.decompte.global.droit.montantRepartiDroit.label", langueDocument));
        }
        // ajout de la liste au tableau global
        tableauGlobal.add(list);

        // stocke le total du versement suppl�mentaire
        String totalVerseSupp = ALServiceLocator.getCalculAdiBusinessService().getTotalVerseSupp(
                adiDroitSearch.getForIdDecompteAdi());

        // ajout libell� m�nage si sup�rieur � 0
        if (!JadeStringUtil.isBlankOrZero(totalVerseSupp)) {
            // ajout du suppl�ment m�nage
            DataList listMenage = new DataList("colonneMenage");

            listMenage.addData("col_men",
                    this.getText("al.adi.decompte.global.droit.droitMenage.valeur", langueDocument));
            // ajout du montant m�nage
            listMenage.addData("col_men_total", JANumberFormatter.fmt(ALServiceLocator.getCalculAdiBusinessService()
                    .getTotalVerseSupp(adiDroitSearch.getForIdDecompteAdi()).toString(), true, true, false, 2));

            tableauGlobal.add(listMenage);
        }

        // Ajout des totaux par droits (enfant ou formation)
        // list des idEnfant
        ArrayList<String> listDroits = listIdDroits(adiDroitSearch);

        // ajout de la ligne droit m�nage et droits enfants ou formations

        for (String value : listDroits) {

            DroitComplexModel droitComplex = ALServiceLocator.getDroitComplexModelService().read(value);

            DataList colonne = new DataList("colonne");

            // nom/Prenom/nss
            colonne.addData("col_nom", droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers()
                    .getDesignation1()
                    + " "
                    + droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers()
                            .getDesignation2()
                    + ", "
                    + droitComplex.getEnfantComplexModel().getPersonneEtendueComplexModel().getPersonneEtendue()
                            .getNumAvsActuel());
            // type de droit
            colonne.addData("col_droit", getTextTypeDroit(droitComplex.getDroitModel().getTypeDroit(), langueDocument));
            // ajoute le total ADI avec m�nage

            // total pour un droit avec suppl�ment

            // ajout libell� m�nage si sup�rieur � 0
            if (!JadeStringUtil.isBlankOrZero(totalVerseSupp)) {

                colonne.addData("col_mont_reparti", JANumberFormatter.fmt(
                        ALServiceLocator.getCalculAdiBusinessService().getTotalVerseParDroitAvecSupp(
                                adiDroitSearch.getForIdDecompteAdi(), droitComplex.getDroitModel().getIdDroit()), true,
                        true, false, 2));
            }
            // sinon ajoute totalVerseSupp
            else {
                colonne.addData("col_mont_reparti", JANumberFormatter.fmt(totalVerseSupp, true, true, false, 2));

            }

            // total pour un droit sans suppl�ment
            colonne.addData("col_mont_non_reparti", JANumberFormatter.fmt(
                    ALServiceLocator.getCalculAdiBusinessService().getTotalVerseParDroitSansSupp(
                            adiDroitSearch.getForIdDecompteAdi(), droitComplex.getDroitModel().getIdDroit()), true,
                    true, false, 2));

            // ajout de la ligne colonne au tableau
            tableauGlobal.add(colonne);

        }
        // Total de l'adi
        DataList colTotal = new DataList("colonneTotal");

        if (!JadeStringUtil.isBlankOrZero(totalVerseSupp)) {
            colTotal.addData("col_mont_reparti_total", JANumberFormatter.fmt(ALServiceLocator
                    .getCalculAdiBusinessService().getTotalDecompte(adiDroitSearch), true, true, false, 2));
        }

        // Total de l'adi
        colTotal.addData("col_mont_non_reparti_total", JANumberFormatter.fmt(ALServiceLocator
                .getCalculAdiBusinessService().getTotalDecompte(adiDroitSearch), true, true, false, 2));

        // ajout de l colonne total au tableau
        tableauGlobal.add(colTotal);

        // ajout de la liste des prestations d�j� vers�es
        DataList coloPrestaVersees = new DataList("colonnePresta");
        // libell� des prestations d�j� vers�es
        coloPrestaVersees.addData("col_presta_vers_label",
                this.getText("al.adi.decompte.global.montantVerse.label", langueDocument));

        // libell� CHF
        coloPrestaVersees.addData("col_chf",
                this.getText("al.adi.decompte.detail.info.monnaieChf.label", langueDocument));

        // montant d�j� vers�e

        coloPrestaVersees.addData(
                "col_mont_presta_vers",
                JANumberFormatter.fmt(
                        ALServiceLocator.getCalculAdiBusinessService().getSommeDejaVersee(
                                adiDroitSearch.getForIdDecompteAdi()), true, true, false, 2));

        // ajout au tableau
        tableauGlobal.add(coloPrestaVersees);

        // solde � payer
        DataList soldeAPayer = new DataList("colonneAPayer");
        // libell� solde a payer
        soldeAPayer.addData("col_a_payer_label",
                this.getText("al.adi.decompte.global.montantAVerser.label", langueDocument));

        // libell� CHF
        soldeAPayer.addData("col_chf", this.getText("al.adi.decompte.detail.info.monnaieChf.label", langueDocument));
        // solde a payer
        soldeAPayer.addData(
                "col_mont_a_verser",
                JANumberFormatter.fmt(
                        ALServiceLocator.getCalculAdiBusinessService().getSoldeAPayer(
                                adiDroitSearch.getForIdDecompteAdi()), true, true, false, 2));

        // ajout au tableau
        tableauGlobal.add(soldeAPayer);

        // ajout de la collection au document
        document.add(tableauGlobal);

    }

    /**
     * prestations horlog�res prises en compte dans les dossiers Adi
     * 
     * @throws JadePersistenceException
     */

    private Collection addTableSuppHorloger(Collection tableauGlobalHorloger, String idEntetetPrestation,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {
        // contr�le des param�tres
        if (tableauGlobalHorloger == null) {
            throw new ALAdiDecomptesException(
                    "AdiDecompteGlobalAbstractServiceImpl# addTableSuppHorloger: tableauGlobalHorloger is null");
        }
        if (JadeStringUtil.isEmpty(idEntetetPrestation)) {
            throw new ALAdiDecomptesException(
                    "AdiDecompteGlobalAbstractServiceImpl# addTableSuppHorloger: idEnTetePrestation is empty or null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALAdiDecomptesException("AdiDecompteGlobalAbstractServiceImpl# addTableSuppHorloger: language  "
                    + langueDocument + " is not  valid ");
        }
        // recherche des prestation horlog�re pour la p�riode du d�compte ADI rechercher l'ent�te de prestations
        // correspondant d�compte ADi
        DetailPrestationComplexSearchModel searchPrestatHorlo = new DetailPrestationComplexSearchModel();
        searchPrestatHorlo.setForIdEntete(idEntetetPrestation);
        searchPrestatHorlo.setForTarif(ALCSTarif.CATEGORIE_SUP_HORLO);
        searchPrestatHorlo.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchPrestatHorlo.setWhereKey("prestationSuppHorlo");
        searchPrestatHorlo.setOrderKey("nomPrestaDroit");
        searchPrestatHorlo = ALServiceLocator.getDetailPrestationComplexModelService().search(searchPrestatHorlo);
        if (searchPrestatHorlo.getSize() > 0) {
            // ligne de titre
            DataList titre = new DataList("bling");
            titre.addData("info_adi_compl_horlo",
                    this.getText("al.adi.decompte.global.prestationHologere.titre", langueDocument));
            // ajout du titre au tableau
            tableauGlobalHorloger.add(titre);
            // remplir le tableau des suppl�ments horlogers
            DataList list = new DataList("entete");
            // ajout libell� pour les ent�tes
            list.addData("entete_adi_glob_nom",
                    this.getText("al.adi.decompte.global.droit.nomPrenom.label", langueDocument));
            list.addData("entete_adi_glob_droit",
                    this.getText("al.adi.decompte.global.droit.droit.label", langueDocument));
            list.addData("entete_adi_glob_presta_horl",
                    this.getText("al.adi.decompte.global.prestationHorlogere.label", langueDocument));
            // ajout de la liste des libell�s au tableau global
            tableauGlobalHorloger.add(list);
            // ajouts des droit distinct
            String numNss = null;

            String nomPrenNss = null;

            // montant total des prestations suppl�ments horlogers pour un droit
            Double sommeTotalEnfant = new Double(0);
            // montant total (droits cumul�s) prestatiosn suppl�ments horlogers

            Double sommeTotalSuppHologer = new Double(0);
            for (int i = 0; i < searchPrestatHorlo.getSize(); i++) {

                DetailPrestationComplexModel detailPresta = (DetailPrestationComplexModel) searchPrestatHorlo
                        .getSearchResults()[i];
                // ajout sommeTotalEnfant
                sommeTotalSuppHologer = sommeTotalSuppHologer
                        + new Double(detailPresta.getDetailPrestationModel().getMontant());

                if ((i == 0)
                        || JadeStringUtil
                                .equals(detailPresta.getDroitComplexModel().getEnfantComplexModel()
                                        .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel(),
                                        numNss, false)) {
                    // montant prestation
                    sommeTotalEnfant = sommeTotalEnfant
                            + new Double(detailPresta.getDetailPrestationModel().getMontant());
                    // nom, pr�nom et nss
                    nomPrenNss = detailPresta.getDroitComplexModel().getEnfantComplexModel()
                            .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                            + " "
                            + detailPresta.getDroitComplexModel().getEnfantComplexModel()
                                    .getPersonneEtendueComplexModel().getTiers().getDesignation2()
                            + ", "
                            + detailPresta.getDroitComplexModel().getEnfantComplexModel()
                                    .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel();
                } else {

                    // sommeTotalEnfant = sommeTotalEnfant
                    // + new Double(detailPresta.getDetailPrestationModel().getMontant());
                    DataList listDonnees = new DataList("colonne");
                    // ajout du nom, pr�nom et nss
                    listDonnees.addData("col_nom", nomPrenNss);
                    // ajout compl�ment
                    listDonnees.addData("col_sup",
                            this.getText("al.adi.decompte.global.prestationHorlogere.colonne", langueDocument));
                    // ajout du montant
                    DecimalFormat df = new DecimalFormat("0.00");

                    listDonnees.addData("col_sup_mont",
                            JANumberFormatter.fmt(
                                    String.valueOf(df.format(Math.round(sommeTotalEnfant / 0.01) * 0.01)), true, true,
                                    false, 2));

                    // alimente le tableau
                    tableauGlobalHorloger.add(listDonnees);
                    sommeTotalEnfant = new Double(detailPresta.getDetailPrestationModel().getMontant());

                }

                if (i == (searchPrestatHorlo.getSize() - 1)) {
                    DataList listDonnees = new DataList("colonne");
                    // ajout du nom, pr�nom et nss
                    listDonnees.addData("col_nom", detailPresta.getDroitComplexModel().getEnfantComplexModel()
                            .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                            + " "
                            + detailPresta.getDroitComplexModel().getEnfantComplexModel()
                                    .getPersonneEtendueComplexModel().getTiers().getDesignation2()
                            + ", "
                            + detailPresta.getDroitComplexModel().getEnfantComplexModel()
                                    .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel());
                    // ajout compl�ment
                    listDonnees.addData("col_sup",
                            this.getText("al.adi.decompte.global.prestationHorlogere.colonne", langueDocument));
                    // ajout du montant
                    DecimalFormat df = new DecimalFormat("0.00");

                    listDonnees.addData("col_sup_mont",
                            JANumberFormatter.fmt(
                                    String.valueOf(df.format(Math.round(sommeTotalEnfant / 0.01) * 0.01)), true, true,
                                    false, 2));

                    // alimente le tableau
                    tableauGlobalHorloger.add(listDonnees);

                }
                numNss = detailPresta.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel()
                        .getPersonneEtendue().getNumAvsActuel();
            }
            // ajout du total des prestations suppl�ments horlog�res au tableau
            DataList totalSupp = new DataList("colonneTotal");
            // libell�
            totalSupp.addData("col_totalApayer",
                    this.getText("al.adi.decompte.global.montantAVerser.label", langueDocument));
            // ajout du total
            // ajout du montant
            DecimalFormat df = new DecimalFormat("0.00");
            totalSupp.addData("col_sup_total", JANumberFormatter.fmt(
                    String.valueOf(df.format(Math.round(sommeTotalSuppHologer / 0.01) * 0.01)), true, true, false, 2));
            // ajout du total au tableau
            tableauGlobalHorloger.add(totalSupp);
        }
        return tableauGlobalHorloger;
    }

    @Override
    public void addTitreDocument(DocumentData document, AdiDecompteComplexModel decompteAdi, String langueDocument)
            throws JadeApplicationException {

        // contr�le des param�tres
        if (document == null) {
            throw new ALAdiDecomptesException(
                    "AdiDecompteGlobalAbstractServiceImpl# addTitreDocument: document is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALAdiDecomptesException("AdiDecompteGlobalAbstractServiceImpl# addTitreDocument: language  "
                    + langueDocument + " is not  valid ");
        }

        document.addData("info_adi_decompte", this.getText("al.adi.decompte.global.titre", langueDocument) + "\n"
                + decompteAdi.getDecompteAdiModel().getPeriodeDebut() + "-"
                + decompteAdi.getDecompteAdiModel().getPeriodeFin());

    }

    @Override
    public DocumentData getDocument(AdiDecompteComplexModel decompteGlobal, String typeDecompte, DocumentData doc,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {

        // ajout des infos communes � tous les types de document
        doc = super.getDocument(decompteGlobal, /* periode, */typeDecompte, doc, langueDocument);

        // ajout du titre de document
        addTitreDocument(doc, decompteGlobal, langueDocument);
        // affectation du tableau du d�compte global
        AdiEnfantMoisComplexSearchModel adiEnfantMois = new AdiEnfantMoisComplexSearchModel();
        adiEnfantMois.setForIdDecompteAdi(decompteGlobal.getDecompteAdiModel().getIdDecompteAdi());

        adiEnfantMois.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        adiEnfantMois.setOrderKey("impressionAdi");
        adiEnfantMois = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(adiEnfantMois);

        // ajout du tableau global au document
        addTableauGlobal(doc, adiEnfantMois, langueDocument);

        // Si caisse horlog�re
        if (ALImplServiceLocator.getHorlogerBusinessService().isCaisseHorlogere() == true) {
            // d�claration du tableau suppl�ments horloger
            Collection tableauGlobalHorloger = new Collection("tableau_adi_supp_horlo");
            tableauGlobalHorloger = addTableSuppHorloger(tableauGlobalHorloger, decompteGlobal.getDecompteAdiModel()
                    .getIdPrestationAdi(), langueDocument);
            // ajout de la collection au document
            doc.add(tableauGlobalHorloger);

        }

        // Ajout du texte libre dans le documentdata
        if (!JadeStringUtil.isBlank(decompteGlobal.getDecompteAdiModel().getTexteLibre())) {
            doc.addData("texte_paragraphe_libre",
                    JadeStringUtil.removeChar(decompteGlobal.getDecompteAdiModel().getTexteLibre(), '\n'));
        }

        return doc;
    }

    /**
     * M�thode qui permet de r�cup�rer les id distincts des enfants pour u
     * 
     * @param droitsAdi
     *            AdiEnfantMoisComplexSearchModel
     * @return ArrayList
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private ArrayList<String> listIdDroits(AdiEnfantMoisComplexSearchModel droitsAdi) throws JadeApplicationException {

        // contr�le des param�tres
        if (droitsAdi == null) {
            throw new ALAdiDecomptesException("AdiDecompteGlobalAbstractServiceImpl# listIdsEnfant: droitsAdi is null");
        }

        // liste des identifiant des enfant
        ArrayList<String> listIdDroits = new ArrayList<String>();

        for (int i = 0; i < droitsAdi.getSize(); i++) {
            AdiEnfantMoisComplexModel adiEnfantDroit = (AdiEnfantMoisComplexModel) droitsAdi.getSearchResults()[i];

            if (!listIdDroits.contains(adiEnfantDroit.getAdiEnfantMoisModel().getIdDroit())) {
                listIdDroits.add(adiEnfantDroit.getAdiEnfantMoisModel().getIdDroit());
            }

        }
        return listIdDroits;
    }

}
