package mock.newsfeed;

public interface ResponseCallback {
    void onSuccess(String response);
    void onUpdate(int state);
    void onFailure(String errorMessage);
}
