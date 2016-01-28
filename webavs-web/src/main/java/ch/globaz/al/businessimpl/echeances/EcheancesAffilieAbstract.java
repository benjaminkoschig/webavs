package ch.globaz.al.businessimpl.echeances;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstLangue;
import ch.globaz.al.business.exceptions.model.tarif.ALEcheanceModelException;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.documents.AbstractDocument;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * classe abstraite impl�mentant des m�thodes communes aux listes d'�ch�ances communes aux affilies (direct et indirect
 * et adresse de paiement)
 * 
 * @author PTA
 * 
 */
public abstract class EcheancesAffilieAbstract extends AbstractDocument implements EcheancesAffilie {
    @Override
    public DocumentData loadData(ArrayList<DroitEcheanceComplexModel> droits, String numAffilie, String typeActivite)
            throws JadePersistenceException, JadeApplicationException {

        if (droits == null) {
            throw new ALEcheanceModelException("EcheancesAffilieAbstract#loadData: droits is null");
        }
        if (numAffilie == null) {
            throw new ALEcheanceModelException("EcheancesAffilieAbstract#loadData: numAffilie is null");
        }
        if (typeActivite == null) {
            throw new ALEcheanceModelException("EcheancesAffilieAbstract#loadData: typeActivite is null");
        }

        DocumentData document = new DocumentData();

        String langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(numAffilie);

        setIdDocument(document);
        this.setIdEntete(document, typeActivite, ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE, langueDocument);

        String idTiersAffilie = AFBusinessServiceLocator.getAffiliationService()
                .findIdTiersForNumeroAffilie(numAffilie);

        this.addDateAdresse(document, JadeDateUtil.getGlobazFormattedDate(new Date()), idTiersAffilie, langueDocument,
                numAffilie);
        setInfos(document, numAffilie, langueDocument);
        setTexteDebut(document, langueDocument);
        setTable(document, droits, langueDocument);
        setTexteFin(document, langueDocument);
        setTexteSalutations(document, langueDocument);
        this.setIdSignature(document, typeActivite, ALConstDocument.DOCUMENT_ECHEANCE_AFFILIE, langueDocument);

        return document;
    }

