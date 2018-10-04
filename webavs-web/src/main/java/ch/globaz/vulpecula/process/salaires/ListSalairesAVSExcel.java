package ch.globaz.vulpecula.process.salaires;

import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import ch.globaz.utils.Pair;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.decompte.DecompteSalaireParTravailleur;
import ch.globaz.vulpecula.external.api.poi.AbstractListExcel;
import ch.globaz.vulpecula.external.models.pyxis.CodeLangue;

public class ListSalairesAVSExcel extends AbstractListExcel {
    private static final String SHEET_TITLE = "SALARY";
    private String typesdecomptes;
    private Employeur employeur;
    private int annee;

    private Map<Pair<String, Annee>, DecompteSalaireParTravailleur> listTravailleurSalairesAVS;

    private final int COL_NOM_PRENOM = 0;
    private final int COL_NUM_NSS = 1;
    private final int COL_MOIS_TRAVAILLES = 2;
    private final int COL_ANNEE = 3;
    private final int COL_SALAIRE_AVS = 4;
    private final int COL_SALAIRE_AC1 = 5;
    private final int COL_SALAIRE_AC2 = 6;

    private static final String EMPTY_CRITERE = "-";
    private static final String EMPTY_CELL = "";

    private CodeLangue getCodeLangue() {
        return CodeLangue.fromValue(employeur.getLangue());
    }

    private String getLangueISO() {
        return getCodeLangue().getCodeIsoLangue();
    }

    private String getTradLabel(String codeLabel) {
        String label = getLabel(codeLabel);
        try {
            label = getSession().getApplication().getLabel(codeLabel, getLangueISO());
        } catch (Exception e) {
            JadeLogger.error(e, e.getMessage());
        }
        return label;
    }

    public ListSalairesAVSExcel(BSession session, String filenameRoot, String documentTitle) {
        super(session, filenameRoot, documentTitle);
        HSSFSheet sheet = createSheet(SHEET_TITLE);
        sheet.setColumnWidth((short) COL_NOM_PRENOM, (short) 8000);
        sheet.setColumnWidth((short) COL_NUM_NSS, AbstractListExcel.COLUMN_WIDTH_5500);
        sheet.setColumnWidth((short) COL_MOIS_TRAVAILLES, (short) 7500);
        sheet.setColumnWidth((short) COL_ANNEE, (short) 2500);
        sheet.setColumnWidth((short) COL_SALAIRE_AVS, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_SALAIRE_AC1, AbstractListExcel.COLUMN_WIDTH_MONTANT);
        sheet.setColumnWidth((short) COL_SALAIRE_AC2, AbstractListExcel.COLUMN_WIDTH_MONTANT);
    }

    @Override
    public void createContent() {
        initPage(true);
        createTitle();
        createRow();
        createCriteres();
        createRow(2);
        createTable();
    }

    private void createTitle() {
        createRow();
        HSSFCellStyle style = getStyleCritereTitle();
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        createCell(getTradLabel("LISTE_EMPLOYEUR"), style);

        createCell(employeur.getAffilieNumero() + "\n" + employeur.getAdressePrincipaleFormatee(), getStyleCritere());
        createRow();
    }

    private void createCriteres() {
        createRow();
        createCell(getTradLabel("LISTE_TYPE_DECOMPTE"), getStyleCritereTitle());

        if (typesdecomptes != null) {
            createCell(typesdecomptes, getStyleCritere());
        } else {
            createCell(EMPTY_CRITERE);
        }
        createRow();
        createCell(getTradLabel("LISTE_SALAIRES_AVS_ANNEE_COMPTABLE"), getStyleCritereTitle());

        if (annee != 0) {
            createCell(annee, getStyleCritere());
        } else {
            createCell(EMPTY_CRITERE);
        }
    }

