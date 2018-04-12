package ch.globaz.orion.ws.service;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import ch.globaz.orion.ws.cotisation.SalarieOrderBy;
import ch.globaz.orion.ws.enums.OrderByDirWebAvs;

public class EBSalarieManager extends BManager {
    private static final long serialVersionUID = -6951981409243701396L;
    private String forNumeroAffilie;
    private Integer forAnnee;
    private String likeNss;
    private String likeNom;
    private String likePrenom;
    private SalarieOrderBy orderBy;
    private OrderByDirWebAvs orderByDir;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new EBSalarie();
    }

    @Override
    protected String _getSql(BStatement statement) {
        String schema = _getCollection();
        StringBuilder requete = new StringBuilder("select");
        requete.append(" distinct(ci.KAIIND),ci.KANAVS as nss,ci.KALNOM as nom_Prenom,ci.KADNAI as date_Naissance, ci.KATSEX as sexe");
        requete.append(" from ").append(schema).append("CIECRIP ecr");
        requete.append(" inner join ").append(schema).append("CIINDIP ci on ci.KAIIND=ecr.KAIIND");
        requete.append(" inner join ").append(schema).append("AFAFFIP aff on aff.MAIAFF=ecr.KBITIE");
        requete.append(" where aff.MALNAF='").append(forNumeroAffilie).append("'");
        requete.append(" and (ecr.KBTGEN=310001 or (ecr.KBTGEN=310007 and ecr.KBTSPE=312003))");
        requete.append(" and ci.KAIREG=" + CICompteIndividuel.CS_REGISTRE_ASSURES);
        requete.append(" and ecr.KBNANN=");

        if (forAnnee != null && forAnnee != 0) {
            requete.append(forAnnee);
        } else {
            // Sinon on va chercher la derniere année renseignée dans les CI
            requete.append("(SELECT MAX(ecr2.KBNANN) FROM ").append(schema)
                    .append("CIECRIP ecr2 where ecr2.KBITIE = aff.maiaff");
            requete.append(" and (ecr2.KBTGEN=310001 or (ecr2.KBTGEN=310007 and ecr2.KBTSPE=312003))) ");
        }

        if (!JadeStringUtil.isEmpty(likeNss)) {
            requete.append(" and ci.KANAVS like '").append(likeNss).append("%'");
        }

        if (!JadeStringUtil.isEmpty(likeNom)) {
            requete.append(" and ci.KALNOM like '").append(likeNom).append("%,%'");
        }

        if (!JadeStringUtil.isEmpty(likePrenom)) {
            requete.append(" and ci.KALNOM like '%,").append(likePrenom).append("%'");
        }

        if (orderBy != null) {
            requete.append(" order by ci.").append(orderBy.getText());
        } else {
            requete.append(" order by ci.KALNOM");
        }

        if (orderByDir != null) {
            requete.append(" " + orderByDir.getText());
        } else {
            requete.append(" ASC");
        }

        return requete.toString();
    }

    @Override
    protected String _getSqlCount(BStatement statement) {
        String schema = _getCollection();
        StringBuilder requete = new StringBuilder("select count(distinct ci.KAIIND)");
        requete.append(" from ").append(schema).append("CIECRIP ecr");
        requete.append(" inner join ").append(schema).append("CIINDIP ci on ci.KAIIND=ecr.KAIIND");
        requete.append(" inner join ").append(schema).append("AFAFFIP aff on aff.MAIAFF=ecr.KBITIE");
        requete.append(" where aff.MALNAF='").append(forNumeroAffilie).append("'");
        requete.append(" and (ecr.KBTGEN=310001 or (ecr.KBTGEN=310007 and ecr.KBTSPE=312003))");
        requete.append(" and ci.KAIREG=" + CICompteIndividuel.CS_REGISTRE_ASSURES);
        requete.append(" and ecr.KBNANN=");

        if (forAnnee != null && forAnnee != 0) {
            requete.append(forAnnee);
        } else {
            // Sinon on va chercher la derniere année renseignée dans les CI
            requete.append("(SELECT MAX(ecr2.KBNANN) FROM ").append(schema)
                    .append("CIECRIP ecr2 where ecr2.KBITIE = aff.maiaff");
            requete.append(" and (ecr2.KBTGEN=310001 or (ecr2.KBTGEN=310007 and ecr2.KBTSPE=312003))) ");
        }

        return requete.toString();
    }

    public String getForNumeroAffilie() {
        return forNumeroAffilie;
    }

    public void setForNumeroAffilie(String forNumeroAffilie) {
        this.forNumeroAffilie = forNumeroAffilie;
    }

    public Integer getForAnnee() {
        return forAnnee;
    }

    public void setForAnnee(Integer forAnnee) {
        this.forAnnee = forAnnee;
    }

    public String getLikeNss() {
        return likeNss;
    }

    public void setLikeNss(String likeNss) {
        this.likeNss = likeNss;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

    public String getLikePrenom() {
        return likePrenom;
    }

    public void setLikePrenom(String likePrenom) {
        this.likePrenom = likePrenom;
    }

    public SalarieOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(SalarieOrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public OrderByDirWebAvs getOrderByDir() {
        return orderByDir;
    }

    public void setOrderByDir(OrderByDirWebAvs orderByDir) {
        this.orderByDir = orderByDir;
    }

}
