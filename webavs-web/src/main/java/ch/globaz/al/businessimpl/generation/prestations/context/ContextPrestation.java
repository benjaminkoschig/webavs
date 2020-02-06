package ch.globaz.al.businessimpl.generation.prestations.context;

import ch.globaz.al.properties.ALProperties;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import java.math.BigDecimal;
import java.util.ArrayList;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationPrestationsContextException;
import ch.globaz.al.business.models.droit.EnfantModel;
import ch.globaz.al.business.models.prestation.DetailPrestationModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.business.services.tucana.TucanaBusinessService;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * Contexte de prestation utilisé pour la génération de prestations. Il contient une en-tête de prestation et tous les
 * détails qui y sont liés
 * 
 * @author jts
 */
public class ContextPrestation {

    /**
     * Retourne une instance de <code>ContextPrestation</code>
     * 
     * @param contextDossier
     *            Contexte du dossier
     * @param bonification
     *            type de bonification {@link ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI}
     * @param unite
     *            Unité de calcul {@link ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL}
     * 
     * @return Instance de <code>ContextPrestation</code>
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    @SuppressWarnings("rawtypes")
    public static ContextPrestation getContextPrestation(ContextDossier contextDossier, String bonification,
            String unite) throws JadeApplicationException {

        if (contextDossier == null) {
            throw new ALGenerationPrestationsContextException(
                    "ContextPrestation#getContextPrestation : contextDossier is null");
        }

        if (!ALCSPrestation.BONI_DIRECT.equals(bonification) && !ALCSPrestation.BONI_INDIRECT.equals(bonification)
                && !ALCSPrestation.BONI_RESTITUTION.equals(bonification)) {
            throw new ALGenerationPrestationsContextException("ContextPrestation#getContextPrestation : "
                    + bonification + " is not a valid bonification type");
        }

        if (!ALCSDossier.UNITE_CALCUL_HEURE.equals(unite) && !ALCSDossier.UNITE_CALCUL_JOUR.equals(unite)
                && !ALCSDossier.UNITE_CALCUL_MOIS.equals(unite) && !ALCSDossier.UNITE_CALCUL_SPECIAL.equals(unite)) {
            throw new ALGenerationPrestationsContextException("ContextPrestation#getContextPrestation : " + unite
                    + " is not a valid unité");
        }

        ContextPrestation context = new ContextPrestation();
        context.detailsPrest = new ArrayList<ArrayList>();
        context.countedDroits = new ArrayList<String>();
        context.entete = null;
        context.contextDossier = contextDossier;
        context.bonification = bonification;
        context.unite = unite;
        return context;
    }

    /**
     * Type de bonification
     * 
     * @see ch.globaz.al.business.constantes.ALCSPrestation#GROUP_BONI
     */
    private String bonification = null;
    /**
     * Contexte de dossier
     * 
     * @see ContextDossier
     */
    private ContextDossier contextDossier = null;

    /**
     * Liste contenant les id des droits qui ont déjà eu une prestation pendant le processus. Cela évite de les compter
     * plusieurs fois si la génération se fait sur plusieurs mois
     */
    private ArrayList<String> countedDroits = null;

    /**
     * Liste des détails de prestation liés à l'en-tête
     * 
     * @see ContextPrestation#entete
     * @see ch.globaz.al.business.models.prestation.DetailPrestationModel
     */
    @SuppressWarnings("rawtypes")
    private ArrayList<ArrayList> detailsPrest = null;
    /**
     * En-tête de prestations
     * 
     * @see ch.globaz.al.business.models.prestation.EntetePrestationModel
     */
    private EntetePrestationModel entete = null;

    /**
     * Unité de calcul
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_UNITE_CALCUL
     */
    private String unite;

    /**
     * Constructeur privé. Utiliser la méthode <code>getContextPrestation</code> pour obtenir une instance
     * 
     * @see ContextPrestation#getContextPrestation(ContextDossier, String, String)
     */
    private ContextPrestation() {
        // Utiliser la méthode getContextPrestation
    }

