package com.lh.seckill.service;

import com.lh.seckill.config.MQConfig;
import com.lh.seckill.controller.vo.mq.SKMessage;
import com.lh.seckill.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SenderService implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private static Logger logger = LoggerFactory.getLogger(SenderService.class);

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public SenderService(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnCallback(this::returnedMessage);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info("SkMessage UUID: " + correlationData.getId());
        if (ack) {
            logger.info("SkMessage 消息消费成功！");
        } else {
            System.out.println("SkMessage 消息消费失败！");
        }

        if (cause != null) {
            logger.info("CallBackConfirm Cause: " + cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("SkMessage 消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",
                exchange, routingKey, replyCode, replyText, message);
    }

    public void sendSkMessage(SKMessage message) {
        logger.info("MQ send message: " + message);
        // 秒杀消息关联的数据
        CorrelationData skCorrData = new CorrelationData(UuidUtil.uuid());
        // 第一个参数为消息队列名(此处也为routingKey)，第二个参数为发送的消息
        rabbitTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, message, skCorrData);
    }
}
