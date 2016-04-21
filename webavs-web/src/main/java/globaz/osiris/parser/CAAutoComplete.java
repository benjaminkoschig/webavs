package globaz.osiris.parser;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.naos.api.IAFAdhesion;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CARole;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.translation.CACodeSystem;
import java.util.Hashtable;

/**
 * @author dda
 */
public class CAAutoComplete {

    private static final String LABEL_SOLDE = "SOLDE";
    private static final String PROPERTY_COMPTE_ANNEXE_AUTOCOMPLETE_START = "compteAnnexeAutoCompleteStart";

    /**
     * Recherche les affiliés en fonction du masque like (affiliés de type standard).
     * 
     * @param session
     * @param like
     * @return
     */
    public static String getAffilies(BSession session, String like) {
        return CAAutoComplete.getAffilies(session, like, CACompteAnnexe.GENRE_COMPTE_STANDARD);
    }

    /**
     * Recherche les affiliés en fonction du masque like.
     * 
     * @param session
     * @param like
     * @return
     */
    public static String getAffilies(BSession session, String like, String forIdGenreCompte) {
        if (like == null) {
            like = "";
        }

        String options = "";
        try {
            CACompteAnnexeManager manager = new CACompteAnnexeManager();

            manager.setSession(session);
            manager.setLikeNumNom(like);

            // on ne veut retourner que les comptes pour lesquels l'utilisateur
            // courant a le droit de lecture.
            manager.setForSelectionRole(CARole.listeIdsRolesPourUtilisateurCourant(session));

            if (!JadeStringUtil.isBlank(forIdGenreCompte)) {
                manager.setForIdGenreCompte(forIdGenreCompte);
            }

            manager.find(BManager.SIZE_USEDEFAULT);

            for (int i = 0; i < manager.size(); i++) {
                CACompteAnnexe compteAnnexe = (CACompteAnnexe) manager.get(i);

                options += "<OPTION value=\"";
                options += compteAnnexe.getIdExterneRole() + "\" selectedIdCompteAnnexe=\"";
                options += compteAnnexe.getIdCompteAnnexe() + "\" selectedIdRole=\"";
                options += compteAnnexe.getIdRole() + "\" selectedCompteAnnexeDesc=\"";
                options += JadeStringUtil.change(compteAnnexe.getTiers().getNom(), "\"", "&quot;") + "\">";
                options += compteAnnexe.getIdExterneRole() + ", " + compteAnnexe.getRole().getDescription() + " "
                        + compteAnnexe.getTiers().getNom() + ", " + session.getLabel(CAAutoComplete.LABEL_SOLDE) + " "
                        + compteAnnexe.getSoldeFormate() + " CHF";
                options += "</OPTION>";

            }

            if (JadeStringUtil.isBlank(options)) {
                options += "<OPTION value=\"" + like
                        + "\" selectedIdCompteAnnexe=\"\" selectedIdRole=\"\" selectedCompteAnnexeDesc=\"\">" + like
                        + "</OPTION>";
            }
        } catch (Exception e) {
            JadeLogger.warn(CAAutoComplete.class, e);
        }

        return options;
    }

