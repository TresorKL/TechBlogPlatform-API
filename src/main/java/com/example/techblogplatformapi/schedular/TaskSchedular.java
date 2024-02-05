package com.example.techblogplatformapi.schedular;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


import com.example.techblogplatformapi.collections.Author;
import com.example.techblogplatformapi.collections.Blog;
import com.example.techblogplatformapi.collections.Post;
import com.example.techblogplatformapi.collections.PostStatus;
import com.example.techblogplatformapi.repositories.BlogRepository;
import com.example.techblogplatformapi.repositories.PostRepository;
import com.example.techblogplatformapi.services.EmailService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Component
public class TaskSchedular {

    private static final Logger logger = Logger.getLogger(TaskSchedular.class.getName());

    @Autowired
    PostRepository postRepository;

    @Autowired
    BlogRepository blogRepository;


    @Autowired
    EmailService emailService;

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Scheduled(cron = "0 10 15 * * ?")
    public void myTask() {
        // Task logic
        //System.out.println("Hello world");
        logger.info("************** Hello, World!*************");
    }

    @Scheduled(fixedRate = 10000)
    public void handlePostAnalysisResult() throws IOException {

        // Receive messages from the specified queue
        Message message  = queueMessagingTemplate.receive("PostAnalysisResultQueue");

                //.receiveAndConvert("PostAnalysisResultQueue", List.class);

        if (message != null) {
            // for (Message message : messages) {
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    // Deserialize the payload to a generic Map
                    Map<String, Object> payloadMap = objectMapper.readValue(message.getPayload().toString(), new TypeReference<>() {});

                    // Access specific fields
                    String id = (String) payloadMap.get("id");
                    String blogId = (String) payloadMap.get("blogId");
                    String title = (String) payloadMap.get("title");
                    String textContent = (String) payloadMap.get("textContent");

                    // Access nested fields
                    Map<String, Object> metaContents = (Map<String, Object>) payloadMap.get("metaContents");
                    String metaContentName = (String) metaContents.get("name");
                    String downloadUrl = (String) metaContents.get("download_url");

                    String analysisResult = (String) payloadMap.get("analysisResult");

                    // Update ost

                    Post post=postRepository.findById(id).get();

                    PostStatus postStatus= new PostStatus();

                    Blog blog=blogRepository.findById(blogId).get();

                    String [] resultArray = analysisResult.split("POST");

                    String content="";

                    if (!analysisResult.contains("INVALID")){
                        post.setVisibility(true);
                        postStatus.setPostValid(true);
                        postStatus.setReason("");

                        content= "<html>"
                                + "<body style='font-family: Arial, sans-serif;'>"
                                + "<h1 style='color: navy;'>2 Factor Authentication</h1>"
                                + "<p>Hello <strong>"+  blog.getAuthor().getName()+" </strong>,</p>"
                                + "<p>Your post was successfully published it's considered as a  <span style='color: green; font-weight: bold;'> VALID POST </span></p>"

                                + "<br>"
                                + "<p>Best regards,<br>Team</p>"
                                + "</body>"
                                + "</html>";

                    }else {
                        post.setVisibility(false);
                        postStatus.setPostValid(false);
                        postStatus.setReason(analysisResult);


                          content= "<html>"
                                + "<body style='font-family: Arial, sans-serif;'>"
                                + "<h1 style='color: navy;'>2 Factor Authentication</h1>"
                                + "<p>Hello <strong>"+  blog.getAuthor().getName()+" </strong>,</p>"
                                + "<p>Your post was not published it's considered as a  <span style='color: green; font-weight: bold;'> INVALID POST </span></p>"
                                + "<p>because "+resultArray[1]+" </p>"
                                + "<p>Trying to change something from your to meet our terms and conditions </p>"
                                + "<br>"
                                + "<p>Best regards,<br>Team</p>"
                                + "</body>"
                                + "</html>";

                    }

                    post.setPostStatus(postStatus);

                    postRepository.save(post);

                    //NOTIFY Author
                    String to = blog.getAuthor().getEmail() ;
                    String subject= "PUBLICATION RESULT - "+post.getTitle();


                    emailService.sendHtmlMail(  to,  subject,   content,true);


                    System.out.println("************ Author Notified Message ********* " + analysisResult);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception accordingly
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            // }
        } else{
            System.out.println("Message NO MESSAGE");
        }

    }
}
