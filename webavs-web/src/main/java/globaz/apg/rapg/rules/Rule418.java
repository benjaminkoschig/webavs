package globaz.apg.rapg.rules;

import ch.globaz.common.domaine.Periode;
import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.util.Dates;
import com.google.common.annotations.VisibleForTesting;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.*;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.utils.APGUtils;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Si entre le premier et le dernier jour de congé de paternité ou de congé de proche-aidant :
 * Le nombre de jours avec code de service ? 91
 * + nombre de jours avec code de service = 91
 * > Nombre de jours entre la période de début et de fin du congé de paternité ?  erreur
 * Si nombre de jours avec type de service ? 92
 * + nombre de jours avec type de service = 92
 * > Nombre de jours entre la période de début et de fin du congé de paternité ?  erreur
 *
 * @author von
 */
public class Rule418 extends Rule {

    private String startOfPeriod;
    private String endOfPeriod;
    private String idDroit;
    private int numberOfDays;

    /**
     * @param errorCode
     */
    public Rule418(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException {

        String serviceType = champsAnnonce.getServiceType();
        setRuleData(champsAnnonce.getStartOfPeriod(), champsAnnonce.getEndOfPeriod(), champsAnnonce.getIdDroit(), champsAnnonce.getNumberOfDays());
        APPeriodeAPG periodeAPGCalculed = createPeriodeAPGCalculed(startOfPeriod, endOfPeriod, idDroit);
        String nss = champsAnnonce.getInsurant();
        if(!(serviceType.equals(APGenreServiceAPG.Maternite.getCodePourAnnonce()) || APGenreServiceAPG.isValidGenreServicePandemie(serviceType))) {

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);
            // Si le droit à contrôler est un droit APG,
            // on récupère le 1er droit Paternité ou Proche aidant dont la période chevauche la période du droit APG.
            if (!serviceType.equals(APGenreServiceAPG.Paternite.getCodePourAnnonce()) && !serviceType.equals(APGenreServiceAPG.ProcheAidant.getCodePourAnnonce())) {
                List<APDroitAvecParent> droitsPaternite = rechercherDroitPatPai(idDroit, nss, periodeAPGCalculed, etatIndesirable, IPRDemande.CS_TYPE_PATERNITE);
                APPeriodeAPG periode = getPeriodeQuiChevauche(periodeAPGCalculed, droitsPaternite);
                serviceType = APGenreServiceAPG.Paternite.getCodePourAnnonce();
                if (periode == null) {
                    List<APDroitAvecParent> droitsProcheAidant = rechercherDroitPatPai(idDroit, nss, periodeAPGCalculed, etatIndesirable, IPRDemande.CS_TYPE_PROCHE_AIDANT);
                    periode = getPeriodeQuiChevauche(periodeAPGCalculed, droitsProcheAidant);
                    serviceType = APGenreServiceAPG.ProcheAidant.getCodePourAnnonce();
                }
                if(periode != null) {
                    // Si le droit à contrôler est un droit paternité,
                    // on récupère là 1ere droit Paternité ou Proche aidant dont la période chevauche la période du droit APG.
                    Optional<APPrestation> prestation = getPrestationDuDroit(getSession(), periode.getIdDroit()).stream().findFirst();
                    if (prestation.isPresent()) {
                        APPrestation prest = prestation.get();
                        setRuleData(prest.getDateDebut(), prest.getDateFin(), prest.getIdDroit(), prest.getNombreJoursSoldes());
                    }
                }
            }

            int nombreJoursPat = serviceType.equals(APGenreServiceAPG.Paternite.getCodePourAnnonce()) ? numberOfDays : 0;
            int nombreJoursProcheAidant = serviceType.equals(APGenreServiceAPG.ProcheAidant.getCodePourAnnonce()) ? numberOfDays : 0;
            Date dateDebut = JadeDateUtil.getGlobazDate(startOfPeriod);
            Date dateFin = JadeDateUtil.getGlobazDate(endOfPeriod);
            periodeAPGCalculed = createPeriodeAPGCalculed(startOfPeriod,endOfPeriod,idDroit);
            validNotEmpty(nss, "NSS");
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");

            int nombreJoursMax = getNombreJoursMax(dateDebut, dateFin);
            int nombreJoursEffectueNonPat = getNombreJoursEffectueNonPat(nss, idDroit, periodeAPGCalculed, etatIndesirable);
            if (serviceType.equals(APGenreServiceAPG.Paternite.getCodePourAnnonce())) {
                nombreJoursPat += rechercherNombreJourPaternite(idDroit, nss, periodeAPGCalculed, etatIndesirable);
            } else if (serviceType.equals(APGenreServiceAPG.ProcheAidant.getCodePourAnnonce())) {
                nombreJoursProcheAidant += rechercherNombreJourProcheAidant(idDroit, nss, periodeAPGCalculed, etatIndesirable);
            }
            return (nombreJoursEffectueNonPat + nombreJoursPat + nombreJoursProcheAidant <= nombreJoursMax);
        }

        return true;
    }

