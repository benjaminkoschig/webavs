package globaz.osiris.db.ordres.sepa;

import globaz.osiris.parser.IntBVRPojo;
import java.text.MessageFormat;
import java.util.logging.Level;

public class CACamt054DetailMessage extends CACamt054MessageAbstract<IntBVRPojo> {

    public CACamt054DetailMessage(final IntBVRPojo object) {
        super(object, null);
    }

    public CACamt054DetailMessage(final IntBVRPojo object, final String numeroReference) {
        super(object, numeroReference);
    }

    @Override
    public String getFormattedEnteteLevel(IntBVRPojo object, String numeroReference) {
        final StringBuilder builder = new StringBuilder();

        builder.append(RETURN_LINE);
        builder.append("Transaction Information (C/D Level)");

        builder.append(RETURN_LINE);
        builder.append("- reference = {0} ");

        builder.append(RETURN_LINE);
        builder.append("- amount = {1} ");

        builder.append(RETURN_LINE);
        builder.append("- accountserviceref = {2} ");

        builder.append(RETURN_LINE);
        builder.append("- banktransactioncode = {3}");

        return MessageFormat.format(builder.toString(),
                numeroReference != null ? numeroReference : object.getNumeroReference(), object.getMontant(),
                object.getAccountServicerReference(), object.getBankTransactionCode());
    }

    @Override
    public String getFormattedMessages() {
        final StringBuilder builder = new StringBuilder();

        if (!messages.isEmpty()) {
            builder.append(RETURN_LINE);
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
        }

        return builder.toString();
    }

    @Override
    public boolean isOnError() {
        return !getMessageFromLevel(Level.SEVERE).isEmpty();
    }
}
