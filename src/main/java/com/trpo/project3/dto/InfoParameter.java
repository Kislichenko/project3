package com.trpo.project3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoParameter {
    private String name;
    private InfoType type;
    private String modifiers;
}
