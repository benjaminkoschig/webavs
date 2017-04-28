package globaz.musca.db.interet.generic.montantsoumis;

import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.db.facturation.FAAfact;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.api.APISection;
import globaz.osiris.db.interets.CAInteretMoratoire;
import globaz.osiris.db.interets.CARubriqueSoumiseInteret;
import globaz.phenix.db.principale.CPDecision;
import java.util.ArrayList;

/**
 * Pour un plan et un passage cumulé le montant facture soumis par entente facture, anneecotisation, date de réception
 * et id externe de la facture. SQL : select a.identetefacture, b.anneecotisation, a.refcollaborateur,
 * a.idexternefacture, a.idsouintmor, sum(b.montantfacture) as montantfacture from webavs.FAENTFP a, webavs.FAAFACP b,
 * webavs.CAIMRSP im where a.identetefacture = b.identetefacture and a.idpassage = 29 and b.idpassage = 29 and
 * a.idsoustype in (227013, 227014) and b.idrubrique = im.idrubrique and im.idplacalint = 1 group by a.identetefacture,
 * b.anneecotisation, a.refcollaborateur, a.idexternefacture, a.idsouintmor having sum(b.montantfacture) > 0 order by
 * a.identetefacture asc, b.anneecotisation asc
 * 
 * @author DDA
 */
public class FASumMontantSoumisParPlanManager extends BManager {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeCotisationInferieur;
    private String forIdPassage;

    private String forIdPlan;

    private ArrayList<String> forIdSousTypeIn = null;
    private boolean forMontantDejaFacture = false;
    private boolean forMontantPositif = true;

    private boolean forReferenceExterneCotPers = false;

    /**
     * see globaz.globall.db.BManager#_beforeFind(globaz.globall.db.BStatement)
     */
    @Override
    protected void _beforeFind(BTransaction transaction) throws Exception {
        super._beforeFind(transaction);
    }

    /**
     * see globaz.globall.db.BManager#_getFields(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFields(BStatement statement) {
        return "sum(b." + FAAfact.FIELD_MONTANTINITIAL + ") as " + FAAfact.FIELD_MONTANTINITIAL + ", sum(b."
                + FAAfact.FIELD_MONTANTDEJAFACTURE + ") as " + FAAfact.FIELD_MONTANTDEJAFACTURE + ", sum(b."
                + FAAfact.FIELD_MONTANTFACTURE + ") as " + FAAfact.FIELD_MONTANTFACTURE + ", a."
                + FAEnteteFacture.FIELD_IDENTETEFACTURE + ", a." + FAEnteteFacture.FIELD_REFCOLLABORATEUR + ", a."
                + FAEnteteFacture.FIELD_IDEXTERNEFACTURE + ", b." + FAAfact.FIELD_ANNEECOTISATION + ", a."
                + FAEnteteFacture.FIELD_IDSOUINTMOR + ", b." + FAAfact.FIELD_TYPECALCULIM;
    }

    /**
     * see globaz.globall.db.BManager#_getFrom(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + FAEnteteFacture.TABLE_FAENTFP + " a, " + _getCollection() + FAAfact.TABLE_FAAFACP
                + " b, " + _getCollection() + CARubriqueSoumiseInteret.TABLE_CAIMRSP + " im";
    }

    /**
     * see globaz.globall.db.BManager#_getWhere(globaz.globall.db.BStatement)
     */
    @Override
    protected String _getWhere(BStatement statement) {
        StringBuilder where = new StringBuilder();
        where.append(getWhere());
        where.append(getGroupBy());
        where.append(getHaving());
        where.append(getOrder());

        return where.toString();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new FASumMontantSoumisParPlan();
    }

    public String getForAnneeCotisationInferieur() {
        return forAnneeCotisationInferieur;
    }

    public String getForIdPassage() {
        return forIdPassage;
    }

    public String getForIdPlan() {
        return forIdPlan;
    }

    public ArrayList<String> getForIdSousTypeIn() {
        return forIdSousTypeIn;
    }

    private String getForIdSousTypeWhere() {
        if (getForIdSousTypeIn() != null) {
            StringBuilder tmp = new StringBuilder("(a." + FAEnteteFacture.FIELD_IDSOUSTYPE + " in (");

            for (int i = 0; i < getForIdSousTypeIn().size(); i++) {

                if (!getForIdSousTypeIn().get(i).equals(APISection.ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR)) {
                    tmp.append("" + getForIdSousTypeIn().get(i));

                    if (i < getForIdSousTypeIn().size() - 1) {
                        tmp.append(", ");
                    }
                }
            }
            tmp.append(") ");
            for (int i = 0; i < getForIdSousTypeIn().size(); i++) {
                if (getForIdSousTypeIn().get(i).equals(APISection.ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR)) {
                    tmp.append(" OR (a.IDSOUSTYPE=" + APISection.ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR);

                    if (isForMontantPositif()) {
                        tmp.append(" and b.MONTANTFACTURE > 0) ");
                    } else {
                        tmp.append(") ");
                    }
                }
            }

            tmp.append(") and ");

            return tmp.toString();
        } else {
            return "";
        }
    }

