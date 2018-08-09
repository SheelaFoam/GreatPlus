package com.erp.sheelafoam.BE;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 12/2/2016.
 */

public class PollsBE {

    private String id_polls,name;
    private ArrayList<PollsOptionBE> polls_option;
    private ArrayList<PollsOptionAnswerBE> polls_option_answer;

    public ArrayList<PollsOptionAnswerBE> getPolls_option_answer() {
        return polls_option_answer;
    }

    public void setPolls_option_answer(ArrayList<PollsOptionAnswerBE> polls_option_answer) {
        this.polls_option_answer = polls_option_answer;
    }

    public String getId_polls() {
        return id_polls;
    }

    public void setId_polls(String id_polls) {
        this.id_polls = id_polls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PollsOptionBE> getPolls_option() {
        return polls_option;
    }

    public void setPolls_option(ArrayList<PollsOptionBE> polls_option) {
        this.polls_option = polls_option;
    }
}
