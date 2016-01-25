package ch.globaz.vulpecula.util;

import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.osiris.api.APIReferenceRubrique;
import globaz.osiris.api.APIRubrique;
import java.util.List;
import ch.globaz.vulpecula.business.models.comptabilite.RubriqueSearchSimpleModel;
import ch.globaz.vulpecula.business.models.comptabilite.RubriqueSimpleModel;
import ch.globaz.vulpecula.domain.constants.Constants;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.repositoriesjade.RepositoryJade;

/**
 * Classe utilitaire répondant aux besoins de la gestion des rubriques
 * 
 * @since WebBMS 0.01.04
 */
public final class RubriqueUtil {
    private static final String CLASS_NAME = "ch.globaz.vulpecula.util.RubriqueUtil";
    private static final String ERREUR_CONSTANTE_INTROUVABLE = "Impossible de créer l'afact, constante introuvable : ";

    private RubriqueUtil() {
        throw new UnsupportedOperationException();
    }

    public enum Convention {
        BOIS("01"),
        PEINTRE("02"),
        TECHBAT("03"),
        CONSTRMETAL("04"),
        ELECTRICIEN("05");
        // PAYSAGISME("12"),
        // INTERPROFESSIONNELLE("13");

        private String value;

        private Convention(String value) {
            this.value = value;
        }

        public String getValue() {
            return String.valueOf(value);
        }

        public static Convention fromValue(String code) {
            for (Convention e : Convention.values()) {
                if (e.value.equals(code)) {
                    return e;
                }
            }
            throw new IllegalArgumentException("La valeur ne correspond à aucune convention connu : " + code);
        }
    }

    public enum Prestation {
        ABSENCE_JUSTIFIEE("AJ"),
        CONGE_PAYE("CP"),
        SERVICE_MILIATIRE("SM");

        private String value;

        private Prestation(String value) {
            this.value = value;
        }

        public String getValue() {
            return String.valueOf(value);
        }
    }

    public enum Compte {
        PRESTATION("PREST", null, null),
        PARTS_PATRONALES_AVS("PPAVS", TypeAssurance.COTISATION_AVS_AI, null),
        PARTS_PATRONALES_AC("PPAC", TypeAssurance.ASSURANCE_CHOMAGE, null),
        PARTS_PATRONALES_AF("PPAF", TypeAssurance.COTISATION_AF, null),
        PARTS_PATRONALES_FCFP("PPFCFP", TypeAssurance.COTISATION_FFPP_MASSE, null),
        PARTS_PATRONALES_CM("PPCM", TypeAssurance.ASSURANCE_MALADIE, null),
        PARTS_PATRONALES_LPP("PPLPP", TypeAssurance.COTISATION_LPP, null),
        PARTS_PATRONALES_RET("PPRET", TypeAssurance.COTISATION_RETAVAL, null),
        RETENUES_AVS("RETENUESAVS", null, TypeAssurance.COTISATION_AVS_AI),
        RETENUES_AC("RETENUESAC", null, TypeAssurance.ASSURANCE_CHOMAGE),
        RETENUES_AF("RETENUESAF", null, TypeAssurance.COTISATION_AF),
        RETENUES_FCFP("RETENUESFCFP", null, TypeAssurance.COTISATION_FFPP_MASSE),
        RETENUES_CM("RETENUESCM", null, TypeAssurance.ASSURANCE_MALADIE),
        RETENUES_LPP("RETENUESLPP", null, TypeAssurance.COTISATION_LPP),
        RETENUES_RET("RETENUESRET", null, TypeAssurance.COTISATION_RETAVAL);

        private String compte;
        private TypeAssurance typeAssurancePP;
        private TypeAssurance typeAssuranceRetenue;

        private Compte(String value, TypeAssurance typeAssurance, TypeAssurance typeAssuranceRetenue) {
            compte = value;
            typeAssurancePP = typeAssurance;
            this.typeAssuranceRetenue = typeAssuranceRetenue;
        }

        public String getValue() {
            return String.valueOf(compte);
        }

        public TypeAssurance getTypeAssurance() {
            return typeAssurancePP;
        }

        public TypeAssurance getTypeAssuranceRetenue() {
            return typeAssuranceRetenue;
        }
    }

    /**
     * @param prestation
     * @param convention
     * @param compte
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    public static String findReferenceRubriqueFor(Prestation prestation, Convention convention, Compte compte)
            throws IllegalArgumentException {
        if (convention == null) {
            throw new IllegalArgumentException("Convention is null");
        }
        if (prestation == null) {
            throw new IllegalArgumentException("Prestation is null");
        }
        if (compte == null) {
            throw new IllegalArgumentException("Compte is null");
        }

        String nom_constante = "METIER_" + convention.name() + "_" + prestation.getValue() + "_" + compte.getValue();

        String cs_reference_rubrique;
        try {
            cs_reference_rubrique = (String) Constants.class.getDeclaredField(nom_constante).get(String.class);
        } catch (SecurityException e) {
            throw new RubriqueUtilException(CLASS_NAME + " - " + RubriqueUtil.ERREUR_CONSTANTE_INTROUVABLE
                    + nom_constante, e);
        } catch (IllegalAccessException e) {
            throw new RubriqueUtilException(CLASS_NAME + " - " + RubriqueUtil.ERREUR_CONSTANTE_INTROUVABLE
                    + nom_constante, e);
        } catch (NoSuchFieldException e) {
            throw new RubriqueUtilException(CLASS_NAME + " - " + RubriqueUtil.ERREUR_CONSTANTE_INTROUVABLE
                    + nom_constante, e);
        }

        return cs_reference_rubrique;
    }

    /**
     * @param session
     * @param csReferenceRubrique
     * @return
     * @throws Exception
     */
    public static APIRubrique retrieveRubriqueForReference(BISession session, String csReferenceRubrique)
            throws Exception {
        BISession sessionOsiris = PTSession.connectSession(session, "OSIRIS");

        if (session == null) {
            throw new IllegalArgumentException("The session is NULL !");
        }

        if (((BSession) session).getCurrentThreadTransaction().hasErrors()) {
            throw new IllegalStateException("The transaction has errors : "
                    + ((BSession) session).getCurrentThreadTransaction().getErrors().toString());
        }

        APIReferenceRubrique referenceRubrique = (APIReferenceRubrique) sessionOsiris
                .getAPIFor(APIReferenceRubrique.class);

        APIRubrique rubrique = referenceRubrique.getRubriqueByCodeReference(csReferenceRubrique);
        if (rubrique == null || rubrique.isNew()) {
            throw new IllegalStateException("Aucune rubrique paramétrée");
        }
        return rubrique;
    }

    public static String findIdRubriqueForIdExterne(String idExterne) {
        RubriqueSearchSimpleModel searchModel = new RubriqueSearchSimpleModel();
        searchModel.setForIdExterne(idExterne);
        List<RubriqueSimpleModel> rubriques = RepositoryJade.searchForAndFetch(searchModel);
        if (rubriques.size() == 0) {
            throw new IllegalStateException("La rubrique " + idExterne + " n'existe pas.");
        }
        return rubriques.get(0).getIdRubrique();
    }
}
