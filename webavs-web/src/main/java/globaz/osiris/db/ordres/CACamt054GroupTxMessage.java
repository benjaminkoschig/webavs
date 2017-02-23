package globaz.osiris.db.ordres;

import globaz.osiris.db.ordres.sepa.CACamt054Notification;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CACamt054GroupTxMessage extends AbstractCACamt054Message<CACamt054Notification> {
    private List<CACamt054DetailMessage> details = null;

    public CACamt054GroupTxMessage(final CACamt054Notification object) {
        super(object);
        details = new ArrayList<CACamt054DetailMessage>();
    }

    public void addDetail(final CACamt054DetailMessage detail) {
        details.add(detail);
    }

    @Override
    public String getFormattedMessages() {
        final StringBuilder builder = new StringBuilder();

        if (!messages.isEmpty() || !details.isEmpty()) {

            builder.append("##################################################################################################################");
            builder.append(enteteLevel);

            for (String message : getMessageFromLevel(Level.SEVERE)) {
                builder.append(RETURN_LINE);
                builder.append("[ERROR] " + message);
            }

            for (String message : getMessageFromLevel(Level.WARNING)) {
                builder.append(RETURN_LINE);
                builder.append("[WARNING] " + message);
            }

            for (String message : getMessageFromLevel(Level.INFO)) {
                builder.append(RETURN_LINE);
                builder.append("[INFORMATION] " + message);
            }

            builder.append(RETURN_LINE);
            builder.append("##################################################################################################################");

            for (int i = 0; i < details.size(); i++) {
                builder.append(details.get(i).getFormattedMessages());
            }
        }

        return builder.toString();
    }

    @Override
    public String getFormattedEnteteLevel(CACamt054Notification object) {
        final StringBuilder builder = new StringBuilder();

        builder.append(RETURN_LINE);
        builder.append("Notification information (B Level)");

        builder.append(RETURN_LINE);
        builder.append("- file = {0} ");

        builder.append(RETURN_LINE);
        builder.append("- notificationId = {1} ");

        builder.append(RETURN_LINE);
        builder.append("- identification = {2}");

        return MessageFormat.format(builder.toString(), object.getFile(), object.getNtfctnId(),
                object.getIdentification());
    }

    @Override
    public boolean isOnError() {
        boolean isOnError = false;

        for (int i = 0; i < details.size(); i++) {
            if (details.get(i).isOnError()) {
                isOnError = true;
            }
        }

        return isOnError || !getMessageFromLevel(Level.SEVERE).isEmpty();
    }
}
