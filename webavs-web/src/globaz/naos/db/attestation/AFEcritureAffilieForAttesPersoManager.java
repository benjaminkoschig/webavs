package globaz.naos.db.attestation;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.osiris.api.APIOperation;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Manager permettant la récupération des ecritures pour un affilié pour une liste de rubrique donné
 * 
 * @author SCO
 * @since 05 juil. 2011
 */
public class AFEcritureAffilieForAttesPersoManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnee;
    private Collection<String> forIdExterneRubriqueIn;
    private String forNumAffilie;

    @Override
    protected String _getFields(BStatement statement) {

        StringBuffer sqlFields = new StringBuffer("");

        AFUtil.sqlAddField(sqlFields, "oper.DATE");
        AFUtil.sqlAddField(sqlFields, "rub.IDEXTERNE");
        AFUtil.sqlAddField(sqlFields, "oper.ANNEECOTISATION");
        AFUtil.sqlAddField(sqlFields, "oper.MONTANT");
        AFUtil.sqlAddField(sqlFields, "oper.CODEDEBITCREDIT");

        return sqlFields.toString();
    }

    @Override
    protected String _getFrom(BStatement statement) {
        StringBuffer sqlFrom = new StringBuffer();

        sqlFrom.append(_getCollection() + "CAOPERP oper ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "CACPTAP comp ON comp.IDCOMPTEANNEXE = oper.IDCOMPTEANNEXE ");
        sqlFrom.append("INNER JOIN " + _getCollection() + "CARUBRP rub ON rub.idRubrique = oper.idCompte ");

        return sqlFrom.toString();
    }

    @Override
    protected String _getWhere(BStatement statement) {
        StringBuffer sqlWhere = new StringBuffer();

        AFUtil.sqlAddCondition(sqlWhere, "oper.ETAT = " + APIOperation.ETAT_COMPTABILISE);

        AFUtil.sqlAddCondition(sqlWhere, "oper.DATE >= " + getForAnnee() + "0101");
        AFUtil.sqlAddCondition(sqlWhere, "oper.DATE <= " + getForAnnee() + "1231");
        AFUtil.sqlAddCondition(sqlWhere, "oper.IDTYPEOPERATION like 'E%'");
        AFUtil.sqlAddConditionInString(sqlWhere, "rub.IDEXTERNE", getForIdExterneRubriqueIn());
        AFUtil.sqlAddCondition(sqlWhere, "comp.IDCOMPTEANNEXE IN ( " + createSousSelect() + " )");

        return sqlWhere.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFEcritureAffilieForAttesPerso();
    }

    private String createSousSelect() {

        StringBuffer buff = new StringBuffer();

        buff.append("SELECT IDCOMPTEANNEXE FROM " + _getCollection() + "CACPTAP compte ");
        buff.append("INNER JOIN " + _getCollection() + "AFAFFIP aff ON aff.MALNAF = compte.IDEXTERNEROLE ");
        buff.append("WHERE aff.malnaf = '" + getForNumAffilie() + "' ");

        Collection<String> listTypeAffiliation = new ArrayList<String>();
        listTypeAffiliation.add(CodeSystem.TYPE_AFFILI_INDEP);
        listTypeAffiliation.add(CodeSystem.TYPE_AFFILI_INDEP_EMPLOY);
        listTypeAffiliation.add(CodeSystem.TYPE_AFFILI_NON_ACTIF);
        listTypeAffiliation.add(CodeSystem.TYPE_AFFILI_TSE);
        listTypeAffiliation.add(CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE);

        AFUtil.sqlAddConditionIn(buff, "aff.mattaf", listTypeAffiliation);

        return buff.toString();
    }

    public String getForAnnee() {
        return forAnnee;
    }

    public Collection<String> getForIdExterneRubriqueIn() {
        return forIdExterneRubriqueIn;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public void setForAnnee(String forAnnee) {
        this.forAnnee = forAnnee;
    }

    public void setForIdExterneRubriqueIn(Collection<String> forIdExterneRubriqueIn) {
        this.forIdExterneRubriqueIn = forIdExterneRubriqueIn;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

}
