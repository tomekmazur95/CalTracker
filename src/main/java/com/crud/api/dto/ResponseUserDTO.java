package com.crud.api.dto;

import com.crud.api.enums.Activity;
import com.crud.api.enums.Gender;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUserDTO implements Serializable {

    private Long id;
    private String userName;
    private Integer age;
    private Gender gender;
    private Activity activity;
    private ResponseMeasurementDTO height;
}
