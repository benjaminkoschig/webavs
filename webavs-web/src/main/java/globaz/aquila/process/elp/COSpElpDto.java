package globaz.aquila.process.elp;

import aquila.ch.eschkg.LossType;
import aquila.ch.eschkg.SpType;
import aquila.ch.eschkg.SpType.Outcome.Seizure;
import globaz.aquila.print.list.elp.COELPException;
import globaz.aquila.print.list.elp.COTypeMessageELP;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersUserCode;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Objects;
import java.util.Optional;

public class COSpElpDto extends COAbstractELP {

    private static final String ADB_LABEL = "ELP_TYPE_ADB";
    private SpType spType;
    private String csTypeSaisie;
    private String typeSaisieLabel;
    private String dateExecution;
    private String delaiVente;

    public COSpElpDto(SpType spType, BSession session) throws COELPException {
        this.spType = spType;
        this.numeroStatut = spType.getStatusInfo().getStatus();
        initializeParam(session);
    }

    private void initializeParam(BSession session) throws COELPException {
        Seizure seizure = spType.getOutcome().getSeizure();
        if (Objects.nonNull(seizure)) {
            Seizure.Deed deed = seizure.getDeed();
            LossType loss = seizure.getLoss();
            if (Objects.nonNull(deed)) {
                Seizure.Deed.Seized seized = deed.getSeized();
                initialiseTypeSaisie(session, seized);
                initialiseDelaiVente(seized);
                dateExecution = getDate(deed.getSeizureDate());
            } else if (Objects.nonNull(loss)) {
                typeSaisieLabel = session.getLabel(ADB_LABEL);
                dateExecution = getDate(loss.getDate());
                delaiVente = StringUtils.EMPTY;
            }
        }
    }

    /**
     * Récupère le type de saisie depuis le fichier xml.
     *
     * @param session la session courante.
     * @param seized  l'objet xml.
     * @throws COELPException : Exception lancée si une erreur se produit lors de lors de la récupération du code système.
     */
    private void initialiseTypeSaisie(BSession session, Seizure.Deed.Seized seized) throws COELPException {
        String localpart = seized.getContent().get(0).getName().getLocalPart();
        String codeSytem = CODeedTypeSaisie.getCodeSystemFromCodeXml(localpart.substring(0, 2));
        FWParametersUserCode code = new FWParametersUserCode();
        code.setIdCodeSysteme(codeSytem);
        code.setIdLangue(session.getIdLangue());
        try {
            code.retrieve();
        } catch (Exception e) {
            throw new COELPException("Erreur lors de la récupération des codes systèmes de type de saisie : " + codeSytem + "/n", e);
        }
        csTypeSaisie = code.getIdCodeSysteme();
        typeSaisieLabel = code.getLibelle();
    }

    /**
     * Récupère le délai de vente depuis le fichier xml.
     *
     * @param seized l'objet xml.
     */
    private void initialiseDelaiVente(Seizure.Deed.Seized seized) {
        for (JAXBElement eachElement : seized.getContent()) {
            if (StringUtils.equals("To", eachElement.getName().getLocalPart().substring(2, 4))) {
                delaiVente = getDate((XMLGregorianCalendar) eachElement.getValue());
                break;
            }
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
     * @return le code système du type de saisie
     */
    public String getCsTypeDeSaisie(){
        return csTypeSaisie;
    }

    /**
     *
     * @return le label du type de saisie
     */
    public String getTypeSaisieLabel(){
        return typeSaisieLabel;
    }

    /**
     * @return la date de Deed si Deed non null
     * sinon date de Loss si Loss non null
     * sinon null
     */
    public String getDateExecution() {
        return dateExecution;
    }

    /**
     * @return le délai de vente.
     */
    public String getDelaiVente() {
        return delaiVente;
    }

}
