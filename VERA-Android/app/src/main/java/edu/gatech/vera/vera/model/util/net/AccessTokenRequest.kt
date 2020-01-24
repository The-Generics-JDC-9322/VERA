package edu.gatech.vera.vera.model.util.net

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import edu.gatech.vera.vera.model.util.net.FitbitAPI.callbackURI
import edu.gatech.vera.vera.model.util.net.FitbitAPI.clientID
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder




class AccessTokenRequest(
    url: String,
    private val code: String,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
) : StringRequest(Method.POST, url, listener, errorListener) {

    override fun getHeaders(): MutableMap<String, String> {
        var defaultHeaders = HashMap<String, String>(super.getHeaders())
        val clientInfo = FitbitAPI.clientID + ":" + FitbitAPI.clientSecret

        val authorization = "Basic " +
                Base64.encodeToString(clientInfo.toByteArray(), Base64.URL_SAFE)

        defaultHeaders["Authorization"] = authorization
        defaultHeaders.put("Content-Type", "application/x-www-form-urlencoded")
        return defaultHeaders
    }

    override fun getBody(): ByteArray {
        val uri = FitbitAPI.callbackURI
        val clientID = FitbitAPI.clientID
        val body = JSONObject(
            "{" +
                    "code:$code," +
                    "grant_type:authorization_code," +
                    "client_id:$clientID," +
                    "redirect_uri:$uri" +
                    "}"
        )

        val params = mapOf<String, String>(
            "code" to code,
            "grant_type" to "authorization_code",
            "client_id" to clientID,
            "redirect_uri" to callbackURI
        )
        return encodeParameters(params, "utf-8")
    }

    override fun getParams(): Map<String, String> {
        val params = mapOf<String, String>(
            "client_id" to clientID,
            "grant_type" to "authorization_code",
            "redirect_uri" to "https://navaem8.wixsite.com",
            "code" to code
        )

        return params
    }

    /** Converts <code>params</code> into an application/x-www-form-urlencoded encoded string. */
    private fun encodeParameters(params: Map<String, String>, paramsEncoding: String): ByteArray {
        val encodedParams = StringBuilder()
        try {
            for (entry in params.entries) {
                if (entry.key == null || entry.value == null) {
                    throw IllegalArgumentException(
                        String.format(
                            "Request#getParams() or Request#getPostParams() returned a map "
                                    + "containing a null key or value: (%s, %s). All keys "
                                    + "and values must be non-null.",
                            entry.key, entry.value
                        )
                    )
                }
                encodedParams.append(entry.key)
                encodedParams.append('=')
                encodedParams.append(entry.value)
                encodedParams.append('&')
            }
            encodedParams.deleteCharAt(encodedParams.length - 1);
            return encodedParams.toString().toByteArray(charset(paramsEncoding))
        } catch (uee: UnsupportedEncodingException) {
            throw RuntimeException("Encoding not supported: $paramsEncoding", uee)
        }

    }
}
