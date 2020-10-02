package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.pcaccordee.CalculRetro;
import ch.globaz.pegasus.business.models.pcaccordee.CalculRetroSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.vo.decompte.PCAccordeeDecompteVO;

/**
 * @author DMA
 */
public class CalculMontantRetroActif {

    private CalculRetroSearch currentPCAccordeVersion = null;
    private String dateDebutPeriode = null;
    private String dateDernierPmt;
    private String dateFinDecomptePourRetro = null;
    private String dateFinPeriode = null;
    private String idDemande = null;
    private List<String> idsOldPca = new ArrayList<String>();
    private HashMap<String, CalculRetro> mapMoisNewPCAccordeConjoint;
    private HashMap<String, CalculRetro> mapMoisNewPCAccordeRequerant;
    private Map<String, CalculRetro> mapMoisOldPCAccordeConjoint = null;
    private Map<String, CalculRetro> mapMoisOldPCAccordeRequerant = null;
    private Map<String, String> mapMontantRetro = null;
    // MOntant retro effectif
    private Map<String, String> mapMontantRetroBrut = null;
    private Map<String, String> mapMontantVerse = null;
    private Map<String, PCAccordeeDecompteVO> mapOldPca = new TreeMap<String, PCAccordeeDecompteVO>();
    private String noVersionDroit = null;
    private CalculRetroSearch oldPCAccordeVersion = null;

    /**
     * A utiliser que pour les tests
     */
    public CalculMontantRetroActif() {

    }

    public CalculMontantRetroActif(String idDemande, String noVersionDroit) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException, DecisionException, JadePersistenceException,
            PCAccordeeException {
        this.idDemande = idDemande;
        this.noVersionDroit = noVersionDroit;
        dateDernierPmt = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
        initSearch();

        if (currentPCAccordeVersion.getSearchResults().length == 0) {
            throw new PCAccordeeException("No pca was found");
        }

        dateFinDecomptePourRetro = ((CalculRetro) currentPCAccordeVersion.getSearchResults()[0]).getDateDecision();
        initListMois();
        initMapMontantRetro();
    }

    private void addPCAinMap(HashMap<String, CalculRetro> moisOldPCAccordeRequerant, CalculRetro CalculRetro,
                             String dateMois) throws PCAccordeeException {
        if (moisOldPCAccordeRequerant.containsKey(dateMois)) {
            throw new PCAccordeeException("The key '" + dateMois
                    + "' is already used that mean exist many pca for the same periode");
        }
        moisOldPCAccordeRequerant.put(dateMois, CalculRetro);
    }

    public String createDateForMap(CalculRetro calculRetro, int i) {
        return this.createDateForMap(calculRetro.getDateDebut(), i);
    }

    public String createDateForMap(String date, int i) {
        String dateKey = null;
        Calendar cal = JadeDateUtil.getGlobazCalendar("01." + date);
        cal.add(Calendar.MONTH, i);

        dateKey = cal.get(Calendar.YEAR)
                + JadeStringUtil.rightJustifyInteger(String.valueOf(cal.get(Calendar.MONTH) + 1), 2);
        return dateKey;
    }

    private void fillOldPcaVo(Map<String, CalculRetro> mapMoisOldPCAccorde, BigDecimal montantPca, String keyDate) {
        PCAccordeeDecompteVO pca = null;
        CalculRetro cal = mapMoisOldPCAccorde.get(keyDate);
        String date = keyDate.substring(4) + "." + keyDate.substring(0, 4);
        // String dateFin = keyDate.substring(4) + "." + keyDate.substring(0, 4);
        if (cal != null) {
            if (mapOldPca.containsKey(cal.getIdPCAccordee())) {
                pca = mapOldPca.get(cal.getIdPCAccordee());
            } else {
                pca = new PCAccordeeDecompteVO();
                pca.setDateDebutPeriode(date);
                pca.setDateFinPeriode(date);
                pca.setMontantPcaMensuel(montantPca);
                pca.setNbreMois(0);
                pca.setCsGenrePCA(cal.getCsGenrePC());
                pca.setDescTiers(getDescriptionTier(mapMoisOldPCAccorde.get(keyDate)));
                pca.setIdPca(cal.getIdPCAccordee());
                mapOldPca.put(cal.getIdPCAccordee(), pca);
                idsOldPca.add(pca.getIdPca());
            }

            if (mapMoisOldPCAccorde.get(keyDate) != null) {

            }

            if (JadeDateUtil.isDateMonthYearBefore(date, pca.getDateDebutPeriode())) {
                pca.setDateDebutPeriode(date);
            }
            pca.setNbreMois(pca.getNbreMois() + 1);
            if (JadeDateUtil.isDateMonthYearAfter(date, pca.getDateFinPeriode())) {
                pca.setDateFinPeriode(date);
            }
            pca.setMontantForPeriod(pca.getMontantForPeriod().add(montantPca));
        }
    }

