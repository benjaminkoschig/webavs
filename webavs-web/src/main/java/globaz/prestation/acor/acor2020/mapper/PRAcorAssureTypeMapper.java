package globaz.prestation.acor.acor2020.mapper;

import ch.admin.zas.xmlns.acor_rentes_in_host._0.AssureType;
import ch.globaz.common.util.Dates;
import ch.globaz.utils.Pair;
import globaz.globall.db.BSession;
import globaz.hera.api.ISFMembreFamilleRequerant;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
public class PRAcorAssureTypeMapper extends PRAcorMapper {

    private final List<ISFMembreFamilleRequerant> membresFamille;

    public PRAcorAssureTypeMapper(final BSession session,
                                  final PRTiersWrapper tiersRequerant,
                                  final Boolean adresseCourrierPourRequerant,
                                  final List<ISFMembreFamilleRequerant> membresFamille) {
        super(adresseCourrierPourRequerant, tiersRequerant, session);
        this.membresFamille = membresFamille;
    }

    public PRAcorAssureTypeMapper(final List<ISFMembreFamilleRequerant> membresFamille,final PRAcorMapper prAcorMapper) {
        super(prAcorMapper.isAdresseCourrierPourRequerant(), prAcorMapper.getTiersRequerant(), prAcorMapper.getSession());
        this.membresFamille = membresFamille;
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

        return assure;
    }
}
