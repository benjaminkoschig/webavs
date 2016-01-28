package ch.globaz.al.businessimpl.services.declarationVersement;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.declarationVersement.ALDeclarationVersementException;
import ch.globaz.al.business.models.prestation.DeclarationVersementDetailleComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.declarationVersement.DeclarationVersementService;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Impl�mentation du service commun � toutes les sortes de d�clarations de versements d�taill�es
 * 
 * @author PTA
 * 
 */
public abstract class DeclarationVersementDetailleAbstractServiceImpl extends DeclarationVersementAbstractServiceImpl
        implements DeclarationVersementService {
    /**
     * M�thode qui alimente la liste d�taill�e pour un document d'attestation de versement
     * 
     * @param listTempPresta
     *            list temporaire des prestations opour un enfant
     * @param tableauVersement
     *            collection li�e au document
     * @param prenomEnfant
     *            prenom de l'enfant
     * @param dateNaissanceEnfant
     *            date de naissance de l'enfant
     * @param typePrestation
     *            type de prestation
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    private void createTableauDetaill(ArrayList<DeclarationVersementDetailleComplexModel> listTempPresta,
            Collection tableauVersement, String prenomEnfant, String dateNaissanceEnfant, String typePrestation,
            String langueDocument) throws JadeApplicationException {

        // contr�le des param�tres
        if (listTempPresta == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# createTableauDetaill:  listTempPresta is null ");
        }
        if (tableauVersement == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# createTableauDetaill:  tableauVersement is null ");
        }
        if (JadeStringUtil.isEmpty(typePrestation)) {
            throw new ALDeclarationVersementException(
                    "declarationVersementDetailleAbstractServiceImpl# createTableauDetaill: " + typePrestation
                            + " is null or empty");

        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "declarationVersementDetailleAbstractServiceImpl# createTableauDetail: language  " + langueDocument
                            + " is not  valid ");
        }
        if (!JadeStringUtil.equals(typePrestation, ALCSDroit.TYPE_MEN, false)
                && !JadeStringUtil.equals(typePrestation, ALCSDroit.TYPE_MEN_LFA, false)
                && !JadeStringUtil.equals(typePrestation, ALCSDroit.TYPE_MEN_LJA, false)) {

            if (JadeStringUtil.isEmpty(prenomEnfant)) {
                throw new ALDeclarationVersementException(
                        "DeclarationVersementDetailleAbstractServiceImpl# createTableauDetaill: " + prenomEnfant
                                + " is null or empty");
            }
            if (!JadeDateUtil.isGlobazDate(dateNaissanceEnfant)) {
                throw new ALDeclarationVersementException(
                        "DeclarationVersementDetailleAbstractServiceImpl# createTableauDetaill: " + dateNaissanceEnfant
                                + "is not a globaz'date valid");
            }
        }

        BigDecimal totalEnfant = new BigDecimal("0");

        for (DeclarationVersementDetailleComplexModel declarVersment : listTempPresta) {
            DataList lignePrestation = new DataList("colonnePrestation");

            // traitement sp�cifique aux prestations non m�nages
            if (!JadeStringUtil.equals(typePrestation, ALCSDroit.TYPE_MEN, false)
                    && !JadeStringUtil.equals(typePrestation, ALCSDroit.TYPE_MEN_LFA, false)
                    && !JadeStringUtil.equals(ALCSDroit.TYPE_MEN_LJA, typePrestation, false)) {
                if (listTempPresta.indexOf(declarVersment) == 0) {
                    // Traitement pour les donn�es de l'enfant
                    lignePrestation.addData("col_prenom", prenomEnfant);
                    // traitement pour la date de naissance
                    lignePrestation.addData("col_date_nais", dateNaissanceEnfant);
                    // pour les donn�es allocacation de m�nage

                } else {
                    lignePrestation.addData("col_prenom", "");
                    // traitement pour la date de naissance
                    lignePrestation.addData("col_date_nais", "");
                }
            }
            // traitement sp�cifique aux prestations m�nages
            else if (JadeStringUtil.equals(typePrestation, ALCSDroit.TYPE_MEN, false)
                    || JadeStringUtil.equals(typePrestation, ALCSDroit.TYPE_MEN_LFA, false)
                    || JadeStringUtil.equals(ALCSDroit.TYPE_MEN_LJA, typePrestation, false)) {
                if (listTempPresta.indexOf(declarVersment) == 0) {
                    // Traitement pour les donn�es de l'enfant
                    lignePrestation.addData("col_prenom",
                            this.getText("al.declarationVersement.allocationMenage.valeur", langueDocument));
                    // traitement pour la date de naissance
                    lignePrestation.addData("col_date_nais", "");
                    // pour les donn�es allocacation de m�nage

                } else {
                    lignePrestation.addData("col_prenom", "");
                    // traitement pour la date de naissance
                    lignePrestation.addData("col_date_nais", "");
                }

            }

            // traitement pour la date de paiement
            lignePrestation.addData("col_date_pmt", declarVersment.getDateVersement());
            // traitement pour la p�riode
            lignePrestation.addData("col_periode", declarVersment.getPeriode());
            // traitement pour le montant
            lignePrestation.addData("col_montant", declarVersment.getMontantDetailPrestation());
            // ajout de la ligne au tableau
            tableauVersement.add(lignePrestation);

            // total par enfant
            totalEnfant = totalEnfant.add(new BigDecimal(declarVersment.getMontantDetailPrestation()));

        }

        DataList ligneTotalEnfant = new DataList("colonneTotalEnfant");
        // label du montant total de l'enfant
        ligneTotalEnfant.addData("col_totalEnfant_label",
                this.getText("al.declarationVersement.montantTotal.libelle", langueDocument) + " " + prenomEnfant);

        // ajout du label devise (CHF)
        ligneTotalEnfant.addData("col_monnaie_label",
                this.getText("al.declarationVersement.montant.devise", langueDocument));

        ligneTotalEnfant
                .addData("col_totalEnfant", JANumberFormatter.fmt(totalEnfant.toString(), true, true, false, 2));

        // ajout de la ligne au tableau
        tableauVersement.add(ligneTotalEnfant);

    }

    /**
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param dateDebut
     *            date du d�but
     * @param dateFin
     *            date du la fin
     * @param dateImpression
     *            date � figurer sur le document
     * @return DocumentData
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected DocumentData getDeclarationVersement(String idDossier, String dateDebut, String dateFin,
            String dateImpression, ArrayList<DeclarationVersementDetailleComplexModel> listDeclaPresta,
            String langueDocument, Boolean textImpot) throws JadePersistenceException, JadeApplicationException {

        // v�rification des param�tres

        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDeclarationVersement: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDeclarationVersement:  " + dateDebut
                            + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDeclarationVersement:  " + dateFin
                            + " is not a valid globaz's date ");
        }
        if (!JadeDateUtil.isGlobazDate(dateImpression)) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# getDeclarationVersement:  " + dateImpression
                            + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "declarationVersementDetailleAbstractServiceImpl# getDeclarationVersement: language  "
                            + langueDocument + " is not  valid ");
        }

        HashSet<String> typeBoni = new HashSet<String>();

        DocumentData document = initDocument();

        String activiteAlloc = ALServiceLocator.getDossierModelService().read(idDossier).getDebutActivite();

        this.setIdEntete(document, activiteAlloc, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, langueDocument);
        setTable(document, idDossier, dateDebut, dateFin, typeBoni, listDeclaPresta, langueDocument);
        this.setIdSignature(document, activiteAlloc, ALConstDocument.DOCUMENT_DECLARATION_VERSEMENT, langueDocument);

        return document;
    }

    /**
     * M�thode de recherche pour les d�clarations de versement d�taill�es
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param dateDebut
     *            date de d�but
     * @param dateFin
     *            date de fin
     * @param typeBoni
     *            type de bonification
     * @return DeclarationVersementDetailleSearchComplexModel
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    /**
     * Initialise le document global
     * 
     * @return DocumentData
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected DocumentData initDocument() throws JadeApplicationException, JadePersistenceException {
        DocumentData document = new DocumentData();
        setIdDocument(document);
        // this.setIdEntete(document);
        // this.setIdSignature(document);
        return document;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.businessimpl.documents.AbstractDocument#setIdDocument(ch .globaz.topaz.datajuicer.DocumentData)
     */
    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {

        if (document == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl#setIdDocument : document is null");
        }
        document.addData("AL_idDocument", "AL_declarationVersementDetaille");
    }

    /**
     * M�thode qui charge le tableau dans le document
     * 
     * @param document
     *            auquel il faut ajouter le tableau
     * @param idDossier
     *            identifiant du dossier
     * @param dateDebut
     *            date du d�but
     * @param dateFin
     *            date de fin
     * @param typeBoni
     *            type de bonification(direct ou indirect)
     * @throws JadeApplicationException
     *             Exception lev�e si l'un des param�tres n'est pas valide
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected void setTable(DocumentData document, String idDossier, String dateDebut, String dateFin,
            HashSet<String> typeBoni, ArrayList<DeclarationVersementDetailleComplexModel> listDeclaPrestat,
            String langueDocument) throws JadeApplicationException, JadePersistenceException {
        // contr�le des param�tres

        if (document == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl#setTable : documnet is null");
        }
        if (idDossier == null) {
            throw new ALDeclarationVersementException(
                    "DeclarationVersementDetailleAbstractServiceImpl# setTable: idDossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(dateDebut)) {
            throw new ALDeclarationVersementException("DeclarationVersementDetailleAbstractServiceImpl# setTable:  "
                    + dateDebut + " is not a valid globaz's date ");
        }

        if (!JadeDateUtil.isGlobazDate(dateFin)) {
            throw new ALDeclarationVersementException("DeclarationVersementDetailleAbstractServiceImpl# setTable:  "
                    + dateFin + " is not a valid globaz's date ");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALDeclarationVersementException(
                    "declarationVersementDetailleAbstractServiceImpl# setTable: language  " + langueDocument
                            + " is not  valid ");
        }

        BigDecimal totalPrestations = new BigDecimal("0");

        Collection tableauVersement = new Collection("ListDetail_declaration_versement");

        // d�finition des en-t�tes ligne une
        document.addData("entete_declarationVersement_prestations",
                this.getText("al.declarationVersement.prestation.libelle", langueDocument));
        document.addData("entete_declarationVersement_montants",
                this.getText("al.declarationVersement.montant.libelle", langueDocument));

        // d�finition des ent�tes ligne deux
        document.addData("entete_declarationVersement_prenom",
                this.getText("al.declarationVersement.prenom.libelle", langueDocument));
        document.addData("entete_declarationVersement_naissance",
                this.getText("al.declarationVersement.dateNaissance.libelle", langueDocument));
        document.addData("entete_declarationVersement_datePmt",
                this.getText("al.declarationVersement.dateVersement.libelle", langueDocument));
        document.addData("entete_declarationVersement_periode",
                this.getText("al.declarationVersement.periodePrestation.libelle", langueDocument));
        document.addData("entete_declarationVersement_montant",
                this.getText("al.declarationVersement.montantPrestation.libelle", langueDocument));
        document.addData("entete_declarationVersement_total=",
                this.getText("al.declarationVersement.montantTotal.libelle", langueDocument));

        String nssEnfantPrecedent = "";

        String prenomEnfantPrecedent = "";

        String dateNaissanceEnfant = "";

        String typePrestation = "";
        ArrayList<DeclarationVersementDetailleComplexModel> listeTempPresta = new ArrayList<DeclarationVersementDetailleComplexModel>();

        // parcourir les prestations pour les ajouter au documentData
        for (int i = 0; i < listDeclaPrestat.size(); i++) {

            // ArrayList temporaire

            DeclarationVersementDetailleComplexModel declarDetail = listDeclaPrestat.get(i);
            // .getSearchResults()[i];

            // (si 1er mais pas le dernier) ou que le nss enfant �gal au
            // pr�c�dent
            // au
            // pr�c�dent ainsi que le direct est identique au pr�c�dent
            if ((i == 0) || (JadeStringUtil.equals(nssEnfantPrecedent, declarDetail.nssEnfant, false))) {
                listeTempPresta.add(declarDetail);
            } else {
                createTableauDetaill(listeTempPresta, tableauVersement, prenomEnfantPrecedent, dateNaissanceEnfant,
                        typePrestation, langueDocument);
                // vide le tableau provisoire
                listeTempPresta.clear();

                // ajoute du declarDetail au tableau temporaire
                listeTempPresta.add(declarDetail);
            }

            // si c'�tait le dernier droit on g�n�re avant de sortir de la
            // lieu de if
            if ((i == listDeclaPrestat.size() - 1)) {
                createTableauDetaill(listeTempPresta, tableauVersement, declarDetail.getPrenomEnfant(),
                        declarDetail.dateNaissanceEnfant, declarDetail.typePrestation, langueDocument);

            }

            nssEnfantPrecedent = declarDetail.getNssEnfant();

            dateNaissanceEnfant = declarDetail.getDateNaissanceEnfant();

            prenomEnfantPrecedent = declarDetail.getPrenomEnfant();

            typePrestation = declarDetail.getTypePrestation();
            // total pour l'ensemble
            totalPrestations = totalPrestations.add(new BigDecimal(declarDetail.getMontantDetailPrestation()));

        }

        // ajouter le montant de la prestation au montant total de l'enfant

        // ajout des totaux
        DataList ligneTotal = new DataList("colonneMontantTotal");
        ligneTotal.addData("col_montantTotal_Label",
                this.getText("al.declarationVersement.montantTotal", langueDocument));
        ligneTotal.addData("col_monnaie_label", this.getText("al.declarationVersement.montant.devise", langueDocument));

        // total des totaux des enfants
        ligneTotal.addData("col_montant_total",
                JANumberFormatter.fmt(totalPrestations.toString(), true, true, false, 2));

        tableauVersement.add(ligneTotal);

        // ajout du tableau au document
        document.add(tableauVersement);
    }
}
