package globaz.hercule.service.comptesIndividuels;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;

/**
 * <pre>
 * Manager permettant la récupération du nombre de CI
 * pour un affilié pour une période donnée
 * </pre>
 * 
 * @author Sullivann Corneille
 * @since 12 février 2014
 */
public class CENombreCIManager extends BManager {

    private static final long serialVersionUID = 5853452709357569634L;
    private int anneeDebut = 0;
    private int anneeFin = 0;
    private String numeroAffilie;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CENombreCI();
    }

    public void setAnneeDebut(int anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public void setAnneeFin(int anneeFin) {
        this.anneeFin = anneeFin;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    @Override
    protected String _getSql(BStatement statement) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT EC.KBNANN AS ANNEE, COUNT(*) AS NOMBRECI ");
        sql.append("FROM ").append(_getCollection()).append("CIECRIP EC ");
        sql.append("INNER JOIN ").append(_getCollection()).append("CIINDIP CI ON (CI.KAIIND = EC.KAIIND) ");
        sql.append("INNER JOIN ").append(_getCollection()).append("AFAFFIP AFF ON (EC.KBITIE = AFF.MAIAFF) ");
        sql.append("WHERE AFF.MALNAF = '").append(numeroAffilie).append("' ");
        sql.append("AND EC.KBNANN between ").append(anneeDebut).append(" AND ").append(anneeFin).append(" ");
        sql.append("GROUP BY EC.KBNANN ");

        return sql.toString();
    }

}
