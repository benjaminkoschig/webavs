package ch.globaz.al.businessimpl.services.compensation;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.compensation.CompensationBusinessModel;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.services.compensation.ProtocoleDetailleCompensationService;
import ch.globaz.al.businessimpl.documents.AbstractProtocole;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service de génération des protocoles de compensation
 * 
 * @author jts
 * 
 */
public class ProtocoleDetailleCompensationServiceImpl extends AbstractProtocole implements
        ProtocoleDetailleCompensationService {

    @Override
    public DocumentData getDocumentData(java.util.Collection<CompensationBusinessModel> listeRecap,
            HashMap<String, String> params) throws JadePersistenceException, JadeApplicationException {

        if (listeRecap == null) {
            throw new ALProtocoleException(
                    "ProtocoleDetailleCompensationServiceImpl#getProtocoleDocumentData : document is null");
        }

        if (params == null) {
            throw new ALProtocoleException(
                    "ProtocoleDetailleCompensationServiceImpl#getProtocoleDocumentData : params is null");
        }

        DocumentData documentData = init(params);
        setListeDetaillee(documentData, listeRecap);
        return documentData;
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException("ProtocoleDetailleCompensationServiceImpl#setIdDocument : document is null");
        }
        document.addData("AL_idDocument", "AL_simulationCompensationDetaillee");
    }

    /**
     * Définit le contenu de la liste du protocole
     * 
     * @param document
     *            Document dans lequel placer les données
     * @param listeRecap
     *            ArrayList contenant des instances de <code>RecapCompensationProtocoleBusinessModel</code>
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     */
    private void setListeDetaillee(DocumentData document, java.util.Collection<CompensationBusinessModel> listeRecap)
            throws JadeApplicationException {

        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleDetailleCompensationServiceImpl#setListeDetaillee : document is null");
        }

        if (listeRecap == null) {
            throw new ALProtocoleException(
                    "ProtocoleDetailleCompensationServiceImpl#setListeDetaillee : listeRecap is null");
        }

        Collection table = new Collection("listeDetaillee");

        Iterator<CompensationBusinessModel> it = listeRecap.iterator();

        if (it.hasNext()) {

            document.addData("entete_affilie", this.getText("al.protocoles.compensation.nom"));
            document.addData("entete_noRecap", this.getText("al.protocoles.compensation.numRecap"));
            document.addData("entete_periode", this.getText("al.protocoles.compensation.periode"));
            document.addData("entete_total", this.getText("al.protocoles.compensation.montant"));

            BigDecimal total = new BigDecimal("0");

            while (it.hasNext()) {
                CompensationBusinessModel recap = it.next();

                DataList ligne = new DataList((recap.getMontant().compareTo(new BigDecimal("0")) > 0) ? "colonne"
                        : "colonneNeg");
                ligne.addData("col_affilie", recap.getNomAffilie() + " - " + recap.getNumeroAffilie());
                ligne.addData("col_noRecap", JANumberFormatter.fmt(recap.getIdRecap(), true, true, false, 0));
                ligne.addData("col_periodeDe", recap.getPeriodeRecapDe());
                ligne.addData("col_periodeA", recap.getPeriodeRecapA());
                ligne.addData("col_total", JANumberFormatter.fmt(recap.getMontant().toString(), true, true, false, 2));
                table.add(ligne);

                total = total.add(recap.getMontant());
            }

            DataList ligne = new DataList("colonneTotal");
            ligne.addData("col_total_label", "Total");
            ligne.addData("col_total_montant", JANumberFormatter.fmt(total.toString(), true, true, false, 2));
            table.add(ligne);
        }

        document.add(table);
    }
}