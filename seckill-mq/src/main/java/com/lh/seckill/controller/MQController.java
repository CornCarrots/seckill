package com.lh.seckill.controller;

import com.lh.seckill.controller.vo.mq.SKMessage;
import com.lh.seckill.result.CodeMsg;
import com.lh.seckill.result.Result;
import com.lh.seckill.service.ConsumerService;
import com.lh.seckill.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MQController {
    @Autowired
    ConsumerService consumerService;
    @Autowired
    SenderService senderService;

    @PostMapping("/send")
    public Result send(@RequestBody SKMessage skMessage){
        senderService.sendSkMessage(skMessage);
        return Result.success(CodeMsg.SUCCESS);
    }
}
