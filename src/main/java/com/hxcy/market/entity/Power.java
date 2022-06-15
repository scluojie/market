package com.hxcy.market.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kevin
 * @date 2022/6/9
 * @desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("power")
public class Power {
    @TableId("id")
    private String id;
    private Integer power;
    @TableField("next_id")
    private String nextId;
    private Integer got;
    private Double era;
}
