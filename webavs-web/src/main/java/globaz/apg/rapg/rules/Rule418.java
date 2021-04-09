package globaz.apg.rapg.rules;

import ch.globaz.common.domaine.Periode;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.*;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadePeriodWrapper;
import globaz.jade.client.util.JadeStringUtil;

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
                manager.find();
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
                manager2.find();
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
                        int nbreJoursAAjouter = getNombreJoursAAjouter(periodeAPGCalculed, periodePat.getDateDebutPeriode(),periodePat.getDateDebutPeriode());
                        nombreJoursPat += nbreJoursAAjouter;
                    }
                }
            }

            if (nombreJoursEffectueNonPat + nombreJoursPat > nombreJoursMax) {
                return false;
            }
        }

        return true;
    }

    private int getNombreJoursAAjouter(APPeriodeAPG periodeAPGCalculed, String dateDebutACompparer, String dateFinAComparer) {
        Periode periode1 = new Periode(periodeAPGCalculed.getDateDebutPeriode(),periodeAPGCalculed.getDateFinPeriode());
        if(JadeStringUtil.isBlankOrZero(dateFinAComparer)){
            dateFinAComparer = JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now()));
        }
        Periode periode2 = new Periode(dateDebutACompparer,dateFinAComparer);
        String dateDebutDroitAAjouter = "";
        String dateDebutFinAAjouter = "";


        if(periode1.comparerChevauchement(periode2) == Periode.ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT){
            if(JadeDateUtil.areDatesEquals(periode1.getDateDebut(), periode2.getDateDebut())){
                dateDebutDroitAAjouter = periode1.getDateDebut();
            }else{
                if(JadeDateUtil.isDateBefore(periode1.getDateDebut(), periode2.getDateDebut())){
                    dateDebutDroitAAjouter = periode2.getDateDebut();
                }else{
                    dateDebutDroitAAjouter = periode1.getDateDebut();
                }
            }
            if(JadeDateUtil.areDatesEquals(periode1.getDateFin(), periode2.getDateFin())){
                dateDebutFinAAjouter = periode1.getDateFin();
            }else{
                if(JadeDateUtil.isDateBefore(periode1.getDateFin(), periode2.getDateFin())){
                    dateDebutFinAAjouter = periode1.getDateFin();
                }else{
                    dateDebutFinAAjouter = periode2.getDateFin();
                }
            }
            return JadeDateUtil.getNbDayBetween(dateDebutDroitAAjouter, dateDebutFinAAjouter)+1;
        }
        return 0;
    }

}
