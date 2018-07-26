package ch.globaz.pegasus.businessimpl.services.models.calcul.joursAppoint;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import ch.globaz.common.domaine.Periode;
import ch.globaz.common.domaine.Periode.ComparaisonDePeriode;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplaceSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.businessimpl.utils.RequerantConjoint;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculComparatif;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.PersonnePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;

public class PrepareDonneForJourAppoint {
    boolean isMemePeriodeEtJoursAppointExist = false;
    boolean is2Rentes = false;
    boolean isMontantRenteConjointExist = false;
    boolean isMontantRenteRequerantExist = false;

    /**
     * Calcul des jours d'appoints s'il le faut l'ajoute dans la periode Pour calcule le montant du jour d'appont on
     * utilse le montant de la période d'avant si elle existe sinon on utilise le montant de l'ancienne version de
     * droit.
     * 
     * @param dateDebutPlageCalcul
     */
    public void addJourAppointInPeriodeIfNeeded(List<PeriodePCAccordee> listePCAccordes,
            CalculPcaReplaceSearch pcaReplaceSearch) throws CalculException {
        try {
            // Récupération de la propriété système, si jours d'appoints actif
            if (PCproperties.getBoolean(EPCProperties.GESTION_JOURS_APPOINTS)) {
                PeriodePCAccordee periodePrecedente = null;
                isMemePeriodeEtJoursAppointExist = false;
                isMontantRenteRequerantExist = false;
                isMontantRenteConjointExist = false;
                for (PeriodePCAccordee periode : listePCAccordes) {
                    CalculComparatif ccRetenu = periode.getCCRetenu()[0];
                    boolean domToSpeMal = false;

                    is2Rentes(ccRetenu.getPersonnes());
                    // on itere sur les personnes
                    for (PersonnePCAccordee personne : ccRetenu.getPersonnes()) {
                        Date dateEntreeHomePersonne = getDateEntreeHome(personne);
                        if (personne.isConjoint() || personne.isRequerant()) {

                            if (mustGenerateJourAppoint(periode.getDateDebut(), dateEntreeHomePersonne)
                                    && personne.getIsHome()) {
                                RequerantConjoint<CalculPcaReplace> pcaReqConj = resolveOldPcaRequerantConjoint(
                                        pcaReplaceSearch, periode);
                                String montantPrecedant = "0";
                                if (personne.isConjoint()) {
                                    if ((!pcaReqConj.isRequerantEmpty())
                                            && !JadeStringUtil.isBlankOrZero(pcaReqConj.getRequerant()
                                                    .getSimplePCAccordee().getIdPrestationAccordeeConjoint())) {
                                        montantPrecedant = pcaReqConj.getRequerant()
                                                .getSimplePrestationsAccordeesConjoint().getMontantPrestation();
                                    } else if (!pcaReqConj.isConjointEmpty()) {
                                        montantPrecedant = pcaReqConj.getConjoint().getSimplePrestationsAccordees()
                                                .getMontantPrestation();
                                    }
                                    CalculComparatif calculComparatifPrecedant = null;
                                    if (periodePrecedente != null) {
                                        calculComparatifPrecedant = periodePrecedente.getCCRetenu()[1];

                                        if (calculComparatifPrecedant == null) {
                                            calculComparatifPrecedant = periodePrecedente.getCCRetenu()[0];
                                        }
                                    }

                                    if (calculComparatifPrecedant != null) {
                                        montantPrecedant = calculComparatifPrecedant.getMontantPCMensuel();
                                        if (!JadeStringUtil.isBlankOrZero(calculComparatifPrecedant
                                                .getMontantMensuelConjoint())) {
                                            montantPrecedant = calculComparatifPrecedant.getMontantMensuelConjoint();
                                        }

                                        periode.setJoursAppointConjoint(generateJoursAppoint(dateEntreeHomePersonne,
                                                periode.getCCRetenu()[1], montantPrecedant, true));
                                    } else {
                                        if (!is2Rentes) {
                                            periode.setJoursAppointConjoint(generateJoursAppoint(
                                                    dateEntreeHomePersonne, periode.getCCRetenu()[1], "0", true));
                                        } else {
                                            periode.setJoursAppointConjoint(generateJoursAppoint(
                                                    dateEntreeHomePersonne, periode.getCCRetenu()[1], montantPrecedant,
                                                    true));
                                        }

                                    }

                                } else if (personne.isRequerant()) {

                                    if (!pcaReqConj.isRequerantEmpty()) {
                                        montantPrecedant = pcaReqConj.getRequerant().getSimplePrestationsAccordees()
                                                .getMontantPrestation();

                                    }

                                    if (periodePrecedente != null) {
                                        CalculComparatif calculComparatifPrecedant = (periodePrecedente.getCCRetenu()[0]);
                                        if (calculComparatifPrecedant != null) {
                                            montantPrecedant = calculComparatifPrecedant.getMontantPCMensuel();
                                            if (!JadeStringUtil.isBlankOrZero(calculComparatifPrecedant
                                                    .getMontantMensuelConjoint())) {
                                                montantPrecedant = calculComparatifPrecedant
                                                        .getMontantMensuelRequerant();
                                            }
                                        }
                                    }

                                    periode.setJoursAppointRequerant(generateJoursAppoint(dateEntreeHomePersonne,
                                            ccRetenu, montantPrecedant, false));

                                }
                            }
                        }
                    }
                    periodePrecedente = periode;
                }
            }
        } catch (Exception e) {
            throw new CalculException("Problem with the processus gestion Jours Appoint", e);
        }
    }

