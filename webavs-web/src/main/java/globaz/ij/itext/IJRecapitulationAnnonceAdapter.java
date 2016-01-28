/*
 * Crée ler septembre 2006
 */

package globaz.ij.itext;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.ij.db.annonces.IJAnnonce;
import globaz.ij.db.annonces.IJAnnonceManager;
import globaz.ij.db.annonces.IJPeriodeAnnonce;
import globaz.ij.db.annonces.IJPeriodeAnnonceManager;
import java.util.Collection;
import java.util.HashMap;

/**
 * <H1>Description</H1>
 * 
 * @author hpe
 */

public class IJRecapitulationAnnonceAdapter {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * <H1>Description</H1>
     * 
     * @author hpe
     */
    public class IJLigneRecapitulationAnnonce {

        // ~ Instance fields
        // --------------------------------------------------------------------------------------------

        private String cuGenreService;
        private double montantQuestionnaires;
        private double montantRestitutions;
        private double montantRetroActifs;

        private double montantTotalAC;
        private int nbCartesQuestionnaires;
        private int nbCartesRestitutions;

        private int nbCartesRetroActifs;
        private int nbJoursQuestionnaires;
        private int nbJoursRestitutions;

        private int nbJoursRetroActifs;

        // ~ Constructors
        // -----------------------------------------------------------------------------------------------

        /**
         * Crée une nouvelle instance de la classe APLigneRecapitulationAnnonce.
         */
        protected IJLigneRecapitulationAnnonce() {
        }

        // ~ Methods
        // ----------------------------------------------------------------------------------------------------

        /**
         * setter pour l'attribut montant paiements retro actifs.
         * 
         * @param d
         *            une nouvelle valeur pour cet attribut
         */
        public void addMontantPaiementsRetroActifs(double d) {
            montantRetroActifs += d;
            addMontantTotalAC(d);
        }

        /**
         * setter pour l'attribut montant questionnaires.
         * 
         * @param d
         *            une nouvelle valeur pour cet attribut
         */
        public void addMontantQuestionnaires(double d) {
            montantQuestionnaires += d;
            addMontantTotalAC(d);
        }

        /**
         * setter pour l'attribut montant restitutions.
         * 
         * @param d
         *            une nouvelle valeur pour cet attribut
         */
        public void addMontantRestitutions(double d) {
            montantRestitutions += d;
            addTotalGeneralRestitutions(d);
        }

        /**
         * setter pour l'attribut montant total AC.
         * 
         * @param d
         *            une nouvelle valeur pour cet attribut
         */
        public void addMontantTotalAC(double d) {
            montantTotalAC += d;
            addTotalGeneralAC(d);
        }

        /**
         * @param nbCartesQuestionnaires
         *            DOCUMENT ME!
         */
        public void addNbCartesQuestionnaires(int nbCartesQuestionnaires) {
            this.nbCartesQuestionnaires += nbCartesQuestionnaires;
            IJRecapitulationAnnonceAdapter.this.addNbCartesQuestionnaires(nbCartesQuestionnaires);
        }

        /**
         * @param nbCartesRestitutions
         *            DOCUMENT ME!
         */
        public void addNbCartesRestitutions(int nbCartesRestitutions) {
            this.nbCartesRestitutions += nbCartesRestitutions;
            addNbCartesRectificatives(nbCartesRestitutions);
        }

        /**
         * @param nbCartesRetroActifs
         *            DOCUMENT ME!
         */
        public void addNbCartesRetroActifs(int nbCartesRetroActifs) {
            this.nbCartesRetroActifs += nbCartesRetroActifs;
            addNbCartesRectificatives(nbCartesRetroActifs);
        }

        /**
         * setter pour l'attribut nb jours questionnaires.
         * 
         * @param i
         *            une nouvelle valeur pour cet attribut
         */
        public void addNbJoursQuestionnaires(int i) {
            nbJoursQuestionnaires += i;
        }

