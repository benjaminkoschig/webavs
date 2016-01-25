/**
 * 
 */
package globaz.osiris.utils.rubrique;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.helios.db.comptes.CGPlanComptableViewBean;
import globaz.jade.log.JadeLogger;
import globaz.naos.api.IAFAssurance;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.db.comptes.CARubrique;

/**
 * Aide pour la création de rubriques
 * 
 * @author sel
 * 
 */
public class CreateRubriques {

    private static final String CC_5500_1101 = "5500.1101.00";
    private static final String EXERCICE_COMPTABLE = "8";
    private static final String PASSWORD = "glob4az";
    private static final String USER = "h511glo";

    /**
     * @param canton
     * @param idrubriqueContriAlfaPar
     * @throws Exception
     */
    private static void createAssurancePar(BSession session, Canton canton, String idrubriqueContriAlfaPar) {
        AFAssurance ass = new AFAssurance();
        ass.setSession(session);
        ass.setAssuranceLibelleCourtFr("Cot. ALFA " + canton.getCantonLibelleCourt());
        ass.setAssuranceLibelleCourtAl("Cot. ALFA " + canton.getCantonLibelleCourt());
        ass.setAssuranceLibelleCourtIt("Cot. ALFA " + canton.getCantonLibelleCourt());
        ass.setAssuranceLibelleFr("Cotisation ALFA " + canton.getCantonLibelleCourt());
        ass.setAssuranceLibelleAl("ALFA-Beiträge " + canton.getCantonLibelleCourt());
        ass.setAssuranceLibelleIt("Cotisation ALFA " + canton.getCantonLibelleCourt());
        ass.setRubriqueId(idrubriqueContriAlfaPar);
        ass.setAssuranceCanton(canton.getCsCanton());
        ass.setAssuranceGenre(CodeSystem.GENRE_ASS_PARITAIRE);
        ass.setTypeAssurance(IAFAssurance.TYPE_ASS_COTISATION_AF);
        ass.setTypeCalcul(CodeSystem.TYPE_CALCUL_STANDARD);
        ass.setSurDocAcompte(true);
        try {
            ass.add();
            System.out.println("Création de l'assurance : " + "Cotisation ALFA " + canton.getCantonLibelleCourt());
        } catch (Exception e) {
            System.out.println("Erreure à la création de l'assurance : " + "Cotisation ALFA "
                    + canton.getCantonLibelleCourt());
            e.printStackTrace();
        }
    }

    /**
     * @param canton
     * @param idrubriqueContriAlfaPer
     * @throws Exception
     */
    private static void createAssurancePer(BSession session, Canton canton, String idrubriqueContriAlfaPer) {
        AFAssurance ass = new AFAssurance();
        ass.setSession(session);
        ass.setAssuranceLibelleCourtFr("Cot. ALFA " + canton.getCantonLibelleCourt() + " pers.");
        ass.setAssuranceLibelleCourtAl("Cot. ALFA " + canton.getCantonLibelleCourt() + " pers.");
        ass.setAssuranceLibelleCourtIt("Cot. ALFA " + canton.getCantonLibelleCourt() + " pers.");
        ass.setAssuranceLibelleFr("Cotisation ALFA " + canton.getCantonLibelleCourt() + " personnelle");
        ass.setAssuranceLibelleAl("ALFA-Beiträge " + canton.getCantonLibelleCourt() + " persönlich");
        ass.setAssuranceLibelleIt("Cotisation ALFA " + canton.getCantonLibelleCourt() + " personnelle");
        ass.setRubriqueId(idrubriqueContriAlfaPer);
        ass.setAssuranceCanton(canton.getCsCanton());
        ass.setAssuranceGenre(CodeSystem.GENRE_ASS_PERSONNEL);
        ass.setTypeAssurance(IAFAssurance.TYPE_ASS_COTISATION_AF);
        ass.setTypeCalcul(CodeSystem.TYPE_CALCUL_STANDARD);
        ass.setSurDocAcompte(false);
        try {
            ass.add();
            System.out.println("Création de l'assurance : " + "Cotisation ALFA " + canton.getCantonLibelleCourt()
                    + " personnelle");
        } catch (Exception e) {
            System.out.println("Erreure à la création de l'assurance : " + "Cotisation ALFA "
                    + canton.getCantonLibelleCourt() + " personnelle");
            e.printStackTrace();
        }
    }

