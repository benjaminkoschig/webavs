package globaz.helios.db.comptes;

import globaz.globall.db.BStatement;
import globaz.helios.translation.CodeSystem;

/**
 * @author: Administrator
 * @revision SCO 9 mars 2010
 */
public class CGExtendedMvtCompteListViewBean extends CGMouvementCompteListViewBean {

    // -----------------------------------------------------------------------------------------
    // SCO le SCO 9 mars 2010
    // Rmq : Voici la requete optimisée que le manager va executer :
    //
    // SELECT webavsciam.cgecrip.idecriture,
    // webavsciam.cgecrip.idcompte,
    // webavsciam.cgecrip.identeteecriture,
    // webavsciam.cgecrip.idjournal,
    // webavsciam.cgecrip.idexercomptable,
    // webavsciam.cgecrip.idremarque,
    // webavsciam.cgecrip.idcentrecharge,
    // webavsciam.cgecrip.idmandat,
    // webavsciam.cgecrip.DATE,
    // webavsciam.cgecrip.datevaleur,
    // webavsciam.cgecrip.piece,
    // webavsciam.cgecrip.libelle,
    // webavsciam.cgecrip.montant,
    // webavsciam.cgecrip.montantmonnaie,
    // webavsciam.cgecrip.coursmonnaie,
    // webavsciam.cgecrip.codedebitcredit,
    // webavsciam.cgecrip.referenceexterne,
    // webavsciam.cgecrip.estpointee,
    // webavsciam.cgecrip.estprovisoire,
    // webavsciam.cgecrip.esterreur,
    // webavsciam.cgecrip.estactive,
    // webavsciam.cgecrip.idlog,
    // webavsciam.cgecrip.idlivre,
    // webavsciam.cgecrip.pspy,
    // code,
    // webavsciam.cgplanp.idexterne idextctrcompte,
    // webavsciam.cgplanp.libellefr libellefrctrecrit,
    // webavsciam.cgplanp.libellede libelledectrecrit,
    // webavsciam.cgplanp.libelleit libelleitctrecrit,
    // contre_ecriture.idecriture idcontreecriture,
    // contre_ecriture.montant montantcontreecr,
    // contre_ecriture.montantmonnaie montantmecontreecr,
    // cpt_contre_ecrit.idnature idnaturcomptctrecr,
    // cpt_contre_ecrit.codeisomonnaie codeisocomptctrecr,
    // webavsciam.cgecrep.nombreavoir,
    // webavsciam.cgecrep.nombredoit,
    // webavsciam.cgecrep.idcontrepartiedoit,
    // webavsciam.cgecrep.idcontrepartieavoi,
    // rep.idcontrepartiedoit,
    // rep.idcontrepartieavoi
    // FROM webavsciam.cgjourp
    // INNER JOIN webavsciam.cgecrip
    // ON (webavsciam.cgjourp.idjournal = webavsciam.cgecrip.idjournal)
    // INNER JOIN webavsciam.cgperip
    // ON (webavsciam.cgjourp.idperiodecomptable =
    // webavsciam.cgperip.idperiodecomptable)
    // INNER JOIN webavsciam.cgexerp
    // ON (webavsciam.cgjourp.idexercomptable =
    // webavsciam.cgexerp.idexercomptable)
    // INNER JOIN webavsciam.cgecrep
    // ON (webavsciam.cgecrep.identeteecriture =
    // webavsciam.cgecrip.identeteecriture)
    // LEFT OUTER JOIN webavsciam.cgecrep AS rep
    // ON (rep.identeteecriture = webavsciam.cgecrip.identeteecriture
    // AND ((rep.idcontrepartiedoit = webavsciam.cgecrip.idecriture
    // AND rep.idcontrepartieavoi = webavsciam.cgecrip.idecriture)
    // OR (rep.idcontrepartiedoit = webavsciam.cgecrip.idecriture
    // AND rep.idcontrepartieavoi = 0)
    // OR (rep.idcontrepartiedoit = 0
    // AND rep.idcontrepartieavoi = webavsciam.cgecrip.idecriture)))
    // LEFT OUTER JOIN webavsciam.cgecrip AS contre_ecriture
    // ON (contre_ecriture.idecriture = rep.idcontrepartieavoi
    // OR contre_ecriture.idecriture = rep.idcontrepartiedoit)
    // LEFT OUTER JOIN webavsciam.cgcomtp AS cpt_contre_ecrit
    // ON cpt_contre_ecrit.idcompte = contre_ecriture.idcompte
    // LEFT OUTER JOIN webavsciam.cgplanp
    // ON (webavsciam.cgplanp.idcompte = cpt_contre_ecrit.idcompte
    // AND webavsciam.cgplanp.idexercomptable =
    // webavsciam.cgexerp.idexercomptable)
    // WHERE webavsciam.cgecrip.idcompte = 273
    // AND webavsciam.cgecrip.idexercomptable = 15
    // AND webavsciam.cgecrip.idmandat = 900
    // AND webavsciam.cgecrip.estactive = '1'
    // ORDER BY webavsciam.cgecrep.DATE,
    // webavsciam.cgecrip.identeteecriture
    // -----------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de CGExtendedMvtCompteListViewBean.
     */
    public CGExtendedMvtCompteListViewBean() {
        super();
    }

