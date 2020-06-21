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
public class InfoConstructor {
    private String name;
    private String modifiers;
    private ArrayList<InfoParameter> parameters;
}
