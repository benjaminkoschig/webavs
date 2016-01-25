package ch.globaz.al.businessimpl.rafam;

import ch.globaz.al.business.constantes.enumerations.RafamEtatAnnonce;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType;
import ch.globaz.al.business.exceptions.model.annonces.rafam.ALAnnonceRafamException;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;

/**
 * Context RAFam utilis� pour la gestion des annonces RAFam.
 * 
 * @author jts
 */
public class ContextAnnonceRafam {

    /**
     * Retourne une instance de <code>ContextAnnonceRafam</code>
     * 
     * @param dossier
     *            Dossier en cours de traitement
     * @param droit
     *            Droit en cours de traitement. Doit �tre li� au dossier
     * 
     * @return nouvelle instance du contexte
     * 
     * @throws ALAnnonceRafamException
     *             Exception lev�e si l'un des param�tres n'est pas valide.
     */
    public static ContextAnnonceRafam getContext(RafamEvDeclencheur evDecl, RafamEtatAnnonce etat,
            DossierComplexModel dossier, DroitComplexModel droit, RafamFamilyAllowanceType type)
            throws ALAnnonceRafamException {

        if ((dossier == null) || dossier.isNew()) {
            throw new ALAnnonceRafamException("ContextAnnonceRafam#getContext : dossier is null or new");
        }

        if ((droit == null) || droit.isNew()) {
            throw new ALAnnonceRafamException("ContextAnnonceRafam#getContext : droit is null or new");
        }

        if (!droit.getDroitModel().getIdDossier().equals(dossier.getId())) {
            throw new ALAnnonceRafamException("ContextAnnonceRafam#getContext : idDroit doesn't match with dossier");
        }

        ContextAnnonceRafam context = new ContextAnnonceRafam();
        context.droit = droit;
        context.dossier = dossier;
        context.evDecl = evDecl;
        context.type = type;
        context.etat = etat;

        return context;
    }

    /** Dossier � traiter */
    private DossierComplexModel dossier = null;

    /** Droit � traiter */
    private DroitComplexModel droit = null;
    /**
     * etat forc� de l'annonce lors de la cr�ation
     */
    private RafamEtatAnnonce etat = null;
    /** Ev�nement d�clencheur */
    private RafamEvDeclencheur evDecl = null;
    /** Type d'annonce */
    private RafamFamilyAllowanceType type = null;

    public DossierComplexModel getDossier() {
        return dossier;
    }

    public DroitComplexModel getDroit() {
        return droit;
    }

    public RafamEtatAnnonce getEtat() {
        return etat;
    }

    public RafamEvDeclencheur getEvenementDeclencheur() {
        return evDecl;
    }

    public RafamFamilyAllowanceType getType() {
        return type;
    }

    public void setEtat(RafamEtatAnnonce etat) {
        this.etat = etat;
    }

    public void setEvDecl(RafamEvDeclencheur evDecl) {
        this.evDecl = evDecl;
    }

    public void setType(RafamFamilyAllowanceType type) {
        this.type = type;
    }
}
