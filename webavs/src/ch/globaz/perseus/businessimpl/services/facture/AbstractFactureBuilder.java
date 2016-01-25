/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.facture;

import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.PlanDeCalculBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author MBO
 * 
 */
public class AbstractFactureBuilder extends PlanDeCalculBuilder {

    protected Dossier dossier = null;
    protected String titreTiers = null;

    protected DocumentData buildHeaderFacture(DocumentData data, boolean isCopie, String idTiersAdresseCourrier,
            String idGestionnaire, String idCaisse, String DateJJMMAAAA, boolean isBandeau, String titreDocument,
            Boolean isSignatureSpecifique) throws Exception {
        try {

            /**
             * Appel du Header générique
             */
            data = buildHeader(data, isCopie, false, idTiersAdresseCourrier, idGestionnaire, idCaisse, DateJJMMAAAA,
                    titreDocument, isSignatureSpecifique);

            /**
             * Chargement des informations complémentaires au Header pour les factures
             */
            // Si appelé par DecisionRefusDemandeBuilder ou DecisionREfusFactureBuilder, le paramètre Facture
            // est null, si il est appelé par un autre builder, facture ne sera pas null et rentrera dans la condition.
            if (isBandeau) {
                data.addData("isNonValide", "TRUE");
                data.addData("TexteBandeau", getBabelContainer()
                        .getTexte(IPFCatalogueTextes.CS_FACTURES_COMMUNES, 1, 1));
            } else {
                data.addData("isNonValide", "FALSE");
            }

            return data;

        } catch (Exception e) {
            throw new DecisionException("AbstractFactureBuilder / buildHeaderFacture -  NSS : "
                    + ", Détail de l'erreur : " + e.toString(), e);
        }
    }

    public void init() throws Exception {
        // Chargement du catalogue de texte
        getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_FACTURES_COMMUNES);

    }

    public void loadEntityAbstractFacture(String idDossier, String catTexte) throws Exception {

        init();
        dossier = PerseusServiceLocator.getDossierService().read(idDossier);
        String codeIsoLangue = dossier.getDemandePrestation().getPersonneEtendue().getTiers().getLangue();
        loadEntity(catTexte, codeIsoLangue);
        titreTiers = getSession().getCodeLibelle(
                dossier.getDemandePrestation().getPersonneEtendue().getTiers().getTitreTiers());
    }

}
