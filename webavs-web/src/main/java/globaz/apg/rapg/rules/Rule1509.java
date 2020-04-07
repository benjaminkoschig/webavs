package globaz.apg.rapg.rules;

import ch.eahv.rapg.eahv000601._4.RegisterStatusRecordType;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGJointTiersManager;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.interfaces.APDroitAvecParent;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.rapg.messages.Message1509;
import globaz.apg.ws.APRapgConsultationUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUtil;

import java.lang.String;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rule1509 extends Rule {
    private List<Message1509> listMessage = new ArrayList<>();

    /**
     * @param errorCode
     */
    public Rule1509(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException  {
        Message1509 message;
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        String endOfPeriod = champsAnnonce.getEndOfPeriod();
        String nss = champsAnnonce.getInsurant();
        String numOffice =  champsAnnonce.getDeliveryOfficeOfficeIdentifier();
        String numBranch =  champsAnnonce.getDeliveryOfficeBranch();
        validNotEmpty(nss, "NSS");
        testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
        testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");
        nss = nss.replaceAll("\\.", "");
        try {
            List<RegisterStatusRecordType> list = APRapgConsultationUtil.findAnnonces(getSession(), nss, numOffice, numBranch);
            if(list !=null) {
                for (final RegisterStatusRecordType reccord : list) {
                    Date dateDebutWS = new Date(reccord.getStartOfPeriod().toGregorianCalendar().getTime()) ;
                    Date dateFinWS = new Date(reccord.getEndOfPeriod().toGregorianCalendar().getTime());
                    Date dateDebutLocal = new Date(startOfPeriod);
                    Date dateFinLocal = new Date(endOfPeriod);
                    if(isChevauchent(dateDebutWS,dateFinWS,dateDebutLocal,dateFinLocal)){
                        message = new Message1509();
                        message.setNumCaisse(String.valueOf(reccord.getDeliveryOffice().getOfficeIdentifier()));
                        message.setDebutPeriode(dateDebutWS.getSwissValue());
                        message.setFinPeriode(dateFinWS.getSwissValue());
                        message.setGenreService(reccord.getServiceType().toString());
                        listMessage.add(message);
                    }
                }
            }

        } catch (Exception e) {
            throw new APRuleExecutionException(e);
        }
        if(listMessage.isEmpty()){
            return true;
        }else {
            return false;
        }
    }
    private  boolean isChevauchent(Date dateDebutWS, Date dateFinWS, Date dateDebutLocal, Date dateFinLocal) {
        if(dateFinLocal.before(dateDebutWS) || dateDebutLocal.after(dateFinWS)){
            return false;
        }else{
            return true;
        }
    }
    @Override
    public String getDetailMessageErreur() {
        StringBuilder messageError = new StringBuilder();
        String errorMessage;
        Message1509 message;
        Iterator<Message1509> iterator = listMessage.iterator();
        while(iterator.hasNext()){
            message = iterator.next();
            errorMessage = getSession().getLabel("APG_RULE_1509");
            errorMessage = errorMessage.replace("{0}", message.getNumCaisse());
            errorMessage = errorMessage.replace("{1}", message.getDebutPeriode());
            errorMessage = errorMessage.replace("{2}", message.getFinPeriode());
            errorMessage = errorMessage.replace("{3}", message.getGenreService());
            messageError.append(errorMessage);
            if(iterator.hasNext()){
                messageError.append("<br>");
            }
        }
        return messageError.toString();
    }


}