    /**
     * Ajoute <code>detail</code> à la liste des détails de prestation et met à jour l'en-tête
     * 
     * @param detail
     *            Le détail de prestation à ajouter
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    @SuppressWarnings("unchecked")
    public void addDetail(DetailPrestationModel detail, EnfantModel enfant) throws JadeApplicationException,
            JadePersistenceException {

        if (detail == null) {
            throw new ALGenerationPrestationsContextException("ContextPrestation#addDetail : detail is null");
        }

        initEntete();

        detail.setNumeroCompte(ALImplServiceLocator.getRubriqueService()
                .getRubriqueComptable(contextDossier.getDossier().getDossierModel(), entete, detail,
                        "01." + contextDossier.getDebutPeriode()));

        if(ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue() && !JadeStringUtil.isBlankOrZero(detail.getMontantIS())) {
            String idExterneIS = ALImplServiceLocator.getRubriqueService().getRubriqueForIS(contextDossier.getDossier().getDossierModel(), detail, "01." + contextDossier.getDebutPeriode());
            detail.setNumeroCompteIS(idExterneIS);
            if(JadeStringUtil.isBlank(entete.getMontantTotalIS())) {
                entete.setMontantTotalIS("0");
            }
            entete.setMontantTotalIS((new BigDecimal(entete.getMontantTotalIS()).add(new BigDecimal(detail.getMontantIS())))
                    .toPlainString());
        }

        @SuppressWarnings("rawtypes")
        ArrayList list = new ArrayList(2);
        list.add(detail);
        list.add(getTucanaData(detail, enfant));
        detailsPrest.add(list);

        /*
         * mise à jour de l'en-tête
         */
        entete.setMontantTotal((new BigDecimal(entete.getMontantTotal()).add(new BigDecimal(detail.getMontant())))
                .toPlainString());

        if (!countedDroits.contains(detail.getIdDroit())
                && (ALCSDroit.TYPE_ENF.equals(detail.getTypePrestation()) || ALCSDroit.TYPE_FORM.equals(detail
                        .getTypePrestation()))) {
            entete.setNombreEnfants(String.valueOf(Integer.parseInt(entete.getNombreEnfants()) + 1));
            countedDroits.add(detail.getIdDroit());
        }

        String currentPeriode = contextDossier.getCurrentPeriode() != null ? contextDossier.getCurrentPeriode()
                : detail.getPeriodeValidite();

        if (JadeStringUtil.isEmpty(currentPeriode)) {
            // ne doit pas arriver au moins l'une des deux périodes doit être
            // renseigné
            throw new ALGenerationPrestationsContextException(
                    "ContextPrestation#addDetail : unable to get current period");
        }

        if (JadeStringUtil.isEmpty(detail.getPeriodeValidite())) {
            detail.setPeriodeValidite(currentPeriode);
        }

        if (entete.getPeriodeDe() == null) {
            entete.setPeriodeDe(currentPeriode);
        }

