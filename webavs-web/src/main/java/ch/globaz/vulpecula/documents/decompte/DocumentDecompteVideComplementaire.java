package ch.globaz.vulpecula.documents.decompte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

/**
 * Création du document décompte vide pour envoi, lors de la création de décomptes vides.
 * 
 * @since WebBMS 1.0
 */
public class DocumentDecompteVideComplementaire extends DocumentDecompteVide {
    private static final long serialVersionUID = 2002626138123336066L;

    private static final String NO_POSTE_TRAVAIL = "COL_1";
    private static final String NOM_PRENOM = "COL_2";
    private static final String DATE_NAISSANCE = "COL_3";
    private static final String CODE_PROF = "COL_4";
    private static final String VACANCES = "COL_5";
    private static final String GRATIFICATION = "COL_6";
    private static final String AJ = "COL_7";
    private static final String TOTAL_COL123 = "COL_8";
    private static final String TAUX_COL123 = "COL_9";
    private static final String APG_SM = "COL_10";
    private static final String TAUX = "COL_11";

    private static final int NB_LINE_MAX = 21;

    /**
     * Constructeur
     */
    public DocumentDecompteVideComplementaire() throws Exception {
        super();
    }

    /**
     * Constructeur, on l'on passe les décomptes que l'on désire imprimer
     * 
     * @param decomptes List<Decompte>
     * @throws Exception
     */
    public DocumentDecompteVideComplementaire(final DecompteContainer decompteContainer) throws Exception {
        super(decompteContainer, DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_CT_NAME,
                DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_TYPE_NUMBER);
    }

    @Override
    protected void fillDataSourceSalaire() {
        Collection<Map<String, Object>> collection = new ArrayList<Map<String, Object>>();

        Decompte decompte = getCurrentElement().getDecompte();
        int linesNumber = decompte.getLignes().size();
        for (DecompteSalaire decompteSalaire : decompte.getLignes()) {
            Map<String, Object> ligne = new HashMap<String, Object>();
            ligne.put(NO_POSTE_TRAVAIL, decompteSalaire.getIdPosteTravail());
            ligne.put(NOM_PRENOM, decompteSalaire.getNomPrenomTravailleur());
            ligne.put(DATE_NAISSANCE, decompteSalaire.getDateNaissanceTravailleur());
            ligne.put(CODE_PROF, getCode(decompteSalaire.getQualificationPoste()));
            ligne.put(VACANCES, "");
            ligne.put(GRATIFICATION, "");
            ligne.put(AJ, "");
            ligne.put(TOTAL_COL123, "");
            ligne.put(TAUX_COL123, decompteSalaire.getTauxContribuableForCaissesSocialesAsValue());
            ligne.put(APG_SM, "");
            ligne.put(TAUX, "0.00");
            collection.add(ligne);
        }

        int lignesARajouter = linesToAdd(linesNumber, NB_LINE_MAX);

        // On complète la dernière page avec des lignes vides
        for (int i = 0; i < lignesARajouter; i++) {
            Map<String, Object> ligne = new HashMap<String, Object>();
            ligne.put(NO_POSTE_TRAVAIL, "");
            ligne.put(NOM_PRENOM, "");
            ligne.put(DATE_NAISSANCE, "");
            ligne.put(CODE_PROF, "");
            ligne.put(VACANCES, "");
            ligne.put(GRATIFICATION, "");
            ligne.put(AJ, "");
            ligne.put(TAUX_COL123, "");
            ligne.put(TOTAL_COL123, "");
            ligne.put(APG_SM, "");
            ligne.put(TAUX, "");
            collection.add(ligne);
        }

        setDataSource(collection);
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_SUBJECT;
    }

    /**
     * définit la template a utiliser
     */
    @Override
    public String getJasperTemplate() {
        return DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_TEMPLATE;
    }

    @Override
    public String getNomDocumentForCataloguesTextes() {
        return DocumentConstants.DECOMPTE_VIDE_COMPLEMENTAIRE_CT_NAME;
    }
}