    public CalculRetroSearch getCurrentPCAccordeVersion() {
        return currentPCAccordeVersion;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateDernierPmt() {
        return dateDernierPmt;
    }

    public String getDateFinDecomptePourRetro() {
        return dateFinDecomptePourRetro;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    private String getDescriptionTier(CalculRetro retro) {

        String descTiers = retro.getNom() + " " + retro.getPrenom();

        if (!JadeStringUtil.isIntegerEmpty(retro.getSimplePCAccordee().getIdPrestationAccordeeConjoint())) {
            descTiers = descTiers + " " + JadeThread.getMessage("pegasus.et") + " " + retro.getNomConj()
                    + retro.getPrenomConj();
        }
        return descTiers;

    }

    public List<String> getIdsOldPca() {
        return idsOldPca;
    }

    public String getIdTiersConjoint() {

        for (CalculRetro cr : getMapMoisOldPCAccordeConjoint().values()) {
            if (!JadeStringUtil.isBlankOrZero(cr.getIdTiersBeneficiaire())) {
                return cr.getIdTiersBeneficiaire();
            }
        }

        for (CalculRetro cr : getMapMoisNewPCAccordeConjoint().values()) {
            if (!JadeStringUtil.isBlankOrZero(cr.getIdTiersBeneficiaire())) {
                return cr.getIdTiersBeneficiaire();
            }
        }

        return null;

    }

    public List<PCAccordeeDecompteVO> getListPcaVoReq() {
        List<PCAccordeeDecompteVO> list = new ArrayList<PCAccordeeDecompteVO>(mapOldPca.values());
        Collections.reverse(list);
        return list;
    }

    public HashMap<String, CalculRetro> getMapMoisNewPCAccordeConjoint() {
        return mapMoisNewPCAccordeConjoint;
    }

    public HashMap<String, CalculRetro> getMapMoisNewPCAccordeRequerant() {
        return mapMoisNewPCAccordeRequerant;
    }

    public Map<String, CalculRetro> getMapMoisOldPCAccordeConjoint() {
        return mapMoisOldPCAccordeConjoint;
    }

    public Map<String, CalculRetro> getMapMoisOldPCAccordeRequerant() {
        return mapMoisOldPCAccordeRequerant;
    }

    public Map<String, String> getMapMontantRetro() {
        return mapMontantRetro;
    }

    public Map<String, String> getMapMontantRetroBrut() {
        return mapMontantRetroBrut;
    }

    public Map<String, String> getMapMontantVerse() {
        return mapMontantVerse;
    }

    public String getNoVersionDroit() {
        return noVersionDroit;
    }

    public CalculRetroSearch getOldPCAccordeVersion() {
        return oldPCAccordeVersion;
    }

    public BigDecimal getTotalRetro() {
        BigDecimal montant = new BigDecimal(0);
        for (String idPca : mapMontantRetro.keySet()) {
            montant = montant.add(new BigDecimal(mapMontantRetro.get(idPca)));
        }
        return montant;
    }

    /**
     * Decoupe les prestation acc en mois
     *
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PmtMensuelException
     * @throws JadePersistenceException
     * @throws DecisionException
     * @throws PCAccordeeException
     */
    private void initListMois() throws PmtMensuelException, JadeApplicationServiceNotAvailableException,
            DecisionException, JadePersistenceException, PCAccordeeException {

        HashMap<String, CalculRetro> moisOldPCAccordeRequerant = new HashMap<String, CalculRetro>();
        HashMap<String, CalculRetro> moisOldPCAccordeConjoint = new HashMap<String, CalculRetro>();

        splitPCAInMois(moisOldPCAccordeRequerant, moisOldPCAccordeConjoint, oldPCAccordeVersion);

        mapMoisOldPCAccordeRequerant = moisOldPCAccordeRequerant;
        mapMoisOldPCAccordeConjoint = moisOldPCAccordeConjoint;

        HashMap<String, CalculRetro> moisCAccordeRequerant = new HashMap<String, CalculRetro>();
        HashMap<String, CalculRetro> moisPCAccordeConjoint = new HashMap<String, CalculRetro>();

        splitPCAInMois(moisCAccordeRequerant, moisPCAccordeConjoint, currentPCAccordeVersion);

        setMapMoisNewPCAccordeRequerant(moisCAccordeRequerant);
        setMapMoisNewPCAccordeConjoint(moisPCAccordeConjoint);
    }

    private void initMapMontantRetro() throws PmtMensuelException, JadeApplicationServiceNotAvailableException,
            DecisionException, JadePersistenceException, PCAccordeeException {

        mapMontantRetro = new HashMap<String, String>();
        mapMontantRetroBrut = new HashMap<String, String>();
        mapMontantVerse = new HashMap<String, String>();
        // Retro, avec gestion old pca
        BigDecimal retroRequerant = null;
        BigDecimal retroConjoint = null;
        // Retro brut effectif, sans comparaison
        BigDecimal retroRequerantBrut = null;
        BigDecimal retroConjointBrut = null;

        BigDecimal retro = null;

        boolean hasConjointInPCA = mapMoisOldPCAccordeConjoint.size() > 0;

        HashMap<String, CalculRetro> moisCAccordeRequerant = new HashMap<String, CalculRetro>();
        HashMap<String, CalculRetro> moisPCAccordeConjoint = new HashMap<String, CalculRetro>();

        splitPCAInMois(moisCAccordeRequerant, moisPCAccordeConjoint, currentPCAccordeVersion);

        for (JadeAbstractModel model : currentPCAccordeVersion.getSearchResults()) {
            CalculRetro calculRetro = ((CalculRetro) model);
            CalculRetro calculRetroConojoint = null;
            retroConjoint = new BigDecimal(0);
            retroConjointBrut = new BigDecimal(0);
            retroRequerant = new BigDecimal(0);
            retroRequerantBrut = new BigDecimal(0);
            BigDecimal montantMoisRequerant = new BigDecimal(0);
            // Retro brut effectif, sans comparaison
            BigDecimal montantMoisRequerantBrut = new BigDecimal(0);
            BigDecimal montantAlreadyPaidReq = new BigDecimal(0);
            BigDecimal montantAlreadyPaidCon = new BigDecimal(0);

            if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(calculRetro.getCsRoleBeneficiaire())) {

            retro = new BigDecimal(0);
            int nbMonth = nbMonth(calculRetro);
            PCAccordeeDecompteVO pcaVoReq = new PCAccordeeDecompteVO();
            PCAccordeeDecompteVO pcaVoConj = new PCAccordeeDecompteVO();

            Integer nbMonthRequ = 0;
            for (int i = 0; i < nbMonth; i++) {
                String keyDate = this.createDateForMap(calculRetro, i);
                calculRetroConojoint = moisPCAccordeConjoint.get(keyDate);

                if (mapMoisOldPCAccordeRequerant.get(keyDate) != null) {

                    montantMoisRequerant = new BigDecimal(calculRetro.getMontantPCMensuelle())
                            .subtract(new BigDecimal(mapMoisOldPCAccordeRequerant.get(keyDate)
                                    .getMontantPCMensuelle()));
                    // Retro effectif brut
                    montantMoisRequerantBrut = new BigDecimal(calculRetro.getMontantPCMensuelle());

                    montantAlreadyPaidReq = montantAlreadyPaidReq.add(new BigDecimal(mapMoisOldPCAccordeRequerant
                            .get(keyDate).getMontantPCMensuelle()));

                    fillOldPcaVo(mapMoisOldPCAccordeRequerant,
                            new BigDecimal(mapMoisOldPCAccordeRequerant.get(keyDate).getMontantPCMensuelle()),
                            keyDate);
                    retroRequerant = retroRequerant.add(montantMoisRequerant);
                    // Retro effectif brut

                    retroRequerantBrut = retroRequerantBrut.add(montantMoisRequerantBrut);
                    // droitInitial
                } else if (calculRetro.getNoVersion().equals("1")) {

                    BigDecimal montanPCMensuel = new BigDecimal(calculRetro.getMontantPCMensuelle());
                    fillOldPcaVo(mapMoisOldPCAccordeRequerant, montanPCMensuel, keyDate);
                    retroRequerant = retroRequerant.add(montanPCMensuel);
                    // Retro effectif brut
                    retroRequerantBrut = retroRequerantBrut.add(montanPCMensuel);

                } else {

                    // ATTENTION: normalement se cas ne devrai pas arrivé. Mais du a la reprise de donné on peut
                    // tomber dans ce cas
                    // On n'a pas trouvé d'ancien PCA. On ne peut donc pas faire de deduction
                    montantMoisRequerantBrut = new BigDecimal(calculRetro.getMontantPCMensuelle());

                    retroRequerant = retroRequerant.add(montantMoisRequerantBrut);

                    // Retro effectif brut
                    retroRequerantBrut = retroRequerantBrut.add(montantMoisRequerantBrut);
                    fillOldPcaVo(mapMoisOldPCAccordeRequerant, montantMoisRequerantBrut, keyDate);
                    // throw new PCAccordeeException("Any pca founded for this date " + keyDate);
                }

                if (calculRetroConojoint != null) {
                    if (mapMoisOldPCAccordeConjoint.get(keyDate) != null) {

                        retroConjoint = retroConjoint.add(new BigDecimal(calculRetroConojoint
                                .getMontantPCMensuelle()).subtract(new BigDecimal(mapMoisOldPCAccordeConjoint.get(
                                keyDate).getMontantPCMensuelle())));
                        // montant brut retro effectif conjoint
                        retroConjointBrut = retroConjointBrut.add(new BigDecimal(calculRetroConojoint
                                .getMontantPCMensuelle()));
                        montantAlreadyPaidCon = montantAlreadyPaidCon.add(new BigDecimal(
                                mapMoisOldPCAccordeConjoint.get(keyDate).getMontantPCMensuelle()));

                        fillOldPcaVo(mapMoisOldPCAccordeConjoint,
                                (new BigDecimal(mapMoisOldPCAccordeConjoint.get(keyDate).getMontantPCMensuelle())),
                                keyDate);

                        // droitInitial
                    } else if (calculRetro.getNoVersion().equals("1")) {
                        retroConjoint = retroConjoint.add(new BigDecimal(calculRetroConojoint
                                .getMontantPCMensuelle()));
                        // montant brut retro effectif conjoint
                        retroConjointBrut = retroConjointBrut.add(new BigDecimal(calculRetroConojoint
                                .getMontantPCMensuelle()));

                        fillOldPcaVo(mapMoisOldPCAccordeConjoint,
                                new BigDecimal(calculRetroConojoint.getMontantPCMensuelle()), keyDate);
                    } else { // on est dans le cas ou la séparation (maladie) vient d'ariver.
                        // On n'a donc pas d'ancien pca pour comparer on prend la pca du requerant.
                        BigDecimal montantMoisConjoint = new BigDecimal(
                                calculRetroConojoint.getMontantPCMensuelle());
                        // montant brut effectif SANS DEDUCTION!!!
                        // retroConjointBrut = retroConjointBrut.add(montantMoisConjoint);
                        // Si on a pas tout pris aux requrant on prend le reste
                        if (montantMoisRequerant.doubleValue() < 0) {
                            montantMoisConjoint = montantMoisConjoint.add(montantMoisRequerant);
                            if (montantMoisConjoint.doubleValue() < 0) {
                                // On remet le retro a 0
                                retroRequerant.subtract(montantMoisRequerant);
                            }
                        }
                        retroConjoint = retroConjoint.add(montantMoisConjoint);
                        retroConjointBrut = retroConjointBrut.add(new BigDecimal(calculRetroConojoint
                                .getMontantPCMensuelle()));

                        fillOldPcaVo(mapMoisOldPCAccordeConjoint,
                                new BigDecimal(calculRetroConojoint.getMontantPCMensuelle()), keyDate);

                    }
                } else {
                    calculRetroConojoint = mapMoisOldPCAccordeConjoint.get(keyDate);
                    // On vas traité le cas d'un passage d'un couple séparé par la maladie à un DOM2R
                    if (calculRetroConojoint != null) {
                        BigDecimal montantMoisConjoint = new BigDecimal(
                                calculRetroConojoint.getMontantPCMensuelle());
                        fillOldPcaVo(mapMoisOldPCAccordeConjoint, montantMoisConjoint, keyDate);

                        montantAlreadyPaidCon = montantAlreadyPaidCon.add(montantMoisConjoint);
                        retroRequerant = retroRequerant.subtract(montantMoisConjoint);
                        // Si le rétro est positif il faut déduire le montant
                        if (retroRequerant.signum() == 1) {
                            // retroRequerant.subtract(montantMoisConjoint);
                            // } else {
                            // retroRequerant.add(montantMoisConjoint);
                        }

                    }
                }
            }

            mapMontantRetro.put(calculRetro.getIdPCAccordee(), retroRequerant.toString());
            // Montant retro brut effectif
            mapMontantRetroBrut.put(calculRetro.getIdPCAccordee(), retroRequerantBrut.toString());
            mapMontantVerse.put(calculRetro.getIdPCAccordee(), montantAlreadyPaidReq.toString());
            if (calculRetroConojoint != null) {
                mapMontantRetro.put(calculRetroConojoint.getIdPCAccordee(), retroConjoint.toString());
                // Montant retro brut effectif
                mapMontantRetroBrut.put(calculRetroConojoint.getIdPCAccordee(), retroConjointBrut.toString());
                mapMontantVerse.put(calculRetroConojoint.getIdPCAccordee(), montantAlreadyPaidCon.toString());

            }
        }
        }
    }

    private void initSearch() throws JadeApplicationServiceNotAvailableException, PCAccordeeException,
            JadePersistenceException {

        currentPCAccordeVersion = searchCurrentPCAccordeVersion();
        // String dateMin = null;
        // String dateMax = null;
        // boolean isDateFinEmty = false;
        // for (JadeAbstractModel model : this.currentPCAccordeVersion.getSearchResults()) {
        // CalculRetro retro = (CalculRetro) model;
        // if ((dateMin == null) || JadeDateUtil.isDateMonthYearAfter(dateMin, retro.getDateDebut())) {
        // dateMin = retro.getDateDebut();
        // }
        // if ((dateMax == null) || JadeStringUtil.isBlankOrZero(retro.getDateFin())
        // || JadeDateUtil.isDateMonthYearBefore(dateMax, retro.getDateFin())) {
        // if (JadeStringUtil.isBlankOrZero(retro.getDateFin())) {
        // isDateFinEmty = true;
        // }
        // if (!isDateFinEmty) {
        // dateMax = retro.getDateFin();
        // }
        // }
        //
        // }

        setDateDebutAndDateFin(currentPCAccordeVersion);
        oldPCAccordeVersion = searchOldPCAccordeVersion(dateDebutPeriode, dateFinPeriode);

    }

    public int nbMonth(CalculRetro calculRetro) throws PmtMensuelException,
            JadeApplicationServiceNotAvailableException, DecisionException, JadePersistenceException {

        String dateFin = calculRetro.getDateFin();
        if (JadeStringUtil.isEmpty(dateFin)) {
            // TODO a tester
            if (IPCPCAccordee.ETAT_PCA_COURANT_VALIDE.equals(calculRetro.getCsEtatPC())) {

                dateFin = JadeDateUtil.addMonths(calculRetro.getDateProchainPaiement(), -1);

            } else {
                if (!JadeStringUtil.isBlankOrZero(dateFinDecomptePourRetro)) {
                    dateFin = dateFinDecomptePourRetro.substring(3);
                } else {
                    if (dateDernierPmt == null) {
                        dateDernierPmt = PegasusServiceLocator.getPmtMensuelService().getDateDernierPmt();
                    }
                    dateFin = dateDernierPmt;
                }
            }
        }
        String dateFinModif = "01." + dateFin;
        String dateDebutModif = "01." + calculRetro.getDateDebut();
        int nbMonth = JadeDateUtil.getNbMonthsBetween(dateDebutModif, dateFinModif);

        // on donne le nombre de mois entre les deux date + 1 (car du 12.2010 au 12.2010, il n'y a pas de mois entre les
        // deux dates, mais nous avons bien paye un mois)
        // if (JadeDateUtil.areDatesEquals(dateDebutModif, dateFinModif)) {
        // if (!JadeDateUtil.areDatesEquals(dateDebutModif, dateDernierPaiement)) {
        nbMonth = nbMonth + 1;
        // }
        // }

        return nbMonth;
    }

    private CalculRetroSearch searchCurrentPCAccordeVersion() throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        CalculRetroSearch search = new CalculRetroSearch();

        search.setForIdDemande(idDemande);
        search.setForNoVersion(noVersionDroit);
        search.setForExcludePcaPartiel(IPCPCAccordee.ETAT_PCA_COURANT_VALIDE);// Partiel
        search.setWhereKey(CalculRetroSearch.FOR_CURRENT_VERSIONED_WITHOUT_COPIE);
        return (CalculRetroSearch) JadePersistenceManager.search(search);
    }

