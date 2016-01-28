package ch.globaz.al.businessimpl.generation.prestations;

import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

/**
 * Classe de g�n�ration de prestation avec montant forc� au niveau de la g�n�ration manuelle. Lorsqu'un montant est
 * forc�, il est r�parti dans les m�mes proportions que si un calcul standard �tait effectu�.
 * 
 * @author jts
 */
public class GenPrestationForcee extends GenPrestationAbstract {
    /**
     * Montant par mois. R�sultat de la division du montant forc� par le nombre de mois � g�n�rer
     */
    protected ArrayList<BigDecimal> montantsParMois = null;

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations n�cessaires � la g�n�ration
     */
    public GenPrestationForcee(ContextAffilie context) {
        super(context);
    }

    @Override
    public void execute() throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationForcee#execute : context is null");
        }

        setMontantParMois();
        super.execute();
    }

    @Override
    protected void generatePrestation(ContextAffilie context, ArrayList<CalculBusinessModel> calcul)
            throws JadeApplicationException, JadePersistenceException {

        if (context == null) {
            throw new ALGenerationException("GenPrestationForcee#process : context is null");
        }

        if (calcul == null) {
            throw new ALGenerationException("GenPrestationForcee#process : calcul is null");
        }

        if (JadeNumericUtil.isEmptyOrZero(context.getContextDossier().getMontantForce())) {
            throw new ALGenerationException("GenPrestationForcee#process : montantForce is empty or zero");
        }

        if (calcul.size() > 0) {

            // ex�cution de la r�partition du montant
            ArrayList<String> repartition = repartirMontant(String.valueOf(getNextMontant()), calcul, context
                    .getContextDossier().getDossier().getDossierModel(), "01."
                    + context.getContextDossier().getCurrentPeriode());

            for (int i = 0; i < calcul.size(); i++) {

                CalculBusinessModel droitCalcule = calcul.get(i);
                String montant = repartition.get(i);

                if (!JadeNumericUtil.isZeroValue(montant)) {
                    droitCalcule.setCalculResultMontantEffectif(montant);
                    droitCalcule.setTarifForce(false);
                    droitCalcule.setTarif(droitCalcule.getTarif());
                    addDetailPrestation(context, droitCalcule);
                }
            }
        }
    }

    /**
     * Retourne le prochain montant � traiter
     * 
     * @return le prochain montant � traiter
     * 
     */
    protected BigDecimal getNextMontant() {
        BigDecimal next = montantsParMois.get(0);
        montantsParMois.remove(0);
        return next;
    }

    /**
     * R�partit le <code>montant</code> dans les m�me proportions que le r�sultat du calcul <code>droitsCalcules</code>
     * 
     * @param montant
     *            montant � r�partir
     * @param droitsCalcules
     *            r�sultat d'un calcul
     * @param dossier
     *            Dossier auquel correspond le calcul
     * @param date
     *            Date pour laquelle le calcul a �t� effectu�e
     * @return Montants r�partis
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected ArrayList<String> repartirMontant(String montant, ArrayList<CalculBusinessModel> droitsCalcules,
            DossierModel dossier, String date) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(montant)) {
            throw new ALCalculException("GenPrestationForcee#repartirMontant : montant is empty or zero");
        }

        if ((droitsCalcules == null) || (droitsCalcules.size() == 0)) {
            throw new ALCalculException("GenPrestationForcee#repartirMontant : droitsCalcules is null or empty");
        }

        if ((dossier == null)) {
            throw new ALCalculException("GenPrestationForcee#repartirMontant : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("GenPrestationForcee#repartirMontant : " + date + " is not a valid date");
        }

        ArrayList<String> repartition = new ArrayList<String>();
        BigDecimal montantTotal = new BigDecimal((String) ALImplServiceLocator.getCalculMontantsService()
                .calculerTotalMontant(dossier, droitsCalcules, dossier.getUniteCalcul(), "1", false, date)
                .get(ALConstCalcul.TOTAL_EFFECTIF));
        BigDecimal totalBrut = new BigDecimal("0.00");

        for (int i = 0; i < droitsCalcules.size(); i++) {

            BigDecimal split = new BigDecimal(montant).multiply(new BigDecimal((droitsCalcules.get(i))
                    .getCalculResultMontantEffectif()).divide(montantTotal, 10, ALConstCalcul.ROUNDING_MODE));

            split = JANumberFormatter.round(split, ALConstCalcul.PRECISION_001, 2, JANumberFormatter.NEAR);
            totalBrut = totalBrut.add(split);
            repartition.add(split.toPlainString());
        }

        BigDecimal residu = totalBrut.subtract(new BigDecimal(montant));

        if (!residu.equals("0")) {

            BigDecimal newMontPrest = new BigDecimal(repartition.remove(0)).subtract(residu);
            repartition.add(0, newMontPrest.toPlainString());
        }

        return repartition;
    }

    /**
     * D�termine le montant par mois en fonction du montant forc� et du nombre de mois � g�n�rer
     */
    protected void setMontantParMois() {

        int nbMois = ALDateUtils.getNbMonthsBetween("01." + context.getContextDossier().getDebutPeriode(), "01."
                + context.getContextDossier().getFinPeriode());

        BigDecimal montant = new BigDecimal(context.getContextDossier().getMontantForce());

        BigDecimal montantTmp = montant.divide(new BigDecimal(nbMois), 2, ALConstCalcul.ROUNDING_MODE);
        BigDecimal residu = montantTmp.multiply(new BigDecimal(nbMois)).subtract(montant);

        montantsParMois = new ArrayList<BigDecimal>();

        for (int i = 0; i < nbMois; i++) {
            if (i == 0) {
                montantsParMois.add(montantTmp.subtract(residu));
            } else {
                montantsParMois.add(montantTmp);
            }
        }
    }
}
