package com.trpo.project3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class StringObject {
    private Object object;
    private String strObject;
    private Set<String> headers;
}
