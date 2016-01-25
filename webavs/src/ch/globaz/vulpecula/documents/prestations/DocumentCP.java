package ch.globaz.vulpecula.documents.prestations;

import globaz.globall.util.JANumberFormatter;
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

public class DocumentCP extends DocumentPrestations<CPParEmployeur> {

    private static final String CAISSE_METIER = "caisseMetier";

    private static final long serialVersionUID = 1L;

    private static final String F_NOM_PRENOM = "F_NOM_PRENOM";
    private static final String F_TAUX = "F_TAUX";
    private static final String F_PERIODE = "F_PERIODE";
    private static final String F_SALAIRE = "F_SALAIRE";
    private static final String F_V = "F_V";
    private static final String P_TITRE = "P_TITRE";
    private static final String F_MONTANT_VERSE = "F_MONTANT_VERSE";
    private static final String P_P1 = "P_P1";
    private static final String F_VERSEMENT_ABR = "F_VERSEMENT_ABR";
    private static final String P_SIGNATURE = "P_SIGNATURE";

    private static final String P_SOMME_MONTANTS_VERSES = "P_SOMME_MONTANTS_VERSES";
    private static final Object P_NOM_PRENOM = "P_NOM_PRENOM";
    private static final Object P_V = "P_V";
    private static final Object P_PERIODE = "P_PERIODE";
    private static final Object P_SALAIRE = "P_SALAIRE";
    private static final Object P_TAUX = "P_TAUX";
    private static final Object P_MONTANT_VERSE = "P_MONTANT_VERSE";

    public DocumentCP() throws Exception {
        this(null);
    }

    public DocumentCP(CPParEmployeur cpParEmployeur) throws Exception {
        super(cpParEmployeur, DocumentConstants.CONGES_PAYES_NAME, DocumentConstants.CONGES_PAYES_TYPE_NUMBER);
    }

    @Override
    public String getJasperTemplate() {
        return DocumentConstants.CONGES_PAYES_TEMPLATE;
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
        CongesPayes congesPayes = getCurrentElement().getCongesPayes();

        Collection<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();

        for (CongePaye congePaye : congesPayes) {
            Map<String, Object> ligne = new HashMap<String, Object>();
            ligne.put(F_NOM_PRENOM, congePaye.getPosteTravail().getNomPrenomTravailleur());
            ligne.put(F_V, getCode(congePaye.getBeneficiaire().getValue()));
            ligne.put(F_VERSEMENT_ABR, getCode(congePaye.getBeneficiaire().getValue()));
            ligne.put(F_PERIODE, congePaye.getAnneeDebut().getValue() + " - " + congePaye.getAnneeFin().getValue());
            ligne.put(F_SALAIRE, JANumberFormatter.format(congePaye.getSalaires().getValue()));
            ligne.put(F_TAUX,
                    JANumberFormatter.format(congePaye.getTauxCP().getValue(), 0.01, 2, JANumberFormatter.NEAR));
            ligne.put(F_MONTANT_VERSE, JANumberFormatter.format(congePaye.getMontantNet().getValue()));
            collection.add(ligne);
        }
        setParametres(P_TITRE, getTexte(1, 1));
        setParametres(P_NOM_PRENOM, getLabel("DOCUMENT_NOM_PRENOM"));
        setParametres(P_V, getLabel("DOCUMENT_VERSEMENT_ABR"));
        setParametres(P_PERIODE, getLabel("DOCUMENT_PERIODE"));
        setParametres(P_SALAIRE, getLabel("DOCUMENT_SALAIRE"));
        setParametres(P_TAUX, getLabel("DOCUMENT_TAUX"));
        setParametres(P_MONTANT_VERSE, getLabel("DOCUMENT_MONTANT_VERSE"));
        setParametres(P_SOMME_MONTANTS_VERSES, JANumberFormatter.format(congesPayes.getSommeNets().getValue()));
        setParametres(P_P1, getTexte(3, 1));
        setSignature();
        setDataSource(collection);
    }

    private void setSignature() throws Exception {
        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put(CAISSE_METIER, getCurrentElement().getEmployeur().getConvention().getDesignation());
        setParametres(P_SIGNATURE, getTexteFormatte(9, 1, parametres));
    }
}
