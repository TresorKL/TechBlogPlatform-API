package com.example.techblogplatformapi.configuration;


import com.amazonaws.services.sqs.model.Message;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SqsMessageListener {

    @SqsListener(value = "PostAnalysisResultQueue",deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void handlePostResult(Message postResult) {
        System.out.println(postResult);
    }


}
