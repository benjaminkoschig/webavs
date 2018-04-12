package globaz.orion.vb.recap;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BManager;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.orion.vb.EBAbstractListViewBeanPagination;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.orion.business.models.af.RecapAf;
import ch.globaz.orion.business.models.af.StatutRecapAfWebAvsEnum;
import ch.globaz.orion.businessimpl.services.af.AfServiceImpl;
import ch.globaz.xmlns.eb.recapaf.EBRecapAfException_Exception;
import ch.globaz.xmlns.eb.recapaf.FindResultBean;
import ch.globaz.xmlns.eb.recapaf.OrderByDirEnum;
import ch.globaz.xmlns.eb.recapaf.RecapAfEnrichie;
import ch.globaz.xmlns.eb.recapaf.RecapAfOrderByEnum;
import ch.globaz.xmlns.eb.recapaf.StatutRecapEnum;

public class EBRecapAfListViewBean extends EBAbstractListViewBeanPagination {

    private List<RecapAf> listRecapAf = new ArrayList<RecapAf>();
    private RecapAfOrderByEnum orderBy;
    private StatutRecapAfWebAvsEnum statut;
    private String likeAffilie;
    private String anneeMoisRecap;
    private String dateSoumission;
    private String likeNom;
    private Integer from;
    private Long matchingRows;
    private static final Integer NBROWS = 25;

    public EBRecapAfListViewBean() {
        from = 0;
        orderBy = RecapAfOrderByEnum.ORDER_BY_ANNEE_MOIS;
        statut = StatutRecapAfWebAvsEnum.TRAITEE;
        likeAffilie = null;
        anneeMoisRecap = null;
        dateSoumission = null;
        likeNom = null;
    }

    public void init() throws EBRecapAfException_Exception {

    }

    public List<RecapAf> getListRecapAf() {
        return listRecapAf;
    }

    public void setListRecapAf(List<RecapAf> listRecapAf) {
        this.listRecapAf = listRecapAf;
    }

    @Override
    public BIPersistentObject get(int idx) {
        return null;
    }

    @Override
    public int getSize() {
        return listRecapAf.size();
    }

    @Override
    public BManager getManager() {
        return null;
    }

    @Override
    public void findNext() throws Exception {
        from = from + NBROWS;
        find();
    }

    @Override
    public void findPrev() throws Exception {
        from = from - NBROWS;
        find();
    }

    @Override
    public int size() {
        return listRecapAf.size();
    }

