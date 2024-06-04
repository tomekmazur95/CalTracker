package com.crud.api.dto;

import com.crud.api.enums.Activity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserActivityDTO implements Serializable {

    private Activity activity;

}
