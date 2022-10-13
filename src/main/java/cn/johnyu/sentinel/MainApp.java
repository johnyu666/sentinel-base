package cn.johnyu.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.List;

public class MainApp {

    public static void main(String[] args) {
        // 首先配置规则.
        initFlowRules();
        Entry entry=null;
        for (int i = 1; i < 4; i++) {
            System.out.printf("进行第 %d 次请求....\n",i);
            for (int j = 0; j <5 ; j++) {
                try{
                    entry=SphU.entry("HelloWorld");
                    //todo: 以下部分都是名称为：HelloWorld的资源
                    System.out.println("hello world!");
                }catch (BlockException ex){
                    System.out.println("blocked!");
                }finally {
                    if(entry!=null) entry.exit();
                }
            }
            //每隔1秒发执行一次
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    //    定义规则
    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("HelloWorld");//rule与resource（HelloWorld）进行绑定
//      Thread和QPS可选
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // QPS==2次，本1秒周期的其余请求将被阻塞处理
        rule.setCount(2);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
