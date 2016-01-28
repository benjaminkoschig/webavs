package globaz.pegasus.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCApiAvsAi;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortuneAuto;
import ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenuAuto;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViagerSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class PCDessaisissementHandler {

    /**
     * Methode pour formatter la description d'un dessaisissement de fortune automatique
     * 
     * @param objSession
     *            Session en cours
     * @param dsa
     *            Entité de dessaisissement à formatter
     * @return Description formattée du dessaisissement
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static String[] formatDessaisissementFortuneAutoDescription(BSession objSession,
            DessaisissementFortuneAuto dsa) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        String csType = dsa.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere();
        String libelleType = objSession.getCodeLibelle(csType);

        if (IPCDroits.CS_NUMERAIRES.equals(csType)) {
            return new String[] { libelleType, "", dsa.getMontantNumeraire() };
        }

        if (IPCDroits.CS_AUTRES_FORTUNES_MOBILIERES.equals(csType)) {

            return new String[] { libelleType, objSession.getCodeLibelle(dsa.getCsTypeFortuneAutreFortuneMobiliere()),
                    dsa.getMontantAutreFortuneMobiliere() };
        }

        if (IPCDroits.CS_COMPTE_BANCAIRE_POSTAL.equals(csType)) {
            return new String[] { libelleType, dsa.getIbanCompteBancaireCCP(), dsa.getMontantCompteBancaireCCP() };
        }

        if (IPCDroits.CS_TITRES.equals(csType)) {
            return new String[] { libelleType,
                    objSession.getCodeLibelle(dsa.getCsGenreTitre()) + " - " + dsa.getDesignationTitre(),
                    dsa.getMontantTitre() };
        }

        if (IPCDroits.CS_PRETS_ENVERS_TIERS.equals(csType)) {
            return new String[] { libelleType, dsa.getDebiteurPretEnversTiers(), dsa.getMontantPretEnversTiers() };
        }

        if (IPCDroits.CS_CAPITAL_LPP.equals(csType)) {
            return new String[] { libelleType, dsa.getIPCapitalLPP(), dsa.getMontantCapitalLPP() };
        }

        if (IPCDroits.CS_ASSURANCE_VIE.equals(csType)) {
            AdministrationComplexModel tier = null;

            tier = TIBusinessServiceLocator.getAdministrationService().read(dsa.getNomCompagnieAssuranceVie());
            // AdministrationComplexModel
            return new String[] { libelleType,
                    tier.getTiers().getDesignation1() + " " + tier.getTiers().getDesignation2(),
                    dsa.getMontantValeurRachatAssuranceVie() };
        }

        if (IPCDroits.CS_ASSURANCE_RENTE_VIAGERE.equals(csType)) {
            return new String[] { libelleType, dsa.getCompagnieRenteViagere(), dsa.getMontantValeurRachatRenteViagere() };
        }

        if (IPCDroits.CS_MARCHANDISES_STOCK.equals(csType)) {
            return new String[] { libelleType, "", dsa.getMontantMarchandisesStock() };
        }

        if (IPCDroits.CS_BETAIL.equals(csType)) {
            return new String[] { libelleType, dsa.getDesignationBetail(), dsa.getMontantBetail() };
        }

        if (IPCDroits.CS_VEHICULE.equals(csType)) {
            return new String[] { libelleType, dsa.getDesignationVehicule(), dsa.getMontantVehicule() };
        }

        if (IPCDroits.CS_BIENS_IMMOBILIERS_SERVANT_HABITATION_PRINCIPALE.equals(csType)) {
            return new String[] { libelleType, objSession.getCodeLibelle(dsa.getTypeBienImmobilierPrincipal()),
                    dsa.getMontantBienImmobilierPrincipal() };
        }

        if (IPCDroits.CS_BIENS_IMMOBILIERS_HABITATION_NON_PRINCIPALE.equals(csType)) {
            return new String[] { libelleType, objSession.getCodeLibelle(dsa.getTypeBienImmobilierNonPrincipal()),
                    dsa.getMontantBienImmobilierNonPrincipal() };
        }

        if (IPCDroits.CS_BIENS_IMMOBILIERS_NON_HABITABLE.equals(csType)) {
            return new String[] { libelleType, objSession.getCodeLibelle(dsa.getTypeBienImmobilierNonHabitable()),
                    dsa.getMontantBienImmobilierNonHabitable() };
        }

        return null;
    }

    /**
     * Methode pour formatter la description d'un dessaisissement de revenu automatique
     * 
     * @param objSession
     *            Session en cours
     * @param dsa
     *            Entité de dessaisissement à formatter
     * @return Description formattée du dessaisissement
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public static String[] formatDessaisissementRevenuAutoDescription(BSession objSession, DessaisissementRevenuAuto dsa)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        String csType = dsa.getSimpleDonneeFinanciereHeader().getCsTypeDonneeFinanciere();
        String libelleType = objSession.getCodeLibelle(csType);
        String libelleCode = objSession.getCode(csType);

        if (JadeStringUtil.isEmpty(libelleType)) {
            libelleType = libelleCode;
        }

        String libelleInteret = objSession.getLabel("PC_HANDLER_DESSAISISSEMENT_INTERET_HYPOTHTIQUES") + libelleType;

        if (IPCDroits.CS_NUMERAIRES.equals(csType)) {
            return new String[] { libelleType, libelleInteret, /* dsa.getMontantNumeraire() */"", libelleCode };
        }

        if (IPCDroits.CS_COMPTE_BANCAIRE_POSTAL.equals(csType)) {
            return new String[] { libelleType, libelleInteret, /* dsa.getMontantCompteBancaireCCP() */"", libelleCode };
        }

        if (IPCDroits.CS_TITRES.equals(csType)) {
            return new String[] { libelleType, libelleInteret, /* dsa.getMontantTitre() */"", libelleCode };
        }

        if (IPCDroits.CS_PRETS_ENVERS_TIERS.equals(csType)) {
            return new String[] { libelleType, libelleInteret, /* dsa.getMontantPretEnversTiers() */"", libelleCode };
        }

        if (IPCDroits.CS_CAPITAL_LPP.equals(csType)) {
            return new String[] { libelleType, libelleInteret, /* dsa.getMontantCapitalLPP() */"", libelleCode };
        }

        if (IPCDroits.CS_ASSURANCE_VIE.equals(csType)) {
            return new String[] { libelleType, libelleInteret, /* dsa.getMontantValeurRachatAssuranceVie() */"",
                    libelleCode };
        }

        if (IPCDroits.CS_ASSURANCE_RENTE_VIAGERE.equals(csType)) {
            String text = objSession.getLabel("PC_HANDLER_DESSAISISSEMENT_EXCEDENTS");
            return new String[] { libelleType,
                    text + " " + (new FWCurrency(dsa.getExcedentRenteViagere()).toStringFormat()),
                    dsa.getMontantRenteViagere(), libelleCode };
        }

        if (IPCDroits.CS_MARCHANDISES_STOCK.equals(csType)) {
            return new String[] { libelleType, "", dsa.getMontantMarchandisesStock(), libelleCode };
        }

        if (IPCDroits.CS_BETAIL.equals(csType)) {
            return new String[] { libelleType, "", dsa.getMontantBetail(), libelleCode };
        }

        if (IPCDroits.CS_VEHICULE.equals(csType)) {
            return new String[] { libelleType, "", dsa.getMontantVehicule(), libelleCode };
        }

        if (IPCDroits.CS_BIENS_IMMOBILIERS_SERVANT_HABITATION_PRINCIPALE.equals(csType)) {
            return new String[] { libelleType, libelleInteret, /* dsa.getMontantBienImmobilierPrincipal() */"",
                    libelleCode };
        }

        if (IPCDroits.CS_BIENS_IMMOBILIERS_HABITATION_NON_PRINCIPALE.equals(csType)) {
            return new String[] { libelleType, libelleInteret, /* dsa.getMontantBienImmobilierNonPrincipal() */"",
                    libelleCode };
        }

        if (IPCDroits.CS_BIENS_IMMOBILIERS_NON_HABITABLE.equals(csType)) {
            return new String[] { libelleType, libelleInteret, /* dsa.getMontantBienImmobilierNonHabitable() */"",
                    libelleCode };
        }

        if (IPCDroits.CS_PENSIONS_ALIMENTAIRES.equals(csType)) {
            PersonneEtendueComplexModel personne = null;

            personne = TIBusinessServiceLocator.getPersonneEtendueService().read(dsa.getIdTiersPensionAlimentaire());

            return new String[] {
                    libelleType,
                    personne.getTiers().getDesignation1() + " " + personne.getTiers().getDesignation2() + " - "
                            + objSession.getCodeLibelle(dsa.getCsMotifPensionAlimentaire()),
                    dsa.getMontantPensionAlimentaire(), libelleCode };
        }

        if (IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_DEPENDANTE.equals(csType)) {
            return new String[] { libelleType, dsa.getNomEmployeurActiviteLucrativeDependante(),
                    dsa.getMontantActiviteLucrativeDependante(), libelleCode };
        }

        if (IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE.equals(csType)) {
            return new String[] { libelleType, "", dsa.getMontantActiviteLucrativeIndependante(), libelleCode };
        }

        if (IPCDroits.CS_CONTRAT_ENTRETIEN_VIAGER.equals(csType)) {
            String libelle = PCDessaisissementHandler.getListeLibelleEntretientViager(
                    dsa.getIdContratEntretienViager(), objSession);

            return new String[] { libelleType, libelle, dsa.getMontantContratEntretienViager(), libelleCode };
        }

        if (IPCDroits.CS_ALLOCATIONS_FAMILIALLES.equals(csType)) {
            return new String[] { libelleType, "", dsa.getMontantAllocationsFamiliales(), libelleCode };
        }

        if (IPCDroits.CS_AUTRES_RENTES.equals(csType)) {
            return new String[] {
                    libelleType,
                    objSession.getCodeLibelle(dsa.getCsGenreAutreRente()) + " - "
                            + objSession.getCodeLibelle(dsa.getCsTypeAutreRente()), dsa.getMontantAutreRente(),
                    libelleCode };
        }

        if (IPCDroits.CS_AUTRES_API.equals(csType)) {
            return new String[] {
                    libelleType,
                    objSession.getCodeLibelle(dsa.getCsGenreAutreApi()) + " - "
                            + objSession.getCodeLibelle(dsa.getCsTypeAutreApi()), dsa.getMontantAutreRente(),
                    libelleCode };
        }
        // TODO autres IJ/APG
        if (IPCDroits.CS_INDEMNITES_JOURNLIERES_APG.equals(csType)) {
            String libelleAutre = dsa.getAutreGenreAutreIjApg();
            if (!JadeStringUtil.isEmpty(libelleAutre)) {
                libelleAutre = "- " + libelleAutre;
            }
            return new String[] { libelleType,
                    objSession.getCodeLibelle(dsa.getCsGenreAutreIjApg()) + " " + libelleAutre,
                    dsa.getMontantAutreRente(), libelleCode };
        }

        // TODO IJAI
        /*
         * if (IPCDroits.CS_IJAI.equals(csType)) { return new String[] { libelleType,
         * objSession.getCodeLibelle(dsa.getCsTypeIjai()), dsa.getMontantIjAi(), libelleCode }; }
         */

        if (IPCDroits.CS_AUTRES_REVENUS.equals(csType)) {
            return new String[] { libelleType, dsa.getLibelleAutresRevenus(), dsa.getMontantAutresRevenus(),
                    libelleCode };
        }

        if (IPCApiAvsAi.CS_TYPE_DONNEE_FINANCIERE.equals(csType)) {
            return new String[] {
                    libelleType,
                    objSession.getCodeLibelle(dsa.getCsGenreAllocationImpotent()) + " "
                            + objSession.getCode(dsa.getCsTypeRenteAllocationImpotent()),
                    dsa.getMontantAllocationImpotent(), libelleCode };
        }

        // TODO IJLCA
        if (IPCDroits.CS_TAXE_JOURN_HOME.equals(csType)) {
            TiersSimpleModel tier = PegasusServiceLocator.getHomeService().read(dsa.getIdHomeTaxeJournaliereHome())
                    .getAdresse().getTiers();
            return new String[] { libelleType, tier.getDesignation1() + " " + tier.getDesignation2(),
                    dsa.getMontantJournaliereHome(), libelleCode };
        }

        return null;
    }

    private static String getListeLibelleEntretientViager(String idContratEntretienViager, BSession objSession)
            throws DroitException, JadeApplicationServiceNotAvailableException, JadePersistenceException {

        SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager = null;
        SimpleLibelleContratEntretienViagerSearch searchLibelle = new SimpleLibelleContratEntretienViagerSearch();
        searchLibelle.setForIdContratEntretienViager(idContratEntretienViager);
        searchLibelle = PegasusServiceLocator.getDroitService()
                .searchSimpleLibelleContratEntretienViager(searchLibelle);

        String libelle = "";
        for (JadeAbstractModel model : searchLibelle.getSearchResults()) {
            if (!JadeStringUtil.isEmpty(libelle)) {
                libelle = libelle + " - ";
            }
            simpleLibelleContratEntretienViager = (SimpleLibelleContratEntretienViager) model;
            libelle = libelle
                    + objSession.getCodeLibelle(simpleLibelleContratEntretienViager
                            .getCsLibelleContratEntretienViager());

        }

        return libelle;
    }

    /**
     * Test si il y le cacul de contre prestation à executer.
     * 
     * On fait le calcule seulement si il y une contre préstation et que le type de dessaisissement est donnation
     * 
     * @param DessaisissementFortune
     *            dessaisissementFortune en cours
     * @return boolean
     **/
    public static boolean hasCalculToDo(DessaisissementFortune dsf) {
        return dsf.getSimpleDessaisissementFortune().getIsContrePrestation()
                && IPCDroits.CS_MOTIF_DESSAISI_DONATION.equals(dsf.getSimpleDessaisissementFortune()
                        .getCsMotifDessaisissement());
    }

}
