package globaz.osiris.db.ordres.sepa;

import globaz.framework.util.FWMessage;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
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
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.CreditorReferenceInformation2;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.CreditorReferenceType1Choice;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.CreditorReferenceType2;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.FinancialInstitutionIdentification7;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.GenericFinancialIdentification1;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.GenericOrganisationIdentification1;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.GenericPersonIdentification1;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.LocalInstrument2Choice;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PaymentTypeInformation20;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.PersonIdentificationSchemeName1Choice;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.RemittanceInformation5;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.ServiceLevel8Choice;
import com.six_interbank_clearing.de.pain_008_001_02_ch_03.StructuredRemittanceInformation7;

public final class CAFormatRecouvrementISOCHTA extends CAFormatRecouvrementISOAbstract {

    public static final String MODE = "CHTA";

    @Override
    protected PaymentTypeInformation20 givePaymentInformationBLevel() throws PropertiesException {

        final PaymentTypeInformation20 pmtTypeInfo = factory.createPaymentTypeInformation20();
        final LocalInstrument2Choice localInstr = factory.createLocalInstrument2Choice();
        final ServiceLevel8Choice serviceLevel = factory.createServiceLevel8Choice();

        serviceLevel.setPrtry(MODE);
        localInstr.setPrtry(CAProperties.ISO_SEPA_CODE_TYPE_PRELEVEMENT_CHTA.getValue());
        pmtTypeInfo.setSvcLvl(serviceLevel);
        pmtTypeInfo.setLclInstrm(localInstr);

        return pmtTypeInfo;
    }

    @Override
    protected GenericOrganisationIdentification1 giveInformationIdALevel(APIOrganeExecution organeExecution) {

        final GenericOrganisationIdentification1 partyIdentification = factory
                .createGenericOrganisationIdentification1();

        partyIdentification.setId(organeExecution.getNoAdherent());

        return partyIdentification;
    }

    @Override
    protected AccountIdentification4Choice giveCreditorAccountBLevel(APIOrdreGroupe og) throws Exception {

        final AccountIdentification4Choice account = factory.createAccountIdentification4Choice();

        account.setIBAN(CASepaOGConverterUtils.getDbtrIBAN(og));

        return account;
    }

    @Override
    protected BranchAndFinancialInstitutionIdentification4 giveCreditorAgentBLevel(APIOrganeExecution organeExecution,
            CAAdressePaiementFormatter adpf) {

        final BranchAndFinancialInstitutionIdentification4 creditor = factory
                .createBranchAndFinancialInstitutionIdentification4();
        final FinancialInstitutionIdentification7 financial = factory.createFinancialInstitutionIdentification7();
        final ClearingSystemMemberIdentification2 clearing = factory.createClearingSystemMemberIdentification2();
        final GenericFinancialIdentification1 other = factory.createGenericFinancialIdentification1();

        other.setId(organeExecution.getNoAdherentBVR());
        clearing.setMmbId(adpf.getClearing());
        financial.setClrSysMmbId(clearing);
        financial.setOthr(other);
        creditor.setFinInstnId(financial);

        return creditor;
    }

    @Override
    protected GenericPersonIdentification1 giveInformationIdBLevel(APIOrganeExecution organeExecution) {

        final GenericPersonIdentification1 genPersonId = factory.createGenericPersonIdentification1();
        final PersonIdentificationSchemeName1Choice persIdSchmeName = factory
                .createPersonIdentificationSchemeName1Choice();

        persIdSchmeName.setPrtry("CHLS");
        genPersonId.setSchmeNm(persIdSchmeName);
        genPersonId.setId(organeExecution.getNoAdherent());

        return genPersonId;
    }

    @Override
    protected BranchAndFinancialInstitutionIdentification4 giveDebitorAgentCLevel(CAAdressePaiementFormatter adpf) {

        final BranchAndFinancialInstitutionIdentification4 creditor = factory
                .createBranchAndFinancialInstitutionIdentification4();
        final FinancialInstitutionIdentification7 financial = factory.createFinancialInstitutionIdentification7();
        final ClearingSystemMemberIdentification2 clearing = factory.createClearingSystemMemberIdentification2();

        clearing.setMmbId(adpf.getClearing());
        financial.setClrSysMmbId(clearing);
        creditor.setFinInstnId(financial);

        return creditor;
    }

    @Override
    protected AccountIdentification4Choice giveDebitorAccountCLevel(CAOperationOrdreRecouvrement ordreRecouvrement)
            throws Exception {

        final AccountIdentification4Choice account = factory.createAccountIdentification4Choice();

        account.setIBAN(CASepaOGConverterUtils.getDbtrIBAN(ordreRecouvrement));

        return account;
    }

    @Override
    protected RemittanceInformation5 giveRemittanceInfoCLevel(CAOperationOrdreRecouvrement or,
            APIOrganeExecution organeExecution) throws Exception {

        final RemittanceInformation5 remittance = factory.createRemittanceInformation5();
        final StructuredRemittanceInformation7 structured = factory.createStructuredRemittanceInformation7();
        final CreditorReferenceInformation2 crdtRef = factory.createCreditorReferenceInformation2();
        final CreditorReferenceType2 crdtRefType = factory.createCreditorReferenceType2();
        final CreditorReferenceType1Choice crdtRefTypeChoice = factory.createCreditorReferenceType1Choice();

        // BVR car on ne fait pas d'international -> IPI (International Payment Instruction)
        crdtRefTypeChoice.setPrtry("ESR");
        crdtRefType.setCdOrPrtry(crdtRefTypeChoice);
        crdtRef.setTp(crdtRefType);
        crdtRef.setRef(getReference(or, organeExecution));

        structured.setCdtrRefInf(crdtRef);
        remittance.setStrd(structured);
        remittance.setUstrd(or.getMotif());

        return remittance;
    }

    private String getReference(CAOperationOrdreRecouvrement or, APIOrganeExecution organe) throws Exception {
        String reference;
        if (!JadeStringUtil.isBlank(or.getReferenceBVR()) && !JadeStringUtil.isBlank(organe.getNumInterneLsv())) {

            if (or.getReferenceBVR().length() > organe.getNumInterneLsv().length()) {
                // TODO : Contrôle et correction !!! CICICAM
                String tmp;
                if (or.getReferenceBVR().startsWith("00" + or.getCompteAnnexe().getIdRole())) {
                    tmp = organe.getNumInterneLsv()
                            + or.getReferenceBVR().substring(organe.getNumInterneLsv().length());
                } else {
                    String compteAnnexe = JadeStringUtil.rightJustifyInteger(or.getCompteAnnexe().getIdCompteAnnexe(),
                            7);
                    tmp = organe.getNumInterneLsv() + "99" + compteAnnexe
                            + or.getReferenceBVR().substring(organe.getNumInterneLsv().length() + 9);
                }

                // Mise à jour du modulo de la référence BVR
                if (tmp.length() == 27) {
                    String partNoChange = tmp.substring(0, 26);
                    tmp = partNoChange + JAUtil.getKeyNumberModulo10(partNoChange);
                }
                reference = tmp;
            } else {
                reference = organe.getNumInterneLsv();
            }
        } else {
            reference = or.getReferenceBVR();
        }

        return reference;
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

        if (!IntAdressePaiement.BANQUE.equals(typeAdresse)) {
            getMemoryLog().logMessage("5206", adpf.getTypeAdresse(), FWMessage.ERREUR, this.getClass().getName());
            throw new SepaException(getSession().getLabel("5206") + adpf.getTypeAdresse());
        }
    }

    @Override
    protected String giveTypeTransfert() {
        return MODE;
    }
}
