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
        initializeADBParam(session);
    }

    public COSpElpDto(SpType spType, String typeSaisie, BSession session) throws COELPException {
        this.spType = spType;
        this.numeroStatut = spType.getStatusInfo().getStatus();
        initializePVSaisieParam(typeSaisie, session);
    }

    /**
     * Initialisation des paramètres dans le cadre d'un pv valant pour ADB.
     *
     * @param session
     * @throws COELPException
     */
    private void initializeADBParam(BSession session) throws COELPException {
        Seizure seizure = spType.getOutcome().getSeizure();
        if (Objects.nonNull(seizure)) {
            LossType loss = seizure.getLoss();
            if (Objects.nonNull(loss)) {
                typeSaisieLabel = session.getLabel(ADB_LABEL);
                dateExecution = getDate(loss.getDate());
                delaiVente = StringUtils.EMPTY;
            } else {
                throw new COELPException("Erreur de cast du fichier xml : impossible de récupérer la balise Loss dans le cadre d'un PV de saisie valant ADB.");
            }
        } else {
            throw new COELPException("Erreur de cast du fichier xml : impossible de récupérer la balise Seizure dans le cadre d'un PV de saisie valant ADB.");
        }

    }

    /**
     * Initialisation des paramètres pour un PV de saisie (salaire, mobilier, immobilier).
     *
     * @param typeSaisie : le code de type de saisie.
     * @param session
     * @throws COELPException
     */
    private void initializePVSaisieParam(String typeSaisie, BSession session) throws COELPException {
        Seizure seizure = spType.getOutcome().getSeizure();
        if (Objects.nonNull(seizure)) {
            Seizure.Deed deed = seizure.getDeed();
            if (Objects.nonNull(deed)) {
                Seizure.Deed.Seized seized = deed.getSeized();
                initialiseTypeSaisie(session, typeSaisie);
                initialiseDelaiVente(seized, typeSaisie);
                dateExecution = getDate(deed.getSeizureDate());
            } else {
                throw new COELPException("Erreur de cast du fichier xml : impossible de récupérer la balise Deed dans le cadre d'un PV de saisie.");
            }
        } else {
            throw new COELPException("Erreur de cast du fichier xml : impossible de récupérer la balise Seizure dans le cadre d'un PV de saisie.");
        }
    }

    /**
     * Récupère le type de saisie depuis le code système.
     *
     * @param session la session courante.
     * @param typeSaisie le code système.
     * @throws COELPException : Exception lancée si une erreur se produit lors de lors de la récupération du code système.
     */
    private void initialiseTypeSaisie(BSession session, String typeSaisie) throws COELPException {
        FWParametersUserCode code = new FWParametersUserCode();
        code.setIdCodeSysteme(typeSaisie);
        code.setIdLangue(session.getIdLangue());
        try {
            code.retrieve();
        } catch (Exception e) {
            throw new COELPException("Erreur lors de la récupération des codes systèmes de type de saisie : " + typeSaisie + "/n", e);
        }
        csTypeSaisie = code.getIdCodeSysteme();
        typeSaisieLabel = code.getLibelle();
    }

    /**
     * Récupère le délai de vente depuis le fichier xml.
     *
     * @param seized l'objet xml.
     * @param typeSaisie le code système du type de saisie.
     */
    private void initialiseDelaiVente(Seizure.Deed.Seized seized, String typeSaisie) {
        for (JAXBElement eachElement : seized.getContent()) {
            if (StringUtils.equals("To", eachElement.getName().getLocalPart().substring(2, 4)) && StringUtils.equals(CODeedTypeSaisie.getCodeXmlFromCodeSystem(typeSaisie), eachElement.getName().getLocalPart().substring(0, 2))) {
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
