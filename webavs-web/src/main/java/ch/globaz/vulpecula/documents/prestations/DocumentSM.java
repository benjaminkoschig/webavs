package ch.globaz.vulpecula.documents.prestations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.domain.models.servicemilitaire.SMParEmployeur;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServiceMilitaire;
import ch.globaz.vulpecula.domain.models.servicemilitaire.ServicesMilitaires;
import globaz.globall.util.JANumberFormatter;

public class DocumentSM extends DocumentPrestations<SMParEmployeur> {

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
    private static final String F_TAUX = "F_TAUX";
    private static final String F_PERIODE = "F_PERIODE";

    private static final String P_LAST_NOM_PRENOM = "P_LAST_NOM_PRENOM";
    private static final String P_LAST_PRESTATION = "P_LAST_PRESTATION";
    private static final String P_LAST_V = "P_LAST_V";
    private static final String P_LAST_JOURS = "P_LAST_JOURS";
    private static final String P_LAST_MONTANT_BRUT = "P_LAST_MONTANT_BRUT";
    private static final String P_LAST_AVS = "P_LAST_AVS";
    private static final String P_LAST_AC = "P_LAST_AC";
    private static final String P_LAST_MONTANT_VERSE = "P_LAST_MONTANT_VERSE";
    private static final String P_LAST_TAUX = "P_LAST_TAUX";
    private static final String P_LAST_PERIODE = "P_LAST_PERIODE";

    private static final String P_TITRE = "P_TITRE";
    private static final String P_NOM_PRENOM = "P_NOM_PRENOM";
    private static final String P_PRESTATION = "P_PRESTATION";
    private static final String P_V = "P_V";
    private static final String P_JOURS = "P_JOURS";
    private static final String P_MONTANT_BRUT = "P_MONTANT_BRUT";
    private static final String P_AVS = "P_AVS";
    private static final String P_AC = "P_AC";
    private static final String P_MONTANT_VERSE = "P_MONTANT_VERSE";
    private static final Object P_TAUX = "P_TAUX";
    private static final String P_P1 = "P_P1";
    private static final String P_PART_PATRONALE = "P_PART_PATRONALE";
    private static final String P_PERIODE = "P_PERIODE";

    private static final String P_SIGNATURE = "P_SIGNATURE";
    private static final String P_SOMME_MONTANTS_VERSES = "P_SOMME_MONTANTS_VERSES";

    public DocumentSM() throws Exception {
        this(null);
    }

    public DocumentSM(SMParEmployeur smParEmployeur) throws Exception {
        super(smParEmployeur, DocumentConstants.SERVICE_MILITAIRE_NAME,
                DocumentConstants.SERVICE_MILITAIRE_TYPE_NUMBER);
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.SERVICE_MILITAIRE_TEMPLATE;
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
        return DocumentConstants.SERVICE_MILITAIRE_NAME;
    }

