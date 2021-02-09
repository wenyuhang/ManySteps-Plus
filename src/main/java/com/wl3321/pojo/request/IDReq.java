package com.wl3321.pojo.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/3 11:36
 * desc   :
 */
@Data
public class IDReq {

    @NotBlank(message = "The id parameter cannot be empty")
    @Min(value = 0, message = "The id parameter cannot be less than 0")
    private String id;
}
