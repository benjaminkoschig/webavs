package ch.globaz.al.businessimpl.services.adi;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.exceptions.model.adi.ALCalculAdiModelException;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisComplexSearchModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisSearchModel;
import ch.globaz.al.business.models.adi.AdiSaisieModel;
import ch.globaz.al.business.models.adi.AdiSaisieSearchModel;
import ch.globaz.al.business.models.adi.DecompteAdiModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexModel;
import ch.globaz.al.business.models.prestation.DetailPrestationComplexSearchModel;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereModel;
import ch.globaz.al.business.models.tauxMonnaieEtrangere.TauxMonnaieEtrangereSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.adi.CalculAdiBusinessService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.param.business.exceptions.ParamException;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import ch.globaz.param.business.vo.KeyNameParameter;

/**
 * Implémentation des services Calcul ADI métier
 * 
 * @author GMO
 * 
 */
public class CalculAdiBusinessServiceImpl implements CalculAdiBusinessService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adi.CalculAdiBusinessService#calculForDecompte (java.lang.String,
     * java.lang.String)
     */
    @Override
    public void calculForDecompte(DecompteAdiModel decompte) throws JadePersistenceException, JadeApplicationException {

        if (decompte == null) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#calculForDecompte: unable to calcul, decompte is null");
        }
        if (!ALCSPrestation.ETAT_PR.equals(decompte.getEtatDecompte())) {

            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#calculForDecompte: Impossible de lancer le calcul pour un décompte qui n'est pas dans un état PR");
        }

        DetailPrestationComplexSearchModel prestationsTravail = ALServiceLocator.getDecompteAdiBusinessService()
                .getPrestationsTravailDossier(decompte.getIdDossier(), decompte.getPeriodeDebut(),
                        decompte.getPeriodeFin());

        // Parmi les détail prestations, on extrait les id droits pour les droit
        // enfant et formation
        ArrayList<String> listDroits = new ArrayList<String>();
        for (int i = 0; i < prestationsTravail.getSize(); i++) {
            DetailPrestationComplexModel currentDetailPrestation = (DetailPrestationComplexModel) prestationsTravail
                    .getSearchResults()[i];
            // on prend que les droits de type ENF / FORM une seule fois
            if (!listDroits.contains(currentDetailPrestation.getDroitComplexModel().getId())
                    && (ALCSDroit.TYPE_ENF.equals(currentDetailPrestation.getDroitComplexModel().getDroitModel()
                            .getTypeDroit()) || ALCSDroit.TYPE_FORM.equals(currentDetailPrestation
                            .getDroitComplexModel().getDroitModel().getTypeDroit()))) {
                listDroits.add(currentDetailPrestation.getDroitComplexModel().getId());
            }

        }
        // on lance le calcul pour chaque droit
        for (String idDroit : listDroits) {
            ALServiceLocator.getCalculAdiBusinessService().calculForDroit(decompte, idDroit, prestationsTravail);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adi.CalculAdiBusinessService#calculForDroit (java.lang.String,
     * java.lang.String)
     */
    @Override
    public void calculForDroit(DecompteAdiModel decompte, String idDroit,
            DetailPrestationComplexSearchModel prestationsTravail) throws JadePersistenceException,
            JadeApplicationException {

        // on supprime les adiEnfantMois existant pour le droit
        AdiEnfantMoisSearchModel searchAdiEnfantMois = new AdiEnfantMoisSearchModel();
        searchAdiEnfantMois.setForIdDroit(idDroit);
        searchAdiEnfantMois.setForIdDecompteAdi(decompte.getId());
        searchAdiEnfantMois = ALImplServiceLocator.getAdiEnfantMoisModelService().search(searchAdiEnfantMois);
        for (int i = 0; i < searchAdiEnfantMois.getSize(); i++) {
            ALImplServiceLocator.getAdiEnfantMoisModelService().delete(
                    (AdiEnfantMoisModel) searchAdiEnfantMois.getSearchResults()[i]);
        }

        // On chope le droitcomplex
        DroitComplexModel droitComplexModel = new DroitComplexModel();

        for (int i = 0; i < prestationsTravail.getSize(); i++) {
            DetailPrestationComplexModel currentDetailPrestation = (DetailPrestationComplexModel) prestationsTravail
                    .getSearchResults()[i];
            if (idDroit.equals(currentDetailPrestation.getDetailPrestationModel().getIdDroit())) {
                droitComplexModel = currentDetailPrestation.getDroitComplexModel();
                break;
            }

        }

        Map montantsAllocCH = new HashMap();
        Map montantsRepartiCH = new HashMap();

        montantsAllocCH = fillMontantsAllocCH(prestationsTravail, idDroit);
        montantsRepartiCH = fillMontantsRepartiCH(prestationsTravail);

        Map montantsAllocETR = new HashMap();
        Map montantsRepartiETR = new HashMap();

        AdiSaisieSearchModel searchSaisies = new AdiSaisieSearchModel();
        searchSaisies.setForIdDecompteAdi(decompte.getId());
        searchSaisies = ALServiceLocator.getAdiSaisieModelService().search(searchSaisies);

        montantsAllocETR = fillMontantsAllocETR(searchSaisies, droitComplexModel.getDroitModel().getIdEnfant());
        montantsRepartiETR = fillMontantsRepartiETR(searchSaisies, prestationsTravail);

        String nextPeriode = "01.".concat(decompte.getPeriodeDebut());

        // parcours de toutes les périodes couvertes par le décompte
        do {
            // on considère que la ligne doit être crée. Ne sera pas créée
            // si montantAlloc CH = 0
            boolean creationRequired = true;

            // on met d'abord tout à 0, car on doit savoir après combien
            // de période couverte pour connaître le montant

            String currentKey = nextPeriode.substring(3).toString();
            AdiEnfantMoisModel adiEnfantMois = new AdiEnfantMoisModel();
            adiEnfantMois.setIdDecompteAdi(decompte.getId());
            adiEnfantMois.setMoisPeriode(nextPeriode.substring(3));

            // FIXME modifier la récupération du taux vers la table
            // tauxMonnaieétrangère: faire appel à la méhtode
            // getTauxAFForPeriode
            // et non ... getTauxForPeriode

            adiEnfantMois.setCoursChangeMonnaie(ALServiceLocator.getCalculAdiBusinessService().getTauxAFForPeriode(
                    decompte.getCodeMonnaie(), nextPeriode.substring(3)));

            adiEnfantMois.setIdDroit(idDroit);
            adiEnfantMois.setNbrEnfantFamille(new Integer(getNbEnfantsForPeriode(prestationsTravail,
                    nextPeriode.substring(3))).toString());
            if (montantsAllocCH.containsKey(currentKey)) {
                adiEnfantMois.setMontantAllocCH(montantsAllocCH.get(currentKey).toString());
            } else {
                adiEnfantMois.setMontantAllocCH("0");
                creationRequired = false;
            }

            if (montantsAllocETR.containsKey(currentKey)) {
                adiEnfantMois.setMontantAllocEtr(montantsAllocETR.get(currentKey).toString());
            } else {
                adiEnfantMois.setMontantAllocEtr("0");
            }
            if (montantsRepartiCH.containsKey(currentKey)) {
                adiEnfantMois.setMontantRepartiCH(montantsRepartiCH.get(currentKey).toString());
            } else {
                adiEnfantMois.setMontantRepartiCH("0");
            }
            if (montantsRepartiETR.containsKey(currentKey)) {
                adiEnfantMois.setMontantRepartiEtr(montantsRepartiETR.get(currentKey).toString());
            } else {
                adiEnfantMois.setMontantRepartiEtr("0");
            }

            adiEnfantMois = calculTotaux(adiEnfantMois);
            // on ne créé l'adi enfant mois que si montantAllocCH défini ou
            // montantAllocEtr défini
            if (creationRequired) {
                ALImplServiceLocator.getAdiEnfantMoisModelService().create(adiEnfantMois);
            }

            nextPeriode = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addMoisDate(1,
                    ALDateUtils.getCalendarDate(nextPeriode)).getTime());

        } while (!JadeDateUtil.isDateAfter(nextPeriode, "01.".concat(decompte.getPeriodeFin())));
    }

    /**
     * Calcul les totaux à calculer selon les données incl. Totaux calculés:
     * <ul>
     * <li>enfantMoisModel.setMontantEtrTotal</li>
     * <li>enfantMoisModel.setMontantEtrTotalEnCh</li>
     * <li>enfantMoisModel.setMontantCHTotal</li>
     * <li>enfantMoisModel.setMontantAdi</li>
     * </ul>
     * 
     * @param enfantMoisModel
     * @return
     */
    private AdiEnfantMoisModel calculTotaux(AdiEnfantMoisModel enfantMoisModel) {

        // 1.montant etr total = (montant alloc etr + montant réparti etr)

        double montantEtrTotal = Double.parseDouble(enfantMoisModel.getMontantAllocEtr())
                + Double.parseDouble(enfantMoisModel.getMontantRepartiEtr());

        enfantMoisModel.setMontantEtrTotal((new Double(montantEtrTotal)).toString());
        // 2.montant etr en CHF
        double montantEtrTotalChf = Double.parseDouble(enfantMoisModel.getMontantEtrTotal())
                * Double.parseDouble(enfantMoisModel.getCoursChangeMonnaie());
        enfantMoisModel.setMontantEtrTotalEnCh(new Double(montantEtrTotalChf).toString());

        // 3. montant CH total = (montant alloc CH + montant réparti CH)

        double montantChTotal = (Double.parseDouble(enfantMoisModel.getMontantAllocCH()) + Double
                .parseDouble(enfantMoisModel.getMontantRepartiCH()));

        enfantMoisModel.setMontantCHTotal(new Double(montantChTotal).toString());

        // 4. montant ADI = montant CH - montant ETR en CHF

        double montantAdi = (Double.parseDouble(enfantMoisModel.getMontantCHTotal()) - Double
                .parseDouble(enfantMoisModel.getMontantEtrTotalEnCh()));

        DecimalFormat format5centimes = new java.text.DecimalFormat();

        format5centimes.setMaximumFractionDigits(2);
        format5centimes.setMinimumFractionDigits(2);

        if (new Double(montantAdi).intValue() > 0) {
            enfantMoisModel.setMontantAdi(format5centimes.format(Math.round(montantAdi / 0.05) * 0.05));
        } else {
            enfantMoisModel.setMontantAdi("0.00");
        }

        enfantMoisModel.setMontantRepartiCHTotal("0.00");

        double montantRepartiCHTotal = Double.parseDouble(enfantMoisModel.getMontantRepartiCH())
                * Double.parseDouble(enfantMoisModel.getNbrEnfantFamille());

        if (new Double(montantRepartiCHTotal).intValue() > 0) {
            enfantMoisModel
                    .setMontantRepartiCHTotal(format5centimes.format(Math.round(montantRepartiCHTotal / 0.05) * 0.05));
        } else {
            enfantMoisModel.setMontantRepartiCHTotal("0.00");

        }

        // TODO (lot 2): mettre les vrais montants même si pas indispensable
        // pour le moment ( pas de montants
        enfantMoisModel.setMontantRepartiEtrTotal("0.00");

        return enfantMoisModel;
    }

    /**
     * Rempli les montants pour chaque période de la saisie passée en paramètre Ex: saisie 01-03.2009, montant : 600
     * euros => 01.2009 : 200; 02.2009 : 200; 03.2009 : 200;
     * 
     * @param montantsAllocETR Map contenant les montants saisies par période
     * @param saisieAdi la saisie en cours
     * @throws JadeApplicationException Exception levée si problème pour énumérer les périodes de la saisie
     */
    private void fillMontantAllocEtrForOneSaisie(Map<String, Double> montantsAllocETR, AdiSaisieModel saisieAdi)
            throws JadeApplicationException {

        String newPeriodeDate = "01.".concat(saisieAdi.getPeriodeDe());

        // nombre de période selon la periode de debut et de fin
        Integer cptPeriodeSaisie = ALDateUtils.getNbMonthsBetween("01.".concat(saisieAdi.getPeriodeDe()),
                "01.".concat(saisieAdi.getPeriodeA()));

        // boucle pour initialiser la map avec période / montant saisie réparti (montantAdi divisé par nb périodes)
        do {
            // Suite au BZ 6492 - la division de java standard n'est pas approprié pour faire des calculs.
            // Il a fallu réadapter le code afin que la division se fasse le plus naturellement possible grâce au
            // Currency et BigDecimal.
            FWCurrency montantSaisi = new FWCurrency(saisieAdi.getMontantSaisi());

            Double montantSaisieAlloc = (montantSaisi.getBigDecimalValue()).divide(
                    BigDecimal.valueOf(cptPeriodeSaisie), 2, RoundingMode.UP).doubleValue();

            montantsAllocETR.put(newPeriodeDate.substring(3), montantSaisieAlloc);

            newPeriodeDate = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addMoisDate(1,
                    ALDateUtils.getCalendarDate(newPeriodeDate)).getTime());

        } while (!JadeDateUtil.isDateAfter(newPeriodeDate, "01.".concat(saisieAdi.getPeriodeA())));

    }

    /**
     * Remplit une liste avec les montants des allocations suisses pour chaque période du droit concerné
     * 
     * @param prestationsTravail prestations de travail suisses où chercher les allocations
     * @param idDroit id du droit concerné
     * @return la liste des montants
     */
    private HashMap<String, String> fillMontantsAllocCH(DetailPrestationComplexSearchModel prestationsTravail,
            String idDroit) {

        Map<String, String> montantsAllocCH = new HashMap<String, String>();

        for (int i = 0; i < prestationsTravail.getSize(); i++) {
            DetailPrestationComplexModel currentPrestationComplex = ((DetailPrestationComplexModel) prestationsTravail
                    .getSearchResults()[i]);
            // on cherche les détails de prestations relatifs au droit en
            // question et on stocke le montant suisse par alloc par mois
            if (idDroit.equals(currentPrestationComplex.getDetailPrestationModel().getIdDroit())) {

                montantsAllocCH.put(currentPrestationComplex.getDetailPrestationModel().getPeriodeValidite(),
                        currentPrestationComplex.getDetailPrestationModel().getMontant());
            }
        }

        return (HashMap<String, String>) montantsAllocCH;
    }

    /**
     * Remplit dans une map les montants allocations ETR saisies pour l'enfant spécifié par périodes couvertes par la
     * saisies
     * 
     * @param saisiesAdi la liste des saisies du décompte
     * @param idEnfant l'enfant dont on veut extraire les montants allocations saisies
     * @return liste contenant les montants allocations saisies pour l'enfant (clé=période, value=montant)
     * @throws JadeApplicationException
     */
    private HashMap<String, Double> fillMontantsAllocETR(AdiSaisieSearchModel saisiesAdi, String idEnfant)
            throws JadeApplicationException {

        Map<String, Double> montantsAllocETR = new HashMap<String, Double>();

        // parmi les saisies du décompte, on prend celles correspondant à
        // l'enfant
        for (int i = 0; i < saisiesAdi.getSize(); i++) {
            AdiSaisieModel currentSaisie = ((AdiSaisieModel) saisiesAdi.getSearchResults()[i]);

            // on prend en compte que les saisies avec enfant
            if (currentSaisie.getIdEnfant().equals(idEnfant)) {

                fillMontantAllocEtrForOneSaisie(montantsAllocETR, currentSaisie);

            }
        }
        return (HashMap<String, Double>) montantsAllocETR;
    }

    /**
     * @param prestationsTravail
     * @return
     * @throws NumberFormatException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadeApplicationException
     */
    private HashMap<String, Double> fillMontantsRepartiCH(DetailPrestationComplexSearchModel prestationsTravail)
            throws NumberFormatException, JadeApplicationServiceNotAvailableException, JadeApplicationException {

        Map<String, Double> montantsRepartiCH = new HashMap<String, Double>();

        for (int i = 0; i < prestationsTravail.getSize(); i++) {

            DetailPrestationComplexModel currentPrestationComplex = (DetailPrestationComplexModel) prestationsTravail
                    .getSearchResults()[i];
            // on cherche aussi les détails de prestations MEN, FNB (alloc
            // suppléments) pour stocker dans
            // une table montant à répartir

            if (ALServiceLocator.getDroitBusinessService().isTypeMenage(
                    currentPrestationComplex.getDroitComplexModel().getDroitModel())
                    || ALCSDroit.TYPE_FNB.equals(currentPrestationComplex.getDroitComplexModel().getDroitModel()
                            .getTypeDroit())) {

                montantsRepartiCH.put(
                        currentPrestationComplex.getDetailPrestationModel().getPeriodeValidite(),
                        Double.parseDouble(currentPrestationComplex.getDetailPrestationModel().getMontant())
                                / Double.parseDouble(currentPrestationComplex.getEntetePrestationModel()
                                        .getNombreEnfants()));
            }

        }

        return (HashMap<String, Double>) montantsRepartiCH;
    }

    /**
     * @param saisiesAdi
     * @param prestationsTravail
     * @return
     * @throws JadeApplicationException
     */
    private HashMap<String, Double> fillMontantsRepartiETR(AdiSaisieSearchModel saisiesAdi,
            DetailPrestationComplexSearchModel prestationsTravail) throws JadeApplicationException {

        Map<String, Double> montantsRepartiETR = new HashMap<String, Double>();

        for (int i = 0; i < saisiesAdi.getSize(); i++) {
            AdiSaisieModel currentSaisie = ((AdiSaisieModel) saisiesAdi.getSearchResults()[i]);
            // saisie sans enfant
            if (JadeNumericUtil.isEmptyOrZero(currentSaisie.getIdEnfant())) {
                String newPeriodeDate = "01.".concat(currentSaisie.getPeriodeDe());
                // boucle pour connaître le nombre de période couverte selon la
                // plage saisie
                do {
                    // on met d'abord tout à 0, car on doit savoir après combien
                    // de période couverte pour connaître le montant
                    montantsRepartiETR.put(newPeriodeDate.substring(3), new Double("0"));
                    newPeriodeDate = JadeDateUtil.getGlobazFormattedDate(ALDateUtils.addMoisDate(1,
                            ALDateUtils.getCalendarDate(newPeriodeDate)).getTime());

                } while (!JadeDateUtil.isDateAfter(newPeriodeDate, "01.".concat(currentSaisie.getPeriodeA())));
                // pour les saisies "sans enfant", on répartit cette saisie par
                // période et par enfant
                int nbPeriodes = montantsRepartiETR.entrySet().size();
                Iterator<Entry<String, Double>> it2 = montantsRepartiETR.entrySet().iterator();
                while (it2.hasNext()) {
                    Entry<String, Double> entry = it2.next();
                    if (JadeNumericUtil.isEmptyOrZero(currentSaisie.getIdEnfant())) {
                        int nbEnfants = getNbEnfantsForPeriode(prestationsTravail, entry.getKey().toString());
                        double montantSaisieReparti = ((Double.parseDouble(currentSaisie.getMontantSaisi()) / nbPeriodes) / nbEnfants);

                        entry.setValue(new Double(montantSaisieReparti));
                    }

                }
            }
        }

        return (HashMap<String, Double>) montantsRepartiETR;
    }

    /**
     * @param prestationsTravail
     * @param periode
     * @return
     */
    private int getNbEnfantsForPeriode(DetailPrestationComplexSearchModel prestationsTravail, String periode) {
        for (int i = 0; i < prestationsTravail.getSize(); i++) {
            if (((DetailPrestationComplexModel) prestationsTravail.getSearchResults()[i]).getDetailPrestationModel()
                    .getPeriodeValidite().equals(periode)) {
                return Integer.parseInt(((DetailPrestationComplexModel) prestationsTravail.getSearchResults()[i])
                        .getEntetePrestationModel().getNombreEnfants());
            }
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adi.CalculAdiBusinessService#getSoldeAPayer (java.lang.String)
     */
    @Override
    public String getSoldeAPayer(String idDecompte) throws JadeApplicationException, JadePersistenceException {

        AdiEnfantMoisComplexSearchModel searchAdiEnfantMois = new AdiEnfantMoisComplexSearchModel();
        searchAdiEnfantMois.setForIdDecompteAdi(idDecompte);
        searchAdiEnfantMois = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(searchAdiEnfantMois);
        Double totalDecompte = new Double(getTotalDecompte(searchAdiEnfantMois));
        Double sommeDejaVersee = new Double(getSommeDejaVersee(idDecompte));

        Double soldeAPayer = totalDecompte - sommeDejaVersee;

        // Format décimal 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(Math.round(soldeAPayer / 0.05) * 0.05));

    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.al.business.services.adi.CalculAdiBusinessService# getSommeDejaVersee(java.lang.String)
     */
    @Override
    public String getSommeDejaVersee(String idDecompte) throws JadeApplicationException, JadePersistenceException {

        Double sommeTotal = new Double(0);

        DecompteAdiModel decompteAdi = ALServiceLocator.getDecompteAdiModelService().read(idDecompte);
        ArrayList<String> periodesToCheck = ALDateUtils.enumPeriodeFromInterval(decompteAdi.getPeriodeDebut(),
                decompteAdi.getPeriodeFin());

        AdiEnfantMoisComplexSearchModel searchMontantsAdi = new AdiEnfantMoisComplexSearchModel();
        // si il y un décompte remplacé par celui en question, on regarde les
        // montants de ce décompte
        // qui correspondent aux montants pour les périodes du décompte en
        // question
        if (!JadeNumericUtil.isEmptyOrZero(decompteAdi.getIdDecompteRemplace())) {
            searchMontantsAdi.setForIdDecompteAdi(decompteAdi.getIdDecompteRemplace());

            for (String periode : periodesToCheck) {
                searchMontantsAdi.setForPeriode(periode);
                searchMontantsAdi = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(searchMontantsAdi);

                for (int i = 0; i < searchMontantsAdi.getSize(); i++) {
                    sommeTotal = sommeTotal
                            + new Double(((AdiEnfantMoisComplexModel) searchMontantsAdi.getSearchResults()[i])
                                    .getAdiEnfantMoisModel().getMontantAdi());
                }

            }
        }

        // Format décimal 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(Math.round(sommeTotal / 0.05) * 0.05));

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adi.CalculAdiBusinessService#getTauxForPeriode (java.lang.String,
     * java.lang.String)
     */

    @Override
    public String getTauxAFForPeriode(String csMonnaie, String periodeCalcul) throws JadePersistenceException,
            JadeApplicationException {
        if (JadeStringUtil.isEmpty(csMonnaie)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTauxForPeriode: unable to get current taux, csMonnaie is undefined");

        }

        try {
            if (!JadeCodesSystemsUtil.checkCodeSystemType(ALCSTiers.GROUP_MONNAIE, csMonnaie)) {
                throw new ALCalculAdiModelException(
                        "CalculAdiBusinessServiceImpl#getTauxForPeriode: unable to get current taux, csMonnaie is not a valid cs");
            }
        } catch (Exception e) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTauxForPeriode:unable to check csMonnaie");
        }

        if (JadeStringUtil.isEmpty(periodeCalcul)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTauxForPeriode: unable to get current taux, periodeCalcul is undefined");

        }

        TauxMonnaieEtrangereModel tauxModel = new TauxMonnaieEtrangereModel();

        // on récupère la méthode de récupération du taux appliqué à la date du
        // jour
        ParameterModel methodeGetTaux = ParamServiceLocator.getParameterModelService().getParameterByName(
                ALConstParametres.APPNAME, ALConstParametres.TAUX_METHOD_DATE,
                JadeDateUtil.getGlobazFormattedDate(new Date()));

        TauxMonnaieEtrangereSearchModel tauxSearch = new TauxMonnaieEtrangereSearchModel();

        // si la méthode est période AF, on récupère le paramètre en passant la
        // période passée en param
        if (ALConstParametres.TAUX_METHOD_PERIODEAF.equals(methodeGetTaux.getValeurAlphaParametre())) {
            tauxSearch.setForDebutValiditeTaux(ALServiceLocator.getPeriodeAFBusinessService().getPeriodeDebutTrimestre(
                    periodeCalcul));
            // antérieur ou égal à la fin du trimestre correspondant à la
            // période
            tauxSearch.setForFinValiditeTaux(ALServiceLocator.getPeriodeAFBusinessService().getPeriodeFinTrimestre(
                    periodeCalcul));
        } else if (ALConstParametres.TAUX_METHOD_JOUR.equals(methodeGetTaux.getValeurAlphaParametre())) {
            tauxSearch.setForDebutValiditeTaux(ALServiceLocator.getPeriodeAFBusinessService().getPeriodeDebutTrimestre(
                    JadeStringUtil.substring(JadeDateUtil.getGlobazFormattedDate(new Date()), 3)));
            tauxSearch.setForFinValiditeTaux(ALServiceLocator.getPeriodeAFBusinessService().getPeriodeFinTrimestre(
                    JadeStringUtil.substring(JadeDateUtil.getGlobazFormattedDate(new Date()), 3)));

        }
        // type de la monnaie
        tauxSearch.setForTypeMonnaie(csMonnaie);

        tauxSearch.setWhereKey("rechercheDecompteAdi");

        tauxSearch = ALServiceLocator.getTauxMonnaieEtrangereModelService().search(tauxSearch);

        if (tauxSearch.getSize() == 0) {
            throw new ParamException(new KeyNameParameter(ALConstParametres.TAUX_EURO),
                    "Unable to get the taux - no taux found");
        }

        if (tauxSearch.getSize() > 1) {
            throw new ParamException(new KeyNameParameter(ALConstParametres.TAUX_EURO),
                    "Unable to get the taux - more one taux found");
        }
        tauxModel = (TauxMonnaieEtrangereModel) tauxSearch.getSearchResults()[0];

        return tauxModel.getTauxMonnaie();
    }

    @Override
    public String getTotalDecompte(AdiEnfantMoisComplexSearchModel searchComplexModel) throws JadePersistenceException,
            JadeApplicationException {
        if (searchComplexModel == null) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalDecompte : unable to calcul total, searchModel is empty");
        }

        Double sommeTotal = new Double(0);

        for (int i = 0; i < searchComplexModel.getSize(); i++) {

            sommeTotal = sommeTotal
                    + Double.parseDouble(((AdiEnfantMoisComplexModel) searchComplexModel.getSearchResults()[i])
                            .getAdiEnfantMoisModel().getMontantAdi());

        }

        // Format décimal 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(Math.round(sommeTotal / 0.05) * 0.05));

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adi.CalculAdiBusinessService#
     * getTotalVerseParEnfantAvecSupp(java.lang.String, java.lang.String)
     */
    @Override
    public String getTotalVerseParDroitAvecSupp(String idDecompte, String idDroit) throws JadePersistenceException,
            JadeApplicationException {

        if (JadeStringUtil.isIntegerEmpty(idDecompte)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalVerseParEnfantAvecSupp : unable to get total, idDecompte is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idDroit)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalVerseParEnfantAvecSupp : unable to get total, idDroit is empty");
        }

        AdiEnfantMoisComplexSearchModel searchAdiEnfantMois = new AdiEnfantMoisComplexSearchModel();
        searchAdiEnfantMois.setForIdDecompteAdi(idDecompte);
        searchAdiEnfantMois.setForIdDroit(idDroit);
        searchAdiEnfantMois = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(searchAdiEnfantMois);

        Double sommeTotal = new Double(0);
        // on additionne les montants par mois pour l'enfant dont l'id
        // correspond à idEnfant
        for (int i = 0; i < searchAdiEnfantMois.getSize(); i++) {

            sommeTotal = sommeTotal
                    + new Double(((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                            .getAdiEnfantMoisModel().getMontantAdi());

        }

        // Format décimal 2 chiffres après la virgule, arrondi au centime
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(Math.round(sommeTotal / 0.01) * 0.01));

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adi.CalculAdiBusinessService#
     * getTotalVerseParEnfantSansSupp(java.lang.String, java.lang.String)
     */
    @Override
    public String getTotalVerseParDroitSansSupp(String idDecompte, String idDroit) throws JadePersistenceException,
            JadeApplicationException {

        if (JadeStringUtil.isIntegerEmpty(idDecompte)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalVerseParEnfantAvecSupp : unable to get total, idDecompte is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idDroit)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalVerseParEnfantAvecSupp : unable to get total, idDroit is empty");
        }

        AdiEnfantMoisComplexSearchModel searchAdiEnfantMois = new AdiEnfantMoisComplexSearchModel();
        searchAdiEnfantMois.setForIdDecompteAdi(idDecompte);
        searchAdiEnfantMois.setForIdDroit(idDroit);
        searchAdiEnfantMois = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(searchAdiEnfantMois);

        Double sommeTotal = new Double(0);
        // on additionne les montants par mois pour l'enfant dont l'id
        // correspond à idEnfant
        for (int i = 0; i < searchAdiEnfantMois.getSize(); i++) {

            String montantAdi = ((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                    .getAdiEnfantMoisModel().getMontantAdi();

            String montantAllocCH = ((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                    .getAdiEnfantMoisModel().getMontantAllocCH();

            String montantCHTotal = ((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                    .getAdiEnfantMoisModel().getMontantCHTotal();

            Double ratioSansMenage = new Double(montantAllocCH) / new Double(montantCHTotal);

            sommeTotal = sommeTotal + (new Double(montantAdi) * new Double(ratioSansMenage));

        }

        // Format décimal 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(Math.round(sommeTotal / 0.01) * 0.01));

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adi.CalculAdiBusinessService#
     * getTotalVerseParEnfantAvecSupp(java.lang.String, java.lang.String)
     */
    @Override
    public String getTotalVerseParEnfantAvecSupp(String idDecompte, String idEnfant) throws JadePersistenceException,
            JadeApplicationException {

        if (JadeStringUtil.isIntegerEmpty(idDecompte)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalVerseParEnfantAvecSupp : unable to get total, idDecompte is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idEnfant)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalVerseParEnfantAvecSupp : unable to get total, idDroit is empty");
        }

        AdiEnfantMoisComplexSearchModel searchAdiEnfantMois = new AdiEnfantMoisComplexSearchModel();
        searchAdiEnfantMois.setForIdDecompteAdi(idDecompte);
        searchAdiEnfantMois.setForIdEnfant(idEnfant);
        searchAdiEnfantMois = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(searchAdiEnfantMois);

        Double sommeTotal = new Double(0);
        // on additionne les montants par mois pour l'enfant dont l'id
        // correspond à idEnfant
        for (int i = 0; i < searchAdiEnfantMois.getSize(); i++) {

            sommeTotal = sommeTotal
                    + new Double(((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                            .getAdiEnfantMoisModel().getMontantAdi());

        }

        // Format décimal 2 chiffres après la virgule, arrondi au centime
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(Math.round(sommeTotal / 0.01) * 0.01));

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adi.CalculAdiBusinessService#
     * getTotalVerseParEnfantSansSupp(java.lang.String, java.lang.String)
     */
    @Override
    public String getTotalVerseParEnfantSansSupp(String idDecompte, String idEnfant) throws JadePersistenceException,
            JadeApplicationException {

        if (JadeStringUtil.isIntegerEmpty(idDecompte)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalVerseParEnfantAvecSupp : unable to get total, idDecompte is empty");
        }
        if (JadeStringUtil.isIntegerEmpty(idEnfant)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalVerseParEnfantAvecSupp : unable to get total, idDroit is empty");
        }

        AdiEnfantMoisComplexSearchModel searchAdiEnfantMois = new AdiEnfantMoisComplexSearchModel();
        searchAdiEnfantMois.setForIdDecompteAdi(idDecompte);
        searchAdiEnfantMois.setForIdEnfant(idEnfant);
        searchAdiEnfantMois = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(searchAdiEnfantMois);

        Double sommeTotal = new Double(0);
        // on additionne les montants par mois pour l'enfant dont l'id
        // correspond à idEnfant
        for (int i = 0; i < searchAdiEnfantMois.getSize(); i++) {

            String montantAdi = ((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                    .getAdiEnfantMoisModel().getMontantAdi();

            String montantAllocCH = ((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                    .getAdiEnfantMoisModel().getMontantAllocCH();

            String montantCHTotal = ((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                    .getAdiEnfantMoisModel().getMontantCHTotal();

            Double ratioSansMenage = new Double(montantAllocCH) / new Double(montantCHTotal);

            sommeTotal = sommeTotal + (new Double(montantAdi) * new Double(ratioSansMenage));

        }

        // Format décimal 2 chiffres après la virgule
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(Math.round(sommeTotal / 0.01) * 0.01));

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.adi.CalculAdiBusinessService#getTotalVerseSupp (java.lang.String)
     */
    @Override
    public String getTotalVerseSupp(String idDecompte) throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isIntegerEmpty(idDecompte)) {
            throw new ALCalculAdiModelException(
                    "CalculAdiBusinessServiceImpl#getTotalVerseParEnfantAvecSupp : unable to get total, idDecompte is empty");
        }

        AdiEnfantMoisComplexSearchModel searchAdiEnfantMois = new AdiEnfantMoisComplexSearchModel();
        searchAdiEnfantMois.setForIdDecompteAdi(idDecompte);
        searchAdiEnfantMois = ALServiceLocator.getAdiEnfantMoisComplexModelService().search(searchAdiEnfantMois);

        Double sommeTotal = new Double(0);
        // on additionne les montants par mois pour l'enfant dont l'id
        // correspond à idEnfant
        for (int i = 0; i < searchAdiEnfantMois.getSize(); i++) {

            String montantAdi = ((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                    .getAdiEnfantMoisModel().getMontantAdi();

            String montantRepartiCH = ((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                    .getAdiEnfantMoisModel().getMontantRepartiCH();

            String montantCHTotal = ((AdiEnfantMoisComplexModel) searchAdiEnfantMois.getSearchResults()[i])
                    .getAdiEnfantMoisModel().getMontantCHTotal();

            Double ratioMenage = new Double(montantRepartiCH) / new Double(montantCHTotal);

            sommeTotal = sommeTotal + (new Double(montantAdi) * new Double(ratioMenage));

        }

        // Format décimal 2 chiffres après la virgule arrondi au centime
        DecimalFormat df = new DecimalFormat("0.00");
        return String.valueOf(df.format(Math.round(sommeTotal / 0.01) * 0.01));

    }
}
