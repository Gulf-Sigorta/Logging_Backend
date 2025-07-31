package com.example.logging_backend.controller;

import com.example.logging_backend.service.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fcm")
public class FcmController {
    @Autowired
    FcmService fcmService;

    @GetMapping
    public String fcm() {
        fcmService.sendPushNotificationToTopic("log","ŞAMPİYON FENERBAHÇE","CAKDURUK DURMAZ AKAR AKTIĞI YERDE GÜLLER ACAR");
        return "Oldu";
    }
}
