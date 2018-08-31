package ch.globaz.vulpecula.facturation;

import java.util.Deque;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.prestations.Beneficiaire;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.util.RubriqueUtil;
import ch.globaz.vulpecula.util.RubriqueUtil.Compte;
import ch.globaz.vulpecula.util.RubriqueUtil.Convention;
import ch.globaz.vulpecula.util.RubriqueUtil.Prestation;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;

/**
 * BProcess permettant la g�n�ration des afacts lors de la g�n�ration des cong�s pay�s.
 */
public class PTProcessFacturationCongePayeGenerer extends PTProcessFacturation {
    private static final long serialVersionUID = 1L;

    private static final String PASSAGE_NAME = "CP. ";
    private static final Logger LOGGER = LoggerFactory.getLogger(PTProcessFacturationCongePayeGenerer.class);

    @Override
    protected void clean() {
    }

    @Override
    protected String getIdAdressePaiement(String idTiers) {
        return VulpeculaRepositoryLocator.getAdresseRepository()
                .findForPrestations(idTiers, new Date(getPassage().getDateFacturation())).getId();
    }

    @Override
    protected boolean launch() {
        // Suppression des entete et afact du journal
        deleteEnteteEtAfactForIdPassage(getIdPassage());

        // Recherche des d�comptes valid�s ou rectifi�s
        // Pour des raisons de mem on passe par un Deque (LinkedList) qui permet de lib�rer la mem apr�s chaque
        // it�ration.
        // La mem va monter et �tre lib�r�e quand il y aura besoin d'espace.
        Deque<CongePaye> deque = new LinkedList<CongePaye>(
                VulpeculaRepositoryLocator.getCongePayeRepository().findForFacturation(getPassage().getId()));

        // Boucler sur les d�comptes et charger les lignes de salaires
        while (!deque.isEmpty() && !isAborted()) {
            CongePaye congePaye = deque.removeFirst();

            if (congePaye.getMontantNet().isZero()) {
                continue;
            }

            // POBMS-61 Prendre la date de fin et non plus la date de d�but.
            NumeroDecompte numero = new NumeroDecompte(congePaye.getAnneeFinAsValue(), NumeroDecompte.CONGES_PAYES);
            FAEnteteFacture enteteFacture = null;
            try {
                if (Beneficiaire.EMPLOYEUR.equals(congePaye.getBeneficiaire())
                        || Beneficiaire.NOTE_CREDIT.equals(congePaye.getBeneficiaire())) {
                    // Cr�ation de l'ent�te de facture
                    enteteFacture = createEnteteFacture(congePaye.getEmployeurIdTiers(), congePaye.getNoAffilie(),
                            numero.getValue(), APISection.ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES);

                    // Cr�ation des lignes de factures
                    createAfactsForEmployeur(enteteFacture, congePaye);

                } else if (Beneficiaire.TRAVAILLEUR.equals(congePaye.getBeneficiaire())) {
                    if (congePaye.getTravailleurNss() == null || congePaye.getTravailleurNss().length() == 0) {
                    	// TODO a corriger (peut-�tre remplacer le adError par un warning)
                        this._addError("Pas de num�ro NSS pour le travailleur "
                                + congePaye.getPosteTravail().getDescriptionTravailleur() + ". IdTiers="
                                + congePaye.getTravailleurIdTiers());
                        continue;
                    }
                    enteteFacture = createEnteteFacture(congePaye.getTravailleurIdTiers(),
                            congePaye.getTravailleurNss(), numero.getValue(),
                            APISection.ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES);
                    createAfactsForTravailleur(enteteFacture, congePaye);
                }
            } catch (Exception ex) {
                String message = "Conge pay� id : " + congePaye.getIdCongePaye() + ", N� d'affili� : "
                        + congePaye.getNoAffilie() + " " + congePaye.getRaisonSocialeEmployeur() + " / "
                        + congePaye.getTravailleurNss() + "\r\n" + ex.toString();

                LOGGER.error(message);
                getTransaction().addErrors(message);
                return false;
            }

            if (!getTransaction().hasErrors()) {
                majEtatPrestation(congePaye);
            }
        }

        VulpeculaServiceLocator.getPassageService().createPassageForNextWeekIfNotExist(getSession(),
                FAModuleFacturation.CS_MODULE_CONGE_PAYE, PASSAGE_NAME, getIdPassage());
        return !isAborted();
    }

    /**
     * Change l'�tat de la prestation � TRAITEE
     *
     * @param congePaye
     */
    private void majEtatPrestation(CongePaye congePaye) {
        congePaye.setEtat(Etat.TRAITEE);
        VulpeculaRepositoryLocator.getCongePayeRepository().update(congePaye);
    }

