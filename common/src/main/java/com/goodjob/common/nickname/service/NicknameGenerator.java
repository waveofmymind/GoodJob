package com.goodjob.common.nickname.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Random;

@Component
public class NicknameGenerator {

    public String getRandomNickname() {
        String[] determiners = readWordFromFile("static/determiners.txt");
        String[] words = readWordFromFile("static/words.txt");

        Random random = new Random();
        String determiner = determiners[random.nextInt(determiners.length)];
        String word = words[random.nextInt(words.length)];

        return determiner+word;
    }

    public String[] readWordFromFile(String fileName) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(classPathResource.getInputStream(), "utf-8"));
            String line;

            if ((line = br.readLine()) != null) {
                return line.split(", ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
