package ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement;

import globaz.globall.util.JAException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.osiris.api.APIEcriture;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.Ecriture;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.OrdreVersementCompta;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.TypeEcriture;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationData;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationHandler;

public class ComputedOvGenerate {

    public final static String PAIEMENT = "_PAIEMENT";
    public SimpleLot lot = null;

    public Map<String, OrdreVersementWrapper> findComputedOvByIdPrestation(String idPrestation)
            throws JadePersistenceException, JadeApplicationException, JAException {
        ComptabilisationData data = generateOperations(idPrestation);
        lot = data.getSimpleLot();
        return groupByCsTypOvAndGenerateOv(data);
    }

    protected ComptabilisationData generateOperations(String idPrestation) throws JadePersistenceException,
            JadeApplicationException, JAException {
        return ComptabilisationHandler.generateEcritureForOnePresation(idPrestation);
    }

    public SimpleLot getLot() {
        return lot;
    }

    private Map<String, OrdreVersementWrapper> groupByCsTypOvAndGenerateOv(ComptabilisationData data) {
        Map<String, OrdreVersementWrapper> map = new HashMap<String, OrdreVersementWrapper>();
        for (Ecriture e : data.getJournalConteneur().getOperations().get(0).getEcritures()) {
            if (isEcritureToDisplay(e)) {
                if (!map.containsKey(e.getCsTypeOv())) {
                    map.put(e.getCsTypeOv(), new OrdreVersementWrapper());
                }
                OrdreVersementDisplay ov = this.newOv(data, e);
                map.get(e.getCsTypeOv()).addOv(ov);
            }
        }

        for (OrdreVersementCompta ovc : data.getJournalConteneur().getOperations().get(0).getOrdresVersements()) {
            String key = ovc.getCsTypeOv() + ComputedOvGenerate.PAIEMENT;
            if (!map.containsKey(key)) {
                map.put(key, new OrdreVersementWrapper());
            }
            OrdreVersementDisplay ov = this.newOv(data, ovc);
            map.get(key).addOv(ov);

        }
        return map;
    }

    private boolean isAllocationNoel(Ecriture e) {
        return TypeEcriture.ALLOCATION_NOEL.equals(e.getTypeEcriture());
    }

    private boolean isCreancier(Ecriture e) {
        return TypeEcriture.CREANCIER.equals(e.getTypeEcriture());
    }

    private boolean isDette(Ecriture e) {
        return TypeEcriture.DETTE.equals(e.getTypeEcriture());
    }

    private boolean isEcritureToDisplay(Ecriture e) {
        return e.getTypeEcriture().equals(TypeEcriture.STANDARD)
                || (isDette(e) && e.getCodeDebitCredit().equals(APIEcriture.CREDIT)) || isCreancier(e)
                || isAllocationNoel(e);
    }

    private OrdreVersementDisplay newOv(ComptabilisationData data, Ecriture e) {
        if (isDette(e)) {
            return new OrdreversmentDetteDisplay(e.getSectionSimple().getId(), e.getMontant(), e.getCsTypeOv());
        } else {
            return new OrdreVersementStandardDisplay(e.getIdDomaineApplication(), e.getIdTiersAdressePaiement(),
                    e.getMontant(), data.getMapIdTierDescription().get(e.getIdTiersOv()), e.getId(), e.getCsTypeOv(),
                    e.getIdRefRubrique(), e.isDom2R());
        }

    }

    private OrdreVersementDisplay newOv(ComptabilisationData data, OrdreVersementCompta ovc) {
        return new OrdreVersementStandardDisplay(ovc.getIdDomaineApplication(), ovc.getIdTiersAdressePaiement(),
                ovc.getMontant(), data.getMapIdTierDescription().get(ovc.getIdTiers()), null, ovc.getCsTypeOv(), null,
                false);
    }

}