    /**
     * @param session
     * @param canton
     * @return
     * @throws Exception
     */
    private static CARubrique createCompteCourant(BSession session, Canton canton) {
        // Vérifie le compte
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(session);
        rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);
        rubrique.setIdExterne(CreateRubriques.CC_5500_1101 + CreateRubriques.numeroCanton(canton.getNumeroCanton()));
        try {
            rubrique.retrieve();

            if (rubrique.isNew()) {

                // Créer c/c en CA
                rubrique.setSaisieEcran(true);

                rubrique.setDescriptionFr("c/c affiliés ALFA " + canton.getCantonLibelleCourt());
                rubrique.setDescriptionDe("kk Beitragspflichtige ALFA " + canton.getCantonLibelleCourt());
                rubrique.setDescriptionIt("c/c affiliés ALFA " + canton.getCantonLibelleCourt());

                // rubrique.setIdExterne(CreateRubriques.CC_5500_1101 +
                // CreateRubriques.numeroCanton(canton.getNumeroCanton()));
                rubrique.setNatureRubrique(APIRubrique.COMPTE_COURANT_DEBITEUR);
                rubrique.setIdSecteur("550");

                rubrique.add();
                System.out.println("Création de la rubrique : " + CreateRubriques.CC_5500_1101
                        + CreateRubriques.numeroCanton(canton.getNumeroCanton()) + " " + "c/c affiliés ALFA "
                        + canton.getCantonLibelleCourt());
            } else {
                System.out.println("Rubrique existante : " + CreateRubriques.CC_5500_1101
                        + CreateRubriques.numeroCanton(canton.getNumeroCanton()) + " " + "c/c affiliés ALFA "
                        + canton.getCantonLibelleCourt());
            }
        } catch (Exception e) {
            System.out.println("Erreure à la création de la rubrique : " + CreateRubriques.CC_5500_1101
                    + CreateRubriques.numeroCanton(canton.getNumeroCanton()) + " " + "c/c affiliés ALFA "
                    + canton.getCantonLibelleCourt());
            e.printStackTrace();
        }

