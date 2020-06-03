package com.trpo.project3.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InfoMethod {
    private String name;
    private InfoType returnType;
    private String modifiers;
    private ArrayList<InfoParameter> parameters;
}
