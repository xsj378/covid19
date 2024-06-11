package com.cy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Desc
 */
@Mapper
@Component
public interface CovidMapper {

    /**
     * 查询全国疫情汇总数据
     * @param datetime
     * @return
     */
    @Select("select `datetime`, `currentConfirmedCount`, `confirmedCount`, `suspectedCount`, `curedCount`, `deadCount` from covid19_1 where datetime = #{datetime}")
    List<Map<String,Object>> getNationalData(String datetime);


    /**
     * 查询全国各省份疫情累计确诊数据
     * @param datetime
     * @return 省份名称,累计确诊数
     */
    @Select("select provinceShortName as name ,confirmedCount as value from covid19_2 where datetime = #{datetime}")
    List<Map<String, Object>> getNationalMapData(String datetime);

    /**
     * 查询全国每一天的疫情数据
      * @return
     */
    //'新增确诊', '累计确诊', '疑似病例', '累计治愈', '累计死亡'
    @Select("select dateId,confirmedIncr as `新增确诊`,confirmedCount as `累计确诊`,suspectedCount as `疑似病例`,curedCount as `累计治愈`,deadCount as `累计死亡` from covid19_3")
    List<Map<String, Object>> getCovidTimeData();

    /**
     * 查询全国2021年9月之后的疫情数据
     * @return
     */
    //'新增确诊', '累计确诊', '疑似病例', '累计治愈', '累计死亡'
    @Select("select dateId,confirmedIncr as `新增确诊`,confirmedCount as `累计确诊`,suspectedCount as `疑似病例`,curedCount as `累计治愈`,deadCount as `累计死亡` from covid19_3 limit 707,100")
    List<Map<String, Object>> getCovidTimeData1();

    /**
     * 查询全国各省份境外输入病例数量
     * @param datetime
     * @return
     */
    @Select("select provinceShortName as `name`,confirmedCount as `value` from covid19_4 where datetime = #{datetime} order by `value` desc limit 10")
    List<Map<String, Object>> getCovidImportData(String datetime);

    /**
     * 查询防疫物资使用情况
     * @return
     */
    //INSERT INTO `bigdata`.`covid19_wz` (`name`, `cg`, `xb`, `jz`, `xh`, `xq`, `kc`)
    //'name', '采购', '下拨', '捐赠', '消耗', '需求', '库存'
    @Select("select name , `cg` as `采购`, `xb` as `下拨`, `jz` as `捐赠`, `xh` as `消耗`, `xq` as `需求`, `kc` as `库存` from covid19_wz")
    List<Map<String, Object>> getCovidWz();

    /**
     * 查询京津冀疫情数据
     * @param datetime
     * @return
     */
    @Select("select currentConfirmedCount as `新增确诊`,confirmedCount as `累计确诊`,suspectedCount as `疑似病例`,curedCount as `累计治愈`,deadCount as `累计死亡` " +
            "from covid19_2 where datetime = #{datetime} and (provinceShortName='河北' or provinceShortName='北京' or provinceShortName='天津')")
    List<Map<String, Object>> getCityData1(String datetime);

    /**
     * 查询江浙沪疫情数据
     * @param datetime
     * @return
     */
    @Select("select currentConfirmedCount as `新增确诊`,confirmedCount as `累计确诊`,suspectedCount as `疑似病例`,curedCount as `累计治愈`,deadCount as `累计死亡` " +
            "from covid19_2 where datetime = #{datetime} and (provinceShortName='江苏' or provinceShortName='浙江' or provinceShortName='上海')")
    List<Map<String, Object>> getCityData2(String datetime);

    /**
     * 查询全国各省份境外输入病例数量
     * @param datetime
     * @return
     */
    @Select("select  cityName as '区名',currentConfirmedCount as '新增确诊',confirmedCount as `累计确诊`,suspectedCount as `疑似病例`,curedCount as `累计治愈`,deadCount as `累计死亡` " +
            "from covid19_5 where datetime = #{datetime} ORDER BY currentConfirmedCount desc limit 5")
    List<Map<String, Object>> getCovidBeJingData(String datetime);
}
