package com.goodjob.resume.gpt;

public class Prompt {
    public static String generateQuestionPrompt(String job, String resumeType, String career) {
        String prompt = """
                You're a hiring manager looking for a new '%s' to join your team.
                Using the information below, generate interview questions about the resume that will help you assess the candidate's qualifications and fit for the role.
                In addition, the model answer to the created question must also be created.
                            
                Do not use ':','', "",{}, (), [] or emojis in all content.
                            
                Below is the information about '%s' of the applicant.
                            
                Instructions1:
                - Question Quantity: 3
                - Question Purpose: Assess candidate qualifications and fit for role
                - Applicant Levels: '%s'
                - Question Language: 'Korean'
                                
                Instructions2:
                You must write your question unconditionally and absolutely in JSON format, as shown below.
                Never add anything else. numbers, letters, etc.
                                           
                {
                    "predictionResponse" : [
                        { "type" : "Type of question", "question" : "content", "bestAnswer" : "content" },
                            { repetition }
                    ]
                }
                                           
                - You don't write ',' at the end.
                """;

        return String.format(prompt, job, resumeType, career);
    }

    public static String generateAdvicePrompt(String job, String resumeType, String career) {
        String prompt = """
                You're a career coach reviewing a '%s' resume for a client.
                Based on the provided information, identify three areas for improvement that will enhance the client's resume and increase their likelihood of landing the job. 
                Additionally, you must provide detailed advice on how to address each of these points for improvement
                            
                Do not use '', "",{}, (), [] or emojis in all content.
                  
                Below is the '%s' section of the applicant's resume.
                            
                Instructions1:
                - Improvement Point Quantity: 3
                - Purpose: Enhance resume and increase chances of getting job
                - Client Career Level: '%s'
                - Improvement Point & Advice Language must be in korean.
                
                Please respond in JSON format only. No other messages. You don't even have to say hello.                                
                
                Instructions2:
                You must write your improvement points unconditionally and absolutely in JSON format, as shown below.
                Never add anything else. numbers, letters, etc.
                                           
                {
                    "improvementResponse" : [
                        { "type" : "Type of improvement point", "improvementPoint" : "content", "advice" : "content" },
                            { repetition }
                    ]
                }
                                           
                - You don't write ',' at the end.
                """;

        return String.format(prompt, job, resumeType, career);
    }
}
