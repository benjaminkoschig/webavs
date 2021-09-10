package globaz.prestation.acor.acor2020.mapper;

import ch.admin.zas.xmlns.acor_rentes_in_host._0.AssureType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.PeriodeEtrangereType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.PeriodeIJType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.PeriodeTravailType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.PeriodeType;
import ch.admin.zas.xmlns.acor_rentes_in_host._0.PeriodeTypeEnum;
import ch.globaz.common.util.Dates;
import ch.globaz.utils.Pair;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.hera.api.ISFPeriode;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
public class PRAcorAssureTypeMapper extends PRAcorMapper {

    private final List<ISFMembreFamilleRequerant> membresFamille;
    private final ISFSituationFamiliale situationFamiliale;
    @Setter
    private BiFunction<ISFMembreFamilleRequerant, ISFPeriode[], ISFPeriode[]> addPeriodeFunction;

    public PRAcorAssureTypeMapper(final List<ISFMembreFamilleRequerant> membresFamille,
                                  final ISFSituationFamiliale situationFamiliale,
                                  final PRAcorMapper prAcorMapper) {
        super(prAcorMapper.getTypeAdressePourRequerant(), prAcorMapper.getTiersRequerant(), prAcorMapper.getDomaineAdresse(), prAcorMapper.getSession());
        this.membresFamille = membresFamille;
        this.situationFamiliale = situationFamiliale;
    }

    public List<AssureType> map() {
        return this.map(this.membresFamille, (membreFamilleRequerant, assureType) -> assureType);
    }

    public List<AssureType> map(BiFunction<ISFMembreFamilleRequerant, AssureType, AssureType> assureTypeFunction) {
        return this.map(this.membresFamille, assureTypeFunction);
    }

    private List<AssureType> map(final List<ISFMembreFamilleRequerant> membresFamille,
                                 final BiFunction<ISFMembreFamilleRequerant, AssureType, AssureType> assureTypeFunction) {

        return membresFamille.stream()
                             .filter(membre -> !StringUtils.equals(ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU, membre.getIdMembreFamille()))
                             .map(membre -> new Pair<>(membre, this.createAssureType(membre)))
                             .map(pair -> assureTypeFunction.apply(pair.getLeft(), pair.getRight()))
                             .collect(Collectors.toList());
    }

    private AssureType createAssureType(ISFMembreFamilleRequerant membre) {
        AssureType assure = new AssureType();
        assure.setNavs(getNssMembre(membre));
        assure.setNom(membre.getNom());
        assure.setPrenom(membre.getPrenom());
        assure.setDateNaissance(Dates.toXMLGregorianCalendar(membre.getDateNaissance()));
        if (!JadeStringUtil.isBlankOrZero(membre.getDateDeces())) {
            assure.setDateDeces(Dates.toXMLGregorianCalendar(membre.getDateDeces()));
        }
        assure.setNationalite(getCodePays(membre.getCsNationalite()));
        assure.setDomicile(getDomicile(membre.getCsCantonDomicile(), membre.getPays(), getTiersRequerant()));
        assure.setSexe(PRACORConst.csSexeToAcor2020(membre.getCsSexe()));

        // EURO_FORM
        assure.setDonneesPostales(createDonneesPostales());

        // PERIODES
        addPeriodesAssure(assure, membre);
        return assure;
    }

    private void addPeriodesAssure(AssureType assure, ISFMembreFamilleRequerant membre) {

        ISFPeriode[] periodes = PRAccorPeriodeMapper.recupererPeriodesMembre(this.situationFamiliale, membre);
        if (this.addPeriodeFunction != null) {
            periodes = this.addPeriodeFunction.apply(membre, periodes);
        }

        for (ISFPeriode isfPeriode : periodes) {
            if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_IJ, isfPeriode.getType())) {
                assure.getPeriodeIJ().add(createPeriodeIJ(isfPeriode));
            } else if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_TRAVAILLE, isfPeriode.getType())) {
                assure.getPeriodeTravail().add(createPeriodeTravailType(isfPeriode));
            } else if (StringUtils.equals(ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE, isfPeriode.getType())) {
                assure.getPeriodeEtrangere().add(createPeriodeEtrangerType(isfPeriode));
            } else if (getEnumPeriodeType(isfPeriode) != null) {
                assure.getPeriode().add(createPeriodeType(isfPeriode));
            }
        }
    }

    private PeriodeTravailType createPeriodeTravailType(ISFPeriode isfPeriode) {
        PeriodeTravailType periode = new PeriodeTravailType();
        periode.setDebut(Dates.toXMLGregorianCalendar(isfPeriode.getDateDebut()));
        periode.setFin(Dates.toXMLGregorianCalendar(isfPeriode.getDateFin()));
        //Mettre à 0 car il n'existe pas sur WebAVS
        periode.setMontantDebut(BigDecimal.ZERO);
        return periode;
    }

    private PeriodeEtrangereType createPeriodeEtrangerType(ISFPeriode isfPeriode) {
        PeriodeEtrangereType periode = new PeriodeEtrangereType();
        periode.setDebut(Dates.toXMLGregorianCalendar(isfPeriode.getDateDebut()));
        periode.setFin(Dates.toXMLGregorianCalendar(isfPeriode.getDateFin()));
        periode.setEtat(PRConverterUtils.formatRequiredInteger(isfPeriode.getPays()));
        return periode;
    }

    private PeriodeType createPeriodeType(ISFPeriode isfPeriode) {
        PeriodeType periode = new PeriodeType();
        periode.setDebut(Dates.toXMLGregorianCalendar(isfPeriode.getDateDebut()));
        periode.setFin(Dates.toXMLGregorianCalendar(isfPeriode.getDateFin()));
        periode.setType(getEnumPeriodeType(isfPeriode));
        return periode;
    }

    private PeriodeIJType createPeriodeIJ(ISFPeriode isfPeriode) {
        PeriodeIJType periode = new PeriodeIJType();
        periode.setDebut(Dates.toXMLGregorianCalendar(isfPeriode.getDateDebut()));
        periode.setFin(Dates.toXMLGregorianCalendar(isfPeriode.getDateFin()));
        //Par défaut false
        periode.setMesureNouvelle(false);
        return periode;
    }

    private PeriodeTypeEnum getEnumPeriodeType(ISFPeriode isfPeriode) {
        switch (isfPeriode.getType()) {
            case ISFSituationFamiliale.CS_TYPE_PERIODE_DOMICILE:
                return PeriodeTypeEnum.DOMICILE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_NATIONALITE:
                return PeriodeTypeEnum.NATIONALITE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_INCARCERATION:
                return PeriodeTypeEnum.INCARCERATION;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_ETUDE:
                return PeriodeTypeEnum.ETUDE;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_AFFILIATION:
                return PeriodeTypeEnum.AFAC;
            case ISFSituationFamiliale.CS_TYPE_PERIODE_COTISATION:
                return PeriodeTypeEnum.EXEMPTION;
            default:
                return null;
        }
    }
}
