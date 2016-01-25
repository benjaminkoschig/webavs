package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import globaz.corvus.api.lots.IRELot;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import java.util.ArrayList;
import ch.globaz.pegasus.business.exceptions.models.lot.ComptabiliserLotException;

class ComptabilisationChecker {

    private ComptabilisationData data;
    private static final ArrayList<String> CS_TYPE_LOT_PC = new ArrayList<String>();

    static {
        CS_TYPE_LOT_PC.add(IRELot.CS_TYP_LOT_DEBLOCAGE_RA);
        CS_TYPE_LOT_PC.add(IRELot.CS_TYP_LOT_DECISION);
        CS_TYPE_LOT_PC.add(IRELot.CS_TYP_LOT_DECISION_RESTITUTION);
    }

    public ComptabilisationChecker(ComptabilisationData data) {
        this.data = data;
    }

    public void check() throws ComptabiliserLotException, JAException {
        checkIsComptabilisable();
        checkDateValeurInSameMonth();
        checkEtatLotDifferentDeValidee();
        checkDateEcheanceIsAffterDateValeur();
    }

    private void checkDateEcheanceIsAffterDateValeur() throws ComptabiliserLotException {
        String dateEcheance = data.getDateEchance().toStr(".");
        String dateValeur = data.getDateValeur().toStr(".");
        if (!JadeDateUtil.isDateAfter(dateEcheance, dateValeur)
                && !JadeDateUtil.areDatesEquals(dateEcheance, dateValeur)) {
            throw new ComptabiliserLotException("La date Echeance (" + dateEcheance
                    + ") ne peut pas être avant la date de valeur comptable (" + dateValeur + ")!");
        }
    }

    private void checkDateValeurInSameMonth() throws ComptabiliserLotException, JAException {
        if ((data.getDateValeur().getMonth() != data.getDateDernierPmt().getMonth())
                || (data.getDateValeur().getYear() != data.getDateDernierPmt().getYear())) {
            throw new ComptabiliserLotException(
                    "La date de valeur comptable doit être dans le même mois que le dernier pmt mensuel.");
        }
    }

    private void checkEtatLotDifferentDeValidee() throws ComptabiliserLotException {
        if (IRELot.CS_ETAT_LOT_VALIDE.equals(data.getSimpleLot().getCsEtat())) {
            throw new ComptabiliserLotException("Impossible de valider un lot déja vlidé");
        }
    }

    private void checkIsComptabilisable() throws ComptabiliserLotException {
        if (!(IRELot.CS_ETAT_LOT_OUVERT.equals(data.getSimpleLot().getCsEtat())
                && isTypeLotPc(data.getSimpleLot().getCsTypeLot()) && IRELot.CS_LOT_OWNER_PC.endsWith(data
                .getSimpleLot().getCsProprietaire()))) {
            throw new ComptabiliserLotException(
                    getMessage("pegasus.lot.comptablisation.comptabilisation.dejasComptabilise"));
        }
    }

    /**
     * Test si le cs type présent dans le lot <code>SimpleLot<code> est bien un type défini pour les pc 
     * Se réfère à la liste statique déinie pour la classe. <code>CS_TYPE_LOT_PC</code>
     * 
     * @return
     */
    private static boolean isTypeLotPc(String csTypeToTest) {
        return (CS_TYPE_LOT_PC.contains(csTypeToTest));
    }

    public String getMessage(String msg) {
        return JadeThread.getMessage(msg);
    }

}
