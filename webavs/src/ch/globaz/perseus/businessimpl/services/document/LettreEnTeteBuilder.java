/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.document;

import globaz.prestation.tools.PRStringUtils;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author MBO
 * 
 */
public class LettreEnTeteBuilder extends AbstractDocumentBuilder {
    private static final String CDT_INFO_TIERS = "{infoTiers}";

    public LettreEnTeteBuilder() {

    }

    protected DocumentData buildLettreEntete(DocumentData data, String idTiersAdresse, String idTiersRequerant,
            String csCaisse, String dateDocument, String idGestionnaire) throws Exception {

        // Charger le tiers pour déterminer la langue et le titre
        PersonneEtendueComplexModel tiersLettreEntete = TIBusinessServiceLocator.getPersonneEtendueService().read(
                idTiersAdresse);
        PersonneEtendueComplexModel tiersRequerant = TIBusinessServiceLocator.getPersonneEtendueService().read(
                idTiersRequerant);
        // Chargement catalogue de textes pour la lettre en-tête
        getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_LETTRE_ENTETE);
        getBabelContainer().setCodeIsoLangue(getSession().getCode(tiersLettreEntete.getTiers().getLangue()));
        getBabelContainer().load();

        data.addData("idProcess", "PCFLettreEnTete");

        // TODO problème avec le titre de la lettre entête car il n'a pas de titre
        data = buildHeader(data, false, true, idTiersAdresse, idGestionnaire, csCaisse, dateDocument,
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_LETTRE_ENTETE, 1, 1), false);

        // Chargement du titre du tiers
        String titreTiers = getSession().getCodeLibelle(tiersLettreEntete.getTiers().getTitreTiers());

        // Insertion du titre de la lettre si c'est l'agence de Lausanne
        if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
            data.addData("titreLettre", getBabelContainer().getTexte(IPFCatalogueTextes.CS_LETTRE_ENTETE, 1, 1));
        }

        // Insertion du sujet de la lettre
        data.addData("sujetLettre", PRStringUtils.replaceString(
                getBabelContainer().getTexte(IPFCatalogueTextes.CS_LETTRE_ENTETE, 2, 1),
                LettreEnTeteBuilder.CDT_INFO_TIERS, tiersRequerant.getPersonneEtendue().getNumAvsActuel() + " - "
                        + tiersRequerant.getTiers().getDesignation1() + " "
                        + tiersRequerant.getTiers().getDesignation2()));

        // Insertion du texte de la lettre
        data.addData("texteLettre", getBabelContainer().getTexte(IPFCatalogueTextes.CS_LETTRE_ENTETE, 3, 1));

        return data;
    }
}
