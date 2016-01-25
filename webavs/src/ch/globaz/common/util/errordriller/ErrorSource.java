package ch.globaz.common.util.errordriller;

import java.util.List;
import ch.globaz.common.util.errordriller.ErrorDriller.DrilledError;

public interface ErrorSource {
    List<? extends DrilledError> drill();
}
