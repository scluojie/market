package com.hxcy.market.task;


import com.hxcy.market.entity.Card;
import com.hxcy.market.entity.Item;
import com.hxcy.market.entity.Power;
import com.hxcy.market.entity.Skill;
import com.hxcy.market.mapper.CardMapper;
import com.hxcy.market.mapper.ItemMapper;
import com.hxcy.market.mapper.PowerMapper;
import com.hxcy.market.mapper.SkillMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.time.LocalDate;
import java.util.List;

/**
 * @author kevin
 * @date 2022/4/28
 * @desc
 */
@Component
public class NoticePipeline implements Pipeline {
    private static Logger logger = LoggerFactory.getLogger(NoticePipeline.class);

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private PowerMapper powerMapper;


    @SneakyThrows
    @Override
    public void process(ResultItems resultItems, Task task) {
        // 获取数据
        List<Item> jobsItems = resultItems.get("key");
        // 判断获取的数据是否符合
        if (jobsItems != null) {
            logger.info("写入数据库：" + jobsItems);
            //存到数据库
           for (Item jobsItem : jobsItems) {
               jobsItem.setDate(LocalDate.now().toString());
               Item selectById = itemMapper.selectByTokenIdAndDate(jobsItem.getTokenId(),jobsItem.getDate());
               if(selectById != null){
                   itemMapper.update(jobsItem);
               }else{
                   itemMapper.insert(jobsItem);
                   //同时插入card表
                   Skill skill = skillMapper.selectById(jobsItem.getCardId());
                   Power power = powerMapper.selectById(jobsItem.getCardId());
                   Card card = new Card(jobsItem, power, skill);
                   cardMapper.insert(card);
               }
           }
        }
    }
}
