package com.cy.controller;

import com.cy.bean.Result;
import com.cy.mapper.CovidMapper;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Desc 用来接收前端数据请求的Controller
 */
@RestController//=@Controller+@ResponseBody //表示该类是SpringBoot的一个Controller,且返回的数据为Json格式
@RequestMapping("covid")
public class Covid19Controller {

    @Autowired
    private CovidMapper covidMapper;

    /**
     * 接收前端请求返回全国疫情汇总数据
     */
    @RequestMapping("getNationalData")
    public Result getNationalData(){
        String datetime = FastDateFormat.getInstance("yyyy-MM-dd").format(System.currentTimeMillis());
        Map<String, Object> data = covidMapper.getNationalData(datetime).get(0);
        Result result = Result.success(data);
        return result;
    }

    //getNationalMapData
    /**
     * 查询全国各省份累计确诊数据并返回
     */
    @RequestMapping("getNationalMapData")
    public Result getNationalMapData(){
        String datetime = FastDateFormat.getInstance("yyyy-MM-dd").format(System.currentTimeMillis());
        List<Map<String, Object>> data =  covidMapper.getNationalMapData(datetime);
        return Result.success(data);
    }


    //getCovidTimeData
    /**
     * 查询全国每一天的疫情数据并返回
     */
    @RequestMapping("getCovidTimeData")
    public Result getCovidTimeData(){
        List<Map<String, Object>> data =  covidMapper.getCovidTimeData();
        return Result.success(data);
    }

    //getCovidTimeData1
    /**
     * 查询全国每一天的疫情数据并返回
     */
    @RequestMapping("getCovidTimeData1")
    public Result getCovidTimeData1(){
        List<Map<String, Object>> data =  covidMapper.getCovidTimeData1();
        return Result.success(data);
    }

    //getCovidImportData
    /**
     * 查询各省份境外输入病例数量
     */
    @RequestMapping("getCovidImportData")
    public Result getCovidImportData(){
        String datetime = FastDateFormat.getInstance("yyyy-MM-dd").format(System.currentTimeMillis());
        List<Map<String, Object>> data = covidMapper.getCovidImportData(datetime);
        return  Result.success(data);
    }

    //getCovidWz
    /**
     * 查询各物资使用情况
     */
    @RequestMapping("getCovidWz")
    public Result getCovidWz(){
        List<Map<String, Object>> data = covidMapper.getCovidWz();
        return Result.success(data);
    }

    //getCityTjData1
    /**
     * 查询京津冀疫情数据
     */
    @RequestMapping("getCityData1")
    public Result getCityTjData1(){
        String datetime = FastDateFormat.getInstance("yyyy-MM-dd").format(System.currentTimeMillis());
        List<Map<String, Object>> data =  covidMapper.getCityData1(datetime);
        return Result.success(data);
    }

    //getCityTjData2
    /**
     * 查询江浙沪疫情数据
     */
    @RequestMapping("getCityData2")
    public Result getCityTjData2(){
        String datetime = FastDateFormat.getInstance("yyyy-MM-dd").format(System.currentTimeMillis());
        List<Map<String, Object>> data =  covidMapper.getCityData2(datetime);
        return Result.success(data);
    }

    //getCovidBeJingData
    /**
     * 查询各省份境外输入病例数量
     */
    @RequestMapping("getCovidBeJingData")
    public Result getCovidBeJingData(){
        String datetime = FastDateFormat.getInstance("yyyy-MM-dd").format(System.currentTimeMillis());
        List<Map<String, Object>> data = covidMapper.getCovidBeJingData(datetime);
        return  Result.success(data);
    }
}
