# COVID19
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

## 项目架构

<img src="/doc/image.png" width="50%" height="50%">

## 详细设计
### 1. 数据爬取与解析
首先封装 HttpClient 工具，方便爬取网页内容，在 HttpUtils 类中写入静态方法 getHtml。
```
public static String getHtml(String url){
	CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm). build();
	HttpGet httpGet = new HttpGet(url);
	httpGet.setConfig(config);
	httpGet.setHeader("User-Agent",userAgentList.get(new
	Random().nextInt(userAgentList.size())));
	if (状态码 == 200){
		return EntityUtils.toString(response.getEntity(), "UTF-8");
	}
}
```
封装时间工具类 TimeUtils，写入方法 format 用来对时间数据格式化。
```
public static String format(Long timestamp,String pattern){
	return FastDateFormat.getInstance(pattern).format(timestamp);
}
```
爬取数据：

<img src="/doc/data.png" width="50%" height="50%">

### 2. 定时功能
在入口类前加入注解@EnableScheduling，开启 SpringBoot 程序的定时任务调度，然后再需要调度的类前面加上注解@Component，表示将该类交给 SpringBoot 管理，作为 Spring 容器中的对象，最后使用@Scheduled 注解开启定时任务。
### 3. KafkaTemplate 配置类
封装一个KafkaTemplate配置类 ，创建getKafkaTemplate方法使其返回KafkaTemplate，并在方法前加上@Bean 注解，表示由Spring管理返回对象。
```
//读取配置
@Value("${kafka.bootstrap.servers}")
private String bootstrap_servers;
@Value("${kafka.retries_config}")
private String retries_config;
@Value("${kafka.batch_size_config}")
private String batch_size_config;
@Value("${kafka.linger_ms_config}")
private String linger_ms_config;
@Value("${kafka.buffer_memory_config}")
private String buffer_memory_config;
@Bean//表示该方法返回的对象交给 Spring 管理
public KafkaTemplate getKafkaTemplate(){
	Map<String,Object> configs = new HashMap<>();
	configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrap_servers);
	configs.put(ProducerConfig.RETRIES_CONFIG,retries_config);
	configs.put(ProducerConfig.BATCH_SIZE_CONFIG,batch_size_config);
	configs.put(ProducerConfig.LINGER_MS_CONFIG,linger_ms_config);
	configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, buffer_memory_config);
	configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
	configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	//使用自定义的分区器
	configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomerPartitioner.class);
	DefaultKafkaProducerFactory producerFactory = new
	DefaultKafkaProducerFactory(configs);
	KafkaTemplate kafkaTemplate = new KafkaTemplate(producerFactory);
	return kafkaTemplate;
}
```
### 4. 物资数据模拟
本项目中模拟了N95 口罩/个, 外科口罩/个, 84 消毒液/瓶,医疗废物袋/个,杀菌洗手液/瓶,红外测温仪/个等，而这些物资的来源分为采购、下拨、捐赠、消耗和需求。
### 5. 数据发送到Kafka
将数据进一步解析并发送到 Kafka，实施过程如下：按照元素标签的 id 解析页面中的全国疫情数据，然后用正则表达式匹配获取 json 格式的疫情数据，获取 data字段对应的数据，最后将爬取解析出来的每一天的数据设置回省份中的字段中。
### 6. 数据处理分析
Spark 提供了两种开发环境分别为 Spark Streaming 和 Structured Streaming。Spark Streaming 对 Spark API 进行了扩展。输入的数据流经过 Spark Streaming进行批处理后交给 Spark Engine 引擎来批处理数据，最终生成结果数据流。工作方
式如图：

<img src="/doc/spark-streaming.png" width="50%" height="50%">

Structured Streaming 的关键思想是将Data stream当做连续追加的表。Structured Streaming 的工作方式如图：

<img src="/doc/structured-streaming.png" width="50%" height="50%">

