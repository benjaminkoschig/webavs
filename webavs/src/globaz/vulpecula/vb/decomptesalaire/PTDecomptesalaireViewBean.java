package globaz.vulpecula.vb.decomptesalaire;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import ch.globaz.common.vb.DomainPersistentObjectViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.businessimpl.services.decompte.DecompteServiceImpl;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.web.application.PTActions;
import ch.globaz.vulpecula.web.views.decomptesalaire.DecompteSalaireViewService;
import ch.globaz.vulpecula.web.views.postetravail.PosteTravailViewService;
import ch.globaz.vulpecula.web.views.postetravail.TravailleurViewService;
import com.google.common.base.Preconditions;

/**
 * ViewBean permettant la gestion d'une ligne de décompte salaire
 * 
 * @since Web@BMS 0.01.02
 */
public class PTDecomptesalaireViewBean extends DomainPersistentObjectViewBean<DecompteSalaire> {

    private DecompteSalaire decompteSalaire;
    private String idDecompte = "";
    private String idPosteTravail = "";

    public PTDecomptesalaireViewBean() {
        decompteSalaire = new DecompteSalaire();
    }

    @Override
    public void add() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() throws Exception {
        throw new UnsupportedOperationException();
    }

    public String getAction() {
        return PTActions.DECOMPTE_SALAIRE_AJAX;
    }

    /**
     * @return a Decompte
     */
    public Decompte getDecompte() {
        if (decompteSalaire.getDecompte() == null && idDecompte != null) {
            Decompte decompte = new Decompte();
            decompte.setId(idDecompte);
            decompteSalaire.setDecompte(decompte);
        }
        if (decompteSalaire.getDecompte() != null && JadeStringUtil.isBlank(decompteSalaire.getDecompte().getSpy())) {
            Decompte decompte = VulpeculaRepositoryLocator.getDecompteRepository().findById(
                    decompteSalaire.getDecompte().getId());
            decompteSalaire.setDecompte(decompte);
        }

        return decompteSalaire.getDecompte();
    }

    /**
     * @return type de décompte
     */
    public String getTypeDecompte() {
        if (getDecompte().getType() != null) {
            return getDecompte().getType().getValue();
        } else {
            return "";
        }
    }

    /**
     * @return la designation du poste de travail
     */
    public String getDesignationPosteTravail() {
        if (getPosteTravail() != null) {
            return getPosteTravail().getDescriptionTravailleur();
        } else {
            return "";
        }
    }

    /**
     * @return an Employeur
     */
    public Employeur getEmployeur() {
        if (getDecompte() != null) {
            return getDecompte().getEmployeur();
        } else {
            return null;
        }
    }

    @Override
    public DecompteSalaire getEntity() {
        return decompteSalaire;
    }

    public String getIdDecompte() {
        if (decompteSalaire.getIdDecompte() == null) {
            return idDecompte;
        }
        return decompteSalaire.getIdDecompte();
    }

    /**
     * @return le poste de travail de la ligne de decompte
     */
    private PosteTravail getPosteTravail() {
        if (decompteSalaire.getPosteTravail() != null
                && JadeStringUtil.isBlank(decompteSalaire.getPosteTravail().getSpy())) {
            PosteTravail posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(
                    decompteSalaire.getPosteTravail().getId());
            decompteSalaire.setPosteTravail(posteTravail);
        }
        return decompteSalaire.getPosteTravail();
    }

    public String getSequence() {
        if (decompteSalaire.getSequence() != null) {
            return decompteSalaire.getSequence();
        } else {
            return "0"; // TODO afficher le total des lignes +1
        }
    }

    @Override
    public void retrieve() throws Exception {
        Preconditions.checkArgument(!JadeNumericUtil.isEmptyOrZero(decompteSalaire.getId()));

        decompteSalaire = VulpeculaRepositoryLocator.getDecompteSalaireRepository().findByIdWithoutDependencies(
                decompteSalaire.getId());
        idPosteTravail = decompteSalaire.getPosteTravail().getId();
    }

    /**
     * @param idDecompte
     *            the idDecompte to set
     */
    public void setIdDecompte(final String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public String getDecompteService() {
        return DecompteServiceImpl.class.getName();
    }

    public String getDecompteSalaireViewService() {
        return DecompteSalaireViewService.class.getName();
    }

    /**
     * Retourne true si le décompte salaire peut être édité.
     * 
     * @return true si {@link EtatDecompte#OUVERT} ou {@link EtatDecompte#GENERE}
     */
    public boolean isEditable() {
        if (decompteSalaire == null || decompteSalaire.getDecompte() == null) {
            return false;
        }
        return decompteSalaire.getDecompte().isEditable();
    }

    /**
     * Retourne le service fournissant des informations à la vue quant aux postes de travail.
     * 
     * @return Nom de la classe de service
     */
    public String getPosteTravailViewService() {
        return PosteTravailViewService.class.getName();
    }

    /**
     * Retourne le service fournissant des informations à la vue quant aux travailleurs.
     * 
     * @return Nom de la classe de service
     */
    public String getTravailleurViewService() {
        return TravailleurViewService.class.getName();
    }

    public String getCsMensuel() {
        return TypeSalaire.MOIS.getValue();
    }

    public boolean isNouveau() {
        return JadeStringUtil.isEmpty(decompteSalaire.getId());
    }

    public String getPeriodeDebut() {
        return decompteSalaire.getDecompte().getPeriode().getPeriodeDebut().getSwissValue();
    }

    /**
     * Retourne la période de début pour un nouveau décompte.
     * Si c'est un décompte complémentaire, on ne saisit pas la période automatiquement.
     * 
     * @return la date de la période du décompte ou vide si complémentaire
     */
    public String getPeriodeDebutForNewDecompte() {
        Decompte decompte = getDecompte();
        if (decompte.isControleEmployeur()) {
            return "";
        } else {
            return getDecompte().getPeriode().getPeriodeDebut().getSwissValue();
        }
    }

    /**
     * Retourne la période de début pour un nouveau décompte.
     * Si c'est un décompte complémentaire, on ne saisit pas la période automatiquement.
     * 
     * @return la date de la période du décompte ou vide si complémentaire
     */
    public String getPeriodeFinForNewDecompte() {
        Decompte decompte = getDecompte();
        if (decompte.isControleEmployeur()) {
            return "";
        } else {
            if (decompte.getPeriode().isMemeAnnee()) {
                return decompte.getPeriodeFin().getSwissValue();
            } else {
                return decompte.getAnnee().getLastDayOfYear().getSwissValue();
            }
        }
    }

    public String getMessageDialogCotisation() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_MESSAGE_DIALOG_COTISATION");
    }

    public String getMessageDroitsAF() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_MESSAGE_DROITS_AF");
    }

    public String getTypePeriodique() {
        return TypeDecompte.PERIODIQUE.getValue();
    }
}