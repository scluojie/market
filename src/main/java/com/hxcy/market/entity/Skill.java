package com.hxcy.market.entity;

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
@TableName("skill")
public class Skill {
    @TableId("id")
    private String id;
    private String name;
    private String skill;
}
