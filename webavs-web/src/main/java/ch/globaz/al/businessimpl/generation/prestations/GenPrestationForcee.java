package ch.globaz.al.businessimpl.generation.prestations;

import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALRepositoryLocator;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.calcul.modes.CalculImpotSource;
import ch.globaz.al.impotsource.domain.TauxImpositions;
import ch.globaz.al.impotsource.domain.TypeImposition;
import ch.globaz.al.impotsource.persistence.TauxImpositionRepository;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.naos.business.data.AssuranceInfo;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextAffilie;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;

/**
 * Classe de génération de prestation avec montant forcé au niveau de la génération manuelle. Lorsqu'un montant est
 * forcé, il est réparti dans les mêmes proportions que si un calcul standard était effectué.
 * 
 * @author jts
 */
public class GenPrestationForcee extends GenPrestationAbstract {
    /**
     * Montant par mois. Résultat de la division du montant forcé par le nombre de mois à générer
     */
    protected List<BigDecimal> montantsParMois = null;

    /**
     * Constructeur
     * 
     * @param context
     *            Context contenant les informations nécessaires à la génération
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
    protected void generatePrestation(ContextAffilie context, List<CalculBusinessModel> calcul)
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

        if (!calcul.isEmpty()) {

            // exécution de la répartition du montant
            List<String> repartition = repartirMontant(String.valueOf(getNextMontant()), calcul, context
                    .getContextDossier().getDossier(), "01."
                    + context.getContextDossier().getCurrentPeriode());

            for (int i = 0; i < calcul.size(); i++) {

                CalculBusinessModel droitCalcule = calcul.get(i);
                String montant = repartition.get(i);

                if (!JadeNumericUtil.isZeroValue(montant)) {
                    droitCalcule.setCalculResultMontantEffectif(montant);
                    droitCalcule.setTarifForce(false);
                    droitCalcule.setTarif(droitCalcule.getTarif());
                    DossierComplexModel dossierComplex = context.getContextDossier().getDossier();
                    DossierModel dossierModel = dossierComplex.getDossierModel();
                    if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                            && !JadeStringUtil.isBlankOrZero(droitCalcule.getCalculResultMontantIS())) {
                        TauxImpositionRepository tauxImpositionRepository = ALRepositoryLocator.getTauxImpositionRepository();
                        TauxImpositions tauxGroupByCanton = tauxImpositionRepository.findAll(TypeImposition.IMPOT_SOURCE);
                        AssuranceInfo infos = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossierModel, getDateCalcul(dossierComplex));
                        String cantonImposition = CalculImpotSource.getCantonImposition(dossierComplex ,infos.getCanton());
                        CalculImpotSource.computeISforDroit(dossierModel, droitCalcule, montant
                                , tauxGroupByCanton, tauxImpositionRepository, cantonImposition, getDateCalcul(dossierComplex));
                    }
                    addDetailPrestation(context, droitCalcule);
                }
            }
        }
    }

    /**
     * Retourne le prochain montant à traiter
     * 
     * @return le prochain montant à traiter
     * 
     */
    protected BigDecimal getNextMontant() {
        BigDecimal next = montantsParMois.get(0);
        montantsParMois.remove(0);
        return next;
    }

    /**
     * Retourne la date à utiliser pour le calcul de la décision selon le dossier
     *
     * @param dossierComplexModel
     *            dossier
     * @return la date à utiliser pour le calcul dans la décision
     */
    private final String getDateCalcul(DossierComplexModel dossierComplexModel) {

        if (JadeDateUtil.isGlobazDate(dossierComplexModel.getDossierModel().getFinValidite())) {
            return dossierComplexModel.getDossierModel().getFinValidite();

        } else {
            return dossierComplexModel.getDossierModel().getDebutValidite();
        }
    }

    /**
     * Répartit le <code>montant</code> dans les même proportions que le résultat du calcul <code>droitsCalcules</code>
     * 
     * @param montant
     *            montant à répartir
     * @param droitsCalcules
     *            résultat d'un calcul
     * @param dossier
     *            Dossier auquel correspond le calcul
     * @param date
     *            Date pour laquelle le calcul a été effectuée
     * @return Montants répartis
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected List<String> repartirMontant(String montant, List<CalculBusinessModel> droitsCalcules,
            DossierComplexModel dossier, String date) throws JadeApplicationException, JadePersistenceException {

        if (JadeNumericUtil.isEmptyOrZero(montant)) {
            throw new ALCalculException("GenPrestationForcee#repartirMontant : montant is empty or zero");
        }

        if ((droitsCalcules == null) || (droitsCalcules.isEmpty())) {
            throw new ALCalculException("GenPrestationForcee#repartirMontant : droitsCalcules is null or empty");
        }

        if ((dossier == null)) {
            throw new ALCalculException("GenPrestationForcee#repartirMontant : dossier is null");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("GenPrestationForcee#repartirMontant : " + date + " is not a valid date");
        }

        List<String> repartition = new ArrayList<>();
        BigDecimal montantTotal = new BigDecimal((String) ALImplServiceLocator.getCalculMontantsService()
                .calculerTotalMontant(dossier, droitsCalcules, dossier.getDossierModel().getUniteCalcul(), "1", false, date)
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
     * Détermine le montant par mois en fonction du montant forcé et du nombre de mois à générer
     */
    protected void setMontantParMois() {

        int nbMois = ALDateUtils.getNbMonthsBetween("01." + context.getContextDossier().getDebutPeriode(), "01."
                + context.getContextDossier().getFinPeriode());

        BigDecimal montant = new BigDecimal(context.getContextDossier().getMontantForce());

        BigDecimal montantTmp = montant.divide(new BigDecimal(nbMois), 2, ALConstCalcul.ROUNDING_MODE);
        BigDecimal residu = montantTmp.multiply(new BigDecimal(nbMois)).subtract(montant);

        montantsParMois = new ArrayList<>();

        for (int i = 0; i < nbMois; i++) {
            if (i == 0) {
                montantsParMois.add(montantTmp.subtract(residu));
            } else {
                montantsParMois.add(montantTmp);
            }
        }
    }
}
