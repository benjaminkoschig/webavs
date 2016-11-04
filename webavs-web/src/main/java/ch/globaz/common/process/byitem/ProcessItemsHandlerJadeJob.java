package ch.globaz.common.process.byitem;

import globaz.jade.job.AbstractJadeJob;
import globaz.jade.smtp.JadeSmtpClient;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.LabelCommonProvider;
import ch.globaz.common.jadedb.TransactionWrapper;
import ch.globaz.common.process.ProcessMailUtils;
import com.google.common.base.Throwables;

public abstract class ProcessItemsHandlerJadeJob<T extends ProcessItem> extends AbstractJadeJob implements
        ProcessItems<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessItem.class);
    private transient List<T> items;
    private boolean mailErrorIsSend = false;
    private boolean isAfterCall = false;
    private ProcessItemsJobInfos jobInfos;

    public ProcessItemsJobInfos getJobInfos() {
        return jobInfos;
    }

    @Override
    public void run() {
        TransactionWrapper transaction = TransactionWrapper.forforceCommit(getSession());
        StopWatch time = new StopWatch();
        ProcessEntity processEntity = new ProcessEntity();
        processEntity.setSession(getSession());

        try {
            processEntity.setEtat(ProcessState.START);
            processEntity.setStartDate(new Date());
            processEntity.setUser(getSession().getUserId());
            processEntity.persist(transaction);
            transaction.commit();
            jobInfos = new ProcessItemsJobInfos(processEntity.getId());
            time.start();
            before();
            time.stop();

            processEntity.setTimeBefore(time.getTime());
            processEntity.setEtat(ProcessState.START);
            processEntity.persist(transaction);
            transaction.commit();

            time.reset();
            time.start();
            entitiesTreat();
            time.stop();

            processEntity.setTimeEntity(time.getTime());
            processEntity.setNbEntityTotal(items.size());
            processEntity.setNbEntityInError(this.countNbItemInError());
            processEntity.persist(transaction);
            transaction.commit();
            time.reset();
            time.start();
            after();
            this.isAfterCall = true;
            time.stop();

            processEntity.setTimeAfter(time.getTime());
            processEntity.setEtat(ProcessState.FINISH);
            processEntity.setEndDate(new Date());

            processEntity.persist(transaction);
            transaction.commit();
        } catch (Exception e) {
            try {
                processEntity.setEtat(ProcessState.ERROR);
                processEntity.persist(transaction);
            } catch (Exception e1) {
                LOG.error("Unable to persist the process state", e1);
            }
            try {
                ProcessMailUtils.sendMailError(getSession().getUserEMail(), e, this, "");
            } catch (Exception e1) {
                LOG.error("Unable to send the mail", e1);
            }
        } finally {
            transaction.close();
            if (!this.isAfterCall) {
                after();
            }
        }
    }

    void thread() {
        items = resolveItems();

        ExecutorService threadExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        for (T item : items) {
            threadExecutor.execute(item);
        }
        threadExecutor.shutdown();

        while (!threadExecutor.isTerminated()) {
        }
    }

    public List<T> getItmes() {
        return items;
    }

    public void entitiesTreat() {
        items = resolveItems();
        getProgressHelper().setMax(this.items.size());
        Integer i = 0;
        for (T item : items) {
            try {
                i++;
                getProgressHelper().setCurrent(i);
                item.treat();
                // on stop le process si les entités devient toutes en erreurs ou si il y en à trop.
                if (mustStopProcess(i)) {
                    this.sendMailIfHasError(translate("PROCESS_ITEMS_TOO_MANY_ERRORS") + " - " + translateName());
                    break;
                }
                if (this.itemsHasError()) {
                    getSession().getCurrentThreadTransaction().rollback();
                } else {
                    getSession().getCurrentThreadTransaction().commit();
                }
            } catch (Exception e) {
                item.catchException(e);
                try {
                    getSession().getCurrentThreadTransaction().rollback();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                // on stop le process si les entités devient toutes en erreurs ou si il y en à trop.
                if (mustStopProcess(i)) {
                    this.sendMailIfHasError(translate("PROCESS_ITEMS_TOO_MANY_ERRORS") + " - " + translateName());
                    break;
                }
            }
        }
    }

    private String translateName() {
        return getSession().getLabel(getName());
    }

    private String translate(String idMessage) {
        return LabelCommonProvider.getLabel(idMessage, getSession().getIdLangueISO());
    }

    private String translate(Entry<String, List<String>> entry) {
        return MessageFormat.format(getSession().getLabel(entry.getKey()).replace("'", "'\'"), entry.getValue()
                .toArray());
    }

    private boolean mustStopProcess(Integer i) {
        return this.countNbItemInError() == 10 && this.countNbItemInError() == i || this.countNbItemInError() == 100;
    }

    public void sendMailIfHasError() {
        this.sendMailIfHasError(translate("PROCESS_ITEMS_DETECTED_ERRORS") + " - " + translateName());
    }

    private void sendMailIfHasError(String objet) {

        if (!mailErrorIsSend) {
            StringBuilder bodyMail = new StringBuilder();

            if (itemsHasError()) {
                bodyMail.append("<b>" + translate("PROCESS_ITEMS_ERRORS_IN_ITEMS") + "</b>" + "\n \n");
                createListErrors(items, bodyMail);
            }

            if (getSession().hasErrors()) {
                bodyMail.append(getSession().getErrors().toString());
            }

            if (itemsHasError() || getSession().hasErrors()) {
                this.sendMail(objet, bodyMail.toString());
                this.mailErrorIsSend = true;
            }
        }
    }

    private void sendMail(String objet, String body) {
        try {
            JadeSmtpClient.getInstance().sendMail(getSession().getUserEMail(), objet, body, null);
        } catch (Exception e) {
            LOG.error("Unable to send mail", e);
        }
    }

    private Integer countNbItemInError() {
        Integer i = 0;
        for (T item : this.items) {
            if (item.hasErrorOrException()) {
                i++;
            }
        }
        return i;
    }

    private boolean itemsHasError() {
        for (T item : items) {
            if (item.hasErrorOrException()) {
                return true;
            }
        }
        return false;
    }

    private void createListErrors(List<T> items, StringBuilder bodyMail) {
        for (ProcessItem item : items) {
            if (item.hasErrorOrException()) {
                bodyMail.append("<b> " + item.getDescription() + " </b>");
                bodyMail.append("<ul>");
                for (Entry<String, List<String>> entry : item.getErrors().entrySet()) {
                    bodyMail.append("<li>");
                    bodyMail.append(translate(entry));
                    bodyMail.append("</li>");
                }

                if (item.hasException()) {
                    bodyMail.append("<li>");
                    bodyMail.append(Throwables.getStackTraceAsString(item.getException()));
                    bodyMail.append("</li>");
                }
                bodyMail.append("</ul>");
            }
        }
    }

}
