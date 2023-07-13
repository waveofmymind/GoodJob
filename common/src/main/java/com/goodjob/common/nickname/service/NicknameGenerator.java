package com.goodjob.common.nickname.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

@Service
public class NicknameGenerator {

    public String getRandomNickname() {
        String[] determiners = readWordFromFile("determiners.txt");
        String[] words = readWordFromFile("words.txt");

        Random random = new Random();
        String determiner = determiners[random.nextInt(determiners.length)];
        String word = words[random.nextInt(words.length)];

        return determiner+word;
    }

    public String[] readWordFromFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;

            if ((line = br.readLine()) != null) {
                return line.split(", ");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
