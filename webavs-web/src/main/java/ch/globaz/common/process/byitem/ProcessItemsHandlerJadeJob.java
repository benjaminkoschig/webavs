package ch.globaz.common.process.byitem;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThreadActivator;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.smtp.JadeSmtpClient;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.globaz.common.LabelCommonProvider;
import ch.globaz.common.exceptions.ExceptionFormatter;
import ch.globaz.common.jadedb.TransactionWrapper;
import ch.globaz.common.process.ProcessMailUtils;
import com.google.common.base.Throwables;

/**
 * 
 * Si l'item est n'as pas d'erreur ou d'exception celui-ci est commiter sinon il est rollbacker.
 * Si une exception est levée au niveau général du processus celui-ci envoie un mail avec l'erreur.
 * Si après la dixième itération du traitement des items il y a 10 erreurs le processus se stop
 * 
 * 
 * @param <T> C'est l'item que l'on vas traiter
 */
public abstract class ProcessItemsHandlerJadeJob<T extends ProcessItem> extends AbstractJadeJob implements
        ProcessItems<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessItem.class);
    private transient List<T> items = new ArrayList<T>();

    private transient ExceptionFormatterResolver exceptionFormatterResolver = new ExceptionFormatterResolver();
    private boolean mailErrorIsSend = false;
    private boolean processOnError = false;
    private boolean notStopProcessIfHasTooMutchErrors = false;
    private ProcessItemsJobInfos jobInfos;
    private int nbErrorsBeforStop = 10;
    private boolean parallelWithSession = false;
    private boolean parallelWitheOutSession = false;

    public ProcessItemsHandlerJadeJob<T> parallelWithSession() {
        this.parallelWithSession = true;
        return this;
    }

    public ProcessItemsHandlerJadeJob<T> parallelWithOutSession() {
        this.parallelWitheOutSession = true;
        return this;
    }

    public ProcessItemsHandlerJadeJob<T> notStopProcessIfHasTooMutchErrors() {
        notStopProcessIfHasTooMutchErrors = true;
        return this;
    }

    protected void addExceptionFormatter(ExceptionFormatter<?> formatter) {
        exceptionFormatterResolver.addFormatter(formatter);
    }

    /**
     * Permet d'avoir des informations sur le processus lancée.
     * 
     * @return ProcessItemsJobInfos
     */
    public ProcessItemsJobInfos getJobInfos() {
        return jobInfos;
    }

    @Override
    public void run() {
        TransactionWrapper transaction = TransactionWrapper.forforceCommit(getSession());
        StopWatch time = new StopWatch();
        ProcessEntity processEntity = new ProcessEntity();
        processEntity.setSession(getSession());
        boolean isAfterCall = false;
        try {
            processEntity.setKey(getKey());
            processEntity.setEtat(ProcessState.START);
            processEntity.setStartDate(new Date());
            processEntity.setUser(getSession().getUserId());
            processEntity.persist(transaction);
            transaction.commit();
            time.start();
            jobInfos = new ProcessItemsJobInfos(processEntity.getId());

            before();

            processEntity.setTimeBefore(time.getTime());
            processEntity.setEtat(ProcessState.START);
            items = resolveItems();
            processEntity.setNbEntityTotal(items.size());
            processEntity.persist(transaction);
            time.stop();

            transaction.commit();

            time.reset();
            time.start();
            treat();
            time.stop();

            processEntity.setTimeEntity(time.getTime());
            processEntity.setNbEntityInError(this.countNbItemInError());
            processEntity.persist(transaction);

            jobInfos = new ProcessItemsJobInfos(processEntity.getId(), processEntity.getTimeEntity(),
                    processEntity.getTimeBefore(), processEntity.getNbEntityTotal(), processEntity.getNbEntityInError());

            transaction.commit();
            time.reset();
            time.start();
            isAfterCall = true; // on le met avant car si il y a une exception qui est déclencher il ne faut pas
                                // relancer la fonction after()
            after();
            time.stop();

            processEntity.setTimeAfter(time.getTime());
            processEntity.setEtat(ProcessState.FINISH);
            processEntity.setEndDate(new Date());
            processEntity.persist(transaction);
            transaction.commit();

        } catch (Exception e) {
            try {
                processOnError = true;
                processEntity.setEtat(ProcessState.ERROR);
                processEntity.setEndDate(new Date());
                transaction.clearThreadLog();
                processEntity.persist(transaction);
                transaction.addThreadLog();
                transaction.forceCommit();
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
            if (!isAfterCall) {
                after();
            }
        }
    }

    private void treat() {
        getProgressHelper().setMax(this.items.size());
        if (parallelWithSession) {
            entitiesTreatWithThread();
        } else if (parallelWitheOutSession) {
            entitiesTreatWithThreadNoSession();
        } else {
            entitiesTreat();
        }
    }

    private void entitiesTreatWithThread() {
        ExecutorService threadExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        final BSession session = BSessionUtil.getSessionFromThreadContext();
        try {
            session.getCurrentThreadTransaction().commit();
        } catch (Exception e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        final AtomicInteger i = new AtomicInteger(0);
        for (final T item : items) {
            threadExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        getProgressHelper().setCurrent(i.getAndIncrement());
                        BSessionUtil.initContext(session, this);
                        try {

                            BTransaction transcation = session.getCurrentThreadTransaction();
                            item.treat();

                            // Attention les warning ne sont pas gérés.
                            // TODO

                            if (transcation.hasErrors()) {
                                item.addErrors(transcation.getErrors().toString());
                                transcation.clearErrorBuffer();
                            }

                            if (session.hasErrors()) {
                                item.addErrors(session.getErrors().toString());
                            }

                            if (item.hasErrorOrException()) {
                                session.getCurrentThreadTransaction().rollback();
                            } else {
                                session.getCurrentThreadTransaction().commit();
                            }
                        } catch (Exception e) {
                            item.catchException(e);
                            try {
                                session.getCurrentThreadTransaction().rollback();
                            } catch (Exception e1) {
                                throw new RuntimeException("IMPOSSIBLE DE ROOLLBACKER :" + e.toString(), e1);
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        JadeThreadActivator.stopUsingContext(this);
                    }
                }
            });
        }
        threadExecutor.shutdown();

        while (!threadExecutor.isTerminated()) {
            if (mustStopProcess(i.get())) {
                threadExecutor.shutdownNow();
                this.sendMailIfHasError(translate("PROCESS_ITEMS_TOO_MANY_ERRORS") + " - " + translateName());
            }
        }
    }

    private void entitiesTreatWithThreadNoSession() {
        ExecutorService threadExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

        final AtomicInteger i = new AtomicInteger(0);
        for (final T item : items) {
            threadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        getProgressHelper().setCurrent(i.getAndIncrement());
                        try {
                            item.treat();
                        } catch (Exception e) {
                            item.catchException(e);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        threadExecutor.shutdown();

        while (!threadExecutor.isTerminated()) {
            if (mustStopProcess(i.get())) {
                threadExecutor.shutdownNow();
                this.sendMailIfHasError(translate("PROCESS_ITEMS_TOO_MANY_ERRORS") + " - " + translateName());
            }
        }
    }

    public boolean isOnError() {
        return processOnError;
    }

    /**
     * Retourne la liste des items traités ceci peut être null si le processus de traitement n’a pas été exécuté
     * 
     * @return List<T>
     */
    public List<T> getItmes() {
        return items;
    }

    /**
     * Permet d’envoyer un mail si des erreurs ou exception sont définies dans les items. Si il y n’y pas d’erreur aucun
     * mail n’est envoyé.
     */
    public void sendMailIfHasError() {
        this.sendMailIfHasError(translate("PROCESS_ITEMS_DETECTED_ERRORS") + " - " + translateName());
    }

    /**
     * Permet d’envoyer un mail si des erreurs ou exception sont définies dans les items. Si il y n’y pas d’erreur aucun
     * mail n’est envoyé.
     */
    public void sendMailIfHasError(String body) {
        this.sendMailIfHasError(translate("PROCESS_ITEMS_DETECTED_ERRORS") + " - " + translateName(), body, null);
    }

    /**
     * Permet d’envoyer un mail si des erreurs ou exception sont définies dans les items. Si il y n’y pas d’erreur aucun
     * mail n’est envoyé.
     */
    public void sendMailIfHasErrorWithFile(String body, String... files) {
        this.sendMailIfHasError(translate("PROCESS_ITEMS_DETECTED_ERRORS") + " - " + translateName(), body, files);
    }

    private void entitiesTreat() {
        Integer i = 0;
        for (T item : items) {
            i++;
            getProgressHelper().setCurrent(i);

            treat(i, item, getSession());
            // on stop le process si les entités devient toutes en erreurs ou si il y en à trop.
            if (mustStopProcess(i)) {
                this.sendMailIfHasError(translate("PROCESS_ITEMS_TOO_MANY_ERRORS") + " - " + translateName());
                break;
            }
        }
    }

    private void treat(Integer i, T item, BSession session) {
        try {

            BTransaction transcation = session.getCurrentThreadTransaction();
            item.treat();

            // Attention les warning ne sont pas gérés.
            // TODO

            if (transcation.hasErrors()) {
                item.addErrors(transcation.getErrors().toString());
                transcation.clearErrorBuffer();
            }

            if (session.hasErrors()) {
                item.addErrors(session.getErrors().toString());
            }

            if (item.hasErrorOrException()) {
                session.getCurrentThreadTransaction().rollback();
            } else {
                session.getCurrentThreadTransaction().commit();
            }
        } catch (Exception e) {
            item.catchException(e);
            try {
                session.getCurrentThreadTransaction().rollback();
            } catch (Exception e1) {
                throw new RuntimeException("IMPOSSIBLE DE ROOLLBACKER :" + e.toString(), e1);
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
        return (this.countNbItemInError() == nbErrorsBeforStop || (this.countNbItemInError() == i && this
                .countNbItemInError() == 100)) && !notStopProcessIfHasTooMutchErrors;
    }

    private void sendMailIfHasError(String objet, String body, String[] files) {

        if (!mailErrorIsSend) {
            StringBuilder bodyMail = buildBody(body);
            if (itemsHasError() || getSession().hasErrors()) {
                this.sendMail(objet, bodyMail.toString(), files);
                this.mailErrorIsSend = true;
            }
        }
    }

    public StringBuilder buildBody(String body) {
        StringBuilder bodyMail = new StringBuilder();
        if (body != null && !body.isEmpty()) {
            bodyMail.append("<br/>" + body + "<br/>");
        }
        if (itemsHasError()) {
            bodyMail.append("<b>" + translate("PROCESS_ITEMS_ERRORS_IN_ITEMS") + "</b>" + "\n \n");
            createListErrors(items, bodyMail);
        }

        if (getSession().hasErrors()) {
            bodyMail.append(getSession().getErrors().toString());
        }
        return bodyMail;
    }

    private void sendMail(String objet, String body, String[] files) {
        try {
            JadeSmtpClient.getInstance().sendMail(getSession().getUserEMail(), objet, body, files);
        } catch (Exception e) {
            LOG.error("Unable to send mail", e);
        }
    }

    protected Integer countNbItemInError() {
        Integer i = 0;
        for (T item : this.items) {
            if (item.hasErrorOrException()) {
                i++;
            }
        }
        return i;
    }

    protected boolean itemsHasError() {
        if (items != null) {
            for (T item : items) {
                if (item.hasErrorOrException()) {
                    return true;
                }
            }
        }
        return false;
    }

    private int countNbItemInErrors(List<T> items) {
        int nb = 0;
        for (ProcessItem item : items) {
            if (item.hasErrorOrException()) {
                nb++;
            }
        }
        return nb;
    }

    private void createListErrors(List<T> items, StringBuilder bodyMail) {
        Locale local = new Locale(getSession().getIdLangueISO());
        bodyMail.append("<br/> NB:" + countNbItemInErrors(items) + "<br/>");

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
                    ExceptionFormatter formatter = this.exceptionFormatterResolver.resolve(item.getException()
                            .getClass());

                    if (formatter != null) {
                        bodyMail.append("<li>");
                        bodyMail.append(formatter.formatte(item.getException(), local));
                        bodyMail.append("</li>");
                    } else {
                        bodyMail.append("<li>");
                        bodyMail.append(Throwables.getStackTraceAsString(item.getException()));
                        bodyMail.append("</li>");
                    }
                }
                bodyMail.append("</ul>");
            }
        }
    }
}
