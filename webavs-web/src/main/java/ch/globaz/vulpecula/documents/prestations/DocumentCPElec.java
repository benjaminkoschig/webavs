package ch.globaz.vulpecula.documents.prestations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentDomaine;
import ch.globaz.vulpecula.documents.catalog.DocumentType;
import ch.globaz.vulpecula.domain.models.congepaye.CPParEmployeur;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.models.congepaye.CongesPayes;
import globaz.globall.util.JANumberFormatter;

public class DocumentCPElec extends DocumentPrestations<CPParEmployeur> {

    private static final String CAISSE_METIER = "caisseMetier";

    private static final long serialVersionUID = 1L;

    private static final String F_NOM_PRENOM = "F_NOM_PRENOM";
    private static final String F_PERIODE = "F_PERIODE";
    private static final String F_SALAIRE = "F_SALAIRE";
    private static final String F_V = "F_V";
    private static final String P_TITRE = "P_TITRE";
    private static final String F_MONTANT_VERSE = "F_MONTANT_VERSE";
    private static final String F_AVS = "F_AVS";
    private static final String F_AC = "F_AC";
    private static final String F_LPP = "F_LPP";
    private static final String F_CM = "F_CM";
    private static final String F_MONTANT_BRUT = "F_MONTANT_BRUT";
    private static final String F_RET = "F_RET";
    private static final String F_FCFP = "F_FCFP";
    private static final String F_AF = "F_AF";

    private static final String P_LAST_NOM_PRENOM = "P_LAST_NOM_PRENOM";
    private static final String P_LAST_PERIODE = "P_LAST_PERIODE";
    private static final String P_LAST_SALAIRE = "P_LAST_SALAIRE";
    private static final String P_LAST_V = "P_LAST_V";
    private static final String P_LAST_MONTANT_VERSE = "P_LAST_MONTANT_VERSE";
    private static final String P_LAST_AVS = "P_LAST_AVS";
    private static final String P_LAST_AC = "P_LAST_AC";
    private static final String P_LAST_LPP = "P_LAST_LPP";
    private static final String P_LAST_CM = "P_LAST_CM";
    private static final String P_LAST_MONTANT_BRUT = "P_LAST_MONTANT_BRUT";
    private static final String P_LAST_RET = "P_LAST_RET";
    private static final String P_LAST_FCFP = "P_LAST_FCFP";
    private static final String P_LAST_AF = "P_LAST_AF";

    private static final String P_SOMME_MONTANTS_VERSES = "P_SOMME_MONTANTS_VERSES";
    private static final String P_P1 = "P_P1";
    private static final String P_NOM_PRENOM = "P_NOM_PRENOM";
    private static final String P_V = "P_V";
    private static final String P_PERIODE = "P_PERIODE";
    private static final String P_SALAIRE = "P_SALAIRE";
    private static final String P_MONTANT_VERSE = "P_MONTANT_VERSE";
    private static final String P_MONTANT_BRUT = "P_MONTANT_BRUT";
    private static final String P_AVS = "P_AVS";
    private static final String P_AC = "P_AC";
    private static final String P_LPP = "P_LPP";
    private static final String P_CM = "P_CM";
    private static final String P_RET = "P_RET";
    private static final String P_FCFP = "P_FCFP";
    private static final String P_AF = "P_AF";
    private static final String P_SIGNATURE = "P_SIGNATURE";

    public DocumentCPElec() throws Exception {
        this(null);
    }

    public DocumentCPElec(CPParEmployeur cpParEmployeur) throws Exception {
        super(cpParEmployeur, DocumentConstants.CONGES_PAYES_NAME, DocumentConstants.CONGES_PAYES_TYPE_NUMBER);
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.CONGES_PAYES_ELEC_TEMPLATE;
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
        return DocumentConstants.CONGES_PAYES_NAME;
    }

