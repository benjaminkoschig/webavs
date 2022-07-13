package globaz.apg.acorweb.mapper;

import acor.ch.admin.zas.xmlns.acor_rentes_in_host._0.AssureType;
import ch.globaz.common.util.Dates;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.acor.web.mapper.PRAcorMapper;
import globaz.prestation.acor.web.mapper.PRConverterUtils;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;


public class APAssureMapper extends PRAcorMapper {

    public APAssureMapper(PRAcorMapper prAcorMapper, BSession session) {
        super(prAcorMapper.getTypeAdressePourRequerant(), prAcorMapper.getTiersRequerant(), prAcorMapper.getDomaineAdresse(), session);
    }

    public AssureType createAssureType() {
        getTiersRequerant();
        AssureType assure = new AssureType();
        assure.setNavs(getNssTiers());
        assure.setNom(getTiersRequerant().getNom());
        assure.setPrenom(getTiersRequerant().getPrenom());
        assure.setDateNaissance(Dates.toXMLGregorianCalendar(getTiersRequerant().getDateNaissance()));
        if (!JadeStringUtil.isBlankOrZero(getTiersRequerant().getDateDeces())) {
            assure.setDateDeces(Dates.toXMLGregorianCalendar(getTiersRequerant().getDateDeces()));
        }
        assure.setNationalite(getCodePays(getTiersRequerant().getIdPays()));
        assure.setDomicile(getDomicile("", getTiersRequerant().getIdPays(), getTiersRequerant()));
        assure.setSexe(PRACORConst.csSexeToAcor2020(getTiersRequerant().getSexe()));
        assure.setDonneesPostales(createDonneesPostales());
        return assure;
    }

    private Long getNssTiers(){
        return PRConverterUtils.formatNssToLong(getTiersRequerant().getNSS());
    }
}