    /**
     * Recherche les affiliés en fonction du masque like (affiliés de type auxiliaire).
     * 
     * @param session
     * @param like
     * @return
     */
    public static String getAffiliesAux(BSession session, String like) {
        String result = "";
        try {
            FWParametersSystemCodeManager manager = CACodeSystem.getGenreComptes(session);

            for (int i = 0; i < manager.size(); i++) {
                FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                if (!JadeStringUtil.isBlank(code.getLibelle())) {
                    result += CAAutoComplete.getAffilies(session, like, code.getIdCode());
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(CAAutoComplete.class, e);
        }

        return result;
    }

    /**
     * Recherche les caisses professionnelles en fonction du masque like (toLowerCase).
     * 
     * @param session Une session valide
     * @param like Un numéro de caisse professionelle
     * @param idExterneRole Un numéro d'affilié
     * @param idRole Le code systeme d'un rôle
     * @return Le code html de selection de la caisse professionnelle
     */
    public static String getCaissesProf(BSession session, String like, String idExterneRole, String idRole) {
        if (like == null) {
            like = "";
        }

        String options = "";
        try {
            CACompteAnnexe compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setSession(session);
            compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compteAnnexe.setIdRole(idRole);
            compteAnnexe.setIdExterneRole(idExterneRole);

            compteAnnexe.retrieve();

            if (compteAnnexe.isNew() || (compteAnnexe.getRole().getAffiliation(idExterneRole) == null)) {
                return options;
            }

            BSession sessionAffiliation = (BSession) GlobazSystem.getApplication("NAOS").newSession();
            session.connectSession(sessionAffiliation);

            Hashtable<String, String> criteres = new Hashtable<String, String>();
            criteres.put(IAFAdhesion.FIND_FOR_AFFILIATION_ID, compteAnnexe.getRole().getAffiliation(idExterneRole)
                    .getAffiliationId());
            criteres.put(IAFAdhesion.FIND_FOR_TYPE_ADHESION, IAFAdhesion.ADHESION_CAISSE_PRINCIPALE);

            IAFAdhesion adhesionHelper = (IAFAdhesion) sessionAffiliation.getAPIFor(IAFAdhesion.class);
            adhesionHelper.setISession(session);
            IAFAdhesion[] adhesions = adhesionHelper.findAdhesions(criteres);

            for (int i = 0; i < adhesions.length; i++) {
                IAFAdhesion adhesion = adhesions[i];

                if (((adhesion.getTypeAdhesionLibelle().toLowerCase().indexOf(like.toLowerCase()) > -1) || (adhesion
                        .getAdministrationCaisseCode().indexOf(like.toLowerCase()) > -1))
                        && (options.indexOf(adhesion.getAdministrationCaisseId()) == -1)) {
                    options += "<OPTION value=\"";
                    options += adhesion.getAdministrationCaisseCode() + "\" selectedCaissesProfLibelle=\"";
                    options += adhesion.getAdministrationCaisseLibelle() + "\" selectedIdCaissesProf=\"";
                    options += adhesion.getAdministrationCaisseId() + "\">";
                    options += adhesion.getAdministrationCaisseCode() + " " + adhesion.getAdministrationCaisseLibelle();
                    options += "</OPTION>";
                }
            }

            if (JadeStringUtil.isBlank(options)) {
                options += "<OPTION value=\"" + like + "\">" + like + "</OPTION>";
            }
        } catch (Exception e) {
            JadeLogger.warn(CAAutoComplete.class, e);
        }

        return options;
    }

    /**
     * Retourne l'index de démarrage de la fonction auto-complete du compte annexe. Utilisé pour la saisie d'écritures.
     * 
     * @param bSession
     * @return
     */
    public static int getCompteAnnexeAutoCompleteStart(BSession bSession) {
        try {
            return Integer.parseInt((bSession.getApplication())
                    .getProperty(CAAutoComplete.PROPERTY_COMPTE_ANNEXE_AUTOCOMPLETE_START));
        } catch (Exception e) {
            JadeLogger.warn(e, "Constante " + CAAutoComplete.PROPERTY_COMPTE_ANNEXE_AUTOCOMPLETE_START
                    + " non résolue.");
            return -1;
        }
    }

    private static String getCompteAnnexeAuxNom(BSession session, CASection section) throws Exception {
        CACompteAnnexe compteAnnexeAux = new CACompteAnnexe();
        compteAnnexeAux.setSession(session);
        compteAnnexeAux.setIdCompteAnnexe(section.getIdCompteAnnexe());
        compteAnnexeAux.retrieve();

        if (compteAnnexeAux.isNew()) {
            return "";
        } else {
            return compteAnnexeAux.getIdExterneRole() + " - " + compteAnnexeAux.getTiers().getNom();
        }
    }

    /**
     * Recherche les sections en fonction du compte annexe (idExterneRole et idRole) et du masque like.
     * 
     * @param session
     * @param like
     * @param idExterneRole
     * @param idRole
     * @return
     */
    public static String getSections(BSession session, String like, String idExterneRole, String idRole) {
        if (like == null) {
            like = "";
        }

        String options = "";
        try {
            CACompteAnnexe compteAnnexe = new CACompteAnnexe();
            compteAnnexe.setSession(session);
            compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
            compteAnnexe.setIdRole(idRole);
            compteAnnexe.setIdExterneRole(idExterneRole);

            compteAnnexe.retrieve();

            if (compteAnnexe.isNew()) {
                return options;
            }

            CASectionManager manager = new CASectionManager();
            manager.setSession(session);
            manager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
            manager.setLikeIdExterne(like);
            manager.find(BManager.SIZE_USEDEFAULT);

            for (int i = 0; i < manager.size(); i++) {
                CASection section = (CASection) manager.get(i);

                options += "<OPTION value=\"";
                options += section.getIdExterne() + "\" selectedIdSection=\"";
                options += section.getIdSection() + "\" selectedIdPrincipale=\"";
                options += "" + CAAutoComplete.isSectionPrincipale(session, section.getIdSection())
                        + "\" selectedIdTypeSection=\"";
                options += section.getIdTypeSection() + "\" selectedSectionDesc=\"";
                options += section.getDescription();
                options += "\" " + "selectedIdPlan=\"" + section.getIdPlanRecouvrement(); // XXX
                // sel
                options += "\">";
                options += section.getIdExterne() + ", " + section.getTypeSection().getDescription() + ", ";

                options += session.getLabel(CAAutoComplete.LABEL_SOLDE) + " " + section.getSoldeFormate();

                options += "</OPTION>";

            }

            if (JadeStringUtil.isBlank(options)) {
                options += "<OPTION value=\""
                        + like
                        + "\" selectedIdSection=\"\" selectedIdTypeSection=\"\" selectedSectionDesc=\"\" selectedIdPlan=\"0\">"
                        + like + "</OPTION>";
            }
        } catch (Exception e) {
            JadeLogger.warn(CAAutoComplete.class, e);
        }

        return options;
    }

    /**
     * Retroune la liste des sections auxiliaire liée à la section principale.
     * 
     * @param session
     * @param like
     * @param idCompteAnnexe
     * @param idSection
     * @return
     */
    public static String getSectionsAux(BSession session, String like, String idSection) {
        if (like == null) {
            like = "";
        }

        String options = "";

        try {
            CASectionManager manager = new CASectionManager();
            manager.setSession(session);
            manager.setForIdSectionPrinc(idSection);
            manager.setLikeIdExterne(like);
            manager.find(BManager.SIZE_USEDEFAULT);

            for (int i = 0; i < manager.size(); i++) {
                CASection section = (CASection) manager.get(i);

                String auxName = CAAutoComplete.getCompteAnnexeAuxNom(session, section);

                options += "<OPTION value=\"";
                options += section.getIdExterne() + "\" selectedIdSectionAux=\"";
                options += section.getIdSection() + "\" selectedIdCompteAnnexeAuxDesc=\"";
                options += auxName + "\" selectedSectionDesc=\"";
                options += section.getDescription() + "\">";
                options += section.getIdExterne() + ", " + section.getTypeSection().getDescription();

                if (!JadeStringUtil.isBlank(auxName)) {
                    options += ", " + auxName;
                }

                options += "</OPTION>";
            }

            if (JadeStringUtil.isBlank(options)) {
                options += "<OPTION value=\"" + like
                        + "\" selectedIdSectionAux=\"\" selectedIdCompteAnnexeAuxDesc=\"\" selectedSectionDesc=\"\">"
                        + like + "</OPTION>";
            }

        } catch (Exception e) {
            JadeLogger.warn(CAAutoComplete.class, e);
        }

        return options;
    }

    /**
     * Le section trouvée est-elle une section de type principale ?
     * 
     * @param session
     * @param idSection
     * @return
     * @throws Exception
     */
    private static boolean isSectionPrincipale(BSession session, String idSection) {
        CASectionManager manager = new CASectionManager();
        manager.setSession(session);
        manager.setForIdSectionPrinc(idSection);
        try {
            manager.find(BManager.SIZE_USEDEFAULT);
        } catch (Exception e) {
            // Do nothing. IDSECTIONPRINCIPALE n'existe pas =>
            // isSectionPrincipale(...) return false
        }

        if ((manager.hasErrors()) || (manager.size() <= 0)) {
            return false;
        } else {
            return true;
        }
    }
}
