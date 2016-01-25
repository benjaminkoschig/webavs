package ch.globaz.al.businessimpl.services.paiement;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.paiement.PaiementRecapitulatifBusinessModel;
import ch.globaz.al.business.services.paiement.ProtocoleRecapitulatifPaiementDirectService;
import ch.globaz.al.businessimpl.documents.AbstractProtocole;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * 
 * 
 * Classe d'implémentation du service de génération du protocole de récapitulations de simulation pour le paiement
 * direct FIXME:JANumberFormatter!
 * 
 * @author PTA
 * 
 */
public class ProtocoleRecapitulatifPaiementDirectServiceImpl extends AbstractProtocole implements
        ProtocoleRecapitulatifPaiementDirectService {

    /**
     * Méthode qui remplit la liste récapitulative
     * 
     * @param doc
     *            document sur lequel apparaît la liste
     * @param listRecapPaiement
     *            liste des paiements contenant des <code>PaiementRecapitulatifBusinessModel</code>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private void addListeRecapitulative(DocumentData doc,
            java.util.Collection<PaiementRecapitulatifBusinessModel> listRecapPaiement)
            throws JadePersistenceException, JadeApplicationException {
        // contrôle des paramètres
        if (doc == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifPaiementDirectServiceImpl# setListeRecapitulative: document is null");
        }

        if (listRecapPaiement == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifPaiementDirectServiceImpl# setListeRecapitulative: listRecapPaiement is null");
        }

        Collection listeRecapitulatif = new Collection("listeRecapitulatif_Paiement");

        BigDecimal totalCredit = new BigDecimal("0");
        BigDecimal totalDebit = new BigDecimal("0");
        BigDecimal totalSolde = new BigDecimal("0");

        Iterator<PaiementRecapitulatifBusinessModel> it = listRecapPaiement.iterator();

        doc.addData("entete_rubrique_comptable",
                this.getText("al.protocoles.paiementDirect.recapitulation.rubriqueComptable"));
        doc.addData("entete_debit", this.getText("al.protocoles.paiementDirect.recapitulation.debit.entete"));
        doc.addData("entete_credit", this.getText("al.protocoles.paiementDirect.recapitulation.credit.entete"));
        doc.addData("solde_entete", this.getText("al.protocoles.paiementDirect.recapitulation.solde.entete"));

        while (it.hasNext()) {
            PaiementRecapitulatifBusinessModel lignePaiement = it.next();

            DataList colonnesPaiement = new DataList("colonnesPaiements");

            colonnesPaiement.addData("col_no_comptabilite", lignePaiement.getNumeroCompte());

            ParameterSearchModel search = new ParameterSearchModel();
            search.setForValeurAlphaParametre(lignePaiement.getNumeroCompte());
            search.setForDateDebutValidite(JadeDateUtil.getGlobazFormattedDate(new Date()));
            search = ParamServiceLocator.getParameterModelService().search(search);
            if (search.getSize() == 1) {
                colonnesPaiement.addData("col_presta_alloc_familliale",
                        ((ParameterModel) search.getSearchResults()[0]).getDesignationParametre());
            }

            colonnesPaiement.addData("col_debit",
                    JANumberFormatter.fmt(lignePaiement.getDebit().toString(), true, true, false, 2));
            colonnesPaiement.addData("col_credit",
                    JANumberFormatter.fmt(lignePaiement.getCredit().toString(), true, true, false, 2));

            BigDecimal solde = new BigDecimal(lignePaiement.getCredit().toString()).add(new BigDecimal(lignePaiement
                    .getDebit().toString()));

            colonnesPaiement.addData("col_solde", JANumberFormatter.fmt(solde.toString(), true, true, false, 2));

            // ajout de la ligne au tableau
            listeRecapitulatif.add(colonnesPaiement);

            // calculs des soldes
            totalCredit = totalCredit.add(new BigDecimal(lignePaiement.getCredit().toString()));
            totalDebit = totalDebit.add(new BigDecimal(lignePaiement.getDebit().toString()));
            totalSolde = totalSolde.add(solde);
        }

        // ajouts des totaux
        DataList ligneTotal = new DataList("colonneTotal");
        ligneTotal.addData("col_total_label", this.getText("al.protocoles.paiementDirect.recapitulation.total.label"));

        ligneTotal.addData("col_tot_debit", JANumberFormatter.fmt(totalDebit.toString(), true, true, false, 2));
        ligneTotal.addData("col_tot_credit", JANumberFormatter.fmt(totalCredit.toString(), true, true, false, 2));
        ligneTotal.addData("col_tot_solde", JANumberFormatter.fmt(totalSolde.toString(), true, true, false, 2));

        // ajout de la ligne total au tableau
        listeRecapitulatif.add(ligneTotal);

        // ajout du tableau
        doc.add(listeRecapitulatif);

    }

    @Override
    public DocumentData getProtocoleDocumentData(java.util.Collection<PaiementRecapitulatifBusinessModel> listeRecap,
            HashMap<String, String> params) throws JadePersistenceException, JadeApplicationException {
        // contrôle de paramètres
        if (listeRecap == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifPaiementDirectServiceImpl# getProtocoleDocumentData: listeRecap is null");

        }
        if (params == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifPaiementDirectServiceImpl# getProtocoleDocumentData: params is null");
        }
        DocumentData document = init(params);
        addListeRecapitulative(document, listeRecap);

        return document;
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        // contrôle des paramètres
        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifPaiementDirectServiceImpl# setIdDocument: document is null");
        }

        document.addData("AL_idDocument", "AL_simulationPaiementDirectListeRecapitulative");

    }
}
