package globaz.helios.batch;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.helios.db.comptes.CGCompte;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGExerciceComptableListViewBean;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGPlanComptableListViewBean;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.jade.client.util.JadeStringUtil;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CGInitPlanComptable {
    private static BSession session;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {
        String fileName = null;
        String idExerciceComptable = null;
        String exercice = null;
        String idMandat = null;
        String user = null;
        String pwd = null;
        boolean doInsert = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].toLowerCase().equals("-exercice")) {
                i++;
                exercice = args[i];
            } else if (args[i].toLowerCase().equals("-file")) {
                i++;
                fileName = args[i];
            } else if (args[i].toLowerCase().equals("-insert")) {
                doInsert = true;
            } else if (args[i].toLowerCase().equals("-user")) {
                i++;
                user = args[i];
            } else if (args[i].toLowerCase().equals("-password")) {
                i++;
                pwd = args[i];
            }
        }

        if (JadeStringUtil.isBlank(fileName)) {
            System.out.println("Parameter \"-file\" missing !");
            System.exit(0);
        }

        if (JadeStringUtil.isIntegerEmpty(exercice)) {
            System.out.println("Parameter \"-exercice\" missing !");
            System.exit(0);
        }

        if (JadeStringUtil.isIntegerEmpty(user)) {
            System.out.println("Parameter \"-user\" missing !");
            System.exit(0);
        }

        if (JadeStringUtil.isIntegerEmpty(user)) {
            System.out.println("Parameter \"-pwd\" missing !");
            System.exit(0);
        }
        BTransaction transaction = null;
        try {
            session = new BSession("HELIOS");
            session.connect(user, pwd);

            Iterator it = (getComptes(fileName)).iterator();

            if (doInsert) {
                transaction = (BTransaction) session.newTransaction();
                transaction.openTransaction();
            }

            while (it.hasNext()) {
                CGOldCompte compte = (CGOldCompte) it.next();

                CGMandat mandat = new CGMandat();
                mandat.setSession(session);
                mandat.setIdMandat(compte.getIdMandat());
                mandat.retrieve();
                if (idMandat == null || !idMandat.equals(compte.getIdMandat())) {
                    idMandat = mandat.getIdMandat();
                    if (mandat == null || mandat.isNew()) {
                        System.out.println("Mandat " + compte.getIdMandat() + " inexistant pour compte "
                                + compte.getIdExterne() + ". Ignoré");
                        continue;
                    } else {
                        System.out.println("> Mandat no. " + mandat.getIdMandat() + " - " + mandat.getLibelleFr());
                    }
                }

                CGExerciceComptableListViewBean exercices = new CGExerciceComptableListViewBean();
                exercices.setSession(session);
                exercices.setFromDateDebut("01.01." + exercice);
                exercices.setForIdMandat(mandat.getIdMandat());
                exercices.find();
                if (exercices.isEmpty()) {
                    System.out.println("Aucun exercice " + idExerciceComptable + "trouvé pour Mandat "
                            + compte.getIdMandat() + ", compte " + compte.getIdExterne() + ". Ignoré");
                    continue;
                }
                idExerciceComptable = ((CGExerciceComptable) exercices.getFirstEntity()).getIdExerciceComptable();

                CGPlanComptableListViewBean manager = new CGPlanComptableListViewBean();
                manager.setSession(session);

                manager.setForIdExerciceComptable(idExerciceComptable);
                manager.setForIdExterne(compte.getIdExterneFormatte());
                manager.setForIdMandat(compte.getIdMandat());

                manager.find(transaction);

                if (manager.hasErrors() || manager.isEmpty()) {
                    addNewPlanComptable(idExerciceComptable, doInsert, transaction, compte);
                } else if (manager.size() == 1) {
                    updateLibelleFrPlanComptable(doInsert, transaction, compte, manager);
                }
            }

            if (doInsert) {
                transaction.commit();
                transaction.closeTransaction();
            }
        } catch (Exception e) {
            if (doInsert) {
                try {
                    transaction.rollback();
                    transaction.closeTransaction();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    System.exit(0);
                }
            }

            e.printStackTrace();
        }

        System.exit(0);
    }

    private static void updateLibelleFrPlanComptable(boolean doInsert, BTransaction transaction, CGOldCompte compte,
            CGPlanComptableListViewBean manager) {
        CGPlanComptableViewBean plan = (CGPlanComptableViewBean) manager.getFirstEntity();

        if (!plan.getLibelleFr().trim().equals(compte.getLibelleFr().trim())) {
            System.out.println("----------------------" + plan.getIdExterne() + ", " + plan.getLibelle()
                    + "----------------------");
            System.out.println("---- old libelle fr : " + plan.getLibelleFr());
            System.out.println("---- new libelle fr : " + compte.getLibelleFr());

            plan.setLibelleFr(compte.getLibelleFr().trim());

            if (doInsert) {
                try {
                    plan.update(transaction);
                } catch (Exception e) {
                    e.printStackTrace();
                    transaction.clearErrorBuffer();
                }

                if (plan.hasErrors()) {
                    System.out.println("ERROR Update : " + plan.getErrors().toString());
                    transaction.clearErrorBuffer();
                }
            }
        }
    }

    private static void addNewPlanComptable(String idExerciceComptable, boolean doInsert, BTransaction transaction,
            CGOldCompte compte) throws Exception, FWSecurityLoginException {
        CGPlanComptableViewBean plan = new CGPlanComptableViewBean();
        plan.setSession(session);

        plan.setIdExerciceComptable(idExerciceComptable);

        plan.setIdExterne(compte.getIdExterneFormatte());
        plan.setLibelleFr(compte.getLibelleFr());

        plan.setIdNature(CGCompte.CS_STANDARD);
        plan.setAReouvrir(new Boolean(true));
        plan.setCodeISOMonnaie(CGCompte.CODE_ISO_CHF);
        plan.setEstVerrouille(new Boolean(false));

        setPlanIdDomaineAndIdGenre(plan, compte);

        System.out.println("----------------------" + plan.getIdExterne() + ", " + plan.getLibelle()
                + "----------------------");

        if (doInsert) {
            try {
                plan.add(transaction);
            } catch (Exception e) {
                e.printStackTrace();
                transaction.clearErrorBuffer();
            }

            if (plan.hasErrors()) {
                System.out.println("ERROR Insert : " + plan.getErrors().toString());
                transaction.clearErrorBuffer();
            }
        }
    }

    public static void setPlanIdDomaineAndIdGenre(CGPlanComptableViewBean plan, CGOldCompte compte) throws Exception {
        if (compte.getType().equals("A")) {
            plan.setIdDomaine(CGCompte.CS_COMPTE_BILAN);
            plan.setIdGenre(CGCompte.CS_GENRE_ACTIF);
        } else if (compte.getType().equals("P")) {
            plan.setIdDomaine(CGCompte.CS_COMPTE_BILAN);
            plan.setIdGenre(CGCompte.CS_GENRE_PASSIF);
        } else if (compte.getType().equals("DE")) {
            plan.setIdDomaine(CGCompte.CS_COMPTE_EXPLOITATION);
            plan.setIdGenre(CGCompte.CS_GENRE_CHARGE);
        } else if (compte.getType().equals("RE")) {
            plan.setIdDomaine(CGCompte.CS_COMPTE_EXPLOITATION);
            plan.setIdGenre(CGCompte.CS_GENRE_PRODUIT);
        } else if (compte.getType().equals("CA")) {
            plan.setIdDomaine(CGCompte.CS_COMPTE_ADMINISTRATION);
            plan.setIdGenre(CGCompte.CS_GENRE_CHARGE);
        } else if (compte.getType().equals("PA")) {
            plan.setIdDomaine(CGCompte.CS_COMPTE_ADMINISTRATION);
            plan.setIdGenre(CGCompte.CS_GENRE_PRODUIT);
        } else if (compte.getType().equals("DI")) {
            plan.setIdDomaine(CGCompte.CS_COMPTE_INVESTISSEMENT);
            plan.setIdGenre(CGCompte.CS_GENRE_CHARGE);
        } else if (compte.getType().equals("RI")) {
            plan.setIdDomaine(CGCompte.CS_COMPTE_INVESTISSEMENT);
            plan.setIdGenre(CGCompte.CS_GENRE_PRODUIT);
        } else if (compte.getType().equals("CL")) {
            String idExterne = compte.getIdExterneFormatte();
            if (!compte.getIdMandat().equals("900")) {
                plan.setIdDomaine(CGCompte.CS_COMPTE_BILAN);
                if (idExterne.equals("50020")) {
                    plan.setIdGenre(CGCompte.CS_GENRE_OUVERTURE);
                } else if (idExterne.equals("50030")) {
                    plan.setIdGenre(CGCompte.CS_GENRE_CLOTURE);
                } else {
                    throw new Exception("Type non résolu !");
                }

            } else {
                String idExterneCompteAvs = idExterne.substring(idExterne.indexOf(".") + 1, idExterne.lastIndexOf("."));

                if (idExterneCompteAvs.equals("9200")) {
                    plan.setIdDomaine(CGCompte.CS_COMPTE_BILAN);
                    plan.setIdGenre(CGCompte.CS_GENRE_OUVERTURE);
                } else if (idExterneCompteAvs.equals("9210")) {
                    plan.setIdDomaine(CGCompte.CS_COMPTE_BILAN);
                    plan.setIdGenre(CGCompte.CS_GENRE_CLOTURE);
                } else if (idExterneCompteAvs.equals("9000")) {
                    plan.setIdDomaine(CGCompte.CS_COMPTE_EXPLOITATION);
                    plan.setIdGenre(CGCompte.CS_GENRE_RESULTAT);
                } else if (idExterneCompteAvs.substring(0, 2).equals("91")) {
                    plan.setIdDomaine(CGCompte.CS_COMPTE_ADMINISTRATION);
                    plan.setIdGenre(CGCompte.CS_GENRE_RESULTAT);
                } else {
                    throw new Exception("Type non résolu !");
                }
            }
        } else {
            throw new Exception("Type non résolu !");
        }
    }

    public static List getComptes(String fileName) throws Exception {
        ArrayList list = new ArrayList();

        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str;
        while ((str = in.readLine()) != null) {
            list.add(getCompte(str));
        }
        in.close();

        return list;
    }

    public static CGOldCompte getCompte(String content) {
        CGOldCompte compte = new CGOldCompte();

        StringBuffer buf = new StringBuffer(content);

        boolean hasComaIntoLibelle = (JadeStringUtil.occurrencesOf(content, ",") > 20);

        int countComa = 0;
        int nextComaIndex = 0;

        while (countComa < 16) {
            String tmp;
            if (hasComaIntoLibelle && countComa == 15) {
                tmp = buf.substring(nextComaIndex, content.indexOf("\",", nextComaIndex + 1));
            } else {
                tmp = buf.substring(nextComaIndex, content.indexOf(",", nextComaIndex + 1));
            }
            if (countComa == 0) {
                compte.setIdMandat(tmp);
            } else if (countComa == 1) {
                compte.setIdExterne(tmp);
            } else if (countComa == 14) {
                compte.setType(JadeStringUtil.change(tmp, "\"", ""));
            } else if (countComa == 15) {
                compte.setLibelleFr(JadeStringUtil.change(tmp, "\"", ""));
            }

            countComa++;
            nextComaIndex = content.indexOf(",", nextComaIndex + 1) + 1;
        }

        return compte;
    }
}
