package com.sharecare.redirects.sdk.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class PatternType {
    private final String id;
    private final String responseCode;
}
