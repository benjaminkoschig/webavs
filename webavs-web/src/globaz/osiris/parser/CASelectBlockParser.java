package globaz.osiris.parser;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.APISection;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CAReferenceRubrique;
import globaz.osiris.db.comptes.CAReferenceRubriqueManager;
import globaz.osiris.translation.CACodeSystem;
import java.util.ArrayList;

/**
 * @author dda
 */
public class CASelectBlockParser {
    public static final String FX_PREFIX_CATEGORIE = "osiris.comptes.categories.";
    public static final String LABEL_COMPTE_ANNEXE_CATEGORIE_STANDARD = "COMPTE_ANNEXE_CATEGORIE_STANDARD";
    public static final String LABEL_COMPTE_AUXILIAIRE_STANDARD = "COMPTE_AUXILIAIRE_STANDARD";
    public static final String LABEL_MODE_COMPENSATION_STANDARD = "MODE_COMPENSATION_STANDARD";
    public static final String LABEL_TOUS = "TOUS";

    private static final String LABEL_TRI_SPECIAL_AUCUN = "TRI_SPECIAL_AUCUN";

    /**
     * equivalent a getForIdCategorieSelectBlock(session, false)
     */
    public static String getForIdCategorieSelectBlock(BSession session) {
        return CASelectBlockParser.getForIdCategorieSelectBlock(session, false);
    }

    /**
     * Construit le bloc select pour la gestion des comptes auxiliaires (standard, auxiliaire etc.). S'il n'éxiste que
     * des comptes standards la méthode retourne un String vide.
     * 
     * @param session
     * @param filter
     *            mettre vrai pour filtrer les resultats en fonction des droits de l'utilisateur
     * @return
     */
    public static String getForIdCategorieSelectBlock(BSession session, boolean filter) {
        StringBuffer forIdCategorieSelectBloc = new StringBuffer();

        try {
            FWParametersSystemCodeManager manager = CACodeSystem.getCategories(session);

            if (manager.size() > 1) {
                StringBuffer tous = new StringBuffer();

                forIdCategorieSelectBloc.append("<OPTION value=\"0\">");
                forIdCategorieSelectBloc.append(session
                        .getLabel(CASelectBlockParser.LABEL_COMPTE_ANNEXE_CATEGORIE_STANDARD));
                forIdCategorieSelectBloc.append("</OPTION>");

                if (filter) {
                    tous.append("0");
                } else {
                    tous.append(CACompteAnnexeManager.ALL_CATEGORIE);
                }

                for (int i = 0; i < manager.size(); i++) {
                    FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                    if (!JadeStringUtil.isBlank(code.getLibelle())) {
                        // ne pas ajouter l'option si l'utilisateur n'a pas le
                        // droit de lecture
                        if (filter
                                && !session.hasRight(CASelectBlockParser.FX_PREFIX_CATEGORIE + code.getIdCode(),
                                        FWSecureConstants.READ)) {
                            continue;
                        }

                        if (filter) {
                            if (tous.length() > 0) {
                                tous.append(',');
                            }

                            tous.append(code.getIdCode());
                        }

                        forIdCategorieSelectBloc.append("<OPTION value=\"");
                        forIdCategorieSelectBloc.append(code.getIdCode());
                        forIdCategorieSelectBloc.append("\">");
                        forIdCategorieSelectBloc.append(code.getCurrentCodeUtilisateur().getLibelle());
                        forIdCategorieSelectBloc.append("</OPTION>");
                    }
                }

                forIdCategorieSelectBloc.append("<OPTION selected value=\"");
                forIdCategorieSelectBloc.append(tous.length() != 0 ? tous.toString() : "-1");
                forIdCategorieSelectBloc.append("\">");
                forIdCategorieSelectBloc.append(session.getLabel(CASelectBlockParser.LABEL_TOUS));
                forIdCategorieSelectBloc.append("</OPTION>");
            }
        } catch (Exception e) {
            JadeLogger.warn(CASelectBlockParser.class, e);
        }

        forIdCategorieSelectBloc.insert(0, "<SELECT id=\"forIdCategorie\" name=\"forIdCategorie\">");
        forIdCategorieSelectBloc.append("</SELECT>");

        return forIdCategorieSelectBloc.toString();
    }