        /**
         * @param nbJoursRestitutions
         *            DOCUMENT ME!
         */
        public void addNbJoursRestitutions(int nbJoursRestitutions) {
            this.nbJoursRestitutions += nbJoursRestitutions;
        }

        /**
         * @param nbJoursRetroActifs
         *            DOCUMENT ME!
         */
        public void addNbJoursRetroActifs(int nbJoursRetroActifs) {
            this.nbJoursRetroActifs += nbJoursRetroActifs;
        }

        /**
         * getter pour l'attribut cu genre service.
         * 
         * @return la valeur courante de l'attribut cu genre service
         */
        public String getCuGenreService() {
            return cuGenreService;
        }

        /**
         * getter pour l'attribut montant questionnaires.
         * 
         * @return la valeur courante de l'attribut montant questionnaires un null s'il n'y en a pas pour cette ligne.
         */
        public Double getMontantQuestionnaires() {
            return (montantQuestionnaires > 0.0) ? new Double(montantQuestionnaires) : null;
        }

        /**
         * getter pour l'attribut montant restitutions.
         * 
         * @return la valeur courante de l'attribut montant restitutions ou null s'il n'y en a pas pour cette ligne.
         */
        public Double getMontantRestitutions() {
            return (Math.abs(montantRestitutions) > 0.0) ? new Double(Math.abs(montantRestitutions)) : null;
        }

        /**
         * getter pour l'attribut montant paiements retro actifs.
         * 
         * @return la valeur courante de l'attribut montant paiements retro actifs ou null s'il n'y en a pas pour cette
         *         ligne.
         */
        public Double getMontantRetroActifs() {
            return (montantRetroActifs > 0.0) ? new Double(montantRetroActifs) : null;
        }

        /**
         * getter pour l'attribut montant total AC.
         * 
         * @return la valeur courante de l'attribut montant total AC ou null s'il n'y en a pas pour cette ligne.
         */
        public Double getMontantTotalAC() {
            return (montantTotalAC > 0.0) ? new Double(montantTotalAC) : null;
        }

        /**
         * getter pour l'attribut nb cartes questionnaires.
         * 
         * @return la valeur courante de l'attribut nb cartes questionnaires
         */
        public Integer getNbCartesQuestionnaires() {
            return (nbCartesQuestionnaires > 0) ? new Integer(nbCartesQuestionnaires) : null;
        }

        /**
         * getter pour l'attribut nb cartes rectificatives.
         * 
         * @return la valeur courante de l'attribut nb cartes rectificatives
         */
        public Integer getNbCartesRectificatives() {
            return (nbCartesRectificatives > 0) ? new Integer(nbCartesRectificatives) : null;
        }

        /**
         * getter pour l'attribut nb cartes restitutions.
         * 
         * @return la valeur courante de l'attribut nb cartes restitutions
         */
        public Integer getNbCartesRestitutions() {
            return (nbCartesRestitutions > 0) ? new Integer(nbCartesRestitutions) : null;
        }

        /**
         * getter pour l'attribut nb cartes retro actifs.
         * 
         * @return la valeur courante de l'attribut nb cartes retro actifs
         */
        public Integer getNbCartesRetroActifs() {
            return (nbCartesRetroActifs > 0) ? new Integer(nbCartesRetroActifs) : null;
        }

        /**
         * getter pour l'attribut nb jours questionnaires.
         * 
         * @return la valeur courante de l'attribut nb jours questionnaires ou null s'il n'y en a pas pour cette ligne.
         */
        public Integer getNbJoursQuestionnaires() {
            return (nbJoursQuestionnaires > 0) ? new Integer(nbJoursQuestionnaires) : null;
        }

        /**
         * getter pour l'attribut nb jours rectificatifs.
         * 
         * @return la valeur courante de l'attribut nb jours rectificatifs
         */
        public Integer getNbJoursRectificatifs() {
            return ((nbJoursRetroActifs > 0) || (nbJoursRestitutions > 0)) ? new Integer(nbJoursRetroActifs
                    + nbJoursRestitutions) : null;
        }

