package com.goodjob.job.api.jaxb;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@Data
public class Items {
    @XmlElement(name = "C_URL")
    private String url;
    @XmlElement(name = "C_Name")
    private String company;
    @XmlElement(name = "GI_Subject")
    private String subject;
    @XmlElement(name = "GI_Keyword")
    private String sector;
    @XmlElement(name = "GI_W_Date")
    private String createData;
    @XmlElement(name = "GI_E_Date")
    private String deadLine;

    /**
     * <GI_Career>1,2</GI_Career>
     *
     * <GI_Career_Year_Cnt>2</GI_Career_Year_Cnt>
     * 둘중 하나
     */
    @XmlElement(name = "GI_Career_Year_Cnt")
    private int career;
}
