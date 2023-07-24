package com.goodjob.resume.adaptor.outs.persistence;

public interface ProducerAdapter {

    void sendQuestionRequest(String message);

    void sendAdviceRequest(String message);

    void sendError(String message);
}