    @Override
    public void fillFields() throws Exception {
        setDocumentTitle(getCurrentElement().getEmployeur().getConvention().getCode() + "-"
                + getCurrentElement().getEmployeur().getAffilieNumero());
        CongesPayes congesPayes = getCurrentElement().getCongesPayes();
        Collection<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();

        int compteur = congesPayes.size();
        for (CongePaye congePaye : congesPayes) {
            if (--compteur == 0) {
                // Dernière ligne doit être avec le totale
                setParametres(P_LAST_NOM_PRENOM, congePaye.getPosteTravail().getNomPrenomTravailleur());
                setParametres(P_LAST_V, getCode(congePaye.getBeneficiaire().getValue()));
                setParametres(P_LAST_PERIODE,
                        congePaye.getAnneeDebut().getValue() + " - " + congePaye.getAnneeFin().getValue());
                setParametres(P_LAST_SALAIRE, JANumberFormatter.format(congePaye.getSalaires().getValue()));
                setParametres(P_LAST_MONTANT_BRUT, JANumberFormatter.format(congePaye.getMontantBrut().getValue()));
                setParametres(P_LAST_AVS, JANumberFormatter.format(congePaye.getMontantAVS().getValue()));
                setParametres(P_LAST_AC, JANumberFormatter.format(congePaye.getMontantAC().getValue()));
                setParametres(P_LAST_RET, JANumberFormatter.format(congePaye.getMontantRetaval().getValue()));
                setParametres(P_LAST_CM, JANumberFormatter.format(congePaye.getMontantMal().getValue()));
                setParametres(P_LAST_LPP, JANumberFormatter.format(congePaye.getMontantLPP().getValue()));
                setParametres(P_LAST_FCFP, JANumberFormatter.format(congePaye.getMontantFCFP().getValue()));
                setParametres(P_LAST_AF, JANumberFormatter.format(congePaye.getMontantAF().getValue()));
                setParametres(P_LAST_MONTANT_VERSE, JANumberFormatter.format(congePaye.getMontantNet().getValue()));
            } else {
                Map<String, Object> ligne = new HashMap<String, Object>();
                ligne.put(F_NOM_PRENOM, congePaye.getPosteTravail().getNomPrenomTravailleur());
                ligne.put(F_V, getCode(congePaye.getBeneficiaire().getValue()));
                ligne.put(F_PERIODE, congePaye.getAnneeDebut().getValue() + " - " + congePaye.getAnneeFin().getValue());
                ligne.put(F_SALAIRE, JANumberFormatter.format(congePaye.getSalaires().getValue()));
                ligne.put(F_MONTANT_BRUT, JANumberFormatter.format(congePaye.getMontantBrut().getValue()));
                ligne.put(F_AVS, JANumberFormatter.format(congePaye.getMontantAVS().getValue()));
                ligne.put(F_AC, JANumberFormatter.format(congePaye.getMontantAC().getValue()));
                ligne.put(F_RET, JANumberFormatter.format(congePaye.getMontantRetaval().getValue()));
                ligne.put(F_CM, JANumberFormatter.format(congePaye.getMontantMal().getValue()));
                ligne.put(F_LPP, JANumberFormatter.format(congePaye.getMontantLPP().getValue()));
                ligne.put(F_FCFP, JANumberFormatter.format(congePaye.getMontantFCFP().getValue()));
                ligne.put(F_AF, JANumberFormatter.format(congePaye.getMontantAF().getValue()));
                ligne.put(F_MONTANT_VERSE, JANumberFormatter.format(congePaye.getMontantNet().getValue()));
                collection.add(ligne);
            }
        }
        if (!collection.isEmpty()) {
            setDataSource(collection);
        }
        setParametres(P_TITRE, getTexte(1, 1));
        setParametres(P_NOM_PRENOM, getLabel("DOCUMENT_NOM_PRENOM"));
        setParametres(P_V, getLabel("DOCUMENT_VERSEMENT_ABR"));
        setParametres(P_PERIODE, getLabel("DOCUMENT_PERIODE"));
        setParametres(P_SALAIRE, getLabel("DOCUMENT_SALAIRE"));
        setParametres(P_MONTANT_VERSE, getLabel("DOCUMENT_MONT_NET"));
        setParametres(P_MONTANT_BRUT, getLabel("DOCUMENT_MONT_BRUT"));
        setParametres(P_AVS, getLabel("DOCUMENT_AVS"));
        setParametres(P_AC, getLabel("DOCUMENT_AC"));
        setParametres(P_LPP, getLabel("DOCUMENT_LPP"));
        setParametres(P_CM, getLabel("DOCUMENT_CM"));
        setParametres(P_FCFP, getLabel("DOCUMENT_FCFP"));
        setParametres(P_RET, getLabel("DOCUMENT_RET"));
        setParametres(P_AF, getLabel("DOCUMENT_AF"));
        setParametres(P_SOMME_MONTANTS_VERSES, JANumberFormatter.format(congesPayes.getSommeNets().getValue()));
        setParametres(P_P1, getTexte(3, 1));
        setSignature();
    }

    private void setSignature() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(CAISSE_METIER, getCurrentElement().getEmployeur().getConvention().getDesignation());
        setParametres(P_SIGNATURE, getTexteFormatte(9, 1, parametres));
    }
}
