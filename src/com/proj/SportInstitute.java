package com.proj;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import java.util.Date;

public class SportInstitute {
    String id;
    String Name;
    String startOfConstruction;
    String endOfConstruction;
    String totalMoney;
    public SportInstitute(String id, String name, String start, String end, String money) {
        this.id = id;
        this.Name = name;
        this.startOfConstruction = start;
        this.endOfConstruction = end;
        this.totalMoney = money;
    }
    @Override
    public String toString() {
        return String.format("'%s','%s','%s','%s','%s'",
                id,
                Name,
                startOfConstruction,
                endOfConstruction,
                totalMoney);
    }

}
