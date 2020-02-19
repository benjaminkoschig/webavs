package globaz.aquila.print.list.elp;

import aquila.ch.eschkg.SpType;
import aquila.ch.eschkg.SpType.Outcome;
import aquila.ch.eschkg.SpType.Outcome.Seizure;
import globaz.aquila.db.access.batch.COEtapeInfoConfig;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersUserCode;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Optional;

public class COResultSpELP extends COAbstractResultELP {

    SpType spType;

    public COResultSpELP(SpType spType) {
        this.spType = spType;
    }

    private static final String ADB_LABEL = "ELP_TYPE_ADB";

    enum DeedTypeSaisie {
        MOBILIER("mo", COEtapeInfoConfig.CS_TYPE_SAISIE_BIENS_MOBILIERS),
        IMMOBILIER("re", COEtapeInfoConfig.CS_TYPE_SAISIE_BIENS_IMMOBILIERS),
        SALAIRE("in", COEtapeInfoConfig.CS_TYPE_SAISIE_SALAIRE);

        String codeXml;
        String codeSystem;

        DeedTypeSaisie(String codeXml, String codeSystem) {
            this.codeXml = codeXml;
            this.codeSystem = codeSystem;
        }

        public static String getCodeSystemFromCodeXml(String codeXml) {
            for (final DeedTypeSaisie type : DeedTypeSaisie.values()) {
                if (type.codeXml.equals(codeXml)) {
                    return type.codeSystem;
                }
            }
            return null;
        }
    }

    @Override
    public COTypeMessageELP getTypemessage() {
        return COTypeMessageELP.SP;
    }

    @Override
    public String getRemarque() {
        return Optional.ofNullable(spType.getStatusInfo().getDetails()).orElse("");
    }

    /**
     *
     * @param session
     * @return le type de saisie :
     */
    public String getTypeDeSaisie(BSession session) throws COELPException {
        Seizure seizure = spType.getOutcome().getSeizure();
        if(seizure == null) {
            // bankruptcyWarning
            return "";
        }

        if(seizure.getDeed() != null) {
            String localpart = seizure.getDeed().getSeized().getContent().get(0).getName().getLocalPart();
            String codeSytem = DeedTypeSaisie.getCodeSystemFromCodeXml(localpart.substring(0,2));
            FWParametersUserCode code = new FWParametersUserCode();
            code.setIdCodeSysteme(codeSytem);
            code.setIdLangue(session.getIdLangue());
            try {
                code.retrieve();
            } catch (Exception e) {
                throw new COELPException("Erreur lors de la récupération des codes systèmes de type de saisie : "+codeSytem +"/n" , e);
            }
            return code.getLibelle();
        } else if(seizure.getLoss() != null) {
            return session.getLabel(ADB_LABEL);
        }
        return "";
    }

    /**
     *
     * @return la date de Deed si Deed non null
     *  sinon date de Loss si Loss non null
     *  sinon null
     */
    public XMLGregorianCalendar getDateExecution() {
        Optional<Outcome> op = Optional.of(spType.getOutcome());
        return op.map(Outcome::getSeizure).map(Seizure::getDeed).map(deed -> deed.getSeizureDate()) // Deed
                .orElse(op.map(Outcome::getSeizure).map(Seizure::getLoss).map(loss -> loss.getDate()) // Loss
                        .orElse(op.map(Outcome::getBankruptcyWarning).map(warn -> warn.getDateOfSummon()) // Warning
                                .orElse(null))); // Other
    }

}