    @Override
    public boolean canDoNext() {
        if ((matchingRows - from) >= (NBROWS - 1)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canDoPrev() {
        boolean canDoPrev = false;
        if (from > 0) {
            canDoPrev = true;
        }
        return canDoPrev;
    }

    @Override
    public void find() throws EBRecapAfException_Exception, DatatypeConfigurationException {
        Integer forAnnee = null;
        Integer forMois = null;

        if (!anneeMoisRecap.isEmpty()) {
            forAnnee = Integer.valueOf(anneeMoisRecap.substring(3, 7));
            forMois = Integer.valueOf(anneeMoisRecap.substring(0, 2));
        }

        XMLGregorianCalendar dateSoumissionGrego = null;

        if (!dateSoumission.isEmpty()) {
            dateSoumissionGrego = defineGregorianDate(dateSoumission);
        }

        OrderByDirEnum orderByDir;
        if (orderBy == null) {
            orderByDir = null;
        } else {
            switch (orderBy) {
                case ORDER_BY_NOM_AFFILIE:
                case ORDER_BY_NUM_AFFILIE:
                    orderByDir = OrderByDirEnum.ASC;
                    break;
                case ORDER_BY_ANNEE_MOIS:
                case ORDER_BY_DATE_MODIFICATION:
                default:
                    orderByDir = OrderByDirEnum.DESC;
                    break;
            }
        }

        FindResultBean resultBean = AfServiceImpl.listRecapAf(BSessionUtil.getSessionFromThreadContext(), likeAffilie,
                JadeStringUtil.convertSpecialChars(likeNom).toUpperCase(), forAnnee, forMois, statut,
                dateSoumissionGrego, from, NBROWS, orderBy, orderByDir);

        matchingRows = resultBean.getNbMatchingRows();
        List<RecapAfEnrichie> listRecapAfEntity = (List) resultBean.getSearchResult();
        prepareListRecaAfWebAvs(listRecapAfEntity);

    }

    private XMLGregorianCalendar defineGregorianDate(String stringDate) throws DatatypeConfigurationException {
        GregorianCalendar gregoCal = new GregorianCalendar();

        Date globazDate = JadeDateUtil.getGlobazDate(stringDate);

        gregoCal.setTime(globazDate);

        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregoCal);

    }

    private void prepareListRecaAfWebAvs(List<RecapAfEnrichie> listRecapAfEntity) {
        listRecapAf.clear();

        for (RecapAfEnrichie recapAfEnrichie : listRecapAfEntity) {
            StatutRecapAfWebAvsEnum statutWebAvs = defineStatutRecapWebAvs(recapAfEnrichie.getRecapAf().getStatut(),
                    recapAfEnrichie.getRecapAf().isAucunChangement());
            Date anneeMoisRecapAf = recapAfEnrichie.getRecapAf().getAnneeMoisRecap().toGregorianCalendar().getTime();
            Date dateMiseADispoRecapAf = recapAfEnrichie.getRecapAf().getDateMiseADisposition().toGregorianCalendar()
                    .getTime();
            Date lastModificationDate = recapAfEnrichie.getRecapAf().getLastModificationDate().toGregorianCalendar()
                    .getTime();

            RecapAf recapAfWebAvs = new RecapAf(recapAfEnrichie.getRecapAf().getIdRecap(), recapAfEnrichie.getRecapAf()
                    .getPartner(), anneeMoisRecapAf, dateMiseADispoRecapAf, lastModificationDate, statutWebAvs,
                    recapAfEnrichie.getRecapAf().isAucunChangement());

            listRecapAf.add(recapAfWebAvs);
        }
    }

    private StatutRecapAfWebAvsEnum defineStatutRecapWebAvs(StatutRecapEnum statutEbu, Boolean aucunChangement) {
        StatutRecapAfWebAvsEnum statutWebAvs = null;
        if (statutEbu.equals(StatutRecapEnum.A_TRAITER)) {
            statutWebAvs = StatutRecapAfWebAvsEnum.GENEREE;
        } else if (statutEbu.equals(StatutRecapEnum.TRAITEE) && !aucunChangement) {
            statutWebAvs = StatutRecapAfWebAvsEnum.A_TRAITER;
        } else if (statutEbu.equals(StatutRecapEnum.TRAITEE) && aucunChangement) {
            statutWebAvs = StatutRecapAfWebAvsEnum.AUCUN_CHANGEMENT;
        } else if (statutEbu.equals(StatutRecapEnum.TRAITEE_CAISSE)) {
            statutWebAvs = StatutRecapAfWebAvsEnum.TRAITEE;
        } else if (statutEbu.equals(StatutRecapEnum.CLOTUREE)) {
            statutWebAvs = StatutRecapAfWebAvsEnum.CLOTUREE;
        }

        return statutWebAvs;
    }

    public String getOrderBy() {
        return orderBy.value();
    }

    public void setOrderBy(String orderBy) {
        if (!orderBy.isEmpty()) {
            this.orderBy = RecapAfOrderByEnum.valueOf(orderBy);
        } else {
            this.orderBy = null;
        }
    }

    public String getStatut() {
        return statut.toString();
    }

    public void setStatut(String statut) {
        if (!statut.isEmpty()) {
            this.statut = StatutRecapAfWebAvsEnum.valueOf(statut);
        } else {
            this.statut = null;
        }
    }

    public String getLikeAffilie() {
        return likeAffilie;
    }

    public void setLikeAffilie(String likeAffilie) {
        this.likeAffilie = likeAffilie;
    }

    public String getAnneeMoisRecap() {
        return anneeMoisRecap;
    }

    public void setAnneeMoisRecap(String anneeMoisRecap) {
        this.anneeMoisRecap = anneeMoisRecap;
    }

    public String getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(String dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public String getLikeNom() {
        return likeNom;
    }

    public void setLikeNom(String likeNom) {
        this.likeNom = likeNom;
    }

}
