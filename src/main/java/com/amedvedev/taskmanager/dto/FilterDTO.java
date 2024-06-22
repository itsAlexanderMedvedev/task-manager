package com.amedvedev.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class FilterDTO {
    private List<String> categories;
    private List<String> priorities;
}
