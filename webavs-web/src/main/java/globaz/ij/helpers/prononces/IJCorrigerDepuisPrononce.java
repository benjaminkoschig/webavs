package globaz.ij.helpers.prononces;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JAException;
import globaz.ij.api.basseindemnisation.IIJBaseIndemnisation;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisation;
import globaz.ij.db.basesindemnisation.IJBaseIndemnisationManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Cette classe regroupe les traitements métier
 * 
 * @author rco Crée le 11.07.2013 Modifié le 30.09.2013
 * 
 */
public class IJCorrigerDepuisPrononce {

    /**
     * Retourne un message d'erreur composé de tout ce dont on a besoin.
     * 
     * @param label
     *            Le text du message dans la langue préselectionné. Par exemple :
     *            session.getLabel("ERROR_CORRIGER_DEPUIS_DATE_BASE_INDEMN")
     * @param dateCorrection
     *            La date de correction que l'utilisateur a choisit.
     * @param dateDebut
     *            La date de début du prononcé ou de la base d'indemnité
     * @param dateFin
     *            La date de début du prononcé ou de la base d'indemnité
     * 
     * @author rco
     */

    enum CorrigerPrononceErreur {
        ANNULER_INTERDIT("ERROR_ANNULER_CORRIGER_DEPUIS"),
        DATE_DANS_BASE("ERROR_CORRIGER_DEPUIS_DATE_BASE_INDEMN"),
        DATE_EGAL_DEBUT("ERROR_CORRIGER_DEPUIS_DEBUT_EGAL"),
        DATE_EGAL_FIN("ERROR_CORRIGER_DEPUIS_FIN_EGAL"),
        DATE_HORS_PLAGE("ERROR_CORRIGER_DEPUIS_DATE_PRONONCE");

        private List<String> datesMsgErreurs = new ArrayList<String>();
        private String libelle;

        /**
         * 
         * @param libelle
         */
        private CorrigerPrononceErreur(String libelle) {
            this.libelle = libelle;
        }

        /**
         * 
         * @param dateErreur
         */
        public void addErreur(String... dateErreur) {

            for (String erreur : dateErreur) {
                datesMsgErreurs.add(erreur);
            }
        }

        /**
         * 
         * @return
         */
        public List<String> getAndClearListeDateMsgErreur() {
            List<String> listeRetour = new ArrayList<String>(datesMsgErreurs);
            datesMsgErreurs = new ArrayList<String>();
            return listeRetour;
        }

        /**
         * 
         * @return
         */
        public String getLibelleErreur() {
            return libelle;
        }
    }

    private CorrigerPrononceErreur corrigePrononceErreur = null;
    private JACalendar myJACalendar = new JACalendarGregorian();

