package com.trpo.project3.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class InfoConstructor {
    private String name;
    private String modifiers;
    private ArrayList<InfoParameter> parameters;
}
