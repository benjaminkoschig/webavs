package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.log.JadeLogger;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.codesystem.CodeSystem;
import ch.globaz.common.codesystem.CodeSystemUtils;
import ch.globaz.common.util.prestations.MotifVersementUtil;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;

/**
 * Le But de cette est de créer une liste qui vas contenir des prestions avec toute les
 * opérations(écritures,ordreVersement) qui vont être utilisé dans la comptabilité.
 * 
 * @author sce
 */
public class GenerateOperationsBlocage extends GenerateOperationBasic implements GenerateOperations {

    private static OrdreVersement generateOv(SimpleOrdreVersement ov, String refPaiement) {

        return new OrdreVersement(ov.getId(), ov.getCsType(), ov.getCsTypeDomaine(), ov.getIdSectionDetteEnCompta(),
                ov.getIdTiers(), ov.getIdTiersAdressePaiement(), ov.getIdTiersAdressePaiementConjoint(),
                ov.getIdTiersOwnerDetteCreance(), ov.getMontant(), ov.getSousTypeGenrePrestation(),
                ov.getIdDomaineApplication(), ov.getIdDomaineApplicationConjoint(), null, refPaiement);
    }

    /**
     * Retourne le csRoleFamillePC en fonction de la comparaison de deux idTiers Règles: si les duex idTiers matches,
     * c'est le requérant, sinon c'est le conjoint
     * 
     * @param idTiers
     * @param idTiersOv
     * @return le code système du role mebre famille
     */
    protected static String getCsRoleFamille(String idTiers, String idTiersOv) {

        if ((idTiers == null) || (idTiersOv == null)) {
            throw new IllegalArgumentException("The idTiers passed in parameters cannont be null");
        }

        if (idTiers.equals(idTiersOv)) {
            return IPCDroits.CS_ROLE_FAMILLE_REQUERANT;
        } else {
            return IPCDroits.CS_ROLE_FAMILLE_CONJOINT;

        }
    }

    List<Ecriture> ecritures = new ArrayList<Ecriture>();

    List<OrdreVersementCompta> ovsCompta = new ArrayList<OrdreVersementCompta>();

    @Override
    public Operations generateAllOperations(List<OrdreVersementForList> ovs, List<SectionSimpleModel> sections,
            String dateForOv, String dateEcheance) throws OrdreVersementException,
            JadeApplicationServiceNotAvailableException {
        ecritures = new ArrayList<Ecriture>();
        ovsCompta = new ArrayList<OrdreVersementCompta>();

        try {
            // iteration sur la listes des ovs
            for (OrdreVersementForList ov : ovs) {
                SectionSimpleModel sectionBlocage = getSectionObjectFromList(ov.getSimpleOrdreVersement()
                        .getIdSection(), sections);
                String csTypeOv = ov.getSimpleOrdreVersement().getCsType();

                if (csTypeOv == null) {
                    throw new IllegalArgumentException("The csType of the simpleOrdreVersement model is null [idOv: "
                            + ov.getSimpleOrdreVersement().getId() + ", idTiers: "
                            + ov.getSimpleOrdreVersement().getIdTiers());
                }

                String motifVersement = formatDeblocage(ov, dateEcheance);

                // versement beneficiaire --> ecriture au credit
                if (csTypeOv.equals(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)) {
                    CompteAnnexeSimpleModel compteAnnexe = resolvedCompteAnnexe(ov);
                    ovsCompta.add(new OrdreVersementCompta(compteAnnexe, ov.getSimpleOrdreVersement()
                            .getIdTiersAdressePaiement(), ov.getSimpleOrdreVersement().getIdDomaineApplication(),
                            new BigDecimal(ov.getSimpleOrdreVersement().getMontant()), sectionBlocage, ov
                                    .getIdTiersRequerant(), csTypeOv, GenerateOperationsBlocage.getCsRoleFamille(
                                    ov.getIdTiersRequerant(), ov.getSimpleOrdreVersement().getIdTiers()),
                            motifVersement));

                }
                // creancier ecriture au credit
                else if (csTypeOv.equals(IREOrdresVersements.CS_TYPE_TIERS)) {
                    CompteAnnexeSimpleModel compteAnnexe = resolvedCompteAnnexe(ov);
                    ovsCompta.add(new OrdreVersementCompta(compteAnnexe, ov.getSimpleOrdreVersement()
                            .getIdTiersAdressePaiement(), ov.getSimpleOrdreVersement().getIdDomaineApplication(),
                            new BigDecimal(ov.getSimpleOrdreVersement().getMontant()), sectionBlocage, ov
                                    .getSimpleOrdreVersement().getIdTiers(), csTypeOv, GenerateOperationsBlocage
                                    .getCsRoleFamille(ov.getIdTiersRequerant(), ov.getSimpleOrdreVersement()
                                            .getIdTiers()), motifVersement));

                }
                // dette ecriture au credit, ecriture au debit
                else if (csTypeOv.equals(IREOrdresVersements.CS_TYPE_DETTE)) {
                    SectionSimpleModel sectionDette = getSectionObjectFromList(ov.getSimpleOrdreVersement()
                            .getIdSectionDetteEnCompta(), sections);

                    // this.ecritures.add(this.generateEcriture(SectionPegasus.BLOCAGE, APIEcriture.DEBIT,
                    // APIReferenceRubrique.COMPENSATION_RENTES, new BigDecimal(ov.getSimpleOrdreVersement()
                    // .getMontant()), null, compteAnnexe.getIdCompteAnnexe(), TypeEcriture.DETTE,
                    // GenerateOperationsBlocage.generateOv(ov.getSimpleOrdreVersement())));

                    ecritures.add(this.generateEcriture(null, APIEcriture.DEBIT,
                            APIReferenceRubrique.COMPENSATION_RENTES, new BigDecimal(ov.getSimpleOrdreVersement()
                                    .getMontant()), sectionBlocage, sectionBlocage.getIdCompteAnnexe(),
                            TypeEcriture.DETTE, GenerateOperationsBlocage.generateOv(ov.getSimpleOrdreVersement(),
                                    motifVersement)));

                    ecritures.add(this.generateEcriture(null, APIEcriture.CREDIT,
                            APIReferenceRubrique.COMPENSATION_RENTES, new BigDecimal(ov.getSimpleOrdreVersement()
                                    .getMontant()), sectionDette, sectionDette.getIdCompteAnnexe(), TypeEcriture.DETTE,
                            GenerateOperationsBlocage.generateOv(ov.getSimpleOrdreVersement(), motifVersement)));

                }

            }

            Operations o = new Operations();
            o.addAllEcritures(ecritures);
            o.addAllOVs(ovsCompta);

            return o;

        } catch (JadeApplicationException e) {
            throw new OrdreVersementException("An exception occured during generation blocage operations", e);
        }
    }

