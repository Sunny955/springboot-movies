package com.project.movies.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.movies.auth.services.MailService;
import com.project.movies.dto.MailRequestDto;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMailHandler(@RequestBody MailRequestDto mailDto) {
        mailService.sendEmail(mailDto.getTo(), mailDto.getSubject(), mailDto.getBody());
        return new ResponseEntity<>("Mail sent successfully", HttpStatus.OK);
    }

}
