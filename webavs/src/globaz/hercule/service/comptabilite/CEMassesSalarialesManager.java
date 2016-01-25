package globaz.hercule.service.comptabilite;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * <pre>
 * Manager permenttant la récupération en base de données des masses salariales
 * pour un affilié pour une période données
 * </pre>
 * 
 * @author Sullivann Corneille
 * @since 12 février 2014
 */
public class CEMassesSalarialesManager extends BManager {

    private static final long serialVersionUID = -7034289072837882275L;

    private int anneeDebut = 0;
    private int anneeFin = 0;
    private String numeroAffilie;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CEMassesSalariales();
    }

    public void setAnneeDebut(int anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public void setAnneeFin(int anneeFin) {
        this.anneeFin = anneeFin;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public int getAnneeDebut() {
        return anneeDebut;
    }

    public int getAnneeFin() {
        return anneeFin;
    }

    @Override
    protected String _getSql(BStatement statement) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ANNEE,CUMULMASSE ");
        sql.append("FROM ").append(_getCollection()).append("CACPTAP CA ");
        sql.append("INNER JOIN ").append(_getCollection())
                .append("CACPTRP CR ON (CA.IDCOMPTEANNEXE = CR.IDCOMPTEANNEXE) ");
        sql.append("INNER JOIN ").append(_getCollection()).append("CARUBRP RU ON (CR.IDRUBRIQUE = RU.IDRUBRIQUE) ");
        sql.append("WHERE CA.IDROLE IN (517039,517002) ");
        sql.append("AND CA.IDEXTERNEROLE = '").append(numeroAffilie).append("' ");
        sql.append("AND RU.IDEXTERNE like '211_.4010.%' ");
        sql.append("AND CR.ANNEE between ").append(anneeDebut).append(" AND ").append(anneeFin).append(" ");
        sql.append("ORDER BY ANNEE DESC ");

        return sql.toString();
    }

}