    protected String getGroupBy() {
        return "group by a." + FAEnteteFacture.FIELD_IDENTETEFACTURE + ", a." + FAEnteteFacture.FIELD_REFCOLLABORATEUR
                + ", a." + FAEnteteFacture.FIELD_IDEXTERNEFACTURE + ", b." + FAAfact.FIELD_ANNEECOTISATION + ", a."
                + FAEnteteFacture.FIELD_IDSOUINTMOR + ", b." + FAAfact.FIELD_TYPECALCULIM + " ";
    }

    protected String getHaving() {
        if (isForMontantPositif()) {
            return "having sum(b." + FAAfact.FIELD_MONTANTFACTURE + ") > 0 ";
        } else {
            return "having sum(b." + FAAfact.FIELD_MONTANTFACTURE + ") < 0 ";
        }
    }

    protected String getOrder() {
        return "order by a." + FAEnteteFacture.FIELD_IDENTETEFACTURE + " asc, b." + FAAfact.FIELD_ANNEECOTISATION
                + " asc";
    }

    protected String getWhere() {
        StringBuilder where = new StringBuilder();
        where.append("a." + FAEnteteFacture.FIELD_IDENTETEFACTURE + " = b." + FAAfact.FIELD_IDENTETEFACTURE + " and ");
        where.append("a." + FAEnteteFacture.FIELD_IDPASSAGE + " = " + getForIdPassage() + " and ");
        where.append("b." + FAAfact.FIELD_IDPASSAGE + " = " + getForIdPassage() + " and ");

        where.append(getForIdSousTypeWhere());

        where.append("a." + FAEnteteFacture.FIELD_IDSOUINTMOR + " in (" + CAInteretMoratoire.CS_AUTOMATIQUE + ", "
                + CAInteretMoratoire.CS_SOUMIS + ", " + CAInteretMoratoire.CS_EXEMPTE + ") and ");
        where.append("b." + FAAfact.FIELD_IDRUBRIQUE + " = im." + CARubriqueSoumiseInteret.FIELD_IDRUBRIQUE + " and ");

        if (isForMontantDejaFacture()) {
            where.append("((b." + FAAfact.FIELD_MONTANTDEJAFACTURE + " = 0 and b." + FAAfact.FIELD_TYPECALCULIM
                    + " <> " + CAInteretMoratoire.CS_AFFILIATION_RETROACTIVE + ") or " + FAAfact.FIELD_TYPECALCULIM
                    + " = " + CAInteretMoratoire.CS_REPRISE_IMPOT + ") and ");
        }

        if (isForReferenceExterneCotPers()) {
            where.append("(b." + FAAfact.FIELD_REFERENCEEXTERNE + " like '" + CPDecision.CS_DEFINITIVE + "%' or ");
            where.append("b." + FAAfact.FIELD_REFERENCEEXTERNE + " like '" + CPDecision.CS_RECTIFICATION + "%' or ");
            where.append("b." + FAAfact.FIELD_REFERENCEEXTERNE + " like '" + CPDecision.CS_IMPUTATION + "%') and "
                    + FAAfact.FIELD_TYPECALCULIM + " <> " + CAInteretMoratoire.CS_REPRISE_IMPOT + " and ");
        }

        if (!JadeStringUtil.isBlank(getForAnneeCotisationInferieur())) {
            where.append("b." + FAAfact.FIELD_ANNEECOTISATION + " < " + getForAnneeCotisationInferieur() + " and ");
        } else {
            where.append("b." + FAAfact.FIELD_ANNEECOTISATION + " < " + JACalendar.today().getYear() + " and ");
        }

        where.append("im." + CARubriqueSoumiseInteret.FIELD_IDPLACALINT + " = " + getForIdPlan() + " and ");
        where.append("b." + FAAfact.FIELD_REFERENCEEXTERNE + " not like '" + CAInteretMoratoire.CS_EXEMPTE + "%' ");

        return where.toString();
    }

    public boolean isForMontantDejaFacture() {
        return forMontantDejaFacture;
    }

    public boolean isForMontantPositif() {
        return forMontantPositif;
    }

    public boolean isForReferenceExterneCotPers() {
        return forReferenceExterneCotPers;
    }

    public void setForAnneeCotisationInferieur(String forAnneeCotisationInferieur) {
        this.forAnneeCotisationInferieur = forAnneeCotisationInferieur;
    }

    public void setForIdPassage(String forIdPassage) {
        this.forIdPassage = forIdPassage;
    }

    public void setForIdPlan(String forIdPlan) {
        this.forIdPlan = forIdPlan;
    }

    public void setForIdSousTypeIn(ArrayList<String> forIdSousTypeIn) {
        this.forIdSousTypeIn = forIdSousTypeIn;
    }

    public void setForMontantDejaFacture(boolean forMontantDejaFacture) {
        this.forMontantDejaFacture = forMontantDejaFacture;
    }

    public void setForMontantPositif(boolean forMontantPositif) {
        this.forMontantPositif = forMontantPositif;
    }

    public void setForReferenceExterneCotPers(boolean forReferenceExterneCotPers) {
        this.forReferenceExterneCotPers = forReferenceExterneCotPers;
    }

}