    /**
     * Construit le bloc select pour le code de référence de la rubrique
     * 
     * @param session
     * @return String forIdCodeReference
     */
    public static String getForIdCodeReferenceSelectBlock(BSession session, String idCodeReference) {
        String forIdCodeReferenceSelectBloc = new String();

        try {
            // Manager des codes systèmes
            FWParametersSystemCodeManager manager = CACodeSystem.getCodeReference(session);

            // Manager des codes de références
            CAReferenceRubriqueManager refManager = new CAReferenceRubriqueManager();
            refManager.setSession(session);
            refManager.changeManagerSize(0);
            refManager.find();

            // Parcourir le manager des références pour voir les codes de
            // références déjà utilisés
            ArrayList codeUtilise = new ArrayList();
            for (int i = 0; i < refManager.size(); i++) {
                CAReferenceRubrique ref = (CAReferenceRubrique) refManager.getEntity(i);
                codeUtilise.add(ref.getIdCodeReference());
            }

            if (manager.size() > 1) {
                forIdCodeReferenceSelectBloc += "<SELECT id=\"idCodeReference\" name=\"idCodeReference\">";
                if (JadeStringUtil.isBlank(idCodeReference)) {
                    forIdCodeReferenceSelectBloc += "<OPTION selected value=\"0\"></OPTION>";
                }

                for (int i = 0; i < manager.size(); i++) {
                    FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                    if (!codeUtilise.contains(code.getIdCode()) || idCodeReference.equalsIgnoreCase(code.getIdCode())) {
                        if (!JadeStringUtil.isBlank(code.getLibelle())) {
                            if (idCodeReference.equalsIgnoreCase(code.getIdCode())) {
                                forIdCodeReferenceSelectBloc += "<OPTION selected value=\"";
                            } else {
                                forIdCodeReferenceSelectBloc += "<OPTION value=\"";
                            }
                            forIdCodeReferenceSelectBloc += code.getIdCode();
                            forIdCodeReferenceSelectBloc += "\">";
                            forIdCodeReferenceSelectBloc += code.getCurrentCodeUtilisateur().getLibelle();
                            forIdCodeReferenceSelectBloc += "</OPTION>";
                        }
                    }
                }

                forIdCodeReferenceSelectBloc += "</SELECT>";
            }
        } catch (Exception e) {
            JadeLogger.warn(CASelectBlockParser.class, e);
        }

        return forIdCodeReferenceSelectBloc;
    }

    /**
     * Construit le bloc select pour la gestion des comptes auxiliaires (standard, auxiliaire etc.). S'il n'éxiste que
     * des comptes standards la méthode retourne un String vide.
     * 
     * @param session
     * @return
     */
    public static String getForIdGenreSelectBlock(BSession session) {
        String forIdGenreSelectBloc = new String();

        try {
            FWParametersSystemCodeManager manager = CACodeSystem.getGenreComptes(session);

            if (manager.size() > 1) {
                forIdGenreSelectBloc += "<SELECT id=\"forIdGenreCompte\" name=\"forIdGenreCompte\">";

                forIdGenreSelectBloc += "<OPTION selected value=\"" + CACompteAnnexe.GENRE_COMPTE_STANDARD + "\">"
                        + session.getLabel(CASelectBlockParser.LABEL_COMPTE_AUXILIAIRE_STANDARD) + "</OPTION>";

                for (int i = 0; i < manager.size(); i++) {
                    FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                    if (!JadeStringUtil.isBlank(code.getLibelle())) {
                        forIdGenreSelectBloc += "<OPTION value=\"";
                        forIdGenreSelectBloc += code.getIdCode();
                        forIdGenreSelectBloc += "\">";
                        forIdGenreSelectBloc += code.getCurrentCodeUtilisateur().getLibelle();
                        forIdGenreSelectBloc += "</OPTION>";
                    }
                }

                forIdGenreSelectBloc += "</SELECT>";
            }
        } catch (Exception e) {
            JadeLogger.warn(CASelectBlockParser.class, e);
        }

        return forIdGenreSelectBloc;
    }

