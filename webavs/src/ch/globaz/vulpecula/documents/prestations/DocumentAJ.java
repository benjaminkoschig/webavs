package ch.globaz.vulpecula.documents.prestations;

import globaz.globall.util.JANumberFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AJParEmployeur;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsencesJustifiees;

public class DocumentAJ extends DocumentPrestations<AJParEmployeur> {

    private static final String CAISSE_METIER = "caisseMetier";

    private static final long serialVersionUID = 1L;

    private static final String F_NOM_PRENOM = "F_NOM_PRENOM";
    private static final String F_PRESTATION = "F_PRESTATION";
    private static final String F_V = "F_V";
    private static final String F_JOURS = "F_JOURS";
    private static final String F_MONTANT_BRUT = "F_MONTANT_BRUT";
    private static final String F_AVS = "F_AVS";
    private static final String F_AC = "F_AC";
    private static final String F_MONTANT_VERSE = "F_MONTANT_VERSE";
    private static final String F_PERIODE = "F_PERIODE";

    private static final String P_TITRE = "P_TITRE";
    private static final String P_NOM_PRENOM = "P_NOM_PRENOM";
    private static final String P_PRESTATION = "P_PRESTATION";
    private static final String P_V = "P_V";
    private static final String P_JOURS = "P_JOURS";
    private static final String P_MONTANT_BRUT = "P_MONTANT_BRUT";
    private static final String P_AVS = "P_AVS";
    private static final String P_AC = "P_AC";
    private static final String P_MONTANT_VERSE = "P_MONTANT_VERSE";
    private static final String P_P1 = "P_P1";
    private static final String P_PERIODE = "P_PERIODE";
    private static final String P_PART_PATRONALE = "P_PART_PATRONALE";

    private static final String P_SIGNATURE = "P_SIGNATURE";
    private static final String P_SOMME_MONTANTS_VERSES = "P_SOMME_MONTANTS_VERSES";

    public DocumentAJ() throws Exception {
        this(null);
    }

    public DocumentAJ(AJParEmployeur ajParEmployeur) throws Exception {
        super(ajParEmployeur, DocumentConstants.ABSENCES_JUSTIFIEES_NAME,
                DocumentConstants.ABSENCES_JUSTIFIEES_TYPE_NUMBER);
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.ABSENCES_JUSTIFIEES_TEMPLATE;
    }

    @Override
    public String getDomaine() {
        return DocumentDomaine.METIER.getCsCode();
    }

    @Override
    public String getTypeDocument() {
        return DocumentType.PRESTATIONS.getCsCode();
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.ABSENCES_JUSTIFIEES_NAME;
    }

    @Override
    public void fillFields() throws Exception {
        AbsencesJustifiees absencesJustifiees = getCurrentElement().getAbsencesJustifiees();

        Collection<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();

        for (AbsenceJustifiee absenceJustifiee : absencesJustifiees) {
            Map<String, Object> ligne = new HashMap<String, Object>();
            ligne.put(F_NOM_PRENOM, absenceJustifiee.getPosteTravail().getNomPrenomTravailleur());
            ligne.put(F_PRESTATION, getCodeLibelle(absenceJustifiee.getTypePrestation()));
            ligne.put(F_V, getCode(absenceJustifiee.getBeneficiaire().getValue()));
            collection.add(ligne);
            ligne.put(F_JOURS, String.valueOf(absenceJustifiee.getNombreDeJours()));
            ligne.put(F_MONTANT_BRUT, JANumberFormatter.format(absenceJustifiee.getMontantBrut().getValue()));
            ligne.put(F_AVS, JANumberFormatter.format(absenceJustifiee.getMontantAVS().getValue()));
            ligne.put(F_AC, JANumberFormatter.format(absenceJustifiee.getMontantAC().getValue()));
            ligne.put(F_MONTANT_VERSE, JANumberFormatter.format(absenceJustifiee.getMontantVerse().getValue()));
            ligne.put(F_PERIODE, absenceJustifiee.getDateDebutAbsence() + " - " + absenceJustifiee.getDateFinAbsence());
        }
        setParametres(P_SOMME_MONTANTS_VERSES,
                JANumberFormatter.format(absencesJustifiees.sommeMontantsVerses().getValue()));
        setParametres(P_TITRE, getTexte(1, 1));
        setParametres(P_NOM_PRENOM, getLabel("DOCUMENT_NOM_PRENOM"));
        setParametres(P_PRESTATION, getLabel("DOCUMENT_PRESTATION"));
        setParametres(P_V, getLabel("DOCUMENT_VERSEMENT_ABR"));
        setParametres(P_JOURS, getLabel("DOCUMENT_JOURS"));
        setParametres(P_MONTANT_BRUT, getLabel("DOCUMENT_MONT_BRUT"));
        setParametres(P_AVS, getLabel("DOCUMENT_AVS"));
        setParametres(P_AC, getLabel("DOCUMENT_AC"));
        setParametres(P_MONTANT_VERSE, getLabel("DOCUMENT_MONT_NET"));
        setParametres(P_P1, getTexte(3, 1));
        setParametres(P_PERIODE, getLabel("DOCUMENT_PERIODE"));
        setParametres(P_PART_PATRONALE, getLabel("DOCUMENT_PART_PATRONALE"));
        setSignature();
        setDataSource(collection);
    }

    private void setSignature() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(CAISSE_METIER, getCurrentElement().getEmployeur().getConvention().getDesignation());
        setParametres(P_SIGNATURE, getTexteFormatte(9, 1, parametres));
    }
}
