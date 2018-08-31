package ch.globaz.vulpecula.process.revision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.HistoriqueDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.repositories.decompte.PrestationsAFPourRevision;
import ch.globaz.vulpecula.domain.repositories.decompte.TravailleurPourRevision;
import ch.globaz.vulpecula.external.models.musca.Passage;
import ch.globaz.vulpecula.process.prestations.PrestationsListExcel;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;

public class ListRevisionExcel extends PrestationsListExcel {
    TreeMap<String, TravailleurPourRevision> listeTravailleur = null;
    // private static final String SHEET_TITLE = "rev";
    private String idEmployeur;
    private String annee;

    public ListRevisionExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
    }

    public ListRevisionExcel(PrestationsListExcel listExcel) {
        super(listExcel);
    }

    @Override
    public void createContent() {
        if (listeTravailleur == null || listeTravailleur.isEmpty()) {
            createFeuilleVide();
        } else {
            createTable();
        }
    }

    private void createFeuilleVide() {
        HSSFSheet sheet = createSheet("rev");

        sheet.setColumnWidth((short) 0, (short) 6000);
        sheet.setColumnWidth((short) 1, (short) 6000);
        sheet.setColumnWidth((short) 2, (short) 6000);
        sheet.setColumnWidth((short) 3, (short) 6000);
        sheet.setColumnWidth((short) 4, (short) 6000);
        sheet.setColumnWidth((short) 5, (short) 6000);
        sheet.setColumnWidth((short) 6, (short) 6000);
        sheet.setColumnWidth((short) 7, (short) 6000);
        sheet.setColumnWidth((short) 8, (short) 6000);
        initPage(true);
        createRow();
        createCriteresEmployeur();
        createRow(2);
    }

    private void createCriteresEmployeur() {
        createCell(getListName());
        createRow(2);
        Employeur employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findById(getIdEmployeur());
        createCell(employeur.getConvention().getDesignation(), getStyleGras());
        createCell(employeur.getRaisonSociale(), getStyleGras());
        createCell(employeur.getAffilieNumero(), getStyleGras());
        createCell(getAnnee(), getStyleGras());

    }

    private void createOnglet(Travailleur travailleur) {
        HSSFSheet sheet = createSheet(travailleur.getNomPrenomTravailleur());

        sheet.setFitToPage(true);
        HSSFPrintSetup ps = sheet.getPrintSetup();
        ps.setFitWidth( (short) 1);
        ps.setFitHeight( (short) 1);
        sheet.setAutobreaks(true);

        sheet.setColumnWidth((short) 0, (short) 6000);
        sheet.setColumnWidth((short) 1, (short) 6000);
        sheet.setColumnWidth((short) 2, (short) 6000);
        sheet.setColumnWidth((short) 3, (short) 6000);
        sheet.setColumnWidth((short) 4, (short) 6000);
        sheet.setColumnWidth((short) 5, (short) 6000);
        sheet.setColumnWidth((short) 6, (short) 6000);
        sheet.setColumnWidth((short) 7, (short) 6000);
        sheet.setColumnWidth((short) 8, (short) 6000);

        initPage(true);
        createRow();
        createCriteresEmployeur();
        createRow(2);
    }

    private static Map<String, List<AdhesionCotisationPosteTravail>> groupByPlanCaisseId(
            List<AdhesionCotisationPosteTravail> adhesionCotisationViews) {
        Map<String, List<AdhesionCotisationPosteTravail>> map = new TreeMap<String, List<AdhesionCotisationPosteTravail>>();
        for (AdhesionCotisationPosteTravail adhesionCotisationView : adhesionCotisationViews) {
            if (!map.containsKey(adhesionCotisationView.getPlanCaisse().getLibelle())) {
                map.put(adhesionCotisationView.getPlanCaisse().getLibelle(),
                        new ArrayList<AdhesionCotisationPosteTravail>());
            }
            map.get(adhesionCotisationView.getPlanCaisse().getLibelle()).add(adhesionCotisationView);
        }

        return map;
    }

    private List<AdhesionCotisationPosteTravail> removeAC2(List<AdhesionCotisationPosteTravail> cotisations){
        List<AdhesionCotisationPosteTravail> listToReturn = new ArrayList<AdhesionCotisationPosteTravail>();
        for(AdhesionCotisationPosteTravail adhesion : cotisations) {
            if(!TypeAssurance.COTISATION_AC2.equals(adhesion.getTypeAssurance())) {
                listToReturn.add(adhesion);
            }         
        }
        return listToReturn;

    }

    private void createInformationsPoste(TravailleurPourRevision travailleur) {
        String idPosteTravail = "";
        if (travailleur.getTravailleur().getPostesTravail().size() <= 0) {
            return;
        } else {
            idPosteTravail = travailleur.getTravailleur().getPostesTravail().get(0).getId();
        }

        Annee annee = new Annee(getAnnee());
        List<AdhesionCotisationPosteTravail> adhesionCotisationPosteTravails = VulpeculaRepositoryLocator
                .getAdhesionCotisationPosteRepository().findByIdPosteTravail(idPosteTravail, annee);
        adhesionCotisationPosteTravails = removeAC2(adhesionCotisationPosteTravails);
        for (AdhesionCotisationPosteTravail coti : adhesionCotisationPosteTravails) {
            coti.setTaux(VulpeculaServiceLocator.getCotisationService().findTaux(coti.getIdCotisation(),
                    annee.getLastDayOfYear()));
        }

        for (Entry<String, List<AdhesionCotisationPosteTravail>> adhesions : groupByPlanCaisseId(
                adhesionCotisationPosteTravails).entrySet()) {
            createCell(adhesions.getKey(), getStyleGras());
            createRow();
            List<AdhesionCotisationPosteTravail> adh = adhesions.getValue();
            // TODO Set locale to sort on good language!
            Collections.sort(adh);
            for (AdhesionCotisationPosteTravail adhesionCotisationView : adh) {
                createCell(adhesionCotisationView.getCotisation().getAssurance().getLibelleFr());
                createCell(adhesionCotisationView.getPeriode().getDateDebutAsSwissValue());
                String datefin = "";
                if (adhesionCotisationView.getPeriode().getDateFin() != null) {
                    datefin = (adhesionCotisationView.getPeriode().getDateFinAsSwissValue());
                }
                createCell(datefin);
                createCell((adhesionCotisationView.getTauxContribuable().getBigDecimal().doubleValue() / 100),
                        getStylePourcentNone());
                createRow();
            }
        }
    }

    private void createTable() {
        for (Map.Entry<String, TravailleurPourRevision> travailleurRevision : listeTravailleur.entrySet()) {

            TravailleurPourRevision trav = travailleurRevision.getValue();
            createOnglet(trav.getTravailleur());

            String qualif = "";
            String idPoste = "";
            if (trav.getTravailleur().getPostesTravail() != null) {
                for (PosteTravail pt : trav.getTravailleur().getPostesTravail()) {
                    if (pt.getIdEmployeur().equals(idEmployeur)) {
                        qualif = getSession().getCodeLibelle(pt.getQualificationAsValue());
                        idPoste = pt.getId();
                    }
                }
            }

            createRow();
            createCell(idPoste, getStyleGris25PourcentGras());
            createCell(trav.getTravailleur().getNomPrenomTravailleur(), getStyleGris25PourcentGras());
            createCell(trav.getTravailleur().getNumAvsActuel(), getStyleGris25PourcentGras());
            createCell(trav.getTravailleur().getDateNaissance(), getStyleGris25PourcentGras());
            createCell(qualif, getStyleGris25PourcentGras());
            createCell("", getStyleGris25PourcentGras());
            createCell("", getStyleGris25PourcentGras());
            createCell("", getStyleGris25PourcentGras());
            createCell("", getStyleGris25PourcentGras());
            createRow();
            createRow();
            createCell(getSession().getLabel("LISTE_REVISION_SALAIRE_AF") + " " + getAnnee() + " : ",
                    getStyleListLeftNone());
            createCell(trav.getMontantAF(), getStyleMontantNone());
            createRow();
            createCell(getSession().getLabel("LISTE_REVISION_SALAIRE_BASE") + " " + getAnnee() + " : ",
                    getStyleListLeftNone());
            createCell(trav.getMontantBase(), getStyleMontantNone());
            createRow();
            createCell(getSession().getLabel("LISTE_REVISION_SALAIRE_AVS") + " " + getAnnee() + " : ",
                    getStyleListLeftNone());
            createCell(trav.getMontantAVS(), getStyleMontantNone());
            createRow();
            createRow();
            if (existeAuMoinsUnDecompte(trav)) {
                createCell(getSession().getLabel("LISTE_REVISION_DECOMPTES"), getStyleGras());
                createRow();
                createCell(getSession().getLabel("LISTE_REVISION_TYPE_DECOMPTE"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_ETABLISSEMENT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_DEBUT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_FIN"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_HEURES"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_SALAIRES"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_TAUX"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_ETAT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_COMPTABILISATION"), getStyleListRightNone());
                createRow();
            }

            for (DecompteSalaire decompteSalaire : trav.getDecomptesSalairePR()) {
                if (decompteSalaire.getDecompte().getEtat().equals(EtatDecompte.COMPTABILISE)) {
                    createCell(getSession().getLabel("LISTE_REVISION_PERIODIQUE_COURT"), getStyleListRightNone());
                    createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(), getStyleListRightNone());
                    createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightNone());
                    createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightNone());
                    createCell(decompteSalaire.getHeures(), getStyleMontantNone());
                    createCell(decompteSalaire.getSalaireTotal(), getStyleMontantNone());
                    createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentNone());
                    createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                            getStyleListRightNone());
                    if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                        createCell(
                                getSession().getCodeLibelle(
                                        decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                getStyleListRightNone());
                    }
                    renseigneDateFacturation(decompteSalaire);
                } else {
                    createCell(getSession().getLabel("LISTE_REVISION_PERIODIQUE_COURT"), getStyleListRightRed());
                    createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getHeures(), getStyleMontantRed());
                    createCell(decompteSalaire.getSalaireTotal(), getStyleMontantRed());
                    createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentNone());
                    createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                            getStyleListRightNone());
                    if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                        createCell(
                                getSession().getCodeLibelle(
                                        decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                getStyleListRightNone());
                    }
                    renseigneDateFacturation(decompteSalaire);
                }
                createRow();
            }

            for (DecompteSalaire decompteSalaire : trav.getDecomptesSalaireSP()) {
                if (decompteSalaire.getDecompte().getEtat().equals(EtatDecompte.COMPTABILISE)) {
                    createCell(getSession().getLabel("LISTE_REVISION_SPECIAL_COURT"), getStyleListRightNone());
                    createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(), getStyleListRightNone());
                    createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightNone());
                    createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightNone());
                    createCell(decompteSalaire.getHeures(), getStyleMontantNone());
                    createCell(decompteSalaire.getSalaireTotal(), getStyleMontantNone());
                    createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentNone());
                    createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                            getStyleListRightNone());
                    if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                        createCell(
                                getSession().getCodeLibelle(
                                        decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                getStyleListRightNone());
                    }
                    renseigneDateFacturation(decompteSalaire);
                } else {
                    createCell(getSession().getLabel("LISTE_REVISION_SPECIAL_COURT"), getStyleListRightRed());
                    createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getHeures(), getStyleMontantRed());
                    createCell(decompteSalaire.getSalaireTotal(), getStyleMontantRed());
                    createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentRed());
                    createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                            getStyleListRightNone());
                    if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                        createCell(
                                getSession().getCodeLibelle(
                                        decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                getStyleListRightNone());
                    }
                    renseigneDateFacturation(decompteSalaire);
                }
                createRow();
            }

            for (DecompteSalaire decompteSalaire : trav.getDecomptesSalaireCP()) {
                if (decompteSalaire.getDecompte().getEtat().equals(EtatDecompte.COMPTABILISE)) {
                    createCell(getSession().getLabel("LISTE_REVISION_COMPLEMENTAIRE"), getStyleListRightNoneGreen());
                    createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(),
                            getStyleListRightNoneGreen());
                    createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightNoneGreen());
                    createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightNoneGreen());
                    createCell(decompteSalaire.getHeures(), getStyleMontantNoneGreen());
                    createCell(decompteSalaire.getSalaireTotal(), getStyleMontantNoneGreen());
                    createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentNoneGreen());
                    createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                            getStyleListRightNone());
                    if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                        createCell(
                                getSession().getCodeLibelle(
                                        decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                getStyleListRightNone());
                    }
                    renseigneDateFacturation(decompteSalaire);
                } else {
                    createCell(getSession().getLabel("LISTE_REVISION_COMPLEMENTAIRE"), getStyleListRightRed());
                    createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getHeures(), getStyleMontantRed());
                    createCell(decompteSalaire.getSalaireTotal(), getStyleMontantRed());
                    createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentRed());
                    createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                            getStyleListRightNone());
                    if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                        createCell(
                                getSession().getCodeLibelle(
                                        decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                getStyleListRightNone());
                    }
                    renseigneDateFacturation(decompteSalaire);
                }
                createRow();
            }

            for (DecompteSalaire decompteSalaire : trav.getDecomptesSalaireCT()) {
                if (decompteSalaire.getDecompte().getEtat().equals(EtatDecompte.COMPTABILISE)) {
                    createCell(getSession().getLabel("LISTE_REVISION_CONTROLE_EMPLOYEUR"), getStyleListRightNone());
                    createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(), getStyleListRightNone());
                    createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightNone());
                    createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightNone());
                    createCell(decompteSalaire.getHeures(), getStyleMontantNone());
                    createCell(decompteSalaire.getSalaireTotal(), getStyleMontantNone());
                    createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentNone());
                    createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                            getStyleListRightNone());
                    if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                        createCell(
                                getSession().getCodeLibelle(
                                        decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                getStyleListRightNone());
                    }
                    renseigneDateFacturation(decompteSalaire);
                } else {
                    createCell(getSession().getLabel("LISTE_REVISION_CONTROLE_EMPLOYEUR"), getStyleListRightRed());
                    createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightRed());
                    createCell(decompteSalaire.getHeures(), getStyleMontantRed());
                    createCell(decompteSalaire.getSalaireTotal(), getStyleMontantRed());
                    createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentRed());
                    createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                            getStyleListRightNone());
                    if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                        createCell(
                                getSession().getCodeLibelle(
                                        decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                getStyleListRightNone());
                    }
                    renseigneDateFacturation(decompteSalaire);
                }
                createRow();
            }

            if (existeAuMoinsUnDecompte(trav)) {
                // Affichage du total
                createCell("");
                createCell("");
                createCell("");
                createCell("");
                createCell(getSession().getLabel("LISTE_REVISION_TOTAL"), getStyleGras());
                createCell(trav.getMontantTotal(), getStyleMontantTotal());
            }

            if (trav.hasDecomptesCPP()) {
                createRow();
                createRow();
                for (DecompteSalaire decompteSalaire : trav.getDecomptesSalaireCPP()) {
                    if (decompteSalaire.isComptabilise()) {
                        createCell(getSession().getLabel("LISTE_REVISION_CPP"), getStyleListRightNone());
                        createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(),
                                getStyleListRightNone());
                        createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightNone());
                        createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightNone());
                        createCell(decompteSalaire.getHeures(), getStyleMontantNone());
                        createCell(decompteSalaire.getSalaireTotal(), getStyleMontantNone());
                        createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentNone());
                        createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                                getStyleListRightNone());
                        if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                            createCell(
                                    getSession().getCodeLibelle(
                                            decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                    getStyleListRightNone());
                        }
                        renseigneDateFacturation(decompteSalaire);
                    } else {
                        createCell(getSession().getLabel("LISTE_REVISION_CPP"), getStyleListRightRed());
                        createCell(decompteSalaire.getDecompte().getDateEtablissement().toString(),
                                getStyleListRightRed());
                        createCell(decompteSalaire.getPeriodeDebut().toString(), getStyleListRightRed());
                        createCell(decompteSalaire.getPeriodeFin().toString(), getStyleListRightRed());
                        createCell(decompteSalaire.getHeures(), getStyleMontantRed());
                        createCell(decompteSalaire.getSalaireTotal(), getStyleMontantRed());
                        createCell((decompteSalaire.getTauxContribuable().doubleValue()) / 100, getStylePourcentRed());
                        createCell(getSession().getCodeLibelle(decompteSalaire.getDecompte().getEtat().getValue()),
                                getStyleListRightNone());
                        if (decompteSalaire.getDecompte().getEtatTaxationOffice() != null) {
                            createCell(
                                    getSession().getCodeLibelle(
                                            decompteSalaire.getDecompte().getEtatTaxationOffice().getValue()),
                                    getStyleListRightNone());
                        }
                        renseigneDateFacturation(decompteSalaire);
                    }
                    createRow();
                }
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                createEmptyCell();
                createCell(getSession().getLabel("LISTE_REVISION_TOTAL"), getStyleGras());
                createCell(trav.getTotalCPP(), getStyleMontantTotal());
                createRow();
            }

            int compteur = 0;

            Montant totalAJ = Montant.ZERO;
            if (trav.getListeAJ().size() > 0) {
                createRow();
                createCell(getSession().getLabel("LISTE_REVISION_ABSENCE_JUSTIFIEE"), getStyleGras());
                createRow();
                createCell(getSession().getLabel("LISTE_REVISION_DATE_DEBUT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_FIN"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_MONTANT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_BENEFICIAIRE"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_VERSEMENT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_CODE_ABSENCE"), getStyleListRightNone());
                createRow();
            }
            for (AbsenceJustifiee aj : trav.getListeAJ()) {
                compteur++;
                createCell(aj.getDateDebutAbsence(), getStyleListRightNone());
                createCell(aj.getDateFinAbsence(), getStyleListRightNone());
                createCell(aj.getMontantBrut(), getStyleMontantNone());
                createCell(getSession().getCodeLibelle(aj.getBeneficiaire().getValue()), getStyleListRightNone());
                createCell(aj.getDateVersementComptable().toString(), getStyleListRightNone());
                createCell(getSession().getCodeLibelle(aj.getType().getValue()), getStyleListRightNone());
                createRow();
                totalAJ = totalAJ.add(aj.getMontantBrut());
                if (compteur == trav.getListeAJ().size()) {
                    createCell("");
                    createCell(getSession().getLabel("LISTE_REVISION_TOTAL"), getStyleGras());
                    createCell(totalAJ, getStyleMontantTotal());
                    createRow();
                }
            }

            compteur = 0;
            Montant totalCP = Montant.ZERO;
            if (trav.getListeCP().size() > 0) {
                createRow();
                createCell(getSession().getLabel("LISTE_REVISION_CONGE_PAYE"), getStyleGras());
                createRow();
                createCell(getSession().getLabel("LISTE_REVISION_ANNEE_DEBUT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_ANNEE_FIN"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_MONTANT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_BENEFICIAIRE"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_VERSEMENT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_TOTAL_SALAIRE"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_TAUX_CP"), getStyleListRightNone());
                createRow();
            }
            for (CongePaye cp : trav.getListeCP()) {
                compteur++;
                createCell(cp.getPeriode().getDateDebut().getAnnee(), getStyleListRightNone());
                createCell(cp.getPeriode().getDateFin().getAnnee(), getStyleListRightNone());
                createCell(cp.getMontantBrut(), getStyleMontantNone());
                createCell(getSession().getCodeLibelle(cp.getBeneficiaire().getValue()), getStyleListRightNone());
                createCell(cp.getDateVersementComptable().toString(), getStyleListRightNone());
                createCell(cp.getSalaires(), getStyleMontantNone());
                createCell((cp.getTauxCP().getBigDecimal().doubleValue() / 100), getStylePourcentNone());
                createRow();
                totalCP = totalCP.add(cp.getMontantBrut());
                if (compteur == trav.getListeCP().size()) {
                    createCell("");
                    createCell(getSession().getLabel("LISTE_REVISION_TOTAL"), getStyleGras());
                    createCell(totalCP, getStyleMontantTotal());
                    createRow();
                }
            }

            compteur = 0;
            Montant totalCOMPL = Montant.ZERO;
            Montant totalAPG = Montant.ZERO;
            if (trav.getListeSM().size() > 0) {
                createRow();
                createCell(getSession().getLabel("LISTE_REVISION_SERVICE_MILITAIRE"), getStyleGras());
                createRow();
                createCell(getSession().getLabel("LISTE_REVISION_DATE_DEBUT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_FIN"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_MONTANT_COMPLEMENT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_BENEFICIAIRE"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_VERSEMENT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_GENRE"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_VERSEMENT_APG"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_APG_PLUS_COMPLEMENT"), getStyleListRightNone());
                createRow();
            }
            for (ServiceMilitaire sm : trav.getListeSM()) {
                compteur++;
                createCell(sm.getPeriode().getDateDebut().toString(), getStyleListRightNone());
                createCell(sm.getPeriode().getDateFin().toString(), getStyleListRightNone());
                createCell(sm.getMontantBrut(), getStyleMontantNone());
                createCell(getSession().getCodeLibelle(sm.getBeneficiaire().getValue()), getStyleListRightNone());
                createCell(sm.getDateVersementComptable().toString(), getStyleListRightNone());
                createCell(getSession().getCodeLibelle(sm.getTypePrestation()), getStyleListRightNone());
                createCell(sm.getVersementAPG(), getStyleMontantNone());
                createCell(sm.getVersementAPG().add(sm.getMontantBrut()), getStyleMontantNone());
                createRow();
                totalCOMPL = totalCOMPL.add(sm.getMontantBrut());
                totalAPG = totalAPG.add(sm.getVersementAPG());
                if (compteur == trav.getListeSM().size()) {
                    createCell("");
                    createCell(getSession().getLabel("LISTE_REVISION_TOTAL"), getStyleGras());
                    createCell(totalCOMPL, getStyleMontantTotal());
                    createCell("");
                    createCell("");
                    createCell("");
                    createCell(totalAPG, getStyleMontantTotal());
                    createCell(totalCOMPL.add(totalAPG), getStyleMontantTotal());
                    createRow();
                }
            }
            compteur = 0;

            if (trav.getListePrestationsAF().size() > 0) {
                createRow();
                createCell(getSession().getLabel("LISTE_REVISION_AF"), getStyleGras());
                createRow();
                createCell(getSession().getLabel("LISTE_REVISION_DATE_DEBUT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_FIN"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_MONTANT"), getStyleListRightNone());
                createCell(getSession().getLabel("LISTE_REVISION_DATE_VERSEMENT"), getStyleListRightNone());
                createRow();
            }
            Montant totalAF = Montant.ZERO;
            for (PrestationsAFPourRevision prestAF : trav.getListePrestationsAF()) {
                compteur++;
                createCell(prestAF.getDateDebut().toString(), getStyleListRightNone());
                createCell(prestAF.getDateFin().toString(), getStyleListRightNone());
                createCell(prestAF.getMontant(), getStyleMontantNone());
                createCell(prestAF.getDateVersement(), getStyleListRightNone());
                createRow();
                totalAF = totalAF.add(prestAF.getMontant());
                if (compteur == trav.getListePrestationsAF().size()) {
                    createCell("");
                    createCell(getSession().getLabel("LISTE_REVISION_TOTAL"), getStyleGras());
                    createCell(totalAF, getStyleMontantTotal());
                    createRow();
                }
            }

            createRow();

            createInformationsPoste(trav);

        }
    }

    private void renseigneDateFacturation(DecompteSalaire decompteSalaire) {
        if (!JadeStringUtil.isEmpty(decompteSalaire.getDecompte().getIdPassageFacturation())) {
            if (decompteSalaire.getDecompte().getIdPassageFacturation().equals("0")) {
                // cas de reprise
                List<HistoriqueDecompte> historique = VulpeculaRepositoryLocator.getHistoriqueDecompteRepository()
                        .findLastHistoriqueDecompte(decompteSalaire.getDecompte().getId(),
                                EtatDecompte.COMPTABILISE.getValue());
                if (historique.size() > 0) {
                    HistoriqueDecompte histo = historique.get(0);
                    createCell(histo.getDateAsSwissValue(), getStyleListRightNone());
                }
            } else {
                Passage passage = VulpeculaServiceLocator.getPassageService().findById(
                        decompteSalaire.getDecompte().getIdPassageFacturation());
                if (passage != null) {
                    createCell(passage.getDateFacturation(), getStyleListRightNone());
                }
            }
        }
    }

    private boolean existeAuMoinsUnDecompte(TravailleurPourRevision trav) {
        return trav.getDecomptesSalairePR().size() > 0 || trav.getDecomptesSalaireSP().size() > 0
                || trav.getDecomptesSalaireCP().size() > 0 || trav.getDecomptesSalaireCT().size() > 0;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_REVISION_TYPE_NUMBER;
    }

    @Override
    public String getListName() {
        return getSession().getLabel("LISTE_TRAVAILLEUR_REVISION");
    }

    public void setListeTravailleur(TreeMap<String, TravailleurPourRevision> listeTravailleur) {
        this.listeTravailleur = listeTravailleur;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getAnnee() {
        return annee;
    }

    //    private Double getTauxSansAC2ToPrint(PosteTravail poste, Date dateFinDecompte) {
    //        List<AdhesionCotisationPosteTravail> adhesions = new ArrayList<AdhesionCotisationPosteTravail>();
    //        Taux taux = new Taux(0);
    //
    //        List<AdhesionCotisationPosteTravail> adhesionsAvecAC2 = VulpeculaRepositoryLocator.getAdhesionCotisationPosteRepository().findByIdPosteTravail(poste.getId(), dateFinDecompte);
    //        for(AdhesionCotisationPosteTravail adhesion : adhesionsAvecAC2) {
    //            if(!TypeAssurance.COTISATION_AC2.equals(adhesion.getTypeAssurance())) {
    //                adhesions.add(adhesion);
    //            }
    //        } 
    //
    //        for(AdhesionCotisationPosteTravail adhesion : adhesions) {
    //            Taux retrieved = VulpeculaServiceLocator.getCotisationService().findTaux(adhesion.getIdCotisation(), dateFinDecompte);
    //            taux = taux.addTaux(retrieved);
    //        }
    //
    //        return taux.getBigDecimal().doubleValue();       
    //    }

}
