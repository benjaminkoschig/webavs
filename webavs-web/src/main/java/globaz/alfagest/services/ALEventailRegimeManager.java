package globaz.alfagest.services;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class ALEventailRegimeManager extends BManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdTiers;

    public String getForIdTiers() {
        return forIdTiers;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new ALEventailRegime();
    }

    @Override
    protected String _getFields(BStatement statement) {
        return "trad1.plib etat,trad2.plib type, dos.eid no , nmois mois,nanne annee, SUM(nmont) montant, efval, edval, eetat";
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer s = new StringBuffer();
        s.append(_getCollection());
        s.append("jafptiaf pt");
        // Dossier
        s.append(" INNER JOIN ");
        s.append(_getCollection());
        s.append("jafpdos dos");
        s.append(" ON dos.eidp=pt.aid ");
        // traduction etat
        s.append(" INNER JOIN ");
        s.append(_getCollection());
        s.append("jafpcod trad1");
        s.append(" ON (dos.eetat=trad1.pcode and trad1.PTCODE='ETATDOS') ");
        // traduction type
        s.append(" INNER JOIN ");
        s.append(_getCollection());
        s.append("jafpcod trad2");
        s.append(" ON (dos.etypal=trad2.pcode and trad2.PTCODE='TYPEALLO') ");
        // période prestations
        s.append(" LEFT JOIN ");
        s.append(_getCollection());
        s.append("jafpepr pp");
        s.append(" ON dos.eid=pp.midd ");
        // historique prestations
        s.append(" LEFT JOIN ");
        s.append(_getCollection());
        s.append("jafphpr hp");
        s.append(" ON pp.mid=hp.nide ");

        return s.toString();
    }

    @Override
    protected String _getOrder(BStatement statement) {
        return " nanne desc, nmois desc ";
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer where = new StringBuffer(" (nmont<>0 OR nmont IS NULL) and (midrec<>99999999 OR midrec IS NULL)");
        if (!JadeStringUtil.isEmpty(getForIdTiers())) {
            where.append(" AND htitie=");
            where.append(getForIdTiers());
        }
        where.append(" group by trad1.plib,trad2.plib, eid, nmois,nanne,efval,edval,eetat ");
        return where.toString();
    }

}
