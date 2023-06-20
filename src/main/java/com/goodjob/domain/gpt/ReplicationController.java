package com.goodjob.domain.gpt;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReplicationController {

    private final ReplicationService replicationService;

    @PostMapping("/write")
    public void write() {
        replicationService.write();
    }

    @GetMapping("/read")
    public void read() {
        replicationService.read();
    }

}
