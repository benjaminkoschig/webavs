package globaz.prestation.util.errordriller;

import globaz.prestation.util.errordriller.ErrorDriller.DrilledError;
import java.util.List;

public interface ErrorSource {
    List<? extends DrilledError> drill();
}
