package globaz.osiris.db.ordres;

import java.util.ArrayList;
import java.util.List;

public class CACamt054GroupsMessage {
    private List<CACamt054GroupTxMessage> groups = null;

    public CACamt054GroupsMessage() {
        groups = new ArrayList<CACamt054GroupTxMessage>();
    }

    public void addGroup(CACamt054GroupTxMessage group) {
        groups.add(group);
    }

    public String getMessage() {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < groups.size(); i++) {
            builder.append("\r\n ");
            builder.append(groups.get(i).getFormattedMessages());
        }

        return builder.toString();
    }

    public boolean hasErrors() {
        boolean isOnError = false;
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).isOnError()) {
                isOnError = true;
            }
        }
        return isOnError;
    }
}