    private void is2Rentes(List<PersonnePCAccordee> list) {
        for (PersonnePCAccordee personne : list) {
            for (CalculDonneesCC donnePersonne : personne.getDonneesBD()) {
                if (donnePersonne.getCsRoleFamille().equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)
                        && !isMontantRenteConjointExist) {
                    isMontantRenteConjointExist = !JadeStringUtil.isBlankOrZero(donnePersonne.getRenteAVSAIMontant());
                }
                if (donnePersonne.getCsRoleFamille().equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)
                        && !isMontantRenteRequerantExist) {
                    isMontantRenteRequerantExist = !JadeStringUtil.isBlankOrZero(donnePersonne.getRenteAVSAIMontant());
                }

            }
        }
        if (isMontantRenteConjointExist & isMontantRenteRequerantExist) {
            is2Rentes = true;
        } else {
            is2Rentes = false;
        }

    }

    private RequerantConjoint<CalculPcaReplace> resolveOldPcaRequerantConjoint(CalculPcaReplaceSearch pcaReplaceSearch,
            PeriodePCAccordee periode) {
        RequerantConjoint<CalculPcaReplace> pcaRequerantConjoint = new RequerantConjoint<CalculPcaReplace>();

        Periode periodeActuelle = generatePeriode(periode);

        for (JadeAbstractModel model : pcaReplaceSearch.getSearchResults()) {
            CalculPcaReplace pca = (CalculPcaReplace) model;
            Periode periodePrecedante = new Periode(pca.getSimplePCAccordee().getDateDebut(), pca.getSimplePCAccordee()
                    .getDateFin());

            if ((periodeActuelle.comparerChevauchementMois(periodePrecedante).equals(
                    ComparaisonDePeriode.LES_PERIODES_SE_CHEVAUCHENT) && !periodeActuelle.getDateDebut().equals(
                    periodePrecedante.getDateDebut()))
                    || periodeActuelle.comparerChevauchementMois(periodePrecedante).equals(
                            ComparaisonDePeriode.LES_PERIODES_SE_SUIVENT)) {
                if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(pca.getSimplePCAccordee().getCsRoleBeneficiaire())) {
                    pcaRequerantConjoint.setRequerant(pca);
                } else if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(pca.getSimplePCAccordee().getCsRoleBeneficiaire())) {
                    pcaRequerantConjoint.setConjoint(pca);
                }
            }
            if (pca.getSimplePCAccordee().getHasJoursAppoint()) {
                isMemeJourDEntreeenHomeEtJoursApppoint(periodePrecedante, periodeActuelle);
            }

        }
        return pcaRequerantConjoint;
    }

    private void isMemeJourDEntreeenHomeEtJoursApppoint(Periode periodePrecedante, Periode periodeActuelle) {
        if (periodePrecedante.compareTo(periodeActuelle) == 0) {
            isMemePeriodeEtJoursAppointExist = true;
        } else {
            isMemePeriodeEtJoursAppointExist = false;
        }

    }

    private boolean mustGenerateJourAppoint(Date dateDebut, Date dateEntreeHomePersonne) {
        return (dateEntreeHomePersonne != null)
                && checkIfDateEntreeHomeInMonthBeforeDateDebutPca(dateEntreeHomePersonne, dateDebut);
    }

    private Date getDateEntreeHome(PersonnePCAccordee personne) {
        TupleDonneeRapport tuplePersonne = personne.getRootDonneesConsolidees();
        Date dateEntreeHomePersonne = JadeDateUtil.getGlobazDate(tuplePersonne
                .getLegendeEnfant(IPCValeursPlanCalcul.CLE_INTER_DATE_ENTREE_HOME));
        return dateEntreeHomePersonne;
    }

    private Periode generatePeriode(PeriodePCAccordee periode) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String dateFin = null;
        if (periode.getDateFin() != null) {
            dateFin = dateFormat.format(periode.getDateFin());
            dateFin.substring(3);
        }
        Periode periodeActuelle = new Periode(dateFormat.format(periode.getDateDebut()).substring(3), dateFin);
        return periodeActuelle;
    }

    private boolean checkIfDateEntreeHomeInMonthBeforeDateDebutPca(Date dateEntree, Date dateDebutPca) {
        if (dateEntree.after(dateDebutPca) || dateEntree.equals(dateDebutPca)) {
            return false;
        } else {
            Calendar calToCompare = Calendar.getInstance();
            calToCompare.setTime(dateEntree);
            calToCompare.add(Calendar.DAY_OF_MONTH, calToCompare.getMaximum(Calendar.DAY_OF_MONTH));
            Date dateToCompare = calToCompare.getTime();
            return dateToCompare.after(dateDebutPca);
        }
    }

    private SimpleJoursAppoint generateJoursAppoint(Date dateEntreeHome, CalculComparatif ccRetenu,
            String montantPrecedantPca, boolean isConjoint) {

        if ((Float.parseFloat(ccRetenu.getExcedentAnnuel()) > 0)) {
            CalculJourAppoint calculJourAppoint = new CalculJourAppoint();
            BigDecimal montantPrecedant = new BigDecimal(montantPrecedantPca);
            SimpleJoursAppoint sja = calculJourAppoint.generateJoursAppoint(dateEntreeHome,
                    new BigDecimal(ccRetenu.getMontantPCMensuel()), montantPrecedant, isMemePeriodeEtJoursAppointExist);

            return sja;
        }
        return null;

    }

}