        if (JadeStringUtil.isEmpty(entete.getPeriodeA()) || !currentPeriode.equals(entete.getPeriodeA())) {
            entete.setPeriodeA(currentPeriode);
            entete.setNombreUnite(String.valueOf(Integer.parseInt(entete.getNombreUnite()) + 1));
        }
    }

    /**
     * Ajoute <code>detail</code> à la liste des détails de prestation et met à jour l'en-tête.
     * 
     * L'indication de versement de l'allocation de naissance est mise à jour au niveau de l'enfant
     * 
     * @param detail
     *            Le détail de prestation à ajouter
     * @param enfant
     *            Enfant auquel est lié le détail de prestation
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    @SuppressWarnings("unchecked")
    public void addDetailNaissance(DetailPrestationModel detail, EnfantModel enfant) throws JadeApplicationException,
            JadePersistenceException {

        if (detail == null) {
            throw new ALGenerationPrestationsContextException("ContextPrestation#addDetailNaissance : detail is null");
        }

        if (enfant == null) {
            throw new ALGenerationPrestationsContextException("ContextPrestation#addDetailNaissance : enfant is null");
        }

        enfant.setAllocationNaissanceVersee(Boolean.TRUE);
        ALImplServiceLocator.getEnfantModelService().update(enfant);

        addDetail(detail, enfant);
    }

    /**
     * Initialise un transfert Tucana qui sera lié au détail de prestation passé en paramètre
     * 
     * @param detail
     *            Detail de prestation pour lequel le transfert doit être généré
     * @return Le transfert initialisé, <code>null</code> si Tucana n'est pas actif
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private TransfertTucanaModel getTucanaData(DetailPrestationModel detail, EnfantModel enfant)
            throws JadeApplicationException, JadePersistenceException {

        if (ContextTucana.tucanaIsEnabled()) {

            TucanaBusinessService s = ALServiceLocator.getTucanaBusinessService();

            TransfertTucanaModel transfert = new TransfertTucanaModel();
            transfert.setRubriqueAllocation(s.getRubriqueAllocation(detail.getTypePrestation(),
                    detail.getCategorieTarif(), contextDossier.getDossier().getAllocataireComplexModel()
                            .getAllocataireModel(), enfant));

            ArrayList<String> rubr = s.getRubriqueSupplements(detail.getTypePrestation(), detail.getCategorieTarif(),
                    contextDossier.getDossier().getAllocataireComplexModel().getAllocataireModel(), enfant);

            transfert.setRubriqueSupplementLegal(rubr.get(0));
            transfert.setRubriqueSupplementConventionnel(rubr.get(1));

            return transfert;
        } else {
            return null;
        }
    }

    /**
     * Vérifie si le contexte contient des prestations
     * 
     * @return <code>true</code> si des prestations sont présentes, <code>false</code> sinon
     */
    public boolean hasPrestations() {
        return (detailsPrest.size() > 0);
    }

    /**
     * Initialise l'en-tête de prestation
     * 
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    private void initEntete() throws JadeApplicationException, JadePersistenceException {

        if (entete == null) {
            entete = new EntetePrestationModel();

            entete.setBonification(bonification);
            entete.setIdDossier(contextDossier.getDossier().getId());
            entete.setIdRecap(contextDossier.getContextAffilie().getIdRecap(entete));

            if (ALCSDossier.STATUT_IS.equals(contextDossier.getDossier().getDossierModel().getStatut())) {
                entete.setStatut(ALCSPrestation.STATUT_ADI);
            } else if (ALCSDossier.STATUT_CS.equals(contextDossier.getDossier().getDossierModel().getStatut())) {
                entete.setStatut(ALCSPrestation.STATUT_ADC);
            } else {
                entete.setStatut(ALCSPrestation.STATUT_CH);
            }

            // TODO (lot 2) tiers de la caisse AF
            entete.setIdTiersCaisseAF("0");
            if (ALConstPrestations.TypeGeneration.ADI_TEMPORAIRE.equals(contextDossier.getTypeGenPrestation())) {
                entete.setEtatPrestation(ALCSPrestation.ETAT_TMP);
            } else {
                entete.setEtatPrestation(ALCSPrestation.ETAT_SA);
            }
            entete.setUnite(unite);
            entete.setNombreUnite("0");
            entete.setNombreEnfants("0");
            entete.setMontantTotal("0");
            entete.setTypeGeneration(contextDossier.getContextAffilie().getTypeGeneration());
            entete.setTauxVersement(contextDossier.getDossier().getDossierModel().getTauxVersement());
            entete.setCantonAffilie(ALImplServiceLocator.getAffiliationService().convertCantonNaos2CantonAF(
                    contextDossier.getAssuranceInfo().getCanton()));
            entete.setNumPsgGeneration(contextDossier.getContextAffilie().getNumGeneration());
            entete.setIdCotisation(contextDossier.getAssuranceInfo().getIdCotisation());
        }
    }

    /**
     * Libère le contexte
     */
    @SuppressWarnings("rawtypes")
    private void release() {
        detailsPrest = new ArrayList<ArrayList>();
        entete = null;
    }

    /**
     * Enregistre le contenu du contexte en persistance puis le libère. Si aucun détail de prestation ne se trouve dans
     * la liste, l'en-tête n'est pas enregistrée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void save() throws JadeApplicationException, JadePersistenceException {

        if (hasPrestations()) {

            if (ALCSDossier.UNITE_CALCUL_HEURE.equals(unite) || ALCSDossier.UNITE_CALCUL_JOUR.equals(unite)) {
                // Si le montant total est à 0, alors on force le nombre de d'unité à 0
                if (JadeStringUtil.isBlankOrZero(entete.getMontantTotal())) {
                    entete.setNombreUnite("0");
                } else {
                    entete.setNombreUnite(JadeStringUtil.isEmpty(contextDossier.getCurrentNbJourDebutOuFin()) ? contextDossier
                            .getNbUnites() : contextDossier.getCurrentNbJourDebutOuFin());
                }
            }

            entete = ALImplServiceLocator.getEntetePrestationModelService().create(entete);

            String[] params = { contextDossier.getDossier().getId() };

            ArrayList<String> csvVal = new ArrayList<String>();
            csvVal.add(contextDossier.getDossier().getId());
            csvVal.add(contextDossier.getContextAffilie().getNumAffilie());

            // ajout info génération
            contextDossier
                    .getContextAffilie()
                    .getLogger()
                    .getInfosLogger(
                            contextDossier.getDossier().getId() + " - "
                                    + contextDossier.getContextAffilie().getNumAffilie(),
                            contextDossier.getDossier().getAllocataireComplexModel().getPersonneEtendueComplexModel()
                                    .getTiers().getDesignation1()
                                    + " "
                                    + contextDossier.getDossier().getAllocataireComplexModel()
                                            .getPersonneEtendueComplexModel().getTiers().getDesignation2(), csvVal)
                    .addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.INFO, ContextPrestation.class.getName(),
                                    "al.protocoles.generation.diagnostique.dossier.generee", params));

            // on affiche tous les avertissements liés à l'affiliation AF
            if (!contextDossier.getAssuranceInfo().getWarningsContainer().isEmpty()) {
                for (String warn : contextDossier.getAssuranceInfo().getWarningsContainer()) {
                    contextDossier
                            .getContextAffilie()
                            .getLogger()
                            .getWarningsLogger(
                                    contextDossier.getDossier().getId() + " - "
                                            + contextDossier.getContextAffilie().getNumAffilie(),
                                    contextDossier.getDossier().getAllocataireComplexModel()
                                            .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                                            + " "
                                            + contextDossier.getDossier().getAllocataireComplexModel()
                                                    .getPersonneEtendueComplexModel().getTiers().getDesignation2(),
                                    csvVal)
                            .addMessage(
                                    new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, ContextPrestation.class
                                            .getName(), warn, params));
                }
            } else if (!contextDossier.getAssuranceInfo().getCouvert().booleanValue()) {

                contextDossier
                        .getContextAffilie()
                        .getLogger()
                        .getWarningsLogger(
                                contextDossier.getDossier().getId() + " - "
                                        + contextDossier.getContextAffilie().getNumAffilie(),
                                contextDossier.getDossier().getAllocataireComplexModel()
                                        .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                                        + " "
                                        + contextDossier.getDossier().getAllocataireComplexModel()
                                                .getPersonneEtendueComplexModel().getTiers().getDesignation2(), csvVal)
                        .addMessage(
                                new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, ContextPrestation.class
                                        .getName(), "al.generation.warning.affilieInactif", params));
            }

            for (int i = 0; i < detailsPrest.size(); i++) {
                @SuppressWarnings("rawtypes")
                ArrayList list = detailsPrest.get(i);
                DetailPrestationModel detail = (DetailPrestationModel) list.get(0);
                TransfertTucanaModel tranfert = (TransfertTucanaModel) list.get(1);
                detail.setIdEntete(entete.getId());

                // dans le cas d'une prestation unitaire jour ou heure, on place
                // les détails à la fin de la période
                if (ALCSDossier.UNITE_CALCUL_HEURE.equals(contextDossier.getDossier().getDossierModel()
                        .getUniteCalcul())) {
                    detail.setPeriodeValidite(entete.getPeriodeA());
                }

                detail = ALImplServiceLocator.getDetailPrestationModelService().create(detail);

                if (tranfert != null) {
                    tranfert.setIdDetailPrestation(detail.getIdDetailPrestation());
                    ALImplServiceLocator.getTransfertTucanaModelService().create(tranfert);
                }
            }
        }

        release();
    }
}
