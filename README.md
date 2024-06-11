## COVID19
项目对疫情数据实时爬取、物资数据模拟生成，将数据发送到Kafka消息队列，使用Spark程序从Kafka集群中实时的消费数据并统计分析，同时存入MySQL数据库中，最后使用Echarts从不同维度可视化呈现疫情数据分布。
## 功能需求
+ 对全国疫情数据进行实时汇总。

+ 以地图展示出从疫情开始至今全国各省份的累计确诊病例数。

+ 以图表的方式展示境外输入趋势、疫情新增趋势（新增病例）等。横轴为日期、纵轴为人数。

+ 播放防疫中的一些新闻：如榜样力量、防疫政策、指南等。

+ 统计物资信息，根据防控常用物资（一次性外科口罩、体温计、洗手液、84消毒等）可详细展开为数据表。

+ 对重点省市的下级各区进行疑似病例，新增确诊，累计治愈，累计确诊，累计死亡的数据统计。

+ 对相邻省市以雷达图的形式进行数据比较分析。
## 性能需求
在CentOS Linux7系统中搭建伪分布式集群，在每个节点中搭建Zookeeper和Kafka集群。
## 数据来源
数据来源于丁香园、百度、腾讯，将易用、灵活的HttpClient封装成HttpUtils类对疫情相关数据爬取为字符串类型数据，使用Jsoup将数据转为Document并解析页面中的指定内容-即id为getAreaStat的标签中的全国疫情数据，最后使用正则表达式获取json格式的疫情数据。丁香园数据：https://ncov.dxy.cn/ncovh5/view/pneumonia
```
{
	"provinceName": "台湾",
	"provinceShortName": "台湾",
	"currentConfirmedCount": 50,
	"confirmedCount": 440,
	"suspectedCount": 348,
	"curedCount": 383,
	"deadCount": 7,
	"comment": "",
	"locationId": 710000,
	"statisticsData": "https://file1.dxycdn.com/2020/0223/045/3398299749526003760-135.json",
	"cities": []
}
```
