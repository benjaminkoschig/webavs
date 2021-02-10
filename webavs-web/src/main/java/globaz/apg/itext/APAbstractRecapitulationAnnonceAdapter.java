package globaz.apg.itext;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.db.annonces.APAbstractListeRecapitulationAnnoncesManager;
import globaz.apg.db.annonces.APAbstractRecapitulationAnnonce;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Agrégateur de données pour la génération de la liste de récapitulation des annonces APG. Regroupe les fonctionnalités
 * communes aux données d'annonce APG et RAPG
 * 
 * @author PBA
 */
public abstract class APAbstractRecapitulationAnnonceAdapter {

    private APDonneesRecapitulationAnnonce donnees;
    private String forMoisAnneeComptable;
    private String forTypeAPG;
    private BSession session;

    public APAbstractRecapitulationAnnonceAdapter(BSession session, String forMoisAnneeComptable) {
        this.session = session;
        this.forMoisAnneeComptable = forMoisAnneeComptable;

        donnees = new APDonneesRecapitulationAnnonce();
    }

    public void chargerParServices() throws Exception {

        String codeUtilisateurPourCodeSystemeDemandeAllocation = getCodeUtilisateurPourCodeSysteme(IAPAnnonce.CS_DEMANDE_ALLOCATION);
        String codeUtilisateurPourCodeSystemeDuplicata = getCodeUtilisateurPourCodeSysteme(IAPAnnonce.CS_DUPLICATA);
        String codeUtilisateurPourCodeSystemePaiementRetroactif = getCodeUtilisateurPourCodeSysteme(IAPAnnonce.CS_PAIEMENT_RETROACTIF);
        String codeUtilisateurPourCodeSystemeRestitution = getCodeUtilisateurPourCodeSysteme(IAPAnnonce.CS_RESTITUTION);

        APAbstractListeRecapitulationAnnoncesManager manager = getManager();
        manager.setSession(session);
        manager.setForMoisAnneeComptable(forMoisAnneeComptable);
        manager.setForTypeAPG(forTypeAPG);
        manager.setForIsExclureAnnonceEnfant(Boolean.TRUE);
        manager.find(session.getCurrentThreadTransaction(), BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); ++i) {
            APAbstractRecapitulationAnnonce recap = (APAbstractRecapitulationAnnonce) manager.get(i);

            String genreService = "";

            if (!JadeStringUtil.isBlank(recap.getGenreService())) {

                switch (Integer.parseInt(recap.getGenreService())) {
                    case 1:
                        genreService = "10";
                        break;

                    case 2:
                        genreService = "12";
                        break;

                    case 3:
                        genreService = "20";
                        break;

                    case 4:
                        genreService = "30";
                        break;

                    case 5:
                        genreService = "50";
                        break;

                    case 6:
                        genreService = "40";
                        break;

                    case 7:
                        genreService = "13";
                        break;

                    default:
                        genreService = recap.getGenreService();

                }

                APLigneRecapitulationAnnonce ligne = new APLigneRecapitulationAnnonce();
                ligne.setCodeGenreService(genreService);

                Double sommeTotalAPG = null;
                if (!JadeStringUtil.isBlankOrZero(recap.getSommeTotalAPG())) {
                    sommeTotalAPG = Double.parseDouble(recap.getSommeTotalAPG());
                }

                Integer sommeNombreJoursService = null;
                if (!JadeStringUtil.isBlankOrZero(recap.getSommeNombreJoursService())) {
                    sommeNombreJoursService = Integer.parseInt(recap.getSommeNombreJoursService());
                }

                Integer compteCartes = null;
                if (!JadeStringUtil.isBlankOrZero(recap.getCompteCartes())) {
                    compteCartes = Integer.parseInt(recap.getCompteCartes());
                }

                if (codeUtilisateurPourCodeSystemeDemandeAllocation.equals(recap.getCodeUtilisateurTypeMontant())) {

                    ligne.setMontantQuestionnaires(sommeTotalAPG);
                    ligne.setNombreJoursQuestionnaires(sommeNombreJoursService);
                    ligne.setNombreCartesQuestionnaires(compteCartes);

                } else if (codeUtilisateurPourCodeSystemePaiementRetroactif.equals(recap
                        .getCodeUtilisateurTypeMontant())) {

                    ligne.setMontantPaiementRetroactifs(sommeTotalAPG);
                    ligne.setNombreJoursPaiementRetroactifs(sommeNombreJoursService);
                    ligne.setNombreCartesPaiementRetroactifs(compteCartes);

                } else if (codeUtilisateurPourCodeSystemeRestitution.equals(recap.getCodeUtilisateurTypeMontant())) {

                    ligne.setMontantRestitutions(sommeTotalAPG);
                    ligne.setNombreJoursRestitutions(sommeNombreJoursService);
                    ligne.setNombreCartesRestitutions(compteCartes);

                } else if (codeUtilisateurPourCodeSystemeDuplicata.equals(recap.getCodeUtilisateurTypeMontant())) {

                    ligne.setMontantDuplicata(sommeTotalAPG);
                    ligne.setNombreJoursDuplicata(sommeNombreJoursService);
                    ligne.setNombreCartesDuplicata(compteCartes);

                }

                if (!JadeStringUtil.isBlankOrZero(recap.getSommeFraisDeGarde())) {
                    donnees.addFraisGarde(Double.parseDouble(recap.getSommeFraisDeGarde()));
                }
                donnees.addLigne(ligne);
            }
        }
    }

    protected abstract String getCodeUtilisateurPourCodeSysteme(String codeSysteme) throws Exception;

    public APDonneesRecapitulationAnnonce getDonnees() {
        return donnees;
    }

    protected abstract APAbstractListeRecapitulationAnnoncesManager getManager();

    public BSession getSession() {
        return session;
    }


    public String getForTypeAPG() {
        return forTypeAPG;
    }

    public void setForTypeAPG(String forTypeAPG) {
        this.forTypeAPG = forTypeAPG;
    }
}