    @Override
    public void fillFields() throws Exception {
        setDocumentTitle(getCurrentElement().getEmployeur().getConvention().getCode() + "-"
                + getCurrentElement().getEmployeur().getAffilieNumero());
        ServicesMilitaires servicesMilitaires = getCurrentElement().getServicesMilitaires();
        Collection<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();

        int compteur = servicesMilitaires.size();
        for (ServiceMilitaire serviceMilitaire : servicesMilitaires) {
            if (--compteur == 0) {
                // Dernière ligne doit être avec le totale
                setParametres(P_LAST_NOM_PRENOM, serviceMilitaire.getPosteTravail().getNomPrenomTravailleur());
                setParametres(P_LAST_PRESTATION, getCodeLibelle(serviceMilitaire.getTypePrestation()));
                if (serviceMilitaire.getMontantAVerser().isZero()) {
                    setParametres(P_LAST_V, "X");
                } else {
                    setParametres(P_LAST_V, getCode(serviceMilitaire.getBeneficiaire().getValue()));
                }
                setParametres(P_LAST_JOURS, String.valueOf(serviceMilitaire.getNbJours()));
                setParametres(P_LAST_TAUX, JANumberFormatter.format(serviceMilitaire.getCouvertureAPG().getValue(),
                        0.01, 2, JANumberFormatter.NEAR));
                setParametres(P_LAST_MONTANT_BRUT,
                        JANumberFormatter.format(serviceMilitaire.getMontantBrut().getValue()));
                setParametres(P_LAST_AVS, JANumberFormatter.format(serviceMilitaire.getMontantAVS().getValue()));
                setParametres(P_LAST_AC, JANumberFormatter.format(serviceMilitaire.getMontantAC().getValue()));
                setParametres(P_LAST_MONTANT_VERSE,
                        JANumberFormatter.format(serviceMilitaire.getMontantAVerser().getValue()));
                setParametres(P_LAST_PERIODE, serviceMilitaire.getPeriode().getDateDebutAsSwissValue() + " - "
                        + serviceMilitaire.getPeriode().getDateFinAsSwissValue());
            } else {
                Map<String, Object> ligne = new HashMap<String, Object>();
                ligne.put(F_NOM_PRENOM, serviceMilitaire.getPosteTravail().getNomPrenomTravailleur());
                ligne.put(F_PRESTATION, getCodeLibelle(serviceMilitaire.getTypePrestation()));
                if (serviceMilitaire.getMontantAVerser().isZero()) {
                    ligne.put(F_V, "X");
                } else {
                    ligne.put(F_V, getCode(serviceMilitaire.getBeneficiaire().getValue()));
                }
                ligne.put(F_JOURS, String.valueOf(serviceMilitaire.getNbJours()));
                ligne.put(F_TAUX, JANumberFormatter.format(serviceMilitaire.getCouvertureAPG().getValue(), 0.01, 2,
                        JANumberFormatter.NEAR));
                ligne.put(F_MONTANT_BRUT, JANumberFormatter.format(serviceMilitaire.getMontantBrut().getValue()));
                ligne.put(F_AVS, JANumberFormatter.format(serviceMilitaire.getMontantAVS().getValue()));
                ligne.put(F_AC, JANumberFormatter.format(serviceMilitaire.getMontantAC().getValue()));
                ligne.put(F_MONTANT_VERSE, JANumberFormatter.format(serviceMilitaire.getMontantAVerser().getValue()));
                ligne.put(F_PERIODE, serviceMilitaire.getPeriode().getDateDebutAsSwissValue() + " - "
                        + serviceMilitaire.getPeriode().getDateFinAsSwissValue());
                collection.add(ligne);
            }
        }
        if (!collection.isEmpty()) {
            setDataSource(collection);
        }
        setParametres(P_NOM_PRENOM, getLabel("DOCUMENT_NOM_PRENOM"));
        setParametres(P_SOMME_MONTANTS_VERSES,
                JANumberFormatter.format(servicesMilitaires.getSommeTotalVerse().getValue()));
        setParametres(P_TITRE, getTexte(1, 1));
        setParametres(P_PRESTATION, getLabel("DOCUMENT_PRESTATION"));
        setParametres(P_V, getLabel("DOCUMENT_VERSEMENT_ABR"));
        setParametres(P_JOURS, getLabel("DOCUMENT_JOURS"));
        setParametres(P_TAUX, getLabel("DOCUMENT_TAUX"));
        setParametres(P_AC, getLabel("DOCUMENT_AC"));
        setParametres(P_AVS, getLabel("DOCUMENT_AVS"));
        setParametres(P_MONTANT_BRUT, getLabel("DOCUMENT_MONT_BRUT"));
        setParametres(P_MONTANT_VERSE, getLabel("DOCUMENT_MONT_NET"));
        setParametres(P_P1, getTexte(3, 1));
        setParametres(P_PART_PATRONALE, getLabel("DOCUMENT_PART_PATRONALE"));
        setParametres(P_PERIODE, getLabel("DOCUMENT_PERIODE"));
        setSignature();
    }

    private void setSignature() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(CAISSE_METIER, getCurrentElement().getEmployeur().getConvention().getDesignation());
        setParametres(P_SIGNATURE, getTexteFormatte(9, 1, parametres));
    }
}