    @Override
    protected String _getFields(BStatement statement) {
        StringBuffer fields = new StringBuffer();

        if (CodeSystem.CS_VUE_CONSOLIDEE.equals(getReqVue())) {
            fields.append(_getCollection()).append("CGPLANP.idexterne,");
            fields.append(_getCollection()).append("CGECREP.date,");
            fields.append(_getCollection()).append("CGECRIP.libelle,");
            fields.append(_getCollection()).append("CGECRIP.piece,");
            fields.append(_getCollection()).append("CGECRIP.codedebitcredit,");
            fields.append(_getCollection()).append("CGECRIP.idjournal,");
            fields.append("code");
            fields.append(", sum(montant) montant");
        } else {
            fields.append(super._getFields(statement));
        }

        return fields.toString();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {

        String table1 = _getCollection() + "CGECRIP";
        String table2 = _getCollection() + "CGECREP";
        String table3 = _getCollection() + "CGPLANP";
        String table4 = _getCollection() + "CGEXERP";
        String table5 = _getCollection() + "CGCOMTP";

        String from = super._getFrom(statement);

        // from += " LEFT OUTER JOIN " + table2 + " AS REP ON (";
        // from += " REP.IDENTETEECRITURE = " + table1 +
        // ".IDENTETEECRITURE AND (";
        // from += " ( REP.IDCONTREPARTIEDOIT = " + table1 +
        // ".IDECRITURE AND REP.IDCONTREPARTIEAVOI = " + table1 +
        // ".IDECRITURE ) OR ";
        // from += " ( REP.IDCONTREPARTIEDOIT = " + table1 +
        // ".IDECRITURE AND REP.IDCONTREPARTIEAVOI = 0 ) OR ";
        // from += " ( REP.IDCONTREPARTIEDOIT = 0 AND REP.IDCONTREPARTIEAVOI = "
        // + table1 + ".IDECRITURE ) ) )";
        // from += " LEFT OUTER JOIN " + table1 +
        // " AS CONTRE_ECRITURE ON (CONTRE_ECRITURE.IDECRITURE = REP.IDCONTREPARTIEAVOI OR CONTRE_ECRITURE.IDECRITURE = REP.IDCONTREPARTIEDOIT) ";

        if (!CodeSystem.CS_VUE_CONSOLIDEE.equals(getReqVue())) {
            from += " LEFT OUTER JOIN " + table1 + " AS CONTRE_ECRITURE ON ((CONTRE_ECRITURE.IDECRITURE = " + table2
                    + ".IDCONTREPARTIEAVOI OR CONTRE_ECRITURE.IDECRITURE = " + table2
                    + ".IDCONTREPARTIEDOIT) AND CONTRE_ECRITURE.IDECRITURE <> " + table1 + ".IDECRITURE) ";
            from += " LEFT OUTER JOIN " + table5
                    + " AS CPT_CONTRE_ECRIT ON CPT_CONTRE_ECRIT.IDCOMPTE = CONTRE_ECRITURE.IDCOMPTE ";
            from += " LEFT OUTER JOIN " + table3 + " ON (" + table3 + ".IDCOMPTE = CPT_CONTRE_ECRIT.idCompte AND "
                    + table3 + ".idExerComptable = " + table4 + ".idExerComptable) ";
        } else {
            // inner join ciciweb.cgplanp pl on (ec.idcompte=pl.idcompte and ec.idexercomptable=pl.idexercomptable)
            from += " INNER JOIN " + table3 + " ON (" + table3 + ".IDCOMPTE = " + table1 + ".idCompte " + " AND "
                    + table1 + ".idExerComptable = " + table3 + ".idExerComptable) ";
        }

        return from;
    }

    @Override
    protected String _getOrder(globaz.globall.db.BStatement statement) {
        StringBuffer order = new StringBuffer();

        if (CodeSystem.CS_VUE_CONSOLIDEE.equals(getReqVue())) {
            order.append(_getCollection()).append("CGPLANP.idexterne,");
            order.append(_getCollection()).append("CGECREP.date,");
            order.append(_getCollection()).append("CGECRIP.libelle,");
            order.append(_getCollection()).append("CGECRIP.piece,");
            order.append(_getCollection()).append("CGECRIP.codedebitcredit,");
            order.append(_getCollection()).append("CGECRIP.idjournal,");
            order.append("code");
        } else {
            order.append(super._getOrder(statement));
        }

        return order.toString();
    }

    @Override
    protected String _getWhere(globaz.globall.db.BStatement statement) {
        StringBuffer where = new StringBuffer(super._getWhere(statement));

        if (CodeSystem.CS_VUE_CONSOLIDEE.equals(getReqVue())) {
            where.append(" group by ");
            where.append(_getCollection()).append("CGPLANP.idexterne,");
            where.append(_getCollection()).append("CGECREP.date,");
            where.append(_getCollection()).append("CGECRIP.libelle,");
            where.append(_getCollection()).append("CGECRIP.piece,");
            where.append(_getCollection()).append("CGECRIP.codedebitcredit,");
            where.append(_getCollection()).append("CGECRIP.idjournal,");
            where.append("code");
        }

        return where.toString();
    }

    /*
     * @see globaz.helios.db.comptes.CGMouvementCompteListViewBean#_newEntity()
     */
    @Override
    protected globaz.globall.db.BEntity _newEntity() throws Exception {
        return new CGExtendedMvtCompteViewBean();
    }

}