    /**
     * 
     * @param document
     *            document � imprimer
     * @param numAffilie
     *            num�ro de l'affili�
     * @param langue
     *            du documnet
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void setInfos(DocumentData document, String numAffilie, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {

        if (document == null) {
            throw new ALEcheanceModelException("EcheanceListeAffilie#setInfos : document is null");
        }

        if (numAffilie == null) {
            throw new ALEcheanceModelException("EcheanceListeAffilie#setInfos : numAffilie is null");
        }

        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("EcheanceListeAffilie#setInfos: language  " + langueDocument
                    + " is not  valid ");
        }

        // infos relatives � l'affili�
        document.addData("droitEcheanceAffilieInd_no_label",
                this.getText("al.echeances.info.numAffilie.libelle", langueDocument));
        document.addData("droitEcheanceAffilieInd_no_val", numAffilie);

        // info relatives au "concerne"
        document.addData("droitEcheanceAffilie_concerne_label",
                this.getText("al.echeances.info.concerne.libelle", langueDocument));
        document.addData("droitEcheanceAffilieInd_concerne_val",
                this.getText("al.echeances.info.concerne.val", langueDocument));

    }

    /**
     * m�thode chargeant dans le document la liste des droits � imprimer
     * 
     * @param document
     *            document � imprimer
     * @param droits
     *            droits
     * @param langueDocument
     *            langue du document
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void setTable(DocumentData document, ArrayList droits, String langueDocument)
            throws JadeApplicationException, JadePersistenceException {

        if (document == null) {
            throw new ALEcheanceModelException("EcheanceListeAffilie#setTable : document is null");
        }

        if (droits == null) {
            throw new ALEcheanceModelException("EcheanceListeAffilie#setTable : droits is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("cheanceListeAffilie#setTable: language  " + langueDocument
                    + " is not  valid ");
        }

        Collection table = new Collection("liste_droits_echeances_affilie");

        if (droits.size() != 0) {

            // JadeAbstractModel[] listeDroits = droits.getSearchResults();

            DataList entete = new DataList("entete");

            // ent�te allocataire
            entete.addData("entete_allocataire", this.getText("al.echeances.liste.entete.allocataire", langueDocument));

            // ent�te enfant
            entete.addData("entete_enfant", this.getText("al.echeances.liste.entete.enfant", langueDocument));

            // ent�te date echeance
            entete.addData("entete_dateEcheance",
                    this.getText("al.echeances.liste.entete.dateEcheance", langueDocument));

            // ent�te motif
            entete.addData("entete_motif", this.getText("al.echeances.liste.entete.motif", langueDocument));
            table.add(entete);

            String nomPrenomAllocatairePrecedent = "";

            // parcourir la liste pour ajouter les donn�es au documentdata
            // for (int i = 0; i < droits.size(); i++) {
            // droits.get(i);
            // DroitEcheanceComplexModel model = ((DroitEcheanceComplexModel)
            // droits[i]);

            Iterator it = droits.iterator();
            while (it.hasNext()) {
                // traitement de l'allocataire
                DroitEcheanceComplexModel droitsEcheance = (DroitEcheanceComplexModel) it.next();
                DataList ligne = new DataList("colonne");
                String nomPrenomAllocataire = droitsEcheance.getNomAllocataire() + " "
                        + droitsEcheance.getPrenomAllocataire();
                // ligne.addData("col_allocataire", nomPrenomAllocataire + "\n"
                // + droitsEcheance.getNumNss());

                ligne.addData(
                        "col_allocataire",
                        (!nomPrenomAllocataire.equals(nomPrenomAllocatairePrecedent) ? nomPrenomAllocataire + "\n"
                                + droitsEcheance.getNumNss() + "/" + droitsEcheance.getDroitModel().getIdDossier() : ""));

                nomPrenomAllocatairePrecedent = nomPrenomAllocataire;

                // traitement de l'enfant
                ligne.addData("col_enfant", droitsEcheance.getNomEnfant() + " " + droitsEcheance.getPrenomEnfant()
                        + "\n" + droitsEcheance.getDateNaissanceEnfant() + ", " + droitsEcheance.getNumNssEnfant());

                // traitement de l'�ch�ance
                ligne.addData("col_dateEcheance", droitsEcheance.getDroitModel().getFinDroitForcee());

                // traitement du motif de l'�ch�ance
                ligne.addData("col_motif",
                        ALServiceLocator.getDroitEcheanceService().getLibelleMotif(droitsEcheance, langueDocument));

                table.add(ligne);

            }
        }
        document.add(table);

    }

    /**
     * M�thode chargeant dans le document le texte d'introduction de l'avis d'�ch�ance
     * 
     * @param document
     *            document � imprimer
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void setTexteDebut(DocumentData document, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {

        if (document == null) {
            throw new ALEcheanceModelException("EcheancesListeAffilieImpl#setTexteDebut : document is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("echeanceListeAffilie#setTexteDebut: language  " + langueDocument
                    + " is not  valid ");
        }

        // formule de politesse
        document.addData("droitEcheanceAffilieInd_politesse", this.getText("al.echeances.politesse", langueDocument));
        // texte d'introduction
        document.addData("droitEcheanceAffilieInd_texteDebut",
                this.getText("al.echeances.AffilieAvisEcheance.texte.debut", langueDocument));
    }

    /**
     * M�thode ajoutant le texte de fin dans le document � imprimer
     * 
     * @param document
     *            document � imprimer
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected abstract void setTexteFin(DocumentData document, String langueDocument) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * M�thode chargeant les salutations dans le document � imprimer pour l'affili�
     * 
     * @param document
     *            document � imprimer
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */

    protected void setTexteSalutations(DocumentData document, String langueDocument) throws JadeApplicationException,
            JadePersistenceException {

        if (document == null) {
            throw new ALEcheanceModelException("EcheancesListeAffilie#setTexteSalutations : document is null");
        }
        if (!JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_FRANCAIS, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ALLEMAND, false)
                && !JadeStringUtil.equals(langueDocument, ALConstLangue.LANGUE_ISO_ITALIEN, false)) {
            throw new ALEcheanceModelException("echeanceListeAffilie#setTexteSalutations: language  " + langueDocument
                    + " is not  valid ");
        }
        // texte de salutations de fin
        document.addData("droitEcheanceAffilieInd_texteSalutations",
                this.getText("al.echeances.AffilieAvisEcheance.texte.politesseFin", langueDocument));

    }

}
