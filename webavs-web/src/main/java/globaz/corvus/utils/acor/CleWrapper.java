package globaz.corvus.utils.acor;

import ch.globaz.corvus.domaine.DemandeRente;
import ch.globaz.corvus.domaine.DemandeRenteVieillesse;
import ch.globaz.corvus.domaine.constantes.TypeDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;

import java.util.List;

/**
 * Cette classe abstraite est la pour factoriser la génération de la clé de comparaison.
 * La clé de comparaison est identique mais généré sur des données différentes si on traite la base de calcul ou la
 * demande de rente
 *
 * @author lga
 */
public abstract class CleWrapper {

    private TypeDemandeRente typeDemandeRente;
    private String idTiers;
    private boolean ajournement = false;

    public CleWrapper(DemandeRente demandeRente) {
        build(demandeRente);
    }

    public CleWrapper(REBasesCalcul baseCalcul, List<RERenteAccordee> renteAccordees) {
        build(baseCalcul, renteAccordees);
    }

    /**
     * Cette méthode construit les données pour la génération de la clé de comparaison sur la base de la demande de
     * rente
     *
     * @param demandeRente La demande de rente
     */
    private void build(DemandeRente demandeRente) {
        if (demandeRente.getRequerant() == null || demandeRente.getRequerant().getId() == null
                || demandeRente.getRequerant().getId() == 0) {
            throw new NullPointerException(
                    "Unable to found the idTiersRequerant for DemandeRente with id [" + demandeRente.getId() + "]");
        } else {
            idTiers = demandeRente.getRequerant().getId().toString();
        }

        if (demandeRente.getTypeDemandeRente() == null) {
            throw new NullPointerException(
                    "Unable to found the TypeDemandeRente for DemandeRente with id [" + demandeRente.getId() + "]");
        } else {
            typeDemandeRente = demandeRente.getTypeDemandeRente();
        }

        if (demandeRente instanceof DemandeRenteVieillesse) {
            boolean ajournementDemande = ((DemandeRenteVieillesse) demandeRente).isAvecAjournement();
            String dateRevocationAjournement = ((DemandeRenteVieillesse) demandeRente)
                    .getDateRevocationAjournement();
            boolean dateAjournementVide = JadeStringUtil.isBlankOrZero(dateRevocationAjournement);
            ajournement = ajournementDemande && dateAjournementVide;
        }
    }

    /**
     * Cette méthode construit les données pour la génération de la clé de comparaison sur la base d'une base de
     * calcul et de ses rentes accordées
     *
     * @param baseCalcul     La base de calcul en question
     * @param renteAccordees Les rentes accordées liées à la base de calcul
     */
    private void build(REBasesCalcul baseCalcul, List<RERenteAccordee> renteAccordees) {

        // Résolution de l'idTiers pour la génération de la clé de comparaison
        if (JadeStringUtil.isBlankOrZero(baseCalcul.getIdTiersBaseCalcul())) {
            throw new NullPointerException(
                    "Unable to found the idTiersBaseCalcul for REBaseCalcul with id [" + baseCalcul.getId() + "]");
        } else {
            idTiers = baseCalcul.getIdTiersBaseCalcul();
        }

        // Analyse des rentes accordées (la 1ère) de la base de calcul pour déterminer le type de demande de rente
        // correspondant à la badse de calcul
        if (renteAccordees == null || renteAccordees.size() == 0) {
            throw new NullPointerException("Unable to define the TypeDemandeRente for REBaseCalcul with id ["
                    + baseCalcul.getId() + "] because REREnteAccordee list is null or empty");
        } else {
            RERenteAccordee ra = renteAccordees.get(0);
            // La résolution est fait via la classe RECodePrestationResolver mais le type retourné doit être
            // convertis...
            PRTypeCodePrestation type = RECodePrestationResolver.getGenreDePrestation(ra.getCodePrestation());
            switch (type) {
                case INVALIDITE:
                    typeDemandeRente = TypeDemandeRente.DEMANDE_INVALIDITE;
                    break;
                case VIEILLESSE:
                    typeDemandeRente = TypeDemandeRente.DEMANDE_VIEILLESSE;
                    break;
                case SURVIVANT:
                    typeDemandeRente = TypeDemandeRente.DEMANDE_SURVIVANT;
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unable to define the RETypeDemandeRente of the REBasesCalcul with id ["
                                    + baseCalcul.getId() + "]. The first RERenteAccordee with id ["
                                    + ra.getIdPrestationAccordee() + "] has an unknow codePrestation ["
                                    + ra.getCodePrestation() + "]");
            }
        }

        // Analyse de la'journement de la base de calcul
        ajournement = hasCodeCasSpecial08(renteAccordees);

    }

    public boolean hasCodeCasSpecial08(List<RERenteAccordee> renteAccordees) {
        for (RERenteAccordee ra : renteAccordees) {
            if (hasCodeCasSpecial(ra, "08")) {
                return true;
            }
        }
        return false;
    }

    public boolean isAjournement() {
        return ajournement;
    }

    public TypeDemandeRente getTypeDemandeRente() {
        return typeDemandeRente;
    }

    public String getCleDeComparaison() {
        StringBuilder sb = new StringBuilder();
        sb.append(typeDemandeRente.toString());
        sb.append("-");
        if (!TypeDemandeRente.DEMANDE_SURVIVANT.equals(typeDemandeRente)) {
            sb.append(idTiers);
            sb.append("-");
        }
        sb.append(ajournement);
        return sb.toString();
    }

    protected boolean hasCodeCasSpecial(RERenteAccordee renteAccordee, String codeCasSpecialRecherche) {
        return codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux1())
                || codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux2())
                || codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux3())
                || codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux4())
                || codeCasSpecialRecherche.equals(renteAccordee.getCodeCasSpeciaux5());
    }
}
