package globaz.corvus.excel;

import globaz.corvus.utils.RETiersForJspUtils;
import globaz.globall.db.BSession;
import globaz.op.common.merge.IMergingContainer;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.List;
import ch.globaz.corvus.process.attestationsfiscales.REAttestationsFiscalesUtils;
import ch.globaz.corvus.process.attestationsfiscales.REFamillePourAttestationsFiscales;
import ch.globaz.corvus.process.attestationsfiscales.RETiersPourAttestationsFiscales;
import ch.globaz.utils.excel.ExcelAbstractDocumentGenerator;

public class REListeExcelAttestationsFiscalesNonSorties extends ExcelAbstractDocumentGenerator {

    private String annee;
    private int anneeAsInteger;
    private List<REFamillePourAttestationsFiscales> familles;
    private BSession session;

    public REListeExcelAttestationsFiscalesNonSorties(BSession session, String annee,
            List<REFamillePourAttestationsFiscales> familles) {
        super();

        this.session = session;
        this.annee = annee;
        anneeAsInteger = Integer.parseInt(annee);
        this.familles = familles;
    }

    public String getAnnee() {
        return annee;
    }

    private String getDescriptionTiers(REFamillePourAttestationsFiscales uneFamille) {
        StringBuilder description = new StringBuilder();

        if (uneFamille.getTiersRequerant() == null) {
            return "Tiers null";
        }

        RETiersPourAttestationsFiscales tiers = uneFamille.getTiersRequerant();

        description.append(tiers.getNumeroAvs()).append(" / ");
        description.append(tiers.getNom()).append(" ").append(tiers.getPrenom()).append(" / ");
        description.append(tiers.getDateNaissance()).append(" / ");
        description.append(RETiersForJspUtils.getInstance(getSession()).getLibelleCourtSexe(tiers.getCsSexe()));

        return description.toString();
    }

    public List<REFamillePourAttestationsFiscales> getFamilles() {
        return familles;
    }

    @Override
    public String getModelPath() {
        return "corvus/excelml/attestations_fiscales_non_sorties.xml";
    }

    @Override
    public String getOutputName() {
        return "AttestationFiscalesNonSorties";
    }

    public BSession getSession() {
        return session;
    }

    private String hasImpotSource(REFamillePourAttestationsFiscales uneFamille) {
        if (REAttestationsFiscalesUtils.hasImpotSourceVerseeDansAnnee(uneFamille, annee)) {
            return "X";
        }
        return "";
    }

    private String hasNotAdresse(REFamillePourAttestationsFiscales uneFamille) {
        if (REAttestationsFiscalesUtils.getTiersCorrespondanceAvecAdresseValide(uneFamille, annee) == null) {
            return "X";
        }
        return "";
    }

    private String hasPersonneDecedeeDurantAnneeFiscale(REFamillePourAttestationsFiscales uneFamille) {
        if (REAttestationsFiscalesUtils.hasPersonneDecedeeDurantAnneeFiscale(uneFamille, annee)) {
            return "X";
        }
        return "";
    }

    private String hasRenteBloquee(REFamillePourAttestationsFiscales uneFamille) {
        if (REAttestationsFiscalesUtils.hasRenteBloquee(uneFamille, annee)) {
            return "X";
        }
        return "";
    }

    private String hasRenteTerminantDansAnnee(REFamillePourAttestationsFiscales uneFamille) {
        if (REAttestationsFiscalesUtils.hasRenteFinissantDansAnnee(uneFamille, annee)) {
            return "X";
        }
        return "";
    }

    private String hasRetroactif(REFamillePourAttestationsFiscales uneFamille) {
        if (REAttestationsFiscalesUtils.hasSeulementDecisionEnDecembre(uneFamille, anneeAsInteger)) {
            return "X";
        }
        return "";
    }

    @Override
    public IMergingContainer loadData() throws Exception {
        CommonExcelmlContainer data = new CommonExcelmlContainer();

        remplirValeursGlobale(data);

        for (REFamillePourAttestationsFiscales uneFamille : familles) {
            data.put("description_tiers", getDescriptionTiers(uneFamille));
            data.put("has_not_adresse", hasNotAdresse(uneFamille));
            data.put("has_rente_bloquee", hasRenteBloquee(uneFamille));
            data.put("has_impot_source", hasImpotSource(uneFamille));
            data.put("has_rente_fin_dans_annee", hasRenteTerminantDansAnnee(uneFamille));
            data.put("has_deces_dans_annee", hasPersonneDecedeeDurantAnneeFiscale(uneFamille));
            data.put("has_retroactif", hasRetroactif(uneFamille));
        }

        return data;
    }

    private void remplirValeursGlobale(CommonExcelmlContainer data) {
        data.put("label_description_tiers", session.getLabel("EXCEL_ATTESTATION_FISCALES_DESCRIPTION_TIERS"));

        data.put("label_raison", session.getLabel("EXCEL_ATTESTATION_FISCALES_RAISON"));
        data.put("label_adresse", session.getLabel("EXCEL_ATTESTATION_FISCALES_SANS_ADRESSE"));
        data.put("label_rente_bloquee", session.getLabel("EXCEL_ATTESTATION_FISCALES_RENTE_BLOQUEE"));
        data.put("label_impot_source", session.getLabel("EXCEL_ATTESTATION_FISCALES_IMPOT_SOURCE"));
        data.put("label_rente_fin_dans_annee",
                session.getLabel("EXCEL_ATTESTATION_FISCALES_RENTE_FIN_DROIT_ANNEE_FISCALE"));
        data.put("label_deces_dans_annee", session.getLabel("EXCEL_ATTESTATION_FISCALES_DECES_DANS_ANNEE_FISCALE"));
        data.put("label_retroactif", session.getLabel("EXCEL_ATTESTATION_FISCALES_RETROACTIF"));
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setFamilles(List<REFamillePourAttestationsFiscales> familles) {
        this.familles = familles;
    }

    public void setSession(BSession session) {
        this.session = session;
    }
}