    private CalculRetroSearch searchOldPCAccordeVersion(String dateMin, String dateMax) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (JadeStringUtil.isBlankOrZero(dateMax)) {
            // try {
            dateMax = dateMin;// PegasusServiceLocator.getPmtMensuelService().getDateProchainPmt();
            // } catch (PmtMensuelException e) {
            // throw new PCAccordeeException("Unabeled to optain the dateProchainPmt", e);
            // }
        }
        CalculRetroSearch search = new CalculRetroSearch();
        search.setForDateMax(dateMax);
        search.setForIdDemande(idDemande);
        search.setForNoVersion(noVersionDroit);
        search.setForDateMin(dateMin);
        search.setWhereKey(CalculRetroSearch.FOR_OLD_VERSIONED_PCA_WITH_MONTANT_MENSUELLE_FOR_DECOMPTE);
        return (CalculRetroSearch) JadePersistenceManager.search(search);
    }

    public void setCurrentPCAccordeVersion(CalculRetroSearch currentPCAccordeVersion) {
        this.currentPCAccordeVersion = currentPCAccordeVersion;
    }

    private void setDateDebutAndDateFin(CalculRetroSearch search) {
        String dateMin = null;
        String dateMax = null;
        boolean isDateFinEmty = false;
        for (JadeAbstractModel model : search.getSearchResults()) {
            CalculRetro retro = (CalculRetro) model;
            if ((dateMin == null) || JadeDateUtil.isDateMonthYearAfter(dateMin, retro.getDateDebut())) {
                dateMin = retro.getDateDebut();
            }
            if ((dateMax == null) || JadeStringUtil.isBlankOrZero(retro.getDateFin())
                    || JadeDateUtil.isDateMonthYearBefore(dateMax, retro.getDateFin())) {
                if (JadeStringUtil.isBlankOrZero(retro.getDateFin())) {
                    isDateFinEmty = true;
                    dateMax = null;
                }
                if (!isDateFinEmty) {
                    dateMax = retro.getDateFin();
                }
            }
        }
        dateDebutPeriode = dateMin;
        dateFinPeriode = dateMax;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public void setDateDernierPmt(String dateDernierPmt) {
        this.dateDernierPmt = dateDernierPmt;
    }

    public void setDateFinDecomptePourRetro(String dateFinDecomptePourRetro) {
        this.dateFinDecomptePourRetro = dateFinDecomptePourRetro;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public void setMapMoisNewPCAccordeConjoint(HashMap<String, CalculRetro> mapMoisNewPCAccordeConjoint) {
        this.mapMoisNewPCAccordeConjoint = mapMoisNewPCAccordeConjoint;
    }

    public void setMapMoisNewPCAccordeRequerant(HashMap<String, CalculRetro> mapMoisNewPCAccordeRequerant) {
        this.mapMoisNewPCAccordeRequerant = mapMoisNewPCAccordeRequerant;
    }

    public void setMapMoisOldPCAccordeConjoint(Map<String, CalculRetro> mapMoisOldPCAccordeConjoint) {
        this.mapMoisOldPCAccordeConjoint = mapMoisOldPCAccordeConjoint;
    }

    public void setMapMoisOldPCAccordeRequerant(Map<String, CalculRetro> mapMoisOldPCAccordeRequerant) {
        this.mapMoisOldPCAccordeRequerant = mapMoisOldPCAccordeRequerant;
    }

    public void setMapMontantRetro(Map<String, String> mapMontantRetro) {
        this.mapMontantRetro = mapMontantRetro;
    }

    public void setMapMontantRetroBrut(Map<String, String> mapMontantRetroBrut) {
        this.mapMontantRetroBrut = mapMontantRetroBrut;
    }

    public void setMapMontantVerse(Map<String, String> mapMontantVerse) {
        this.mapMontantVerse = mapMontantVerse;
    }

    public void setNoVersionDroit(String noVersionDroit) {
        this.noVersionDroit = noVersionDroit;
    }

    public void setOldPCAccordeVersion(CalculRetroSearch oldPCAccordeVersion) {
        this.oldPCAccordeVersion = oldPCAccordeVersion;
    }

    private void splitPCAInMois(HashMap<String, CalculRetro> moisPCAccordeRequerant,
                                HashMap<String, CalculRetro> moisPCAccordeConjoint, CalculRetroSearch CalculRetroSearch)
            throws PmtMensuelException, JadeApplicationServiceNotAvailableException, DecisionException,
            JadePersistenceException, PCAccordeeException {
        for (JadeAbstractModel model : CalculRetroSearch.getSearchResults()) {
            CalculRetro calculRetro = ((CalculRetro) model);
            int nbMonth = nbMonth(calculRetro);
            for (int i = 0; i < nbMonth; i++) {
                String dateMois = this.createDateForMap(calculRetro, i);

                if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(calculRetro.getCsRoleBeneficiaire())) {
                    // final String csRoleBeneficiaire = (isConjoint ? IPCDroits.CS_ROLE_FAMILLE_CONJOINT : I);
                    addPCAinMap(moisPCAccordeRequerant, calculRetro, dateMois);
                } else {
                    addPCAinMap(moisPCAccordeConjoint, calculRetro, dateMois);
                }
            }
        }
    }

    //
    public boolean wasSeparerParLaMaladie(CalculRetro pcAccordeePlanCalcul) throws PCAccordeeException,
            NumberFormatException {

        String year = pcAccordeePlanCalcul.getSimplePCAccordee().getDateDebut().substring(3);
        String month = pcAccordeePlanCalcul.getSimplePCAccordee().getDateDebut().substring(0, 2);

        String keyDate = year + month;
        CalculRetro oldPcaConjoint = getMapMoisOldPCAccordeConjoint().get(keyDate);

        CalculRetro newdPcaConjoint = getMapMoisNewPCAccordeConjoint().get(keyDate);
        if ((oldPcaConjoint != null) && (newdPcaConjoint == null)) {
            return true;
        }
        return false;

    }
    //
    // }
    // boolean passageSeparerADomicile = (this.getMapMoisOldPCAccordeConjoint().get(
    // this.createDateForMap(pcAccordeePlanCalcul, 0)) != null)
    // && !JadeStringUtil.isBlankOrZero(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
    // .getIdDecisionConjoint());
    //
    // }
}
