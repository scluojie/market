package com.hxcy.market.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author kevin
 * @date 2022/6/14
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@TableName("card")
public class Card {
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
    private Integer power;
    private String name;
    private String skill;

    public Card(Item item,Power power,Skill skill) throws ParseException {
        this.id = null;
        this.tokenId = item.getTokenId();
        this.price = item.getPrice();
        this.upTime = item.getUpTime();
        this.cardId = item.getCardId();
        this.owner = item.getOwner();
        this.type = item.getType();
        this.race = item.getRace();
        this.race2 = item.getRace2();
        this.mp = item.getMp();
        this.grade = item.getGrade();
        this.lv = item.getLv();
        this.era = item.getEra();
        this.date = item.getDate();
        this.power = power.getPower();
        this.name = skill.getName();
        this.skill = skill.getSkill();
    }
}
