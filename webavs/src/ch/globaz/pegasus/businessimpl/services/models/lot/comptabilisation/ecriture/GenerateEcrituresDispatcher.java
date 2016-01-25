package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import ch.globaz.osiris.business.model.CompteAnnexeSimpleModel;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ComptabilisationUtil;

abstract class GenerateEcrituresDispatcher extends GenerateOperationBasic {
    protected CompteAnnexeSimpleModel compteAnnexeConjoint;
    protected CompteAnnexeSimpleModel compteAnnexeRequerant;
    protected List<Ecriture> ecritures = new ArrayList<Ecriture>();
    private String idTiersConjoint;
    private String idTiersRequerant;
    private MontantAdisposition<OrdreVersement> montantDisponibleConjoint = new MontantAdisposition<OrdreVersement>();
    protected MontantAdisposition<OrdreVersement> montantDisponibleRequerant = new MontantAdisposition<OrdreVersement>();
    protected List<OrdreVersement> ovs;

    public GenerateEcrituresDispatcher(PrestationOvDecompte decompte, MontantDispo montantDispo,
            List<OrdreVersement> ovs) throws JadeApplicationException {
        compteAnnexeRequerant = decompte.getCompteAnnexeRequerant();
        idTiersRequerant = decompte.getIdTiersRequerant();
        idTiersConjoint = decompte.getIdTiersConjoint();
        compteAnnexeConjoint = decompte.getCompteAnnexeConjoint();
        this.ovs = ovs;
        montantDisponibleRequerant.addMontantDispo(montantDispo.getStandardRequerant());
        montantDisponibleRequerant.addMontantDispoDom2R(montantDispo.getDom2RRequerant());
        montantDisponibleConjoint.addMontantDispo(montantDispo.getStandarConjoint());
        montantDisponibleConjoint.addMontantDispoDom2R(montantDispo.getDom2RConjoint());
    }

    protected abstract void addOperation(OrdreVersement ov, BigDecimal montant, CompteAnnexeSimpleModel compteAnnexe)
            throws ComptabiliserLotException;

    protected void generateAllOperations() throws JadeApplicationException {
        for (OrdreVersement ov : ovs) {
            this.generateEcriture(ov);
        }
        this.generateEcrituresEntrePersonne();
    }

    private void generateEcriture(OrdreVersement ov) throws ComptabiliserLotException {
        BigDecimal montant = ov.getMontant();// this.resolveMontantOv(ov.getSimpleOrdreVersement());
        if (isOwnerSpecified(ov) && !isOwnerEnfant(ov)) {
            if (isOwnerRequerant(ov)) {
                this.generateEcriture(ov, montant, montantDisponibleRequerant, compteAnnexeRequerant);
            } else {
                this.generateEcriture(ov, montant, montantDisponibleConjoint, compteAnnexeConjoint);
            }
        } else {

            if (hasConjoint()) {
                BigDecimal[] montants = ComptabilisationUtil.splitMontant(montant);
                this.generateEcriture(ov, montants[0], montantDisponibleRequerant, compteAnnexeRequerant);
                this.generateEcriture(ov, montants[1], montantDisponibleConjoint, compteAnnexeConjoint);
            } else {
                this.generateEcriture(ov, montant, montantDisponibleRequerant, compteAnnexeRequerant);
            }
        }
    }

    private void generateEcriture(OrdreVersement ov, BigDecimal montant,
            MontantAdisposition<OrdreVersement> montantDisp, CompteAnnexeSimpleModel compteAnnexe)
            throws ComptabiliserLotException {
        BigDecimal montantMin;
        BigDecimal montantNonRembourse = montant;
        if (montantDisp.hasMontantAdispositionDom2R()) {
            montantMin = montantDisp.getMontantAdispositionDom2R().min(montantNonRembourse);
            montantDisp.substractMontantAdispositionDom2R(montantMin);
            montantNonRembourse = montant.subtract(montantMin);
            addOperation(ov, montantMin, compteAnnexeRequerant);
        }
        if (montantDisp.hasMontantAdisposition() & (montantNonRembourse.signum() == 1)) {
            montantMin = montantDisp.getMontantAdisposition().min(montantNonRembourse);
            montantDisp.substractMontantAdisposition(montantMin);
            montantNonRembourse = montantNonRembourse.subtract(montantMin);
            addOperation(ov, montantMin, compteAnnexe);
        }
        montantDisp.getMapMontantNonRemboursee().remove(ov);
        if (montantNonRembourse.signum() == 1) {
            montantDisp.getMapMontantNonRemboursee().put(ov, montantNonRembourse);
        }
    }

    private void generateEcrituresEntrePersonne() throws ComptabiliserLotException {
        this.generateEcrituresEntrePersonne(montantDisponibleRequerant, montantDisponibleConjoint, compteAnnexeConjoint);
        this.generateEcrituresEntrePersonne(montantDisponibleConjoint, montantDisponibleRequerant,
                compteAnnexeRequerant);
    }

    private void generateEcrituresEntrePersonne(MontantAdisposition<OrdreVersement> map1,
            MontantAdisposition<OrdreVersement> map2, CompteAnnexeSimpleModel compteAnnexe)
            throws ComptabiliserLotException {
        for (Entry<OrdreVersement, BigDecimal> entry : map1.getMapMontantNonRemboursee().entrySet()) {
            this.generateEcriture(entry.getKey(), entry.getValue(), map2, compteAnnexe);
        }
    }

    public List<Ecriture> getEcritures() {
        return ecritures;
    }

    protected BigDecimal getMontantOv(SimpleOrdreVersement ov) {
        return new BigDecimal(ov.getMontant());
    }

    public MontantDispo getMontantsDisponible() {
        MontantDispo montantDispo = new MontantDispo(montantDisponibleRequerant.getMontantAdispositionDom2R(),
                montantDisponibleConjoint.getMontantAdispositionDom2R(),
                montantDisponibleRequerant.getMontantAdisposition(), montantDisponibleConjoint.getMontantAdisposition());
        return montantDispo;

    }

    protected boolean hasConjoint() {
        if ((compteAnnexeConjoint != null) && !JadeStringUtil.isBlankOrZero(compteAnnexeConjoint.getIdCompteAnnexe())) {
            return true;
        }
        if (montantDisponibleConjoint.hasMontantAdispositionDom2R()) {
            return true;
        }

        return false;
    }

    private boolean isOwnerConjoint(OrdreVersement ov) {
        return ov.getIdTiersOwnerDetteCreance().equals(idTiersConjoint);
    }

    private boolean isOwnerEnfant(OrdreVersement ov) {
        if (isOwnerRequerant(ov)) {
            return false;
        } else if (isOwnerConjoint(ov)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isOwnerRequerant(OrdreVersement ov) {
        return ov.getIdTiersOwnerDetteCreance().equals(idTiersRequerant);
    }

    private boolean isOwnerSpecified(OrdreVersement ov) {
        return !JadeStringUtil.isBlankOrZero(ov.getIdTiersOwnerDetteCreance());
    }

    protected abstract BigDecimal resolveMontantOv(SimpleOrdreVersement ov);
}
