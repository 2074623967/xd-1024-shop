package net.xdclass.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 轮播图vo
 *
 * @author tangcj
 * @date 2024/09/18 15:10
 **/
@Data
@EqualsAndHashCode(callSuper = false)
public class BannerVO {


    private Integer id;

    /**
     * 图片
     */
    private String img;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 权重
     */
    private Integer weight;
}
