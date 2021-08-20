/**
 *
 */
package globaz.pegasus.vb.droit;

// globaz.pegasus.vb.droit.PCCorrigerDroitViewBean

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneeFinanciereType;
import ch.globaz.pegasus.business.domaine.donneeFinanciere.DonneesFinancieresContainer;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.MembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.models.pcaccordee.*;
import ch.globaz.pegasus.business.models.revisionquadriennale.DonneeFinanciereComplexModel;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.donneeFinanciere.DonneeFinanciereLoader;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.PcaPlanCalculReforme;
import ch.globaz.pegasus.businessimpl.utils.calcul.IPeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere.UtilFortune;
import ch.globaz.pegasus.businessimpl.utils.plancalcul.PCPlanCalculHandler;
import com.google.common.collect.Sets;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.*;

/**
 * @author DMA
 *
 */
public class PCCorrigerDroitViewBean extends BJadePersistentObjectViewBean {
    private String dateSuppression = null;
    private String csMotif = null;
    private String dateAnnonce = null;
    private Droit droit = null;
    private String idDroit = null;
    private String dateDecision = null;
    private String mailProcessCompta = getSession().getUserEMail();
    private boolean isComptabilisationAuto = false;

    public PCCorrigerDroitViewBean() {
        super();
        droit = new Droit();
        dateAnnonce = "";
        dateSuppression = "";
        dateDecision = "";
    }

    /**
     * @param droit
     */
    public PCCorrigerDroitViewBean(Droit droit) {
        super();
        this.droit = droit;
    }

    @Override
    public void add() throws Exception {

    }

    @Override
    public void delete() throws Exception {
    }

    public String getCsMotif() {
        if (!JadeStringUtil.isBlankOrZero(csMotif)) {
            return csMotif;
        } else {
            return IPCDroits.CS_MOTIF_DROIT_MODIFICATIONS_DIVERSES;
        }

    }

    /**
     * @return the dateAnnonce
     */
    public String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * @return the droit
     */
    public Droit getDroit() {
        return droit;
    }

    @Override
    public String getId() {
        return droit.getId();
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(droit.getSpy());
    }

    @Override
    public void retrieve() throws Exception {
        droit = PegasusServiceLocator.getDroitService().getCurrentVersionDroit(idDroit);
    }

    public void setCsMotif(String csMotif) {
        this.csMotif = csMotif;
    }

    /**
     * @param dateAnnonce
     *            the dateAnnonce to set
     */
    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    /**
     * @param droit
     *            the droit to set
     */
    public void setDroit(Droit droit) {
        this.droit = droit;
    }