    String formatDeblocage(OrdreVersementForList ov, String dateEcheance) {
        String idTiersPrincipal = ov.getSimpleOrdreVersement().getIdTiersAdressePaiement();

        if (JadeStringUtil.isBlankOrZero(idTiersPrincipal)) {
            idTiersPrincipal = ov.getSimpleOrdreVersement().getIdTiersOwnerDetteCreance();
        }

        String isoLangFromIdTiers = PRTiersHelper.getIsoLangFromIdTiers(BSessionUtil.getSessionFromThreadContext(),
                idTiersPrincipal);

        String message = MotifVersementUtil.getTranslatedLabelFromIsolangue(isoLangFromIdTiers,
                "PEGASUS_COMPTABILISATION_VERSEMENT_DU", BSessionUtil.getSessionFromThreadContext());

        String libelle = "";
        try {
            CodeSystem csLibelle = CodeSystemUtils.searchCodeSystemTraduction("64055001",
                    BSessionUtil.getSessionFromThreadContext(), isoLangFromIdTiers);
            libelle = csLibelle.getTraduction();
        } catch (Exception e) {
            JadeLogger.warn(e, e.getMessage());
            libelle = BSessionUtil.getSessionFromThreadContext().getCodeLibelle("64055001");
        }

        return MotifVersementUtil.formatDeblocage(ov.getNumAvs(),
                ov.getDesignationRequerant1() + " " + ov.getDesignationRequerant2(), ov.getSimpleOrdreVersement()
                        .getRefPaiement(), libelle, message + " " + dateEcheance);
    }

    private SectionSimpleModel getSectionObjectFromList(String idSection, List<SectionSimpleModel> sectionsDette) {

        SectionSimpleModel simpleSectionToReturn = null;

        for (SectionSimpleModel simpleSection : sectionsDette) {
            if (simpleSection.getIdSection().equals(idSection)) {
                simpleSectionToReturn = simpleSection;
            }
        }

        if (simpleSectionToReturn == null) {
            throw new IllegalArgumentException("The section with this id : " + idSection
                    + " was not found in the liste");
        }

        return simpleSectionToReturn;

    }

    /**
     * Effectue la mapping entre un ordre de versement et une section. Le lien se fait via les champs
     * SimpleOrdreVersement.idSectionDetteEnCompta et SectionSimpleModel.idSection
     * 
     * @param simpleOv
     * @param sectionsDette
     * @return une instance de l'eunm SectionPegasus
     */
    // protected static SectionPegasus getSectionMappingWithOv(SimpleOrdreVersement simpleOv, List<SectionSimpleModel>
    // sectionsDette){
    //
    // SectionPegasus matchingSection = null;
    //
    // //iteration sur les sections dettes
    // for(SectionSimpleModel section:sectionsDette){
    // if(section.getIdSection().equals(simpleOv.getIdSectionDetteEnCompta())){
    // matchingSection = getSectionPegasusByIdSection(section.getIdSection());
    // }
    // }
    //
    // return matchingSection;
    // }

    // protected static SectionPegasus getSectionPegasusByIdSection(String idSection){
    //
    // for(SectionPegasus sectionPegasus:SectionPegasus.values()){
    // if(sectionPegasus.getType().equals(idSection)){
    // return sectionPegasus;
    // }
    // }
    //
    // throw new IllegalArgumentException("The idSection passed dont'match any SectionPegasus section ["+idSection+"]");
    // }

    protected GenerateEcrituresResitutionBeneficiareForDecisionAc newEcritureBasic() {
        GenerateEcrituresResitutionBeneficiareForDecisionAc ac = new GenerateEcrituresResitutionBeneficiareForDecisionAc();
        return ac;
    }

    private CompteAnnexeSimpleModel resolvedCompteAnnexe(OrdreVersementForList ov) throws ComptabiliserLotException {
        return CompteAnnexeResolver.resolveByIdCompteAnnexe(ov.getIdCompteAnnexeRequerant());
    }

    @Override
    public Operations generateAllOperations(List<OrdreVersementForList> ovs, List<SectionSimpleModel> sections,
            String dateForOv, String dateEcheance, PrestationOvDecompte decompteInit) throws JadeApplicationException {
        return generateAllOperations(ovs, sections, dateForOv, dateEcheance);
    }

}
