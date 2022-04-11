package globaz.osiris.process.ebill;

import ch.globaz.common.exceptions.NotFoundException;

import java.util.Arrays;
import java.util.Objects;

public enum CAInscriptionEBillEnum implements AutoCloseable {
    SUBSCRIPTION_TYPE(-1, null, "SUBSCRIPTIONTYPE", "TYPE", false),
    BILLER_ID(-1, null, "BILLERID", "", false),
    RECIPIENT_ID(0, "numeroAdherent", "RECIPIENTID", "EBILL_ACCOUNT_ID", false),
    RECIPIENT_TYPE(-1, null, "RECIPIENTTYPE", "", true),
    LANGUAGE(-1, null, "LANGUAGE", "", true),
    GIVEN_NAME(1, "prenom", "GIVENNAME", "PRENOM", false),
    FAMILY_NAME(2, "nom", "FAMILYNAME", "NOM", false),
    COMPANY_NAME(3, "entreprise", "COMPANYNAME", "ENTREPRISE", false),
    ADRESSE(4, "adresse1", "ADDRESS", "ADRESSE_1", false),
    ADRESSE_2(5, "adresse2", null, "ADRESSE_2", false),
    ZIP(6, "npa", "ZIP", "NPA", false),
    CITY(7, "localite", "CITY", "LOCALITE", false),
    COUNTRY(-1, null, "COUNTRY", "PAYE", false),
    PHONE(8, "numeroTel", "PHONE", "NUM_TEL", false),
    EMAIL(9, "email", "EMAIL", "EMAIL", false),
    IDE(-1, null, "UID", "", true),
    CREDIT_ACCOUNT(13, "numAdherentBVR", "CREDITACCOUNT", "NUM_ADHERENT_BVR", false),
    CREDITOR_REFERENCE(14, "numeroRefBVR", "CREDITORREFERENCE", "NUM_REF_BVR", false),
    CUSTOMER_NBR(10, "numeroAffilie", "CUSTOMERNBR", "NUMERO_AFFILIE", false),
    BIRTHDATE(-1, null, "BIRTHDATE", "", true),
    PARITAIRE(11, "roleParitaire", "PARITAIRE", "ROLE_PARITAIRE", false),
    PERSONNEL(12, "rolePersonnel", "PERSONNEL", "ROLE_PERSONNEL", false),
    STATUS(15, "statut", null, "STATUT", false);

    private int index;
    private String titre;
    //Nom de la colonne dans le fichier csv V2
    private String colNameCsv;
    //Nom de la colonne en base de données
    private String colNameSql;
    private boolean ignored;

    CAInscriptionEBillEnum(int index, String csvV1, String csvV2, String sql, boolean ignored) {
        this.index = index;
        this.titre = csvV1;
        this.colNameCsv = csvV2;
        this.colNameSql = sql;
        this.ignored = ignored;
    }

    public String getTitre() {
        return titre;
    }

    public String getColNameCsv() {
        return colNameCsv;
    }

    public String getColNameSql() {
        return colNameSql;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public static CAInscriptionEBillEnum getInscriptionEBill(String csv) {
        return Arrays.stream(CAInscriptionEBillEnum.values()).filter(ebillEnum -> Objects.equals(ebillEnum.colNameCsv, csv)).findFirst().orElseThrow(() -> new NotFoundException(csv + " value not found"));
    }

    /**
     * Retourne le type énuméré {@link CAInscriptionEBillEnum} correspondant au code système passé en paramètre
     *
     * @param index Le code système à tester
     * @return le type énuméré {@link CAInscriptionEBillEnum} correspondant au code système passé en paramètre ou null si pas
     *         trouvé
     */
    public static CAInscriptionEBillEnum fromIndex(int index) {
        return Arrays.stream(CAInscriptionEBillEnum.values()).filter(type -> type.index == index).findFirst().orElse(null);
    }

    @Override
    public void close() throws Exception {
    }
}