    @Override
    public void setId(String newId) {
        idDroit = newId;
        droit.setId(newId);
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public String getDateSuppression() {
        return dateSuppression;
    }

    public void setDateSuppression(String dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    public String getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    public String getCurrentUserId() {
        return getSession().getUserId();
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public void update() throws Exception {
        retrieve();
        if (IPCDroits.CS_MOTIF_DROIT_DECES.equals(csMotif)) {
            droit = PegasusServiceLocator.getDroitService().corrigerDroitEnCasDeDeces(droit, dateAnnonce, csMotif,
                    dateSuppression, dateDecision, getCurrentUserId(), isComptabilisationAuto(), mailProcessCompta);
        } else {
            droit = PegasusServiceLocator.getDroitService().corrigerDroit(droit, dateAnnonce, csMotif);
        }

    }

    public boolean isComptabilisationAuto() {
        return isComptabilisationAuto;
    }

    public boolean getIsComptabilisationAuto() {
        return isComptabilisationAuto;
    }

    public void setIsComptabilisationAuto(boolean isComptabilisationAuto) {
        this.isComptabilisationAuto = isComptabilisationAuto;
    }

    public String getMailProcessCompta() {
        return mailProcessCompta;
    }

    public void setMailProcessCompta(String mailProcessCompta) {
        this.mailProcessCompta = mailProcessCompta;
    }

    public boolean hasControleForturne() throws Exception {

        MembreFamilleEtenduSearch search = new MembreFamilleEtenduSearch();
        search.setForIdDroit(droit.getId());
        MembreFamilleEtenduSearch membreSearch = new MembreFamilleEtenduSearch();
        membreSearch.setForIdDroit(idDroit);
        membreSearch.setOrderKey("orderByRole");
        membreSearch = PegasusServiceLocator.getDroitService().searchMembreFamilleEtendu(membreSearch);
        if (membreSearch.getSize() == 1) {
            MembreFamilleEtendu membreFamilleEtendu = (MembreFamilleEtendu) membreSearch.getSearchResults()[0];
            List<PCAccordeePlanCalculReforme> listPcaPrecedentes = PcaPlanCalculReforme.findPcaCourranteWithDateDebutDesc(droit.getId(), droit.getSimpleVersionDroit().getNoVersion());
            PCAccordeePlanCalculReforme pcaCourrant = listPcaPrecedentes.get(0);
            String idBeneficiaire = membreFamilleEtendu.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getId();
            PCPlanCalculHandler planCalculHandler = PCPlanCalculHandler.getHandlerForIdPlanCalcul(getISession(), pcaCourrant.getIdPlanDeCalcul(), idBeneficiaire);
            SimplePlanDeCalcul planDeCalcul = planCalculHandler.getPlanDeCalcul();
            String byteArrayToString = null;
            byteArrayToString = new String(planDeCalcul.getResultatCalcul());
            if(JadeStringUtil.isBlankOrZero(byteArrayToString)){
                return false;
            }
            TupleDonneeRapport tupleRoot = PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(
                    byteArrayToString);
            return UtilFortune.isRefusFortunePopUp(tupleRoot);


//            Map<String, DonneesFinancieresContainer> mapDonneesFinancieres = DonneeFinanciereLoader
//                    .loadByIdsVersionDroitAndGroupByIdVersionDroit(Sets.newHashSet(droit.getSimpleVersionDroit().getIdVersionDroit())
//                            , DonneeFinanciereType.COMPTE_BANCAIRE_POSTAL, DonneeFinanciereType.TITRE
//                            , DonneeFinanciereType.ASSURANCE_VIE, DonneeFinanciereType.CAPITAL_LPP, DonneeFinanciereType.BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE
//                            , DonneeFinanciereType.BIEN_IMMOBILIER_HABITATION_NON_PRINCIPALE, DonneeFinanciereType.BIEN_IMMOBILIER_NON_HABITABLE,
//                            DonneeFinanciereType.PRET_ENVERS_TIERS, DonneeFinanciereType.ASSURANCE_RENTE_VIAGERE, DonneeFinanciereType.NUMERAIRE,
//                            DonneeFinanciereType.MARCHANDISE_STOCK, DonneeFinanciereType.BETAIL, DonneeFinanciereType.AUTRE_FORTUNE_MOBILIERE, DonneeFinanciereType.VEHICULE);
//            Montant totalFortune = Montant.ZERO;
//            for (DonneesFinancieresContainer donnee : mapDonneesFinancieres.values()) {
//                totalFortune = totalFortune.add(donnee.getComptesBancairePostal().sumFortune());
//                totalFortune = totalFortune.add(donnee.getTitres().sumFortune());
//                totalFortune = totalFortune.add(donnee.getAssurancesVie().sumFortune());
//                totalFortune = totalFortune.add(donnee.getCapitalsLpp().sumFortune());
//                totalFortune = totalFortune.add(donnee.getBiensImmobiliersServantHbitationPrincipale().sumFortune());
//                totalFortune = totalFortune.add(donnee.getBiensImmobiliersNonPrincipale().sumFortune());
//                totalFortune = totalFortune.add(donnee.getBiensImmobiliersNonHabitable().sumFortune());
//
//                totalFortune = totalFortune.add(donnee.getPretsEnversTiers().sumFortune());
//                totalFortune = totalFortune.add(donnee.getAssurancesRentesViageres().sumFortune());
//                totalFortune = totalFortune.add(donnee.getNumeraires().sumFortune());
//                totalFortune = totalFortune.add(donnee.getMarchandisesStocks().sumFortune());
//                totalFortune = totalFortune.add(donnee.getVehicules().sumFortune());
//                totalFortune = totalFortune.add(donnee.getBetails().sumFortune());
//                totalFortune = totalFortune.add(donnee.getAutresFortunesMobilieres().sumFortune());
//            }
//            if(totalFortune.greater(new Montant(40000))){
//                return true;
//            }

        }

        return false;
    }

}
