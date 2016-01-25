package ch.globaz.vulpecula.facturation;

import globaz.framework.util.FWMessage;
import globaz.globall.db.BProcess;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.osiris.api.APIRubrique;
import globaz.osiris.api.APISection;
import java.util.Deque;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.NumeroDecompte;
import ch.globaz.vulpecula.domain.models.prestations.Etat;
import ch.globaz.vulpecula.util.RubriqueUtil;
import ch.globaz.vulpecula.util.RubriqueUtil.Compte;
import ch.globaz.vulpecula.util.RubriqueUtil.Convention;
import ch.globaz.vulpecula.util.RubriqueUtil.Prestation;
import ch.globaz.vulpecula.util.RubriqueUtilException;

/**
 * Processus de facturation des absences
 * Va rechercher les d�comptes � l'�tat valid� ou rectifi�
 * Cr�� les afacts
 * 
 */
public final class PTProcessFacturationAbsencesJustifieesGenerer extends PTProcessFacturation {
    private static final long serialVersionUID = 2947007104564077249L;

    private static final String PASSAGE_NAME = "AJ. ";
    private static final Logger LOGGER = LoggerFactory.getLogger(PTProcessFacturationAbsencesJustifieesGenerer.class);

    public PTProcessFacturationAbsencesJustifieesGenerer() {
        super();
    }

    public PTProcessFacturationAbsencesJustifieesGenerer(final BProcess parent) {
        super(parent);
    }

    @Override
    protected void clean() {
    }

    @Override
    protected String getIdAdressePaiement(String idTiers) {
        return VulpeculaRepositoryLocator.getAdresseRepository()
                .findForPrestations(idTiers, new Date(getPassage().getDateFacturation())).getId();
    }

    /**
     * Cette m�thode ex�cute le processus de facturation des d�comptes
     * 
     * @return boolean est-ce que la facturation s'est effectu�e avec succ�s
     * @throws Exception
     */
    @Override
    protected boolean launch() {
        // Pour des raisons de mem on passe par un Deque (LinkedList) qui permet de lib�rer la mem apr�s chaque
        // it�ration.
        // La mem va monter et �tre lib�r�e quand il y aura besoin d'espace.
        Deque<AbsenceJustifiee> deque = new LinkedList<AbsenceJustifiee>(VulpeculaRepositoryLocator
                .getAbsenceJustifieeRepository().findDecomptesForFacturation(getPassage().getId()));

        // Boucler sur les d�comptes et charger les lignes de salaires
        while (!deque.isEmpty() && !isAborted()) {
            AbsenceJustifiee absence = deque.removeFirst();

            if (absence.getMontantBrut().isZero()) {
                continue;
            }

            // Cr�ation de l'ent�te de facture
            NumeroDecompte numero = new NumeroDecompte(absence.getAnnee(), NumeroDecompte.ABSENCES_JUSTIFIEES);
            FAEnteteFacture enteteFacture = null;
            try {
                enteteFacture = createEnteteFacture(absence.getEmployeurIdTiers(), absence.getNoAffilie(),
                        numero.getValue(), APISection.ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES);

                // Cr�ation des lignes de factures
                createAfactsForEmployeur(enteteFacture, absence);
            } catch (RubriqueUtilException e) {
                String message = "Absence justifi�e id : " + absence.getId() + ", N� d'affili� : "
                        + absence.getNoAffilie() + " " + absence.getRaisonSocialeEmployeur() + " / "
                        + absence.getTravailleurNss() + "\r\n" + e.toString();
                LOGGER.warn(message);
                getMemoryLog().logMessage(message, FWMessage.AVERTISSEMENT, this.getClass().getName());
            } catch (Exception ex) {
                String message = "Absence justifi�e id : " + absence.getId() + ", N� d'affili� : "
                        + absence.getNoAffilie() + " " + absence.getRaisonSocialeEmployeur() + " / "
                        + absence.getTravailleurNss() + "\r\n" + ex.toString();

                LOGGER.error(message);
                getTransaction().addErrors(message);
                return false;
            }

            if (!getTransaction().hasErrors()) {
                majEtatPrestation(absence);
            }
        }

        VulpeculaServiceLocator.getPassageService().createPassageForNextWeekIfNotExist(getSession(),
                FAModuleFacturation.CS_MODULE_ABSENCES_JUSTIFIEES, PASSAGE_NAME, getIdPassage());
        return !isAborted();
    }

