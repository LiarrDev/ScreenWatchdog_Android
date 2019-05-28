package com.liarr.screenwatchdog;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class ResumeTime extends LitePalSupport {

    private Date resumeDate;

    public Date getResumeDate() {
        return resumeDate;
    }

    public void setResumeDate(Date resumeDate) {
        this.resumeDate = resumeDate;
    }
}
