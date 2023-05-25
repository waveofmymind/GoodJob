package com.goodjob.domain.job.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "DataList")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataListTag {
    private Items items;
}
