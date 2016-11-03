package ch.globaz.common.process.byitem;

import java.util.Arrays;

public class ProcessItemsService {
    public static boolean isProcessRunnig(String... keyProcess) {
        ProcessManager manager = new ProcessManager();
        manager.setForInKeyProcess(Arrays.asList(keyProcess));
        manager.setForEtat(ProcessState.START);
        try {
            manager.find(10);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return !manager.isEmpty();
    }
}
