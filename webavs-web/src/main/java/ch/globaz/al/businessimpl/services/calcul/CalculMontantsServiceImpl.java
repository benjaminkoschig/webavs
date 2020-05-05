package ch.globaz.al.businessimpl.services.calcul;

import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.services.ALRepositoryLocator;
import ch.globaz.al.businessimpl.calcul.modes.CalculImpotSource;
import ch.globaz.al.impotsource.domain.TauxImpositions;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSTarif;
import ch.globaz.al.business.constantes.ALConstCalcul;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.calcul.ALCalculException;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationPrestationsContextException;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.droit.CalculBusinessModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.tarif.TarifComplexModel;
import ch.globaz.al.business.models.tarif.TarifComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.calcul.CalculMontantsService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.param.business.service.ParameterModelService;

/**
 * Implémentation du service fournissant les méthodes permettant le calcul des montants effectif des prestations, la
 * répartition des prestations et les totaux
 * 
 * @author jts
 * 
 */
public class CalculMontantsServiceImpl extends ALAbstractBusinessServiceImpl implements CalculMontantsService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.calcul.CalculMontantsService#addDroitCalculeActif(ch.globaz.al.business.models
     * .dossier.DossierModel, ch.globaz.al.business.models.droit.DroitComplexModel,
     * ch.globaz.al.business.models.tarif.TarifComplexSearchModel, java.lang.String, java.util.ArrayList,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<CalculBusinessModel> addDroitCalculeActif(DossierModel dossier, DroitComplexModel droit,
                                                          TarifComplexSearchModel tarifs, String tarifForce, List<CalculBusinessModel> droitsCalcules,
                                                          String rang, String date) throws JadeApplicationException, JadePersistenceException {

        return this.addDroitCalculeActif(dossier, droit, tarifs, tarifForce, droitsCalcules, rang, date, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.calcul.CalculMontantsService#
     * addDroitCalculeActif(ch.globaz.al.business.models.dossier.DossierModel,
     * ch.globaz.al.business.models.droit.DroitComplexModel, ch.globaz.al.business.models.tarif.TarifComplexSearchModel,
     * java.lang.String, java.util.ArrayList, java.lang.String, java.lang.String)
     */
    @Override
    public List<CalculBusinessModel> addDroitCalculeActif(DossierModel dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs, String tarifForce, List<CalculBusinessModel> droitsCalcules,
            String rang, String date, boolean hideDroit) throws JadeApplicationException, JadePersistenceException {

        if (droit == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeActif : droit is null");
        }

        if (tarifs == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeActif : montant is null");
        }

        if (droitsCalcules == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeActif : list is null");
        }

        if (!JadeNumericUtil.isIntegerPositif(rang)) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeActif : rang '" + rang
                    + "' is not an integer");
        }

        TarifComplexModel tarif = getTarif(tarifForce, tarifs);

        CalculBusinessModel calc = new CalculBusinessModel();
        calc.setExportable(isExportable(dossier, droit, date));
        calc.setEcheanceOverLimiteLegale(ALServiceLocator.getDroitBusinessService().isEcheanceOverLimiteLegale(droit));

        calc.setDroit(droit);
        calc.setType(droit.getDroitModel().getTypeDroit());
        calc.setActif(true);
        calc.setRang(rang);
        boolean isTarifForce = (JadeNumericUtil.isEmptyOrZero(droit.getDroitModel().getTarifForce()) && JadeNumericUtil
                .isEmptyOrZero(dossier.getTarifForce()))
                || !JadeNumericUtil.isZeroValue(droit.getDroitModel().getMontantForce())
                || !JadeNumericUtil.isZeroValue(dossier.getMontantForce()) ? false : true;
        calc.setTarifForce(isTarifForce);
        calc.setHideDroit(hideDroit);
        calc = setMontantCaisseCanton(tarifs, calc);

        if (droit.getDroitModel().getForce()) {
            calc.setCalculResultMontantBase(droit.getDroitModel().getMontantForce());
            calc.setTarif(null);
        } else {
            calc.setCalculResultMontantBase(tarif == null ? "0" : tarif.getPrestationTarifModel().getMontant());
            calc.setTarif(tarif == null ? null : tarif.getCategorieTarifModel().getCategorieTarif());
        }

        droitsCalcules.add(calc);
        return droitsCalcules;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.calcul.CalculMontantsService#
     * addDroitCalculeInactif(ch.globaz.al.business.models.dossier.DossierModel,
     * ch.globaz.al.business.models.droit.DroitComplexModel, java.util.ArrayList)
     */
    @Override
    public List<CalculBusinessModel> addDroitCalculeInactif(DroitComplexModel droit,
            List<CalculBusinessModel> droitsCalcules, String date) throws JadeApplicationException,
            JadePersistenceException {

        if (droit == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeInactif : droit is null");
        }

        if (droitsCalcules == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeInactif : list is null");
        }

        CalculBusinessModel calc = new CalculBusinessModel();
        calc.setExportable(true);
        calc.setEcheanceOverLimiteLegale(ALServiceLocator.getDroitBusinessService().isEcheanceOverLimiteLegale(droit));
        calc.setDroit(droit);
        calc.setCalculResultMontantBase("0");
        calc.setCalculResultMontantEffectif("0");
        calc.setCalculResultMontantBaseCaisse("0");
        calc.setCalculResultMontantEffectifCaisse("0");
        calc.setCalculResultMontantBaseCanton("0");
        calc.setCalculResultMontantEffectifCanton("0");
        calc.setTarif(null);
        calc.setTarifCaisse(null);
        calc.setTarifCanton(null);
        calc.setType(droit.getDroitModel().getTypeDroit());
        calc.setActif(false);
        calc.setTarifForce(false);

        droitsCalcules.add(calc);
        return droitsCalcules;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.calcul.CalculMontantsService# addDroitCalculeNaissance
     * (ch.globaz.al.business.models.dossier.DossierModel, ch.globaz.al.business.models.droit.DroitComplexModel,
     * ch.globaz.al.business.models.tarif.TarifComplexSearchModel, java.lang.String, java.util.ArrayList,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<CalculBusinessModel> addDroitCalculeNaissance(DossierModel dossier, DroitComplexModel droit,
            TarifComplexSearchModel tarifs, String tarifForce, List<CalculBusinessModel> droitsCalcules,
            String rang, String type, String date) throws JadeApplicationException, JadePersistenceException {

        if (droit == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeNaissance : droit is null");
        }

        if (tarifs == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeNaissance : montant is null");
        }

        if (droitsCalcules == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeNaissance : list is null");
        }

        if (!JadeNumericUtil.isIntegerPositif(rang)) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeNaissance : rang '" + rang
                    + "' is not an integer");
        }

        if (!ALCSDroit.TYPE_ACCE.equals(type) && !ALCSDroit.TYPE_NAIS.equals(type)) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeNaissance : type '" + type
                    + "' is not valid");
        }

        TarifComplexModel tarif = getTarif(tarifForce, tarifs);

        CalculBusinessModel calc = new CalculBusinessModel();
        calc.setExportable(isExportable(dossier, droit, date));
        calc.setEcheanceOverLimiteLegale(ALServiceLocator.getDroitBusinessService().isEcheanceOverLimiteLegale(droit));

        calc.setDroit(droit);
        calc.setType(type);
        calc.setActif(true);
        calc.setRang(rang);
        boolean isTarifForce = (JadeNumericUtil.isEmptyOrZero(droit.getDroitModel().getTarifForce()) && JadeNumericUtil
                .isEmptyOrZero(dossier.getTarifForce()))
                || !JadeNumericUtil.isZeroValue(droit.getEnfantComplexModel().getEnfantModel()
                        .getMontantAllocationNaissanceFixe()) ? false : true;
        calc.setTarifForce(isTarifForce);

        calc = setMontantCaisseCanton(tarifs, calc);

        // montant forcé
        if (!JadeNumericUtil.isEmptyOrZero(droit.getEnfantComplexModel().getEnfantModel()
                .getMontantAllocationNaissanceFixe())) {

            calc.setCalculResultMontantBase(droit.getEnfantComplexModel().getEnfantModel()
                    .getMontantAllocationNaissanceFixe());
            calc.setTarif(null);
            // montant provenant des tarifs
        } else {
            calc.setCalculResultMontantBase(tarif == null ? "0" : tarif.getPrestationTarifModel().getMontant());
            calc.setTarif(tarif == null ? null : tarif.getCategorieTarifModel().getCategorieTarif());
        }

        droitsCalcules.add(calc);
        return droitsCalcules;
    }

    @Override
    public List<CalculBusinessModel> addDroitCalculeSpecial(String montant,
            List<CalculBusinessModel> droitsCalcules, DroitComplexModel droit, String typeDroit,
            String categorieTarif, String tarifCaisse, String tarifCanton) throws JadeApplicationException {

        if (!JadeNumericUtil.isNumeric(montant)) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeSpecial : montant is not numeric");
        }

        if (droitsCalcules == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeSpecial : list is null");
        }

        if (typeDroit == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeSpecial : typeDroit is null");
        }

        return this.addDroitCalculeSpecial(montant, droitsCalcules, droit, typeDroit, categorieTarif, tarifCaisse,
                tarifCanton, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.calcul.CalculMontantsService#
     * addDroitCalculeSpecial(ch.globaz.al.business.models.dossier.DossierModel, java.lang.String, java.util.ArrayList,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<CalculBusinessModel> addDroitCalculeSpecial(String montant,
            List<CalculBusinessModel> droitsCalcules, DroitComplexModel droit, String typeDroit,
            String categorieTarif, String tarifCaisse, String tarifCanton, boolean hideDroit)
            throws JadeApplicationException {

        if (!JadeNumericUtil.isNumeric(montant)) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeSpecial : montant is not numeric");
        }

        if (droitsCalcules == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeSpecial : list is null");
        }

        if (typeDroit == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeSpecial : typeDroit is null");
        }

        CalculBusinessModel calc = new CalculBusinessModel();
        calc.setExportable(true);
        calc.setEcheanceOverLimiteLegale(false);
        calc.setCalculResultMontantBase(montant);
        calc.setCalculResultMontantBaseCaisse(montant);
        calc.setCalculResultMontantBaseCanton(montant);
        calc.setDroit(droit);
        calc.setType(typeDroit);
        calc.setTarif(categorieTarif);
        calc.setTarifCaisse(tarifCaisse);
        calc.setTarifCanton(tarifCanton);
        calc.setTarifForce(false);
        calc.setActif(true);
        calc.setHideDroit(hideDroit);

        droitsCalcules.add(calc);
        return droitsCalcules;
    }

    /**
     * Ajoute le montant <code>montant</code> à <code>somme</code>. Avant l'ajout, le <code>type</code> est vérifié.
     * S'il s'agit d'une prestation de naissance ou d'accueil et que <code>avecNAIS</code> vaut <code>false</code>, le
     * montant n'est pas ajouté
     * 
     * @param somme
     *            Montant auquel est ajouté <code>montant</code>
     * @param montant
     *            Le montant à ajouter
     * @param avecNAIS
     *            Indique si le montant doit être ajouté dans le cas d'une prestation de naissance ou d'accueil
     * @param type
     *            Type de prestation ( {@link ch.globaz.al.business.constantes.ALCSDroit#GROUP_TYPE} )
     * 
     * @return La nouvelle somme
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private BigDecimal addMontant(BigDecimal somme, BigDecimal montant, boolean avecNAIS, String type)
            throws JadeApplicationException {

        if (somme == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#addDroitCalculeSpecial : somme is null");
        }

        if (avecNAIS || (!ALCSDroit.TYPE_NAIS.equals(type) && !ALCSDroit.TYPE_ACCE.equals(type))) {
            somme = somme.add(montant);
        }

        return somme;
    }

    @Override
    public Map calculerTotalMontant(DossierComplexModel dossier, List<CalculBusinessModel> droitsCalcules,
                                    String unite, String nbUnites, boolean avecNAIS, String date) throws JadeApplicationException,
            JadePersistenceException {

        if (dossier == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#calculerTotalMontant : dossier is null");
        }

        if (droitsCalcules == null) {
            throw new ALCalculException(
                    "CalculMontantsServiceImpl#calculerTotalMontant : droitsCalcules is null or empty");
        }

        if (!ALCSDossier.UNITE_CALCUL_HEURE.equals(unite) && !ALCSDossier.UNITE_CALCUL_JOUR.equals(unite)
                && !ALCSDossier.UNITE_CALCUL_MOIS.equals(unite)) {
            throw new ALGenerationPrestationsContextException(
                    "CalculMontantsServiceImpl#calculerTotalMontant : unite '" + unite + "' is not valid");
        }

        if (!JadeNumericUtil.isIntegerPositif(nbUnites) && !JadeNumericUtil.isZeroValue(nbUnites)) {
            throw new ALCalculException(
                    "CalculMontantsServiceImpl#calculerTotalMontant : nbUnites is not an unsigned integer");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("CalculMontantsServiceImpl#calculerTotalMontant : " + date
                    + " is not a valid date");
        }

        BigDecimal totalBrut = new BigDecimal("0");
        BigDecimal totalBrutNAIS = new BigDecimal("0");
        BigDecimal total = new BigDecimal("0.00");
        BigDecimal totalBase = new BigDecimal("0");
        BigDecimal montantUnite = new BigDecimal("0.00");
        BigDecimal totalBrutIS = new BigDecimal("0");
        BigDecimal totalIS = new BigDecimal("0.00");

        // nombre d'unitées et taux
        BigDecimal nb = new BigDecimal("0.01").multiply(new BigDecimal(dossier.getDossierModel().getTauxVersement())
                .multiply(getNombreUnitesMax(unite, nbUnites, date)));
        BigDecimal nbNAIS = new BigDecimal("1.00");

        ArrayList<Integer> indexPrest = new ArrayList<Integer>();

        TauxImpositions tauxGroupByCanton = null;

        TauxImpositionRepository tauxImpositionRepository = ALRepositoryLocator
                .getTauxImpositionRepository();

        if (nb.compareTo(new BigDecimal("0")) != 0) {
            // parcours des droits
            for (int i = 0; i < droitsCalcules.size(); i++) {
                CalculBusinessModel calculDroit = (droitsCalcules.get(i));
                String type = calculDroit.getType();
                BigDecimal tauxDroit = new BigDecimal(calculDroit.getDroit().getDroitModel().getTauxVersement())
                        .multiply(new BigDecimal("0.01"));

                BigDecimal montantBase = getMontantBase(
                        new BigDecimal(calculDroit.getCalculResultMontantBase()).multiply(isNaissance(type) ? new BigDecimal(
                                "1.00") : tauxDroit), unite, date, calculDroit.getType());
                BigDecimal montantCaisse = getMontantBase(
                        new BigDecimal(calculDroit.getCalculResultMontantBaseCaisse()).multiply(isNaissance(type) ? new BigDecimal(
                                "1.00") : tauxDroit), unite, date, calculDroit.getType());
                BigDecimal montantCanton = getMontantBase(
                        new BigDecimal(calculDroit.getCalculResultMontantBaseCanton()).multiply(isNaissance(type) ? new BigDecimal(
                                "1.00") : tauxDroit), unite, date, calculDroit.getType());
                BigDecimal calculUnite = getMontantBase(
                        new BigDecimal(calculDroit.getCalculResultMontantBase()).multiply(isNaissance(type) ? new BigDecimal(
                                "1.00") : tauxDroit), ALCSDossier.UNITE_CALCUL_JOUR, date, calculDroit.getType());

                // D0135 arrondi des montant au 0.05 supp sur tarif jour
                montantBase = JANumberFormatter.round(montantBase, 0.05, 2, JANumberFormatter.SUP);
                montantCaisse = JANumberFormatter.round(montantCaisse, 0.05, 2, JANumberFormatter.SUP);
                montantCanton = JANumberFormatter.round(montantCanton, 0.05, 2, JANumberFormatter.SUP);
                // nouveau montant unité effectif pour le tarif par mois
                calculUnite = JANumberFormatter.round(calculUnite, 0.05, 2, JANumberFormatter.SUP);

                // end D0135
                // addition du montant du droit au total brut
                if (isNaissance(type)) {
                    totalBrutNAIS = addMontant(totalBrutNAIS, montantBase, avecNAIS, calculDroit.getType());
                } else {
                    totalBrut = addMontant(totalBrut, montantBase, avecNAIS, calculDroit.getType());
                    montantUnite = montantUnite.add(calculUnite);
                }

                // addition du montant du droit avec arrondi selon les paramètres de
                // la caisse
                BigDecimal montantToAdd = JANumberFormatter.round(
                        montantBase.multiply(isNaissance(type) ? nbNAIS : nb), getPrecision(unite, date), 2,
                        JANumberFormatter.NEAR);
                (droitsCalcules.get(i)).setCalculResultMontantEffectif(montantToAdd.toPlainString());
                total = addMontant(total, montantToAdd, avecNAIS, calculDroit.getType());

                // montant caisse
                (droitsCalcules.get(i)).setCalculResultMontantEffectifCaisse(JANumberFormatter.round(
                        montantCaisse.multiply(isNaissance(type) ? nbNAIS : nb), getPrecision(unite, date), 2,
                        JANumberFormatter.NEAR).toPlainString());

                // montant canton
                (droitsCalcules.get(i)).setCalculResultMontantEffectifCanton(JANumberFormatter.round(
                        montantCanton.multiply(isNaissance(type) ? nbNAIS : nb), getPrecision(unite, date), 2,
                        JANumberFormatter.NEAR).toPlainString());

                if ((Double.parseDouble(calculDroit.getCalculResultMontantEffectif()) > 0) && !isNaissance(type)) {
                    indexPrest.add(i);
                }

                if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                        && !JadeStringUtil.isBlankOrZero(calculDroit.getCalculResultMontantIS())) {
                    AssuranceInfo infos = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier.getDossierModel(), date);
                    String cantonImposition = CalculImpotSource.getCantonImposition(dossier ,infos.getCanton());
                    CalculImpotSource.computeISforDroit(calculDroit, calculDroit.getCalculResultMontantEffectif()
                            , tauxGroupByCanton, tauxImpositionRepository, cantonImposition, date);
                }
            }

            totalBase = JANumberFormatter.round(totalBrut, 0.05, 2, JANumberFormatter.NEAR).add(totalBrutNAIS);
            totalBrut = JANumberFormatter.round(totalBrut, 0.05, 2, JANumberFormatter.NEAR).multiply(nb)
                    .add(totalBrutNAIS);

            // arrondi du total brut à 0.05
            BigDecimal totalArrondi = JANumberFormatter.round(totalBrut, 0.05, 2, JANumberFormatter.NEAR);

            BigDecimal residu = total.subtract(totalArrondi);

            // s'il y a une différence on corrige le montant effectif et une
            // prestation
            if ((residu.compareTo(new BigDecimal("0")) != 0) && (indexPrest.size() >= 0)) {
                total = total.subtract(residu);
                BigDecimal div = residu.divide(new BigDecimal(indexPrest.size()), 2, ALConstCalcul.ROUNDING_MODE);
                BigDecimal reste = residu.subtract(div.multiply(new BigDecimal(indexPrest.size())));

                for (int i = 0; i < indexPrest.size(); i++) {
                    BigDecimal newMontPrest = new BigDecimal(
                            (droitsCalcules.get(indexPrest.get(i))).getCalculResultMontantEffectif()).subtract(div);

                    if ((i + 1) == indexPrest.size()) {
                        newMontPrest = newMontPrest.subtract(reste);
                    }

                    (droitsCalcules.get(indexPrest.get(i))).setCalculResultMontantEffectif(newMontPrest.toString());

                    if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()
                            && !JadeStringUtil.isBlankOrZero(droitsCalcules.get(indexPrest.get(i)).getCalculResultMontantIS())) {
                        AssuranceInfo infos = ALServiceLocator.getAffiliationBusinessService().getAssuranceInfo(dossier.getDossierModel(), date);
                        String cantonImposition = CalculImpotSource.getCantonImposition(dossier ,infos.getCanton());
                        CalculImpotSource.computeISforDroit(droitsCalcules.get(indexPrest.get(i)), droitsCalcules.get(indexPrest.get(i)).getCalculResultMontantEffectif()
                                , tauxGroupByCanton, tauxImpositionRepository, cantonImposition, date);
                    }
                }
            }

            if (ALCSDossier.UNITE_CALCUL_MOIS.equals(unite)) {
                ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                        ALConstParametres.APPNAME, ALConstParametres.NBR_JOURS_PAR_MOIS, date);
                // D0135 remplacé par calcul plus haut pour le mandat D0135
                // montantUnite = JANumberFormatter.round(
                // total.divide(new BigDecimal(param.getValeurAlphaParametre()), ALConstCalcul.ROUNDING_MODE),
                // 0.05, 4, JANumberFormatter.NEAR);

            } else if (ALCSDossier.UNITE_CALCUL_JOUR.equals(unite)) {
                montantUnite = JANumberFormatter.round(
                        total.divide(new BigDecimal(nbUnites), ALConstCalcul.ROUNDING_MODE), 0.05, 4,
                        JANumberFormatter.NEAR);
            } else if (ALCSDossier.UNITE_CALCUL_HEURE.equals(unite)) {
                montantUnite = JANumberFormatter.round(
                        totalArrondi.divide(new BigDecimal(nbUnites), ALConstCalcul.ROUNDING_MODE), 0.01, 4,
                        JANumberFormatter.NEAR);
            }
        }

        Map<String, Object> res = new HashMap<>();
        res.put(ALConstCalcul.TOTAL_EFFECTIF, total.toPlainString());
        res.put(ALConstCalcul.TOTAL_UNITE_EFFECTIF, montantUnite.toPlainString());
        res.put(ALConstCalcul.TOTAL_BASE, totalBase.toPlainString());
        res.put(ALConstCalcul.DROITS_CALCULES, droitsCalcules);
        return res;
    }

    /**
     * Retourne le montant de base d'un droit aux allocations familiales en fonction du montant de base et de l'unité du
     * dossier (heure, jour, mois)
     * 
     * @param montantBase
     *            Montant de base mensuel
     * @param unite
     *            Unité à utiliser pour déterminer le montant ( {@link ch.globaz.al.business.constantes.ALCSDossier})
     * @param date
     *            Date pour laquelle le calcul a été effectué
     * @return montant effectif
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private BigDecimal getMontantBase(BigDecimal montantBase, String unite, String date, String type)
            throws JadeApplicationException, JadePersistenceException {

        if (montantBase == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#getMontantBase : montantBase is null");
        }

        if (unite == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#getMontantBase : unite is null");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("CalculMontantsServiceImpl#getMontantBase : " + date + " is not a valid date");
        }

        if (ALCSDossier.UNITE_CALCUL_MOIS.equals(unite) || isNaissance(type)) {
            return montantBase;
        } else if (ALCSDossier.UNITE_CALCUL_JOUR.equals(unite)) {
            return getMontantBaseJour(montantBase, date);
        } else if (ALCSDossier.UNITE_CALCUL_HEURE.equals(unite)) {
            return getMontantBaseHeure(montantBase, date);
        } else {
            throw new ALCalculException(
                    "CalculMontantsServiceImpl#getMontantBase : Impossible de calculer le montant de base, l'unité passée n'est pas valide");
        }
    }

    /**
     * Retourne le montant de base pour une heure d'un droit aux allocations familiales en fonction du montant de base
     * 
     * @param montantBase
     *            Montant de base mensuel
     * @param date
     *            Date pour laquelle le calcul a été effectué
     * @return Montant de base horaire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private BigDecimal getMontantBaseHeure(BigDecimal montantBase, String date) throws JadeApplicationException,
            JadePersistenceException {

        if (montantBase == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#getMontantBaseHeure : montantBase is null");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("CalculMontantsServiceImpl#getMontantBaseHeure : " + date
                    + " is not a valid date");
        }

        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.NBR_HEURES_PAR_MOIS, date);

        BigDecimal nbHeuresMois = new BigDecimal(param.getValeurAlphaParametre());
        return montantBase.divide(nbHeuresMois, 4, ALConstCalcul.ROUNDING_MODE);
    }

    /**
     * Retourne le montant de base pour un jour d'un droit aux allocations familiales en fonction du montant de base
     * 
     * @param montantBase
     *            Montant de base mensuel
     * @param date
     *            Date pour laquelle le calcul a été effectué
     * @return Montant de base journalier
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private BigDecimal getMontantBaseJour(BigDecimal montantBase, String date) throws JadeApplicationException,
            JadePersistenceException {

        if (montantBase == null) {
            throw new ALCalculException("CalculMontantsServiceImpl#getMontantBaseJour : montantBase is null");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("CalculMontantsServiceImpl#getMontantBaseJour : " + date
                    + " is not a valid date");
        }

        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.NBR_JOURS_PAR_MOIS, date);

        BigDecimal nbJourMois = new BigDecimal(param.getValeurAlphaParametre());
        return montantBase.divide(nbJourMois, 4, ALConstCalcul.ROUNDING_MODE);
    }

    /**
     * Récupère le nombre d'unité à utiliser pour le calcul. Si <code>nbUnites</code> dépasse le maximum autorisé
     * (défini dans la table des paramètres), le nombre retourné est plafonné à cette valeur
     * 
     * @param unite
     *            Unité du dossier. Code système du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL}
     * @param nbUnites
     *            Nombre d'unités
     * @param date
     *            Date pour laquelle récupérer la précision
     * @return précision correspondant aux paramètres passés à la méthode
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private BigDecimal getNombreUnitesMax(String unite, String nbUnites, String date) throws JadeApplicationException,
            JadePersistenceException {

        if (JadeStringUtil.isEmpty(unite)) {
            throw new ALCalculException("CalculMontantsServiceImpl#getNombreUnitesMax : unite is null or empty");
        }

        if (!JadeNumericUtil.isIntegerPositif(nbUnites) && !JadeNumericUtil.isZeroValue(nbUnites)) {
            throw new ALCalculException(
                    "CalculMontantsServiceImpl#getNombreUnitesMax : nbUnites is not an unsigned integer");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("CalculMontantsServiceImpl#getNombreUnitesMax : " + date
                    + " is not a valid date");
        }

        String paramName;

        if (ALCSDossier.UNITE_CALCUL_MOIS.equals(unite)) {
            return new BigDecimal("1");
        } else if (ALCSDossier.UNITE_CALCUL_JOUR.equals(unite)) {
            paramName = ALConstParametres.NBR_JOURS_PAR_MOIS;
        } else if (ALCSDossier.UNITE_CALCUL_HEURE.equals(unite)) {
            paramName = ALConstParametres.NBR_HEURES_PAR_MOIS;
        } else {
            throw new ALCalculException("CalculMontantsServiceImpl#getNombreUnitesMax : " + unite
                    + " is not a valid unity");
        }

        ParameterModel param = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, paramName, date);

        BigDecimal nbMax = new BigDecimal(param.getValeurAlphaParametre());

        return ((new BigDecimal(nbUnites)).compareTo(nbMax) > 0) ? nbMax : new BigDecimal(nbUnites);
    }

    /**
     * Récupère la précision à utiliser en fonction du type d'unité du dossier et de la date
     * 
     * @param unite
     *            Unité du dossier. Code système du groupe
     *            {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL}
     * @param date
     *            Date pour laquelle récupérer la précision
     * @return précision correspondant aux paramètres passés à la méthode
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private double getPrecision(String unite, String date) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(unite)) {
            throw new ALCalculException("CalculMontantsServiceImpl#getPrecision : unite is null or empty");
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALCalculException("CalculMontantsServiceImpl#getPrecision : " + date + " is not a valid date");
        }

        ParameterModelService service = ParamServiceLocator.getParameterModelService();
        ParameterModel param;

        if (ALCSDossier.UNITE_CALCUL_MOIS.equals(unite)) {
            param = service.getParameterByName(ALConstParametres.APPNAME, ALConstParametres.PRECISION_UNITE_MOIS, date);
        } else if (ALCSDossier.UNITE_CALCUL_JOUR.equals(unite)) {
            param = service.getParameterByName(ALConstParametres.APPNAME, ALConstParametres.PRECISION_UNITE_JOUR, date);
        } else if (ALCSDossier.UNITE_CALCUL_HEURE.equals(unite)) {
            param = service
                    .getParameterByName(ALConstParametres.APPNAME, ALConstParametres.PRECISION_UNITE_HEURE, date);
        } else {
            throw new ALCalculException("CalculMontantsServiceImpl#getPrecision : " + unite + " is not a valid unity");
        }

        return new Double(param.getValeurNumParametre());
    }

    /**
     * Recherche le tarif correspondant à la catégorie <code>tarifForce</code> dans <code>tarifs</code>
     * 
     * @param tarifForce
     *            catégorie de tarif recherchée
     * @param tarifs
     *            Résultat d'une recherche de tarifs
     * @return Tarif trouvé, <code>null</code> si aucun tarif ne correspond
     */
    private TarifComplexModel getTarif(String tarifForce, TarifComplexSearchModel tarifs) {
        int i = 0;

        // si aucun mode n'est définit on prend le meilleur tarif (le premier)
        if (tarifForce == null) {
            if (tarifs.getSize() == 0) {
                return null;
            } else {
                return ((TarifComplexModel) tarifs.getSearchResults()[0]);
            }
        } else {
            // recherche du tarif correspondant à tarifForce
            while ((i < tarifs.getSize())) {
                TarifComplexModel tmp = ((TarifComplexModel) tarifs.getSearchResults()[i]);
                if (tarifForce.equals(tmp.getCategorieTarifModel().getCategorieTarif())) {
                    return tmp;
                }

                i++;
            }

            return null;
        }
    }

    /**
     * Vérifie si une prestation est exportable. Aucune vérification n'est effectué pour les droits de ménage, sauf s'il
     * s'agit de ménage dans l'agriculture.
     * 
     * @param dossier
     *            Dossier auquel appartient le droit à vérifier
     * @param droit
     *            Le droit à vérifier
     * @param date
     *            Date pour laquelle effectuer la vérification
     * @return <code>true</code> si le droit est exportable, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    private boolean isExportable(DossierModel dossier, DroitComplexModel droit, String date)
            throws JadePersistenceException, JadeApplicationException {

        if (ALCSDroit.TYPE_MEN.equals(droit.getDroitModel().getTypeDroit())) {
            if (ALImplServiceLocator.getDossierBusinessService().isAgricole(dossier.getActiviteAllocataire())) {
                return ALServiceLocator.getDroitBusinessService().ctrlDroitExportabilite(droit, date);
            } else {
                return true;
            }
        } else {
            return ALServiceLocator.getDroitBusinessService().ctrlDroitExportabilite(droit, date);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.calcul.CalculMontantsService#isMontantForce(ch.globaz.al.business.models.droit
     * .CalculBusinessModel)
     */
    @Override
    public boolean isMontantForce(CalculBusinessModel calcDroit) {
        return (calcDroit.getDroit().getDroitModel().getForce());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.al.business.services.calcul.CalculMontantsService#isMontantForceZero(ch.globaz.al.business.models.droit
     * .CalculBusinessModel)
     */
    @Override
    public boolean isMontantForceZero(CalculBusinessModel calcDroit) {
        return (calcDroit.getDroit().getDroitModel().getForce() && JadeNumericUtil.isEmptyOrZero(calcDroit
                .getCalculResultMontantBase()));
    }

    /**
     * Indique si <code>type</code> est de type NAIS ou ACCE
     * 
     * @param type
     *            Le type à vérifier
     * @return <code>true</code> si <code>type</code> est de type NAIS ou ACCE, <code>false</code> sinon
     */
    private boolean isNaissance(String type) {
        return (ALCSDroit.TYPE_NAIS.equals(type) || ALCSDroit.TYPE_ACCE.equals(type));
    }

    /**
     * Ajoute le montant de caisse et cantonal dans <code>calc</code>
     * 
     * @param tarifs
     *            Modèle de recherche dans lequel se trouve les montants à ajouter
     * @param calc
     *            Modèle auquel ajouter les montants
     * 
     * @return Modèle mis à jour
     */
    private CalculBusinessModel setMontantCaisseCanton(TarifComplexSearchModel tarifs, CalculBusinessModel calc) {
        int i = 0;

        // recherche de montant caisse et canton dans le résultat de la
        // recherche.
        while ((i < tarifs.getSize())
                && (JadeStringUtil.isEmpty(calc.getCalculResultMontantBaseCaisse()) || JadeStringUtil.isEmpty(calc
                        .getCalculResultMontantBaseCanton()))) {

            TarifComplexModel tarif = ((TarifComplexModel) tarifs.getSearchResults()[i]);

            if (ALCSTarif.LEGISLATION_CAISSE.equals(tarif.getLegislationTarifModel().getTypeLegislation())) {
                calc.setCalculResultMontantBaseCaisse(tarif.getPrestationTarifModel().getMontant());
                calc.setTarifCaisse(tarif.getCategorieTarifModel().getCategorieTarif());

            } else if (ALCSTarif.LEGISLATION_CANTONAL.equals(tarif.getLegislationTarifModel().getTypeLegislation())) {
                calc.setCalculResultMontantBaseCanton(tarif.getPrestationTarifModel().getMontant());
                calc.setTarifCanton(tarif.getCategorieTarifModel().getCategorieTarif());
            }

            i++;
        }

        // si aucun tarif ne correspond on mets les montant à 0

        if (JadeStringUtil.isEmpty(calc.getCalculResultMontantBaseCaisse())) {
            calc.setCalculResultMontantBaseCaisse("0");
            calc.setTarifCaisse(null);
        }

        if (JadeStringUtil.isEmpty(calc.getCalculResultMontantBaseCanton())) {
            calc.setCalculResultMontantBaseCanton("0");
            calc.setTarifCanton(null);
        }

        return calc;
    }
}