        /**
         * getter pour l'attribut nb jours restitutions.
         * 
         * @return la valeur courante de l'attribut nb jours restitutions
         */
        public Integer getNbJoursRestitutions() {
            return (nbJoursRestitutions > 0) ? new Integer(nbJoursRestitutions) : null;
        }

        /**
         * getter pour l'attribut nb jours retro actifs.
         * 
         * @return la valeur courante de l'attribut nb jours retro actifs
         */
        public Integer getNbJoursRetroActifs() {
            return (nbJoursRetroActifs > 0) ? new Integer(nbJoursRetroActifs) : null;
        }

        /**
         * setter pour l'attribut cu genre service.
         * 
         * @param cuGenreService
         *            une nouvelle valeur pour cet attribut
         */
        public void setCuGenreService(String cuGenreService) {
            this.cuGenreService = cuGenreService;
        }

    }

    private String forMoisAnneeComptable;

    private HashMap lignes = new HashMap();

    // private IJRecapitulationAnnonceManager mgr = new
    // IJRecapitulationAnnonceManager();
    private IJAnnonceManager mgr = new IJAnnonceManager();

    private int nbCartesQuestionnaires;
    private int nbCartesRectificatives;
    private int nbCartesTotal;

    private BSession session;
    private double totalGeneralAC;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    private double totalGeneralRestitutions;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APRecapitulationAnnonceAdapter.
     * 
     * @param session
     *            DOCUMENT ME!
     * @param forMoisAnneeComptable
     *            DOCUMENT ME!
     */
    public IJRecapitulationAnnonceAdapter(BSession session, String forMoisAnneeComptable) {
        this.session = session;
        this.forMoisAnneeComptable = forMoisAnneeComptable;
    }

    /**
     * setter pour l'attribut nb cartes questionnaires.
     * 
     * @param i
     *            une nouvelle valeur pour cet attribut
     */
    public void addNbCartesQuestionnaires(int i) {
        nbCartesQuestionnaires += i;
        nbCartesTotal += i;
    }

    /**
     * setter pour l'attribut nb cartes rectificatives.
     * 
     * @param i
     *            une nouvelle valeur pour cet attribut
     */
    public void addNbCartesRectificatives(int i) {
        nbCartesRectificatives += i;
        nbCartesTotal += i;
    }

    /**
     * <p>
     * il FAUT passer par cette méthode pour que la méthode getTotalGeneralAC retourne correctement null s'il n'y a pas
     * de montant général AC.
     * </p>
     * 
     * @param d
     *            DOCUMENT ME!
     */
    public void addTotalGeneralAC(double d) {
        totalGeneralAC += d;
    }

    /**
     * <p>
     * il FAUT passer par cette méthode pour que la méthode getTotalGeneralRestitutions retourne correctement null s'il
     * n'y a pas de restitutions.
     * </p>
     * 
     * @param d
     *            DOCUMENT ME!
     */
    public void addTotalGeneralRestitutions(double d) {
        totalGeneralRestitutions += d;
    }

    /**
     * Charge les infos et trie les resultats par genre de service.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */

