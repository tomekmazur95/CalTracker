package com.crud.api.dto;

import com.crud.api.enums.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserActivityDTO implements Serializable {

    private Long id;
    private Activity activity;
}
