package com.sharecare.redirects.sdk.model;

import com.sharecare.core.sdk.model.DataResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;


@Getter
@EqualsAndHashCode
@ToString
public class RedirectResponse implements DataResponse {
    private String id;
    private String inboundPattern;
    private String outboundPattern;
    private String type;
    private Boolean retainQueryString;
    private List<String> segments;
}