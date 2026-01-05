package energymix.service.model;

public record ChargingWindow(
        String start,
        String end,
        Double cleanProc
) {
}
