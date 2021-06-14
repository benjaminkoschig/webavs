package globaz.apg.rapg.rules;

import ch.globaz.common.domaine.Periode;
import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.util.Dates;
import com.google.common.annotations.VisibleForTesting;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitAPGJointTiers;
import globaz.apg.db.droits.APDroitAPGJointTiersManager;
import globaz.apg.db.droits.APDroitLAPGJointTiers;
import globaz.apg.db.droits.APDroitLAPGJointTiersManager;
import globaz.apg.db.droits.APDroitPaterniteJointTiers;
import globaz.apg.db.droits.APDroitPaterniteJointTiersManager;
import globaz.apg.db.droits.APPeriodeAPG;
import globaz.apg.db.droits.APPeriodeAPGManager;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.api.IPRDemande;
import org.joda.time.Days;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    private static final int NB_JOUR_MAX = 42;

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
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        String endOfPeriod = champsAnnonce.getEndOfPeriod();
        String nss = champsAnnonce.getInsurant();
        String serviceType = champsAnnonce.getServiceType();


        if(!(serviceType.equals(APGenreServiceAPG.Maternite.getCodePourAnnonce()) || APGenreServiceAPG.isValidGenreServicePandemie(serviceType))){
            int nombreJoursEffectueNonPat = 0;
            int nombreJoursPat = Integer.parseInt(champsAnnonce.getNumberOfDays());
            Date dateDebut = JadeDateUtil.getGlobazDate(startOfPeriod);
            Date dateFin = JadeDateUtil.getGlobazDate(endOfPeriod);

            int nombreJoursMax ;

            if (dateDebut.equals(dateFin)) {
                nombreJoursMax =  1;
            } else {
                if (dateDebut != null && dateFin != null && dateFin.getTime() >= dateDebut.getTime()) {
                    double msDiff = (double)(dateFin.getTime() - dateDebut.getTime());
                    nombreJoursMax =  ((int)Math.round(msDiff / 8.64E7D))+1;
                } else {
                    nombreJoursMax = 0;
                }
            }
            APPeriodeAPG periodeAPGCalculed = new APPeriodeAPG();
            periodeAPGCalculed.setDateDebutPeriode(startOfPeriod);
            periodeAPGCalculed.setDateFinPeriode(endOfPeriod);

            validNotEmpty(nss, "NSS");
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");

            // Ne pas traiter les droits en état refusé ou transféré
            List<String> etatIndesirable = new ArrayList<String>();
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_REFUSE);
            etatIndesirable.add(IAPDroitLAPG.CS_ETAT_DROIT_TRANSFERE);

            APDroitAPGJointTiersManager manager = new APDroitAPGJointTiersManager();
            manager.setSession(getSession());
            manager.setLikeNumeroAvs(nss);
            manager.setForEtatDroitNotIn(etatIndesirable);
            List<APDroitAvecParent> droitsTries = null;
            try {
                manager.find(BManager.SIZE_NOLIMIT);
                List<APDroitAvecParent> tousLesDroits = manager.getContainer();
                droitsTries = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
            // On va trouver tous les droits, même celui qu'on vient de créer,
            // on additionne tous les jours soldées
            for (int i = 0; i < droitsTries.size(); i++) {
                APDroitAPGJointTiers droit = (APDroitAPGJointTiers) droitsTries.get(i);
                for(JadePeriodWrapper periodePat : droit.getPeriodes()) {
                    int nbreJoursAAjouter = getNombreJoursAAjouter(periodeAPGCalculed, periodePat.getDateDebut(),periodePat.getDateFin());
                    if(!droit.getIdDroit().equals(champsAnnonce.getIdDroit())){
                        nombreJoursEffectueNonPat += nbreJoursAAjouter;
                    }
                }
            }
            APDroitPaterniteJointTiersManager manager2 = new APDroitPaterniteJointTiersManager();
            manager2.setSession(getSession());
            manager2.setLikeNumeroAvs(nss);
            manager2.setForEtatDroitNotIn(etatIndesirable);
            droitsTries = null;
            try {
                manager2.find(BManager.SIZE_NOLIMIT);
                List<APDroitAvecParent> tousLesDroits = manager2.getContainer();
                droitsTries = skipDroitParent(tousLesDroits);
            } catch (Exception e) {
                throwRuleExecutionException(e);
            }
            // On va trouver tous les droits, même celui qu'on vient de créer,
            // on additionne tous les jours soldées
            for (int i = 0; i < droitsTries.size(); i++) {
                APDroitPaterniteJointTiers droit = (APDroitPaterniteJointTiers) droitsTries.get(i);

                for(APPeriodeAPG periodePat : droit.getPeriodes()){
                    if (!JadeStringUtil.isBlank(periodePat.getNbrJours()) && !droit.getIdDroit().equals(champsAnnonce.getIdDroit())) {
                        int nbreJoursAAjouter = getNombreJoursAAjouter(periodeAPGCalculed, periodePat.getDateDebutPeriode(),periodePat.getDateFinPeriode());
                        nombreJoursPat += nbreJoursAAjouter;
                    }
                }
            }

            Integer nombreJoursProcheAidant = rechercherNombreJourProcheAidant(champsAnnonce, nss, periodeAPGCalculed, etatIndesirable);

            if (nombreJoursEffectueNonPat + nombreJoursPat + nombreJoursProcheAidant > nombreJoursMax) {
                return false;
            }
        }

        return true;
    }

    private Integer rechercherNombreJourProcheAidant(APChampsAnnonce champsAnnonce,
                                                     String nss,
                                                     APPeriodeAPG periodeAPGCalculed,
                                                     List<String> etatIndesirable) throws APRuleExecutionException {

        APDroitLAPGJointTiersManager apDroitLAPGJointTiersManager = new APDroitLAPGJointTiersManager();
        apDroitLAPGJointTiersManager.setSession(this.getSession());
        apDroitLAPGJointTiersManager.setLikeNumeroAvs(nss);
        apDroitLAPGJointTiersManager.setForEtatDroitNotIn(etatIndesirable);
        apDroitLAPGJointTiersManager.setForDroitContenuDansDateDebut(periodeAPGCalculed.getDateDebutPeriode());
        apDroitLAPGJointTiersManager.setForDroitContenuDansDateFin(periodeAPGCalculed.getDateFinPeriode());
        apDroitLAPGJointTiersManager.setForIdDroitNotIn(Collections.singletonList(champsAnnonce.getIdDroit()));
        apDroitLAPGJointTiersManager.setForCsTypeDemandeIn(Collections.singletonList(IPRDemande.CS_TYPE_PROCHE_AIDANT));
        Exceptions.checkedToUnChecked(() -> apDroitLAPGJointTiersManager.find(BManager.SIZE_NOLIMIT));
        List<APDroitLAPGJointTiers> droits = skipDroitParent(apDroitLAPGJointTiersManager.getContainerAsList());

        return droits.stream()
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

        if(periode1.comparerChevauchement(periode2) == Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT){
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
}