    public void chargerParServices() throws Exception {
        mgr.setSession(session);
        mgr.setForMoisAnneeComptable(forMoisAnneeComptable);
        // on ne doit pas condidere les annonces envoyees, mais les annonces qui
        // ne sont pas
        // erronees. Pour avoir une visibilite sur le resultat avant d'envoyer
        // les annonces
        // mgr.setForCsEtat(IIJAcor.CS_ENVOYEE);
        mgr.setForIsExclureAnnonceEnfant(Boolean.TRUE);
        BTransaction transaction = session.getCurrentThreadTransaction();
        boolean hasOpenedTransaction = false;
        BStatement statement = null;
        try {
            if (transaction == null) {
                transaction = (BTransaction) session.newTransaction();
                transaction.openTransaction();
                hasOpenedTransaction = true;
            }
            statement = mgr.cursorOpen(transaction);

            IJAnnonce annonce = null;
            while ((annonce = (IJAnnonce) mgr.cursorReadNext(statement)) != null) {
                IJLigneRecapitulationAnnonce ligne = (IJLigneRecapitulationAnnonce) lignes.get("IJAI");

                if (ligne == null) {
                    ligne = new IJLigneRecapitulationAnnonce();
                    ligne.setCuGenreService("IJAI");
                    lignes.put("IJAI", ligne);
                }

                // Si 1 --> Questionnaires | Si 3 --> Rétroactifs | Si 4 -->
                // Restitutions

                if ("1".equals(annonce.getCodeGenreCarte())) {
                    ligne.addNbCartesQuestionnaires(1);

                    IJPeriodeAnnonceManager perMgr = new IJPeriodeAnnonceManager();
                    perMgr.setSession(session);
                    perMgr.setForIdAnnonce(annonce.getIdAnnonce());
                    perMgr.find(transaction);
                    for (int i = 0; i < perMgr.size(); i++) {
                        IJPeriodeAnnonce periode = (IJPeriodeAnnonce) perMgr.getEntity(i);
                        ligne.addNbJoursQuestionnaires(Integer.parseInt(periode.getNombreJours()));

                        // AIT
                        if ("3".equals(annonce.getPetiteIJ())) {
                            ligne.addMontantQuestionnaires(Double.parseDouble(periode.getMontantAit()));
                        }
                        // AA
                        else if ("4".equals(annonce.getPetiteIJ())) {
                            ligne.addMontantQuestionnaires(Double.parseDouble(periode.getMontantAllocAssistance()));
                        }
                        // Grande IJ, Petite IJ
                        else {
                            ligne.addMontantQuestionnaires(Double.parseDouble(periode.getTotalIJ()));
                        }
                    }
                } else if ("3".equals(annonce.getCodeGenreCarte())) {
                    ligne.addNbCartesRetroActifs(1);

                    IJPeriodeAnnonceManager perMgr = new IJPeriodeAnnonceManager();
                    perMgr.setSession(session);
                    perMgr.setForIdAnnonce(annonce.getIdAnnonce());
                    perMgr.find(transaction);
                    for (int i = 0; i < perMgr.size(); i++) {
                        IJPeriodeAnnonce periode = (IJPeriodeAnnonce) perMgr.getEntity(i);
                        ligne.addNbJoursRetroActifs(Integer.parseInt(periode.getNombreJours()));

                        // AIT
                        if ("3".equals(annonce.getPetiteIJ())) {
                            ligne.addMontantPaiementsRetroActifs(Double.parseDouble(periode.getMontantAit()));
                        }
                        // AA
                        else if ("4".equals(annonce.getPetiteIJ())) {
                            ligne.addMontantPaiementsRetroActifs(Double.parseDouble(periode.getMontantAllocAssistance()));
                        }
                        // Grande IJ, Petite IJ
                        else {
                            ligne.addMontantPaiementsRetroActifs(Double.parseDouble(periode.getTotalIJ()));
                        }
                    }
                } else if ("4".equals(annonce.getCodeGenreCarte())) {
                    ligne.addNbCartesRestitutions(1);

                    IJPeriodeAnnonceManager perMgr = new IJPeriodeAnnonceManager();
                    perMgr.setSession(session);
                    perMgr.setForIdAnnonce(annonce.getIdAnnonce());
                    perMgr.find(transaction);
                    for (int i = 0; i < perMgr.size(); i++) {
                        IJPeriodeAnnonce periode = (IJPeriodeAnnonce) perMgr.getEntity(i);
                        ligne.addNbJoursRestitutions(Integer.parseInt(periode.getNombreJours()));

                        // AIT
                        if ("3".equals(annonce.getPetiteIJ())) {
                            ligne.addMontantRestitutions(Double.parseDouble(periode.getMontantAit()));
                        }
                        // AA
                        else if ("4".equals(annonce.getPetiteIJ())) {
                            ligne.addMontantRestitutions(Double.parseDouble(periode.getMontantAllocAssistance()));
                        }
                        // Grande IJ, Petite IJ
                        else {
                            ligne.addMontantRestitutions(Double.parseDouble(periode.getTotalIJ()));
                        }
                    }
                }
            }
        } finally {
            try {
                if (statement != null) {
                    try {
                        mgr.cursorClose(statement);
                    } finally {
                        statement.closeStatement();
                    }
                }
            } finally {
                if (hasOpenedTransaction) {
                    transaction.closeTransaction();
                }
            }
        }
    }

