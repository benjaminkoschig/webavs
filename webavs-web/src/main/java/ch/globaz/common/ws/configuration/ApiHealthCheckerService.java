package ch.globaz.common.ws.configuration;

import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.ws.ApiHealthChecker;
import ch.globaz.common.ws.HealthDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
class ApiHealthCheckerService {

    private static List<ApiHealthChecker> apiHealthCheckers;

    static void check(List<Class<ApiHealthChecker>> apiHealthCheckerClass) {
        ApiHealthCheckerService.apiHealthCheckers = apiHealthCheckerClass.stream()
                                                                         .map(it -> Exceptions.checkedToUnChecked(it::newInstance, "New instance is impossible with this class: " + it))
                                                                         .collect(Collectors.toList());
        ApiHealthCheckerService checkWebService = new ApiHealthCheckerService();
        Executors.newSingleThreadScheduledExecutor().schedule(checkWebService::checkApiWithLog, 10, TimeUnit.SECONDS);
    }

    private void checkApiWithLog() {
        checkApi().forEach(healthDto -> {
            if (healthDto.getState().equals("UP")) {
                LOG.info("The API REST in webAVS for the service " + healthDto.getService() + " is up :)");
            }
        });
    }

    List<HealthDto> checkApi() {
        return apiHealthCheckers.stream()
                                .map(ApiHealthChecker::check)
                                .collect(Collectors.toList());
    }
}
