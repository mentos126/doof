package fr.doofapp.doof.App;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestAPI extends com.android.volley.toolbox.StringRequest {

    private final Map<String, String> _params;

    /**
     * @param method
     * @param url
     * @param params
     *            and indicates no parameters will be posted along with request.
     * @param listener
     * @param errorListener
     */
    public RequestAPI(int method, String url, Map<String, String> params, Response.Listener<String> listener,
                      Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);

        _params = params;
    }

    @Override
    protected Map<String, String> getParams() {
        return _params;
    }

    /* (non-Javadoc)
     * @see com.android.volley.toolbox.StringRequest#parseNetworkResponse(com.android.volley.NetworkResponse)
     */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // since we don't know which of the two underlying network vehicles
        // will Volley use, we have to handle and store session cookies manually
        SessionApp.get().checkSessionCookie(response.headers);

        return super.parseNetworkResponse(response);
    }

    /* (non-Javadoc)
     * @see com.android.volley.Request#getHeaders()
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        if (headers == null
                || headers.equals(Collections.emptyMap())) {
            headers = new HashMap</*String, String*/>();
        }

        SessionApp.get().addSessionCookie(headers);

        return headers;
    }
}