    private APPeriodeAPG createPeriodeAPGCalculed(String start, String end, String idDroit) {
        APPeriodeAPG periode = new APPeriodeAPG();
        periode.setDateDebutPeriode(start);
        periode.setDateFinPeriode(end);
        periode.setIdDroit(idDroit);
        return periode;
    }

    private void setRuleData(String start, String end, String id, String days) {
        startOfPeriod = start;
        endOfPeriod = end;
        idDroit = id;
        numberOfDays = Integer.parseInt(days);
    }

    private int getNombreJoursMax(Date dateDebut, Date dateFin) {
        int nombreJoursMax;

        if (dateDebut.equals(dateFin)) {
            nombreJoursMax = 1;
        } else if (dateFin != null && dateFin.getTime() >= dateDebut.getTime()) {
            double msDiff = (double) (dateFin.getTime() - dateDebut.getTime());
            nombreJoursMax = ((int) Math.round(msDiff / 8.64E7D)) + 1;
        } else {
            nombreJoursMax = 0;
        }

        return nombreJoursMax;
    }

    private int getNombreJoursEffectueNonPat(String nss, String idDroit, APPeriodeAPG periodeAPGCalculed, List<String> etatIndesirable) throws APRuleExecutionException {
        APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
        manager.setSession(getSession());
        manager.setLikeNumeroAvs(nss);
        manager.setForEtatDroitNotIn(etatIndesirable);
        List<APDroitAvecParent> droitsTries = null;
        try {
            manager.find(BManager.SIZE_NOLIMIT);
            List<APDroitAvecParent> tousLesDroits = manager.toList();
            droitsTries = skipDroitParent(tousLesDroits);
        } catch (Exception e) {
            throwRuleExecutionException(e);
        }
        int nombreJoursEffectueNonPat = 0;
        // On va trouver tous les droits, même celui qu'on vient de créer,
        // on additionne tous les jours soldées
        if(droitsTries != null) {
            for (APDroitAvecParent droitsTry : droitsTries) {
                APDroitAPGJointTiers droit = (APDroitAPGJointTiers) droitsTry;
                if(!APGUtils.isTypeAllocationPandemie(droit.getCsGenreService())) {
                    for (JadePeriodWrapper periodePat : droit.getPeriodes()) {
                        int nbreJoursAAjouter = getNombreJoursAAjouter(periodeAPGCalculed, periodePat.getDateDebut(), periodePat.getDateFin());
                        if (!droit.getIdDroit().equals(idDroit)) {
                            nombreJoursEffectueNonPat += nbreJoursAAjouter;
                        }
                    }
                }
            }
        }
        return nombreJoursEffectueNonPat;
    }

    /**
     * Trouve la 1ère periode qui chevauche la période fourni dans la liste des droit fourni-
     * @param periodeAPG : Periode fournie
     * @param droits : Droits à controler
     *
     * @return La 1ère période qui chevauche la période fournie.
     */
    private APPeriodeAPG getPeriodeQuiChevauche(APPeriodeAPG periodeAPG, List<APDroitAvecParent> droits) {
        for (APDroitAvecParent droitTiers : droits) {
            APPeriodeAPGManager periodeAPGManager = new APPeriodeAPGManager();
            periodeAPGManager.setSession(this.getSession());
            periodeAPGManager.setForIdDroit(droitTiers.getIdDroit());
            Exceptions.checkedToUnChecked(() -> periodeAPGManager.find(BManager.SIZE_NOLIMIT));
            for (APPeriodeAPG periode : periodeAPGManager.<APPeriodeAPG>getContainerAsList()) {
                if (chevauchementDePeriode(periodeAPG.getDateDebutPeriode(), periodeAPG.getDateFinPeriode(), periode.getDateDebutPeriode(), periode.getDateFinPeriode())) {
                    return periode;
                }
            }
        }
        return null;
    }

    public List<APPrestation> getPrestationDuDroit(final BSession session,
                                                   final String idDroit) {
        final APPrestationManager prestationManager = new APPrestationManager();
        prestationManager.setSession(session);
        prestationManager.setForIdDroit(idDroit);
        Exceptions.checkedToUnChecked(() -> prestationManager.find(BManager.SIZE_NOLIMIT));
        return prestationManager.toList();
    }

    private Integer rechercherNombreJourPaternite(String idDroit,
                                                     String nss,
                                                     APPeriodeAPG periodeAPGCalculed,
                                                     List<String> etatIndesirable) throws APRuleExecutionException {

        return getTotalNombreJoursAAjouter(rechercherDroitPatPai(idDroit, nss, periodeAPGCalculed, etatIndesirable, IPRDemande.CS_TYPE_PATERNITE), periodeAPGCalculed);
    }

    private Integer rechercherNombreJourProcheAidant(String idDroit,
                                                     String nss,
                                                     APPeriodeAPG periodeAPGCalculed,
                                                     List<String> etatIndesirable) throws APRuleExecutionException {

        return getTotalNombreJoursAAjouter(rechercherDroitPatPai(idDroit, nss, periodeAPGCalculed, etatIndesirable, IPRDemande.CS_TYPE_PROCHE_AIDANT), periodeAPGCalculed);
    }

