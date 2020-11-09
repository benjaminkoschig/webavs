package globaz.apg.rapg.rules;

import ch.globaz.common.domaine.Date;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.PropertiesException;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.rapg.messages.Message1509;
import globaz.apg.ws.APRapgConsultationUtil;
import globaz.apg.ws.APRapgConsultationUtilV2;
import globaz.jade.properties.JadePropertiesService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Rule1509 extends Rule {
    private List<Message1509> listMessage = new ArrayList<>();
    private String messageErrorPropertyActivation = null;

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
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException {
        boolean isActivated = false;
        if (JadePropertiesService.getInstance().getProperty("apg.rapg.activer.webservice") != null) {
            isActivated = JadePropertiesService.getInstance().getProperty("apg.rapg.activer.webservice").equals("true");
        } else {
            messageErrorPropertyActivation = getSession().getLabel("PROPERTIES_WEBSERVICE_EMPTY");
            return false;
        }
        if (isActivated) {
            String startOfPeriod = champsAnnonce.getStartOfPeriod();
            String endOfPeriod = champsAnnonce.getEndOfPeriod();
            String nss = champsAnnonce.getInsurant();
            String numOffice = champsAnnonce.getDeliveryOfficeOfficeIdentifier();
            String numBranch = champsAnnonce.getDeliveryOfficeBranch();
            validNotEmpty(nss, "NSS");
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");
            nss = nss.replaceAll("\\.", "");
            String wsdlName = CommonPropertiesUtils.getValue(CommonProperties.RAPG_WEBSERVICE_WSDL_PATH);
            if (wsdlName.contains("V2")) {
                List<rapg.v2.ch.eahv_iv.xmlns.eahv_iv_2015_000601._5.RegisterStatusRecordType> list = APRapgConsultationUtilV2.findAnnonces(getSession(), nss, numOffice, numBranch);
                if (list != null) {
                    for (final rapg.v2.ch.eahv_iv.xmlns.eahv_iv_2015_000601._5.RegisterStatusRecordType reccord : list) {
                        addMessageV2(startOfPeriod, endOfPeriod, reccord);
                    }
                }
            } else {
                List<rapg.v1.ch.eahv_iv.xmlns.eahv_iv_2015_000601._4.RegisterStatusRecordType> list = APRapgConsultationUtil.findAnnonces(getSession(), nss, numOffice, numBranch);
                if (list != null) {
                    for (final rapg.v1.ch.eahv_iv.xmlns.eahv_iv_2015_000601._4.RegisterStatusRecordType reccord : list) {
                        addMessageV1(startOfPeriod, endOfPeriod, reccord);
                    }
                }
            }
            if (listMessage.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void addMessageV2(String startOfPeriod, String endOfPeriod, rapg.v2.ch.eahv_iv.xmlns.eahv_iv_2015_000601._5.RegisterStatusRecordType reccord) {
        Message1509 message;
        Date dateDebutWS = new Date(reccord.getStartOfPeriod().toGregorianCalendar().getTime());
        Date dateFinWS = new Date(reccord.getEndOfPeriod().toGregorianCalendar().getTime());
        Date dateDebutLocal = new Date(startOfPeriod);
        Date dateFinLocal = new Date(endOfPeriod);
        if (isChevauchent(dateDebutWS, dateFinWS, dateDebutLocal, dateFinLocal)) {
            message = new Message1509();
            message.setNumCaisse(String.valueOf(reccord.getDeliveryOffice().getOfficeIdentifier()));
            message.setDebutPeriode(dateDebutWS.getSwissValue());
            message.setFinPeriode(dateFinWS.getSwissValue());
            message.setGenreService(reccord.getServiceType().toString());
            listMessage.add(message);
        }
    }

    private void addMessageV1(String startOfPeriod, String endOfPeriod, rapg.v1.ch.eahv_iv.xmlns.eahv_iv_2015_000601._4.RegisterStatusRecordType reccord) {
        Message1509 message;
        Date dateDebutWS = new Date(reccord.getStartOfPeriod().toGregorianCalendar().getTime());
        Date dateFinWS = new Date(reccord.getEndOfPeriod().toGregorianCalendar().getTime());
        Date dateDebutLocal = new Date(startOfPeriod);
        Date dateFinLocal = new Date(endOfPeriod);
        if (isChevauchent(dateDebutWS, dateFinWS, dateDebutLocal, dateFinLocal)) {
            message = new Message1509();
            message.setNumCaisse(String.valueOf(reccord.getDeliveryOffice().getOfficeIdentifier()));
            message.setDebutPeriode(dateDebutWS.getSwissValue());
            message.setFinPeriode(dateFinWS.getSwissValue());
            message.setGenreService(reccord.getServiceType().toString());
            listMessage.add(message);
        }
    }


    private boolean isChevauchent(Date dateDebutWS, Date dateFinWS, Date dateDebutLocal, Date dateFinLocal) {
        if (dateFinLocal.before(dateDebutWS) || dateDebutLocal.after(dateFinWS)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String getDetailMessageErreur() {
        StringBuilder messageError = new StringBuilder();
        String errorMessage;
        Message1509 message;
        Iterator<Message1509> iterator = listMessage.iterator();
        if (messageErrorPropertyActivation == null) {
            while (iterator.hasNext()) {
                message = iterator.next();
                errorMessage = getSession().getLabel("APG_RULE_1509");
                errorMessage = errorMessage.replace("{0}", message.getNumCaisse());
                errorMessage = errorMessage.replace("{1}", message.getDebutPeriode());
                errorMessage = errorMessage.replace("{2}", message.getFinPeriode());
                errorMessage = errorMessage.replace("{3}", message.getGenreService());
                messageError.append(errorMessage);
                if (iterator.hasNext()) {
                    messageError.append("<br>");
                }
            }
            return messageError.toString();
        } else {
            return messageErrorPropertyActivation;
        }

    }


}