    /**
     * retourne la ligne de récapitulation pour le code système du genre de service donné.
     * 
     * @param csGenreService
     *            un code système.
     * 
     * @return la ligne de récapitulation ou null s'il n'y a pas de données pour ce moisAnneeComptable ou si l'adapter
     *         n'a pas ete charger par services.
     */
    public IJLigneRecapitulationAnnonce getLigneParCS(String csGenreService) {
        return getLigneParCU(session.getCode(csGenreService));
    }

    /**
     * retourne la ligne de récapitulation pour le code utilisateur du genre de service donné.
     * 
     * @param cuGenreService
     *            un code utilisateur.
     * 
     * @return la ligne de récapitulation ou null s'il n'y a pas de données pour ce moisAnneeComptable ou si l'adapter
     *         n'a pas ete charge par services.
     */
    public IJLigneRecapitulationAnnonce getLigneParCU(String cuGenreService) {
        IJLigneRecapitulationAnnonce retValue = (IJLigneRecapitulationAnnonce) lignes.get(cuGenreService);

        if (retValue == null) {
            // crée une ligne vide
            retValue = new IJLigneRecapitulationAnnonce();
            retValue.setCuGenreService(cuGenreService);
            lignes.put(cuGenreService, retValue);
        }

        return retValue;
    }

    /**
     * getter pour l'attribut lignes.
     * 
     * @return la valeur courante de l'attribut lignes
     */
    public Collection getLignes() {
        return lignes.values();
    }

    /**
     * getter pour l'attribut nb cartes questionnaires.
     * 
     * @return la valeur courante de l'attribut nb cartes questionnaires ou null s'il n'y a pas de cartes questionnaires
     *         pour cette annonce.
     */
    public Integer getNbCartesQuestionnaires() {
        return (nbCartesQuestionnaires != 0) ? new Integer(nbCartesQuestionnaires) : null;
    }

    /**
     * getter pour l'attribut nb cartes rectificatives.
     * 
     * @return la valeur courante de l'attribut nb cartes rectificatives ou null s'il n'y a pas de cartes rectificatives
     *         pour cette annonce.
     */
    public Integer getNbCartesRectificatives() {
        return (nbCartesRectificatives != 0) ? new Integer(nbCartesRectificatives) : null;
    }

    /**
     * getter pour l'attribut nb cartes total.
     * 
     * @return la valeur courante de l'attribut nb cartes total ou null s'il n'y a pas de cartes pour cette annonce.
     */
    public Integer getNbCartesTotal() {
        return (nbCartesTotal != 0) ? new Integer(nbCartesTotal) : null;
    }

    /**
     * getter pour l'attribut nb lignes.
     * 
     * @return la valeur courante de l'attribut nb lignes
     */
    public int getNbLignes() {
        return lignes.size();
    }

    /**
     * getter pour l'attribut total general AC.
     * 
     * @return la valeur courante de l'attribut total general AC ou null s'il n'y a pas de AC pour cette annonce.
     */
    public Double getTotalGeneralAC() {
        return (totalGeneralAC > 0.0) ? new Double(totalGeneralAC) : null;
    }

    // ~ Inner Classes
    // --------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut total general restitutions.
     * 
     * @return la valeur courante de l'attribut total general restitutions ou null s'il n'y a pas de restitutions pour
     *         le mois selectionne.
     */
    public Double getTotalGeneralRestitutions() {
        return (Math.abs(totalGeneralRestitutions) > 0.0) ? new Double(Math.abs(totalGeneralRestitutions)) : null;
    }
}
