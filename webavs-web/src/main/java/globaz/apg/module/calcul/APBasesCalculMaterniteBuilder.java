package globaz.apg.module.calcul;

import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APEnfantMatManager;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.prestation.application.PRAbstractApplication;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class APBasesCalculMaterniteBuilder extends APBasesCalculBuilder{

    protected static final Date PREMIER_JUILLET_2001;
    protected static final Date PREMIER_JUILLET_2005;
    protected static final Date TRENTE_JUIN_2001;
    protected static final Date TRENTE_JUIN_2005;

    static {
        Calendar cal = getCalendarInstance();

        cal.set(2005, Calendar.JUNE, 30);
        TRENTE_JUIN_2005 = cal.getTime();
        cal.set(Calendar.YEAR, 2001);
        TRENTE_JUIN_2001 = cal.getTime();
        cal.set(Calendar.YEAR, 2005);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        PREMIER_JUILLET_2005 = cal.getTime();
        cal.set(Calendar.YEAR, 2001);
        PREMIER_JUILLET_2001 = cal.getTime();
    }

    public APBasesCalculMaterniteBuilder(BSession session, APDroitLAPG droit) {
        super(session, droit);
    }

    void ajoutDesCommandes() throws Exception {
        ajouterSituationProfessionnelle();
        ajouterTauxImposition();
        /*
         * pour le cas où on traite un droit maternité, on ne traite que la situation familiale et on découpe les
         * prestations par mois et/ou par année.
         */
        if ("true".equals(PRAbstractApplication.getApplication(APApplication.DEFAULT_APPLICATION_APG).getProperty(
                "isDroitMaterniteCantonale"))) {
            ajouterPremierJuillet2001();
        } else {
            ajouterPremierJuillet2005();
        }

        ajouterSituationFamilialeMat();
    }

    // ajoute le premier juillet 2001 comme date de début des versements des
    // prestations.
    private void ajouterPremierJuillet2001() throws ParseException {
        Date debut = DF.parse(droit.getDateDebutDroit());

        if (debut.before(PREMIER_JUILLET_2001)) {
            commands.add(new VersementInterditCommand(null, true));
            commands.add(new VersementInterditCommand(TRENTE_JUIN_2001, false));
        }
        commands.add(new VersementInterditCommand(TRENTE_JUIN_2005, false));
    }

    // ajoute le premier juillet 2005 comme date de début des versements des
    // prestations.
    private void ajouterPremierJuillet2005() throws ParseException {
        Date debut = DF.parse(droit.getDateDebutDroit());

        if (debut.before(PREMIER_JUILLET_2005)) {
            commands.add(new VersementInterditCommand(null, true));
            commands.add(new VersementInterditCommand(TRENTE_JUIN_2005, false));
        }
    }

    // ajouter les événements relatifs à la situation familiale maternité à la
    // liste des commandes.
    private void ajouterSituationFamilialeMat() throws Exception {
        // obtenir les date des débuts et de fin du droit
        Date debut = DF.parse(droit.getDateDebutDroit());
        Calendar fin = getCalendarInstance();

        fin.setTime(DF.parse(droit.getDateFinDroit()));

        // creer les commandes de début de droit et de find de droit à
        // l'allocation maternité
        EnfantMatCommand commandDebut = new EnfantMatCommand(debut, true);
        EnfantMatCommand commandFin = new EnfantMatCommand(fin.getTime(), false);

        // ajouter les enfants aux commandes
        APEnfantMatManager mgr = new APEnfantMatManager();

        mgr.setSession(session);
        mgr.setForIdDroitMaternite(droit.getIdDroit());
        mgr.find(session.getCurrentThreadTransaction(), BManager.SIZE_USEDEFAULT);

        for (int idEnfant = 0; idEnfant < mgr.size(); ++idEnfant) {
            commands.add(commandDebut);
            commands.add(commandFin);
        }

        // couper par mois
        couperParMois(debut, fin);
    }

}