使用 Structured Streaming 实时分析处理数据并存入 MySQL 数据库中，实施过程如下：
+ 创建 Structured Streaming 执行环境
+ 连接 Kafka
+ 处理数据
+ 统计分析
+ 结果输出，先输出到控制台观察

全国疫情汇总信息输出结果：

<img src="/doc/covid19_1.png" width="50%" height="50%">

全国各省份病例数输出结果：

<img src="/doc/covid19_2.png" width="50%" height="50%">

全国疫情趋势输出结果：

<img src="/doc/covid19_3.png" width="50%" height="50%">

具体代码如下：
```
result1.writeStream.foreach(new BaseJdbcSink("replace into covid19_1(datetime,existingCases,cumulativeCases,doubtfulCase,healingCases,deathCases)values(?,?,?,?,?,?)")
{
	override def realProcess(sql: String, row: Row): Unit = {
		//取出 row 中的数据
		val datetime: String = row.getAs[String]("datetime")
		val existingCases: Long = row.getAs[Long]("existingCases")
		val cumulativeCases: Long = row.getAs[Long]("cumulativeCases")
		val doubtfulCase: Long = row.getAs[Long]("doubtfulCase")
		val healingCases: Long = row.getAs[Long]("healingCases")
		val deathCases: Long = row.getAs[Long]("deathCases")
		//获取预编译语句对象
		ps = conn.prepareStatement(sql)
		//给 sql 设置参数值
		ps.setString(1,datetime)
		ps.setLong(2,existingCases)
		ps.setLong(3,cumulativeCases)
		ps.setLong(4,doubtfulCase)
		ps.setLong(5,healingCases)
		ps.setLong(6,deathCases)
		ps.executeUpdate()
	}
})
		.outputMode("complete")
		.trigger(Trigger.ProcessingTime(0))
		.option("truncate",false)
		.start()
```
物资数据处理分析：
```
val topics: Array[String] = Array("covid19_wz")
//从 MySQL 中查询出 offsets:Map[TopicPartition, Long]
val offsetsMap: mutable.Map[TopicPartition, Long] = OffsetUtils.getOffsetsMap("SparkKafka", "covid19_wz")
val kafkaDS: InputDStream[ConsumerRecord[String, String]] = if(offsetsMap.size > 0) {
	KafkaUtils.createDirectStream[String, String](
		ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](topics, kafkaParams, offsetsMap))
} else {
	KafkaUtils.createDirectStream[String, String](
	ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](topics, kafkaParams))
}
val tupleDS: DStream[(String, (Int, Int, Int, Int, Int, Int))] = kafkaDS.map(record =>
{
	val jsonStr: String = record.value()
	val from: String = jsonObj.getString("from")
	val count: Int = jsonObj.getInteger("count")
	//根据物资来源不同,将 count 记在不同的位置,最终形成统一的格式
	from match {
		case "物资采办" => (name, (count, 0, 0, 0, 0, count))
		case "物资下发" => (name, (0, count, 0, 0, 0, count))
		case "物资捐赠" => (name, (0, 0, count, 0, 0, count))
		case "物资损耗" => (name, (0, 0, 0, -count, 0, -count))
		case "物资需求" => (name, (0, 0, 0, 0, -count, -count))
	}
})
resultDS.foreachRDD(rdd => {
	rdd.foreachPartition(lines => {
		//编写 sql 并获取 ps
		val sql: String = "replace intocovid19_wz(name,purchase,allocate,donation,consume,demand,stock)values(?,?,?,?,?,?,?)"
  		val ps: PreparedStatement = conn.prepareStatement(sql)
		//设置参数并执行
		for (line <- lines) {
			ps.setString(1,line._1)
			ps.setInt(2,line._2._1)
			ps.setInt(3,line._2._2)
			ps.setInt(4,line._2._3)
			ps.setInt(5,line._2._4)
			ps.setInt(6,line._2._5)
			ps.setInt(7,line._2._6)
			ps.executeUpdate()
		}
		//关闭资源
		ps.close()
		conn.close()
	})
})
```
## 大屏展示

<img src="/doc/show.png" width="50%" height="50%">