    /**
     * Construit le bloc select pour la gestion des modes de compensation de la section (standard, bloquer compensation
     * etc.).
     * 
     * @param session
     * @return
     */
    public static String getForIdModeCompensationSelectBlock(BSession session, String idModeCompensation) {
        String forModeCompensationNotEqualsBloc = new String();

        try {
            FWParametersSystemCodeManager manager = CACodeSystem.getModeCompensation(session);
            if (manager.size() > 1) {
                forModeCompensationNotEqualsBloc += "<SELECT id=\"idModeCompensation\" name=\"idModeCompensation\">";
                if (JadeStringUtil.isBlankOrZero(idModeCompensation)) {
                    forModeCompensationNotEqualsBloc += "<OPTION selected value=\""
                            + APISection.MODE_COMPENSATION_STANDARD + "\">"
                            + session.getLabel(CASelectBlockParser.LABEL_MODE_COMPENSATION_STANDARD) + "</OPTION>";
                } else {
                    forModeCompensationNotEqualsBloc += "<OPTION value=\"" + APISection.MODE_COMPENSATION_STANDARD
                            + "\">" + session.getLabel(CASelectBlockParser.LABEL_MODE_COMPENSATION_STANDARD)
                            + "</OPTION>";
                }

                for (int i = 0; i < manager.size(); i++) {
                    FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                    if (!JadeStringUtil.isBlank(code.getLibelle())) {
                        if (idModeCompensation.equals(code.getIdCode())) {
                            forModeCompensationNotEqualsBloc += "<OPTION  selected value=\"";
                        } else {
                            forModeCompensationNotEqualsBloc += "<OPTION value=\"";
                        }
                        forModeCompensationNotEqualsBloc += code.getIdCode();
                        forModeCompensationNotEqualsBloc += "\">";
                        forModeCompensationNotEqualsBloc += code.getCurrentCodeUtilisateur().getLibelle();
                        forModeCompensationNotEqualsBloc += "</OPTION>";
                    }
                }
                forModeCompensationNotEqualsBloc += "</SELECT>";
            }
        } catch (Exception e) {
            JadeLogger.warn(CASelectBlockParser.class, e);
        }

        return forModeCompensationNotEqualsBloc;
    }

    /**
     * Construit le bloc select pour la gestion du tri spécial (CS tri sur CACPTAP.AAITRN). S'il n'éxiste que des
     * comptes standards la méthode retourne un String vide.
     * 
     * @param session
     * @return
     */
    public static String getForTriSpecialSelectBlock(BSession session) {
        String forTriSpecialSelectBlock = new String();

        try {
            FWParametersSystemCodeManager manager = CACodeSystem.getTriSpecial(session);

            if (manager.size() > 1) {
                forTriSpecialSelectBlock += "<SELECT id=\"forTriSpecial\" name=\"forTriSpecial\">";

                forTriSpecialSelectBlock += "<OPTION selected value=\"0\">"
                        + session.getLabel(CASelectBlockParser.LABEL_TRI_SPECIAL_AUCUN) + "</OPTION>";

                for (int i = 0; i < manager.size(); i++) {
                    FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                    if (!JadeStringUtil.isBlank(code.getLibelle())) {
                        forTriSpecialSelectBlock += "<OPTION value=\"";
                        forTriSpecialSelectBlock += code.getIdCode();
                        forTriSpecialSelectBlock += "\">";
                        forTriSpecialSelectBlock += code.getCurrentCodeUtilisateur().getLibelle();
                        forTriSpecialSelectBlock += "</OPTION>";
                    }
                }

                forTriSpecialSelectBlock += "</SELECT>";
            }
        } catch (Exception e) {
            JadeLogger.warn(CASelectBlockParser.class, e);
        }

        return forTriSpecialSelectBlock;
    }
}