    /**
     * Cr�ation de la ligne de facture selon la cotisation calculee pour une entete de facture
     *
     * @param enteteFacture
     * @param congePaye
     * @throws Exception
     */
    private void createAfactsForEmployeur(FAEnteteFacture enteteFacture, CongePaye congePaye) throws Exception {
        // cr�ation de l'afact
        int idCaisseMetier = VulpeculaServiceLocator.getPosteTravailService()
                .getIdTiersCaissePrincipale(congePaye.getIdPosteTravail());

        // Ajout des afacts pour la rubrique CP
        addAfact(enteteFacture, congePaye, congePaye.getMontantBrut().getNegativeValue().toString(), Compte.PRESTATION,
                idCaisseMetier);

        if (congePaye.getMontantBrut().getMontantAbsolu().greater(congePaye.getMontantNet().getMontantAbsolu())) {
            // Que dans le cas des �lectriciens o� l'on d�duit les CPS cotis�es
            addAfactForCotisationForRetenue(enteteFacture, congePaye, idCaisseMetier);
        } else {
            // Tous les autres cas
            for (Compte compte : Compte.values()) {
                if (compte.getTypeAssurance() != null) {
                    String montant = congePaye.getMontantFor(compte.getTypeAssurance()).normalize().getNegativeValue()
                            .toString();
                    if (!"0".equals(montant)) {
                        addAfact(enteteFacture, congePaye, montant, compte, idCaisseMetier);
                    }
                }
            }
        }
    }

    /**
     * @param enteteFacture
     * @param congePaye
     * @throws Exception
     */
    private void createAfactsForTravailleur(FAEnteteFacture enteteFacture, CongePaye congePaye) throws Exception {
        // cr�ation de l'afact
        int idCaisseMetier = VulpeculaServiceLocator.getPosteTravailService()
                .getIdTiersCaissePrincipale(congePaye.getIdPosteTravail());

        addAfact(enteteFacture, congePaye, congePaye.getMontantBrut().getNegativeValue().toString(), Compte.PRESTATION,
                idCaisseMetier);

        addAfactForCotisationForRetenue(enteteFacture, congePaye, idCaisseMetier);
    }

    /**
     * @param enteteFacture
     * @param congePaye
     * @param idCaisseMetier
     * @throws Exception
     */
    private void addAfactForCotisationForRetenue(FAEnteteFacture enteteFacture, CongePaye congePaye, int idCaisseMetier)
            throws Exception {

        Montant mntRetenueMax = congePaye.getTotalSalaire().multiply(congePaye.getTauxCP()).normalize()
                .substract(congePaye.getMontantNet());
        // Hack pour probl�me d'arrondi
        if (mntRetenueMax.absolute().equals(new Montant(0.05))) {
            return;
        }
        for (Compte compte : Compte.values()) {
            if (compte.getTypeAssuranceRetenue() != null) {
                Montant montantCoti = congePaye.getMontantFor(compte.getTypeAssuranceRetenue()).normalize();
                mntRetenueMax = mntRetenueMax.substract(montantCoti);

                if (mntRetenueMax.getMontantAbsolu().less(new Montant("0.09"))) {
                    montantCoti = montantCoti.add(mntRetenueMax);
                }
                if (!montantCoti.isZero()) {
                    addAfact(enteteFacture, congePaye, montantCoti.toString(), compte, idCaisseMetier);
                }
            }
        }
    }

    private void addAfact(FAEnteteFacture enteteFacture, CongePaye congePaye, String montant, Compte compte,
            int idCaisseMetier) throws Exception {
        FAAfact afact = initAfact(enteteFacture.getIdEntete(), FAModuleFacturation.CS_MODULE_CONGE_PAYE);
        if (!JadeStringUtil.isBlankOrZero(montant)) {
            String csRefRubrique = RubriqueUtil.findReferenceRubriqueFor(Prestation.CONGE_PAYE,
                    Convention.fromValue(congePaye.getConventionEmployeur().getCode()), compte);
            APIRubrique rubriquePartCot = RubriqueUtil.retrieveRubriqueForReference(getSession(), csRefRubrique);
            afact.setLibelle(rubriquePartCot.getDescription(congePaye.getPosteTravail().getEmployeur().getLangue()));
            afact.setIdRubrique(rubriquePartCot.getId());
            afact.setDebutPeriode(congePaye.getPeriodeDebutAsSwissValue());
            afact.setFinPeriode(congePaye.getPeriodeFinAsSwissValue());
            afact.setMontantFacture(montant);
            afact.setNumCaisse("" + idCaisseMetier);
            afact.setReferenceExterne(
                    congePaye.getBeneficiaire().getValue() + "-" + congePaye.getConventionEmployeur().getCode());
            afact.add();
        }
    }

}
