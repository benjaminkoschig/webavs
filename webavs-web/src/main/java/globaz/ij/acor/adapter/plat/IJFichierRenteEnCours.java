/*
 * Créé le 2 mai 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ij.acor.adapter.plat;

import globaz.ij.application.IJApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORException;
import globaz.prestation.acor.plat.PRAbstractFichierPlatPrinter;
import globaz.prestation.acor.plat.PRAbstractPlatAdapter;
import java.io.PrintWriter;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * un printer qui cree les donnees pour le fichier rente d'ACOR.
 * </p>
 * 
 * <p>
 * Les donnees
 * </p>
 * 
 * @author hpe
 */
public class IJFichierRenteEnCours extends PRAbstractFichierPlatPrinter {

    private static final int NB_POS_CODES_CAS_SPECIAL = 10;

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private int nbLignes = 0;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJFichierRenteEnCours.
     * 
     * @param parent
     *            DOCUMENT ME!
     * @param nomFichier
     *            DOCUMENT ME!
     */
    public IJFichierRenteEnCours(PRAbstractPlatAdapter parent, String nomFichier) {
        super(parent, nomFichier);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    private IJACORPrononceAdapter adapter() {
        return (IJACORPrononceAdapter) parent;
    }

    /**
     * Format les codes des cas special selon les normes ACOR 5 codes de 2 positions, cadrés à droite, positions non
     * utilisée à 0
     * 
     * @param string
     * @return
     */
    private String formatCodesCS(String ccs) {
        StringBuffer formatedCcs = new StringBuffer();

        // on recupere les code donnes
        for (int i = 0; i < ccs.length(); i++) {

            // on supprime la mise en forme "-"
            if (ccs.charAt(i) == '-') {
                continue;
            } else {
                formatedCcs.append(ccs.charAt(i));
            }
        }

        // on complete avec des 0
        for (int i = formatedCcs.length(); i < NB_POS_CODES_CAS_SPECIAL; i++) {
            formatedCcs.insert(0, "0");
        }

        return formatedCcs.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.PRFichierACORPrinter#hasLignes()
     */
    @Override
    public boolean hasLignes() throws PRACORException {

        if (JadeStringUtil.isIntegerEmpty(adapter().getPrononce().getMontantRenteEnCours())) {
            return false;
        } else {
            if (nbLignes > 0) {
                return false;
            } else {
                nbLignes++;
                return true;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.acor.PRFichierACORPrinter#printLigne(java.io.PrintWriter )
     * 
     * @deprecated
     */
    public void printLigne(PrintWriter writer) throws PRACORException {

        nbLignes = 0;
        if (hasLignes()) {

            // Date au format YYYYMM
            String mois = (adapter().getPrononce().getDateDebutPrononce()).substring(3, 5);
            String annee = (adapter().getPrononce().getDateDebutPrononce()).substring(6, 10);

            String dateDebutPrononce = annee + mois;

            // 1. N° AVS du bénéficiaire
            writeAVS(writer, adapter().numeroAVSAssure());

            // 2. Genre de prestation
            try {
                writeEntier(writer,
                        getSession().getApplication().getProperty(IJApplication.PROPERTY_GENRE_PRESTATION_ACOR));
            } catch (Exception e) {
                // TODO Bloc catch auto-généré
                e.printStackTrace();
            }

            // 3. Fraction de rente (toujours 1 dans ce cas)
            writeEntier(writer, "1");

            // 4. Date de début de droit
            writeEntier(writer, dateDebutPrononce);

            // 5. Date de fin du droit (non utilisé dans ce cas)
            writeChampVide(writer);

            // 6. Montant prestation
            Double MontantDouble = Double.valueOf(adapter().getPrononce().getMontantRenteEnCours());
            int MontantInt = MontantDouble.intValue();
            String MontantString = String.valueOf(MontantInt);
            writeEntier(writer, MontantString);

            // 7. Revenu annuel moyen
            String ram = adapter().getPrononce().getRam();
            if (!JadeStringUtil.isEmpty(ram)) {
                ram = String.valueOf(new Float(ram).intValue());
            }
            writeEntier(writer, ram);

            // 8. Durée cotisation RAM (non utilisé dans ce cas)
            writeChampVide(writer);

            // 9. Code Revenus (non utilisé dans ce cas)
            writeChampVide(writer);

            // 10. Echelle On la force à 44.
            String echelle = adapter().getPrononce().getEchelle();
            // si on ne donne pas d'echelle on force a 44
            if (JadeStringUtil.isEmpty(echelle) || "0".equals(echelle)) {
                writeEntier(writer, "44");
            } else {
                writeEntier(writer, echelle);
            }

            // 11. Durée cotisation avant 73 (non utilisé dans ce cas)
            writeChampVide(writer);

            // 12. Durée cotisation après 73 (non utilisé dans ce cas)
            writeChampVide(writer);

            // 13. Mois d'appoint avant 73 MM (non utilisé dans ce cas)
            writeChampVide(writer);

            // 14. Mois d'appoint après 73 MM (non utilisé dans ce cas)
            writeChampVide(writer);

            // 15. durée de cotisation de la classe d'âge (non utilisé dans ce
            // cas)
            writeChampVide(writer);

            // 16. Année de niveau (non utilisé dans ce cas)
            writeChampVide(writer);

            // 17. Codes cas spécial (non utilisé dans ce cas)
            // place pour 5 codes a 2 position, cadre a droite, positions
            // inutilisees a 0 ou a blanc
            String ccs = formatCodesCS(adapter().getPrononce().getCodesCasSpecial());
            writeChaine(writer, ccs);

            // 18. Supplément de carrière (non utilisé dans ce cas)
            writeChampVide(writer);

            // 19. Degré d'invalidité (100 dans tous les cas pour cet exemple)
            writeEntier(writer, "100");

            // 20. Clé d'infirmité (non utilisé dans ce cas)
            writeChampVide(writer);

            // 21. Survenance événement assuré (toujours date début prononcé
            // dans cet exemple)
            writeEntier(writer, dateDebutPrononce);

            // 22. Invalide précoce (non utilisé dans ce cas)
            writeChampVide(writer);

            // 23. Office AI (non utilisé dans ce cas)
            writeChampVide(writer);

            // 24. Durée ajournement (non utilisé dans ce cas)
            writeChampVide(writer);

            // 25. Supplément d'ajournement (non utilisé dans ce cas)
            writeChampVide(writer);

            // 26. Date de révocation d'ajournement (non utilisé dans ce cas)
            writeChampVide(writer);

            // 27. Montant du bonus éducatif (non utilisé dans ce cas)
            writeChampVide(writer);

            // 28. Nombre d'années de bonif. pour tâches d'éducation (non
            // utilisé dans ce cas)
            writeChampVide(writer);

            // 29. Nombre d'années de bonif. pour tâches d'assistance (non
            // utilisé dans ce cas)
            writeChampVide(writer);

            // 30. Nombre d'années de bonif. transitoires (non utilisé dans ce
            // cas)
            writeChampVide(writer);

            // 31. Code revenus splittés (non utilisé dans ce cas)
            writeChampVide(writer);

            // 32. Code survivant invalid (non utilisé dans ce cas)
            writeChampVide(writer);

            // 33. Nombre d'années d'anticipation (non utilisé dans ce cas)
            writeChampVide(writer);

            // 34. Montant de la réduction pour anticipation (non utilisé dans
            // ce cas)
            writeChampVide(writer);

            // 35. Date de début d'anticipation (non utilisé dans ce cas)
            writeChampVide(writer);

            // 36. Somme des revenus non revalorisés (non utilisé dans ce cas)
            writeChampVide(writer);

            // 37. Première année de cotisation (non utilisé dans ce cas)
            writeChampVide(writer);

            // 38. Année du montant et du RAM (non utilisé dans ce cas)
            String etat = adapter().getPrononce().getAnneeRenteEnCours();
            if (!JadeStringUtil.isIntegerEmpty(etat)) {
                writeEntier(writer, etat);
            } else {
                writeChampVide(writer);
            }

            // 39. Durée cotisation étrangère avant 73 (non utilisé dans ce cas)
            writeChampVide(writer);

            // 40. Durée cotisation étrangère après 73 (non utilisé dans ce cas)
            writeChampVide(writer);

            // 41. Transférée (non utilisé dans ce cas)
            writeChampVide(writer);

            // 42. Droit appliqué (toujours 10 dans cet exemple)
            writeEntier(writer, "10");

        }
    }

    @Override
    public void printLigne(StringBuffer cmd) throws PRACORException {
        nbLignes = 0;
        if (hasLignes()) {

            // Date au format YYYYMM
            String mois = (adapter().getPrononce().getDateDebutPrononce()).substring(3, 5);
            String annee = (adapter().getPrononce().getDateDebutPrononce()).substring(6, 10);

            String dateDebutPrononce = annee + mois;

            // 1. N° AVS du bénéficiaire
            writeAVS(cmd, adapter().numeroAVSAssure());

            // 2. Genre de prestation
            try {
                writeEntier(cmd, getSession().getApplication()
                        .getProperty(IJApplication.PROPERTY_GENRE_PRESTATION_ACOR));
            } catch (Exception e) {
                // TODO Bloc catch auto-généré
                e.printStackTrace();
            }

            // 3. Fraction de rente (toujours 1 dans ce cas)
            writeEntier(cmd, "1");

            // 4. Date de début de droit
            writeEntier(cmd, dateDebutPrononce);

            // 5. Date de fin du droit (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 6. Montant prestation
            Double MontantDouble = Double.valueOf(adapter().getPrononce().getMontantRenteEnCours());
            int MontantInt = MontantDouble.intValue();
            String MontantString = String.valueOf(MontantInt);
            writeEntier(cmd, MontantString);

            // 7. Revenu annuel moyen
            String ram = adapter().getPrononce().getRam();
            if (!JadeStringUtil.isEmpty(ram)) {
                ram = String.valueOf(new Float(ram).intValue());
            }
            writeEntier(cmd, ram);

            // 8. Durée cotisation RAM (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 9. Code Revenus (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 10. Echelle On la force à 44.
            String echelle = adapter().getPrononce().getEchelle();
            // si on ne donne pas d'echelle on force a 44
            if (JadeStringUtil.isEmpty(echelle) || "0".equals(echelle)) {
                writeEntier(cmd, "44");
            } else {
                writeEntier(cmd, echelle);
            }

            // 11. Durée cotisation avant 73 (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 12. Durée cotisation après 73 (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 13. Mois d'appoint avant 73 MM (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 14. Mois d'appoint après 73 MM (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 15. durée de cotisation de la classe d'âge (non utilisé dans ce
            // cas)
            writeChampVide(cmd);

            // 16. Année de niveau (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 17. Codes cas spécial (non utilisé dans ce cas)
            // place pour 5 codes a 2 position, cadre a droite, positions
            // inutilisees a 0 ou a blanc
            String ccs = formatCodesCS(adapter().getPrononce().getCodesCasSpecial());
            writeChaine(cmd, ccs);

            // 18. Supplément de carrière (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 19. Degré d'invalidité (100 dans tous les cas pour cet exemple)
            writeEntier(cmd, "100");

            // 20. Clé d'infirmité (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 21. Survenance événement assuré (toujours date début prononcé
            // dans cet exemple)
            writeEntier(cmd, dateDebutPrononce);

            // 22. Invalide précoce (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 23. Office AI (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 24. Durée ajournement (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 25. Supplément d'ajournement (non utilisé dans ce cas)
            writeEntier(cmd, "0");

            // 26. Date de révocation d'ajournement (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 27. Montant du bonus éducatif (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 28. Nombre d'années de bonif. pour tâches d'éducation (non
            // utilisé dans ce cas)
            writeChampVide(cmd);

            // 29. Nombre d'années de bonif. pour tâches d'assistance (non
            // utilisé dans ce cas)
            writeChampVide(cmd);

            // 30. Nombre d'années de bonif. transitoires (non utilisé dans ce
            // cas)
            writeChampVide(cmd);

            // 31. Code revenus splittés (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 32. Code survivant invalid (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 33. Nombre d'années d'anticipation (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 34. Montant de la réduction pour anticipation (non utilisé dans
            // ce cas)
            writeEntier(cmd, "0");

            // 35. Date de début d'anticipation (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 36. Somme des revenus non revalorisés (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 37. Première année de cotisation (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 38. Année du montant et du RAM (non utilisé dans ce cas)
            String etat = adapter().getPrononce().getAnneeRenteEnCours();
            if (!JadeStringUtil.isIntegerEmpty(etat)) {
                writeEntier(cmd, etat);
            } else {
                writeChampVide(cmd);
            }

            // 39. Durée cotisation étrangère avant 73 (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 40. Durée cotisation étrangère après 73 (non utilisé dans ce cas)
            writeChampVide(cmd);

            // 41. Transférée (non utilisé dans ce cas)
            writeBoolean(cmd, false);

            // 42. Droit appliqué (toujours 10 dans cet exemple)
            writeEntier(cmd, "10");

        }

    }

}
