# flink-batch-outputFormat
the custom ouputFormat

1.针对flink批处理的自定义输出

2.包括sink到mail发出的三种实现方式：
  2.1调用liunx中sendmail命令发送邮件 最简单暴力 
  
  2.2使用java自带的mail API 注册邮件服务器发送
  
  2.3使用http请求的方式向公司报警/邮件系统发送邮件
  
3.还包括sink到kafka的简单实现kafkaOutputFormat