    private void createTable() {
        createCell(getTradLabel("LISTE_SALAIRES_AVS_NOM_PRENOM"), getStyleListGris25PourcentGras());
        createCell(getTradLabel("LISTE_SALAIRES_AVS_NUM_NSS"), getStyleListGris25PourcentGras());
        createCell(getTradLabel("LISTE_SALAIRES_AVS_MOIS_TRAVAILLES"), getStyleListGris25PourcentGras());
        createCell(getTradLabel("LISTE_SALAIRES_AVS_ANNEE"), getStyleListGris25PourcentGras());
        createCell(getTradLabel("LISTE_SALAIRES_AVS_SALAIRE_AVS"), getStyleListGris25PourcentGras());
        createCell(getTradLabel("LISTE_SALAIRES_AVS_SALAIRE_AC1"), getStyleListGris25PourcentGras());
        createCell(getTradLabel("LISTE_SALAIRES_AVS_SALAIRE_AC2"), getStyleListGris25PourcentGras());
        Map<Integer, Montant> totauxAnnee = new HashMap<Integer, Montant>();
        Montant totalEntreprise = new Montant(0);
        Montant totalEntrepriseAC1 = new Montant(0);
        Montant totalEntrepriseAC2 = new Montant(0);
        for (Map.Entry<Pair<String, Annee>, DecompteSalaireParTravailleur> travailleurSalaires : listTravailleurSalairesAVS
                .entrySet()) {

            DecompteSalaireParTravailleur trav = travailleurSalaires.getValue();

            createRow();
            createCell(trav.getTravailleur().getNomPrenomTravailleur(), getStyleListRightNone());
            createCell(trav.getTravailleur().getNumAvsActuel(), getStyleListRightNone());
            Montant totalSalaire = new Montant(0);
            Montant totalSalaireAC1 = new Montant(0);
            Montant totalSalaireAC2 = new Montant(0);
            Set<Integer> moisTravaille = new TreeSet<Integer>();
            for (DecompteSalaire decompteSalaire : trav.getDecomptesSalaire()) {
                if (decompteSalaire.getDecompte().getEtat().equals(EtatDecompte.COMPTABILISE)) {
                    totalSalaire = totalSalaire.add(decompteSalaire.getMasseAVS());
                    totalSalaireAC1 = totalSalaireAC1.add(decompteSalaire.getMasseAC());
                    totalSalaireAC2 = totalSalaireAC2.add(decompteSalaire.getMasseAC2());

                    int periodeDebut = Integer.parseInt(decompteSalaire.getPeriodeDebut().getMois());
                    int periodeFin = Integer.parseInt(decompteSalaire.getPeriodeFin().getMois());

                    if (decompteSalaire.getTypeDecompte().equals(TypeDecompte.PERIODIQUE)) {
                        for (int i = periodeDebut; i <= periodeFin; i++) {
                            moisTravaille.add(i);
                        }
                    }
                }
            }

            for (AbsenceJustifiee absenceJustifiee : trav.getAbsenceJustifiees()) {
                totalSalaire = totalSalaire.add(absenceJustifiee.getMasseAVS());
                totalSalaireAC1 = totalSalaireAC1.add(absenceJustifiee.getMasseAC());
            }

            for (CongePaye congePaye : trav.getCongePayes()) {
                totalSalaire = totalSalaire.add(congePaye.getMasseAVS());
                totalSalaireAC1 = totalSalaireAC1.add(congePaye.getMasseAC());
            }

            String strMois = Arrays.toString(moisTravaille.toArray());
            createCell(strMois.substring(1, (strMois.length() - 1)), getStyleListRightNone());
            createCell(trav.getAnnee().getValue());

            createCell(totalSalaire, getStyleMontantNone());

            createCell(totalSalaireAC1, getStyleMontantNone());
            createCell(totalSalaireAC2, getStyleMontantNone());

            Montant totalAnnee;
            if (totauxAnnee.containsKey(trav.getAnnee().getValue())) {
                totalAnnee = totauxAnnee.get(trav.getAnnee().getValue());
            } else {
                totalAnnee = new Montant(0);
            }
            totalAnnee = totalAnnee.add(totalSalaire);
            totauxAnnee.put(trav.getAnnee().getValue(), totalAnnee);
            totalEntreprise = totalEntreprise.add(totalSalaire);
            totalEntrepriseAC1 = totalEntrepriseAC1.add(totalSalaireAC1);
            totalEntrepriseAC2 = totalEntrepriseAC2.add(totalSalaireAC2);
        }
        createRow();

        for (int annee : convertAndSort(totauxAnnee)) {
            createRow();
            createCell(EMPTY_CELL);
            createCell(EMPTY_CELL);
            createMergedRegion(2, getTradLabel("LISTE_SALAIRES_AVS_SALAIRE_TOTAL_ANNEE") + " " + annee,
                    getStyleCritereTitle());
            createCell(totauxAnnee.get(annee), getStyleMontantNone());
        }
        createRow();
        createCell(EMPTY_CELL);
        createCell(EMPTY_CELL);
        createMergedRegion(2, getTradLabel("LISTE_SALAIRES_AVS_SALAIRE_TOTAL_ENTREPRISE"), getStyleCritereTitle());
        createCell(totalEntreprise, getStyleMontantNone());
        createCell(totalEntrepriseAC1, getStyleMontantNone());
        createCell(totalEntrepriseAC2, getStyleMontantNone());
    }

    private List<Integer> convertAndSort(Map<Integer, Montant> totauxAnnee) {
        List<Integer> annees = new ArrayList<Integer>(totauxAnnee.keySet());
        Collections.sort(annees);
        return annees;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.LISTES_SALAIRES_AVS_TYPE_NUMBER;
    }

    public String getListName() {
        return getTradLabel("LISTE_SALAIRES_AVS_TITLE");
    }

    public void setEmployeur(Employeur findByIdAffilie) {
        employeur = findByIdAffilie;
        loadAdresse();
    }

    private void loadAdresse() {
        if (employeur.getAdressePrincipale() == null) {
            employeur.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdressePrioriteCourrierByIdTiers(employeur.getIdTiers()));
        }
    }

    public String getAnnee() {
        return String.valueOf(annee);
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public void setTypesdecomptes(String typesdecomptes) {
        this.typesdecomptes = typesdecomptes;
    }

    public void setSalairesAVS(Map<Pair<String, Annee>, DecompteSalaireParTravailleur> listTravilleurSalairesAVS) {
        listTravailleurSalairesAVS = listTravilleurSalairesAVS;
    }
}
