package com.hxcy.market;

import com.hxcy.market.utils.HttpClientUtil;

import java.time.LocalDate;

/**
 * @author kevin
 * @date 2022/6/9
 * @desc
 */
public class Demo {
    public static void main(String[] args) {
        //网页资源
        //https://marketplace.era7.io/FirstOfTruth_Server/marketClientConfigs.php?version=1
        //config
        String url = "https://d2akm58mo2ti85.cloudfront.net/game_main/config_files/v23/card_configs";
        //https://d2akm58mo2ti85.cloudfront.net/game_main/config_files/v23/zh_tw/card_configs
        //战力
        String url1 = "https://d2akm58mo2ti85.cloudfront.net/game_main/config_files/v23/battle_cards_power_configs";
        //https://d2akm58mo2ti85.cloudfront.net/game_main/config_files/v23/zh_tw/battle_cards_power_configs

       // String s = HttpClientUtil.sendGetForProxy(url);
       // System.out.println(s);
        System.out.println(LocalDate.now().toString());
    }
}
