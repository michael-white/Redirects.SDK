package com.sharecare.redirects.sdk;

import com.google.gson.reflect.TypeToken;
import com.sharecare.core.sdk.*;
import com.sharecare.core.sdk.configuration.BasicAuthCredentials;
import com.sharecare.core.sdk.configuration.ServerInfo;
import com.sharecare.redirects.sdk.model.BasicRedirectResponse;
import com.sharecare.redirects.sdk.model.FullRedirectResponse;
import com.sharecare.redirects.sdk.model.RedirectRequest;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;


@AllArgsConstructor
public class RedirectsApiClient {

    private final BasicAuthCredentials basicAuthCredentials;
    private final ServerInfo serverInfo;


    public GetRequest<FullRedirectResponse> getRequest() {
        return new GetRequest<>(serverInfo, basicAuthCredentials, FullRedirectResponse.class);
    }

    public DeleteRequest deleteRequest() {
        return new DeleteRequest(serverInfo, basicAuthCredentials);
    }

    public SearchRequest<Collection<BasicRedirectResponse>> searchRequest() {
        return new SearchRequest<Collection<BasicRedirectResponse>>(serverInfo, basicAuthCredentials, new TypeToken<ArrayList<BasicRedirectResponse>>() { }.getType());
    }

    public PutRequest putRequest() {
        return new PutRequest<RedirectRequest>(serverInfo, basicAuthCredentials, RedirectRequest.class);
    }

    public PatchRequest patchRequest() {
        return new PatchRequest(serverInfo, basicAuthCredentials, RedirectRequest.class);
    }

}
