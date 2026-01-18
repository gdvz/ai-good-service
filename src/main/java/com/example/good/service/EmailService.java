package com.example.good.service;

import com.example.good.util.SslUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Tool(description = "Send Email for given email receiver and given subject and given body")
    public String sendEmail(@ToolParam(description= "email receiver") String email,
                          @ToolParam(description= "subject") String subject,
                          @ToolParam(description= "body") String body) throws IOException, InterruptedException {

        var client = HttpClient.newBuilder()
                .sslContext(SslUtil.trustAllSslContext())
                .build();
        var auth = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJzb3VyY2VTeXN0ZW0iOiJURVNUIiwiYWNjb3VudE51bWJlciI6IioiLCJpc3MiOiJndyIsInNlcnZpY2VDb2RlIjoic2VydmljZUNvZGUiLCJpZGVudGlmaWNhdGlvbk51bWJlciI6IioiLCJjdXN0b21lclJlZmVyZW5jZU51bWJlciI6IioiLCJhbGxvd2VkVG9rZW5pemF0aW9uU2NvcGUiOiJnbG9iYWwgc2NvcGUyIiwiY2lmTnVtYmVyIjoiMDAwMDAwODg4ODg4ODgiLCJleHAiOjYxNTg4ODUzNTUwLCJzdWIiOiJsNzIwZGIyMDFmMmI5OTQ5Mjc4Mzk0ODA0MzJkMWNkNDhhIn0.AjxVp5AwVlH4DZCPgV8Y2Ttev6DgzO3ofGFUo_Exeu3E2QF2pWwkGod96x9CV2p5qfx4dsyaTVeWqoPS174yXCb0APzx-Sin8OAooyKTqv2ByQkcRN8FFfEy74yl0pTNGS86UJLhH3c99aY0nSrjaU86f7EsAjIWCw29vleYBT_Z2QUkctmH7ZMnrnVezSkt3Y0XbPHXMv4NTRHOVlosLV-nplrkRuGbvQDOibByN_O21SIWk-TWaPlMcPPdmrvv4OL3ZFH-phKKX4SSD7fzJ7hFbZc3XfajaBBDGCIRvN9hXixqc_sS-C1Gb8KPt3WsiYYEN7Az1zpEMaHNDb_YOA";

        String tranRef = UUID.randomUUID().toString();

        String payload = String.format("""
            {
                "transactionReference": "%s",
                "notificationChannel": "EMAIL",
                "emailChannelInformation": {
                    "emailMainRecipients": [
                        "%s"
                    ],
                    "emailSubject": "%s",
                    "emailBody": "%s"
                },
                "notificationType": "REALTIME"
            }
            """, tranRef, email, subject, body);

        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://bay-umh-uat.apps.api-test.krungsri.net/rest/api/v1/businessSupport/notification/submission"))
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", auth)
                .header("X-Client-Transaction-ID", UUID.randomUUID().toString())
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        log.info("[EmailService] AI is triggering B6 Email Notification");
        log.info("[EmailService] payload is {}", payload);
        log.info("[EmailService] response is {}", response.body());

        return response.body();
    }
}