    /**
     * 
     * @param date
     * @throws IllegalArgumentException
     */
    protected void checkArgument(String date) throws IllegalArgumentException {

        if (date == null) {
            String message = "La date est à null. Voir class [IJCorrigerDepuisPrononce]";
            throw new IllegalArgumentException(message);
        }

        if (!JadeDateUtil.isGlobazDate(date)) {
            String message = "La date n'est pas dans le bon format . [" + date
                    + "] Le format est dd.MM.yyyy. Voir class [IJCorrigerDepuisPrononce]";
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 
     * @param baseIndemnisationManager
     * @param prononce1
     * @param prononce2
     * @param dateCorrection
     * @throws Exception
     * @throws JAException
     */
    public void deplaceBaseIndemnite(IJBaseIndemnisationManager baseIndemnisationManager, IJPrononce prononce1,
            IJPrononce prononce2, String dateCorrection) throws Exception, JAException {

        prononce2.setCsEtat(IIJPrononce.CS_COMMUNIQUE);

        /*
         * Pour pouvoir déplacer les BI, il faut qu'elles soient dans un état différent de ANNULER.
         * 
         * Si elles sont ANNULER mais qu'elles possèdent un enfant dans un autre état et que les dates correspondent
         * alors elles peuvent être déplacés.
         * 
         * Si elles sont annulés sans enfants et que les dates correspondent alors elle peuvent être déplacés aussi.
         * 
         * Dans tout les autres cas, elles ne sont pas déplacés.
         */

        // Stockage des BI qui ne peuvent pas être copié tout de suite
        List<IJBaseIndemnisation> baseIndemniteTmp = new ArrayList<IJBaseIndemnisation>();
        // Stockage des id corrections des BI qui ne peuvent pas être copié tout de suite
        List<String> baseIndemniteId = new ArrayList<String>();
        // Stockage des id correction qui ne peuvent être corrigé tout de suite
        List<String> baseIndemniteEnfant = new ArrayList<String>();
        // Stockage des id correction qui ne doivent pas être déplacés
        List<String> baseIndemniteEnfantATuer = new ArrayList<String>();

        deplaceBaseIndemnitePasAnnullerDateSuperieurADateCorrection(baseIndemnisationManager, prononce2,
                dateCorrection, baseIndemniteTmp, baseIndemniteId, baseIndemniteEnfant, baseIndemniteEnfantATuer);

        deplaceBaseIndemniteEnfants(prononce2, baseIndemniteTmp, baseIndemniteId, baseIndemniteEnfant);

        supprimerBaseIndemniteNonDeplace(baseIndemniteTmp, baseIndemniteId, baseIndemniteEnfantATuer);

        deplaceBaseIndemniteEtatAnnulerNeCorrigeantRien(prononce2, dateCorrection, baseIndemniteTmp);

        // Finalement, les dates de début et de fin des prononcés doivent être ajustées
        // prononcé P1 est mise à jour avec le la date du jour précédant la date de correction donnée
        String newDateFinPrononce = myJACalendar.addDays(dateCorrection, -1);
        prononce1.setDateFinPrononce(newDateFinPrononce);

        // La date de début du prononcé P2 est mise à jour avec la date de la correction
        prononce2.setDateDebutPrononce(dateCorrection);
        // On marque que le prononce2 est issus du prononce1
        prononce2.setIdParentCorrigeDepuis(prononce1.getIdPrononce());
    }

    private void deplaceBaseIndemniteEnfants(IJPrononce prononce2, List<IJBaseIndemnisation> baseIndemniteTmp,
            List<String> baseIndemniteId, List<String> baseIndemniteEnfant) throws Exception {
        // On déplace les BI référencés par baseIndemniteEnfant
        int index;
        for (int i = 0; i < baseIndemniteEnfant.size(); i++) {

            if (baseIndemniteId.indexOf(baseIndemniteEnfant.get(i)) != -1) {
                index = baseIndemniteId.indexOf(baseIndemniteEnfant.get(i));

                baseIndemniteTmp.get(index).setIdPrononce(prononce2.getIdPrononce());
                baseIndemniteTmp.get(index).update();

                if (!JadeStringUtil.isBlankOrZero(baseIndemniteTmp.get(i).getIdCorrection())) {
                    baseIndemniteEnfant.add(baseIndemniteTmp.get(i).getIdCorrection());
                }

                baseIndemniteTmp.remove(index);
                baseIndemniteId.remove(index);
            }
        }
    }

    private void deplaceBaseIndemniteEtatAnnulerNeCorrigeantRien(IJPrononce prononce2, String dateCorrection,
            List<IJBaseIndemnisation> baseIndemniteTmp) throws JAException, Exception {
        // On déplace toutes les BI qui sont dans l'état ANNULER et qui ne corrigent personne
        for (int i = 0; i < baseIndemniteTmp.size(); i++) {
            if ((myJACalendar.compare(baseIndemniteTmp.get(i).getDateFinPeriode(), dateCorrection) == (JACalendar.COMPARE_FIRSTUPPER))
                    && (JadeStringUtil.isBlankOrZero(baseIndemniteTmp.get(i).getIdCorrection()))) {
                baseIndemniteTmp.get(i).setIdPrononce(prononce2.getIdPrononce());
                baseIndemniteTmp.get(i).update();
            }
        }
    }

    /**
     * 
     * @param baseIndemnisationManager
     * @param prononce2
     * @param dateCorrection
     * @param baseIndemniteTmp
     * @param baseIndemniteId
     * @param baseIndemniteEnfant
     * @param baseIndemniteEnfantATuer
     * @throws JAException
     * @throws Exception
     */
    private void deplaceBaseIndemnitePasAnnullerDateSuperieurADateCorrection(
            IJBaseIndemnisationManager baseIndemnisationManager, IJPrononce prononce2, String dateCorrection,
            List<IJBaseIndemnisation> baseIndemniteTmp, List<String> baseIndemniteId, List<String> baseIndemniteEnfant,
            List<String> baseIndemniteEnfantATuer) throws JAException, Exception {
        // Les bases d’indemnisation du prononcé1 qui ont :
        // - Une date de début plus grande ou égale à la date de correction
        // - Qui ne sont pas dans l'état ANNULER
        // => Sont déplacés tout de suite
        for (int i = 0; i < baseIndemnisationManager.size(); i++) {
            IJBaseIndemnisation baseIndemnisation = (IJBaseIndemnisation) baseIndemnisationManager.getEntity(i);

            // On stocke les BI qui sont ANNULER
            if (baseIndemnisation.getCsEtat().equals(IIJBaseIndemnisation.CS_ANNULE)) {
                baseIndemniteTmp.add(baseIndemnisation);
                baseIndemniteId.add(baseIndemnisation.getIdBaseIndemisation());

            } else if (myJACalendar.compare(baseIndemnisation.getDateFinPeriode(), dateCorrection) == (JACalendar.COMPARE_FIRSTUPPER)) {
                baseIndemnisation.setIdPrononce(prononce2.getIdPrononce());
                baseIndemnisation.update();

                // Si cette base corrige une autre alors on stocke l'id de celle qui est corrigé dans
                // baseIndemniteEnfant
                if (!JadeStringUtil.isBlankOrZero(baseIndemnisation.getIdCorrection())) {
                    baseIndemniteEnfant.add(baseIndemnisation.getIdCorrection());
                }
            } // Si une base ne doit pas être déplacé mais qu'elle a un id corrigé alors on stocke l'id dans
              // baseIndemniteEnfantATuer
            else if (!JadeStringUtil.isBlankOrZero(baseIndemnisation.getIdCorrection())) {
                baseIndemniteEnfantATuer.add(baseIndemnisation.getIdCorrection());
            }
        }
    }

    /**
     * 
     * @return
     */
    public CorrigerPrononceErreur getTypeErreur() {
        return corrigePrononceErreur;
    }

    private boolean isDateCorrectionEntreDateDebutEtFinPrononce(final String dateCorrection,
            final String dateDebutPrononce, final String dateFinPrononce) throws JAException {
        if ((myJACalendar.compare(dateDebutPrononce, dateCorrection) == JACalendar.COMPARE_FIRSTLOWER)
                && (myJACalendar.compare(dateFinPrononce, dateCorrection) == JACalendar.COMPARE_FIRSTUPPER || myJACalendar
                        .compare(dateFinPrononce, dateCorrection) == JACalendar.COMPARE_EQUALS)) {

            return true;
        } else {
            corrigePrononceErreur = CorrigerPrononceErreur.DATE_HORS_PLAGE;
            CorrigerPrononceErreur.DATE_HORS_PLAGE.addErreur(dateCorrection, dateDebutPrononce, dateFinPrononce);
            return false;
        }
    }

    private boolean isDateDebutPrononceEgalDateCorrection(final String dateCorrection, final String dateDebutPrononce,
            final String dateFinPrononce) throws JAException {
        if (myJACalendar.compare(dateDebutPrononce, dateCorrection) == JACalendar.COMPARE_EQUALS) {
            corrigePrononceErreur = CorrigerPrononceErreur.DATE_EGAL_DEBUT;
            CorrigerPrononceErreur.DATE_EGAL_DEBUT.addErreur(dateCorrection, dateDebutPrononce, dateFinPrononce);
            return false;
        }
        return true;
    }

    public void prononceAAnnulerNonValide(final String dateDebutPrononceAAnnuler,
            final String dateDebutPrononceAvantCeluiAAnnuler) throws JAException {

        corrigePrononceErreur = CorrigerPrononceErreur.ANNULER_INTERDIT;
        CorrigerPrononceErreur.ANNULER_INTERDIT.addErreur(dateDebutPrononceAAnnuler,
                dateDebutPrononceAvantCeluiAAnnuler);
    }

    public void setTypeErreur(CorrigerPrononceErreur corrigerPrononceErreur) {
        corrigePrononceErreur = corrigerPrononceErreur;
    }

    private void supprimerBaseIndemniteNonDeplace(List<IJBaseIndemnisation> baseIndemniteTmp,
            List<String> baseIndemniteId, List<String> baseIndemniteEnfantATuer) {
        int index;
        // Suppression des BI qui ne sont pas déplacés
        for (int i = 0; i < baseIndemniteEnfantATuer.size(); i++) {
            if (baseIndemniteId.indexOf(baseIndemniteEnfantATuer.get(i)) != -1) {
                index = baseIndemniteId.indexOf(baseIndemniteEnfantATuer.get(i));

                if (!JadeStringUtil.isBlankOrZero(baseIndemniteTmp.get(index).getIdCorrection())) {
                    baseIndemniteEnfantATuer.add(baseIndemniteTmp.get(index).getIdCorrection());
                }

                baseIndemniteTmp.remove(index);
                baseIndemniteId.remove(index);
            }
        }
    }

    /**
     * 
     * @param dateCorrection
     * @param baseIndemnisationManager
     * @return
     * @throws Exception
     * @throws IllegalArgumentException
     */
    public boolean verifierDateCorrectionDansBaseIndemnite(final String dateCorrection,
            IJBaseIndemnisationManager baseIndemnisationManager) throws Exception, IllegalArgumentException {

        checkArgument(dateCorrection);

        // Comparaison des dates
        for (int i = 0; i < baseIndemnisationManager.size(); i++) {
            IJBaseIndemnisation baseIndemnisation = (IJBaseIndemnisation) baseIndemnisationManager.getEntity(i);

            if (baseIndemnisation.getCsEtat().equals(IIJBaseIndemnisation.CS_ANNULE)) {
                continue;
            }

            // Est-ce que la BI est coupé ?
            if ((myJACalendar.compare(baseIndemnisation.getDateDebutPeriode(), dateCorrection) == JACalendar.COMPARE_FIRSTLOWER)
                    && ((myJACalendar.compare(baseIndemnisation.getDateFinPeriode(), dateCorrection) == JACalendar.COMPARE_FIRSTUPPER) || (myJACalendar
                            .compare(baseIndemnisation.getDateFinPeriode(), dateCorrection) == JACalendar.COMPARE_EQUALS))) {

                corrigePrononceErreur = CorrigerPrononceErreur.DATE_DANS_BASE;
                CorrigerPrononceErreur.DATE_DANS_BASE.addErreur(dateCorrection,
                        baseIndemnisation.getDateDebutPeriode(), baseIndemnisation.getDateFinPeriode());

                return false;
            } else {
                if (i + 1 < baseIndemnisationManager.size()) {
                    continue;
                }
            }
        }

        return true;
    }

    /**
     * 
     * @param dateCorrection
     * @param dateDebutPrononce
     * @param dateFinPrononce
     * @return
     * @throws Exception
     * @throws IllegalArgumentException
     */
    public boolean verifierDateCorrectionDansPrononce(final String dateCorrection, final String dateDebutPrononce,
            final String dateFinPrononce) throws Exception, IllegalArgumentException {

        checkArgument(dateCorrection);
        checkArgument(dateDebutPrononce);
        checkArgument(dateFinPrononce);

        if (!isDateDebutPrononceEgalDateCorrection(dateCorrection, dateDebutPrononce, dateFinPrononce)) {
            return false;
        }

        return isDateCorrectionEntreDateDebutEtFinPrononce(dateCorrection, dateDebutPrononce, dateFinPrononce);
    }
}
