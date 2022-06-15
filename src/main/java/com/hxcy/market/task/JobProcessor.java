package com.hxcy.market.task;

import com.alibaba.fastjson.JSONArray;
import com.hxcy.market.entity.Item;
import com.hxcy.market.entity.Power;
import com.hxcy.market.entity.Skill;
import com.hxcy.market.mapper.CardMapper;
import com.hxcy.market.mapper.ItemMapper;
import com.hxcy.market.mapper.PowerMapper;
import com.hxcy.market.mapper.SkillMapper;
import com.hxcy.market.utils.HttpClientUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.HashMap;
import java.util.List;

/**
 * @author kevin
 * @date 2022/5/20
 * @desc
 */
@Slf4j
@Component
public class JobProcessor implements PageProcessor {

    @Value("${crawler.url}")
    private String DATA_URL;

    @Value("${crawler.skill}")
    private String SKILL_URL;

    @Value("${crawler.power}")
    private String POWER_URL;

    /**
     * 每页卡牌数量
     */
    private static Integer size = 20;

    private static Integer start = 17800;

    /**
     * total数量
     */
    private static Long total;

    @Autowired
    private SkillMapper skillMapper;

    @Autowired
    private PowerMapper powerMapper;



    /**
     * 处理下载后的页面逻辑
     *
     * @param page
     */
    @SneakyThrows
    @Override
    public void process(Page page) {
        //1.解析Html 数据
        log.info(page.getJson().jsonPath(".list").toString());
        total = Long.parseLong(page.getJson().jsonPath("total").get());
        log.info("卡牌数量：" + total);
        List<Item> items = JSONArray.parseArray(page.getJson().jsonPath(".list").toString(), Item.class);
        try {
            if (items.size() == 0) {
                log.warn("no data....");
            } else {
                //封装数据
                page.putField("key", items);
                //添加下一次链接
                start = start + size;
                HashMap<String, Object> nameValuePair = new HashMap<>();
                nameValuePair.put("c", "getSellList");
                nameValuePair.put("start", String.valueOf(start));
                nameValuePair.put("end", String.valueOf(start + size));
                nameValuePair.put("type", "[1,2]");
                /*nameValuePair.put("grade", "[2]");
                nameValuePair.put("lv", "[4]");*/

                Request request = new Request("https://marketplace.era7.io/gs:443");
                request.setMethod(HttpConstant.Method.POST);
                request.setRequestBody(HttpRequestBody.form(nameValuePair, "UTF-8"));
                page.addTargetRequest(request);
            }
        }catch (NullPointerException e){
            log.info("爬取完成");
        }
        Thread.sleep(5000);
    }

    private Site site = Site.me()
            .setCharset("UTF-8")
            // 设置超时时间
            .setTimeOut(60 * 1000)
            // 设置重试间隔
            .setRetrySleepTime(10 * 1000)
            // 设置重试次数
            .setSleepTime(3);

    @Override
    public Site getSite() {
        return site;
    }

    @Autowired
    private NoticePipeline noticePipeline;


    /**
     * initialDelay当任务启动后，等多久在执行
     * fixedDelay每隔多久执行一次
     */
    @Scheduled(cron = "10 50 12 * * ?")
    public void process() {
        log.info("开始执行任务.....");
        //先下载Skill 和 Power
        String skillStr = HttpClientUtil.sendGetForProxy(SKILL_URL);
        String[] lineStr = skillStr.split("\n");
        for (int i = 1; i < lineStr.length; i++) {
            String[] item = lineStr[i].split("\t");
            Skill skill = new Skill(item[0], item[1], item[13]);
            //插入数据库
            Skill selectById = skillMapper.selectById(item[0]);
            if (selectById != null) {
                skillMapper.updateById(skill);
            } else {
                skillMapper.insert(skill);
            }
        }

        String powerStr = HttpClientUtil.sendGetForProxy(POWER_URL);
        String[] powerLine = powerStr.split("\n");
        for (int i = 1; i < powerLine.length; i++) {
            String[] item = powerLine[i].split("\t");
            Power power = new Power(item[0], Integer.parseInt(item[1]), item[2], Integer.parseInt(item[3]), Double.parseDouble(item[4]));
            //插入数据库
            Power selectById = powerMapper.selectById(item[0]);
            if (selectById != null) {
                powerMapper.updateById(power);
            } else {
                powerMapper.insert(power);
            }
        }
       /* //获取总数量
        HashMap<String, String> headers = new HashMap<>();
        HashMap<String, String> parameters = new HashMap<>();

        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");

        parameters.put("c", "getSellList");
        parameters.put("start", "0");
        parameters.put("end", "20");
        parameters.put("type", "[1,2]");
        try {
            String totalStr = new Json(HttpClientUtil.doPost(DATA_URL, headers, parameters).getContent()).jsonPath("total").get();
            JobProcessor.total = Long.parseLong(totalStr);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        // 创建下载器Downloader
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();

        // 给下载器设置代理服务器信息 localhost 20001 127.0.0.1 8123  new Proxy("localhost", 20001),
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1", 20001)));
        Spider spider = Spider.create(new JobProcessor());

        HashMap<String, Object> nameValuePair = new HashMap<>();
        nameValuePair.put("c", "getSellList");
        nameValuePair.put("start", start);
        nameValuePair.put("end", start + size);
        nameValuePair.put("type", "[1,2]");
        /*nameValuePair.put("grade", "[2]");
        nameValuePair.put("lv", "[4]");*/

        Request request = new Request(DATA_URL);
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(HttpRequestBody.form(nameValuePair, "UTF-8"));
        spider.addRequest(request);

        spider
                .setDownloader(httpClientDownloader)
                // 使用BloomFilter来进行去重，占用内存较小，但是可能漏抓页面   //100000是估计的页面数量
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(1)
                // 设置输出位置
                .addPipeline(this.noticePipeline)
                .run();
    }
}