    /**
     * Change l'�tat de la prestation � TRAITEE
     * 
     * @param absence
     */
    private void majEtatPrestation(final AbsenceJustifiee absence) {
        absence.setEtat(Etat.TRAITEE);
        VulpeculaRepositoryLocator.getAbsenceJustifieeRepository().update(absence);
    }

    /**
     * Cr�ation de la ligne de facture selon la cotisation calculee pour une entete de facture
     * 
     * @param enteteFacture
     * @param absence
     * @throws Exception
     */
    private void createAfactsForEmployeur(FAEnteteFacture enteteFacture, AbsenceJustifiee absence) throws Exception {
        int idCaisseMetier = VulpeculaServiceLocator.getPosteTravailService().getIdTiersCaissePrincipale(
                absence.getIdPosteTravail());
        Montant montantBrut = absence.getMontantBrut();

        // cr�ation des afacts
        addAfact(enteteFacture, absence, montantBrut.getNegativeValue().toString(), Compte.PRESTATION, idCaisseMetier);

        Montant montantAC = montantBrut.multiply(absence.getTauxAC()).normalize();
        Montant montantAVS = montantBrut.multiply(absence.getTauxAVS()).normalize();
        if (absence.getMontantBrut().getMontantAbsolu().greater(absence.getMontantVerse().getMontantAbsolu())) {
            // Cas des �lectriciens ; travailleur horaire ; AVS au BMS
            addAfact(enteteFacture, absence, montantAVS.toString(), Compte.RETENUES_AVS, idCaisseMetier);
            addAfact(enteteFacture, absence, montantAC.toString(), Compte.RETENUES_AC, idCaisseMetier);
        } else {
            // Tous les autres cas
            addAfact(enteteFacture, absence, montantAVS.getNegativeValue().toString(), Compte.PARTS_PATRONALES_AVS,
                    idCaisseMetier);
            addAfact(enteteFacture, absence, montantAC.getNegativeValue().toString(), Compte.PARTS_PATRONALES_AC,
                    idCaisseMetier);
        }
    }

    /**
     * @param enteteFacture
     * @param absence
     * @param montant
     * @param compte
     * @param idCaisseMetier
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws Exception
     */
    private void addAfact(FAEnteteFacture enteteFacture, AbsenceJustifiee absence, String montant, Compte compte,
            int idCaisseMetier) throws Exception {
        FAAfact afact = initAfact(enteteFacture.getIdEntete(), FAModuleFacturation.CS_MODULE_ABSENCES_JUSTIFIEES);
        if (!JadeStringUtil.isBlankOrZero(montant)) {
            String csRefRubrique = RubriqueUtil.findReferenceRubriqueFor(Prestation.ABSENCE_JUSTIFIEE,
                    Convention.fromValue(absence.getConventionEmployeur().getCode()), compte);
            APIRubrique rubriquePartCot = RubriqueUtil.retrieveRubriqueForReference(getSession(), csRefRubrique);
            afact.setLibelle(rubriquePartCot.getDescription(absence.getPosteTravail().getEmployeur().getLangue()));
            afact.setIdRubrique(rubriquePartCot.getId());
            afact.setDebutPeriode(absence.getDateDebutAbsence());
            afact.setFinPeriode(absence.getDateFinAbsence());
            afact.setMontantFacture(montant);
            afact.setNumCaisse("" + idCaisseMetier);
            afact.setReferenceExterne(absence.getBeneficiaire().getValue() + "-"
                    + absence.getConventionEmployeur().getCode());
            afact.add();
        }
    }
}
