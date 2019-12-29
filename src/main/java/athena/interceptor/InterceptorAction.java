package athena.interceptor;

import okhttp3.Request;

/**
 * An interface which allows the user to implement custom actions upon interception of the main HTTP client.
 */
public interface InterceptorAction {

    /**
     * Run the action on the chain.
     *
     * @param request the request.
     * @return a new request to proceed with
     */
    Request run(Request request);

}
