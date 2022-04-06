package com.gd.base.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "分页通用实体类")
@Data
public class PageBaseInfo<T>  implements Serializable {
    @ApiModelProperty(value = "分页页码", example = "1", required = true)
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNo;

    @ApiModelProperty(value = "分页大小", example = "10", required = true)
    @Min(value = 10, message = "分页大小必须在10-200之间")
    @Max(value = 200, message = "分页大小必须在10-200之间")
    private Integer pageSize;

    @ApiModelProperty("总页数")
    private Integer totalPages;

    @ApiModelProperty("是否是导出")
    Boolean export;

    @ApiModelProperty("数据总条数")
    private Integer totalSize;

    @ApiModelProperty("当前页数据")
    private List<T> data;

    @ApiModelProperty("最大页数")
    final Integer maxSize = 100000;

    //默认为第一页，20条数据
    public PageBaseInfo() {
        this.pageNo = 1;
        this.pageSize = 20;
        this.export=false;
    }

    public PageBaseInfo(Integer pageNo, Integer pageSize,Boolean export) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.export=export;
    }

    public PageBaseInfo(List<T> date) {
        this.data = date;
    }

    public PageBaseInfo(Integer pageNo, Integer pageSize,Integer totalPages,Integer totalSize,List<T> date) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalSize = totalSize;
        this.data = date;
    }

    /**
     * 获取分页对象
     */
    public Pageable pageable() {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return pageable;
    }



}
