package client;

public interface CollectorClient {

    void collectUserAction(long userId, long eventId, ActionType actionType);
}
