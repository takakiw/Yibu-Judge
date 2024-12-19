package com.yibu.yibujudge.listener;


import com.yibu.yibujudge.model.mq.JudgeResultMQType;
import com.yibu.yibujudge.model.mq.SubmitMQType;
import com.yibu.yibujudge.model.response.JudgeResult;
import com.yibu.yibujudge.service.JudgeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JudgeListener {

    private final JudgeService judgeService;
    private final RabbitTemplate rabbitTemplate;

    public JudgeListener(JudgeService judgeService,
                         RabbitTemplate rabbitTemplate) {
        this.judgeService = judgeService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${judge.result-queue:result-queue}")
    private String resultQueue;


    @RabbitListener(queues = "${judge.judge-queue:judge-queue}", concurrency = "3-5")
    public void judgeHandler(SubmitMQType submit) {
        JudgeResult result = judgeService.judge(submit);
        JudgeResultMQType judgeResultMQType =
                new JudgeResultMQType(submit.getSubmitId(), submit.getContestId(), submit.getProblem().getId(), submit.getUserId(), result);
        rabbitTemplate.convertAndSend(resultQueue, judgeResultMQType);
    }
}
