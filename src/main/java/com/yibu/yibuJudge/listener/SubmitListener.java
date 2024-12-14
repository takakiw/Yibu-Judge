package com.yibu.yibuJudge.listener;

import com.yibu.yibuJudge.model.mq.JudgeResultMQType;
import com.yibu.yibuJudge.service.SubmitService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SubmitListener {

    private final SubmitService submitService;

    public SubmitListener(SubmitService submitService) {
        this.submitService = submitService;
    }

    @RabbitListener(queues = "${judge.result-queue}")
    public void resultHandler(JudgeResultMQType judgeResult) {
        // 更新数据库
        submitService.updateSubmitResult(judgeResult.getSubmitId(), judgeResult.getJudgeResult());
    }

}
