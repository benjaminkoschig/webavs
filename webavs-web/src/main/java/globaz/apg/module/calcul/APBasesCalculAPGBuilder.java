package globaz.apg.module.calcul;

import globaz.apg.db.droits.*;
import globaz.apg.properties.APParameter;
import globaz.globall.db.BSession;
import globaz.globall.db.FWFindParameter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.utils.PRDateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class APBasesCalculAPGBuilder extends APBasesCalculBuilder{

    public APBasesCalculAPGBuilder(BSession session, APDroitLAPG droit) {
        super(session, droit);
    }

    @Override
    void ajoutDesCommandes() throws Exception {
        ajouterSituationProfessionnelle();
        /*
         * pour le cas ou on traite un droit APG, on commence par ajouter les �v�nements relatifs aux p�riodes de
         * prestation puis on ajoute les changements de situation familiale.
         */
        ajouterTauxImposition();
        ajouterPeriodesAPG(); // ajouter les p�riodes et splitter par ann�e
        ajouterSituationFamilialeAPG();
        nbJoursSoldes = Integer.parseInt(((APDroitAPG) droit).getNbrJourSoldes());
    }

    // ajouter les �v�nements relatifs aux p�riodes de prestation APG � la liste
    // des commandes et splitter par ann�e.
    private void ajouterPeriodesAPG() throws Exception {
        // charger toutes les p�riodes pour ce droit.
        APPeriodeAPGManager mgr = new APPeriodeAPGManager();
        Calendar bCal = getCalendarInstance();

        mgr.setSession(session);
        mgr.setForIdDroit(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction());

        // pour chaque p�riode
        for (int idPeriode = 0; idPeriode < mgr.size(); ++idPeriode) {
            APPeriodeAPG periode = (APPeriodeAPG) mgr.get(idPeriode);
            PeriodeAPGCommand bCommand = new PeriodeAPGCommand(periode.getDateDebutPeriode(), true);
            PeriodeAPGCommand eCommand = new PeriodeAPGCommand(periode.getDateFinPeriode(), false);

            // ajouter l'�v�nement "d�but de la p�riode"
            commands.add(bCommand);

            // ajouter l'�v�nement "fin de la p�riode"
            commands.add(eCommand);

            // couper par ann�e si n�cessaire
            calendar.setTime(bCommand.date);
            bCal.setTime(eCommand.date);

            for (int year = calendar.get(Calendar.YEAR); year < bCal.get(Calendar.YEAR); ++year) {
                calendar.set(year, Calendar.DECEMBER, 31);
                commands.add(new NouvelleBaseCommand(calendar.getTime(), false));
            }
        }
    }

    // ajouter les �v�nements relatifs � la situation familiale APG � la liste
    // des commandes.
    private void ajouterSituationFamilialeAPG() throws Exception {
        // charger tous les enfants pour la situation familiale de ce droit.
        APEnfantAPGManager mgr = new APEnfantAPGManager();

        mgr.setSession(session);
        mgr.setForIdSituationFamiliale(((APDroitAPG) droit).getIdSituationFam());
        mgr.find(session.getCurrentThreadTransaction());

        // pour chaque enfant
        for (int idEnfant = 0; idEnfant < mgr.size(); ++idEnfant) {
            APEnfantAPG enfant = (APEnfantAPG) mgr.get(idEnfant);

            // ajouter l'�v�nement "naissance de l'enfant"
            commands.add(new EnfantAPGCommand(enfant.getDateDebutDroit(), true));

            // si une fin de droit est d�finie pour cet enfant, ajouter
            // l'�v�nement "fin de droit pour l'enfant"
            if (!JadeStringUtil.isEmpty(enfant.getDateFinDroit())) {
                commands.add(new EnfantAPGCommand(enfant.getDateFinDroit(), false));
            }
        }
    }
}
