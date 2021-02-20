package com.wl3321.pojo.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2021/2/2 10:58
 * desc   : 分页请求
 */
@Data
public class PageReq {

    @Min(value = 1,message = "page 参数不能小于1")
    private int page;

    @Min(value = 1,message = "size 参数不能小于1")
    @Max(value = 100,message = "size 参数不能大于100")
    private int size;

}
