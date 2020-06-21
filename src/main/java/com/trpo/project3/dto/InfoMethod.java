package com.trpo.project3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoMethod {
    private String name;
    private InfoType returnType;
    private String modifiers;
    private ArrayList<InfoParameter> parameters;
    private String nameClass;
}
