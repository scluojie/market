package com.hxcy.market.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author kevin
 * @date 2022/5/20
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@TableName("item")
public class Item {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @TableField("token_id")
    private Long tokenId;
    private String price;
    @TableField("up_time")
    private Long upTime;
    @TableField("card_id")
    private String cardId;
    private String owner;
    private String type;
    private String race;
    private String race2;
    private String mp;
    private String grade;
    private Integer lv;
    private String era;
    private String date;
}
