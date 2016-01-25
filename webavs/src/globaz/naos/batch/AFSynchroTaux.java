package globaz.naos.batch;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.tauxAssurance.AFSynchroTauxAssuranceManager;
import globaz.naos.db.tauxAssurance.AFTauxAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CATauxRubriques;
import globaz.osiris.db.comptes.CATauxRubriquesManager;

public class AFSynchroTaux {

    /**
     * Ajoute le taux en comptabilité auxiliaire
     * 
     * @param taux
     * @throws Exception
     */
    private static void addTaux(AFTauxAssurance taux) throws Exception {
        CATauxRubriques tauxRubrique = new CATauxRubriques();
        tauxRubrique.setIdCaisseProf(taux.getCategorieId());
        tauxRubrique.setIdRubrique(taux.getAssurance().getRubriqueId());
        tauxRubrique.setDate(taux.getDateDebut());
        tauxRubrique.setTauxEmployeur(taux.getValeurEmployeur());
        tauxRubrique.setTauxSalarie(taux.getValeurEmploye());
        tauxRubrique.setSession(taux.getSession());
        tauxRubrique.add();

    }

    /**
     * CABvrFtpChangePassword génére un nouveau mot de passe pour la PostFinance. <br/>
     * Le mot de passe est uniquement généré/stocké et envoyé à PostFinance si il est échue (voire
     * CABvrFtpPasswordUtil.MAX_DAYS_PASSWORD_VALIDITY).
     * 
     * @param args
     *            "-user user -password password -email email@globaz.ch,email2@globaz.ch" (Emails optionnel sinon
     *            AdminEmail)
     * 
     */
    public static void main(String[] args) {
        String user = null;
        String password = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().equals("-user")) {
                i++;
                user = args[i];
            } else if (args[i].toLowerCase().equals("-password")) {
                i++;
                password = args[i];
            }
        }

        if (JadeStringUtil.isBlank(user) || JadeStringUtil.isBlank(password)) {
            System.err.println("User/password empty.");
            System.exit(-1);
        }

        BTransaction transaction = null;

        try {
            BSession session = (BSession) GlobazSystem.getApplication(AFApplication.DEFAULT_APPLICATION_NAOS)
                    .newSession(user, password);

            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();

            AFSynchroTauxAssuranceManager tauxManager = new AFSynchroTauxAssuranceManager();
            tauxManager.setSession(session);
            tauxManager.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
            tauxManager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < tauxManager.size(); i++) {
                AFTauxAssurance taux = (AFTauxAssurance) tauxManager.getEntity(i);

                CATauxRubriquesManager tauxmanager2 = new CATauxRubriquesManager();
                tauxmanager2.setForIdRubrique(taux.getAssurance().getRubriqueId());
                tauxmanager2.setForDate(taux.getDateDebut());
                tauxmanager2.setSession(taux.getSession());
                tauxmanager2.find();

                if (tauxmanager2.size() == 0) {

                    if ((taux.getGenreValeur().equalsIgnoreCase(CodeSystem.GEN_VALEUR_ASS_TAUX))
                            && (!JadeStringUtil.isBlankOrZero(taux.getValeurEmploye()))) {

                        AFSynchroTaux.addTaux(taux);
                    }
                } else {

                    for (int j = 0; j < tauxmanager2.size(); j++) {
                        CATauxRubriques tauxRubriquesModif = (CATauxRubriques) tauxmanager2.getEntity(j);

                        if ((!BSessionUtil.compareDateEqual(transaction.getSession(), taux.getDateDebut(),
                                tauxRubriquesModif.getDate()))
                                && (!tauxRubriquesModif.getTauxEmployeur().equalsIgnoreCase(taux.getValeurEmployeur()))) {

                            AFSynchroTaux.addTaux(taux);
                        }
                    }
                }
            }

            if (session.hasErrors() || transaction.hasErrors()) {
                transaction.rollback();
            } else {
                transaction.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();

            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
            }

            System.exit(-1);
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.exit(0);
    }

}
