package test.com.sharecare.redirects.sdk;

import com.google.common.collect.Lists;
import com.sharecare.core.sdk.BasicResponse;
import com.sharecare.core.sdk.DataResponse;
import com.sharecare.core.sdk.configuration.BasicAuthCredentials;
import com.sharecare.core.sdk.configuration.ServerInfo;
import com.sharecare.redirects.sdk.RedirectsApiClient;
import com.sharecare.redirects.sdk.model.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Range.greaterThan;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@Slf4j
public class RedirectsFunctionalTest {

    final private RedirectsApiClient redirectsApiClient = new RedirectsApiClient(new BasicAuthCredentials("sharecare", "hswi"), new ServerInfo("https", "api.dev.sharecare.com", "/redirects", 443));

    private final RedirectRequest redirectRequest = RedirectRequest.builder()
            .id("redirect-id")
            .inboundPattern("/some/source/path")
            .outboundPattern("/some/target/path")
            .redirectType(RedirectType.builder()
                                        .id("PERMANENT")
                                        .responseCode(301)
                                        .build())
            .patternType("REGEX")
            .patternCategory("SYSTEM")
            .retainQueryString(true)
            .segments(Lists.newArrayList("global"))
            .build();


    @Before
    public void setUp() {
        redirectsApiClient.deleteRequest()
                .withUri(redirectRequest.getId())
                .addHeader("Accept-Language", "en-US")
                .addHeader("X-Flow-ID", "1234")
                .execute();
    }

    @Test
    public void testGetRequest() {
        createRedirect();
        DataResponse<FullRedirectResponse> getResponse = redirectsApiClient.getRequest()
                .withUri(redirectRequest.getId())
                .addHeader("X-Flow-ID", "1234")
                .addHeader("Accept-Language", "en-US")
                .addHeader("Content-Type", "application/json")
                .execute();

        FullRedirectResponse fullRedirect = getResponse.getResult();
        assertThat(fullRedirect.getId(), is(redirectRequest.getId()));
        assertThat(fullRedirect.getInboundPattern(), is(redirectRequest.getInboundPattern()));
        assertThat(fullRedirect.getOutboundPattern(), is(redirectRequest.getOutboundPattern()));
        assertThat(fullRedirect.getRedirectType(), is(redirectRequest.getRedirectType()));
    }

    @Test
    public void testCreateRequest() {
        BasicResponse createResponse = redirectsApiClient.putRequest()
                .withData(redirectRequest)
                .withUri(redirectRequest.getId())
                .addHeader("X-Flow-ID", "1234")
                .execute();
        assertThat(createResponse.getStatusCode(), is(200));
    }

    @Test
    public void testUpdateRequest() {
        createRedirect();
        BasicResponse createResponse = redirectsApiClient.patchRequest()
                .withData(RedirectRequest.builder()
                                        .redirectType(RedirectType.builder()
                                                .id("TEMPORARY")
                                                .responseCode(302)
                                                .build()))
                .withUri(redirectRequest.getId())
                .addHeader("X-Flow-ID", "1234")
                .execute();
        assertThat(createResponse.getStatusCode(), is(200));
    }



    @Test
    public void testEmptySearchRequest() {

        DataResponse<Collection<BasicRedirectResponse>> searchResult2 = redirectsApiClient.searchRequest()
                .searchParam("inboundPattern", "/some/non_existant/path")
                .execute();
        assertNotNull(searchResult2.getResult());
        assertThat(searchResult2.getResult(), hasSize(0));
    }

    @Test
    public void testSearchRequest() {
        createRedirect();
        DataResponse<Collection<BasicRedirectResponse>> searchResult = redirectsApiClient.searchRequest()
                .searchParam("inboundPattern", "/some/source/path")
                .execute();

        Collection<BasicRedirectResponse> response = searchResult.getResult();
        assertThat(response.size(), is(greaterThan(0)));

        List<BasicRedirectResponse> redirects = new ArrayList<>(response);
        BasicRedirectResponse firstRedirect = redirects.get(0);
        assertThat(redirectRequest.getOutboundPattern(), startsWith(firstRedirect.getOutboundPattern()));
        assertThat(redirectRequest.getInboundPattern(), startsWith(firstRedirect.getInboundPattern()));
    }

    @Test
    public void testDeleteRequest() {
        createRedirect();
        BasicResponse deleteResponse = redirectsApiClient.deleteRequest()
                .withUri(redirectRequest.getId())
                .addHeader("Accept-Language", "en-US")
                .addHeader("X-Flow-ID", "1234")
                .execute();
        assertThat(deleteResponse.getStatusCode(), is(200));
    }

    private void createRedirect() {
        redirectsApiClient.putRequest()
                .withData(redirectRequest)
                .withUri(redirectRequest.getId())
                .addHeader("X-Flow-ID", "1234")
                .execute();
    }
}