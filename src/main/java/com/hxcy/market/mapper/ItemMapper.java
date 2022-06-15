package com.hxcy.market.mapper;

import com.hxcy.market.entity.Item;
import org.apache.ibatis.annotations.*;

/**
 * @author kevin
 * @date 2022/5/20
 * @desc
 */
@Mapper
public interface ItemMapper {

    @Select("select id,token_id,price,up_time,card_id,owner,type,race,race2,mp,grade,lv,era,date " +
            "from item where token_id = #{tokenId} and date = #{date}")
    public Item selectByTokenIdAndDate(@Param("tokenId") Long tokenId,@Param("date") String date);

    @Update("update item set price = #{price},up_time = #{upTime},card_id = #{cardId},owner = #{owner}," +
            "type = #{type},race = #{race},race2 = #{race2},mp = #{mp},grade = #{grade},lv =#{lv},era = #{era} " +
            " where token_id = #{tokenId} and date = #{date}")
    public void update(Item item);

    @Insert("insert into item(id,token_id,price,up_time,card_id,owner,type,race,race2,mp,grade,lv,era,date) " +
            "value (null,#{tokenId},#{price},#{upTime},#{cardId},#{owner},#{type},#{race},#{race2},#{mp}," +
            "#{grade},#{lv},#{era},#{date})")
    public void insert(Item item);
}
