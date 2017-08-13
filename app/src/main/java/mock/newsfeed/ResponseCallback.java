package mock.newsfeed;

public interface ResponseCallback {
    void onSuccess(String response);

    void onFailure(String errorMessage);
}