    private List<APDroitAvecParent> rechercherDroitPatPai(String idDroit,
                                                              String nss,
                                                              APPeriodeAPG periodeAPGCalculed,
                                                              List<String> etatIndesirable,
                                                              String typeDroit) throws APRuleExecutionException {
        APDroitLAPGJointTiersManager apDroitLAPGJointTiersManager = new APDroitLAPGJointTiersManager();
        apDroitLAPGJointTiersManager.setSession(this.getSession());
        apDroitLAPGJointTiersManager.setLikeNumeroAvs(nss);
        apDroitLAPGJointTiersManager.setForEtatDroitNotIn(etatIndesirable);
        apDroitLAPGJointTiersManager.setForDroitContenuDansDateDebut(periodeAPGCalculed.getDateDebutPeriode());
        apDroitLAPGJointTiersManager.setForDroitContenuDansDateFin(periodeAPGCalculed.getDateFinPeriode());
//        if(!StringUtils.EMPTY.equals(idDroit)) {
//            apDroitLAPGJointTiersManager.setForIdDroitNotIn(Collections.singletonList(idDroit));
//
//        }
        apDroitLAPGJointTiersManager.setForCsTypeDemandeIn(Collections.singletonList(typeDroit));
        Exceptions.checkedToUnChecked(() -> apDroitLAPGJointTiersManager.find(BManager.SIZE_NOLIMIT));
        List<APDroitAvecParent> droitsTries = null;
        try {
            apDroitLAPGJointTiersManager.find(BManager.SIZE_NOLIMIT);
            List<APDroitAvecParent> tousLesDroits = apDroitLAPGJointTiersManager.toList();
            droitsTries = skipDroitParent(tousLesDroits);
        } catch (Exception e) {
            throwRuleExecutionException(e);
        }
        return droitsTries;
    }

    private Integer getTotalNombreJoursAAjouter(List<APDroitAvecParent> droits, APPeriodeAPG periodeAPGCalculed){

        List<APDroitAvecParent> droitsFiltrer = droits.stream().filter(droit -> !droit.getIdDroit().equals(periodeAPGCalculed.getIdDroit())).collect(Collectors.toList());
        return droitsFiltrer.stream()
                .map(droit -> {
                    APPeriodeAPGManager periodeAPGManager = new APPeriodeAPGManager();
                    periodeAPGManager.setSession(this.getSession());
                    periodeAPGManager.setForIdDroit(droit.getIdDroit());
                    Exceptions.checkedToUnChecked(() -> periodeAPGManager.find(BManager.SIZE_NOLIMIT));
                    return periodeAPGManager.<APPeriodeAPG>getContainerAsList();
                })
                .flatMap(Collection::stream)
                .map(periode -> getNombreJoursAAjouter(periodeAPGCalculed, periode.getDateDebutPeriode(), periode.getDateFinPeriode()))
                .reduce(0, Integer::sum);
    }

    @VisibleForTesting
    int getNombreJoursAAjouter(APPeriodeAPG periodeAPGCalculed, String dateDebutACompparer, String dateFinAComparer) {
        Periode periode1 = new Periode(periodeAPGCalculed.getDateDebutPeriode(),periodeAPGCalculed.getDateFinPeriode());
        if(JadeStringUtil.isBlankOrZero(dateFinAComparer)){
            dateFinAComparer = JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now()));
        }
        Periode periode2 = new Periode(dateDebutACompparer,dateFinAComparer);

        if(chevauchementDePeriode(periode1.getDateDebut(), periode1.getDateFin(), periode2.getDateDebut(), periode2.getDateFin())){
            LocalDate periode1Debut = Dates.toDate(periode1.getDateDebut());
            LocalDate periode1Fin = Dates.toDate(periode1.getDateFin());
            LocalDate periode2Debut = Dates.toDate(periode2.getDateDebut());
            LocalDate periode2Fin = Dates.toDate(periode2.getDateFin());
            LocalDate dateDebutDroit;
            LocalDate dateFinDroit;
            if(periode1Debut.equals(periode2Debut) ||
               periode1Debut.isAfter(periode2Debut)) {
                dateDebutDroit = periode1Debut;
            } else {
                dateDebutDroit = periode2Debut;
            }

            if(periode1Fin.equals(periode2Fin) ||
               periode1Fin.isBefore(periode2Fin)){
                dateFinDroit = periode1Fin;
            } else {
                dateFinDroit = periode2Fin;
            }
            return (int) Dates.daysBetween(dateDebutDroit, dateFinDroit);
        }
        return 0;
    }

    private boolean chevauchementDePeriode(String debutPeriod1, String finPeriod1, String debutPeriod2, String finPeriod2){
        Periode periode1 = new Periode(debutPeriod1,finPeriod1);
        if(JadeStringUtil.isBlankOrZero(finPeriod2)){
            finPeriod2 = JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now()));
        }
        Periode periode2 = new Periode(debutPeriod2, finPeriod2);
        return periode1.comparerChevauchement(periode2) == Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT;
    }
}
