# 鱼子酱

## 功能需求

「鱼子酱」的总目标是：使使用该软件的学生养成记账习惯，逐渐掌握收支情况、从而 使月赤字比例下降，最后学会理财规划。并且本系统实现记账高效、便捷和无广告化。 根据可行性研究的结果和客户的要求，分析现有情况及问题，采用 Client/Server 结构， 将「鱼子酱」划分为两个子系统：客户端子系统、服务器端子系统

### 客户端子系统 

在客户端系统的功能实现上可以分为以下几个部分： 

1. 收支记录的增删改查： 用户将自己的消费信息以及账户信息输入系统中，即可对收支记录进行增删改查。
2. 分析账单、生成报表： 根据用户消费信息生成周账单、月账单及年账单，进行分类和可视化，归纳账户在 各个类别中的比例。 
3. 心愿储蓄： 设置心愿商品金额，存储金钱时间，提醒有规划的存钱。
4. 消费限额提醒 根据用户月初设置的类别消费限额，提醒用户合理分配开销。

5. 没有广告和推荐理财产品 6. 养成系统： 设置积分系统，记账获取积分，积分可以换取鱼籽，孵化小鱼。若记账中断或心愿 存钱中断，鱼会死亡。激励用户养成记账习惯。 

### 服务器端子系统 

在服务器系统的功能实现上可以分为以下几个部分：

1. 同步账单： 获取各大支付平台账单信息 API，自动与客户端同步账户余额以及消费记录。
2. 同步账户信息： 固定时间与服务器数据库同步用户账单信息，避免用户切换或丢失设备导致账户信 息丢失。 
3. 分析语音： 分析用户语音输入的收支信息，返回相应的收支记录存入客户端
4. 分析心愿单： 分析用户添加入心愿单多的商品，和商家取得优惠折扣。 

 