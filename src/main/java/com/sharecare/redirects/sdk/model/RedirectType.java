package com.sharecare.redirects.sdk.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class RedirectType implements Serializable {
    private final String id;
    private final Integer responseCode;
}
