package com.atguigu.edusta.service.impl;

import com.atguigu.commonutils.R;
import com.atguigu.edusta.client.UcenterClient;
import com.atguigu.edusta.entity.StatisticsDaily;
import com.atguigu.edusta.mapper.StatisticsDailyMapper;
import com.atguigu.edusta.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author lytstart
 * @since 2020-08-13
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    /**
     * 1.统一某一天的注册人数
     *
     * @param day
     */
    @Override
    public void registerCount(String day) {

        //添加之前，删除相同日期的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);

        //远程调用
        R r = ucenterClient.registerCount(day);
        Integer registerNum = (Integer) r.getData().get("registerCount");

        //把统计的数据放到统计分析表
        StatisticsDaily daily = new StatisticsDaily();
        daily.setRegisterNum(registerNum);
        daily.setDateCalculated(day);
        //TODO
        daily.setLoginNum(RandomUtils.nextInt(100, 200));
        //TODO
        daily.setVideoViewNum(RandomUtils.nextInt(100, 200));
        //TODO
        daily.setCourseNum(RandomUtils.nextInt(100, 200));
        baseMapper.insert(daily);
    }


    /**
     * 2.图表显示，返回两部分数据，日期json数组，数量json数组
     *
     * @param type
     * @param begin
     * @param end
     * @return
     */
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {

        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        //精准查询(返回两列数据)
        wrapper.select("date_calculated", type);

        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        //因为返回 日期 和 日期对应数量
        //前端要求数组json结构,对应 Java代码是List集合
        //创建两个List
        List<String> dataList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();

        for (int i = 0; i < staList.size(); i++) {
            StatisticsDaily daily = staList.get(i);
            dataList.add(daily.getDateCalculated());

            //类型判断
            switch (type) {
                case "login_num":
                    numberList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numberList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numberList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numberList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }

        Map<String, Object> map = new HashMap<>(16);
        map.put("dataList", dataList);
        map.put("numberList", numberList);
        return map;
    }

}
