package com.dhl.assetmanager.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class MailMessage implements Serializable {

    private final String recipient;
    private final String title;
    private final String text;

}