        return rubrique;
    }

    /**
     * @param idExerciceComptable
     * @param session
     * @param canton
     * @throws Exception
     */
    private static void createCompteCourantCG(BSession session, String idExerciceComptable, Canton canton) {
        // Vérifie
        try {
            CGPlanComptableViewBean compte = new CGPlanComptableViewBean();
            compte.setSession(session);
            compte.setIdExterne(CreateRubriques.CC_5500_1101 + CreateRubriques.numeroCanton(canton.getNumeroCanton()));
            compte.setIdExerciceComptable(idExerciceComptable);
            compte.setAlternateKey(CGPlanComptableViewBean.AK_EXTERNE);
            compte.retrieve();

            if (compte.isNew()) {
                // Créer c/c en CG
                compte.setAReouvrir(true);
                compte.setIdMandat("900");
                // compte.setIdExerciceComptable(idExerciceComptable);
                // compte.setIdExterne(CreateRubriques.CC_5500_1101 +
                // CreateRubriques.numeroCanton(canton.getNumeroCanton()));
                compte.setLibelleFr("c/c affiliés ALFA " + canton.getCantonLibelleCourt());
                compte.setLibelleDe("kk Beitragspflichtige ALFA " + canton.getCantonLibelleCourt());
                compte.setLibelleIt("");
                compte.setIdNature("700001");
                compte.setCodeISOMonnaie("CHF");
                compte.add();

                System.out.println("Création du compte : " + CreateRubriques.CC_5500_1101
                        + CreateRubriques.numeroCanton(canton.getNumeroCanton()) + " " + "c/c affiliés ALFA "
                        + canton.getCantonLibelleCourt());
            } else {
                System.out.println("Compte existant : " + CreateRubriques.CC_5500_1101
                        + CreateRubriques.numeroCanton(canton.getNumeroCanton()) + " " + "c/c affiliés ALFA "
                        + canton.getCantonLibelleCourt() + " " + compte.toMyString());
            }
        } catch (Exception e) {
            System.out.println("Erreure à la création du compte : " + CreateRubriques.CC_5500_1101
                    + CreateRubriques.numeroCanton(canton.getNumeroCanton()) + " " + "c/c affiliés ALFA "
                    + canton.getCantonLibelleCourt());
            e.printStackTrace();
        }

    }

    /**
     * @param session
     * @param canton
     * @param numeroRubrique
     *            : au format xxxx.xxxx.xx
     * @param descriptionFr
     * @param descriptionDe
     * @return
     * @throws Exception
     */
    private static CARubrique createRubrique(BSession session, Canton canton, String numeroRubrique,
            String descriptionFr, String descriptionDe, String nature, Boolean tenirCompteur) {

        // Check rubrique
        CARubrique rubrique = new CARubrique();
        rubrique.setSession(session);
        rubrique.setIdExterne(numeroRubrique + CreateRubriques.numeroCanton(canton.getNumeroCanton()));
        rubrique.setAlternateKey(APIRubrique.AK_IDEXTERNE);
        try {
            rubrique.retrieve();
            // if (!rubrique.isNew()) {
            // rubrique.delete();
            // }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // Créer rubrique
        if (rubrique.isNew()) {
            rubrique.setSaisieEcran(true);

            rubrique.setDescriptionFr(descriptionFr + canton.getCantonLibelleCourt());
            rubrique.setDescriptionDe(descriptionDe + canton.getCantonLibelleCourt());
            rubrique.setDescriptionIt("");
            rubrique.setNatureRubrique(nature);
            rubrique.setIdSecteur("550");
            rubrique.setTenirCompteur(tenirCompteur);
            // rubrique.setIdContrepartie(""); // id compte courant
            rubrique.setIdExterneCompteCourantEcran(CreateRubriques.CC_5500_1101
                    + CreateRubriques.numeroCanton(canton.getNumeroCanton()));
            rubrique.setNumCompteCG(numeroRubrique + "00");

            try {
                rubrique.add();
                System.out.println("Création de la rubrique : " + numeroRubrique
                        + CreateRubriques.numeroCanton(canton.getNumeroCanton()) + " " + descriptionFr
                        + canton.getCantonLibelleCourt());
            } catch (Exception e) {
                System.out.println("Erreur à la création de la rubrique : " + numeroRubrique
                        + CreateRubriques.numeroCanton(canton.getNumeroCanton()) + " " + descriptionFr
                        + canton.getCantonLibelleCourt());
                e.printStackTrace();
            }
        } else {
            System.out.println("Rubrique existante : " + numeroRubrique
                    + CreateRubriques.numeroCanton(canton.getNumeroCanton()) + " " + descriptionFr
                    + canton.getCantonLibelleCourt());
        }

        return rubrique;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        String idExerciceComptable = CreateRubriques.EXERCICE_COMPTABLE;

        JadeLogger.info(CreateRubriques.class, "START");
        try {
            BSession session = (BSession) GlobazSystem.getApplication("OSIRIS").newSession(CreateRubriques.USER,
                    CreateRubriques.PASSWORD);
            // BSession session = new BSession("OSIRIS");
            // session.connect("h511glo", "gob4az");

            CantonManager manager = new CantonManager();
            manager.setSession(session);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                Canton canton = (Canton) manager.get(i);
                // Canton canton = (Canton) manager.getFirstEntity();
                System.out.println("Canton : " + canton.getNumeroCanton() + " " + canton.getCantonLibelleCourt());

                // Vérification des comptes en comptabilité générale

                // Création du compte courant en comptabilité générale
                CreateRubriques.createCompteCourantCG(session, idExerciceComptable, canton);

                // Création du compte courant en comptabilité auxiliaire
                CreateRubriques.createCompteCourant(session, canton);

                // Création des rubriques
                String idrubriqueContriAlfaPar = CreateRubriques.createRubrique(session, canton, "5500.4030.00",
                        "Contributions ALFA ", "ALFA-Beiträge ", APIRubrique.COTISATION_AVEC_MASSE, true)
                        .getIdRubrique();

                String idrubriqueContriAlfaInd = CreateRubriques.createRubrique(session, canton, "5500.4030.60",
                        "Contributions ALFA indépendants ", "ALFA-Beiträge SE ", APIRubrique.COTISATION_SANS_MASSE,
                        true).getIdRubrique();

                CreateRubriques.createRubrique(session, canton, "5500.3073.00",
                        "Allocations familiales aux indépendants ALFA ", "Familienzulagen ALFA SE ",
                        APIRubrique.STANDARD, false);
                CreateRubriques.createRubrique(session, canton, "5500.3075.00",
                        "Allocations familiales aux salariés ALFA ", "Familienzulagen ALFA ", APIRubrique.STANDARD,
                        false);

                // remise
                CreateRubriques.createRubrique(session, canton, "5500.3320.00", "Contributions ALFA irréc. ",
                        "Absch. ALFA-Beiträge ", APIRubrique.STANDARD, false).getIdRubrique();

                // Création de l'assurance
                // CreateRubriques.createAssurancePar(session, canton, idrubriqueContriAlfaPar);
                // CreateRubriques.createAssurancePer(session, canton, idrubriqueContriAlfaInd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JadeLogger.info(CreateRubriques.class, "STOP");
        System.exit(1);
    }

    /**
	 * 
	 */
    private static String numeroCanton(String numero) {
        if (Integer.parseInt(numero) < 10) {
            return "0" + numero;
        } else {
            return numero;
        }
    }
}
