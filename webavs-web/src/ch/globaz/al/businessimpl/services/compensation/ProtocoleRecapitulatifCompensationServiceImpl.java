package ch.globaz.al.businessimpl.services.compensation;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.compensation.CompensationRecapitulatifBusinessModel;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.business.services.compensation.ProtocoleRecapitulatifCompensationService;
import ch.globaz.al.businessimpl.documents.AbstractProtocole;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.models.ParameterSearchModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.topaz.datajuicer.Collection;
import ch.globaz.topaz.datajuicer.DataList;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Implémentation du service de génération des protocoles de compensation
 * 
 * @author jts
 * 
 */
public class ProtocoleRecapitulatifCompensationServiceImpl extends AbstractProtocole implements
        ProtocoleRecapitulatifCompensationService {

    @Override
    public DocumentData getProtocoleDocumentData(
            java.util.Collection<CompensationRecapitulatifBusinessModel> listeRecap, HashMap<String, String> params)
            throws JadePersistenceException, JadeApplicationException {

        if (listeRecap == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifCompensationServiceImpl#getProtocoleDocumentData : document is null");
        }

        if (params == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifCompensationServiceImpl#getProtocoleDocumentData : params is null");
        }

        DocumentData documentData = init(params);
        setListeRecapitulative(documentData, listeRecap);
        return documentData;
    }

    @Override
    protected void setIdDocument(DocumentData document) throws JadeApplicationException {
        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifCompensationServiceImpl#setIdDocument : document is null");
        }
        document.addData("AL_idDocument", "AL_simulationCompensationRecapitulative");
    }

    /**
     * Définit le contenu de la liste du protocole
     * 
     * @param document
     *            Document dans lequel placer les données
     * @param rubriques
     *            ArrayList contenant des instances de <code>CompensationProtocoleRecapitulatifBusinessModel</code>
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     * @throws JadePersistenceException
     *             Exception levée si le nom d'une rubrique comptable n'a pas pu être trouvée
     */
    private void setListeRecapitulative(DocumentData document,
            java.util.Collection<CompensationRecapitulatifBusinessModel> rubriques) throws JadeApplicationException,
            JadePersistenceException {

        if (document == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifCompensationServiceImpl#setListeRecapitulative : document is null");
        }

        if (rubriques == null) {
            throw new ALProtocoleException(
                    "ProtocoleRecapitulatifCompensationServiceImpl#setListeRecapitulative : listeRecap is null");
        }

        Collection table = new Collection("listeRecapitulative");

        Iterator<CompensationRecapitulatifBusinessModel> it = rubriques.iterator();

        if (it.hasNext()) {

            document.addData("entete_rubrique", this.getText("al.protocoles.compensation.rubriqueComptable"));
            document.addData("entete_debit", this.getText("al.protocoles.compensation.debit"));
            document.addData("entete_credit", this.getText("al.protocoles.compensation.credit"));
            document.addData("entete_solde", this.getText("al.protocoles.compensation.solde"));

            BigDecimal debit = new BigDecimal("0");
            BigDecimal credit = new BigDecimal("0");

            while (it.hasNext()) {
                CompensationRecapitulatifBusinessModel rubr = it.next();

                DataList ligne = new DataList("colonne");
                ligne.addData("col_num_rubr", rubr.getNumeroCompte());

                ParameterSearchModel search = new ParameterSearchModel();
                search.setForValeurAlphaParametre(rubr.getNumeroCompte());
                search = ParamServiceLocator.getParameterModelService().search(search);
                if (search.getSize() == 1) {
                    ligne.addData("col_nom_rubr",
                            ((ParameterModel) search.getSearchResults()[0]).getDesignationParametre());
                }

                ligne.addData("col_debit",
                        JANumberFormatter.fmt(rubr.getDebit().abs().toString(), true, true, false, 2));
                ligne.addData("col_credit", JANumberFormatter.fmt(rubr.getCredit().toString(), true, true, false, 2));
                ligne.addData("col_solde", JANumberFormatter.fmt((rubr.getCredit()).subtract(rubr.getDebit().abs())
                        .toString(), true, true, false, 2));
                table.add(ligne);

                debit = debit.add(rubr.getDebit().abs());
                credit = credit.add(rubr.getCredit());
            }

            DataList ligne = new DataList("colonneTotal");
            ligne.addData("col_total_label", "Total");
            ligne.addData("col_total_debit", JANumberFormatter.fmt(debit.toString(), true, true, false, 2));
            ligne.addData("col_total_credit", JANumberFormatter.fmt(credit.toString(), true, true, false, 2));
            ligne.addData("col_total_solde",
                    JANumberFormatter.fmt((credit.subtract(debit)).toString(), true, true, false, 2));
            table.add(ligne);
        }

        document.add(table);
    }
}