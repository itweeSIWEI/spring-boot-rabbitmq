package com.mq.mssage.controller;

import com.mq.mssage.msg.MsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wusw
 * @date 2020/4/17 14:33
 */
@RestController
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    private MsgProducer msgProducer;

    @GetMapping("/send")
    public String send(){
        int size = 66;
        for (int i = 0; i < size; i++) {
            msgProducer.sendMsg("消息A 第"+i+"次");
        }
        msgProducer.sendMsgB("消息B");
        msgProducer.sendMsgC("消息C");
        return "success";
    }
}
