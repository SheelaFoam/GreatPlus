package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 12/2/2016.
 */

public class PollsOptionAnswerBE {
    private String id_poll_answer,id_poll_options;
    private ArrayList<PercentageBE> percent;

    public ArrayList<PercentageBE> getPercent() {
        return percent;
    }

    public void setPercent(ArrayList<PercentageBE> percent) {
        this.percent = percent;
    }

    public String getId_poll_answer() {
        return id_poll_answer;
    }

    public void setId_poll_answer(String id_poll_answer) {
        this.id_poll_answer = id_poll_answer;
    }

    public String getId_poll_options() {
        return id_poll_options;
    }

    public void setId_poll_options(String id_poll_options) {
        this.id_poll_options = id_poll_options;
    }
}
