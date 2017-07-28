package globaz.osiris.db.ordres.sepa;

import globaz.framework.util.FWMessage;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.api.ordre.APIOrganeExecution;
import globaz.osiris.db.comptes.CAOperationOrdreRecouvrement;
import globaz.osiris.db.ordres.sepa.AbstractSepa.SepaException;
import globaz.osiris.db.ordres.sepa.utils.CASepaCommonUtils;
import globaz.osiris.db.ordres.sepa.utils.CASepaOGConverterUtils;
import globaz.osiris.db.utils.CAAdressePaiementFormatter;
import globaz.osiris.external.IntAdressePaiement;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.AccountIdentification4Choice;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.BranchAndFinancialInstitutionIdentification4;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.ClearingSystemMemberIdentification2;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.FinancialInstitutionIdentification7;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.GenericOrganisationIdentification1;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.GenericPersonIdentification1;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.LocalInstrument2Choice;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PaymentTypeInformation20;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PersonIdentificationSchemeName1Choice;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.RemittanceInformation5;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.ServiceLevel8Choice;

/**
 * Insérez la description du type ici. Date de création : (08.04.2002 09:05:57)
 * 
 * @author: Administrator
 */
public final class CAFormatRecouvrementISOCHDD extends CAFormatRecouvrementISOAbstract {

    public static final String MODE = "CHDD";

    @Override
    protected PaymentTypeInformation20 givePaymentInformationBLevel() throws PropertiesException {

        final PaymentTypeInformation20 pmtTypeInfo = factory.createPaymentTypeInformation20();
        final LocalInstrument2Choice localInstr = factory.createLocalInstrument2Choice();
        final ServiceLevel8Choice serviceLevel = factory.createServiceLevel8Choice();

        serviceLevel.setPrtry(MODE);
        localInstr.setPrtry(CAProperties.ISO_SEPA_CODE_TYPE_PRELEVEMENT_CHDD.getValue());

        pmtTypeInfo.setSvcLvl(serviceLevel);
        pmtTypeInfo.setLclInstrm(localInstr);

        return pmtTypeInfo;
    }

    @Override
    protected GenericOrganisationIdentification1 giveInformationIdALevel(final APIOrganeExecution organeExecution) {

        final GenericOrganisationIdentification1 partyIdentification = factory
                .createGenericOrganisationIdentification1();

        partyIdentification.setId(organeExecution.getNoAdherent());

        return partyIdentification;
    }

    @Override
    protected AccountIdentification4Choice giveCreditorAccountBLevel(final APIOrdreGroupe og) throws Exception {

        final AccountIdentification4Choice account = factory.createAccountIdentification4Choice();

        account.setIBAN(CASepaOGConverterUtils.getDbtrIBAN(og));
        account.setOthr(CASepaOGConverterUtils.getDbtrNotIBANPain008(og));

        return account;
    }

    @Override
    protected BranchAndFinancialInstitutionIdentification4 giveCreditorAgentBLevel(APIOrganeExecution organeExecution,
            CAAdressePaiementFormatter adpf) {

        final BranchAndFinancialInstitutionIdentification4 creditor = factory
                .createBranchAndFinancialInstitutionIdentification4();
        final FinancialInstitutionIdentification7 financial = factory.createFinancialInstitutionIdentification7();
        final ClearingSystemMemberIdentification2 clearing = factory.createClearingSystemMemberIdentification2();

        clearing.setMmbId(CASepaCommonUtils.CLEARING_POSTAL);
        financial.setClrSysMmbId(clearing);
        creditor.setFinInstnId(financial);

        return creditor;
    }

    @Override
    protected GenericPersonIdentification1 giveInformationIdBLevel(APIOrganeExecution organeExecution) {

        final GenericPersonIdentification1 genPersonId = factory.createGenericPersonIdentification1();
        final PersonIdentificationSchemeName1Choice persIdSchmeName = factory
                .createPersonIdentificationSchemeName1Choice();

        persIdSchmeName.setPrtry(MODE);
        genPersonId.setId(organeExecution.getNoAdherent());
        genPersonId.setSchmeNm(persIdSchmeName);

        return genPersonId;
    }

    @Override
    protected BranchAndFinancialInstitutionIdentification4 giveDebitorAgentCLevel(CAAdressePaiementFormatter adpf) {

        final BranchAndFinancialInstitutionIdentification4 brancheAndFinancialInstId = factory
                .createBranchAndFinancialInstitutionIdentification4();
        final FinancialInstitutionIdentification7 financialInstId = factory.createFinancialInstitutionIdentification7();
        final ClearingSystemMemberIdentification2 clearingSystemMemberId = factory
                .createClearingSystemMemberIdentification2();

        clearingSystemMemberId.setMmbId(CASepaCommonUtils.CLEARING_POSTAL);
        financialInstId.setClrSysMmbId(clearingSystemMemberId);
        brancheAndFinancialInstId.setFinInstnId(financialInstId);

        return brancheAndFinancialInstId;
    }

    @Override
    protected AccountIdentification4Choice giveDebitorAccountCLevel(CAOperationOrdreRecouvrement ordreRecouvrement)
            throws Exception {

        final AccountIdentification4Choice account = factory.createAccountIdentification4Choice();

        account.setIBAN(CASepaOGConverterUtils.getDbtrIBAN(ordreRecouvrement));
        if (account.getIBAN() == null) {
            account.setOthr(CASepaOGConverterUtils.getDbtrNotIBANPain008(ordreRecouvrement));
        }

        return account;
    }

    @Override
    protected RemittanceInformation5 giveRemittanceInfoCLevel(CAOperationOrdreRecouvrement or,
            APIOrganeExecution organeExecution) throws Exception {

        final RemittanceInformation5 remittance = factory.createRemittanceInformation5();

        remittance.setUstrd(or.getMotif());

        return remittance;
    }

    @Override
    protected String giveEndToEndIdCLevel(CAOperationOrdreRecouvrement or) {
        return or.getId();
    }

    @Override
    protected void checkFormat(CAAdressePaiementFormatter adpf) {
        checkTypeAdresse(adpf);
    }

    @Override
    protected void checkFormatEOF(CAAdressePaiementFormatter adpf) {
        checkTypeAdresse(adpf);
    }

    private void checkTypeAdresse(CAAdressePaiementFormatter adpf) {

        final String typeAdresse = CASepaCommonUtils.getTypeAdresseWithIBANPostalEnable(adpf);

        if (!IntAdressePaiement.CCP.equals(typeAdresse)) {
            getMemoryLog().logMessage("5206", adpf.getTypeAdresse(), FWMessage.ERREUR, this.getClass().getName());
            throw new SepaException(getSession().getLabel("5206") + adpf.getTypeAdresse());
        }
    }

    @Override
    protected String giveTypeTransfert() {
        return MODE;
    }
}
