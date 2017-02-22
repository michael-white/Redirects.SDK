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
            .source("/some/source/path")
            .target("/some/target/path")
            .type("some-type")
            .build();


    @Ignore
    @Before
    public void setUp() {
        redirectsApiClient.deleteRequest()
                .withUri(redirectRequest.getId())
                .addHeader("Accept-Language", "en-US")
                .addHeader("X-Flow-ID", "1234")
                .execute();
    }

    @Ignore
    @Test
    public void testGetRequest() {
        createQuiz();
        DataResponse<FullRedirectResponse> getResponse = redirectsApiClient.getRequest()
                .withUri(redirectRequest.getId())
                .addHeader("X-Flow-ID", "1234")
                .addHeader("Accept-Language", "en-US")
                .addHeader("Content-Type", "application/json")
                .execute();

        FullRedirectResponse fullRedirect = getResponse.getResult();
        assertThat(fullRedirect.getId(), is(redirectRequest.getId()));
        assertThat(fullRedirect.getSource(), is(redirectRequest.getSource()));
        assertThat(fullRedirect.getTarget(), is(redirectRequest.getTarget()));
        assertThat(fullRedirect.getType(), is(redirectRequest.getType()));
    }

    @Ignore
    @Test
    public void testCreateRequest() {
        BasicResponse createResponse = redirectsApiClient.putRequest()
                .withData(redirectRequest)
                .withUri(redirectRequest.getId())
                .addHeader("X-Flow-ID", "1234")
                .execute();
        assertThat(createResponse.getStatusCode(), is(201));
    }

    @Ignore
    @Test
    public void testUpdateRequest() {
        createQuiz();
        BasicResponse createResponse = redirectsApiClient.patchRequest()
                .withData(RedirectRequest.builder().type("some-other-type").build())
                .withUri(redirectRequest.getId())
                .addHeader("X-Flow-ID", "1234")
                .execute();
        assertThat(createResponse.getStatusCode(), is(200));
    }



    @Ignore
    @Test
    public void testEmptySearchRequest() {
        DataResponse<Collection<BasicRedirectResponse>> searchResult2 = redirectsApiClient.searchRequest()
                .searchParam("source", "/some/searched/path")
                .execute();
        assertNotNull(searchResult2.getResult());
        assertThat(searchResult2.getResult(), hasSize(0));
    }

    @Ignore
    @Test
    public void testSearchRequest() {
        DataResponse<Collection<BasicRedirectResponse>> searchResult = redirectsApiClient.searchRequest()
                .searchParam("target", "/some/target/path")
                .execute();

        Collection<BasicRedirectResponse> response = searchResult.getResult();
        assertThat(response.size(), is(greaterThan(0)));

        List<BasicRedirectResponse> redirects = new ArrayList<>(response);
        BasicRedirectResponse firstRedirect = redirects.get(0);
        assertThat(redirectRequest.getTarget(), startsWith(firstRedirect.getTarget()));
    }

    @Ignore
    @Test
    public void testDeleteRequest() {
        createQuiz();
        BasicResponse deleteResponse = redirectsApiClient.deleteRequest()
                .withUri(redirectRequest.getId())
                .addHeader("Accept-Language", "en-US")
                .addHeader("X-Flow-ID", "1234")
                .execute();
        assertThat(deleteResponse.getStatusCode(), is(200));
    }

    private void createQuiz() {
        redirectsApiClient.putRequest()
                .withData(redirectRequest)
                .withUri(redirectRequest.getId())
                .addHeader("X-Flow-ID", "1234")
                .execute();
    }
}