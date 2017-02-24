package com.sharecare.redirects.sdk.model;

import com.sharecare.core.sdk.model.DataRequest;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class RedirectRequest implements DataRequest {
    private String id;
    private String inboundPattern;
    private String outboundPattern;
    private String type;
    private Boolean retainQueryString;
    private List<String> segments;
}