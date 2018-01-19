package globaz.corvus.db.deblocage;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.lignedeblocage.RELigneDeblocages;
import globaz.corvus.db.lignedeblocageventilation.RELigneDeblocageVentilation;
import globaz.corvus.db.lignedeblocageventilation.RELigneDeblocageVentilationServices;
import globaz.corvus.db.lots.RELot;
import globaz.corvus.db.lots.RELotManager;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.externe.IPRConstantesExternes;
import globaz.globall.db.BSession;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.db.comptes.CASectionJoinCompteAnnexeJoinTiers;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;

/**
 * @author ebko
 * 
 */
public class RELibererDevaliderDeblocage {
    private BSession session;
    private RELigneDeblocageVentilationServices ventilationService;
    private REDeblocageService deblocageService;

    public RELibererDevaliderDeblocage(BSession session) {
        this.session = session;
        ventilationService = new RELigneDeblocageVentilationServices(session);
        deblocageService = new REDeblocageService(session);
    }

    public void devalider(Long idRenteAccordee) {
        REDeblocage deblocage = deblocageService.read(idRenteAccordee);

        Montant montant = deblocage.getMontantDebloquer().substract(
                deblocage.getLignesDeblocages().filtreValides().sumMontants());

        changeMontantDebloque(deblocage.getEnteteBlocage(), montant);

        RELot lot = findLot();
        Long idLot = Long.valueOf(lot.getIdLot());

        RELigneDeblocages lignes = deblocage.getLignesDeblocages().filtreValides().filtreByIdLot(idLot);
        lignes.changeEtatToEnregistre().changeIdLot(null);

        deblocageService.update(lignes);

        ventilationService.delete(ventilationService.searchByIdsLigneDeblocage(lignes.getIdsLigne()));

        deleteLotIfNeeded(lot, idLot);
    }

    public void liberer(Long idRenteAccordee) {
        REDeblocage deblocage = deblocageService.read(idRenteAccordee);

        // On calcule sur les lignes qui sont dans l'�tat enregistr�
        changeMontantDebloque(deblocage.getEnteteBlocage(), deblocage.computTotalMontantDebloquer());

        RELot lot = findLotOrCreate();
        Long idLot = Long.valueOf(lot.getIdLot());
        RELigneDeblocages deblocagesEnregistre = deblocage.getLignesDeblocages().filtreEnregistres();
        RELigneDeblocages deblocagesValide = deblocage.getLignesDeblocages().filtreValides();
        updateSectionsSolde(deblocage, deblocagesValide);
        Long idApplication = Long.valueOf(IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE);
        Long idTiersAdressePaiement = Long.valueOf(deblocage.getPracc().getIdTiersAdressePmt());

        deblocagesEnregistre.changeEtatToValide().changeIdLot(idLot);

        deblocagesEnregistre.getLigneDeblocageVersementBeneficiaire().changeIdTiersAdressePaiementAndApplication(
                idTiersAdressePaiement, idApplication, deblocage.getPracc().getIdCompteAnnexe());

        deblocagesEnregistre.getLigneDeblocageImpotsSource().changeSection(
                Long.valueOf(APIReferenceRubrique.IMPOT_A_LA_SOURCE));

        RELigneDeblocageVentilator ventilator = new RELigneDeblocageVentilator(deblocagesEnregistre,
                deblocage.getSections(), deblocage.getPracc().getIdCompteAnnexe());

        ventilationService.save(ventilator.ventil());
        deblocageService.update(deblocagesEnregistre);
    }

    private RELot findLotOrCreate() {
        RELot lot = findLot();
        if (lot == null) {
            lot = new RELot();
            lot.setSession(session);
            lot.setCsEtatLot(IRELot.CS_ETAT_LOT_OUVERT);
            lot.setCsTypeLot(IRELot.CS_TYP_LOT_DEBLOCAGE_RA);
            lot.setCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
            lot.setDateCreationLot(Date.now().getSwissValue());
            lot.setDescription(session.getLabel("DESCRIPTION_LOT_DEBLOCAGE") + " "
                    + (new Date()).getMoisAnneeFormatte());
            try {
                lot.add();
            } catch (Exception e) {
                throw new REDeblocageException("Unable to create the lot", e);
            }
        }
        return lot;
    }

    private void deleteLotIfNeeded(RELot lot, Long idLot) {
        Integer nb = deblocageService.countLignesDeblocageByIdlot(idLot);
        if (nb == 0) {
            try {
                lot.setSession(session);
                lot.delete();
            } catch (Exception e) {
                throw new REDeblocageException("Unable to delete the lot", e);
            }
        }
    }

    private RELot findLot() {
        RELotManager lotMgr = new RELotManager();
        lotMgr.setSession(session);
        lotMgr.setForCsType(IRELot.CS_TYP_LOT_DEBLOCAGE_RA);
        lotMgr.setForCsLotOwner(IRELot.CS_LOT_OWNER_RENTES);
        lotMgr.setForCsEtat(IRELot.CS_ETAT_LOT_OUVERT);

        try {
            lotMgr.find(1);
            RELot lot = (RELot) lotMgr.getFirstEntity();
            if (lot != null) {
                // pour charger les spy
                lot.retrieve();
            }
            return (RELot) lotMgr.getFirstEntity();
        } catch (Exception e) {
            throw new REDeblocageException("Unable to load the lot", e);
        }
    }

    private void changeMontantDebloque(REEnteteBlocage enteteBlocage, Montant montant) {
        enteteBlocage.setMontantDebloque(montant.toStringValue());
        enteteBlocage.setSession(session);
        try {
            enteteBlocage.update();
        } catch (Exception e) {
            throw new REDeblocageException("Unable to update the entete: " + enteteBlocage, e);
        }
    }

    /**
     * Mets � jour le montant des sections avec les ventilations d�j� valid�es
     */
    private void updateSectionsSolde(REDeblocage deblocage, RELigneDeblocages deblocagesValide) {
        if (deblocagesValide.isEmpty()) {
            return;
        }
        List<RELigneDeblocageVentilation> ventilations = ventilationService.searchByIdsLigneDeblocage(deblocagesValide
                .getIdsLigne());
        for (RELigneDeblocageVentilation ventil : ventilations) {
            for (CASectionJoinCompteAnnexeJoinTiers section : deblocage.getSections()) {
                if (section.getIdSection().equals(ventil.getIdSectionSource().toString())) {
                    // ajout du montant sur solde n�gatif = soustraction
                    section.setSolde(new Montant(section.getSolde()).add(ventil.getMontant()).toStringValue());
                }
            }
        }

    }
}
