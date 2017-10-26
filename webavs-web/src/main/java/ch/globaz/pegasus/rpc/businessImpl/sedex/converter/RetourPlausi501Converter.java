package ch.globaz.pegasus.rpc.businessImpl.sedex.converter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.eahv_iv.xmlns.eahv_iv_2469_000501._1.DecisionReturnType;
import ch.eahv_iv.xmlns.eahv_iv_2469_000501._1.Message;
import ch.eahv_iv.xmlns.eahv_iv_2469_000501._1.OverlapInformationType;
import ch.eahv_iv.xmlns.eahv_iv_2469_000501._1.PlausibilityContentType;
import ch.eahv_iv.xmlns.eahv_iv_2469_000501._1.PlausibilityGeneralType;
import ch.eahv_iv.xmlns.eahv_iv_2469_000501._1.PlausibilityPersonType;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.rpc.businessImpl.sedex.Converter;
import ch.globaz.pegasus.rpc.businessImpl.sedex.Retour;
import ch.globaz.pegasus.rpc.domaine.RetourAnnonce;
import ch.globaz.pegasus.rpc.domaine.TypeViolationPlausi;
import ch.globaz.pegasus.rpc.domaine.plausi.AnnoncePlausiRetour;
import ch.globaz.pegasus.rpc.domaine.plausi.PlausiRetour;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiCategory;
import ch.globaz.pegasus.rpc.plausi.core.RpcPlausiType;

public class RetourPlausi501Converter implements Converter<Message, AnnoncePlausiRetour> {
    private static final Logger LOG = LoggerFactory.getLogger(RetourPlausi501Converter.class);

    @Override
    public AnnoncePlausiRetour convert(Message xml, File file) {
        List<DecisionReturnType> decisions = xml.getContent().getDecisionReturned();
        List<PlausiRetour> plausis = new ArrayList<PlausiRetour>();
        for (DecisionReturnType decision : decisions) {
            PlausiRetour plausi = new PlausiRetour();
            plausi.setIdDecision(decision.getDecisionId());
            plausi.setNss(String.valueOf(decision.getVn()));

            plausi.setReceiptMonth(getDateFromXmlCalendar(decision.getReceiptMonth()));

            List<RetourAnnonce> plausisGeneral = createPlauibilityGeneral(decision);
            List<RetourAnnonce> plausisPersonne = createPlausibilityPerson(decision);
            List<RetourAnnonce> plausisOverLaps = createOverLaps(decision);

            plausisGeneral.addAll(plausisPersonne);
            plausisGeneral.addAll(plausisOverLaps);
            plausi.setRetours(plausisGeneral);
            plausis.add(plausi);
        }
        return new AnnoncePlausiRetour(xml.getContent().getBusinessCaseIdRPC(), plausis);
    }

    private List<RetourAnnonce> createPlauibilityGeneral(DecisionReturnType decision) {
        PlausibilityGeneralType generalType = decision.getViolatedPlausibilityGeneral();

        List<RetourAnnonce> plausisGeneral = new ArrayList<RetourAnnonce>();
        if (generalType != null && generalType.getContent() != null) {
            for (PlausibilityContentType pl : generalType.getContent()) {
                RetourAnnonce retour = createPlausi(pl, TypeViolationPlausi.GENERAL);
                retour.setOfficePC(decision.getDeliveryOffice().getElOffice().toString());
                plausisGeneral.add(retour);
            }
        }
        return plausisGeneral;
    }

    private List<RetourAnnonce> createPlausibilityPerson(DecisionReturnType decision) {
        List<PlausibilityPersonType> plausibilityPersonTypes = decision.getViolatedPlausibilityPerson();
        List<RetourAnnonce> plausisPersonne = new ArrayList<RetourAnnonce>();
        if (plausibilityPersonTypes != null) {
            for (PlausibilityPersonType pl : plausibilityPersonTypes) {
                if (pl.getContent() != null) {
                    for (PlausibilityContentType p : pl.getContent()) {
                        RetourAnnonce plausiPersonne = createPlausi(p, TypeViolationPlausi.PERSON);
                        plausiPersonne.setNssPersonne(String.valueOf(pl.getVn()));
                        plausisPersonne.add(plausiPersonne);
                    }
                }
            }
        }
        return plausisPersonne;
    }

    private List<RetourAnnonce> createOverLaps(DecisionReturnType decision) {
        List<OverlapInformationType> ovelerLaps = decision.getOverlapInformation();
        List<RetourAnnonce> plausis = new ArrayList<RetourAnnonce>();
        if (ovelerLaps != null) {
            for (OverlapInformationType overlap : ovelerLaps) {
                RetourAnnonce plausiOverlap = createPlausi(overlap.getViolatedPlausibility(),
                        TypeViolationPlausi.OVERLAP);
                plausiOverlap.setCaseIdConflit(overlap.getBusinessCaseIdRPC());
                plausiOverlap.setDecisionIdConflit(overlap.getDecisionId());
                plausiOverlap.setOfficePCConflit(overlap.getDeliveryOffice().getElOffice().toString());
                plausiOverlap.setValidFromConflit(getDateFromXmlCalendar(overlap.getValidFrom()));
                if (overlap.getValidTo() != null) {
                    plausiOverlap.setValidToConflit(getDateFromXmlCalendar(overlap.getValidTo()));
                }
                plausis.add(plausiOverlap);
            }
        }
        return plausis;
    }

    List<AnnoncePlausiRetour> toDomaine(List<Retour<Message>> messages) {
        List<AnnoncePlausiRetour> retours = new ArrayList<AnnoncePlausiRetour>();
        for (Retour<Message> retour : messages) {
            retours.add(convert(retour.getUnmarshalledObject(), retour.getFile()));
        }
        return retours;
    }

    private static RetourAnnonce createPlausi(final PlausibilityContentType plausi, TypeViolationPlausi typeVio) {

        return new RetourAnnonce(plausi.getPlausibilityCode(), RpcPlausiCategory.parseByCode(plausi
                .getPlausibilityCategory()), RpcPlausiType.valueOf(plausi.getPlausibilityType()), typeVio);
    }

    private Date getDateFromXmlCalendar(XMLGregorianCalendar xmlCalendar) {
        return new Date(new SimpleDateFormat("dd.MM.yyyy").format(xmlCalendar.toGregorianCalendar().getTime()));
    }

}
