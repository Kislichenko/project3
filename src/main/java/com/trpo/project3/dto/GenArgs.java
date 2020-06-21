package com.trpo.project3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GenArgs {

    List<String> genArgs;
    Object[] objects;
}
