package globaz.osiris.db.ordres.sepa;

import globaz.framework.util.FWCurrency;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.parser.IntBVRPojo;
import iso.std.iso._20022.tech.xsd.camt_054_001.AccountNotification7;
import iso.std.iso._20022.tech.xsd.camt_054_001.BankToCustomerDebitCreditNotificationV04;
import iso.std.iso._20022.tech.xsd.camt_054_001.CreditDebitCode;
import iso.std.iso._20022.tech.xsd.camt_054_001.EntryDetails3;
import iso.std.iso._20022.tech.xsd.camt_054_001.EntryTransaction4;
import iso.std.iso._20022.tech.xsd.camt_054_001.PartyIdentification43;
import iso.std.iso._20022.tech.xsd.camt_054_001.ReportEntry4;
import iso.std.iso._20022.tech.xsd.camt_054_001.TransactionParties3;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CACamt054BVRV0104 extends AbstractCamt054Version<iso.std.iso._20022.tech.xsd.camt_054_001.Document> {

    public CACamt054BVRV0104() {
        super(iso.std.iso._20022.tech.xsd.camt_054_001.Document.class);
    }

    @Override
    protected List<CACamt054Notification> constructPojos(iso.std.iso._20022.tech.xsd.camt_054_001.Document document,
            String fileName) {

        final List<CACamt054Notification> notifications = new ArrayList<CACamt054Notification>();

        if (document == null) {
            return notifications;
        }

        final BankToCustomerDebitCreditNotificationV04 bankToCustomer = document.getBkToCstmrDbtCdtNtfctn();

        if (bankToCustomer == null) {
            return notifications;
        }

        final String messageId = getMessageId(bankToCustomer);
        for (final AccountNotification7 ntfc : bankToCustomer.getNtfctn()) {
            final String notificationId = ntfc.getId();
            final String createdDateTime = getCreatedDateTime(ntfc);
            final String identification = getIdentification(ntfc);

            final CACamt054Notification notification = new CACamt054Notification();
            notification.setMsgId(messageId);
            notification.setNtfctnId(notificationId);
            notification.setCreDtTm(createdDateTime);
            notification.setFile(fileName);
            notification.setIdentification(identification);

            for (final ReportEntry4 entry : ntfc.getNtry()) {
                final CACamt054GroupTransaction groupTx = new CACamt054GroupTransaction();
                groupTx.setNtryRef(entry.getNtryRef());
                groupTx.setCtrlAmount(getMontantControle(entry));
                groupTx.setCrdtDbtIndicator(getCreditOrDebitInterne(entry.getCdtDbtInd()));
                groupTx.setNoAdherent(entry.getNtryRef());
                groupTx.setStatus(getStatusEntry(entry));
                groupTx.setDomainCode(getDomainCodeEntry(entry));
                groupTx.setFamilyCode(getFamilyCodeEntry(entry));
                groupTx.setSubFamilyCode(getSubFamilyCode(entry));
                groupTx.setReversalIndication(entry.isRvslInd());

                final String bxTxCd = getBxTxCdEntry(groupTx);

                final String accountServicerRef = entry.getAcctSvcrRef();

                // date formatter
                final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                final String noAdherent = entry.getNtryRef();
                final String referenceInterne = entry.getAcctSvcrRef();
                final String dateInscription = getDateInscription(entry, sdf);
                final String dateTraitement = getDateTraitement(entry, sdf);

                for (EntryDetails3 detail : entry.getNtryDtls()) {

                    groupTx.setNbTransactions(calculNbTransactions(groupTx, detail));

                    // Création d'un BVR pojo par BVR
                    for (EntryTransaction4 tx : detail.getTxDtls()) {
                        final CACamt054Transaction pojo = new CACamt054Transaction();

                        pojo.setDateDepot(manageAccptnDtTime(sdf, tx));
                        pojo.setDateInscription(dateInscription);
                        pojo.setDateTraitement(dateTraitement);

                        pojo.setGenreEcriture(getCreditOrDebitInterne(tx.getCdtDbtInd()));

                        pojo.setGenreTransaction(managePrtryTp(tx));

                        pojo.setMontant(getMontantTx(tx));
                        pojo.setNumeroAdherent(noAdherent);

                        pojo.setNumeroReference(manageCdtrRefInf(tx));
                        pojo.setReferenceInterne(referenceInterne);
                        pojo.setBankTransactionCode(getBxTxCdTransaction(bxTxCd, tx));
                        pojo.setAccountServicerReference(manageAccountServierRef(accountServicerRef, tx));
                        pojo.setDebtor(manageDebtor(tx.getRltdPties()));

                        // set to null never used param
                        pojo.setCodeRejet(null);
                        pojo.setTaxeTraitement(null);
                        pojo.setTaxeVersement(null);

                        groupTx.getListTransactions().add(pojo);
                    }
                }

                notification.getListGroupTxs().add(groupTx);
            }

            notifications.add(notification);
        }

        return notifications;
    }

    private String getMontantTx(EntryTransaction4 tx) {
        if (tx.getAmt() != null && tx.getAmt().getValue() != null) {
            return String.valueOf(tx.getAmt().getValue());
        }
        return null;
    }

    private Integer calculNbTransactions(final CACamt054GroupTransaction groupTx, EntryDetails3 detail) {
        if (detail.getBtch() != null && !JadeStringUtil.isEmpty(detail.getBtch().getNbOfTxs())) {
            return groupTx.getNbTransactions() + Integer.valueOf(detail.getBtch().getNbOfTxs());
        }
        return groupTx.getNbTransactions();
    }

    private String getDateTraitement(final ReportEntry4 entry, final SimpleDateFormat sdf) {
        if (entry.getValDt() != null && entry.getValDt().getDt() != null) {
            return sdf.format(entry.getValDt().getDt().toGregorianCalendar().getTime());
        }
        return null;
    }

    private String getDateInscription(final ReportEntry4 entry, final SimpleDateFormat sdf) {
        if (entry.getBookgDt() != null && entry.getBookgDt().getDt() != null) {
            return sdf.format(entry.getBookgDt().getDt().toGregorianCalendar().getTime());
        }
        return null;
    }

    private String getBxTxCdEntry(final CACamt054GroupTransaction groupTx) {
        String bxTxCdEntry = "";

        if (!JadeStringUtil.isEmpty(groupTx.getDomainCode())) {
            bxTxCdEntry += groupTx.getDomainCode();
        }

        bxTxCdEntry += "/";

        if (!JadeStringUtil.isEmpty(groupTx.getFamilyCode())) {
            bxTxCdEntry += groupTx.getFamilyCode();
        }

        bxTxCdEntry += "/";

        if (!JadeStringUtil.isEmpty(groupTx.getSubFamilyCode())) {
            bxTxCdEntry += groupTx.getSubFamilyCode();
        }

        return bxTxCdEntry;
    }

    private String getStatusEntry(final ReportEntry4 entry) {
        if (entry.getSts() != null) {
            return entry.getSts().name();
        }
        return null;
    }

    private FWCurrency getMontantControle(final ReportEntry4 entry) {
        if (entry.getAmt() != null && entry.getAmt().getValue() != null) {
            return new FWCurrency(entry.getAmt().getValue().doubleValue());
        }
        return null;
    }

    private String getSubFamilyCode(final ReportEntry4 entry) {
        if (entry.getBkTxCd() != null && entry.getBkTxCd().getDomn() != null
                && entry.getBkTxCd().getDomn().getFmly() != null
                && !JadeStringUtil.isEmpty(entry.getBkTxCd().getDomn().getFmly().getSubFmlyCd())) {
            return entry.getBkTxCd().getDomn().getFmly().getSubFmlyCd();
        }
        return null;
    }

    private String getFamilyCodeEntry(final ReportEntry4 entry) {
        if (entry.getBkTxCd() != null && entry.getBkTxCd().getDomn() != null
                && entry.getBkTxCd().getDomn().getFmly() != null
                && !JadeStringUtil.isEmpty(entry.getBkTxCd().getDomn().getFmly().getCd())) {
            return entry.getBkTxCd().getDomn().getFmly().getCd();
        }
        return null;
    }

    private String getDomainCodeEntry(final ReportEntry4 entry) {
        if (entry.getBkTxCd() != null && entry.getBkTxCd().getDomn() != null
                && !JadeStringUtil.isEmpty(entry.getBkTxCd().getDomn().getCd())) {
            return entry.getBkTxCd().getDomn().getCd();
        }
        return null;
    }

    private String getCreatedDateTime(final AccountNotification7 ntfc) {
        return ntfc.getCreDtTm() != null ? String.valueOf(ntfc.getCreDtTm()) : null;
    }

    private String getIdentification(final AccountNotification7 ntfc) {
        String identification = null;

        if (ntfc.getAcct() != null && ntfc.getAcct().getId() != null
                && !JadeStringUtil.isEmpty(ntfc.getAcct().getId().getIBAN())) {
            identification = ntfc.getAcct().getId().getIBAN();
        } else {
            if (ntfc.getAcct().getId().getOthr() != null
                    && !JadeStringUtil.isEmpty(ntfc.getAcct().getId().getOthr().getId())) {
                identification = ntfc.getAcct().getId().getOthr().getId();
            }
        }

        return identification;
    }

    private String getMessageId(final BankToCustomerDebitCreditNotificationV04 bkToCstmrDbtCdtNtfctn) {
        if (bkToCstmrDbtCdtNtfctn.getGrpHdr() != null
                && !JadeStringUtil.isEmpty(bkToCstmrDbtCdtNtfctn.getGrpHdr().getMsgId())) {
            return bkToCstmrDbtCdtNtfctn.getGrpHdr().getMsgId();
        }
        return null;
    }

    private String getBxTxCdTransaction(final String bxTxCd, EntryTransaction4 tx) {
        String message = bxTxCd + " - ";

        if (tx.getBkTxCd() != null && tx.getBkTxCd().getDomn() != null) {
            message += tx.getBkTxCd().getDomn().getCd();
        }

        message += "/";

        if (tx.getBkTxCd() != null && tx.getBkTxCd().getDomn() != null && tx.getBkTxCd().getDomn().getFmly() != null
                && !JadeStringUtil.isEmpty(tx.getBkTxCd().getDomn().getFmly().getCd())) {
            message += tx.getBkTxCd().getDomn().getFmly().getCd();
        }

        message += "/";

        if (tx.getBkTxCd() != null && tx.getBkTxCd().getDomn() != null && tx.getBkTxCd().getDomn().getFmly() != null
                && !JadeStringUtil.isEmpty(tx.getBkTxCd().getDomn().getFmly().getSubFmlyCd())) {
            message += tx.getBkTxCd().getDomn().getFmly().getSubFmlyCd();
        }

        return message;
    }

    private String manageAccountServierRef(final String accountServicerRef, EntryTransaction4 tx) {
        String message = accountServicerRef;

        if (tx.getRefs() != null && !JadeStringUtil.isEmpty(tx.getRefs().getAcctSvcrRef())) {
            message += " - " + tx.getRefs().getAcctSvcrRef();
        }

        return message;
    }

    private String getCreditOrDebitInterne(final CreditDebitCode genreEcriture) {
        if (CreditDebitCode.DBIT.equals(genreEcriture)) {
            return IntBVRPojo.GENRE_DEBIT;
        } else {
            return IntBVRPojo.GENRE_CREDIT;
        }
    }

    private String manageAccptnDtTime(final SimpleDateFormat sdf, EntryTransaction4 tx) {
        if (tx.getRltdDts() != null && tx.getRltdDts().getAccptncDtTm() != null) {
            return sdf.format(tx.getRltdDts().getAccptncDtTm().toGregorianCalendar().getTime());
        }
        return null;
    }

    /**
     * avoid null pointer
     * 
     * @param tx
     * @return
     */
    private static String manageCdtrRefInf(EntryTransaction4 tx) {
        if (tx.getRmtInf() != null && tx.getRmtInf().getStrd() != null && !tx.getRmtInf().getStrd().isEmpty()
                && tx.getRmtInf().getStrd().get(0).getCdtrRefInf() != null) {
            return tx.getRmtInf().getStrd().get(0).getCdtrRefInf().getRef();
        }
        return null;
    }

    /**
     * avoid null pointer
     * 
     * @param tx
     * @return
     */
    private static String managePrtryTp(EntryTransaction4 tx) {
        if (tx.getRefs() != null && tx.getRefs().getPrtry() != null && !tx.getRefs().getPrtry().isEmpty()) {
            return tx.getRefs().getPrtry().get(0).getTp();
        }
        return null;
    }

    private static String manageDebtor(TransactionParties3 partie3) {

        if (partie3 == null) {
            return null;
        }

        PartyIdentification43 debtor = partie3.getDbtr();

        if (debtor == null) {
            return null;
        }

        final String newLine = "&#13;&#10;";

        final StringBuilder builder = new StringBuilder();
        builder.append(debtor.getNm()).append(newLine);

        if (debtor.getPstlAdr() != null && debtor.getPstlAdr().getAdrLine() != null
                && !debtor.getPstlAdr().getAdrLine().isEmpty()) {
            for (final String line : debtor.getPstlAdr().getAdrLine()) {
                builder.append(line).append(newLine);
            }
        } else {
            if (debtor.getPstlAdr() != null) {
                builder.append(debtor.getPstlAdr().getStrtNm() + " " + debtor.getPstlAdr().getBldgNb()).append(newLine);
                builder.append(debtor.getPstlAdr().getPstCd() + " " + debtor.getPstlAdr().getTwnNm() + " "
                        + debtor.getPstlAdr().getCtry());
            }
        }

        return builder.toString();
    }
}
