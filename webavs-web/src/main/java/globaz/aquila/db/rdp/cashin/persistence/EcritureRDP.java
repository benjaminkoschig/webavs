package globaz.aquila.db.rdp.cashin.persistence;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import java.util.Arrays;
import java.util.List;

public class EcritureRDP extends BEntity {
    private static final long serialVersionUID = -2974707792856978574L;

    private static final String SECTEUR_AVS = "2";
    private static final String SECTEUR_FRAIS_ADMIN = "9";

    public static final List<String> SECTEURS_AVS = Arrays.asList(SECTEUR_AVS, SECTEUR_FRAIS_ADMIN);

    private String noSection;
    private String noRubrique;
    private String montant;
    private String noRubriqueCompteCourant;
    private String idTypeSection;

    public String getNoSection() {
        return noSection;
    }

    public void setNoSection(String noSection) {
        this.noSection = noSection;
    }

    public String getNoRubrique() {
        return noRubrique;
    }

    public void setNoRubrique(String noRubrique) {
        this.noRubrique = noRubrique;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getNoRubriqueCompteCourant() {
        return noRubriqueCompteCourant;
    }

    public void setNoRubriqueCompteCourant(String noRubriqueCompteCourant) {
        this.noRubriqueCompteCourant = noRubriqueCompteCourant;
    }

    public String getIdTypeSection() {
        return idTypeSection;
    }

    public void setIdTypeSection(String idTypeSection) {
        this.idTypeSection = idTypeSection;
    }

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        montant = statement.dbReadNumeric("MONTANT");
        noSection = statement.dbReadString("NOSECTION");
        noRubrique = statement.dbReadString("NORUBRIQUE");
        noRubriqueCompteCourant = statement.dbReadString("NORUBRIQUECC");
        idTypeSection = statement.dbReadNumeric("IDTYPESECTION");
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
    }
}
