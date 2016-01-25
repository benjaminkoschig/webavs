package ch.globaz.al.businessimpl.services.paiement;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.paiement.PaiementBusinessModel;
import ch.globaz.al.business.services.paiement.ProtocoleDetaillePaiementDirectService;
import ch.globaz.al.businessimpl.documents.AbstractProtocole;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * 
 * Classe d'impl�mentation du service de g�n�ration du protocole d�taill� de simulation pour le paiement direct
 * 
 * @author PTA
 * 
 */
public class ProtocoleDetaillePaiementDirectServiceImpl extends AbstractProtocole implements
        ProtocoleDetaillePaiementDirectService {

    /**
     * D�finit le contenu de la liste d�taill� des paiements direct
     * 
     * @param document
     *            document � remplir
     * @param listPaiement
     *            liste de <code>PaiementBusinessModel</code>
     * @throws JadeApplicationException
     *             Exception lev�e si l'un des param�tres n'est pas valide
     */
    private void addListeDetaille(DocumentData document, java.util.Collection listPaiement)
            throws JadeApplicationException {
        // contr�le des param�tres
        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleDetaillePaiementDirectServiceImpl#setListeDetaille: document is null");
        }
        if (listPaiement == null) {
            throw new ALProtocoleException(
                    "ProtocoleDetaillePaiementDirectServiceImpl#setListeDetaille: listPaiement is null");
        }

        // liste d�taill� pour les versements
        Collection tableVerse = new Collection("listeDetaillee_versement");
        // liste d�taill� pour les restitutions
        Collection tableResti = new Collection("listeDetaillee_restitution");
        // tableau total g�n�ral
        // Collection tableTotal = new Collection("total_general");

        BigDecimal totalCreditVersement = new BigDecimal("0");
        BigDecimal totalDebitVersement = new BigDecimal("0");
        BigDecimal totalDebitRestitution = new BigDecimal("0");
        BigDecimal totalOrdreVersement = new BigDecimal("0");

        Iterator it = listPaiement.iterator();
        // if (it.hasNext()) {

        // ajouts des ent�tes de colonnes au document
        // allocataire
        document.addData("entete_allocataire", this.getText("al.protocoles.paiementDirect.detail.allocataire.entete"));
        // solde initial
        document.addData("entete_solde_initial",
                this.getText("al.protocoles.paiementDirect.detail.soldeInitial.entete"));
        // D�bit
        document.addData("entete_debit", this.getText("al.protocoles.paiementDirect.detail.debit.entete"));
        // cr�dit
        document.addData("entete_credit", this.getText("al.protocoles.paiementDirect.detail.credit.entete"));
        // Nouveau Solde
        document.addData("entete_nouveau_solde",
                this.getText("al.protocoles.paiementDirect.detail.nouveauSolde.entete"));
        // ordre versement
        document.addData("entete_ordre_versement",
                this.getText("al.protocoles.paiementDirect.detail.ordreVersement.entete"));
        // versement compensatoire
        document.addData("entete_versement",
                this.getText("al.protocoles.paiementDirect.detail.verseCompensation.entete"));

        // ajout de l'entete restitution
        document.addData("entete_restitution", this.getText("al.protocoles.paiementDirect.detail.restitution.entete"));

        while (it.hasNext()) {
            PaiementBusinessModel paiement = (PaiementBusinessModel) it.next();

            DataList lignePaiement = new DataList("colonne");
            lignePaiement.addData("col_allocataire",
                    paiement.getNomAllocataire() + " - " + paiement.getNssAllocataire());
            lignePaiement.addData("col_solde_init",
                    JANumberFormatter.fmt(paiement.getSoldeInitial().toString(), true, true, false, 2));
            lignePaiement.addData("col_debit",
                    JANumberFormatter.fmt(paiement.getDebit().toString(), true, true, false, 2));
            lignePaiement.addData("col_credit",
                    JANumberFormatter.fmt(paiement.getCredit().toString(), true, true, false, 2));

            lignePaiement.addData("col_nouv_solde",
                    JANumberFormatter.fmt(paiement.getNouveauSolde().toString(), true, true, false, 2));
            lignePaiement.addData("col_odrdre_versement",
                    JANumberFormatter.fmt(paiement.getOrdreVersement().toString(), true, true, false, 2));

            if (paiement.isRestitution()) {
                // ajoute la ligne au tableau des restitutions
                tableResti.add(lignePaiement);
                // calcul des totaux
                totalDebitRestitution = totalDebitRestitution.add(paiement.getDebit());

            } else {
                // ajoute la ligne au tableau des versements
                tableVerse.add(lignePaiement);
                // calcul des totaux
                totalCreditVersement = totalCreditVersement.add(paiement.getCredit());
                totalDebitVersement = totalDebitVersement.add(paiement.getDebit());
                totalOrdreVersement = totalOrdreVersement.add(paiement.getOrdreVersement());
            }

        }

        // ajouts des totaux pour les versements
        DataList listTotauxVersement = new DataList("colonneTotalVersement");
        // label pour le total
        listTotauxVersement.addData("col_vers_total_label",
                this.getText("al.protocoles.paiementDirect.detail.total.verseCompensation.label"));
        // total debit
        listTotauxVersement.addData("col_vers_tot_debit",
                JANumberFormatter.fmt(totalDebitVersement.toString(), true, true, false, 2));
        // total cr�dit
        listTotauxVersement.addData("col_vers_tot_credit",
                JANumberFormatter.fmt(totalCreditVersement.toString(), true, true, false, 2));
        // total ordre versement
        listTotauxVersement.addData("col_vers_tot_ordre",
                JANumberFormatter.fmt(totalOrdreVersement.toString(), true, true, false, 2));
        // ajout au tableau versement
        tableVerse.add(listTotauxVersement);

        // ajouts des totaux pour les restitutions
        DataList listTotauxRestitution = new DataList("colonneTotaRestitution");
        // label pour le total
        listTotauxRestitution.addData("col_resti_total_label",
                this.getText("al.protocoles.paiementDirect.detail.total.restitution.label"));
        // total D�bit
        listTotauxRestitution.addData("col_resti_tot_debit",
                JANumberFormatter.fmt(totalDebitRestitution.toString(), true, true, false, 2));
        // ajout au tableau restitution
        tableResti.add(listTotauxRestitution);

        // ajout de la liste des versements au document
        document.add(tableVerse);
        // ajout de la liste des restitutions au document
        document.add(tableResti);

        // ajout des totaux g�n�raux
        document.addData("entete_tot_gen", this.getText("al.protocoles.paiementDirect.detail.total.general.label"));

        BigDecimal totalDebit = totalDebitVersement.add(totalDebitRestitution);
        document.addData("col_gen_debit", JANumberFormatter.fmt(totalDebit.toString(), true, true, false, 2));
        document.addData("col_gen_credit", JANumberFormatter.fmt(totalCreditVersement.toString(), true, true, false, 2));
        document.addData("col_gen_total", JANumberFormatter.fmt(totalOrdreVersement.toString(), true, true, false, 2));

    }

    @Override
    public DocumentData getDocumentData(java.util.Collection<PaiementBusinessModel> listePaiement,
            HashMap<String, String> params) throws JadePersistenceException, JadeApplicationException {
        // contr�le des param�tres
        if (listePaiement == null) {
            throw new ALProtocoleException(
                    "ProtocolDetaillePaiementDirectServiceImpl#getDocumentData: listePaiement is null");
        }
        if (params == null) {
            throw new ALProtocoleException("ProtocolDetaillePaiementDirectServiceImpl#getDocumentData: params is null");
        }

        DocumentData doc = init(params);
        addListeDetaille(doc, listePaiement);
        return doc;
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        // contr�le des param�tres
        if (document == null) {
            throw new ALProtocoleException("ProtocolDetaillePaiementDirectServiceImpl#setIdDocument: document is null");
        }
        document.addData("AL_idDocument", "AL_simulationPaiementDirectListeDetaillee");

    }

}
