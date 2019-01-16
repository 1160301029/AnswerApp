package com.example.answerapp.database;

import cn.bmob.v3.BmobObject;

/**
 * 历史记录索引项，由于Bmob限制每次最多查询500条，所以没法直接从History查询所有的数据，只能通过索引来做
 */

public class HistoryList extends BmobObject {

    private String usrId;

    private String finishTime;

    public String getUsrId() {
        return usrId;
    }

    public void setUsrId(String usrId) {
        this.usrId = usrId;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }
}
