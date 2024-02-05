# Integrated Serverless Image Processing, Task Scheduling, and Caching using redis with MySQL & MongoDB for a Tech Blog Platform in a Monolithic Architecture

##Problem Scenario:
Develop the backend for a tech blog platform where users can publish articles, upload images, receive periodic newsletters, and access frequently read articles efficiently through caching.

##Key Components and Flow:

1. ##Article Publication, Text and Image Processing :
    - Authors publish articles on the tech blog platform, including text content and images.
    - Upon article submission, a job(task) is add to SQS, Lambda functions subscribed to the SQS are triggered to process the text and images (Analyse text content & image if not sexual using AWS ML services) uploaded within the articles.
    - Results/Outcome of the analysis is sent to the author if the publication was successful or not (with reason)
    - Utilize Amazon S3 to store images linked to the articles.
2. **Asynchronous Text & Image Processing with AWS SQS:**
    - Use AWS Simple Queue Service (SQS) as a job queue to manage image processing tasks asynchronously.
    - Lambda functions subscribed to the SQS queue process these tasks and update the articles with processed images stored in S3.
3. **Task Scheduling for Newsletters:**  
    - Implement a scheduler using AWS CloudWatch Events or AWS Lambda to trigger periodic newsletters containing the latest articles.
    - Scheduled tasks will trigger Lambda functions responsible for compiling newsletters and sending them to subscribers.

**AWS Services Used:**

- AWS Lambda with Spring Boot for image processing.
- Amazon S3 for storing images (both original and processed).
- AWS Simple Queue Service (SQS) for managing image processing tasks.
- AWS CloudWatch Events or AWS Lambda for scheduling and triggering newsletters.

**Key Points to Showcase:**

- Integration of serverless architecture with Spring Boot for image processing tasks.
- Utilizing AWS services for asynchronous job processing (AWS SQS), task scheduling, and newsletter delivery.
- Implementing a caching layer to enhance performance and speed up content access for a tech blog or newsletter platform.
