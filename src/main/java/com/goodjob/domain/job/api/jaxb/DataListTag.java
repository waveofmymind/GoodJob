package com.goodjob.domain.job.api.jaxb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "DataList")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataListTag {
    private List<Items> items;
}
